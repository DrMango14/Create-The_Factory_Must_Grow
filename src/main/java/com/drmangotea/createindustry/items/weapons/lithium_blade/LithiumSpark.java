package com.drmangotea.createindustry.items.weapons.lithium_blade;


import com.drmangotea.createindustry.items.weapons.explosives.thermite_grenades.fire.BlueFireBlock;
import com.drmangotea.createindustry.registry.TFMGEntityTypes;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.drmangotea.createindustry.registry.TFMGMobEffects;
import com.simibubi.create.content.trains.CubeParticleData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class LithiumSpark extends ThrowableProjectile {
    public LithiumSpark(EntityType<? extends LithiumSpark> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);

    }
    public LithiumSpark(Level p_37399_, LivingEntity p_37400_) {
        super(TFMGEntityTypes.LITHIUM_SPARK.get(), p_37400_, p_37399_);
    }

    public LithiumSpark(Level p_37394_, double p_37395_, double p_37396_, double p_37397_) {
        super(TFMGEntityTypes.LITHIUM_SPARK.get(), p_37395_, p_37396_, p_37397_, p_37394_);
    }


    @Override
    protected float getGravity(){
        return 0.02f;
    }
    @Override
    protected void defineSynchedData() {

    }

    public void tick(){
        super.tick();
        if (this.isInWaterOrRain()) {
            this.discard();
        }
        if(this.level.isClientSide) {

            CubeParticleData data =
                    new CubeParticleData(100, 0, 0, .0125f + .0625f * random.nextFloat(), 30, false);
            level.addParticle(data, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, -this.getDeltaMovement().y * 0.5D, this.random.nextGaussian() * 0.05D);

        }
    }


    private ParticleOptions getParticle() {

        return ParticleTypes.FLAME;
    }


    public void burst(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        Vec3 vec3 = (new Vec3(pX, pY, pZ)).normalize().add(this.random.triangle(0.0D, 0.0172275D * (double)pInaccuracy), 0, this.random.triangle(0.0D, 0.0172275D * (double)pInaccuracy)).scale((double)pVelocity);
        this.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
        this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public void handleEntityEvent(byte p_37402_) {
        if (p_37402_ == 3) {
            ParticleOptions particleoptions = this.getParticle();

            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }
    protected void onHitBlock(BlockHitResult p_37384_) {
        super.onHitBlock(p_37384_);

    }

    protected void onHitEntity(EntityHitResult p_37386_) {
        super.onHitEntity(p_37386_);
        if (!this.level.isClientSide) {
            Entity entity = p_37386_.getEntity();
            Entity entity1 = this.getOwner();


            if(entity instanceof LivingEntity){

                ((LivingEntity)entity).addEffect(new MobEffectInstance(TFMGMobEffects.HELLFIRE.get(),400));


            }


        }
    }

    protected void onHit(HitResult p_37406_) {
        super.onHit(p_37406_);

        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)3);


            //this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 2.0F, Explosion.BlockInteraction.NONE);
            this.discard();
        }

    }

    @SuppressWarnings("unchecked")
    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        EntityType.Builder<LithiumSpark> entityBuilder = (EntityType.Builder<LithiumSpark>) builder;
        return entityBuilder.sized(.25f, .25f);
    }
}