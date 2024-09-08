package com.drmangotea.tfmg.config;

import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.infrastructure.config.CEquipment;

public class MachineConfig extends ConfigBase {

	public final ConfigFloat largeGeneratorFeModifier = f(4, 0, "largeGeneratorFEModifier", Comments.largeGenerator);
	public final ConfigFloat smallGeneratorFeModifier = f(0.4f, 0, "smallGeneratorFEModifier", Comments.generator);

	@Override
	public String getName() {
		return "machines";
	}



	private static class Comments {
		static String largeGenerator = "Changes the FE production of large generators.";
		static String generator = "Changes the FE production of small generators.";

	}
}
