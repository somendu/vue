<template>
  <table class="lineage" border="1">

  

  <tr>

  <img v-for="(item,index) in images" :key="index" :src="item.url" @click="imageView(index)">
  
  </tr>

 

  </table>
</template>

<script>

 

export default {
  name: 'imageViewer',

  components: {
    },


  
  data () {
    return {
      
      visible: false,
      images : [
                {
                    name : "image1",
                    url : "/static/images/blog-4.jpg"
                },
                {
                    name : "image2",
                    url : "/static/images/blog-5.jpg"
                }
            ]
    };
  },

  computed: {
     
  },

  created () {
      this.$imageViewer.images(this.images);
  },
   
  methods: {
            imageView(index) {
                this.$imageViewer.index(index);
                this.$imageViewer.show();
            }
        }
  
};
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
