<template>
    <div>
        <b-modal
            id="indeterminedAccessoryModal"
            ref="indeterminedAccessoryModal"
            centered
            title="Accessory in indetermined state"
            ok-title="Yes"
            cancel-title="No"
            @cancel="ignoreIndeterminedAccessory()"
            @ok="updateIndeterminedAccessory()"
        >
            <p class="my-4">The accessory only applies to some of the selected vehicles. This will apply the accessory to all selected vehicles. Do you want to proceed?</p>
        </b-modal>
    </div>
</template>
<script>
export default {
  props: {
    showModal: {
      type: Boolean,
      required: false,
      default: false
    }
  },

  methods: {
    updateIndeterminedAccessory () {
      console.log('update indetermined accessory');
      this.$emit('updateIndeterminedAccessory');
      this.emitClearShowModal();
    },

    ignoreIndeterminedAccessory () {
      this.$emit('ignoreIndeterminedAccessory');
      this.emitClearShowModal();
    },

    emitClearShowModal () {
      this.$emit('clearShowModel');
    }
  },

  watch: {
    showModal (newValue) {
      if (newValue === true) {
        this.showModal = newValue;
        this.$refs.indeterminedAccessoryModal.show();
      }
    }
  }
};
</script>
<style scoped>
</style>
