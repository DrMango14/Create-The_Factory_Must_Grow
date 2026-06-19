package com.drmangotea.tfmg.datagen.recipes.values.create;


import com.drmangotea.tfmg.TFMG;

import com.drmangotea.tfmg.recipes.WindingRecipe;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.api.data.recipe.SequencedAssemblyRecipeGen;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.lubricationOil;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.*;


public class TFMGSequencedAssemblyRecipeGen extends SequencedAssemblyRecipeGen {

    GeneratedRecipe POTENTIOMETER = create("potentiometer", b -> b.require(TFMGBlocks.HEAVY_MACHINERY_CASING.get())
            .transitionTo(TFMGItems.UNFINISHED_POTENTIOMETER.get())
            .addOutput(TFMGBlocks.POTENTIOMETER.get(), 120)
            .addOutput(TFMGBlocks.STEEL_CASING.get(), 8)
            .addOutput(TFMGBlocks.STEEL_COGWHEEL.get(), 8)
            .addOutput(TFMGBlocks.ELECTRIC_POST.get(), 8)
            .loops(3)
            .addStep(WindingRecipe::new, rb -> rb.require(TFMGItems.CONSTANTAN_SPOOL.get()).duration(100))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGBlocks.STEEL_COGWHEEL))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(copperWire()))
            .addStep(FillingRecipe::new, rb -> rb.require(SizedFluidIngredient.of(lubricationOil(), 50)))),

    GENERATOR = create("generator", b -> b.require(shaft())
            .transitionTo(TFMGItems.UNFINISHED_GENERATOR.get())
            .addOutput(TFMGBlocks.GENERATOR.get(), 120)
            .addOutput(TFMGBlocks.STEEL_CASING.get(), 8)
            .addOutput(TFMGBlocks.STEEL_COGWHEEL.get(), 8)
            .addOutput(TFMGItems.CAPACITOR.get(), 8)
            .loops(3)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGItems.CAPACITOR))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(steelSheet()))
            .addStep(WindingRecipe::new, rb -> rb.require(TFMGItems.COPPER_SPOOL.get()).duration(75))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(magnet()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(steelMechanism()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGItems.SCREWDRIVER))
    ),

    CIRCUIT_BOARD = create("unfinished_circuit_board", b -> b.require(TFMGItems.ETCHED_CIRCUIT_BOARD)
            .transitionTo(TFMGItems.UNFINISHED_CIRCUIT_BOARD.get())
            .addOutput(TFMGItems.CIRCUIT_BOARD.get(), 1)
            .loops(4)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGItems.CAPACITOR))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGBlocks.RESISTOR))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGItems.TRANSISTOR))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGBlocks.RESISTOR))

    ),


    HEAVY_PLATE = create("heavy_plate", b -> b.require(steelIngot())
            .transitionTo(TFMGItems.UNPROCESSED_HEAVY_PLATE.get())
            .addOutput(TFMGItems.HEAVY_PLATE.get(), 1)
            .loops(1)
            .addStep(PressingRecipe::new, rb -> rb)
            .addStep(PressingRecipe::new, rb -> rb)
            .addStep(PressingRecipe::new, rb -> rb)

    ),

    STEEL_MECHANISM = create("steel_mechanism", b -> b.require(TFMGItems.HEAVY_PLATE)
            .transitionTo(TFMGItems.UNFINISHED_STEEL_MECHANISM.get())
            .addOutput(TFMGItems.STEEL_MECHANISM.get(), 120)
            .addOutput(Items.COMPASS, 4)
            .addOutput(TFMGItems.STEEL_INGOT, 4)
            .loops(2)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGBlocks.STEEL_COGWHEEL))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGItems.NICKEL_SHEET))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGBlocks.LARGE_STEEL_COGWHEEL))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGItems.LEAD_SHEET))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGItems.SCREW))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGItems.SCREWDRIVER))

    ),

    MOTOR = create("motor", b -> b.require(shaft())
            .transitionTo(TFMGItems.UNFINISHED_ELECTRIC_MOTOR.get())
            .addOutput(TFMGBlocks.ELECTRIC_MOTOR.get(), 120)
            .addOutput(TFMGBlocks.STEEL_CASING.get(), 4)
            .addOutput(TFMGItems.NICKEL_SHEET.get(), 4)
            .loops(3)
            .addStep(WindingRecipe::new, rb -> rb.require(TFMGItems.COPPER_SPOOL.get()).duration(75))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(magnet()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(steelMechanism()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGItems.SCREWDRIVER))
    ),

    HEAVY_MOTOR = create("heavy_motor", b -> b.require(TFMGBlocks.ELECTRIC_MOTOR.get())
                    .transitionTo(TFMGItems.UNFINISHED_HEAVY_ELECTRIC_MOTOR.get())
                    .addOutput(TFMGBlocks.HEAVY_ELECTRIC_MOTOR.get(), 120)
                    .addOutput(TFMGBlocks.STEEL_CASING.get(), 4)
                    .addOutput(TFMGItems.NICKEL_SHEET.get(), 4)
                    .loops(3)
                    .addStep(WindingRecipe::new, rb -> rb.require(TFMGItems.COPPER_SPOOL.get()).duration(75))
                    .addStep(DeployerApplicationRecipe::new, rb -> rb.require(magnet()))
                    .addStep(DeployerApplicationRecipe::new, rb -> rb.require(steelMechanism()))
                    .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGItems.SCREWDRIVER))
            ),

    TRANSISTOR_PLASTIC = create("transistor", b -> b.require(plasticSheet())
            .transitionTo(TFMGItems.UNFINISHED_TRANSISTOR.get())
            .addOutput(new ItemStack(TFMGItems.TRANSISTOR.get(), 4), 120)
            .addOutput(TFMGItems.SILICON_INGOT.get(), 8)
            .addOutput(TFMGItems.P_SEMICONDUCTOR.get(), 8)
            .addOutput(TFMGItems.N_SEMICONDUCTOR.get(), 8)
            .loops(1)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(copperWire()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGItems.N_SEMICONDUCTOR))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGItems.P_SEMICONDUCTOR))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(TFMGItems.N_SEMICONDUCTOR))),
    CAPACITOR = create("capacitor", b -> b.require(steelSheet())
            .transitionTo(TFMGItems.UNFINISHED_CAPACITOR.get())
            .addOutput(new ItemStack(TFMGItems.CAPACITOR.get(), 4), 1)
            .loops(1)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(copperSheet()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.PAPER))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(copperSheet())));

    public TFMGSequencedAssemblyRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
        super(generator, registries,TFMG.MOD_ID);
    }




}
