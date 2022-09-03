package tfc.dynamicweaponry.loading;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class Material {
	public final ClientMaterial clientMaterial;
	public final ResourceLocation regName;
	
	protected TagKey<Item> tag;
	protected ResourceLocation item;
	
	protected double damage;
	
	protected String layer;
	
	public TagKey<Item> getTag() {
		return tag;
	}
	
	public ResourceLocation getItem() {
		return item;
	}
	
	public double getDamage() {
		return damage;
	}
	
	public String getLayer() {
		return layer;
	}
	
	public Material(ClientMaterial clientMaterial) {
		this.clientMaterial = clientMaterial;
		this.regName = clientMaterial.regName;
		tag = null;
		item = null;
	}
	
	public Material(ClientMaterial material, ResourceLocation regName) {
		this.clientMaterial = material;
		this.regName = regName;
	}
	
	public Material(ClientMaterial material, Material parent) {
		this.clientMaterial = material;
		
		regName = parent.regName;
		tag = parent.tag;
		item = parent.item;
		damage = parent.damage;
		layer = parent.layer;
	}
	
	public Material(ClientMaterial material, JsonObject object) {
		this.clientMaterial = material;
		// TODO: error reporting
		regName = new ResourceLocation(object.getAsJsonPrimitive("name").getAsString());
		
		String item = object.getAsJsonPrimitive("item").getAsString();
		if (item.startsWith("#")) tag = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(item.substring(1)));
		else this.item = new ResourceLocation(item);
		
		damage = object.get("damage").getAsDouble();
		layer = object.get("layer").getAsString();
	}
	
	public static Material fromTag(CompoundTag tag) {
		Material m;
		if (tag.contains("client", Tag.TAG_COMPOUND))
			m = new Material(ClientMaterial.fromTag(tag.getCompound("client")));
		else m = new Material(null, new ResourceLocation(tag.getString("name")));
		
		if (tag.contains("item")) m.item = new ResourceLocation(tag.getString("item"));
		else if (tag.contains("tag")) TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(tag.getString("tag")));
		
		m.damage = tag.getDouble("damage");
		m.layer = tag.getString("layer");
		
		return m;
	}
	
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		if (clientMaterial != null) tag.put("client", clientMaterial.toTag());
		
		tag.putString("name", regName.toString());
		
		if (item != null)
			tag.putString("item", item.toString());
		else if (this.tag != null)
			tag.putString("tag", this.tag.location().toString());
		
		tag.putDouble("damage", damage);
		tag.putString("layer", layer);
		
		return tag;
	}
	
	public static Material parse(String path, JsonObject object) {
		ClientMaterial clientMaterial = null;
		if (object.has("client")) clientMaterial = ClientMaterial.parse(path, object.getAsJsonObject("client"));
		return new Material(clientMaterial, object);
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
	
	public boolean isValid(ItemStack item) {
		if (this.item != null) return item.getItem().getRegistryName().equals(this.item);
		if (tag != null) item.is(tag);
		return false;
	}
}
