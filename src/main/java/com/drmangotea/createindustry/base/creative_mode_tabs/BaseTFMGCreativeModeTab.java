package com.drmangotea.createindustry.base.creative_mode_tabs;

import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGCreativeModeTabs;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.infrastructure.item.CreateCreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class BaseTFMGCreativeModeTab extends TFMGCreativeModeTab {
    public BaseTFMGCreativeModeTab() {
        super("base");
    }

    @Override
    public ItemStack makeIcon() {
        return TFMGBlocks.GASOLINE_ENGINE.asStack();
    }
}
