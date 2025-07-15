package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.fluid.GasFluidType;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.VirtualFluidBuilder;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.FluidBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

import static com.drmangotea.tfmg.registry.TFMGFluids.getGasTexture;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class TFMGRegistrate extends CreateRegistrate {
    protected TFMGRegistrate(String modid) {
        super(modid);
    }

    public TFMGRegistrate setTooltipModifierFactory(@Nullable Function<Item, TooltipModifier> factory) {
        currentTooltipModifierFactory = factory;
        return this;
    }


    public static String autoLang(String id) {
        StringBuilder builder = new StringBuilder();
        boolean b = true;
        for (char c: id.toCharArray()) {
            if(c == '_') {
                builder.append(' ');
                b = true;
            } else {
                builder.append(b ? String.valueOf(c).toUpperCase() : c);
                b = false;
            }
        }
        return builder.toString();
    }

    public FluidBuilder<VirtualFluid, CreateRegistrate> gasFluid(String name, int color) {
        return entry(name, c -> new VirtualFluidBuilder<>(self(),self(), name, c, getGasTexture(), getGasTexture(),
                GasFluidType.create(color),VirtualFluid::createSource,VirtualFluid::createFlowing));
    }



    public static TFMGRegistrate create(String id) {
        return new TFMGRegistrate(id);
    }

    //public static Block getBlock(String name) {
    //    return TFMG.REGISTRATE.get(name, Registrate.BLOCKS.getRegistryKey()).get();
    //}
    //public static Item getItem(String name) {
    //    return TFMG.REGISTRATE.get(name, ForgeRegistries.ITEMS.getRegistryKey()).get();
    //}
    public static Item getBucket(String name) {
        return TFMG.REGISTRATE.get(name+"_bucket", Registries.ITEM).get();
    }

}
