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

public class CustomLegendGrid extends Grid<CustomLegendComponent> {

	public CustomLegendGrid() {

		setId("legend-grid");
		setThemeName("image-compact");

		addClassName("margin-top-15");

		setSelectionMode(Grid.SelectionMode.NONE);

		addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.MATERIAL_COLUMN_DIVIDERS,
				GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_NO_ROW_BORDERS);

		setHeightByRows(true);

		List<Column<CustomLegendComponent>> columnsList = getColumns();

		for (Column<CustomLegendComponent> columnImage : columnsList) {
			columnImage.setFlexGrow(0);

			// TODO This is to get the key name
			// columnImage.setKey("Key Setting");

		}

	}
}
