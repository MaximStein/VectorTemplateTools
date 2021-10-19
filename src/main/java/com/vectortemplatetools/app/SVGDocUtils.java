package com.vectortemplatetools.app;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import org.xml.sax.SAXException;

import javafx.scene.shape.Rectangle;

public class SVGDocUtils {

	public static String getHexString(Color c) {
		if (c == null)
			return "-";
		return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
	}

	public static String setColorStyleAttribute(String style, String attr, Color val) {
		return SVGDocUtils.setStyleAttribute(style, attr, getHexString(val));
	}

	public static String setStyleAttribute(String style, String attr, String val) {
		var output = "";
		for (var a : style.split(";"))
			if (!a.startsWith(attr + ":"))
				output += a + ";";

		if (val.contains(" "))
			output += attr + ":'" + val + "'";
		else
			output += attr + ":" + val + "";

		return output;
	}

	public static void setAttribute(String key, String val, Element... els) {
		Stream.of(els).forEach(el -> {
			el.setAttribute(key, val);
		});
	}

	public static String getStyleAttribute(String style, String attr) {

		for (var a : style.split(";")) {
			if (a.startsWith(attr + ":")) {
				return a.split(attr + ":")[1];
			}
		}
		return null;
	}

	public static void clearChildren(Node n) {
		while (n.hasChildNodes())
			n.removeChild(n.getFirstChild());
	}

	public static void forEach(NodeList nodes, ElementOperation f) {
		for (int i = 0; i < nodes.getLength(); i++) {
			f.apply((Element)nodes.item(i));
		}
	}
	
	public interface ElementOperation {
		void apply(Element n);
	}

	public static Rectangle getRectangleFromNode(Element e) {

		var x = e.getAttribute("x");
		var y = e.getAttribute("y");
		var width = e.getAttribute("width");
		var height = e.getAttribute("height");

		if (x != null && width != null) {
			var rect = new Rectangle(Double.parseDouble(width), Double.parseDouble(height));
			rect.setX(Double.parseDouble(x));
			rect.setY(Double.parseDouble(y));

			return rect;
		}

		return null;

	}

	public static GraphicsNode getRootGraphicsNode(Document doc) {
		UserAgent agent = new UserAgentAdapter();
		DocumentLoader loader = new DocumentLoader(agent);
		BridgeContext context = new BridgeContext(agent, loader);
		context.setDynamic(true);
		GVTBuilder builder = new GVTBuilder();
		GraphicsNode root = builder.build(context, doc);

		return root;
	}

	public static Double getStyleAttributeDouble(String style, String attr) {
		var a = SVGDocUtils.getStyleAttribute(style, attr);

		if (a == null)
			return null;

		if (a.startsWith("'"))
			a = a.substring(1);

		if (a.endsWith("'"))
			a = a.substring(0, a.length() - 1);

		if (a.endsWith("px"))
			a = a.split("px")[0];

		return Double.parseDouble(a);
	}

	public static Map<String, String> getLaserConfigs() {
		var m = new HashMap<String, String>();
		m.put("#0000FF", "Acrylglas 3mm");
		return m;
	}

	public static Node convertTextToPath(Node textNode, Document document, Font font) {

		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		if (document == null)
			document = domImpl.createDocument("http://www.w3.org/2000/svg", "svg", null);

		var children = textNode.getChildNodes();

		var a = textNode.getAttributes();
		Element node;

		if (font == null)
			font = new Font("Arial", 0, 30);

		if (children.getLength() > 0) {
			node = document.createElement("g");

			if (a.getNamedItem("style") != null) {
				var style = a.getNamedItem("style");
				var parts = style.getNodeValue().split(";");
				for (var part : parts) {
					if (part.startsWith("font-size")) {
						var fs = SVGDocUtils.getStyleAttributeDouble(style.getNodeValue(), "font-size");

						font = font.deriveFont(fs.floatValue());
					} else if (part.startsWith("font-family")) {
						var val = part.split(":")[1];
						var fontFamily = val.contains("'") ? StringUtils.substringBetween(val, "'") : "Arial";
						Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
						attributes.put(TextAttribute.FAMILY, fontFamily);
						font = font.deriveFont(attributes);
					} else if (part.startsWith("letter-spacing")) {
						var strVal = part.split(":")[1].split("px")[0];
						if (strVal.startsWith("'"))
							strVal = strVal.substring(1);

						var val = Double.valueOf(strVal);
						Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
						var adjustedVal = val * 0.42;
						attributes.put(TextAttribute.TRACKING, adjustedVal);

						font = font.deriveFont(attributes);
					}
				}
			}

			for (int i = 0; i < textNode.getChildNodes().getLength(); i++) {
				var child = convertTextToPath(textNode.getChildNodes().item(i), document, font);

				if (child != null) {
					node.appendChild(child);
				}
			}
		} else {
			node = document.createElement("path");

			SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

			Font f = font;
			Canvas c = new Canvas();
			FontMetrics fm = c.getFontMetrics(f);
			GlyphVector v = f.createGlyphVector(fm.getFontRenderContext(), textNode.getTextContent());
			var shape = v.getOutline();
			var svg = svgGenerator.getShapeConverter().toSVG((shape));

			if (svg == null)
				return null;

			node.setAttribute("d", svg.getAttributes().getNamedItem("d").getNodeValue());
		}

		if (a == null)
			return node;

		var transform = a.getNamedItem("transform") == null ? null : a.getNamedItem("transform").getNodeValue();
		var vals = StringUtils.substringBetween(transform, "(", ")");
		var parts = vals == null ? null : vals.split(",");

		String newTransform = "";

		if (a.getNamedItem("x") != null && textNode.getNodeName() == "tspan") {
			newTransform += "translate(" + a.getNamedItem("x").getNodeValue() + "," + a.getNamedItem("y").getNodeValue()
					+ ")";
		}

		if (transform != null) {
			if (transform.startsWith("scale(")) {

				newTransform += parts.length == 2 ? "scale(" + parts[0] + "," + parts[1] + ")"
						: "scale(" + parts[0] + ")";
			} else if (transform.startsWith("translate(")) {
				newTransform += "translate(" + parts[0] + "," + parts[1] + ")";
			}
		}

		if (!newTransform.isEmpty())
			node.setAttribute("transform", newTransform);

		var styleNode = a.getNamedItem("style");
		if (styleNode == null)
			return null;

		var css = styleNode.getNodeValue();
		node.setAttribute("style", css);

		return node;

	}

	public static String nodeToString(Node n) {

		DOMSource domSource = new DOMSource(n);
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

	public static Document getDocumentFromFile(String uri) {
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);

		SVGDocument document = null;
		try {
			document = factory.createSVGDocument(uri);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return document;
	}


}
