package com.drmangotea.createindustry.blocks.engines.diesel;


import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlockEntity;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;

public class DieselEngineInstance extends BlockEntityInstance<DieselEngineBlockEntity> implements DynamicInstance {

	protected final ModelData piston;
	protected final ModelData linkage;
	protected final ModelData connector;
	//protected final ModelData connection;

	public DieselEngineInstance(MaterialManager materialManager, DieselEngineBlockEntity blockEntity) {
		super(materialManager, blockEntity);

		piston = materialManager.defaultSolid()
				.material(Materials.TRANSFORMED)
				.getModel(TFMGPartialModels.DIESEL_ENGINE_PISTON, blockState)
				.createInstance();
		linkage = materialManager.defaultSolid()
				.material(Materials.TRANSFORMED)
				.getModel(TFMGPartialModels.DIESEL_ENGINE_LINKAGE, blockState)
				.createInstance();
		connector = materialManager.defaultSolid()
				.material(Materials.TRANSFORMED)
				.getModel(AllPartialModels.ENGINE_CONNECTOR, blockState)
				.createInstance();
		//connection = materialManager.defaultSolid()
		//		.material(Materials.TRANSFORMED)
		//		.getModel(TFMGPartialModels.DIESEL_ENGINE_CONNECTION, blockState)
		//		.createInstance();

	}

	@Override
	public void beginFrame() {
		Float angle = blockEntity.getTargetAngle();
		if (angle == null) {
			piston.setEmptyTransform();
			linkage.setEmptyTransform();
			connector.setEmptyTransform();
			//connection.setEmptyTransform();
			return;
		}

		Direction facing = DieselEngineBlock.getFacing(blockState);
		Axis facingAxis = facing.getAxis();
		Axis axis = Axis.Y;

		PoweredShaftBlockEntity shaft = blockEntity.getShaft();
		if (shaft != null)
			axis = KineticBlockEntityRenderer.getRotationAxisOf(shaft);

		boolean roll90 = facingAxis.isHorizontal() && axis == Axis.Y || facingAxis.isVertical() && axis == Axis.Z;
		float sine = Mth.sin(angle);
		float sine2 = Mth.sin(angle - Mth.HALF_PI);
		float piston = ((1 - sine) / 4) * 24 / 16f;


		transformed(this.piston, facing, roll90)
			.translate(0, piston, 0);


		transformed(linkage, facing, roll90)
			.centre()
			.translate(0, 1, 0)
			.unCentre()
			.translate(0, piston, 0)
			.translate(0, 4 / 16f, 8 / 16f)
			.rotateX(sine2 * 23f)
			.translate(0, -4 / 16f, -8 / 16f);

		transformed(connector, facing, roll90)
			.translate(0, 2, 0)
			.centre()
			.rotateXRadians(-angle + Mth.HALF_PI)
			.unCentre();

	//	if(blockEntity.connectionVerticalX)
	//	transformed(this.connection, facing, roll90)
	//			.rotateY(90)
	//			.translate(-1, 0, 0);
//
	//	if(blockEntity.connectionVerticalZ)
	//		transformed(this.connection, facing, roll90)
	//				.rotateY(90)
	//				.translate(-1, 0, 0);
//
	//	if(blockEntity.connectionX)
	//		transformed(this.connection, facing, roll90)
	//				.rotateY(90)
	//				.translate(-1, 0, 0);
	//	if(blockEntity.connectionX2)
	//		transformed(this.connection, facing, roll90)
	//				.rotateY(90)
	//				.translate(-1, 0,0 );
//
	//	if(blockEntity.connectionZ)
	//		transformed(this.connection, facing, roll90)
	//				.rotateY(90)
	//				.translate(-1, 0, 0);
	//	if(blockEntity.connectionZ2)
	//		transformed(this.connection, facing, roll90)
	//				.rotateY(90)
	//				.translate(-1, 0, 0);


	}

	protected ModelData transformed(ModelData modelData, Direction facing, boolean roll90) {
		return modelData.loadIdentity()
			.translate(getInstancePosition())
			.centre()
			.rotateY(AngleHelper.horizontalAngle(facing))
			.rotateX(AngleHelper.verticalAngle(facing) + 90)
			.rotateY(roll90 ? -90 : 0)
			.unCentre();
	}

	@Override
	public void updateLight() {

		BlockPos inFront = pos.above(10);
		relight(pos, piston, linkage, connector);
	}

	@Override
	protected void remove() {
		piston.delete();
		linkage.delete();
		connector.delete();
		//connection.delete();
	}

}
