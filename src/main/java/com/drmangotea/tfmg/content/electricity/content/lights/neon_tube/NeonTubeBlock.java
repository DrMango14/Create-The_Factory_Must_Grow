package com.drmangotea.tfmg.content.electricity.content.lights.neon_tube;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.content.electricity.content.lights.ElectricLightBlock;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;


@NothingNullByDefault
public class NeonTubeBlock extends PipeBlock implements IBE<NeonTubeBlockEntity>, IWrenchable {
    public static final IntegerProperty LIGHT = ElectricLightBlock.LIGHT;

    public NeonTubeBlock(Properties properties) {
        super(0.3125F/2.5f, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIGHT, 0).setValue(NORTH, false).setValue(EAST, Boolean.FALSE).setValue(SOUTH, false).setValue(WEST, false).setValue(UP, true).setValue(DOWN, true));
    }
	
    public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockGetter level = context.getLevel();
		BlockPos pos = context.getClickedPos();

        return this.defaultBlockState()
			.setValue(DOWN, level.getBlockState(pos.below()).is(this))
			.setValue(UP, level.getBlockState(pos.above()).is(this))
			.setValue(NORTH, level.getBlockState(pos.north()).is(this))
			.setValue(EAST, level.getBlockState(pos.east()).is(this))
			.setValue(SOUTH, level.getBlockState(pos.south()).is(this))
			.setValue(WEST, level.getBlockState(pos.west()).is(this));
    }
	
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN,LIGHT);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction dirToToggle = getDirToToggle(context, pos);

        switch (dirToToggle) {
			case UP -> level.setBlockAndUpdate(pos, state.setValue(UP,!state.getValue(UP)));
			case DOWN -> level.setBlockAndUpdate(pos, state.setValue(DOWN,!state.getValue(DOWN)));
			case WEST -> level.setBlockAndUpdate(pos, state.setValue(WEST,!state.getValue(WEST)));
			case EAST -> level.setBlockAndUpdate(pos, state.setValue(EAST,!state.getValue(EAST)));
			case NORTH -> level.setBlockAndUpdate(pos, state.setValue(NORTH,!state.getValue(NORTH)));
			case SOUTH -> level.setBlockAndUpdate(pos, state.setValue(SOUTH,!state.getValue(SOUTH)));
		}




        return InteractionResult.SUCCESS;
    }

    private static Direction getDirToToggle(UseOnContext context, BlockPos pos) {
        Vec3 position = context.getClickLocation();

        double X = position.x()- pos.getX();
        double Y = position.y()- pos.getY();
        double Z = position.z()- pos.getZ();

        Direction dirToToggle = Direction.NORTH;

        if (   X >= 0.375 && X <= 0.625
            && Y >= 0.375 && Y <= 0.625
            && Z >= 0.375 && Z <= 0.625
        ) { dirToToggle = context.getClickedFace(); }

        if(X>0.625) dirToToggle = Direction.EAST;
        if(X<0.375) dirToToggle = Direction.WEST;
        if(Y>0.625) dirToToggle = Direction.UP;
        if(Y<0.375) dirToToggle = Direction.DOWN;
        if(Z>0.625) dirToToggle = Direction.SOUTH;
        if(Z<0.375) dirToToggle = Direction.NORTH;
        return dirToToggle;
    }


    @Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult hitResult) {
        if (player.isShiftKeyDown())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        ItemStack heldItem = player.getItemInHand(pHand);
        NeonTubeBlockEntity be = getBlockEntity(level, pos);
        DyeColor dye = DyeColor.getColor(heldItem);
        if (be != null && dye != null) {
			level.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
			be.setColor(dye);
			return ItemInteractionResult.SUCCESS;
		}
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
		if(!oldState.is(TFMGBlocks.NEON_TUBE.get()))
			for(Direction facing : Direction.values()) {
				BlockState neighbourState = level.getBlockState(pos.relative(facing));
				 if (neighbourState.is(TFMGBlocks.NEON_TUBE.get())) {
					 level.setBlockAndUpdate(pos.relative(facing), neighbourState.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(facing.getOpposite()), true));
				 }
			}
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }
	
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }

    @Override
    public Class<NeonTubeBlockEntity> getBlockEntityClass() {
        return NeonTubeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends NeonTubeBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.NEON_TUBE.get();
    }
	
    public static final MapCodec<NeonTubeBlock> CODEC = simpleCodec(NeonTubeBlock::new);

    @Override
    protected MapCodec<? extends PipeBlock> codec() {
        return CODEC;
    }
}