package com.drmangotea.tfmg.mixin.accessor;

import com.simibubi.create.content.kinetics.flywheel.FlywheelBlockEntity;
import net.createmod.catnip.animation.LerpedFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FlywheelBlockEntity.class)
public interface FlywheelBlockEntityMixin {
    @Accessor("angle")
    float tfmg$angle();

    @Accessor("visualSpeed")
    LerpedFloat tfmg$visualSpeed();
}
