<template>
  <div id="search-content-div">
    <div class="search-basic">
      <div class="form-inline">
        <label class="mr-4" for="lcl-project-selector">Local Project</label>
        <select id="lcl-project-selector" class="form-control form-control-sm" v-model="selectedProject">
          <option :value="''">
            <div v-if="projects.loading === true" class="loading">Loading projects...</div>
            <div v-if="projects.loading === false">Select Project</div>
          </option>
          <option v-for="(o, idx) in projects.entries" :value="o.key" :key="idx" :id="'vs-project-' + idx">{{ o.value }}</option>
        </select>

        <label class="mr-4" for="lcl-model-selector">Local model</label>
        <select id="lcl-model-selector" class="form-control form-control-sm" v-model="selectedModel" :disabled="selectedProject === ''">
          <option :value="''">
            <div v-if="models.loading === true" class="loading">Loading models...</div>
            <div v-if="models.loading === false">Select Model</div>
          </option>
          <option v-for="(o, idx) in models.entries" :value="o.key" :key="idx" :id="'vs-model-' + idx">{{ o.value }}</option>
        </select>
      </div>

      <div class="form-inline">
        <b-form-group label="Direction of work">
          <b-form-radio value="vehicle" v-model="direction" name="direction-radios">Select vehicles for accessories</b-form-radio>
          <b-form-radio value="accessories" v-model="direction" name="direction-radios">Select accessories for vehicles</b-form-radio>
        </b-form-group>
      </div>

      <div class="form-inline">
        <b-button id="lcl-set-button"
                  class="btn btn-sm btn-primary mr-4 mb-2"
                  @click="setAction">Set</b-button>
        <b-button id="lcl-reset-button"
                  class="btn btn-sm btn-primary mr-4 mb-2"
                  @click="resetAction">Reset</b-button>
      </div>
    </div>

    <div class="simple-collapse pt-3 mb-3 border-top"
       :class="{'collapsed': !showAdvanced}">
      <h6>
        <collapse-link
          id="advanced-search-collapse-link"
          v-model="showAdvanced"
          label="Advanced Search">
        </collapse-link>
      </h6>
      <div class="form-inline">
        <div class="form-inline advanced-search">
          <label class="mr-4" for="lcl-tme-body-selector">TME body</label>
          <select id="lcl-tme-body-selector" class="form-control form-control-sm" v-model="advancedSelection.tmeBody" :disabled="!selectedModel || selectedModel === ''">
            <option :value="''">
              <div v-if="tmeBodies.loading === true" class="loading">Loading bodies...</div>
              <div v-if="tmeBodies.loading === false">Select Body</div>
            </option>
            <option v-for="(o, idx) in tmeBodies.entries" :value="o.key" :key="idx" :id="'vs-model-' + idx">{{ o.value }}</option>
          </select>

          <label class="mr-4" for="lcl-tme-engine-selector">TME engine</label>
          <select id="lcl-tme-body-selector" class="form-control form-control-sm" v-model="advancedSelection.tmeEngine" :disabled="!selectedModel || selectedModel === ''">
            <option :value="''">
              <div v-if="tmeEngines.loading === true" class="loading">Loading engines...</div>
              <div v-if="tmeEngines.loading === false">Select Engine</div>
            </option>
            <option v-for="(o, idx) in tmeEngines.entries" :value="o.key" :key="idx" :id="'vs-model-' + idx">{{ o.value }}</option>
          </select>

          <label class="mr-4" for="lcl-tme-engine-selector">TME transmission</label>
          <select id="lcl-tme-transmission-selector" class="form-control form-control-sm" v-model="advancedSelection.tmeTransmission" :disabled="!selectedModel || selectedModel === ''">
            <option :value="''">
              <div v-if="tmeTransmissions.loading === true" class="loading">Loading transmission...</div>
              <div v-if="tmeTransmissions.loading === false">Select Transmission</div>
            </option>
            <option v-for="(o, idx) in tmeTransmissions.entries" :value="o.key" :key="idx" :id="'vs-model-' + idx">{{ o.value }}</option>
          </select>
        </div>

        <div class="separator border-left">&nbsp;</div>

        <div class="form-inline">
          <label class="mr-4" for="lcl-local-body-selector">Local body</label>
          <select id="lcl-local-body-selector" class="form-control form-control-sm" v-model="advancedSelection.localBody" :disabled="!selectedModel || selectedModel === ''">
            <option :value="''">Please select</option>
          </select>

          <label class="mr-4" for="lcl-local-engine-selector">Local engine</label>
          <select id="lcl-local-engine-selector" class="form-control form-control-sm" v-model="advancedSelection.localEngine" :disabled="!selectedModel || selectedModel === ''">
            <option :value="''">Please select</option>
          </select>

          <label class="mr-4" for="lcl-local-engine-selector">Local transmission</label>
          <select id="lcl-local-transmission-selector" class="form-control form-control-sm" v-model="advancedSelection.localTransmission" :disabled="!selectedModel || selectedModel === ''">
            <option :value="''">Please select</option>
          </select>
        </div>

        <div class="separator border-left">&nbsp;</div>

        <div class="form-inline">
          <b-button id="lcl-clear-advanced-search-button"
                    class="btn btn-sm btn-primary mr-4 mb-2"
                    @click="resetAdvancedSearch">Clear search</b-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import CollapseLink from '@/components/CollapseLink';

