<template>
  <div>
    <p class="loading-small" v-if="loading">Loading options ...</p>

    <div v-if="entries">
      <div class="mb-2">
        <input id="ce-colourInput"
               type="text"
               :placeholder="getPlaceholder"
               class="form-control form-control-sm"
               v-model="filter"/>
      </div>

      <button :id="'ce-colourSelect' + index"
              class="btn btn-sm btn-primary mr-2 mb-2"
              v-for="(option, index) in filteredEntries" :key="index"
              :class="{ 'btn-primary': option.selected, 'btn-outline-secondary': !option.selected }"
              :disabled="option.disabled"
              @click="buttonClicked(option)">{{ option.value }}</button>
    </div>
  </div>

</template>

<script>
export default {
  props: {
    value: {
      required: true
    },
    options: {
      required: true
    },
    loading: {
      type: Boolean
    },
    allModels: {
      type: Boolean,
      required: true
    }
  },

  data () {
    return {
      filter: null,
      entries: [],
      placeholder: String
    };
  },

  created () {
    this.constructEntries();
  },

  methods: {
    constructEntries () {
      if (!this.options || !this.options.length) {
        this.$set(this, 'entries', []);
        return;
      }

      // make a copy of options, including only key and value
      // this.setSelectedOptions();
      const entries = this.options
        .map(entry => ({
          key: entry.key,
          value: entry.value
          // selected: entry.selected ? entry.selected : false
        }));

      this.mixinValue(entries);

      this.$set(this, 'entries', entries);
    },

    // setSelectedOptions () {
    //   // use this.value.category to find the correct option
    //   // then use this.value.value to find the correct child option based on key
    //   if (this.options && Array.isArray(this.options) && this.value && Array.isArray(this.value)) {
    //     for (const item of this.value) {
    //       const idx = this.options.findIndex(option => item.category === option.value);
    //       if (idx !== -1) {
    //         const children = this.options[idx].children;
    //         if (children && Array.isArray(children)) {
    //           for (const child of children) {
    //             if (child.key === item.value) {
    //               child.selected = true;
    //             }
    //           }
    //         }
    //       }
    //     }
    //   }
    // },

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
      // if (entries && Array.isArray(entries) && this.value && Array.isArray(this.value)) {
      //   for (const entry of entries) {
      //     entry.selected = false;
      //   }
      //
      //   for (const item of this.value) {
      //     for (const entry of entries) {
      //       if (entry.key === item.value) {
      //         entry.selected = true;
      //       }
      //     }
      //   }
      // }
    },

    buttonClicked (option) {
      option.selected = !option.selected;

      if (option.selected) {
        // ensure it's in
        if (!this.value.find(v => v.key === option.key)) {
          this.value.push({
            key: option.key,
            value: option.value
            // selected: option.selected
          });
          this.$emit('input', this.value);
        }
      } else {
        // ensure it's out
        const index = this.value.findIndex(v => v.key === option.key);
        if (index !== -1) {
          this.value.splice(index, 1);
          this.$emit('input', this.value);
        }
      }
    }
  },

  computed: {
    filteredEntries () {
      const f = this.filter && this.filter.trim().toLowerCase();

      if (this.allModels === true && (f === null || f.length < 2)) {
        return [];
      }

      if (f != null && f !== '') {
        const search = new RegExp(f, 'i');
        return this.entries.filter(entry => search.test(entry.value));
      } else {
        return this.entries;
      }
    },

    getPlaceholder () {
      if (this.allModels) {
        return 'Please enter filter criteria for available colours...';
      }
      return 'Filter...';
    }
  },

  watch: {
    options () {
      this.filter = null;
      this.constructEntries();
    },

    value () {
      if (this.entries) {
        this.mixinValue(this.entries);
      }
    }
  }
};
</script>

<style scoped>
.container {
  overflow-y: auto;
}
</style>
