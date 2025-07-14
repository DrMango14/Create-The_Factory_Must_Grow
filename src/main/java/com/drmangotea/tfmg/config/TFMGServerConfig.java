package com.drmangotea.tfmg.config;


import net.createmod.catnip.config.ConfigBase;

public class TFMGServerConfig extends ConfigBase {



	public final TFMGStress stressValues = nested(0, TFMGStress::new, "Fine tune the kinetic stats of individual components");

	@Override
	public String getName() {
		return "server";
	}
}
