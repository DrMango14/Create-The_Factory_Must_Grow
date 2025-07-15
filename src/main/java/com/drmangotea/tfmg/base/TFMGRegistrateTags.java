package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.foundation.data.recipe.Mods;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import java.util.List;

public class TFMGRegistrateTags {
    public static void addGenerators() {
        TFMG.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, TFMGRegistrateTags::genBlockTags);
       TFMG.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, TFMGRegistrateTags::genItemTags);
       // TFMG.REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, TFMGRegistrateTags::genFluidTags);
       // TFMG.REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, TFMGRegistrateTags::genEntityTags);
    }
    private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
        TagGen.CreateTagsProvider<Item> prov = new TagGen.CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);

        prov.tag(TFMGTags.TFMGItemTags.RODS.tag)
                .add(Items.STICK);

        for (TFMGTags.TFMGItemTags tag : TFMGTags.TFMGItemTags.values()) {
            if (tag.alwaysDatagen) {
                prov.getOrCreateRawBuilder(tag.tag);
            }
        }
    }
    private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
        TagGen.CreateTagsProvider<Block> prov = new TagGen.CreateTagsProvider<>(provIn, Block::builtInRegistryHolder);


        prov.tag(TFMGTags.TFMGBlockTags.PUMPJACK_HEAD.tag)
                .add(Blocks.IRON_BLOCK);
        prov.tag(TFMGTags.TFMGBlockTags.PUMPJACK_PART.tag)
                .addTag(TFMGTags.TFMGBlockTags.PUMPJACK_SMALL_PART.tag);

        for (TFMGTags.TFMGBlockTags tag : TFMGTags.TFMGBlockTags.values()) {
            if (tag.alwaysDatagen) {
                prov.getOrCreateRawBuilder(tag.tag);
            }
        }
    }
}
