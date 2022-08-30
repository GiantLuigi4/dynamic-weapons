package tfc.dynamicweaponry;

public class ToolLayer {
	Material[] materials;
	
	public ToolLayer() {
		materials = new Material[16 * 16];
	}
	
	public ToolLayer(Material[] materials) {
		this.materials = materials;
	}
	
	private static int index(int x, int y) {
		return x * 16 + y;
	}
	
	public void set(int i, int i1, Material mat) {
		materials[index(i, i1)] = mat;
	}
	
	public Material get(int x, int y) {
		return materials[index(x, y)];
	}
}
