import {PolymerElement,html} from '@polymer/polymer/polymer-element.js';

class MainView extends PolymerElement {

    static get template() {
        return html`
            <div>
                <p> Page Here </p>
            </div>`;
    }

    static get is() {
          return 'main-view';
    }
}

customElements.define(MainView.is, MainView);

{/* <dom-module id="main-view">
    <template>
        <div>
            <paper-input id="inputId" value="{{userInput}}"></paper-input>
            <button id="helloButton" on-click="sayHello">Say hello</button>
            <div id="greeting">[[greeting]]</div>
        </div>
    </template>
    <script>
        class MainView extends Polymer.Element {
            static get is() {
                return 'main-view';
            }
        }
        customElements.define(MainView.is, MainView);
    </script>
</dom-module> */}