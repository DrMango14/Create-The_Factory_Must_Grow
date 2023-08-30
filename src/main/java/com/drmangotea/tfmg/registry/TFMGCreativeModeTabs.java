package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.base.creative_mode_tabs.BaseTFMGCreativeModeTab;
import com.drmangotea.tfmg.base.creative_mode_tabs.ConcreteCreativeModeTab;
import com.simibubi.create.content.decoration.palettes.PalettesCreativeModeTab;
import com.simibubi.create.infrastructure.item.BaseCreativeModeTab;
import net.minecraft.world.item.CreativeModeTab;

public class TFMGCreativeModeTabs {

        public static final CreativeModeTab TFMG_BASE = new BaseTFMGCreativeModeTab();
        public static final CreativeModeTab TFMG_CONCRETE = new ConcreteCreativeModeTab();

        public static void init() {
        }

}
