<template>
  <div id="localisation-view">
    <na2a-header title="Localise Accessories for a Vehicle"></na2a-header>
    <p id="loading-localisation" class="loading" v-if="loading">Loading...</p>
    <div id="loading-accesories-failed" class="alert alert-danger" role="alert" v-if="error">Could not load localisation: {{ error }}</div>

    <div class="localisation-wrapper">
      <div id="localisation-search" class="localisation-section-top">
        <div class="localisation-collapse simple-collapse pt-3 mb-3 border-top"
          :class="{'collapsed':!showSearch}">
          <h4>
            <collapse-link
              id="localisation-search-collapse-link"
              v-model="showSearch"
              :label="searchLabel">
            </collapse-link>
          </h4>
          <div class="component">
            <vehicle-search
              :organisation="organisation"
              :model="selectedModel"
              :permittedSearchOptions="permittedSearchOptions"
              @setSearchCriteria="updateSearchCriteria"
              @setAdvancedSearch="setAdvancedSearch"
              @resetAdvancedSearch="resetAdvancedSearch"></vehicle-search>
          </div>
        </div>
      </div>

      <div id="localisation-selection" class="localisation-section">
        <div class="localisation-collapse simple-collapse pt-3 mb-3 border-top"
          :class="{'collapsed':!showSelection}">
          <h4>
            <collapse-link
              id="localisation-selection-collapse-link"
              v-model="showSelection"
              :label="'Vehicle selection (' + vehicleSelectionCount + ' vehicle(s) selected)'">
            </collapse-link>
          </h4>
          <p id="loading-localisation" class="loading" v-if="loadingVehicles">Loading vehicles...</p>
          <div class="component" v-if="!loadingVehicles">
            <vehicle-selector
              :vehicles="filteredVehicles"
              @updateVehicleCount="updateVehicleCount"
              @updateVehicle="updateVehicle"
            ></vehicle-selector>
          </div>
        </div>
      </div>

      <div id="localisation-accessories" class="localisation-section">
        <div class="localisation-collapse simple-collapse pt-3 mb-3 border-top"
          :class="{'collapsed':!showAccessories}">
          <h4>
            <collapse-link
              id="localisation-selection-collapse-link"
              v-model="showAccessories"
              label="Accessories for the selected Vehicles"
              :enabled="false">
            </collapse-link>
          </h4>
          <p id="loading-localisation" class="loading" v-if="loadingAccessories">Loading accessories...</p>
          <div class="component" v-if="!loadingAccessories">
            <vehicle-accessories-selector
              :vehicleAccessories="vehicleAccessories"
              @updateAccessory="updateAccessory"
            >
            </vehicle-accessories-selector>
          </div>
        </div>
      </div>

      <div id="localisation-services" class="localisation-section-bottom">
        <!-- <div class="localisation-collapse simple-collapse pt-3 mb-3 border-top"
          :class="{'collapsed':!showServices}">
          <h4>
            <collapse-link
              id="localisation-selection-collapse-link"
              v-model="showServices"
              label="Services">
            </collapse-link>
          </h4> -->
          <p id="loading-accessory-table" class="loading" v-if="loadingAccessoryTable">Loading accessory table...</p>
          <div class="component" v-if="!loadingAccessoryTable">
            <accessory-overview-table :accessoryTable="accessoryTable" :data="accessoryOverviewData"></accessory-overview-table>
          </div>
        <!-- </div> -->
      </div>
    </div>
    <div>
      <indetermined-accessory-modal
        :showModal="showIndeterminedAccessoryModal"
        @clearShowModel="showIndeterminedAccessoryModal = false"
        @updateIndeterminedAccessory="updateAccessoryClassification"
        @ignoreIndeterminedAccessory="ignoreIndeterminedAccessory"
      ></indetermined-accessory-modal>
    </div>
  </div>
</template>

<script>
import Na2aHeader from './Header/Na2aHeader';
import axios from 'axios';
import eventbus from '@/eventbus';
import { filterBrand } from '@/lib/utils';
import CollapseLink from './CollapseLink';
import VehicleSearch from '@/components/Localisation/VehicleSearch';
import VehicleSelector from '@/components/Localisation/VehicleSelector';
import VehicleAccessoriesSelector from '@/components/Localisation/VehicleAccessoriesSelector';
import IndeterminedAccessoryModal from '@/components/Localisation/IndeterminedAccessoryModal';
import AccessoryOverviewTable from '@/components/Localisation/AccessoryOverviewTable';
import { findMatchingAccessories, buildContent } from '@/lib/localisation-utils';

