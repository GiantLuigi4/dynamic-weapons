package tfc.dynamicweaponry.registry;

import com.tfc.assortedutils.API.gui.screen.SimpleContainerScreenFactory;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import tfc.dynamicweaponry.client.ToolCreationScreen;

public class RegistryClient {
	public static final DeferredRegister<SimpleContainerScreenFactory> CONTAINERS_SCREENS = DeferredRegister.create(SimpleContainerScreenFactory.class, "dynamic_weaponry");
	
	public static final RegistryObject<SimpleContainerScreenFactory> TOOL_FORGE_SCREEN_FACTORY = CONTAINERS_SCREENS.register("tool_forge", () -> SimpleContainerScreenFactory.build(((minecraft, containerType) -> new ToolCreationScreen(StringTextComponent.EMPTY, minecraft, containerType))));
}
