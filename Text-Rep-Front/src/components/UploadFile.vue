<template>
  <div class="upload-file">
    <div class="large-12 medium-12 small-12 cell">
      <label>File
        <input type="file" id="file" ref="file" v-on:change="handleFileUpload()"/>
      </label>
        <!--<button v-on:click="submitFile()">Submit</button>-->

      <label>Search Text</label>
      <input type="text" v-model="searchText" placeholder="Search Text"/>

      <label>Replace Text</label>
      <input type="text" v-model="replaceText" placeholder="Replace Text"/>

      <button v-on:click="replaceAndDownload()">Replace and Download</button>



    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'UploadFile',

	data(){
  return {
    file: '',
    searchText: '',
    replaceText: ''
  }
	},

  methods: {

    replaceAndDownload(){
         
      let formData = new FormData();
    formData.append('file', this.file);
    formData.append('searchText',this.searchText);
    formData.append('replaceText',this.replaceText);
    
     
     //Method for uploading file 
    axios
        .post('/api/uploadFile', formData);
      
    //alert ("Submit");
    
    window.location.reload();

    //TODO Append all data 
    
  },

  

  handleFileUpload(){
		this.file = this.$refs.file.files[0];
  }
  }
  }
</script>