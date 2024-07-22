package com.drmangotea.createindustry.base.datagen.recipe.create;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.TFMGPipes;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGRecipeProvider;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.google.common.base.Supplier;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.data.recipe.MechanicalCraftingRecipeBuilder;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.UnaryOperator;

public class MechanicalCraftingGen extends TFMGRecipeProvider {
    
    GeneratedRecipe advancedPotatoCannon = create(TFMGItems.ADVANCED_POTATO_CANNON::get).recipe((b) -> {
        return b.key('I', TFMGPipes.STEEL_PIPE.get()).key('P', I.steelMechanism()).key('H', I.heavyPlate()).key('A', I.plasticSheet()).patternLine("IIPPH").patternLine(" AH H");
    });
    
    GeneratedRecipe dieselEngine = create(TFMGBlocks.DIESEL_ENGINE::get).recipe((b) -> {
        return b.key('A', IT.aluminumIngot()).key('H', I.heavyPlate()).key('S', I.steelMechanism()).key('C', I.heavyMachineryCasing()).key('O', IT.steelIngot()).key('T', TFMGBlocks.STEEL_FLUID_TANK.get())
                .patternLine(" O ").patternLine(" A ").patternLine("AOA").patternLine("SCS").patternLine("STS").patternLine("HHH");
    });
    
    GeneratedRecipe engineBase = create(TFMGItems.ENGINE_BASE::get).recipe((b) -> {
        return b.key('A', AllBlocks.SHAFT.get()).key('H', I.heavyPlate()).key('C', I.heavyMachineryCasing())
                .patternLine("HAH").patternLine("HCH");
    });
    
    GeneratedRecipe engineChamber = create(TFMGItems.ENGINE_CHAMBER::get).recipe((b) -> {
        return b.key('A', IT.aluminumIngot()).key('S', TFMGItems.SPARK_PLUG.get()).key('P', I.steelMechanism())
                .patternLine("S").patternLine("A").patternLine("P");
    });
    
    GeneratedRecipe flamethrower = create(TFMGItems.FLAMETHROWER::get).recipe((b) -> {
        return b.key('P', I.steelMechanism()).key('A', IT.aluminumIngot()).key('I', TFMGPipes.STEEL_PIPE.get()).key('H', I.heavyPlate()).key('T', TFMGBlocks.STEEL_FLUID_TANK.get())
                .patternLine("IIPPH").patternLine(" ATAH");
    });
    
    GeneratedRecipe generator = create(TFMGBlocks.GENERATOR::get).recipe((b) -> {
        return b.key('M', TFMGItems.MAGNETIC_INGOT.get()).key('C', TFMGBlocks.ELECTRIC_CASING.get()).key('E', I.steelMechanism()).key('K', TFMGItems.COPPER_CABLE.get())
                .patternLine("EME").patternLine("MCM").patternLine("KMK");
    });
    
    GeneratedRecipe largeRadialEngine = create(TFMGBlocks.LARGE_RADIAL_ENGINE::get).recipe((b) -> {
        return b.key('L', F.lubricationOilBucket()).key('C', I.heavyMachineryCasing()).key('M', I.engineChamber()).key('S', AllBlocks.SHAFT.get()).key('P', TFMGPipes.STEEL_PIPE.get()).key('E', TFMGBlocks.EXHAUST.get()).key('H', I.heavyPlate()).key('N', I.steelMechanism())
                .patternLine(" MHM ").patternLine("MNLNM").patternLine("EPCPE").patternLine("MHSHM").patternLine(" MHM ");
    });
    
    GeneratedRecipe lithiumBlade = create(TFMGItems.LITHIUM_BLADE::get).recipe((b) -> {
        return b.key('T', TFMGBlocks.LITHIUM_TORCH.get()).key('M', I.steelMechanism()).key('S', TFMGItems.STEEL_SWORD.get()).key('K', TFMGItems.CAPACITOR.get()).key('R', TFMGItems.RESISTOR.get()).key('P', I.plasticSheet()).key('L', TFMGItems.SYNTHETIC_LEATHER.get()).key('C', IT.copperWire())
                .patternLine(" T ").patternLine("CSC").patternLine("CMK").patternLine("PLR");
    });
    
    GeneratedRecipe pumpjackBase = create(TFMGBlocks.PUMPJACK_BASE::get).recipe((b) -> {
        return b.key('A', IT.string()).key('H', I.heavyPlate()).key('S', I.steelMechanism()).key('C', I.heavyMachineryCasing()).key('I', I.industrialPipe())
                .patternLine("HAH").patternLine("SCS").patternLine("HIH");
    });
    
    GeneratedRecipe pumpjackCrank = create(TFMGBlocks.PUMPJACK_CRANK::get).recipe((b) -> {
        return b.key('A', IT.string()).key('H', I.heavyPlate()).key('S', TFMGItems.REBAR.get()).key('C', I.heavyMachineryCasing())
                .patternLine("HAH").patternLine("SCS");
    });
    
