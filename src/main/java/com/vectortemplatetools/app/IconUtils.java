package com.vectortemplatetools.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;

import com.vectortemplatetools.vectorTemplates.SVGTemplate;

public class IconUtils {


	public static Element getShape(String fileName) {
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		try {

			var svg = f.createDocument(null, SVGTemplate.class.getResourceAsStream("icons/"+fileName));

			var children = svg.getFirstChild().getChildNodes();

			for(int i = 0;i<children.getLength();i++) {
				var node = children.item(i);

				if(node.getNodeName() == "g" || node.getNodeName() == "path") {
					return (Element) node;
				}
			}
			System.err.println("no shapes found");
			return (Element) svg.getDocumentElement().getFirstChild();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	public static List<String> getShapeNames()  {
		var iconsFolder = SVGTemplate.class.getResource("icons");

		try {
			return AppUtils.getFiles(iconsFolder.toURI(), s -> s.endsWith(".svg"),true);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return null;
		
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
