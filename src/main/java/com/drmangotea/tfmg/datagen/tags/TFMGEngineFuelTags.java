package com.drmangotea.tfmg.datagen.tags;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.content.engines.fuels.EngineFuelType;
import com.drmangotea.tfmg.registry.TFMGEngineFuelTypes;
import com.drmangotea.tfmg.registry.TFMGTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TFMGEngineFuelTags extends TagsProvider<EngineFuelType> {

    public TFMGEngineFuelTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, TFMGRegistries.ENGINE_FUEL_TYPE, lookupProvider, TFMG.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(TFMGTags.EngineFuel.LARGE_ENGINE.tag)
                .add(TFMGEngineFuelTypes.DIESEL, TFMGEngineFuelTypes.KEROSENE, TFMGEngineFuelTypes.NAPHTHA, TFMGEngineFuelTypes.FURNACE_GAS);

        tag(TFMGTags.EngineFuel.SIMPLE_LARGE_ENGINE.tag)
                .add(TFMGEngineFuelTypes.DIESEL, TFMGEngineFuelTypes.KEROSENE, TFMGEngineFuelTypes.NAPHTHA, TFMGEngineFuelTypes.FURNACE_GAS);
    }
}
