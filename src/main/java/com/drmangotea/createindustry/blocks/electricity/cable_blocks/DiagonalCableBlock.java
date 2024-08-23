package com.drmangotea.createindustry.blocks.electricity.cable_blocks;

import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.drmangotea.createindustry.registry.TFMGShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@SuppressWarnings({"unused","deprecation"})
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DiagonalCableBlock extends DirectionalBlock implements SimpleWaterloggedBlock, IWrenchable, IBE<DiagonalCableBlockEntity> {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty FACING_UP = BooleanProperty.create("facing_up");
    public DiagonalCableBlock(Properties p_54120_) {
        super(p_54120_);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.FALSE).setValue(FACING, Direction.NORTH).setValue(FACING_UP, false));
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55125_) {
        p_55125_.add(WATERLOGGED,FACING, FACING_UP);
    }


    @Override
    public FluidState getFluidState(BlockState p_51475_) {
        return p_51475_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_51475_);
    }
    public VoxelShape getShape(BlockState state, BlockGetter p_54562_, BlockPos p_54563_, CollisionContext p_54564_) {




        if (state.getValue(FACING_UP)) {
            return TFMGShapes.DIAGONAL_CABLE_BLOCK_UP.get(state.getValue(FACING));
        }


            return TFMGShapes.DIAGONAL_CABLE_BLOCK_DOWN.get(state.getValue(FACING));


    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        InteractionResult onWrenched = IWrenchable.super.onWrenched(state, context);
        if (!onWrenched.consumesAction())
            return onWrenched;

        context.getLevel().setBlock(context.getClickedPos(),state.setValue(FACING_UP,!state.getValue(FACING_UP)),2);

        playRotateSound(context.getLevel(), context.getClickedPos());
        return onWrenched;
    }

    @Override
    public BlockState updateShape(BlockState p_51461_, Direction p_51462_, BlockState p_51463_, LevelAccessor p_51464_, BlockPos p_51465_, BlockPos p_51466_) {
        if (p_51461_.getValue(WATERLOGGED)) {
            p_51464_.scheduleTick(p_51465_, Fluids.WATER, Fluids.WATER.getTickDelay(p_51464_));
        }

        return super.updateShape(p_51461_, p_51462_, p_51463_, p_51464_, p_51465_, p_51466_);
    }



    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        Direction facing = Objects.requireNonNull(context.getPlayer()).getDirection();
        Direction clickedFace = context.getClickedFace();

        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            if (clickedFace == Direction.DOWN)
                return defaultBlockState().setValue(FACING, facing.getOpposite()).setValue(FACING_UP,true).setValue(WATERLOGGED, flag);
                else
            return defaultBlockState().setValue(FACING, facing.getOpposite()).setValue(FACING_UP,false).setValue(WATERLOGGED, flag);
        }
        if (clickedFace == Direction.DOWN)
            return defaultBlockState().setValue(FACING, facing).setValue(FACING_UP,true).setValue(WATERLOGGED, flag);


    return defaultBlockState().setValue(FACING, facing).setValue(FACING_UP,false).setValue(WATERLOGGED, flag);
    }

    @Override
    public Class<DiagonalCableBlockEntity> getBlockEntityClass() {
        return DiagonalCableBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DiagonalCableBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.DIAGONAL_CABLE_BLOCK.get();
    }
}
