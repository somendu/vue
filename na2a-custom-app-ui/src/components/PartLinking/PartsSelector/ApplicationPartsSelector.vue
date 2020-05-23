<template>
    <div id="application-parts-overview">
        <div id="application-parts-table-section" class="scrollable">
            <table id="application-parts-table" class="table table-sm table-striped">
                <thead>
                    <tr id="application-parts-headerRow">
                      <th></th>
                      <th>Part Number</th>
                      <th>Part Name</th>
                      <th>Colour/Material</th>
                      <th>Type</th>
                      <th>Quantity</th>
                    </tr>
                </thead>
                <tbody>
                  <tr id="application-parts-filters">
                    <td></td>
                    <td>
                      <div class="input-group input-group-sm mb-3">
                        <input
                        id="application-parts-filterByNumber"
                        type="text"
                        class="form-control"
                        placeholder="part number"
                        aria-label="partNumber"
                        aria-describedby="clear-partnumber"
                        v-model="partNumberFilter"
                        @keyup="filterByPartNumber">
                        <div class="input-group-prepend">
                          <b-button variant="info" id="clear-partnumber" @click="clearPartNumberFilter">X</b-button>
                        </div>
                      </div>
                    </td>
                    <td>
                      <div class="input-group input-group-sm mb-3">
                        <input
                        id="application-parts-filterByName"
                        type="text"
                        class="form-control"
                        placeholder="part name"
                        aria-label="partName"
                        aria-describedby="clear-partname"
                        v-model="partNameFilter"
                        @keyup="filterByPartName">
                        <div class="input-group-prepend">
                          <b-button variant="info" id="clear-partname" @click="clearPartNameFilter">X</b-button>
                        </div>
                      </div>
                    </td>
                    <td>
                      <div class="input-group input-group-sm mb-3">
                        <input
                        id="application-parts-filterByColourMaterial"
                        type="text"
                        class="form-control"
                        placeholder="colour / material"
                        aria-label="colourMaterial"
                        aria-describedby="clear-colour-material"
                        v-model="colourMaterialFilter"
                        @keyup="filterByColourMaterial">
                        <div class="input-group-prepend">
                          <b-button variant="info" id="clear-colour-material" @click="clearColourMaterialFilter">X</b-button>
                        </div>
                      </div>
                    </td>
                    <td></td>
                    <td></td>
                  </tr>
                  <tr v-for="(part,idx) of parts"
                      :key="idx"
                      :id="'application-parts-row-' + idx">
                      <td width="10px">
                        <b-form-checkbox
                          :id="'application-parts-partSelected' + idx"
                          v-model="part.selected"
                          @change="updatePartSelection(idx)"
                        ></b-form-checkbox>
                      </td>
                      <td :id="'application-parts-partNumber-' + idx">{{part.partNumber}}</td>
                      <td :id="'application-parts-partName-' + idx">{{part.partName}}</td>
                      <td :id="'application-parts-colour-material-' + idx">
                        <span v-if="part.colour !== ''">{{ part.colour }}</span>
                        <span v-if="part.colour !== '' && part.material !== ''"> / </span>
                        <span v-if="part.material !== ''">{{ part.material }}</span>
                      </td>
                      <td :id="'application-parts-type-' + idx">{{getPartTypeName(part.type)}}</td>
                      <td :id="'application-parts-quantity-' + idx">{{part.quantity}}</td>
                  </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
import { partTypeName } from '@/lib/utils';

export default {
  props: {
    applicationParts: {
      type: Array,
      required: true
    }
  },
  data () {
    return {
      parts: [],
      partNumberFilter: '',
      partNameFilter: '',
      colourMaterialFilter: ''
    };
  },
  created () {
    this.getParts();
  },
  watch: {
    applicationParts () {
      this.getParts();
    }
  },
  methods: {
    getParts () {
      this.parts = this.applicationParts.slice(0);
    },
    filterByPartName () {
      if (!this.applicationParts) {
        this.parts = [];
        return;
      }

      if (this.partNameFilter === '') {
        this.parts = this.applicationParts.slice(0);
      } else {
        this.parts = this.applicationParts.filter(p => p.partName && (p.partName.toLowerCase().indexOf(this.partNameFilter.toLowerCase()) !== -1));
      }
    },
    filterByPartNumber () {
      if (!this.applicationParts) {
        this.parts = [];
        return;
      }

      if (this.partNumberFilter === '') {
        this.parts = this.applicationParts.slice(0);
      } else {
        this.parts = this.applicationParts.filter(p => p.partNumber && (p.partNumber.toLowerCase().indexOf(this.partNumberFilter.toLowerCase()) !== -1));
      }
    },
    filterByColourMaterial () {
      if (!this.applicationParts) {
        this.parts = [];
        return;
      }

      if (this.colourMaterialFilter === '') {
        this.parts = this.applicationParts.slice(0);
      } else {
        this.parts = this.applicationParts.filter(p => p.characteristic && (p.characteristic.toLowerCase().indexOf(this.characteristicFilter.toLowerCase()) !== -1));
      }
    },
    clearPartNumberFilter () {
      this.partNumberFilter = '';
      this.getParts();
    },
    clearPartNameFilter () {
      this.partNameFilter = '';
      this.getParts();
    },
    clearColourMaterialFilter () {
      this.colourMaterialFilter = '';
      this.getParts();
    },
    updatePartSelection (idx) {
      this.$emit('updatePartSelection',
        Object.assign({},
          { partNumber: this.parts[idx].partNumber },
          { partName: this.parts[idx].partName },
          // { colourMaterial: this.parts[idx].colourMaterial },
          { type: this.parts[idx].type },
          { quantity: this.parts[idx].quantity },
          { selected: !this.parts[idx].selected }));
    },
    getPartTypeName (type) {
      return partTypeName(type);
    }
  }
};
</script>

<style scoped>
/* .scrollable {
  overflow-y: scroll;
  height: 600px;
} */
</style>
