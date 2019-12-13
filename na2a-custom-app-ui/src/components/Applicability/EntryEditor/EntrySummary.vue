<template>
  <div class="entry-summary">
    <ul v-if="sections">
      <li v-for="(section, idx) of sections" :key="idx" :id="'es-section-' + idx">
        <collapse-link v-model="section.showValues" :label="section.type"></collapse-link>
        <b-collapse class="mt-2"
                    v-model="section.showValues"
                    :id="'es-' + section.type">
          <ul>
            <li v-for="(value, valueIdx) of section.values" :key="valueIdx" :id="'es-entry-' + valueIdx">{{ value }}</li>
          </ul>
        </b-collapse>
      </li>
    </ul>
  </div>
</template>

<script>
import CollapseLink from '@/components/CollapseLink';
import { summarize } from '@/lib/utils';

export default {
  components: {
    CollapseLink
  },

  props: {
    value: {
      required: true
    }
  },

  data () {
    return {
      sections: []
    };
  },

  created () {
    this.update(this.value);
  },

  methods: {
    update (newValue) {
      if (newValue) {
        const sections = summarize(newValue, false);

        const previous = {};
        for (let section of this.sections) {
          previous[section.type] = section.showValues;
        }

        for (let section of sections) {
          // only set to false if previous state had false set for this section
          section.showValues = previous[section.type] !== false;
        }

        this.$set(this, 'sections', sections);
      }
    }
  },

  watch: {
    value: {
      handler (newValue) {
        this.update(newValue);
      },
      deep: true
    }
  }
};
</script>

<style scoped>
.entry-summary > ul {
  list-style: none;
  margin: 0;
  padding: 0;
}
</style>
