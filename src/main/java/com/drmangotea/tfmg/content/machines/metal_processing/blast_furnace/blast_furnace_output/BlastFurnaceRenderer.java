package com.drmangotea.tfmg.content.machines.metal_processing.blast_furnace.blast_furnace_output;



import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class BlastFurnaceRenderer extends SafeBlockEntityRenderer<BlastFurnaceOutputBlockEntity> {

    public BlastFurnaceRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(BlastFurnaceOutputBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

        ms.pushPose();
        TransformStack msr = TransformStack.cast(ms);
        msr.translate(1 / 2f, 0.5, 1 / 2f);

            float coalCokeLevel = be.coalCokeHeight.getValue()/32;

            this.renderNorth(be,partialTicks,ms,buffer,light,overlay,coalCokeLevel);




        ms.popPose();

    }

    protected void renderNorth(BlastFurnaceOutputBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay,float height) {
        BlockState blockState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        //small
        if (be.type == BlastFurnaceOutputBlockEntity.BlastFurnaceType.SMALL) {


            CachedBufferer.partial(TFMGPartialModels.COAL_COKE_DUST_LAYER, blockState)
                    .centre()
                    .translateX(-1)
                    .translateY(height)
                    .light(light)
                    .renderInto(ms, vb);



        }
    }

    @Override
    public boolean shouldRenderOffScreen(BlastFurnaceOutputBlockEntity te) {
        return true;
    }

}
