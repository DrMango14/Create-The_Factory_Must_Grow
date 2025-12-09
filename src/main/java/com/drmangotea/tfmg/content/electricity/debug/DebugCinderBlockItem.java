package com.drmangotea.tfmg.content.electricity.debug;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class DebugCinderBlockItem extends Item {
    public DebugCinderBlockItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        if (level.getBlockEntity(pos) instanceof IElectric be) {
            TFMG.LOGGER.debug("VOLTAGE "+be.getData().voltage);

        }
        return InteractionResult.SUCCESS;
    }
}
