/**
 * 
 */
package com.data.story.spring.views.common;

import com.vaadin.flow.component.grid.Grid;

/**
 * @author somendu
 *
 */

public class CustomImageGrid extends Grid<CustomImage> {

	public CustomImageGrid() {

		setId("image-grid");
		setThemeName("image-compact");
		// addClassName("image-display");

	}
}
