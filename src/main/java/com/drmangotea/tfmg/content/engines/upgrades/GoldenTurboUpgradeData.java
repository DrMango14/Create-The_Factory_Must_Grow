package com.drmangotea.tfmg.content.engines.upgrades;

import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class GoldenTurboUpgradeData extends EngineUpgrade {


    LerpedFloat speed = LerpedFloat.linear();

    float angle;

    public GoldenTurboUpgradeData() {
    }

    @Override
    public Optional<? extends EngineUpgrade> createUpgrade() {
        return Optional.of(new GoldenTurboUpgradeData());
    }


    public PartialModel getModel() {
        return TFMGPartialModels.GOLDEN_TURBO;
    }

    @Override
    public void tickUpgrade(AbstractSmallEngineBlockEntity engine) {
        if (!engine.getLevel().isClientSide)
            return;
        speed.chase(engine.rpm / 200, 1 / 128f, LerpedFloat.Chaser.EXP);
        speed.tickChaser();
    }


    @Override
    public void render(AbstractSmallEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light) {

        angle+=be.rpm/500f;
        angle %= 360;

        BlockState state = be.getBlockState();
        Direction facing = state.getValue(FACING);
        boolean side = false;
        ms.pushPose();
        if (be instanceof RegularEngineBlockEntity blockEntity) {
            side = blockEntity.type.upgradesOnSide;
        }
        CachedBuffers.partial(getModel(), state)
                .center()
                .rotateYDegrees(facing.toYRot())
                .translateX(side ? -4/16f : 0)
                .rotateZDegrees(side ? 90 : 0)
                .uncenter()
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));

        CachedBuffers.partial(TFMGPartialModels.GOLDEN_TURBO_PROPELLER, state)
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
        return 1.4f;
    }

    @Override
    public Item getItem() {
        return TFMGItems.GOLDEN_TURBO.asItem();
    }

    @Override
    public float getSpeedModifier(AbstractSmallEngineBlockEntity engine) {
        return 1.3f;
    }

}
