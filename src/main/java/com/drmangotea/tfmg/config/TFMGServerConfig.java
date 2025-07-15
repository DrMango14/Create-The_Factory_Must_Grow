package com.drmangotea.tfmg.config;


import net.createmod.catnip.config.ConfigBase;

public class TFMGServerConfig extends ConfigBase {



	public final TFMGStress stressValues = nested(0, TFMGStress::new, "Fine tune the kinetic stats of individual components");
	public final TFMGResistivity resistivityValues = nested(0, TFMGResistivity::new, "Fine tune the resistivity stats of individual cable types");

	@Override
	public String getName() {
		return "server";
	}
}
