<template>
  <div id="partlinking-view">
    <na2a-header title="Part Linking Management"></na2a-header>
    <p id="loading-part-linking" class="loading" v-if="loading">Loading part linking</p>

    <div class="partlinking-wrapper" v-if="!loading">
      <div class="partlinking-top-content">
        <div class="partlinking-application simple-collapse pt-3 mb-3 border-top"
            :class="{'collapsed': !showApplications}">
          <h4>
            <collapse-link v-model="showApplications" label="Applications"></collapse-link>
          </h4>
          <application-entries
            :partLinkingVariants="variants"
            @openModal="toggleVariant"
            @selectAllClick="toggleAllVariants"></application-entries>
        </div>
      </div>

      <div class="partlinking-bottom-content">
        <div class="partlinking-editor pt-3 mb-3 border-top">
          <h4>Common linked parts</h4>
          <parts-editor :noAppsSelected="noApplicationsSelected"
            :updating="updating"
            :parts="commonSelectedParts"
            @updatePartSelection="updateCommonPartSelection"
            @changed="updateParts"></parts-editor>

          <div id="pl-buttonControls" align="right">
            <p id="pl-loading-common-linked-parts" v-if="updating">Updating common linked parts...</p>
            <b-btn-group size="sm">
              <b-button id="pl-clearPartInfo" :disabled="this.commonSelectedParts.length === 0" variant="outline-primary" v-b-modal.pl-clearPartInfoModal>Undo</b-button>
              <b-button id="pl-savePartInfo" :disabled="this.commonSelectedParts.length === 0" variant="outline-primary" @click="savePartChanges">Update</b-button>
              <link-parts class="partlinking-delinking-buttons"
                align="right"
                :commonParts="selectedParts"
                :nonCommonParts="selectedNonCommonParts"
                :isLinking='false'
                @update="updateLinking">
              </link-parts>
            </b-btn-group>
          </div>
        </div>

        <div class="partlinking-selector pt-3 mb-3 border-top">
          <div>
            <h4 style="display:inline">Parts selection</h4>
            <div v-if="haveSelectedApplicationsNonCommonParts" class="alert alert-danger pl-alert" size="sm">There are uncommon linked parts for the applications you selected</div>
          </div>
          <parts-selector
            :nonCommonApplicationParts="allNonCommonParts"
            :hasPartsSelected="hasPartsSelected"
            :clearSelectedParts="clearSelectedParts"
            @updatePartSelection="updateNonCommomPartSelection"
            @clearPartsSelectionDone="clearPartsSelectionDone"
          >
          </parts-selector>

          <link-parts align="left" class="pt-3 mb-3 border-top"
            :commonParts="selectedParts"
            :nonCommonParts="selectedNonCommonParts"
            :isLinking='true'
            @update="updateLinking"
          ></link-parts>
        </div>
      </div>
    </div>
    <b-modal
      id="ae-confirmToggleModal"
      ref="confirmToggleModal"
      centered
      title="Unsaved changes"
      ok-title="Yes"
      cancel-title="No"
      @cancel="ignoreToggleRequest()"
      @ok="undoPartChanges()"
    >
      <p class="my-4">There are unsaved changes that will be lost. Do you want to proceed?</p>
    </b-modal>

    <b-modal
      id="pl-clearPartInfoModal"
      centered
      title="Clear common linked parts changes?"
      ok-title="Yes"
      cancel-title="No"
      @ok="undoPartChanges"
    >
      <p class="my-4">Do you want to clear all common linked parts changes?</p>
    </b-modal>

    <ignore-parts-linking-modal
      :show="showIgnoredLinkedParts"
      :ignoredParts="ignoredLinkingParts"
      @saveLinkedParts="savePartChanges"
      @clearShowIgnoredLinkedPartsModel="showIgnoredLinkedParts = false"
    >
    </ignore-parts-linking-modal>
  </div>
</template>

<script>
import axios from 'axios';
import eventbus from '@/eventbus';
import Na2aHeader from './Header/Na2aHeader';
import CollapseLink from './CollapseLink';
import ApplicationEntries from './Shared/ExtendedApplicationEntries';
import PartsEditor from './PartLinking/PartsEditor';
import LinkParts from './PartLinking/LinkParts';
import PartsSelector from './PartLinking/PartsSelector';
import IgnorePartsLinkingModal from './PartLinking/IgnorePartsLinkingModel';

