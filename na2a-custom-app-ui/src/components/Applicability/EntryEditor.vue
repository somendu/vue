<template>
  <div class="editor-content">
    <div class="editor-selectors pr-2">
      <div>
        <model-selector class="pb-2 border-bottom"
                      v-model="value.model"
                      @input="refreshOptions"
                      @checkchange="checkSummary"
                      @resetchangeflag="resetChangeFlag"
                      @resetdropdownflag="resetDropDownFlag"
                      :brand="brand"
                      :changedFlag="changedFlag"
                      :resetFlag="resetFlag"
                      ></model-selector>
      </div>

      <div class="editor-options pr-2">
        <div class="editor-column pr-2 border-right">
          <h5>Vehicle options</h5>
          <!-- <div> -->
            <tree-editor v-model="value.vehicle"
                         :options="vehicleOptions.options"
                         :loading="vehicleOptions.loading"
                         :validation-enabled="true"></tree-editor>
          <!-- </div> -->
       </div>

        <div class="editor-column pl-2 pr-2">
          <h5>Equipment options</h5>
            <tree-editor v-model="value.equipment"
                         :show-filter="true"
                         :options="equipmentOptions.options"
                         :loading="equipmentOptions.loading"></tree-editor>
        </div>

        <div class="editor-column pl-2 border-left">
          <h5>Vehicle colours</h5>
          <div>
            <collapse-link v-model="exteriorColours.open" label="Exterior"></collapse-link>
            <b-collapse class="mt-2" v-model="exteriorColours.open"   id="ee-exterior_colours">
              <colour-editor v-model="value.exteriorColours"
                             :options="exteriorColours.options"
                             :loading="exteriorColours.loading"
                             :allModels="value.model.all"></colour-editor>
            </b-collapse>

            <collapse-link v-model="interiorColours.open" label="Interior"></collapse-link>
            <b-collapse class="mt-2" v-model="interiorColours.open" id="ee-interior_colours">
              <colour-editor v-model="value.interiorColours"
                             :options="interiorColours.options"
                             :loading="interiorColours.loading"
                             :allModels="value.model.all"></colour-editor>
            </b-collapse>

            <collapse-link v-model="trimColours.open" label="Trim"></collapse-link>
            <b-collapse class="mt-2" v-model="trimColours.open" id="ee-trim_colours">
              <colour-editor v-model="value.trimColours"
                             :options="trimColours.options"
                             :loading="trimColours.loading"
                             :allModels="value.model.all"></colour-editor>
            </b-collapse>
          </div>
        </div>

        <div class="editor-column pl-2 border-left">
          <h5>Countries</h5>
          <country-editor
            v-model="value.countries"
            :show="countries.show"
            :countries="countries.entries"
          ></country-editor>
        </div>
      </div>
    </div>

    <div class="editor-summary editor-column pl-1 border-left">
      <h5>Summary</h5>
      <div class="">
        <entry-summary :value="value"></entry-summary>
      </div>

      <div>
        <b-button-group>
          <b-button id="ee-clearEntry" variant="outline-primary" v-b-modal.ee-clearEntryModal :disabled="!selectionMade">
            <octicon name="x"></octicon> Clear
          </b-button>
          <b-button id="ee-updateEntry" variant="outline-primary" @click="update" :disabled="!selectionMade || updating || creating" v-if="updateAllowed">
            <octicon name="sync" :spin="updating"></octicon> Update
          </b-button>
          <b-button id="ee-saveEntry" variant="outline-primary" @click="create" :disabled="!selectionMade || updating || creating">
            <octicon name="plus" :spin="creating"></octicon> Save as new
          </b-button>
        </b-button-group>

        <b-modal id="ee-clearEntryModal"
                centered
                title="Clear applicability criteria?"
                ok-title="Yes"
                cancel-title="No"
                @ok="clear"
        >
          <p class="my-4">Do you want to clear all currently selected applicability criteria?</p>
        </b-modal>
      </div>
      <div>
        <b-modal id="checksurewarn"
            @close="onCheckSummaryClose"
            :no-close-on-backdrop="true"
            :no-close-on-esc="true"
            :hide-header-close="true"
            ref="refchecksurewarn"
            centered
            title="Confirm Applicability selection?"
            ok-title="Yes"
            cancel-title="Cancel"
            @ok="clearSummary"
            @cancel="resetDropDowns"
            >
            <p class="my-4">WARNING! - Changing any of the selectors will clear the summary details ?</p>
        </b-modal>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import Octicon from 'vue-octicon/components/Octicon';
