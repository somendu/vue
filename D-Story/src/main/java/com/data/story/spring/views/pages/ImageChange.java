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
import com.data.story.spring.views.common.CustomLegendComponent;
import com.data.story.spring.views.common.DivExtended;
import com.data.story.spring.views.common.PaperSlider;
import com.data.story.spring.views.common.PaperSliderChangEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
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
//@CssImport(value = "./styles/views/imagechange/image-change.css", include = "lumo-badge")
//@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class ImageChange extends Div {

	CustomImage customImageTransfer = new CustomImage();

	DivExtended gridDiv = new DivExtended();
	Div sliderDiv = new Div();
	Div labelDiv = new Div();

	Div resetButtonDiv = new Div();

	ArrayList<CustomImage> imageList = new ArrayList<CustomImage>();

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

//	Map<Integer, LinkedHashSet<Integer>> columnMap = new HashMap<Integer, LinkedHashSet<Integer>>();

//	private TextComponent sliderText = new TextComponent();
//	private TextComponent gridText = new TextComponent();

	// Showing the legend
	Div legendDiv = new Div();

	CustomLegendComponent vaccinatedLegend = new CustomLegendComponent();
	CustomLegendComponent susceptibleLegend = new CustomLegendComponent();
	CustomLegendComponent infectedLegend = new CustomLegendComponent();
	CustomLegendComponent yourKidSuscetibleLegend = new CustomLegendComponent();
	CustomLegendComponent yourKidInfectedLegend = new CustomLegendComponent();

	List<CustomLegendComponent> legendComponentList = new ArrayList<CustomLegendComponent>();

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

		legendDiv.setId("legend-div");

//		sliderDiv.addClassName("wrapper");

		// grid.getElement().getStyle().set("display", "contents");

		addComponentAtIndex(0, sliderDiv);
		addComponentAtIndex(1, labelDiv);

		// TODO Add a Label which changes according to the percentage

		addComponentAtIndex(2, gridDiv);

		// TODO Add second Label which changes according to the percentage

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

		grid.setHeightByRows(true);

		List<Column<CustomImage>> columnsList = grid.getColumns();

		for (Column<CustomImage> columnImage : columnsList) {
			columnImage.setFlexGrow(0);

			// TODO This is to get the key name
			// columnImage.setKey("Key Setting");

		}

		sliderDiv.add(paperSlider);

		valueLabel.setText(paperSlider.getInitValue());
		valueLabel.addClassName("label-text");

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

		// Showing the grid for the legend

		vaccinatedLegend.getLegendImage().setSrc("/images/vaccinated.png");
		susceptibleLegend.getLegendImage().setSrc("/images/susceptible.png");
		infectedLegend.getLegendImage().setSrc("/images/infected.png");
		yourKidSuscetibleLegend.getLegendImage().setSrc("/images/susceptible-your-child.png");
		yourKidInfectedLegend.getLegendImage().setSrc("/images/infected-your-child.png");

		vaccinatedLegend.getTextComponent().setText(" Vaccinated");
		susceptibleLegend.getTextComponent().setText(" Susceptible");
		infectedLegend.getTextComponent().setText(" Infected");
		yourKidSuscetibleLegend.getTextComponent().setText(" Your Kid Susceptible");
		yourKidInfectedLegend.getTextComponent().setText(" Your Kid Infected");

		legendDiv.add(vaccinatedLegend, susceptibleLegend, infectedLegend, yourKidSuscetibleLegend,
				yourKidInfectedLegend);

		addComponentAtIndex(3, legendDiv);

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

