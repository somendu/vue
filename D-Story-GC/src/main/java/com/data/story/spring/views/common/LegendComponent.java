/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.data.story.spring.views.common;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;

/**
 * Displays the list of available categories, with a search filter as well as
 * buttons to add a new category or edit existing ones.
 *
 * Implemented using a simple template.
 */

//@Route(value = "ImageChange", layout = MainView.class)
//@RouteAlias(value = "", layout = MainView.class)

@Tag("legend-component")

public class LegendComponent extends Grid {

	private Label textLabel = new Label();

	public LegendComponent() {

		setId("text-component");
		initWidget();

	}

	private void initWidget() {

	}

	public void setComponentText(String string) {

	}

}
