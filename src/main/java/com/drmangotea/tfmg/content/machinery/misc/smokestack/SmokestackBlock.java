package com.drmangotea.tfmg.content.machinery.misc.smokestack;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class SmokestackBlock extends Block implements IBE<SmokestackBlockEntity> {

    public static final BooleanProperty TOP = BooleanProperty.create("top");


    public SmokestackBlock(Properties p_49795_) {
        super(p_49795_);

    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state1, boolean p_60570_) {
        super.onPlace(state, level, pos, state1, p_60570_);

        if(level.getBlockState(pos.above()).getBlock() == state.getBlock())
            level.setBlock(pos,defaultBlockState().setValue(TOP,false),2);

        if(level.getBlockState(pos.below()).getBlock() == state.getBlock())
            level.setBlock(pos.below(),defaultBlockState().setValue(TOP,false),2);




    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        super.destroy(level, pos, state);

        if(level.getBlockState(pos.below()).getBlock() == state.getBlock())
            level.setBlock(pos.below(),defaultBlockState().setValue(TOP,true),2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_);
        p_49915_.add(TOP);
    }

    @Override
    public Class<SmokestackBlockEntity> getBlockEntityClass() {
        return SmokestackBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SmokestackBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.SMOKESTACK.get();
    }
}
