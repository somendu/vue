<template>
  <div id="wltp-design-main">
    <b-card title="Design" style="margin-top: 10px">
      <!-- Accessory Mass -->
      <div class="simple-collapse pt-3 mb-3 border-top" :class="{'collapsed': !showAccessoryMass}">
        <h5>
          <collapse-link v-model="showAccessoryMass" label="Accessory Mass"></collapse-link>
        </h5>
        <div id="accessory-mass-section" class="accessory-mass" :class="getAccessoryMassStyle">
          {{ accessoryMass }} grams
        </div>
      </div>
      <!-- Replacement Accessory -->
      <div class="simple-collapse pt-3 mb-3 border-top" :class="{'collapsed': !showReplacementAccessory}">
        <h5>
          <collapse-link v-model="showReplacementAccessory" label="Replacement accessory / Equipment removed"></collapse-link>
        </h5>
        <div id="replacement-accessory-section">
        <b-form-checkbox
          v-model="replacementAccessory"
          :checked="replacementAccessory"
          :indeterminate.sync="indeterminedReplacementAccessoryState"
          :disabled="disableReplacementAccessory || areAllControlsDisabled"
          >

      </b-form-checkbox>
        </div>
      </div>
      <!-- Delta mass -->
      <div class="simple-collapse pt-3 mb-3 border-top" :class="{'collapsed': !showDeltaMass}">
        <h5>
          <collapse-link v-model="showDeltaMass" label="Mass of removed equipment"></collapse-link>
        </h5>
        <div id="delta-mass-section">
          <div v-if="deltaMassError" class="alert alert-danger dm-alert">{{ deltaMassError }}</div>
          <b-container>
            <b-row>
              <b-col>
                <b-form-group
                  id="fieldset-1"
                  label="Mass of removed equipment"
                  labe-for="input-delta-mass">
                  <b-input-group append="kg">
                    <b-input
                      id="input-delta-mass"
                      append="kg"
                      v-model="deltaMass"
                      @input="processRounding"
                      :disabled="!replacementAccessory === null || replacementAccessory === false || areAllControlsDisabled || !getFieldState('deltaMass')"
                      :state="stateDeltaMassSelection"
                      :placeholder="getDeltaMassPlaceHolder"></b-input>
                  </b-input-group >
                </b-form-group>
              </b-col>
              <b-col>
                <b-form-group
                  id="fieldset-1"
                  label="Delta mass rounded"
                  label-for="input-delta-mass-rounded">
                  <b-input-group append="kg">
                    <b-input
                      id="input-delta-mass-rounded"
                      :value="calculatedDeltaMassRounded"
                      :state="stateDeltaMassSelection"
                      :placeholder="getDeltaMassPlaceHolder"
                      readonly></b-input>
                  </b-input-group>
                </b-form-group>
              </b-col>
            </b-row>
            <b-row>
              <b-col cols="1" md="1">
                <octicon name="italic" style="margin-top:5px"></octicon>
              </b-col>
              <b-col cols="9">
                <div style="margin-top:3px">
                    (If 0.9=0, If 1.4=1, If 1.5=2, Unit: kg)
                </div>
              </b-col>
            </b-row>
          </b-container>
        </div>
      </div>

      <!-- incalculable / coordinates -->
      <div class="simple-collapse pt-3 mb-3 border-top" :class="{'collapsed': !showCordinates}">
        <h5>
          <collapse-link v-model="showCordinates" label="L-Coordinates"></collapse-link>
        </h5>
        <div id="l-coordinates-section" style="margin-left:10px">
          <b-container>
            <b-row>
              <b-col>
                <!-- Incalculable -->
                <b-form-checkbox
                  id="incalculable"
                  :checked="incalculable"
                  v-model="incalculable"
                  @change="changeCalculable"
                  :disabled="deltaMass < 1 || areAllControlsDisabled"
                  :indeterminate.sync="indeterminedCalculableState"
                >Incalculable</b-form-checkbox>
              </b-col>
              <!-- L coordinate -->
              <b-col>
                L-Coordinates -> {{ indeterminedCalculableState }}
              </b-col>
              <b-col cols="3">
                <b-input
                  v-model="lCoordinates"
                  :disabled="deltaMass < 1 ||
                    (incalculable || incalculable === null || incalculable === undefined) ||
                    indeterminedCalculableState ||
                    areAllControlsDisabled ||
                    !getFieldState('lcoOrdinates')"
                  :state="stateLCoordinatesSelection"
                  :placeholder="stateLCoordinatesSelection ? '' : '*****'"></b-input>
                <div v-if="lCoordinatesError" style="margin: 5px;" class="alert alert-danger dm-alert">{{ lCoordinatesError }}</div>

              </b-col>
              <b-col cols="3">
                <b-input
                  v-model="lCoordinatesRounded"
                  :state="stateLCoordinatesSelection"
                  :placeholder="stateLCoordinatesSelection ? '' : '*****'"
                  readonly></b-input>
              </b-col>
              <b-col>
                mm
              </b-col>
            </b-row>
            <b-container>
              <b-row>
                <b-col cols="1" md="1">
                  <octicon name="italic" style="margin-top:5px"></octicon>
                </b-col>
                <b-col cols="9">
                  <div style="margin-top:3px">
                  (If 1005.8 = 1006, Unit: mm)
                  </div>
                </b-col>
              </b-row>
            </b-container>
          </b-container>
        </div>
      </div>
      <hr>
      <div id="update-section">
        <b-button
          id="btn-update"
          variant="outline-primary"
          :disabled="!updateAllowed || updatingDesign || areAllControlsDisabled || indeterminedWltpState || deltaMassError !== null || lCoordinatesError !== null"
          @click="update"
        ><octicon name="sync" :spin="updatingDesign"></octicon> Save</b-button>
      </div>
    </b-card>

    <b-modal id="wltp-design-confirm-replacement-modal"
      :no-close-on-backdrop="true"
      :no-close-on-esc="true"
      :hide-header-close="true"
      @close="showModal = false; replacement = previousReplacementValue"
      ref="checkreplacementchange"
      centered
      title="Are you sure?"
      ok-title="Yes"
      cancel-title="No"
      @cancel="processReplacementIgnore()"
      @ok="processReplacementConfirmation()">
      <p v-if="replacementAccessory === false" class="my-4">Delta mass will be overridden with the application mass. Do you want to proceed??</p>
      <p v-if="replacementAccessory === true" class="my-4">Delta mass will be editable. Do you want to proceed??</p>
    </b-modal>

    <b-modal id="wltp-design-confirm-update-modal"
      :no-close-on-backdrop="true"
      :no-close-on-esc="true"
      :hide-header-close="true"
      ref="confirmUpdateModal"
      centered
      title="Are you sure?"
      ok-title="Yes"
      cancel-title="No"
      @cancel="ignoreUpdate()"
      @ok="confirmUpdate()">
      <p class="my-4">Changes have been made to fields in an undetermined state. Do you want to continue?</p>
    </b-modal>
  </div>
