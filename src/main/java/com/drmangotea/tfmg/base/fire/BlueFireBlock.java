package com.drmangotea.tfmg.base.fire;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;

@NothingNullByDefault
public class BlueFireBlock extends BaseTFMGFireBlock {
    public static final MapCodec<BlueFireBlock> CODEC = simpleCodec(BlueFireBlock::new);

    public BlueFireBlock(Properties p) {
        super(p, 1.0F);
    }

    @Override
    protected MapCodec<? extends BaseFireBlock> codec() {
        return CODEC;
    }
	
	public static BlockState getState(BlockGetter level, BlockPos pos) {
		return TFMGColoredFires.BLUE_FIRE.get().getStateForPlacement(level, pos);
	}
}
