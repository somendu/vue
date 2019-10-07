package com.data.story.spring;

import com.data.story.spring.views.pages.ImageChange;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * The main view is a top-level placeholder for other views.
 */

@JsModule("./styles/shared-styles.js")
@PWA(name = "Story", shortName = "Story")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
public class MainView extends AppLayout {

	// private final Tabs menu;

	public MainView() {
		ImageChange page4 = new ImageChange();
		setContent(page4);
	}

	@Override
	protected void afterNavigation() {
		super.afterNavigation();

	}

}
