<template>
  <div class="tree-wrapper">
    <div class="mb-2" v-if="showFilter">
      <input id="te-filterInput"
             type="text"
             placeholder="Filter ..."
             class="form-control form-control-sm"
             v-model="filter" />
    </div>

    <p class="loading-small" v-if="loading">Loading options ...</p>

    <ul v-if="tree">
      <li v-for="(category, catIdx) of filteredTree" :key="catIdx">
        <collapse-link v-model="category.open" :label="category.value"></collapse-link>
        <b-collapse class="mt-2" v-model="category.open"
                    :id="'te-category' + category.key">
          <ul>
            <li v-for="(option, optIdx) of category.children" :key="optIdx">
              <div class="form-check" :title="option.disabled">
                <input class="form-check-input" type="checkbox"
                       v-model="option.selected"
                       @click="changed(category, option)"
                       :id="'te-option-' + option.key"
                       :disabled="!!option.disabled">
                <label class="form-check-label"
                       :for="'te-option-'+ option.key">{{ option.value }}</label>
              </div>
            </li>
          </ul>
        </b-collapse>
      </li>
    </ul>
  </div>
</template>

<script>
import CollapseLink from '@/components/CollapseLink';

export default {
  components: {
    CollapseLink
  },

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
    validationEnabled: {
      type: Boolean,
      required: false
    },
    showFilter: {
      type: Boolean
    }
  },

  data () {
    return {
      tree: null,
      filter: null
    };
  },

  created () {
    this.constructTree();
  },

  methods: {
    constructTree () {
      if (!this.options) {
        this.$set(this, 'tree', []);
        return;
      }

      // make a copy (in which we only include key and value)
      const tree = this.options
        .map(node => {
          return {
            key: node.key,
            value: node.value,
            open: false,
            children: node.children.map(child => ({
              key: child.key,
              value: child.value,
              katashiki: child.katashiki,
              selected: false
            }))
          };
        });

      this.mixinValueInTree(tree);
      this.mixinKatashiki(tree);

      this.$set(this, 'tree', tree);
    },

    updateTree () {
      if (this.tree) {
        this.mixinValueInTree(this.tree);
      }
    },

    mixinValueInTree (tree) {
      const values = {};

      if (Array.isArray(this.value)) {
        for (let entry of this.value) {
          values[entry.key] = true;
        }
      }

      for (let category of tree) {
        for (let option of category.children) {
          option.selected = !!values[option.key];
        }
      }
      // if (tree && Array.isArray(tree) && this.value && Array.isArray(this.value)) {
      //   for (const item of this.value) {
      //     const idx = tree.findIndex(option => item.category === option.value);
      //     if (idx !== -1) {
      //       const children = tree[idx].children;
      //       if (children && Array.isArray(children)) {
      //         for (const child of children) {
      //           if (child.key === item.value) {
      //             child.selected = true;
      //           }
      //         }
      //       }
      //     }
      //   }
      // }
    },

    mixinKatashiki (tree) {
      for (let category of tree) {
        for (let option of category.children) {
          let disabled = false;

          if (!this.validationEnabled) continue;

          if (this.katashiki.enabled && option.katashiki && option.katashiki.length) {
            for (let kCat of Object.keys(this.katashiki.categories)) {
              // ignore if the restriction came from the same category
              if (kCat === category.value) continue;

              // disable if multiSelectionCategory is set to another category ...
              if (this.katashiki.multiSelectionCategory && this.katashiki.multiSelectionCategory !== category.value) {
                // ... and this category has one option selected (which isn't this one)
                if (category.children.find(op => (op.selected && op.value !== option.value))) {
                  disabled = 'Multiple selection has already been used elsewhere';
                  break;
                }
              }
              // we're done; it's disabled
              if (disabled) break;

              for (let kCatEntry of this.katashiki.categories[kCat]) {
                // if I don't have one of these katashiki values, I'm disabled
                if (!option.katashiki.find(k => kCatEntry.indexOf(k) !== -1)) {
                  disabled = 'Not compatible with selected items';
                  break;
                }
              }
              // we're done; it's disabled
              if (disabled) break;
            }
          }

          option.disabled = disabled;
        }
      }
    },

    changed (category, option) {
      setTimeout(() => {
        if (this.value) {
          // see if this one is already in
          for (let i = 0; i < this.value.length; i++) {
            if (this.value[i].key === option.key) {
              // if it was deselected -> remove
              if (!option.selected) {
                this.value.splice(i, 1);
                this.$emit('input', this.value);
              }
              // otherwise; if it's in and it is selected, value is up to date
              return;
            }
          }

          // otherwise add one
          if (option.selected) {
            this.value.push({
              key: option.key,
              value: option.value,
              katashiki: option.katashiki,
              category: category.value
            });
            this.$emit('input', this.value);
          }
        }
      }, 0);
    }
  },

  computed: {
    katashiki () {
      return this.$store.state.Editor.katashiki;
    },

    filteredTree () {
      const treeWithoutSSNS = this.tree.filter(v => v.value !== 'SSNS');

      if (treeWithoutSSNS && this.filter) {
        const f = this.filter.trim();
        if (f !== '') {
          const search = new RegExp(f, 'i');
          const filtered = [];

          for (const category of treeWithoutSSNS) {
            let filteredChildren;

            if (search.test(category.value)) {
              // category name matches
              filteredChildren = category.children;
            } else {
              filteredChildren = category.children.filter(option => search.test(option.value));
            }

            if (filteredChildren.length) {
              filtered.push({
                key: category.key,
                value: category.value,
                open: true,
                children: filteredChildren
              });
            }
          }

          return filtered;
        }
      }

      return treeWithoutSSNS;
    }
  },

  watch: {
    value (newValue, oldValue) {
      // prevent updating the tree we just modified ourselves (event loop)
      if (newValue !== oldValue) {
        this.updateTree();
      }
    },

    options () {
      this.filter = null;
      this.constructTree();
    },

    katashiki () {
      if (this.tree) {
        this.mixinKatashiki(this.tree);
      }
    }
  }
};
</script>

<style scoped>
.tree-wrapper {
  /* overflow: auto; */
}
ul {
  list-style: none;
  margin: 0;
  padding: 0;
}
li ul {
  padding-left: 20px;
}
</style>
