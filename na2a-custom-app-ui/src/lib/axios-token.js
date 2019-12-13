import axios from 'axios';
import eventbus from '@/eventbus';

const instance = axios;
let token;
let tokenGetter;

instance.interceptors.request.use(config => {
  // add token, if we have one
  if (token) config.headers['Authorization'] = `Token ${token}`;
  return config;
});

function reloginCallback (e) {
  if (e.data && e.data.message === 'didRelogin') {
    if (tokenGetter) {
      // now that the user has authenticated again, we should be able to get a token
      instance
        .get(`${window.pimConfig.externalUrl}/pim/rest-api-token`, { withCredentials: true, isGetToken: true })
        .then(res => {
          token = res.data;
          // now we can resolve our refreshToken promise
          tokenGetter.resolve();
          tokenGetter = null;
        })
        .catch(err => {
          if (window.console) console.error('could not refresh token (after login)', err);
          tokenGetter.reject(err);
          tokenGetter = null;
        });

      eventbus.$emit('relogin.hide');
    }
  }
}

window.addEventListener('message', reloginCallback, false);

function refreshToken () {
  if (!tokenGetter) {
    tokenGetter = {};
    tokenGetter.promise = new Promise((resolve, reject) => {
      // try to refresh our token
      instance
        .get(`${window.pimConfig.externalUrl}/pim/rest-api-token`, { withCredentials: true, isGetToken: true })
        .then(res => {
          // if this worked out, we're done
          token = res.data;
          tokenGetter = null;
          resolve();
        })
        .catch(err => {
          // if it didn't, it means you're not logged in (anymore)
          if (window.console) console.error('could not refresh token', err);

          // save these for our reloving callback
          tokenGetter.resolve = resolve;
          tokenGetter.reject = reject;

          // present relogin
          eventbus.$emit('relogin.show');
        });
    });
  }

  return tokenGetter.promise;
}

instance.interceptors.response.use(response => {
  return response;
}, function (error) {
  // ignore our own calls
  if (!error.config.isGetToken && !error.config.isRetry) {
    // needs token?
    if (error.response && error.response.status === 401) {
      // wait for a new one
      return refreshToken()
        .then(() => {
          // now retry
          return instance({ ...error.config, isRetry: true });
        });
    }
  }

  // Do something with response error
  return Promise.reject(error);
});
