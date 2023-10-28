package com.drmangotea.createindustry.blocks.concrete.formwork;


import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;


import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;

import com.simibubi.create.foundation.utility.animation.LerpedFloat;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FormWorkRenderer extends SafeBlockEntityRenderer<FormWorkBlockEntity> {

    public FormWorkRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(FormWorkBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

        renderConcreteInside(be,partialTicks,ms,buffer,light,overlay);

        BlockState blockState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        ms.pushPose();
        TransformStack msr = TransformStack.cast(ms);
        msr.translate(1 / 2f, 0.5, 1 / 2f);

        if(be.north) {
            CachedBufferer.partial(TFMGPartialModels.FORMWORK_SIDE, blockState)
                    .unCentre()
                    .light(light)
                    .renderInto(ms, vb);
        }
        if(be.east) {
            CachedBufferer.partial(TFMGPartialModels.FORMWORK_SIDE, blockState)
                    .rotateY(270)
                    .unCentre()
                    .light(light)
                    .renderInto(ms, vb);
        }
        if(be.south) {
            CachedBufferer.partial(TFMGPartialModels.FORMWORK_SIDE, blockState)
                    .rotateY(180)
                    .unCentre()
                    .light(light)
                    .renderInto(ms, vb);
        }

        if(be.west) {
            CachedBufferer.partial(TFMGPartialModels.FORMWORK_SIDE, blockState)
                    .rotateY(90)
                    .unCentre()
                    .light(light)
                    .renderInto(ms, vb);
        }
        ///
        if(be.bottom) {
            CachedBufferer.partial(TFMGPartialModels.FORMWORK_BOTTOM, blockState)
                    .unCentre()
                    .light(light)
                    .renderInto(ms, vb);

        }


        ms.popPose();

    }


    protected void renderConcreteInside(FormWorkBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        LerpedFloat fluidLevel = be.getFluidLevel();

          if (fluidLevel == null) {
              return;
          }


        FluidTank tank = be.tankInventory;
        FluidStack fluidStack = tank.getFluid();

        if (fluidStack.isEmpty())
            return;



        if(be.getLevel().getBlockEntity(be.getBlockPos().below())instanceof FormWorkBlockEntity){
            if(((FormWorkBlockEntity) be.getLevel().getBlockEntity(be.getBlockPos().below())).tankInventory.getFluidAmount()<1000){
                be.fluidLevel.setValue(0);
                return;
            }

        }



        ms.pushPose();
        //  ms.translate(0, clampedLevel - 1, 0);
        FluidRenderer.renderFluidBox(fluidStack, 0, 0, 0, 1, be.getFluidLevel().getValue(partialTicks)/1000, 1, buffer, ms, light, false);
        ms.popPose();


    }





    @Override
    public boolean shouldRenderOffScreen(FormWorkBlockEntity te) {
        return false;
    }

}