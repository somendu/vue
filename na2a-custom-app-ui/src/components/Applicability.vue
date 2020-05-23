<template>
  <div id="applicability-view">
    <na2a-header title="Application Management" :ids="ids" :type="type"></na2a-header>
    <p id="loading-accesories" class="loading" v-if="loading">Loading {{ ids.length === 1 ? 'accessory' : 'accessories' }}</p>
    <div id="loading-accesories-failed" class="alert alert-danger" role="alert" v-if="error">Could not load item(s): {{ error }}</div>

    <div class="applicability-wrapper" v-if="item">
      <item-summary :details="item.details"></item-summary>

      <!-- The challenging ones -->
      <div id="applicability-editor-div" class="applicability-editor-container">
        <div class="applicability-collapse simple-collapse pt-3 mb-3 border-top"
             :class="{'collapsed':!showEntryEditor}">
          <h4>
            <collapse-link
              id="entry-editor-collapse-link"
              v-model="showEntryEditor"
              label="Create/Edit applicability">
            </collapse-link>
          </h4>
            <entry-editor
              class="entry-editor"
              v-model="currentEntry"
              :updating="updating"
              :creating="creating"
              :update-allowed="updateAllowed"
              @clear="clearCurrentEntry"
              @update="updateCurrentEntry"
              @create="createCurrentEntry"
              @options="setOptions">
            </entry-editor>
       </div>
      </div>

      <div id="applicability-summary-div" class="applicability-summary-container">
        <div class="applicability-summary-collapse simple-collapse pt-3-mb-4 border-top"
          :class="{'collapsed':!showEntries}">
          <h4>
            <collapse-link
              id="application-entries-collapse-link"
              v-model="showEntries"
              label="All applicabilities for accessory(s)">
            </collapse-link>
          </h4>
          <div class="applicability-entries">
            <applicability-entries
              :entries="item.entries"
              :items="ids.length"
              :current="currentEntryIdx"
              @edit="editEntry"
              @remove="removeEntry"
              :showStatus="true"
            ></applicability-entries>
          </div>
        </div>
      </div>
    </div>

    <b-modal v-model="removeModal.show" title="Confirmation - Cancellation" @ok="doRemove">
        <div class="modal-body">
          <p>Are you sure to cancel this applicability entry? Please provide a reason.</p>

          <select
            class="form-control mr-4"
            id="a-removeModalReason"
            v-model="removeModal.reason"
            required
          >
            <option :value="null" id="a-removeModelReason-select">- select -</option>
            <option
              v-for="(reason, idx) of cancelledReasons"
              :value="reason.key"
              :key="idx"
              :id="'a-removeModelReason-' + idx"
            >{{ reason.value }}</option>
          </select>
        </div>
      </b-modal>
  </div>
</template>

<script>
import Na2aHeader from './Header/Na2aHeader';
import axios from 'axios';
import eventbus from '@/eventbus';
import CollapseLink from './CollapseLink';
import ApplicabilityEntries from './Applicability/ApplicabilityEntries';
import EntryEditor from './Applicability/EntryEditor';
import ItemSummary from './Applicability/ItemSummary';
import { findApplicationsForSameSpec, hasAnotherEmptyApplication } from '@/lib/utils';

const EMPTY_ENTRY = {
  model: {
    all: false,
    brand: null,
    model: null,
    project: null
  },
  vehicle: [],
  equipment: [],
  exteriorColours: [],
  interiorColours: [],
  trimColours: [],
  countries: []
};

function clone (obj) {
  return JSON.parse(JSON.stringify(obj));
}

