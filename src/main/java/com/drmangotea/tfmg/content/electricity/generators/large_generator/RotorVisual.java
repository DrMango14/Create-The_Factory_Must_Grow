package com.drmangotea.tfmg.content.electricity.generators.large_generator;

import com.drmangotea.tfmg.content.decoration.flywheels.TFMGFlywheelBlock;
import com.drmangotea.tfmg.content.decoration.flywheels.TFMGFlywheelBlockEntity;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.function.Consumer;

public class RotorVisual extends KineticBlockEntityVisual<RotorBlockEntity> implements SimpleDynamicVisual {

	protected final RotatingInstance shaft;
	protected final TransformedInstance wheel;
	protected float lastAngle = Float.NaN;

	protected final Matrix4f baseTransform = new Matrix4f();


	public RotorVisual(VisualizationContext context, RotorBlockEntity blockEntity, float partialTick) {
		super(context, blockEntity, partialTick);

		var axis = rotationAxis();
		shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT))
			.createInstance();

		shaft.setup(RotorVisual.this.blockEntity)
			.setPosition(getVisualPosition())
			.rotateToFace(axis)
			.setChanged();

		wheel = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(TFMGPartialModels.ROTOR))
			.createInstance();


		Direction align = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);

		wheel.translate(getVisualPosition())
			.center()
			.rotate(new Quaternionf().rotateTo(0, 1, 0, align.getStepX(), align.getStepY(), align.getStepZ()));


		//wheel.rotateX((float) Math.PI/2);
		//wheel.rotateY((float) Math.PI/2);
		//wheel.rotateZ((float) Math.PI/2);


		baseTransform.set(wheel.pose);

		animate(blockEntity.angle);
	}

	@Override
	public void beginFrame(Context ctx) {

		float partialTicks = ctx.partialTick();

		float speed = blockEntity.visualSpeed.getValue(partialTicks) * 3 / 10f;
		float angle = blockEntity.angle + speed * partialTicks;

		if (Math.abs(angle - lastAngle) < 0.001)
			return;

		animate(angle);

		lastAngle = angle;
	}

	private void animate(float angle) {
		wheel.setTransform(baseTransform)
			.rotateY(AngleHelper.rad(angle))
			.uncenter()
			.setChanged();
	}

	@Override
	public void update(float pt) {
		shaft.setup(blockEntity)
			.setChanged();
	}

	@Override
	public void updateLight(float partialTick) {
		relight(shaft, wheel);
	}

	@Override
	protected void _delete() {
		shaft.delete();
		wheel.delete();
	}

	@Override
	public void collectCrumblingInstances(Consumer<Instance> consumer) {
		consumer.accept(shaft);
		consumer.accept(wheel);
	}
}
