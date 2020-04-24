import "@vaadin/vaadin-charts/theme/vaadin-chart-default-theme";

const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `
<custom-style>
  <style>
html {
    }
  </style>
</custom-style>


<custom-style>
  <style>
    html {
      overflow:auto;
    }
    vaadin-grid vaadin-tab a:hover {
      text-decoration: none;
    }
  </style>
</custom-style>

<custom-style>
  <style>
    html {
      overflow:auto;
    }
    vaadin-app-layout vaadin-tab a:hover {
      text-decoration: none;
    }
  </style>
</custom-style>

<dom-module id="app-layout-theme" theme-for="vaadin-app-layout">
  <template>
    <style>
      [part="navbar"] {
        align-items: center;
      }
    </style>
  </template>
</dom-module>

<dom-module id="chart" theme-for="vaadin-chart">
  <template>
    <style include="vaadin-chart-default-theme">
      :host {
        --vaadin-charts-color-0: var(--lumo-primary-color);
        --vaadin-charts-color-1: var(--lumo-error-color);
        --vaadin-charts-color-2: var(--lumo-success-color);
        --vaadin-charts-color-3: var(--lumo-contrast);
      }
      .highcharts-container {
        font-family: var(--lumo-font-family);
      }
      .highcharts-background {
        fill: var(--lumo-base-color);
      }
      .highcharts-title {
        fill: var(--lumo-header-text-color);
        font-size: var(--lumo-font-size-xl);
      }
      .highcharts-legend-item text {
        fill: var(--lumo-body-text-color);
      }
      .highcharts-axis-title,
      .highcharts-axis-labels {
        fill: var(--lumo-secondary-text-color);
      }
      .highcharts-axis-line,
      .highcharts-grid-line,
      .highcharts-tick {
        stroke: var(--lumo-contrast-10pct);
      }
      .highcharts-column-series rect.highcharts-point {
        stroke: var(--lumo-base-color);
      }
    </style>

    <image-change id="imageChange"></image-change>

    
  </template>
</dom-module>

<dom-module id="vaadin-grid-theme" theme-for="vaadin-grid">
  <template>
    <style>
      :host {
        width: 100%;
        margin: auto;
      }

      [part~="row"]:last-child [part~="header-cell"],
      [part~="header-cell"]:not(:empty):not([details-cell]) {
        padding-top: var(--lumo-space-l);
        padding-bottom: var(--lumo-space-m);
      }

      :host(:not([theme~="no-row-borders"])) [part~="cell"]:not([part~="details-cell"]) {
        border-top: 1px solid var(--lumo-shade-5pct);
      }

      /* a special grid theme for the bakery storefront view */
      :host([theme~="orders"]) {
        background: transparent;
      }

      :host([theme~="orders"]) [part~="cell"]:not(:empty):not([details-cell]) {
        padding: 0;
      }

      :host([theme~="orders"]) [part~="row"][selected] [part~="cell"] {
        background: transparent !important;
      }

      :host([theme~="orders"]) [part~="body-cell"] {
        background: transparent;
      }

      @media (max-width: 600px) {
        :host([theme~="orders"]) [part~="cell"] ::slotted(vaadin-grid-cell-content) {
          padding: 0 !important;
        }
      }

      :host([theme~="dashboard"]) [part~="cell"] ::slotted(vaadin-grid-cell-content) {
        padding: 0;
      }

      :host([theme~="image-compact"]) [part~="cell"] ::slotted(vaadin-grid-cell-content) {
        padding: var(--lumo-space-xs) !important;
        -webkit-padding: var(--lumo-space-xs);
      }

      :host([theme~="crud"]) {
        max-width: calc(964px + var(--lumo-space-m));
        background-color: var(--lumo-base-color);
      }
    </style>
  </template>
</dom-module>

<!-- shared styles for all views -->
<dom-module id="shared-styles">
  <template>
    <style>
      *,
      *::before,
      *::after,
      ::slotted(*) {
        box-sizing: border-box;
      }

      :host([hidden]),
      [hidden] {
        display: none !important;
      }

      h2,
      h3 {
        margin-top: var(--lumo-space-m);
        margin-bottom: var(--lumo-space-s);
      }

      h2 {
        font-size: var(--lumo-font-size-xxl);
      }

      h3 {
        font-size: var(--lumo-font-size-xl);
      }

      .scrollable {
        padding: var(--lumo-space-m);
        overflow: auto;
        -webkit-overflow-scrolling: touch;
      }

      .count {
        display: inline-block;
        background: var(--lumo-shade-10pct);
        border-radius: var(--lumo-border-radius);
        font-size: var(--lumo-font-size-s);
        padding: 0 var(--lumo-space-s);
        text-align: center;
      }

      .total {
        padding: 0 var(--lumo-space-s);
        font-size: var(--lumo-font-size-l);
        font-weight: bold;
        white-space: nowrap;
      }

      @media (min-width: 600px) {
        .total {
          min-width: 0;
          order: 0;
          padding: 0 var(--lumo-space-l);
        }
      }

      @media (max-width: 600px) {
        search-bar {
          order: 1;
        }
      }

      .flex {
        display: flex;
      }

      .flex1 {
        flex: 1 1 auto;
      }

      .bold {
        font-weight: 600;
      }

      flow-component-renderer[theme="dialog"],
      flow-component-renderer[theme="dialog"] > div {
        display: flex;
        flex-direction: column;
        flex: auto;
      }

      .center-align {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 4em;
        height: 4em;
        background-color: rgba(0, 0, 0, 0.1);
        border: 1px solid rgba(0, 0, 0, 0.1);
      }
    </style>
  </template>
</dom-module>

`;

document.head.appendChild($_documentContainer.content);
