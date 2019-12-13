<template>
    <div id="task-parts-view">
        <div id="task-parts-table-section" class="scrollable">
            <table id="task-parts-table" class="table parts-table table-sm table-striped">
                <thead>
                    <th>Part number</th>
                    <th>Part name</th>
                    <th>Colour/Material</th>
                    <th>Type</th>
                    <th>Quantity</th>
                </thead>
                <tbody>
                    <tr>
                        <td>
                            <div class="input-group input-group-sm sm-3">
                                <input
                                  id="tsk-parts-filterPartNumber"
                                  type="text"
                                  class="form-control form-control-sm"
                                  placeholder="Part number"
                                  aria-label="partNumber"
                                  aria-describedby="clear-partNumber"
                                  v-model="filteredPartNumber" />
                                <div class="input-group-prepend">
                                    <b-button variant="info" id="clear-partnumber" class="form-control form-control-sm" @click="filteredPartNumber = ''">X</b-button>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="input-group input-group-sm sm-3">
                                <input
                                  id="tsk-parts-filterPartName"
                                  type="text"
                                  class="form-control form-control-sm"
                                  placeholder="Part name"
                                  aria-label="partName"
                                  aria-describedby="clear-partName"
                                  v-model="filteredPartName" />
                                <div class="input-group-prepend">
                                    <b-button variant="info" id="clear-partname" class="form-control form-control-sm" @click="filteredPartName = ''">X</b-button>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="input-group input-group-sm sm-3">
                                <input
                                  id="tsk-parts-filterCharacteristics"
                                  type="text"
                                  class="form-control form-control-sm"
                                  placeholder="Characteristics"
                                  aria-label="characteristics"
                                  aria-describedby="clear-characteristics"
                                  v-model="filteredCharacteristics" />
                                <div class="input-group-prepend">
                                    <b-button variant="info" id="clear-partname" class="form-control form-control-sm" @click="filteredCharacteristics = ''">X</b-button>
                                </div>
                            </div>
                        </td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr v-for="(part, idx) of getParts" :key="idx">
                        <td>{{ part.partNumber }}</td>
                        <td>{{ part.partName }}</td>
                        <td>{{ part.characteristics }}</td>
                        <td>{{ getPartTypeName( part.type ) }} </td>
                        <td>{{ part.quantity }}</td>
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
    details: Array
  },

  data () {
    return {
      filteredPartNumber: '',
      filteredPartName: '',
      filteredCharacteristics: ''
    };
  },

  methods: {
    getPartTypeName (type) {
      return partTypeName(type);
    }
  },

  computed: {
    getParts () {
      let parts = [];
      this.details.forEach(element => {
        parts = parts.concat(element.taskApplication.parts);
      });

      if (this.filteredPartNumber !== '') {
        parts = parts.filter(part => part.partNumber.toLowerCase().includes(this.filteredPartNumber.toLowerCase()));
      }

      if (this.filteredPartName !== '') {
        parts = parts.filter(part => part.partName.toLowerCase().includes(this.filteredPartName.toLowerCase()));
      }

      if (this.filteredCharacteristics !== '') {
        parts = parts.filter(part => part.characteristics.toLowerCase().includes(this.filteredCharacteristics.toLowerCase()));
      }

      return parts;
    }
  }
};
</script>
<style scoped>
.scrollable {
  overflow-y: auto;
}

.parts-table {
  table-layout: fixed;
  width: 100%;
  white-space: nowrap;

}
.parts-table td {
  white-space: wrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.row-checkbox {
  width: 50px;
}

.row-small {
  width: 10%;
}

.row-medium {
  width: 20%
}

.row-large {
  width: 25%
}
</style>
