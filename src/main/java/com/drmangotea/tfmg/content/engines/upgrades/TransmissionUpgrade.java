package com.drmangotea.tfmg.content.engines.upgrades;

import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class TransmissionUpgrade extends EngineUpgrade{





    @Override
    public Optional<? extends EngineUpgrade> createUpgrade() {
        return Optional.of(new TransmissionUpgrade());
    }


    @Override
    public void render(AbstractSmallEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light) {

        BlockState state = be.getBlockState();
        Direction facing = state.getValue(FACING);
        boolean side = false;
        ms.pushPose();
        if (be instanceof RegularEngineBlockEntity blockEntity) {
            side = blockEntity.type.upgradesOnSide;
        }

        CachedBuffers.partial(TFMGPartialModels.TRANSMISSION, state)
                .center()
                .translateY(side ? -2/16f :0)
                .rotateYDegrees(facing.toYRot())
                .rotateZDegrees(side ? 90 : 0)
                .translateY(side ? 4 / 16f : 0)
                .uncenter()
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));

        ms.popPose();

    }

    @Override
    public Item getItem() {
        return TFMGItems.TRANSMISSION.asItem();
    }

    public enum TransmissionState{
        REVERSE(0.9f,true),
        NEUTRAL(0),
        SHIFT_1(0.6f),
        SHIFT_2(0.7f),
        SHIFT_3(1.1f),
        SHIFT_4(1.4f),
        SHIFT_5(1.5f),
        SHIFT_6(1.9f)



        ;
        public final float value;
        public final boolean reverse;
        TransmissionState(float value){
            this(value,false);
        }
        TransmissionState(float value, boolean reverse){
            this.value = value;
            this.reverse = reverse;
        }
    }
}
