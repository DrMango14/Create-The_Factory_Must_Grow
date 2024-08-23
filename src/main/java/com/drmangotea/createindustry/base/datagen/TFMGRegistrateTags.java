package com.drmangotea.createindustry.base.datagen;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.registry.TFMGTags;
import com.simibubi.create.AllTags;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;

public class TFMGRegistrateTags {
    public static void addGenerators() {
        CreateTFMG.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, TFMGRegistrateTags::genBlockTags);
        CreateTFMG.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, TFMGRegistrateTags::genItemTags);
        CreateTFMG.REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, TFMGRegistrateTags::genFluidTags);
        CreateTFMG.REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, TFMGRegistrateTags::genEntityTags);
    }
    
    private static void genBlockTags(RegistrateTagsProvider<Block> prov) {
        prov.tag(TFMGTags.TFMGBlockTags.AIR_INTAKE_TRANSPARENT.tag).addTag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag).add(Blocks.MAGMA_BLOCK);
    }
    
    private static void genItemTags(RegistrateTagsProvider<Item> prov) {
    
    }
    
    private static void genFluidTags(RegistrateTagsProvider<Fluid> prov) {
    
    }
    
    private static void genEntityTags(RegistrateTagsProvider<EntityType<?>> prov) {
    
    }
}
