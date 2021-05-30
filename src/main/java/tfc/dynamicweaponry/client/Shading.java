package tfc.dynamicweaponry.client;

import com.tfc.assortedutils.utils.Color;
import tfc.dynamicweaponry.Config;
import tfc.dynamicweaponry.data.DataLoader;
import tfc.dynamicweaponry.data.Material;
import tfc.dynamicweaponry.item.tool.MaterialPoint;
import tfc.dynamicweaponry.item.tool.Tool;
import tfc.dynamicweaponry.item.tool.ToolComponent;

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
		
		Material material = DataLoader.INSTANCE.getMaterial(point.material);
		ClientMaterialInfo materialInfo = AssetLoader.INSTANCE.getMaterial(material.item);
		Color c = new Color(isBorder ? materialInfo.colorBorder : materialInfo.color);

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
		
		if (Config.CLIENT.useMaterialPatterns.get() && materialInfo.pattern != null) {
			int sizeLeft = 0;
			int sizeRight = 0;
			boolean hitLeft = false;
			boolean hitRight = false;
			
			int sizeTop = 0;
			int sizeBottom = 0;
			boolean hitTop = false;
			boolean hitBottom = false;
			
			for (int index = 0; index < 16; index++) {
				if (!hitLeft) {
					MaterialPoint point1 = tool.getPoint(point.x - index, point.y);
					if (
							point1 == null/* ||
									(false && !point1.material.equals(point.material))*/
					) hitLeft = true;
					else sizeLeft++;
				}
				if (!hitRight) {
					MaterialPoint point1 = tool.getPoint(point.x + index, point.y);
					if (
							point1 == null/* ||
									(false && !point1.material.equals(point.material))*/
					) hitRight = true;
					else sizeRight++;
				}
				if (!hitTop) {
					MaterialPoint point1 = tool.getPoint(point.x, point.y + index);
					if (
							point1 == null/* ||
									(false && !point1.material.equals(point.material))*/
					) hitTop = true;
					else sizeTop++;
				}
				if (!hitBottom) {
					MaterialPoint point1 = tool.getPoint(point.x, point.y - index);
					if (
							point1 == null/* ||
									(false && !point1.material.equals(point.material))*/
					) hitBottom = true;
					else sizeBottom++;
				}
				if (
						hitLeft &&
								hitRight &&
								hitTop &&
								hitBottom
				) break;
			}
			
			int[][] pattern = materialInfo.getPattern();
			
			try {
				int totalSizeY = sizeTop + sizeBottom;
//				float patProgressY = sizeTop / (float) totalSizeY;
//				float entryY = (int) (patProgressY * (pattern.length));
//				if (entryY < pattern.length/2f) entryY = (float)Math.floor(entryY);
//				else entryY = (float)Math.ceil(entryY);
				float entryY = (sizeTop / (float) totalSizeY) * pattern.length;
				if (sizeTop == 1) entryY = 0;
				if (sizeBottom == 1) entryY = pattern.length - 1;
				int[] row = pattern[((int) (entryY))];
				
				int totalSizeX = sizeLeft + sizeRight;
//				float patProgressX = sizeLeft / (float) totalSizeX;
//				float entryX = (patProgressX * (row.length));
//				if (entryX <= row.length/2f) entryX = (float)Math.floor(entryX-1);
//				else entryX = (float)Math.ceil(entryX-1);
				float entryX = (sizeLeft / (float) totalSizeX) * row.length;
				if (sizeLeft == 1) entryX = 0;
				if (sizeRight == 1) entryX = row.length - 1;
				int col = row[((int) (entryX))];

//			isBorder ? material.colorBorder : material.color
				c = new Color(col);
			} catch (Throwable err) {
				err.printStackTrace();
			}
		}
		Color c1;
		if (Config.CLIENT.useMaterialPatterns.get() && materialInfo.getPattern() != null) {
			c1 = c.darker(
					Config.CLIENT.shadedPatterns.get() ? shades[(int) ((distMin / distMax) * (shades.length - 1))] : 1,
//					-12f * shades[(int) ((distMin / distMax) * (shades.length - 1))]
					0
			);
		} else {
			c1 = c.darker(
					shades[(int) ((distMin / distMax) * (shades.length - 1))],
					-12f * shades[(int) ((distMin / distMax) * (shades.length - 1))]
			);
		}
		
		return new Color(
				c1.getRed(),
				c1.getGreen(),
				c1.getBlue()
		);
	}
}
