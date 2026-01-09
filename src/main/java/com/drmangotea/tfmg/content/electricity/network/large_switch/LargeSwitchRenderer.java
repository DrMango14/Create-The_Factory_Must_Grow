package com.drmangotea.tfmg.content.electricity.network.large_switch;

import com.drmangotea.tfmg.content.machinery.misc.winding_machine.WindingMachineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

import static com.drmangotea.tfmg.content.electricity.network.large_switch.LargeSwitchBlock.IS_MAIN_PART;
import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class LargeSwitchRenderer extends KineticBlockEntityRenderer<LargeSwitchBlockEntity> {
    public LargeSwitchRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(LargeSwitchBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

        if(!be.getBlockState().getValue(IS_MAIN_PART))
            return;

        BlockState blockState = be.getBlockState();

        VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());

        CachedBuffers.partial(TFMGPartialModels.LARGE_SWITCH_ARM, blockState)
                .light(light)
                .center()
                .rotateYDegrees(blockState.getValue(HORIZONTAL_FACING).getAxis() == Direction.Axis.Z ? Math.abs(blockState.getValue(FACING).toYRot() ) : blockState.getValue(FACING).toYRot()- 180)
                .translateY(8/16f)
                .rotateXDegrees(-be.visualAngle.getValue(partialTicks))
                .uncenter()
                .renderInto(ms, vb);


        super.renderSafe(be, partialTicks, ms, bufferSource, light, overlay);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(LargeSwitchBlockEntity be, BlockState state) {

        if(!state.getValue(IS_MAIN_PART))
            return CachedBuffers.partialFacing(TFMGPartialModels.LARGE_SWITCH_BUFFER, state, state
                    .getValue(FACING).getCounterClockWise());

        return CachedBuffers.partialFacing(TFMGPartialModels.LARGE_SWITCH_SHAFT, state, state
                .getValue(FACING).getCounterClockWise());
    }


}
