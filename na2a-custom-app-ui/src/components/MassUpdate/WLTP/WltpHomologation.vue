<template>
  <div>
    <b-card title="Homologation" style="margin-top: 10px">
      <div class="custom-control custom-checkbox">
        <input
          type="checkbox"
          class="custom-control-input"
          id="wltp-status-flag"
          @click.prevent="preventedWltpFlagUpdate($event)"
          v-model="wltpFlag"
          :disabled="isNonAllowedHomologationType && !wltpFlag && !indeterminedWltpState"
          >
        <label class="custom-control-label" for="wltp-status-flag">WLTP</label>
        <p class="wltp-error-message" v-if="isNonAllowedHomologationType && !wltpFlag && !indeterminedWltpState">
          At least one of the selected applications is <b>not</b> of homologation type <b>MSTA</b> or <b>WVTA.<br>
          Value cannot be set to checked.</b>
        </p>
      </div>
      <hr>
      <!-- Hub Fitment Flag -->
      <div id="hub-fitment-flag-section" style="margin-left:10px; max-width:20%">
        <label class="mr-2" for="ms-brand">Available for PPO request</label>
        <b-form-select
          @click.native="removeIndeterminateState"
          @change="changeHubFitmentValue($event)"
          class="form-control mr-4"
          id="hub-fitment-flag-input"
          v-model="hubFitmentFlag"
          :options="hubFitmentOptions"
          :disabled="isAllDisabled || wltpFlag"
          :placeholder="colourState('hubFitmentFlag', 'homologation') === null ? '' : '*****'"
          :state="colourState('hubFitmentFlag', 'homologation')">
        </b-form-select>
      </div>
      <!-- deltaCDA -->
      <div class="simple-collapse pt-3 mb-3 border-top" :class="{'collapsed': !showDeltaCDA}" style="margin-top:5px">
        <h5>
          <collapse-link v-model="showDeltaCDA" label="Delta Cd.A"></collapse-link>
        </h5>
        <div id="delta-cda-section" style="margin-left:10px;">
          <b-form-input
            id="delta-cda-input"
            @change="formatWithPrecision($event)"
            @input="validateDeltaCDAInput($event)"
            type="text"
            v-model="deltaCDA"
            placeholder="*****"
            style="max-width:20%"
            :state="colourState('deltaCDA', 'homologation')"
            :disabled="isAllDisabled || !wltpFlag"
          ></b-form-input>
          <p v-if="deltaCDAError" class="delta-cda-validation">{{ deltaCDAError }}</p>
        </div>
      </div>
      <div class="simple-collapse pt-3 mb-3 border-top" :class="{'collapsed': !showGenerationCode}">
        <h5>
          <collapse-link v-model="showGenerationCode" label="Generation Code"></collapse-link>
        </h5>
        <div id="generation-code-section" style="margin-left:10px">
          <generation-code :isAllDisabled="isAllDisabled"></generation-code>
        </div>
      </div>

      <div id="update-homologation-section">
        <b-button
          id="btn-update-homologation"
          variant="outline-primary"
          :disabled="!updateAllowed || updatingHomologation || validationError.generationCodes || indeterminedWltpState || deltaCDAError"
          @click="update"
        ><octicon name="sync" :spin="updatingHomologation"></octicon> Save</b-button>
      </div>
    </b-card>

    <b-modal id="wltp-homologation-confirm-update-modal"
      ref="confirmUpdateModal"
      centered
      title="Are you sure?"
      :no-close-on-backdrop="true"
      :no-close-on-esc="true"
      :hide-header-close="true"
      ok-title="Yes"
      cancel-title="No"
      @cancel="ignoreUpdate()"
      @ok="confirmUpdate()">
      <p class="my-4">Changes have been made to fields in an undetermined state. Do you want to continue?</p>
    </b-modal>

    <b-modal id="wltp-homologation-confirm-force-yes-modal"
      ref="confirmForceYesModal"
      centered
      title="Information"
      :no-close-on-backdrop="true"
      :no-close-on-esc="true"
      :hide-header-close="true"
      ok-title="OK"
      ok-only>
      <p class="my-4"><b>Please be aware:</b> Changing wltp to yes forces the <b>Available for PPO request</b> value to <b>Yes</b></p>
    </b-modal>
  </div>
</template>
<script>
import CollapseLink from '@/components/CollapseLink';
import Octicon from 'vue-octicon/components/Octicon';
import GenerationCode from './GenerationCode.vue';
import { mapGetters, mapActions, mapMutations } from 'vuex';

