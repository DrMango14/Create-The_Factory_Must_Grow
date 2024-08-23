package com.drmangotea.createindustry.base;

import com.drmangotea.createindustry.registry.TFMGParticleTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.content.equipment.bell.BasicParticleData;
import com.simibubi.create.content.equipment.bell.CustomRotationParticle;
import com.simibubi.create.content.equipment.bell.SoulPulseEffect;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class ElectricSparkParticle extends CustomRotationParticle {

	private final SpriteSet animatedSprite;

	protected int startTicks;
	protected int endTicks;
	protected int numLoops;

	protected int startFrames = 17;


	protected int loopFrames = 16;


	protected int endFrames = 20;



	protected int totalFrames = 53;


	public ElectricSparkParticle(ClientLevel worldIn, double x, double y, double z, double vx, double vy, double vz,
								 SpriteSet spriteSet, ParticleOptions data) {
		super(worldIn, x, y, z, spriteSet, 0);
		this.animatedSprite = spriteSet;
		this.quadSize = 0.5f;
		this.setSize(this.quadSize, this.quadSize);

		this.loopLength = loopFrames + (int) (this.random.nextFloat() * 5f - 4f);
		this.startTicks = startFrames + (int) (this.random.nextFloat() * 5f - 4f);
		this.endTicks = endFrames + (int) (this.random.nextFloat() * 5f - 4f);
		this.numLoops = (int) (1f + this.random.nextFloat() * 2f);

		this.setFrame(0);
		this.mirror = this.random.nextBoolean();


	}



	public void setFrame(int frame) {
		if (frame >= 0 && frame < totalFrames)
			setSprite(animatedSprite.get(frame, totalFrames));
	}



	public static class Data extends BasicParticleData<ElectricSparkParticle> {
		@Override
		public IBasicParticleFactory<ElectricSparkParticle> getBasicFactory() {
			return (worldIn, x, y, z, vx, vy, vz, spriteSet) -> new ElectricSparkParticle(worldIn, x, y, z, vx, vy, vz,
				spriteSet, this);
		}

		@Override
		public ParticleType<?> getType() {
			return TFMGParticleTypes.ELECTRIC_SPARK.get();
		}
	}


}
