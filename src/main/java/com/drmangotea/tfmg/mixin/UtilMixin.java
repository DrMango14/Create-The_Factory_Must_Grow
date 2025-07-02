package com.drmangotea.tfmg.mixin;


import net.minecraft.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Util.class)
public class UtilMixin {


    @Inject(at = @At("HEAD"), method = "logAndPauseIfInIde(Ljava/lang/String;)V", cancellable = true, remap = true)
    private static void logAndPauseIfInIde(String message, CallbackInfo ci) {
        if (message.contains("tfmg"))
            ci.cancel();

    }
}
