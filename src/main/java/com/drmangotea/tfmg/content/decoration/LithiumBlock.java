package com.drmangotea.tfmg.content.decoration;

import com.drmangotea.tfmg.registry.TFMGEntityTypes;
import com.simibubi.create.Create;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class LithiumBlock extends Block {
    public LithiumBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public boolean isRandomlyTicking(BlockState p_49921_) {
        return true;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        super.randomTick(blockState, level, pos, randomSource);



    }
}
