import {PolymerElement,html} from '@polymer/polymer/polymer-element.js';

class MainView extends PolymerElement {

    static get template() {
        return html`
            <image id="imageS" src="/images/susceptible.png" alt="susceptible"> </image>`;
    }

    static get is() {
          return 'main-view';
    }

    handleScroll() {
      console.log('Button was clicked.');
      window.alert('Hello');
    }

    handleClick() {
      console.log('Button was clicked.');
      window.alert('Hello');
    }
}

customElements.define(MainView.is, MainView);