package com.drmangotea.tfmg.mixin;

import com.drmangotea.tfmg.remap.TFMGRemapper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void tfmg$remapComponents(Level level, Entity entity, int inventorySlot, boolean isCurrentItem, CallbackInfo ci) {
        if (level.isClientSide()) {
            return;
        }

        ItemStack stack = (ItemStack) (Object) this;

        if (stack.isEmpty()) {
            return;
        }

        TFMGRemapper.remapComponents(stack, level.registryAccess());
    }
}

