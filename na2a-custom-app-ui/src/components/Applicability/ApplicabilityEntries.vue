<template>
  <div>
    <div v-if="showStatus">
      <b-form-checkbox id="ae-cancelCheckbox"
                       v-model="showCancelled"
                       :value="true"
                       :unchecked-value="false">
        Show cancelled?
      </b-form-checkbox>
    </div>
    <table id="ae-summaryTable" class="table table-sm table-striped">
      <colgroup>
        <col>
        <col width="90">
      </colgroup>

      <thead>
        <tr id="ae-headerRow">
          <th>Applicability</th>
          <th v-if="items !== 1">Coverage</th>
          <th v-if="showCancelled">Status</th>
          <th v-if="showCancelled">Operations</th>
        </tr>
      </thead>

      <tbody v-if="summaries">
        <tr v-for="(entry, idx) of filterApplicabilities"
            :key="idx"
            :id="'ae-row-' + idx"
            :class="{'table-info':(entry.idx === current)}">
          <td :id="'ae-sectionData-' + idx">
            <span v-for="(section, sectionIdx) of entry.summary" :key="sectionIdx">
              <span v-if="sectionIdx">, </span>
              <strong>{{ section.type }}: </strong>
              <span>{{ section.value }}</span>
            </span>
          </td>

          <td :id="'ae-variantCount-' + idx" v-if="items !== 1" class="text-center">
            {{ entry.variantsCnt }} / {{ items }}
          </td>

          <td v-if="showStatus" :id="'ae-status-' +idx">
            <span v-if="entry.cancelled" v-b-tooltip.hover :title="getCancelledReason(entry.cancelledReason)">Cancelled</span>
            <span v-if="!entry.cancelled">Active</span>
          </td>

          <td v-if="showStatus" :id="'ae-btnGroup-' + idx">
            <b-btn-group>
              <b-btn :id="'ae-deleteEntry-' + idx"
                     :disabled="entry.cancelled"
                     size="sm"
                     variant="outline-secondary"
                     @click="remove(entry.idx)">
                <octicon name="trashcan"></octicon>
              </b-btn>
              <b-btn :id="'ae-editEntry-' + idx"
                     size="sm"
                     variant="outline-secondary"
                     @click="checkBeforeEdit(entry.idx)">
                <octicon name="pencil"></octicon>
              </b-btn>
            </b-btn-group>
          </td>
        </tr>
      </tbody>
    </table>
    <div>
      <b-modal id="checkchangeapplicability"
        @close="showModal = false"
        ref="refcheckchangeapplicability"
        centered
        title="Change of Brand/Model confirmation?"
        ok-title="Yes"
        cancel-title="Cancel"
        @ok="edit">
        <p class="my-4">This will replace the Criteria currently selected.
          Do you want to proceed?</p>
      </b-modal>
    </div>
  </div>
</template>

<script>
import Octicon from 'vue-octicon/components/Octicon';
import { summarize } from '@/lib/utils';

export default {
  components: {
    Octicon
  },

  data () {
    return {
      summaries: [],
      showCancelled: false,
      storedIdx: null
    };
  },

  props: {
    entries: {
      required: true
    },
    items: {
      type: Number,
      required: false
    },
    current: {
      type: Number,
      required: false
    },
    showStatus: Boolean
  },

  created () {
    this.updateSummaries();
  },

  methods: {
    updateSummaries () {
      if (Array.isArray(this.entries)) {
        this.$set(this, 'summaries', this.entries.map(entry => {
          return {
            idx: entry.idx,
            variantsCnt: Object.keys(entry.variantIds).length,
            summary: summarize(entry),
            cancelled: entry.cancelled,
            cancelledReason: entry.cancelledReason
          };
        }));
      } else {
        this.$set(this, 'summaries', null);
      }
    },

    remove (idx) {
      this.$emit('remove', idx);
    },

    checkBeforeEdit (idx) {
      this.storedIdx = idx;
      this.$refs.refcheckchangeapplicability.show();
    },

    edit (idx) {
      this.$emit('edit', this.storedIdx);
    },

    getCancelledReason (value) {
      if (this.cancelledReasons) {
        for (let reason of this.cancelledReasons) {
          if (reason.key === value) return reason.value;
        }
      }

      return value;
    }
  },

  computed: {
    filterApplicabilities () {
      if (this.showCancelled) {
        return this.summaries;
      } else {
        return this.summaries.filter(e => !e.cancelled);
      }
    },

    cancelledReasons () {
      return this.$store.state.Lookups.cancelledReasons;
    }
  },

  watch: {
    entries: {
      handler (newValue) {
        this.updateSummaries();
      },
      deep: true
    }
  }
};
</script>
