package com.drmangotea.createindustry.config;

import com.drmangotea.createindustry.CreateTFMG;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.BlockStressValues.IStressValueProvider;
import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class StressConfig extends ConfigBase implements IStressValueProvider {

	private final Map<ResourceLocation, ConfigValue<Double>> capacities = new HashMap<>();
	private final Map<ResourceLocation, ConfigValue<Double>> impacts = new HashMap<>();

	@Override
	public void registerAll(Builder builder) {
		builder.comment(".", Comments.su, Comments.impact)
				.push("impact");
		BlockStressDefaults.DEFAULT_IMPACTS.forEach((r, i) -> {
			if (r.getNamespace()
					.equals(CreateTFMG.MOD_ID))
				getImpacts().put(r, builder.define(r.getPath(), i));
		});
		builder.pop();

		builder.comment(".", Comments.su, Comments.capacity)
				.push("capacity");
		BlockStressDefaults.DEFAULT_CAPACITIES.forEach((r, i) -> {
			if (r.getNamespace()
					.equals(CreateTFMG.MOD_ID))
				getCapacities().put(r, builder.define(r.getPath(), i));
		});
		builder.pop();
	}

	@Override
	public String getName() {
		return "stressValues";
	}

	@Override
	public double getImpact(Block block) {
		block = redirectValues(block);
		ResourceLocation key = RegisteredObjects.getKeyOrThrow(block);
		ConfigValue<Double> value = getImpacts().get(key);
		if(value != null) return value.get();
		return 0;
	}

	@Override
	public double getCapacity(Block block) {
		block = redirectValues(block);
		ResourceLocation key = RegisteredObjects.getKeyOrThrow(block);
		ConfigValue<Double> value = getCapacities().get(key);
		if(value != null) return value.get();
		return 0;
	}

	@Override
	public boolean hasImpact(Block block) {
		block = redirectValues(block);
		ResourceLocation key = RegisteredObjects.getKeyOrThrow(block);
		return getImpacts().containsKey(key);
	}

	@Override
	public boolean hasCapacity(Block block) {
		block = redirectValues(block);
		ResourceLocation key = RegisteredObjects.getKeyOrThrow(block);
		return getCapacities().containsKey(key);
	}

	@Nullable
	@Override
	public Couple<Integer> getGeneratedRPM(Block block) {
		block = redirectValues(block);
		ResourceLocation key = RegisteredObjects.getKeyOrThrow(block);
		Supplier<Couple<Integer>> supplier = BlockStressDefaults.GENERATOR_SPEEDS.get(key);
		if(supplier == null) return null;
		return supplier.get();
	}

	protected Block redirectValues(Block block) {
		return block;
	}

	public Map<ResourceLocation, ConfigValue<Double>> getImpacts() {
		return impacts;
	}

	public Map<ResourceLocation, ConfigValue<Double>> getCapacities() {
		return capacities;
	}

	private static class Comments {
		static String su = "[in Stress Units]";
		static String impact = "Configure the individual stress impact of mechanical blocks. Note that this cost is doubled for every speed increase it receives";
		static String capacity = "Configure how much stress a source can accommodate for.";
	}
}
