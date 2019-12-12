<template>
  <div class="gc-wrapper">
    <div v-if="isEditable && tabIndex === 0 && !isAllDisabled">
      <b-btn style="border: none"
        size="sm"
        variant="outline-secondary"
        @click="addGenerationCode()">
        <octicon id="addGenerationCode" name="plus" scale="0.75" style="cursor:pointer" ></octicon>
      </b-btn>
    </div>
    <div>
      <b-tabs v-model="tabIndex">
        <b-tab title="Consolidated">
          <b-table v-if="equalsPreviousElement('generationCodes', 'homologation')"
            :style="borderStyle"
            striped
            :fields="tableFields"
            :items="generationCodes"
            :sort-by.sync="sortBy"
            :sort-desc.sync="sortDesc"
            >
            <template slot="type" slot-scope="row">
              <b-form-select v-model="row.item.type" :disabled="!isEditable">
               <option :value="null">Please select an option</option>
               <option value="WVTA">WVTA</option>
               <option value="MSTA">MSTA</option>
              </b-form-select>
            </template>

            <template slot="from" slot-scope="row">
              <b-form-select :id="'gc-from-select-' + row.index" v-model="row.item.from" :disabled="!isEditable">
                <option v-for="(character, idx) of fromToKeyValues" v-if="character.key !== 26"
                  :key="idx" :value="character.value"
                  :id="'gc-from-select-' + character.value.toLowerCase() + '-' + row.index">
                    {{ character.value }}
                </option>
              </b-form-select>
            </template>

            <template slot="to" slot-scope="row">
              <b-form-select :id="'gc-to-select-' + row.index" class="form-control mr-4" v-model="row.item.to" :disabled="!isEditable || row.item.from === null">
                <option v-for="(character, idx) of fromToKeyValues"
                  :key="idx" :value="character.value"
                  :id="'gc-to-select-' + character.value.toLowerCase() + '-' + row.index">
                    {{ character.value }}
                  </option>
              </b-form-select>
            </template>

            <template slot="r" slot-scope="row">
              <b-button
                size="sm"
                variant="outline-secondary"
                @click="removeGenerationCodes(row.item)"
                :disabled="!isEditable">
                <octicon name="trashcan"></octicon>
              </b-button>
            </template>
          </b-table>
          <b-table v-if="!equalsPreviousElement('generationCodes', 'homologation')"
            :style="borderStyle"
            striped
            :fields="tableFields"
            :items="mockItems"
            >
            <template slot="type" slot-scope="row">
              <b-form-select class="form-control mr-4" disabled v-model="row.item.type"><option>*****</option></b-form-select>
            </template>

            <template slot="from" slot-scope="row">
              <b-form-select class="form-control mr-4" disabled v-model="row.item.from"><option>*****</option></b-form-select>
            </template>

            <template slot="to" slot-scope="row">
              <b-form-select class="form-control mr-4" disabled v-model="row.item.to"><option>*****</option></b-form-select>
            </template>
          </b-table>
          <div style="margin-top:5px;" align="center" v-if="error.state">
            <octicon name="italic"></octicon><span style="margin-left: 5px;">{{ error.message }}</span>
          </div>
        </b-tab>

        <b-tab title="Individual">
          <table class="table table-sm table-striped">
            <thead>
              <tr>
                <td>Vehicle Generation</td>
                <td>Type</td>
                <td>From</td>
                <td>To</td>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(gc, idx) of individualGenerationCodes" :key="idx">
                <td> {{ isVehicleGenShowable(individualGenerationCodes, idx) === true ? gc.vehicleGeneration : '' }} </td>
                <td> {{ gc.type }} </td>
                <td> {{ gc.from }} </td>
                <td> {{ gc.to }} </td>
              </tr>
            </tbody>
          </table>
        </b-tab>
      </b-tabs>
    </div>
    <b-modal ref="gcfromerror" title="Error" ok-only>
      <div class="modal-body">
        <p>Can't set a <i>to</i> value if the <i>from</i> is not set.</p>
      </div>
    </b-modal>
  </div>
</template>

<script>
import Octicon from 'vue-octicon/components/Octicon';
import { mapGetters, mapActions, mapMutations } from 'vuex';

