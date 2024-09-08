package com.drmangotea.tfmg.blocks.electricity.debug;


import com.drmangotea.tfmg.blocks.electricity.base.IHaveCables;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class VoltageCubeBlock extends Block implements IBE<VoltageCubeBlockEntity>, IHaveCables {
    public VoltageCubeBlock(Properties pProperties) {
        super(pProperties);
    }



    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }
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

      // pPlayer.discard();

      // pPlayer.resetAttackStrengthTicker();

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
}
