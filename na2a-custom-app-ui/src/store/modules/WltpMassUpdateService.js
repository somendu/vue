// import Vue from 'vue';
import axios from 'axios';
import eventbus from '@/eventbus';

const state = {
  path: '',
  applications: [],
  loading: false,
  updatingHomologation: false,
  updatingDesign: false,
  indeterminedWltpState: false,
  validationError: {
    generationCodes: false,
    replacementOfEquipments: false
  },
  wltpFlag: null,
  areGenerationCodesEqual: false,
  homologationTypes: []
};

const getters = {
  path: state => state.path,
  applications: state => state.applications,
  loading: state => state.loading,
  updatingHomologation: state => state.updatingHomologation,
  updatingDesign: state => state.updatingDesign,
  selectedApplications: state => state.applications.filter(application => application.selected === true),
  validationError: state => state.validationError,
  wltpFlag: state => state.wltpFlag,
  indeterminedWltpState: state => state.indeterminedWltpState,
  homologationTypes: state => state.homologationTypes,
  currentDeltaCDA: state => {
    const selectedApplications = state.applications.filter(application => application.selected === true);

    if (selectedApplications && selectedApplications.length > 0) {
      const appsWithDeltaCDA = selectedApplications.filter(application => application.homologation && application.homologation.deltaCDA);
      const appsWithoutDeltaCDA = selectedApplications.filter(application => !application.homologation || !application.homologation.deltaCDA);

      if (appsWithDeltaCDA && appsWithDeltaCDA.length > 0 && appsWithoutDeltaCDA && appsWithoutDeltaCDA.length > 0) {
        // At this point we have applications with and without deltaCDA values => indeterminate state
        return null;
      }

      if (appsWithoutDeltaCDA && appsWithoutDeltaCDA.length > 0 && (!appsWithDeltaCDA || appsWithDeltaCDA.length === 0)) {
        // At this point we only have apps without a deltaDCA value
        return null;
      }

      const deltaCDA = selectedApplications[0].homologation.deltaCDA;
      const appsWithDifferentDeltaCDA = selectedApplications.filter(app => app.id !== selectedApplications[0].id && app.homologation && app.homologation.deltaCDA !== deltaCDA);
      if (appsWithDifferentDeltaCDA && appsWithDifferentDeltaCDA.length > 0) {
        return null;
      }
      return deltaCDA;
    }

    return null;
  },
  currentHubFitmentFlag: state => {
    const selectedApplications = state.applications.filter(application => application.selected === true);

    if (selectedApplications && selectedApplications.length > 0) {
      const appsWithFlag = selectedApplications.filter(application => application.homologation && (application.homologation.hubFitmentFlag === false || application.homologation.hubFitmentFlag === true));
      const appsWithoutFlag = selectedApplications.filter(application => !application.homologation || (application.homologation.hubFitmentFlag === undefined || application.homologation.hubFitmentFlag === null));

      if (appsWithFlag && appsWithFlag.length > 0 && appsWithoutFlag && appsWithoutFlag.length > 0) {
        // At this point we have applications with and without hub fitment flag values => indeterminate state
        return null;
      }

      if (appsWithoutFlag && appsWithoutFlag.length > 0 && (!appsWithFlag || appsWithFlag.length === 0)) {
        // At this point we only have apps without a hub fitment flag value
        return '';
      }

      const hubFitmentFlag = selectedApplications[0].homologation.hubFitmentFlag;
      const appsWithDifferentFlag = selectedApplications.filter(app => app.id !== selectedApplications[0].id && app.homologation && app.homologation.hubFitmentFlag !== hubFitmentFlag);
      if (appsWithDifferentFlag && appsWithDifferentFlag.length > 0) {
        return null;
      }
      return hubFitmentFlag;
    }
    return null;
  },
  areGenerationCodesEqual: state => state.areGenerationCodesEqual
};

