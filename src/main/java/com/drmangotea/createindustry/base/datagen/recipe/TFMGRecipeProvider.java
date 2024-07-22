package com.drmangotea.createindustry.base.datagen.recipe;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.TFMGRegistrate;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGFluids;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.drmangotea.createindustry.registry.TFMGPaletteStoneTypes;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.DataIngredient;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class TFMGRecipeProvider extends RecipeProvider {
    
    protected final List<GeneratedRecipe> all = new ArrayList<>();
    
    public TFMGRecipeProvider(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        all.forEach(c -> c.register(consumer));
        CreateTFMG.LOGGER.info("{} registered {} recipe{}", getName(), all.size(), all.size() == 1 ? "" : "s");
    }
    
    protected GeneratedRecipe register(GeneratedRecipe recipe) {
        all.add(recipe);
        return recipe;
    }
    
    
    @FunctionalInterface
    public interface GeneratedRecipe {
        void register(Consumer<FinishedRecipe> consumer);
    }
    
    public static class Marker {
    }
    
    public static class IT {
        public static TagKey<Item> aluminumIngot() {
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation("forge", "ingots/aluminum"));
        }
        public static TagKey<Item> steelIngot() {
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation("forge", "ingots/steel"));
        }
        public static TagKey<Item> copperIngot() {
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation("forge", "ingots/copper"));
        }
        public static TagKey<Item> zincIngot() {
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation("forge", "ingots/zinc"));
        }
        public static TagKey<Item> bauxiteStoneType() {
            return TFMGPaletteStoneTypes.BAUXITE.materialTag;
        }
        public static TagKey<Item> galenaStoneType() {
            return TFMGPaletteStoneTypes.GALENA.materialTag;
        }
        public static TagKey<Item> planks() {
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation("minecraft", "planks"));
        }
        public static TagKey<Item> string() {
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation("forge", "string"));
        }
        public static TagKey<Item> copperWire() {
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation("forge", "wires/copper"));
        }
        public static TagKey<Item> copperPlate() {
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation("forge", "plates/copper"));
        }
        public static TagKey<Item> leadIngot() {
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation("forge", "ingots/lead"));
        }
        public static TagKey<Item> nickelIngot() {
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation("forge", "ingots/nickel"));
        }
        public static TagKey<Item> brassIngot() {
            return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation("forge", "ingots/brass"));
        }
    }
    
    public static class I {
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
        public static ItemLike steelIngot() {
            return TFMGItems.STEEL_INGOT.get();
        }
        public static ItemLike steelBlock() {
            return TFMGBlocks.STEEL_BLOCK.get();
        }
        public static ItemLike blastingMixture() {
            return TFMGItems.BLASTING_MIXTURE.get();
        }
        public static ItemLike bitumen() {
            return TFMGItems.BITUMEN.get();
        }
        public static ItemLike cinderFlour() {
            return AllItems.CINDER_FLOUR.get();
        }
        public static ItemLike cinderflourBlock() {
            return TFMGBlocks.CINDERFLOUR_BLOCK.get();
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
        public static ItemLike copperSulfate() {
            return TFMGItems.COPPER_SULFATE.get();
        }
        public static ItemLike boneMeal() {
            return Items.BONE_MEAL;
        }
        public static ItemLike blueDye() {
            return Items.BLUE_DYE;
        }
        public static ItemLike cyanDye() {
            return Items.CYAN_DYE;
        }
        public static ItemLike crushedRawLead() {
            return AllItems.CRUSHED_LEAD.get();
        }
        public static ItemLike lignite() {
            return TFMGBlocks.LIGNITE.get();
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
        public static ItemLike nitrateDust() {
            return TFMGItems.NITRATE_DUST.get();
        }
        public static ItemLike sulfur() {
            return TFMGBlocks.SULFUR.get();
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
        public static ItemLike bottleOfBatteryAcid() {
            return TFMGItems.BOTTLE_OF_BATTERY_ACID.get();
        }
        public static ItemLike bottleOfConcrete() {
            return TFMGItems.BOTTLE_OF_CONCRETE.get();
        }
        public static ItemLike hardenedPlanks() {
            return TFMGBlocks.HARDENED_PLANKS.get();
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
        public static ItemLike heavyPlate() {
            return TFMGItems.HEAVY_PLATE.get();
        }
        //public static ItemLike charcoalDust() {
        //    return TFMGItems.CHARCOAL_DUST.get();
        //}
        public static ItemLike crushedRawIron() {
            return AllItems.CRUSHED_IRON.get();
        }
        public static ItemLike ironIngot() {
            return Items.IRON_INGOT;
        }
        public static ItemLike castIronIngot() {
            return TFMGItems.CAST_IRON_INGOT.get();
        }
        public static ItemLike clayBall() {
            return Items.CLAY_BALL;
        }
        public static ItemLike cement() {
            return TFMGBlocks.CEMENT.get();
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
        public static ItemLike slag() {
            return TFMGItems.SLAG.get();
        }
        public static ItemLike gunpowder() {
            return Items.GUNPOWDER;
        }
        public static ItemLike zincSulfate() {
            return TFMGItems.ZINC_SULFATE.get();
        }
        public static ItemLike syntheticLeather() {
            return TFMGItems.SYNTHETIC_LEATHER.get();
        }
        public static ItemLike engineBase() {
            return TFMGItems.ENGINE_BASE.get();
        }
        public static ItemLike unfinishedGasolineEngine() {
            return TFMGItems.UNFINISHED_GASOLINE_ENGINE.get();
        }
        public static ItemLike gasolineEngine() {
            return TFMGBlocks.GASOLINE_ENGINE.get();
        }
        public static ItemLike unfinishedLpgEngine() {
            return TFMGItems.UNFINISHED_LPG_ENGINE.get();
        }
        public static ItemLike lpgEngine() {
            return TFMGBlocks.LPG_ENGINE.get();
        }
        public static ItemLike engineChamber() {
            return TFMGItems.ENGINE_CHAMBER.get();
        }
        public static ItemLike screw() {
            return TFMGItems.SCREW.get();
        }
        public static ItemLike screwdriver() {
            return TFMGItems.SCREWDRIVER.get();
        }
        public static ItemLike unprocessedHeavyPlate() {
            return TFMGItems.UNPROCESSED_HEAVY_PLATE.get();
        }
        public static ItemLike steelMechanism() {
            return TFMGItems.STEEL_MECHANISM.get();
        }
        public static ItemLike unfinishedSteelMechanism() {
            return TFMGItems.UNFINISHED_STEEL_MECHANISM.get();
        }
        public static ItemLike aluminumIngot() {
            return TFMGItems.ALUMINUM_INGOT.get();
        }
        public static ItemLike industrialPipe() {
            return TFMGBlocks.INDUSTRIAL_PIPE.get();
        }
        public static ItemLike turbineBlade() {
            return TFMGItems.TURBINE_BLADE.get();
        }
        public static ItemLike turbineEngine() {
            return TFMGBlocks.TURBINE_ENGINE.get();
        }
        public static ItemLike unfinishedTurbineEngine() {
            return TFMGItems.UNFINISHED_TURBINE_ENGINE.get();
        }
        public static ItemLike magneticIngot() {
            return TFMGItems.MAGNETIC_INGOT.get();
        }
    }
    
    public static class F {
        //GASSES
        public static Fluid air() {
            return TFMGFluids.AIR.get();
        }
        public static Fluid heatedAir() {
            return TFMGFluids.HEATED_AIR.get();
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
            return TFMGFluids.BLAST_FURNACE_GAS.get();
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
            return TFMGFluids.LIQUID_PLASTIC.get();
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
        public static ItemLike heatedAirTank() {
            return TFMGRegistrate.getBucket("heated_air");
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
        public static ItemLike butaneTank() {
            return TFMGRegistrate.getBucket("butane");
        }
        public static ItemLike lpgTank() {
            return TFMGRegistrate.getBucket("lpg");
        }
        public static ItemLike neonTank() {
            return TFMGRegistrate.getBucket("neon");
        }
        public static ItemLike blastFurnaceGasTank() {
            return TFMGRegistrate.getBucket("blast_furnace_gas");
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
