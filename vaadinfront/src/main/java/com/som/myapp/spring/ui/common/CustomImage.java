/**
 * 
 */
package com.som.myapp.spring.ui.common;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;

/**
 * @author somendu
 *
 */
public class CustomImage extends Component {

	Div div = new Div();

	Image image = new Image();

	public void setSource() {
		image.setSrc("/images/vaccinated.png");
	}

	public Image getImage() {
		return image;
	}
}
