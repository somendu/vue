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
import java.util.concurrent.atomic.AtomicReference;

import com.som.myapp.spring.MainView;
import com.som.myapp.spring.ui.common.CustomImage;
import com.som.myapp.spring.ui.common.PaperSlider;
import com.som.myapp.spring.ui.common.PaperSliderChangEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

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

	CustomImage customImageTransfer = new CustomImage();

	Div gridDiv = new Div();
	Div sliderDiv = new Div();
	Div labelDiv = new Div();

	Div resetButtonDiv = new Div();

	List<CustomImage> imageList = new ArrayList<CustomImage>();
	Grid<CustomImage> grid = new Grid<CustomImage>();

	CustomImage customImage = new CustomImage();
	CustomImage customImageTwo = new CustomImage();
	CustomImage customImageThree = new CustomImage();
	CustomImage customImageFour = new CustomImage();
	CustomImage customImageFive = new CustomImage();
	CustomImage customImageSix = new CustomImage();
	CustomImage customImageSeven = new CustomImage();
	CustomImage customImageEight = new CustomImage();
	CustomImage customImageNine = new CustomImage();
	CustomImage customImageTen = new CustomImage();
	CustomImage customImageEleven = new CustomImage();
	CustomImage customImageTwelve = new CustomImage();
	CustomImage customImageThirteen = new CustomImage();
	CustomImage customImageFourteen = new CustomImage();
	CustomImage customImageFifteen = new CustomImage();
	CustomImage customImageSixteen = new CustomImage();
	CustomImage customImageSeventeen = new CustomImage();
	CustomImage customImageEighteen = new CustomImage();

	PaperSlider paperSlider = new PaperSlider("0", "100", "0");

	Label valueLabel = new Label("");

	Button startButton = new Button("Start");

	Button resetButton = new Button("Start Over");

	Registration gridFirstCLick;
	Registration gridSecondCLick;

	public Page4() {

		initWidget();

	}

	private void initWidget() {

		gridDiv.setId("gridDiv-push-me-2");

		sliderDiv.setId("sliderDiv-push-me-2");

		labelDiv.setId("labelDiv-push-me-2");

		grid.setId("grid");

//		CustomImage customImage = new CustomImage();

		//

		imageList.add(customImage);

		grid.setItems(imageList);

		grid.addComponentColumn(i -> customImageTwo.getVerticalInitialLayout());
		grid.addComponentColumn(i -> customImageThree.getVerticalInitialLayout());
		grid.addComponentColumn(i -> customImageFour.getHorizontalLayout());
		grid.addComponentColumn(i -> customImageFive.getVerticalInitialLayout());
		grid.addComponentColumn(i -> customImageSix.getVerticalInitialLayout());
		grid.addComponentColumn(i -> customImageSeven.getHorizontalLayout());
		grid.addComponentColumn(i -> customImageEight.getVerticalInitialLayout());
		grid.addComponentColumn(i -> customImageNine.getHorizontalLayout());
		grid.addComponentColumn(i -> customImageTen.getVerticalInitialLayout());
		grid.addComponentColumn(i -> customImageEleven.getVerticalInitialLayout());
		grid.addComponentColumn(i -> customImageTwelve.getVerticalInitialLayout());
		grid.addComponentColumn(i -> customImageThirteen.getVerticalInitialLayout());
		grid.addComponentColumn(i -> customImageFourteen.getVerticalTwoImageLayout());

		gridDiv.add(grid);
		grid.setSelectionMode(Grid.SelectionMode.NONE);

		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.MATERIAL_COLUMN_DIVIDERS,
				GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);

//		grid.getElement().getStyle().set("position", "static");

		grid.setHeightByRows(true);

