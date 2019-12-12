import Vue from 'vue';
import axios from 'axios';
import eventbus from '@/eventbus';

const state = {
  cancelledReasons: []
};

const mutations = {
  setCancelledReasons (state, reasons) {
    Vue.set(state, 'cancelledReasons', reasons);
  }
};

const actions = {
  loadCancelledReasons (context) {
    axios.get('/api/cancelled-reasons')
      .then(res => {
        context.commit('setCancelledReasons', res.data);
      })
      .catch(err => {
        eventbus.$emit('showMessage', { title: 'Couldn\'t load reasons for cancelling', err });
      });
  }
};

export default {
  state,
  mutations,
  actions
};
