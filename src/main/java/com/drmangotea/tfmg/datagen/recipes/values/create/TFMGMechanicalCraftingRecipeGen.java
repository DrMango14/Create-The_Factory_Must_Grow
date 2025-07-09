package com.drmangotea.tfmg.datagen.recipes.values.create;

import java.util.function.UnaryOperator;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.google.common.base.Supplier;
import com.simibubi.create.Create;

import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeBuilder;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.*;

public class TFMGMechanicalCraftingRecipeGen extends TFMGRecipeProvider {

    GeneratedRecipe

            ENGINE_CONTROLLER = create(TFMGBlocks.ENGINE_CONTROLLER::get)
            .recipe(b -> b
                    .key('R',  rubber())
                    .key('S', shaft())
                    .key('V', TFMGBlocks.VOLTMETER)
                    .key('W', copperWire())
                    .key('C', heavyMachineryCasing())
                    .key('Z', circuitBoard())
                    .key('M', steelMechanism())
                    .patternLine("RRR")
                    .patternLine("VSV")
                    .patternLine("WCW")
                    .patternLine("ZMZ")
                    .disallowMirrored()),

    ROTOR = create(TFMGBlocks.ROTOR::get)
            .recipe(b -> b
                    .key('C', TFMGItems.ELECTROMAGNETIC_COIL)
                    .key('A', aluminumIngot())
                    .key('S', steelBlock())
                    .patternLine(" CCC ")
                    .patternLine("CAAAC")
                    .patternLine("CASAC")
                    .patternLine("CAAAC")
                    .patternLine(" CCC ")
                    .disallowMirrored()),

    STATOR = create(TFMGBlocks.STATOR::get).returns(3)
            .recipe(b -> b
                    .key('C', TFMGItems.ELECTROMAGNETIC_COIL)
                    .key('A', aluminumSheet())
                    .key('W', copperWire())
                    .key('M', magnet())
                    .key('S', steelBlock())
                    .patternLine("CM  ")
                    .patternLine("ACM ")
                    .patternLine("ASCM")
                    .patternLine("WAAC")
                    .disallowMirrored()),

    SIMPLE_LARGE_ENGINE = create(TFMGBlocks.SIMPLE_LARGE_ENGINE::get)
            .recipe(b -> b
                    .key('C', castIronIngot())
                    .key('O', steelSheet())
                    .key('M', precisionMechanism())
                    .patternLine("CCC")
                    .patternLine("OCO")
                    .patternLine("OMO")
                    .patternLine("OCO")
                    .disallowMirrored()),

    QUAD_POTATO_CANNON = create(TFMGItems.QUAD_POTATO_CANNON::get)
            .recipe(b -> b
                    .key('O', steelIngot())
                    .key('C', castIronIngot())
                    .key('P', steelPipe())
                    .key('M', steelMechanism())
                    .patternLine("PMPC")
                    .patternLine("PMPC")
                    .patternLine(" O  ")
                    .disallowMirrored()),

    FLAMETHROWER = create(TFMGItems.FLAMETHROWER::get)
            .recipe(b -> b
                    .key('O', steelIngot())
                    .key('C', circuitBoard())
                    .key('T', steelTank())
                    .key('P', steelPipe())
                    .key('S', TFMGItems.SPARK_PLUG)
                    .key('M', steelMechanism())
                    .key('B', TFMGBlocks.ALUMINUM_BARS)
                    .key('W', copperWire())
                    .patternLine("BWC ")
                    .patternLine("PPTM")
                    .patternLine("S O ")
                    .disallowMirrored()),

    ADVANCED_POTATO_CANNON = create(TFMGItems.ADVANCED_POTATO_CANNON::get)
            .recipe(b -> b
                    .key('O', rebar())
                    .key('C', circuitBoard())
                    .key('T', steelTank())
                    .key('P', steelPipe())
                    .key('M', steelMechanism())
                    .patternLine("PPPT")
                    .patternLine(" MCO")
                    .disallowMirrored()),

    LARGE_ENGINE = create(TFMGBlocks.LARGE_ENGINE::get)
            .recipe((b) -> b
                    .key('A', aluminumSheet())
                    .key('B', aluminumIngot())
                    .key('H', I.heavyPlate())
                    .key('S', I.steelMechanism())
                    .key('C', I.heavyMachineryCasing())
                    .key('O', steelIngot())
                    .key('T', TFMGBlocks.STEEL_FLUID_TANK.get())
                    .patternLine(" O ")
                    .patternLine(" B ")
                    .patternLine("AOA")
                    .patternLine("SCS")
                    .patternLine("STS")
                    .patternLine("HHH")),

    SPARK_PLUG = create(TFMGItems.SPARK_PLUG::get)
            .recipe(b -> b
                    .key('F', Items.FLINT)
                    .key('A', aluminumIngot())
                    .patternLine("F")
                    .patternLine("A")
                    .disallowMirrored());



    public TFMGMechanicalCraftingRecipeGen(PackOutput p_i48262_1_) {
        super(p_i48262_1_);
    }

    GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder(result);
    }

    class GeneratedRecipeBuilder {

        private String suffix;
        private Supplier<ItemLike> result;
        private int amount;

        public GeneratedRecipeBuilder(Supplier<ItemLike> result) {
            this.suffix = "";
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
            return register(consumer -> {
                MechanicalCraftingRecipeBuilder b =
                        builder.apply(MechanicalCraftingRecipeBuilder.shapedRecipe(result.get(), amount));
                ResourceLocation location = TFMG.asResource("mechanical_crafting/" + CatnipServices.REGISTRIES.getKeyOrThrow(result.get()
                                .asItem())
                        .getPath() + suffix);
                b.build(consumer, location);
            });
        }
    }


}