/**
 * 
 */
package com.gmail.marco.ui.views.support;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;

/**
 * @author somendu
 *
 */
@DomEvent("change")
public class PaperSliderChangEvent extends ComponentEvent<PaperSlider> {

	private String value = "value";

	public PaperSliderChangEvent(PaperSlider source, boolean fromClient,
			@EventData("event.target.value") String value) {

		super(source, fromClient);
		this.value = value;

	}

	public String getValue() {
		return value;
	}

}
