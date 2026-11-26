package com.drmangotea.tfmg.datagen.integration;

import com.drmangotea.tfmg.TFMG;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class TFMGCuriosProvider extends CuriosDataProvider {
	public TFMGCuriosProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper fileHelper) {
		super(TFMG.MOD_ID, output, fileHelper, registries);
	}
	
	@Override
	public void generate(Provider registries, ExistingFileHelper fileHelper) {
		createEntities("players")
			.addPlayer()
			.addSlots("belt");
	}
}
