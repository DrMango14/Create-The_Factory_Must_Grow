package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.transformers.large;

import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Transformer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.switches.large_switch.LargeSwitchBlock.IS_MAIN_PART;

public class LargeTransformerBlockEntity extends SmartBlockEntity implements IRealisticElectric {
    public boolean updateInFront = false;


    public float turnRatio = 10;

    ElectricalProperties p;

    public double[] primaryVoltage = new double[3];

    public double[] secondaryVoltage = new double[3];


    public final boolean isMainPart;

    public LargeTransformerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        p = state.getValue(IS_MAIN_PART) ? new LargeTransformerProperties(getPos(), state.getValue(TFMGHorizontalDirectionalBlock.FACING)) : null;
        isMainPart = state.getValue(IS_MAIN_PART);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void remove() {
        super.remove();
        this.removeBlock();
    }
    @Override
    public void onPlace() {
        if (!isMainPart)
            return;
        IRealisticElectric.super.onPlace();

        getNetwork().setTransformerRatio(this, 0, turnRatio);
    }

    @Override
    public void onUpdated() {
        if (!isMainPart)
            return;
        RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(level);


        for (int i = 0; i < 3; i++) {
            Transformer t = network.getTransformer(getPos(), i);

            primaryVoltage[i] = t == null ? 0 : network.nodeVoltages.get(t.nodeA.networkId).minus(network.nodeVoltages.get(t.nodeB.networkId)).abs();
            secondaryVoltage[i] = t == null ? 0 : network.nodeVoltages.get(t.sA.networkId).minus(network.nodeVoltages.get(t.sB.networkId)).abs();

        }


    }

    public IRealisticElectric getControlledBlock() {
        Direction facing = getBlockState().getValue(TFMGHorizontalDirectionalBlock.FACING).getOpposite();
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof LargeTransformerBlockEntity be) {
            return be;
        }
        return null;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putFloat("Ratio", turnRatio);

    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        turnRatio = compound.getFloat("Ratio");

    }


    @Override
    public ElectricalProperties getProperties() {
        return p;
    }

    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public Level getWorld() {
        return level;
    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        for (int i = 0; i < 3; i++) {
            TFMGLang.text("Voltage primary " + i + "  " + primaryVoltage[i]).forGoggles(tooltip);
            TFMGLang.text("Voltage secondary " + i + "  " + secondaryVoltage[i]).forGoggles(tooltip);
        }

        TFMGLang.text("Ratio " + turnRatio).forGoggles(tooltip);

        return true;
    }


}
