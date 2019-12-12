const promises = {};

function get (name, isHistory) {
  const cacheKey = `${name}#${isHistory}`;
  if (!promises[cacheKey]) {
    promises[cacheKey] = fetch('/api/reference-tables/' + encodeURIComponent(name) + '?history=' + isHistory)
      .then(res => res.json());
  }
  return promises[cacheKey];
}

export {
  get
};
