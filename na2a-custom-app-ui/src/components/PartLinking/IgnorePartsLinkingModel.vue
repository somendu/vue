<template>
    <div class="modal-container">
        <b-modal
            id="ignorePartsLinkingModal"
            ref="ignorePartsLinkingModalRef"
            centered
            title="Part(s) referenced by all selected applications"
            ok-title="ok"
            ok-only
            @ok="continueSave"
            :no-close-on-backdrop="true"
            :no-close-on-esc="true"
            :hide-header-close="true"
            >
            <p class="my-4">
                The following parts are already referenced by all selected applications and will be ignored:
                <ul>
                    <li v-for="(ignoredPart, idx) in ignoredParts" :key="idx">
                      {{formatIgnoredPartLabel(ignoredPart)}}
                    </li>
                </ul>
            </p>
        </b-modal>
    </div>
</template>
<script>
import { partTypeName } from '@/lib/utils';
export default {
  props: {
    show: {
      type: Boolean,
      required: false,
      default: false
    },
    ignoredParts: {
      type: Array,
      required: false
    }
  },

  methods: {
    continueSave () {
      this.$emit('saveLinkedParts');
      this.emitClearShowModal();
    },

    emitClearShowModal () {
      this.$emit('clearShowIgnoredLinkedPartsModel');
    },

    formatIgnoredPartLabel (part) {
      return part.partName + ' - [Number] ' + part.partNumber + ' [Type] ' + partTypeName(part.type);
    }
  },

  watch: {
    show (newValue) {
      if (newValue === true) {
        this.show = newValue;
        this.$refs.ignorePartsLinkingModalRef.show();
      }
    }
  }
};
</script>
<style scoped>

</style>
