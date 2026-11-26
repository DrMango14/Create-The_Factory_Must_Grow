package com.drmangotea.tfmg.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

@Mixin(Util.class)
public class UtilMixin {
    @Shadow
    private static Consumer<String> thePauser;

    @Shadow
    static final Logger LOGGER = LogUtils.getLogger();

    /**
     * @author DrMangoTea
     * @reason Remove log spam
     */
    @Overwrite
    public static void logAndPauseIfInIde(String error) {
        if(error.contains("Detected setBlock in a far chunk"))
            return;
        LOGGER.error(error);
        if (SharedConstants.IS_RUNNING_WITH_JDWP) {
            doPause(error);
        }
    }

    @Shadow
    private static void doPause(String message) {
        Instant instant = Instant.now();
        LOGGER.warn("Did you remember to set a breakpoint here?");
        boolean flag = Duration.between(instant, Instant.now()).toMillis() > 500L;
        if (!flag) {
            thePauser.accept(message);
        }
    }
}
