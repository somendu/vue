<template>
  <div id="demo">
        <div v-show="active">Show</div>
        <div @mouseover="mouseOver">Hover over me!</div>
    </div>
</template>

<script>
export default {
  name: 'Hover',

	data () {
    return {
      active : false
    }
  },

  methods:  {
    mouseOver: function() {
         this.active = !this.active;  
      }
    }
    
}
 
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
