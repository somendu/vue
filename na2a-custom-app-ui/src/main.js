import './assets/bootstrap-custom.scss';
import './assets/applicability.css';
import Vue from 'vue';
import BootstrapVue from 'bootstrap-vue';
import 'vue-octicon/icons';
import App from './App.vue';
import router from './router';
import store from './store';
import VToolTip from 'v-tooltip';
import './lib/axios-token';

Vue.config.productionTip = false;
Vue.use(BootstrapVue);
Vue.use(VToolTip);

new Vue({
  render: h => h(App),
  store,
  router
}).$mount('#app');
