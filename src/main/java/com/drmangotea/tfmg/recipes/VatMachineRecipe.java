package com.drmangotea.tfmg.recipes;

import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.List;

public class VatMachineRecipe extends ProcessingRecipe<RecipeWrapper> {

    public List<String> machines = new ArrayList<>();
    public List<String> allowedVatTypes = new ArrayList<>();
    public int minSize;
    public int heatLevel=0;

    public VatMachineRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(TFMGRecipeTypes.VAT_MACHINE_RECIPE, params);
    }



    @Override
    protected int getMaxInputCount() {
        return 4;
    }
    @Override
    protected int getMaxOutputCount() {
        return 4;
    }
    @Override
    protected int getMaxFluidInputCount() {
        return 4;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 4;
    }

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }

    @Override
    protected boolean canRequireHeat() {
        return true;
    }

    public void readAdditional(JsonObject json) {
        super.readAdditional(json);
        JsonArray machineArray = json.getAsJsonArray("machines");
        if (machineArray != null) {
            for (int i = 0; i < machineArray.size(); i++) {



                 machines.add(machineArray.get(i).getAsString());
            }
        }
        //
        JsonArray vatTypeArray = json.getAsJsonArray("allowedVatTypes");
        if (vatTypeArray != null) {
            for (int i = 0; i < vatTypeArray.size(); i++) {
                allowedVatTypes.add(vatTypeArray.get(i).getAsString());
            }
        }
        //
        this.minSize = GsonHelper.getAsInt(json, "minSize", 1);
    }



    public void writeAdditional(JsonObject json) {
        super.writeAdditional(json);
        JsonArray machineArray = new JsonArray();

        for (String string : machines) {
            machineArray.add(string);

        }
        json.add("machines", machineArray);
        //
        JsonArray vatTypeArray = new JsonArray();
        for (String string : allowedVatTypes) {
            vatTypeArray.add(string);
        }
        json.add("allowedVatTypes", vatTypeArray);
        //
        json.addProperty("minSize", this.minSize);

    }

    @Override
    public boolean matches(RecipeWrapper inv, Level worldIn) {
        return false;
    }
}
