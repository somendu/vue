function summarizeOptions (section, target) {
  let categories = {};

  for (let entry of section) {
    if (!categories[entry.category]) categories[entry.category] = [];
    categories[entry.category].push(entry.value);
  }

  for (let category of Object.keys(categories)) {
    target.push({
      type: category,
      values: categories[category]
    });
  }
}

function summarizeArray (values, label, target) {
  if (Array.isArray(values) && values.length) {
    target.push({
      type: label,
      values: values.map(value => value.value)
    });
  }
}

function summarizeArrayWithKey (values, label, target) {
  if (Array.isArray(values) && values.length) {
    target.push({
      type: label,
      values: values.map(value => value.value + ' [' + value.key + ']')
    });
  }
}

export function summarize (entry, joinValues) {
  const sections = [];

  if (entry.model.all) {
    sections.push({
      type: 'Model',
      values: [ 'All models' ]
    });
  } else {
    if (entry.model.brand) sections.push({ type: 'Brand', values: [ entry.model.brand ] });
    if (entry.model.model) sections.push({ type: 'Model', values: [ entry.model.model.value ] });
    if (entry.model.project) sections.push({ type: 'MMC/FMC', values: [ entry.model.project.value ] });

    summarizeOptions(entry.vehicle, sections);
    summarizeOptions(entry.equipment, sections);
  }

  summarizeArray(entry.exteriorColours, 'Exterior Colours', sections);
  summarizeArray(entry.interiorColours, 'Interior Colours', sections);
  summarizeArray(entry.trimColours, 'Trim Colours', sections);
  summarizeArrayWithKey(entry.countries, 'Country', sections);

  if (joinValues !== false) {
    for (let section of sections) {
      section.value = section.values.join(', ');
    }
  }

  return sections;
};

function optionsEquals (o1, o2) {
  const keys1 = {};
  const keys2 = {};

  for (let o of o1) {
    keys1[o.key] = true;
  }

  for (let o of o2) {
    if (!keys1[o.key]) return false;
    keys2[o.key] = true;
  }

  for (let o of o1) {
    if (!keys2[o.key]) return false;
  }

  return true;
}

export function findApplicationsForSameSpec (idx, application, existingApplications) {
  return existingApplications.filter(entry => {
    const currentBrand = application.model.brand;
    const currentModel = application.model.model ? application.model.model.key : undefined;
    const currentProject = application.model.project ? application.model.project.key : undefined;

    const existingBrand = entry.model.brand;
    const existingModel = entry.model.model ? entry.model.model.key : undefined;
    const existingProject = entry.model.project ? entry.model.project.key : undefined;

    return entry.cancelled === false && entry.idx !== idx && existingBrand === currentBrand && existingModel === currentModel && existingProject === currentProject;
  });
}

export function hasAnotherEmptyApplication (filteredEntries) {
  for (const existingApplication of filteredEntries) {
    if (!hasSelection(existingApplication.equipment) && !hasSelection(existingApplication.vehicle) &&
        !hasSelection(existingApplication.exteriorColours) && !hasSelection(existingApplication.interiorColours) &&
        !hasSelection(hasSelection.trimColours) && !hasSelection(existingApplication.countries)) {
      return true;
    }
  }
  return false;
}

function hasSelection (options) {
  return options && Array.isArray(options) && options.length > 0;
}

export function variantEquals (e1, e2) {
  if (e1 == null) return e2 == null;
  if (e2 == null) return false;

  // cast both to boolean and do a not-equals
  if (!!e1.cancelled === !e2.cancelled) return false;

  // does model differ?
  if (!!e1.model.all === !e2.model.all) return false;
  if (e1.model.brand !== e2.model.brand) return false;
  if ((e1.model.model && e1.model.model.key) !== (e2.model.model && e2.model.model.key)) return false;
  if ((e1.model.project && e1.model.project.key) !== (e2.model.project && e2.model.project.key)) return false;

  if (!optionsEquals(e1.vehicle, e2.vehicle)) return false;
  if (!optionsEquals(e1.equipment, e2.equipment)) return false;
  if (!optionsEquals(e1.exteriorColours, e2.exteriorColours)) return false;
  if (!optionsEquals(e1.interiorColours, e2.interiorColours)) return false;
  if (!optionsEquals(e1.trimColours, e2.trimColours)) return false;

  return true;
};

export function partTypeName (value) {
  if (value === '1') return 'Spare';
  if (value === '12') return 'Main Part';
  if (value === '13') return 'Fitting Part';
  if (value === '14') return 'Consumable';
  return 'Unknown';
}

export function filterBrand (value) {
  const params = value.split('|');

  if (params.length === 3) {
    return params[1];
  }

  return '';
}
