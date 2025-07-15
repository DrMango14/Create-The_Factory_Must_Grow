package com.drmangotea.tfmg.content.machinery.vat.base;


import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.platform.NeoForgeCatnipServices;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;


public class VatRenderer extends SafeBlockEntityRenderer<VatBlockEntity> {

    public VatRenderer(BlockEntityRendererProvider.Context context) {
    }


    @Override
    protected void renderSafe(VatBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        if (!be.isController())
            return;

        LerpedFloat[] fluidLevel = be.getFluidLevel();
        if (fluidLevel == null)
            return;
        if (!be.window)
            return;
        float capHeight = 1 / 4f;
        float tankHullWidth = 1 / 16f + 1 / 128f;
        float minPuddleHeight = 1 / 16f;


        ms.pushPose();

        float totalFluidHeight = 0;
        float xMin = tankHullWidth;
        float xMax = xMin + be.width - 2 * tankHullWidth;
        float zMin = tankHullWidth;
        float zMax = zMin + be.width - 2 * tankHullWidth;

        int tankNumber = 0;
        for (int i = 0; i < 8; i++) {

            IFluidHandler fluidHandler = be.fluidCapability;

            if (fluidHandler.getFluidInTank(tankNumber).isEmpty())
                continue;

            float level = fluidLevel[i].getValue(partialTicks);
            float yMin = capHeight + totalFluidHeight;
            float yMax = yMin + (level * (be.height - (2 * capHeight))) / 8;


            NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(fluidHandler.getFluidInTank(tankNumber), xMin, yMin, zMin, xMax, yMax, zMax, bufferSource, ms, light, false, false);
            tankNumber++;
            totalFluidHeight += yMax-yMin;
        }
        ms.popPose();

        //  float level = fluidLevel.getValue(partialTicks);
        //  if (level < 1 / (512f * totalHeight))
        //      return;
//
        //  float clampedLevel = Mth.clamp(level * totalHeight, 0, totalHeight);
//
//
        //  IFluidHandler fluidHandler = be.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
//
//
        //  float xMin = tankHullWidth;
        //  float xMax = xMin + be.width - 2 * tankHullWidth;
        //  float yMin = totalHeight + capHeight + minPuddleHeight - clampedLevel;
        //  float yMax = yMin + clampedLevel;
//
//
        //  float zMin = tankHullWidth;
        //  float zMax = zMin + be.width - 2 * tankHullWidth;
//
        //  ms.pushPose();
        //  ms.translate(0, clampedLevel - totalHeight, 0);
//
        //  for (int i = 0; i < fluidHandler.getTanks(); i++) {
        //      if (!fluidHandler.getFluidInTank(i).isEmpty()) {
        //          //FluidStack stack = fluidHandler.getFluidInTank()
        //          //FluidRenderer.renderFluidBox(fluidHandler.getFluidInTank(i), xMin, yMin, zMin, xMax, yMax, zMax, bufferSource, ms, light, false);
        //          //break;
        //      }
        //  }
        //  ms.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(VatBlockEntity te) {
        return te.isController();
    }

}
