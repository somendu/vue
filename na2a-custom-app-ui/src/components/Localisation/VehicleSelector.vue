<template>
    <div id="localisation-vehicle-selections">
        <div id="localisation-vehicle-table-section" class="scrollable">
            <b-form-checkbox
                id="show-unselected"
                style="height: 10px; margin: 5px"
                v-model="filterUnselectedVehicles"
            >Show only unselected</b-form-checkbox>

            <table id="localisation-vehicle-table" class="table vehicle-table table-sm table-stripped">
                <thead>
                    <tr id="localisation-vehicle-headerRow">
                        <th class="row-checkbox">
                            <b-form-checkbox
                                id="select-all-vehicles"
                                :checked="allSelected"
                                @change="selectAll"
                                style="height: 10px;"></b-form-checkbox>
                        </th>
                        <th class="row-medium">Local grade</th>
                        <th class="row-medium">Suffix</th>
                        <th class="row-medium">Interior colour</th>
                        <th class="row-medium">Exterior colour</th>
                        <th class="row-medium">Trim colour</th>
                        <th class="row-large">Local code</th>
                    </tr>
                </thead>

                <tbody>
                    <tr id="localisation-vehicle-filter-row">
                        <td></td>
                        <td>
                            <div class="input-group input-group-sm sm-3">
                                <input
                                  id="localisation-vehicle-filterLocalGrade"
                                  type="text"
                                  class="form-control form-control-sm"
                                  placeholder="Local Grade"
                                  aria-label="LocalGrade"
                                  aria-describedby="clear-localgrade"
                                  v-model="filterLocalGrade" />
                                <div class="input-group-prepend">
                                    <b-button variant="info" id="clear-localgrade" class="form-control form-control-sm" @click="filterLocalGrade = ''">X</b-button>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="input-group input-group-sm sm-3">
                                <input
                                  id="localisation-vehicle-filterSuffix"
                                  type="text"
                                  class="form-control form-control-sm"
                                  placeholder="Suffix"
                                  aria-label="Suffix"
                                  aria-describedby="clear-suffix"
                                  v-model="filterSuffix" />
                                <div class="input-group-prepend">
                                    <b-button variant="info" id="clear-suffix" class="form-control form-control-sm" @click="filterSuffix = ''">X</b-button>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="input-group input-group-sm sm-3">
                                <input
                                  id="localisation-vehicle-filterInteriorColour"
                                  type="text"
                                  class="form-control form-control-sm"
                                  placeholder="Interior colour"
                                  aria-label="InteriorColour"
                                  aria-describedby="clear-interior-colour"
                                  v-model="filterInteriorColour" />
                                <div class="input-group-prepend">
                                    <b-button variant="info" id="clear-interior-colour" class="form-control form-control-sm" @click="filterInteriorColour = ''">X</b-button>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="input-group input-group-sm sm-3">
                                <input
                                  id="localisation-vehicle-filterExteriorColour"
                                  type="text"
                                  class="form-control form-control-sm"
                                  placeholder="Exterior colour"
                                  aria-label="ExteriorColour"
                                  aria-describedby="clear-exterior-colour"
                                  v-model="filterExteriorColour" />
                                <div class="input-group-prepend">
                                    <b-button variant="info" id="clear-exterior-colour" class="form-control form-control-sm" @click="filterExteriorColour = ''">X</b-button>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="input-group input-group-sm sm-3">
                                <input
                                  id="localisation-vehicle-filterTrimColour"
                                  type="text"
                                  class="form-control form-control-sm"
                                  placeholder="Trim colour"
                                  aria-label="TrimColour"
                                  aria-describedby="clear-trim-colour"
                                  v-model="filterTrimColour" />
                                <div class="input-group-prepend">
                                    <b-button variant="info" id="clear-trim-colour" class="form-control form-control-sm" @click="filterTrimColour = ''">X</b-button>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="input-group input-group-sm sm-3">
                                <input
                                  id="localisation-vehicle-filterLocalCode"
                                  type="text"
                                  class="form-control form-control-sm"
                                  placeholder="Local code"
                                  aria-label="LocalCode"
                                  aria-describedby="clear-localcode"
                                  v-model="filterLocalCode" />
                                <div class="input-group-prepend">
                                    <b-button variant="info" id="clear-localcode" class="form-control form-control-sm" @click="filterLocalCode = ''">X</b-button>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr v-for="(vehicle, idx) of filteredVehicles" :key="idx">
                      <td class="row-checkbox" v-if="showUnselectedVehicles(vehicle)">
                          <b-form-checkbox
                              style="height: 10px;"
                              :checked = vehicle.selected
                              @change="updateVehicleSelected(vehicle)"
                          >
                          </b-form-checkbox>
                      </td>
                      <td class="row-medium" v-if="showUnselectedVehicles(vehicle)">{{vehicle.localGrade}}</td>
                      <td class="row-medium" v-if="showUnselectedVehicles(vehicle)">{{vehicle.suffix}}</td>
                      <td class="row-medium" v-if="showUnselectedVehicles(vehicle)">{{vehicle.interiorColour}}</td>
                      <td class="row-medium" v-if="showUnselectedVehicles(vehicle)">{{vehicle.exteriorColour}}</td>
                      <td class="row-medium" v-if="showUnselectedVehicles(vehicle)">{{vehicle.trimColour}}</td>
                      <td class="row-large" v-if="showUnselectedVehicles(vehicle)">{{vehicle.localCode}}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>
