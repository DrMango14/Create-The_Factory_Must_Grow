package com.drmangotea.createindustry.blocks.electricity.generation.creative_generator;

import com.drmangotea.createindustry.blocks.electricity.base.ElectricBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.base.IElectricBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CreativeGeneratorBlockEntity extends ElectricBlockEntity implements IHaveGoggleInformation {

    protected ScrollValueBehaviour outputVoltage;

    public CreativeGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();

        energy.setEnergy(5000);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        int max = 250;
        outputVoltage = new ScrollValueBehaviour(Lang.translateDirect("creative_generator.voltage_generation"),
                this, new CreativeGeneratorValueBox());
        outputVoltage.between(0, max);
        outputVoltage.value = 50;
        //outputVoltage.withCallback(i -> this.updateVoltageOutput());
        behaviours.add(outputVoltage);
    }
    @Override
    public float maxVoltage() {
        return Float.MAX_VALUE;
    }
    @Override
    public int feGeneration() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int voltageGeneration() {
        return outputVoltage.getValue()*10;
    }




    @Override
    public boolean hasElectricitySlot(Direction direction) {


        return true;
    }

    @Override
    public int transferSpeed() {
        return 1000;
    }

    class CreativeGeneratorValueBox extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 16);
        }

        @Override
        public Vec3 getLocalOffset(BlockState state) {
            return super.getLocalOffset(state);
        }

        @Override
        public void rotate(BlockState state, PoseStack ms) {
            super.rotate(state, ms);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return direction == Direction.UP;
        }

    }
    @Override
    public void transferCharge(IElectricBlock be) {


        int energy = getForgeEnergy().getEnergyStored();
        int freeSpace = be.getForgeEnergy().getMaxEnergyStored()-be.getForgeEnergy().getEnergyStored();


        //  int maxPossibleTransfer = (int) (Math.min(energy,freeSpace));


        int maxPossibleTransfer = 1000;


        int test = be.getForgeEnergy().receiveEnergy(maxPossibleTransfer, true);
        int test2 = getForgeEnergy().extractEnergy(maxPossibleTransfer, true);

        maxPossibleTransfer = Math.min(Math.min(test2,test2),maxPossibleTransfer);




        if(be.getForgeEnergy().getEnergyStored() <= getForgeEnergy().getEnergyStored()) {
            be.getForgeEnergy().receiveEnergy(Integer.MAX_VALUE, false);
        }





    }
}
