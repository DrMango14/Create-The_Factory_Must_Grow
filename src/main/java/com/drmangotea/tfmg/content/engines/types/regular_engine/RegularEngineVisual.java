package com.drmangotea.tfmg.content.engines.types.regular_engine;

import com.drmangotea.tfmg.content.engines.base.AbstractEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.base.EngineBlock;
import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.IndustrialMixerBlockEntity;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class RegularEngineVisual extends KineticBlockEntityVisual<AbstractSmallEngineBlockEntity> {
	@Nullable
    protected final RotatingInstance shaft;

    public RegularEngineVisual(VisualizationContext context, AbstractSmallEngineBlockEntity blockEntity, float partialTick) {
		super(context, blockEntity, partialTick);


		RotatingInstance shaft = null;

		Block block = blockState.getBlock();
		if (block instanceof IRotate def) {
			for (Direction d : Iterate.directionsInAxis(rotationAxis())) {
				if (!def.hasShaftTowards(blockEntity.getLevel(), blockEntity.getBlockPos(), blockState, d))
					continue;
				RotatingInstance instance = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
						.createInstance();
				instance.setup(blockEntity)
						.setPosition(getVisualPosition())
						.rotateToFace(Direction.SOUTH, d)
						.setChanged();
					shaft = instance;
			}
		}

		this.shaft = shaft;
	}

    @Override
    public void update(float pt) {
		if (shaft != null)shaft.setup(blockEntity)
			.setChanged();

	}

    @Override
    public void updateLight(float partialTick) {
        BlockPos behind = pos.relative(Direction.UP);
		if (shaft != null)relight(behind, shaft);

    }

    @Override
    protected void _delete() {
		if (shaft != null)shaft.delete();
    }

	@Override
	public void collectCrumblingInstances(Consumer<Instance> consumer) {
		consumer.accept(shaft);
	}
}
