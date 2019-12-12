import Vue from 'vue';

const state = {
  katashiki: {
    enabled: false,
    multiSelectionCategory: null,
    categories: {}
  }
};

const mutations = {
  setKatashiki (state, value) {
    Vue.set(state, 'katashiki', value);
  }
};

const actions = {
  setKatashiki (context, value) {
    context.commit('setKatashiki', value);
  }
};

export default {
  state,
  mutations,
  actions
};
