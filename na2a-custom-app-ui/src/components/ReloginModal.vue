<template>
  <b-modal ref="modal"
           title="Please login"
           :ok-only="true"
           @ok="dismiss"
           header-bg-variant="primary"
           header-text-variant="light">
    <p>Your session appears to have expired</p>
    <p><a :href="loginUrl" target="_blank">Click here to login</a></p>
  </b-modal>
</template>

<script>
import eventbus from '@/eventbus';

export default {
  data () {
    return {
      loginUrl: `${window.pimConfig.externalUrl}/pim/webaccess/relogin`
    };
  },

  created () {
    eventbus.$on('relogin.show', this.appear);
    eventbus.$on('relogin.hide', this.dismiss);
  },

  destroyed () {
    eventbus.$off('relogin.show', this.appear);
    eventbus.$off('relogin.hide', this.dismiss);
  },

  methods: {
    appear () {
      this.$refs.modal.show();
    },

    dismiss () {
      this.$refs.modal.hide();
    }
  }
};
</script>
