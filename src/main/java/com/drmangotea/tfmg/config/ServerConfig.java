package com.drmangotea.tfmg.config;

import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.infrastructure.config.CEquipment;

public class ServerConfig extends ConfigBase {


	public final MachineConfig machines = nested(0, MachineConfig::new, "Config options for TFMG's machinery");
	public final StressConfig stressValues = nested(0, StressConfig::new, "Fine tune the kinetic stats of individual components");

	@Override
	public String getName() {
		return "server";
	}
}
