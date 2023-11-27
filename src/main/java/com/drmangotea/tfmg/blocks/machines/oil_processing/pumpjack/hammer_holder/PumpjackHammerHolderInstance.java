package com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer_holder;


import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

public class PumpjackHammerHolderInstance extends KineticBlockEntityInstance<PumpjackHammerHolderBlockEntity> implements DynamicInstance {


	protected final ModelData hammer;
	protected final ModelData holder;
	protected float lastAngle = Float.NaN;

	public PumpjackHammerHolderInstance(MaterialManager modelManager, PumpjackHammerHolderBlockEntity tile) {
		super(modelManager, tile);


		hammer = getTransformMaterial()
				.getModel(TFMGPartialModels.PUMPJACK_HAMMER,blockState,tile.direction)
			.createInstance();

		holder = getTransformMaterial()
				.getModel(blockState)
				.createInstance();

	}

	@Override
	public void beginFrame() {

		float partialTicks = AnimationTickHolder.getPartialTicks();

		float speed = blockEntity.visualSpeed.getValue(partialTicks) * 3 / 10f;
		float angle = blockEntity.angle.getValue() + speed * partialTicks;

		if (Math.abs(angle - lastAngle) < 0.001)
			return;

		animate(angle);

		lastAngle = angle;
	}

	private void animate(float angle) {
		PoseStack ms = new PoseStack();
		TransformStack msr = TransformStack.cast(ms);

		msr.translate(getInstancePosition());
		msr.centre()
			.rotate(blockEntity.direction.getClockWise(), AngleHelper.rad(angle))
			.unCentre();
		PoseStack ms2 = new PoseStack();
		TransformStack msr2 = TransformStack.cast(ms2);

		msr2.translate(getInstancePosition());
		//msr.centre()
		//		.rotate(blockEntity.direction.getClockWise(), AngleHelper.rad(angle))
		//		.unCentre();


		hammer.setTransform(ms);
		holder.setTransform(ms2);
	}



	@Override
	public void updateLight() {
		relight(pos, hammer,holder);
	}

	@Override
	public void remove() {
		hammer.delete();
		holder.delete();
	}

}
