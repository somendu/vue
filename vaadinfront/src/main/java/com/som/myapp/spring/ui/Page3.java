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

		Div mainDiv = new Div();
		mainDiv.setId("main-div");

		Div target = new Div();
		target.setId("div-push-me-2");

		// "/images/slider-1.jpg", "alt text"

		Image image = new Image();
		image.setSrc("/images/3.png");

		Image slide2 = new Image("/images/4.png", "slider-2");
		Image slide3 = new Image("/images/5.png", "slider-3");
		Image blog4 = new Image("/images/6.png", "blog-4");
		Image blog5 = new Image("/images/7.png", "blog-5");

		setSize(image);
		setSize(slide2);
		setSize(slide3);
		setSize(blog4);
		setSize(blog5);

		add(target, image);

		image.addClickListener(e -> operate(target, image, slide2));

		slide2.addClickListener(e -> operate(target, slide2, slide3));

		slide3.addClickListener(e -> operate(target, slide3, blog4));

		blog4.addClickListener(e -> operate(target, blog4, blog5));

	}

	private void operate(Div target, Image toRemove, Image toAdd) {

		remove(toRemove);
		add(target, toAdd);

	}

	private void setSize(Image image) {

		image.setWidth("800px");
		image.setHeight("800px");

	}
}
