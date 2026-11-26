package com.drmangotea.tfmg.content.electricity.content.power_transmission.adapters;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@NothingNullByDefault
public class ElectricAdapterBlock extends TFMGDirectionalBlock implements IBE<ElectricAdapterBlockEntity> {

    public final int slotCount;

    public ElectricAdapterBlock(Properties properties, int slotCount) {
        super(properties);
        this.slotCount = slotCount;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
		withBlockEntityDo(level, pos, IElectric::onPlace);
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return slotCount == 4 ? TFMGShapes.FOUR_SLOT_ADAPTER.get(pState.getValue(FACING)):TFMGShapes.TWO_SLOT_ADAPTER.get(pState.getValue(FACING));
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
        super.onRemove(state,level,pos,newState,isMoving);
    }

    @Override
    public Class<ElectricAdapterBlockEntity> getBlockEntityClass() {
        return ElectricAdapterBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ElectricAdapterBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.CABLE_ADAPTER.get();
    }
}
