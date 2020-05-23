<template>
    <div id="parts-selector-overview">
      <b-tabs id="parts-selector-tabs">
        <b-tab id="all-parts-tab" title="All parts" :disabled="hasPartsSelected">
          <parts-selection-filter-bar
            @updateFilterSelection="updateFilterSelection"
          >
          </parts-selection-filter-bar>
          <p id="loading-all-parts" class="loading" v-if="loading">Retrieving all parts...</p>
          <all-parts-selector
            :allParts="partsList"
            :partNumber="partNumber"
            :partName="partName"
            @updateParts="updateParts"
            @updatePartSelection="updatePartSelection"
          >
          </all-parts-selector>
        </b-tab>
        <b-tab id="application-parts-tab" :title="'Application parts (' + partCount + ')'" :disabled="hasPartsSelected">
          <application-parts-selector
            :applicationParts="applicationParts"
            @updatePartSelection="updatePartSelection"
          ></application-parts-selector>
        </b-tab>
        <b-tab id="npa-tab" title="NPA" :disabled="hasPartsSelected">
          <npa-parts-selection-bar
            :npaSearching="npaSearching"
            :hasPartsSelected="hasPartsSelected"
            @npaSearch="npaSearch"
          >
          </npa-parts-selection-bar>
          <npa-parts-selector
            :npaParts="npaPartsList"
            :partNumber="partNumber"
            :partName="partName"
            @updateNpaParts="updateNpaParts"
            @updatePartSelection="updatePartSelection"
          >
          </npa-parts-selector>
        </b-tab>
      </b-tabs>
    </div>
</template>

<script>
import axios from 'axios';
import eventbus from '@/eventbus';
import AllPartsSelector from '@/components/PartLinking/PartsSelector/AllPartsSelector';
import ApplicationPartsSelector from '@/components/PartLinking/PartsSelector/ApplicationPartsSelector';
import NpaPartsSelector from '@/components/PartLinking/PartsSelector/NpaPartsSelector';
import CollapseLink from '@/components/CollapseLink';
import PartsSelectionFilterBar from '@/components/PartLinking/PartsSelector/PartsSelectionFilterBar';
import NpaPartsSelectionBar from '@/components/PartLinking/PartsSelector/NpaPartsSelectionBar';

export default {
  components: {
    AllPartsSelector,
    ApplicationPartsSelector,
    NpaPartsSelector,
    CollapseLink,
    PartsSelectionFilterBar,
    NpaPartsSelectionBar
  },
  props: {
    nonCommonApplicationParts: {
      type: Array,
      required: true
    },
    hasPartsSelected: {
      type: Boolean,
      required: true
    },
    clearSelectedParts: {
      type: Boolean,
      required: true
    }
  },

  data () {
    return {
      loading: false,
      npaSearching: false,
      allParts: [],
      npaParts: [],
      applicationParts: [],
      partNumber: '',
      searchPartNumber: '',
      partName: '',
      colourMaterial: '',
      selectedProject: '',
      selectedPic: '',
      selectedCommodity: ''
    };
  },
  watch: {
    nonCommonApplicationParts () {
      this.applicationParts = Object.assign([], this.nonCommonApplicationParts);
      this.getPartCount();
    },

    clearSelectedParts (newValue) {
      if (newValue === true) {
        for (const part of this.allParts) {
          part.selected = false;
        }
        for (const part of this.npaParts) {
          part.selected = false;
        }
        this.$emit('clearPartsSelectionDone');
      }
    }
  },

  created () {
    this.loadApplicationParts();
  },

  methods: {
    loadApplicationParts () {
      for (const part of this.nonCommonApplicationParts) {
        this.applicationParts.push(
          Object.assign({}, part, { selected: false })
        );
      }
    },

    load () {
      // Only load the parts if at least one filter is selected
      if (this.selectedProject !== '' || this.selectedPic !== '' || this.selectedCommodity !== '' || this.searchPartNumber !== '') {
        this.loading = true;

        let partNumber = this.searchPartNumber !== '' ? this.searchPartNumber : this.partNumber;

        axios.get(`/api/partlinking/parts?partNumber=` +
            partNumber +
            `&partName=` + this.partName +
            `&colourMaterial=` + this.colourMaterial +
            `&project=` + this.selectedProject +
            `&pic=` + this.selectedPic +
            `&commodity=` + this.selectedCommodity)
          .then(res => {
            res.data.forEach(entry => {
              entry.selected = false;
            });

            this.$set(this, 'allParts', res.data);
            this.loading = false;
          }).catch(err => {
            this.loading = false;
            eventbus.$emit('showMessage', {
              title: "Couldn't load applications!",
              err
            });
          });
      }
    },

    updateFilterSelection (type, value) {
      if (type === 'project') {
        this.selectedProject = value;
      }

      if (type === 'pic') {
        this.selectedPic = value;
      }

      if (type === 'commodity') {
        this.selectedCommodity = value;
      }

      if (type === 'partNumber') {
        this.searchPartNumber = value;
      }

      // TODO: fetch the parts if at least one filter is selected
      this.load();
    },

    npaSearch (value) {
      this.searchPartNumber = value;
      this.npaLoad();
    },

    npaLoad () {
      if (this.searchPartNumber !== '') {
        this.npaSearching = true;
        let partNumber = this.partNumber !== '' ? this.partNumber : this.searchPartNumber;
        axios.get(`/api/partlinking/npa?` +
            `partNumber=` + partNumber +
            `&partName=` + this.partName)
          .then(res => {
            res.data.forEach(entry => {
              entry.selected = false;
            });
            this.$set(this, 'npaParts', res.data);
            this.npaSearching = false;
          }).catch(err => {
            this.npaSearching = true;
            eventbus.$emit('showMessage', {
              title: "Couldn't load applications!",
              err
            });
          });
      }
    },

    updateNpaParts (type, value) {
      if (type === 'partNumber') {
        this.partNumber = value;
      }
      if (type === 'partName') {
        this.partName = value;
      }
      this.npaLoad();
    },

    updateParts (type, value) {
      if (type === 'partNumber') {
        this.partNumber = value;
      }
      if (type === 'partName') {
        this.partName = value;
      }

      if (type === 'colourMaterial') {
        this.colourMaterial = value;
      }

      this.load();
    },

    getPartCount () {
      return this.applicationParts ? this.applicationParts.length : 0;
    },

    updatePartSelection (part) {
      this.$emit('updatePartSelection', part);
    }
  },

  computed: {
    partCount () {
      return this.getPartCount();
    },

    partsList () {
      if (this.partNumber !== '' && this.searchPartNumber !== '') {
        return this.allParts.filter(part => part.partNumber.includes(this.partNumber));
      }

      if (this.selectedProject === '' && this.selectedPic === '' && this.selectedCommodity === '' && this.searchPartNumber === '') {
        return [];
      }
      return this.allParts;
    },

    npaPartsList () {
      return this.npaParts;
    }
  }
};
</script>

<style scoped>
#parts-selector-overview {
  max-height: 100%;
  overflow: auto;
}
</style>
