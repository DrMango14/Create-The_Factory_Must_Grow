package com.drmangotea.tfmg.content.electricity.lights;



import com.drmangotea.tfmg.base.blocks.WallMountBlock;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.RenderTypes;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;

public class LightBulbRenderer extends SafeBlockEntityRenderer<LightBulbBlockEntity> {



    public LightBulbRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    protected void renderSafe(LightBulbBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if(getLightModel() ==null)
            return;
        BlockState blockState = be.getBlockState();
        ms.pushPose();
        float glow = be.glow.getValue(partialTicks);
        int color =  Math.min(100,(int) (glow/0.2f));
        if(be.glow.getValue()!=0) {

                SuperByteBuffer lightModel =  CachedBuffers.partialFacing(getLightModel(), blockState, blockState.getValue(WallMountBlock.FACING))
                        .light((int) glow * 3 + 40)
                        .color(color, color, (int) (color * 0.8), 255)
                        .disableDiffuse();
                if(be.color == DyeColor.WHITE){
                    lightModel.color(color, color, (int) (color * 0.8), 255);
                }else   lightModel.color(be.color.getTextColor());

                lightModel.renderInto(ms, buffer.getBuffer(RenderTypes.additive()));
        }
        ms.popPose();
    }

    public PartialModel getLightModel(){
        return TFMGPartialModels.LIGHT_BULB;
    }
}