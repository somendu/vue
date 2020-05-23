<template>
    <div class="sortable-header-container">
        <div class="header">
            {{header}}
        </div>
        <div @click="sort('asc')">
            <octicon class="sortable" name="chevron-up"></octicon>
        </div>
        <div @click="sort('desc')">
            <octicon class="sortable" name="chevron-down"></octicon>
        </div>
    </div>
</template>
<script>
import Octicon from 'vue-octicon/components/Octicon';
export default {
  name: 'SortableHeader',
  components: {
    Octicon
  },

  props: {
    header: {
      type: String,
      required: true
    },
    sortableItem: {
      type: String,
      required: true
    }
  },

  methods: {
    sort (direction) {
      this.$emit('sort', direction, this.sortableItem);
    }
  }
};
</script>
<style scoped>
.sortable-header-container {
    display: flex;
    flex-direction: row;
    padding: 0px;
    margin: 0px;
}

.header {
    margin-right: 5px;
}

.sortable {
    cursor: pointer;
}
</style>