export default {
  components: {
    Octicon
  },

  props: {
    isAllDisabled: {
      type: Boolean,
      required: true
    }
  },

  data () {
    return {
      tableFields: [
        { key: 'type', sortable: false },
        { key: 'from', sortable: false },
        { key: 'to', sortable: false },
        { key: 'r', thClass: 'd-none', sortable: false }
      ],
      mockItems: [
        { type: '*****', from: '*****', to: '*****' }
      ],
      isEditable: Boolean,
      borderStyle: '',
      fromToKeyValues: [
        { key: 0, value: 'A', state: true },
        { key: 1, value: 'B', state: true },
        { key: 2, value: 'C', state: true },
        { key: 3, value: 'D', state: true },
        { key: 4, value: 'E', state: true },
        { key: 5, value: 'F', state: true },
        { key: 6, value: 'G', state: true },
        { key: 7, value: 'H', state: true },
        { key: 8, value: 'I', state: true },
        { key: 9, value: 'J', state: true },
        { key: 10, value: 'K', state: true },
        { key: 11, value: 'L', state: true },
        { key: 12, value: 'M', state: true },
        { key: 13, value: 'N', state: true },
        { key: 14, value: 'O', state: true },
        { key: 15, value: 'P', state: true },
        { key: 16, value: 'Q', state: true },
        { key: 17, value: 'R', state: true },
        { key: 18, value: 'S', state: true },
        { key: 19, value: 'T', state: true },
        { key: 20, value: 'U', state: true },
        { key: 21, value: 'V', state: true },
        { key: 22, value: 'W', state: true },
        { key: 23, value: 'X', state: true },
        { key: 24, value: 'Y', state: true },
        { key: 25, value: 'Z', state: true },
        { key: 26, value: 'Infinity', state: true }
      ],
      error: {
        state: false,
        message: ''
      },
      sortBy: 'from',
      sortDesc: false,
      localApplications: [],
      generationCodes: [],
      tabIndex: 0
    };
  },

  created () {
    this.isEditable = this.wltpFlag === true;

    this.generationCodes = this.getGenerationCodes();
    this.validate();
    this.getBorder();
    this.updateAvailableChars();
  },

  methods: {
    ...mapActions(['updateGenerationCodes', 'removeGenerationCode']),
    ...mapMutations(['setValidationError', 'setGenerationCodesEqual']),

    equalsPreviousElement () {
      let isEqual = true;

      // loop over each selected application
      this.selectedApplications.forEach(currentApplication => {
        let currentGenerationCodes = currentApplication.homologation.generationCodes;
        // then loop over each selected application except for the current application
        this.selectedApplications.forEach(otherApplication => {
          if (otherApplication.id !== currentApplication.id) {
            let otherGenerationCodes = otherApplication.homologation.generationCodes;

            if (otherGenerationCodes.length !== currentGenerationCodes.length) {
              isEqual = false;
              return;
            }

            for (let i in currentGenerationCodes) {
              let current = currentGenerationCodes[i];
              let other = otherGenerationCodes[i];

              if (current.type !== other.type || current.to !== other.to || current.from !== other.from) {
                isEqual = false;
                break;
              }
            }
          }
        });
      });
      this.setGenerationCodesEqual(isEqual);
      return isEqual;
    },

    getEditableStatus () {
      if (this.wltpFlag === false) {
        return false;
      }

      return this.equalsPreviousElement('generationCodes', 'homologation');
    },

    getBorder () {
      if (this.selectedApplications.length === 0 || this.selectedApplications.length === 1) {
        this.borderStyle = '';
      } else {
        this.borderStyle = this.equalsPreviousElement('generationCodes', 'homologation') === true ? 'border: 1px solid green' : 'border: 1px solid red';
      }
    },

    addGenerationCode () {
      let newGenerationCode = {};

      if (this.generationCodes.length === 0) {
        newGenerationCode = {
          type: null,
          to: null,
          from: null
        };
      } else {
        let lastGc = this.generationCodes[this.generationCodes.length - 1];
        if (lastGc.type === null || lastGc.from === null || lastGc.to === null) {
          this.error = {
            state: true,
            message: 'Please select a TO, FROM and Type value first.'
          };
          return;
        }

        let fullRangeTaken = this.fromToKeyValues[0].state === false && this.fromToKeyValues[this.fromToKeyValues.length - 1].state === false;

        if (fullRangeTaken) {
          this.error = {
            state: true,
            message: 'Cannot add row as full range is already taken!'
          };
          return;
        }

        let unavailableChars = this.fromToKeyValues.filter(ac => ac.state === false);
        let lastAvailable = unavailableChars[unavailableChars.length - 1];
        let lastAvailableIdx = lastAvailable.key;
        lastAvailableIdx++;

        let kv = this.fromToKeyValues.find(element => element.key === lastAvailableIdx);

        newGenerationCode = {
          type: null,
          to: null,
          from: kv.value
        };
      }

      this.selectedApplications.forEach(app => {
        app.homologation.generationCodes.push(newGenerationCode);
      });

      this.generationCodes = this.getGenerationCodes();
    },

    isVehicleGenShowable (individualGenerationCodes, idx) {
      if (idx === 0) {
        return true;
      }
      if (individualGenerationCodes[idx].vehicleGeneration === individualGenerationCodes[idx - 1].vehicleGeneration) {
        return false;
      }
      return true;
    },

    validate () {
      this.error = {
        state: false,
        message: ''
      };
      this.setValidationError({ value: false, generationCodes: true });

      if (!this.generationCodes) {
        return;
      }

      if (this.generationCodes.length === 0) {
        this.error = {
          state: true,
          message: 'At least one active generation code exists'
        };
        this.setValidationError({ value: false, generationCodes: true });
        return;
      }

      for (let i in this.generationCodes) {
        let gc = this.generationCodes[i];
        let fromKv = this.fromToKeyValues.find(kv => kv.value === gc.from);
        let toKv = this.fromToKeyValues.find(kv => kv.value === gc.to);

        if (toKv && fromKv && toKv.key < fromKv.key) {
          this.error = {
            state: true,
            message: 'TO value cannot be smaller then FROM value'
          };
          this.setValidationError({ value: true, generationCodes: true });
          break;
        }

        for (let j in this.generationCodes) {
          if (i !== j) {
            let otherGc = this.generationCodes[j];
            let otherFromKv = this.fromToKeyValues.find(kv => kv.value === otherGc.from);

            if (fromKv && toKv && toKv.key >= otherFromKv.key && fromKv.key <= otherFromKv.key) {
              this.error = {
                state: true,
                message: 'Periods cannot overlap...'
              };
              this.setValidationError({ value: true, generationCodes: true });
            }
          }
        }
      }
    },

    getGenerationCodes () {
      if (this.selectedApplications.length === 0) {
        return [];
      }

      if (this.selectedApplications.length === 1) {
        return this.selectedApplications[0].homologation.generationCodes;
      }

      if (this.selectedApplications.length > 1) {
        if (this.equalsPreviousElement('generationCodes', 'homologation')) {
          return JSON.parse(JSON.stringify(this.applications[0].homologation.generationCodes));
        }
      }
    },

    removeGenerationCodes (item) {
      for (let application of this.localApplications) {
        let idx = application.homologation
          .generationCodes.findIndex(gc => {
            return gc.from === item.from && gc.to === item.to;
          });

        if (idx !== -1) {
          application.homologation.generationCodes.splice(idx, 1);
        }
      }

      this.removeGenerationCode(item);

      this.generationCodes = this.getGenerationCodes();
    },

    updateAvailableChars () {
      this.fromToKeyValues.forEach(ac => {
        let found = this.generationCodes.find(gc => { return ac.value === gc.from || ac.value === gc.to; });

        if (found) {
          ac.state = false;
        } else {
          ac.state = true;
        }
      });
    }
  },

  computed: {
    ...mapGetters([
      'selectedApplications',
      'wltpFlag',
      'applications']),

    individualGenerationCodes () {
      let individualGenerationCodes = [];

      this.selectedApplications.forEach(app => {
        app.homologation.generationCodes.forEach(gc => {
          let tempObj = Object.create(gc);
          tempObj.vehicleGeneration = app.vehicleGeneration;
          individualGenerationCodes.push(tempObj);
        });
      });

      return individualGenerationCodes;
    }
  },

  watch: {
    wltpFlag () {
      this.$set(this, 'isEditable', this.getEditableStatus());
    },

    generationCodes: {
      handler (newValue, oldValue) {
        if (!oldValue || !newValue || !oldValue.length) return;

        for (let i in this.selectedApplications) {
          this.selectedApplications[i].homologation.generationCodes = JSON.parse(JSON.stringify(newValue));
        }
        this.validate();
        this.updateAvailableChars();
      },
      deep: true
    },

    selectedApplications: {
      handler (newValue, oldValue) {
        if (newValue.length !== oldValue.length) {
          this.generationCodes = this.getGenerationCodes();
        }
        this.getBorder();
        this.isEditable = this.getEditableStatus();
        this.validate();
        this.updateAvailableChars();
      }
    }
  }
};
</script>

<style scoped>
.gc-wrapper {
    display: flex;
    flex-direction: column;
}
</style>
