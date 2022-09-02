package tfc.dynamicweaponry.loading;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Objects;

public class Material {
	public final ClientMaterial clientMaterial;
	public final ResourceLocation regName;
	
	protected TagKey<Item> tag;
	protected ResourceLocation item;
	
	protected double damage;
	
	public Material(ClientMaterial clientMaterial) {
		this.clientMaterial = clientMaterial;
		this.regName = clientMaterial.regName;
		tag = null;
		item = null;
	}
	
	public Material(ClientMaterial material, JsonObject object) {
		this.clientMaterial = material;
		// TODO: error reporting
		regName = new ResourceLocation(object.getAsJsonPrimitive("name").getAsString());
		
		String item = object.getAsJsonObject("item").getAsString();
		if (item.startsWith("#")) tag = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(item.substring(1)));
		else this.item = new ResourceLocation(item);
		
		damage = object.get("damage").getAsDouble();
	}
	
	public Material parse(String path, JsonObject object) {
		ClientMaterial clientMaterial = null;
		if (object.has("client")) clientMaterial = ClientMaterial.parse(path, object.getAsJsonObject("client"));
		return new Material(clientMaterial, object);
	}
	
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		if (clientMaterial != null) tag.put("client", clientMaterial.toTag());
		tag.putString("name", regName.toString());
		return tag;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Material material = (Material) o;
		return Objects.equals(regName, material.regName);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(regName);
	}
}
