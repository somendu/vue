/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.som.myapp.spring.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.som.myapp.spring.MainView;
import com.som.myapp.spring.MessageBean;
import com.vaadin.componentfactory.Popup;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * Displays the list of available categories, with a search filter as well as
 * buttons to add a new category or edit existing ones.
 *
 * Implemented using a simple template.
 */

@Route(value = "Page3", layout = MainView.class)
@PageTitle("Page 3")
@Tag("page3-list")

public class Page3 extends HorizontalLayout {

	public Page3(@Autowired MessageBean bean) {

		// Button button = new Button("Click me", e ->
		// Notification.show(bean.getMessage()));
		// add(button);

		Div target = new Div();
		// target.setText("I have popup, click me(I dont change popup content)");
		target.setId("div-push-me-2");

		Image image = new Image("/images/blog-5.jpg", "alt text");
		image.setHeight("100px");
		image.setWidth("100px");

		add(image);

		Image imagePop = new Image("/images/blog-5.jpg", "alt text");
		imagePop.setId("image-popup");

		Popup popup = new Popup();

		add(target, popup);
		popup.setFor(target.getId().orElse(null));
		popup.add(imagePop);

		image.addClickListener(e -> popup.show());

	}
}
