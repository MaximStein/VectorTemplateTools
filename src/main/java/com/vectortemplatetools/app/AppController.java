package com.vectortemplatetools.app;

import com.vectortemplatetools.vectorTemplates.SVGTemplate;

import com.vectortemplatetools.vectorTemplates.CalendarTemplate;
import com.vectortemplatetools.vectorTemplates.LabelPendantTemplate;
import com.vectortemplatetools.vectorTemplates.SVGTemplate;
import com.vectortemplatetools.vectorTemplates.SVGTemplate.DocumentChangedHandler;
import com.vectortemplatetools.vectorTemplates.SVGTemplateVariation;
import com.vectortemplatetools.vectorTemplates.SteelPendantTemplate;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class AppController {

	public static AppController instance;
	
	
	@FXML
	ChoiceBox<SVGTemplate> templateChoiceBox;

	@FXML
	ChoiceBox<SVGTemplateVariation> variationChoiceBox;
	
	@FXML
	ChoiceBox<LaserSetting> laserSettingChoiceBox;
	
	@FXML 
	VBox controlsContainer;
	
	@FXML
	WebView svgWebView;

	@FXML
	HBox outerContainer;
	
	@FXML
	public 
	VBox templateConfigContainer;

	@FXML
	Button saveToClipboardButton;
	
	
	private SVGTemplate selectedTemplate;
	
	public AppController() {
		instance = this;
		//var bounds = Utils.getRootGraphicsNode(Utils.getDocumentFromFile("icons.svg"));
	}

	public void initialize() {



		DocumentChangedHandler dch = (content) -> {
			System.out.println(content);
			loadWebViewContent(content);
		};

		var calendarTemplate = new CalendarTemplate(dch);
		var labelPendantTemplate = new LabelPendantTemplate(dch);
		var steelPendant = new SteelPendantTemplate(dch);

		var templates = new SVGTemplate[] {
			steelPendant,
			labelPendantTemplate,
			calendarTemplate,
		};

		templateChoiceBox.setItems(FXCollections.observableArrayList(templates));

		templateChoiceBox.valueProperty().addListener((a,oldVal,newVal) -> {
			selectedTemplate = newVal;
			templateConfigContainer.getChildren().clear();
			newVal.select(templateConfigContainer);
			variationChoiceBox.getItems().clear();

			if(newVal.getVariations() == null || newVal.getVariations().size() == 0) {
				variationChoiceBox.setDisable(true);
			}
			else {
				variationChoiceBox.setDisable(false);
				variationChoiceBox.getItems().addAll(newVal.getVariations());
			}

			loadWebViewContent(newVal.getDocumentContent(true));
		});

		variationChoiceBox.valueProperty().addListener((a,b,c) -> {
			if(selectedTemplate != null) {
				selectedTemplate.applyVariation(c);
			}
		});

		laserSettingChoiceBox.valueProperty().addListener((a,old,newVal) -> {
			if(selectedTemplate != null)
				selectedTemplate.assignLaserSetting(newVal);
		});

		laserSettingChoiceBox.getItems().addAll(LaserSetting.getAllSettings());

		saveToClipboardButton.setOnAction((event) -> {
			ClipboardContent content = new ClipboardContent();
			content.putString(getSelectedTemplate().getDocumentContent(true));
			javafx.scene.input.Clipboard.getSystemClipboard().setContent(content);
		});

		templateChoiceBox.getSelectionModel().select(0);
		laserSettingChoiceBox.getSelectionModel().select(0);
		variationChoiceBox.getSelectionModel().select(0);
		controlsContainer.getChildren().add(UIUtils.getBackgroundColorSelection());

	}
	
	
	
	public SVGTemplate getSelectedTemplate() {
		return templateChoiceBox.getValue();
	}
	

	private void loadWebViewContent(String content) {
		
		svgWebView.getEngine().loadContent(content);
		
	}
}
