<template>
  <div
    id="common-link-parts-view"
    class="simple-collapse"
    :disabled="updating"
  >
    <div id="pl-collapse-application-view">
      <p id="pl-no-application-selected" v-if="noAppsSelected">Please select at least one application</p>
      <p id="pl-no-common-parts" v-if="!noAppsSelected && hasNoCommonParts">No common parts for the current selection of applications</p>
      <b-table
      id="commonLinkedPartsOverview"
      striped
      :fields="fields"
      :items="entries"
      :sort-by.sync="sortBy"
      :sort-desc.sync="sortDesc"
      >
        <template slot="selected" slot-scope="row">
          <b-form-checkbox
              :id="'pe-partSelected' + row.index"
              v-model="row.item.selected"
              @change="updatePartSelection(row.item)"
            >
          </b-form-checkbox>
        </template>

        <template :id="pl-part-partNumber- + row.index" slot="partNumber" slot-scope="row">
          {{ row.item.partNumber }}
        </template>

        <template :id="'pl-part-partName-' + row.index" slot="partName" slot-scope="row">{{ row.item.partName }}</template>

        <template :id="'pl-part-colour-material-' + row.index"  slot="colourMaterial" slot-scope="row">
          <div v-if="row.item.colour !== ''"><b>Colour</b>{{ row.item.colour }}</div>
          <div v-if="row.item.material !== ''"><b >Material</b>{{ row.item.material }}</div>
        </template>

        <template slot="type" slot-scope="row">
          <select
              :id="'pl-part-type-' + row.index"
              class="form-control form-control-sm"
              v-model="row.item.type"
              @change="updated('type', row.item)">
            <option value="12">{{getPartTypeName('12')}}</option>
            <option value="13">{{getPartTypeName('13')}}</option>
            <option value="1">{{getPartTypeName('1')}}</option>
            <option value="14">{{getPartTypeName('14')}}</option>
          </select>
        </template>
        <template slot="quantity" slot-scope="row">
          <input
            :id="'pl-part-quantity-' + row.index"
            type="number"
            step=".01"
            class="form-control form-control-sm"
            v-model="row.item.quantity"
            @mouseout="unfocus($event)"
            @change="updated('quantity', row.item)"
            @blur="validateQuantity(row.item)">
        </template>

        <template slot="knownInNPA" slot-scope="row">
          {{ getKnownInNPA(row.item.knownInNPA) }}
        </template>
      </b-table>
    </div>
  </div>
</template>

<script>
import CollapseLink from '@/components/CollapseLink';
import { partTypeName } from '@/lib/utils';

function parseQuantity (value) {
  if (typeof value === 'number') {
    if (value <= 0) return NaN;
    return value;
  }

  if (typeof value === 'string') {
    const quantity = parseFloat(value);
    if (isNaN(quantity)) return NaN;
    if (quantity <= 0) return NaN;
    return quantity;
  }

  return NaN;
}

export default {
  components: {
    CollapseLink
  },
  props: {
    parts: {
      required: true
    },
    noAppsSelected: {
      type: Boolean,
      required: true
    },
    updating: {
      type: Boolean,
      required: true
    }
  },

  data () {
    return {
      entries: [],
      fields: [
        { key: 'selected', label: '' },
        { key: 'partNumber' },
        { key: 'partName' },
        { key: 'colourMaterial', label: 'Colour/Material' },
        { key: 'type' },
        { key: 'quantity' },
        { key: 'knownInNPA' }
      ],
      sortBy: 'partNumber',
      sortDesc: false
    };
  },

  created () {
    this.load();
  },

  methods: {
    load () {
      const entries = [];
      for (const entry of this.parts) {
        entries.push({
          material: entry.material,
          colour: entry.colour,
          orgType: entry.type,
          oldType: entry.type,
          orgQuantity: entry.quantity,
          oldQuantity: entry.quantity,
          selected: false,
          ...entry
        });
      }
      this.$set(this, 'entries', entries);
    },
    updated (field, item) {
      let idx = this.entries.findIndex(i => i.partNumber === item.partNumber && i.type === item.type);
      // if quantity was changed, but the value isn't valid, we won't emit an update
      if (field === 'quantity' && isNaN(parseQuantity(this.entries[idx].quantity))) {
        return;
      }

      // make sure to cast quantity to number, it is 'our' problem that it's a string
      this.$emit(
        'changed',
        Object.assign({}, this.entries[idx], { quantity: parseFloat(this.entries[idx].quantity) }),
        { type: this.entries[idx].oldType, quantity: parseFloat(this.entries[idx].oldQuantity) });

      this.entries[idx].oldType = this.entries[idx].type;
      this.entries[idx].oldQuantity = this.entries[idx].quantity;
    },

    validateQuantity (item) {
      let idx = this.entries.findIndex(i => i.partNumber === item.partNumber && i.type === item.type);

      const quantity = parseQuantity(this.entries[idx].quantity);
      // set quantity to 1 if it's not valid
      if (isNaN(quantity)) {
        this.entries[idx].quantity = 1;
        this.updated('quantity', item);
      } else {
        this.entries[idx].quantity = quantity.toFixed(2);
        this.updated('quantity', item);
      }
    },

    updatePartSelection (item) {
      // find it
      let idx = this.entries.findIndex(i => i.partNumber === item.partNumber && i.type === item.type);
      this.$emit('updatePartSelection',
        Object.assign({},
          { partNumber: this.entries[idx].partNumber },
          { quantity: this.entries[idx].quantity },
          { type: this.entries[idx].type },
          { selected: !this.entries[idx].selected }));
    },

    unfocus (event) {
      event.target.blur();
    },

    getPartTypeName (type) {
      return partTypeName(type);
    },

    getKnownInNPA (knownInNPA) {
      if (knownInNPA === null || knownInNPA === 'null') {
        return 'Unknown';
      }

      if (knownInNPA) {
        return 'Yes';
      }

      if (!knownInNPA) {
        return 'No';
      }
    }
  },

  computed: {
    hasNoCommonParts () {
      return this.entries.length === 0;
    }
  },

  watch: {
    parts: {
      handler (newValue) {
        // make a shallow copy
        const entries = [];
        if (newValue) {
          for (const entry of newValue) {
            entries.push({
              orgType: entry.type,
              oldType: entry.type,
              orgQuantity: entry.quantity,
              oldQuantity: entry.quantity,
              selected: false,
              ...entry
            });
          }
        }
        this.$set(this, 'entries', entries);
      },
      deep: true
    }
  }
};
</script>

<style scoped>
</style>
