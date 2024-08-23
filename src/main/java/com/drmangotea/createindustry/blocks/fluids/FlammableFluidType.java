package com.drmangotea.createindustry.blocks.fluids;


import com.mojang.math.Vector3f;
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

import java.util.function.Supplier;

public class FlammableFluidType extends AllFluids.TintedFluidType {

		private Vector3f fogColor;
		private Supplier<Float> fogDistance;

		public static FluidBuilder.FluidTypeFactory  create(int fogColor, Supplier<Float> fogDistance) {
			return (p, s, f) -> {
				FlammableFluidType fluidType = new FlammableFluidType(p, s, f);
				fluidType.fogColor = new Color(fogColor, false).asVectorF();
				fluidType.fogDistance = fogDistance;
				return fluidType;
			};
		}

		private FlammableFluidType(Properties properties, ResourceLocation stillTexture,
                                   ResourceLocation flowingTexture) {
			super(properties, stillTexture, flowingTexture);
		}

		@Override
		protected int getTintColor(FluidStack stack) {
			return NO_TINT;
		}


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






	}