<script>
export default {
  props: {
    vehicles: {
      type: Object,
      required: true
    }
  },

  data () {
    return {
      filterLocalGrade: '',
      filterSuffix: '',
      filterInteriorColour: '',
      filterExteriorColour: '',
      filterTrimColour: '',
      filterLocalCode: '',
      filterUnselectedVehicles: false
    };
  },

  methods: {
    selectAll () {
      let newStatus = !this.allSelected;
      this.vehicles.vehicles.forEach(vehicle => { vehicle.selected = newStatus; });
      this.emitVehicleCount();
    },

    showUnselectedVehicles (vehicle) {
      return this.filterUnselectedVehicles === false || (this.filterUnselectedVehicles === true && vehicle.selected === false);
    },

    updateVehicleSelected (vehicle) {
      // TODO: send out event to save selection
      vehicle.selected = !vehicle.selected;
      this.emitVehicleUpdate(vehicle);
      this.emitVehicleCount();
    },

    emitVehicleUpdate (vehicle) {
      this.$emit('updateVehicle', vehicle);
    },

    emitVehicleCount () {
      this.$emit('updateVehicleCount', this.countVehicles);
    }

  },

  computed: {
    countVehicles () {
      return this.vehicles.vehicles.filter(v => v.selected === true).length;
    },

    allSelected () {
      return !this.vehicles.vehicles.find(v => v.selected === false);
    },

    filteredVehicles () {
      let list = this.vehicles.vehicles;

      if (this.filterLocalGrade === '' &&
            this.filterSuffix === '' &&
            this.filterInteriorColour === '' &&
            this.filterExteriorColour === '' &&
            this.filterTrimColour === '' &&
            this.filterLocalCode === '') {
        return this.vehicles.vehicles;
      }

      if (this.filterLocalGrade !== '') {
        list = list.filter(v => v.localGrade.toLowerCase().includes(this.filterLocalGrade.toLowerCase()));
      }

      if (this.filterSuffix !== '') {
        list = list.filter(v => v.suffix.toLowerCase().includes(this.filterSuffix.toLowerCase()));
      }

      if (this.filterInteriorColour !== '') {
        list = list.filter(v => v.interiorColour.toLowerCase().includes(this.filterInteriorColour.toLowerCase()));
      }

      if (this.filterExteriorColour !== '') {
        list = list.filter(v => v.ExteriorColour.toLowerCase().includes(this.filterExteriorColour.toLowerCase()));
      }

      if (this.filterTrimColour !== '') {
        list = list.filter(v => v.TrimColour.toLowerCase().includes(this.filterTrimColour.toLowerCase()));
      }

      if (this.filterLocalCode !== '') {
        list = list.filter(v => v.localCode.toLowerCase().includes(this.filterLocalCode.toLowerCase()));
      }

      if (this.filterShowUnselected) {
        list = list.filter(v => v.selected === false);
      }

      return list;
    }
  }
};
</script>
<style scoped>
.scrollable {
  overflow-y: scroll;
}

.vehicle-table {
  table-layout: fixed;
  width: 100%;
  white-space: nowrap;

}
.vehicle-table td {
  white-space: wrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.cell-input {
  width: 90%;
}

.row-checkbox {
  width: 50px;
}

.row-small {
  width: 10%;
}

.row-medium {
  width: 15%
}

.row-large {
  width: 30%
}
</style>
