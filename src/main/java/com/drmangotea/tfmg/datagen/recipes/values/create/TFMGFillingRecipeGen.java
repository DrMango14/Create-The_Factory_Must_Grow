package com.drmangotea.tfmg.datagen.recipes.values.create;

import com.drmangotea.tfmg.content.items.weapons.fire_extinguisher.FireExtinguisherItem;
import com.drmangotea.tfmg.datagen.recipes.TFMGProcessingRecipeGen;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.AllRecipeTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.air;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.airTank;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.blastFurnaceGas;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.carbonDioxide;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.carbonDioxideTank;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.ethylene;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.ethyleneTank;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.furnaceGasTank;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.hotAir;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.hotAirTank;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.hydrogen;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.hydrogenTank;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.lpg;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.lpgTank;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.neon;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.neonTank;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.propylene;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.propyleneTank;


public class TFMGFillingRecipeGen extends TFMGProcessingRecipeGen {

    GeneratedRecipe

            HARDENED_PLANKS = create("hardened_planks", b -> b
            .require(ItemTags.PLANKS)
            .require(TFMGFluids.CREOSOTE.getSource(), 250)
            .output(TFMGBlocks.HARDENED_PLANKS)),

    NAPALM_POTATO = create("napalm_potato", b -> b
            .require(Items.POTATO)
            .require(TFMGFluids.NAPALM.getSource(), 500)
            .output(TFMGItems.NAPALM_POTATO)),


    //GAS TANKS

    LPG_TANK = create("lpg_tank", b -> b
            .require(Items.BUCKET)
            .require(lpg(), 1000)
            .output(lpgTank())
    ),
            //BUTANE_TANK = create("butane_tank", b -> b
            //        .require(butane(), 1000)
            //        .output(butaneTank())
            //),
            //PROPANE_TANK = create("propane_tank", b -> b
            //        .require(propane(), 1000)
            //        .output(propaneTank())
            //),
            HYDROGEN_TANK = create("hydrogen_tank", b -> b
                    .require(Items.BUCKET)
                    .require(hydrogen(), 1000)
                    .output(hydrogenTank())
            ),
            FURNACE_GAS_TANK = create("furnace_gas_tank", b -> b
                    .require(Items.BUCKET)
                    .require(blastFurnaceGas(), 1000)
                    .output(furnaceGasTank())
            ),
            ETHYLENE_TANK = create("ethylene_tank", b -> b
                    .require(Items.BUCKET)
                    .require(ethylene(), 1000)
                    .output(ethyleneTank())
            ),
            PROPYLENE_TANK = create("propylene_tank", b -> b
                    .require(Items.BUCKET)
                    .require(propylene(), 1000)
                    .output(propyleneTank())
            ),
            NEON_TANK = create("neon_tank", b -> b
                    .require(Items.BUCKET)
                    .require(neon(), 1000)
                    .output(neonTank())
            ),
            CARBON_DIOXIDE_TANK = create("carbon_dioxide_tank", b -> b
                    .require(Items.BUCKET)
                    .require(carbonDioxide(), 1000)
                    .output(carbonDioxideTank())
            ),
            AIR_TANK = create("air_tank", b -> b
                    .require(Items.BUCKET)
                    .require(air(), 1000)
                    .output(airTank())
            ),
            HOT_AIR_TANK = create("hot_air_tank", b -> b
                    .require(Items.BUCKET)
                    .require(hotAir(), 1000)
                    .output(hotAirTank())
            ),

            FILLED_FIRE_EXTINGUISHER = create("filled_fire_extinguisher", b -> b
                    .require(TFMGItems.FIRE_EXTINGUISHER)
                    .require(carbonDioxide(), 1000)
                    .output(createFilledExtinguisherStack())
            );

    // Helper method to create the filled extinguisher ItemStack
    private static ItemStack createFilledExtinguisherStack() {
        ItemStack stack = new ItemStack(TFMGItems.FIRE_EXTINGUISHER.get());
        stack.getOrCreateTag().putInt("fill_level", FireExtinguisherItem.DRY_ICE_CAPACITY);
        return stack;
    }

    public TFMGFillingRecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.FILLING;
    }

}
