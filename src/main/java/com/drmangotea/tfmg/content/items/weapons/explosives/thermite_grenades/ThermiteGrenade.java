package com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.base.spark.Spark;
import com.drmangotea.tfmg.registry.TFMGEntityTypes;
import com.drmangotea.tfmg.registry.TFMGItems;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import java.util.function.Function;

@NothingNullByDefault
public class ThermiteGrenade extends ThrowableItemProjectile {
    public final ChemicalColor flameColor;

    public ThermiteGrenade(EntityType<? extends ThermiteGrenade> type, Level level, ChemicalColor color) {
        super(type, level);
        this.flameColor = color;
    }

    public ThermiteGrenade(Level level, LivingEntity entity, ChemicalColor color, EntityType<? extends ThrowableItemProjectile> type) {
        super(type, entity, level);
        this.flameColor = color;
    }

    protected Item getDefaultItem() {
        return TFMGItems.THERMITE_GRENADE.get();
    }

    private ParticleOptions getParticle() {
        return ParticleTypes.FLAME;
    }

    public void handleEntityEvent(byte id) {
        if (id == 3) {
			if (!level().isClientSide) return;
			ParticleOptions particleoptions = this.getParticle();
			for (int i = 0; i < 8; ++i) {
				level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
        }

    }
	
	protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
		
		if (level().isClientSide) return;
		level().broadcastEntityEvent(this, (byte) 3);
		
		Function<Level, ? extends Spark> sparkFactory = switch (flameColor) {
			case BASE  -> TFMGEntityTypes.SPARK::create;
			case GREEN -> TFMGEntityTypes.GREEN_SPARK::create;
			case BLUE  -> TFMGEntityTypes.BLUE_SPARK::create;
		};
		
		RandomSource random = level().getRandom();
		for (int i = 0; i < 20; i++) {
			double
				x = random.nextDouble() * Math.TAU,
				y = random.nextDouble() * Math.TAU,
				z = random.nextDouble() * Math.TAU,
				f = -Math.sin(y) * Math.cos(x),
				f1 = -Math.sin(x + z),
				f2 = Math.cos(y) * Math.cos(x);
				
			Spark spark = sparkFactory.apply(level());
			if (spark == null) continue;
			spark.moveTo(this.getX(), this.getY() + 1, this.getZ());
			spark.shoot(f, f1, f2, 0.2f, 1);
			level().addFreshEntity(spark);
		}
		
		level().explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 2.0F, Level.ExplosionInteraction.NONE);
		this.discard();
    }

    public static EntityType.Builder<? extends ThermiteGrenade> build(EntityType.Builder<? extends ThermiteGrenade> builder) {
        return builder.sized(.25f, .25f);
    }

    public enum ChemicalColor {
        BASE,
        GREEN,
        BLUE
    }
}