
package com.vectortemplatetools.vectorTemplates;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.vectortemplatetools.app.LaserSetting;
import com.vectortemplatetools.app.SVGDocUtils;
import javafx.scene.layout.Pane;

public abstract class SVGTemplate {
	public String fileName;
	public Document svg;
	public SVGTemplateVariation selectedVariation;
	public DocumentChangedHandler docChangedHandler = null;

	protected Pane controlsContainer;
	
	public interface DocumentChangedHandler {
		void documentChanged(String newContent);
	}
	
	public SVGTemplate(String fileName, DocumentChangedHandler dch) {
		try {
			this.docChangedHandler = dch;
			processSVGFile(fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	protected abstract void updateControlsContainer(Pane controlsContainer);
	
	public void applyVariation(SVGTemplateVariation v) {
		v.applyVariation(this);
		selectedVariation = v;
		this.docChanged();
	}
	
	public void select(Pane controlsContainer) {
		updateControlsContainer(controlsContainer);
		
	}

	private void processSVGFile(String fileName) throws FileNotFoundException, IOException {
		this.fileName = fileName;

		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		svg = f.createDocument(null,SVGTemplate.class.getResourceAsStream("templates/"+fileName));

	}

	public void docChanged() {
		if (docChangedHandler != null)
			docChangedHandler.documentChanged(getDocumentContent(true));
	}

	public String toString() {
		return this.fileName;
	}



	private Document getPathConvertedSVG() {
		var clone = (Document) svg.cloneNode(true);
		var textNodes = clone.getElementsByTagName("text");

		for (int i = 0; i < textNodes.getLength(); i++) {
			var node = textNodes.item(i);
			var convertedNode = SVGDocUtils.convertTextToPath(node, clone, null);

			if (convertedNode != null) {				
				node.getParentNode().appendChild(convertedNode);
				node.getParentNode().removeChild(node);
				i--;
			}
		}

		return clone;
	}

	public String getDocumentContent(boolean convertTextToPaths) {
		if (convertTextToPaths) {
			// System.out.println(getDocumentContent(getPathConvertedSVG()));
			return getDocumentContent(getPathConvertedSVG());

		} else {
			return getDocumentContent(svg);
		}
	}

	public String getDocumentContent() {
		return this.getDocumentContent(svg);
	}

	public String getDocumentContent(Node doc) {
		DOMSource domSource = new DOMSource(doc);
		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();

			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);

			return sw.toString();
		} catch (TransformerException | TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	
	public void assignLaserSetting(LaserSetting l) {
		if(this.getCutIds() != null)
		for(var elementId : this.getCutIds()) {
			assignColorToElement(elementId, l.cutColor, false);
		}
		
		if(this.getFillIds() != null)
		for(var elementId : this.getFillIds()) {
			assignColorToElement(elementId, l.fillColor, true);
		}
	}
	
	public String[] getFillIds() {		
		return new String[] {};	
	}
	
	
	public String[] getCutIds() {				
		return new String[] {};		
	}
	
	private void assignColorToElement(String elementId, Color c, boolean fill) {
		if(c== null)
			return;
		var el = svg.getElementById(elementId);
		
		if(el == null)
			return;
		
		var style = el.getAttribute("style");								
		
		if(fill) {
			style = SVGDocUtils.setStyleAttribute(style,"stroke", "none");
			style = SVGDocUtils.setColorStyleAttribute(style, "fill", c);						
		} 
		else {
			style = SVGDocUtils.setStyleAttribute(style,"fill", "none");
			style = SVGDocUtils.setColorStyleAttribute(style, "stroke", c);
		}
		

		el.setAttribute("style", style);
		docChanged();
	}

	public Collection<SVGTemplateVariation> getVariations() {
		return null;
	}
		
	public double getIconScale() {	return 1;	}

	
	
}