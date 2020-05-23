<template>
    <div id="parts-selection-filter-bar">
        <div class="loading" v-if="loading">Loading filter content...</div>
        <table id="parts-selection-filter-bar-table" class="table table-sm" v-if="!loading">
            <tbody>
                <tr>
                    <td>
                        <div id="project-code-masking">
                          <select id="project-code-selector" class="form-control form-control-sm" v-if="!isHidden" v-model="selectedProject" @change="deActivateSelect">
                              <option :value="''">Select Project</option>
                              <option v-for="(o, idx) in filterLists.projects" :value="o" :key="idx" :id="'pl-select-project-code' + idx">{{ o }}</option>
                          </select>
                          <input id="display-masked-project-code" type="text" class="form-control form-control-sm" readonly v-if="isHidden" @click="activateSelect" v-model="maskedValue">
                        </div>
                    </td>
                    <td>
                        <select id="pic-selector" class="form-control form-control-sm" v-model="selectedPic">
                            <option :value="''">Select PIC</option>
                            <option v-for="(o, idx) in filterLists.pics" :value="o" :key="idx" :id="'pl-select-pics' + idx">{{ o }}</option>
                        </select>
                    </td>
                    <td>
                        <select id="commodity-selector" class="form-control form-control-sm" v-model="selectedCommodity">
                            <option :value="''">Select Commodity</option>
                            <option v-for="(o, id) in filterLists.commodities" :value="id" :key="id" :id="'pl-select-commodities' + id">{{ o.value }}</option>
                        </select>
                    </td>
                    <td>
                      <input type="text" class="form-control form-control-sm" placeholder="Search part number" v-model="searchPartNumber"/>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import axios from 'axios';
import eventbus from '@/eventbus';

export default {
  data () {
    return {
      loading: true,
      filterLists: Object,
      selectedProject: '',
      selectedPic: '',
      selectedCommodity: '',
      searchPartNumber: '',
      isHidden: false,
      maskedValue: ''
    };
  },

  created () {
    this.load();
  },

  methods: {
    load () {
      axios.get('/api/partlinking/filter-lists')
        .then(res => {
          res.data.projects.sort();

          this.$set(this, 'filterLists', res.data);
          this.loading = false;
        }).catch(err => {
          this.loading = false;
          eventbus.$emit('showMessage', {
            title: "Couldn't filter contents!",
            err
          });
        });
    },
    updateFilterSelection (type, value) {
      this.$emit('updateFilterSelection', type, value);
    },
    deActivateSelect () {
      this.isHidden = true;
      this.maskedValue = this.selectedProject.substr(0, 1) + '***';
    },
    activateSelect () {
      this.isHidden = false;
      this.selectedProject = '';
    }
  },
  watch: {
    selectedProject (newValue) {
      this.updateFilterSelection('project', newValue);
    },
    selectedPic (newValue) {
      this.updateFilterSelection('pic', newValue);
    },
    selectedCommodity (newValue) {
      let commodityIdentifier = this.filterLists.commodities[newValue].id;
      this.updateFilterSelection('commodity', commodityIdentifier);
    },
    searchPartNumber (newValue, oldValue) {
      if (newValue.length > 3) {
        this.updateFilterSelection('partNumber', newValue);
      } else {
        this.updateFilterSelection('partNumber', '');
      }
    }
  }
};
</script>

<style scoped>
</style>
