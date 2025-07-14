package com.drmangotea.tfmg.content.decoration.cogs;

import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogwheelBlock;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TFMGEncasedCogRenderer extends KineticBlockEntityRenderer<SimpleKineticBlockEntity> {

    private boolean large;
    private boolean steel;

    public static TFMGEncasedCogRenderer steelSmall(BlockEntityRendererProvider.Context context) {
        return new TFMGEncasedCogRenderer(context, false,true);
    }

    public static TFMGEncasedCogRenderer steellarge(BlockEntityRendererProvider.Context context) {
        return new TFMGEncasedCogRenderer(context, true,true);
    }
    public static TFMGEncasedCogRenderer aluminumSmall(BlockEntityRendererProvider.Context context) {
        return new TFMGEncasedCogRenderer(context, false,false);
    }

    public static TFMGEncasedCogRenderer aluminumlarge(BlockEntityRendererProvider.Context context) {
        return new TFMGEncasedCogRenderer(context, true,false);
    }

    public TFMGEncasedCogRenderer(BlockEntityRendererProvider.Context context, boolean large, boolean steel) {
        super(context);
        this.large = large;
        this.steel = steel;
    }

    @Override
    protected void renderSafe(SimpleKineticBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        if (VisualizationManager.supportsVisualization(be.getLevel()))
            return;

        BlockState blockState = be.getBlockState();
        Block block = blockState.getBlock();
        if (!(block instanceof IRotate def))
            return;

        Direction.Axis axis = getRotationAxisOf(be);
        BlockPos pos = be.getBlockPos();
        float angle = large ? BracketedKineticBlockEntityRenderer.getAngleForLargeCogShaft(be, axis)
                : getAngleForBe(be, pos, axis);

        for (Direction d : Iterate.directionsInAxis(getRotationAxisOf(be))) {
            if (!def.hasShaftTowards(be.getLevel(), be.getBlockPos(), blockState, d))
                continue;
            SuperByteBuffer shaft = CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, be.getBlockState(), d);
            kineticRotationTransform(shaft, be, axis, angle, light);
            shaft.renderInto(ms, buffer.getBuffer(RenderType.solid()));
        }
    }

    @Override
    protected SuperByteBuffer getRotatedModel(SimpleKineticBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacingVertical(
                large ? steel? TFMGPartialModels.SHAFTLESS_LARGE_STEEL_COGHWEEL :TFMGPartialModels.SHAFTLESS_LARGE_ALUMINUM_COGHWEEL : steel ? TFMGPartialModels.SHAFTLESS_STEEL_COGHWEEL : TFMGPartialModels.SHAFTLESS_ALUMINUM_COGHWEEL, state,
                Direction.fromAxisAndDirection(state.getValue(EncasedCogwheelBlock.AXIS), Direction.AxisDirection.POSITIVE));
    }



}
