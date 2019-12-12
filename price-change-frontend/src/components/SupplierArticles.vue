<template>
  <div>
    <search-form class="mt-2"
                 @submit="searchFormSubmit" ref="searchForm"
                 :historic="history || financereport"
                 :showStartDate="history || financereport"
                 :showStatus="history"></search-form>

    <p v-if="loading">
      Loading ...
      <b-progress v-if="resultSet &amp;&amp; !pagingEnabled"
                  :value="resultSet.rows.length" :max="resultSet.count" show-progress animated></b-progress>
    </p>

    <div v-if="resultSet" class="mt-2">
      <div class="d-flex">
        <template v-if="pagingEnabled">
          <b-pagination size="sm" v-model="page" v-if="resultSet.count > paging.limit" class="mr-4"
                        :total-rows="resultSet.count"
                        :per-page="paging.limit"
                        @change="pageChanged"></b-pagination>

          <div class="mr-4 pt-2" v-if="resultSet.count">
            Showing {{ resultSet.start + 1 }} - {{ resultSet.end }} of {{ resultSet.count }} entries
          </div>
        </template>

        <div class="mr-4 pt-2" v-if="!pagingEnabled &amp;&amp; resultSet.count">
          Showing {{ resultSet.rows.length }} entries
        </div>

        <div class="form-group form-check mb-0 pt-2 mr-4" v-if="!financereport">
          <input type="checkbox" class="form-check-input" id="allColumns" v-model="showAllColumns">
          <label class="form-check-label" for="allColumns">Show all columns</label>
        </div>

        <b-button @click="togglePagingMode" variant="outline-secondary">{{ pagingEnabled ? 'Show all pages' : 'Show one page' }}</b-button>

        <template v-if="!history &amp;&amp; !financereport">
          <b-button-group size="sm">
            <b-button @click="selectPage" variant="outline-secondary">Select page</b-button>
            <b-button @click="selectAll" variant="outline-secondary">Select all</b-button>
            <b-button @click="selectNone" variant="outline-secondary">Select none</b-button>
          </b-button-group>

          <template v-if="selectedItems">
            <div class="pt-2 ml-2">
              {{ selectedItems }}
              {{ selectedItems === 1 ? 'item' : 'items' }}
              selected
            </div>

            <b-button-group size="sm" class="ml-4">
              <b-button @click="confirmStatusChange('APPROVED')" variant="outline-secondary">Approve selected</b-button>
              <b-button @click="confirmStatusChange('REJECTED')" variant="outline-secondary">Reject selected</b-button>
            </b-button-group>

            <b-button-group size="sm" class="ml-4">
              <b-button @click="changeAllRemarks" variant="outline-secondary">Change all remarks</b-button>
            </b-button-group>
          </template>
        </template>
      </div>

      <supplier-article-rows v-if="!financereport" class="mt-2"
                             :sort="sort" @sortChanged="sortChanged" @selectionChanged="selectionChanged"
                             :rows="resultSet.rows"
                             :allColumns="showAllColumns"
                             :showStatusColumn="history"
                             @remarksUpdated="remarksUpdated"></supplier-article-rows>

      <supplier-article-rows-finance v-else-if="financereport" class="mt-2"
                             :sort="sort" @sortChanged="sortChanged" @selectionChanged="selectionChanged"
                             :rows="resultSet.rows" :financeColumns="showFinanceColumns"></supplier-article-rows-finance>
     </div>

     <b-modal v-model="confirmation.show" size="sm" :title="confirmation.state === 'APPROVED' ? 'Approve price changes' : 'Reject price changes'" @ok="changeStatus">
      <p>
        Are you sure to
        {{ confirmation.state === 'APPROVED' ? 'approve' : 'reject' }}
        {{ selectedItems === 1 ? 'this row' : `these ${selectedItems} rows` }}?
      </p>
     </b-modal>

     <b-modal v-model="remarks.show" size="sm" title="Change remarks">
      <p>
        Enter new remarks and click <strong>Ok</strong> to apply to all selected rows.
      </p>

      <b-form @submit="updateAllRemarks">
        <b-form-group>
          <b-form-input type="text" v-model="remarks.value" :disabled="remarks.clear" ref="massUpdateRemark"></b-form-input>
        </b-form-group>

        <b-form-group class="mb-0">
          <b-form-checkbox v-model="remarks.clear">Clear value</b-form-checkbox>
        </b-form-group>
      </b-form>

      <div slot="modal-footer">
         <b-btn variant="secondary" class="mr-2" @click="remarks.show = false">Cancel</b-btn>
         <b-btn variant="primary" :disabled="cantUpdateAllRemarks" @click="updateAllRemarks">OK</b-btn>
       </div>
     </b-modal>
  </div>
</template>

<script>
import SearchForm from './SupplierArticles/SearchForm';
import SupplierArticleRows from './SupplierArticles/SupplierArticleRows';
import SupplierArticleRowsFinance from './SupplierArticles/SupplierArticleRowsFinance';
import router from '@/router';
import { EventBus } from '../lib/eventbus';

