<template>
  <div>
    <div id="lp-buttonView">
      <b-button-group size="sm">
        <b-button :id="idText" :disabled="buttonDisabled" @click="openModal()" variant="outline-primary"> {{ buttonText }}</b-button>
      </b-button-group>
    </div>
      <b-modal
        id="lp-verifyModal"
        ref="verifyModal"
        centered
        title="Are you sure?"
        ok-title="Yes"
        cancel-title="No"
        @ok="preSave()"
    >
      <p class="modal-alignment">Are you sure you want to {{ buttonText }}?</p>
    </b-modal>
  </div>
</template>

<script>
export default {
  props: {
    commonParts: {
      type: Array,
      required: true
    },
    nonCommonParts: {
      type: Array,
      required: true
    },
    isLinking: {
      type: Boolean,
      required: true
    }
  },
  data () {
    return {
      selectedParts: [],
      buttonText: String
    };
  },
  created () {
    this.buttonText = this.isLinking ? 'Link' : 'De-Link';
    this.idText = this.isLinking ? 'pl-linkParts' : 'pl-delinkParts';
  },
  computed: {
    partsSelected () {
      return this.selectedParts.length >= 0;
    },

    buttonDisabled () {
      // Disable linking button if no commons parts selected
      if (this.isLinking === true) {
        return this.nonCommonParts.length === 0;
      } else {
        return this.commonParts.length === 0;
      }
    }
  },
  methods: {
    openModal () {
      this.$refs.verifyModal.show();
    },

    preSave () {
      this.$refs.verifyModal.hide();
      this.$emit('update', this.isLinking, this.selectedParts);
    }
  },
  watch: {
    commonParts: {
      handler (newValue) {
        // make a shallow copy
        const entries = [];
        if (newValue) {
          for (const entry of newValue) {
            entries.push({
              ...entry
            });
          }
        }
        this.$set(this, 'selectedParts', entries);
      },
      deep: true
    },
    nonCommonParts: {
      handler (newValue) {
        // make a shallow copy
        const entries = [];
        if (newValue) {
          for (const entry of newValue) {
            entries.push({
              ...entry
            });
          }
        }
        this.$set(this, 'selectedParts', entries);
      },
      deep: true
    }
  }
};
</script>

<style scoped>
.modal-alignment {
  text-align: left;
}
</style>
