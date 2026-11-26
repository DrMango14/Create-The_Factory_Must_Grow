package com.drmangotea.tfmg.content.items.weapons.explosives.napalm;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.registry.TFMGEntityTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

@NothingNullByDefault
public class NapalmBombEntity extends Entity {
    private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(NapalmBombEntity.class, EntityDataSerializers.INT);
    private @Nullable LivingEntity owner;

    public NapalmBombEntity(EntityType<? extends NapalmBombEntity> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    public NapalmBombEntity(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        this(TFMGEntityTypes.NAPALM_BOMB.get(), level);
        this.setPos(x, y, z);
        double d0 = level.random.nextDouble() * Math.TAU;
        this.setDeltaMovement(-Math.sin(d0) * 0.02D, 0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = owner;
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_FUSE_ID, 80);
    }

    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    public boolean isPickable() {
        return !this.isRemoved();
    }

    public void tick() {
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
        }

        int i = this.getFuse() - 1;
        this.setFuse(i);
        if (i <= 0) {
            this.discard();
            if (!this.level().isClientSide) {
                this.explode();
            }
        } else {
            this.updateInWaterStateAndDoFluidPushing();
            if (this.level().isClientSide) {
                this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    protected void explode() {
        TFMGUtils.createFireExplosion(level(), this, position(), 40, 2.5f);
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putShort("Fuse", (short)this.getFuse());
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setFuse(tag.getShort("Fuse"));
    }

    @Nullable
    public LivingEntity getOwner() {
        return this.owner;
    }

    public void setFuse(int fuse) {
        this.entityData.set(DATA_FUSE_ID, fuse);
    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE_ID);
    }


    public static EntityType.Builder<? extends NapalmBombEntity> build(EntityType.Builder<? extends NapalmBombEntity> builder) {
        return builder.sized(.25f, .25f);
    }
}