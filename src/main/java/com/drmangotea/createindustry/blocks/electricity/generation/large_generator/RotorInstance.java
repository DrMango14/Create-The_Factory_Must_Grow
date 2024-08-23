package com.drmangotea.createindustry.blocks.electricity.generation.large_generator;


import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;

import static com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock.AXIS;

public class RotorInstance extends KineticBlockEntityInstance<RotorBlockEntity> implements DynamicInstance {

	protected final RotatingData shaft;
	protected final ModelData wheel;

	protected final ModelData frame;
	protected float lastAngle = Float.NaN;

	public RotorInstance(MaterialManager materialManager, RotorBlockEntity blockEntity) {
		super(materialManager, blockEntity);

		shaft = setup(getRotatingMaterial().getModel(shaft())
			.createInstance());
		wheel = getTransformMaterial().getModel(blockState)
			.createInstance();

		Direction direction=null;
		Direction.Axis axis = blockState.getValue(AXIS);

		if(axis == Direction.Axis.X)
			direction = Direction.WEST;
		if(axis == Direction.Axis.Y)
			direction = Direction.UP;
		if(axis == Direction.Axis.Z)
			direction = Direction.NORTH;

		frame = getTransformMaterial()
				.getModel(TFMGPartialModels.AIR_INTAKE_FAN_MEDIUM, blockState, direction)
				.createInstance();

		animate(blockEntity.angle);
	}

	@Override
	public void beginFrame() {

		float partialTicks = AnimationTickHolder.getPartialTicks();

		float speed = blockEntity.visualSpeed.getValue(partialTicks) * 3 / 10f;
		float angle = blockEntity.angle + speed * partialTicks;

		if (Math.abs(angle - lastAngle) < 0.001)
			return;

		animate(angle);

		lastAngle = angle;

		if(!blockEntity.valid)
			frame.setEmptyTransform();
	}

	private void animate(float angle) {
		PoseStack ms = new PoseStack();
		TransformStack msr = TransformStack.cast(ms);

		msr.translate(getInstancePosition());
		msr.centre()
			.rotate(Direction.get(Direction.AxisDirection.POSITIVE, axis), AngleHelper.rad(angle))
			.unCentre();

		wheel.setTransform(ms);
	}

	@Override
	public void update() {
		updateRotation(shaft);
	}

	@Override
	public void updateLight() {
		relight(pos, shaft, wheel,frame);
	}

	@Override
	public void remove() {
		shaft.delete();
		wheel.delete();
		frame.delete();
	}

}
