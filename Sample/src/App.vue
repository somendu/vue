<template>
  <div id="app">

    <HelloWorld msg="Here you go"/>
    <Inventory/>
  </div>
</template>

<script>
// import HelloWorld from './components/HelloWorld.vue';
import Inventory from './components/Inventory.vue';

export default {
  name: 'App',
  components: {
    // HelloWorld,
    Inventory
  }
};
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
