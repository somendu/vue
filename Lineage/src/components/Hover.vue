<template>
  <div class="Hover">
      <span v-bind:title="message">
    Hover your mouse over me for a few seconds
    to see my dynamically bound title!
  </span>
  </div>
</template>

<script>
export default {
  name: 'Hover',

	data () {
	
		'You loaded this page on ' + new Date().toLocaleString();
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
