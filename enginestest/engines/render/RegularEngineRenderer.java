package com.drmangotea.tfmg.content.engines.render;

import com.drmangotea.tfmg.content.engines.EngineBlockEntity;
import com.drmangotea.tfmg.content.engines.PistonPosition;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class RegularEngineRenderer extends EngineRenderer {
    public RegularEngineRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(EngineBlockEntity be1, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        EngineBlockEntity be = (EngineBlockEntity) be1;

        BlockState blockState = be.getBlockState();

        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

        for (int i = 0; i < be.engineType.pistons.size(); i++) {
            PistonPosition position = be.engineType.pistons.get(i);

            if (be.engineType.pistons.size() == be.cylinderInventory.getSlots()) {
                ms.pushPose();
                if (i < be.cylinderInventory.getSlots())
                    if (!be.cylinderInventory.getStackInSlot(i).isEmpty())
                        CachedBuffers.partial(getCylinderModel(be), blockState)
                                .center()
                                .light(light)
                                .rotateYDegrees(blockState.getValue(HORIZONTAL_FACING).getAxis() == Direction.Axis.Z ? 0 : 90)
                                .translateY(position.getYOffset())
                                .translateZ(position.getXOffset())
                                .translateX(position.getZOffset())
                                .rotateZDegrees(position.getRotation())
                                .uncenter()
                                .renderInto(ms, vb);
                ms.popPose();
            }
        }

        super.renderSafe(be1, partialTicks, ms, buffer, light, overlay);
    }

    private PartialModel getCylinderModel(EngineBlockEntity be) {

        return switch (be.engineType) {
            case I, U, BOXER, TURBINE -> TFMGPartialModels.SMALL_CYLINDER;
            case V, W -> TFMGPartialModels.CYLINDER;
            case RADIAL -> TFMGPartialModels.RADIAL_ENGINE_CYLINDER;
        };

    }
}
