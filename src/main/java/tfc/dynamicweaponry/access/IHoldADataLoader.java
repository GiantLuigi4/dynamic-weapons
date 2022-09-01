package tfc.dynamicweaponry.access;

import tfc.dynamicweaponry.loading.MaterialLoader;

public interface IHoldADataLoader {
	MaterialLoader myLoader();
	void setLoader(MaterialLoader loader);
}
