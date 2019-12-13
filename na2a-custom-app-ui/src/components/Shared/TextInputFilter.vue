<template>
    <div class="input-group input-group-sm sm-3">
        <input
            :id="'text-input-filter-' + placeholder.toLowerCase().replace(/\s+/g, '')"
            type="text"
            class="form-control form-control-sm"
            :placeholder="placeholder"
            :aria-label="placeholder"
            :aria-describedby="'clear-' + placeholder.toLowerCase().replace(/\s+/g, '')"
            v-model="filterValue"
            @input="update" />
        <div class="input-group-prepend">
            <b-button variant="info" :id="'clear-' + placeholder.toLowerCase().replace(/\s+/g, '')" class="form-control form-control-sm" @click="clear">X</b-button>
        </div>
    </div>
</template>
<script>
export default {
  props: {
    filterType: {
      type: String,
      required: true
    },
    placeholder: {
      type: String,
      required: true
    }
  },

  data () {
    return {
      filterValue: ''
    };
  },

  methods: {
    update () {
      this.$emit('update', this.filterType, this.filterValue);
    },

    clear () {
      this.filterValue = '';
      this.update();
    }
  }
};
</script>
<style scoped>
</style>
