package com.drmangotea.tfmg.datagen.recipes.values.create;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGPaletteStoneTypes;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.CrushingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.FalseCondition;

import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.*;

@SuppressWarnings("unused")
public class TFMGCrushingRecipeGen extends CrushingRecipeGen {

    GeneratedRecipe
            COPPER_SULFATE = create(TFMG.asResource("copper_sulfate"), b -> b
                    .require(TFMGRecipeProvider.I.copperSulfate())
                    .output(boneMeal(), 4)
                    .output(.25f, boneMeal(), 3)
                    .output(.5f, cyanDye(), 1)
                    .output(.75f, blueDye(), 1)
            ),
            LIGNITE = create(TFMG.asResource("lignite"), b -> b
                    .require(TFMGBlocks.LIGNITE)
                    .output(.75f,coal(), 1)
                    .output(.2f,coal(), 1)
            ),
            BAUXITE = create(TFMG.asResource("bauxite"), b -> b
                    .require(TFMGPaletteStoneTypes.BAUXITE.getBaseBlock().get())
                    .output(.75f, TFMGItems.BAUXITE_POWDER, 2)
                    .output(.2f,TFMGItems.BAUXITE_POWDER, 1)
            ),
            LIMESAND = create(TFMG.asResource("limestone"), b -> b
                    .require(TFMGRecipeProvider.I.limestone())
                    .output(limesand(), 1)
            ),
            SLAG = create(TFMG.asResource("slag_block"), b -> b
                    .require(TFMGBlocks.SLAG_BLOCK)
                    .output(slag(), 2)
                    .output(.3f,slag())
            ),
            COAL_COKE = create(TFMG.asResource("coal_coke"), b -> b
                    .require(TFMGRecipeProvider.I.coalCoke())
                    .output(coalCokeDust(), 1)
            ),
            SALTPETER = create(TFMG.asResource("dirt"), b -> b
                    .require(TFMGRecipeProvider.I.dirt())
                    .output(.05f, nitrateDust(), 1)
            ),
            GALENA = create(TFMG.asResource("galena"), b -> b
                    .require(TFMGPaletteStoneTypes.GALENA.getBaseBlock().get())
                    .output(.4f, crushedRawLead(), 1)
                    .output(.1f, TFMGItems.LEAD_NUGGET, 2)
            ),
            SULFUR = create(TFMG.asResource("sulfur"), b -> b
                    .require(TFMGBlocks.SULFUR)
                    .output(.2f, sulfurDust(), 1)
                    .output(.1f, sulfurDust(), 1)
            ),
            LITHIUM_ORE = create(TFMG.asResource("lithium_ore"), b -> b
                    .require(TFMGBlocks.LITHIUM_ORE)
                    .output(TFMGItems.CRUSHED_LITHIUM, 1)
                    .output(.25f, TFMGItems.CRUSHED_LITHIUM, 1)
                            .output(.75f, experienceNugget(), 1)
                    .output(.12f,Items.COBBLESTONE, 1)
            ),
            DEEPSLATE_LITHIUM_ORE = create(TFMG.asResource("deepslate_lithium_ore"), b -> b
                    .require(TFMGBlocks.DEEPSLATE_LITHIUM_ORE)
                    .output(TFMGItems.CRUSHED_LITHIUM, 2)
                    .output(.25f, TFMGItems.CRUSHED_LITHIUM, 1)
                            .output(.75f, experienceNugget(), 1)
                    .output(.12f, Items.COBBLED_DEEPSLATE, 1)
            ),
            RAW_LITHIUM = create(TFMG.asResource("raw_lithium"), b -> b
                    .require(TFMGItems.RAW_LITHIUM)
                    .output(TFMGItems.CRUSHED_LITHIUM, 1)
                    .output(.75f, experienceNugget(), 1)
            ),
            RAW_LITHIUM_BLOCK = create(TFMG.asResource("raw_lithium_block"), b -> b
                    .require(TFMGBlocks.RAW_LITHIUM_BLOCK)
                    .output(TFMGItems.CRUSHED_LITHIUM, 9)
                    .output(.75f, experienceNugget(), 9)
            ),
            LEAD_ORE = create(TFMG.asResource("lead_ore"), b -> b
                    .require(TFMGBlocks.LEAD_ORE)
                    .output(AllItems.CRUSHED_LEAD, 1)
                    .output(.25f, AllItems.CRUSHED_LEAD, 1)
                    .output(.75f, experienceNugget(), 1)
                    .output(.12f,Items.COBBLESTONE, 1)
            ),
            DEEPSLATE_LEAD_ORE = create(TFMG.asResource("deepslate_lead_ore"), b -> b
                    .require(TFMGBlocks.DEEPSLATE_LEAD_ORE)
                    .output(AllItems.CRUSHED_LEAD, 2)
                    .output(.25f, AllItems.CRUSHED_LEAD, 1)
                    .output(.75f, experienceNugget(), 1)
                    .output(.12f, Items.COBBLED_DEEPSLATE, 1)
            ),
            NICKEL_ORE = create(TFMG.asResource("nickel_ore"), b -> b
                    .require(TFMGBlocks.NICKEL_ORE)
                    .output(AllItems.CRUSHED_NICKEL, 1)
                    .output(.25f, AllItems.CRUSHED_NICKEL, 1)
                    .output(.75f, experienceNugget(), 1)
                    .output(.12f,Items.COBBLESTONE, 1)
            ),
            DEEPSLATE_NICKEL_ORE = create(TFMG.asResource("deepslate_nickel_ore"), b -> b
                    .require(TFMGBlocks.DEEPSLATE_NICKEL_ORE)
                    .output(AllItems.CRUSHED_NICKEL, 2)
                    .output(.25f, AllItems.CRUSHED_NICKEL, 1)
                    .output(.75f, experienceNugget(), 1)
                    .output(.12f, Items.COBBLED_DEEPSLATE, 1)
            )
    ;

    //Overriding recipes
    GeneratedRecipe
            C_LEAD_ORE = overrideOther(Create.asResource("lead_ore")),
            C_NICKEL_ORE = overrideOther(Create.asResource("nickel_ore"));

    private GeneratedRecipe overrideOther(ResourceLocation name) {
        return create(name, b -> b.withCondition(FalseCondition.INSTANCE));
    }

    public TFMGCrushingRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
        super(generator, registries, TFMG.MOD_ID);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.CRUSHING;
    }

}