const mutations = {
  setPath: (state, url) => (state.path = url),
  setApplications: (state, applications) => (state.applications = applications),
  setLoading: (state, loading) => (state.loading = loading),
  setWltpFlag: (state, flag) => (state.wltpFlag = flag),
  setIndeterminedWltpState: (state, flag) => (state.indeterminedWltpState = flag),
  setHomologationTypes: (state, homologationTypes) => (state.homologationTypes = homologationTypes),
  UPDATE_HOMOLOGATION: (state, updatedApplications) => {
    updatedApplications.forEach(updatedApp => {
      const app = state.applications.find(app => app.id === updatedApp.id);
      if (app) {
        app.homologation = updatedApp.homologation;
      }
    });
  },
  UPDATE_DESIGN: (state, updatedApplications) => {
    updatedApplications.forEach(updatedApp => {
      const app = state.applications.find(app => app.id === updatedApp.id);
      if (app) {
        app.design = updatedApp.design;
      }
    });
  },
  SET_UPDATING_HOMOLOGATION: (state, updating) => (state.updatingHomologation = updating),
  SET_UPDATING_DESIGN: (state, updating) => (state.updatingDesign = updating),
  setValidationError (state, error) {
    if (error.generationCodes) {
      state.validationError.generationCodes = error.value;
    } else {
      state.validationError.replacementOfEquipments = error.value;
    }
  },
  setGenerationCodesEqual: (state, status) => (state.areGenerationCodesEqual = status)
};

