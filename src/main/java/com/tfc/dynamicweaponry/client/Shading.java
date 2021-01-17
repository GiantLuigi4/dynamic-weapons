package com.tfc.dynamicweaponry.client;

import com.tfc.assortedutils.utils.Color;
import com.tfc.dynamicweaponry.Config;
import com.tfc.dynamicweaponry.data.Loader;
import com.tfc.dynamicweaponry.data.Material;
import com.tfc.dynamicweaponry.item.tool.MaterialPoint;
import com.tfc.dynamicweaponry.item.tool.Tool;
import com.tfc.dynamicweaponry.item.tool.ToolComponent;

public class Shading {
	public static final float[] shades = new float[]{
//			0.25f,
//			0.5f,
//			0.6f,
//			0.78f,
//			0.8f,
//			0.8f,
//			0.74f,
//			0.7f,
//			0.6f,
//			0.25f
			1
	};
	
	public static Color shade(MaterialPoint point, Tool tool, ToolComponent from) {
		float[] shades;
		if (Config.CLIENT.useShading.get()) {
			shades = new float[]{
//				0.5f,0.6f,0.675f,0.7f,0.75f,0.5f,0.5f
					0.5f, 0.7f, 0.75f, 0.8f, 0.9f, 1f
			};
		} else {
			shades = new float[]{1f};
		}
		
		boolean isBorder = false;
		if (Config.CLIENT.useOutlines.get()) {
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if (Math.abs(x) != Math.abs(y)) {
						MaterialPoint point1 = tool.getPoint(point.x + x, point.y + y);
						if (point1 != null) continue;
						isBorder = true;
						break;
					}
				}
			}
		}
		
		float dist = 0;
		float dist1 = 0;
		
		int xOff = -16;
		int yOff = -7;
		
		if (Config.CLIENT.useShading.get()) {
			MaterialPoint point1 = new MaterialPoint(point.x + xOff, point.y + yOff, point.material);
			
			for (int i = 0; i < 16; i++) {
				MaterialPoint lerped = point.lerp(i / 16f, point1);
				MaterialPoint point2 = tool.getPoint(lerped.x, lerped.y);
				dist++;
				
				if (point2 == null) break;
			}
			
			point1 = new MaterialPoint(point.x - xOff, point.y - yOff, point.material);
			
			for (int i = 0; i < 16; i++) {
				MaterialPoint lerped = point.lerp(i / 16f, point1);
				MaterialPoint point2 = tool.getPoint(lerped.x, lerped.y);
				dist1++;
				
				if (point2 == null) break;
			}
		}

//		dist /= 15f;
//		dist *= shades.length - 1;

//		dist1 /= 15f;
//		dist1 *= shades.length - 1;
		
		Material material = Loader.INSTANCE.getMaterial(point.material);
		Color c = new Color(isBorder ? material.colorBorder : material.color);

//		return new Color(
//				(int) (c.getRed() * shades[(int) dist]),
//				(int) (c.getGreen() * shades[(int) dist]),
//				(int) (c.getBlue() * shades[(int) dist])
//		);

//		return c.darker(shades[(int) dist], 0f);
//		Color c1 = new Color(c.getHue(), c.getSaturation(), c.getValue(), false);

//		if (true) return c.darker(shades[(int) dist],0);

//		Color c1 = c.darker(shades[(int) dist], 0);
//		if (true) return c;
		
		float distMin = Math.min(dist, dist1);
		float distMax = Math.max(dist, dist1);
		
		Color c1 = c.darker(
				shades[(int) ((distMin / distMax) * (shades.length - 1))],
				-12f * shades[(int) ((distMin / distMax) * (shades.length - 1))]
		);
		
		return new Color(
				c1.getRed(),
				c1.getGreen(),
				c1.getBlue()
		);
	}
}
