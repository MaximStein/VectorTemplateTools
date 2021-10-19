package com.vectortemplatetools.vectorTemplates;

import java.util.Collection;

import com.vectortemplatetools.app.LaserSetting;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

public class CalendarTemplate extends SVGTemplate {
	
	public CalendarTemplate(DocumentChangedHandler dch) {
		super("calendar.svg", dch);
		// TODO Auto-generated constructor stub
	}
		
		String[] jahre = {
			"Januar","Februar","M�rz",
			"April","Mai","Juni",
			"Juli","August","September",
			"Oktober","November","Dezember",
		};

		@Override
		public void updateControlsContainer(Pane controlsContainer) {
				
				var datePicker = new DatePicker();
				datePicker.valueProperty().addListener((a, old, newVal)-> {
					var day = newVal.getDayOfMonth();
					
					var herz = svg.getElementById("herz");
					var colIndex = (day-7) % 5;
					var tx = day < 7 ? 4.6d * (day-1) : 5.65d * colIndex;
					var ty = day < 7 ? 0 : ((day-7) / 5 + 1) * 4.55d;
					
					herz.setAttribute("transform", "translate("+tx+", "+ty+")");
					
					var monatJahr = svg.getElementById("monat-jahr");
					if(monatJahr != null)
						monatJahr.getFirstChild().setTextContent(jahre[newVal.getMonthValue()-1] + " "+newVal.getYear());
					
					docChanged();
				});
				
				controlsContainer.getChildren().add(datePicker);
				
				var nameInput = new TextArea();
				var textNode = svg.getElementById("nameText");
				nameInput.textProperty().addListener((a,o,n) -> {
					
					var lines = n.split("\n");
					textNode.getFirstChild().setTextContent(lines[0]);
					
					if(lines.length>1)
						textNode.getChildNodes().item(1).setTextContent(lines[1]);
					
					docChanged();
				});
				
				controlsContainer.getChildren().add(nameInput);
			
		}

	
	}

