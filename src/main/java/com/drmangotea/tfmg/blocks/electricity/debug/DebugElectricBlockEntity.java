package com.drmangotea.tfmg.blocks.electricity.debug;


import com.drmangotea.tfmg.blocks.electricity.base.ElectricBlockEntity;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class DebugElectricBlockEntity extends ElectricBlockEntity {
    public DebugElectricBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {



        Lang.translate("voltage", getVoltage())
                .style(ChatFormatting.GREEN)
                .forGoggles(tooltip, 1);

        Lang.translate("voltage", getOrCreateElectricNetwork().members.size())
                .style(ChatFormatting.GOLD)
                .forGoggles(tooltip, 1);

        Lang.translate("voltage", getOrCreateElectricNetwork().voltage)
                .style(ChatFormatting.GREEN)
                .forGoggles(tooltip, 1);
        Lang.translate("voltage", voltageGeneration())
                    .style(ChatFormatting.BLUE)
                    .forGoggles(tooltip, 1);


        Lang.translate("network", BlockPos.of(network).getX())
                .style(ChatFormatting.YELLOW)
                .forGoggles(tooltip, 1);
        Lang.translate("network", BlockPos.of(network).getY())
                .style(ChatFormatting.YELLOW)
                .forGoggles(tooltip, 1);
        Lang.translate("network", BlockPos.of(network).getZ())
                .style(ChatFormatting.YELLOW)
                .forGoggles(tooltip, 1);


        return true;
    }


    @Override
    public void lazyTick() {
        super.lazyTick();
        if(!(this instanceof DebugSourceBlockEntity))
            getOrCreateElectricNetwork().requestEnergy(this);
    }

    @Override
    public int maxVoltage() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return true;
    }
}
