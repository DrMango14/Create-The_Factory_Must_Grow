package com.drmangotea.tfmg.content.machinery.vat.base;

import com.drmangotea.tfmg.mixin.accessor.TankSegmentAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.platform.NeoForgeCatnipServices;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;

public class VatRenderer extends SafeBlockEntityRenderer<VatBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public VatRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }


    @Override
    protected void renderSafe(VatBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        if (!be.isController()) return;
        FilteringRenderer.renderOnBlockEntity(be, partialTicks, ms, bufferSource, light, overlay);

        float fluidLevel = renderFluids(be, partialTicks, ms, bufferSource, light);
        renderItems(be, partialTicks, ms, bufferSource, light, overlay, fluidLevel);
    }

    private void renderItems(VatBlockEntity vat, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, float fluidLevel) {
        if (vat.getLevel() == null) return;
        VatInventory[] inventories = { vat.inputInventory, vat.outputInventory };
        ItemRenderer itemRenderer = context.getItemRenderer();

        float capHeight = 1 / 4f;
        float tankHullWidth = 1 / 16f + 1 / 128f;
        float maxRadius = Math.max(.08f, (vat.width - 2 * tankHullWidth) * .5f - .35f);

        // It was so odd having it spin when paused
        if (!Minecraft.getInstance().isPaused())
            vat.spinningAngle = (vat.spinningAngle + vat.visualSpeed.getValue(partialTicks)) % 360;

        ms.pushPose();
        ms.translate(vat.width * .5f, 0, vat.width * .5f);
        TransformStack.of(ms).rotateYDegrees(vat.spinningAngle);

        RandomSource r = RandomSource.create(vat.getBlockPos().asLong());

        int inventoryIndex = 0;
        int totalItems = totalRenderedItems(vat);
        float anglePartition = 360f / totalItems;
        int index = 0;

        for (VatInventory inv : inventories) {
            float startAngle = 90f * inventoryIndex;

            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack.isEmpty())
                    continue;

                float levelY = Math.max(capHeight, fluidLevel - .3f);

                ms.pushPose();
                ms.translate(0, .2f + levelY, 0);

                if (fluidLevel > 0) {
                    ms.translate(0, (Mth.sin(AnimationTickHolder.getRenderTime(vat.getLevel()) / 12f + anglePartition * index) + 1.5f) * 1 / 32f, 0);
                }

                // Prevents items from poking out the top
                Vec3 baseVector = new Vec3(itemRadius(maxRadius, totalItems, index), -0.2f, 0);

                Vec3 itemPosition = VecHelper.rotate(baseVector, startAngle + anglePartition * index, Axis.Y);
                ms.translate(itemPosition.x, itemPosition.y, itemPosition.z);
                TransformStack.of(ms)
                        .rotateYDegrees(startAngle + anglePartition * index + 35)
                        .rotateXDegrees(65);

                for (int k = 0; k <= stack.getCount() / 8; k++) {
                    ms.pushPose();
                    Vec3 vec = VecHelper.offsetRandomly(Vec3.ZERO, r, 1 / 16f);
                    ms.translate(vec.x, vec.y, vec.z);
                    // Maybe provide a level?
                    BakedModel model = itemRenderer.getModel(stack, null, null, 0);
                    itemRenderer.render(stack, ItemDisplayContext.GROUND, false, ms, buffer, light, overlay, model);
                    ms.popPose();
                }
                ms.popPose();

                index++;
            }
            inventoryIndex++;
        }
        ms.popPose();
    }

    private float itemRadius(float maxRadius, int totalItems, int index) {
        return totalItems <= 1 ? maxRadius * .55f : maxRadius * (0.5f + 0.5f * (index / (float) (totalItems - 1)));
    }

    private int totalRenderedItems(VatBlockEntity vat) {
        int count = 0;
        for (VatInventory inv : new VatInventory[] { vat.inputInventory, vat.outputInventory })
            for (int i = 0; i < inv.getSlots(); i++)
                if (!inv.getStackInSlot(i).isEmpty())
                    count++;
        return Math.max(1, count);
    }

    protected float renderFluids(VatBlockEntity vat, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light) {
        Couple<SmartFluidTankBehaviour> tanks = vat.getTanks();

		float totalUnits = 0;
		int renderedFluids = 0;
		int totalCapacity = 0;
		
		for (SmartFluidTankBehaviour behaviour : tanks) {
			if (behaviour == null)
				continue;
			for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
				totalCapacity += ((TankSegmentAccessor) tankSegment).tfmg$tank().getCapacity();
				if (tankSegment.getRenderedFluid().isEmpty())
					continue;
				float units = tankSegment.getTotalUnits(partialTicks);
				if (units < 1)
					continue;
				totalUnits += units;
				renderedFluids++;
			}
		}

		if (renderedFluids == 0 || totalUnits < 1)
            return 0;

        float fluidLevel = Mth.clamp(totalUnits / totalCapacity, 0, 1);
		
        fluidLevel *= 2 - fluidLevel;

        float capHeight = (1 / 4f);
        float tankHullWidth = 1 / 16f + 1 / 128f;

        float xzMax = tankHullWidth + vat.width - 2 * tankHullWidth;

        float level = 0;
        float surfaceY = capHeight;

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
                // The - 0.1f is to prevent fluid z-fighting
                float yMax = Math.min(yMin + (fluidLevel * (vat.height - (2 * capHeight))), vat.height - 0.1f);

                NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(renderedFluid, tankHullWidth, yMin, tankHullWidth, xzMax, yMax, xzMax, buffer, ms, light, false, false);

                level += yMax - yMin;
                surfaceY = yMax;
            }
        }

        return surfaceY;
    }

    @Override
    public boolean shouldRenderOffScreen(VatBlockEntity te) {
        return te.isController();
    }

}
