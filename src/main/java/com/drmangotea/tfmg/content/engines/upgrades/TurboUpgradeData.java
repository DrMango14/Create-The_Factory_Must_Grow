package com.drmangotea.tfmg.content.engines.upgrades;

import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.upgrades.base.EngineUpgrade;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class TurboUpgradeData extends EngineUpgrade {
    LerpedFloat speed = LerpedFloat.linear();
    float angle;

    public TurboUpgradeData(ResourceLocation id) {
        super(id);
    }

    public PartialModel getModel() {
        return TFMGPartialModels.TURBO;
    }

    @Override
    public void render(AbstractSmallEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light) {
        if(!Minecraft.getInstance().isPaused()) {
            speed.chase(be.rpm / 100, 1 / 32f, LerpedFloat.Chaser.EXP);
            speed.tickChaser();
            angle += be.rpm / 100;
            angle %= 360;
        }

        BlockState state = be.getBlockState();
        Direction facing = state.getValue(FACING);
        boolean side = false;
        ms.pushPose();
        if (be instanceof RegularEngineBlockEntity blockEntity) {
            side = blockEntity.type.is(TFMGTags.Engines.UPGRADES_ON_SIDE.tag);
        }

        CachedBuffers.partial(getModel(), state)
                .center()
                .rotateYDegrees(facing.toYRot())
                .translateX(side ? -4/16f : 0)
                .rotateZDegrees(side ? 90 : 0)
                .uncenter()
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));

        CachedBuffers.partial(TFMGPartialModels.TURBO_PROPELLER, state)
                .center()
                .rotateYDegrees(facing.toYRot())
                .translateX(side ? -4/16f : 0)
                .rotateZDegrees(side ? 90 : 0)
                .rotateYDegrees(angle)
                .uncenter()
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));

        ms.popPose();
    }

    @Override
    public float getTorqueModifier(AbstractSmallEngineBlockEntity engine) {
        return  1.2f;
    }

    @Override
    public float getSpeedModifier(AbstractSmallEngineBlockEntity engine) {
        return 1.3f;
    }

    @Override
    public float getEfficiencyModifier(AbstractSmallEngineBlockEntity engine) {
        return 0.7f;
    }
}
