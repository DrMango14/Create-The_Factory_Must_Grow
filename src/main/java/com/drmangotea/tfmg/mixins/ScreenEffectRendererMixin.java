package com.drmangotea.tfmg.mixins;

import com.drmangotea.tfmg.CreateTFMG;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.client.renderer.ScreenEffectRenderer.renderFluid;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    private static final ResourceLocation MOLTEN_METAL_OVERLAY = CreateTFMG.asResource("textures/gui/molten_metal.png");



    @Inject(method = "renderScreenEffect", at = @At("HEAD"))
    private static void renderScreenEffect(Minecraft p_110719_, PoseStack p_110720_, CallbackInfo ci) {



        if (!p_110719_.player.isSpectator()) {
            if (p_110719_.player.level().getBlockState(p_110719_.player.getOnPos().above(2)).is(TFMGBlocks.MOLTEN_METAL.get())) {
                    renderMoltenMetal(p_110719_, p_110720_);
            }
        }

    }
    private static void renderMoltenMetal(Minecraft p_110726_, PoseStack p_110727_) {

        for(int i = 0 ; i<10; i++)
            renderFluid(p_110726_, p_110727_, MOLTEN_METAL_OVERLAY);
    }

}
