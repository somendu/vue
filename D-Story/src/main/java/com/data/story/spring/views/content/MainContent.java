/**
 * 
 */
package com.data.story.spring.views.content;

import com.data.story.spring.MainView;
import com.data.story.spring.views.dashboard.DashboardView;
import com.data.story.spring.views.masterdetail.MasterDetailView;
import com.data.story.spring.views.pages.ImageChange;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

/**
 * @author somendu
 *
 */
@Tag("main-content")
//@CssImport(value = "./styles/views/main-view.css", include = "lumo-badge")

@Route(value = "story", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Story")
public class MainContent extends Div {

	private ImageChange imageChange = new ImageChange();

	private DashboardView dashboardView = new DashboardView();

	private MasterDetailView masterDetailView = new MasterDetailView();

	public MainContent() {

		// TODO Change the routes accordingly

		setId("main-content");

		Div headerDiv = new Div();

		HorizontalLayout header = new HorizontalLayout();
		header.getElement().getStyle().set("display", "block");

		// Board board = new Board();

		Label newLabel = new Label("Label Here");

		header.addComponentAtIndex(0, imageChange);
		// header.addComponentAtIndex(1, dashboardView);
		// header.addComponentAtIndex(2, masterDetailView);
		header.setWidthFull();

		add(header);

//		headerDiv.set

//		headerDiv.add(header);

		// add(headerDiv);
//		getElement().addEventListener("mouseleave", new DomEventListener() {
//
//			@Override
//			public void handleEvent(DomEvent event) {
//
//				System.out.println("Mouse Leave in Mani COntent");
//
//			}
//		});

	}

}
