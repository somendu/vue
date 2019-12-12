<template>
    <div id="countries-overview">
        <div v-if="entries && show">
            <div id="selectAllCountries">
              <b-form-checkbox
                id="selectAllCountriesCheckbox"
                v-model="allSelected"
                @change="selectAll">
                Select all/Deselect all
              </b-form-checkbox>
            </div>
            <div class="mb-2">
                <input id="countryFilter"
                    type="text"
                    :placeholder="getPlaceholder"
                    class="form-control form-control-sm"
                    v-model="filter"/>
            </div>
            <button
                class="btn btn-sm btn-primary mr-2 mb-2"
                v-for="(country, idx) of filteredEntries"
                :class="{ 'btn-primary': country.selected, 'btn-outline-secondary': !country.selected }"
                :key="idx"
                :id="'country-btn-' + idx"
                 @click="buttonClicked(country)">{{country.value + ' [' + country.key + ']'}}</button>
        </div>
    </div>
</template>
<script>
export default {
  props: {
    value: {
      required: true
    },
    show: {
      type: Boolean,
      default: false
    },
    countries: {
      type: Array
    }
  },

  data () {
    return {
      filter: null,
      allSelected: false,
      entries: []
    };
  },

  created () {
    this.constructEntries();
  },

  sortByText (a, b) {
    let countryA = a.value.toUpperCase(); // ignore upper and lowercase
    let countryB = b.value.toUpperCase(); // ignore upper and lowercase
    if (countryA < countryB) {
      return -1;
    }
    if (countryA > countryB) {
      return 1;
    }

    return 0;
  },

  methods: {
    constructEntries () {
      if (!this.countries || !this.countries.length) {
        this.$set(this, 'entries', []);
        return;
      }

      // make a copy of countries
      const entries = this.countries
        .map(entry => ({
          key: entry.key,
          value: entry.value
        }));

      this.mixinValue(entries);

      this.$set(this, 'entries', entries);
    },

    mixinValue (entries) {
      const selected = {};

      if (this.value) {
        for (let v of this.value) {
          selected[v.key] = true;
        }
      }

      for (let entry of entries) {
        entry.selected = !!selected[entry.key];
      }
    },

    buttonClicked (country) {
      country.selected = !country.selected;
      this.updateCountrySelection(country);

      this.allSelected = (this.value.length !== this.filteredEntries.filter(country => country.selected).length);
    },

    updateCountrySelection (country) {
      if (country.selected) {
        // ensure it's in
        if (!this.value.find(v => v.key === country.key)) {
          this.value.push({
            key: country.key,
            value: country.value
          });
          this.$emit('input', this.value);
        }
      } else {
        // ensure it's out
        const index = this.value.findIndex(v => v.key === country.key);
        if (index !== -1) {
          this.value.splice(index, 1);
          this.$emit('input', this.value);
        }
      }
    },

    selectAll (value) {
      this.filteredEntries.forEach(country => {
        country.selected = value;
        this.updateCountrySelection(country);
      });
      this.allSelected = value;
    }
  },

  computed: {
    filteredEntries () {
      let filteredEntries = Object.assign([], this.entries);
      const f = this.filter && this.filter.trim().toLowerCase();

      filteredEntries.sort(function (a, b) {
        let countryA = a.value.toUpperCase();
        let countryB = b.value.toUpperCase();
        if (countryA < countryB) {
          return -1;
        }
        if (countryA > countryB) {
          return 1;
        }

        return 0;
      });

      if (f != null && f !== '') {
        const search = new RegExp(f, 'i');
        return filteredEntries.filter(entry => search.test(entry.value));
      } else {
        return filteredEntries;
      }
    },

    getPlaceholder () {
      return 'Filter...';
    }
  },

  watch: {
    countries () {
      if (this.entries) {
        this.constructEntries();
      }
    },

    value () {
      if (this.entries) {
        this.constructEntries();
      }
    }
  }
};
</script>
<style scoped>

</style>
