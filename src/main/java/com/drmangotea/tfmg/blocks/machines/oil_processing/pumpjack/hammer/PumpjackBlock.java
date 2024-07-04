package com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer;


import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class PumpjackBlock extends BearingBlock implements IBE<PumpjackBlockEntity> {


	public static final BooleanProperty WIDE = BooleanProperty.create("wide");

	public PumpjackBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WIDE);
		super.createBlockStateDefinition(builder);
	}


	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
		BlockHitResult hit) {
		if (!player.mayBuild())
			return InteractionResult.FAIL;

		if (player.isShiftKeyDown())
			return InteractionResult.FAIL;

		if (player.getItemInHand(handIn)
			.isEmpty()) {
			if (worldIn.isClientSide)
				return InteractionResult.SUCCESS;
			withBlockEntityDo(worldIn, pos, be -> {
				if (be.running) {
					//be.disassemble();
					return;
				}

				//if(be.crank==null||be.base == null)
				//	return;


			});
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {

		boolean wide = context.getLevel().getBlockState(context.getClickedPos().above()).is(TFMGBlocks.LARGE_PUMPJACK_HAMMER_PART.get());

		Direction preferredDirection = getPreferredHorizontalFacing(context);
		if (preferredDirection != null)
			return this.defaultBlockState().setValue(FACING, preferredDirection).setValue(WIDE,wide);
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(WIDE,wide);
	}

	public static Direction getPreferredHorizontalFacing(BlockPlaceContext context) {
		Direction prefferedSide = null;
		for (Direction side : Iterate.horizontalDirections) {
			BlockState blockState = context.getLevel().getBlockState(context.getClickedPos().relative(side));
			if (blockState.getBlock() instanceof IRotate) {
				if (((IRotate) blockState.getBlock()).hasShaftTowards(context.getLevel(), context.getClickedPos().relative(side),
						blockState, side.getOpposite()))
					if (prefferedSide != null && prefferedSide.getAxis() != side.getAxis()) {
						prefferedSide = null;
						break;
					} else {
						prefferedSide = side;
					}
			}
		}
		return prefferedSide == null ? null : prefferedSide;
	}

	@Override
	public Class<PumpjackBlockEntity> getBlockEntityClass() {
		return PumpjackBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends PumpjackBlockEntity> getBlockEntityType() {
		return TFMGBlockEntities.PUMPJACK_HAMMER.get();
	}

}
