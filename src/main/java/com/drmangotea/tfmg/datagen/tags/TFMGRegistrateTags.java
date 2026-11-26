package com.drmangotea.tfmg.datagen.tags;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGRegistrate;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

public class TFMGRegistrateTags {
    private static final TFMGRegistrate REGISTRATE = TFMG.registrate();

    public static void addGenerators() {
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, TFMGRegistrateTags::genBlockTags);
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, TFMGRegistrateTags::genItemTags);
    }

    private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
        TagGen.CreateTagsProvider<Item> prov = new TagGen.CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);

        prov.tag(Tags.Items.RODS)
                .add(Items.STICK);

        prov.tag(TFMGTags.Items.ENGINE_CYLINDER.tag)
                .add(TFMGItems.DIESEL_ENGINE_CYLINDER.get(), TFMGItems.SIMPLE_ENGINE_CYLINDER.get(), TFMGItems.ENGINE_CYLINDER.get(), TFMGItems.AUTOGAS_ENGINE_CYLINDER.get());

        prov.tag(TFMGTags.Items.ENGINE_TURBINE.tag)
                .add(TFMGItems.TURBINE_BLADE.get());
    }

    private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
        TagGen.CreateTagsProvider<Block> prov = new TagGen.CreateTagsProvider<>(provIn, Block::builtInRegistryHolder);

        prov.tag(TFMGTags.Blocks.PUMPJACK_HEAD.tag)
                .add(Blocks.IRON_BLOCK);

        prov.tag(TFMGTags.Blocks.PUMPJACK_PART.tag)
                .addTag(TFMGTags.Blocks.PUMPJACK_SMALL_PART.tag);

        prov.tag(TFMGTags.Blocks.BLAST_FURNACE_MELTS.tag)
                .add(Blocks.SNOW);

        prov.tag(TFMGTags.Blocks.CARRYON_BLOCK_BLACKLIST.tag)
                .add(TFMGBlocks.CAST_IRON_CHEMICAL_VAT.get())
                .add(TFMGBlocks.STEEL_CHEMICAL_VAT.get())
                .add(TFMGBlocks.FIREPROOF_CHEMICAL_VAT.get());
    }
}
