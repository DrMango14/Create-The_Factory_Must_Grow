package com.drmangotea.tfmg.content.machinery.misc.winding_machine;

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
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class WindingMachineVisual extends KineticBlockEntityVisual<WindingMachineBlockEntity> {
    protected final RotatingInstance shaft;

    public WindingMachineVisual(VisualizationContext context, WindingMachineBlockEntity blockEntity, float partialTick) {
		super(context, blockEntity, partialTick);

		Direction facing = blockEntity.getBlockState().getValue(HORIZONTAL_FACING).getCounterClockWise();

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
		Direction facing = blockEntity.getBlockState().getValue(HORIZONTAL_FACING).getCounterClockWise();
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