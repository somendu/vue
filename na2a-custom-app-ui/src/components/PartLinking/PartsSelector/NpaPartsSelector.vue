<template>
    <div id="npa-parts-overview">
        <div id="npa-parts-table-section">
            <table id="npa-parts-table" class="table table-sm table-striped">
                <thead>
                    <tr id="npa-parts-headerRow">
                        <th></th>
                        <th>Part Number</th>
                        <th>Part Name</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td></td>
                        <td>
                          <div class="input-group input-group-sm sm-3">
                            <input
                              id="npa-parts-filterByNumber"
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
                              id="npa-parts-filterByName"
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
                    </tr>
                    <tr v-for="(part,idx) of parts"
                      :key="idx"
                      :id="'application-parts-row-' + idx">
                      <td width="10px">
                        <b-form-checkbox
                          :id="'npa-parts-partSelected-' + idx"
                          v-model="part.selected"
                          @change="updatePartSelection(idx)"
                        ></b-form-checkbox>
                      </td>
                      <td :id="'npa-parts-partNumber-' + idx">{{ part.partNumber }}</td>
                      <td :id="'npa-parts-partName-' + idx">{{ part.partName }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
export default {
  props: {
    npaParts: {
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
      filterPartName: ''
    };
  },

  created () {
  },

  methods: {
    getParts () {
      this.parts = this.npaParts.slice(0);
    },
    filterInput (type, value, oldValue) {
      this.$emit('updateNpaParts', type, value);
    },
    clearPartNumberFilter () {
      this.filterPartNumber = '';
    },
    clearPartNameFilter () {
      this.filterPartName = '';
    },
    updatePartSelection (idx) {
      this.$emit('updatePartSelection',
        Object.assign({},
          { partNumber: this.parts[idx].partNumber },
          { partName: this.parts[idx].partName },
          { knownInNPA: this.parts[idx].knownInNPA },
          { selected: !this.parts[idx].selected }));
    }
  },

  watch: {
    npaParts () {
      this.getParts();
    },
    filterPartNumber (newValue, oldValue) {
      this.filterInput('partNumber', newValue, oldValue);
    },
    filterPartName (newValue, oldValue) {
      this.filterInput('partName', newValue, oldValue);
    }
  }
};
</script>

<style scoped>
#npa-parts-overview {
  /* max-height: 10%; */
  /* overflow-y: scroll; */
}
</style>
