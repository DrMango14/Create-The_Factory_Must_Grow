package com.drmangotea.tfmg.base.datagen;

import com.drmangotea.tfmg.CreateTFMG;
import com.drmangotea.tfmg.registry.TFMGDamageTypes;
import com.simibubi.create.AllDamageTypes;
import com.simibubi.create.Create;
import com.simibubi.create.infrastructure.worldgen.AllBiomeModifiers;
import com.simibubi.create.infrastructure.worldgen.AllConfiguredFeatures;
import com.simibubi.create.infrastructure.worldgen.AllPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.RegistrySetBuilder.RegistryBootstrap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TFMGGeneratedEntriesProvider extends DatapackBuiltinEntriesProvider {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.DAMAGE_TYPE, TFMGDamageTypes::bootstrap);

	public TFMGGeneratedEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BUILDER, Set.of(CreateTFMG.MOD_ID));

	}

	@Override
	public String getName() {
		return "TFMG's Generated Registry Entries";
	}
}