export default {
  components: {
    Na2aHeader,
    CollapseLink,
    ApplicabilityEntries,
    EntryEditor,
    ItemSummary
  },

  props: {
    ids: {
      type: Array,
      required: true
    },
    url: {
      type: String,
      required: true
    },
    type: String
  },

  data () {
    return {
      loading: false,

      updating: false,
      creating: false,
      updateAllowed: false,

      item: null,
      error: null,
      showEntryEditor: true,
      showEntries: true,
      currentEntry: clone(EMPTY_ENTRY),
      currentEntryIdx: -1,
      removeModal: {
        show: false,
        idx: -1,
        loading: false,
        reasons: []
      },
      optionLenghts: {}
    };
  },

  computed: {
    cancelledReasons () {
      return this.$store.state.Lookups.cancelledReasons;
    },

    productIds () {
      const productIds = [];
      this.item.details.forEach(detail => {
        const index = productIds.findIndex(id => id === detail.id);
        if (index === -1) {
          productIds.push(detail.id);
        }
      });
      return productIds;
    }
  },

  created () {
    this.load();
    this.$store.dispatch('loadCancelledReasons');
    this.$store.dispatch('loadCountries');
  },

  watch: {
    ids () {
      this.load();
    }
  },

  methods: {
    load () {
      if (this.ids && this.ids.length) {
        this.loading = true;
        this.error = null;
        this.currentEntryIdx = -1;

        axios
          .get(`${this.url}${this.ids.join(',')}`)
          .then(res => {
            this.loading = false;

            res.data.entries.forEach((entry, idx) => {
              entry.idx = idx;
            });

            this.$set(this, 'item', res.data);

            this.$set(this, 'currentEntry', clone(EMPTY_ENTRY));
            if (this.type === 'variant' && this.ids.length >= 1) {
              const idx = this.item.entries.findIndex(entry => entry.variantIds[this.item.details[0].id] === this.ids[0]);
              if (idx !== -1) this.editEntry(idx);
            }
          })
          .catch(err => {
            this.loading = false;
            eventbus.$emit('showMessage', {
              title: "Couldn't load products!",
              err
            });
          });
      }
    },

    editEntry (idx) {
      // TODO ask for confirmation if the current one wasn't saved?
      this.currentEntryIdx = idx;
      const entry = this.item.entries.find(e => e.idx === idx);
      this.$set(this, 'currentEntry', clone(entry));
      if (entry) {
        this.updateAllowed = true;
      } else {
        this.updateAllowed = false;
      }
    },

    removeEntry (idx) {
      this.removeModal.idx = idx;
      this.removeModal.reason = null;
      this.removeModal.show = true;

      if (!this.removeModal.length && !this.removeModal.loading) {
        this.removeModal.loading = true;

        axios
          .get('/api/cancelled-reasons')
          .then(res => {
            this.removeModal.loading = false;
            this.$set(this.removeModal, 'reasons', res.data);
          })
          .catch(err => {
            this.removeModal.loading = false;
            eventbus.$emit('showMessage', {
              title: "Couldn't load reasons for cancelling",
              err
            });
          });
      }
    },

    doRemove (e) {
      if (!this.removeModal.reason) {
        // TODO highlight reason field
        e.preventDefault();
        return;
      }

      // cancel
      const entry = this.item.entries.find(e => e.idx === this.removeModal.idx);
      entry.cancelled = true;
      entry.cancelledReason = this.removeModal.reason;

      // save
      axios
        .post('/api/products', entry)
        .then(() => {
          console.log('entry updated');
          // this.currentEntry = clone(EMPTY_ENTRY);
        })
        .catch(err => {
          eventbus.$emit('showMessage', {
            title: "Couldn't cancel entry!",
            err
          });
        });
    },

    clearCurrentEntry () {
      // retain current entry index
      const value = clone(EMPTY_ENTRY);
      // clear the selection
      this.currentEntryIdx = -1;
      // (but retain variantIds)
      value.variantIds = this.currentEntry.variantIds;
      this.$set(this, 'currentEntry', value);
      // allow save if you were editing an existing and we're not doing a mass update
      this.updateAllowed = this.currentEntryIdx !== -1 && this.ids.length === 1;
    },

    updateCurrentEntry (isNewEntry) {
      let idx = isNewEntry ? -1 : this.currentEntryIdx;
      const progressProp = isNewEntry ? 'creating' : 'updating';

      // TODO: add logic to prevent saving duplicate applications
      // making a duplicate? - Needs to be replaced
      if (this.isDuplicate(idx, this.currentEntry, this.item.entries, this.optionLenghts)) {
        eventbus.$emit('showMessage', {
          title: 'Duplicate entry',
          description:
          'There already is an entry with the same configuration.'
        });
        return;
      }

      // Get Item details = item.details[x].id = product ID
      // const productIds = [];
      // this.item.details.forEach(detail => {
      //   const index = productIds.findIndex(id => id === detail.id);
      //   if (index === -1) {
      //     productIds.push(detail.id);
      //   }
      // });

      if (idx === -1) {
        this.currentEntryIdx = -1;
        this.currentEntry.variantIds = {};

        // for (let id of this.ids) {/
        for (let id of this.productIds) {
          this.currentEntry.variantIds[id] = '-1';
        }
      }

      this[progressProp] = true;

      axios
        .post('/api/products', this.currentEntry)
        .then(res => {
          if (idx !== -1) {
            // in case of saving an existing ..
            res.data.idx = idx;
            const index = this.item.entries.findIndex(e => e.idx === idx);
            this.$set(this.item.entries, index, res.data);
          } else {
            // in case of a new one ..
            res.data.idx = this.item.entries.length;
            this.item.entries.unshift(res.data);
          }

          // edit this one, if a new one is still selected
          if (this.currentEntryIdx === idx) {
            this.editEntry(res.data.idx);
          }
          this[progressProp] = false;
        })
        .catch(err => {
          this[progressProp] = false;
          eventbus.$emit('showMessage', {
            title: "Couldn't save entry",
            err
          });
        });
    },

    createCurrentEntry () {
      this.updateCurrentEntry(true);
    },
    setOptions (options) {
      this.optionLenghts = options;
    },

    hasSameVehicleSpecs (currentEntry, otherEntry) {
      // if (currentEntry.length !== otherEntry.length) {
      //   return false;
      // }

      let hasSameVehicleSpecs = true;
      currentEntry.forEach(current => {
        let idx = otherEntry.findIndex(other => current.key === other.key);

        if (idx === -1) {
          // is it because parent is empty in other , then still true, otherwise false
          let categoryIdx = otherEntry.findIndex(other => other.category === current.category);
          if (categoryIdx !== -1) {
            hasSameVehicleSpecs = false;
          }
        }
      });
      return hasSameVehicleSpecs;
    },

    isDuplicate (idx, currentEntry, entries) {
      const filteredEntries = findApplicationsForSameSpec(idx, currentEntry, entries);
      let uniqueness = {
        vehicle: true,
        equipment: true,
        interiorColours: true,
        exteriorColours: true,
        trimColours: true,
        countries: true
      };
      if (filteredEntries && Array.isArray(filteredEntries) && filteredEntries.length === 0) return false;

      if (hasAnotherEmptyApplication(filteredEntries)) {
        return true;
      }

      for (const existingApplication of filteredEntries) {
        if (this.otherEntryHasOption(currentEntry.vehicle, existingApplication.vehicle)) {
          uniqueness.vehicle = false;
        }
      }

      for (const existingApplication of filteredEntries) {
        if (this.hasSameVehicleSpecs(currentEntry.vehicle, existingApplication.vehicle)) {
          if (this.otherEntryHasOption(currentEntry.equipment, existingApplication.equipment)) {
            uniqueness.equipment = false;
          }
        }
      }

      for (const existingApplication of filteredEntries) {
        if (this.otherColoursAndCountriesHasOptions(currentEntry.exteriorColours, existingApplication.exteriorColours, 'key')) {
          uniqueness.exteriorColours = false;
        }
      }

      for (const existingApplication of filteredEntries) {
        if (this.otherColoursAndCountriesHasOptions(currentEntry.interiorColours, existingApplication.interiorColours, 'key')) {
          uniqueness.interiorColours = false;
        }
      }

      for (const existingApplication of filteredEntries) {
        if (this.otherColoursAndCountriesHasOptions(currentEntry.trimColours, existingApplication.trimColours, 'key')) {
          uniqueness.trimColours = false;
        }
      }

      for (const existingApplication of filteredEntries) {
        if (this.otherColoursAndCountriesHasOptions(currentEntry.countries, existingApplication.countries, 'key')) {
          uniqueness.countries = false;
        }
      }

      return (!uniqueness.vehicle && !uniqueness.equipment) && !uniqueness.exteriorColours && !uniqueness.interiorColours && !uniqueness.trimColours && !uniqueness.countries;
    },

    otherEntryHasOption (currentEntry, otherEntry, isSameVehicle) {
      let uniqueCurrentIds = currentEntry.map(entry => entry.key);
      let uniqueOtherIds = otherEntry.map(entry => entry.key);
      let hasImpactingDuplicates = true;

      // get unique ideas
      let uniqueIds = this.getUniqueValuesOfArrays(uniqueCurrentIds, uniqueOtherIds);

      if (uniqueIds.length === 0) {
        return true;
      }

      // get parents
      let parents = new Set();
      parents = this.getParents(currentEntry, parents);
      parents = this.getParents(otherEntry, parents);

      let duplicatesInParent = {};
      parents.forEach(parent => { duplicatesInParent[parent] = true; });

      // check if parent of other application is empty or not
      uniqueIds.forEach(id => {
        let currentFound = currentEntry.find(current => current.key === id);
        let otherFound = otherEntry.find(other => other.key === id);

        if (currentFound) {
          if (!this.hasEmptyParent(currentFound, otherEntry)) {
            if (!this.hasEqualChildren(currentFound, otherEntry, currentEntry)) {
              duplicatesInParent[currentFound.category] = false;
            }
          }
        }

        if (otherFound) {
          if (!this.hasEmptyParent(otherFound, currentEntry)) {
            if (!this.hasEqualChildren(otherFound, currentEntry, otherEntry)) {
              duplicatesInParent[otherFound.category] = false;
            }
          }
        }
      });

      Object.keys(duplicatesInParent).forEach(key => {
        if (duplicatesInParent[key] === false) {
          hasImpactingDuplicates = false;
        }
      });

      return hasImpactingDuplicates;
    },

    hasEmptyParent (foundEntry, entries) {
      let parent = foundEntry.category;
      let idx = entries.findIndex(entry => entry.category === parent);
      return idx === -1;
    },

    getParents (entry, parents) {
      entry.forEach(element => parents.add(element.category));
      return parents;
    },

    hasEqualChildren (foundEntry, entries, comparableEntries) {
      let hasEqualChildren = false;

      let ids = entries.map(entry => {
        if (entry.category === foundEntry.category) {
          return entry.key;
        }
      });

      ids.forEach(id => {
        let idx = comparableEntries.findIndex(comparableEntry => comparableEntry.key === id);
        if (idx !== -1) {
          hasEqualChildren = true;
        }
      });

      return hasEqualChildren;
    },

    getUniqueValuesOfArrays (uniqueCurrentIds, uniqueOtherIds) {
      let uniqueIds = [];

      uniqueCurrentIds.forEach(id => {
        let idx = uniqueOtherIds.findIndex(otherId => otherId === id);
        if (idx === -1) {
          uniqueIds.push(id);
        }
      });

      uniqueOtherIds.forEach(id => {
        let idx = uniqueCurrentIds.findIndex(currentId => currentId === id);
        if (idx === -1) {
          uniqueIds.push(id);
        }
      });

      return uniqueIds;
    },

    otherColoursAndCountriesHasOptions (currentEntry, otherEntry) {
      let parents = this.colourAndCountryParents(currentEntry);

      if (currentEntry.length === 0 || otherEntry.length === 0) {
        return true;
      }

      for (const currentSelection of currentEntry) {
        for (const otherSelection of otherEntry) {
          if (otherSelection.key === currentSelection.key) {
            for (const parent of parents) {
              if (parent.key === otherSelection.key) {
                parent.duplicateChildren++;
                break;
              }
            }
            break;
          }
        }
      }

      let hasDuplicates = false;

      for (const parent of parents) {
        if (parent.duplicateChildren > 0) {
          hasDuplicates = true;
        }
      }
      return hasDuplicates;
    },

    colourAndCountryParents (currentEntry) {
      let parents = [];
      for (const currentSelection of currentEntry) {
        let isParentFound = false;
        for (const parent of parents) {
          if (parent.key === currentSelection.key) {
            isParentFound = true;
            parent.occurrence++;
          }
        }
        if (!isParentFound) {
          parents.push({ key: currentSelection.key, occurrence: 1, duplicateChildren: 0 });
        }
      }

      return parents;
    }

  }
};
</script>

<style scoped>
.applicability-wrapper {
  height: 90vh;
  display: flex;
  flex-direction: column;
  margin-left: 5px;
}

.applicability-editor-container {
    min-height: 5%;
    flex: 1 -1 auto;
    max-height: 60%;
    margin-bottom: 40px;
}

.applicability-summary-container {
    min-height: 5%;
    flex: 1 -1 auto;
    max-height: 40%;
}

.applicability-collapse {
  height: 100%;
}

.applicability-summary-collapse {
  height: 95%;
}

.applicability-entries {
  height: inherit;
  overflow-y: auto;
}

.simple-collapse.collapsed {
  flex: initial;
}
.simple-collapse.collapsed > div {
  display: none;
}

.entry-editor {
  /* padding-bottom: 10px; */
  height: 100%;
  /* overflow: hidden; */
}
</style>
