package com.drmangotea.tfmg.content.machinery.vat.electrode_holder;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.base.IVatMachine;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlock;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.Electrode;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ElectrodeHolderBlockEntity extends ElectricBlockEntity implements IVatMachine {

    Electrode electrode = TFMGUtils.getElectrode(TFMG.asResource("none"));
    boolean isTallEnough = true;

    public ElectrodeHolderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == Direction.UP;
    }


    public boolean setElectrode(ItemStack modeItem, boolean simulate) {
        if (level == null) return false;
        for (Electrode electrode : TFMGRegistries.ELECTRODE_REGISTRY.stream().toList()) {
            if (electrode.getStack().isEmpty()) continue;
            if (modeItem.is(electrode.getStack().getItem())) {
                if (!simulate) {
                    this.electrode = electrode;
                } else return true;
            }
        }
        if (!simulate && hasLevel())
            VatBlock.updateVatState(getBlockState(), getLevel(), getBlockPos().relative(Direction.DOWN));
        sendData();
        return false;
    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        super.makeMultimeterTooltip(tooltip, isPlayerSneaking);
        if (getCurrent() < TFMGConfigs.common().machines.electrolysisMinimumCurrent.get())
            CreateLang.translate("goggles.electrode_holder.min_amps")
                    .style(ChatFormatting.RED)
                    .add(CreateLang.text(TFMGConfigs.common().machines.electrolysisMinimumCurrent.get() + "A)"))
                    .forGoggles(tooltip);

        return true;
    }

    @Override
    public float resistance() {
        return this.electrode.getResistance();
    }

    @Override
    public boolean canBeInGroups() {
        return true;
    }

    public boolean setElectrode(Electrode electrode, boolean simulate) {
        if (electrode != null) {
            if (!simulate) {
                this.electrode = electrode;
            } else return true;
        }
        if (!simulate && hasLevel())
            VatBlock.updateVatState(getBlockState(), getLevel(), getBlockPos().relative(Direction.DOWN));
        sendData();
        return false;
    }

    @Override
    public void onNetworkChanged(int oldVoltage, int oldPower) {
        super.onNetworkChanged(oldVoltage, oldPower);
        VatBlock.updateVatState(getBlockState(), level, getBlockPos().relative(Direction.DOWN));
    }


    boolean isOperational() {
        return getCurrent() >= TFMGConfigs.common().machines.electrolysisMinimumCurrent.get() && canWork();
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos()).setMinY(getBlockPos().getY() - 2);
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putString("Electrode", electrode.getKey().toString());

        super.write(compound,registries , clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);

        setElectrode(TFMGUtils.getElectrode(ResourceLocation.parse(compound.getString("Electrode"))), false);

    }

    @Override
    public String getOperationId() {
        return isOperational() ? electrode.getOperationId() : "";
    }

    @Override
    public int getWorkPercentage() {
        return (getPowerUsage() / 5000) * 100;
    }

    @Override
    public void vatUpdated(VatBlockEntity be) {
        IVatMachine.super.vatUpdated(be);
    }


}
