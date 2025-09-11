package com.drmangotea.tfmg.base.spark;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.registry.TFMGMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.stream.Stream;

public class DryIceFlake extends Spark{
    public DryIceFlake(EntityType<? extends Spark> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }
    protected void onHitBlock(BlockHitResult blockHit) {
        if (!this.level().isClientSide) {
            Entity owner = this.getOwner();
            if (!(owner instanceof Mob) ) {
                BlockPos hitBlockPos = blockHit.getBlockPos().relative(blockHit.getDirection());

                AABB extinguisRadius = new AABB(hitBlockPos);
                extinguisRadius = extinguisRadius.inflate(TFMGConfigs.common().machines.fireExtinguisherClearRadius.get());

                Stream<BlockPos> positions = BlockPos.betweenClosedStream(extinguisRadius);

                positions.forEach(p->{
                    BlockState state = level().getBlockState(p);

                    if (state.getBlock() instanceof BaseFireBlock) {
                        this.level().setBlockAndUpdate(p, Blocks.AIR.defaultBlockState());
                    }
                });


            }
        }
    }

    protected void onHitEntity(EntityHitResult entityHit) {
        if (!this.level().isClientSide) {
            Entity hitEntity = entityHit.getEntity();
            hitEntity.extinguishFire();

            if (hitEntity.canFreeze()) {
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
