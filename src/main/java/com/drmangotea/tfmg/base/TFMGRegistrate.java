package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.fluid.GasFluidType;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableType;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableTypeBuilder;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.Electrode;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.ElectrodeBuilder;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.VirtualFluidBuilder;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraftforge.registries.ForgeRegistries;

import static com.drmangotea.tfmg.registry.TFMGFluids.getGasTexture;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class TFMGRegistrate extends CreateRegistrate {
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

    protected TFMGRegistrate() {
        super(TFMG.MOD_ID);
    }

    public static TFMGRegistrate create() {
        return (TFMGRegistrate) new TFMGRegistrate().setTooltipModifierFactory(item ->
                new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                        .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
        );
    }

    public static Block getBlock(String name) {
        return TFMG.REGISTRATE.get(name, ForgeRegistries.BLOCKS.getRegistryKey()).get();
    }
    public static Item getItem(String name) {
        return TFMG.REGISTRATE.get(name, ForgeRegistries.ITEMS.getRegistryKey()).get();
    }
    public static Item getBucket(String name) {
        return TFMG.REGISTRATE.get(name+"_bucket", ForgeRegistries.ITEMS.getRegistryKey()).get();
    }

    public <T extends CableType> CableTypeBuilder<T, TFMGRegistrate> cableType(NonNullFunction<CableType.Properties, T> factory) {
        return cableType((TFMGRegistrate) self(), factory);
    }

    public <T extends CableType> CableTypeBuilder<T, TFMGRegistrate> cableType(String name, NonNullFunction<CableType.Properties, T> factory) {
        return cableType((TFMGRegistrate) self(), name, factory);
    }

    public <T extends CableType, P> CableTypeBuilder<T, P> cableType(P parent, NonNullFunction<CableType.Properties, T> factory) {
        return cableType(parent, currentName(), factory);
    }

    public <T extends CableType, P> CableTypeBuilder<T, P> cableType(P parent, String name, NonNullFunction<CableType.Properties, T> factory) {
        return entry(name, callback -> CableTypeBuilder.create(this, parent, name, callback, factory));
    }

    public <T extends Electrode> ElectrodeBuilder<T, TFMGRegistrate> electrode(NonNullFunction<Electrode.Properties, T> factory) {
        return electrode((TFMGRegistrate) self(), factory);
    }

    public <T extends Electrode> ElectrodeBuilder<T, TFMGRegistrate> electrode(String name, NonNullFunction<Electrode.Properties, T> factory) {
        return electrode((TFMGRegistrate) self(), name, factory);
    }

    public <T extends Electrode, P> ElectrodeBuilder<T, P> electrode(P parent, NonNullFunction<Electrode.Properties, T> factory) {
        return electrode(parent, currentName(), factory);
    }

    public <T extends Electrode, P> ElectrodeBuilder<T, P> electrode(P parent, String name, NonNullFunction<Electrode.Properties, T> factory) {
        return entry(name, callback -> ElectrodeBuilder.create(this, parent, name, callback, factory));
    }
}
