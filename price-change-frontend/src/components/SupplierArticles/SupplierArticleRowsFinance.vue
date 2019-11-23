<template>
  <table class="table table-sm" v-if="rows">
    <thead>
      <tr>
        <th v-for="(financeheader, index) in financeheaders" :key="index">
          <span @click="setSort(financeheader)">{{ financeheader.title }}</span>
          <octicon name="arrow-down" v-if="sort.column === financeheader.name &amp;&amp; !sort.descending"></octicon>
          <octicon name="arrow-up" v-if="sort.column === financeheader.name &amp;&amp; sort.descending"></octicon>
        </th>

      </tr>
    </thead>

    <tbody>
      <tr v-for="(row, index) in rows" :key="index" :class="{ 'table-primary': row.selected }" @click="selectRow(row, $event)">
        <td>{{ row.sapArticleNumber }}</td><!-- SAP article number -->
        <td>{{ row.articleId }}</td><!-- Supplier article number -->
        <td>{{ row.goodsGroup }}</td><!-- Goods group (SAP goederengroep) -->
        <td>{{ row.supplierCode }}</td><!-- Supplier -->
        <td>{{ row.prodHierDescription }}</td><!-- Merk merklijnomschrijving -->
        <td>{{ row.revaluationOutlets | euro }}</td><!-- Revaluation outlets -->
        <td>{{ row.revaluationDepots | euro }}</td><!-- Revaluation depots -->
        <td>{{ row.stockPositionCM }}</td><!-- Stock position CM (Ede + Oosterhout) -->
      </tr>
    </tbody>
  </table>
</template>

<script>
import Octicon from 'vue-octicon/components/Octicon.vue';

const FINANCEHEADERS = [
  { name: 'sapArticleNumber', title: 'SAP artikelnummer' },
  { name: 'articleId', title: 'Leverancier artikelnummer' },
  { name: 'goodsGroup', title: 'SAP goederengroep' },
  { name: 'supplierCode', title: 'Leverancier' },
  { name: 'prodHierDescription', title: 'Merk merklijnomschrijving' },
  { name: 'revaluationOutlets', title: 'Herwaardering VKP\'s' },
  { name: 'revaluationDepots', title: 'Herwaardering Depots' },
  { name: 'stockPositionCM', title: 'Voorraad CM' }
];

export default {
  name: 'SupplierArticleRowsFinance',

  components: {
    Octicon
  },

  props: [
    'sort',
    'rows',
    'financeColumns'
  ],

  data () {
    return {
      financeheaders: FINANCEHEADERS
    };
  },

  methods: {
    setSort (header) {
      if (this.sort.column === header.name) {
        if (!this.sort.descending) {
          this.sort.descending = true;
        } else {
          this.sort.column = null;
        }
      } else {
        this.sort.column = header.name;
        this.sort.descending = false;
      }

      this.$emit('sortChanged', this.sort);
    },

    selectRow (row, evt) {
      row.selected = !row.selected;
      this.$emit('selectionChanged');
    }
  },

  filters: {
    euro (value) {
      if (typeof value === 'number') {
        return '\u20AC ' + value.toFixed(2);
      }
      return '';
    },

    percentage (value) {
      if (typeof value === 'number') {
        return Math.round(value * 100).toString() + ' %';
      }
      return '';
    }
  }
};
</script>

<style scoped>
.remark {
  width: 20em;
}
.saving {
  padding-right: 20px;
  background-image: url('../../assets/loading_small.gif');
  background-position: right center;
  background-repeat: no-repeat;
}
</style>