</template>
<script>
import CollapseLink from '@/components/CollapseLink';
import GenerationCode from './GenerationCode.vue';
import Octicon from 'vue-octicon/components/Octicon';
import { mapGetters, mapActions } from 'vuex';

export default {
  components: {
    Octicon,
    CollapseLink,
    GenerationCode
  },

  data () {
    return {
      disableReplacementAccessory: true,
      areAllControlsDisabled: false,
      showReplacementAccessory: true,
      replacementAccessory: null,
      indeterminedReplacementAccessoryState: false,
      showDeltaMass: true,
      showAccessoryMass: true,
      showCordinates: true,
      showReplacementEquipmentSpec: true,
      replacement: null,
      deltaMass: null,
      deltaMassRounded: null,
      deltaMassError: null,
      incalculable: false,
      replacementOptions: [{ value: true, text: 'Yes' }, { value: false, text: 'No' }, { value: null, text: 'Not Selected' }],
      previousReplacementValue: null,
      lCoordinates: null,
      lCoordinatesRounded: null,
      lCoordinatesError: null,
      updateAllowed: false
    };
  },

  created () {
    this.evaluateApplications();
    this.setUpdateAllowed();
    this.setDeltaMassValue();
  },

  methods: {
    ...mapActions(['updateDesign']),
    evaluateApplications () {
      if (!this.wltpFlag) {
        this.$set(this, 'disableReplacementAccessory', true);
      } else {
        this.$set(this, 'disableReplacementAccessory', false);
      }
      this.evaluateReplacementAccessoryState();
      this.setIncalculable();
    },

    evaluateReplacementAccessoryState () {
      if (this.selectedApplications.length === 1) {
        let app = this.selectedApplications[0];

        if (app.design.replacementAccessory !== null && app.design.replacementAccessory !== undefined) {
          this.$set(this, 'indeterminedReplacementAccessoryState', false);
          this.replacementAccessory = app.design.replacementAccessory;
        } else {
          this.$set(this, 'indeterminedReplacementAccessoryState', true);
          this.replacementAccessory = false;
        }
      } else if (this.selectedApplications.length > 1) {
        let currentState = null;
        for (let i = 1; i < this.selectedApplications.length; i++) {
          currentState = this.selectedApplications[i].design.replacementAccessory;
          let previousState = this.selectedApplications[i - 1].design.replacementAccessory;

          if (currentState !== previousState) {
            currentState = null;
            break;
          }
        }

        if (currentState === null || currentState === undefined) {
          this.$set(this, 'indeterminedReplacementAccessoryState', true);
          this.replacementAccessory = false;
        } else {
          this.$set(this, 'indeterminedReplacementAccessoryState', false);
          this.replacementAccessory = currentState;
        }
      }
    },

    setUpdateAllowed () {
      this.updateAllowed = this.selectedApplications.length > 0;
    },

    changeCalculable (event) {
      this.selectedApplications.forEach(app => {
        app.design.incalculable = event;
      });
      this.setIncalculable();
    },

    // this methods sets the showable value. Not the value per application which is selected.
    setDeltaMassValue () {
      if (this.indeterminedReplacementAccessoryState) {
        this.deltaMass = null;
        this.deltaMassRounded = null;
        return;
      }

      // if values are equal show the equal value, otherwise create indetermined
      let isEqual = this.getFieldState('deltaMass');

      if (isEqual && this.replacementAccessory) {
        let currentDeltaMassValue = this.selectedApplications[0].design.deltaMass;
        let currentDeltaMassRoundedValue = this.selectedApplications[0].design.deltaMassRounded;

        this.deltaMass = currentDeltaMassValue;
        this.deltaMassRounded = currentDeltaMassRoundedValue;
      } else if (!isEqual && this.replacementAccessory) {
        this.deltaMass = null;
        this.deltaMassRounded = null;
      } else if (!isEqual && this.replacementAccessory) {
        this.selectedApplications.forEach(app => {
          app.design.deltaMass = 0;
          app.design.deltaMassRounded = 0;
        });
        this.deltaMass = 0;
        this.deltaMassRounded = 0;
      }
    },

    setIncalculable () {
      if (this.selectedApplications.length === 1) {
        this.incalculable = this.selectedApplications[0].design.incalculable;
        let lCoordinates = this.selectedApplications[0].design.lcoOrdinates;
        if (lCoordinates) {
          this.lCoordinates = lCoordinates;
        }
        return;
      }

      let currentState = null;
      for (let i = 1; i < this.selectedApplications.length; i++) {
        currentState = this.selectedApplications[i].design.incalculable;
        let previousState = this.selectedApplications[i - 1].design.incalculable;
        if (currentState !== previousState) {
          currentState = null;
          break;
        }
      }

      if (currentState === null || currentState === undefined) {
        this.incalculable = false;
      } else {
        this.incalculable = currentState;
      }
    },

    disableAllControls () {
      this.$set(this, 'disableReplacementAccessory', true);
      this.$set(this, 'areAllControlsDisabled', true);
    },

    processReplacementConfirmation () {
      this.selectedApplications.forEach(app => {
        app.design.replacementAccessory = true;
      });
      this.setDeltaMassValue();
    },

    processReplacementIgnore () {
      this.$set(this, 'replacementAccessory', this.previousReplacementValue);
    },

    processRounding (event) {
      if (this.deltaMass > 999.99) {
        this.$set(this, 'deltaMass', 999.99);
        this.$set(this, 'deltaMassError', 'DeltaMass is not allowed to exceed 999.99');
      }
    },

    roundNumber (value) {
      if (value <= 1.4) {
        return Math.floor(value);
      }
      return Math.round(value);
    },

    equalsPreviousElement (key, parent) {
      for (let i = 1; i < this.selectedApplications.length; i++) {
        let current = this.selectedApplications[i].design[key];
        let previous = this.selectedApplications[i - 1].design[key];
        if (current !== previous) {
          return false;
        }
      }
      return true;
    },

    getFieldState (key) {
      if (this.selectedApplications.length === 1) {
        return true;
      }
      return this.equalsPreviousElement(key);
    },

    isFieldEmpty (field) {
      switch (field) {
        case 'replacementAccessory':
          return this.replacementAccessory === undefined || this.replacementAccessory === null;
        case 'deltaMass':
          return this.deltaMass === undefined || this.deltaMass === null || this.deltaMass === '';
        case 'incalculable':
          return this.incalculable === undefined || this.incalculable === null;
        case 'lCoOrdinates':
          return this.lCoordinates === undefined || this.lCoordinates === null || this.lCoordinates === '';
        default:
          return true;
      }
    },

    update () {
      if (!this.isAllEqual) {
        this.$refs.confirmUpdateModal.show();
      } else {
        let design = {};
        design.replacementAccessory = !this.replacementAccessory === null ? null : this.replacementAccessory === true;
        if (this.replacementAccessory === true) {
          design.deltaMass = this.deltaMass;
          design.deltaMassRounded = this.deltaMassRounded;
          design.incalculable = this.incalculable;
          design.lCoordinates = this.lCoordinates;
          design.lCoordinatesRounded = this.lCoordinatesRounded;
        }
        design.replacementOfEquipments = this.selectedApplications[0].design.replacementOfEquipments;
        this.updateDesign(design);
      }
    },

    confirmUpdate () {
      // If value != null add to object
      let design = {};

      if (this.isFieldEmpty('replacement') === false) {
        design.replacementAccessory = this.replacementAccessory === true;
      }
      if (this.isFieldEmpty('deltaMass') === false && this.replacementAccessory === true) {
        design.deltaMass = this.deltaMass;
        design.deltaMassRounded = this.deltaMassRounded;
      }
      if (this.isFieldEmpty('incalculable') === false && this.replacementAccessory === true) {
        design.incalculable = this.incalculable;
      }
      if (this.isFieldEmpty('lCoOrdinates') === false && this.replacementAccessory === true) {
        design.lCoordinates = this.lCoordinates;
        design.lCoordinatesRounded = this.lCoordinatesRounded;
      }

      this.updateDesign(design);
    },

    ignoreUpdate () {
      this.evaluateReplacementAccessoryState();
      console.log('Update has been canceled');
    },

    compareAccessoryMasses () {
      for (let i = 1; i < this.selectedApplications.length; i++) {
        let current = this.selectedApplications[i].accessoryMass;
        let previous = this.selectedApplications[i - 1].accessoryMass;
        if (current !== previous) {
          return false;
        }
      }
      return true;
    }
  },

  computed: {
    ...mapGetters([
      'wltpFlag',
      'updatingDesign',
      'indeterminedWltpState'
    ]),

    indeterminedCalculableState () {
      if (this.selectedApplications.length === 1) {
        let app = this.selectedApplications[0];
        if (app.design.incalculable === null || app.design.incalculable === undefined) {
          return true;
        }
        return false;
      }

      let currentState = null;
      for (let i = 1; i < this.selectedApplications.length; i++) {
        currentState = this.selectedApplications[i].design.incalculable;
        let previousState = this.selectedApplications[i - 1].design.incalculable;
        if (currentState !== previousState) {
          currentState = null;
          break;
        }
      }

      if (currentState === null || currentState === undefined) {
        return true;
      }
      return false;
    },

    calculatedDeltaMassRounded () {
      if (this.deltaMass && !isNaN(this.deltaMass)) {
        return this.accessoryMass - this.roundNumber(this.deltaMassRounded);
      }
      if (isNaN(this.deltaMass) || this.deltaMass === '') {
        return '';
      }

      return '*****';
    },

    getDeltaMassPlaceHolder () {
      let equals = this.getFieldState('deltaMass');

      if (equals) {
        if (this.selectedApplications.length) {
          // if something is selected and they are all equal, show empty placeholder if value is something, else show '****'
          // if non editable
          let value = this.selectedApplications[0].design.deltaMass;
          if (value) {
            return '';
          }
          if (this.replacementAccessory) {
            return '';
          }
          return '*****';
        } else {
          return '';
        }
      }
      return '*****';
    },

    accessoryMass () {
      let isEqual = this.compareAccessoryMasses();

      if (isEqual) {
        if (this.selectedApplications.length) {
          return this.selectedApplications[0].accessoryMass;
        }
      }

      return '*****';
    },

    applications () {
      return this.$store.getters.applications;
    },

    selectedApplications () {
      return this.$store.getters.selectedApplications;
    },

    getAccessoryMassStyle () {
      if (this.selectedApplications.length < 2) {
        return 'no-border';
      }

      let isEqual = this.compareAccessoryMasses();

      if (isEqual) {
        return 'green-border';
      }
      return 'red-border';
    },

    stateReplacementSelection () {
      if (this.selectedApplications.length === 0) {
        return;
      }

      let allEqual = this.getFieldState('replacementAccessory');

      if (allEqual) {
        let value = this.selectedApplications[0].design.replacementAccessory;
        if (value === null) {
          this.$set(this, 'replacement', value);
        } else {
          this.$set(this, 'replacement', value ? 'yes' : 'no');
        }
      } else {
        this.$set(this, 'replacement', null);
      }
      return this.selectedApplications.length === 1 ? null : allEqual;
    },

    stateDeltaMassSelection () {
      if (this.selectedApplications.length === 0) {
        return;
      }

      let allEqual = this.getFieldState('deltaMass');
      return this.selectedApplications.length === 1 ? null : allEqual;
    },

    stateLCoordinatesSelection () {
      if (this.selectedApplications.length === 0) {
        return;
      }

      let allEqual = this.getFieldState('lcoOrdinates');

      if (allEqual) {
        this.$set(this, 'lCoordinates', this.selectedApplications[0].design.lcoOrdinates);
      } else {
        this.$set(this, 'lCoordinates', '');
      }
      return this.selectedApplications.length === 1 ? null : allEqual;
    },

    isAllEqual () {
      return this.getFieldState('replacementAccessory') &&
        this.getFieldState('deltaMass') &&
        this.getFieldState('lCoOrdinates') &&
        this.getFieldState('incalculable');
    }
  },

  watch: {
    wltpFlag (newValue) {
      this.areAllControlsDisabled = false;

      if (newValue) {
        this.disableAllControls();
        this.$set(this, 'areAllControlsDisabled', false);
        this.evaluateApplications();
        this.setDeltaMassValue();
      } else {
        this.disableAllControls();
      }
    },

    incalculable (newValue) {
      if (newValue) {
        this.lCoordinates = '';
        this.lCoordinatesRounded = '';
      }
    },

    selectedApplications () {
      if (this.selectedApplications === 0) {
        this.disableAllControls();
      }
      this.evaluateApplications();
      this.setDeltaMassValue();
    },

    lCoordinates (newValue) {
      this.lCoordinatesError = null;
      if (newValue && !isNaN(newValue)) {
        if (newValue > 10000) {
          this.lCoordinatesError = 'Cannot exceed 10000';

          return;
        }

        let rounded = this.roundNumber(newValue);
        this.lCoordinatesRounded = rounded;
        this.selectedApplications.forEach(app => {
          app.design.lcoOrdinates = newValue;
        });
      } else {
        this.lCoordinatesRounded = '';
      }
    },

    replacementAccessory (newValue, oldValue) {
      if (newValue) {
        if (this.selectedApplications.length) {
          let state = this.selectedApplications[0].design.replacementAccessory;
          let deltaMass = this.selectedApplications[0].design.deltaMass;
          if (!state && !deltaMass) {
            this.previousReplacementValue = oldValue;
            this.$refs.checkreplacementchange.show();
          }
        }
      }
      this.setDeltaMassValue();
    },

    deltaMass (newValue) {
      this.deltaMassError = null;
      if (isNaN(newValue)) {
        this.deltaMassError = 'Delta mass should be a number';
      } else {
        if (newValue < 0) {
          this.deltaMassError = 'Delta Mass cannot be negative';
          return;
        }

        let isEqual = this.getFieldState('deltaMass');
        if (this.replacementAccessory && isEqual) {
          this.selectedApplications.forEach(app => {
            app.design.deltaMass = newValue;
            let roundedValue = this.roundNumber(newValue);
            app.design.deltaMassRounded = roundedValue;
            this.deltaMassRounded = roundedValue;
          });
        }
      }
    }
  }
};
</script>
<style scoped>
.deltamass-input-wrapper {
  display: flex;
  flex-direction: row;
  margin-left: 10px;
}
.deltamass-input-content {
  flex: 1;
  flex-direction: row;
  margin-right: 5px;
  max-width: 200px;
}

.dm-alert {
  display:inline;
  padding:3px 3px;
  margin: 5px;
}
.simple-collapse.collapsed {
  flex: initial;
}
.simple-collapse.collapsed > div {
  display: none;
}

.accessory-mass {
  border-radius: 25px;
  margin: 1%;
  padding: 1px;
  width: 75px;
  text-align: center;
}

.red-border {
  /* padding: 0; */
  border: 1px solid red;
}
.green-border {
  /* padding: 0; */
  border: 1px solid green;
}

.no-border {
  padding: 0;
  border: 0px;
}
</style>
