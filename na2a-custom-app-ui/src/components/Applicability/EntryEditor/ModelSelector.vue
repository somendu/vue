<template>
  <form class="form-inline">
    <label class="mr-2" for="ms-brand">Brand</label>
    <select class="form-control mr-4"
            id="ms-brand"
            ref="brandSelect"
            v-model="selectedBrand"
            :disabled="all"
            @change="checkBrandChange('brand')"
            v-focus>
      <option id="ms-brand-select" :value="null">- select -</option>
      <option v-for="(o, idx) in brands.entries" :value="o.key" :key="idx" :id="'ms-brand-' + idx">{{ o.value }}</option>
    </select>

    <label class="mr-2" for="ms-model">Model</label>
    <select class="form-control mr-4"
            id="ms-model"
            ref="modelSelect"
            v-model="model"
            :disabled="all"
            autocomplete="on"
            @change="checkModelChange('model')">
      <option id="ms-model-select" :value="null">- select -</option>
      <option v-for="(o, idx) in models.entries" :value="o.key" :key="idx" :id="'ms-model-' + idx">{{ o.value }}</option>
    </select>

    <label class="mr-2" for="ms-project">FMC/MMC</label>
    <select class="form-control mr-4"
            id="ms-project"
            v-model="project"
            :disabled="all"
            autocomplete="on"
            @change="checkProjectChange('project')">
      <option id="ms-project-select" :value="null">- select -</option>
      <option v-for="(o, idx) in projects.entries" :value="o.key" :key="idx" :id="'ms-project-' + idx">{{ o.value }}</option>
    </select>

    <!-- on request temporary disabled as not part of MVP 1 -->
    <!-- <div class="form-check">
      <input class="form-check-input" type="checkbox" id="ms-selectAll"
             v-model="all"
             @change="allChanged">
      <label class="form-check-label" for="ms-selectAll">
        All models
      </label>
    </div> -->
  </form>
</template>

<script>
import axios from 'axios';

