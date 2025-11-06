package com.drmangotea.tfmg.content.electricity.connection.tube;


import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.content.decoration.concrete.ConcreteloggedBlock;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.placement.PoleHelper;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Predicate;

public class CableTubeBlock extends RotatedPillarBlock implements IBE<CableTubeBlockEntity>, ConcreteloggedBlock {

    public static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());

    public final boolean concreteEncased;

    public CableTubeBlock(Properties p_49795_, boolean concreteEncased) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONCRETELOGGED, false).setValue(AXIS, Direction.Axis.Y));
        this.concreteEncased = concreteEncased;
    }

    @Override
    public FluidState getFluidState(BlockState p_60577_) {
        return fluidState(p_60577_);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONCRETELOGGED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {

        if (state.is(TFMGBlocks.CABLE_TUBE.get()))
            tickDrying(level, state, TFMGBlocks.CONCRETE_ENCASED_CABLE_TUBE.getDefaultState().setValue(AXIS, state.getValue(AXIS)), pos, randomSource);

        if (state.is(TFMGBlocks.ELECTRIC_POST.get()))
            tickDrying(level, state, TFMGBlocks.CONCRETE_ENCASED_ELECTRIC_POST.getDefaultState().setValue(AXIS, state.getValue(AXIS)), pos, randomSource);
    }



    @Override
    public boolean isRandomlyTicking(BlockState p_49921_) {
        return true;
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
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel,
                                  BlockPos pCurrentPos, BlockPos pFacingPos) {
        if(!concreteEncased)
            updateConcrete(pLevel, pState, pCurrentPos);
        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return concreteEncased ? super.getShape(pState, pLevel, pPos, pContext) : TFMGShapes.CABLE_TUBE.get(pState.getValue(AXIS));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());

        return concreteEncased ? state : withConcrete(state, context);
    }

    @Override
    public Class<CableTubeBlockEntity> getBlockEntityClass() {
        return CableTubeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CableTubeBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.CABLE_TUBE.get();
    }



    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult pHit) {



        ItemStack itemInHand = player.getItemInHand(hand);

        IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
        if (itemInHand.is(state.getBlock().asItem())){
            return helper.getOffset(player, level, state, pos, pHit)
                    .placeInWorld(level, (BlockItem) itemInHand.getItem(), player, hand, pHit);
        }


        return concreteEncased ? InteractionResult.PASS : onClicked(level, pos, state, player, hand);
    }

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper extends PoleHelper<Direction.Axis> {


        private PlacementHelper() {
            super(state -> state.getBlock() instanceof CableTubeBlock, state -> state.getValue(AXIS), AXIS);
        }

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return i -> i.getItem() instanceof BlockItem
                    && ((BlockItem) i.getItem()).getBlock() instanceof CableTubeBlock;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return s -> s.getBlock() instanceof CableTubeBlock;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos,
                                         BlockHitResult ray) {
            PlacementOffset offset = super.getOffset(player, world, state, pos, ray);
            if (offset.isSuccessful())
                offset.withTransform(offset.getTransform()
                        .andThen(s -> state.setValue(AXIS, state.getValue(AXIS))));
            return offset;
        }

    }
}
