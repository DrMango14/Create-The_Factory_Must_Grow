package com.drmangotea.tfmg.content.electricity.storage;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.utilities.converter.ConverterBlockEntity;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.world.level.block.DirectionalBlock.FACING;

public class AccumulatorBlockEntity extends ElectricBlockEntity {

    public TFMGForgeEnergyStorage energy = createEnergyStorage(1);
    private LazyOptional<IEnergyStorage> energyCapability = LazyOptional.empty();
    public int length = 1;
    boolean refreshNextTick = true;
    public BlockPos controller = getBlockPos();
    int signal;
    boolean signalChanged;

    public AccumulatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        energyCapability = LazyOptional.of(() -> energy);

    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        neighbourChanged();
    }

    public void neighbourChanged() {

        if (!hasLevel())
            return;
        if (isController()) {
            int power = level.getBestNeighborSignal(worldPosition);


            if (power != signal)
                signalChanged = true;
        }
        if (level.getBlockEntity(controller) instanceof AccumulatorBlockEntity be) {
            int power = level.getBestNeighborSignal(worldPosition);


            if (power != be.signal)
                be.signalChanged = true;
        }
    }


    @Override
    public void remove() {
        super.remove();
        refreshController();
    }

    @Override
    public void onPlaced() {
        super.onPlaced();
        refreshController();
    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        if(!isController())
            if(level.getBlockEntity(controller) instanceof AccumulatorBlockEntity be)
                return be.makeMultimeterTooltip(tooltip, isPlayerSneaking);


        super.makeMultimeterTooltip(tooltip, isPlayerSneaking);


        CreateLang.text("Capacity ")
                .add(Component.literal(TFMGUtils.formatUnits(energy.getEnergyStored(), "FE")))
                .color(0x127799)
                .forGoggles(tooltip, 1);

        CreateLang.text("Charging Rate ")
                .add(CreateLang.number(getChargingRate()))
                .color(0x127799)
                .forGoggles(tooltip, 1);
        CreateLang.text("Max Capacity ")
                .add(CreateLang.number(getMaxCapacity()))
                .color(0x127799)
                .forGoggles(tooltip, 1);

        return true;
    }

    public void refreshController() {
        Direction facing = getBlockState().getValue(FACING);
        for (int i = 0; i < 15; i++) {
            BlockPos pos = getBlockPos().relative(getBlockState().getValue(FACING), i);
            if (level.getBlockEntity(pos) instanceof AccumulatorBlockEntity be && be.getBlockState().getValue(FACING) == facing &&
                    !(level.getBlockEntity(pos.relative(facing)) instanceof AccumulatorBlockEntity otherBE && otherBE.getBlockState().getValue(FACING) == facing)) {
                be.refreshMultiblock();
            }
        }
    }

    public void refreshMultiblock() {
        Direction facing = getBlockState().getValue(FACING);
        refreshCapability();
        if (!(level.getBlockEntity(getBlockPos().relative(facing)) instanceof AccumulatorBlockEntity be && be.getBlockState().getValue(FACING) == facing)) {
            int newLength = 1;
            controller = getBlockPos();
            for (int i = 1; i < 15; i++) {
                BlockPos pos = getBlockPos().relative(getBlockState().getValue(FACING).getOpposite(), i);
                if (level.getBlockEntity(pos) instanceof AccumulatorBlockEntity otherBe && otherBe.getBlockState().getValue(FACING) == getBlockState().getValue(FACING)) {
                    otherBe.controller = this.getBlockPos();
                    otherBe.refreshCapability();
                    otherBe.length = 0;
                    otherBe.energy.setEnergy(0);

                    newLength++;
                } else break;
            }
            length = newLength;
            int oldEnergy = energy.getEnergyStored();
            energy = createEnergyStorage(length);
            energy.setEnergy(Math.min(oldEnergy, energy.getMaxEnergyStored() * length));
            refreshCapability();
            updateNextTick();
            for (int i = 1; i < length; i++) {
                BlockPos pos = getBlockPos().relative(getBlockState().getValue(FACING).getOpposite(), i);
                if (level.getBlockEntity(pos) instanceof AccumulatorBlockEntity be) {
                    be.refreshCapability();
                    be.sendStuff();
                }
            }
            sendStuff();
        }
    }

    public void refreshCapability() {
        LazyOptional<IEnergyStorage> oldCap = energyCapability;
        if (level.getBlockEntity(controller) instanceof AccumulatorBlockEntity be) {
            energyCapability = LazyOptional.of(() -> be.energy);
        } else energyCapability = LazyOptional.of(() -> energy);
        oldCap.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (cap == ForgeCapabilities.ENERGY && side == null) {
            return energyCapability.cast();
        } else if (cap == ForgeCapabilities.ENERGY && hasElectricitySlot(side)) {
            return energyCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    public boolean isController() {
        return controller == getBlockPos();
    }

    public TFMGForgeEnergyStorage createEnergyStorage(int multiplier) {
        return new TFMGForgeEnergyStorage(getMaxCapacity() * multiplier, 10000) {
            @Override
            public void onEnergyChanged(int amount, int oldAmount) {

                if ((oldAmount == 0 && amount > 0) || (this.energy == 0)) {
                    updateNextTick();
                }
                sendStuff();
            }
        };
    }

    public void setCapacity(ItemStack stack) {
        energy.setEnergy(stack.getOrCreateTag().getInt("Storage"));
    }

    protected void analogSignalChanged() {

        if (!isController()) {
            signal = level.getBestNeighborSignal(controller);
            return;
        }

        int newSignal = 0;

        for (int i = 0; i < length; i++) {
            BlockPos pos = getBlockPos().relative(getBlockState().getValue(FACING).getOpposite(), i);

            newSignal = Math.max(newSignal, level.getBestNeighborSignal(pos));


        }

        updateNextTick();

        signal = newSignal;
    }

    @Override
    public void tick() {
        super.tick();
        if (signalChanged) {
            signalChanged = false;
            analogSignalChanged();
        }
        if (!isController())
            return;

        if (refreshNextTick) {
            refreshMultiblock();
            refreshNextTick = false;
        }

        if (getData().getVoltage() > TFMGConfigs.common().machines.accumulatorVoltage.get() * length) {
            energy.receiveEnergy((int) (getChargingRate() / TFMGConfigs.common().machines.FEtoWattTickConversionRate.get()), false);

            return;
        }
        if (canPower()) {

            int energyToExtract = data.networkPowerGeneration == 0 ? getNetworkPowerUsage() : (int) Math.max(0, Math.max(((float) powerGeneration() / (float) data.networkPowerGeneration) * (float) getNetworkPowerUsage(), 0));
            energyToExtract /= TFMGConfigs.common().machines.FEtoWattTickConversionRate.get();
            energy.extractEnergy(Math.max(energyToExtract, 1), false);
            if (energy.getEnergyStored() == 0)
                updateNextTick();
        }

    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.putInt("ForgeEnergy", energy.getEnergyStored());
        compound.putInt("Length", length);

    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        length = compound.getInt("Length");
        //energy = createEnergyStorage(length);
        energy.setEnergy(compound.getInt("ForgeEnergy"));


    }

    @Override
    public float resistance() {
        if (voltageGeneration() > 0)
            return 0;
        if (!isController())
            return 0;

        int power = 0;
        for (IElectric member : getOrCreateElectricNetwork().members)
            if (!(member instanceof ConverterBlockEntity) && !(member instanceof AccumulatorBlockEntity))
                power += member.getPowerUsage();
        if (energy.getEnergyStored() == getMaxCapacity() || getData().getVoltage() <= getOutputVoltage() || canPower())
            return 0;
        if(Math.min(Math.max((data.networkPowerGeneration - power), 0), getMaxChargingRate())==0){
            return 0;
        }

        return (float) Math.min((Math.pow(data.voltage, 2)) / Math.min(Math.max((data.networkPowerGeneration - power), 0), getMaxChargingRate()),750);
    }

    public boolean canPower() {
        return getData().networkResistance > 0 && (getData().getVoltage() <= getOutputVoltage()) && energy.getEnergyStored() > 0 && signal == 0;
    }


    public int getChargingRate() {
        //
        // int chargingRate = Math.max((data.networkPowerGeneration - getNetworkPowerUsage()), 0);
        if (energy.getEnergyStored() >= getMaxCapacity() || getData().getVoltage() < getOutputVoltage() || canPower()|| data.notEnoughtPower)
            return 0;

        //return Math.min(chargingRate, getMaxChargingRate());
        return getMaxChargingRate();
    }

    @Override
    public int powerGeneration() {
        if (canPower()) {
            return getData().networkResistance > 0 ? maxPowerOutput() : 0;
        }
        return 0;
    }

    public int maxPowerOutput() {
        return getOutputVoltage() * TFMGConfigs.common().machines.accumulatorMaxAmpOutput.get();
    }

    public int getMaxCapacity() {
        return TFMGConfigs.common().machines.accumulatorStorage.get()*length;
    }

    //in FE per tick
    public int getMaxChargingRate() {
        return TFMGConfigs.common().machines.accumulatorChargingRate.get();
    }


    public int getOutputVoltage() {


        return TFMGConfigs.common().machines.accumulatorVoltage.get() * length;
    }

    @Override
    public int voltageGeneration() {

        return canPower() ? getOutputVoltage() : 0;
    }


    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction.getAxis() == getBlockState().getValue(FACING).getAxis();
    }
}
