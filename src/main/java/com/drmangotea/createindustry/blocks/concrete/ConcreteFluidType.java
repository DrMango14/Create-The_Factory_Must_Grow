package com.drmangotea.createindustry.blocks.concrete;




import com.simibubi.create.AllFluids;
import com.simibubi.create.foundation.utility.Color;
import com.tterrag.registrate.builders.FluidBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Vector3f;


import java.util.function.Supplier;

public class ConcreteFluidType extends AllFluids.TintedFluidType {

		private Vector3f fogColor;
		private Supplier<Float> fogDistance;

		public static FluidBuilder.FluidTypeFactory  create(int fogColor, Supplier<Float> fogDistance) {
			return (p, s, f) -> {
				ConcreteFluidType fluidType = new ConcreteFluidType(p, s, f);
				fluidType.fogColor = new Color(fogColor, false).asVectorF();
				fluidType.fogDistance = fogDistance;
				return fluidType;
			};
		}

		private ConcreteFluidType(Properties properties, ResourceLocation stillTexture,
								  ResourceLocation flowingTexture) {
			super(properties, stillTexture, flowingTexture);
		}

		@Override
		protected int getTintColor(FluidStack stack) {
			return NO_TINT;
		}

		/*
		 * Removing alpha from tint prevents optifine from forcibly applying biome
		 * colors to modded fluids (this workaround only works for fluids in the solid
		 * render layer)
		 */
		@Override
		public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
			return 0x00ffffff;
		}
		
		@Override
		protected Vector3f getCustomFogColor() {
			return fogColor;
		}
		
		@Override
		protected float getFogDistanceModifier() {
			return fogDistance.get();
		}

	@Override
	public boolean canSwim(Entity entity)
	{
		return false;
	}
	@Override
	public float getFallDistanceModifier(Entity entity)
	{
		return 1;
	}
	@Override
	public boolean move(FluidState state, LivingEntity entity, Vec3 movementVector, double gravity)
	{
		entity.setDeltaMovement(entity.getDeltaMovement().scale(0.2d));
		//entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 5, true, false, false));
		return false;
	}


	}