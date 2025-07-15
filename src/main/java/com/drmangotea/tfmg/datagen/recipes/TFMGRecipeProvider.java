package com.drmangotea.tfmg.datagen.recipes;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGRegistrate;
import com.drmangotea.tfmg.content.decoration.pipes.TFMGPipes;
import com.drmangotea.tfmg.datagen.recipes.values.create.*;
import com.drmangotea.tfmg.datagen.recipes.values.tfmg.*;
import com.drmangotea.tfmg.registry.*;
import com.simibubi.create.*;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.content.decoration.palettes.AllPaletteBlocks;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.foundation.data.recipe.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
public class TFMGRecipeProvider extends RecipeProvider {

    static final List<ProcessingRecipeGen<?, ?, ?>> GENERATORS = new ArrayList<>();

    protected final List<TFMGRecipeProvider.GeneratedRecipe> all = new ArrayList<>();

    public TFMGRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput pRecipeOutput) {
        all.forEach(c -> c.register(pRecipeOutput));
        TFMG.LOGGER.info("{} registered {} recipe{}", getName(), all.size(), all.size() == 1 ? "" : "s");
    }


    public static void registerAllProcessing(DataGenerator gen, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        GENERATORS.add(new TFMGCokingRecipeGen(output,registries));
        GENERATORS.add(new TFMGDistillationRecipeGen(output,registries));
        GENERATORS.add(new TFMGWindingRecipeGen(output,registries));
        GENERATORS.add(new TFMGPolarizingRecipeGen(output,registries));
        GENERATORS.add(new TFMGHotBlastRecipeGen(output,registries));
        GENERATORS.add(new TFMGCastingRecipeGen(output,registries));
        GENERATORS.add(new TFMGIndustrialBlastingRecipeGen(output,registries));
        GENERATORS.add(new TFMGVatRecipeGen(output,registries));

        GENERATORS.add(new TFMGPressingRecipeGen(output,registries));
        GENERATORS.add(new TFMGItemApplicationRecipeGen(output,registries));
        GENERATORS.add(new TFMGFillingRecipeGen(output,registries));
        GENERATORS.add(new TFMGMixingRecipeGen(output,registries));
        GENERATORS.add(new TFMGCompactingRecipeGen(output,registries));
        GENERATORS.add(new TFMGCrushingRecipeGen(output,registries));
        GENERATORS.add(new TFMGDeployingRecipeGen(output,registries));

        gen.addProvider(true, new DataProvider() {

            @Override
            public String getName() {
                return "TFMG's Processing Recipes";
            }

            @Override
            public CompletableFuture<?> run(CachedOutput dc) {
                return CompletableFuture.allOf(GENERATORS.stream()
                        .map(gen -> gen.run(dc))
                        .toArray(CompletableFuture[]::new));
            }
        });
    }

    protected TFMGRecipeProvider.GeneratedRecipe register(TFMGRecipeProvider.GeneratedRecipe recipe) {
        all.add(recipe);
        return recipe;
    }

    @FunctionalInterface
    public interface GeneratedRecipe {
        void register(RecipeOutput output);
    }

    public static class Marker {
    }


    public static class I {

        public static ItemLike tfmgSteelIngot() {
            return TFMGItems.STEEL_INGOT;
        }
        public static ItemLike coal() {
            return Items.COAL;
        }
        public static ItemLike charcoal() {
            return Items.CHARCOAL;
        }
        public static ItemLike coalCoke() {
            return TFMGItems.COAL_COKE.get();
        }
        public static ItemLike coalCokeDust() {
            return TFMGItems.COAL_COKE_DUST.get();
        }
        public static TagKey<Item> steelIngot() {
            return AllTags.commonItemTag("ingots/steel");
        }
        public static TagKey<Item> steelNugget() {
            return AllTags.commonItemTag("nuggets/steel");
        }
        public static TagKey<Item> steelSheet() {
            return AllTags.commonItemTag("plates/steel");
        }
        public static TagKey<Item> steelBlock() {
            return AllTags.commonItemTag("storage_blocks/steel");
        }
        public static TagKey<Item> aluminumIngot() {
            return AllTags.commonItemTag("ingots/aluminum");
        }
        public static TagKey<Item> aluminumNugget() {
            return AllTags.commonItemTag("nuggets/aluminum");
        }
        public static TagKey<Item> aluminumBlock() {
            return AllTags.commonItemTag("storage_blocks/aluminum");
        }
        public static TagKey<Item> aluminumSheet() {
            return AllTags.commonItemTag("plates/aluminum");
        }
        public static TagKey<Item> castIronIngot() {
            return AllTags.commonItemTag("ingots/cast_iron");
        }
        public static TagKey<Item> castIronNugget() {
            return AllTags.commonItemTag("nuggets/cast_iron");
        }
        public static TagKey<Item> castIronBlock() {
            return AllTags.commonItemTag("storage_blocks/cast_iron");
        }
        public static TagKey<Item> castIronSheet() {
            return AllTags.commonItemTag("plates/cast_iron");
        }
        public static TagKey<Item> leadIngot() {
            return AllTags.commonItemTag("ingots/lead");
        }
        public static TagKey<Item> leadNugget() {
            return AllTags.commonItemTag("nuggets/lead");
        }
        public static TagKey<Item> leadSheet() {
            return AllTags.commonItemTag("plates/lead");
        }
        public static TagKey<Item> leadBlock() {
            return AllTags.commonItemTag("storage_blocks/lead");
        }
        public static TagKey<Item> nickelIngot() {
            return AllTags.commonItemTag("ingots/nickel");
        }
        public static TagKey<Item> nickelNugget() {
            return AllTags.commonItemTag("nuggets/nickel");
        }
        public static TagKey<Item> nickelBlock() {
            return AllTags.commonItemTag("storage_blocks/nickel");
        }
        public static TagKey<Item> nickelSheet() {
            return AllTags.commonItemTag("plates/nickel");
        }
        public static TagKey<Item> lithiumIngot() {
            return AllTags.commonItemTag("ingots/lithium");
        }
        public static TagKey<Item> lithiumNugget() {
            return AllTags.commonItemTag("nuggets/lithium");
        }
        public static TagKey<Item> lithiumBlock() {
            return AllTags.commonItemTag("storage_blocks/lithium");
        }
        public static TagKey<Item> constantanIngot() {
            return AllTags.commonItemTag("ingots/constantan");
        }
        public static TagKey<Item> constantanNugget() {
            return AllTags.commonItemTag("nuggets/constantan");
        }
        public static TagKey<Item> constantanBlock() {
            return AllTags.commonItemTag("storage_blocks/constantan");
        }
        public static TagKey<Item> copperWire() {
            return AllTags.commonItemTag("wires/copper");
        }
        public static TagKey<Item> constantanWire() {
            return AllTags.commonItemTag("wires/constantan");
        }
        public static TagKey<Item> aluminumWire() {
            return AllTags.commonItemTag("wires/aluminum");
        }

        //



        //

        public static ItemLike bitumen() {
            return TFMGItems.BITUMEN.get();
        }
        public static ItemLike cinderFlour() {
            return AllItems.CINDER_FLOUR.get();
        }
        public static ItemLike plasticSheet() {
            return TFMGItems.PLASTIC_SHEET.get();
        }


        public static ItemLike crimsite() {
            return AllPaletteStoneTypes.CRIMSITE.getBaseBlock().get();
        }
        public static ItemLike thermitePowder() {
            return TFMGItems.THERMITE_POWDER.get();
        }
        public static ItemLike crushedRawAluminum() {
            return AllItems.CRUSHED_BAUXITE.get();
        }
        public static ItemLike experienceNugget() {
            return AllItems.EXP_NUGGET.get();
        }
        public static ItemLike capacitor() {
            return TFMGItems.CAPACITOR;
        }
        public static ItemLike copperSulfate() {
            return TFMGItems.COPPER_SULFATE.get();
        }
        public static ItemLike boneMeal() {
            return Items.BONE_MEAL;
        }
        public static ItemLike blueDye() {
            return Items.BLUE_DYE;
        }
        public static ItemLike greenDye() {
            return Items.GREEN_DYE;
        }
        public static ItemLike cyanDye() {
            return Items.CYAN_DYE;
        }
        public static ItemLike crushedRawLead() {
            return AllItems.CRUSHED_LEAD.get();
        }
        public static ItemLike limestone() {
            return AllPaletteStoneTypes.LIMESTONE.getBaseBlock().get();
        }
        public static ItemLike limesand() {
            return TFMGItems.LIMESAND.get();
        }
        public static ItemLike dirt() {
            return Items.DIRT;
        }
        public static ItemLike rebar() {
            return TFMGItems.REBAR.get();
        }
        public static ItemLike nitrateDust() {
            return TFMGItems.NITRATE_DUST.get();
        }
        public static ItemLike sulfurDust() {
            return TFMGItems.SULFUR_DUST.get();
        }
        public static ItemLike bucket() {
            return Items.BUCKET;
        }
        public static ItemLike bottle() {
            return Items.GLASS_BOTTLE;
        }
        public static ItemLike potato() {
            return Items.POTATO;
        }
        public static ItemLike napalmPotato() {
            return TFMGItems.NAPALM_POTATO.get();
        }
        public static ItemLike heavyMachineryCasing() {
           return TFMGBlocks.HEAVY_MACHINERY_CASING.get();
        }
        public static ItemLike steelCasing() {
            return TFMGBlocks.STEEL_CASING.get();
        }
        public static ItemLike aluminumCasing() {
            return TFMGBlocks.ALUMINUM_CASING.get();
        }
        public static ItemLike lightBulb() {
            return TFMGBlocks.LIGHT_BULB.get();
        }


        public static ItemLike heavyPlate() {
            return TFMGItems.HEAVY_PLATE.get();
        }
        public static ItemLike castIronSheetTFMG() {
            return TFMGItems.CAST_IRON_SHEET.get();
        }
        public static ItemLike nickelSheetTFMG() {
            return TFMGItems.NICKEL_SHEET.get();
        }
        public static ItemLike leadSheetTFMG() {
            return TFMGItems.LEAD_SHEET.get();
        }
        public static ItemLike aluminumSheetTFMG() {
            return TFMGItems.ALUMINUM_SHEET.get();
        }
        public static ItemLike crushedRawIron() {
            return AllItems.CRUSHED_IRON.get();
        }
        public static ItemLike ironIngot() {
            return Items.IRON_INGOT;
        }
        public static ItemLike clayBall() {
            return Items.CLAY_BALL;
        }
        public static ItemLike sand() {
            return Items.SAND;
        }
        public static ItemLike gravel() {
            return Items.GRAVEL;
        }
        public static ItemLike concreteMixture() {
            return TFMGItems.CONCRETE_MIXTURE.get();
        }
        public static ItemLike asphaltMixture() {
            return TFMGItems.ASPHALT_MIXTURE.get();
        }
        public static ItemLike slag() {
            return TFMGItems.SLAG.get();
        }
        public static ItemLike gunpowder() {
            return Items.GUNPOWDER;
        }
        public static ItemLike steelTank() {
            return TFMGBlocks.STEEL_FLUID_TANK;
        }
        public static ItemLike castIronTank() {
            return TFMGBlocks.CAST_IRON_FLUID_TANK;
        }
        public static ItemLike circuitBoard() {
            return TFMGItems.CIRCUIT_BOARD;
        }
        public static ItemLike cement() {
            return TFMGBlocks.CEMENT;
        }
        public static ItemLike aluminumTank() {
            return TFMGBlocks.ALUMINUM_FLUID_TANK;
        }
        public static ItemLike syntheticLeather() {
            return TFMGItems.SYNTHETIC_LEATHER.get();
        }
        public static ItemLike engineChamber() {
            return TFMGItems.ENGINE_CYLINDER.get();
        }
        public static ItemLike screw() {
            return TFMGItems.SCREW.get();
        }
        public static ItemLike steelMechanism() {
            return TFMGItems.STEEL_MECHANISM.get();
        }
        public static ItemLike industrialPipe() {
            return TFMGBlocks.INDUSTRIAL_PIPE.get();
        }
        public static ItemLike steelPipe() {
            return TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.STEEL).get(0);
        }
        public static ItemLike brassPipe() {
            return TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.BRASS).get(0);
        }
        public static ItemLike castIronPipe() {
            return TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.CAST_IRON).get(0);
        }
        public static ItemLike aluminumPipe() {
            return TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.ALUMINUM).get(0);
        }
        public static ItemLike plasticPipe() {
            return TFMGPipes.TFMG_PIPES.get(TFMGPipes.PipeMaterial.PLASTIC).get(0);
        }
        public static ItemLike magneticIngot() {
            return TFMGItems.MAGNETIC_ALLOY_INGOT.get();
        }
        public static ItemLike magnet() {
            return TFMGItems.MAGNET.get();
        }
        //
        public static TagKey<Item> redstone() {
            return Tags.Items.DUSTS_REDSTONE;
        }

        public static TagKey<Item> planks() {
            return ItemTags.PLANKS;
        }

        public static TagKey<Item> woodSlab() {
            return ItemTags.WOODEN_SLABS;
        }
        public static TagKey<Item> log() {
            return ItemTags.LOGS_THAT_BURN;
        }
        public static TagKey<Item> gold() {
            return AllTags.commonItemTag("ingots/gold");
        }
        public static TagKey<Item> string() {
            return AllTags.commonItemTag("string");
        }

        public static ItemLike propeller() {
            return AllItems.PROPELLER.get();
        }
        public static ItemLike framedGlass() {
            return AllPaletteBlocks.FRAMED_GLASS;
        }
        //"steel", "aluminum", "cast_iron", "lead", "nickel","constantan" , "copper", "zinc", "brass"
        public static ItemLike steelTruss() {
            return TFMGBlocks.TRUSSES.get(0).get();
        }
        public static ItemLike steelFrame() {
            return TFMGBlocks.FRAMES.get(0).get();
        }

        public static ItemLike aluminumTruss() {
            return TFMGBlocks.TRUSSES.get(1).get();
        }
        public static ItemLike aluminumFrame() {
            return TFMGBlocks.FRAMES.get(1).get();
        }


        public static ItemLike castIronTruss() {
            return TFMGBlocks.TRUSSES.get(2).get();
        }
        public static ItemLike castIronFrame() {
            return TFMGBlocks.FRAMES.get(2).get();
        }


        public static ItemLike leadTruss() {
            return TFMGBlocks.TRUSSES.get(3).get();
        }
        public static ItemLike leadFrame() {
            return TFMGBlocks.FRAMES.get(3).get();
        }

        public static ItemLike nickelTruss() {
            return TFMGBlocks.TRUSSES.get(4).get();
        }
        public static ItemLike nickelFrame() {
            return TFMGBlocks.FRAMES.get(4).get();
        }

        public static ItemLike constantanTruss() {
            return TFMGBlocks.TRUSSES.get(5).get();
        }
        public static ItemLike constantanFrame() {
            return TFMGBlocks.FRAMES.get(5).get();
        }

        public static ItemLike copperTruss() {
            return TFMGBlocks.TRUSSES.get(6).get();
        }
        public static ItemLike copperFrame() {
            return TFMGBlocks.FRAMES.get(6).get();
        }

        public static ItemLike zincTruss() {
            return TFMGBlocks.TRUSSES.get(7).get();
        }
        public static ItemLike zincFrame() {
            return TFMGBlocks.FRAMES.get(7).get();
        }

        public static ItemLike brassTruss() {
            return TFMGBlocks.TRUSSES.get(8).get();
        }
        public static ItemLike brassFrame() {
            return TFMGBlocks.FRAMES.get(8).get();
        }



        public static TagKey<Item> goldSheet() {
            return AllTags.commonItemTag("plates/gold");
        }

        //public static TagKey<Item> stone() {
        //    return Tags.Items.STONE;
        //}

        public static ItemLike andesite() {
            return AllItems.ANDESITE_ALLOY.get();
        }

        public static ItemLike fireproofBricks() {
            return TFMGBlocks.FIREPROOF_BRICKS.get();
        }


        public static ItemLike shaft() {
            return AllBlocks.SHAFT.get();
        }

        public static ItemLike cog() {
            return AllBlocks.COGWHEEL.get();
        }

        public static ItemLike rubber() {
            return TFMGItems.RUBBER_SHEET.get();
        }

        public static ItemLike largeCog() {
            return AllBlocks.LARGE_COGWHEEL.get();
        }

        public static ItemLike andesiteCasing() {
            return AllBlocks.ANDESITE_CASING.get();
        }

        public static TagKey<Item> brassIngot() {
            return AllTags.commonItemTag("ingots/brass");
        }

        public static TagKey<Item> brassSheet() {
            return AllTags.commonItemTag("plates/brass");
        }

        public static TagKey<Item> iron() {
            return Tags.Items.INGOTS_IRON;
        }

        public static TagKey<Item> ironNugget() {
            return AllTags.commonItemTag("nuggets/iron");
        }

        public static TagKey<Item> ironDust() {
            return AllTags.commonItemTag("dusts/iron");
        }

        public static TagKey<Item> zincIngot() {
            return AllTags.commonItemTag("ingots/zinc");
        }

        public static TagKey<Item> ironSheet() {
            return AllTags.commonItemTag("plates/iron");
        }

        public static TagKey<Item> sturdySheet() {
            return AllTags.commonItemTag("plates/obsidian");
        }

        public static ItemLike brassCasing() {
            return AllBlocks.BRASS_CASING.get();
        }

        public static ItemLike railwayCasing() {
            return AllBlocks.RAILWAY_CASING.get();
        }

        public static ItemLike electronTube() {
            return AllItems.ELECTRON_TUBE.get();
        }

        public static ItemLike precisionMechanism() {
            return AllItems.PRECISION_MECHANISM.get();
        }

        public static ItemLike copperBlock() {
            return Items.COPPER_BLOCK;
        }

        public static TagKey<Item> brassBlock() {
            return AllTags.commonItemTag("storage_blocks/brass");
        }

        public static TagKey<Item> zincBlock() {
            return AllTags.commonItemTag("storage_blocks/zinc");
        }

        public static TagKey<Item> wheatFlour() {
            return AllTags.commonItemTag("flour/wheat");
        }

        public static ItemLike copperIngot() {
            return Items.COPPER_INGOT;
        }

        public static TagKey<Item> copperSheet() {
            return AllTags.commonItemTag("plates/copper");
        }

        public static TagKey<Item> copperNugget() {
            return AllTags.commonItemTag("nuggets/copper");
        }

        public static TagKey<Item> brassNugget() {
            return AllTags.commonItemTag("nuggets/brass");
        }

        public static TagKey<Item> zincNugget() {
            return AllTags.commonItemTag("nuggets/zinc");
        }

        public static ItemLike copperCasing() {
            return AllBlocks.COPPER_CASING.get();
        }

        public static ItemLike refinedRadiance() {
            return AllItems.REFINED_RADIANCE.get();
        }

        public static ItemLike shadowSteel() {
            return AllItems.SHADOW_STEEL.get();
        }

        public static Ingredient netherite() {
            return Ingredient.of(AllTags.commonItemTag("ingots/netherite"));
        }


        public static ItemStack resistor10Ohms(){
            ItemStack stack = TFMGBlocks.RESISTOR.asStack();

            stack.set(TFMGDataComponents.RESISTANCE,10);

            return stack;
        }

        public static ItemStack coil100Turns(){
            ItemStack stack = TFMGItems.ELECTROMAGNETIC_COIL.asStack();
            stack.set(TFMGDataComponents.COIL_TURNS,100);
            return stack;
        }

        public static ItemStack turbineBlade(){
            ItemStack stack = TFMGItems.TURBINE_BLADE.asStack();

            CompoundTag keroseneTag = new CompoundTag();
            keroseneTag.putString("kerosene", "forge:kerosene");
            CompoundTag keroseneTagName = new CompoundTag();
            keroseneTagName.putString("kerosene", "Kerosene");

            stack.set(TFMGDataComponents.FUEL_TAGS, keroseneTag);
            stack.set(TFMGDataComponents.FUELS, keroseneTagName);

            return stack;
        }


    }

    public static class F {
        //GASSES
        public static Fluid air() {
            return TFMGFluids.AIR.get();
        }
        public static Fluid hotAir() {
            return TFMGFluids.HOT_AIR.get();
        }
        public static Fluid carbonDioxide() {
            return TFMGFluids.CARBON_DIOXIDE.get();
        }
        public static Fluid ethylene() {
            return TFMGFluids.ETHYLENE.get();
        }
        public static Fluid propylene() {
            return TFMGFluids.PROPYLENE.get();
        }
        public static Fluid propane() {
            return TFMGFluids.PROPANE.get();
        }
        public static Fluid hydrogen() {
            return TFMGFluids.HYDROGEN.get();
        }
        public static Fluid butane() {
            return TFMGFluids.BUTANE.get();
        }
        public static Fluid lpg() {
            return TFMGFluids.LPG.get();
        }
        public static Fluid neon() {
            return TFMGFluids.NEON.get();
        }
        public static Fluid blastFurnaceGas() {
            return TFMGFluids.FURNACE_GAS.get();
        }

        //LIQUIDS
        public static Fluid crudeOil() {
            return TFMGFluids.CRUDE_OIL.get();
        }
        public static Fluid heavyOil() {
            return TFMGFluids.HEAVY_OIL.get();
        }
        public static Fluid lubricationOil() {
            return TFMGFluids.LUBRICATION_OIL.get();
        }
       public static Fluid napalm() {
           return TFMGFluids.NAPALM.get();
       }
        public static Fluid naphtha() {
            return TFMGFluids.NAPHTHA.get();
        }
        public static Fluid kerosene() {
            return TFMGFluids.KEROSENE.get();
        }
        public static Fluid gasoline() {
            return TFMGFluids.GASOLINE.get();
        }
        public static Fluid diesel() {
            return TFMGFluids.DIESEL.get();
        }
        public static Fluid creosote() {
            return TFMGFluids.CREOSOTE.get();
        }
        public static Fluid water() {
            return Fluids.WATER;
        }

        //MISC
        public static Fluid coolingFluid() {
            return TFMGFluids.COOLING_FLUID.get();
        }
        public static Fluid sulfuricAcid() {
            return TFMGFluids.SULFURIC_ACID.get();
        }
        public static Fluid liquidConcrete() {
            return TFMGFluids.LIQUID_CONCRETE.get();
        }
        public static Fluid liquidAsphalt() {
            return TFMGFluids.LIQUID_ASPHALT.get();
        }
        public static Fluid liquidPlastic() {
            return TFMGFluids.MOLTEN_PLASTIC.get();
        }
        public static Fluid moltenSteel() {
            return TFMGFluids.MOLTEN_STEEL.get();
        }
        public static Fluid moltenSlag() {
            return TFMGFluids.MOLTEN_SLAG.get();
        }
        public static Fluid potion() {
            return AllFluids.POTION.get();
        }

        //BUCKETS
        public static ItemLike airTank() {
            return TFMGRegistrate.getBucket("air");
        }
        public static ItemLike hotAirTank() {
            return TFMGRegistrate.getBucket("hot_air");
        }
        public static ItemLike carbonDioxideTank() {
            return TFMGRegistrate.getBucket("carbon_dioxide");
        }
        public static ItemLike ethyleneTank() {
            return TFMGRegistrate.getBucket("ethylene");
        }
        public static ItemLike propyleneTank() {
            return TFMGRegistrate.getBucket("propylene");
        }
        public static ItemLike propaneTank() {
            return TFMGRegistrate.getBucket("propane");
        }
        public static ItemLike hydrogenTank() {
            return TFMGRegistrate.getBucket("hydrogen");
        }
        public static ItemLike butaneTank() {
            return TFMGRegistrate.getBucket("butane");
        }
        public static ItemLike lpgTank() {
            return TFMGRegistrate.getBucket("lpg");
        }
        public static ItemLike neonTank() {
            return TFMGRegistrate.getBucket("neon");
        }
        public static ItemLike furnaceGasTank() {
            return TFMGRegistrate.getBucket("furnace_gas");
        }
        public static ItemLike crudeOilBucket() {
            return TFMGRegistrate.getBucket("crude_oil");
        }
        public static ItemLike heavyOilBucket() {
            return TFMGRegistrate.getBucket("heavy_oil");
        }
        public static ItemLike lubricationOilBucket() {
            return TFMGRegistrate.getBucket("lubrication_oil");
        }
        public static ItemLike napalmBucket() {
            return TFMGRegistrate.getBucket("napalm");
        }
        public static ItemLike naphthaBucket() {
            return TFMGRegistrate.getBucket("naphtha");
        }
        public static ItemLike keroseneBucket() {
            return TFMGRegistrate.getBucket("kerosene");
        }
        public static ItemLike gasolineBucket() {
            return TFMGRegistrate.getBucket("gasoline");
        }
        public static ItemLike dieselBucket() {
            return TFMGRegistrate.getBucket("diesel");
        }
        public static ItemLike creosoteBucket() {
            return TFMGRegistrate.getBucket("creosote");
        }
        public static ItemLike coolingFluidBucket() {
            return TFMGRegistrate.getBucket("cooling_fluid");
        }
        public static ItemLike sulfuricAcidBucket() {
            return TFMGRegistrate.getBucket("sulfuric_acid");
        }
        public static ItemLike liquidConcreteBucket() {
            return TFMGRegistrate.getBucket("liquid_concrete");
        }
        public static ItemLike liquidAsphaltBucket() {
            return TFMGRegistrate.getBucket("liquid_asphalt");
        }
        public static ItemLike liquidPlasticBucket() {
            return TFMGRegistrate.getBucket("liquid_plastic");
        }
        public static ItemLike moltenSteelBucket() {
            return TFMGRegistrate.getBucket("molten_steel");
        }
        public static ItemLike moltenSlagBucket() {
            return TFMGRegistrate.getBucket("molten_slag");
        }
        public static ItemLike waterBucket() {
            return Fluids.WATER.getBucket();
        }
    }

}