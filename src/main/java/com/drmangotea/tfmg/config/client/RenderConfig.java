package com.drmangotea.tfmg.config.client;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import net.createmod.catnip.config.ConfigBase;

@NothingNullByDefault
public class RenderConfig extends ConfigBase {
	public final ConfigInt cableRenderDistance = i(64, "cableRenderDistance", "The distance cables have to start within to render.");
	
	@Override
	public String getName() {
		return "render";
	}
}
