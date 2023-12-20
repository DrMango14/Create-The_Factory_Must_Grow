package com.drmangotea.createindustry.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class WorldGenConfig extends ConfigBase {

	/**
	 * Increment this number if all worldgen config entries should be overwritten
	 * in this update. Worlds from the previous version will overwrite potentially
	 * changed values with the new defaults.
	 */
	public static final int FORCED_UPDATE_VERSION = 1;

	public final ConfigBool disableOil = b(false, "disableOil", Comments.disableOil);
	public final ConfigBool disableSimulatedOil = b(false, "disableSimulatedOil", Comments.disableSimulatedOil);


	@Override
	public String getName() {
		return "worldgen" + FORCED_UPDATE_VERSION;
	}

	private static class Comments {
		static String disableOil = "Prevents oil geodes from spawning";
		static String disableSimulatedOil = "Prevents oil deposit veins from spawning in bedrock";
	}
}
