package com.drmangotea.tfmg.base.spark;

import com.drmangotea.tfmg.registry.TFMGEntityTypes;
import com.drmangotea.tfmg.registry.TFMGMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class DryIceFlake extends ThrowableProjectile {
    public DryIceFlake(EntityType<? extends DryIceFlake> entityType, Level level) {
        super(entityType, level);
    }
    public DryIceFlake(Level level, LivingEntity owner) {
        super(TFMGEntityTypes.DRY_ICE_FLAKE.get(), owner, level);
    }
    public DryIceFlake(Level level, double xCoord, double yCoord, double zCoord) {
        super(TFMGEntityTypes.DRY_ICE_FLAKE.get(), xCoord, yCoord, zCoord, level);
    }

    @Override
    protected float getGravity(){
        return 0.01f;
    }
    @Override
    protected void defineSynchedData() {}

    public void tick(){
        super.tick();

        if (this.tickCount > 20) this.discard();

        if(this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, -this.getDeltaMovement().y * 0.5D, this.random.nextGaussian() * 0.05D);
        }
    }

    private ParticleOptions getParticle() {
        return ParticleTypes.SNOWFLAKE;
    }

    public void handleEntityEvent(byte p_37402_) {
        if (p_37402_ == 3) {
            ParticleOptions particleoptions = this.getParticle();

            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }
    protected void onHitBlock(BlockHitResult blockHit) {
        super.onHitBlock(blockHit);
        if (!this.level().isClientSide) {
            Entity owner = this.getOwner();
            if (!(owner instanceof Mob) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                BlockPos hitBlockPos = blockHit.getBlockPos().relative(blockHit.getDirection());
                BlockState hitBlockState = this.level().getBlockState(hitBlockPos);
                if (hitBlockState.getBlock() instanceof BaseFireBlock) {
                    this.level().setBlockAndUpdate(hitBlockPos, Blocks.AIR.defaultBlockState());
                }
            }
        }
    }

    protected void onHitEntity(EntityHitResult entityHit) {
        super.onHitEntity(entityHit);
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

                    livingEntity.removeEffect(TFMGMobEffects.HELLFIRE.get());
                }
            }
        }
    }

    protected void onHit(HitResult hit) {
        super.onHit(hit);

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @SuppressWarnings("unchecked")
    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        EntityType.Builder<DryIceFlake> entityBuilder = (EntityType.Builder<DryIceFlake>) builder;
        return entityBuilder.sized(.25f, .25f);
    }
}
