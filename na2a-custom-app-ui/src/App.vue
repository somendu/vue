<template>
  <div id="app">
    <router-view class="na2a-custom-app-main"></router-view>
    <error-modal></error-modal>
    <relogin-modal></relogin-modal>
  </div>
</template>

<script>
import ErrorModal from './components/ErrorModal';
import ReloginModal from './components/ReloginModal';

export default {
  components: {
    ErrorModal,
    ReloginModal
  }
};
</script>

<style scoped>
#app {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.na2a-custom-app-main {
  /* let this take all remaining space, under the header */
  flex: 1;
  /* flex: 0 1 auto; */
}
</style>