export default {
  components: {
    Na2aHeader,
    CollapseLink,
    ApplicationEntries,
    PartsEditor,
    LinkParts,
    PartsSelector,
    IgnorePartsLinkingModal
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

  data () {
    return {
      loading: false,
      updating: false,
      variants: [],
      selectedParts: [],
      originalParts: String,
      showApplications: true,
      showCommonLinkedParts: true,
      showParts: true,
      selectedNonCommonParts: [],
      clickedCheckbox: null,
      hasPartsSelected: false,
      showIgnoredLinkedParts: false,
      ignoredLinkingParts: [],
      clearSelectedParts: false
    };
  },

  created () {
    this.load();
  },

  watch: {
    ids () {
      this.load();
    },
    selectedNonCommonParts () {
      this.hasPartsSelected = (this.selectedNonCommonParts.length !== 0);
    }
  },

  methods: {
    load () {
      if (this.ids && this.ids.length) {
        this.loading = true;
        this.error = null;

        axios
          .get(`${this.url}${this.ids.join(',')}`)
          .then(res => {
            this.loading = false;

            res.data.forEach(row => {
              row.selected = true;
              row.hasUnsavedChanges = false;
            });

            let filteredData = res.data.filter(entry => entry.status !== 'cancelled');

            this.$set(this, 'variants', filteredData);
            this.$set(this, 'originalParts', JSON.stringify(filteredData));
          })
          .catch(err => {
            this.loading = false;
            eventbus.$emit('showMessage', {
              title: "Couldn't load applications!",
              err
            });
          });
      }
    },

    toggleVariant (idx) {
      this.variants[idx].selected = !this.variants[idx].selected;
      this.clickedCheckbox = idx;

      if (this.hasUnsavedChanges) {
        this.$refs.confirmToggleModal.show();
      } else {
        this.selectedParts = [];
      }
    },

    toggleAllVariants () {
      if (!this.variants) return;

      let allSelected = true;

      for (const row of this.variants) {
        if (!row.selected) {
          allSelected = false;
          break;
        }
      }

      for (const row of this.variants) {
        row.selected = !allSelected;
      }
    },

    updateParts (newValue, oldValue) {
      // match parts of selected variants on partNumber, type and quantity
      const originals = JSON.parse(this.originalParts);

      for (const variantIdx in this.variants) {
        const variant = this.variants[variantIdx];
        const originalVariant = originals[variantIdx];

        // Only update parts for selected variants !!
        if (variant.selected) {
          for (const partIdx in variant.parts) {
            const part = variant.parts[partIdx];
            const originalPart = originalVariant.parts[partIdx];

            if (part.partNumber === newValue.partNumber &&
              part.type === oldValue.type &&
              part.quantity === oldValue.quantity) {
              // update edited values
              part.type = newValue.type;
              part.quantity = newValue.quantity;

              if (originalPart &&
              part.partNumber === originalPart.partNumber &&
              part.type === originalPart.type &&
              part.quantity === originalPart.quantity) {
                variant.hasUnsavedChanges = false;
              } else {
                variant.hasUnsavedChanges = true;
              }
            }
          }
        }
      }
    },

    undoPartChanges () {
      this.updating = true;
      this.error = null;

      axios
        .get(`${this.url}${this.ids.join(',')}`)
        .then(res => {
          this.updating = false;

          let activeVariants = res.data.filter(v => v.status !== 'cancelled');

          activeVariants.forEach(row => {
            const v = this.variants.find(variant => variant.id === row.id);
            row.hasUnsavedChanges = false;
            row.selected = v.selected;
          });

          this.$set(this, 'variants', res.data);
        })
        .catch(err => {
          this.updating = false;
          eventbus.$emit('showMessage', {
            title: "Couldn't load applications!",
            err
          });
        });
    },

    savePartChanges (possibleNewparts) {
      const variantsToSave = this.variants.filter(v => v.hasUnsavedChanges);
      this.updating = true;
      this.error = null;

      this.clearSelectedParts = false;

      axios
        .put('/api/partlinking/variants', variantsToSave)
        .then(resp => {
          this.updating = false;
          this.variants.forEach(variant => {
            variant.hasUnsavedChanges = false;
          });
          this.clearPartsSelection();
        })
        .catch(err => {
          this.updating = false;
          this.undoPartChanges();
          eventbus.$emit('showMessage', {
            title: "Couldn't save the application changes!",
            err
          });

          if (!possibleNewparts) {
            return;
          }
          for (let variant of this.variants) {
            possibleNewparts.forEach(possibleNewPart => {
              let idx = variant.parts.findIndex(currentPart => currentPart.partNumber === possibleNewPart.partNumber && currentPart.type === possibleNewPart.type);

              if (idx !== -1) {
                variant.parts.splice(idx, 1);
              }
            });
          }
        });
    },

    updatePartSelection (parts, part) {
      const index = parts.findIndex(p => p.partNumber === part.partNumber && p.type === part.type);
      if (part.selected && index === -1) {
        parts.push(part);
      } else if (!part.selected && index !== -1) {
        parts.splice(index, 1);
      }
    },

    updateCommonPartSelection (part) {
      this.updatePartSelection(this.selectedParts, part);
    },

    updateNonCommomPartSelection (part) {
      this.updatePartSelection(this.selectedNonCommonParts, part);
    },

    ignoreToggleRequest () {
      this.variants[this.clickedCheckbox].selected = !this.variants[this.clickedCheckbox].selected;
    },

    undoUnsavedChanges () {
      this.undoPartChanges();
    },
    getCommonSelectedParts () {
      if (!this.variants) return [];

      let first = true;
      const parts = [];

      for (const variant of this.variants) {
        if (!variant.selected) continue;

        if (first) {
          // for the first selected variant, we set parts
          for (const part of variant.parts) {
            parts.push(part); // (a copy of variant.parts)
          }
          first = false;
        } else {
          // for subsequent selected variants, we unset parts
          for (let i = parts.length - 1; i >= 0; i--) {
            // every part that's not within this variants parts ...
            if (!variant.parts.find(p => (
              p.partNumber === parts[i].partNumber &&
              p.type === parts[i].type &&
              p.quantity === parts[i].quantity))) {
              // ... will be removed
              parts.splice(i, 1);
            }
          }
        }
      }

      return parts;
    },

    updateLinking (isLinking, possibleNewPart) {
      let selectedVariants = this.variants.filter(v => v.selected === true);

      if (isLinking) {
        this.saveNewCommonPart(possibleNewPart, selectedVariants);
      } else {
        this.removeCommonPart(selectedVariants);
      }
    },

    removeCommonPart (selectedVariants) {
      this.updating = true;

      for (let variant of selectedVariants) {
        for (let partIdx in variant.parts) {
          let part = variant.parts[partIdx];

          const selectedPart = this.selectedParts.find(p => p.partNumber === part.partNumber && p.type === part.type);
          if (selectedPart) {
            axios
              .delete(`/api/partlinking/variants?variantId=${variant.id}&partId=${selectedPart.partNumber}&partType=${selectedPart.type}`)
              .then(res => {
                variant.parts = variant.parts.filter(p => !(p.partNumber === selectedPart.partNumber && p.type === selectedPart.type));
                this.updating = false;
                this.selectedParts = [];
              }).catch(err => {
                this.updating = false;
                eventbus.$emit('showMessage', {
                  title: "Couldn't save the application changes!",
                  err
                });
              });
          } else {
            console.warn('selected part not found on application!!!', part);
          }
        }
      }
    },

    saveNewCommonPart (possibleNewparts, selectedVariants) {
      this.ignoredLinkingParts = [];
      this.showIgnoredLinkedParts = false;

      const existingParts = [];
      for (let variant of selectedVariants) {
        for (let part of possibleNewparts) {
          let isExistingPart = variant.parts.find(
            p => part.partNumber === p.partNumber &&
            (part.type ? part.type === p.type : p.type === '12'));

          if (isExistingPart) {
            const index = existingParts.findIndex(p => p.partNumber === isExistingPart.partNumber);
            if (index === -1) {
              existingParts.push(isExistingPart);
            }
          } else {
            part.quantity = part.quantity ? part.quantity : 1;
            part.type = part.type ? part.type : '12';
            part.selected = false;
            variant.parts.push(Object.assign({}, part));
            variant.hasUnsavedChanges = true;
          }
        }
      }

      /* if existingParts => show warning message containing existing parts
        on close, process the new parts
        else process the new parts
      */
      this.ignoredLinkingParts = Object.assign([], existingParts);
      if (this.ignoredLinkingParts.length > 0) {
        this.showIgnoredLinkedParts = true;
      } else {
        this.savePartChanges(possibleNewparts);
      }
    },

    clearPartsSelection () {
      this.selectedNonCommonParts = [];
      this.clearSelectedParts = true;
    },

    clearPartsSelectionDone () {
      this.clearSelectedParts = false;
    }
  },

  computed: {
    commonSelectedParts () {
      return this.getCommonSelectedParts();
    },

    allOccurringParts () {
      if (!this.variants) return [];

      // collect all partNumbers / partNames
      const occurringParts = {};

      for (const variant of this.variants) {
        for (const part of variant.parts) {
          occurringParts[part.partNumber] = part.partName;
        }
      }

      return Object.keys(occurringParts)
        .map(key => ({
          partNumber: key,
          partName: occurringParts[key]
        }));
    },

    allNonCommonParts () {
      if (!this.variants) return [];

      const uniqueParts = [];

      for (const variant of this.variants) {
        const allParts = [];
        for (const v of this.variants) {
          if (variant.id !== v.id) {
            for (const part of v.parts) {
              const index = allParts.findIndex(p => p.partNumber === part.partNumber && p.type === part.type && p.quantity === part.quantity);
              if (index === -1) {
                allParts.push(part);
              }
            }
          }
        }
        if (variant.parts.length === 0) {
          return allParts;
        } else {
          for (const part of variant.parts) {
            const index = allParts.findIndex(p => p.partNumber === part.partNumber && p.type === part.type && p.quantity === part.quantity);
            if (index === -1) {
              uniqueParts.push(part);
            }
          }
        }
      }

      return uniqueParts;
    },

    haveSelectedApplicationsNonCommonParts () {
      let selectedVariants = this.variants.filter(v => v.selected === true);

      if (selectedVariants.length > 1) {
        // has non common parts if partNumber|partType|partQuantity combination isn't found in other parts

        const allParts = [];
        selectedVariants.forEach(variant => {
          variant.parts.forEach(part => {
            allParts.push(part);
          });
        });

        // Remark Array.forEach doesn't return a value!!
        for (const variant of selectedVariants) {
          for (const part of variant.parts) {
            const commonParts = allParts.filter(p => p.partName === part.partName && p.type === part.type && p.quantity === part.quantity);
            if (commonParts && commonParts.length === 1) {
              return true;
            }
          }
        }
        return false;
      }

      return false;
    },

    noApplicationsSelected () {
      let noApplicationsSelected = true;
      this.variants.forEach(v => {
        if (v.selected === true) {
          noApplicationsSelected = false;
        }
      });
      return noApplicationsSelected;
    },

    hasUnsavedChanges () {
      return this.variants.find(v => v.hasUnsavedChanges) !== undefined;
    }
  }
};
</script>

<style scoped>
.partlinking-wrapper {
  display: flex;
  flex-direction: column;
  margin-left: 5px;
  height: 90vh;
}

.partlinking-top-content {
  flex: 1 1 auto;
  margin-left: 5px;
  margin-right: 5px;
  max-height: 45%;
}

.partlinking-bottom-content {
  display: flex;
  flex: 1 1 auto;
  flex-direction: row;
  margin-left: 5px;
  margin-right: 5px;
  max-height: 85%;
}

.partlinking-bottom-content > div {
  width: 50%;
  max-height: inherit;
  margin: 5px;
}

.partlinking-application {
  max-height: 100%;
  overflow-y: auto;
}

.partlinking-delinking-buttons {
  text-align: right;
  margin-left: 10px;
}

.pl-alert {
  display:inline;
  padding:3px 3px;
  margin-left:20px;
}

.simple-collapse.collapsed {
  flex: initial;
}
.simple-collapse.collapsed > div {
  display: none;
}
</style>
