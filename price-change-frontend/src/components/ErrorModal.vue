<template>
  <b-modal v-model="show" :title="title" :ok-only="true" header-bg-variant="danger" header-text-variant="light">
    <p v-if="description">{{ description }}</p>
    <p v-if="responseText">Response text: {{ responseText }}</p>
    <ul v-if="errors">
      <li v-for="(error, index) in errors" :key="index">{{ error }}</li>
    </ul>
  </b-modal>
</template>

<script>
import { EventBus } from '@/lib/eventbus';

export default {
  name: 'App',

  data () {
    return {
      show: false,
      title: null,
      description: null,
      responseText: null,
      errors: null
    };
  },

  created () {
    EventBus.$on('error', this.open);
  },

  destroyed () {
    EventBus.$off('error', this.open);
  },

  methods: {
    open (args) {
      this.title = args.title || 'Error occurred';
      this.description = args.description;
      this.show = true;

      this.responseText = args.err && (args.err.responseText || args.err.toString());
      this.$set(this, 'errors', args.err && args.err.errors);
    }
  }
};
</script>
