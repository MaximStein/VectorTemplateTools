package com.vectortemplatetools.app;

import java.util.Arrays;

import com.vectortemplatetools.vectorTemplates.SVGTemplate;
import com.vectortemplatetools.vectorTemplates.SVGTemplateVariation;
import org.w3c.dom.Element;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class UIUtils {

	public static Node getFontsChoiceBox(SVGTemplate t, String... textElements) {

		var fonts = new String[] { "Arial", "Architects Daughter" };

		var fontSelect = new FlowPane();
		fontSelect.setHgap(5);
		fontSelect.setVgap(5);

		for (var font : fonts) {
			var button = new Button();
			button.setText(font);
			button.setStyle("-fx-font-family:'" + font + "'; -fx-font-size:100%");

			button.setOnAction(event -> {
				for (var el : textElements) {
					var text = t.svg.getElementById(el);
					var style = SVGDocUtils.setStyleAttribute(text.getAttribute("style"), "font-family", font);
					text.setAttribute("style", style);
					t.docChanged();
				}
			});

			fontSelect.getChildren().add(button);
		}

		var container = getWithLabel(fontSelect, "Font Family:");

		return container;
	}


	public static TextArea getTextArea(SVGTemplate t,  DoubleProperty lineSpacing, String textNodeId) {

		var textArea = new TextArea();
		textArea.setPrefHeight(50);
		
		textArea.textProperty().addListener((a,old,n) -> {			
			
			var text = t.svg.getElementById(textNodeId);
			
			if(text == null)
				return;
			
			var lines = n.split("\n");
			
			SVGDocUtils.clearChildren(text);
			final var y = new SimpleDoubleProperty(Double.parseDouble(text.getAttribute("y")));
			
			Arrays.stream(lines).forEach(line -> {
				var tspan = t.svg.createElement("tspan");
				tspan.setAttribute("x", text.getAttribute("x"));
				tspan.setAttribute("y", y.doubleValue() + "");
				tspan.setAttribute("style", text.getAttribute("style"));
				tspan.setTextContent(line);
				text.appendChild(tspan);
				
				y.set(y.doubleValue() + lineSpacing.doubleValue());
			});
			
			t.docChanged();			
		});
		
		
		return textArea;
	}

	
	public static Slider getLineHeightSlider(SVGTemplate t, String... els) {
		var slider = new Slider(2, 30, 12);
		
		var firstNode = t.svg.getElementById(els[0]);		
		var lineHeight = SVGDocUtils.getStyleAttributeDouble(firstNode.getAttribute("style"), "line-height");		
		if (lineHeight != null)
			slider.setValue(lineHeight);
		
		t.docChanged();

		slider.valueProperty().addListener((a, old, n) -> {
			for (var el : els) {
				var node = t.svg.getElementById(el);
				var style = SVGDocUtils.setStyleAttribute(node.getAttribute("style"), "line-height", n + "");
				
				node.setAttribute("style", style);

				final var y = new SimpleDoubleProperty(Double.parseDouble(node.getAttribute("y")));
				
				SVGDocUtils.forEach(node.getChildNodes(), (child) -> {
					child.setAttribute("y", y.doubleValue()+"");					
					y.set(y.doubleValue() + n.doubleValue());				
				});	
			}
			t.docChanged();
		});
		
		return slider;

	}
	
	public static Slider getFontSizeSlider(SVGTemplate t, String... els) {
		
		var slider = new Slider(2, 30, 12);

		for (var el : els) {
			var node = t.svg.getElementById(el);
			
			var fontSize = SVGDocUtils.getStyleAttributeDouble(node.getAttribute("style"), "font-size");

			if (fontSize == null)
				continue;

			slider.setValue(fontSize);

			t.docChanged();
		}

		slider.valueProperty().addListener((a, old, n) -> {
			for (var el : els) {
				var node = t.svg.getElementById(el);
				var style = SVGDocUtils.setStyleAttribute(node.getAttribute("style"), "font-size", n + "px");
				
				node.setAttribute("style", style);
				var fontSize = SVGDocUtils.getStyleAttribute(style, "font-size");
				var strokeWidth = SVGDocUtils.getStyleAttribute(style, "stroke-width");
				
				SVGDocUtils.forEach(node.getChildNodes(), (child) -> {
					
					child.setAttribute("style", "font-size:"+fontSize+";stroke-width:"+strokeWidth);
				});	
			}
			t.docChanged();
		});


		return slider;
	}

	public static Slider getSpacingSlider(SVGTemplate t, String... ids) {
		var slider = new Slider(-2.1, 3, 0);

		for (var id : ids) {
			var el = t.svg.getElementById(id);
			var val = SVGDocUtils.getStyleAttributeDouble(el.getAttribute("style"), "letter-spacing");

			if (val == null)
				continue;

			slider.setValue(val);
		}

		slider.valueProperty().addListener((a, old, n) -> {
			for (var id : ids) {
				var el = t.svg.getElementById(id);
				el.setAttribute("style", SVGDocUtils.setStyleAttribute(el.getAttribute("style"), "letter-spacing", n + "px"));
			}
			t.docChanged();
		});

		return slider;
	}

	public static Node getIconsChoiceBox(SVGTemplate t) {
		var iconSelect = new ChoiceBox<String>();

		iconSelect.getItems().add("-");
		iconSelect.getItems().addAll(IconUtils.getShapeNames());
		iconSelect.getSelectionModel().select(0);

		iconSelect.valueProperty().addListener((a, old, newVal) -> {
			var iconShape = IconUtils.getShape(newVal);
			Element placeholder = t.svg.getElementById("iconPlaceholder");
			var icon = t.svg.getElementById("icon");

			if (icon != null) {
				icon.getParentNode().removeChild(icon);
			}

			icon = t.svg.createElement("g");
			icon.setAttribute("id", "icon");

			if (placeholder != null) {
				placeholder.getParentNode().appendChild(icon);
				var scale = t.getIconScale() == 1d ? ""
						: " scale(" + t.getIconScale() + " " + t.getIconScale() + ")";

				var width = Double.parseDouble(placeholder.getAttribute("width"));
				var x = Double.parseDouble(placeholder.getAttribute("x"));

				var transform = "translate(" +(-x-width)  + " " + placeholder.getAttribute("y")
						+ ")";
				transform += scale;
				icon.setAttribute("transform", transform);
			}

			/*var parent = iconShape.getParentNode();

			while (parent.getChildNodes().getLength() > 1) {
				if (parent.getFirstChild() == iconShape)
					parent.removeChild(parent.getLastChild());
				else
					parent.removeChild(parent.getFirstChild());
			}*/

			iconShape = (Element) t.svg.importNode(iconShape, true);
			icon.appendChild(iconShape);

			t.docChanged();

		});

		iconSelect.getSelectionModel().select(1);
		var result = getWithLabel(iconSelect, "Icon:");

		return result;
	}

	public static VBox getWithLabel(Node n, String label) {
		var c = new VBox(new Label(label), n);
		c.setPadding(new javafx.geometry.Insets(0, 0, 20, 0));
		return c;
	}

	public static Node getBackgroundColorSelection() {

		var colors = new String[] { "white", "gray", "silver", "black" };

		var container = new FlowPane();

		Arrays.stream(colors).forEach((c) -> {
			Button b = new Button();
			b.setOnAction((e) -> {
				com.vectortemplatetools.vectorTemplates.SVGTemplate t = AppController.instance.getSelectedTemplate();

				if (t == null)
					return;

				var el = t.svg.getElementById("bg");

				if (el == null) {
					var r = t.svg.createElement("rect");
					r.setAttribute("x", "0");
					r.setAttribute("y", "0");
					r.setAttribute("width", "800");
					r.setAttribute("height", "900");
					r.setAttribute("id", "bg");

					var n = t.svg.getFirstChild();
					n.insertBefore(r, n.getFirstChild());
					el = r;
				}

				el.setAttribute("style", "fill:" + c + ";");

				t.docChanged();
			});
			b.setPrefWidth(30);
			b.setPrefHeight(30);
			b.setStyle("-fx-background-color:" + c + ";fx-border-width:0");

			container.getChildren().add(b);
		});

		return container;
	}
}
