package com.drmangotea.tfmg.content.engines.types.regular_engine;

import com.drmangotea.tfmg.content.engines.base.EngineRenderer;
import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.turbine_engine.TurbineEngineBlockEntity;
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
    protected void renderSafe(AbstractSmallEngineBlockEntity be1, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        RegularEngineBlockEntity be = (RegularEngineBlockEntity) be1;
        if(be instanceof TurbineEngineBlockEntity)
            return;
        BlockState blockState = be.getBlockState();

        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

        for (int i = 0; i < be.type.pistons.size(); i++) {
            PistonPosition position = be.type.pistons.get(i);

            if (be.type.pistons.size() == be.pistonInventory.getSlots())
                ms.pushPose();
            if (!be.pistonInventory.getStackInSlot(i).isEmpty())
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

        super.renderSafe(be1, partialTicks, ms, buffer, light, overlay);
    }

    private PartialModel getCylinderModel(RegularEngineBlockEntity be){
        return be.type.cylinderModel;
    }
}
