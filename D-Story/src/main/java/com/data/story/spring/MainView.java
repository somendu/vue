package com.data.story.spring;

import com.data.story.spring.views.content.MainContent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * The main view is a top-level placeholder for other views.
 */

@Tag("main-view")
@CssImport(value = "./styles/views/main-view.css", include = "lumo-badge")
//@Route
@JsModule("./styles/shared-styles.js")
//@JsModule("./styles/main-view.js")

@PWA(name = "Story", shortName = "Story")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
//@Push
public class MainView extends AppLayout {

	// private final Tabs menu;

	private MainContent mainContent = new MainContent();

	public MainView() {

		// TODO Change the routes accordingly

		setId("main-view");

		Div header = new Div();
		Div imageSDiv = new Div();
		Div imageIDiv = new Div();
		header.getElement().getStyle().set("display", "block");

		// Board board = new Board();

		Image imageS = new Image("/images/susceptible.png", "susceptible");

		Image imageI = new Image("/images/infected.png", "infected");

		Label label = new Label("Label Here");

		Label newLabel = new Label("Label Here");

		setImageSize(imageS);
		imageSDiv.add(imageS);
		setImageSize(imageI);
		imageIDiv.add(imageI);

		imageSDiv.addClassName("divPosition762");

		Page page = UI.getCurrent().getPage();

		mainContent.getElement().addEventListener("scroll", new DomEventListener() {

			@Override
			public void handleEvent(DomEvent event) {

				System.out.println("Scroll Check ");

			}
		});

//		mainContent.getElement().addEventListener("mouseleave", new DomEventListener() {
//
//			@Override
//			public void handleEvent(DomEvent event) {
//
//				System.out.println("Mouse Leave");
//
//			}
//		});

//		imageSDiv.getElement().addEventListener("scroll", new DomEventListener() {
//
//			@Override
//			public void handleEvent(DomEvent event) {
//
//				imageSDiv.removeClassName("divPosition762");
//				imageSDiv.addClassName("divPosition526");
//
//			}
//		});

//		header.addComponentAtIndex(0, imageS);
//
//		header.addComponentAtIndex(1, mainContent);
//
//		header.addComponentAtIndex(2, imageI);

		header.add(mainContent);

//		header.add(mainContent);
		header.setWidthFull();

		setContent(header);

//		headerDiv.set

//		headerDiv.add(header);

		// add(headerDiv);

	}

	/**
	 * Setting Image Size
	 * 
	 * @param image
	 */
	private void setImageSize(Image image) {

		image.setWidth("29px");
		image.setHeight("39px");

	}

//	private final Tabs menu;
//
//	public MainView() {
//		menu = createMenuTabs();
//		addToNavbar(menu);
//	}
//
//	private static Tabs createMenuTabs() {
//		final Tabs tabs = new Tabs();
//		tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
//		tabs.add(getAvailableTabs());
//		return tabs;
//	}
//
//	private static Tab[] getAvailableTabs() {
//		final List<Tab> tabs = new ArrayList<>();
//		tabs.add(createTab("Story", MainContent.class));
//		tabs.add(createTab("MasterDetail", MasterDetailView.class));
//		return tabs.toArray(new Tab[tabs.size()]);
//	}
//
//	private static Tab createTab(String title, Class<? extends Component> viewClass) {
//		return createTab(populateLink(new RouterLink(null, viewClass), title));
//	}
//
//	private static Tab createTab(Component content) {
//		final Tab tab = new Tab();
//		tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
//		tab.add(content);
//		return tab;
//	}
//
//	private static <T extends HasComponents> T populateLink(T a, String title) {
//		a.add(title);
//		return a;
//	}
//
//	@Override
//	protected void afterNavigation() {
//		super.afterNavigation();
//		selectTab();
//	}
//
//	private void selectTab() {
//		String target = RouteConfiguration.forSessionScope().getUrl(getContent().getClass());
//		Optional<Component> tabToSelect = menu.getChildren().filter(tab -> {
//			Component child = tab.getChildren().findFirst().get();
//			return child instanceof RouterLink && ((RouterLink) child).getHref().equals(target);
//		}).findFirst();
//		tabToSelect.ifPresent(tab -> menu.setSelectedTab((Tab) tab));
//	}
}
