package com.drmangotea.tfmg.content.electricity.network.large_switch;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LargeSwitchBlock extends HorizontalKineticBlock implements IBE<LargeSwitchBlockEntity> {

    public static final BooleanProperty IS_MAIN_PART = BooleanProperty.create("is_main_part");

    public LargeSwitchBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(IS_MAIN_PART);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            BlockPos secondPos = pos.relative(state.getValue(HORIZONTAL_FACING));
            level.setBlock(secondPos, state.setValue(IS_MAIN_PART, false), 3);
            level.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, 3);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(IS_MAIN_PART)
                ? TFMGShapes.LARGE_SWITCH_PRIMARY.get(state.getValue(HORIZONTAL_FACING))
                : TFMGShapes.LARGE_SWITCH_SECONDARY.get(state.getValue(HORIZONTAL_FACING));
    }

    private static Direction getNeighbourDirection(boolean mainPart, Direction direction) {
        return mainPart ? direction : direction.getOpposite();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockPos neighbourPos = pos.relative(context.getHorizontalDirection().getOpposite());
        if (!level.getBlockState(neighbourPos).isAir())
            return null;
        return super.getStateForPlacement(context);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        if (!state.getValue(IS_MAIN_PART))
            return new ArrayList<>();
        return super.getDrops(state, params);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState,
            LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (facing == getNeighbourDirection(state.getValue(IS_MAIN_PART), state.getValue(HORIZONTAL_FACING))) {
            if (!(facingState.is(this) && facingState.getValue(IS_MAIN_PART) != state.getValue(IS_MAIN_PART)))
                return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        if (!state.getValue(IS_MAIN_PART))
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
    public Class<LargeSwitchBlockEntity> getBlockEntityClass() {
        return LargeSwitchBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends LargeSwitchBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.LARGE_SWITCH.get();
    }
}