//		sliderText.setComponentText("Text to change as per " + sliderEvent.getValue() + " percentage ");
//		sliderDiv.add(sliderText);
//
//		gridText.setComponentText(
//				"The Text below the grid to Change as per " + sliderEvent.getValue() + " percentage ");
//
//		addComponentAtIndex(3, gridText);

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
			// System.out.println("Key : " + key);
			Map<Integer, Image> map = customImage[key].getImageMap();

			LinkedHashSet<Integer> integerSet = columnSet.get(key);

			for (Integer hashInt : integerSet) {
				// System.out.println("hashInt : " + hashInt);
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

		LinkedHashSet<Integer> positionKidSet = new LinkedHashSet<Integer>();

		List<Integer> listInt = new ArrayList<Integer>();

		// TODO Take the integer set from customPassedSet

		for (Integer key : customPassedSet.keySet()) {
//			System.out.println("key at changeImageYourKid : " + key);

			LinkedHashSet<Integer> innerSet = customPassedSet.get(key);

			if (innerSet == null || innerSet.isEmpty()) {
				// setCount++;
				continue;

			} else {
				positionKidSet.addAll(innerSet);
				positionKidSet.add(key);
				break;
			}

		}

		for (Integer integer : positionKidSet) {
			listInt.add(integer);
		}

		int positionYourKid = positionKidSet.iterator().next();

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

		// TODO Changing a random image for first sick from the set only
		customImage[valKey].getImageMap().get(valKey).setSrc("/images/infected.png");

		AtomicReference<Registration> registrationHolder = new AtomicReference<>();
		registrationHolder.set(gridDiv.addClickListener((ComponentEventListener<ClickEvent<Div>>) event -> {
			registrationHolder.get().remove();
			registrationHolder.set(null);

			setSickImages(sliderEvent, customPassedSet, mapPosition, positionYourKid, customImage);
		}));

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

			// TODO Take this on count else call setSusceptibleChild
			if (Integer.parseInt(sliderEvent.getValue()) > 50) {
				setInfectedChild(sliderEvent, customPassedSet, mapPosition, positionYourKid, customImage);
			} else {
				setSusceptibleChild(sliderEvent, customPassedSet, mapPosition, positionYourKid, customImage);
			}
		}));

	}

	/**
	 * Method for keeping the child as susceptible only
	 * 
	 * @param sliderEvent
	 * @param customPassedSet
	 * @param customImage
	 */
	private void setSusceptibleChild(PaperSliderChangEvent sliderEvent,
			Map<Integer, LinkedHashSet<Integer>> customPassedSet, Integer mapPosition, Integer positionYourKid,
			CustomImage... customImage) {

		customImage[mapPosition].getImageMap().get(positionYourKid).setSrc("/images/susceptible-your-child.png");

		// Reset Everything on Click
		AtomicReference<Registration> registrationHolder = new AtomicReference<>();
		registrationHolder.set(gridDiv.addClickListener((ComponentEventListener<ClickEvent<Div>>) event -> {
			registrationHolder.get().remove();
			registrationHolder.set(null);

			resetAll(sliderEvent, customPassedSet, customImage);

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

		// valueLabel.setText(paperSlider.getInitValue());

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

	/**
	 * The map for images based on the slider value selection
	 * 
	 * @param sliderValue
	 * @return
	 */
	private Map<Integer, LinkedHashSet<Integer>> getColumnMap(int sliderValue) {

		Map<Integer, LinkedHashSet<Integer>> columnSet = new HashMap<Integer, LinkedHashSet<Integer>>();

		int modulus = Math.round(sliderValue % 10);

		System.out.println("Modulus : " + modulus);

		int nearestMultiple = 10 * (Math.round(sliderValue / 10)) + 10;

		System.out.println("nearestMultiple : " + nearestMultiple);

		int columnCount = nearestMultiple / 10;

		System.out.println("columnCount : " + columnCount);

		LinkedHashSet<Integer> columnList = new LinkedHashSet<Integer>();

		// When Slider value less than 10 -> 1 image will be random selected from any
		// column
		if (columnCount == 1) {
			while (columnList.size() < modulus) {

				int columnValue = getRandomRow();
				columnList.add(columnValue);
			}

			System.out.println("Column List When column count 1 : " + columnList);

			// Creating the map for the first multiple of 10
			for (Integer column : columnList) {
				LinkedHashSet<Integer> imageList = getRandomList(columnCount);

				columnSet.put(column, imageList);
			}
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

				columnSet.put(column, imageList);
			}

			// Create image list for remaining images - modulus
			int imageCountToRemove = 10 - modulus;
			for (int removeCounter = 0; removeCounter < imageCountToRemove; removeCounter++)

			{

				LinkedHashSet<Integer> imageListRemoved = new LinkedHashSet<Integer>();
				imageListRemoved = removeImage(columnSet.get(removeCounter));

				columnSet.put(removeCounter, imageListRemoved);

			}

		}

		return columnSet;
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
}
