<template>
  <b-form inline @submit.prevent="submit">
    <b-form-select class="mb-2 mr-sm-2 mb-sm-0" v-model="value.status" :options="options.status" v-if="showStatus">
      <option slot="first" :value="null">(status)</option>
    </b-form-select>

    <b-form-select class="mb-2 mr-sm-2 mb-sm-0" v-model="value.productManager" :options="options.buyers">
      <option slot="first" :value="null">(buyer / PM)</option>
    </b-form-select>

    <b-form-select class="mb-2 mr-sm-2 mb-sm-0" v-model="value.goodsGroup" :options="options.goodsGroups">
      <option slot="first" :value="null">(goods group)</option>
    </b-form-select>

    <b-form-select class="mb-2 mr-sm-2 mb-sm-0" v-model="value.articleGroup" :options="options.articleGroups">
      <option slot="first" :value="null">(article group)</option>
    </b-form-select>

    <b-form-select class="mb-2 mr-sm-2 mb-sm-0" v-model="value.supplierCode" :options="options.supplierCodes">
      <option slot="first" :value="null">(supplier)</option>
    </b-form-select>

    <b-form-input class="mb-2 mr-sm-2 mb-sm-0" v-model="value.sapArticleNumber" type="text" placeholder="SAP article number"></b-form-input>

    <datepicker name="startDateFrom" placeholder="start date from" input-class="form-control"
                v-model="value.startDateFrom" format="dd-MM-yyyy" v-if="showStartDate"
                :clear-button="true"></datepicker>
    <datepicker name="startDateTo" placeholder="start date to" input-class="form-control mr-2"
                v-model="value.startDateTo" format="dd-MM-yyyy" v-if="showStartDate"
                :clear-button="true"></datepicker>

    <b-button variant="primary" @click="submit">Load</b-button>
  </b-form>
</template>

<script>
import Datepicker from 'vuejs-datepicker';
import { get as getRefTable } from '@/lib/reftables';

function pad2 (num) {
  if (num < 10) return '0' + num;
  return num;
}

function formatDate (value) {
  return value.getFullYear() + '-' + pad2(value.getMonth() + 1) + '-' + pad2(value.getDate());
}

function parseDate (value) {
  if (typeof value === 'string' && value.length === 10) {
    const year = parseInt(value.substring(0, 4), 10);
    const month = parseInt(value.substring(5, 7), 10);
    const day = parseInt(value.substring(8), 10);

    if (!isNaN(year) && !isNaN(month) && !isNaN(day)) {
      return new Date(year, month - 1, day);
    }
  }

  return null;
}

export default {
  name: 'SearchForm',

  components: {
    Datepicker
  },

  props: {
    historic: {
      type: Boolean
    },

    showStartDate: {
      type: Boolean
    },

    showStatus: {
      type: Boolean
    }
  },

  data () {
    return {
      value: {
        status: null,
        productManager: null,
        goodsGroup: null,
        articleGroup: null,
        supplierCode: null,
        sapArticleNumber: null,
        startDateFrom: null,
        startDateTo: null
      },
      options: {
        status: [
          { value: 'REJECTED', text: '03 - Afgekeurd' },
          { value: 'APPROVED', text: '04 - Goedgekeurd' }
        ]
      },
      selected: 'APPROVED'
    };
  },

  created () {
    this.loadOptions();
  },

  watch: {
    historic () {
      this.loadOptions();
    }
  },

  methods: {
    set (value) {
      // prevent missing properties
      if (typeof value.status === 'undefined') value.status = null;
      if (typeof value.productManager === 'undefined') value.productManager = null;
      if (typeof value.goodsGroup === 'undefined') value.goodsGroup = null;
      if (typeof value.articleGroup === 'undefined') value.articleGroup = null;
      if (typeof value.supplierCode === 'undefined') value.supplierCode = null;
      if (typeof value.sapArticleNumber === 'undefined') value.sapArticleNumber = null;
      value.startDateFrom = parseDate(value.startDateFrom);
      value.startDateTo = parseDate(value.startDateTo);

      this.$set(this, 'value', value);
    },

    loadOptions () {
      getRefTable('buyers', this.historic)
        .then(values => this.setOptions('buyers', values))
        .catch(console.error);

      getRefTable('goodsGroups', this.historic)
        .then(values => this.setOptions('goodsGroups', values))
        .catch(console.error);

      getRefTable('articleGroups', this.historic)
        .then(values => this.setOptions('articleGroups', values))
        .catch(console.error);

      getRefTable('supplierCodes', this.historic)
        .then(values => this.setOptions('supplierCodes', values))
        .catch(console.error);
    },

    setOptions (name, values) {
      this.$set(this.options, name, values.map(value => ({
        value: value.key,
        text: value.value
      })));
    },

    submit () {
      const result = {};

      for (let key of Object.keys(this.value)) {
        if (this.value[key] != null && this.value[key] !== '') {
          result[key] = this.value[key];
        }
      }

      if (result.startDateFrom) result.startDateFrom = formatDate(result.startDateFrom);
      if (result.startDateTo) result.startDateTo = formatDate(result.startDateTo);

      this.$emit('submit', result);
    }
  }
};
</script>
