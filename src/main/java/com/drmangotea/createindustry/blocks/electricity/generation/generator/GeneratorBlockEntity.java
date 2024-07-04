package com.drmangotea.createindustry.blocks.electricity.generation.generator;

import com.drmangotea.createindustry.blocks.electricity.base.KineticElectricBlockEntity;
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
    public int feGeneration() {
        return (int) Math.abs(getSpeed()*1.5);
    }
    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getOpposite();
    }
    @Override
    public int voltageGeneration() {
        return (int) Math.abs(getSpeed());
    }




    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        generationSpeed.chase(getGeneratedSpeed(), 1 / 16f, LerpedFloat.Chaser.EXP);
    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {


        super.addToGoggleTooltip(tooltip, isPlayerSneaking);


        Lang.translate("goggles.generator.production", voltageGeneration())
                .style(ChatFormatting.AQUA)
                .forGoggles(tooltip, 1);


        return true;
    }
}
