package com.drmangotea.tfmg.config.client;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import net.createmod.catnip.config.ConfigBase;

@NothingNullByDefault
public class UIConfig extends ConfigBase {
    public final ConfigBool pressureNeedleWobble = b(true, "enablePressureNeedleWobble", "Whether the pressure needle wobbles for the aesthetics of extremely high or low pressure");

    @Override
    public String getName() {
        return "ui";
    }
}
