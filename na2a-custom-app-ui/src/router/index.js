import Vue from 'vue';
import Router from 'vue-router';
import Applicability from '@/components/Applicability';
import PartLinking from '@/components/PartLinking';
import Localisation from '@/components/Localisation';
import WltpMassUpdate from '@/components/WltpMassUpdate';
import Task from '@/components/Task';
import ErrorPage from '@/components/ErrorPage';

Vue.use(Router);

function splitIds (joinedIds) {
  const ids = [];

  if (joinedIds != null && joinedIds !== '') {
    const values = joinedIds.split(',');
    for (let value of values) {
      if (value !== '') ids.push(value);
    }
  }

  return ids;
}

export default new Router({
  routes: [{
    path: '/product/:productNumbers/application',
    component: Applicability,
    props: route => ({
      ids: splitIds(route.params.productNumbers),
      url: '/api/products?productNumbers=',
      type: 'product'
    })
  }, {
    path: '/variant/:variantIds/application',
    component: Applicability,
    props: route => ({
      ids: splitIds(route.params.variantIds),
      url: '/api/products/variants?variantIds=',
      type: 'variant'
    })
  }, {
    path: '/localisation/:organisation/:modelId',
    component: Localisation,
    props: route => ({
      organisation: route.params.organisation,
      modelId: route.params.modelId,
      url: '/api/products?productNumbers='
    })
  }, {
    path: '/product/:productNumbers/partlinking',
    component: PartLinking,
    props: route => ({
      ids: splitIds(route.params.productNumbers),
      url: '/api/partlinking/products?ids='
    })
  }, {
    path: '/variant/:variantIds/partlinking/',
    component: PartLinking,
    props: route => ({
      ids: splitIds(route.params.variantIds),
      url: '/api/partlinking/variants?ids='
    })
  }, {
    path: '/task/:id',
    component: Task,
    props: route => ({
      id: route.params.id
    })
  }, {
    path: '/product/:productNumbers/massupdate/wltp',
    component: WltpMassUpdate,
    props: route => ({
      ids: splitIds(route.params.productNumbers),
      url: '/api/massupdate/products?ids='
    })
  }, {
    path: '/variant/:variantIds/massupdate/wltp',
    component: WltpMassUpdate,
    props: route => ({
      ids: splitIds(route.params.variantIds),
      url: '/api/massupdate/variants?ids='
    })
  }, {
    path: '/404',
    component: ErrorPage
  }, {
    path: '*',
    redirect: '/404'
  }]
});
