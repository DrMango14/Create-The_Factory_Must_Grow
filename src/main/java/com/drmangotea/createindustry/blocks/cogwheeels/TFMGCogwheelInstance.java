package com.drmangotea.createindustry.blocks.cogwheeels;

import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;

import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;

public class TFMGCogwheelInstance extends SingleRotatingInstance<SimpleKineticBlockEntity> {

	protected RotatingData additionalShaft;

	public TFMGCogwheelInstance(MaterialManager materialManager, SimpleKineticBlockEntity blockEntity) {
		super(materialManager, blockEntity);
	}

	@Override
	public void init() {
		super.init();
		if (!ICogWheel.isLargeCog(blockEntity.getBlockState()))
			return;

		// Large cogs sometimes have to offset their teeth by 11.25 degrees in order to
		// mesh properly

		float speed = blockEntity.getSpeed();
		Axis axis = KineticBlockEntityRenderer.getRotationAxisOf(blockEntity);
		BlockPos pos = blockEntity.getBlockPos();
		float offset = BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, pos);
		Direction facing = Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE);
		Instancer<RotatingData> half = getRotatingMaterial().getModel(AllPartialModels.COGWHEEL_SHAFT, blockState,
			facing, () -> this.rotateToAxis(axis));

		additionalShaft = setup(half.createInstance(), speed);
		additionalShaft.setRotationOffset(offset);
	}

	@Override
	protected Instancer<RotatingData> getModel() {

		Axis axis = KineticBlockEntityRenderer.getRotationAxisOf(blockEntity);
		Direction facing = Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE);


		if (!ICogWheel.isLargeCog(blockEntity.getBlockState()))
			return getCutoutRotatingMaterial().getModel(blockState);
			//return super.getModel();



		PartialModel model = blockEntity.getBlockState().is(TFMGBlocks.LARGE_ALUMINUM_COGWHEEL.get()) ? TFMGPartialModels.LARGE_ALUMINUM_COGHWEEL : TFMGPartialModels.LARGE_STEEL_COGHWEEL;




		return getCutoutRotatingMaterial().getModel(model, blockState, facing,
			() -> this.rotateToAxis(axis));
	}

	protected Material<RotatingData> getCutoutRotatingMaterial() {
		return materialManager.defaultCutout()
				.material(AllMaterialSpecs.ROTATING);
	}

	private PoseStack rotateToAxis(Axis axis) {
		Direction facing = Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE);
		PoseStack poseStack = new PoseStack();
		TransformStack.cast(poseStack)
				.centre()
				.rotateToFace(facing)
				.multiply(Vector3f.XN.rotationDegrees(-90))
				.unCentre();
		return poseStack;
	}

	@Override
	public void update() {
		super.update();
		if (additionalShaft != null) {
			updateRotation(additionalShaft);
			additionalShaft.setRotationOffset(TFMGCogwheelRenderer.getShaftAngleOffset(axis, pos));
		}
	}

	@Override
	public void updateLight() {
		super.updateLight();
		if (additionalShaft != null)
			relight(pos, additionalShaft);
	}

	@Override
	public void remove() {
		super.remove();
		if (additionalShaft != null)
			additionalShaft.delete();
	}

}