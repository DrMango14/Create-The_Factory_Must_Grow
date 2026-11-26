package com.drmangotea.tfmg.content.engines.upgrades;

import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.upgrades.base.EngineGenerator;
import com.drmangotea.tfmg.content.engines.upgrades.base.EngineUpgrade;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.mojang.blaze3d.vertex.PoseStack;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class GeneratorEngineUpgrade extends EngineUpgrade implements EngineGenerator {
    public GeneratorEngineUpgrade(ResourceLocation id) {
        super(id);
    }

    @Override
    public void render(AbstractSmallEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light) {
        BlockState state = be.getBlockState();
        Direction facing = state.getValue(FACING);
        boolean side = false;
        ms.pushPose();
        if (be instanceof RegularEngineBlockEntity blockEntity) {
            side = blockEntity.type.is(TFMGTags.Engines.UPGRADES_ON_SIDE.tag);
        }

        CachedBuffers.partial(TFMGPartialModels.ENGINE_GENERATOR, state)
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
    public float getTorqueModifier(AbstractSmallEngineBlockEntity engine) {
        return 0.7f;
    }

    @Override
    public int getVoltageGeneration(AbstractSmallEngineBlockEntity engine) {
        return (int) (engine.rpm / 25f);
    }

    @Override
    public float getPowerGeneration(AbstractSmallEngineBlockEntity engine) {
        return engine.rpm;
    }
	
	@Override
	public ElectricalProperties getProperties(AbstractSmallEngineBlockEntity engine) {
		//TODO: Implement
		// Maybe use ThreePhaseGeneratorProperties for this, since it's attached to the three phase generator.
		// Maybe Rename this class.
		return null;
	}
}
