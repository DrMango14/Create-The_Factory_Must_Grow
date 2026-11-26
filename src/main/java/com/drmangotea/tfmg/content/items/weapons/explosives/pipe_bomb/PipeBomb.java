package com.drmangotea.tfmg.content.items.weapons.explosives.pipe_bomb;

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
public class PipeBomb extends ThrowableItemProjectile {
    public PipeBomb(EntityType<? extends PipeBomb> entityType, Level level) {
        super(entityType, level);
    }
	
    public PipeBomb(LivingEntity shooter, Level level) {
		super(TFMGEntityTypes.PIPE_BOMB.get(), shooter, level);
    }

    protected Item getDefaultItem() {
        return TFMGItems.PIPE_BOMB.get();
    }

    private ParticleOptions getParticle() {

        return ParticleTypes.FLAME;
    }

    public void handleEntityEvent(byte id) {
        if (id == 3) {
			Level level = level();
				ParticleOptions particleoptions = this.getParticle();
				for (int i = 0; i < 8; ++i) {
					level.addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
				}
        }
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
		Level level = level();
			if (level.isClientSide) return;
            level.broadcastEntityEvent(this, (byte) 3);
            level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 2.0F, Level.ExplosionInteraction.NONE);
            this.discard();
    }

    public static EntityType.Builder<?> build(EntityType.Builder<? extends PipeBomb> builder) {
        return builder.sized(.25f, .25f);
    }
}