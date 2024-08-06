package com.drmangotea.createindustry.registry;

import com.drmangotea.createindustry.CreateTFMG;

import com.drmangotea.createindustry.base.ElectricSparkParticle;
import com.simibubi.create.foundation.particle.ICustomParticleData;
import com.simibubi.create.foundation.utility.Lang;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Supplier;

public enum TFMGParticleTypes {

	ELECTRIC_SPARK(ElectricSparkParticle.Data::new);


	private final ParticleEntry<?> entry;

	<D extends ParticleOptions> TFMGParticleTypes(Supplier<? extends ICustomParticleData<D>> typeFactory) {
		String name = Lang.asId(name());
		entry = new ParticleEntry<>(name, typeFactory);
	}

	public static void register() {
		ParticleEntry.REGISTER.register();
	}

	@Environment(EnvType.CLIENT)
	public static void registerFactories() {
		ParticleEngine particles = Minecraft.getInstance().particleEngine;
		for (TFMGParticleTypes particle : values())
			particle.entry.registerFactory(particles);
	}

	public ParticleType<?> get() {
		//don't question it, fabric is stupid /:
		RegistryObject<? extends ParticleType<?>> object = entry.object;
		return object.get();
	}

	public String parameter() {
		return entry.name;
	}

	private static class ParticleEntry<D extends ParticleOptions> {
		private static final LazyRegistrar<ParticleType<?>> REGISTER = LazyRegistrar.create(Registry.PARTICLE_TYPE, CreateTFMG.MOD_ID);

		private final String name;
		private final Supplier<? extends ICustomParticleData<D>> typeFactory;
		private final RegistryObject<ParticleType<D>> object;

		public ParticleEntry(String name, Supplier<? extends ICustomParticleData<D>> typeFactory) {
			this.name = name;
			this.typeFactory = typeFactory;

			object = REGISTER.register(name, () -> this.typeFactory.get().createType());
		}

		@Environment(EnvType.CLIENT)
		public void registerFactory(ParticleEngine event) {
			typeFactory.get()
				.register(object.get(), event);
		}

	}

}
