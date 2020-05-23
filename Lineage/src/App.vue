<template>
  <div id="app">

    <Lineage/>
    
  </div>
</template>

<script>

//import Hover from './components/Hover.vue';
import Lineage from './components/Lineage.vue';
// import ImageView from './components/ImageView.vue';

export default {
  name: 'App',

  components: {
  
  Lineage
  
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
