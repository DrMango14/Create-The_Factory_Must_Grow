package com.drmangotea.tfmg.base.fire;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;

@NothingNullByDefault
public class GreenFireBlock extends BaseTFMGFireBlock {
    public static final MapCodec<GreenFireBlock> CODEC = simpleCodec(GreenFireBlock::new);

    public GreenFireBlock(Properties p) {
        super(p, 1.0F);
    }

    @Override
    protected MapCodec<? extends BaseFireBlock> codec() {
        return CODEC;
    }
	
	public static BlockState getState(BlockGetter level, BlockPos pos) {
		return TFMGColoredFires.GREEN_FIRE.get().getStateForPlacement(level, pos);
	}
}
