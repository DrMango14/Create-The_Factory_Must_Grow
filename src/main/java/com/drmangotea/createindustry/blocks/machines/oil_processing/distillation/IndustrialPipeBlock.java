package com.drmangotea.createindustry.blocks.machines.oil_processing.distillation;

import com.drmangotea.createindustry.blocks.machines.oil_processing.distillation.output.DistillationOutputBlock;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGShapes;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Predicate;

public class IndustrialPipeBlock extends Block {

    public static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());
    public IndustrialPipeBlock(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return TFMGShapes.INDUSTRIAL_PIPE;
    }
    @Override
    public InteractionResult use(BlockState pState, Level level, BlockPos pos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemInHand = pPlayer.getItemInHand(pHand);



        if (pPlayer == null)
            return InteractionResult.PASS;


        IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
        if (helper.matchesItem(itemInHand))
            return helper.getOffset(pPlayer, level, pState, pos, pHit)
                    .placeInWorld(level, (BlockItem) itemInHand.getItem(), pPlayer, pHand, pHit);

        return super.use(pState,level,pos,pPlayer,pHand,pHit);

    }
    @MethodsReturnNonnullByDefault
    private static class PlacementHelper extends VerticalPlacementHelper {


        private PlacementHelper() {
            super(state -> state.getBlock() instanceof IndustrialPipeBlock);
        }

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return i -> i.getItem() instanceof BlockItem
                    && ((BlockItem) i.getItem()).getBlock() instanceof IndustrialPipeBlock;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return s ->s.getBlock() instanceof IndustrialPipeBlock;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos,
                                         BlockHitResult ray) {
            PlacementOffset offset = super.getOffset(player, world, state, pos, ray);
            if (offset.isSuccessful())
                offset.withTransform(offset.getTransform()
                        .andThen(s -> TFMGBlocks.INDUSTRIAL_PIPE.getDefaultState()));
            return offset;
        }

    }
}
