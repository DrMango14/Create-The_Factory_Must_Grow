package com.drmangotea.tfmg.blocks.electricity.generation.generator;


import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.blocks.electricity.base.KineticElectricBlockEntity;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public class GeneratorBlockEntity extends KineticElectricBlockEntity {

    LerpedFloat generationSpeed = LerpedFloat.linear();
    public GeneratorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public void tick(){
        super.tick();


        float targetSpeed = getSpeed();
        generationSpeed.updateChaseTarget(targetSpeed);
        generationSpeed.tickChaser();




    }



    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getOpposite();
    }
    @Override
    public int voltageGeneration() {
        return (int) Math.min(255,FEProduction()*3.5f);
    }


    @Override
    public int FEProduction() {
        float modifier = TFMGConfigs.server().machines.smallGeneratorFeModifier.getF();


        return  (int) (((Math.log(Math.abs(getSpeed())-68.25)/Math.log(1.026))-22)*modifier);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        generationSpeed.chase(getGeneratedSpeed(), 1 / 16f, LerpedFloat.Chaser.EXP);
    }
    @Override
    public int maxVoltage() {
        return MaxBlockVoltage.MAX_VOLTAGES.get(TFMGBlockEntities.GENERATOR.get());
    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {


        super.addToGoggleTooltip(tooltip, isPlayerSneaking);


        Lang.translate("goggles.generator.production", voltageGeneration())
                .style(ChatFormatting.AQUA)
                .forGoggles(tooltip, 1);


        return true;
    }

   // @Override
   // public void lazyTick() {
   //     super.lazyTick();
   //    // if(voltageGeneration() == 0)
   //    //     getOrCreateElectricNetwork().updateNetworkVoltage();
//
//
   // }
}
