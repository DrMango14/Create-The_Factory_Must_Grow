package com.drmangotea.createindustry.blocks.machines.simple.welding_machine;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

public class WeldingMachineInstance extends BlockEntityInstance<WeldingMachineBlockEntity> implements DynamicInstance {

	private final OrientedData pressHead;

	public WeldingMachineInstance(MaterialManager materialManager, WeldingMachineBlockEntity blockEntity) {
		super(materialManager, blockEntity);

		pressHead = materialManager.defaultSolid()
				.material(Materials.ORIENTED)
				.getModel(AllPartialModels.MECHANICAL_PRESS_HEAD, blockState)
				.createInstance();

		Quaternion q = Vector3f.YP
			.rotationDegrees(AngleHelper.horizontalAngle(blockState.getValue(WeldingMachineBlock.FACING)));

		pressHead.setRotation(q);

		transformModels();
	}

	@Override
	public void beginFrame() {
		transformModels();
	}

	private void transformModels() {
		float renderedHeadOffset = getRenderedHeadOffset(blockEntity);

		pressHead.setPosition(getInstancePosition())
			.nudge(0, -renderedHeadOffset, 0);
	}

	private float getRenderedHeadOffset(WeldingMachineBlockEntity press) {
		WeldingBehaviour pressingBehaviour = press.getWeldingBehaviour();
		return pressingBehaviour.getRenderedHeadOffset(AnimationTickHolder.getPartialTicks())
			* pressingBehaviour.mode.headOffset;
	}

	@Override
	public void updateLight() {
		super.updateLight();

		relight(pos, pressHead);
	}

	@Override
	public void remove() {

		pressHead.delete();
	}
}
