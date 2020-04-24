package com.data.story.spring.views.common;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;

public class DivComponent extends Div {

	public DivComponent(String className, Component[] components,
			String... classes) {
		addClassName(className);

		Div card = new Div();
		card.addClassNames(classes);
		card.add(components);

		add(card);
	}

}