export default {
  props: {
    value: {
      required: true
    },
    brand: {
      type: String,
      required: false
    },
    changedFlag: {
      type: Boolean,
      required: false
    },
    resetFlag: {
      type: Boolean,
      required: false
    }
  },

  data () {
    return {
      selectedBrand: null,
      model: null,
      project: null,
      all: false,
      brands: {
        loading: false,
        error: null,
        entries: []
      },
      models: {
        loading: false,
        error: null,
        entries: []
      },
      projects: {
        loading: false,
        error: null,
        entries: []
      },
      previousBrand: null,
      previousModel: null,
      previousProject: null
    };
  },

  directives: {
    focus: {
      // directive definition
      inserted (el) {
        el.focus();
      }
    }
  },

  created () {
    // this.loadModels();
    // this.$refs.modelSelectorBrand.$el.focus();
    this.selectedBrand = this.brand ? this.brand : null;
    this.loadBrands();
    this.updateValue();
  },

  watch: {
    value (newValue, oldValue) {
      // prevent unnecessary updates
      if (newValue !== oldValue) {
        this.updateValue();
      }
    },
    changedFlag (newValue, oldValue) {
      // is brand or model changing
      if (newValue) {
        this.modelOrBrandChanged();
        this.$emit('resetchangeflag');
      } else {
        this.selectedBrand = this.previousBrand;
        this.model = this.previousModel;
        this.project = this.previousProject;
      }
    },
    resetFlag (newValue, oldValue) {
      // change to brand or model cancelled
      if (newValue) {
        this.selectedBrand = this.previousBrand;
        this.model = this.previousModel;
        this.project = this.previousProject;
        this.$emit('resetdropdownflag');
      }
    }
  },

  methods: {
    loadBrands () {
      this.brands.loading = true;

      axios.get(`/api/brands`)
        .then(res => {
          this.brands.loading = false;
          this.brands.error = null;
          this.$set(this.brands, 'entries', res.data);
        })
        .catch(err => {
          this.brands.loading = false;
          this.brands.error = err.toString();
        });
    },

    loadModels () {
      this.models.loading = true;
      this.$set(this.projects, 'entries', []);

      axios.get(`/api/brands/${this.selectedBrand}/models`)
        .then(res => {
          this.models.loading = false;
          this.models.error = null;
          this.$set(this.models, 'entries', res.data);
        })
        .catch(err => {
          this.models.loading = false;
          this.models.error = err.toString();
        });
    },

    loadProjects () {
      if (this.selectedBrand && this.model) {
        const model = this.model;

        axios.get(`/api/brands/${this.selectedBrand}/models/${model}/projects`)
          .then(res => {
            if (this.model === model) {
              this.projects.loading = false;
              this.projects.error = null;
              this.$set(this.projects, 'entries', res.data);
            }
          })
          .catch(err => {
            if (this.model === model) {
              this.projects.loading = false;
              this.projects.error = err.toString();
            }
          });
      } else {
        this.projects.loading = false;
        this.projects.error = null;
        this.$set(this.projects, 'entries', []);
      }
    },

    updateValue () {
      const brand = this.value && this.value.brand;
      const model = this.value && this.value.model;
      const project = this.value && this.value.project;

      // do not reset the following if changing model or brand
      // if (!this.changedFlag) {
      //   this.selectedBrand = brand;
      //   this.model = model && model.key;
      //   this.previousBrand = null;
      //   this.previousModel = null;
      // }

      this.selectedBrand = brand;
      this.model = model && model.key;
      this.project = project && project.key;
      this.all = !!(this.value && this.value.all);

      this.previousBrand = this.previousBrand === null ? this.selectedBrand : this.previousBrand;
      this.previousModel = this.previousModel === null ? this.model : this.previousModel;
      this.previousProject = this.previousProject === null ? this.project : this.previousProject;

      this.loadModels();
      this.loadProjects();
    },

    modelOrBrandChanged () {
      if (this.previousBrand !== this.selectedBrand) {
        this.brandChanged();
      }
      if (this.previousModel !== this.model) {
        this.modelChanged();
      }
      if (this.previousProject !== this.project) {
        this.projectChanged();
      }
    },

    brandChanged () {
      this.model = null;
      this.project = null;
      this.previousModel = null;
      this.previousProject = null;
      this.loadModels();
      this.previousBrand = this.selectedBrand;
    },

    checkBrandChange: function (level) {
      if (this.previousBrand !== null && this.model !== null) {
        this.$emit('checkchange', level);
      } else {
        if (level !== 'model') {
          this.brandChanged();
        }
      }
    },

    checkModelChange: function (level) {
      if (this.previousModel !== null && this.project !== null) {
        this.$emit('checkchange', level);
      } else {
        this.modelChanged();
      }
    },

    checkProjectChange: function (level) {
      if (this.previousProject !== null && this.project !== null) {
        this.$emit('checkchange', level);
      } else {
        this.projectChanged();
      }
    },

    modelChanged () {
      this.project = null;
      this.previousModel = null;
      this.loadProjects();
      if (this.previousModel !== this.selectedModel || this.previousModel === null) {
        this.previousModel = this.model;
      }
    },

    projectChanged () {
      if (this.previousProject !== this.project || this.previousProject === null) {
        this.previousProject = this.project;
      }
      if (this.selectedBrand && this.model && this.project) {
        this.value.brand = this.brands.entries.find(v => v.key === this.selectedBrand).value;
        this.value.model = this.models.entries.find(v => v.key === this.model);
        this.value.project = this.projects.entries.find(v => v.key === this.project);
        this.value.all = this.all;

        this.$emit('input', this.value);
      }
    },

    allChanged () {
      this.selectedBrand = null;
      this.model = null;
      this.project = null;

      this.value.brand = null;
      this.value.model = null;
      this.value.project = null;
      this.value.all = this.all;

      this.$emit('input', this.value);
    }
  }
};
</script>
