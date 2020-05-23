<template>
  <div
    id="applications-view"
    class="simple-collapse"
  >
    <div id="pl-collapse-application-view">
      <b-form-checkbox
        id="select-all"
        :checked="allSelected"
        class="part-linking"
        @change="selectAll">Select/De-select all</b-form-checkbox>
      <table id="applicationOverview" class="table table-sm table-striped">
        <thead>
          <tr id="apo-headerRow">
            <th></th>
            <th>Commodity</th>
            <th>Application Name</th>
            <th>Accessory Category</th>
            <th>Application ID</th>
            <th>Description</th>
            <th>Vehicle Generation</th>
            <th>Application Summary</th>
            <th>Application Status</th>
            <th>Interior Color</th>
            <th>Trim Color</th>
            <th>Exterior color</th>
            <th>Color from AP</th>
            <th>Material from AP</th>
            <!-- <th>Homologation Type</th> -->
          </tr>
        </thead>
        <tbody>
        <tr v-for="(variant,idx) of filteredLinkingVariants"
            :key="idx"
            :id="'apo-row-' + idx">
            <td :id="'apo-selected-' + idx" width="10px">
              <b-form-checkbox
                :checked="variant.selected"
                @change="openModal(idx)"
                style="height: 10px;"
              >
              </b-form-checkbox>
            </td>
            <td :id="'apo-commodity-' + idx">{{variant.commodity}}</td>
            <td :id="'apo-name-' + idx"> {{variant.name}}</td>
            <td :id="'apo-accessory-category-' + idx"> {{variant.accessoryCategory}}</td>
            <td :id="'apo-application-id-' + idx">{{variant.variantNo}}</td>
            <td :id="'apo-description-' + idx"> {{variant.description}}</td>
            <td :id="'apo-vehicleGen-' + idx"> {{variant.vehicleGeneration}}</td>
            <td :id="'apo-summary-' + idx"> {{variant.summary}}</td>
            <td :id="'apo-status-' + idx"> {{variant.status}}</td>
            <td :id="'apo-interior-colours-' + idx"> {{variant.interiorColours}}</td>
            <td :id="'apo-trim-colours-' + idx"> {{variant.trimColours}}</td>
            <td :id="'apo-exteriour-colours-' + idx"> {{variant.exteriorColours}}</td>
            <td :id="'apo-product-colour-' + idx"> {{variant.productColour}}</td>
            <td :id="'apo-product-material-' + idx"> {{variant.productMaterial}}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import CollapseLink from '@/components/CollapseLink';

export default {
  components: {
    CollapseLink
  },
  props: {
    partLinkingVariants: {
      type: Array,
      required: true
    }
  },
  created () {
    this.load();
  },
  computed: {
    allSelected () {
      return !this.partLinkingVariants.find(v => !v.selected);
    },

    filteredLinkingVariants () {
      return this.partLinkingVariants.filter(v => v.status !== 'cancelled');
    }
  },

  methods: {
    load () {
    },
    selectAll () {
      this.$emit('selectAllClick');
    },
    undoUnsavedChanges (idx) {
      // TODO: clear changes + toggle
      console.log('clear changes + toggle ' + idx);
    },
    openModal (idx) {
      this.$emit('openModal', idx);
    }
  }
};
</script>

<style scoped>
</style>