export default {
  components: {
    CollapseLink,
    GenerationCode,
    Octicon
  },

  data () {
    return {
      showDeltaCDA: true,
      showGenerationCode: true,
      showHubFitmentFlag: true,
      deltaCDA: null,
      deltaCDAError: null,
      hubFitmentFlag: false,
      updateAllowed: false,
      hubFitmentOptions: [
        { value: true, text: 'Yes' },
        { value: false, text: 'No' },
        { value: null, text: 'Throw' }]
    };
  },

  computed: {
    ...mapGetters([
      'wltpFlag',
      'selectedApplications',
      'indeterminedWltpState',
      'currentDeltaCDA',
      'updatingHomologation',
      'validationError',
      'homologationTypes']),

    isAllDisabled () {
      let selection = this.selectedApplications.length;
      if (selection === 0) {
        this.updateWltpFlag(false);
        this.addIndeterminedHubFitment();
      }
      return selection === 0;
    },

    isNonAllowedHomologationType () {
      let wvtaType = this.homologationTypes.find(type => type.parentKey === '100');
      let mstaType = this.homologationTypes.find(type => type.parentKey === '200');
      let noRegulationType = this.homologationTypes.find(type => type.parentKey === '500');

      let foundNonAllowable = false;

      this.selectedApplications.forEach(app => {
        let homologation = app.homologation.homologation;

        if (homologation && homologation.id) {
          if (!wvtaType.key.includes(homologation.id) && !mstaType.key.includes(homologation.id) && !noRegulationType.key.includes(homologation.id)) {
            foundNonAllowable = true;
          }
        } else {
          foundNonAllowable = true;
        }
      });

      return foundNonAllowable;
    }
  },

  created () {
    this.setUpdateAllowed();
    this.setFlags();
  },

  mounted () {
    document.getElementById('wltp-status-flag').indeterminate = this.indeterminedWltpState;
  },

  watch: {
    selectedApplications () {
      this.setUpdateAllowed();
      this.setFlags();
      this.updateWltpFlagAfterSelectionChange();
      if (this.deltaCDA) {
        this.formatWithPrecision(this.deltaCDA);
      }
    },

    indeterminedWltpState (newValue) {
      document.getElementById('wltp-status-flag').indeterminate = newValue;
    },

    wltpFlag (newValue, oldValue) {
      if (newValue) {
        this.hubFitmentFlag = newValue;
      }
    }
  },

  methods: {
    ...mapActions([
      'updateWltpFlag',
      'updateWltpFlagAfterSelectionChange',
      'updateHomologation'
    ]),
    ...mapMutations(['setIndeterminedWltpState']),

    setFlags () {
      this.setFlagValue('hubFitmentFlag');
      this.setFlagValue('deltaCDA');
    },

    preventedWltpFlagUpdate (event) {
      this.updateWltp(!this.wltpFlag);
    },

    setUpdateAllowed () {
      this.updateAllowed = this.selectedApplications.length > 0;
    },

    colourState (key, parent) {
      if (this.selectedApplications.length === 0) {
        return null;
      } else if (this.selectedApplications.length === 1) {
        return null;
      }

      return this.equalsPreviousElement(key, parent);
    },

    updateWltp (newValue) {
      if (newValue === true) {
        if (this.isNonAllowedHomologationType) {
          newValue = !newValue;
          this.$set(this, 'wltpFlag', newValue);
          // this.setIndeterminedWltpState = false;
        } else {
          this.$refs.confirmForceYesModal.show();
          this.changeHubFitmentValue(newValue);
          // this.updateWltpFlag(newValue);
        }
      }
      // if (this.previousWltpFlag !== null) {
      this.updateWltpFlag(newValue);
      // }
      // this.previousWltpFlag = newValue;
    },

    preventUpdateWltp (value) {
      console.log('poep', value);
    },

    equalsPreviousElement (key, parent) {
      for (let i = 1; i < this.selectedApplications.length; i++) {
        let current = this.selectedApplications[i][parent][key];
        let previous = this.selectedApplications[i - 1][parent][key];
        if (current !== previous) {
          return false;
        }
      }
      return true;
    },

    setFlagValue (type) {
      if (type === 'hubFitmentFlag') {
        this.showHubFitmentFlag = true;
      }
      if (this.selectedApplications.length === 1) {
        this[type] = this.getValue(this.selectedApplications[0].homologation[type], type);
      } else {
        let isEqual = this.equalsPreviousElement(type, 'homologation');
        if (isEqual) {
          this[type] = this.getValue(this.selectedApplications[0].homologation[type], type);
        } else {
          if (type === 'deltaCDA') {
            this.$set(this, type, null);
          } else if (type === 'hubFitmentFlag') {
            this.addIndeterminedHubFitment();
          }
        }
      }
    },

    getValue (value, type) {
      if (value) {
        return JSON.parse(JSON.stringify(value));
      }
      return null;
    },

    removeIndeterminateState () {
      let idx = this.hubFitmentOptions.findIndex(option => option.value === 3);
      if (idx !== -1) {
        this.hubFitmentOptions.pop();
      }
    },

    update () {
      if ((this.colourState('wltp', 'homologation') === false ||
        this.colourState('deltaCDA', 'homologation') === false ||
        this.colourState('hubFitmentFlag', 'homologation') === false) && this.hubFitmentFlag !== 3) {
        this.$refs.confirmUpdateModal.show();
      } else {
        let homologation = {};
        homologation.wltpFlag = this.wltpFlag;
        if (this.wltpFlag === true) {
          homologation.deltaCDA = this.deltaCDA;
        }
        homologation.generationCodes = this.selectedApplications[0].homologation.generationCodes;
        homologation.hubFitmentFlag = this.selectedApplications[0].homologation.hubFitmentFlag;
        this.updateHomologation(homologation);
      }
    },

    addIndeterminedHubFitment () {
      let idx = this.hubFitmentOptions.findIndex(option => option.value === 3);
      if (idx === -1) {
        this.hubFitmentOptions.push({ value: 3, text: '*****' });
      }
      this.hubFitmentFlag = 3;
    },

    ignoreUpdate () {
      console.log('Update has been canceled');
    },

    confirmUpdate () {
      // If value != null add to object
      let homologation = {};

      if (this.wltpFlag !== null) {
        homologation.wltpFlag = this.wltpFlag;
      }

      if (this.deltaCDA !== null && this.wltpFlag === true) {
        homologation.deltaCDA = this.deltaCDA;
      }

      if (!(this.colourState('hubFitmentFlag', 'homologation') === false && this.hubFitmentFlag !== 3)) {
        homologation.hubFitmentFlag = this.hubFitmentFlag;
      }

      this.updateHomologation(homologation);
    },

    changeHubFitmentValue (value) {
      if (value !== 3) {
        this.selectedApplications.forEach(app => {
          app.homologation.hubFitmentFlag = value;
        });
      }
      this.hubFitmentFlag = value;
    },

    validateDeltaCDAInput (value) {
      this.deltaCDAError = null;
      if (value.toString().length === 0) {
        this.deltaCDAError = 'Value cannot be returned to empty...';
        return;
      }

      if (isNaN(value)) {
        this.deltaCDAError = 'Must be a number...';
      } else {
        let maxLength = value.toString().indexOf('.') + 4;
        if (value.length > maxLength) {
          this.deltaCDAError = 'It is not allowed to have more then 3 decimal numbers. Leaving the inputfield automatically truncates the value...';
        }
      }
    },

    formatWithPrecision (value) {
      if (!isNaN(value)) {
        let decimalIdx = value.toString().indexOf('.');
        let newValueX = '';
        let precision = 0;

        if (decimalIdx === -1) {
          precision = value.toString().length + 4;
          newValueX = value + '.000';
        } else {
          precision = value.toString().indexOf('.') + 4;
          for (let i = 0; i < precision; i++) {
            let c = value[i];
            if (!c) {
              newValueX += '0';
            } else {
              newValueX += c;
            }
          }
        }

        this.deltaCDA = newValueX.substring(0, precision);

        for (let app of this.selectedApplications) {
          app.homologation.deltaCDA = this.deltaCDA;
        }
      } else {
        this.deltaCDAError = 'Must be a number...';
      }
    }
  }
};
</script>
<style scoped>
.simple-collapse.collapsed {
  flex: initial;
}
.simple-collapse.collapsed > div {
  display: none;
}
.delta-cda-validation {
  margin-top: 5px;
  font-family: Arial, Helvetica, sans-serif;
  color: red;
}
.undetermined-wltp {
  padding: 0;
  border: 1px solid red;
}
.determined-wltp {
  padding: 0;
  border: 1px solid green;
}
.wltp-error-message {
  margin: 1px;
  color: darkred;
  font-size: 11px;
}
</style>
