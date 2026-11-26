package com.drmangotea.tfmg.mixin;

import com.drmangotea.tfmg.base.TFMGTiers;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DeployerBlockEntity.class, remap = false)
public abstract class DeployerBlockEntityMixin extends KineticBlockEntity {
    @Shadow
    protected DeployerFakePlayer player;

    public DeployerBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    /**
     * Handles inventory ticking for the fake player.
     */
    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (level == null || player == null || level.isClientSide) return;

        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item.isEmpty())
                continue;
            if (item.is(TFMGItems.LITHIUM_BLADE)) {
                tfmg$decrementLithiumCharge(inventory, i);
            }
        }
    }

    @Unique
    private void tfmg$decrementLithiumCharge(Inventory inventory, int slot) {
        ItemStack item = inventory.getItem(slot);
        if (item.isEmpty()) return;
        if (item.has(TFMGDataComponents.LITHIUM_BLADE_TIMER)) {
            if (item.getOrDefault(TFMGDataComponents.LITHIUM_BLADE_TIMER, 0) > 0) {
                int toDecrement = Math.max(0, item.getOrDefault(TFMGDataComponents.LITHIUM_BLADE_TIMER, 0) - 1);
                if (item.getOrDefault(TFMGDataComponents.LITHIUM_BLADE_TIMER, 0) - toDecrement > 0) {
                    item.set(DataComponents.ATTRIBUTE_MODIFIERS, AxeItem.createAttributes(TFMGTiers.STEEL, 3, -2.4F));
                }
                item.set(TFMGDataComponents.LITHIUM_BLADE_TIMER, toDecrement);
            } else {
                item.remove(TFMGDataComponents.LITHIUM_BLADE_TIMER);
                item.set(DataComponents.ATTRIBUTE_MODIFIERS, AxeItem.createAttributes(TFMGTiers.STEEL, 2, -2.4F));
            }
            inventory.setItem(slot, item);
        }
    }
}
