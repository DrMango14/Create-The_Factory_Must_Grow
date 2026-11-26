package com.drmangotea.tfmg.content.machinery.vat.compressor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.level.block.state.BlockState;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class CompressorRenderer extends KineticBlockEntityRenderer<CompressorBlockEntity> {
    public CompressorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(CompressorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

        BlockState blockState = be.getBlockState();
        ItemRenderer itemRenderer = Minecraft.getInstance()
                .getItemRenderer();

        VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());



        super.renderSafe(be, partialTicks, ms, bufferSource, 9999999, overlay);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(CompressorBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, state, state
                .getValue(HORIZONTAL_FACING));
    }


}
