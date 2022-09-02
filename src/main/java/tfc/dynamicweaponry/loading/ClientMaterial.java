package tfc.dynamicweaponry.loading;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class ClientMaterial {
	public final int color;
	public final int highlightColor;
	public final float shininess;
	public final ResourceLocation regName;
	
	public ClientMaterial(int color, int highlightColor, float shininess, ResourceLocation regName) {
		this.color = color;
		this.highlightColor = highlightColor;
		this.shininess = shininess;
		this.regName = regName;
	}
	
	public static ClientMaterial parse(String path, JsonObject object) {
		int step = 0;
		try {
			ResourceLocation r = new ResourceLocation(object.getAsJsonPrimitive("name").getAsString());
			path = r + " in " + path;
			step = 1;
			int c = object.getAsJsonPrimitive("color").getAsInt();
			step = 2;
			int h = object.getAsJsonPrimitive("highlight").getAsInt();
			step = 3;
			float s = object.getAsJsonPrimitive("shininess").getAsFloat();
			// trust me, it is used
			// noinspection UnusedAssignment
			step = 4;
			return new ClientMaterial(c, h, s, r);
		} catch (NullPointerException err) {
			boolean unknown = false;
			NullPointerException ex = new NullPointerException("Cannot create material because " + path + " is missing a" + switch (step) {
				case 0 -> " string definition for name";
				case 1 -> "n int definition for color";
				case 2 -> "n int definition for highlight";
				case 3 -> " float definition for shininess";
				default -> "" + (unknown = true);
			});
			if (unknown) {
				err.printStackTrace();
			} else {
				ex.setStackTrace(err.getStackTrace());
				ex.printStackTrace();
			}
			return null;
		} catch (Throwable err) {
			err.printStackTrace();
			return null;
		}
	}
	
	public static ClientMaterial fromTag(CompoundTag client) {
		return new ClientMaterial(
				client.getInt("color"),
				client.getInt("highlight"),
				client.getFloat("shininess"),
				new ResourceLocation(client.getString("name"))
		);
	}
	
	public Tag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("color", color);
		tag.putInt("highlight", highlightColor);
		tag.putFloat("shininess", shininess);
		tag.putString("name", regName.toString());
		return tag;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ClientMaterial material = (ClientMaterial) o;
		return color == material.color && highlightColor == material.highlightColor && Float.compare(material.shininess, shininess) == 0 && Objects.equals(regName, material.regName);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(color, highlightColor, shininess, regName);
	}
}