//		grid.setWidth("50%");

		List<Column<CustomImage>> columnsList = grid.getColumns();

		for (Column<CustomImage> columnImage : columnsList) {
			columnImage.setFlexGrow(0);

			// TODO This is to get the key name
			// columnImage.setKey("Key Setting");

		}

		sliderDiv.add(paperSlider);

		valueLabel.setText(paperSlider.getInitValue());

		labelDiv.add(valueLabel);

		startButton.setEnabled(false);

		paperSlider.addChangeListener(slideEvent -> changeSusceptibleImage(slideEvent, customImageTwo, customImageThree,
				customImageFour, customImageFive, customImageSix, customImageSeven, customImageEight, customImageNine,
				customImageTen, customImageEleven, customImageTwelve, customImageThirteen, customImageFourteen));

		addComponentAtIndex(0, sliderDiv);
		addComponentAtIndex(1, labelDiv);
		addComponentAtIndex(2, gridDiv);
		// addComponentAtIndex(3, startButton);

	}

	/**
	 * Slide change event
	 * 
	 * @param div
	 * @param label
	 * @param button
	 * @param sliderEvent
	 * @param customImage
	 */
	private void changeSusceptibleImage(PaperSliderChangEvent sliderEvent, CustomImage... customImage) {

		// TODO THis will be used once timer thing in place
		sliderEvent.getSource().getElement().setAttribute("disabled", "true");

//		startButton.setEnabled(true);

		// if (startButton.isEnabled()) {
		// startButton.addClickListener(buttonEvent -> imageChanger(sliderEvent,
		// customImage));

		AtomicReference<Registration> registrationHolder = new AtomicReference<>();
		registrationHolder.set(gridDiv.addClickListener((ComponentEventListener<ClickEvent<Div>>) event -> {
			registrationHolder.get().remove();
			registrationHolder.set(null);
			imageChanger(sliderEvent, customImage);
		}));
		// }

		valueLabel.setText(sliderEvent.getValue());

		// TODO Check count of the images and change accordingly

		for (CustomImage customImageSingle : customImage) {
			customImageSingle.setVaccinatedImage();
		}

		if (Integer.parseInt(sliderEvent.getValue()) <= 10) {

			customImage[0].setSusceptibleImage();
		} else if (Integer.parseInt(sliderEvent.getValue()) > 10 && Integer.parseInt(sliderEvent.getValue()) <= 20) {

			customImage[0].setSusceptibleImage();
			customImage[1].setSusceptibleImage();
		} else if (Integer.parseInt(sliderEvent.getValue()) > 20 && Integer.parseInt(sliderEvent.getValue()) <= 30) {

			customImage[0].setSusceptibleImage();
			customImage[1].setSusceptibleImage();
			customImage[2].setSusceptibleImage();
		} else if (Integer.parseInt(sliderEvent.getValue()) > 30 && Integer.parseInt(sliderEvent.getValue()) <= 40) {

			customImage[0].setSusceptibleImage();
			customImage[1].setSusceptibleImage();
			customImage[2].setSusceptibleImage();
			customImage[3].setSusceptibleImage();
		} else if (Integer.parseInt(sliderEvent.getValue()) > 40 && Integer.parseInt(sliderEvent.getValue()) <= 50) {

			customImage[0].setSusceptibleImage();
			customImage[1].setSusceptibleImage();
			customImage[2].setSusceptibleImage();
			customImage[3].setSusceptibleImage();
			customImage[4].setSusceptibleImage();

		} else if (Integer.parseInt(sliderEvent.getValue()) > 50 && Integer.parseInt(sliderEvent.getValue()) <= 60) {

			customImage[0].setSusceptibleImage();
			customImage[1].setSusceptibleImage();
			customImage[2].setSusceptibleImage();
			customImage[3].setSusceptibleImage();
			customImage[4].setSusceptibleImage();
			customImage[5].setSusceptibleImage();
		} else if (Integer.parseInt(sliderEvent.getValue()) > 60 && Integer.parseInt(sliderEvent.getValue()) <= 70) {

			customImage[0].setSusceptibleImage();
			customImage[1].setSusceptibleImage();
			customImage[2].setSusceptibleImage();
			customImage[3].setSusceptibleImage();
			customImage[4].setSusceptibleImage();
			customImage[5].setSusceptibleImage();
			customImage[6].setSusceptibleImage();
		} else if (Integer.parseInt(sliderEvent.getValue()) > 70 && Integer.parseInt(sliderEvent.getValue()) <= 80) {

			customImage[0].setSusceptibleImage();
			customImage[1].setSusceptibleImage();
			customImage[2].setSusceptibleImage();
			customImage[3].setSusceptibleImage();
			customImage[4].setSusceptibleImage();
			customImage[5].setSusceptibleImage();
			customImage[6].setSusceptibleImage();
			customImage[7].setSusceptibleImage();
		} else if (Integer.parseInt(sliderEvent.getValue()) > 80 && Integer.parseInt(sliderEvent.getValue()) <= 90) {

			customImage[0].setSusceptibleImage();
			customImage[1].setSusceptibleImage();
			customImage[2].setSusceptibleImage();
			customImage[3].setSusceptibleImage();
			customImage[4].setSusceptibleImage();
			customImage[5].setSusceptibleImage();
			customImage[6].setSusceptibleImage();
			customImage[7].setSusceptibleImage();
			customImage[8].setSusceptibleImage();
		} else if (Integer.parseInt(sliderEvent.getValue()) > 90 && Integer.parseInt(sliderEvent.getValue()) <= 100) {
			customImage[0].setSusceptibleImage();
			customImage[1].setSusceptibleImage();
			customImage[2].setSusceptibleImage();
			customImage[3].setSusceptibleImage();
			customImage[4].setSusceptibleImage();
			customImage[5].setSusceptibleImage();
			customImage[6].setSusceptibleImage();
			customImage[7].setSusceptibleImage();
			customImage[8].setSusceptibleImage();
			customImage[9].setSusceptibleImage();

		}

	}

	@SuppressWarnings("unchecked")
	private void imageChanger(PaperSliderChangEvent sliderEvent, CustomImage... customImage) {

		// TODO Based on slider event count value - need to change the image

		// TODO Set the susceptible/infected your child image as well
		customImage[2].getImageOne().setSrc("/images/susceptible-your-child.png");

		AtomicReference<Registration> registrationHolder = new AtomicReference<>();
		registrationHolder.set(gridDiv.addClickListener((ComponentEventListener<ClickEvent<Div>>) event -> {
			registrationHolder.get().remove();
			registrationHolder.set(null);
			setSickImages(sliderEvent, customImage);
		}));

		// gridFirstCLick = gridDiv.addClickListener(gridEvent ->
		// setSickImages(sliderEvent, customImage));

		startButton.setEnabled(false);

	}

	private void setSickImages(PaperSliderChangEvent sliderEvent, CustomImage... customImage) {

		customImage[7].setInfectedImage();
		customImage[8].setInfectedImage();
		customImage[9].setInfectedImage();

		AtomicReference<Registration> registrationHolder = new AtomicReference<>();
		registrationHolder.set(gridDiv.addClickListener((ComponentEventListener<ClickEvent<Div>>) event -> {
			registrationHolder.get().remove();
			registrationHolder.set(null);
			setInfectedChild(sliderEvent, customImage);
		}));

	}

	private void setInfectedChild(PaperSliderChangEvent sliderEvent, CustomImage... customImage) {

		customImage[2].getImageOne().setSrc("/images/infected-your-child.png");

		// resetButtonDiv.add(resetButton);
		// addComponentAtIndex(3, resetButtonDiv);
		sliderEvent.getSource().getElement().removeAttribute("disabled");

		resetButton.addClickListener(resetClick -> reset(sliderEvent, customImage));

	}

	private void reset(PaperSliderChangEvent sliderEvent, CustomImage... customImage) {

		// TODO slider need to call once the change animation completed
		sliderEvent.getSource().getElement().removeAttribute("disabled");
		sliderDiv.remove(paperSlider);

		// TODO
		// Check to reinitiate the Slider
		paperSlider = new PaperSlider("0", "100", "0");
		sliderDiv.add(paperSlider);

		valueLabel.setText(paperSlider.getInitValue());

		paperSlider.addChangeListener(slideEvent -> changeSusceptibleImage(slideEvent, customImageTwo, customImageThree,
				customImageFour, customImageFive, customImageSix, customImageSeven, customImageEight, customImageNine,
				customImageTen, customImageEleven, customImageTwelve, customImageThirteen, customImageFourteen));

		customImage[0].setImage();
		customImage[1].setImage();
		customImage[2].setImage();
		customImage[3].setImage();
		customImage[4].setImage();
		customImage[5].setImage();
		customImage[6].setImage();
		customImage[7].setImage();
		customImage[8].setImage();
		customImage[9].setImage();

		startButton.setEnabled(false);

		resetButtonDiv.remove(resetButton);

	}
}
