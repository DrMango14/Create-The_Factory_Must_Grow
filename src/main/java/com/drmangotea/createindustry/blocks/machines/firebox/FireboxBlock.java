package com.drmangotea.createindustry.blocks.machines.firebox;

import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.block.IBE;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

import static com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FireboxBlock extends HorizontalDirectionalBlock implements IBE<FireboxBlockEntity>, IWrenchable {

	public static final EnumProperty<HeatLevel> HEAT_LEVEL = BlazeBurnerBlock.HEAT_LEVEL;

	public FireboxBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(HEAT_LEVEL, HeatLevel.SMOULDERING));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(HEAT_LEVEL, FACING);
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_) {
		if (world.isClientSide)
			return;
		BlockEntity blockEntity = world.getBlockEntity(pos.above());
		if (!(blockEntity instanceof BasinBlockEntity))
			return;
		BasinBlockEntity basin = (BasinBlockEntity) blockEntity;
		basin.notifyChangeOfContents();
	}



	@Override
	public Class<FireboxBlockEntity> getBlockEntityClass() {
		return FireboxBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends FireboxBlockEntity> getBlockEntityType() {
		return TFMGBlockEntities.FIREBOX.get();
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		if (state.getValue(HEAT_LEVEL) == HeatLevel.NONE)
			return null;
		return IBE.super.newBlockEntity(pos, state);
	}



	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		ItemStack stack = context.getItemInHand();
		Item item = stack.getItem();
		BlockState defaultState = defaultBlockState();

		return defaultState.setValue(FACING, context.getHorizontalDirection()
				.getOpposite());
	}



	@Override
	public boolean hasAnalogOutputSignal(BlockState p_149740_1_) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level p_180641_2_, BlockPos p_180641_3_) {
		return Math.max(0, state.getValue(HEAT_LEVEL)
			.ordinal() - 1);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
		return false;
	}

	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		if (random.nextInt(10) != 0)
			return;
		if (!state.getValue(HEAT_LEVEL)
			.isAtLeast(HeatLevel.SMOULDERING))
			return;
		world.playLocalSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F),
			(double) ((float) pos.getZ() + 0.5F), SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS,
			0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
	}

	public static HeatLevel getHeatLevelOf(BlockState blockState) {
		return blockState.hasProperty(FireboxBlock.HEAT_LEVEL) ? blockState.getValue(FireboxBlock.HEAT_LEVEL)
			: HeatLevel.NONE;
	}

	public static int getLight(BlockState state) {
		HeatLevel level = state.getValue(HEAT_LEVEL);
		return switch (level) {
			case NONE,SMOULDERING -> 0;
		default -> 15;
		};
	}




}
