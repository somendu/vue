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

import java.util.ArrayList;
import java.util.List;

import com.som.myapp.spring.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.grid.Grid;
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

@Route(value = "Page4", layout = MainView.class)
@PageTitle("Page 4")
@Tag("page4-list")

public class Page4 extends HorizontalLayout {

	public Page4() {

		Div target = new Div();
		target.setId("div-push-me-2");

		List<Image> imageList = new ArrayList<Image>();

		Grid<Image> grid = new Grid<Image>(10);
		grid.setId("grid");

		Image image = new Image();
		Image image1 = new Image();
		Image image2 = new Image();
		Image image3 = new Image();
		Image image4 = new Image();
		Image image5 = new Image();

		Image image6 = new Image();
		Image image7 = new Image();
		Image image8 = new Image();

		Image image9 = new Image();
		Image image10 = new Image();

		image.setSrc("/images/susceptible.png");

		image1.setSrc("/images/vaccinated.png");
		image2.setSrc("/images/vaccinated.png");
		image3.setSrc("/images/vaccinated.png");
		image4.setSrc("/images/vaccinated.png");
		image5.setSrc("/images/vaccinated.png");

		image6.setSrc("/images/vaccinated.png");
		image7.setSrc("/images/vaccinated.png");
		image8.setSrc("/images/vaccinated.png");

		image9.setSrc("/images/vaccinated.png");
		image10.setSrc("/images/vaccinated.png");

		imageList.add(image);
//		imageList.add(image1);
//		imageList.add(image2);
//		imageList.add(image3);
//		imageList.add(image4);
//		imageList.add(image5);
//		imageList.add(image6);
//		imageList.add(image7);

		grid.addComponentColumn(i -> image).setAutoWidth(false);
		grid.addComponentColumn(i -> image1).setAutoWidth(false);
		grid.addComponentColumn(i -> image2).setAutoWidth(false);
//		grid.addComponentColumn(i -> image3).setAutoWidth(false);
//		grid.addComponentColumn(i -> image4).setAutoWidth(false);
//		grid.addComponentColumn(i -> image5).setAutoWidth(false);
//		grid.addComponentColumn(i -> image6).setAutoWidth(false);
//		grid.addComponentColumn(i -> image7).setAutoWidth(false);
//		grid.addComponentColumn(i -> image8).setAutoWidth(false);
//		grid.addComponentColumn(i -> image9).setAutoWidth(false);
//		grid.addComponentColumn(i -> image10).setAutoWidth(false);

		grid.setItems(image, image1, image2);

		add(target, grid);

	}
}
