package com.drmangotea.tfmg.base.fire;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.registry.TFMGMobEffects;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;

@NothingNullByDefault
public class LithiumFireBlock extends BaseTFMGFireBlock {
    public static final MapCodec<LithiumFireBlock> CODEC = simpleCodec(LithiumFireBlock::new);

    public LithiumFireBlock(Properties p) {
        super(p, 1.0F);
    }
	
	@Override
	protected MapCodec<? extends BaseFireBlock> codec() {
		return CODEC;
	}
	
	public static BlockState getState(BlockGetter level, BlockPos pos) {
		return TFMGColoredFires.LITHIUM_FIRE.get().getStateForPlacement(level, pos);
	}

    public void entityInside(BlockState blockState, Level level, BlockPos pos, Entity entity) {
        if (!entity.fireImmune()) {
            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
            if (entity.getRemainingFireTicks() == 0) {
                entity.setRemainingFireTicks(8);
            }
            if(entity instanceof LivingEntity livingEntity)
                livingEntity.addEffect(new MobEffectInstance(TFMGMobEffects.HELLFIRE, 20*15));
        }

        entity.hurt(level.damageSources().inFire(), 2);
        super.entityInside(blockState, level, pos, entity);
    }
}
