package com.drmangotea.tfmg.blocks.electricity.generation.creative_generator;


import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.blocks.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.blocks.electricity.base.cables.VoltagePacket;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGPackets;
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
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class CreativeGeneratorBlockEntity extends ElectricBlockEntity implements IHaveGoggleInformation {

    protected ScrollValueBehaviour outputVoltage;

    boolean packetNextTick = false;

    public CreativeGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();

        if(packetNextTick&&!level.isClientSide){
            TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new VoltagePacket(getBlockPos()));
            packetNextTick = false;
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        int max = 250;
        outputVoltage = new ScrollValueBehaviour(Lang.translateDirect("creative_generator.voltage_generation"),
                this, new CreativeGeneratorValueBox());
        outputVoltage.between(0, max);
        outputVoltage.value = 50;
        outputVoltage.withCallback(i -> update());
        //outputVoltage.withCallback(i -> this.updateVoltageOutput());
        behaviours.add(outputVoltage);
    }

    public void update(){


        needsNetworkUpdate();
        needsVoltageUpdate();

        packetNextTick = true;

        sendStuff();
    }



    @Override
    public int FEProduction() {
        return 1000;
    }

    @Override
    public int FECapacity() {
        return 100000;
    }

    @Override
    public int maxVoltage() {
        return MaxBlockVoltage.MAX_VOLTAGES.get(TFMGBlockEntities.CREATIVE_GENERATOR.get());
    }


    @Override
    public int voltageGeneration() {
        return outputVoltage.getValue()*10;
    }




    @Override
    public boolean hasElectricitySlot(Direction direction) {


        return true;
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

    //@Override
    //public void lazyTick() {
    //    super.lazyTick();
    //    getOrCreateElectricNetwork().updateNetworkVoltage();
    //}
}
