package com.drmangotea.tfmg.content.electricity.utilities.fuse_block;

import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class FuseBlockRenderer extends SafeBlockEntityRenderer<FuseBlockEntity> {

    public FuseBlockRenderer(BlockEntityRendererProvider.Context context){}

    @Override
    protected void renderSafe(FuseBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

        BlockState blockState = be.getBlockState();

        if(!be.fuse.isEmpty())
            CachedBuffers.partialFacing(TFMGPartialModels.FUSE, blockState, blockState.getValue(FACING).getOpposite())
                    .light(light)
                    .renderInto(ms, bufferSource.getBuffer(RenderType.cutoutMipped()));


    }
}
