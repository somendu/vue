package com.data.story.spring;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * The main view is a top-level placeholder for other views.
 */

//@Route
@JsModule("./styles/shared-styles.js")
@PWA(name = "Story", shortName = "Story")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@Push
public class MainView extends AppLayout {

//	private final Tabs menu;

	public MainView() {
//		menu = createMenuTabs();
//		addToNavbar(menu);

		Div div = new Div();

		Label label = new Label("Here it is");

		div.add(label);

	}

//	private static Tabs createMenuTabs() {
//		final Tabs tabs = new Tabs();
//		tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
//		tabs.add(getAvailableTabs());
//		return tabs;
//	}
//
//	private static Tab[] getAvailableTabs() {
//		final List<Tab> tabs = new ArrayList<>();
////        tabs.add(createTab("Dashboard", DashboardView.class));
////        tabs.add(createTab("MasterDetail", MasterDetailView.class));
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
	@Override
	protected void afterNavigation() {
		super.afterNavigation();

	}
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
