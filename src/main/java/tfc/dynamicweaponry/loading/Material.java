package tfc.dynamicweaponry.loading;

import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class Material {
	public final ClientMaterial clientMaterial;
	public final ResourceLocation regName;
	
	public Material(ClientMaterial clientMaterial) {
		this.clientMaterial = clientMaterial;
		this.regName = clientMaterial.regName;
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
