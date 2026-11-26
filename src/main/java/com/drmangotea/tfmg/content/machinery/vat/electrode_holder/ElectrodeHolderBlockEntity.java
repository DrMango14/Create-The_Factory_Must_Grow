package com.drmangotea.tfmg.content.machinery.vat.electrode_holder;

import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.content.devices.base.ResistiveLoadBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.base.IVatMachine;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlock;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatOperation;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.Electrode;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGElectrodes;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import java.util.List;

public class ElectrodeHolderBlockEntity extends ResistiveLoadBlockEntity implements IVatMachine, Clearable {
    public SmartInventory inventory = new SmartInventory(1, this, 1, false)
            .whenContentsChanged(this::onInventoryChanged);
    public IItemHandlerModifiable itemCapability;

    Electrode electrode = TFMGElectrodes.NONE.get();
    public boolean updateVat = false;

    public ElectrodeHolderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        itemCapability = inventory;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                TFMGBlockEntities.ELECTRODE_HOLDER.get(),
                (be, context) -> be.itemCapability
        );
    }

    public void onInventoryChanged(int slot) {
        sendData();
        setChanged();
        if (inventory.isEmpty()) {
            this.electrode = TFMGElectrodes.NONE.get();
            this.updateVat = true;
            return;
        }
        ItemStack itemStack = inventory.getItem(0);
        this.electrode = itemStack.getOrDefault(TFMGDataComponents.ELECTRODE, Electrode.Stored.NONE).electrode().value();
        this.updateVat = true;
    }

	@Override
    public double resistance() {
        return 20;
    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean operational = current >= TFMGConfigs.common().machines.electrolysisMinimumCurrent.get();
        TFMGTexts.CommonMachines.state("goggles." + (operational ? "operational" : "not_operational")).style(operational ? ChatFormatting.GREEN : ChatFormatting.RED).forGoggles(tooltip);
        if (!operational)
            TFMGTexts.Multimeter.notEnoughCurrent(TFMGConfigs.common().machines.electrolysisMinimumCurrent.get()).forGoggles(tooltip);
        super.makeMultimeterTooltip(tooltip, isPlayerSneaking);
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null) return;
        var vatBE = level.getBlockEntity(getBlockPos().relative(Direction.DOWN));
        if (vatBE instanceof VatBlockEntity vat) {
            BlockPos electrodePos = getBlockPos().relative(Direction.DOWN);
            this.electrode.tick(vat.getControllerBE(), this.level, electrodePos, isOperational(), this.level.isClientSide());
        }
        if (this.updateVat) {
            VatBlock.updateVatState(getBlockState(), level, getBlockPos().relative(Direction.DOWN));
            this.updateVat = false;
        }
    }



    public void setElectrode(Electrode electrode) {
        if (electrode != null) {
			this.electrode = electrode;
		}
        if (hasLevel())
            VatBlock.updateVatState(getBlockState(), getLevel(), getBlockPos().relative(Direction.DOWN));
        sendData();
	}


    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inventory);
    }



    boolean isOperational() {
        return current >= TFMGConfigs.common().machines.electrolysisMinimumCurrent.get();
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos()).setMinY(getBlockPos().getY() - 2);
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);
        compound.put("Inventory", inventory.serializeNBT(registries));
        TFMGRegistries.ELECTRODE_REGISTRY.byNameCodec().encodeStart(NbtOps.INSTANCE, electrode).ifSuccess(nbt -> compound.put("Electrode", nbt));
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        inventory.deserializeNBT(registries,compound.getCompound("Inventory"));
        TFMGRegistries.ELECTRODE_REGISTRY.byNameCodec().parse(NbtOps.INSTANCE, compound.get("Electrode")).ifSuccess(electrode -> this.electrode = electrode);
    }

    @Override
    public VatOperation getOperationId() {
        return electrode.getOperationId().get();
    }

    @Override
    public boolean canOperate(VatBlockEntity vat) {
        return isOperational();
    }

    @Override
    public int getWorkPercentage() {
        return (int) ((current*voltage / 5000) * 100);
    }

    @Override
    public void vatUpdated(VatBlockEntity be) {
        IVatMachine.super.vatUpdated(be);
    }

    @Override
    public void clearContent() {
        this.inventory.clearContent();
    }
}