export default {
  components: {
    Na2aHeader,
    CollapseLink,
    VehicleSearch,
    VehicleSelector,
    VehicleAccessoriesSelector,
    IndeterminedAccessoryModal,
    AccessoryOverviewTable
  },

  props: {
    organisation: {
      type: String,
      required: true
    },
    modelId: {
      type: String,
      required: true
    },
    url: {
      type: String,
      required: true
    }
  },

  data () {
    return {
      loading: true,
      loadingVehicles: true,
      loadingAccessories: true,
      loadingAccessoryTable: true,
      showSearch: true,
      showSelection: false,
      showAccessories: true,
      showServices: false,
      searchCriteria: {},
      selectedModel: {},
      accessoryTable: {},
      vehicles: {
        vehicles: []
      },
      selectedVehicles: [],
      vehicleAccessories: {
        accessories: []
      },
      selectedAccessory: {},
      vehicleSelectionCount: 0,
      showIndeterminedAccessoryModal: false,
      accessoryOverviewData: [ {
        rows: []
      }],
      error: null
    };
  },

  created () {
    this.load();
  },

  computed: {
    searchLabel () {
      if (this.searchCriteria && this.searchCriteria.project && this.searchCriteria.model) {
        return `Vehicle Search(${this.searchCriteria.project.value},${this.searchCriteria.model.value})`;
      }

      return 'Vehicle Search';
    },

    filteredVehicles () {
      // if advanced filter criteria set => filter out vehicles
      const filteredVehicles = Object.assign({}, this.vehicles);

      // TODO: implement filtering once objects are present in PIM
      if (this.searchCriteria && this.searchCriteria.advanced) {
        if (this.searchCriteria.advanced.tmeBody && this.searchCriteria.advanced.tmeBody !== '') {
          console.log('Filter out vehicles with correct TMEBody');
        }

        if (this.searchCriteria.advanced.tmeEngine && this.searchCriteria.advanced.tmeEngine !== '') {
          console.log('Filter out vehicles with correct TMEEngine');
        }

        if (this.searchCriteria.advanced.tmeTransmission && this.searchCriteria.advanced.tmeTransmission !== '') {
          console.log('Filter out vehicles with correct TMETransmission');
        }
      }

      return filteredVehicles;
    },

    permittedSearchOptions () {
      console.log('find permitted advanced searches');
      const vehicles = Object.assign([], this.filteredVehicles.vehicles);

      const permittedSearchOptions = {
        tmeBodies: [],
        tmeEngines: [],
        tmeTransmissions: []
      };

      if (!vehicles || vehicles.length === 0) return permittedSearchOptions;

      vehicles.forEach(v => {
        // TODO: find all unique permitted bodies/engines/transmissions;
        if (this.searchCriteria && this.searchCriteria.advanced) {
          if (this.searchCriteria.advanced.tmeBody && this.searchCriteria.advanced.tmeBody !== '') {
            console.log('find all unique permitted tme bodies');
          }
          if (this.searchCriteria.advanced.tmeEngine && this.searchCriteria.advanced.tmeEngine !== '') {
            console.log('find all unique permitted tme engines');
          }
          if (this.searchCriteria.advanced.tmeTransmission && this.searchCriteria.advanced.tmeTransmission !== '') {
            console.log('find all unique permitted tme transmissions');
          }
        }
      });
      return permittedSearchOptions;
    }
  },

  methods: {
    load () {
      this.loading = false;
      this.loadingAccessoryTable = false;
      this.loadingVehicles = false;
      this.loadingAccessories = false;
      this.loadAccessoryTable();
      this.loadModel();
    },

    loadAccessoryTable () {
      this.loadingAccessoryTable = true;
      axios.get(`/api/localisation/tableinfo`)
        .then(res => {
          this.loadingAccessoryTable = false;
          this.error = null;

          res.data.tabs.forEach(row => {
            row.vehicleData = [];
          });

          this.$set(this, 'accessoryTable', res.data);
        })
        .catch(err => {
          this.loadingAccessoryTable = false;
          this.error = err.toString();
        });
    },

    loadModel () {
      if (this.modelId && this.modelId !== '0') {
        this.loading = true;
        axios.get(`/api/localisation/model?modelId=${this.modelId}`)
          .then(res => {
            this.loading = false;
            this.error = null;
            this.$set(this, 'selectedModel', res.data);
          })
          .catch(err => {
            this.loading = false;
            this.error = err.toString();
          });
      } else {
        this.selectedModel = {};
      }
    },

    loadVehicles () {
      if (this.searchCriteria.model) {
        this.loadingVehicles = true;
        axios.get(`/api/localisation/vehicles?modelId=${this.searchCriteria.model.key}`)
          .then(res => {
            this.loadingVehicles = false;
            this.error = null;

            res.data.vehicles.forEach(row => {
              row.selected = false;
            });

            this.$set(this, 'vehicles', res.data);
            this.showSelection = true;
          })
          .catch(err => {
            this.loadingVehicles = false;
            this.showSelection = false;
            this.error = err.toString();
          });
      }
    },

    loadVehicleAccessories () {
      if (this.searchCriteria.model) {
        this.loadingAccessories = true;

        const requestParams = {
          brand: encodeURI(filterBrand(this.searchCriteria.project.key)),
          model: encodeURI(this.searchCriteria.project.value),
          tmeProject: encodeURI(this.searchCriteria.model.parentKey)
        };

        axios.get(`/api/localisation/accessories?brand=${requestParams.brand}&model=${requestParams.model}&tmeProject=${requestParams.tmeProject}`)
          .then(res => {
            this.loadingAccessories = false;
            this.error = null;

            res.data.accessories.forEach(row => {
              row.selected = false;
              row.indeterminate = false;
            });

            this.$set(this, 'vehicleAccessories', res.data);
          })
          .catch(err => {
            this.loadingAccessories = false;
            this.error = err.toString();
          });
      }
    },

    updateSearchCriteria (criteria) {
      this.searchCriteria = criteria;
      this.loadVehicles();
      this.loadVehicleAccessories();
    },

    updateVehicleCount (count) {
      this.vehicleSelectionCount = count;
    },

    updateVehicle (vehicle) {
      if (vehicle.selected === true) {
        this.addVehicle(vehicle);
      } else {
        this.removeVehicle(vehicle);
      }
      this.updateAccessories();
    },

    addVehicle (vehicle) {
      const result = this.selectedVehicles.find(v => v.localGrade === vehicle.localGrade && v.localCode === vehicle.localCode);
      if (!result) {
        this.selectedVehicles.push(vehicle);
      }
      this.buildAccessoryOverviewTable();
    },

    removeVehicle (vehicle) {
      const index = this.selectedVehicles.findIndex(v => v.localGrade === vehicle.localGrade && v.localCode === vehicle.localCode);
      if (index !== -1) {
        this.selectedVehicles.splice(index, 1);
      }
      this.buildAccessoryOverviewTable();
    },

    buildAccessoryOverviewTable () {
      const accessories = findMatchingAccessories(this.selectedVehicles, this.vehicleAccessories.accessories);
      this.accessoryOverviewData = buildContent(this.selectedVehicles, accessories, this.accessoryTable);
    },

    updateAccessories () {
      /*
      1. if a common accessory is found for all selected vehicles => accessory is checked
      2. if a accessory is found for one or more (but not all) selected vehicles => accessory is indetermined
      3. if no accessory is found for one or more selected vehicles => accessory is not checked.
       */
      let vm = this;
      for (const accessory of vm.vehicleAccessories.accessories) {
        if (accessory.classifications) {
          const matchingVehicles = [];
          for (const classification of accessory.classifications) {
            const vehicles = vm.selectedVehicles.find(v => v.localCode === classification.label);
            if (vehicles) {
              matchingVehicles.push(vehicles);
            }
          }
          if (matchingVehicles.length === 0) {
            accessory.selected = false;
            setTimeout(function () {
              accessory.indeterminate = false;
            }, 10);
          } else if (matchingVehicles.length === vm.selectedVehicles.length) {
            accessory.selected = true;
            setTimeout(function () {
              accessory.indeterminate = false;
            }, 10);
          } else {
            accessory.selected = false;
            setTimeout(function () {
              accessory.indeterminate = true;
            }, 10);
          }
        }
      }
    },

    updateAccessory (accessory) {
      this.selectedAccessory = accessory;
      if (accessory.indeterminate === true) {
        this.showIndeterminedAccessoryModal = true;
      } else {
        this.updateAccessoryClassification();
      }
    },

    updateAccessoryClassification () {
      const accessory = {
        id: this.selectedAccessory.applicationId,
        classifications: this.selectedAccessory.classifications,
        add: this.selectedAccessory.selected
      };

      const vehicles = [];
      this.selectedVehicles.forEach(v => {
        const vehicle = {
          localCodeIdentifier: v.localCodeIdentifier,
          localCodeLabel: v.localCode
        };
        vehicles.push(vehicle);
      });

      const body = {
        accessory: accessory,
        vehicles: vehicles
      };

      axios.post(`/api/localisation/update`, body)
        .then(res => {
          console.log('update request send and processed.');
          this.buildAccessoryOverviewTable();
        })
        .catch(err => {
          eventbus.$emit('showMessage', {
            title: "Couldn't save entry",
            err
          });
        });
    },

    ignoreIndeterminedAccessory () {
      const index = this.vehicleAccessories.accessories.findIndex(a => a.id === this.selectedAccessory.id);
      if (index !== -1) {
        this.vehicleAccessories.accessories[index].selected = false;
        let vm = this;
        setTimeout(function () {
          vm.vehicleAccessories.accessories[index].indeterminate = true;
        }, 10);
      } else {
        console.log('accessory not found!!!');
      }
    },

    setAdvancedSearch (advancedSearch) {
      this.searchCriteria.advanced = Object.assign({}, advancedSearch);
    },

    resetAdvancedSearch (advancedSearch) {
      console.log('Reset filtered vehicles');
      this.searchCriteria.advanced = Object.assign({}, advancedSearch);
    }
  }
};
</script>

<style scoped>
.localisation-wrapper {
  height: 90vh;
  display: flex;
  flex-direction: column;
  margin-left: 5px;
}
.localisation-section-top {
  flex: 1 -1 auto;
  max-height: 100%;
}
.localisation-section {
  min-height: 40px;
  flex: 1 -1 auto;
  max-height: 100%;
  overflow-y: auto;
  margin-top: 10px;
}

.localisation-section-bottom {
  flex: 0 -1 auto;
  max-height: 100%;
  overflow: auto;
}

.localisation-collapse {
  height: 100%;
}

.simple-collapse.collapsed { flex: initial; }
.simple-collapse.collapsed > div { display: none; }
</style>
