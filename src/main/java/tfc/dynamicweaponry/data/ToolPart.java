package tfc.dynamicweaponry.data;

import java.util.Arrays;
import java.util.Objects;

public class ToolPart {
	public final PartType type;
	protected ToolPart[] incompatibilities;
	protected ToolPart[] dependencies;
	public final int listIndex;
	public final ToolType toolType;
	
	public ToolPart(PartType type, int listIndex, ToolType toolType) {
		this.type = type;
		this.listIndex = listIndex;
		this.toolType = toolType;
	}
	
	public ToolPart[] getIncompatibilities() {
		return incompatibilities;
	}
	
	public ToolPart[] getDependencies() {
		return dependencies;
	}
	
	@Override
	public String toString() {
		return "ToolPart{" +
				"type=" + type +
				", incompatibilities=" + Arrays.toString(incompatibilities) +
				", dependencies=" + Arrays.toString(dependencies) +
				", listIndex=" + listIndex +
				'}';
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ToolPart toolPart = (ToolPart) o;
		return listIndex == toolPart.listIndex;
//				&& Objects.equals(toolType, toolPart.toolType);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(listIndex);
	}
}
