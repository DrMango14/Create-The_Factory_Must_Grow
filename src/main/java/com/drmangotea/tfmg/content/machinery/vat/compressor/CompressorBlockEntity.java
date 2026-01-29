package com.drmangotea.tfmg.content.machinery.vat.compressor;

import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
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



    public CompressorState getState(){
        if(Math.abs(getSpeed())<120){
            return CompressorState.NON_OPERATIONAL;
        }
        if(getSpeed()>0){
            return CompressorState.PRESSURIZING;
        }
        return CompressorState.DEPRESSURIZING;
    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if(getState()==CompressorState.NON_OPERATIONAL) {
            TFMGLang.translate("goggles.compressor.non_operational").style(ChatFormatting.RED).forGoggles(tooltip);
        }else if(getState()==CompressorState.PRESSURIZING){
            TFMGLang.translate("goggles.compressor.pressurizing").style(ChatFormatting.YELLOW).forGoggles(tooltip);
        }else {
            TFMGLang.translate("goggles.compressor.depressurizing").style(ChatFormatting.AQUA).forGoggles(tooltip);
        }

        return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }

    public enum CompressorState{
        PRESSURIZING,
        DEPRESSURIZING,
        NON_OPERATIONAL

    }

}
