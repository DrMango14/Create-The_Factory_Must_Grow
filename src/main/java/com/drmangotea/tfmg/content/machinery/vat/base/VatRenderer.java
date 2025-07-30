package com.drmangotea.tfmg.content.machinery.vat.base;


import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.platform.NeoForgeCatnipServices;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;


public class VatRenderer extends SafeBlockEntityRenderer<VatBlockEntity> {

    public VatRenderer(BlockEntityRendererProvider.Context context) {
    }


    @Override
    protected void renderSafe(VatBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        if (!be.isController())
            return;

        float fluidLevel = renderFluids(be, partialTicks, ms, bufferSource, light, overlay);

    }

    protected float renderFluids(VatBlockEntity vat, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        SmartFluidTankBehaviour inputFluids = vat.getBehaviour(SmartFluidTankBehaviour.INPUT);
        SmartFluidTankBehaviour outputFluids = vat.getBehaviour(SmartFluidTankBehaviour.OUTPUT);
        SmartFluidTankBehaviour[] tanks = { inputFluids, outputFluids };

        float totalUnits = vat.getTotalFluidUnits(partialTicks);
        if (totalUnits < 1)
            return 0;

        float fluidLevel = Mth.clamp(totalUnits / vat.getTotalCapacity(), 0, 1);

        fluidLevel = 1 - ((1 - fluidLevel) * (1 - fluidLevel));

        float capHeight = 1 / 4f;
        float tankHullWidth = 1 / 16f + 1 / 128f;

        float xMin = tankHullWidth;
        float xMax = xMin + vat.width - 2 * tankHullWidth;
        float zMin = tankHullWidth;
        float zMax = zMin + vat.width - 2 * tankHullWidth;

        float level = 0;

        for (SmartFluidTankBehaviour behaviour : tanks) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                FluidStack renderedFluid = tankSegment.getRenderedFluid();
                if (renderedFluid.isEmpty())
                    continue;
                float units = tankSegment.getTotalUnits(partialTicks);
                if (units < 1)
                    continue;
                float yMin = capHeight + level;
                float yMax = yMin + (fluidLevel * (vat.height - (2 * capHeight))) / 5;

                NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(renderedFluid, xMin, yMin, zMin, xMax, yMax, zMax,
                        buffer, ms, light, false, false);

                level += yMax - yMin;
            }
        }

        return level;
    }

    @Override
    public boolean shouldRenderOffScreen(VatBlockEntity te) {
        return te.isController();
    }

}
