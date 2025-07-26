package com.drmangotea.tfmg.datagen.recipes.values;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGRegistrate;
import com.drmangotea.tfmg.content.decoration.pipes.TFMGPipes;
import com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.palettes.AllPaletteBlocks;
import com.simibubi.create.foundation.data.recipe.CompatMetals;
import com.simibubi.create.foundation.data.recipe.Mods;
import com.simibubi.create.foundation.mixin.accessor.MappedRegistryAccessor;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.*;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.brassPipe;


public class TFMGStandardRecipeGen extends TFMGRecipeProvider {
    private Marker MATERIALS = enterFolder("materials");
    public static final Map<String, ItemLike> DYES_FROM_COLOR = new HashMap<>();


    static {
        DYES_FROM_COLOR.put("white", Items.WHITE_DYE);
        DYES_FROM_COLOR.put("blue", Items.BLUE_DYE);
        DYES_FROM_COLOR.put("light_blue", Items.LIGHT_BLUE_DYE);
        DYES_FROM_COLOR.put("red", Items.RED_DYE);
        DYES_FROM_COLOR.put("green", Items.GREEN_DYE);
        DYES_FROM_COLOR.put("lime", Items.LIME_DYE);
        DYES_FROM_COLOR.put("pink", Items.PINK_DYE);
        DYES_FROM_COLOR.put("magenta", Items.MAGENTA_DYE);
        DYES_FROM_COLOR.put("yellow", Items.YELLOW_DYE);
        DYES_FROM_COLOR.put("gray", Items.GRAY_DYE);
        DYES_FROM_COLOR.put("light_gray", Items.LIGHT_GRAY_DYE);
        DYES_FROM_COLOR.put("brown", Items.BROWN_DYE);
        DYES_FROM_COLOR.put("cyan", Items.CYAN_DYE);
        DYES_FROM_COLOR.put("purple", Items.PURPLE_DYE);
        DYES_FROM_COLOR.put("orange", Items.ORANGE_DYE);
        DYES_FROM_COLOR.put("black", Items.BLACK_DYE);


    }

    GeneratedRecipe
            STEEL_COMPACTING =
            metalCompacting(ImmutableList.of(TFMGItems.STEEL_NUGGET, TFMGItems.STEEL_INGOT, TFMGBlocks.STEEL_BLOCK),
                    ImmutableList.of(I::steelNugget, I::steelIngot, I::steelBlock)),
            ALUMINUM_COMPACTING =
                    metalCompacting(ImmutableList.of(TFMGItems.ALUMINUM_NUGGET, TFMGItems.ALUMINUM_INGOT, TFMGBlocks.ALUMINUM_BLOCK),
                            ImmutableList.of(I::aluminumNugget, I::aluminumIngot, I::aluminumBlock)),
            CAST_IRON_COMPACTING =
                    metalCompacting(ImmutableList.of(TFMGItems.CAST_IRON_NUGGET, TFMGItems.CAST_IRON_INGOT, TFMGBlocks.CAST_IRON_BLOCK),
                            ImmutableList.of(I::castIronNugget, I::castIronIngot, I::castIronBlock)),
            LEAD_COMPACTING =
                    metalCompacting(ImmutableList.of(TFMGItems.LEAD_NUGGET, TFMGItems.LEAD_INGOT, TFMGBlocks.LEAD_BLOCK),
                            ImmutableList.of(I::leadNugget, I::leadIngot, I::leadBlock)),
            NICKEL_COMPACTING =
                    metalCompacting(ImmutableList.of(TFMGItems.NICKEL_NUGGET, TFMGItems.NICKEL_INGOT, TFMGBlocks.NICKEL_BLOCK),
                            ImmutableList.of(I::nickelNugget, I::nickelIngot, I::nickelBlock)),
            LITHIUM_COMPACTING =
                    metalCompacting(ImmutableList.of(TFMGItems.LITHIUM_NUGGET, TFMGItems.LITHIUM_INGOT, TFMGBlocks.LITHIUM_BLOCK),
                            ImmutableList.of(I::lithiumNugget, I::lithiumIngot, I::lithiumBlock)),
            CONSTANTAN_COMPACTING =
                    metalCompacting(ImmutableList.of(TFMGItems.CONSTANTAN_NUGGET, TFMGItems.CONSTANTAN_INGOT, TFMGBlocks.CONSTANTAN_BLOCK),
                            ImmutableList.of(I::constantanNugget, I::constantanIngot, I::constantanBlock)),


    //
    LEAD_FLYWHEEL = create(TFMGBlocks.LEAD_FLYWHEEL)
            .unlockedBy(TFMGItems.LEAD_INGOT::get)
            .viaShaped(b -> b
                    .define('C', leadIngot())
                    .define('S', shaft())
                    .pattern("CCC")
                    .pattern("CSC")
                    .pattern("CCC")),

    STEEL_FLYWHEEL = create(TFMGBlocks.STEEL_FLYWHEEL)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('C', tfmgSteelIngot())
                    .define('S', shaft())
                    .pattern("CCC")
                    .pattern("CSC")
                    .pattern("CCC")),

    CAST_IRON_FLYWHEEL = create(TFMGBlocks.CAST_IRON_FLYWHEEL)
            .unlockedBy(TFMGItems.CAST_IRON_INGOT::get)
            .viaShaped(b -> b
                    .define('C', castIronIngot())
                    .define('S', shaft())
                    .pattern("CCC")
                    .pattern("CSC")
                    .pattern("CCC")),

    NICKEL_FLYWHEEL = create(TFMGBlocks.NICKEL_FLYWHEEL)
            .unlockedBy(TFMGItems.NICKEL_INGOT::get)
            .viaShaped(b -> b
                    .define('C', nickelIngot())
                    .define('S', shaft())
                    .pattern("CCC")
                    .pattern("CSC")
                    .pattern("CCC")),

    ALUMINUM_FLYWHEEL = create(TFMGBlocks.ALUMINUM_FLYWHEEL)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('C', aluminumIngot())
                    .define('S', shaft())
                    .pattern("CCC")
                    .pattern("CSC")
                    .pattern("CCC")),

    ELECTRIC_PUMP = create(TFMGBlocks.ELECTRIC_PUMP)
            .unlockedBy(TFMGItems.CIRCUIT_BOARD::get)
            .viaShaped(b -> b
                    .define('C', circuitBoard())
                    .define('P', TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.STEEL).getPump())
                    .define('W', copperWire())
                    .define('Q', capacitor())
                    .define('K', TFMGItems.ELECTROMAGNETIC_COIL)
                    .define('M', magnet())
                    .pattern("MQK")
                    .pattern("CPW")
                    .pattern("MQK")),

    //STEEL_HELMET = create(TFMGItems.STEEL_HELMET)
    //        .unlockedBy(TFMGItems.STEEL_INGOT::get)
    //        .viaShaped(b -> b
    //                .define('C', steelIngot())
    //                .pattern("CCC")
    //                .pattern("C C")
    //                .pattern("   ")),
//
   // STEEL_CHESTPLATE = create(TFMGItems.STEEL_CHESTPLATE)
   //         .unlockedBy(TFMGItems.STEEL_INGOT::get)
   //         .viaShaped(b -> b
   //                 .define('C', steelIngot())
   //                 .pattern("C C")
   //                 .pattern("CCC")
   //                 .pattern("CCC")),
//
   // STEEL_LEGGINGS = create(TFMGItems.STEEL_LEGGINGS)
   //         .unlockedBy(TFMGItems.STEEL_INGOT::get)
   //         .viaShaped(b -> b
   //                 .define('C', steelIngot())
   //                 .pattern("CCC")
   //                 .pattern("C C")
   //                 .pattern("C C")),
