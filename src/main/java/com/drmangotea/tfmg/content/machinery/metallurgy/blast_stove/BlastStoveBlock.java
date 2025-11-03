package com.drmangotea.tfmg.content.machinery.metallurgy.blast_stove;


import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


public class BlastStoveBlock extends Block implements IWrenchable, IBE<BlastStoveBlockEntity> {

    public BlastStoveBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    public static boolean isBlastStove(BlockState state) {
        return state.getBlock() instanceof BlastStoveBlock;
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved) {
        if (oldState.getBlock() == state.getBlock())
            return;
        if (moved)
            return;
        withBlockEntityDo(world, pos, BlastStoveBlockEntity::updateConnectivity);

    }


    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
            BlockEntity te = world.getBlockEntity(pos);
            if (!(te instanceof BlastStoveBlockEntity))
                return;
            BlastStoveBlockEntity tankTE = (BlastStoveBlockEntity) te;
            world.removeBlockEntity(pos);
            ConnectivityHandler.splitMulti(tankTE);
        }
    }

    @Override
    public Class<BlastStoveBlockEntity> getBlockEntityClass() {
        return BlastStoveBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BlastStoveBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.BLAST_STOVE.get();
    }





}
