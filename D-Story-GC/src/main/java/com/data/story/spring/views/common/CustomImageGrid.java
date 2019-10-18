/**
 * 
 */
package com.data.story.spring.views.common;

import java.util.List;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

/**
 * @author somendu
 *
 */

public class CustomImageGrid extends Grid<CustomImage> {

	public CustomImageGrid() {

		setId("image-grid");
		setThemeName("image-compact");

		setSelectionMode(Grid.SelectionMode.NONE);

		addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.MATERIAL_COLUMN_DIVIDERS,
				GridVariant.LUMO_WRAP_CELL_CONTENT);

		setHeightByRows(true);

		List<Column<CustomImage>> columnsList = getColumns();

		for (Column<CustomImage> columnImage : columnsList) {
			columnImage.setFlexGrow(0);

			// TODO This is to get the key name
			// columnImage.setKey("Key Setting");

		}
	}
}
