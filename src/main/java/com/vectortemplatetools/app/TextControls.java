package com.vectortemplatetools.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vectortemplatetools.vectorTemplates.SVGTemplate;
import org.w3c.dom.Node;

import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;



public class TextControls extends VBox {
	
	
	
	private List<Node> _textNodes = new ArrayList<Node>();

	public Slider lineHeightSlider, fontSizeSlider, spacingSlider;
	
	public TextArea textArea;
	
	public TextControls(SVGTemplate t, String... textNodeIds) {
		Arrays.stream(textNodeIds).forEach(id -> {
			_textNodes.add(t.svg.getElementById(id));
		});
		
		this.lineHeightSlider = UIUtils.getLineHeightSlider(t, textNodeIds);
		this.fontSizeSlider = UIUtils.getFontSizeSlider(t, textNodeIds);
		this.spacingSlider = UIUtils.getSpacingSlider(t, textNodeIds);

		this.getChildren().add(UIUtils.getTextArea(t, lineHeightSlider.valueProperty(), textNodeIds[0]));
		this.getChildren().add(UIUtils.getWithLabel(lineHeightSlider, "line-height"));		
		this.getChildren().add(UIUtils.getWithLabel(fontSizeSlider, "font-size"));		
		this.getChildren().add(UIUtils.getWithLabel(spacingSlider, "spacing"));				
		this.getChildren().add(UIUtils.getFontsChoiceBox(t,"text"));
	}
	
	
}
