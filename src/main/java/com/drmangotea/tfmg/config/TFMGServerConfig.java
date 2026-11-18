package com.drmangotea.tfmg.config;

import com.drmangotea.tfmg.config.server.TFMGStress;
import net.createmod.catnip.config.ConfigBase;

public class TFMGServerConfig extends ConfigBase {

	public final TFMGStress stressValues = nested(0, TFMGStress::new, "Fine tune the kinetic stats of individual components");

	@Override
	public String getName() {
		return "server";
	}
}
