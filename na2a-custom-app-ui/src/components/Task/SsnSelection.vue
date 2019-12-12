<template>
    <div id="task-ssn-table-view" class="pt-3 mb-3">
        <form class="form-inline">
            <select
                id="ssn-brand"
                class="form-control mr-4"
                v-model="brand"
                autocomplete="on"
                placeholder="brand"
                :disabled="selectionDisabled === true">
              <option :value="null" disabled selected>Select Brand</option>
              <option v-for="(o, idx) in brands.entries" :value="o.key" :key="idx" :id="'ms-brand-' + idx">{{ o.value }}</option>
            </select>

            <select
                id="ssn-model"
                class="form-control mr-4"
                v-model="model"
                autocomplete="on"
                :disabled="brand === null || selectionDisabled === true">
              <option id="ssn-model-select" :value="null">Select Model</option>
              <option v-for="(o, idx) in models.entries" :value="o" :key="idx" :id="'ms-model-' + idx">{{ o.value }}</option>
            </select>

            <select
                id="ssn-ssn"
                class="form-control mr-4"
                v-model="ssn"
                autocomplete="on"
                :disabled="model === null || ssns.loading === true || selectionDisabled === true">
                <option :value="null" disabled>
                  <div v-if="ssns.loading === true" class="loading">Loading ssns...</div>
                  <div v-if="ssns.loading === false">Select SSN</div>
                </option>
                <option v-for="(o, idx) in ssnFilter" :value="o.value" :key="idx" :id="'ms-ssn-' + idx">{{ o.value }}</option>
            </select>

            <b-button id="task-ssn-selection-button"
              class="my-2 my-sm-0" type="text"
              @click="submitSelection"
              :disabled="ssn === null || loading === true || selectionDisabled === true"
            >Set</b-button>

        <p id="loading-task" class="loading-small" v-if="loading"></p>
        </form>
    </div>
</template>

<script>
import axios from 'axios';

export default {
  props: {
    loading: {
      type: Boolean,
      value: false,
      required: true
    }
  },

  data () {
    return {
      brand: null,
      model: null,
      ssn: null,
      brands: {
        loading: false,
        error: null,
        entries: []
      },
      models: {
        loading: false,
        error: null,
        entries: []
      },
      ssns: {
        loading: false,
        error: null,
        entries: []
      },
      selectionDisabled: false
    };
  },

  created () {
    this.loadBrands();
    this.loadModels();
  },

  methods: {
    loadBrands () {
      this.brands.loading = true;

      axios.get(`/api/brands`)
        .then(res => {
          this.brands.loading = false;
          this.brands.error = null;
          this.$set(this.brands, 'entries', res.data);
        })
        .catch(err => {
          this.brands.loading = false;
          this.brands.error = err.toString();
        });
    },

    loadModels () {
      this.models.loading = true;

      axios.get(`/api/brands/${this.brand}/models`)
        .then(res => {
          this.models.loading = false;
          this.models.error = null;
          this.$set(this.models, 'entries', res.data);
        })
        .catch(err => {
          this.models.loading = false;
          this.models.error = err.toString();
        });
    },

    loadSsn () {
      this.ssns.loading = true;

      axios.get(`/api/task/brands/${this.brand}/models/${this.model.key}`)
        .then(res => {
          this.$set(this.ssns, 'entries', res.data);
          this.ssns.loading = false;
        }).catch(err => {
          this.ssns.loading = false;
          this.ssns.error = err.toString();
        });
    },

    brandUpdated (newValue) {
      this.model = null;
      this.ssn = null;
      this.loadModels();
    },

    modelUpdated (newValue) {
      this.ssn = null;
      this.loadSsn();
    },

    submitSelection () {
      let project = this.getProjects();
      let params = {
        brand: this.brand,
        model: this.model.value,
        project: project
      };
      this.selectionDisabled = true;
      this.$emit('performRequest', params);
    },

    getProjects () {
      let project = this.ssns.entries.find(entry => {
        return entry.children.find(child => child.value === this.ssn);
      });

      return project.value;
    }
  },

  computed: {
    ssnFilter () {
      let ssnsList = [];
      Object.keys(this.ssns.entries).forEach(key => {
        let project = this.ssns.entries[key];
        Object.keys(project).forEach(projectKey => {
          if (projectKey === 'children') {
            project[projectKey].forEach(ssn => ssnsList.push(ssn));
          }
        });
      });
      return ssnsList;
    }
  },

  watch: {
    brand (newValue, oldValue) {
      if (newValue !== oldValue) {
        this.brandUpdated(newValue);
      }
    },
    model (newValue, oldValue) {
      if (newValue !== oldValue) {
        this.modelUpdated(newValue);
      }
    }
  }
};
</script>

<style>
.loading-small {
  width: 25px;
  height: 15px;
}

</style>
