package com.drmangotea.tfmg.mixin.accessor;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SlidingDoorBlockEntity.class)
public interface SlidingDoorBlockEntityAccessor {
    @Accessor("animation")
    LerpedFloat tfmg$getAnimation();

    @Invoker("shouldRenderSpecial")
    boolean tfmg$shouldRenderSpecial(BlockState state);
}
