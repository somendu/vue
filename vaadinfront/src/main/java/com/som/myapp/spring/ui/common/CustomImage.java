/**
 * 
 */
package com.som.myapp.spring.ui.common;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import lombok.Data;

/**
 * @author somendu
 *
 */
@Tag(Tag.IMG)
@Data
public class CustomImage extends Div {

	public CustomImage() {
		setImage();
	}

	Div div = new Div();

	Image imageOne = new Image();
	Image imageTwo = new Image();
	Image imageThree = new Image();
	Image imageFour = new Image();
	Image imageFive = new Image();
	Image imageSix = new Image();
	Image imageSeven = new Image();
	Image imageEight = new Image();

	/**
	 * Setting Image Path
	 * 
	 * @param imagePath
	 */
	public void setImage() {

		setImageSize(imageOne);
		setImageSize(imageTwo);
		setImageSize(imageThree);
		setImageSize(imageFour);
		setImageSize(imageFive);
		setImageSize(imageSix);
		setImageSize(imageSeven);
		setImageSize(imageEight);

		imageOne.setSrc("/images/vaccinated.png");
		imageTwo.setSrc("/images/vaccinated.png");
		imageThree.setSrc("/images/vaccinated.png");
		imageFour.setSrc("/images/vaccinated.png");
		imageFive.setSrc("/images/vaccinated.png");
		imageSix.setSrc("/images/vaccinated.png");
		imageSeven.setSrc("/images/vaccinated.png");
		imageEight.setSrc("/images/vaccinated.png");
	}

	private void setImageSize(Image image) {

		image.setWidth("29px");
		image.setHeight("39px");

	}

	public HorizontalLayout getHorizontalLayout() {

		HorizontalLayout horizontalLayout = new HorizontalLayout();

		horizontalLayout.add(imageOne);
		horizontalLayout.add(imageTwo);
		horizontalLayout.add(imageThree);
		horizontalLayout.add(imageFour);
		horizontalLayout.add(imageFive);
		horizontalLayout.add(imageSix);
		horizontalLayout.add(imageSeven);
		horizontalLayout.add(imageEight);
		horizontalLayout.getElement().getStyle().set("display", "contents");

		horizontalLayout.setAlignItems(Alignment.CENTER);
		return horizontalLayout;
	}

	public VerticalLayout getVerticalInitialLayout() {

		VerticalLayout verticalLayout = new VerticalLayout();

		verticalLayout.add(imageOne);
		verticalLayout.add(imageTwo);
		verticalLayout.add(imageThree);
		verticalLayout.add(imageFour);
		verticalLayout.add(imageFive);
		verticalLayout.add(imageSix);
		verticalLayout.add(imageSeven);
		verticalLayout.add(imageEight);
		verticalLayout.getElement().getStyle().set("display", "initial");
//		verticalLayout.setAlignItems(Alignment.END);

		return verticalLayout;
	}

	public VerticalLayout getVerticalInheritLayout() {

		VerticalLayout verticalLayout = new VerticalLayout();

		verticalLayout.add(imageOne);
		verticalLayout.add(imageTwo);
		verticalLayout.add(imageThree);
		verticalLayout.add(imageFour);
		verticalLayout.add(imageFive);
		verticalLayout.add(imageSix);
		verticalLayout.add(imageSeven);
		verticalLayout.add(imageEight);
		verticalLayout.getElement().getStyle().set("display", "inherit");
		verticalLayout.setAlignItems(Alignment.END);

		return verticalLayout;
	}

	public VerticalLayout getVerticalTwoImageLayout() {

		VerticalLayout verticalLayout = new VerticalLayout();

		verticalLayout.add(imageOne);
		verticalLayout.add(imageTwo);
		verticalLayout.add(imageThree);
		verticalLayout.add(imageFour);

		verticalLayout.getElement().getStyle().set("display", "inherit");
		verticalLayout.setAlignItems(Alignment.END);

		return verticalLayout;
	}

	public void setSusceptibleImage() {

		imageOne.removeAll();
		imageThree.removeAll();
		imageOne.setSrc("/images/susceptible.png");
		imageThree.setSrc("/images/susceptible.png");

	}

	public void setVaccinatedImage() {

		imageOne.removeAll();
		imageThree.removeAll();
		imageOne.setSrc("/images/vaccinated.png");
		imageThree.setSrc("/images/vaccinated.png");

	}

	public void setInfectedImage() {

		imageOne.removeAll();
		imageThree.removeAll();
		imageOne.setSrc("/images/infected.png");
		imageThree.setSrc("/images/infected.png");

	}

	public void setSusceptibleChildImage() {

		imageOne.removeAll();
		imageOne.setSrc("/images/susceptible-your-child.png");

	}

	public void setInfectedChildImage() {

		imageOne.removeAll();
		imageOne.setSrc("/images/infected-your-child.png");

	}
}
