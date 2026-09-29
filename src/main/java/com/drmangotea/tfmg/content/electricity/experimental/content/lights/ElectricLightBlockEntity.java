package com.drmangotea.tfmg.content.electricity.experimental.content.lights;


import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Resistance;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.drmangotea.tfmg.content.electricity.experimental.content.lights.ElectricLightBlock.LIGHT;


public class ElectricLightBlockEntity extends SmartBlockEntity implements IRealisticElectric {

    public LerpedFloat glow = LerpedFloat.linear();

    boolean signalChanged;

    boolean hasSignal;

    public DyeColor color = DyeColor.WHITE;

    LightProperties p;

    public float current = 0;

    public ElectricLightBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        p = new LightProperties(pos.asLong());
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void tick() {
        super.tick();
        if (!hasSignal) {
            glow.chase(current * 102.5, 0.1, LerpedFloat.Chaser.EXP);
            glow.tickChaser();
            //if (Math.min(current * 30, 15) != getBlockState().getValue(LIGHT))
            //    level.setBlock(getBlockPos(), getBlockState().setValue(LIGHT, (int) Math.min(current * 30, 15)), 2);
        } else {
            if (getBlockState().getValue(LIGHT) != 0)
                level.setBlock(getBlockPos(), getBlockState().setValue(LIGHT, 0), 2);
            glow.chase(0, 0.4, LerpedFloat.Chaser.EXP);
            glow.tickChaser();
//
        }
        if (signalChanged) {
            signalChanged = false;
            analogSignalChanged(level.getBestNeighborSignal(worldPosition));
        }
    }

    public void setColor(DyeColor color) {
        if (color == DyeColor.BLACK || color == DyeColor.LIGHT_GRAY || color == DyeColor.GRAY)
            return;

        this.color = color;
        notifyUpdate();
    }

    @Override
    public ElectricalProperties getProperties() {
        return p;
    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        IRealisticElectric.super.makeMultimeterTooltip(tooltip, isPlayerSneaking);
        ElectricalProperties properties = RealElectricNetworkManager.getNetwork(level).members.get(getPos());

        if (properties == null) {
            return false;
        }


        if (!properties.components.isEmpty() && properties.components.getFirst() instanceof Resistance resistor) {
            TFMGLang.text("Resistance: " + resistor.resistance).forGoggles(tooltip);
            TFMGLang.text("Voltage: " + resistor.getVoltage(level)).forGoggles(tooltip);
            TFMGLang.text("Power: " + Math.pow(resistor.getVoltage(level), 2) / resistor.resistance).forGoggles(tooltip);
        }
        return true;
    }

    @Override
    public void onUpdated() {
        RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(level);
        
            Resistance resistor = network.getResistance(getPos(),0);

            if(resistor != null){

                double resistance = resistor.resistance;
                double voltage = resistor.getVoltage(level);
                current = (float) (voltage / resistance);

            }

    }

    @Override
    public void remove() {
        super.remove();
        this.removeBlock();
    }

    public void neighbourChanged() {
        if (!hasLevel())
            return;
        boolean powered = level.getBestNeighborSignal(worldPosition) > 0;
        if (powered != hasSignal)
            signalChanged = true;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        neighbourChanged();
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        NBTHelper.writeEnum(compound, "color", color);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        color = NBTHelper.readEnum(compound, "color", DyeColor.class);
    }


    protected void analogSignalChanged(int newSignal) {
        hasSignal = newSignal > 0;
    }


    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public Level getWorld() {
        return level;
    }
}