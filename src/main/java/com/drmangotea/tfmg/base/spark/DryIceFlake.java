package com.drmangotea.tfmg.base.spark;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.registry.TFMGMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

@NothingNullByDefault
public class DryIceFlake extends Spark {
    public DryIceFlake(EntityType<? extends Spark> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }
	
    protected void onHitBlock(BlockHitResult blockHit) {
		if (level().isClientSide || this.getOwner() instanceof Mob) return;
		
		BlockPos.betweenClosedStream(
			new AABB(blockHit.getBlockPos().relative(blockHit.getDirection()))
				.inflate(TFMGConfigs.common().equipment.fireExtinguisherClearRadius.get())
		).forEach(p -> {
			if (level().getBlockState(p).getBlock() instanceof BaseFireBlock) {
				level().setBlockAndUpdate(p, Blocks.AIR.defaultBlockState());
			}
		});
    }

    protected void onHitEntity(EntityHitResult hitResult) {
		if (level().isClientSide) return;
		Entity hitEntity = hitResult.getEntity();
		hitEntity.extinguishFire();
		
		if (!hitEntity.canFreeze()) return;
		
		// Apply freezing
		int currentFreeze = hitEntity.getTicksFrozen();
		int freezeIncrement = 10;
		int newFreeze = Math.min(currentFreeze + freezeIncrement, hitEntity.getTicksRequiredToFreeze() + 20); // Slightly overfreeze
		hitEntity.setTicksFrozen(newFreeze);
		
		// Damage if fully frozen
		if (newFreeze >= hitEntity.getTicksRequiredToFreeze()) {
			hitEntity.hurt(this.damageSources().freeze(), 2.0F);
		}
		
		// Slow movement and remove hellfire
		if (hitEntity instanceof LivingEntity livingEntity) {
			livingEntity.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN,
				20,
				2,
				false, false, true
			));
			
			livingEntity.removeEffect(TFMGMobEffects.HELLFIRE);
		}
    }

    @Override
    public int getColor() {
        return 0xFFFFFF;
    }

    @Override
    public ResourceLocation getTexture() {
        return TFMG.asResource("textures/entity/dry_ice_flake.png");
    }

    public ParticleOptions getTrailParticle(){
        return ParticleTypes.SNOWFLAKE;
    }
}
