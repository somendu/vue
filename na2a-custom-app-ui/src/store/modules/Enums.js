import Vue from 'vue';
import axios from 'axios';
import eventbus from '@/eventbus';

const state = {
  countries: []
};

const mutations = {
  setCountries (state, reasons) {
    Vue.set(state, 'countries', reasons);
  }
};

const actions = {
  loadCountries (context) {
    axios.get('/api/all/countries')
      .then(res => {
        context.commit('setCountries', res.data);
      })
      .catch(err => {
        eventbus.$emit('showMessage', { title: 'Couldn\'t load the countries', err });
      });
  }
};

export default {
  state,
  mutations,
  actions
};
