package com.drmangotea.tfmg.content.machinery.vat.compressor;

import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.machinery.vat.base.IVatMachine;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlock;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatOperation;
import com.drmangotea.tfmg.registry.TFMGVatOperations;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class CompressorBlockEntity extends KineticBlockEntity implements IVatMachine {
    public boolean updateVat = false;
    public boolean updateCapability = false;

//    protected IPressureHandler pressureCapability;
//    protected SmartPressureTankBehaviour pressureTank;

    public CompressorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
//        pressureTank = new SmartPressureTankBehaviour(SmartPressureTankBehaviour.OUTPUT, this, 1, 100);
//        refreshCapability();
    }

//    private void refreshCapability() {
//        pressureCapability = getNewPressureCapability();
//        invalidateCapabilities();
//    }
//
//    private IPressureHandler getNewPressureCapability() {
//        return pressureTank.getCapability();
//    }

    @Override
    public void tick() {
        super.tick();
        if (level == null)
            return;
        if (this.updateVat) {
            VatBlock.updateVatState(getBlockState(), level, getBlockPos().relative(Direction.UP));
            this.updateVat = false;
        }
    }

    public CompressorState getState() {
        if (Math.abs(getSpeed()) < TFMGConfigs.common().machines.compressorMinimumRPM.get()){
            return CompressorState.NOT_OPERATIONAL;
        }
        if (getSpeed() > 0){
            return CompressorState.PRESSURIZING;
        }
        return CompressorState.DEPRESSURIZING;
    }

    @Override
    public VatOperation getOperationId() {
        return getSpeed() < 0 ? TFMGVatOperations.DECOMPRESSOR.get() : TFMGVatOperations.COMPRESSOR.get();
    }

    @Override
    public boolean canOperate(VatBlockEntity vat) {
        return getState() != CompressorState.NOT_OPERATIONAL;
    }

    @Override
    public PositionRequirement getPositionRequirement() {
        return PositionRequirement.BOTTOM;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        TFMGTexts.CommonMachines.state("goggles." + getState().getSerializedName()).style(getState().color).forGoggles(tooltip);
        if(getState() == CompressorState.NOT_OPERATIONAL) {
            TFMGTexts.CommonMachines.minRPM(TFMGConfigs.common().machines.compressorMinimumRPM.get()).style(ChatFormatting.RED).forGoggles(tooltip);
        }
        return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }

    public void onSpeedChanged(float previous) {
        super.onSpeedChanged(previous);
        if (getSpeed() != previous) {
            this.updateVat = true;
        }
    }

//    @Override
//    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
//        super.write(compound, registries, clientPacket);
//    }
//
//    @Override
//    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
//        super.read(compound, registries, clientPacket);
//    }

    public enum CompressorState implements StringRepresentable {
        PRESSURIZING(ChatFormatting.YELLOW),
        DEPRESSURIZING(ChatFormatting.AQUA),
        NOT_OPERATIONAL(ChatFormatting.RED);

        public final ChatFormatting color;

        CompressorState(ChatFormatting color) {
            this.color = color;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }

}
