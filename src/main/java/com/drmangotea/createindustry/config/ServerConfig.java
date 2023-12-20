package com.drmangotea.createindustry.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class ServerConfig extends ConfigBase {

	public final StressConfig stressValues = nested(0, StressConfig::new, "Fine tune the kinetic stats of individual components");

	@Override
	public String getName() {
		return "server";
	}
}