const actions = {
  loadBasePath ({ commit }, url) {
    commit('setPath', url);
  },

  loadById ({ commit, dispatch }, ids) {
    commit('setLoading', true);
    axios
      .get(`${state.path}${ids.join(',')}`)
      .then(res => {
        commit('setLoading', false);

        res.data.forEach(row => {
          row.selected = true;
          row.hasUnsavedChanges = false;
        });

        let filteredData = res.data.filter(entry => entry.status !== 'cancelled');
        commit('setApplications', filteredData);

        dispatch('updateWltpFlagAfterSelectionChange');
      })
      .catch(err => {
        commit('setLoading', false);
        eventbus.$emit('showMessage', {
          title: "Couldn't load applications!",
          err
        });
      });

    axios
      .get('/api/all/homologation-types')
      .then(res => {
        commit('setHomologationTypes', res.data);
      })
      .catch(err => {
        eventbus.$emit('showMessage', {
          title: "Couldn't load homologation types",
          err
        });
      });
  },

  toggleAll (context) {
    let applications = Object.assign([], state.applications);
    if (!applications) return;

    let allSelected = true;

    for (const application of applications) {
      if (!application.selected) {
        allSelected = false;
        break;
      }
    }

    for (const application of applications) {
      application.selected = !allSelected;
    }
    context.commit('setApplications', applications);
  },

  updateWltpFlag (context, newValue) {
    let selection = context.getters.selectedApplications;
    selection.forEach(app => {
      app.homologation.wltp = newValue;
    });
    context.commit('setWltpFlag', newValue);
    context.commit('setIndeterminedWltpState', false);
  },

  updateWltpFlagAfterSelectionChange (context) {
    let selection = context.getters.selectedApplications;
    let currentStatus = null;

    if (selection.length === 1) {
      currentStatus = selection[0].homologation.wltp;
    } else {
      for (let i = 1; i < selection.length; i++) {
        currentStatus = selection[i].homologation.wltp;
        let previousWltp = selection[i - 1].homologation.wltp;

        if (currentStatus !== previousWltp) {
          currentStatus = null;
          break;
        }
      }
    }
    if (currentStatus === undefined || currentStatus === null) {
      context.commit('setIndeterminedWltpState', true);
      context.commit('setWltpFlag', false);
    } else {
      context.commit('setIndeterminedWltpState', false);
      context.commit('setWltpFlag', currentStatus);
    }
  },

  toggleSelectedApplication (context, idx) {
    let applications = Object.assign([], state.applications);
    if (applications[idx]) {
      applications[idx].selected = !applications[idx].selected;
      context.commit('setApplications', applications);
    }
  },

  updateHomologation (context, homologation) {
    /* TODO:
      send backend call to save homologation(s) for selected applications
      save response data of selected applications
    */
    context.commit('SET_UPDATING_HOMOLOGATION', true);
    const request = [];
    context.getters.selectedApplications.forEach(app => {
      const homologationRequest = {};
      homologationRequest.applicationId = app.id;
      homologationRequest.wltpFlag = (homologation.wltpFlag === true || homologation.wltpFlag === false) ? homologation.wltpFlag : app.wltp;
      homologationRequest.deltaCDA = !homologation.deltaCDA ? app.deltaCDA : homologation.deltaCDA;
      homologationRequest.hubFitmentFlag = (homologation.hubFitmentFlag || homologation.hubFitmentFlag === false) ? homologation.hubFitmentFlag : app.hubFitmentFlag;
      homologationRequest.generationCodes = !state.areGenerationCodesEqual ? app.generationCodes : homologation.generationCodes; // TODO: Check if null would be better and in back-end ignore null.
      request.push(homologationRequest);
    });

    axios
      .put('/api/massupdate/homologation', request)
      .then(res => {
        context.commit('UPDATE_HOMOLOGATION', res.data);
        context.commit('SET_UPDATING_HOMOLOGATION', false);
      })
      .catch(err => {
        context.commit('SET_UPDATING_HOMOLOGATION', false);
        eventbus.$emit('showMessage', {
          title: "Couldn't update homologation data!",
          err
        });
      });
  },

  updateDesign (context, design) {
    context.commit('SET_UPDATING_DESIGN', true);
    const request = [];
    context.getters.selectedApplications.forEach(app => {
      const designRequest = {};
      designRequest.applicationId = app.id;
      designRequest.replacementAccessory = (design.replacementAccessory === undefined || design.replacementAccessory === null) ? app.replacementAccessory : design.replacementAccessory;
      designRequest.deltaMass = !design.deltaMass ? app.deltaMass : design.deltaMass;
      designRequest.deltaMassRounded = !design.deltaMassRounded ? app.deltaMassRounded : design.deltaMassRounded;
      designRequest.incalculable = design.incalculable === undefined ? app.incalculable : design.incalculable;
      designRequest.lcoordinates = !design.lCoordinates ? Number(app.lcoOrdinates) : Number(design.lCoordinates);
      designRequest.lcoordinatesRounded = !design.lCoordinatesRounded ? app.lCoordinatesRounded : design.lCoordinatesRounded;
      designRequest.replacementOfEquipments = !design.replacementOfEquipments ? app.replacementOfEquipments : design.replacementOfEquipments; // TODO check if null works better and be ignored in middleware.
      request.push(designRequest);
    });

    console.log(request);

    axios
      .put('/api/massupdate/design', request)
      .then(res => {
        context.commit('UPDATE_DESIGN', res.data);
        context.commit('SET_UPDATING_DESIGN', false);
      })
      .catch(err => {
        context.commit('SET_UPDATING_DESIGN', false);
        eventbus.$emit('showMessage', {
          title: "Couldn't update design data!",
          err
        });
      });
  },

  removeGenerationCode (context, item) {
    let applications = Object.assign([], state.applications);

    for (let application of applications) {
      let idx = application.homologation.generationCodes.findIndex(gc =>
        gc.from === item.from && gc.to === item.to);

      if (idx !== -1) {
        application.homologation.generationCodes.splice(idx, 1);
      }
    }

    context.commit('setApplications', applications);
  },

  updateGenerationCodes (context, localApplications) {
    for (let i in state.applications) {
      let app = state.applications[i];

      app.homologation.generationCodes = localApplications[i].homologation.generationCodes;
    }
  }
};

export default {
  state,
  getters,
  mutations,
  actions
};