    GeneratedRecipe quadPotatoCannon = create(TFMGItems.QUAD_POTATO_CANNON::get).recipe((b) -> {
        return b.key('P', I.steelMechanism()).key('A', TFMGItems.REBAR.get()).key('S', I.industrialPipe()).key('I', TFMGPipes.STEEL_PIPE.get()).key('H', I.heavyPlate())
                .patternLine("HIIIS").patternLine("HPPIS").patternLine("   A ");
    });
    
    GeneratedRecipe radialEngine = create(TFMGBlocks.RADIAL_ENGINE::get).recipe((b) -> {
        return b.key('L', F.lubricationOilBucket()).key('C', I.heavyMachineryCasing()).key('M', I.engineChamber()).key('S', AllBlocks.SHAFT.get()).key('P', TFMGPipes.STEEL_PIPE.get()).key('E', TFMGBlocks.EXHAUST.get())
                .patternLine("  M  ").patternLine(" MLM ").patternLine("MECPM").patternLine(" MSM ").patternLine("  M  ");
    });
    
    GeneratedRecipe rotor = create(TFMGBlocks.ROTOR::get).recipe((b) -> {
        return b.key('A', IT.aluminumIngot()).key('C', TFMGBlocks.COPPER_COIL.get()).key('S', AllBlocks.SHAFT.get()).key('R', TFMGItems.REBAR.get())
                .patternLine(" CCC ").patternLine("CRARC").patternLine("CASAC").patternLine("CRARC").patternLine(" CCC ");
    });
    
    GeneratedRecipe sparkPlug = create(TFMGItems.SPARK_PLUG::get).recipe((b) -> {
        return b.key('F', Items.FLINT).key('A', IT.aluminumIngot())
                .patternLine("F").patternLine("A");
    });
    
    GeneratedRecipe stator = create(TFMGBlocks.STATOR::get).recipe((b) -> {
        return b.key('C', TFMGItems.COPPER_CABLE.get()).key('M', TFMGItems.MAGNETIC_INGOT.get()).key('P', I.steelMechanism()).key('R', IT.steelIngot()).key('A', TFMGBlocks.ELECTRIC_CASING.get())
                .patternLine("MMM").patternLine("CPC").patternLine("RAR");
    });
    
    GeneratedRecipe steelDistillationController = create(TFMGBlocks.STEEL_DISTILLATION_CONTROLLER::get).recipe((b) -> {
        return b.key('P', I.steelMechanism()).key('I', I.industrialPipe()).key('H', I.heavyPlate()).key('C', I.heavyMachineryCasing()).key('D', AllBlocks.DISPLAY_BOARD.get()).key('E', AllItems.ELECTRON_TUBE.get())
                .patternLine("HIH").patternLine("PDP").patternLine("ECE");
    });
    
    GeneratedRecipe steelDistillationOutput = create(TFMGBlocks.STEEL_DISTILLATION_OUTPUT::get).recipe((b) -> {
        return b.key('T', TFMGBlocks.STEEL_FLUID_TANK.get()).key('H', I.heavyPlate()).key('P', TFMGPipes.STEEL_PIPE.get())
                .patternLine("HPH").patternLine("PTP").patternLine("HPH");
    });
    
    GeneratedRecipe surfaceScanner = create(TFMGBlocks.SURFACE_SCANNER::get).recipe((b) -> {
        return b.key('I', I.heavyPlate()).key('C', I.heavyMachineryCasing()).key('H', Items.COMPASS).key('S', AllBlocks.SHAFT.get()).key('E', I.steelMechanism()).key('K', IT.copperPlate())
                .patternLine("IHI").patternLine("SCK").patternLine("EEK");
    });
    
    public MechanicalCraftingGen(DataGenerator generator) {
        super(generator);
    }
    
    GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder(result);
    }
    
    public String getName() {
        return "TFMG's Mechanical Crafting Recipes";
    }
    
    class GeneratedRecipeBuilder {
        private String suffix = "";
        private Supplier<ItemLike> result;
        private int amount;
        
        public GeneratedRecipeBuilder(Supplier<ItemLike> result) {
            this.result = result;
            this.amount = 1;
        }
        
        GeneratedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }
        
        GeneratedRecipeBuilder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }
        
        GeneratedRecipe recipe(UnaryOperator<MechanicalCraftingRecipeBuilder> builder) {
            return MechanicalCraftingGen.this.register((consumer) -> {
                MechanicalCraftingRecipeBuilder b = builder.apply(MechanicalCraftingRecipeBuilder.shapedRecipe(this.result.get(), this.amount));
                String var10000 = RegisteredObjects.getKeyOrThrow(this.result.get().asItem()).getPath();
                ResourceLocation location = CreateTFMG.asResource("mechanical_crafting/" + var10000 + this.suffix);
                b.build(consumer, location);
            });
        }
    }
}
