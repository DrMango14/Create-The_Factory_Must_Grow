package com.drmangotea.createindustry.blocks.electricity.polarizer;


import com.drmangotea.createindustry.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.drmangotea.createindustry.registry.TFMGShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PolarizerBlock extends TFMGHorizontalDirectionalBlock implements IBE<PolarizerBlockEntity>, IWrenchable {
    public PolarizerBlock(Properties p_54120_) {
        super(p_54120_);
    }


    @Override
    public InteractionResult use(BlockState pState, Level level, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack inHand = player.getItemInHand(pHand);


        if (level.getBlockEntity(pos) instanceof PolarizerBlockEntity be)
            if (be.playerInteract(player, pHand))
                return InteractionResult.SUCCESS;

        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return TFMGShapes.SLAB;
    }

    @Override
    public Class<PolarizerBlockEntity> getBlockEntityClass() {
        return PolarizerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PolarizerBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.POLARIZER.get();
    }
}
