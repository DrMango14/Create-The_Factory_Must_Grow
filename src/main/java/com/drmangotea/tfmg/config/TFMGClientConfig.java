package com.drmangotea.tfmg.config;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.config.client.RenderConfig;
import com.drmangotea.tfmg.config.client.UIConfig;
import net.createmod.catnip.config.ConfigBase;

@NothingNullByDefault
public class TFMGClientConfig extends ConfigBase {

    public final UIConfig ui = nested(0, UIConfig::new, "Config options for TFMG's UI");
	public final RenderConfig render = nested(0, RenderConfig::new, "Config options for custom TFMG rendering");

    @Override
    public String getName() {
        return "client";
    }
}
