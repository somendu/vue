/**
 * 
 */
package com.data.story.spring.views.common;

import java.util.HashMap;
import java.util.Map;

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

	/**
	 * 
	 */
	private static final long serialVersionUID = 5320555960852120490L;

	public CustomImage() {
		setImage();
	}

	Div div = new Div();

	Image imageZero = new Image();
	Image imageOne = new Image();
	Image imageTwo = new Image();
	Image imageThree = new Image();
	Image imageFour = new Image();
	Image imageFive = new Image();
	Image imageSix = new Image();
	Image imageSeven = new Image();
	Image imageEight = new Image();
	Image imageNine = new Image();

	Map<Integer, Image> imageMap = new HashMap<Integer, Image>();

	/**
	 * Setting Image Path
	 * 
	 * @param imagePath
	 */
	public void setImage() {

		setImageSize(imageZero);
		setImageSize(imageOne);
		setImageSize(imageTwo);
		setImageSize(imageThree);
		setImageSize(imageFour);
		setImageSize(imageFive);
		setImageSize(imageSix);
		setImageSize(imageSeven);
		setImageSize(imageEight);
		setImageSize(imageNine);

		imageZero.setSrc("/images/vaccinated.png");
		imageOne.setSrc("/images/vaccinated.png");
		imageTwo.setSrc("/images/vaccinated.png");
		imageThree.setSrc("/images/vaccinated.png");
		imageFour.setSrc("/images/vaccinated.png");
		imageFive.setSrc("/images/vaccinated.png");
		imageSix.setSrc("/images/vaccinated.png");
		imageSeven.setSrc("/images/vaccinated.png");
		imageEight.setSrc("/images/vaccinated.png");
		imageNine.setSrc("/images/vaccinated.png");

		imageMap.put(0, imageZero);
		imageMap.put(1, imageOne);
		imageMap.put(2, imageTwo);
		imageMap.put(3, imageThree);
		imageMap.put(4, imageFour);
		imageMap.put(5, imageFive);
		imageMap.put(6, imageSix);
		imageMap.put(7, imageSeven);
		imageMap.put(8, imageEight);
		imageMap.put(9, imageNine);
	}

	/**
	 * Setting Image Size
	 * 
	 * @param image
	 */
	private void setImageSize(Image image) {

		image.setWidth("29px");
		image.setHeight("39px");

	}

	/**
	 * Horizontal Layout images put
	 * 
	 * @return
	 */
	public HorizontalLayout getHorizontalLayout() {

		HorizontalLayout horizontalLayout = new HorizontalLayout();

		horizontalLayout.add(imageZero);
		horizontalLayout.add(imageOne);
		horizontalLayout.add(imageTwo);
		horizontalLayout.add(imageThree);
		horizontalLayout.add(imageFour);
		horizontalLayout.add(imageFive);
		horizontalLayout.add(imageSix);
		horizontalLayout.add(imageSeven);
		horizontalLayout.add(imageEight);
		horizontalLayout.add(imageNine);

		horizontalLayout.getElement().getStyle().set("display", "contents");

		horizontalLayout.setAlignItems(Alignment.CENTER);
		return horizontalLayout;
	}

	/**
	 * Display of the images in vertical initial layout
	 * 
	 * @return
	 */
	public VerticalLayout getVerticalInitialLayout() {

		VerticalLayout verticalLayout = new VerticalLayout();

		verticalLayout.add(imageZero);
		verticalLayout.add(imageOne);
		verticalLayout.add(imageTwo);
		verticalLayout.add(imageThree);
		verticalLayout.add(imageFour);
		verticalLayout.add(imageFive);
		verticalLayout.add(imageSix);
		verticalLayout.add(imageSeven);
		verticalLayout.add(imageEight);
		verticalLayout.add(imageNine);

		verticalLayout.getElement().getStyle().set("display", "initial");
//		verticalLayout.setAlignItems(Alignment.END);

		return verticalLayout;
	}

	/**
	 * Vertical Inherit Layout
	 * 
	 * @return
	 */
	public VerticalLayout getVerticalInheritLayout() {

		VerticalLayout verticalLayout = new VerticalLayout();

		verticalLayout.add(imageZero);
		verticalLayout.add(imageOne);
		verticalLayout.add(imageTwo);
		verticalLayout.add(imageThree);
		verticalLayout.add(imageFour);
		verticalLayout.add(imageFive);
		verticalLayout.add(imageSix);
		verticalLayout.add(imageSeven);
		verticalLayout.add(imageEight);
		verticalLayout.add(imageNine);

		verticalLayout.getElement().getStyle().set("display", "inherit");
		verticalLayout.setAlignItems(Alignment.END);

		return verticalLayout;
	}

	/**
	 * Only 4 images in this layout
	 * 
	 * @return
	 */
	public VerticalLayout getVerticalFourImageLayout() {

		VerticalLayout verticalLayout = new VerticalLayout();

		verticalLayout.add(imageZero);
		verticalLayout.add(imageOne);
		verticalLayout.add(imageTwo);
		verticalLayout.add(imageFour);

		verticalLayout.getElement().getStyle().set("display", "inherit");
		verticalLayout.setAlignItems(Alignment.END);

		return verticalLayout;
	}

	// TODO Need to change the image color configuration based

	public void setSusceptibleImage() {

		imageZero.removeAll();
		imageZero.setSrc("/images/susceptible.png");

	}

	public void setVaccinatedImage() {

		imageZero.removeAll();
		imageTwo.removeAll();
		imageZero.setSrc("/images/vaccinated.png");
		imageOne.setSrc("/images/vaccinated.png");
		imageTwo.setSrc("/images/vaccinated.png");
		imageFour.setSrc("/images/vaccinated.png");
		imageFive.setSrc("/images/vaccinated.png");
		imageSix.setSrc("/images/vaccinated.png");
		imageSeven.setSrc("/images/vaccinated.png");
		imageEight.setSrc("/images/vaccinated.png");
		imageNine.setSrc("/images/vaccinated.png");
		imageThree.setSrc("/images/vaccinated.png");

	}

	public void setInfectedImage() {

		imageZero.removeAll();
		imageTwo.removeAll();
		imageZero.setSrc("/images/infected.png");
		imageTwo.setSrc("/images/infected.png");

	}

	public void setSusceptibleChildImage() {

		imageZero.removeAll();
		imageZero.setSrc("/images/susceptible-your-child.png");

	}

	public void setInfectedChildImage() {

		imageZero.removeAll();
		imageZero.setSrc("/images/infected-your-child.png");

	}
}
