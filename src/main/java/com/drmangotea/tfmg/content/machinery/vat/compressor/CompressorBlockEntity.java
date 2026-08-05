package com.drmangotea.tfmg.content.machinery.vat.compressor;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CompressorBlockEntity extends KineticBlockEntity {

    public CompressorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    public CompressorState getState() {
        if (Math.abs(getSpeed()) < 120)
            return CompressorState.NON_OPERATIONAL;
        if (getSpeed() > 0)
            return CompressorState.PRESSURIZING;
        return CompressorState.DEPRESSURIZING;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        switch (getState()) {
            case NON_OPERATIONAL ->
                CreateLang.translate("goggles.compressor.non_operational")
                        .style(ChatFormatting.RED).forGoggles(tooltip);
            case PRESSURIZING ->
                CreateLang.translate("goggles.compressor.pressurizing")
                        .style(ChatFormatting.YELLOW).forGoggles(tooltip);
            case DEPRESSURIZING ->
                CreateLang.translate("goggles.compressor.depressurizing")
                        .style(ChatFormatting.AQUA).forGoggles(tooltip);
        }
        return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }

    public enum CompressorState {
        PRESSURIZING,
        DEPRESSURIZING,
        NON_OPERATIONAL
    }
}