const ONE_PAGE_SIZE = 100;
const ALL_PAGES_CHUNK_SIZE = 500;

let loadRequestIdx = 0;

export default {
  name: 'SupplierArticles',

  props: [
    'history', 'financereport'
  ],

  data () {
    return {
      filter: null,
      sort: {
        column: null,
        descending: false
      },
      paging: {
        offset: 0,
        limit: ONE_PAGE_SIZE
      },
      page: 1,

      pagingEnabled: true,

      loading: false,
      resultSet: null,

      showAllColumns: false,
      showFinanceColumns: false,
      selectedItems: 0,
      allSelected: false,

      confirmation: {
        show: false,
        state: false
      },

      remarks: {
        show: false,
        value: null,
        clear: false
      }
    };
  },

  components: {
    SearchForm,
    SupplierArticleRows,
    SupplierArticleRowsFinance
  },

  created () {
    this.deriveParams();
  },

  methods: {
    deriveParams () {
      const { offset, allPages, sort, ...query } = router.currentRoute.query;

      // if no offset nor allPages is set, this would be the initial view
      if (offset == null && allPages == null) {
        // clear resultSet
        this.$set(this, 'resultSet', null);
        setTimeout(() => {
          this.$refs.searchForm.set({});
        }, 0);
        return;
      }

      if (sort) {
        if (sort.startsWith('-')) {
          this.sort.column = sort.substring(1);
          this.sort.descending = true;
        } else {
          this.sort.column = sort;
          this.sort.descending = false;
        }
      } else {
        this.sort.column = null;
        this.sort.descending = false;
      }

      this.paging.offset = parseInt(offset);
      this.pagingEnabled = allPages !== 'true';

      if (isNaN(this.paging.offset)) this.paging.offset = 0;
      this.page = this.paging.offset / this.paging.limit + 1;

      this.$set(this, 'filter', query);

      // delay, this component is created earlier than the search form
      setTimeout(() => {
        this.$refs.searchForm.set(query);
        this.load();
      }, 0);
    },

    searchFormSubmit (values) {
      const query = {
        ...values,
        offset: 0
      };
      if (this.sort.column) {
        query.sort = this.sort.descending ? '-' + this.sort.column : this.sort.column;
      }
      router.push({ path: router.currentRoute.path, query });
    },

    sortChanged (specifier) {
      let query = Object.assign({}, router.currentRoute.query);
      if (specifier.column) {
        query.sort = specifier.descending ? '-' + specifier.column : specifier.column;
      } else {
        delete query.sort;
      }
      router.push({ path: router.currentRoute.path, query });
    },

    selectionChanged () {
      this.allSelected = false;
      this.updateSelectedItems();
    },

    updateSelectedItems () {
      let count = 0;

      if (this.resultSet && Array.isArray(this.resultSet.rows)) {
        if (this.allSelected) {
          count = this.resultSet.count;
        } else {
          for (let row of this.resultSet.rows) {
            if (row.selected) count++;
          }
        }
      }

      this.selectedItems = count;
    },

    pageChanged () {
      // change event is actually emitted before the model has changed
      setTimeout(() => {
        let query = Object.assign({}, router.currentRoute.query);
        query.offset = (this.page - 1) * this.paging.limit;
        router.push({ path: router.currentRoute.path, query });
      }, 0);
    },

    load () {
      if (this.financereport) {
        this.showFinanceColumns = true;
      }

      if (this.filter) {
        let url;

        if (this.history) {
          url = 'api/supplier-articles-history';
        } else if (this.financereport) {
          url = 'api/finance-report';
        } else {
          url = 'api/supplier-articles';
        }

        if (this.pagingEnabled) {
          this.loadOnePage(url, ++loadRequestIdx);
        } else {
          this.loadAllPages(url, ++loadRequestIdx);
        }
      }
    },

    loadOnePage (url) {
      this.loading = true;

      fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          filter: this.filter,
          sort: this.sort,
          paging: this.paging
        })
      })
        .then(this.fetchResponseHandler)
        .then(resultSet => {
          // TODO restore selection state
          if (resultSet && resultSet.rows) {
            for (const row of resultSet.rows) {
              row.selected = false;
              row.saving = false;
            }
          }
          this.$set(this, 'resultSet', resultSet);
          this.loading = false;
          this.allSelected = false;
          this.updateSelectedItems();
        })
        .catch(err => {
          this.loading = false;
          this.$set(this, 'resultSet', null);

          EventBus.$emit('error', {
            title: 'Load error',
            description: 'Could not load supplier articles',
            err
          });
        });
    },

    loadAllPages (url, requestIdx) {
      let offset = 0;

      this.loading = true;
      this.$set(this, 'resultSet', null);

      const getNextChunk = () => {
        fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            filter: this.filter,
            sort: this.sort,
            paging: {
              offset,
              limit: ALL_PAGES_CHUNK_SIZE,
              dontReturnCount: offset !== 0
            }
          })
        })
          .then(this.fetchResponseHandler)
          .then(resultSet => {
            // discard response if the user started another query
            if (requestIdx !== loadRequestIdx) return;

            // TODO restore selection state
            if (resultSet && resultSet.rows) {
              for (const row of resultSet.rows) {
                row.selected = false;
                row.saving = false;
              }
            }

            if (offset === 0) {
              this.$set(this, 'resultSet', resultSet);
            } else {
              for (const row of resultSet.rows) {
                this.resultSet.rows.push(row);
              }
            }

            // need another page?
            if (resultSet.rows.length) {
              offset += ALL_PAGES_CHUNK_SIZE;
              getNextChunk();
            } else {
              this.loading = false;
              this.allSelected = false;
              this.updateSelectedItems();
            }
          })
          .catch(err => {
            this.loading = false;
            this.$set(this, 'resultSet', null);

            EventBus.$emit('error', {
              title: 'Load error',
              description: 'Could not load supplier articles',
              err
            });
          });
      };

      getNextChunk();
    },

    selectPage () {
      this.setPageSelection(true);
      this.allSelected = false;
      this.updateSelectedItems();
    },

    selectNone () {
      this.setPageSelection(false);
      this.allSelected = false;
      this.updateSelectedItems();
    },

    setPageSelection (state) {
      if (this.resultSet && Array.isArray(this.resultSet.rows)) {
        for (let row of this.resultSet.rows) {
          row.selected = state;
        }
      }
    },

    selectAll (state) {
      // just to make them appear as selected
      this.setPageSelection(true);
      this.allSelected = true;

      this.updateSelectedItems();
    },

    confirmStatusChange (state) {
      this.confirmation.state = state;
      this.confirmation.show = true;
    },

    changeStatus () {
      const body = {
        value: this.confirmation.state
      };

      if (!this.allSelected) {
        body.ids = this.getSelectedIds();
      } else {
        body.filter = this.filter;
      }

      this.loading = true;

      fetch(this.allSelected ? 'api/update-all-supplier-articles' : 'api/update-supplier-articles', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
      })
        .then(this.fetchResponseHandler)
        .then(() => {
          this.loading = false;
          this.load();
        })
        .catch(err => {
          this.loading = false;

          EventBus.$emit('error', {
            title: 'Save error',
            description: 'Could not set approval status',
            err
          });
        });
    },

    getSelectedIds () {
      return this.resultSet.rows
        .filter(row => row.selected)
        .map(row => ({
          articleId: row.articleId,
          supplierCode: row.supplierCode,
          countryCode: row.countryCode
        }));
    },

    changeAllRemarks () {
      this.remarks.show = true;
      this.remarks.value = null;
      this.remarks.clear = false;

      setTimeout(() => {
        this.$refs.massUpdateRemark.$el.focus();
      }, 0);
    },

    updateAllRemarks () {
      this.remarks.show = false;

      const body = {
        remarks: (this.remarks.clear ? null : this.remarks.value)
      };

      if (!this.allSelected) {
        body.ids = this.getSelectedIds();
      } else {
        body.filter = this.filter;
      }

      this.loading = true;

      fetch(this.allSelected ? 'api/update-all-remarks' : 'api/update-remarks', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
      })
        .then(this.fetchResponseHandler)
        .then(() => {
          this.loading = false;
          this.load();
        })
        .catch(err => {
          this.loading = false;

          EventBus.$emit('error', {
            title: 'Save error',
            description: 'Could not set approval status',
            err
          });
        });
    },

    remarksUpdated (row) {
      row.saving = true;

      fetch('api/update-remarks', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ids: [{
            articleId: row.articleId,
            supplierCode: row.supplierCode,
            countryCode: row.countryCode
          }],
          remarks: row.remarks
        })
      })
        .then(this.fetchResponseHandler)
        .then(res => {
          row.saving = false;
        })
        .catch(err => {
          row.saving = false;

          EventBus.$emit('error', {
            title: 'Save error',
            description: `Could not update remarks of ${row.articleId} / ${row.supplierCode}`,
            err
          });
        });
    },

    fetchResponseHandler (res) {
      if (!res.ok) {
        return res.text()
          .then(text => {
            const error = new Error(`HTTP Status ${res.status}`);
            try {
              // try to mixin the response into the error, e.g. adding the 'errors' property to the thrown exception
              const json = JSON.parse(text);
              Object.assign(error, json);
            } catch (ex) {
              // ignore
              error.responseText = text;
            }
            throw error;
          });
      }
      return res.json();
    },

    togglePagingMode () {
      const query = { ...router.currentRoute.query };

      if (this.pagingEnabled) {
        delete query.offset;
        query.allPages = 'true';
      } else {
        delete query.allPages;
        query.offset = 0;
      }

      router.push({ path: router.currentRoute.path, query });
    }
  },

  computed: {
    cantUpdateAllRemarks () {
      if (this.remarks.clear) return false;
      if (this.remarks.value == null || this.remarks.value === '') return true;
      return false;
    }
  },

  watch: {
    '$route' (to, from) {
      this.deriveParams();
    }
  }
};
</script>
