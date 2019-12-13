<template>
    <div id="localisation-accessories-selections">
        <div id="localisation-accessories-table-section" class="scrollable">
            <b-form-checkbox
                id="show-unselected-accessories"
                style="height: 10px; margin: 5px"
                v-model="filterUnselectedAccessories"
            >Show only unselected</b-form-checkbox>

            <table id="localisation-accessories-table" class="table accessory-table table-sm table-stripped">
                <thead>
                    <tr id="localisation-accessories-headerRow">
                        <th class="row-checkbox">
                            <b-form-checkbox
                                id="select-all-accessories"
                                :checked="allSelected"
                                @change="selectAll"
                                style="height: 10px;"></b-form-checkbox>
                        </th>
                        <th class="row-medium">
                          <sortable-header :header="'Commodity'" :sortableItem="'commodity'" @sort="sort"></sortable-header>
                        </th>
                        <th class="row-large">
                          <sortable-header :header="'Accessory Product'" :sortableItem="'productName'" @sort="sort"></sortable-header>
                        </th>
                        <th class="row-large">
                          <sortable-header :header="'Application'" :sortableItem="'applicationName'" @sort="sort"></sortable-header>
                        </th>
                        <th class="row-medium">
                          <sortable-header :header="'Colour'" :sortableItem="'productColour'" @sort="sort"></sortable-header>
                        </th>
                        <th class="row-medium">
                          <sortable-header :header="'Material'" :sortableItem="'productMaterial'" @sort="sort"></sortable-header>
                        </th>
                        <th class="row-medium">
                          <sortable-header :header="'Std Fit PPO'" :sortableItem="'isStdFitPPO'" @sort="sort"></sortable-header>
                        </th>
                        <th class="row-medium">
                          <sortable-header :header="'In PPO?'" :sortableItem="'isInPPO'" @sort="sort"></sortable-header>
                        </th>
                        <th class="row-medium">
                          <sortable-header :header="'TME standard'" :sortableItem="'tmeStandard'" @sort="sort"></sortable-header>
                        </th>
                        <th class="row-medium">
                          <sortable-header :header="'TME pack offer'" :sortableItem="'tmePackOffer'" @sort="sort"></sortable-header>
                        </th>
                        <th class="row-medium">
                          <sortable-header :header="'TME demo'" :sortableItem="'tmeDemo'" @sort="sort"></sortable-header>
                        </th>
                    </tr>
                </thead>

                <thead>
                    <tr>
                      <td></td> <!--Empty field as the checkbox has no filter-->
                      <td>
                        <text-input-filter :filterType="'filterCommodity'" :placeholder="'Commodity'" @update="updateFilter"></text-input-filter>
                      </td>
                      <td>
                        <text-input-filter :filterType="'filterProduct'" :placeholder="'Product'" @update="updateFilter"></text-input-filter>
                      </td>
                      <td>
                        <text-input-filter :filterType="'filterApplication'" :placeholder="'Application'" @update="updateFilter"></text-input-filter>
                      </td>
                      <td>
                        <text-input-filter :filterType="'filterColour'" :placeholder="'Colour'" @update="updateFilter"></text-input-filter>
                      </td>
                      <td>
                        <text-input-filter :filterType="'filterMaterial'" :placeholder="'Material'" @update="updateFilter"></text-input-filter>
                      </td>
                      <td>
                        <text-input-filter :filterType="'filterStdFitPPO'" :placeholder="'Std Fit PPO'" @update="updateFilter"></text-input-filter>
                      </td>
                      <td>
                        <text-input-filter :filterType="'filterInPPO'" :placeholder="'Is in PPO'" @update="updateFilter"></text-input-filter>
                      </td>
                      <td>
                        <text-input-filter :filterType="'filterTMEStandard'" :placeholder="'TME Standard'" @update="updateFilter"></text-input-filter>
                      </td>
                      <td>
                        <text-input-filter :filterType="'filterTMEPackOffer'" :placeholder="'TME Pack Offer'" @update="updateFilter"></text-input-filter>
                      </td>
                      <td>
                        <text-input-filter :filterType="'filterTMEDemo'" :placeholder="'TME Demo'" @update="updateFilter"></text-input-filter>
                      </td>
                    </tr>
                    <tr v-for="(accessory, idx) of filteredAccessories" :key="idx">
                      <td class="row-checkbox" v-if="showUnselectedAccessory(accessory)">
                          <b-form-checkbox
                              style="height: 10px;"
                              :checked = accessory.selected
                              :indeterminate.sync="accessory.indeterminate"
                              @change="updateAccessorySelected(accessory)"
                          >
                          </b-form-checkbox>
                      </td>
                      <td class="row-medium" v-if="showUnselectedAccessory(accessory)">{{accessory.commodity}}</td>
                      <td class="row-large" v-if="showUnselectedAccessory(accessory)">{{accessory.productName}}</td>
                      <td class="row-large" v-if="showUnselectedAccessory(accessory)">{{accessory.applicationName}}</td>
                      <td class="row-medium" v-if="showUnselectedAccessory(accessory)">{{accessory.productColour}}</td>
                      <td class="row-medium" v-if="showUnselectedAccessory(accessory)">{{accessory.productMaterial}}</td>
                      <td class="row-medium" v-if="showUnselectedAccessory(accessory)">{{accessory.isStdFitPPO}}</td>
                      <td class="row-medium" v-if="showUnselectedAccessory(accessory)">{{accessory.isInPPO}}</td>
                      <td class="row-medium" v-if="showUnselectedAccessory(accessory)">{{join(accessory.tmeStandard)}}</td>
                      <td class="row-medium" v-if="showUnselectedAccessory(accessory)">{{join(accessory.tmePackOffer)}}</td>
                      <td class="row-medium" v-if="showUnselectedAccessory(accessory)">{{join(accessory.tmeDemo)}}</td>
                    </tr>
                </thead>
            </table>
        </div>
    </div>
