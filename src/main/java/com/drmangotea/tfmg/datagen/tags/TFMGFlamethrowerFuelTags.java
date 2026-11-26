package com.drmangotea.tfmg.datagen.tags;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.content.items.weapons.flamethrover.FlamethrowerFuelType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TFMGFlamethrowerFuelTags extends TagsProvider<FlamethrowerFuelType> {

    public TFMGFlamethrowerFuelTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, TFMGRegistries.FLAMETHROWER_FUEL_TYPE, lookupProvider, TFMG.MOD_ID, existingFileHelper);
    }

    /**
     * Implement Later
     */
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        //tag(TFMGTags.FlamethrowerFuel.HELLFIRE.tag);
        //tag(TFMGTags.FlamethrowerFuel.COLD.tag);
    }
}
