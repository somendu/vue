<template>
    <div :id="'accessory-overview-tab-' + name" class="scrollable">
        <b-form-checkbox
                :id="'show-empty-columns-' + name"
                style="height: 10px; margin: 5px"
                v-model="showEmptyColumns"
            >Show empty columns</b-form-checkbox>

        <table :id="'localisation-accessories-tab-table' + name" class="table accessory-table table-sm table-stripped">
            <!-- TODO: hide column if vehicle info is empty-->
            <thead>
                <tr id="localisation-accessories-tab-headerRow">
                    <th class="row-large">Local vehicle<br>(Grade|Model Code|TOCode)</th>
                    <th class="row-medium" v-for="(column, idx) of columns" :key="idx" v-show="showColumn(idx)">
                        {{column.headerName}}
                    </th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(row, idx) of entries" :key="idx" :id="'localisation-accesory-tab-row-' + idx">
                  <td class="row-large">{{row.vehicleName}}</td>
                  <td class="row-medium" v-for="(item, itemIdx) of row.data" :key="itemIdx" v-show="showColumn(itemIdx)">{{item}}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>
<script>
export default {
  props: {
    name: {
      type: String,
      required: true
    },
    columns: {
      type: Array,
      required: true
    },
    rows: {
      type: Array,
      required: true
    }
  },

  data () {
    return {
      showEmptyColumns: true,
      entries: []
    };
  },

  watch: {
    rows: {
      handler: 'load',
      immediate: true
    }
  },

  methods: {
    load () {
      this.entries = [];
      for (const row of this.rows) {
        this.entries.push(Object.assign({}, row));
      }
    },

    showColumn (idx) {
      if (this.entries && this.entries.length > 0) {
        for (const row of this.entries) {
          if (row.data[idx] !== '') {
            return true;
          }
        }
        return this.showEmptyColumns;
      }
      return true;
    }
  }

};
</script>
<style scoped>
.scrollable {
  overflow-y: scroll;
}

.accessory-tab-table {
  table-layout: fixed;
  width: 100%;
  white-space: nowrap;

}
.accessory-tab-table td {
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
