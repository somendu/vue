<template>
  <a :id="'cl-collapse-link-' + label"
     class="collapse-link"
     @click="toggle"
     :aria-expanded="value ? 'true' : 'false'">
    <octicon name="chevron-down" v-if="value"></octicon>
    <octicon name="chevron-right" v-if="!value"></octicon>
    {{ label }}
  </a>
</template>

<script>
import Octicon from 'vue-octicon/components/Octicon';

export default {
  components: {
    Octicon
  },

  props: {
    value: {
      type: Boolean,
      required: true
    },

    label: {
      type: String,
      required: true
    },

    enabled: {
      type: Boolean,
      default: true,
      required: false
    }
  },

  methods: {
    toggle () {
      if (this.enabled) {
        this.$emit('input', !this.value);
      }
    }
  }
};
</script>

<style scoped>
.collapse-link {
  display: block;
  cursor: pointer;
}
</style>
