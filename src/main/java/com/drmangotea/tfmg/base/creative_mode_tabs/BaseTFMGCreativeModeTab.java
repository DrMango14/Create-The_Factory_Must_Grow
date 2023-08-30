package com.drmangotea.tfmg.base.creative_mode_tabs;

import com.drmangotea.tfmg.registry.TFMGCreativeModeTabs;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.infrastructure.item.CreateCreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class BaseTFMGCreativeModeTab extends TFMGCreativeModeTab {
    public BaseTFMGCreativeModeTab() {
        super("base");
    }

    @Override
    public ItemStack makeIcon() {
        return TFMGItems.ZINC_GRENADE.asStack();
    }
}
