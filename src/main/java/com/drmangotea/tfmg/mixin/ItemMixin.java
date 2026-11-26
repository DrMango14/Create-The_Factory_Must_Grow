package com.drmangotea.tfmg.mixin;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.locale.Language;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Item.class)
public class ItemMixin {

    @ModifyReturnValue(method = "getOrCreateDescriptionId()Ljava/lang/String;", at = @At(value = "RETURN"))
    public String aprilFools(String original) {
        if (TFMGUtils.isAprilFirst()) {
            String key = original + ".fools";
            if (Language.getInstance().has(key)) {
                return key;
            }
        }
        return original;
    }
}
