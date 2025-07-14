package com.drmangotea.tfmg.datagen;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

public class TFMGDamageSources {

	public static DamageSource concrete(Level level) {
		return source(TFMGDamageTypes.CONCRETE, level);
	}

	public static DamageSource acid(Level level) {
		return source(TFMGDamageTypes.ACID, level);
	}

	public static DamageSource blastFurnace(Level level) {
		return source(TFMGDamageTypes.BLAST_FURNACE, level);
	}



	private static DamageSource source(ResourceKey<DamageType> key, LevelReader level) {
		Registry<DamageType> registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
		return new DamageSource(registry.getHolderOrThrow(key));
	}


}
