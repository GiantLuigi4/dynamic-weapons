package com.tfc.dynamicweaponry.client;

import com.tfc.assortedutils.utils.Color;
import com.tfc.dynamicweaponry.data.Loader;
import com.tfc.dynamicweaponry.data.Material;
import com.tfc.dynamicweaponry.tool.MaterialPoint;
import com.tfc.dynamicweaponry.tool.Tool;
import com.tfc.dynamicweaponry.tool.ToolComponent;

import java.util.ArrayList;

public class Shading {
	public static final float[] shades = new float[]{
			0.25f,
			0.5f,
			0.6f,
			0.78f,
			0.8f,
			0.8f,
			0.74f,
			0.7f,
			0.6f,
			0.25f
	};
	
	public static Color shade(MaterialPoint point, Tool tool, ToolComponent from) {
		ArrayList<ToolComponent> components = new ArrayList<>();
		for (ToolComponent component : tool.components) {
			if (
					from.type != null &&
							component.type != null &&
							from.type.renderLayer == component.type.renderLayer
			)
				components.add(component);
		}
		float dist = 0;
		for (int i = 0; i < 16; i++) {
			boolean finishedTrace = false;
			for (ToolComponent component : components) {
				MaterialPoint lerped = point.lerp(
						i / 15f,
						new MaterialPoint(
								point.x - 8,
								point.y + 5,
								null
						)
				);
				MaterialPoint lerped1 = point.lerp(
						i / 15f,
						new MaterialPoint(
								point.x + 8,
								point.y - 3,
								null
						)
				);
				MaterialPoint lerped2 = point.lerp(
						i / 15f,
						new MaterialPoint(
								point.x + 8,
								point.y + 3,
								null
						)
				);
				MaterialPoint lerped3 = point.lerp(
						i / 15f,
						new MaterialPoint(
								point.x - 8,
								point.y - 3,
								null
						)
				);
				dist = i;
				if (
						component.getPoint(lerped.x, lerped.y) == null ||
								lerped.x < 0 ||
								lerped.x > 15 ||
								lerped.y < 0 ||
								lerped.y > 15 ||
								
								component.getPoint(lerped1.x, lerped1.y) == null ||
								lerped1.x < 0 ||
								lerped1.x > 15 ||
								lerped1.y < 0 ||
								lerped1.y > 15 ||
								
								component.getPoint(lerped2.x, lerped2.y) == null ||
								lerped2.x < 0 ||
								lerped2.x > 15 ||
								lerped2.y < 0 ||
								lerped2.y > 15 ||
								
								component.getPoint(lerped3.x, lerped3.y) == null ||
								lerped3.x < 0 ||
								lerped3.x > 15 ||
								lerped3.y < 0 ||
								lerped3.y > 15
				) {
					finishedTrace = true;
					break;
				}
			}
			if (finishedTrace) break;
		}
		dist /= 15f;
		dist *= shades.length - 1;
		Material material = Loader.INSTANCE.getMaterial(point.material);
		Color c = new Color(material.color);
		return new Color(
				(int) (c.getRed() * shades[(int) dist]),
				(int) (c.getGreen() * shades[(int) dist]),
				(int) (c.getBlue() * shades[(int) dist])
		);
	}
}
