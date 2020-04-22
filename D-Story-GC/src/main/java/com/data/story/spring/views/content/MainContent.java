/**
 * 
 */
package com.data.story.spring.views.content;

import com.data.story.spring.MainView;
import com.data.story.spring.views.common.DivComponent;
import com.data.story.spring.views.dashboard.DashboardView;
import com.data.story.spring.views.masterdetail.MasterDetailView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.html.Div;
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
@PageTitle("Main Page")
public class MainContent extends Div {

	// private ImageChange imageChange = new ImageChange();

	private DashboardView dashboardView = new DashboardView();

	private MasterDetailView masterDetailView = new MasterDetailView();

	public MainContent() {

		setId("main-content");

		Board board = new Board();

//		DivComponent imageComponent = new DivComponent("outerDiv", new Component[] { imageChange }, "innerDiv");
		DivComponent imageComponent = new DivComponent("outerDiv", new Component[] { masterDetailView }, "innerDiv");

		board.add(imageComponent);

		add(board);

	}

}
