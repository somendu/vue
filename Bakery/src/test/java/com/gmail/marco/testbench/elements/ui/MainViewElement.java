package com.gmail.marco.testbench.elements.ui;

import com.gmail.marco.testbench.elements.components.AppNavigationElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("main-view")
public class MainViewElement extends TestBenchElement {

	public AppNavigationElement getMenu() {
		return $(AppNavigationElement.class).first();
	}

}
