package com.drmangotea.createindustry.items.weapons.explosives.thermite_grenades;


import com.drmangotea.createindustry.base.util.spark.BlueSpark;
import com.drmangotea.createindustry.base.util.spark.GreenSpark;
import com.drmangotea.createindustry.base.util.spark.Spark;
import com.drmangotea.createindustry.registry.TFMGEntityTypes;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.simibubi.create.Create;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThermiteGrenade extends ThrowableItemProjectile {
    public final ChemicalColor flameColor;

    public ThermiteGrenade(EntityType<? extends ThermiteGrenade> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
        this.flameColor =ChemicalColor.BLUE;
    }





    public ThermiteGrenade(Level p_37399_, LivingEntity p_37400_, ChemicalColor color,EntityType grenade) {
        super(grenade, p_37400_, p_37399_);
        this.flameColor = color;
    }

    public ThermiteGrenade(Level p_37394_, double p_37395_, double p_37396_, double p_37397_) {
        super(TFMGEntityTypes.THERMITE_GRENADE.get(), p_37395_, p_37396_, p_37397_, p_37394_);
        this.flameColor =ChemicalColor.BLUE;
    }

    protected Item getDefaultItem() {
        return TFMGItems.THERMITE_GRENADE.get();
    }

    private ParticleOptions getParticle() {

        return ParticleTypes.FLAME;
    }

    public void handleEntityEvent(byte p_37402_) {
        if (p_37402_ == 3) {
            ParticleOptions particleoptions = this.getParticle();

            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    protected void onHitEntity(EntityHitResult p_37404_) {
        super.onHitEntity(p_37404_);
        Entity entity = p_37404_.getEntity();
        entity.hurt(DamageSource.thrown(this, this.getOwner()), 1);
    }

    protected void onHit(HitResult p_37406_) {
        super.onHit(p_37406_);

        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte) 3);

            for (int i=0; i<20;i++){
                float x= Create.RANDOM.nextFloat(360);
                float y= Create.RANDOM.nextFloat(360);
                float z= Create.RANDOM.nextFloat(360);

                if(flameColor==ChemicalColor.GREEN){
                    GreenSpark spark = TFMGEntityTypes.GREEN_SPARK.create(level);
                    spark.moveTo(this.getX(), this.getY()+1, this.getZ());
                    spark.shootFromRotation( this,x,y,z,0.2f,1);
                    this.level.addFreshEntity(spark);
                }else
                if(flameColor==ChemicalColor.BLUE){
                    BlueSpark spark = TFMGEntityTypes.BLUE_SPARK.create(level);
                    spark.moveTo(this.getX(), this.getY()+1, this.getZ());
                    spark.shootFromRotation( this,x,y,z,0.2f,1);
                    this.level.addFreshEntity(spark);
                } else {                Spark spark = TFMGEntityTypes.SPARK.create(level);
                    spark.moveTo(this.getX(), this.getY()+1, this.getZ());
                    spark.shootFromRotation( this,x,y,z,0.2f,1);
                    this.level.addFreshEntity(spark);}







        }


            this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 2.0F, Explosion.BlockInteraction.NONE);
            this.discard();
        }

    }

    @SuppressWarnings("unchecked")
    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        EntityType.Builder<ThermiteGrenade> entityBuilder = (EntityType.Builder<ThermiteGrenade>) builder;
        return entityBuilder.sized(.25f, .25f);
    }
}