package com.drmangotea.tfmg.base.spark;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.simibubi.create.content.trains.CubeParticleData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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

@NothingNullByDefault
public class Spark extends ThrowableProjectile {

    public float gravity = 0.02f;

    public Spark(EntityType<? extends Spark> type, Level level) {
        super(type, level);
    }

    @Override
    protected double getDefaultGravity() {
        return gravity;
    }

    public Optional<BlockState> getFireState(BlockPos pos) {
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

    public void burst(Vec3 look, float pVelocity, double pInaccuracy) {
        Vec3 vec3 = look.normalize()
			.add(
				this.random.triangle(0.0D, 0.0172275D * pInaccuracy),
				0,
				this.random.triangle(0.0D, 0.0172275D * pInaccuracy)
			).scale(pVelocity);
        this.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        this.setYRot((float) Mth.atan2(vec3.x, vec3.z) * Mth.RAD_TO_DEG);
        this.setXRot((float) Mth.atan2(vec3.y, d0) * Mth.RAD_TO_DEG);
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

		if (!level().isClientSide) return;
		float[] trail = getCustomParticleTrail();
		if (trail[0] == 0 && trail[1] == 0 && trail[2] == 0) {
			level().addParticle(getTrailParticle(), this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, -this.getDeltaMovement().y * 0.5D, this.random.nextGaussian() * 0.05D);
		} else {
			CubeParticleData data =
				new CubeParticleData(trail[0], trail[1], trail[2], .0125f + .0625f * random.nextFloat(), 30, true);
			level().addParticle(data, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, -this.getDeltaMovement().y * 0.5D, this.random.nextGaussian() * 0.05D);
		}
    }

    private ParticleOptions getParticle() {
        return getTrailParticle();
    }

    public void handleEntityEvent(byte id) {
        if (id == 3) {
			ParticleOptions particleoptions = this.getParticle();
			for (int i = 0; i < 8; ++i) {
				level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
        }
    }

    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
		if (level().isClientSide || this.getOwner() instanceof Mob) return;
		BlockPos blockpos = hitResult.getBlockPos().relative(hitResult.getDirection());
		if (level().isEmptyBlock(blockpos)) {
			getFireState(blockpos).ifPresent(state -> level().setBlockAndUpdate(blockpos, state));
		}
	}

    public int getColor(){
        return 0xFFFF8C;
    }

    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
		if (level().isClientSide) return;
		//ignite
		Entity entity = hitResult.getEntity();
		entity.igniteForSeconds(4.0F);
		if (entity.getRemainingFireTicks() > 0 && !entity.fireImmune()) {
			entity.hurt(this.damageSources().onFire(), 1.0F);
		}
		//just for Lithium Sparks atm, but no longer hardcoded
		if (entity instanceof LivingEntity living) {
			onHitLiving(living);
		}
    }
	
	protected void onHitLiving(LivingEntity living) { }

    protected void onHit(HitResult p_37406_) {
        super.onHit(p_37406_);

        if (level().isClientSide) return;
		level().broadcastEntityEvent(this, (byte) 3);
		this.discard();
    }

    public static EntityType.Builder<? extends Spark> build(EntityType.Builder<? extends Spark> builder) {
        return builder.sized(.25f, .25f);
    }

}