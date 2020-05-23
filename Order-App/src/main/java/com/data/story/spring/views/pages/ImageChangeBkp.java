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

import com.data.story.spring.views.common.CustomImage;
import com.data.story.spring.views.common.CustomImageGrid;
import com.data.story.spring.views.common.CustomLegendComponent;
import com.data.story.spring.views.common.PaperSlider;
import com.data.story.spring.views.common.PaperSliderChangEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
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
public class ImageChangeBkp extends Div {

	private Div gridDiv = new Div();
	private Div sliderDiv = new Div();
	private Div labelDiv = new Div();

	private ArrayList<CustomImage> imageList = new ArrayList<CustomImage>();

	private CustomImageGrid grid = new CustomImageGrid();

	private CustomImage customImage = new CustomImage();
	private CustomImage customImageTwo = new CustomImage();
	private CustomImage customImageThree = new CustomImage();
	private CustomImage customImageFour = new CustomImage();
	private CustomImage customImageFive = new CustomImage();
	private CustomImage customImageSix = new CustomImage();
	private CustomImage customImageSeven = new CustomImage();
	private CustomImage customImageEight = new CustomImage();
	private CustomImage customImageNine = new CustomImage();
	private CustomImage customImageTen = new CustomImage();
	private CustomImage customImageEleven = new CustomImage();

	private PaperSlider paperSlider = new PaperSlider("0", "100", "0");

	private Label valueLabel = new Label("");

	private Button startButton = new Button("Start");

	private List<Integer> rowArray = new ArrayList<Integer>();

	// Showing the legend
	private Div legendDiv = new Div();

	private CustomLegendComponent vaccinatedLegend = new CustomLegendComponent();
	private CustomLegendComponent susceptibleLegend = new CustomLegendComponent();
	private CustomLegendComponent infectedLegend = new CustomLegendComponent();
	private CustomLegendComponent yourKidSuscetibleLegend = new CustomLegendComponent();
	private CustomLegendComponent yourKidInfectedLegend = new CustomLegendComponent();

	public ImageChangeBkp() {

		setId("image-change");
		initImageGrid();

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

	}

	/**
	 * Initializing the image grid component
	 */
	private void initImageGrid() {

		sliderDiv.setId("slider-div");
		labelDiv.setId("label-div");
		gridDiv.setId("grid-div");
		legendDiv.setId("legend-div");

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

		Map<Integer, LinkedHashSet<Integer>> columnSet = getColumnMap(100 - Integer.parseInt(sliderEvent.getValue()));

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
			// System.out.println("Key inside Sick Images : " + key);
			Map<Integer, Image> map = customImage[key].getImageMap();

			LinkedHashSet<Integer> integerSet = customPassedSet.get(key);

			for (int setInt : integerSet) {
				// System.out.println("Integer Set here : " + setInt);
			}

			if (key == mapPosition) {
				integerSet.remove(positionYourKid);
			}

			if (!(integerSet.isEmpty())) {
				// System.out.println("To remove first : " + integerSet.iterator().next());
				integerSet.remove(integerSet.iterator().next());
			}
			if (!(integerSet.isEmpty())) {
				// System.out.println("To remove second : " + integerSet.iterator().next());
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
			if (Integer.parseInt(sliderEvent.getValue()) < 50) {
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
	 * @param customImageArray
	 */
	private void resetAll(PaperSliderChangEvent sliderEvent, Map<Integer, LinkedHashSet<Integer>> customPassedSet,
			CustomImage... customImageArray) {

		sliderEvent.getSource().getElement().removeAttribute("disabled");

		for (CustomImage customImage : customImageArray) {
			customImage.setVaccinatedImage();

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

		// System.out.println("Modulus : " + modulus);

		int nearestMultiple = 10 * (Math.round(sliderValue / 10)) + 10;

		// System.out.println("nearestMultiple : " + nearestMultiple);

		int columnCount = nearestMultiple / 10;

		// System.out.println("columnCount : " + columnCount);

		LinkedHashSet<Integer> columnList = new LinkedHashSet<Integer>();

		// When Slider value less than 10 -> 1 image will be random selected from any
		// column
		if (columnCount == 1) {
			while (columnList.size() < modulus) {

				int columnValue = getRandomRow();
				columnList.add(columnValue);
			}

			// System.out.println("Column List When column count 1 : " + columnList);

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

			// System.out.println("Column List : " + columnList);

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
