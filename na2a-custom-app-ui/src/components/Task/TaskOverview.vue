<template>
   <div id="task-overview">
       <div class="task-overview-grid">
           <div class="task-view-content-row-1">
                <div class="task-view-content-column">
                    <div class="task-view-content-row-1">Name:</div>
                    <div class="task-view-content-row-2"><input type="text" id="tsk-task-input-name" v-model="details.name" class="input-field"/></div>
                </div>
                <div class="task-view-content-column">
                    <div class="task-view-content-row-1">Description:</div>
                    <div class="task-view-content-row-2"><input type="text" id="tsk-task-input-description" v-model="details.description" class="input-field"/></div>
                </div>
                <div class="task-view-content-column">
                    <div class="task-view-content-row-1">Created By:</div>
                    <div class="task-view-content-row-2">{{ details.creationUser }}&nbsp;</div>
                </div>
                <div class="task-view-content-column">
                    <div class="task-view-content-row-1">Created on:</div>
                    <div class="task-view-content-row-2">{{ creationDateTransformation }}&nbsp;</div>
                </div>
           </div>
          <div class="task-view-content-row-1">
                <div class="task-view-content-column">
                    <div class="task-view-content-row-1">Escalation on:</div>
                    <div class="task-view-content-row-2">{{ escalationDateTransformation }}</div>
                </div>
                <div class="task-view-content-column">
                    <div class="task-view-content-row-1">Time expires on:</div>
                    <div class="task-view-content-row-2">{{ deadlineDataTransformation }}</div>
                </div>
                <div class="task-view-content-column">
                    <div class="task-view-content-row-1">Status:</div>
                    <div class="task-view-content-row-2">{{ details.status }}</div>
                </div>
                <div class="task-view-content-column">
                    <div class="task-view-content-row-1">Pack:</div>
                    <div class="task-view-content-row-2">
                        <b-form-checkbox id="tsk-packCheckbox"
                               v-model="details.isPack"
                               :value="true"
                               :unchecked-value="false">
                        </b-form-checkbox>
                    </div>
                </div>
           </div>
       </div>
       <div class="task-overview-buttons">
            <b-button id="tsk-overview-save" class="task-button" size="sm" @click="save">Save</b-button>
            <b-button id="tsk-overview-review" class="task-button" size="sm">Send Review</b-button>
       </div>
       <hr>
   </div>
</template>

<script>
export default {
  props: {
    details: {
      type: Object,
      required: true
    }
  },

  methods: {
    save () {
      if (this.details.isPack === null) {
        this.details.isPack = false;
      }
      this.$emit('save');
    },

    transformDate (taskDate) {
      if (taskDate === null || taskDate === '') {
        return 'N/A';
      }

      let date = new Date(taskDate.substring(0, 19));
      return date.toLocaleString('en-US', { timeZone: 'UTC' });
    }
  },

  computed: {
    creationDateTransformation () {
      return this.transformDate(this.details.creationDate);
    },

    escalationDateTransformation () {
      return this.transformDate(this.details.escalationDate);
    },

    deadlineDataTransformation () {
      return this.transformDate(this.details.deadline);
    }
  }
};
</script>

<style scoped>
.task-overview {
    display: flex;
    flex-direction: column;
}

.task-overview-grid {
    display: flex;
    flex-direction: row;
}

.task-view-content {
    flex-direction: row;
}

.task-view-content-column {
    display: flex;
    flex-direction: row;
}

.task-view-content-row-1 {
    flex-direction: column;
    flex: 1;
    font-weight: bold;
}

.task-view-content-row-1 > div {
    margin: 7px;
}

.task-view-content-row-2 {
    flex-direction: column;
    flex: 2;
    font-weight: normal;
}

.task-view-content-row-2 > div {
    margin: 7px;
}

.task-overview-buttons {
    flex-direction: row;
    text-align: right;
    margin-right: 15px;
}

.task-overview-buttons > button {
    margin: 15x;
}

.task-button {
    margin-right: 10px;
}

.input-field {
  max-width: 100%;
}
</style>
