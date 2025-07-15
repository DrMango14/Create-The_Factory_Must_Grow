package com.drmangotea.tfmg.content.machinery.vat.industrial_mixer;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class IndustrialMixerVisual extends KineticBlockEntityVisual<IndustrialMixerBlockEntity> {

    protected final RotatingInstance shaft;

    public IndustrialMixerVisual(VisualizationContext context, IndustrialMixerBlockEntity blockEntity, float partialTick) {
		super(context, blockEntity, partialTick);

		shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
			.createInstance();

		shaft.setup(blockEntity)
			.setPosition(getVisualPosition())
			.rotateToFace(Direction.SOUTH, Direction.UP)
			.setChanged();
	}

    @Override
    public void update(float pt) {
		shaft.setup(blockEntity)
			.setChanged();
	}

    @Override
    public void updateLight(float partialTick) {
        BlockPos behind = pos.relative(Direction.UP);
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
