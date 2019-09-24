/**
 * 
 */
package com.som.myapp.spring.ui.common;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.shared.Registration;

/**
 * @author somendu
 *
 */
@Tag("paper-slider")
@JsModule("@polymer/paper-slider/paper-slider.js")
public class PaperSlider extends Component {

	private String min;
	private String max;
	private String initValue;

	public PaperSlider(String min, String max, String initValue) {

		this.min = min;
		this.max = max;
		this.initValue = initValue;

		getElement().setAttribute("min", min);
		getElement().setAttribute("max", max);
		getElement().setAttribute("value", initValue);

	}

	public Registration addChangeListener(ComponentEventListener<PaperSliderChangEvent> listener) {

		return addListener(PaperSliderChangEvent.class, listener);
	}

	// TODO
	// Check how to change the layout of slider
	// Check how to change the style of slider

	/**
	 * @return the min
	 */
	public String getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(String min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public String getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(String max) {
		this.max = max;
	}

	/**
	 * @return the initValue
	 */
	public String getInitValue() {
		return initValue;
	}

	/**
	 * @param initValue the initValue to set
	 */
	public void setInitValue(String initValue) {
		this.initValue = initValue;
	}

}