</template>
<script>
import SortableHeader from '@/components/Shared/SortableHeader';
import TextInputFilter from '@/components/Shared/TextInputFilter';
export default {
  components: {
    SortableHeader,
    TextInputFilter
  },

  props: {
    vehicleAccessories: {
      type: Object,
      required: true
    }
  },

  data () {
    return {
      currentSort: 'commodity',
      currentSortDir: 'asc',
      filterCommodity: '',
      filterProduct: '',
      filterApplication: '',
      filterColour: '',
      filterMaterial: '',
      filterStdFitPPO: '',
      filterInPPO: '',
      filterTMEStandard: '',
      filterTMEPackOffer: '',
      filterTMEDemo: '',
      filterUnselectedAccessories: false
    };
  },

  methods: {
    selectAll () {
      let newStatus = !this.allSelected;
      this.vehicleAccessories.accessories.forEach(accessory => { accessory.selected = newStatus; });
    },

    showUnselectedAccessory (accessory) {
      return this.filterUnselectedAccessories === false || (this.filterUnselectedAccessories === true && accessory.selected === false);
    },

    updateAccessorySelected (accessory) {
      accessory.selected = !accessory.selected;
      this.$emit('updateAccessory', accessory);
    },

    join (value) {
      if (Array.isArray(value) && value.length > 1) {
        return value.join(', ');
      }
      return !value ? '' : value[0];
    },

    sort (direction, column) {
      this.currentSort = column;
      this.currentSortDir = direction.toLowerCase();

      if (this.currentSort !== 'tmeStandard' && this.currentSort !== 'tmePackOffer' && this.currentSort !== 'tmeDemo') {
        this.filteredAccessories.sort((a, b) => {
          let modifier = 1;
          if (this.currentSortDir === 'desc') modifier = -1;

          if (a[this.currentSort] < b[this.currentSort]) return -1 * modifier;
          if (a[this.currentSort] > b[this.currentSort]) return 1 * modifier;
          return 0;
        });
      } else {
        this.filteredAccessories.sort((a, b) => {
          let modifier = 1;
          if (this.currentSortDir === 'desc') modifier = -1;

          const aJoined = this.join(a[this.currentSort]);
          const bJoined = this.join(b[this.currentSort]);

          if (aJoined < bJoined) return -1 * modifier;
          if (aJoined > bJoined) return 1 * modifier;
          return 0;
        });
      }
    },

    updateFilter (filterType, filterValue) {
      if (filterType === 'filterCommodity') this.filterCommodity = filterValue;
      if (filterType === 'filterProduct') this.filterProduct = filterValue;
      if (filterType === 'filterApplication') this.filterApplication = filterValue;
      if (filterType === 'filterColour') this.filterColour = filterValue;
      if (filterType === 'filterMaterial') this.filterMaterial = filterValue;
      if (filterType === 'filterStdFitPPO') this.filterStdFitPPO = filterValue;
      if (filterType === 'filterInPPO') this.filterInPPO = filterValue;
      if (filterType === 'filterTMEStandard') this.filterTMEStandard = filterValue;
      if (filterType === 'filterTMEPackOffer') this.filterTMEPackOffer = filterValue;
      if (filterType === 'filterTMEDemo') this.filterTMEDemo = filterValue;
    }
  },

  computed: {
    allSelected () {
      return !this.vehicleAccessories.accessories.find(a => a.selected === false);
    },

    filteredAccessories () {
      let list = this.vehicleAccessories.accessories;

      if (this.filterCommodity === '' &&
            this.filterProduct === '' &&
            this.filterApplication === '' &&
            this.filterColour === '' &&
            this.filterMaterial === '' &&
            this.filterStdFitPPO === '' &&
            this.filterInPPO === '' &&
            this.filterTMEStandard === '' &&
            this.filterTMEPackOffer === '' &&
            this.filterTMEDemo === '') {
        return list;
      }

      if (this.filterCommodity !== '') {
        list = list.filter(v => v.commodity.toLowerCase().includes(this.filterCommodity.toLowerCase()));
      }

      if (this.filterProduct !== '') {
        list = list.filter(v => v.productName.toLowerCase().includes(this.filterProduct.toLowerCase()));
      }

      if (this.filterApplication !== '') {
        list = list.filter(v => v.applicationName.toLowerCase().includes(this.filterApplication.toLowerCase()));
      }

      if (this.filterColour !== '') {
        list = list.filter(v => v.productColour.toLowerCase().includes(this.filterColour.toLowerCase()));
      }

      if (this.filterMaterial !== '') {
        list = list.filter(v => v.productMaterial.toLowerCase().includes(this.filterMaterial.toLowerCase()));
      }

      if (this.filterStdFitPPO !== '') {
        list = list.filter(v => v.isStdFitPPO.toLowerCase().includes(this.filterStdFitPPO.toLowerCase()));
      }

      if (this.filterInPPO !== '') {
        list = list.filter(v => v.isInPPO.toLowerCase().includes(this.filterInPPO.toLowerCase()));
      }

      if (this.filterTMEStandard !== '') {
        list = list.filter(v => this.join(v.tmeStandard).toLowerCase().includes(this.filterTMEStandard.toLowerCase()));
      }

      if (this.filterTMEPackOffer !== '') {
        list = list.filter(v => this.join(v.tmePackOffer).toLowerCase().includes(this.filterTMEPackOffer.toLowerCase()));
      }

      if (this.filterTMEDemo !== '') {
        list = list.filter(v => this.join(v.tmeDemo).toLowerCase().includes(this.filterTMEDemo.toLowerCase()));
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

.accessory-table {
  table-layout: fixed;
  width: 100%;
  white-space: nowrap;

}
.accessory-table td {
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
  width: 15%;
  white-space: normal;
}

.row-large {
  width: 30%;
  white-space: normal;
}
</style>
