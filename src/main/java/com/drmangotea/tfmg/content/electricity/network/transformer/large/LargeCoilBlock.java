package com.drmangotea.tfmg.content.electricity.network.transformer.large;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Collections;
import java.util.List;

public class LargeCoilBlock extends Block implements IBE<LargeCoilBlockEntity> {

    public LargeCoilBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        withBlockEntityDo(level, pos, be -> be.setCapacity(stack));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        // Drops handled by onDestroyedByPlayer to preserve turn count NBT
        return Collections.emptyList();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(TFMGBlocks.LAMINATED_MAGNETIC_ALLOY_BLOCK.get().asItem())) {
            if (level.getBlockEntity(pos) instanceof LargeCoilBlockEntity be)
                return be.createTransformer(player, player.getDirection());
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos,
            Player player, boolean willHarvest, FluidState fluid) {
        if (!player.isCreative() && level.getBlockEntity(pos) instanceof LargeCoilBlockEntity be) {
            ItemStack item = new ItemStack(this);
            item.getOrCreateTag().putInt("Turns", be.turns);
            ItemEntity itemEntity = new ItemEntity(level,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, item);
            if (itemEntity.getItem().getCount() > 0)
                level.addFreshEntity(itemEntity);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public Class<LargeCoilBlockEntity> getBlockEntityClass() {
        return LargeCoilBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends LargeCoilBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.LARGE_COIL.get();
    }
}
