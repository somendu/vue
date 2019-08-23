<template>
  <div id="app">

    <Inventory/>
    
  </div>
</template>

<script>

//import Hover from './components/Hover.vue';
import Inventory from './components/Inventory.vue';

export default {
  name: 'App',

  components: {
  
  Inventory,
  
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
