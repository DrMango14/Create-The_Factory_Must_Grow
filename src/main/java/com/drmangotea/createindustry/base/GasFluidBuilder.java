package com.drmangotea.createindustry.base;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;

public class GasFluidBuilder<T extends ForgeFlowingFluid, P> extends FluidBuilder<T, P> {

	public GasFluidBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
						   ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory,
						   NonNullFunction<Properties, T> factory) {
		super(owner, parent, name, callback, stillTexture, flowingTexture, typeFactory, factory);
		source(factory);
	}

	@Override
	public NonNullSupplier<T> asSupplier() {
		return this::getEntry;
	}



}