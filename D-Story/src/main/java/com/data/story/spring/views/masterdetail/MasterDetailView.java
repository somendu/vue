package com.data.story.spring.views.masterdetail;

import org.springframework.beans.factory.annotation.Autowired;

import com.data.story.spring.MainView;
import com.data.story.spring.backend.BackendService;
import com.data.story.spring.backend.Employee;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("master-detail-view")
@HtmlImport("frontend://src/views/master-detail-view.html")
@Route(value = "masterDetail", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("MasterDetail")
@CssImport("styles/views/masterdetail/master-detail-view.css")
//@Route(value = BakeryConst.PAGE_STOREFRONT, layout = MainView.class)
//@RouteAlias(value = BakeryConst.PAGE_STOREFRONT_EDIT, layout = MainView.class)
//@RouteAlias(value = BakeryConst.PAGE_ROOT, layout = MainView.class)
//@PageTitle(BakeryConst.TITLE_STOREFRONT)

public class MasterDetailView extends PolymerTemplate<TemplateModel> {

	@Autowired
	private BackendService service;

	@Id("grid")
	private Grid<Employee> employees;

	private TextField firstname = new TextField();
	private TextField lastname = new TextField();
	private TextField email = new TextField();
	private PasswordField password = new PasswordField();

	private Button cancel = new Button("Cancel");
	private Button save = new Button("Save");

	private Binder<Employee> binder;

	public MasterDetailView() {
		setId("master-detail-view");
		// Configure Grid
		employees = new Grid<>();
		employees.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		employees.setHeightFull();
		employees.addColumn(Employee::getFirstname).setHeader("First name");
		employees.addColumn(Employee::getLastname).setHeader("Last name");
		employees.addColumn(Employee::getEmail).setHeader("Email");

		// when a row is selected or deselected, populate form
		employees.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));

		// Configure Form
		binder = new Binder<>(Employee.class);

		// Bind fields. This where you'd define e.g. validation rules
		binder.bindInstanceFields(this);
		// note that password field isn't bound since that property doesn't exist in
		// Employee

		// the grid valueChangeEvent will clear the form too
		cancel.addClickListener(e -> employees.asSingleSelect().clear());

		save.addClickListener(e -> {
			Notification.show("Not implemented");
		});

		SplitLayout splitLayout = new SplitLayout();
		splitLayout.setSizeFull();

		createGridLayout(splitLayout);
		createEditorLayout(splitLayout);

		// add(splitLayout);

	}

	private void createEditorLayout(SplitLayout splitLayout) {
		Div editorDiv = new Div();
		editorDiv.setId("editor-layout");
		FormLayout formLayout = new FormLayout();
		addFormItem(editorDiv, formLayout, firstname, "First name");
		addFormItem(editorDiv, formLayout, lastname, "Last name");
		addFormItem(editorDiv, formLayout, email, "Email");
		addFormItem(editorDiv, formLayout, password, "Password");
		createButtonLayout(editorDiv);
		splitLayout.addToSecondary(editorDiv);
	}

	private void createButtonLayout(Div editorDiv) {
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setId("button-layout");
		buttonLayout.setWidthFull();
		buttonLayout.setSpacing(true);
		cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		buttonLayout.add(cancel, save);
		editorDiv.add(buttonLayout);
	}

	private void createGridLayout(SplitLayout splitLayout) {
		Div wrapper = new Div();
		wrapper.setId("wrapper");
		wrapper.setWidthFull();
		splitLayout.addToPrimary(wrapper);
		wrapper.add(employees);
	}

	private void addFormItem(Div wrapper, FormLayout formLayout, AbstractField field, String fieldName) {
		formLayout.addFormItem(field, fieldName);
		wrapper.add(formLayout);
		field.getElement().getClassList().add("full-width");
	}

	private void populateForm(Employee value) {
		// Value can be null as well, that clears the form
		binder.readBean(value);

		// The password field isn't bound through the binder, so handle that
		password.setValue("");
	}

}
