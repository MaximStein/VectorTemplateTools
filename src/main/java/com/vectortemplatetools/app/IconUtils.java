package com.vectortemplatetools.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
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
			var files = Files.walk(Path.of(iconsFolder.toURI()))
					.map(f -> f.toString())
					.filter( f -> f.endsWith(".svg"))
					.map(f -> {
						var pattern = Pattern.quote(System.getProperty("file.separator"));
						var parts = f.split(pattern);
						return parts[parts.length-1];
					});

			return files.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
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
