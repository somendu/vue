/**
 * 
 */
package com.data.story.spring.views.content;

import com.data.story.spring.MainView;
import com.data.story.spring.views.common.DivComponent;
import com.data.story.spring.views.pages.ImageChange;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;
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

//	private TextComponent firstText = new TextComponent();

	private ImageChange imageChange = new ImageChange();

	private IFrame iframe = new IFrame();

//	private TextComponent secondText = new TextComponent();

//	private TextComponent thirdText = new TextComponent();

//	private DashboardView dashboardView = new DashboardView();

//	private MasterDetailView masterDetailView = new MasterDetailView();

	public MainContent() {

		// TODO Change the routes accordingly

		setId("main-content");

		Board board = new Board();

		// TODO This text to come dynamically
		// firstText.setComponentText("Ontdek hier waarom vaccinaties belangrijk zijn
		// voor de gezondheid van jouw kind.");

		// secondText.setComponentText(
		// "Zoals je hierboven ziet kan de ziekte zich gemakkelijk verspreiden door de
		// groep. Veel kinderen, waaronder jouw ongevaccineerde kind, worden ziek.");

//		thirdText.setComponentText(
//				"Dit komt doordat kinderen die ziek zijn in contact te komen met kinderen die niet gevaccineerd zijn. Als dit gebeurt kunnen ze deze kinderen besmetten en zo verspreidt de ziekte zich door de groep. ");

//		DivComponent imageComponent = new DivComponent("test",
//				new Component[] { firstText, imageChange, secondText, thirdText }, "test");

		iframe.setSrc("https://story-13.appspot.com/");
		iframe.setSizeFull();
		iframe.setHeight("720px");
		iframe.getElement().getStyle().set("border-style", "none");

		DivComponent imageComponent = new DivComponent("test", new Component[] { imageChange }, "test");

//		UI.getCurrent().getPage().executeJS("window.addEventListener('scroll', function(){ console.error('oooo');});");

//		imageComponent.getElement().executeJs("window.addEventListener('scroll', function(){ console.error('oooo');});",
//				"");

		board.add(imageComponent);

		add(board);

		// add(headerDiv);
		imageComponent.getElement().addEventListener("scroll", new DomEventListener() {

			@Override
			public void handleEvent(DomEvent event) {

				System.out.println("Scroll Track");

			}
		});

	}

}
