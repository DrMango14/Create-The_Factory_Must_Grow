package com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.reinforcement;

import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlastFurnaceReinforcementWallBlock extends TFMGHorizontalDirectionalBlock {
    public BlastFurnaceReinforcementWallBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState blockState1, boolean b) {
        changeFireproofBricks(level, pos, blockState.getValue(FACING).getOpposite(), true);
        super.onPlace(blockState, level, pos, blockState1, b);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState blockState1, boolean b) {
        changeFireproofBricks(level, pos, blockState.getValue(FACING).getOpposite(), false);
        super.onRemove(blockState, level, pos, blockState1, b);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return TFMGShapes.BLAST_FURNACE_REINFORCEMENT_WALL.get(p_60555_.getValue(FACING));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighbor, boolean b) {

        BlockState stateBehind = level.getBlockState(pos.relative(state.getValue(FACING).getOpposite()));

        if(stateBehind.is(TFMGBlocks.FIREPROOF_BRICKS.get()))
            changeFireproofBricks((Level) level, pos, state.getValue(FACING).getOpposite(), true);


        super.neighborChanged(state, level, pos, block, neighbor, b);
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        BlockState stateBehind = level.getBlockState(pos.relative(state.getValue(FACING).getOpposite()));

        if(stateBehind.is(TFMGBlocks.FIREPROOF_BRICKS.get()))
            changeFireproofBricks((Level) level, pos, state.getValue(FACING).getOpposite(), false);

        super.onNeighborChange(state, level, pos, neighbor);
    }

    public static void changeFireproofBricks(Level level, BlockPos pos, Direction direction, boolean reinforce){
        BlockState state = level.getBlockState(pos.relative(direction));
        if(reinforce&&state.is(TFMGBlocks.FIREPROOF_BRICKS.get())){
            level.setBlock(pos.relative(direction), TFMGBlocks.REINFORCED_FIREPROOF_BRICKS.getDefaultState(), 2);
        }
        if(!reinforce&&state.is(TFMGBlocks.REINFORCED_FIREPROOF_BRICKS.get())){

            level.setBlock(pos.relative(direction), TFMGBlocks.FIREPROOF_BRICKS.getDefaultState(), 2);
        }

    }
}
