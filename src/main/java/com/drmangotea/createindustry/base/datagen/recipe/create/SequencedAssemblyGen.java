package com.drmangotea.createindustry.base.datagen.recipe.create;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGRecipeProvider;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.ItemStack;

import java.util.function.UnaryOperator;

public class SequencedAssemblyGen extends TFMGRecipeProvider {
    
    GeneratedRecipe gasolineEngine = create("gasoline_engine", (b) -> {
        return b.require(I.engineBase()).transitionTo(I.unfinishedGasolineEngine()).addOutput(new ItemStack(I.gasolineEngine().asItem(), 2), 120.0F).loops(8).addStep(FillingRecipe::new, (rb) -> {
            return rb.require(F.lubricationOil(), 1000);
        }).addStep(DeployerApplicationRecipe::new, (rb) -> {
            return rb.require(I.engineChamber());
        }).addStep(DeployerApplicationRecipe::new, (rb) -> {
            return rb.require(I.screw());
        }).addStep(DeployerApplicationRecipe::new, (rb) -> {
            return rb.require(I.screwdriver());
        });
    });
    
    GeneratedRecipe heavyPlate = create("heavy_plate", (b) -> {
        return b.require(IT.steelIngot()).transitionTo(I.unprocessedHeavyPlate()).addOutput(I.heavyPlate(), 120.0F).loops(1).addStep(PressingRecipe::new, (rb) -> {
            return rb;
        }).addStep(PressingRecipe::new, (rb) -> {
            return rb;
        }).addStep(PressingRecipe::new, (rb) -> {
            return rb;
        });
    });
    
    GeneratedRecipe lpgEngine = create("lpg_engine", (b) -> {
        return b.require(I.engineBase()).transitionTo(I.unfinishedLpgEngine()).addOutput(new ItemStack(I.lpgEngine().asItem(), 2), 120.0F).loops(8).addStep(DeployerApplicationRecipe::new, (rb) -> {
            return rb.require(I.engineChamber());
        }).addStep(DeployerApplicationRecipe::new, (rb) -> {
            return rb.require(I.screw());
        }).addStep(DeployerApplicationRecipe::new, (rb) -> {
            return rb.require(I.screwdriver());
        }).addStep(FillingRecipe::new, (rb) -> {
            return rb.require(F.lubricationOil(), 1000);
        });
    });
    
    GeneratedRecipe steelMechanism = create("steel_mechanism", (b) -> {
        return b.require(IT.steelIngot()).transitionTo(I.unfinishedSteelMechanism()).addOutput(I.steelMechanism(), 120.0F).addOutput(I.heavyPlate(), 0.8F).addOutput(I.steelIngot(), 0.8F).addOutput(I.aluminumIngot(), 0.5F).addOutput(I.industrialPipe(), 0.3F).loops(1)
        .addStep(DeployerApplicationRecipe::new, (rb) -> {
            return rb.require(IT.steelIngot());
        }).addStep(DeployerApplicationRecipe::new, (rb) -> {
            return rb.require(IT.aluminumIngot());
        }).addStep(DeployerApplicationRecipe::new, (rb) -> {
            return rb.require(I.screw());
        }).addStep(DeployerApplicationRecipe::new, (rb) -> {
            return rb.require(I.screwdriver());
        });
    });
    
    GeneratedRecipe turbineEngine = create("turbine_engine", (b) -> {
        return b.require(I.engineBase()).transitionTo(I.unfinishedTurbineEngine()).addOutput(new ItemStack(I.turbineEngine().asItem(), 2), 120.0F).loops(6).addStep(DeployerApplicationRecipe::new, (rb) -> {
            return rb.require(I.turbineBlade());
        }).addStep(DeployerApplicationRecipe::new, (rb) -> {
            return rb.require(I.screw());
        }).addStep(DeployerApplicationRecipe::new, (rb) -> {
            return rb.require(I.screwdriver());
        }).addStep(FillingRecipe::new, (rb) -> {
            return rb.require(F.lubricationOil(), 1000);
        }).addStep(DeployerApplicationRecipe::new, (rb) -> {
            return rb.require(I.steelMechanism());
        });
    });
    
    public SequencedAssemblyGen(DataGenerator generator) {
        super(generator);
    }
    
    protected GeneratedRecipe create(String name, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
        GeneratedRecipe generatedRecipe = (c) -> {
            transform.apply(new SequencedAssemblyRecipeBuilder(CreateTFMG.asResource(name))).build(c);
        };
        this.all.add(generatedRecipe);
        return generatedRecipe;
    }
    
    public String getName() {
        return "TFMG's Sequenced Assembly Recipes";
    }
}
