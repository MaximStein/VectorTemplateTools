package com.vectortemplatetools.vectorTemplates;

import com.vectortemplatetools.app.SVGDocUtils;
import com.vectortemplatetools.app.TextControls;
import com.vectortemplatetools.app.UIUtils;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.Collection;

public class SteelPendantTemplate extends SVGTemplate {

	public SteelPendantTemplate(DocumentChangedHandler dch) {
		super("steelPendant.svg", dch);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateControlsContainer(Pane controlsContainer) {		
		controlsContainer.getChildren().add(new TextControls(this, "text"));
		
		var iconSelect = UIUtils.getIconsChoiceBox(this);
		controlsContainer.getChildren().add(iconSelect);
		
	}
	
	@Override
	public Collection<SVGTemplateVariation> getVariations() {
		return Arrays.asList(
			new SVGTemplateVariation() {

				@Override
				public String toString() {
					return "40x9";
				}

				@Override
				public void applyVariation(SVGTemplate t) {
					var content = t.svg.getElementById("contentBox");
					var margin = t.svg.getElementById("marginBox");
					
					margin.setAttribute("height", "9");
					margin.setAttribute("width", "4");
					content.setAttribute("height", "9");
					content.setAttribute("width", "36");
					content.setAttribute("x","4");
				}
			
			},
			
			new SVGTemplateVariation() {

			@Override
			public String toString() {
				return "38x16";
			}

			@Override
			public void applyVariation(SVGTemplate t) {
				var content = t.svg.getElementById("contentBox");
				var margin = t.svg.getElementById("marginBox");

				SVGDocUtils.setAttribute("height", "16", content, margin);
				content.setAttribute("width", "33");
				margin.setAttribute("width", "5");
				content.setAttribute("x", "5");
			}
			
		});
	}

	@Override
	public double getIconScale() {
		return .6d;
	}
	
	
	@Override
	public String[] getFillIds() {		
		return new String[]{"text", "icon"};	
	}

	
}
