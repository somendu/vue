<template>
    <div>
        <b-tabs id="accessory-overview-tabs">
            <b-tab v-for="(tab, idx) of accessoryTable.tabs" :key="idx" :title="tab.name + ' (' + tabCount(idx) + ')'">
                <accessory-overview-tab
                    :name=tab.name
                    :columns=tab.columns
                    :rows="tabData[idx].rows"
                >
                </accessory-overview-tab>
            </b-tab>
        </b-tabs>
    </div>
</template>

<script>
import AccessoryOverviewTab from '@/components/Localisation/AccessoryOverviewTab';
export default {
  components: {
    AccessoryOverviewTab
  },
  props: {
    accessoryTable: {
      type: Object,
      required: true
    },
    data: {
      type: Array,
      required: false
    }
  },

  data () {
    return {
      count: 0,
      tabData: [{
        rows: []
      }]
    };
  },

  created () {
    this.loadTabData();
  },

  watch: {
    data () {
      this.loadTabData();
    }
  },

  methods: {
    loadTabData () {
      this.tabData = [];
      for (const tableTab of this.accessoryTable.tabs) {
        this.tabData.push(Object.assign({}, { tabName: tableTab.name, rows: [] }));
      }

      for (const tab of this.data) {
        const t = this.tabData.find(t => t.tabName === tab.tabName);
        if (t) {
          for (const row of tab.rows) {
            t.rows.push(row);
          }
        }
      }
    },

    tabCount (idx) {
      if (this.tabData && this.tabData.length >= idx) {
        let count = 0;
        for (const row of this.tabData[idx].rows) {
          if (row.data && row.data.length > 0) {
            for (const value of row.data) {
              if (value !== '') {
                count++;
              }
            }
          }
        }
        return count;
      } else {
        return 0;
      }
    }
  }
};
</script>
<style scoped>

</style>
