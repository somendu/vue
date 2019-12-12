<template>
  <b-navbar toggleable="sm" type="dark" variant="dark">
    <b-navbar-brand href="#">
      <img
        id="navbar-logo"
        src="https://upload.wikimedia.org/wikipedia/fr/a/a1/Toyota_logo2.svg"
        alt="Toyota"
        height="40px"
        width="40px"
      >
    </b-navbar-brand>
    <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>
    <b-navbar-brand id="navbar-title">{{title}}</b-navbar-brand>

    <b-navbar-nav v-if="title === 'Application Management'" class="ml-auto">
      <router-link :to="`/${this.type}/${this.stringIds}/partlinking`" >
        <b-button id="navbar-goToParts" size="sm" class="my-2 my-sm-0" type="text">Go To Parts</b-button>
      </router-link>

    </b-navbar-nav>

  </b-navbar>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      required: true
    },
    ids: {
      type: Array,
      required: false
    },
    type: {
      type: String,
      required: false
    }
  },

  computed: {
    stringIds () {
      return this.ids.join(',');
    }
  }
};
</script>

<style scoped>
</style>
