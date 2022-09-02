package tfc.dynamicweaponry.access;

import tfc.dynamicweaponry.loading.MaterialLoader;
import tfc.dynamicweaponry.loading.Materials;

public interface IHoldADataLoader {
	MaterialLoader myLoader();
	void setLoader(MaterialLoader loader);
}
