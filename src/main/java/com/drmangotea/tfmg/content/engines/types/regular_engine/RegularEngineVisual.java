package com.drmangotea.tfmg.content.engines.types.regular_engine;

import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RegularEngineVisual extends KineticBlockEntityVisual<AbstractSmallEngineBlockEntity> {

	protected final Map<Direction, RotatingInstance> shafts;

    public RegularEngineVisual(VisualizationContext context, AbstractSmallEngineBlockEntity blockEntity, float partialTick) {
		super(context, blockEntity, partialTick);
		this.shafts = new HashMap<>();
		if (blockEntity.getBlockState().getBlock() instanceof IRotate def) {
			for (Direction d : Iterate.directionsInAxis(rotationAxis())) {
				if (!def.hasShaftTowards(blockEntity.getLevel(), blockEntity.getBlockPos(), blockState, d)) continue;
				RotatingInstance shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
						.createInstance()
						.rotateToFace(Direction.SOUTH, d)
						.setup(blockEntity)
						.setPosition(getVisualPosition());
				shafts.put(d, shaft);
			}
		}
		for (RotatingInstance shaft : shafts.values()) {
			shaft.setChanged();
		}
	}

    @Override
    public void update(float pt) {
		for (RotatingInstance shaft : shafts.values()) {
			shaft.setup(blockEntity).setChanged();
		}
	}

    @Override
    public void updateLight(float partialTick) {
        BlockPos behind = pos.relative(Direction.UP);
		for (RotatingInstance shaft : shafts.values()) {
			if (shaft != null)
				relight(behind, shaft);
		}
    }

    @Override
    protected void _delete() {
		for (RotatingInstance shaft : shafts.values()) {
			shaft.delete();
		}
    }

	@Override
	public void collectCrumblingInstances(Consumer<Instance> consumer) {
		for (RotatingInstance shaft : shafts.values()) {
			consumer.accept(shaft);
		}
	}
}
