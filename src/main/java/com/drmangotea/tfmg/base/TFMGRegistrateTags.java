package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.TFMG;
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
       // TFMG.REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, TFMGRegistrateTags::genFluidTags);
       // TFMG.REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, TFMGRegistrateTags::genEntityTags);
    }
    private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
        TagGen.CreateTagsProvider<Item> prov = new TagGen.CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);

        prov.tag(Tags.Items.RODS)
                .add(Items.STICK);
    }
    private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
        TagGen.CreateTagsProvider<Block> prov = new TagGen.CreateTagsProvider<>(provIn, Block::builtInRegistryHolder);

        prov.tag(TFMGTags.TFMGBlockTags.PUMPJACK_HEAD.tag)
                .add(Blocks.IRON_BLOCK);

        prov.tag(TFMGTags.TFMGBlockTags.PUMPJACK_PART.tag)
                .addTag(TFMGTags.TFMGBlockTags.PUMPJACK_SMALL_PART.tag);
    }
}
