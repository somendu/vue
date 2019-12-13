<template>
    <div id="task-view">
        <na2a-header title="Task Manager"></na2a-header>
        <p id="loading-task" class="loading" v-if="loading">Loading Task</p>

        <div class="task-wrapper" v-if="!loading">
            <div class="task-item simple-collapse" :class="{'collapsed': !showItem}">
                <h4>
                    <collapse-link v-model="showItem" :label="'Task: ' + details.task.name"></collapse-link>
                </h4>
                <task-overview :details="details.task"
                                @save="save"
                                v-model="details.task"></task-overview>
            </div>

            <div id="tsk-content" class="task-content">
                <div class="task-left-content">
                    <div class="task-ssn-selection simple-collapse" :class="{'collapsed': !showSsn}">
                        <h4>
                            <collapse-link v-model="showSsn" label="SSN Selection"></collapse-link>
                        </h4>
                        <ssn-selection :loading="loadingApplications"
                                       @performRequest="getApplicationsAndParts">
                        </ssn-selection>
                    </div>
                    <div class="task-applications simple-collapse" :class="{'collapsed': !showApplications}">
                        <h4>
                            <collapse-link v-model="showApplications" label="Applications"></collapse-link>
                        </h4>
                        <application-entries
                            :applications="details.applications"
                            v-model="details.applications"></application-entries>
                    </div>
                    <div class="task-selection simple-collapse" :class="{'collapsed': !showParts}">
                        <h4>
                            <collapse-link v-model="showParts" label="Parts"></collapse-link>
                        </h4>
                        <parts-view :details="details.applications"></parts-view>
                    </div>
                </div>

                <div class="task-right-content">
                    <div class="task-summary simple-collapse" :class="{'collapsed': !showSummary}">
                        <h4>
                            <collapse-link v-model="showSummary" label="Summary"></collapse-link>
                        </h4>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import Na2aHeader from './Header/Na2aHeader';
import CollapseLink from './CollapseLink';
import SsnSelection from './Task/SsnSelection';
import ApplicationEntries from './Task/ApplicationEntries';
import PartsView from './Task/PartsView';
import TaskOverview from './Task/TaskOverview';
import axios from 'axios';
import eventbus from '@/eventbus';

export default {
  components: {
    Na2aHeader,
    CollapseLink,
    SsnSelection,
    ApplicationEntries,
    PartsView,
    TaskOverview
  },

  props: {
    id: {
      type: String,
      required: true
    }
  },

  data () {
    return {
      loading: true,
      loadingApplications: false,
      showItem: true,
      showSsn: true,
      showApplications: true,
      showParts: true,
      showSummary: true,
      details: {
        task: {
          name: '',
          description: '',
          creationDate: '',
          escalationDate: '',
          creationUser: '',
          deadline: '',
          status: '',
          isPack: false
        },
        applications: []
      },
      defaultTask: {
        name: '',
        description: '',
        creationDate: '',
        escalationDate: '',
        creationUser: '',
        deadline: '',
        status: '',
        isPack: false
      },
      updating: false
    };
  },

  created () {
    this.load();
  },

  methods: {
    load () {
      if (this.id === '0') {
        this.details.task = this.defaultTask;
        this.loading = false;
      } else {
        this.loadTask();
      }
    },

    loadTask () {
      axios
        .get(`/api/task/${this.id}`)
        .then(res => {
          this.$set(this.details, 'task', res.data);
          this.loading = false;
        })
        .catch(err => {
          this.loading = false;
          eventbus.$emit('showMessage', {
            title: 'Error on getting task details...',
            err
          });
        });
    },

    save () {
      if (!this.isTaskValid()) {
        return;
      };

      this.updating = true;
      axios
        .put(`/api/task/${this.id}`, this.details)
        .then(res => {
          alert('The to be saved data will not be processed. Please check status of ticket NAA-776');
        });
    },

    isTaskValid () {
      if (this.details.task.name === null || this.details.task.name === '') {
        eventbus.$emit('showMessage', {
          title: "Can't save task",
          err: 'Name of task is incorrect. It cannot be unspecified.'
        });

        return false;
      }
      return true;
    },

    getApplicationsAndParts (selection) {
      this.loadingApplications = true;

      axios.get(`/api/task/applications?brand=${selection.brand}&model=${selection.model}&project=` + encodeURIComponent(selection.project))
        .then(res => {
          res.data.forEach(application => { application.selected = false; });
          this.$set(this.details, 'applications', res.data);
          this.loadingApplications = false;
        })
        .catch(err => {
          this.loadingApplications = false;
          eventbus.$emit('showMessage', {
            title: 'Error on getting applications and part details...',
            err
          });
        });
    }
  }
};
</script>

<style scoped>
.task-wrapper {
    height: 90vh;
    display: flex;
    flex-direction: column;
    margin-left: 5px;
}

.task-item {
    flex: 1 -1 auto;
}

.task-content {
    flex: 1 -1 auto;
    max-height: inherit;
    display: flex;
    flex-direction: row;
}

.task-left-content {
    max-height: inherit;
    height: 100%;
    overflow-y: auto;
    flex: 4;
    flex-direction: column;
}

.task-right-content {
    max-height: inherit;
    overflow-y: auto;
    flex: 1;
    flex-direction: column;
    text-align: left;
}

.simple-collapse.collapsed {
  flex: initial;
}
.simple-collapse.collapsed > div {
  display: none;
}
</style>
