import Vue from 'vue';
import Router from 'vue-router';
import SupplierArticles from '@/components/SupplierArticles';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/changes'
    },
    {
      path: '/changes',
      component: SupplierArticles,
      props: { history: false, financereport: false }
    },
    {
      path: '/history',
      component: SupplierArticles,
      props: { history: true, financereport: false }
    },
    {
      path: '/financereport',
      component: SupplierArticles,
      props: { history: false, financereport: true }
    }
  ]
});
