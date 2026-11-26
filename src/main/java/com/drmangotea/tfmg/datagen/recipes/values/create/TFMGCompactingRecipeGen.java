package com.drmangotea.tfmg.datagen.recipes.values.create;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.FalseCondition;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.concurrent.CompletableFuture;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.*;

@SuppressWarnings("unused")
public class TFMGCompactingRecipeGen extends CompactingRecipeGen {

    GeneratedRecipe
            BITUMEN = create(TFMG.asResource("bitumen"), b -> b
            .require(SizedFluidIngredient.of(TFMGTags.Fluids.HEAVY_OIL.tag,1000))
            .output(bitumen(), 1)
            .requiresHeat(HeatCondition.HEATED)
            ),
            CINDERFLOURBLOCK = create(TFMG.asResource("cinderflourblock"), b -> b
                    .require(cinderFlour())
                    .require(cinderFlour())
                    .require(cinderFlour())
                    .require(cinderFlour())
                    .output(TFMGItems.CINDERFLOURBLOCK)
            ),
            CAST_IRON = create(TFMG.asResource("cast_iron"), b -> b
                    .require(Tags.Items.INGOTS_IRON)
                    .require(coal())
                    .output(TFMGItems.CAST_IRON_INGOT, 1)
                    .requiresHeat(HeatCondition.HEATED)
            );

    //Overriding CBC recipes
    GeneratedRecipe
            IRON_TO_CAST_IRON_INGOT = overrideOther(cbcLoc("iron_to_cast_iron_ingot")),
            IRON_TO_CAST_IRON_BLOCK = overrideOther(cbcLoc("iron_to_cast_iron_block")),
            FORGE_CAST_IRON_INGOT = overrideOther(cbcLoc("forge_cast_iron_ingot")),
            FORGE_CAST_IRON_NUGGET = overrideOther(cbcLoc("forge_cast_iron_nugget")),
            FORGE_CAST_IRON_BLOCK = overrideOther(cbcLoc("forge_cast_iron_block")),
            FORGE_BRONZE_INGOT = overrideOther(cbcLoc("forge_bronze_ingot")),
            FORGE_BRONZE_NUGGET = overrideOther(cbcLoc("forge_bronze_nugget")),
            FORGE_BRONZE_BLOCK = overrideOther(cbcLoc("forge_bronze_block")),
            FORGE_STEEL_INGOT = overrideOther(cbcLoc("forge_steel_ingot")),
            FORGE_STEEL_NUGGET = overrideOther(cbcLoc("forge_steel_nugget")),
            FORGE_STEEL_BLOCK = overrideOther(cbcLoc("forge_steel_block")),
            FORGE_NETHERSTEEL_INGOT = overrideOther(cbcLoc("forge_nethersteel_ingot")),
            FORGE_NETHERSTEEL_NUGGET = overrideOther(cbcLoc("forge_nethersteel_nugget")),
            FORGE_NETHERSTEEL_BLOCK = overrideOther(cbcLoc("forge_nethersteel_block"));


    private GeneratedRecipe overrideOther(ResourceLocation name) {
        return create(name, b -> b.withCondition(FalseCondition.INSTANCE));
    }

    public TFMGCompactingRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
        super(generator, registries, TFMG.MOD_ID);
    }

    private ResourceLocation cbcLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath("createbigcannons", path);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.COMPACTING;
    }

}
