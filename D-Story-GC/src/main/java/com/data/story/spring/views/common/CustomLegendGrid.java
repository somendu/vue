/**
 * 
 */
package com.data.story.spring.views.common;

import com.vaadin.flow.component.grid.Grid;

/**
 * @author somendu
 *
 */

public class CustomLegendGrid extends Grid<CustomLegendComponent> {

	public CustomLegendGrid() {

		setId("legend-grid");
		setThemeName("image-compact");
		// addClassName("image-display");

	}
}
