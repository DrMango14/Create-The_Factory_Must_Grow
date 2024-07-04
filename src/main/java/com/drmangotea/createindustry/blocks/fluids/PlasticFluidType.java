package com.drmangotea.createindustry.blocks.fluids;

import com.drmangotea.createindustry.blocks.concrete.ConcreteFluidType;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllFluids;
import com.simibubi.create.foundation.utility.Color;
import com.tterrag.registrate.builders.FluidBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Supplier;

public class PlasticFluidType extends HotFluidType {
    public PlasticFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
        super(properties, stillTexture, flowingTexture);
    }

}
