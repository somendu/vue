package com.som.myapp.spring;

import com.som.myapp.spring.ui.Page1;
import com.som.myapp.spring.ui.Page2;
import com.som.myapp.spring.ui.Page3;
import com.som.myapp.spring.ui.Page4;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.PageConfigurator;

@Route
@PWA(name = "Project Base for Vaadin Flow with Spring", shortName = "Project Base")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@Push

public class MainView extends Div implements RouterLayout, PageConfigurator {

	public MainView() {

		H2 title = new H2("Main View");
		// title.addClassName("main-layout__title");

		RouterLink reviews = new RouterLink(null, Page1.class);
		reviews.add(new Icon(VaadinIcon.LIST), new Text("First"));
		// reviews.addClassName("main-layout__nav-item");

		RouterLink categories = new RouterLink(null, Page2.class);
		categories.add(new Icon(VaadinIcon.ARCHIVES), new Text("Second"));
		// categories.addClassName("main-layout__nav-item");

		RouterLink images = new RouterLink("Hello", Page3.class);
		// images.add(new Icon(VaadinIcon.LIST), new Text("Images"));
		images.setHighlightCondition(HighlightConditions.sameLocation());

		// Div navigation = new Div(reviews);
		Div navigation = new Div(images);
		// navigation.addClassName("main-layout__nav");

//		Div header = new Div(title, images);
		Div header = new Div(title);
		// header.addClassName("main-layout__header");

		Page3 page3 = new Page3();
//		add(header, page3);

		Page4 page4 = new Page4();
		add(page4);

		setSizeFull();
	}

	@Override
	public void configurePage(InitialPageSettings settings) {
		// TODO Auto-generated method stub

	}

}
