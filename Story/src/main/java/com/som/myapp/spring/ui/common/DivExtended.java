/**
 * 
 */
package com.som.myapp.spring.ui.common;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.DomEventListener;

/**
 * @author somendu
 *
 */
public class DivExtended extends Div {

	public DivExtended() {

		DomEventListener l = e -> System.out.println("invoked!");

		Div div = new Div();
		add(div);
		div.getElement().addEventListener("scroll", l);
		getElement().addEventListener("scroll", l);
		UI.getCurrent().getElement().addEventListener("scroll", l);
	}

}
