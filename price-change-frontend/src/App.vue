<template>
  <div id="app">
    <b-navbar toggleable="sm" type="dark" variant="dark">
      <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>
      <b-navbar-brand to="/">Supplier Prices</b-navbar-brand>
    </b-navbar>

    <main role="main" class="container-fluid">
      <router-view/>
    </main>

    <error-modal></error-modal>
  </div>
</template>

<script>
import ErrorModal from '@/components/ErrorModal.vue';

export default {
  name: 'App',

  components: {
    ErrorModal
  }
};
</script>

<style lang="scss">
$spacer: 0.5rem;
$grid-gutter-width: 15px;
$font-size-base: 0.7rem;

@import 'bootstrap/scss/bootstrap.scss';
</style>
