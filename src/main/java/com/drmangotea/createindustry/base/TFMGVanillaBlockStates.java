package com.drmangotea.createindustry.base;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.palettes.TFMGPaletteBlockPattern;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.function.Supplier;

public class TFMGVanillaBlockStates {



    //WALL
    public static void generateWallBlockState(DataGenContext<Block, WallBlock> ctx, RegistrateBlockstateProvider prov,
                                                 String name) {
        prov.wallBlock(ctx.get(), name, CreateTFMG.asResource("block/"+name));
    }
    public static ItemBuilder<BlockItem, BlockBuilder<WallBlock, CreateRegistrate>> transformWallItem(
            ItemBuilder<BlockItem, BlockBuilder<WallBlock, CreateRegistrate>> builder, String name) {
        builder.model((c, p) -> p.wallInventory(c.getName(), CreateTFMG.asResource("block/"+name)));
        return builder;
    }

    //STAIR
    public static void generateStairBlockState(DataGenContext<Block, StairBlock> ctx, RegistrateBlockstateProvider prov,
                                               String name) {
        prov.stairsBlock(ctx.get(), name, CreateTFMG.asResource("block/"+name));
    }
    public static ItemBuilder<BlockItem, BlockBuilder<StairBlock, CreateRegistrate>> transformStairItem(
            ItemBuilder<BlockItem, BlockBuilder<StairBlock, CreateRegistrate>> builder, String variantName) {
        return builder;
    }

    //SLAB

    public static void generateSlabBlockState(DataGenContext<Block, SlabBlock> ctx, RegistrateBlockstateProvider prov,
                                      String variantName) {
        String name = variantName;
        ResourceLocation texture = CreateTFMG.asResource("block/"+name);


        ModelFile bottom = prov.models()
                .slab(name+"_bottom", texture, texture, texture);
        ModelFile top = prov.models()
                .slabTop(name + "_top", texture, texture, texture);
        ModelFile doubleSlab = prov.models()
                .getExistingFile(prov.modLoc("block/"+name));


        prov.slabBlock(ctx.get(), bottom, top, doubleSlab);
    }
    public static ItemBuilder<BlockItem, BlockBuilder<SlabBlock, CreateRegistrate>> transformSlabItem(
            ItemBuilder<BlockItem, BlockBuilder<SlabBlock, CreateRegistrate>> builder, String variantName) {
        return builder;
    }


}
