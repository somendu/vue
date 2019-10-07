/**
 * 
 */
package com.data.story.spring.views.common;

import com.vaadin.flow.component.grid.Grid;

/**
 * @author somendu
 *
 */

public class CustomGrid extends Grid<CustomImage> {

	public CustomGrid() {

		setId("image-grid");
		setThemeName("image-compact");

	}
}
