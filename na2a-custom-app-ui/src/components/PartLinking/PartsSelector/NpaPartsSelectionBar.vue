<template>
  <div id="npa-parts-selection-bar">
    <table id="npa-parts-selection-bar-table" class="table table-sm">
      <tbody>
        <tr>
          <td>
            <input type="text" class="form-control form-control-sm" placeholder="Search part number" v-model="searchPartNumber"/>
          </td>
          <td>
            <div id="lp-buttonView">
              <b-button-group size="sm">
                <b-button :id="idText" @click="npaSearch" :disabled="npaSearching || hasPartsSelected" variant="outline-primary">
                  <octicon name="sync" :spin="npaSearching"></octicon> Search
                </b-button>
              </b-button-group>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import eventbus from '@/eventbus';
import Octicon from 'vue-octicon/components/Octicon';

export default {
  components: {
    Octicon
  },

  props: {
    npaSearching: {
      type: Boolean
    },
    hasPartsSelected: {
      type: Boolean
    }
  },

  data () {
    return {
      searchPartNumber: ''
    };
  },

  created () {
  },

  methods: {
    npaSearch () {
      if (this.searchPartNumber.length > 3) {
        this.$emit('npaSearch', this.searchPartNumber);
      } else {
        eventbus.$emit('showMessage', {
          title: "Can't search",
          err: 'Minimum of 4 characters must be entered to perform a search.'
        });
      }
    }
  },

  watch: {
  }
};
</script>

<style scoped>
</style>
