package com.drmangotea.tfmg.content.machinery.vat.freezer;

import com.drmangotea.tfmg.content.electricity.base.IOldElectric;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FreezerBlock extends Block implements IBE<FreezerBlockEntity> {
    public FreezerBlock(Properties p_49795_) {
        super(p_49795_);
    }


    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        withBlockEntityDo(level,pos, IOldElectric::onPlaced);
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }
    @Override
    public Class<FreezerBlockEntity> getBlockEntityClass() {
        return FreezerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FreezerBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.FREEZER.get();
    }
}
