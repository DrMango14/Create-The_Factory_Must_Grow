package com.drmangotea.tfmg.base.spark;

import com.drmangotea.tfmg.registry.TFMGParticleTypes;
import com.simibubi.create.content.equipment.bell.BasicParticleData;
import com.simibubi.create.content.equipment.bell.CustomRotationParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

//TODO - Fix clients disconnecting when doing ServerLevel#sendParticles (bad data)
public class ElectricSparkParticle extends CustomRotationParticle {

	protected int startTicks;
	protected int endTicks;
	protected int numLoops;
	protected int startFrames = 17;
	protected int loopFrames = 16;
	protected int endFrames = 20;
	protected int totalFrames = 53;

	public ElectricSparkParticle(ClientLevel worldIn, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
		super(worldIn, x, y, z, spriteSet, 0);
		this.setPos(x, y, z);
		this.quadSize = 0.5f;
		this.setSize(this.quadSize, this.quadSize);
		this.loopLength = loopFrames + (int) ((this.random.nextFloat() * 5f) - 4f);
		this.startTicks = startFrames + (int) ((this.random.nextFloat() * 5f) - 4f);
		this.endTicks = endFrames + (int) ((this.random.nextFloat() * 5f) - 4f);
		this.numLoops = (int) (1f + (this.random.nextFloat() * 2f));
		this.setFrame(0);
		this.mirror = this.random.nextBoolean();
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
			return;
		}
		setSpriteFromAge(sprites);
	}

	public void setFrame(int frame) {
		if (frame >= 0 && frame < totalFrames)
			setSprite(sprites.get(frame, totalFrames));
	}

	public static class Data extends BasicParticleData<ElectricSparkParticle> implements ParticleOptions {
		@Override
		public IBasicParticleFactory<ElectricSparkParticle> getBasicFactory() {
			return ElectricSparkParticle::new;
		}

		@Override
		public ParticleType<?> getType() {
			return TFMGParticleTypes.ELECTRIC_SPARK.get();
		}
	}
}