//
   // STEEL_BOOTS = create(TFMGItems.STEEL_BOOTS)
   //         .unlockedBy(TFMGItems.STEEL_INGOT::get)
   //         .viaShaped(b -> b
   //                 .define('C', steelIngot())
   //                 .pattern("C C")
   //                 .pattern("C C")
   //                 .pattern("   ")),

    STEEL_SWORD = create(TFMGItems.STEEL_TOOLS.get(0))
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('C', steelIngot())
                    .define('S', Items.STICK)
                    .pattern("C  ")
                    .pattern("C  ")
                    .pattern("S  ")),

    STEEL_PICKAXE = create(TFMGItems.STEEL_TOOLS.get(1))
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('C', steelIngot())
                    .define('S', Items.STICK)
                    .pattern("CCC")
                    .pattern(" S ")
                    .pattern(" S ")),

    STEEL_AXE = create(TFMGItems.STEEL_TOOLS.get(2))
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('C', steelIngot())
                    .define('S', Items.STICK)
                    .pattern(" CC")
                    .pattern(" SC")
                    .pattern(" S ")),

    STEEL_SHOVEL = create(TFMGItems.STEEL_TOOLS.get(3))
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('C', steelIngot())
                    .define('S', Items.STICK)
                    .pattern("C  ")
                    .pattern("S  ")
                    .pattern("S  ")),

    STEEL_HOE = create(TFMGItems.STEEL_TOOLS.get(4))
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('C', steelIngot())
                    .define('S', Items.STICK)
                    .pattern("CC ")
                    .pattern("S  ")
                    .pattern("S  ")),

    ALUMINUM_SWORD = create(TFMGItems.ALUMINUM_TOOLS.get(0))
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('C', aluminumIngot())
                    .define('S', Items.STICK)
                    .pattern("C  ")
                    .pattern("C  ")
                    .pattern("S  ")),

    ALUMINUM_PICKAXE = create(TFMGItems.ALUMINUM_TOOLS.get(1))
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('C', aluminumIngot())
                    .define('S', Items.STICK)
                    .pattern("CCC")
                    .pattern(" S ")
                    .pattern(" S ")),

    ALUMINUM_AXE = create(TFMGItems.ALUMINUM_TOOLS.get(2))
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('C', aluminumIngot())
                    .define('S', Items.STICK)
                    .pattern(" CC")
                    .pattern(" SC")
                    .pattern(" S ")),

    ALUMINUM_SHOVEL = create(TFMGItems.ALUMINUM_TOOLS.get(3))
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('C', aluminumIngot())
                    .define('S', Items.STICK)
                    .pattern("C  ")
                    .pattern("S  ")
                    .pattern("S  ")),

    ALUMINUM_HOE = create(TFMGItems.ALUMINUM_TOOLS.get(4))
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('C', aluminumIngot())
                    .define('S', Items.STICK)
                    .pattern("CC ")
                    .pattern("S  ")
                    .pattern("S  ")),

    LEAD_SWORD = create(TFMGItems.LEAD_TOOLS.get(0))
            .unlockedBy(TFMGItems.LEAD_INGOT::get)
            .viaShaped(b -> b
                    .define('C', leadIngot())
                    .define('S', Items.STICK)
                    .pattern("C  ")
                    .pattern("C  ")
                    .pattern("S  ")),

    LEAD_PICKAXE = create(TFMGItems.LEAD_TOOLS.get(1))
            .unlockedBy(TFMGItems.LEAD_INGOT::get)
            .viaShaped(b -> b
                    .define('C', leadIngot())
                    .define('S', Items.STICK)
                    .pattern("CCC")
                    .pattern(" S ")
                    .pattern(" S ")),

    LEAD_AXE = create(TFMGItems.LEAD_TOOLS.get(2))
            .unlockedBy(TFMGItems.LEAD_INGOT::get)
            .viaShaped(b -> b
                    .define('C', leadIngot())
                    .define('S', Items.STICK)
                    .pattern(" CC")
                    .pattern(" SC")
                    .pattern(" S ")),

    LEAD_SHOVEL = create(TFMGItems.LEAD_TOOLS.get(3))
            .unlockedBy(TFMGItems.LEAD_INGOT::get)
            .viaShaped(b -> b
                    .define('C', leadIngot())
                    .define('S', Items.STICK)
                    .pattern("C  ")
                    .pattern("S  ")
                    .pattern("S  ")),

    LEAD_HOE = create(TFMGItems.LEAD_TOOLS.get(4))
            .unlockedBy(TFMGItems.LEAD_INGOT::get)
            .viaShaped(b -> b
                    .define('C', leadIngot())
                    .define('S', Items.STICK)
                    .pattern("CC ")
                    .pattern("S  ")
                    .pattern("S  ")),

    /// ////////////
    STEEL_PIPE = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.STEEL).getPipe()).returns(4)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('I', steelIngot())
                    .define('P', steelSheet())
                    .pattern("   ")
                    .pattern("PIP")
                    .pattern("   ")),

    STEEL_PIPE_VERTICAL = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.STEEL).getPipe()).withSuffix("_vertical").returns(4)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('I', steelIngot())
                    .define('P', steelSheet())
                    .pattern("P")
                    .pattern("I")
                    .pattern("P")),

    STEEL_MECHANICAL_PUMP = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.STEEL).getPump())
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShapeless(b -> b
                    .requires(cog())
                    .requires(steelPipe())),

    STEEL_SMART_FLUID_PIPE = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.STEEL).getSmart())
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('P', electronTube())
                    .define('S', steelPipe())
                    .define('I', aluminumSheet())
                    .pattern("I")
                    .pattern("S")
                    .pattern("P")),

    STEEL_FLUID_VALVE = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.STEEL).getValve())
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShapeless(b -> b
                    .requires(ironSheet())
                    .requires(steelPipe())),
    /// ////////////
    ALUMINUM_PIPE = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.ALUMINUM).getPipe()).returns(4)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('I', aluminumIngot())
                    .define('P', aluminumSheet())
                    .pattern("   ")
                    .pattern("PIP")
                    .pattern("   ")),

    ALUMINUM_PIPE_VERTICAL = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.ALUMINUM).getPipe()).withSuffix("_vertical").returns(4)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('I', aluminumIngot())
                    .define('P', aluminumSheet())
                    .pattern("P")
                    .pattern("I")
                    .pattern("P")),

    ALUMINUM_MECHANICAL_PUMP = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.ALUMINUM).getPump())
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShapeless(b -> b
                    .requires(cog())
                    .requires(aluminumPipe())),

    ALUMINUM_SMART_FLUID_PIPE = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.ALUMINUM).getSmart())
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('P', electronTube())
                    .define('S', aluminumPipe())
                    .define('I', brassSheet())
                    .pattern("I")
                    .pattern("S")
                    .pattern("P")),

    ALUMINUM_FLUID_VALVE = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.ALUMINUM).getValve())
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShapeless(b -> b
                    .requires(ironSheet())
                    .requires(aluminumPipe())),
    /// ////////////
    PLASTIC_PIPE = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.PLASTIC).getPipe()).returns(4)
            .unlockedBy(TFMGItems.PLASTIC_SHEET::get)
            .viaShaped(b -> b
                    .define('I', plasticSheet())
                    .pattern("   ")
                    .pattern("III")
                    .pattern("   ")),

    PLASTIC_PIPE_VERTICAL = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.PLASTIC).getPipe()).withSuffix("_vertical").returns(4)
            .unlockedBy(TFMGItems.PLASTIC_SHEET::get)
            .viaShaped(b -> b
                    .define('I', plasticSheet())
                    .pattern("I")
                    .pattern("I")
                    .pattern("I")),

    PLASTIC_MECHANICAL_PUMP = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.PLASTIC).getPump())
            .unlockedBy(TFMGItems.PLASTIC_SHEET::get)
            .viaShapeless(b -> b
                    .requires(cog())
                    .requires(plasticPipe())),

    PLASTIC_SMART_FLUID_PIPE = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.PLASTIC).getSmart())
            .unlockedBy(TFMGItems.PLASTIC_SHEET::get)
            .viaShaped(b -> b
                    .define('P', electronTube())
                    .define('S', plasticPipe())
                    .define('I', steelSheet())
                    .pattern("I")
                    .pattern("S")
                    .pattern("P")),

    PLASTIC_FLUID_VALVE = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.PLASTIC).getValve())
            .unlockedBy(TFMGItems.PLASTIC_SHEET::get)
            .viaShapeless(b -> b
                    .requires(ironSheet())
                    .requires(plasticPipe())),
    /// ////////////
    BRASS_PIPE = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.BRASS).getPipe()).returns(4)
            .unlockedBy(AllItems.BRASS_INGOT::get)
            .viaShaped(b -> b
                    .define('I', brassIngot())
                    .define('P', brassSheet())
                    .pattern("   ")
                    .pattern("PIP")
                    .pattern("   ")),

    BRASS_PIPE_VERTICAL = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.BRASS).getPipe()).withSuffix("_vertical").returns(4)
            .unlockedBy(AllItems.BRASS_INGOT::get)
            .viaShaped(b -> b
                    .define('I', brassIngot())
                    .define('P', brassSheet())
                    .pattern("P")
                    .pattern("I")
                    .pattern("P")),

    BRASS_MECHANICAL_PUMP = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.BRASS).getPump())
            .unlockedBy(AllItems.BRASS_INGOT::get)
            .viaShapeless(b -> b
                    .requires(cog())
                    .requires(brassPipe())),

    BRASS_SMART_FLUID_PIPE = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.BRASS).getSmart())
            .unlockedBy(AllItems.BRASS_INGOT::get)
            .viaShaped(b -> b
                    .define('P', electronTube())
                    .define('S', brassPipe())
                    .define('I', copperSheet())
                    .pattern("I")
                    .pattern("S")
                    .pattern("P")),

    BRASS_FLUID_VALVE = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.BRASS).getValve())
            .unlockedBy(AllItems.BRASS_INGOT::get)
            .viaShapeless(b -> b
                    .requires(ironSheet())
                    .requires(brassPipe())),
    /// ////////////
    CAST_IRON_PIPE = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.CAST_IRON).getPipe()).returns(4)
            .unlockedBy(TFMGItems.CAST_IRON_INGOT::get)
            .viaShaped(b -> b
                    .define('I', castIronIngot())
                    .define('P', castIronSheet())
                    .pattern("   ")
                    .pattern("PIP")
                    .pattern("   ")),

    CAST_IRON_PIPE_VERTICAL = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.CAST_IRON).getPipe()).withSuffix("_vertical").returns(4)
            .unlockedBy(TFMGItems.CAST_IRON_INGOT::get)
            .viaShaped(b -> b
                    .define('I', castIronIngot())
                    .define('P', castIronSheet())
                    .pattern("P")
                    .pattern("I")
                    .pattern("P")),

    CAST_IRON_MECHANICAL_PUMP = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.CAST_IRON).getPump())
            .unlockedBy(TFMGItems.CAST_IRON_INGOT::get)
            .viaShapeless(b -> b
                    .requires(cog())
                    .requires(castIronPipe())),

    CAST_IRON_SMART_FLUID_PIPE = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.CAST_IRON).getSmart())
            .unlockedBy(TFMGItems.CAST_IRON_INGOT::get)
            .viaShaped(b -> b
                    .define('P', electronTube())
                    .define('S', castIronPipe())
                    .define('I', castIronSheet())
                    .pattern("I")
                    .pattern("S")
                    .pattern("P")),

    CAST_IRON_FLUID_VALVE = create(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.CAST_IRON).getValve())
            .unlockedBy(TFMGItems.CAST_IRON_INGOT::get)
            .viaShapeless(b -> b
                    .requires(ironSheet())
                    .requires(castIronPipe())),
    /// ////////////


    STEEL_COGWHEEL = create(TFMGBlocks.STEEL_COGWHEEL).returns(4)
            .unlockedBy(() -> TFMGItems.STEEL_INGOT)
            .viaShapeless(b -> b
                    .requires(Ingredient.of(tfmgSteelIngot()))
                    .requires(Ingredient.of(AllBlocks.SHAFT))),

    LARGE_STEEL_COGWHEEL = create(TFMGBlocks.LARGE_STEEL_COGWHEEL).returns(2)
            .unlockedBy(() -> TFMGItems.STEEL_INGOT)
            .viaShapeless(b -> b
                    .requires(Ingredient.of(tfmgSteelIngot()))
                    .requires(Ingredient.of(tfmgSteelIngot()))
                    .requires(Ingredient.of(AllBlocks.SHAFT))),

    ALUMINUM_COGWHEEL = create(TFMGBlocks.ALUMINUM_COGWHEEL).returns(4)
            .unlockedBy(() -> TFMGItems.ALUMINUM_INGOT)
            .viaShapeless(b -> b
                    .requires(Ingredient.of(aluminumIngot()))
                    .requires(Ingredient.of(AllBlocks.SHAFT))),

    LARGE_ALUMINUM_COGWHEEL = create(TFMGBlocks.LARGE_ALUMINUM_COGWHEEL).returns(2)
            .unlockedBy(() -> TFMGItems.ALUMINUM_INGOT)
            .viaShapeless(b -> b
                    .requires(Ingredient.of(aluminumIngot()))
                    .requires(Ingredient.of(aluminumIngot()))
                    .requires(Ingredient.of(AllBlocks.SHAFT))),

    FIREPROOF_BRICKS = create(TFMGBlocks.FIREPROOF_BRICKS)
            .unlockedBy(TFMGItems.FIRECLAY_BALL::get)
            .viaShaped(b -> b
                    .define('B', TFMGItems.FIREPROOF_BRICK)
                    .pattern("BB ")
                    .pattern("BB ")
                    .pattern("   ")),

    FIRECLAY_BLOCK = create(TFMGBlocks.FIRECLAY)
            .unlockedBy(TFMGItems.FIRECLAY_BALL::get)
            .viaShaped(b -> b
                    .define('B', TFMGItems.FIRECLAY_BALL)
                    .pattern("BB ")
                    .pattern("BB ")
                    .pattern("   ")),

    BLAST_FURNACE_OUTPUT = create(TFMGBlocks.BLAST_FURNACE_OUTPUT)
            .unlockedBy(TFMGItems.FIREPROOF_BRICK::get)
            .viaShaped(b -> b
                    .define('B', fireproofBricks())
                    .define('C', castIronPipe())
                    .define('Q', castIronBlock())
                    .pattern("BCB")
                    .pattern("CQC")
                    .pattern("BCB")),

    BLAST_FURNACE_HATCH = create(TFMGBlocks.BLAST_FURNACE_HATCH)
            .unlockedBy(TFMGItems.FIREPROOF_BRICK::get)
            .viaShaped(b -> b
                    .define('T', castIronTank())
                    .define('F', fireproofBricks())
                    .define('I', castIronSheet())
                    .define('P', castIronPipe())
                    .pattern("FIF")
                    .pattern("PTP")
                    .pattern("FIF")),

    NAPALM_BOMB = create(TFMGBlocks.NAPALM_BOMB)
            .unlockedBy(TFMGItems.FIREPROOF_BRICK::get)
            .viaShaped(b -> b
                    .define('P', plasticSheet())
                    .define('N', TFMGFluids.NAPALM.getBucket().get())
                    .pattern("PPP")
                    .pattern("NNN")
                    .pattern("PPP")),

    STEEL_VAT = create(TFMGBlocks.STEEL_CHEMICAL_VAT).returns(2)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('T', steelTank())
                    .define('N', nickelSheet())
                    .define('P', heavyPlate())
                    .pattern("PPP")
                    .pattern("NTN")
                    .pattern("PPP")),

    CAST_IRON_VAT = create(TFMGBlocks.CAST_IRON_CHEMICAL_VAT)
            .unlockedBy(TFMGItems.CAST_IRON_INGOT::get)
            .viaShaped(b -> b
                    .define('T', castIronTank())
                    .define('N', leadSheet())
                    .define('P', castIronSheet())
                    .pattern("PPP")
                    .pattern("NTN")
                    .pattern("PPP")),

    FIREPROOF_VAT = create(TFMGBlocks.FIREPROOF_CHEMICAL_VAT)
            .unlockedBy(TFMGItems.CIRCUIT_BOARD::get)
            .viaShaped(b -> b
                    .define('N', TFMGItems.CIRCUIT_BOARD)
                    .define('H', heavyMachineryCasing())
                    .define('R', rubber())
                    .define('T', TFMGBlocks.STEEL_CHEMICAL_VAT)
                    .define('P', fireproofBricks())
                    .pattern("PRP")
                    .pattern("NTN")
                    .pattern("PHP")),

    UNFINISHED_ELECTROMAGNETIC_COIL = create(TFMGItems.UNFINISHED_ELECTROMAGNETIC_COIL).returns(2)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('M', magneticIngot())
                    .define('N', steelNugget())
                    .pattern(" N ")
                    .pattern(" M ")
                    .pattern(" N ")),

    RAW_LEAD_BLOCK = create(TFMGBlocks.RAW_LEAD_BLOCK)
            .unlockedBy(TFMGItems.RAW_LEAD::get)
            .viaShaped(b -> b
                    .define('B', TFMGItems.RAW_LEAD)
                    .pattern("BBB")
                    .pattern("BBB")
                    .pattern("BBB")),

    RAW_NICKEL_BLOCK = create(TFMGBlocks.RAW_NICKEL_BLOCK)
            .unlockedBy(TFMGItems.RAW_NICKEL::get)
            .viaShaped(b -> b
                    .define('B', TFMGItems.RAW_NICKEL)
                    .pattern("BBB")
                    .pattern("BBB")
                    .pattern("BBB")),

    RAW_LITHIUM_BLOCK = create(TFMGBlocks.RAW_LITHIUM_BLOCK)
            .unlockedBy(TFMGItems.RAW_LITHIUM::get)
            .viaShaped(b -> b
                    .define('B', TFMGItems.RAW_LITHIUM)
                    .pattern("BBB")
                    .pattern("BBB")
                    .pattern("BBB")),

    AIR_INTAKE = create(TFMGBlocks.AIR_INTAKE).returns(3)
            .unlockedBy(AllItems.PROPELLER::get)
            .viaShaped(b -> b
                    .define('B', AllBlocks.ANDESITE_BARS)
                    .define('T', castIronPipe())
                    .define('P', AllItems.PROPELLER)
                    .define('C', AllBlocks.INDUSTRIAL_IRON_BLOCK)
                    .define('G', AllBlocks.COGWHEEL)
                    .define('S', shaft())
                    .pattern("SPT")
                    .pattern("GCG")
                    .pattern(" B ")),

    LITHIUM_TORCH = create(TFMGBlocks.LITHIUM_TORCH).returns(4)
            .unlockedBy(TFMGItems.LITHIUM_INGOT::get)
            .viaShaped(b -> b
                    .define('L', lithiumIngot())
                    .define('A', aluminumIngot())
                    .pattern(" L ")
                    .pattern(" A ")
                    .pattern("   ")),

    OIL_HAMMER = create(TFMGItems.OIL_HAMMER)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('O', steelIngot())
                    .define('R', rebar())
                    .define('A', aluminumSheet())
                    .pattern("OOA")
                    .pattern(" R ")
                    .pattern(" R ")),


    CABLE_TUBE = create(TFMGBlocks.CABLE_TUBE).returns(8)
            .unlockedBy(TFMGItems.RUBBER_SHEET::get)
            .viaShaped(b -> b
                    .define('C', copperWire())
                    .define('R', rubber())
                    .define('N', steelNugget())
                    .pattern(" N ")
                    .pattern("CRC")
                    .pattern(" N ")),

    DIAGONAL_CABLE = create(TFMGBlocks.DIAGONAL_CABLE_BLOCK).returns(8)
            .unlockedBy(TFMGItems.RUBBER_SHEET::get)
            .viaShaped(b -> b
                    .define('C', copperWire())
                    .define('R', rubber())
                    .define('N', steelNugget())
                    .pattern(" C ")
                    .pattern("CRN")
                    .pattern(" N ")),

    ELECTRIC_POST = create(TFMGBlocks.ELECTRIC_POST).returns(4)
            .unlockedBy(TFMGItems.COPPER_WIRE::get)
            .viaShaped(b -> b
                    .define('C', copperWire())
                    .define('R', steelIngot())
                    .define('N', steelNugget())
                    .pattern(" N ")
                    .pattern("CRC")
                    .pattern(" N ")),

    CABLE_CONNECTOR = create(TFMGBlocks.CABLE_CONNECTOR)
            .unlockedBy(TFMGItems.COPPER_WIRE::get)
            .viaShaped(b -> b
                    .define('C', TFMGItems.UNFINISHED_INSULATOR)
                    .define('N', copperIngot())
                    .define('O', steelNugget())
                    .pattern("OOO")
                    .pattern(" C ")
                    .pattern(" N ")),

    ELECTRICAL_SWITCH = create(TFMGBlocks.ELECTRICAL_SWITCH)
            .unlockedBy(TFMGItems.COPPER_WIRE::get)
            .viaShaped(b -> b
                    .define('C', heavyMachineryCasing())
                    .define('R', redstone())
                    .define('L', leadSheet())
                    .define('P', TFMGBlocks.ELECTRIC_POST)
                    .pattern("RPR")
                    .pattern("LCL")
                    .pattern("RPR")),

   // FUSE_BLOCK = create(TFMGBlocks.FUSE_BLOCK)
   //         .unlockedBy(TFMGItems.COPPER_WIRE::get)
   //         .viaShaped(b -> b
   //                 .define('C', heavyMachineryCasing())
   //                 .define('W', copperWire())
   //                 .define('R', aluminumSheet())
   //                 .define('L', copperIngot())
   //                 .pattern("RWR")
   //                 .pattern("LCL")
   //                 .pattern("RWR")),

    SEGMENTED_DISPLAY = create(TFMGBlocks.SEGMENTED_DISPLAY).returns(4)
            .unlockedBy(TFMGBlocks.ALUMINUM_CASING::get)
            .viaShaped(b -> b
                    .define('A', aluminumCasing())
                    .define('W', copperWire())
                    .define('G', Blocks.BLACK_STAINED_GLASS_PANE)
                    .define('C', circuitBoard())
                    .define('K', TFMGItems.SILICON_INGOT)
                    .pattern("WGW")
                    .pattern("KAK")
                    .pattern("WCW")),

    NEON_TUBE = create(TFMGBlocks.NEON_TUBE).returns(4)
            .unlockedBy(TFMGItems.COPPER_WIRE::get)
            .viaShaped(b -> b
                    .define('C', copperNugget())
                    .define('W', framedGlass())
                    .define('N', TFMGRegistrate.getBucket("neon"))
                    .define('O', steelNugget())
                    .pattern("OCO")
                    .pattern("NWN")
                    .pattern("OCO")),

    GLASS_INSULATOR_SEGMENT = create(TFMGItems.GLASS_INSULATOR_SEGMENT)
            .unlockedBy(TFMGItems.COPPER_WIRE::get)
            .viaShaped(b -> b
                    .define('G', Blocks.GREEN_STAINED_GLASS_PANE)
                    .define('C', copperNugget())
                    .define('O', steelNugget())
                    .pattern(" O ")
                    .pattern("GGG")
                    .pattern(" C ")),

    GLASS_INSULATOR = create(TFMGBlocks.GLASS_CABLE_CONNECTOR)
            .unlockedBy(TFMGItems.COPPER_WIRE::get)
            .viaShaped(b -> b
                    .define('I', TFMGItems.GLASS_INSULATOR_SEGMENT)
                    .define('C', copperNugget())
                    .define('O', steelNugget())
                    .pattern("CIC")
                    .pattern("OIO")
                    .pattern("CIC")),

    STEEL_TRAPDOOR = create(TFMGBlocks.STEEL_TRAPDOOR)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('O', steelIngot())
                    .pattern("   ")
                    .pattern("OOO")
                    .pattern("   ")),

    STEEL_GEARBOX = create(TFMGBlocks.STEEL_GEARBOX)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('O', TFMGBlocks.STEEL_COGWHEEL)
                    .define('C', heavyMachineryCasing())
                    .pattern(" O ")
                    .pattern("OCO")
                    .pattern(" O ")),

    REBAR_PILE = create(TFMGBlocks.REBAR_PILE)
            .unlockedBy(TFMGItems.REBAR::get)
            .viaShaped(b -> b
                    .define('O', rebar())
                    .pattern("OOO")
                    .pattern("OOO")
                    .pattern("OOO")),

    REBAR_BLOCK = create(TFMGBlocks.REBAR_BLOCK).returns(4)
            .unlockedBy(TFMGItems.REBAR::get)
            .viaShaped(b -> b
                    .define('O', rebar())
                    .pattern("O O")
                    .pattern("   ")
                    .pattern("O O")),

    REBAR_STAIRS = create(TFMGBlocks.REBAR_STAIRS).returns(3)
            .unlockedBy(TFMGItems.REBAR::get)
            .viaShaped(b -> b
                    .define('O', rebar())
                    .pattern("   ")
                    .pattern("O  ")
                    .pattern("OO ")),

    REBAR_FLOOR = create(TFMGBlocks.REBAR_FLOOR).returns(3)
            .unlockedBy(TFMGItems.REBAR::get)
            .viaShaped(b -> b
                    .define('O', rebar())
                    .pattern("   ")
                    .pattern("OOO")
                    .pattern("   ")),

    REBAR_PILLAR = create(TFMGBlocks.REBAR_PILLAR).returns(3)
            .unlockedBy(TFMGItems.REBAR::get)
            .viaShaped(b -> b
                    .define('O', rebar())
                    .pattern(" O ")
                    .pattern(" O ")
                    .pattern(" O ")),

    REBAR_WALL = create(TFMGBlocks.REBAR_WALL).returns(6)
            .unlockedBy(TFMGItems.REBAR::get)
            .viaShaped(b -> b
                    .define('O', rebar())
                    .pattern("   ")
                    .pattern("OOO")
                    .pattern("OOO")),


    SCREWDRIVER = create(TFMGItems.SCREWDRIVER)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('I', aluminumIngot())
                    .define('R', rebar())
                    .pattern("  R")
                    .pattern(" I ")
                    .pattern("   ")),

    SCREWDRIVER_FROM_BRASS = create(TFMGItems.SCREWDRIVER).withSuffix("from_brass")
            .unlockedBy(AllItems.BRASS_INGOT::get)
            .viaShaped(b -> b
                    .define('I', brassIngot())
                    .define('R', rebar())
                    .pattern("  R")
                    .pattern(" I ")
                    .pattern("   ")),

    ELECTRIC_GAUGE = create(TFMGBlocks.VOLTMETER)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('M', magnet())
                    .define('N', steelNugget())
                    .define('A', steelIngot())
                    .define('C', Items.COMPASS)
                    .pattern("NNN")
                    .pattern("NCN")
                    .pattern("AMA")),

    DISTILLATION_CONTROLLER = create(TFMGBlocks.STEEL_DISTILLATION_CONTROLLER)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('M', steelMechanism())
                    .define('S', leadSheet())
                    .define('P', industrialPipe())
                    .define('E', electronTube())
                    .define('H', heavyMachineryCasing())
                    .define('C', Items.COMPASS)
                    .pattern("SPS")
                    .pattern("ECE")
                    .pattern("MHM")),

    VOLTAGE_OBSERVER = create(TFMGBlocks.VOLTAGE_OBSERVER)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('W', copperWire())
                    .define('R', redstone())
                    .define('T', TFMGItems.TRANSISTOR)
                    .define('Q', capacitor())
                    .define('C', aluminumCasing())
                    .pattern("TRT")
                    .pattern("WCR")
                    .pattern("QRQ")),

    LIGHT_BULB = create(TFMGBlocks.LIGHT_BULB).returns(2)
            .unlockedBy(TFMGItems.CONSTANTAN_INGOT::get)
            .viaShaped(b -> b
                    .define('W', TFMGItems.CONSTANTAN_WIRE)
                    .define('G', framedGlass())
                    .define('N', steelNugget())
                    .define('C', copperNugget())
                    .pattern("CWC")
                    .pattern("CGC")
                    .pattern("NNN")),

    CASTING_BASIN = create(TFMGBlocks.CASTING_BASIN)
            .unlockedBy(TFMGItems.FIREPROOF_BRICK::get)
            .viaShaped(b -> b
                    .define('C', castIronIngot())
                    .define('B', TFMGItems.FIREPROOF_BRICK)
                    .define('P', castIronPipe())
                    .pattern("BPB")
                    .pattern("CBC")
                    .pattern("CCC")),

    HAMMER_HOLDER = create(TFMGBlocks.PUMPJACK_HAMMER)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('T', steelTruss())
                    .define('R', rebar())
                    .define('S', steelIngot())
                    .pattern("RSR")
                    .pattern("STS")
                    .pattern(" T ")),

    WINDING_MACHINE = create(TFMGBlocks.WINDING_MACHINE)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('T', shaft())
                    .define('M', steelMechanism())
                    .define('P', heavyPlate())
                    .define('C', heavyMachineryCasing())
                    .define('I', steelIngot())
                    .pattern("   ")
                    .pattern("ITP")
                    .pattern("ICM")),

    DISTILLATION_OUTPUT = create(TFMGBlocks.STEEL_DISTILLATION_OUTPUT)
            .unlockedBy(TFMGBlocks.HEAVY_MACHINERY_CASING::get)
            .viaShaped(b -> b
                    .define('B', heavyMachineryCasing())
                    .define('T', steelTank())
                    .define('P', steelPipe())
                    .pattern("PTP")
                    .pattern("TBT")
                    .pattern("PTP")),

    PUMPJACK_CRANK = create(TFMGBlocks.PUMPJACK_CRANK)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('M', steelMechanism())
                    .define('H', heavyPlate())
                    .define('R', rebar())
                    .define('C', heavyMachineryCasing())
                    .define('P', string())
                    .pattern("P P")
                    .pattern("HMH")
                    .pattern("RCR")),

    TRANSMISSION = create(TFMGItems.TRANSMISSION)
            .unlockedBy(TFMGItems.PLASTIC_SHEET::get)
            .viaShaped(b -> b
                    .define('M', steelMechanism())
                    .define('A', aluminumSheet())
                    .define('P', plasticSheet())
                    .define('C', TFMGBlocks.ALUMINUM_COGWHEEL)
                    .pattern("AAA")
                    .pattern("CPC")
                    .pattern("MMM")),

    COPPER_SPOOL = create(TFMGItems.COPPER_SPOOL)
            .unlockedBy(TFMGItems.EMPTY_SPOOL::get)
            .viaShaped(b -> b
                    .define('S', TFMGItems.EMPTY_SPOOL)
                    .define('W', copperWire())
                    .pattern("WWW")
                    .pattern("WSW")
                    .pattern("WWW")),

    ALUMINUM_SPOOL = create(TFMGItems.ALUMINUM_SPOOL)
            .unlockedBy(TFMGItems.EMPTY_SPOOL::get)
            .viaShaped(b -> b
                    .define('S', TFMGItems.EMPTY_SPOOL)
                    .define('W', aluminumWire())
                    .pattern("WWW")
                    .pattern("WSW")
                    .pattern("WWW")),

    CONSTANTAN_SPOOL = create(TFMGItems.CONSTANTAN_SPOOL)
            .unlockedBy(TFMGItems.EMPTY_SPOOL::get)
            .viaShaped(b -> b
                    .define('S', TFMGItems.EMPTY_SPOOL)
                    .define('W', constantanWire())
                    .pattern("WWW")
                    .pattern("WSW")
                    .pattern("WWW")),

    REGULAR_ENGINE = create(TFMGBlocks.REGULAR_ENGINE).returns(2)
            .unlockedBy(TFMGBlocks.HEAVY_MACHINERY_CASING::asItem)
            .viaShaped(b -> b
                    .define('O', steelIngot())
                    .define('I', heavyPlate())
                    .define('C', heavyMachineryCasing())
                    .pattern("   ")
                    .pattern("OIO")
                    .pattern("ICI")),

    TURBINE_ENGINE = create(TFMGBlocks.TURBINE_ENGINE).returns(2)
            .unlockedBy(TFMGBlocks.HEAVY_MACHINERY_CASING::asItem)
            .viaShaped(b -> b
                    .define('O', steelSheet())
                    .define('P', aluminumPipe())
                    .define('H', heavyMachineryCasing())
                    .pattern("OOO")
                    .pattern("PHP")
                    .pattern("OOO")),

    RADIAL_ENGINE = create(TFMGBlocks.RADIAL_ENGINE)
            .unlockedBy(TFMGBlocks.HEAVY_MACHINERY_CASING::asItem)
            .viaShaped(b -> b
                    .define('O', steelIngot())
                    .define('S', shaft())
                    .define('I', steelNugget())
                    .define('C', heavyMachineryCasing())
                    .pattern("IOI")
                    .pattern("OCO")
                    .pattern("ISI")),

    STEEL_TANK = create(TFMGBlocks.STEEL_FLUID_TANK).returns(2)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('B', Blocks.BARREL)
                    .define('P', heavyPlate())
                    .pattern(" P ")
                    .pattern(" B ")
                    .pattern(" P ")),

    CONFIGURATION_WRENCH = create(TFMGItems.CONFIGURATION_WRENCH)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('O', steelSheet())
                    .define('A', aluminumSheet())
                    .define('R', rebar())
                    .define('C', TFMGBlocks.STEEL_COGWHEEL)
                    .pattern("OA ")
                    .pattern("OCA")
                    .pattern(" R ")),

    OIL_CAN = create(TFMGItems.OIL_CAN)
            .unlockedBy(TFMGItems.STEEL_NUGGET::get)
            .viaShaped(b -> b
                    .define('O', steelNugget())
                    .define('B', brassSheet())
                    .pattern(" OO")
                    .pattern("BBB")
                    .pattern(" BB")),

    COOLING_FLUID_BOTTLE = create(TFMGItems.COOLING_FLUID_BOTTLE)
            .unlockedBy(TFMGItems.PLASTIC_SHEET::get)
            .viaShaped(b -> b
                    .define('P', plasticSheet())
                    .define('B', Items.LIGHT_BLUE_DYE)
                    .pattern(" P ")
                    .pattern("PBP")
                    .pattern("PPP")),

    ALUMINUM_TANK = create(TFMGBlocks.ALUMINUM_FLUID_TANK)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('B', Blocks.BARREL)
                    .define('P', aluminumSheet())
                    .pattern(" P ")
                    .pattern(" B ")
                    .pattern(" P ")),

    CAST_IRON_TANK = create(TFMGBlocks.CAST_IRON_FLUID_TANK)
            .unlockedBy(TFMGItems.CAST_IRON_INGOT::get)
            .viaShaped(b -> b
                    .define('B', Blocks.BARREL)
                    .define('P', castIronSheet())
                    .pattern(" P ")
                    .pattern(" B ")
                    .pattern(" P ")),

    EXHAUST = create(TFMGBlocks.EXHAUST)
            .unlockedBy(TFMGItems.CAST_IRON_INGOT::get)
            .viaShaped(b -> b
                    .define('B', Blocks.IRON_BARS)
                    .define('P', castIronPipe())
                    .define('C', castIronIngot())
                    .pattern("BPB")
                    .pattern("BPB")
                    .pattern("CPC")),

    FLARESTACK = create(TFMGBlocks.FLARESTACK)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('B', Blocks.IRON_BARS)
                    .define('P', castIronPipe())
                    .define('C', steelIngot())
                    .define('S', Items.FLINT_AND_STEEL)
                    .pattern("SPS")
                    .pattern("BPB")
                    .pattern("CPC")),

    SURFACE_SCANNER = create(TFMGBlocks.SURFACE_SCANNER)
            .unlockedBy(TFMGBlocks.HEAVY_MACHINERY_CASING::get)
            .viaShaped(b -> b
                    .define('H', heavyMachineryCasing())
                    .define('T', electronTube())
                    .define('M', steelMechanism())
                    .pattern("TTT")
                    .pattern("TTT")
                    .pattern("MHM")),

    POLARIZER = create(TFMGBlocks.POLARIZER)
            .unlockedBy(TFMGBlocks.STEEL_CASING::get)
            .viaShaped(b -> b
                    .define('B', brassSheet())
                    .define('W', copperWire())
                    .define('V', TFMGBlocks.VOLTMETER)
                    .define('S', steelCasing())
                    .define('R', TFMGBlocks.RESISTOR)
                    .define('C', capacitor())
                    .pattern("BWB")
                    .pattern("CVC")
                    .pattern("RSR")),

    ELECTRODE_HOLDER = create(TFMGBlocks.ELECTRODE_HOLDER)
            .unlockedBy(TFMGBlocks.HEAVY_MACHINERY_CASING::get)
            .viaShaped(b -> b
                    .define('S', steelIngot())
                    .define('W', copperWire())
                    .define('C', heavyMachineryCasing())
                    .define('L', leadSheet())
                    .pattern("SLS")
                    .pattern("WCW")
                    .pattern("SLS")),

    TURBO = create(TFMGItems.TURBO)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('P', aluminumPipe())
                    .define('F', propeller())
                    .define('B', TFMGBlocks.STEEL_BARS)
                    .pattern("PPP")
                    .pattern("BFP")
                    .pattern("PPP")),

    CINDERBLOCK = create(TFMGBlocks.CINDER_BLOCK).returns(8)
            .unlockedBy(TFMGItems.CINDERBLOCK::get)
            .viaShaped(b -> b
                    .define('C', TFMGFluids.LIQUID_CONCRETE.getBucket().get())
                    .define('B', TFMGItems.CINDERBLOCK)
                    .define('R', rebar())
                    .pattern("BBB")
                    .pattern("RCR")
                    .pattern("BBB")),

    CINDERFLOURBLOCK = create(TFMGBlocks.CINDERFLOUR_BLOCK).returns(8)
            .unlockedBy(TFMGItems.CINDERFLOURBLOCK::get)
            .viaShaped(b -> b
                    .define('C', Blocks.NETHER_WART_BLOCK)
                    .define('B', TFMGItems.CINDERFLOURBLOCK)
                    .define('R', rebar())
                    .pattern("BBB")
                    .pattern("RCR")
                    .pattern("BBB")),

    TRANSFORMER = create(TFMGBlocks.TRANSFORMER)
            .unlockedBy(TFMGBlocks.STEEL_CASING::get)
            .viaShaped(b -> b
                    .define('C', steelCasing())
                    .define('M', magneticIngot())
                    .define('N', nickelSheet())
                    .define('W', copperWire())
                    .pattern("MMM")
                    .pattern("MNM")
                    .pattern("WCW")),

    CONCRETE_HOSE = create(TFMGBlocks.CONCRETE_HOSE)
            .unlockedBy(TFMGBlocks.STEEL_CASING::get)
            .viaShaped(b -> b
                    .define('C', heavyMachineryCasing())
                    .define('K', Items.DRIED_KELP_BLOCK)
                    .define('N', nickelSheet())
                    .define('P', steelPipe())
                    .define('S', shaft())
                    .pattern(" C ")
                    .pattern("SKP")
                    .pattern(" N ")),

    ACCUMULATOR = create(TFMGBlocks.ACCUMULATOR)
            .unlockedBy(TFMGFluids.SULFURIC_ACID.getBucket()::get)
            .viaShaped(b -> b
                    .define('C', aluminumCasing())
                    .define('L', leadSheet())
                    .define('W', copperWire())
                    .define('S', TFMGFluids.SULFURIC_ACID.getBucket().get())
                    .define('B', leadBlock())
                    .pattern("LWL")
                    .pattern("SBS")
                    .pattern("LCL")),

    ACCUMULATOR_FROM_LITHIUM = create(TFMGBlocks.ACCUMULATOR).withSuffix("from_lithium")
            .unlockedBy(TFMGItems.LITHIUM_INGOT::get)
            .viaShaped(b -> b
                    .define('C', aluminumCasing())
                    .define('L', lithiumIngot())
                    .define('W', copperWire())
                    .define('S', nickelSheet())
                    .define('B', lithiumBlock())
                    .pattern("LWL")
                    .pattern("SBS")
                    .pattern("LCL")),

    CONVERTER = create(TFMGBlocks.CONVERTER)
            .unlockedBy(TFMGBlocks.TRANSFORMER::get)
            .viaShaped(b -> b
                    .define('T', TFMGBlocks.TRANSFORMER)
                    .define('R', redstone())
                    .define('W', copperWire())
                    .define('L', leadSheet())
                    .pattern("WLR")
                    .pattern("WTR")
                    .pattern("WLR")),

    MULTIMETER = create(TFMGItems.MULTIMETER)
            .unlockedBy(TFMGBlocks.VOLTMETER::get)
            .viaShaped(b -> b
                    .define('G', TFMGBlocks.VOLTMETER)
                    .define('W', copperWire())
                    .define('B', brassSheet())
                    .pattern("BGB")
                    .pattern("BWB")
                    .pattern("BWB")),

    COPPER_CABLE_HUB = create(TFMGBlocks.COPPER_CABLE_HUB).returns(2)
            .unlockedBy(TFMGItems.MAGNET::get)
            .viaShaped(b -> b
                    .define('M', copperIngot())
                    .define('W', copperWire())
                    .pattern("WWW")
                    .pattern("MMM")
                    .pattern("WWW")),

    BRASS_CABLE_HUB = create(TFMGBlocks.BRASS_CABLE_HUB).returns(2)
            .unlockedBy(TFMGItems.MAGNET::get)
            .viaShaped(b -> b
                    .define('M', brassIngot())
                    .define('W', copperWire())
                    .pattern("WWW")
                    .pattern("MMM")
                    .pattern("WWW")),

    ALUMINUM_CABLE_HUB = create(TFMGBlocks.ALUMINUM_CABLE_HUB).returns(2)
            .unlockedBy(TFMGItems.MAGNET::get)
            .viaShaped(b -> b
                    .define('M', aluminumIngot())
                    .define('W', copperWire())
                    .pattern("WWW")
                    .pattern("MMM")
                    .pattern("WWW")),

    STEEL_CABLE_HUB = create(TFMGBlocks.STEEL_CABLE_HUB).returns(2)
            .unlockedBy(TFMGItems.MAGNET::get)
            .viaShaped(b -> b
                    .define('M', steelNugget())
                    .define('W', copperWire())
                    .pattern("WWW")
                    .pattern("MMM")
                    .pattern("WWW")),

    STEEL_CASING_CABLE_HUB = create(TFMGBlocks.STEEL_CASING_CABLE_HUB).returns(2)
            .unlockedBy(TFMGItems.MAGNET::get)
            .viaShaped(b -> b
                    .define('M', steelCasing())
                    .define('W', copperWire())
                    .pattern("WWW")
                    .pattern(" M ")
                    .pattern("WWW")),

    HEAVY_CABLE_HUB = create(TFMGBlocks.HEAVY_CABLE_HUB).returns(2)
            .unlockedBy(TFMGItems.MAGNET::get)
            .viaShaped(b -> b
                    .define('M', heavyMachineryCasing())
                    .define('W', copperWire())
                    .pattern("WWW")
                    .pattern(" M ")
                    .pattern("WWW")),

    FIREPROOF_BRICK = create(TFMGItems.FIREPROOF_BRICK::get)
            .viaCooking(() -> TFMGItems.FIRECLAY_BALL)
            .inFurnace(),

    UNFINISHED_INSULATOR = create(TFMGItems.UNFINISHED_INSULATOR::get)
            .viaCooking(() -> TFMGItems.UNFIRED_INSULATOR)
            .inFurnace(),

    RAW_LEAD = create(TFMGItems.LEAD_INGOT::get)
            .viaCooking(() -> TFMGItems.RAW_LEAD)
            .inFurnace(),

    NICKEL = create(TFMGItems.NICKEL_INGOT::get)
            .viaCooking(() -> TFMGItems.RAW_NICKEL)
            .inFurnace(),

    LITHIUM = create(TFMGItems.LITHIUM_INGOT::get)
            .viaCooking(() -> TFMGItems.RAW_LITHIUM)
            .inFurnace(),

    RAW_LEAD_BLASTING = create(TFMGItems.LEAD_INGOT::get).withSuffix("_blasting")
            .viaCooking(() -> TFMGItems.RAW_LEAD)
            .inBlastFurnace(),

    NICKEL_LEAD_BLASTING = create(TFMGItems.NICKEL_INGOT::get).withSuffix("_blasting")
            .viaCooking(() -> TFMGItems.RAW_NICKEL)
            .inBlastFurnace(),

    LITHIUM_BLASTING = create(TFMGItems.LITHIUM_INGOT::get).withSuffix("_blasting")
            .viaCooking(() -> TFMGItems.RAW_LITHIUM)
            .inBlastFurnace(),
    //

    LEAD_FROM_CRUSHED_BLASTING = create(TFMGItems.LEAD_INGOT::get).withSuffix("_from_crushed_blasting")
            .viaCooking(() -> AllItems.CRUSHED_LEAD)
            .inBlastFurnace(),

    NICKEL_FROM_CRUSHED_BLASTING = create(TFMGItems.NICKEL_INGOT::get).withSuffix("_from_crushed_blasting")
            .viaCooking(() -> AllItems.CRUSHED_NICKEL)
            .inBlastFurnace(),

    LITHIUM_FROM_CRUSHED_BLASTING = create(TFMGItems.LITHIUM_INGOT::get).withSuffix("_from_crushed_blasting")
            .viaCooking(() -> TFMGItems.CRUSHED_LITHIUM)
            .inBlastFurnace(),
    //

    THERMITE_GRENADE = create(TFMGItems.THERMITE_GRENADE)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('A', aluminumIngot())
                    .define('T', thermitePowder())
                    .pattern(" T ")
                    .pattern("TAT")
                    .pattern(" T ")),

    COPPER_GRENADE = create(TFMGItems.COPPER_GRENADE)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('A', TFMGItems.THERMITE_GRENADE)
                    .define('T', copperIngot())
                    .pattern("   ")
                    .pattern("TAT")
                    .pattern("   ")),

    ZINC_GRENADE = create(TFMGItems.ZINC_GRENADE)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('A', TFMGItems.THERMITE_GRENADE)
                    .define('T', zincIngot())
                    .pattern("   ")
                    .pattern("TAT")
                    .pattern("   ")),

    LITHIUM_BLADE = create(TFMGItems.LITHIUM_BLADE)
            .unlockedBy(TFMGItems.LITHIUM_INGOT::get)
            .viaShaped(b -> b
                    .define('P', heavyPlate())
                    .define('M', TFMGItems.STEEL_TOOLS.get(0))
                    .define('C', circuitBoard())
                    .define('W', copperWire())
                    .define('A', aluminumSheet())
                    .define('S', TFMGItems.SPARK_PLUG)
                    .pattern(" P ")
                    .pattern("AMS")
                    .pattern("ACW")),

    LITHIUM_CHARGE = create(TFMGItems.LITHIUM_CHARGE)
            .unlockedBy(TFMGItems.LITHIUM_INGOT::get)
            .viaShaped(b -> b
                    .define('P', plasticSheet())
                    .define('A', aluminumSheet())
                    .define('L', lithiumIngot())
                    .pattern(" P ")
                    .pattern("LLL")
                    .pattern(" A ")),

    STEEL_TRUSS = create(steelTruss()::asItem).returns(4)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('N', steelNugget())
                    .pattern("N N")
                    .pattern("NNN")
                    .pattern("N N")),

    ALUMINUM_TRUSS = create(aluminumTruss()::asItem).returns(4)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('N', aluminumNugget())
                    .pattern("N N")
                    .pattern("NNN")
                    .pattern("N N")),

    CAST_IRON_TRUSS = create(castIronTruss()::asItem).returns(4)
            .unlockedBy(TFMGItems.CAST_IRON_INGOT::get)
            .viaShaped(b -> b
                    .define('N', castIronNugget())
                    .pattern("N N")
                    .pattern("NNN")
                    .pattern("N N")),

    LEAD_TRUSS = create(leadTruss()::asItem).returns(4)
            .unlockedBy(TFMGItems.LEAD_INGOT::get)
            .viaShaped(b -> b
                    .define('N', leadNugget())
                    .pattern("N N")
                    .pattern("NNN")
                    .pattern("N N")),

    NICKEL_TRUSS = create(nickelTruss()::asItem).returns(4)
            .unlockedBy(TFMGItems.NICKEL_INGOT::get)
            .viaShaped(b -> b
                    .define('N', nickelNugget())
                    .pattern("N N")
                    .pattern("NNN")
                    .pattern("N N")),

    CONSTANTAN_TRUSS = create(constantanTruss()::asItem).returns(4)
            .unlockedBy(TFMGItems.CONSTANTAN_INGOT::get)
            .viaShaped(b -> b
                    .define('N', constantanNugget())
                    .pattern("N N")
                    .pattern("NNN")
                    .pattern("N N")),

    COPPER_TRUSS = create(copperTruss()::asItem).returns(4)
            .unlockedBy(Items.COPPER_INGOT::asItem)
            .viaShaped(b -> b
                    .define('N', copperNugget())
                    .pattern("N N")
                    .pattern("NNN")
                    .pattern("N N")),

    ZINC_TRUSS = create(zincTruss()::asItem).returns(4)
            .unlockedBy(AllItems.ZINC_INGOT::get)
            .viaShaped(b -> b
                    .define('N', zincNugget())
                    .pattern("N N")
                    .pattern("NNN")
                    .pattern("N N")),

    BRASS_TRUSS = create(brassTruss()::asItem).returns(4)
            .unlockedBy(AllItems.BRASS_INGOT::get)
            .viaShaped(b -> b
                    .define('N', brassNugget())
                    .pattern("N N")
                    .pattern("NNN")
                    .pattern("N N")),
    /// /////////////////////

    STEEL_FRAME = create(steelFrame()::asItem).returns(4)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('N', steelNugget())
                    .pattern("NNN")
                    .pattern("N N")
                    .pattern("NNN")),

    ALUMINUM_FRAME = create(aluminumFrame()::asItem).returns(4)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('N', aluminumNugget())
                    .pattern("NNN")
                    .pattern("N N")
                    .pattern("NNN")),

    CAST_IRON_FRAME = create(castIronFrame()::asItem).returns(4)
            .unlockedBy(TFMGItems.CAST_IRON_INGOT::get)
            .viaShaped(b -> b
                    .define('N', castIronNugget())
                    .pattern("NNN")
                    .pattern("N N")
                    .pattern("NNN")),

    LEAD_FRAME = create(leadFrame()::asItem).returns(4)
            .unlockedBy(TFMGItems.LEAD_INGOT::get)
            .viaShaped(b -> b
                    .define('N', leadNugget())
                    .pattern("NNN")
                    .pattern("N N")
                    .pattern("NNN")),

    NICKEL_FRAME = create(nickelFrame()::asItem).returns(4)
            .unlockedBy(TFMGItems.NICKEL_INGOT::get)
            .viaShaped(b -> b
                    .define('N', nickelNugget())
                    .pattern("NNN")
                    .pattern("N N")
                    .pattern("NNN")),

    CONSTANTAN_FRAME = create(constantanFrame()::asItem).returns(4)
            .unlockedBy(TFMGItems.CONSTANTAN_INGOT::get)
            .viaShaped(b -> b
                    .define('N', constantanNugget())
                    .pattern("NNN")
                    .pattern("N N")
                    .pattern("NNN")),

    COPPER_FRAME = create(copperFrame()::asItem).returns(4)
            .unlockedBy(Items.COPPER_INGOT::asItem)
            .viaShaped(b -> b
                    .define('N', copperNugget())
                    .pattern("NNN")
                    .pattern("N N")
                    .pattern("NNN")),

    ZINC_FRAME = create(zincFrame()::asItem).returns(4)
            .unlockedBy(AllItems.ZINC_INGOT::get)
            .viaShaped(b -> b
                    .define('N', zincNugget())
                    .pattern("NNN")
                    .pattern("N N")
                    .pattern("NNN")),

    BRASS_FRAME = create(brassFrame()::asItem).returns(4)
            .unlockedBy(AllItems.BRASS_INGOT::get)
            .viaShaped(b -> b
                    .define('N', brassNugget())
                    .pattern("NNN")
                    .pattern("N N")
                    .pattern("NNN")),
    /// /////////////////////////

    FIREBOX = create(TFMGBlocks.FIREBOX)
            .unlockedBy(TFMGBlocks.INDUSTRIAL_PIPE::get)
            .viaShaped(b -> b
                    .define('B', fireproofBricks())
                    .define('P', brassPipe())
                    .define('T', steelTank())
                    .pattern("BTB")
                    .pattern("PPP")
                    .pattern("BPB")),

    EMPTY_CIRCUIT_BOARD = create(TFMGItems.EMPTY_CIRCUIT_BOARD)
            .unlockedBy(TFMGItems.PLASTIC_SHEET::get)
            .viaShaped(b -> b
                    .define('P', plasticSheet())
                    .define('G', greenDye())
                    .pattern("   ")
                    .pattern(" G ")
                    .pattern("PPP")),

    COKE_OVEN = create(TFMGBlocks.COKE_OVEN).returns(2)
            .unlockedBy(TFMGItems.CAST_IRON_INGOT::get)
            .viaShaped(b -> b
                    .define('C', castIronIngot())
                    .define('I', AllBlocks.INDUSTRIAL_IRON_BLOCK)
                    .pattern(" C ")
                    .pattern("CIC")
                    .pattern(" C ")),

    INDUSTRIAL_MIXER = create(TFMGBlocks.INDUSTRIAL_MIXER)
            .unlockedBy(TFMGBlocks.HEAVY_MACHINERY_CASING::get)
            .viaShaped(b -> b
                    .define('M', steelMechanism())
                    .define('H', heavyMachineryCasing())
                    .define('C', TFMGBlocks.LARGE_STEEL_COGWHEEL)
                    .define('S', shaft())
                    .define('K', screw())
                    .pattern("KSK")
                    .pattern("MHM")
                    .pattern("KCK")),

    CENTRIFUGE = create(TFMGItems.CENTRIFUGE)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('A', aluminumIngot())
                    .define('B', TFMGBlocks.ALUMINUM_BARS)
                    .pattern("BAB")
                    .pattern("BAB")
                    .pattern("BAB")),

    RESISTOR = create(TFMGItems.UNFINISHED_RESISTOR)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('W', copperWire())
                    .define('P', plasticSheet())
                    .pattern(" W ")
                    .pattern(" P ")
                    .pattern(" W ")),

    RESISTOR_FROM_SLIME = create(TFMGItems.UNFINISHED_RESISTOR).withSuffix("from_slime")
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('W', copperWire())
                    .define('P', Items.SLIME_BALL)
                    .pattern(" W ")
                    .pattern(" P ")
                    .pattern(" W ")),

    MIXER_BLADE = create(TFMGItems.MIXER_BLADE)
            .unlockedBy(TFMGItems.HEAVY_PLATE::get)
            .viaShaped(b -> b
                    .define('S', shaft())
                    .define('P', AllItems.PROPELLER)
                    .define('H', heavyPlate())
                    .pattern(" S ")
                    .pattern(" S ")
                    .pattern("HPH")),

    CRANKSHAFT = create(TFMGItems.CRANKSHAFT)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('A', rebar())
                    .define('B', aluminumSheet())
                    .pattern("ABA")
                    .pattern("BAB")
                    .pattern("   ")),

    PUMPJACK_BASE = create(TFMGBlocks.PUMPJACK_BASE)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('Q', industrialPipe())
                    .define('O', steelPipe())
                    .define('M', steelMechanism())
                    .define('C', heavyMachineryCasing())
                    .define('S', string())
                    .define('T', steelTank())
                    .pattern("STS")
                    .pattern("MCM")
                    .pattern("OQO")),

    MACHINE_INPUT = create(TFMGBlocks.MACHINE_INPUT)
            .unlockedBy(TFMGBlocks.HEAVY_MACHINERY_CASING::get)
            .viaShaped(b -> b
                    .define('M', steelMechanism())
                    .define('H', heavyMachineryCasing())
                    .define('S', shaft())
                    .pattern(" S ")
                    .pattern(" H ")
                    .pattern(" M ")),


    BRICK_SMOKESTACK = create(TFMGBlocks.BRICK_SMOKESTACK).returns(4)
            .unlockedBy(TFMGBlocks.INDUSTRIAL_PIPE::get)
            .viaShaped(b -> b
                    .define('B', Blocks.BRICKS)
                    .define('P', industrialPipe())
                    .pattern("BPB")
                    .pattern("BPB")
                    .pattern("BPB")),

    CONCRETE_SMOKESTACK = create(TFMGBlocks.CONCRETE_SMOKESTACK).returns(4)
            .unlockedBy(TFMGBlocks.INDUSTRIAL_PIPE::get)
            .viaShaped(b -> b
                    .define('B', TFMGBlocks.CONCRETE.block)
                    .define('P', industrialPipe())
                    .pattern("BPB")
                    .pattern("BPB")
                    .pattern("BPB")),

    METAL_SMOKESTACK = create(TFMGBlocks.METAL_SMOKESTACK).returns(4)
            .unlockedBy(TFMGBlocks.INDUSTRIAL_PIPE::get)
            .viaShaped(b -> b
                    .define('B', steelNugget())
                    .define('P', industrialPipe())
                    .pattern("BPB")
                    .pattern("BPB")
                    .pattern("BPB")),

    COPYCAT_CABLE = create(TFMGBlocks.COPYCAT_CABLE_BLOCK).returns(4)
            .unlockedBy(TFMGItems.NICKEL_INGOT::get)
            .viaShaped(b -> b
                    .define('N', nickelTruss())
                    .define('W', copperWire())
                    .pattern("NWN")
                    .pattern("WNW")
                    .pattern("NWN")),

    BLAST_FURNACE_REINFORCEMENT = create(TFMGBlocks.BLAST_FURNACE_REINFORCEMENT).returns(4)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('O', heavyPlate())
                    .define('I', steelIngot())
                    .define('B', fireproofBricks())
                    .pattern("IOB")
                    .pattern("IOB")
                    .pattern("IOB")),

    FIREPROOF_BRICK_REINFORCEMENT = create(TFMGBlocks.FIREPROOF_BRICK_REINFORCEMENT).returns(6)
            .unlockedBy(TFMGBlocks.FIREPROOF_BRICKS::get)
            .viaShaped(b -> b
                    .define('W', fireproofBricks())
                    .pattern("   ")
                    .pattern("WWW")
                    .pattern("WWW")),

    HEAVY_PLATED_DOOR = create(TFMGBlocks.HEAVY_PLATED_DOOR).returns(3)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('I', steelIngot())
                    .pattern("II ")
                    .pattern("II ")
                    .pattern("II ")),

    ALUMINUM_DOOR = create(TFMGBlocks.ALUMINUM_DOOR).returns(3)
            .unlockedBy(TFMGItems.ALUMINUM_INGOT::get)
            .viaShaped(b -> b
                    .define('I', aluminumIngot())
                    .pattern("II ")
                    .pattern("II ")
                    .pattern("II ")),


    PUMPJACK_HAMMER_HEAD = create(TFMGBlocks.PUMPJACK_HAMMER_HEAD)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('B', steelBlock())
                    .define('P', steelSheet())
                    .pattern(" ")
                    .pattern("B")
                    .pattern("P")),

    LARGE_PUMPJACK_HAMMER_HEAD = create(TFMGBlocks.LARGE_PUMPJACK_HAMMER_HEAD)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('B', steelBlock())
                    .define('P', steelSheet())
                    .pattern("P")
                    .pattern("B")
                    .pattern(" ")),

    CIRCULAR_LAMP = create(TFMGBlocks.CIRCULAR_LIGHT)
            .unlockedBy(TFMGBlocks.LIGHT_BULB::get)
            .viaShaped(b -> b
                    .define('S', steelNugget())
                    .define('P', AllPaletteBlocks.FRAMED_GLASS)
                    .define('B', lightBulb())
                    .pattern("P")
                    .pattern("B")
                    .pattern("S")),

    ALUMINUM_LAMP = create(TFMGBlocks.ALUMINUM_LAMP)
            .unlockedBy(TFMGBlocks.LIGHT_BULB::get)
            .viaShaped(b -> b
                    .define('S', aluminumSheet())
                    .define('P', AllPaletteBlocks.FRAMED_GLASS_PANE)
                    .define('B', lightBulb())
                    .pattern(" P ")
                    .pattern(" B ")
                    .pattern(" S ")),

    MODERN_LIGHT = create(TFMGBlocks.MODERN_LIGHT)
            .unlockedBy(TFMGBlocks.LIGHT_BULB::get)
            .viaShaped(b -> b
                    .define('N', steelNugget())
                    .define('P', AllPaletteBlocks.FRAMED_GLASS_PANE)
                    .define('B', lightBulb())
                    .pattern(" P ")
                    .pattern(" B ")
                    .pattern("NNN")),

    TRAFFIC_LIGHT = create(TFMGBlocks.TRAFFIC_LIGHT)
            .unlockedBy(TFMGBlocks.LIGHT_BULB::get)
            .viaShaped(b -> b
                    .define('N', copperWire())
                    .define('C', circuitBoard())
                    .define('P', heavyMachineryCasing())
                    .define('B', lightBulb())
                    .define('R', Blocks.RED_STAINED_GLASS_PANE)
                    .define('O', Blocks.ORANGE_STAINED_GLASS_PANE)
                    .define('G', Blocks.GREEN_STAINED_GLASS_PANE)
                    .pattern("CBR")
                    .pattern("PBO")
                    .pattern("NBG")),

    DIODE = create(TFMGBlocks.DIODE)
            .unlockedBy(TFMGItems.SILICON_INGOT::get)
            .viaShaped(b -> b
                    .define('W', copperWire())
                    .define('I', AllBlocks.INDUSTRIAL_IRON_BLOCK)
                    .define('P', heavyMachineryCasing())
                    .define('B', castIronSheet())
                    .define('S', TFMGItems.P_SEMICONDUCTOR)
                    .define('N', TFMGItems.N_SEMICONDUCTOR)
                    .pattern("WBW")
                    .pattern("SPN")
                    .pattern("WIW")),

    BLAST_STOVE = create(TFMGBlocks.BLAST_STOVE).returns(2)
            .unlockedBy(TFMGItems.FIREPROOF_BRICK::get)
            .viaShaped(b -> b
                    .define('F', fireproofBricks())
                    .define('T', castIronTank())
                    .define('C', castIronPipe())
                    .pattern("CC ")
                    .pattern("TT ")
                    .pattern("FF ")),

    PUMPJACK_HAMMER_CONNECTOR = create(TFMGBlocks.PUMPJACK_HAMMER_CONNECTOR)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('H', TFMGBlocks.PUMPJACK_HAMMER_PART)
                    .define('R', rebar())
                    .pattern("   ")
                    .pattern("RHR")
                    .pattern("   ")),

    LARGE_PUMPJACK_HAMMER_CONNECTOR = create(TFMGBlocks.LARGE_PUMPJACK_HAMMER_CONNECTOR)
            .unlockedBy(TFMGItems.STEEL_INGOT::get)
            .viaShaped(b -> b
                    .define('H', TFMGBlocks.LARGE_PUMPJACK_HAMMER_PART)
                    .define('R', rebar())
                    .pattern("   ")
                    .pattern("RHR")
                    .pattern("   ")),

    ENGINE_GEARBODX = create(TFMGBlocks.ENGINE_GEARBOX)
            .unlockedBy(TFMGItems.STEEL_MECHANISM::get)
            .viaShaped(b -> b
                    .define('M', steelMechanism())
                    .define('C', steelCasing())
                    .define('K', screw())
                    .define('S', shaft())
                    .pattern("KMK")
                    .pattern("SCS")
                    .pattern("KSK")),


    //SHAPELESS

    PIPE_BOMB_SO_COOL = create(TFMGItems.PIPE_BOMB).returns(2)
            .unlockedBy(I::steelPipe)
            .viaShapeless(b -> b
                    .requires(steelPipe())
                    .requires(Blocks.TNT)
            ),

    RUSTED_BLAST_FURNACE_REINFORCEMENT = create(TFMGBlocks.RUSTED_BLAST_FURNACE_REINFORCEMENT).returns(8)
            .unlockedBy(TFMGBlocks.BLAST_FURNACE_REINFORCEMENT::get)
            .viaShapeless(b -> b
                    .requires(Items.WATER_BUCKET)
                    .requires(TFMGBlocks.BLAST_FURNACE_REINFORCEMENT,8)
            ),


    RAW_LEAD_FROM_BLOCK = create(TFMGItems.RAW_LEAD).returns(9)
            .unlockedBy(() -> TFMGItems.RAW_LEAD)
            .viaShapeless(b -> b
                    .requires(TFMGBlocks.RAW_LEAD_BLOCK)),

    RAW_NICKEL_FROM_BLOCK = create(TFMGItems.RAW_NICKEL).returns(9)
            .unlockedBy(() -> TFMGItems.RAW_NICKEL)
            .viaShapeless(b -> b
                    .requires(TFMGBlocks.RAW_NICKEL_BLOCK)),

    RAW_LITHIUM_FROM_BLOCK = create(TFMGItems.RAW_LITHIUM).returns(9)
            .unlockedBy(() -> TFMGItems.RAW_LITHIUM)
            .viaShapeless(b -> b
                    .requires(TFMGBlocks.RAW_LITHIUM_BLOCK)),

    COAL_COKE_FROM_BLOCK = create(TFMGItems.COAL_COKE).returns(9)
            .unlockedBy(() -> TFMGItems.COAL_COKE)
            .viaShapeless(b -> b
                    .requires(TFMGBlocks.COAL_COKE_BLOCK)),

    COAL_COKE_BLOCK = create(TFMGBlocks.COAL_COKE_BLOCK)
            .unlockedBy(() -> TFMGItems.COAL_COKE)
            .viaShapeless(b -> b
                    .requires(coalCoke(), 9)),

    PLASTIC_FROM_BLOCK = create(TFMGItems.PLASTIC_SHEET).returns(9)
            .unlockedBy(() -> TFMGItems.PLASTIC_SHEET)
            .viaShapeless(b -> b
                    .requires(TFMGBlocks.PLASTIC_BLOCK)),




    PLASTIC_BLOCK = create(TFMGBlocks.PLASTIC_BLOCK)
            .unlockedBy(() -> TFMGItems.PLASTIC_SHEET)
            .viaShapeless(b -> b
                    .requires(plasticSheet(), 9)),

    REBAR_FROM_BLOCK = create(TFMGItems.REBAR).returns(9)
            .unlockedBy(() -> TFMGItems.REBAR)
            .viaShapeless(b -> b
                    .requires(TFMGBlocks.REBAR_PILE)),

    STEEL_CASING_DOOR = create(TFMGBlocks.STEEL_CASING_DOOR)
            .unlockedBy(() -> TFMGItems.STEEL_INGOT)
            .viaShapeless(b -> b
                    .requires(steelCasing())
                    .requires(ItemTags.WOODEN_DOORS)),

    HEAVY_CASING_DOOR = create(TFMGBlocks.HEAVY_CASING_DOOR)
            .unlockedBy(() -> TFMGItems.STEEL_INGOT)
            .viaShapeless(b -> b
                    .requires(heavyMachineryCasing())
                    .requires(ItemTags.WOODEN_DOORS)),


    ////////

    WHITE_MULTIMETER = create(TFMGItems.MULTIMETERS.get("white")::get)
            .unlockedBy(() -> TFMGItems.MULTIMETER)
            .viaShapeless(b -> b
                    .requires(TFMGItems.MULTIMETER)
                    .requires(DYES_FROM_COLOR.get("white"))),

    YELLOW_MULTIMETER = create(TFMGItems.MULTIMETERS.get("yellow")::get)
            .unlockedBy(() -> TFMGItems.MULTIMETER)
            .viaShapeless(b -> b
                    .requires(TFMGItems.MULTIMETER)
                    .requires(DYES_FROM_COLOR.get("yellow"))),

    BROWN_MULTIMETER = create(TFMGItems.MULTIMETERS.get("brown")::get)
            .unlockedBy(() -> TFMGItems.MULTIMETER)
            .viaShapeless(b -> b
                    .requires(TFMGItems.MULTIMETER)
                    .requires(DYES_FROM_COLOR.get("brown"))),

    ORANGE_MULTIMETER = create(TFMGItems.MULTIMETERS.get("orange")::get)
            .unlockedBy(() -> TFMGItems.MULTIMETER)
            .viaShapeless(b -> b
                    .requires(TFMGItems.MULTIMETER)
                    .requires(DYES_FROM_COLOR.get("orange"))),

    BLACK_MULTIMETER = create(TFMGItems.MULTIMETERS.get("black")::get)
            .unlockedBy(() -> TFMGItems.MULTIMETER)
            .viaShapeless(b -> b
                    .requires(TFMGItems.MULTIMETER)
                    .requires(DYES_FROM_COLOR.get("black"))),

    CYAN_MULTIMETER = create(TFMGItems.MULTIMETERS.get("cyan")::get)
            .unlockedBy(() -> TFMGItems.MULTIMETER)
            .viaShapeless(b -> b
                    .requires(TFMGItems.MULTIMETER)
                    .requires(DYES_FROM_COLOR.get("cyan"))),

    BLUE_MULTIMETER = create(TFMGItems.MULTIMETERS.get("blue")::get)
            .unlockedBy(() -> TFMGItems.MULTIMETER)
            .viaShapeless(b -> b
                    .requires(TFMGItems.MULTIMETER)
                    .requires(DYES_FROM_COLOR.get("blue"))),

    LIGHT_BLUE_MULTIMETER = create(TFMGItems.MULTIMETERS.get("light_blue")::get)
            .unlockedBy(() -> TFMGItems.MULTIMETER)
            .viaShapeless(b -> b
                    .requires(TFMGItems.MULTIMETER)
                    .requires(DYES_FROM_COLOR.get("light_blue"))),

    GRAY_MULTIMETER = create(TFMGItems.MULTIMETERS.get("gray")::get)
            .unlockedBy(() -> TFMGItems.MULTIMETER)
            .viaShapeless(b -> b
                    .requires(TFMGItems.MULTIMETER)
                    .requires(DYES_FROM_COLOR.get("gray"))),

    LIGHT_GRAY_MULTIMETER = create(TFMGItems.MULTIMETERS.get("light_gray")::get)
            .unlockedBy(() -> TFMGItems.MULTIMETER)
            .viaShapeless(b -> b
                    .requires(TFMGItems.MULTIMETER)
                    .requires(DYES_FROM_COLOR.get("light_gray"))),

    GREEN_MULTIMETER = create(TFMGItems.MULTIMETERS.get("green")::get)
            .unlockedBy(() -> TFMGItems.MULTIMETER)
            .viaShapeless(b -> b
                    .requires(TFMGItems.MULTIMETER)
                    .requires(DYES_FROM_COLOR.get("green"))),

    LIME_MULTIMETER = create(TFMGItems.MULTIMETERS.get("lime")::get)
            .unlockedBy(() -> TFMGItems.MULTIMETER)
            .viaShapeless(b -> b
                    .requires(TFMGItems.MULTIMETER)
                    .requires(DYES_FROM_COLOR.get("lime"))),

    PINK_MULTIMETER = create(TFMGItems.MULTIMETERS.get("pink")::get)
            .unlockedBy(() -> TFMGItems.MULTIMETER)
            .viaShapeless(b -> b
                    .requires(TFMGItems.MULTIMETER)
                    .requires(DYES_FROM_COLOR.get("pink"))),

    PURPLE_MULTIMETER = create(TFMGItems.MULTIMETERS.get("purple")::get)
            .unlockedBy(() -> TFMGItems.MULTIMETER)
            .viaShapeless(b -> b
                    .requires(TFMGItems.MULTIMETER)
                    .requires(DYES_FROM_COLOR.get("purple"))),

    MAGENTA_MULTIMETER = create(TFMGItems.MULTIMETERS.get("magenta")::get)
            .unlockedBy(() -> TFMGItems.MULTIMETER)
            .viaShapeless(b -> b
                    .requires(TFMGItems.MULTIMETER)
                    .requires(DYES_FROM_COLOR.get("magenta"))),

    RED_MULTIMETER = create(TFMGItems.MULTIMETERS.get("red")::get)
            .unlockedBy(() -> TFMGItems.MULTIMETER)
            .viaShapeless(b -> b
                    .requires(TFMGItems.MULTIMETER)
                    .requires(DYES_FROM_COLOR.get("red"))),



    ////////


 WHITE_CONCRETE = create(TFMGBlocks.COLORED_CONCRETE.get("white").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("white"))
                 .define('C', TFMGBlocks.CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 BLACK_CONCRETE = create(TFMGBlocks.COLORED_CONCRETE.get("black").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("black"))
                 .define('C', TFMGBlocks.CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),

 ORANGE_CONCRETE = create(TFMGBlocks.COLORED_CONCRETE.get("orange").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("orange"))
                 .define('C', TFMGBlocks.CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 RED_CONCRETE = create(TFMGBlocks.COLORED_CONCRETE.get("red").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("red"))
                 .define('C', TFMGBlocks.CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 GRAY_CONCRETE = create(TFMGBlocks.COLORED_CONCRETE.get("gray").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("gray"))
                 .define('C', TFMGBlocks.CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 LIGHT_GRAY_CONCRETE = create(TFMGBlocks.COLORED_CONCRETE.get("light_gray").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("light_gray"))
                 .define('C', TFMGBlocks.CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 BLUE_CONCRETE = create(TFMGBlocks.COLORED_CONCRETE.get("blue").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("blue"))
                 .define('C', TFMGBlocks.CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 LIGHT_BLUE_CONCRETE = create(TFMGBlocks.COLORED_CONCRETE.get("light_blue").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("light_blue"))
                 .define('C', TFMGBlocks.CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 CYAN_CONCRETE = create(TFMGBlocks.COLORED_CONCRETE.get("cyan").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("cyan"))
                 .define('C', TFMGBlocks.CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 GREEN_CONCRETE = create(TFMGBlocks.COLORED_CONCRETE.get("green").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("green"))
                 .define('C', TFMGBlocks.CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 LIME_CONCRETE = create(TFMGBlocks.COLORED_CONCRETE.get("lime").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("lime"))
                 .define('C', TFMGBlocks.CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 PINK_CONCRETE = create(TFMGBlocks.COLORED_CONCRETE.get("pink").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("pink"))
                 .define('C', TFMGBlocks.CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 PURPLE_CONCRETE = create(TFMGBlocks.COLORED_CONCRETE.get("purple").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("purple"))
                 .define('C', TFMGBlocks.CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 MAGENTA_CONCRETE = create(TFMGBlocks.COLORED_CONCRETE.get("magenta").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("magenta"))
                 .define('C', TFMGBlocks.CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 BROWN_CONCRETE = create(TFMGBlocks.COLORED_CONCRETE.get("brown").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("brown"))
                 .define('C', TFMGBlocks.CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 YELLOW_CONCRETE = create(TFMGBlocks.COLORED_CONCRETE.get("yellow").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("yellow"))
                 .define('C', TFMGBlocks.CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),
 ///  ////////////////////////////////////////
 WHITE_REBAR_CONCRETE = create(TFMGBlocks.COLORED_REBAR_CONCRETE.get("white").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("white"))
                 .define('C', TFMGBlocks.REBAR_CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 BLACK_REBAR_CONCRETE = create(TFMGBlocks.COLORED_REBAR_CONCRETE.get("black").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("black"))
                 .define('C', TFMGBlocks.REBAR_CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),

 ORANGE_REBAR_CONCRETE = create(TFMGBlocks.COLORED_REBAR_CONCRETE.get("orange").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("orange"))
                 .define('C', TFMGBlocks.REBAR_CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 RED_REBAR_CONCRETE = create(TFMGBlocks.COLORED_REBAR_CONCRETE.get("red").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("red"))
                 .define('C', TFMGBlocks.REBAR_CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 GRAY_REBAR_CONCRETE = create(TFMGBlocks.COLORED_REBAR_CONCRETE.get("gray").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("gray"))
                 .define('C', TFMGBlocks.REBAR_CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 LIGHT_REBAR_GRAY_CONCRETE = create(TFMGBlocks.COLORED_REBAR_CONCRETE.get("light_gray").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("light_gray"))
                 .define('C', TFMGBlocks.REBAR_CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 BLUE_REBAR_CONCRETE = create(TFMGBlocks.COLORED_REBAR_CONCRETE.get("blue").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("blue"))
                 .define('C', TFMGBlocks.REBAR_CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 LIGHT_BLUE_REBAR_CONCRETE = create(TFMGBlocks.COLORED_REBAR_CONCRETE.get("light_blue").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("light_blue"))
                 .define('C', TFMGBlocks.REBAR_CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 CYAN_REBAR_CONCRETE = create(TFMGBlocks.COLORED_REBAR_CONCRETE.get("cyan").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("cyan"))
                 .define('C', TFMGBlocks.REBAR_CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 GREEN_REBAR_CONCRETE = create(TFMGBlocks.COLORED_REBAR_CONCRETE.get("green").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("green"))
                 .define('C', TFMGBlocks.REBAR_CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 LIME_REBAR_CONCRETE = create(TFMGBlocks.COLORED_REBAR_CONCRETE.get("lime").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("lime"))
                 .define('C', TFMGBlocks.REBAR_CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 PINK_REBAR_CONCRETE = create(TFMGBlocks.COLORED_REBAR_CONCRETE.get("pink").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("pink"))
                 .define('C', TFMGBlocks.REBAR_CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 PURPLE_REBAR_CONCRETE = create(TFMGBlocks.COLORED_REBAR_CONCRETE.get("purple").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("purple"))
                 .define('C', TFMGBlocks.REBAR_CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 MAGENTA_REBAR_CONCRETE = create(TFMGBlocks.COLORED_REBAR_CONCRETE.get("magenta").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("magenta"))
                 .define('C', TFMGBlocks.REBAR_CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 BROWN_REBAR_CONCRETE = create(TFMGBlocks.COLORED_REBAR_CONCRETE.get("brown").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("brown"))
                 .define('C', TFMGBlocks.REBAR_CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC")),


 YELLOW_REBAR_CONCRETE = create(TFMGBlocks.COLORED_REBAR_CONCRETE.get("yellow").block).returns(8)
         .unlockedBy(TFMGBlocks.CONCRETE.block::get)
         .viaShaped(b -> b
                 .define('D', DYES_FROM_COLOR.get("yellow"))
                 .define('C', TFMGBlocks.REBAR_CONCRETE.block)
                 .pattern("CCC")
                 .pattern("CDC")
                 .pattern("CCC"));

    /// ////////////////////

    String currentFolder = "";

    TFMGRecipeProvider.Marker enterFolder(String folder) {
        currentFolder = folder;
        return new TFMGRecipeProvider.Marker();
    }

    GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder(currentFolder, result);
    }

    GeneratedRecipeBuilder create(ResourceLocation result) {
        return new GeneratedRecipeBuilder(currentFolder, result);
    }

    GeneratedRecipeBuilder create(ItemProviderEntry<? extends ItemLike, ? extends ItemLike> result) {
        return create(result::get);
    }

    TFMGRecipeProvider.GeneratedRecipe createSpecial(Function<CraftingBookCategory, Recipe<?>> builder, String recipeType,
                                                       String path) {
        ResourceLocation location = TFMG.asResource(recipeType + "/" + currentFolder + "/" + path);
        return register(consumer -> {
            SpecialRecipeBuilder b = SpecialRecipeBuilder.special(builder);
            b.save(consumer, location.toString());
        });
    }

    TFMGRecipeProvider.GeneratedRecipe blastCrushedMetal(Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> ingredient) {
        return create(result::get).withSuffix("_from_crushed")
                .viaCooking(ingredient)
                .rewardXP(.1f)
                .inBlastFurnace();
    }

    TFMGRecipeProvider.GeneratedRecipe blastModdedCrushedMetal(ItemEntry<? extends Item> ingredient, CompatMetals metal) {
        for (Mods mod : metal.getMods()) {
            String metalName = metal.getName(mod);
            ResourceLocation ingot = mod.ingotOf(metalName);
            String modId = mod.getId();
            create(ingot).withSuffix("_compat_" + modId)
                    .whenModLoaded(modId)
                    .viaCooking(ingredient::get)
                    .rewardXP(.1f)
                    .inBlastFurnace();
        }
        return null;
    }

    TFMGRecipeProvider.GeneratedRecipe recycleGlass(BlockEntry<? extends Block> ingredient) {
        return create(() -> Blocks.GLASS).withSuffix("_from_" + ingredient.getId()
                        .getPath())
                .viaCooking(ingredient::get)
                .forDuration(50)
                .inFurnace();
    }

    TFMGRecipeProvider.GeneratedRecipe recycleGlassPane(BlockEntry<? extends Block> ingredient) {
        return create(() -> Blocks.GLASS_PANE).withSuffix("_from_" + ingredient.getId()
                        .getPath())
                .viaCooking(ingredient::get)
                .forDuration(50)
                .inFurnace();
    }

    TFMGRecipeProvider.GeneratedRecipe metalCompacting(List<ItemProviderEntry<? extends ItemLike, ? extends ItemLike>> variants,
                                                         List<Supplier<TagKey<Item>>> ingredients) {
        TFMGRecipeProvider.GeneratedRecipe result = null;
        for (int i = 0; i + 1 < variants.size(); i++) {
            ItemProviderEntry<? extends ItemLike, ? extends ItemLike> currentEntry = variants.get(i);
            ItemProviderEntry<? extends ItemLike, ? extends ItemLike> nextEntry = variants.get(i + 1);
            Supplier<TagKey<Item>> currentIngredient = ingredients.get(i);
            Supplier<TagKey<Item>> nextIngredient = ingredients.get(i + 1);

            result = create(nextEntry).withSuffix("_from_compacting")
                    .unlockedBy(currentEntry::get)
                    .viaShaped(b -> b.pattern("###")
                            .pattern("###")
                            .pattern("###")
                            .define('#', currentIngredient.get()));

            result = create(currentEntry).returns(9)
                    .withSuffix("_from_decompacting")
                    .unlockedBy(nextEntry::get)
                    .viaShapeless(b -> b.requires(nextIngredient.get()));
        }
        return result;
    }

    TFMGRecipeProvider.GeneratedRecipe conversionCycle(List<ItemProviderEntry<? extends ItemLike, ? extends ItemLike>> cycle) {
        TFMGRecipeProvider.GeneratedRecipe result = null;
        for (int i = 0; i < cycle.size(); i++) {
            ItemProviderEntry<? extends ItemLike, ? extends ItemLike> currentEntry = cycle.get(i);
            ItemProviderEntry<? extends ItemLike, ? extends ItemLike> nextEntry = cycle.get((i + 1) % cycle.size());
            result = create(nextEntry).withSuffix("_from_conversion")
                    .unlockedBy(currentEntry::get)
                    .viaShapeless(b -> b.requires(currentEntry.get()));
        }
        return result;
    }

    TFMGRecipeProvider.GeneratedRecipe clearData(ItemProviderEntry<? extends ItemLike, ? extends ItemLike> item) {
        return create(item).withSuffix("_clear")
                .unlockedBy(item::get)
                .viaShapeless(b -> b.requires(item.get()));
    }

    class GeneratedRecipeBuilder {

        private String path;
        private String suffix;
        private Supplier<? extends ItemLike> result;
        private ResourceLocation compatDatagenOutput;
        List<ICondition> recipeConditions;

        private Supplier<ItemPredicate> unlockedBy;
        private int amount;

        private GeneratedRecipeBuilder(String path) {
            this.path = path;
            this.recipeConditions = new ArrayList<>();
            this.suffix = "";
            this.amount = 1;
        }

        public GeneratedRecipeBuilder(String path, Supplier<? extends ItemLike> result) {
            this(path);
            this.result = result;
        }

        public GeneratedRecipeBuilder(String path, ResourceLocation result) {
            this(path);
            this.compatDatagenOutput = result;
        }

        GeneratedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }

        GeneratedRecipeBuilder unlockedBy(Supplier<? extends ItemLike> item) {
            this.unlockedBy = () -> ItemPredicate.Builder.item()
                    .of(item.get())
                    .build();
            return this;
        }

        GeneratedRecipeBuilder unlockedByTag(Supplier<TagKey<Item>> tag) {
            this.unlockedBy = () -> ItemPredicate.Builder.item()
                    .of(tag.get())
                    .build();
            return this;
        }

        GeneratedRecipeBuilder whenModLoaded(String modid) {
            return withCondition(new ModLoadedCondition(modid));
        }

        GeneratedRecipeBuilder whenModMissing(String modid) {
            return withCondition(new NotCondition(new ModLoadedCondition(modid)));
        }

        GeneratedRecipeBuilder withCondition(ICondition condition) {
            recipeConditions.add(condition);
            return this;
        }

        GeneratedRecipeBuilder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        // FIXME 5.1 refactor - recipe categories as markers instead of sections?
        TFMGRecipeProvider.GeneratedRecipe viaShaped(UnaryOperator<ShapedRecipeBuilder> builder) {
            return register(consumer -> {
                ShapedRecipeBuilder b =
                        builder.apply(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        TFMGRecipeProvider.GeneratedRecipe viaShapeless(UnaryOperator<ShapelessRecipeBuilder> builder) {
            return register(recipeOutput -> {
                ShapelessRecipeBuilder b =
                        builder.apply(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));

                RecipeOutput conditionalOutput = recipeOutput.withConditions(recipeConditions.toArray(new ICondition[0]));

                b.save(recipeOutput, createLocation("crafting"));
            });
        }

        TFMGRecipeProvider.GeneratedRecipe viaNetheriteSmithing(Supplier<? extends Item> base, Supplier<Ingredient> upgradeMaterial) {
            return register(consumer -> {
                SmithingTransformRecipeBuilder b =
                        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                                Ingredient.of(base.get()), upgradeMaterial.get(), RecipeCategory.COMBAT, result.get()
                                        .asItem());
                b.unlocks("has_item", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(base.get())
                        .build()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        private ResourceLocation createSimpleLocation(String recipeType) {
            return TFMG.asResource(recipeType + "/" + getRegistryName().getPath() + suffix);
        }

        private ResourceLocation createLocation(String recipeType) {
            return TFMG.asResource(recipeType + "/" + path + "/" + getRegistryName().getPath() + suffix);
        }

        private ResourceLocation getRegistryName() {
            return compatDatagenOutput == null ? RegisteredObjectsHelper.getKeyOrThrow(result.get()
                    .asItem()) : compatDatagenOutput;
        }

        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCooking(Supplier<? extends ItemLike> item) {
            return unlockedBy(item).viaCookingIngredient(() -> Ingredient.of(item.get()));
        }

        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCookingTag(Supplier<TagKey<Item>> tag) {
            return unlockedByTag(tag).viaCookingIngredient(() -> Ingredient.of(tag.get()));
        }

        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCookingIngredient(Supplier<Ingredient> ingredient) {
            return new GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder(ingredient);
        }

        class GeneratedCookingRecipeBuilder {

            private Supplier<Ingredient> ingredient;
            private float exp;
            private int cookingTime;

            GeneratedCookingRecipeBuilder(Supplier<Ingredient> ingredient) {
                this.ingredient = ingredient;
                cookingTime = 200;
                exp = 0;
            }

            GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder forDuration(int duration) {
                cookingTime = duration;
                return this;
            }

            GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder rewardXP(float xp) {
                exp = xp;
                return this;
            }

            TFMGRecipeProvider.GeneratedRecipe inFurnace() {
                return inFurnace(b -> b);
            }

            TFMGRecipeProvider.GeneratedRecipe inFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                return create(RecipeSerializer.SMELTING_RECIPE, builder, SmeltingRecipe::new, 1);
            }

            TFMGRecipeProvider.GeneratedRecipe inSmoker() {
                return inSmoker(b -> b);
            }

            TFMGRecipeProvider.GeneratedRecipe inSmoker(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(RecipeSerializer.SMELTING_RECIPE, builder, SmeltingRecipe::new, 1);
                create(RecipeSerializer.CAMPFIRE_COOKING_RECIPE, builder, CampfireCookingRecipe::new, 3);
                return create(RecipeSerializer.SMOKING_RECIPE, builder, SmokingRecipe::new, .5f);
            }

            TFMGRecipeProvider.GeneratedRecipe inBlastFurnace() {
                return inBlastFurnace(b -> b);
            }

            TFMGRecipeProvider.GeneratedRecipe inBlastFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(RecipeSerializer.SMELTING_RECIPE, builder, SmeltingRecipe::new, 1);
                return create(RecipeSerializer.BLASTING_RECIPE, builder, BlastingRecipe::new, .5f);
            }

            private <T extends AbstractCookingRecipe> TFMGRecipeProvider.GeneratedRecipe create(RecipeSerializer<T> serializer,
                                                                                                  UnaryOperator<SimpleCookingRecipeBuilder> builder, AbstractCookingRecipe.Factory<T> factory, float cookingTimeModifier) {
                return register(recipeOutput -> {
                    boolean isOtherMod = compatDatagenOutput != null;

                    SimpleCookingRecipeBuilder b = builder.apply(SimpleCookingRecipeBuilder.generic(ingredient.get(),
                            RecipeCategory.MISC, isOtherMod ? Items.DIRT : result.get(), exp,
                            (int) (cookingTime * cookingTimeModifier), serializer, factory));
                    if (unlockedBy != null)
                        b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));

                    RecipeOutput conditionalOutput = recipeOutput.withConditions(recipeConditions.toArray(new ICondition[0]));

                    b.save(
                            isOtherMod ? new ModdedCookingRecipeOutput(conditionalOutput, compatDatagenOutput) : conditionalOutput,
                            createSimpleLocation(RegisteredObjectsHelper.getKeyOrThrow(serializer).getPath())
                    );
                });
            }
        }
    }



    public TFMGStandardRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    private static class ModdedCookingRecipeOutputShim implements Recipe<RecipeInput> {

        private static final Map<RecipeType<?>, ModdedCookingRecipeOutputShim.Serializer> serializers = new ConcurrentHashMap<>();

        private final Recipe<?> wrapped;
        private final ResourceLocation overrideID;

        private ModdedCookingRecipeOutputShim(Recipe<?> wrapped, ResourceLocation overrideID) {
            this.wrapped = wrapped;
            this.overrideID = overrideID;
        }

        @Override
        public boolean matches(RecipeInput recipeInput, Level level) {
            throw new AssertionError("Only for datagen output");
        }

        @Override
        public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
            throw new AssertionError("Only for datagen output");
        }

        @Override
        public boolean canCraftInDimensions(int pWidth, int pHeight) {
            throw new AssertionError("Only for datagen output");
        }

        @Override
        public ItemStack getResultItem(HolderLookup.Provider registries) {
            throw new AssertionError("Only for datagen output");
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return serializers.computeIfAbsent(
                    getType(),
                    t -> ModdedCookingRecipeOutputShim.Serializer.create(wrapped)
            );
        }

        @Override
        public RecipeType<?> getType() {
            return wrapped.getType();
        }

        private record Serializer(MapCodec<Recipe<?>> wrappedCodec) implements RecipeSerializer<ModdedCookingRecipeOutputShim> {
            private static ModdedCookingRecipeOutputShim.Serializer create(Recipe<?> wrapped) {
                RecipeSerializer<?> wrappedSerializer = wrapped.getSerializer();
                @SuppressWarnings("unchecked")
                ModdedCookingRecipeOutputShim.Serializer serializer = new ModdedCookingRecipeOutputShim.Serializer((MapCodec<Recipe<?>>) wrappedSerializer.codec());

                // Need to do some registry injection to get the Recipe/Registry#byNameCodec to encode the right type for this
                // getResourceKey and getId
                // byValue and toId
                // Holder.Reference: key
                if (BuiltInRegistries.RECIPE_SERIALIZER instanceof MappedRegistryAccessor<?> mra) {
                    @SuppressWarnings("unchecked")
                    MappedRegistryAccessor<RecipeSerializer<?>> mra$ = (MappedRegistryAccessor<RecipeSerializer<?>>) mra;

                    int wrappedId = mra$.getToId().getOrDefault(wrappedSerializer, -1);
                    ResourceKey<RecipeSerializer<?>> wrappedKey = mra$.getByValue().get(wrappedSerializer).key();

                    mra$.getToId().put(serializer, wrappedId);
                    //noinspection DataFlowIssue - it is ok to pass null as the owner, because this is only being used for serialization
                    mra$.getByValue().put(serializer, Holder.Reference.createStandAlone(null, wrappedKey));
                } else {
                    throw new AssertionError("ModdedCookingRecipeOutputShim will not be able to" +
                            " serialize without injecting into a registry. Expected" +
                            " BuiltInRegistries.RECIPE_SERIALIZER to be of class MappedRegistry, is of class " +
                            BuiltInRegistries.RECIPE_SERIALIZER.getClass()
                    );
                }
                return serializer;
            }

            @Override
            public MapCodec<ModdedCookingRecipeOutputShim> codec() {
                return RecordCodecBuilder.mapCodec(instance -> instance.group(
                        wrappedCodec.forGetter(i -> i.wrapped),
                        ModdedCookingRecipeOutputShim.FakeItemStack.CODEC.fieldOf("result").forGetter(i -> new ModdedCookingRecipeOutputShim.FakeItemStack(i.overrideID))
                ).apply(instance, (wrappedRecipe, fakeItemStack) -> {
                    throw new AssertionError("Only for datagen output");
                }));
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, ModdedCookingRecipeOutputShim> streamCodec() {
                throw new AssertionError("Only for datagen output");
            }
        }

        private record FakeItemStack(ResourceLocation id) {
            public static Codec<ModdedCookingRecipeOutputShim.FakeItemStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(ModdedCookingRecipeOutputShim.FakeItemStack::id)
            ).apply(instance, ModdedCookingRecipeOutputShim.FakeItemStack::new));
        }
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    private record ModdedCookingRecipeOutput(RecipeOutput wrapped, ResourceLocation outputOverride) implements RecipeOutput {

        @Override
        public Advancement.Builder advancement() {
            return wrapped.advancement();
        }

        @Override
        public void accept(ResourceLocation id, Recipe<?> recipe, @Nullable AdvancementHolder advancement, ICondition... conditions) {
            wrapped.accept(id, new ModdedCookingRecipeOutputShim(recipe, outputOverride), advancement, conditions);
        }
    }
}
