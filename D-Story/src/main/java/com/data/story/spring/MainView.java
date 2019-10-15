package com.data.story.spring;

import com.data.story.spring.views.pages.ImageChange;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * The main view is a top-level placeholder for other views.
 */

@Tag("main-view")
@CssImport(value = "./styles/views/main-view.css", include = "lumo-badge")
@JsModule("./styles/shared-styles.js")
@PWA(name = "Story", shortName = "Story")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
public class MainView extends AppLayout {

//	private final Tabs menu;
//
//	private TextComponent titleText = new TextComponent();

	private ImageChange imageChange = new ImageChange();

	public MainView() {

//		UI.getCurrent().getPage().setTitle("Title Here");
//		Image img = new Image("https://i.imgur.com/GPpnszs.png", "Vaadin Logo");
//		img.setHeight("44px");
//		titleText.setComponentText("Title Text");
//		titleText.getElement().getStyle().set("padding-left", "1.5%");
//		addToNavbar(titleText);

//		menu = createMenuTabs();
//		addToNavbar(menu);

		// UI.getCurrent().getPage().executeJs("window.addEventListener('scroll',
		// function(){ console.error('oooo');});", "$0");

		setContent(imageChange);
	}

//	private static Tabs createMenuTabs() {
//		final Tabs tabs = new Tabs();
//
//		tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
//		tabs.add(getAvailableTabs());
//		return tabs;
//	}
//
//	private static Tab[] getAvailableTabs() {
//		final List<Tab> tabs = new ArrayList<>();
//
//		// tabs.add(createBlankTab());
//
////		tabs.add(createTab("Title Text", TextComponent.class));
//
//		tabs.add(createTab("Story", MainContent.class));
//		tabs.add(createTab("MasterDetail", MasterDetailView.class));
//
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
//		// tab.addClassName("contents-display");
//
//		tab.getElement().addEventListener("scroll", new DomEventListener() {
//
//			@Override
//			public void handleEvent(DomEvent event) {
//				System.out.println("Scrolled Tab");
//
//			}
//		});
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
