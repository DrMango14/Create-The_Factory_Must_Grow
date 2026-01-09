package com.drmangotea.tfmg.content.electricity.network.large_switch;

import com.drmangotea.tfmg.content.machinery.misc.winding_machine.WindingMachineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
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

import static com.drmangotea.tfmg.content.electricity.network.large_switch.LargeSwitchBlock.IS_MAIN_PART;
import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class LargeSwitchVisual extends KineticBlockEntityVisual<LargeSwitchBlockEntity> {
    protected final RotatingInstance shaft;

    public LargeSwitchVisual(VisualizationContext context, LargeSwitchBlockEntity blockEntity, float partialTick) {
		super(context, blockEntity, partialTick);

		Direction facing = blockEntity.getBlockState().getValue(HORIZONTAL_FACING).getCounterClockWise();
		if(!blockEntity.getBlockState().getValue(IS_MAIN_PART)) {
			shaft = null;
			return;
		}
		shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(TFMGPartialModels.LARGE_SWITCH_SHAFT))
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