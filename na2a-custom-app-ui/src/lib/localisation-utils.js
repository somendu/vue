export function findMatchingAccessories (vehicles, accessories) {
  const matches = [];

  for (const vehicle of vehicles) {
    const result = findAccessoriesByVehicle(vehicle, accessories);
    if (result && result.length > 0) {
      for (const accessory of result) {
        const foundAccessory = matches.find(a => a === accessory);
        if (!foundAccessory) {
          matches.push(accessory);
        }
      }
    }
  }

  return matches;
};

function findAccessoriesByVehicle (vehicle, accessories) {
  const matches = [];

  for (const accessory of accessories) {
    if (accessory.classifications) {
      for (const classification of accessory.classifications) {
        if (classification.identifier === vehicle.localCodeIdentifier) {
          const index = matches.findIndex(a => a === accessory);
          if (index === -1) {
            matches.push(accessory);
          }
        }
      }
    }
  }
  return matches;
}

export function buildContent (vehicles, accessories, table) {
  const accessoryOverviewData = [];

  for (const vehicle of vehicles) {
    const content = buildContentForVehicle(vehicle, accessories, table);
    if (content) {
      for (const item of content) {
        const foundItem = accessoryOverviewData.find(data => data === item);
        if (!foundItem) {
          accessoryOverviewData.push(item);
        }
      }
    }
  }

  return accessoryOverviewData;
};

function buildContentForVehicle (vehicle, accessories, table) {
  const overviewData = [];

  for (const accessory of accessories) {
    for (const tab of table.tabs) {
      if (hasAccessory(tab, accessory) === true) {
        const row = {
          vehicleName: vehicle.localGrade + ' | ' + vehicle.localCode,
          data: getColumnData(tab.columns, accessory)
        };
        addRow(overviewData, tab.name, row);
      }
    }
  }

  return overviewData;
}

function hasAccessory (tab, accessory) {
  for (const column of tab.columns) {
    if (column.availableItems.find(item => item === accessory.commodity)) {
      return true;
    }
  }
  return false;
};

function getColumnData (columns, accessory) {
  const data = [];
  for (const column of columns) {
    if (column.availableItems.find(item => item === accessory.commodity)) {
      data.push(accessory.commodity);
    } else {
      data.push('');
    }
  }
  return data;
};

function addRow (accessoryOverviewData, tabName, row) {
  const tab = accessoryOverviewData.find(d => d.tabName === tabName);
  if (!tab) {
    accessoryOverviewData.push({
      tabName: tabName,
      rows: [row]
    });
  } else {
    tab.rows.push(row);
  }
};
