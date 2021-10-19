package com.vectortemplatetools.app;

import java.awt.Color;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.stream.Stream;

public class LaserSetting {
	public String label;
	public Color fillColor;
	public Color cutColor;
	
	public LaserSetting(String label, Color fill, Color cut) {		
		this.fillColor = fill;
		this.cutColor = cut;
		this.label = label;		

	}
	
	
	public String toString() {
		var str = Stream.of(fillColor,cutColor).map(a -> SVGDocUtils.getHexString(a)).reduce((a,b)-> a+" | "+b).get();
		return label+" | "+str;
	}
	
	
	public static LaserSetting[] getAllSettings() {
		return new LaserSetting[] {
				ACRYL_3_MM,
				NUSSBAUM_4MM,
				EDELSTAHL,
				BIRKE_4MM
			};
	}
	
	
	public static final LaserSetting ACRYL_3_MM = new LaserSetting("Acryl 3mm", new Color(0xa0,0,0xa0),new Color(0,0,255));
	public static final LaserSetting NUSSBAUM_4MM = new LaserSetting("Nussbaum 4mm", new Color(0,0xe0,0xe0), new Color(255,255,0));
	public static final LaserSetting EDELSTAHL = new LaserSetting("Edelstahl", new Color(0,0xa0,0), new Color(255,255,0));
	public static final LaserSetting BIRKE_4MM = new LaserSetting("Birke 4mm",  new Color(0,0xe0,0xe0), new Color(0xd0,0xd0,0));
	
}
