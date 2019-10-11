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
package com.data.story.spring.views.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.data.story.spring.views.common.CustomGrid;
import com.data.story.spring.views.common.CustomImage;
import com.data.story.spring.views.common.DivExtended;
import com.data.story.spring.views.common.PaperSlider;
import com.data.story.spring.views.common.PaperSliderChangEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.shared.Registration;

/**
 * Displays the list of available categories, with a search filter as well as
 * buttons to add a new category or edit existing ones.
 *
 * Implemented using a simple template.
 */

//@Route(value = "ImageChange", layout = MainView.class)
//@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Image Change")
@Tag("image-change")
@CssImport(value = "./styles/views/imagechange/image-change.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class ImageChange extends Div {

	CustomImage customImageTransfer = new CustomImage();

	DivExtended gridDiv = new DivExtended();
	Div sliderDiv = new Div();
	Div labelDiv = new Div();

	Div resetButtonDiv = new Div();

	List<CustomImage> imageList = new ArrayList<CustomImage>();

	CustomGrid grid = new CustomGrid();

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

	List<Integer> rowArray = new ArrayList<Integer>();

	Map<Integer, LinkedHashSet<Integer>> columnMap = new HashMap<Integer, LinkedHashSet<Integer>>();

	public ImageChange() {

		setId("image-change");
		initWidget();

//		setAlignItems(Alignment.CENTER);
		// setWidth("70%");

		getElement().addEventListener("scroll", new DomEventListener() {

			@Override
			public void handleEvent(DomEvent event) {

				System.out.println("Scroll Check");

			}
		});

		gridDiv.getElement().addEventListener("scroll", new DomEventListener() {

			@Override
			public void handleEvent(DomEvent event) {

				System.out.println("Grid Scroll Check");

			}
		});
//		getElement().addEventListener("mouseleave", new DomEventListener() {
//
//			@Override
//			public void handleEvent(DomEvent event) {
//
//				// System.out.println("Moouse Leaved");
//
//			}
//		});
	}

	private void initWidget() {

		sliderDiv.setId("slider-div");
		labelDiv.setId("label-div");
		gridDiv.setId("grid-div");

		sliderDiv.addClassName("wrapper");

		// grid.getElement().getStyle().set("display", "contents");

		addComponentAtIndex(0, sliderDiv);
		addComponentAtIndex(1, labelDiv);

		// TODO Add a Label which changes according to the percentage

		addComponentAtIndex(2, gridDiv);

		// TODO Add second Label which changes according to the percentage

		DomEventListener l = e -> System.out.println("invoked!");

		gridDiv.getElement().addEventListener("scroll", l);

		imageList.add(customImage);

		grid.setItems(imageList);

		grid.addComponentColumn(i -> customImageTwo.getHorizontalLayout());
		grid.addComponentColumn(i -> customImageThree.getVerticalInitialLayout());
		grid.addComponentColumn(i -> customImageFour.getHorizontalLayout());
		grid.addComponentColumn(i -> customImageFive.getVerticalInitialLayout());
		grid.addComponentColumn(i -> customImageSix.getHorizontalLayout());
		grid.addComponentColumn(i -> customImageSeven.getVerticalInitialLayout());
		grid.addComponentColumn(i -> customImageEight.getHorizontalLayout());
		grid.addComponentColumn(i -> customImageNine.getVerticalInitialLayout());
		grid.addComponentColumn(i -> customImageTen.getHorizontalLayout());
		grid.addComponentColumn(i -> customImageEleven.getVerticalInitialLayout());

		gridDiv.add(grid);
		grid.setSelectionMode(Grid.SelectionMode.NONE);

		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.MATERIAL_COLUMN_DIVIDERS,
				GridVariant.LUMO_WRAP_CELL_CONTENT);

//		gridDiv.getElement().getStyle().set("display", "block");

		grid.setHeightByRows(true);

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

		AtomicReference<Registration> registrationHolder = new AtomicReference<>();
		registrationHolder
				.set(paperSlider.addChangeListener((ComponentEventListener<PaperSliderChangEvent>) slideEvent -> {
					registrationHolder.get().remove();
					registrationHolder.set(null);
					changeSusceptibleImage(slideEvent, customImageTwo, customImageThree, customImageFour,
							customImageFive, customImageSix, customImageSeven, customImageEight, customImageNine,
							customImageTen, customImageEleven);
				}));

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

		for (CustomImage customImageSingle : customImage) {
			customImageSingle.setVaccinatedImage();

		}

		Map<Integer, LinkedHashSet<Integer>> columnSet = getColumnMap(Integer.parseInt(sliderEvent.getValue()));

		for (Integer key : columnSet.keySet()) {
			Map<Integer, Image> map = customImage[key].getImageMap();

			LinkedHashSet<Integer> integerSet = columnSet.get(key);

			for (Integer hashInt : integerSet) {
				map.get(hashInt).setSrc("/images/susceptible.png");
			}

		}

		AtomicReference<Registration> registrationHolder = new AtomicReference<>();
		registrationHolder.set(gridDiv.addClickListener((ComponentEventListener<ClickEvent<Div>>) event -> {
			registrationHolder.get().remove();
			registrationHolder.set(null);
			changeImageYourKid(sliderEvent, columnSet, customImage);
		}));

//		gridDiv.getElement().addEventListener("scroll", new ScrollEventListener());
		// }

		valueLabel.setText(sliderEvent.getValue());

		// TODO Check count of the images and change accordingly

		// TODO
		// Check for changing the colour accordingly

		// TODO
		// Make the configurable code to change the color of the image

	}

	@SuppressWarnings("unchecked")
	private void changeImageYourKid(PaperSliderChangEvent sliderEvent,
			Map<Integer, LinkedHashSet<Integer>> customPassedSet, CustomImage... customImage) {

		// Setting always the element from the second column

		LinkedHashSet<Integer> integerSet = new LinkedHashSet<Integer>();

		List<Integer> listInt = new ArrayList<Integer>();

		int setCount = 0;
		while (setCount < customPassedSet.size()) {
			LinkedHashSet<Integer> innerSet = customPassedSet.get(setCount);

			if (innerSet.isEmpty()) {
				setCount++;
				continue;

			} else {
				integerSet.addAll(innerSet);
				integerSet.add(setCount);
				break;
			}

		}

		for (Integer integer : integerSet) {
			listInt.add(integer);
		}

		int positionYourKid = integerSet.iterator().next();

		int mapPosition = listInt.get(listInt.size() - 1);

		// TODO Based on slider event count value - need to change the image

		customImage[mapPosition].getImageMap().get(positionYourKid).setSrc("/images/susceptible-your-child.png");

		AtomicReference<Registration> registrationHolder = new AtomicReference<>();
		registrationHolder.set(gridDiv.addClickListener((ComponentEventListener<ClickEvent<Div>>) event -> {
			registrationHolder.get().remove();
			registrationHolder.set(null);

			// TODO Set First Sick Image
			setFirstSickImage(sliderEvent, customPassedSet, mapPosition, positionYourKid, customImage);

		}));

		startButton.setEnabled(false);

	}

	/**
	 * Setting first sick image
	 * 
	 * @param sliderEvent
	 * @param customPassedSet
	 * @param customImage
	 */
	private void setFirstSickImage(PaperSliderChangEvent sliderEvent,
			Map<Integer, LinkedHashSet<Integer>> customPassedSet, Integer mapPosition, Integer positionYourKid,
			CustomImage... customImage) {

		int keyValue = getRandomRow();

		int valKey = 0;

		for (Integer key : customPassedSet.keySet()) {

			LinkedHashSet<Integer> integerSet = customPassedSet.get(key);

			if (key == mapPosition || key == keyValue) {
				integerSet.remove(positionYourKid);
				continue;
			} else {
				valKey = keyValue;
				break;

			}
		}

		// Changing a random image for first sick
		customImage[valKey].getImageMap().get(valKey).setSrc("/images/infected.png");

		AtomicReference<Registration> registrationHolder = new AtomicReference<>();
		registrationHolder.set(gridDiv.addClickListener((ComponentEventListener<ClickEvent<Div>>) event -> {
			registrationHolder.get().remove();
			registrationHolder.set(null);

			setSickImages(sliderEvent, customPassedSet, mapPosition, positionYourKid, customImage);
		}));

	}

	/**
	 * The map for images based on the slider value selection
	 * 
	 * @param sliderValue
	 * @return
	 */
	private Map<Integer, LinkedHashSet<Integer>> getColumnMap(int sliderValue) {

		int modulus = Math.round(sliderValue % 10);

		int nearestMultiple = 10 * (Math.round(sliderValue / 10)) + 10;

		int columnCount = nearestMultiple / 10;

		LinkedHashSet<Integer> columnList = new LinkedHashSet<Integer>();

		// When Slider value less than 10 -> 1 image will be random selected from any
		// column
		if (columnCount == 0) {
			while (columnList.size() < modulus) {

				int columnValue = getRandomRow();
				columnList.add(columnValue);
			}

			columnMap.put(columnCount, columnList);
		}

		// When Slider value More than 10 -> n images will be random selected from all
		// columns
		else {

			while (columnList.size() < 10)

			{
				int columnValue = getRandomRow();
				columnList.add(columnValue);
			}

			System.out.println("Column List : " + columnList);

			// Creating the map for the first multiple of 10
			for (Integer column : columnList) {
				LinkedHashSet<Integer> imageList = getRandomList(columnCount);

				columnMap.put(column, imageList);
			}

			// Create image list for remaining images - modulus
			int imageCountToRemove = 10 - modulus;
			for (int removeCounter = 0; removeCounter < imageCountToRemove; removeCounter++)

			{

				LinkedHashSet<Integer> imageListRemoved = new LinkedHashSet<Integer>();
				imageListRemoved = removeImage(columnMap.get(removeCounter));

				columnMap.put(removeCounter, imageListRemoved);

			}

		}

		return columnMap;
	}

	/**
	 * To remove the first element from the image list
	 * 
	 * @param imageList
	 * @return
	 */
	private LinkedHashSet<Integer> removeImage(LinkedHashSet<Integer> imageList) {

		imageList.remove(imageList.iterator().next());

		return imageList;

	}

	/**
	 * Based on column count it will return number 'random'
	 * 
	 * @param columnCount
	 * @return
	 */
	public LinkedHashSet<Integer> getRandomList(int columnCount) {

		LinkedHashSet<Integer> imageList = new LinkedHashSet<Integer>();

		while (imageList.size() < columnCount)

		{
			int columnValue = getRandomRow();
			imageList.add(columnValue);
		}

		return imageList;

	}

	/**
	 * Setting sick images
	 * 
	 * @param sliderEvent
	 * @param customPassedSet
	 * @param mapPosition
	 * @param positionYourKid
	 * @param customImage
	 */
	private void setSickImages(PaperSliderChangEvent sliderEvent, Map<Integer, LinkedHashSet<Integer>> customPassedSet,
			Integer mapPosition, Integer positionYourKid, CustomImage... customImage) {

		for (Integer key : customPassedSet.keySet()) {

			Map<Integer, Image> map = customImage[key].getImageMap();

			LinkedHashSet<Integer> integerSet = customPassedSet.get(key);

			if (key == mapPosition) {
				integerSet.remove(positionYourKid);
			}

			if (!(integerSet.isEmpty())) {
				integerSet.remove(integerSet.iterator().next());
			}
			if (!(integerSet.isEmpty())) {
				integerSet.remove(integerSet.iterator().next());
			}

			for (Integer hashInt : integerSet) {
				map.get(hashInt).setSrc("/images/infected.png");
			}

		}

		AtomicReference<Registration> registrationHolder = new AtomicReference<>();
		registrationHolder.set(gridDiv.addClickListener((ComponentEventListener<ClickEvent<Div>>) event -> {
			registrationHolder.get().remove();
			registrationHolder.set(null);

			// TODO This will be effective only based on Formula
			setInfectedChild(sliderEvent, customPassedSet, mapPosition, positionYourKid, customImage);
		}));

	}

	/**
	 * Setting your kid as infected
	 * 
	 * @param sliderEvent
	 * @param customPassedSet
	 * @param customImage
	 */
	private void setInfectedChild(PaperSliderChangEvent sliderEvent,
			Map<Integer, LinkedHashSet<Integer>> customPassedSet, Integer mapPosition, Integer positionYourKid,
			CustomImage... customImage) {

		customImage[mapPosition].getImageMap().get(positionYourKid).setSrc("/images/infected-your-child.png");

		// Reset Everything on Click
		AtomicReference<Registration> registrationHolder = new AtomicReference<>();
		registrationHolder.set(gridDiv.addClickListener((ComponentEventListener<ClickEvent<Div>>) event -> {
			registrationHolder.get().remove();
			registrationHolder.set(null);

			resetAll(sliderEvent, customPassedSet, customImage);

		}));

	}

	/**
	 * Resetting Back
	 * 
	 * @param sliderEvent
	 * @param customPassedSet
	 * @param customImage
	 */
	private void resetAll(PaperSliderChangEvent sliderEvent, Map<Integer, LinkedHashSet<Integer>> customPassedSet,
			CustomImage... customImage) {

		sliderEvent.getSource().getElement().removeAttribute("disabled");
		for (CustomImage customImageSingle : customImage) {
			customImageSingle.setVaccinatedImage();

		}

		valueLabel.setText(paperSlider.getInitValue());

		AtomicReference<Registration> registrationHolder = new AtomicReference<>();
		registrationHolder
				.set(paperSlider.addChangeListener((ComponentEventListener<PaperSliderChangEvent>) slideEvent -> {
					registrationHolder.get().remove();
					registrationHolder.set(null);

					changeSusceptibleImage(slideEvent, customImageTwo, customImageThree, customImageFour,
							customImageFive, customImageSix, customImageSeven, customImageEight, customImageNine,
							customImageTen, customImageEleven);
				}));
	}

	/**
	 * Getting a random row
	 * 
	 * @return
	 */
	public int getRandomRow() {

		int randomRow = ((int) (Math.random() * ((9 - 0) + 1)) + 0);

		rowArray.add(randomRow);

		return randomRow;
	}
}
