<template>
  <table class="inventory" border="1">

  <!--  <tr
      border="2"
      align="center">
      <td
        v-for="product in products"
        :key="product">
        <input
          v-model.number="product.quantity"
          type="number">
        for {{ product.name }}
        <span v-if="product.quantity == 0">
          - OUT OF STOCK
        </span>
        <button @click="product.quantity += 1">
          Add
        </button>
      </td>

    </tr>

    
    <h2> Total Inventory : {{ totalProducts }} </h2> -->

  <tr>
 

  <upload-file
              class="upload-file"
              v-model="UploadFile"
              @submitFile="submitFile">
            </upload-file>

  </tr>

 

  </table>
</template>

<script>

import UploadFile from './UploadFile';

export default {
  name: 'Inventory',

  components: {
    UploadFile
  },


  data () {
    return {
      products: ['Boots', 'Socks', 'Tie'],
      messages: ['hello', 'vue', 'js'],
      shoppingItems: [
        {name: 'apple', price: '10'},
        {name: 'orange', price: '12'}]
    };
  },

  computed: {
    totalProducts () {
      return this.products.reduce((sum, product) => {
        return sum + product.quantity;
      }, 0);
    }
  },

  created () {
    fetch('https://api.myjson.com/bins/74l63')
      .then(response => response.json())
      .then(json => {
        this.products = json.products;
      });
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
