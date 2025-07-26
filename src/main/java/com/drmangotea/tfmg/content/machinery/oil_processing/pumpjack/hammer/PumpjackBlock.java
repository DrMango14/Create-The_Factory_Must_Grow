package com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.hammer;


import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.content.contraptions.bearing.BearingBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		boolean wide = context.getLevel().getBlockState(context.getClickedPos().above()).is(TFMGBlocks.LARGE_PUMPJACK_HAMMER_PART.get());
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(WIDE,wide);
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
