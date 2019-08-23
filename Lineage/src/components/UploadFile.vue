<template>
  <div class="upload-file">
    <div class="large-12 medium-12 small-12 cell">
      <label>File
        <input type="file" id="file" ref="file" v-on:change="handleFileUpload()"/>
      </label>
        <button v-on:click="submitFile()">Submit</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'UploadFile',

	data(){
  return {
    file: ''
  }
	},

  methods: {

    submitFile(){
         
      let formData = new FormData();
    formData.append('file', this.file);
    
     
     //Method for uploading file 
     axios
        .post('/api/uploadFile', formData);
      
      alert ("Submit");
      

    
  },

  handleFileUpload(){
		this.file = this.$refs.file.files[0];
  }
  }
  }
</script>