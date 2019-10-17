/**
 * 
 */
package com.data.story.spring.views.common;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import lombok.Data;

/**
 * @author somendu
 *
 */

@Data
public class CustomLegendComponent extends Div {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5320555960852120490L;

	public CustomLegendComponent() {

		setLegend();
	}

	public CustomLegendComponent(String src, String text) {
		setLegend(src, text);
	}

	Div div = new Div();

	Image legendImage = new Image();

	TextComponent textComponent = new TextComponent();

	/**
	 * Setting Image Path
	 * 
	 * @param imagePath
	 */
	public void setLegend() {

		setImageSize(legendImage);

		add(getHorizontalLayout());

	}

	/**
	 * Setting Image Path
	 * 
	 * @param imagePath
	 */
	public void setLegend(String src, String text) {

		setImageSize(legendImage);

		legendImage.setSrc(src);
		textComponent.setText(text);
		add(getHorizontalLayout());

	}

	/**
	 * Setting Image Size
	 * 
	 * @param image
	 */
	private void setImageSize(Image image) {

		image.setWidth("29px");
		image.setHeight("39px");

		image.getElement().getStyle().set("padding", "1%");

	}

	/**
	 * Horizontal Layout images put
	 * 
	 * @return
	 */
	public HorizontalLayout getHorizontalLayout(String imageSrc, String text) {

		HorizontalLayout horizontalLayout = new HorizontalLayout();

		legendImage.setSrc(imageSrc);
		textComponent.setText(text);

		horizontalLayout.add(legendImage);

		horizontalLayout.add(textComponent);

		horizontalLayout.getElement().getStyle().set("display", "flex");

		horizontalLayout.setAlignItems(Alignment.CENTER);
		return horizontalLayout;
	}

	/**
	 * Horizontal Layout images put
	 * 
	 * @return
	 */
	public HorizontalLayout getHorizontalLayout() {

		HorizontalLayout horizontalLayout = new HorizontalLayout();

		horizontalLayout.add(legendImage);

		horizontalLayout.add(textComponent);

		horizontalLayout.getElement().getStyle().set("display", "flex");

		horizontalLayout.setAlignItems(Alignment.CENTER);
		return horizontalLayout;
	}

}
