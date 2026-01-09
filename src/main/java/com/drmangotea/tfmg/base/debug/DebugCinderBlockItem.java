package com.drmangotea.tfmg.base.debug;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankBlock;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankBlockEntity;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.generators.large_generator.StatorBlockEntity;
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

           be.onPlaced();

        }

        if (level.getBlockEntity(pos) instanceof SteelTankBlockEntity be) {
            if (!context.getPlayer().isCrouching()) {
                //SteelTankBlock.updateTowerState(be.getLevel(), be.getBlockPos(), true, false);
                // be.updateTemperature();
                TFMG.LOGGER.debug(String.valueOf(be.isDistillationTower));
                //be.sendData();
                //be.getControllerBE().sendData();
            } else {
                SteelTankBlock.updateTowerState(be.getLevel(), be.getBlockPos(), false, false);
                TFMG.LOGGER.debug(String.valueOf(be.isDistillationTower));
            }
        }
        return InteractionResult.SUCCESS;
    }
}
