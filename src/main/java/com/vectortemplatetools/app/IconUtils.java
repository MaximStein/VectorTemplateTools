package com.vectortemplatetools.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vectortemplatetools.vectorTemplates.SVGTemplate;
import com.vectortemplatetools.vectorTemplates.SVGTemplate.DocumentChangedHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;

public class IconUtils {

	public static Element getShape(String fileName) {
		return getShape(fileName, null); 
	}
	
	public static Document getIconsDocument() {
		return SVGDocUtils.getDocumentFromFile("graphics/icons.svg");
	}
	
	public static Element getShape(String fileName, String shapeName) {
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		try {
			var svg = f.createDocument(null, new FileInputStream("graphics/"+fileName));
			
			if(shapeName != null)
				return svg.getElementById(shapeName);
			else {
			
				var children = svg.getFirstChild().getChildNodes();
				
				for(int i = 0;i<children.getLength();i++) {
					var node = children.item(i);
					
					if(node.getNodeName() == "g" || node.getNodeName() == "path") {
						return (Element) node;
					}
				}
				
				return (Element) svg.getDocumentElement().getFirstChild();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static HashMap<String, Element> getAllShapes() {
		var map = new HashMap<String, Element>();
		for(String str:getShapeNames()) {
			map.put(str, getShape(str));
		}
		
		return map;
	}
	 
	public static String[] getShapeNames() {
		
		 return new String[] {
			"pfote.svg",
			"herz.svg"
		}; 
		
	/*	return new String[] {
			"Telefon",
			"Pfote",
			"Herz",
			"Schluessel",
			"Medizin",
			"Medizin_2",
			"Musik",
			"Whatsapp",
		}; */
	}

}
