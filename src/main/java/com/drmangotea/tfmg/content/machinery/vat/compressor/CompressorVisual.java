package com.drmangotea.tfmg.content.machinery.vat.compressor;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.function.Consumer;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class CompressorVisual extends KineticBlockEntityVisual<CompressorBlockEntity> {
    protected final RotatingInstance shaft;

    public CompressorVisual(VisualizationContext context, CompressorBlockEntity blockEntity, float partialTick) {
		super(context, blockEntity, partialTick);

		Direction facing = blockEntity.getBlockState().getValue(HORIZONTAL_FACING);

		shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
				.createInstance();

		shaft.setup(blockEntity)
				.setPosition(getVisualPosition())
				.rotateToFace(Direction.SOUTH, facing)
				.setChanged();
	}

    @Override
    public void update(float pt) {
		shaft.setup(blockEntity)
			.setChanged();

	}

    @Override
    public void updateLight(float partialTick) {
		Direction facing = blockEntity.getBlockState().getValue(HORIZONTAL_FACING);
        BlockPos behind = pos.relative(facing);
		relight(behind, shaft);

    }

    @Override
    protected void _delete() {
		shaft.delete();
    }

	@Override
	public void collectCrumblingInstances(Consumer<Instance> consumer) {
		consumer.accept(shaft);
	}
}