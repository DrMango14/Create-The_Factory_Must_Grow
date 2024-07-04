package com.drmangotea.createindustry.blocks.decoration.kinetics.flywheels;


import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TFMGFlywheelBlock extends RotatedPillarKineticBlock implements IBE<TFMGFlywheelBlockEntity> {

	public TFMGFlywheelBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Class<TFMGFlywheelBlockEntity> getBlockEntityClass() {
		return TFMGFlywheelBlockEntity.class;
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return AllShapes.LARGE_GEAR.get(pState.getValue(AXIS));
	}
	
	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public BlockEntityType<? extends TFMGFlywheelBlockEntity> getBlockEntityType() {
		return TFMGBlockEntities.STEEL_FLYWHEEL.get();
	}
	
	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return face.getAxis() == getRotationAxis(state);
	}

	@Override
	public Axis getRotationAxis(BlockState state) {
		return state.getValue(AXIS);
	}

	@Override
	public float getParticleTargetRadius() {
		return 2f;
	}

	@Override
	public float getParticleInitialRadius() {
		return 1.75f;
	}
	
}
