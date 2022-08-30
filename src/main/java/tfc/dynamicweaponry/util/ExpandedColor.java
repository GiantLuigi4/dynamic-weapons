package tfc.dynamicweaponry.util;

import net.minecraft.util.Mth;

import java.awt.*;
import java.util.Objects;

public class ExpandedColor {
	private int value;
	
	public ExpandedColor(int rgb) {
		value = 0xff000000 | rgb;
	}
	
	public ExpandedColor(int rgb, boolean hasAlpha) {
		if (hasAlpha) {
			value = rgb;
		} else {
			value = 0xff000000 | rgb;
		}
	}
	
	public ExpandedColor(int r, int g, int b) {
		this(r, g, b, 255);
	}
	
	public ExpandedColor(int r, int g, int b, int a) {
		value = ((a & 0xFF) << 24) |
				((r & 0xFF) << 16) |
				((g & 0xFF) << 8) |
				((b & 0xFF) << 0);
	}
	
	public int getRed() {
		return (getRGB() >> 16) & 0xFF;
	}
	
	public int getGreen() {
		return (getRGB() >> 8) & 0xFF;
	}
	
	public int getBlue() {
		return (getRGB()) & 0xFF;
	}
	
	public int getRGB() {
		return value;
	}
	
	public int getAlpha() {
		return (getRGB() >> 24) & 0xff;
	}
	
	public ExpandedColor(float v, float v1, float v2, boolean RGB) {
		this(v, v1, v2, 255, RGB);
	}
	
	public ExpandedColor(float v, float v1, float v2, int alpha, boolean RGB) {
		if (RGB) {
			int r = (int) (v * 255);
			int g = (int) (v1 * 255);
			int b = (int) (v2 * 255);
			value = ((alpha & 0xFF) << 24) |
					((r & 0xFF) << 16) |
					((g & 0xFF) << 8) |
					((b & 0xFF));
		} else {
			ExpandedColor c = fromHSV(v, v1, v2);
			ExpandedColor c1 = new ExpandedColor(c.getRed(), c.getGreen(), c.getBlue(), alpha);
			value = c1.value;
		}
	}
	
	/*(thanks lorenzo): https://www.rapidtables.com/convert/color/rgb-to-hsv.html*/
	public static float[] toHSV(ExpandedColor color) {
		float r = color.getRed() / 255f;
		float g = color.getGreen() / 255f;
		float b = color.getBlue() / 255f;
		
		float colorMax = Math.max(r, Math.max(b, g));
		float colorMin = Math.min(r, Math.min(b, g));
		
		float delta = colorMax - colorMin;
		
		float hue = 0;
		if (delta == 0) ;
		else if (colorMax == r) hue = 60 * (((g - b) / delta) % 6);
		else if (colorMax == g) hue = 60 * (((b - r) / delta) + 2);
		else if (colorMax == b) hue = 60 * (((r - g) / delta) + 4);
		
		float saturation = 0;
		if (colorMax != 0) saturation = delta / colorMax;
		
		if (hue < 0) hue = 360 + hue;
		
		return new float[]{hue, saturation, colorMax};
	}
	
	public float getHue() {
		return toHSV(this)[0];
	}
	
	public float getSaturation() {
		return toHSV(this)[1];
	}
	
	public float getValue() {
		return toHSV(this)[2];
	}
	
	public ExpandedColor darker(float amt, float hueShiftScale) {
		ExpandedColor darker = new ExpandedColor(Math.max((int) (getRed() * amt), 0),
				Math.max((int) (getGreen() * amt), 0),
				Math.max((int) (getBlue() * amt), 0),
				getAlpha());
		if (hueShiftScale == 0) {
			return darker;
		} else {
			return new ExpandedColor(
					darker.getHue() - (hueShiftScale * (1 - amt)),
					darker.getSaturation(),
					darker.getValue(),
					false
			);
		}
	}
	
	public ExpandedColor brighter(float amt, float hueShiftScale) {
		int r = getRed();
		int g = getGreen();
		int b = getBlue();
		int alpha = getAlpha();
		
		r = (int) Mth.lerp(amt, r, 255);
		g = (int) Mth.lerp(amt, g, 255);
		b = (int) Mth.lerp(amt, b, 255);
		
		ExpandedColor brighter = new ExpandedColor(r, g, b, alpha);
		
//		ExpandedColor brighter = null;
//
//		/* From 2D group:
//		 * 1. black.brighter() should return grey
//		 * 2. applying brighter to blue will always return blue, brighter
//		 * 3. non pure color (non zero rgb) will eventually return white
//		 */
//		int i = (int) (1.0 / (1.0 - amt));
//		if (r == 0 && g == 0 && b == 0) {
//			brighter = new ExpandedColor(i, i, i, alpha);
//		}
//
//		if (brighter == null) {
//			if (r > 0 && r < i) r = i;
//			if (g > 0 && g < i) g = i;
//			if (b > 0 && b < i) b = i;
//
//			brighter = new ExpandedColor(
//					Math.min((int) (r / amt), 255),
//					Math.min((int) (g / amt), 255),
//					Math.min((int) (b / amt), 255),
//					alpha
//			);
//		}
		
		if (hueShiftScale == 0) {
			return brighter;
		} else {
			return new ExpandedColor(
					brighter.getHue() + (hueShiftScale * amt),
					brighter.getSaturation(),
					brighter.getValue(),
					false
			);
		}
	}
	
	public ExpandedColor hueshift(float amt) {
		return new ExpandedColor(
				this.getHue() + amt,
				this.getSaturation(),
				this.getValue(),
				false
		);
	}
	
	/*(thanks lorenzo): https://www.rapidtables.com/convert/color/hsv-to-rgb.html*/
	public ExpandedColor fromHSV(float hue, float saturation, float value) {
		hue %= 360;
		if (hue < 0) hue += 360;
		
		float c = value * saturation;
		float x = c * (1 - Math.abs((hue / 60) % 2 - 1));
		
		float m = value - c;
		
		float rPrime = 0;
		float gPrime = 0;
		float bPrime = 0;
		
		float num = (hue / 60f);
		if ((0 <= num) && (num < 2)) {
			rPrime = num < 1 ? c : x; // thanks to andrew0030 for finding an issue with this line
			gPrime = num < 1 ? x : c;
		} else if ((2 <= num) && (num < 4)) {
			gPrime = num < 3 ? c : x;
			bPrime = num < 3 ? x : c;
		} else if ((4 <= num) && (num < 6)) {
			rPrime = num < 5 ? x : c;
			bPrime = num < 5 ? c : x;
		}
		
		float r = (rPrime + m) * 255;
		float g = (gPrime + m) * 255;
		float b = (bPrime + m) * 255;
		
		return new ExpandedColor((int) r, (int) g, (int) b);
	}
	
	@Override
	public String toString() {
		return "Color{" +
				"r:" + getRed() + ',' +
				"g:" + getGreen() + ',' +
				"b:" + getBlue() + ',' +
				"a:" + getAlpha() +
				'}';
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ExpandedColor color = (ExpandedColor) o;
		return value == color.value;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}