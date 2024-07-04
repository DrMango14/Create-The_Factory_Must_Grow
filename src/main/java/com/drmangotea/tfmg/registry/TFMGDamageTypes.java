package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.CreateTFMG;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.damageTypes.DamageTypeBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class TFMGDamageTypes {
	public static final ResourceKey<DamageType>
			CONCRETE = key("concrete"),
			ACID = key("acid");

	private static ResourceKey<DamageType> key(String name) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, CreateTFMG.asResource(name));
	}

	public static void bootstrap(BootstapContext<DamageType> ctx) {
		new DamageTypeBuilder(CONCRETE).register(ctx);
		new DamageTypeBuilder(ACID).register(ctx);
	}
}
