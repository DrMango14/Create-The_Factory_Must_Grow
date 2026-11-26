package com.drmangotea.tfmg.content.machinery.vat.industrial_mixer;

import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.machinery.vat.base.IVatMachine;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlock;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatOperation;
import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.mode.MixerMode;
import com.drmangotea.tfmg.registry.*;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import net.createmod.catnip.animation.LerpedFloat;
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


public class IndustrialMixerBlockEntity extends KineticBlockEntity implements IVatMachine, Clearable {
    public SmartInventory inventory = new SmartInventory(1, this, 1, false)
            .whenContentsChanged(this::onInventoryChanged);
    public IItemHandlerModifiable itemCapability;

    public MixerMode mixerMode = TFMGMixerModes.NONE.get();
    public int vatSize = 1;
    public int vatHeight = 1;
    public BlockPos vatPos = null;
    public boolean updateVat = false;

    LerpedFloat visualSpeed = LerpedFloat.linear();
    public float angle;

    public IndustrialMixerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        itemCapability = inventory;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                TFMGBlockEntities.INDUSTRIAL_MIXER.get(),
                (be, context) -> be.itemCapability
        );
    }

    public void onInventoryChanged(int slot) {
        sendData();
        setChanged();
        if (inventory.isEmpty()) {
            this.mixerMode = TFMGMixerModes.NONE.get();
            this.updateVat = true;
            return;
        }
        ItemStack itemStack = inventory.getItem(0);
        this.mixerMode = itemStack.getOrDefault(TFMGDataComponents.MIXER_MODE, MixerMode.Stored.NONE).mode().value();
        this.updateVat = true;
    }

    @Override
    public void vatUpdated(VatBlockEntity be) {
        vatSize = be.getWidth();
        vatHeight = be.getHeight();
        vatPos = be.getBlockPos();
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null || !level.isClientSide)
            return;
        float targetSpeed = getSpeed();
        visualSpeed.updateChaseTarget(targetSpeed);
        visualSpeed.tickChaser();
        if (this.updateVat) {
            VatBlock.updateVatState(getBlockState(), level, getBlockPos().relative(Direction.DOWN));
            this.updateVat = false;
        }
    }

    public void onSpeedChanged(float previous) {
        super.onSpeedChanged(previous);
        if (getSpeed() != previous) {
            this.updateVat = true;
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean operational = Math.abs(getSpeed()) >= TFMGConfigs.common().machines.industrialMixerMinimumRPM.get();
        boolean needsAttachment = !mixerMode.isValid();
        String state = operational && !needsAttachment ? "goggles.operational" : "goggles.not_operational";
        TFMGTexts.CommonMachines.state(state).style(operational && !needsAttachment ? ChatFormatting.GREEN : ChatFormatting.RED).forGoggles(tooltip);
        if(!operational) {
            TFMGTexts.CommonMachines.minRPM(TFMGConfigs.common().machines.industrialMixerMinimumRPM.get()).style(ChatFormatting.RED).forGoggles(tooltip);
        }
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        return true;
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.put("Inventory", inventory.serializeNBT(registries));
        TFMGRegistries.MIXER_MODE_REGISTRY.byNameCodec().encodeStart(NbtOps.INSTANCE, mixerMode).ifSuccess(nbt -> compound.put("Mode", nbt));
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        if (clientPacket)
            visualSpeed.chase(getGeneratedSpeed(), (double) 1 / 32, LerpedFloat.Chaser.EXP);
        super.read(compound, registries, clientPacket);
        inventory.deserializeNBT(registries,compound.getCompound("Inventory"));
        remapMode(compound);
        TFMGRegistries.MIXER_MODE_REGISTRY.byNameCodec().parse(NbtOps.INSTANCE, compound.get("Mode")).ifSuccess(mode -> mixerMode = mode);
    }

    private void remapMode(CompoundTag compound) {
        if (compound.contains("MixerMode")) {
            ItemStack remapped = switch (compound.getString("MixerMode")) {
                case "mixing" -> TFMGItems.MIXER_BLADE.asStack();
                case "centrifuge" -> TFMGItems.CENTRIFUGE.asStack();
                default -> ItemStack.EMPTY;
            };
            if (inventory.isEmpty() && !remapped.isEmpty()) {
                inventory.insertItem(0, remapped, false);
                sendData();
            }
            compound.remove("MixerMode");
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inventory);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(getBlockPos()).inflate(3);
    }

    @Override
    public VatOperation getOperationId() {
        return mixerMode.getOperationId().get();
    }

    @Override
    public boolean canOperate(VatBlockEntity vat) {
        return Math.abs(getSpeed()) >= TFMGConfigs.common().machines.industrialMixerMinimumRPM.get();
    }

    @Override
    public int getWorkPercentage() {
        return (int) ((getSpeed() / 255f) * 100);
    }

    @Override
    public PositionRequirement getPositionRequirement() {
        return PositionRequirement.TOP_CENTER;
    }

    @Override
    public List<VatOperation> doesntWorkWith() {
        return List.of(TFMGVatOperations.ELECTRODE.get(), TFMGVatOperations.GRAPHITE_ELECTRODE.get());
    }

    @Override
    public void clearContent() {
        this.inventory.clearContent();
    }
}
