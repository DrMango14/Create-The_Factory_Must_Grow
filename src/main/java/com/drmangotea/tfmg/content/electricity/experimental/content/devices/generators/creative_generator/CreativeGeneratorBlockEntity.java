package com.drmangotea.tfmg.content.electricity.experimental.content.devices.generators.creative_generator;

import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.packets.UpdateVoltagePacket;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Resistance;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CreativeGeneratorBlockEntity extends SmartBlockEntity implements IRealisticElectric {

    ElectricalProperties properties;


    protected ScrollValueBehaviour outputVoltage;

    public CreativeGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        properties = new CreativeGeneratorProperties(getPos());
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        int max = 250;
        outputVoltage = new ScrollValueBehaviour(TFMGLang.translateDirect("creative_generator.voltage_generation"),
                this, new CreativeGeneratorValueBox());
        outputVoltage.between(0, max);
        outputVoltage.value = 50;
        outputVoltage.withCallback(i -> update(this, i));
        behaviours.add(outputVoltage);
    }

    @Override
    public ElectricalProperties getProperties() {
        return properties;
    }

    @Override
    public void remove() {
        super.remove();
        this.removeBlock();
    }

    
    public static void update(CreativeGeneratorBlockEntity be, int value) {

        Level level = be.level;

        RealElectricalNetwork electricalNetwork = RealElectricNetworkManager.getNetwork(level);



        electricalNetwork.setVoltageGen(be, value * 10);
        be.sendData();
        electricalNetwork.update();

        CatnipServices.NETWORK.sendToClientsTrackingChunk((ServerLevel) level, new ChunkPos(be.worldPosition), new UpdateVoltagePacket(be, value * 10));



    }

    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public Level getWorld() {
        return level;
    }

    class CreativeGeneratorValueBox extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 16);
        }


        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
            super.rotate(level, pos, state, ms);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return direction == Direction.UP;
        }

    }
}
