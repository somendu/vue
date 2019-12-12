<template>
    <div id="task-application-entries">
        <div id="task-application-table-section" class="scrollable">
            <b-form-checkbox
              id="select-all"
              :checked="allSelected"
              @change="selectAll">Select/De-select all</b-form-checkbox>
            <table id="task-application-table" class="table application-table table-sm table-striped">
                <thead>
                    <tr id="task-application-headerRow">
                        <th class="row-checkbox"></th>
                        <th class="row-medium">Commodity</th>
                        <th class="row-large">Accessory Product</th>
                        <th class="row-large">Application</th>
                        <th class="row-medium">Indicative Volume</th>
                        <th class="row-medium">Price (euro)</th>
                        <th class="row-medium">PPO Code</th>
                        <th class="row-small">Status</th>
                    </tr>
                </thead>

                <tbody>
                    <tr>
                        <td></td>
                        <td>
                            <div class="input-group input-group-sm sm-3">
                                <input
                                  id="tsk-application-filterCommodity"
                                  type="text"
                                  class="form-control form-control-sm"
                                  placeholder="Commodity"
                                  aria-label="Commodity"
                                  aria-describedby="clear-commodity"
                                  v-model="filterCommodity" />
                                <div class="input-group-prepend">
                                    <b-button variant="info" id="clear-partnumber" class="form-control form-control-sm" @click="filterCommodity = ''">X</b-button>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="input-group input-group-sm sm-3">
                                <input
                                  id="tsk-application-filterProduct"
                                  type="text"
                                  class="form-control form-control-sm"
                                  placeholder="Accessory Product"
                                  aria-label="filterProduct"
                                  aria-describedby="clear-product"
                                  v-model="filterProduct" />
                                <div class="input-group-prepend">
                                    <b-button variant="info" id="clear-partnumber" class="form-control form-control-sm" @click="filterProduct = ''">X</b-button>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="input-group input-group-sm sm-3">
                                <input
                                  id="tsk-application-filterApplication"
                                  type="text"
                                  class="form-control form-control-sm"
                                  placeholder="Application"
                                  aria-label="filterApplication"
                                  aria-describedby="clear-application"
                                  v-model="filterApplication" />
                                <div class="input-group-prepend">
                                    <b-button variant="info" id="clear-partnumber" class="form-control form-control-sm" @click="filterApplication = ''">X</b-button>
                                </div>
                            </div>
                        </td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr v-for="(application, idx) of filteredApplications" :key="idx" >
                      <td>
                        <b-form-checkbox v-model="application.selected" style="height: 10px;">
                        </b-form-checkbox>
                      </td>
                      <td>{{ application.taskApplication.commodity.value }}</td>
                      <td v-tooltip :title="application.taskApplication.product.name">
                        {{ application.taskApplication.product.name }}
                      </td>
                      <td v-tooltip :title="application.taskApplication.name">
                        {{ application.taskApplication.name }}
                      </td>
                      <td><input class="cell-input" type="text" v-model="application.volume" :disabled="!application.selected"/></td>
                      <td><input class="cell-input" type="text" v-model="application.price" :disabled="!application.selected"/></td>
                      <td>{{ application.ppoCode }}</td>
                      <td>{{ application.status}}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
export default {
  props: {
    applications: {
      type: Array,
      required: false
    }
  },

  data () {
    return {
      filterCommodity: '',
      filterProduct: '',
      filterApplication: ''
    };
  },

  methods: {
    selectAll () {
      let newStatus = !this.allSelected;
      this.applications.forEach(application => { application.selected = newStatus; });
    }
  },

  computed: {
    allSelected () {
      return !this.applications.find(a => !a.selected);
    },

    filteredApplications () {
      this.applications.forEach(application => { application.selected = false; });

      let list = this.applications;

      if (this.filterCommodity === '' && this.filterProduct === '' && this.filterApplication === '') {
        return this.applications;
      }

      if (this.filterCommodity !== '') {
        list = list.filter(a => a.taskApplication.commodity.value.toLowerCase().includes(this.filterCommodity.toLowerCase()));
      }

      if (this.filterProduct !== '') {
        list = list.filter(a => a.taskApplication.product.name.toLowerCase().includes(this.filterProduct.toLowerCase()));
      }

      if (this.filterApplication !== '') {
        list = list.filter(a => a.taskApplication.name.toLowerCase().includes(this.filterApplication.toLowerCase()));
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

.application-table {
  table-layout: fixed;
  width: 100%;
  white-space: nowrap;

}
.application-table td {
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
