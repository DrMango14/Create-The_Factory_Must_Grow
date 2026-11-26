package com.drmangotea.tfmg.datagen.integration;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider;
import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.FalseCondition;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.foundry.MeltingRecipe;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class TFMGBigCannonsProvider {

    public static class Melting extends StandardProcessingRecipeGen<MeltingRecipe> {

        BaseRecipeProvider.GeneratedRecipe MELT_STEEL_BLOCK = this.create(TFMG.asResource("melt_steel_block"), (b) -> b.whenModLoaded("createbigcannons")
                .require(CommonMetal.STEEL.storageBlocks.items()).duration(1620)
                .requiresHeat(HeatCondition.HEATED)
                .output(TFMGFluids.MOLTEN_STEEL.get().getSource(), 810));
        BaseRecipeProvider.GeneratedRecipe MELT_STEEL_INGOT = this.create(TFMG.asResource("melt_steel_ingot"), (b) -> b.whenModLoaded("createbigcannons")
                .require(CommonMetal.STEEL.ingots).duration(180)
                .requiresHeat(HeatCondition.HEATED)
                .output(TFMGFluids.MOLTEN_STEEL.get().getSource(), 90));
        BaseRecipeProvider.GeneratedRecipe MELT_STEEL_NUGGET = this.create(TFMG.asResource("melt_steel_nugget"), (b) -> b.whenModLoaded("createbigcannons")
                .require(CommonMetal.STEEL.nuggets).duration(20)
                .requiresHeat(HeatCondition.HEATED)
                .output(TFMGFluids.MOLTEN_STEEL.get().getSource(), 10));

        BaseRecipeProvider.GeneratedRecipe MELT_PLASTIC_INGOT = this.create(TFMG.asResource("melt_plastic_ingot"), (b) -> b.whenModLoaded("createbigcannons")
                .require(TFMGTags.Items.INGOTS_PLASTIC.tag).duration(180)
                .requiresHeat(HeatCondition.HEATED)
                .output(TFMGFluids.MOLTEN_PLASTIC.get().getSource(), 90));
        BaseRecipeProvider.GeneratedRecipe MELT_SLAG = this.create(TFMG.asResource("melt_slag"), (b) -> b.whenModLoaded("createbigcannons")
                .require(TFMGBlocks.SLAG_BLOCK).duration(40)
                .requiresHeat(HeatCondition.HEATED)
                .output(TFMGFluids.MOLTEN_SLAG.get().getSource(), 20));
        BaseRecipeProvider.GeneratedRecipe MELT_SILICON = this.create(TFMG.asResource("melt_silicon"), (b) -> b.whenModLoaded("createbigcannons")
                .require(TFMGTags.Items.INGOTS_SILICON.tag).duration(180)
                .requiresHeat(HeatCondition.HEATED)
                .output(TFMGFluids.LIQUID_SILICON.get().getSource(), 90));

        //Overriding CBC recipes
        GeneratedRecipe
                CBC_MELT_STEEL_BLOCK = overrideOther(CreateBigCannons.resource("melt_steel_block")),
                CBC_MELT_STEEL_INGOT = overrideOther(CreateBigCannons.resource("melt_steel_ingot")),
                CBC_MELT_STEEL_NUGGET = overrideOther(CreateBigCannons.resource("melt_steel_nugget"));


        private GeneratedRecipe overrideOther(ResourceLocation name) {
            return create(name, b -> b.withCondition(FalseCondition.INSTANCE));
        }

        public Melting(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries, TFMG.MOD_ID);
        }

        @Override
        protected IRecipeTypeInfo getRecipeType() {
            return CBCRecipeTypes.MELTING;
        }
    }
}
