package com.drmangotea.tfmg.base.spark;


import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGMobEffects;
import com.simibubi.create.content.trains.CubeParticleData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class Spark extends ThrowableProjectile {

    public float gravity = 0.02f;

    public Spark(EntityType<? extends Spark> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }


    @Override
    protected double getDefaultGravity() {
        return gravity;
    }

    public Optional<BlockState> getFireState(BlockPos pos){
        return Optional.of(BaseFireBlock.getState(this.level(), pos));
    }



    public float[] getCustomParticleTrail() {
        return new float[]{0,0,0};
    }

    public ParticleOptions getTrailParticle(){
        return ParticleTypes.FLAME;
    }

    public ResourceLocation getTexture(){
        return TFMG.asResource("textures/entity/spark.png");
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    public void burst(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        Vec3 vec3 = (new Vec3(pX, pY, pZ)).normalize().add(this.random.triangle(0.0D, 0.0172275D * (double) pInaccuracy), 0, this.random.triangle(0.0D, 0.0172275D * (double) pInaccuracy)).scale((double) pVelocity);
        this.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI)));
        this.setXRot((float) (Mth.atan2(vec3.y, d0) * (double) (180F / (float) Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }


    public void tick() {
        super.tick();
        if (this.isInWaterOrRain()) {
            this.discard();
        }

        if (level().isClientSide) {
            if (getCustomParticleTrail()[0] == 0 && getCustomParticleTrail()[1] == 0 && getCustomParticleTrail()[2] == 0) {
                this.level().addParticle(getTrailParticle(), this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, -this.getDeltaMovement().y * 0.5D, this.random.nextGaussian() * 0.05D);
            } else {
                CubeParticleData data =
                        new CubeParticleData(getCustomParticleTrail()[0], getCustomParticleTrail()[1], getCustomParticleTrail()[2], .0125f + .0625f * random.nextFloat(), 30, true);
                level().addParticle(data, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, -this.getDeltaMovement().y * 0.5D, this.random.nextGaussian() * 0.05D);
            }
        }

    }

    private ParticleOptions getParticle() {
        return getTrailParticle();
    }

    public void handleEntityEvent(byte p_37402_) {
        if (p_37402_ == 3) {
            ParticleOptions particleoptions = this.getParticle();

            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }


    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (!this.level().isClientSide) {
            Entity entity = this.getOwner();
            if (!(entity instanceof Mob)) {
                BlockPos blockpos = hitResult.getBlockPos().relative(hitResult.getDirection());
                if (this.level().isEmptyBlock(blockpos)&&getFireState(blockpos).isPresent()) {
                    this.level().setBlockAndUpdate(blockpos, getFireState(blockpos).get());
                }
            }
        }
    }

    public int getColor(){
        return 0xFFFF8C;
    }

    protected void onHitEntity(EntityHitResult p_37386_) {
        super.onHitEntity(p_37386_);
        if (!this.level().isClientSide) {
            Entity entity = p_37386_.getEntity();
            entity.igniteForSeconds(4.0F);
            if (entity.getRemainingFireTicks() > 0 && !entity.fireImmune()) {
                entity.hurt(this.damageSources().onFire(), 1.0F);
            }
            if (this instanceof LithiumSpark)
                if (entity instanceof LivingEntity) {

                    ((LivingEntity) entity).addEffect(new MobEffectInstance(TFMGMobEffects.HELLFIRE, 60));


                }
        }
    }

    protected void onHit(HitResult p_37406_) {
        super.onHit(p_37406_);

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @SuppressWarnings("unchecked")
    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        EntityType.Builder<Spark> entityBuilder = (EntityType.Builder<Spark>) builder;
        return entityBuilder.sized(.25f, .25f);
    }



}