package com.drmangotea.tfmg.content.electricity.network.transformer.large;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.network.large_switch.LargeSwitchBlock;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LargeTransformerBlock extends HorizontalKineticBlock implements IBE<LargeTransformerBlockEntity> {

    public static final BooleanProperty IS_MAIN_PART = LargeSwitchBlock.IS_MAIN_PART;
    public static final BooleanProperty UNFINISHED_MODEL = BooleanProperty.create("unfinished_model");


    public LargeTransformerBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(UNFINISHED_MODEL, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(IS_MAIN_PART,UNFINISHED_MODEL);
    }



    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            BlockPos blockpos = pos.relative(state.getValue(HORIZONTAL_FACING));
            level.setBlock(blockpos, state.setValue(IS_MAIN_PART, false), 3);
            level.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, 3);
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if(!state.getValue(IS_MAIN_PART)) {
            return state.getValue(UNFINISHED_MODEL) ? TFMGShapes.LARGE_TRANSFORMER_UNFINISHED.get(state.getValue(HORIZONTAL_FACING)) : TFMGShapes.LARGE_TRANSFORMER.get(state.getValue(HORIZONTAL_FACING).getOpposite());
        }else return state.getValue(UNFINISHED_MODEL) ? TFMGShapes.LARGE_TRANSFORMER_UNFINISHED.get(state.getValue(HORIZONTAL_FACING).getOpposite()) : TFMGShapes.LARGE_TRANSFORMER.get(state.getValue(HORIZONTAL_FACING));
        }

    private static Direction getNeighbourDirection(boolean mainPart, Direction direction) {
        return mainPart ? direction : direction.getOpposite();
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        BlockPos mainPartPos = pos;

        if(!state.getValue(IS_MAIN_PART))
            mainPartPos = pos.relative(state.getValue(HORIZONTAL_FACING).getOpposite());

        if(level.getBlockEntity(mainPartPos) instanceof LargeTransformerBlockEntity be){
            return be.addComponent(stack,player,hand);
        }


        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        BlockPos neighbourPos = pos.relative(context.getHorizontalDirection().getOpposite());



        if(!level.getBlockState(neighbourPos).is(TFMGBlocks.LARGE_COIL))
            return null;
        return super.getStateForPlacement(context);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {

        if(!state.getValue(IS_MAIN_PART))
            return new ArrayList<>();

        return super.getDrops(state, params);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
         if (facing == getNeighbourDirection(state.getValue(IS_MAIN_PART), state.getValue(HORIZONTAL_FACING))) {
             if(!(facingState.is(this) && facingState.getValue(IS_MAIN_PART) != state.getValue(IS_MAIN_PART)))

                     return Blocks.AIR.defaultBlockState();
         }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);

    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        if(!state.getValue(IS_MAIN_PART))
            return false;

        return state.getValue(HORIZONTAL_FACING).getClockWise().getAxis() == face.getAxis();

    }



    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        withBlockEntityDo(level, pos, IElectric::onPlaced);
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getClockWise().getAxis();
    }

    @Override
    public Class<LargeTransformerBlockEntity> getBlockEntityClass() {
        return LargeTransformerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends LargeTransformerBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.LARGE_TRANSFORMER.get();
    }
}
