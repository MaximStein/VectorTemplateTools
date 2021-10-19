package com.vectortemplatetools.vectorTemplates;

import java.util.Arrays;
import java.util.Collection;

import com.vectortemplatetools.app.UIUtils;
import org.w3c.dom.Element;

import com.vectortemplatetools.app.LaserSetting;
import com.vectortemplatetools.app.IconUtils;
import com.vectortemplatetools.app.UIUtils;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class LabelPendantTemplate extends SVGTemplate{

	public LabelPendantTemplate(DocumentChangedHandler dch) {
		super("labelPendant.svg", dch);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateControlsContainer(Pane controlsContainer) {
		
		var textField = new TextField();
		
		textField.textProperty().addListener((a,old,n) -> {			
			Element textNode = (Element) svg.getElementById("text").getFirstChild();
			textNode.setTextContent(n);
			docChanged();			
		});
		
		controlsContainer.getChildren().add(textField);
		
		var iconSelect = UIUtils.getIconsChoiceBox(this);
		
		var textNode = svg.getElementById("text");
		
		controlsContainer.getChildren().add(iconSelect);
		controlsContainer.getChildren().add(UIUtils.getFontsChoiceBox(this,"text"));
		controlsContainer.getChildren().addAll(UIUtils.getSpacingSlider(this, "text"), UIUtils.getFontSizeSlider(this,"text"));
	}

	@Override
	public double getIconScale() {
		return .6d;
	}
	
	
	@Override
	public String[] getFillIds() {		
		return new String[]{"text", "icon"};	
	}
	
	@Override
	public String[] getCutIds() {				
		return new String[]{"outerShape", "hole"};		
	}


	
	
}
