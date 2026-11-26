package com.drmangotea.tfmg.content.electricity.content.devices.polarizer;

import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.DirectionalBlock.FACING;

public class PolarizerRenderer extends SafeBlockEntityRenderer<PolarizerBlockEntity> {

	public PolarizerRenderer(BlockEntityRendererProvider.Context context) {}

	@Override
	protected void renderSafe(PolarizerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		BlockState blockState = be.getBlockState();
		VertexConsumer vb = buffer.getBuffer(RenderType.solid());
		CachedBuffers.partial(TFMGPartialModels.POLARIZER_DIAL, blockState)
				.center()
				.rotateYDegrees(blockState.getValue(FACING).getAxis() == Direction.Axis.Z
						? Math.abs(blockState.getValue(FACING).toYRot() - 180)
						: blockState.getValue(FACING).toYRot())
				.translateY(-0.025f)
				.rotateZDegrees(be.angle.getValue(partialTicks))
				.translateX(0.05f)
				.uncenter()
				.renderInto(ms, vb);

		ItemStack inventoryItem = be.inventory.getItem(0);
		ItemStack outputItem = be.getOutputItem();

		if (inventoryItem.isEmpty() && outputItem.isEmpty()) {
			return;
		}

		ItemStack toRender = inventoryItem.isEmpty() ? outputItem : inventoryItem;

		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		boolean blockItem = itemRenderer.getModel(toRender, be.getLevel(), null, 0).isGui3d();

		ms.pushPose();

		TransformStack.of(ms)
				.center()
				.rotateYDegrees(blockState.getValue(DirectionalBlock.FACING).getAxis() == Direction.Axis.X ? 90 : 0)
				.translate(0, 0.4, 0)
				.rotateXDegrees(90)
				.scale(blockItem ? .5f : .375f);

		itemRenderer.renderStatic(toRender, ItemDisplayContext.FIXED, light, overlay, ms, buffer, be.getLevel(), 0);

		ms.popPose();
	}
}