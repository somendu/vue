<template>
  <div id="app">

    <ReplaceText/>
    
  </div>
</template>

<script>


import ReplaceText from './components/ReplaceText.vue';


export default {
  name: 'App',

  components: {
  
  ReplaceText
  
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
