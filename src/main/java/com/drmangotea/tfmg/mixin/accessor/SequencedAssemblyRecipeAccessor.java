package com.drmangotea.tfmg.mixin.accessor;

import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SequencedAssemblyRecipe.class)
public interface SequencedAssemblyRecipeAccessor {

    @Accessor("ingredient")
    void tfmg$setIngredient(Ingredient ingredient);

    @Accessor("transitionalItem")
    void tfmg$setTransitionalItem(ProcessingOutput transitionalItem);

    @Accessor("loops")
    void tfmg$setLoops(int loops);
}
