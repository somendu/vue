<template>
  <table class="table table-sm" v-if="rows">
    <thead>
      <tr>
        <th v-for="(header, index) in visibleHeaders" :key="index">
          <span @click="setSort(header)">{{ header.title }}</span>
          <octicon name="arrow-down" v-if="sort.column === header.name &amp;&amp; !sort.descending"></octicon>
          <octicon name="arrow-up" v-if="sort.column === header.name &amp;&amp; sort.descending"></octicon>
        </th>
      </tr>
    </thead>

    <tbody>
      <tr v-for="(row, index) in rows" :key="index" :class="{ 'table-primary': row.selected }"  @click="selectRow(row, $event)">
        <td>{{ row.sapArticleNumber }}</td><!-- SAP article number -->
        <td v-if="showStatusColumn">{{ row.statusDisplay }}</td><!-- Approval status -->
        <td>{{ row.articleDescriptionNL }}</td><!-- Article description NL -->
        <td>{{ row.articleId }}</td><!-- Supplier article number -->
        <td>{{ row.supplierCode }}</td><!-- Supplier -->
        <td>{{ row.countryCode }}</td>
        <td>{{ row.netPriceSupplierOld | euro }}</td><!-- Net price supplier (old) -->
        <td>{{ row.netPriceSupplierNew | euro }}</td><!-- Net price supplier (new) -->
        <td>{{ row.netPriceSupplierChange | percentage }}</td><!-- Percentage change (new-old/old) -->
        <td>{{ row.grossPriceSupplierOld | euro }}</td><!-- Gross price supplier (old) -->
        <td>{{ row.grossPriceSupplierNew | euro }}</td><!-- Gross price supplier (new) -->
        <td>{{ row.grossPriceSupplierChange | percentage }}</td><!-- Percentage change (new-old/old) -->
        <td>{{ row.revaluationTotal | euro }}</td><!-- Total revaluation -->
        <td>{{ row.backOfficeMarginPctDiff | percentage }}</td><!-- Back-office margin % (difference) -->
        <td>{{ row.outletMarginPctDiff | percentage }}</td><!-- Outlet margin % (difference) -->
        <td>{{ row.goodsDescription }}</td><!-- Goederengroepomschrijving -->
        <td>{{ row.articleGroupDescription }}</td><!-- Artikelgroep omschrijving -->
        <td>{{ row.prodHierDescription }}</td><!-- Merk merklijnomschrijving -->
        <td v-if="allColumns">{{ row.popCodeNL }}</td><!-- Popcode NL -->
        <td>{{ row.mainSupplier }}</td><!-- Main supplier -->
        <td v-if="allColumns">{{ row.popCodeBE }}</td><!-- Popcode BE -->
        <td v-if="allColumns">{{ row.discountGroup }}</td><!-- Discount group -->
        <td>{{ row.discountGroupPercent | percentage }}</td><!-- Discount group percentage -->
        <td v-if="allColumns">{{ row.transferPrice | euro }}</td><!-- Transfer price -->
        <td v-if="allColumns">{{ row.outletDiscount }}</td><!-- Outlet discount -->
        <td v-if="allColumns">{{ row.outletMarkup }}</td><!-- Outlet markup -->
        <td v-if="allColumns">{{ row.pimArticleNumberMaster }}</td><!-- PIM article number master -->
        <td v-if="allColumns">{{ row.pimArticleNumberSupplier }}</td><!-- PIM article number supplier -->
        <td>{{ row.productManager }}</td><!-- Buyer / PM -->
        <td v-if="allColumns">{{ row.netPriceSupplierStartDateNew }}</td><!-- Net price supplier start date (new) -->
        <td v-if="allColumns">{{ row.netPriceSupplierStartDateOld }}</td><!-- Net price supplier start date (old) -->
        <td v-if="allColumns">{{ row.grossPriceSupplierStartDateNew }}</td><!-- Gross price supplier start date (new) -->
        <td v-if="allColumns">{{ row.grossPriceSupplierStartDateOld }}</td><!-- Gross price supplier start date (old) -->
        <td v-if="allColumns">{{ row.ownSalesPriceNew | euro }}</td><!-- Gross price – own (new) -->
        <td v-if="allColumns">{{ row.ownSalesPriceStartDateNew }}</td><!-- Gross price - own start date (new) -->
        <td v-if="allColumns">{{ row.ownSalesPriceOld | euro }}</td><!-- Gross price - own (old) -->
        <td v-if="allColumns">{{ row.ownSalesPriceStartDateOld }}</td><!-- Gross price - own start date (old) -->
        <td v-if="allColumns">{{ row.ownSalesPriceChange | percentage }}</td><!-- Percentage change (new-old/old) -->
        <td v-if="allColumns">{{ row.ownNetPriceNew | euro }}</td>
        <td v-if="allColumns">{{ row.ownNetPriceStartDateNew }}</td>
        <td v-if="allColumns">{{ row.ownNetPriceOld | euro }}</td>
        <td v-if="allColumns">{{ row.ownNetPriceStartDateOld }}</td>
        <td v-if="allColumns">{{ row.ownNetPriceChange | percentage }}</td>
        <td v-if="allColumns">{{ row.stockPositionCM }}</td><!-- Stock position CM (Ede + Oosterhout) -->
        <td v-if="allColumns">{{ row.stockPositionOutlet }}</td><!-- Stock position Outlets (verkooppunten) -->
        <td>{{ row.revaluationOutlets | euro }}</td><!-- Revaluation outlets -->
        <td>{{ row.revaluationDepots | euro }}</td><!-- Revaluation depots -->
        <td v-if="allColumns">{{ row.backOfficeMarginNew | euro }}</td><!-- Back-office margin (new) -->
        <td v-if="allColumns">{{ row.backOfficeMarginOld | euro }}</td><!-- Back-office margin (old) -->
        <td v-if="allColumns">{{ row.backOfficeMarginDiff | euro }}</td><!-- Back-office margin (difference) -->
        <td>{{ row.backOfficeMarginPctNew | percentage }}</td><!-- Back-office margin % (new) -->
        <td>{{ row.backOfficeMarginPctOld | percentage }}</td><!-- Back-office margin % (old) -->
        <td v-if="allColumns">{{ row.outletMarginNew | euro }}</td><!-- Outlet margin (new) -->
        <td v-if="allColumns">{{ row.outletMarginOld | euro }}</td><!-- Outlet margin (old) -->
        <td v-if="allColumns">{{ row.outletMarginDiff | euro }}</td><!-- Outlet margin (difference) -->
        <td>{{ row.outletMarginPctNew | percentage }}</td><!-- Outlet margin % (new) -->
        <td>{{ row.outletMarginPctOld | percentage }}</td><!-- Outlet margin % (old) -->
        <td v-if="allColumns">{{ row.goodsGroup }}</td><!-- Goods group (SAP goederengroep) -->
        <td v-if="allColumns">{{ row.articleGroup }}</td><!-- Article group (AX artikelgroep) -->
        <td v-if="allColumns">{{ row.prodHierarchy }}</td><!-- Product hierarchy (from Brand/brand line -->
        <td class="remarks-cell">
          <input type="text" class="form-control remarks" v-model="row.remarks" :class="{'saving':row.saving}"
                             @change="remarksChange(row)"
                             @blur="remarksUpdated(row)"></td><!-- Remark -->
      </tr>
    </tbody>
  </table>
</template>

<script>
import Octicon from 'vue-octicon/components/Octicon.vue';

const HEADERS = [
  { name: 'sapArticleNumber', title: 'SAP artikelnummer' },
  { name: 'status', title: 'Beoordelingsstatus', status: true },
  { name: 'articleDescriptionNL', title: 'Artikelomschrijving NL' },
  { name: 'articleId', title: 'Leveranciersartikelnummer' },
  { name: 'supplierCode', title: 'Leverancier' },
  { name: 'countryCode', title: 'Land' },
  { name: 'netPriceSupplierOld', title: 'Nettoprijs leverancier (oud)' },
  { name: 'netPriceSupplierNew', title: 'Nettoprijs leverancier (nieuw)' },
  { name: 'netPriceSupplierChange', title: 'Wijzigingspercentage' },
  { name: 'grossPriceSupplierOld', title: 'Brutoprijs leverancier (oud)' },
  { name: 'grossPriceSupplierNew', title: 'Brutoprijs leverancier (nieuw)' },
  { name: 'grossPriceSupplierChange', title: 'Wijzigingspercentage' },
  { name: 'revaluationTotal', title: 'Totale herwaardering' },
  { name: 'backOfficeMarginPctDiff', title: 'Back-office marge % (verschil)' },
  { name: 'outletMarginPctDiff', title: 'VKP marge % (verschil)' },
  { name: 'goodsDescription', title: 'Goederengroepomschrijving' },
  { name: 'articleGroupDescription', title: 'Artikelgroepomschrijving' },
  { name: 'prodHierDescription', title: 'Merk merklijnomschrijving' },
  { name: 'popCodeNL', title: 'Popcode NL', detail: true },
  { name: 'mainSupplier', title: 'Voorkeursleverancier' },
  { name: 'popCodeBE', title: 'Popcode BE', detail: true },
  { name: 'discountGroup', title: 'Kortingsgroep', detail: true },
  { name: 'discountGroupPercent', title: 'Kortingsgroepprecentage' },
  { name: 'transferPrice', title: 'Transferprijs', detail: true },
  { name: 'outletDiscount', title: 'VKP korting', detail: true },
  { name: 'outletMarkup', title: 'VKP markup', detail: true },
  { name: 'pimArticleNumberMaster', title: 'PIM artikelnummer master', detail: true },
  { name: 'pimArticleNumberSupplier', title: 'PIM artikelnummer leverancier', detail: true },
  { name: 'productManager', title: 'PM-er' },
  { name: 'netPriceSupplierStartDateNew', title: 'Nettoprijs leverancier startdatum (nieuw)', detail: true },
  { name: 'netPriceSupplierStartDateOld', title: 'Nettoprijs leverancier startdatum (oud)', detail: true },
  { name: 'grossPriceSupplierStartDateNew', title: 'Brutoprijs leverancier startdatum (nieuw)', detail: true },
  { name: 'grossPriceSupplierStartDateOld', title: 'Brutoprijs leverancier startdatum (oud)', detail: true },
  { name: 'ownSalesPriceNew', title: 'Brutoprijs eigen (nieuw)', detail: true },
  { name: 'ownSalesPriceStartDateNew', title: 'Brutoprijs eigen startdatum (nieuw)', detail: true },
  { name: 'ownSalesPriceOld', title: 'Brutoprijs eigen (oud)', detail: true },
  { name: 'ownSalesPriceStartDateOld', title: 'Brutoprijs eigen startdatum (oud)', detail: true },
  { name: 'ownSalesPriceChange', title: 'Wijzigingspercentage', detail: true },
  { name: 'ownNetPriceNew', title: 'Nettoprijs eigen (nieuw)', detail: true },
  { name: 'ownNetPriceStartDateNew', title: 'Nettoprijs eigen startdatum (nieuw)', detail: true },
  { name: 'ownNetPriceOld', title: 'Nettoprijs eigen (oud)', detail: true },
  { name: 'ownNetPriceStartDateOld', title: 'Nettoprijs eigen startdatum (oud)', detail: true },
  { name: 'ownNetPriceChange', title: 'Wijzigingspercentage', detail: true },
  { name: 'stockPositionCM', title: 'Voorraad CM', detail: true },
  { name: 'stockPositionOutlet', title: 'Voorraad VKP\'s', detail: true },
  { name: 'revaluationOutlets', title: 'Herwaardering VKP\'s' },
  { name: 'revaluationDepots', title: 'Herwaardering Depots' },
  { name: 'backOfficeMarginNew', title: 'Back-office marge (nieuw)', detail: true },
  { name: 'backOfficeMarginOld', title: 'Back-office marge (oud)', detail: true },
  { name: 'backOfficeMarginDiff', title: 'Back-office marge verschil', detail: true },
  { name: 'backOfficeMarginPctNew', title: 'Back-office marge % (nieuw)' },
  { name: 'backOfficeMarginPctOld', title: 'Back-office marge % (oud)' },
  { name: 'outletMarginNew', title: 'VKP marge (nieuw)', detail: true },
  { name: 'outletMarginOld', title: 'VKP marge (oud)', detail: true },
  { name: 'outletMarginDiff', title: 'VKP marge wijziging', detail: true },
  { name: 'outletMarginPctNew', title: 'VKP marge % (nieuw)' },
  { name: 'outletMarginPctOld', title: 'VKP marge % (oud)' },
  { name: 'goodsGroup', title: 'SAP goederengroep', detail: true },
  { name: 'articleGroup', title: 'AX artikelgroep', detail: true },
  { name: 'prodHierarchy', title: 'Merk/merklijn', detail: true },
  { name: 'remarks', title: 'Opmerking' }
];

export default {
  name: 'SupplierArticleRows',

  components: {
            Octicon
  },

  props: [
    'sort',
    'rows',
    'allColumns',
    'showStatusColumn'
  ],

  computed: {
    visibleHeaders () {
      return HEADERS.filter(header => {
        if (header.status) return this.showStatusColumn;
        if (header.detail) return this.allColumns;
        return true;
      });
    }
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
      // prevent selection toggle for remarks field clicks
      if (this.isInRemarkCell(evt.target)) return;

      row.selected = !row.selected;
      this.$emit('selectionChanged');
    },

    remarksChange (row) {
      row._remarksChanged = true;
    },

    remarksUpdated (row) {
      if (row._remarksChanged) {
        this.$emit('remarksUpdated', row);
        row._remarksChanged = false;
      }
    },

    isInRemarkCell (node) {
      let current = node;

      while (current) {
        // the input field itself
        if (current.nodeName === 'INPUT' && current.className === 'remarks') return true;
        // the cell that holds the input field
        if (current.nodeName === 'TD' && current.className === 'remarks-cell') return true;
        // don't dig further than the table row
        if (current.nodeName === 'TR') break;
        current = current.parentElement;
      }

      return false;
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
        return value.toFixed(2) + '%';
      }
      return '';
    }
  }
};
</script>

<style scoped>
.remarks {
  width: 20em;
}
.saving {
  padding-right: 20px;
  background-image: url('../../assets/loading_small.gif');
  background-position: right center;
  background-repeat: no-repeat;
}
</style>
