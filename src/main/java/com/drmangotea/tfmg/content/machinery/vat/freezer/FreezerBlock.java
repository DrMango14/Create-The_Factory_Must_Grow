package com.drmangotea.tfmg.content.machinery.vat.freezer;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@NothingNullByDefault
public class FreezerBlock extends Block implements IBE<FreezerBlockEntity> {
    public FreezerBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
		withBlockEntityDo(level, pos, IElectric::onPlace);
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
        super.onRemove(state,level,pos,newState,isMoving);
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
