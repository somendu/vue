<template>
  <div id="wltp-mass-update-tabs-section">
    <b-tabs id="wltp-mass-update-tabs">
      <b-tab title="WLTP" class="tab-wrapper">
        <div id="wltp-design-section" class="tab-left-content">
          <wltp-design></wltp-design>
        </div>
        <div id="wltp-homologation-section" class="tab-right-content">
          <wltp-homologation></wltp-homologation>
        </div>
      </b-tab>
      <b-tab title="DESIGN">
        Component for DESIGN comes here!!
      </b-tab>
      <b-tab title="HOMOLOGATION">
        Component for HOMOLOGATION comes here!!
      </b-tab>
      <b-tab title="INSTALLATION">
        Component for INSTALLATION comes here!!
      </b-tab>
    </b-tabs>
  </div>
</template>
<script>
import WltpDesign from '@/components/MassUpdate/WLTP/WltpDesign';
import WltpHomologation from '@/components/MassUpdate/WLTP/WltpHomologation';

export default {
  components: {
    WltpDesign,
    WltpHomologation
  }
};
</script>
<style scoped>
#wltp-mass-update-tabs-section {
  max-height: 100%;
  overflow-y: auto;
}
.tab-wrapper {
  display: flex;
  flex-direction: row;
  margin-left: 5px;
}

.tab-left-content {
  flex: 1;
  flex-direction: row;
  margin-left: 5px;
  margin-right: 5px;
}

.tab-right-content {
  flex: 1;
  flex-direction: row;
  margin-left: 5px;
  margin-right: 5px;
}
</style>
