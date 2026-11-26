package com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.crank;

import com.drmangotea.tfmg.content.machinery.misc.machine_input.MachineInputBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class PumpjackCrankBlockEntity extends KineticBlockEntity {

    public float angle = 0;

    public Direction direction;
    public float heightModifier = 0;
    public float crankRadius = 0.7f;

    public PumpjackCrankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick() {
        super.tick();
        direction = this.getBlockState().getValue(FACING);
        setAngle();
        heightModifier = (float) (crankRadius * Math.sin(Math.toRadians(angle)));
    }

    public float getMachineInputSpeed() {
        if (level != null && level.getBlockEntity(getBlockPos().below()) instanceof MachineInputBlockEntity be)
            return be.getSpeed();
        return 0;
    }

    private void setAngle() {
        if (level != null && level.getBlockEntity(getBlockPos().below()) instanceof MachineInputBlockEntity) {
            float time;
            if (level.isClientSide) {
                time = AnimationTickHolder.getRenderTime(getLevel());
            } else time = level.getBlockTicks().hashCode();
            float speed = Math.min(getMachineInputSpeed() / 20f, 3f);
            angle = (speed == 0) ? 180f : (time * speed) % 360;
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}
}