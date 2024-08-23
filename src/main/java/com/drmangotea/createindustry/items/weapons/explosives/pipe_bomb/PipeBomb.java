package com.drmangotea.createindustry.items.weapons.explosives.pipe_bomb;


import com.drmangotea.createindustry.registry.TFMGEntityTypes;
import com.drmangotea.createindustry.registry.TFMGItems;
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

public class PipeBomb extends ThrowableItemProjectile {


    public PipeBomb(EntityType<? extends PipeBomb> entityType, Level level) {
        super(entityType,level);


    }
    public PipeBomb(Level p_37399_, LivingEntity p_37400,EntityType bomb) {
        super(bomb, p_37400, p_37399_);

    }
    public PipeBomb(Level p_37394_, double p_37395_, double p_37396_, double p_37397_) {
        super(TFMGEntityTypes.PIPE_BOMB.get(), p_37395_, p_37396_, p_37397_, p_37394_);
    }


    protected Item getDefaultItem() {
        return TFMGItems.PIPE_BOMB.get();
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



            this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 2.0F, Explosion.BlockInteraction.NONE);
            this.discard();
        }

    }

    @SuppressWarnings("unchecked")
    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        EntityType.Builder<PipeBomb> entityBuilder = (EntityType.Builder<PipeBomb>) builder;
        return entityBuilder.sized(.25f, .25f);
    }
}