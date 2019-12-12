<template>
    <div id="all-parts-overview">
        <div id="all-parts-table-section">
            <table id="all-parts-table" class="table table-sm table-striped">
                <thead>
                    <tr id="all-parts-headerRow">
                        <th></th>
                        <th>Part Number</th>
                        <th>Part Name</th>
                        <th>Colour/Material</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td></td>
                        <td>
                          <div class="input-group input-group-sm sm-3">
                            <input
                              id="all-parts-filterByNumber"
                              type="text"
                              class="form-control form-control-sm"
                              placeholder="Part Number"
                              aria-label="partNumber"
                              aria-describedby="clear-partnumber"
                              v-model="filterPartNumber" />
                            <div class="input-group-prepend">
                              <b-button variant="info" id="clear-partnumber" class="form-control form-control-sm" @click="clearPartNumberFilter">X</b-button>
                            </div>
                          </div>
                        </td>
                        <td>
                          <div class="input-group input-group-sm mb-3">
                            <input
                              id="all-parts-filterByName"
                              type="text"
                              class="form-control form-control-sm"
                              placeholder="Part Name"
                              aria-label="partName"
                              aria-describedby="clear-partname"
                              v-model="filterPartName"/>
                            <div class="input-group-prepend">
                              <b-button variant="info" id="clear-partname" class="form-control-sm" @click="clearPartNameFilter">X</b-button>
                            </div>
                          </div>
                        </td>
                        <td>
                          <div class="input-group input-group-sm mb-3">
                            <input
                              id="all-parts-filterByColourOrMaterial"
                              class="form-control form-control-sm"
                              aria-label="colour-material"
                              aria-describedby="clear-colour-material"
                              placeholder="Colour / Material"
                              v-model="filterColourMaterial"
                            />
                            <div class="input-group-prepend">
                              <b-button variant="info" id="clear-colour-material" class="form-control form-control-sm" @click="clearColourMaterialFilter">X</b-button>
                            </div>
                          </div>
                        </td>
                    </tr>
                    <tr v-for="(part,idx) of parts"
                      :key="idx"
                      :id="'application-parts-row-' + idx">
                      <td width="10px">
                        <b-form-checkbox
                          :id="'all-parts-partSelected-' + idx"
                          v-model="part.selected"
                          @change="updatePartSelection(idx)"
                        ></b-form-checkbox>
                      </td>
                      <td :id="'all-parts-partNumber-' + idx">{{ part.partNumber }}</td>
                      <td :id="'all-parts-partName-' + idx">{{ part.partName }}</td>
                      <td :id="'all-parts-colour-material-' + idx">
                        <span v-if="part.colour !== ''">{{ part.colour }} </span>
                        <span v-if="part.colour !== '' && part.marterial !== ''"> / </span>
                        <span v-if="part.material !== ''">{{ part.material }}</span></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
export default {
  props: {
    allParts: {
      type: Array,
      required: true
    },
    partNumber: String,
    partName: String
  },

  data () {
    return {
      parts: [],
      filterPartNumber: '',
      filterPartName: '',
      filterColourMaterial: ''
    };
  },

  created () {
    this.filterPartNumber = this.partNumber;
  },

  methods: {
    getParts () {
      this.parts = this.allParts.slice(0);
    },
    filterInput (type, value, oldValue) {
      if (oldValue.length > value.length) {
        this.$emit('updateParts', type, value);
        return;
      }

      if (value.length > 2) {
        this.$emit('updateParts', type, value);
      }
    },
    clearPartNumberFilter () {
      this.filterPartNumber = '';
    },
    clearPartNameFilter () {
      this.filterPartName = '';
    },
    clearColourMaterialFilter () {
      this.filterColourMaterial = '';
    },
    updatePartSelection (idx) {
      this.$emit('updatePartSelection',
        Object.assign({},
          { partNumber: this.parts[idx].partNumber },
          { partName: this.parts[idx].partName },
          { characteristic: this.parts[idx].characteristic },
          { selected: !this.parts[idx].selected }));
    }
  },

  watch: {
    allParts () {
      this.getParts();
    },
    filterPartNumber (newValue, oldValue) {
      this.filterInput('partNumber', newValue, oldValue);
    },
    filterPartName (newValue, oldValue) {
      this.filterInput('partName', newValue, oldValue);
    },
    filterColourMaterial (newValue, oldValue) {
      this.filterInput('colourMaterial', newValue, oldValue);
    }
  }
};
</script>

<style scoped>
#all-parts-overview {
  /* max-height: 10%; */
  /* overflow-y: scroll; */
}
</style>
