package com.drmangotea.tfmg.content.machinery.vat.industrial_mixer;

import com.drmangotea.tfmg.registry.TFMGPartialModels;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class IndustrialMixerRenderer extends KineticBlockEntityRenderer<IndustrialMixerBlockEntity> {


    public IndustrialMixerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(IndustrialMixerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        BlockState blockState = be.getBlockState();
        int height = be.vatHeight;

        if (be.mixerMode == IndustrialMixerBlockEntity.MixerMode.NONE)
            return;
        if (!Minecraft.getInstance().isPaused()) {
            be.angle += be.visualSpeed.getValue(partialTicks) * 3 / 10f;
            be.angle %= 360;
        }

        for (int i = 0; i < height; i++) {
            PartialModel model = i == height - 1 ? be.vatSize > 1 ? TFMGPartialModels.MIXER : TFMGPartialModels.SMALL_MIXER : TFMGPartialModels.MIXER_SHAFT;
            if (be.mixerMode == IndustrialMixerBlockEntity.MixerMode.CENTRIFUGE) {

                if(be.vatPos==null)
                    return;

                model = getCentrifugeModel(i + 1, height, be);
            }

            float posX = be.vatSize == 2 ? (be.vatPos.getX() - be.getBlockPos().getX() + 0.5f) : 0f;
            float posZ = be.vatSize == 2 ? (be.vatPos.getZ() - be.getBlockPos().getZ() + 0.5f) : 0f;
            CachedBuffers.partial(model, blockState)
                    .light(LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().below()))
                    .center()
                    .translate(posX, -i - 1, posZ)
                    .rotateYDegrees(be.angle)
                    .uncenter()
                    .renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
        }
    }

    public PartialModel getCentrifugeModel(int i, int height, IndustrialMixerBlockEntity be) {
        if (be.vatSize == 1) {
            if (height == 1)
                return TFMGPartialModels.SMALL_CENTRIFUGE_ALONE;

            if (i == 1)
                return TFMGPartialModels.SMALL_CENTRIFUGE_TOP;
            if (i == height)
                return TFMGPartialModels.SMALL_CENTRIFUGE_BOTTOM;
            return TFMGPartialModels.SMALL_CENTRIFUGE_MIDDLE;

        } else {
            if (height == 1)
                return TFMGPartialModels.LARGE_CENTRIFUGE_ALONE;

            if (i == 1)
                return TFMGPartialModels.LARGE_CENTRIFUGE_TOP;
            if (i == height)
                return TFMGPartialModels.LARGE_CENTRIFUGE_BOTTOM;
            return TFMGPartialModels.LARGE_CENTRIFUGE_MIDDLE;

        }

    }

    @Override
    protected SuperByteBuffer getRotatedModel(IndustrialMixerBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, state, Direction.UP);
    }
}