import CollapseLink from '@/components/CollapseLink';
import EntrySummary from './EntryEditor/EntrySummary';
import TreeEditor from './EntryEditor/TreeEditor';
import ModelSelector from './EntryEditor/ModelSelector';
import ColourEditor from './EntryEditor/ColourEditor';
import CountryEditor from './EntryEditor/CountryEditor';

function updateKatashikiState (state, values) {
  if (values) {
    for (let value of values) {
      if (value.katashiki && value.katashiki.length) {
        state.enabled = true;

        if (!state.categories[value.category]) {
          state.categories[value.category] = [];
        } else {
          state.multiSelectionCategory = value.category;
        }

        state.categories[value.category].push(value.katashiki);
      }
    }
  }
}

export default {
  components: {
    Octicon,
    CollapseLink,
    EntrySummary,
    TreeEditor,
    ModelSelector,
    ColourEditor,
    CountryEditor
  },

  props: {
    value: {
      required: true
    },
    updateAllowed: {
      type: Boolean
    },
    updating: {
      type: Boolean
    },
    creating: {
      type: Boolean
    }
  },

  created () {
    this.refreshOptions();
    this.determineKatashikiState();
  },

  data () {
    return {
      brand: null,
      vehicleOptions: { loading: false, error: null, options: [] },
      equipmentOptions: { loading: false, error: null, options: [] },
      interiorColours: { loading: false, error: null, options: [], open: true },
      exteriorColours: { loading: false, error: null, options: [], open: true },
      trimColours: { loading: false, error: null, options: [], open: true },
      countries: { show: false, error: null, entries: [] },
      katashiki: null,
      loadedOptions: {
        model: null,
        project: null,
        all: false
      },
      showModal: false,
      changedFlag: false,
      resetFlag: false
    };
  },

  methods: {
    refreshOptions () {
      const brand = this.value.model.brand;
      const model = this.value.model.model && this.value.model.model.key;
      const project = this.value.model.project && this.value.model.project.key;
      const all = this.value.model.all;

      // only load options if selection has actually changed
      if (this.brand === brand &&
              this.loadedOptions.model === model &&
              this.loadedOptions.project === project &&
              this.loadedOptions.all === all) { // eslint-disable-line
        return;
      }

      this.brand = brand;
      this.loadedOptions.model = model;
      this.loadedOptions.project = project;
      this.loadedOptions.all = all;

      this.loadOptions('vehicle-options', 'vehicleOptions');
      this.loadOptions('equipment-options', 'equipmentOptions');
      this.loadOptions('interior-colours', 'interiorColours', true);
      this.loadOptions('exterior-colours', 'exteriorColours', true);
      this.loadOptions('trim-colours', 'trimColours', true);

      if ((brand && model && project) || (this.value.model.all)) {
        this.countries.entries = this.$store.state.Enums.countries;
        this.countries.show = true;
      }
    },

    loadOptions (resource, target, supportsAll) {
      const brand = this.value.model.brand;
      const model = this.value.model.model && this.value.model.model.key;
      const project = this.value.model.project && this.value.model.project.key;
      const all = this.value.model.all;

      let loadPromise;

      if (brand && model && project) {
        loadPromise = axios.get(`/api/brands/${brand}/models/${model}/projects/${project}/${resource}`);
      } else if (all && supportsAll) {
        loadPromise = axios.get(`/api/all/${resource}`);
      }

      if (loadPromise) {
        // load
        this[target].loading = true;
        loadPromise
          .then(res => {
            if (this.isModelSelected(brand, model, project, all)) {
              this[target].loading = false;
              this[target].error = null;

              if (resource === 'vehicle-options') {
                res.data = this.setSubProjectsOnTop(res.data);
              }

              this.$set(this[target], 'options', res.data);

              let optionLengths = {
                vehicle: this.vehicleOptions.options.length,
                equipment: this.equipmentOptions.options.length,
                interiorColours: this.interiorColours.options.length,
                exteriorColours: this.exteriorColours.options.length,
                trimColours: this.trimColours.options.length,
                countries: this.countries.entries.length
              };
              this.$emit('options', optionLengths);
            }
          })
          .catch(err => {
            if (this.isModelSelected(brand, model, project, all)) {
              this[target].loading = false;
              this[target].error = err.toString();
              this.$set(this[target], 'options', []);
            }
          });
      } else {
        // clear
        this[target].loading = false;
        this[target].error = null;
        this.$set(this[target], 'options', []);
      }
    },

    isModelSelected (brand, model, project, all) {
      if (all && this.value.model.all) return true;
      if (!this.value.model.brand || this.value.model.brand !== brand) return false;
      if (!this.value.model.model || this.value.model.model.key !== model) return false;
      if (!this.value.model.project || this.value.model.project.key !== project) return false;
      return true;
    },

    clear () {
      this.clearCountries();
      this.$emit('clear');
    },

    clearCountries () {
      this.countries.entries = [];
      this.countries.show = false;
    },

    doNothing () {
      console.log('Clear action has been cancelled');
    },

    checkSummary: function (level) {
      console.log('check Summary: ' + level);
      this.$refs.refchecksurewarn.show();
    },

    clearSummary: function () {
      console.log('change flag ' + this.changedFlag);
      this.clear();
      this.changedFlag = true;
    },

    onCheckSummaryClose () {
      this.showModal = false;
    },

    resetChangeFlag: function () {
      this.changedFlag = false;
      this.$refs.refchecksurewarn.hide();
    },

    resetDropDownFlag: function () {
      this.resetFlag = false;
      this.$refs.refchecksurewarn.hide();
    },

    resetDropDowns: function () {
      this.resetFlag = true;
    },

    update () {
      this.$emit('update');
    },

    create () {
      this.$emit('create');
    },

    determineKatashikiState () {
      // determine katashiki state
      const state = {
        enabled: false,
        multiSelectionCategory: null,
        categories: {}
      };

      if (this.value) {
        updateKatashikiState(state, this.value.vehicle);
        // updateKatashikiState(state, this.value.equipment);
      }

      this.$store.dispatch('setKatashiki', state);

      this.$emit('input', this.value);
    },

    setSubProjectsOnTop (data) {
      // let dataCopy = Object.assing({}, data);
      let idx = data.findIndex(d => d.value === 'Subprojects');

      if (idx === -1) {
        return;
      }
      data.unshift(data[idx]);
      data.splice(idx + 1, 1);
      return data;
    }
  },

  computed: {
    selectionMade () {
      return (this.value &&
        this.value.model &&
        this.value.model.all === true) ||
        (this.value &&
        this.value.model &&
        this.value.model.brand &&
        this.value.model.model &&
        this.value.model.project);
    }
  },

  watch: {
    value: {
      handler (newValue) {
        this.refreshOptions();
        this.determineKatashikiState();
      },
      deep: true
    }
  }
};
</script>

<style scoped>
.editor-content {
  display: flex;
  flex-direction: row;
}

.editor-selectors {
  flex: 0 1 75%;
  display: flex;
  flex-direction: column;
  max-height: inherit;
}

.editor-selectors > div {
  flex: 1 0 auto;
}
.editor-options {
  /* occupy remaining space of editor_selectors */
  flex: 1;
  /* column layout for children */
  display: flex;
  flex-direction: row;
  max-height: 95%;
  overflow: auto;
}
.editor-options > div {
  flex: 1;
  max-height: inherit;
}

.editor-column {
  max-height: 95%;
  overflow-y: auto;
}
</style>
