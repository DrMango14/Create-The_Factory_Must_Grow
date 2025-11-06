package com.drmangotea.tfmg.mixin;

import com.drmangotea.tfmg.datagen.recipes.values.TFMGStandardRecipeGen;
import com.drmangotea.tfmg.datagen.recipes.values.create.TFMGMechanicalCraftingRecipeGen;
import com.drmangotea.tfmg.datagen.recipes.values.create.TFMGSequencedAssemblyRecipeGen;
import com.drmangotea.tfmg.datagen.recipes.values.tfmg.IndustrialBlastingRecipeGen;
import com.drmangotea.tfmg.datagen.recipes.values.tfmg.VatRecipeGen;
import net.minecraft.data.recipes.RecipeProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeProvider.class)
public class RecipeProviderMixin {


    @Inject(at = @At("HEAD"), method = "getName",cancellable = true , remap = true)
    public final void getName(CallbackInfoReturnable<String> cir) {
        if((Object)this instanceof TFMGMechanicalCraftingRecipeGen)
            cir.setReturnValue("TFMG'S Mechanical Crafting Recipes");
        if((Object)this instanceof IndustrialBlastingRecipeGen)
            cir.setReturnValue("TFMG'S Industrial Blasting Recipes");
        if((Object)this instanceof VatRecipeGen)
            cir.setReturnValue("TFMG'S Vat Recipes");
        if((Object)this instanceof TFMGStandardRecipeGen)
            cir.setReturnValue("TFMG'S Standard Recipes");
        if((Object)this instanceof TFMGSequencedAssemblyRecipeGen)
            cir.setReturnValue("TFMG'S Sequenced Assembly Recipes");


    }

}
