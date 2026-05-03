package com.drmangotea.tfmg.content.electricity.utilities.converter;

import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.storage.AccumulatorBlockEntity;
import com.drmangotea.tfmg.content.electricity.storage.TFMGForgeEnergyStorage;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import dev.technici4n.grandpower.api.ILongEnergyStorage;

import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.List;

import static com.drmangotea.tfmg.content.electricity.utilities.converter.ConverterBlock.INPUT;
import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class ConverterBlockEntity extends ElectricBlockEntity {

    public final TFMGForgeEnergyStorage energy = createEnergyStorage();
    private final ILongEnergyStorage longEnergy = ILongEnergyStorage.of(energy);

    private static final int MAX_FE_TRANSFER = 10000;

    public int timer = 0;

    protected ScrollValueBehaviour voltageGenerated;

    public ConverterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public TFMGForgeEnergyStorage createEnergyStorage() {
        int cap = TFMGConfigs.common().machines.accumulatorStorage.get();
        return new TFMGForgeEnergyStorage(cap, MAX_FE_TRANSFER) {
            @Override
            public void onEnergyChanged(int amount, int a) {
                sendStuff();
            }
        };

    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                TFMGBlockEntities.CONVERTER.get(),
                (be, context) -> be.energy
        );
        event.registerBlockEntity(
                ILongEnergyStorage.BLOCK,
                TFMGBlockEntities.CONVERTER.get(),
                (be, context) -> be.longEnergy
        );
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        int max = 250;
        voltageGenerated = new ScrollValueBehaviour(TFMGLang.translateDirect("creative_generator.voltage_generation"),
                this, new ConverterValueBox());
        voltageGenerated.between(1, max);
        voltageGenerated.value = 20;
        voltageGenerated.withCallback(i -> this.updateNextTick());
        behaviours.add(voltageGenerated);

    }


    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);
        compound.putInt("ForgeEnergy", energy.getEnergyStored());
    }


    public boolean isInput() {
        return getBlockState().getValue(INPUT);
    }




    @Override
    public float resistance() {
        if (voltageGeneration() > 0)
            return 0;


        int power = 0;
        for (IElectric member : getOrCreateElectricNetwork().members)
            if (!(member instanceof ConverterBlockEntity) && !(member instanceof AccumulatorBlockEntity))
                power += member.getPowerUsage();
        if (energy.getEnergyStored() == getMaxCapacity() || getData().getVoltage() <= voltageGenerated.getValue() || canPower())
            return 0;
        if(Math.min(Math.max((data.networkPowerGeneration - power), 0), getMaxChargingRate())==0){
            return 0;
        }


        return (float) (data.voltage * data.voltage) /Math.min(Math.max((data.networkPowerGeneration - power), 0), getMaxChargingRate());
    }

    public boolean canPower() {
        if(timer!=0)
            return false;

        if (getBlockState().getValue(INPUT))
            return false;

        return getData().networkResistance > 0 && (getData().getVoltage() <= voltageGenerated.getValue()) && energy.getEnergyStored() > 0;
    }


    public int getChargingRate() {
        //
        // int chargingRate = Math.max((data.networkPowerGeneration - getNetworkPowerUsage()), 0);
        if (energy.getEnergyStored() == getMaxCapacity() || getData().getVoltage() < voltageGenerated.value || canPower()|| data.notEnoughPower)
            return 0;

        //return Math.min(chargingRate, getMaxChargingRate());
        return getMaxChargingRate();
    }

    @Override
    public int powerGeneration() {
        if (canPower()) {
            return 10000;
        }
        return 0;
    }

    @Override
    public void tick() {
        super.tick();

        if(timer>0){

            if(timer == 1)
                updateNextTick();

            timer--;
        }

        pushFeToLongEnergyNeighbors();

        if (getBlockState().getValue(INPUT)) {
            if (getData().getVoltage() > TFMGConfigs.common().machines.accumulatorVoltage.get()) {
                energy.receiveEnergy((int) (getChargingRate() / TFMGConfigs.common().machines.FEtoWattTickConversionRate.get()), false);

            }
        } else if (canPower()) {

            int energyToExtract = data.networkPowerGeneration == 0 ? getNetworkPowerUsage() : (int) Math.max(0, Math.max(((float) powerGeneration() / (float) data.networkPowerGeneration) * (float) getNetworkPowerUsage(), 0));
            energyToExtract /= TFMGConfigs.common().machines.FEtoWattTickConversionRate.get();
            energy.extractEnergy(Math.max(energyToExtract, 1), false);
            if (energy.getEnergyStored() == 0) {
                timer = 100;
                updateNextTick();
            }
        }

    }

    private void pushFeToLongEnergyNeighbors() {
        Level level = this.level;
        if (level == null || level.isClientSide() || energy.getEnergyStored() <= 0) {
            return;
        }
        BlockPos origin = getBlockPos();
        for (Direction dir : Direction.values()) {
            BlockPos p = origin.relative(dir);
            ILongEnergyStorage target = level.getCapability(
                    ILongEnergyStorage.BLOCK,
                    p,
                    level.getBlockState(p),
                    level.getBlockEntity(p),
                    dir.getOpposite()
            );
            if (target == null || !target.canReceive()) {
                continue;
            }
            int offer = Math.min(energy.getEnergyStored(), MAX_FE_TRANSFER);
            long accepted = target.receive(offer, true);
            if (accepted <= 0L) {
                continue;
            }
            int moved = energy.extractEnergy((int) Math.min(accepted, offer), false);
            if (moved > 0) {
                target.receive(moved, false);
            }
            if (energy.getEnergyStored() <= 0) {
                break;
            }
        }
    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.makeMultimeterTooltip(tooltip, isPlayerSneaking);

        TFMGTexts.electricalCapacity(energy.getEnergyStored()).forGoggles(tooltip, 1);
        TFMGTexts.chargingRate(getChargingRate()).forGoggles(tooltip, 1);
        TFMGTexts.electricalMaxCapacity(getMaxCapacity()).forGoggles(tooltip, 1);

        return true;
    }

    public int getMaxCapacity() {
        return TFMGConfigs.common().machines.accumulatorStorage.get();
    }

    //in FE per tick
    public int getMaxChargingRate() {
        return TFMGConfigs.common().machines.accumulatorChargingRate.get()*10;
    }

    @Override
    public int voltageGeneration() {

        return canPower() ? voltageGenerated.getValue() : 0;
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        energy.setEnergy(compound.getInt("ForgeEnergy"));
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getClockWise();
    }
    public static class ConverterValueBox extends ValueBoxTransform.Sided {
        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 3, 16.05);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return direction.getAxis() == state.getValue(HORIZONTAL_FACING).getAxis();
        }
    }
}
