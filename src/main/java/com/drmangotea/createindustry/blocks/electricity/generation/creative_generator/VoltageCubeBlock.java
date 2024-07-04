package com.drmangotea.createindustry.blocks.electricity.generation.creative_generator;

import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class VoltageCubeBlock extends Block implements IBE<VoltageCubeBlockEntity> {
    public VoltageCubeBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void fillItemCategory(CreativeModeTab pTab, NonNullList<ItemStack> pItems) {}

    @Override
    public Class<VoltageCubeBlockEntity> getBlockEntityClass() {
        return VoltageCubeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends VoltageCubeBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.VOLTAGE_CUBE.get();
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        pPlayer.discard();

        pPlayer.resetAttackStrengthTicker();

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
}
