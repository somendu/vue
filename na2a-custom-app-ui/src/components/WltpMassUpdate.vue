<template>
  <div id="wltp-mass-update-main">
    <na2a-header id="wltp-mass-update-title" title="ACC360 Data Management"></na2a-header>
    <p id="loading-wltp-mass-update" class="loading" v-if="loading">Loading ACC360 data management...</p>

    <div class="wltp-mass-update-wrapper" v-if="!loading">
      <div class="wltp-applications simple-collapse pt-3 mb-3 border-top" :class="{'collapsed': !showApplications}">
        <h4>
          <collapse-link v-model="showApplications" label="Applications"></collapse-link>
        </h4>
        <extended-application-entries
          :partLinkingVariants="applications"
          @openModal="toggleApplication"
          @selectAllClick="toggleAllApplications">
        </extended-application-entries>
      </div>

      <div class="wltp-content">
        <wltp-mass-update-tabs ></wltp-mass-update-tabs>
      </div>
    </div>
  </div>
</template>
<script>

import Na2aHeader from './Header/Na2aHeader.vue';
import CollapseLink from './CollapseLink';
import ExtendedApplicationEntries from './Shared/ExtendedApplicationEntries';
import WltpMassUpdateTabs from './MassUpdate/WltpMassUpdateTabs';

import { mapGetters, mapActions } from 'vuex';

export default {
  components: {
    Na2aHeader,
    CollapseLink,
    ExtendedApplicationEntries,
    WltpMassUpdateTabs
  },

  props: {
    ids: {
      type: Array,
      required: true
    },
    url: {
      type: String,
      required: true
    }
  },

  created () {
    this.load();
  },

  watch: {
    ids () {
      this.load();
    }
  },

  methods: {
    ...mapActions(['loadBasePath',
      'loadById',
      'toggleAll',
      'toggleSelectedApplication']),

    load () {
      if (this.ids && this.ids.length) {
        this.loadBasePath(this.url);
        this.loadById(this.ids);
      }
    },

    toggleAllApplications () {
      this.toggleAll();
    },

    toggleApplication (idx) {
      this.toggleSelectedApplication(idx);
    }
  },

  data () {
    return {
      showApplications: true
    };
  },

  computed: mapGetters(['loading', 'applications'])
};
</script>
<style>
.wltp-mass-update-wrapper {
  display: flex;
  flex-direction: column;
  margin-left: 5px;
  max-height: 95vh;
}

.wltp-applications {
  flex: 1 0 auto;
  max-height: 40vh;
  height: 100%;
  overflow-y: auto;
}

.wltp-content {
  flex: -1 -1 auto;
  max-height: 80vh;
  height: 750px;
  overflow-y: auto;
}

.simple-collapse.collapsed {
  flex: initial;
}
.simple-collapse.collapsed > div {
  display: none;
}
</style>