export default {
  components: {
    CollapseLink
  },

  props: {
    organisation: {
      type: String,
      required: true
    },
    model: {
      type: Object,
      required: true
    },
    permittedSearchOptions: {
      type: Object,
      required: true
    }
  },

  data () {
    return {
      direction: 'vehicle',
      projects: {
        loading: false,
        error: null,
        entries: []
      },
      models: {
        loading: false,
        error: null,
        entries: []
      },
      tmeBodies: {
        loading: false,
        error: null,
        entries: []
      },
      tmeEngines: {
        loading: false,
        error: null,
        entries: []
      },
      tmeTransmissions: {
        loading: false,
        error: null,
        entries: []
      },
      selectedProject: '',
      selectedModel: '',
      showAdvanced: false,
      advancedSelection: {
        tmeBody: '',
        tmeEngine: '',
        tmeTransmission: '',
        localBody: '',
        localEngine: '',
        localTransmission: ''
      }
    };
  },

  created () {
    this.load();
  },

  methods: {
    load () {
      this.loadProjects();
    },

    loadProjects () {
      this.projects.loading = true;

      axios.get(`/api/localisation/${this.organisation}/projects/`)
        .then(res => {
          this.projects.loading = false;
          this.projects.error = null;
          this.$set(this.projects, 'entries', res.data);
        })
        .catch(err => {
          this.projects.loading = false;
          this.projects.error = err.toString();
        });
    },

    loadModels (model) {
      this.models.loading = true;

      // encodedURI needed as '|' is an invalid request char for Tomcat 8 and higher
      axios.get(encodeURI(`/api/localisation/models?projectId=${this.selectedProject}`))
        .then(res => {
          this.models.loading = false;
          this.models.error = null;
          this.$set(this.models, 'entries', res.data);

          if (model) {
            this.selectedModel = model;
          }
        })
        .catch(err => {
          this.models.loading = false;
          this.models.error = err.toString();
        });
    },

    loadTMEBodies (model) {
      this.tmeBodies.loading = true;

      // encodedURI needed as '|' is an invalid request char for Tomcat 8 and higher
      axios.get(encodeURI(`/api/localisation/vehicleOptions?tmeProject=${model.parentKey}&optionType=bodies`))
        .then(res => {
          this.tmeBodies.loading = false;
          this.tmeBodies.error = null;
          this.$set(this.tmeBodies, 'entries', res.data);
        })
        .catch(err => {
          this.tmeBodies.loading = false;
          this.tmeBodies.error = err.toString();
        });
    },

    loadTMEEngines (model) {
      this.tmeEngines.loading = true;

      // encodedURI needed as '|' is an invalid request char for Tomcat 8 and higher
      axios.get(encodeURI(`/api/localisation/vehicleOptions?tmeProject=${model.parentKey}&optionType=engines`))
        .then(res => {
          this.tmeEngines.loading = false;
          this.tmeEngines.error = null;
          this.$set(this.tmeEngines, 'entries', res.data);
        })
        .catch(err => {
          this.tmeEngines.loading = false;
          this.tmeBodies.error = err.toString();
        });
    },

    loadTMETransmissions (model) {
      this.tmeTransmissions.loading = true;

      // encodedURI needed as '|' is an invalid request char for Tomcat 8 and higher
      axios.get(encodeURI(`/api/localisation/vehicleOptions?tmeProject=${model.parentKey}&optionType=transmissions`))
        .then(res => {
          this.tmeTransmissions.loading = false;
          this.tmeTransmissions.error = null;
          this.$set(this.tmeTransmissions, 'entries', res.data);
        })
        .catch(err => {
          this.tmeTransmissions.loading = false;
          this.tmeTransmissions.error = err.toString();
        });
    },

    setAction () {
      // todo  post something????? emit to localisation first
    },

    resetAction () {
      this.selectedProject = '';
      this.selectedModel = '';
      this.advancedSelection.tmeBody = '';
      this.advancedSelection.tmeEngine = '';
      this.advancedSelection.tmeTransmission = '';
      this.advancedSelection.localBody = '';
      this.advancedSelection.localEngine = '';
      this.advancedSelection.localTransmission = '';

      this.emitSearchCriteria();
    },

    emitSearchCriteria () {
      let criteria = {
        project: this.projects.entries.find(entry => entry.key === this.selectedProject),
        model: this.models.entries.find(entry => entry.key === this.selectedModel),
        advanced: this.advancedSelection
      };

      this.$emit('setSearchCriteria', criteria);
    },

    emitAdvancedSearch () {
      let criteria = {
        project: this.projects.entries.find(entry => entry.key === this.selectedProject),
        model: this.models.entries.find(entry => entry.key === this.selectedModel),
        advanced: this.advancedSelection
      };
      this.$emit('setAdvancedSearch', criteria.advanced);
    },

    resetAdvancedSearch () {
      this.advancedSelection.tmeBody = '';
      this.advancedSelection.tmeEngine = '';
      this.advancedSelection.tmeTransmission = '';
      this.advancedSelection.localBody = '';
      this.advancedSelection.localEngine = '';
      this.advancedSelection.localTransmission = '';

      const model = this.models.entries.find(entry => entry.key === this.selectedModel);
      if (model) {
        this.loadTMEBodies(model);
        this.loadTMEEngines(model);
        this.loadTMETransmissions(model);

        this.$emit('resetAdvancedSearch', this.advancedSelection);
      }
    },

    filterPermittedOptions (permittedOptions, searchOptions) {
      if (permittedOptions) {
        // tme bodies/engines/transmissions are key/value pairs
        permittedOptions.forEach(permittedBody => {
          const index = searchOptions.entries.findIndex(body => body.key === permittedBody.key);
          if (index !== -1) {
            searchOptions.entries.splice(index, 1);
          }
        });
      }
    }

  },

  watch: {
    model () {
      if (this.model.model) {
        this.selectedProject = this.model.project.key;
      }
    },

    selectedProject (newValue, oldValue) {
      if (newValue !== oldValue) {
        if (this.model.model) {
          this.loadModels(this.model.model.key);
        } else {
          this.selectedModel = '';
          this.loadModels();
        }
      }
    },

    selectedModel (newValue, oldValue) {
      if (newValue !== oldValue) {
        this.emitSearchCriteria();

        const model = this.models.entries.find(entry => entry.key === this.selectedModel);
        if (model) {
          this.loadTMEBodies(model);
          this.loadTMEEngines(model);
          this.loadTMETransmissions(model);
        }
      }
    },

    advancedSelection (newValue, oldValue) {
      if (newValue !== oldValue) {
        this.emitAdvancedSearch();
      }
    },

    permittedSearchOptions (newValue, oldValue) {
      if (newValue !== oldValue) {
        console.log('Permitted search options', newValue);

        // tme bodies/engines/transmissions are key/value pairs
        this.filterPermittedOptions(newValue.tmeBodies, this.tmeBodies);
        this.filterPermittedOptions(newValue.tmeEngines, this.tmeEngines);
        this.filterPermittedOptions(newValue.tmeTransmissions, this.tmeTransmissions);
      }
    }
  }
};

</script>

<style scoped>
#search-content-div {
    margin-left: 2%;
    display: flex;
    flex-direction: column;
}

.search-basic {
    flex: 1 -1 auto;
    display: flex;
    flex-direction: row
}

.form-inline > select {
    margin-right: 15px;
}

.advanced-search {
  margin-left: 15px;
}

.separator {
  margin-left: 5px;
  margin-right: 5px;
}

.simple-collapse.collapsed {
  flex: initial;
}
.simple-collapse.collapsed > div {
  display: none;
}
.loading-small {
  width: 25px;
  height: 15px;
}
</style>
