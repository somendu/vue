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