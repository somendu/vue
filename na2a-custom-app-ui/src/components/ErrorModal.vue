<template>
  <b-modal ref="modal"
           :title="title"
           :ok-only="true"
           header-bg-variant="danger"
           header-text-variant="light">
    <p>{{ description }}</p>
    <p v-if="needsRelogin"><a :href="loginUrl" target="_blank">Click here to login</a></p>
  </b-modal>
</template>

<script>
import eventbus from '@/eventbus';

export default {
  data () {
    return {
      show: false,
      title: null,
      description: null,
      needsRelogin: false,
      loginUrl: `${window.pimConfig.externalUrl}/pim/login`
    };
  },

  created () {
    eventbus.$on('showMessage', this.appear);
  },

  destroyed () {
    eventbus.$off('showMessage', this.appear);
  },

  methods: {
    appear ({ title, description, err }) {
      this.title = title;

      if (description) {
        this.description = description;
      } else if (err) {
        this.description = (err.response && err.response.data) || err.toString();
        this.needsRelogin = !!err.needsRelogin;
      }

      this.$refs.modal.show();
    }
  }
};
</script>
