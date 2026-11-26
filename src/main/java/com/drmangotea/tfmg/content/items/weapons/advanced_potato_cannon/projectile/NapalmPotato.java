package com.drmangotea.tfmg.content.items.weapons.advanced_potato_cannon.projectile;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.registry.TFMGEntityTypes;
import com.drmangotea.tfmg.registry.TFMGItems;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;


@NothingNullByDefault
public class NapalmPotato extends ThrowableItemProjectile {
    public NapalmPotato(EntityType<? extends NapalmPotato> entityType, Level level) {
        super(entityType, level);
    }

    public NapalmPotato(Level level, LivingEntity shooter, EntityType<? extends ThrowableItemProjectile> type) {
		super(type, shooter, level);
	}
	
    public NapalmPotato(Level level, double x, double y, double z) {
        super(TFMGEntityTypes.NAPALM_POTATO.get(), x, y, z, level);
    }

    protected Item getDefaultItem() {
        return TFMGItems.NAPALM_POTATO.get();
    }

    private ParticleOptions getParticle() {
        return ParticleTypes.FLAME;
    }

    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particleoptions = this.getParticle();
            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            TFMGUtils.createFireExplosion(level(),this, position(),15,2.5f);
            this.discard();
        }
    }

    public static EntityType.Builder<? extends NapalmPotato> build(EntityType.Builder<? extends NapalmPotato> builder) {
        return builder.sized(.25f, .25f);
    }
}