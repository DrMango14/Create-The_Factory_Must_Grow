package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.fluid.GasFluidType;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.cable_type.CableType;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.cable_type.CableTypeBuilder;
import com.drmangotea.tfmg.content.engines.types.EngineType;
import com.drmangotea.tfmg.content.engines.types.EngineTypeBuilder;
import com.drmangotea.tfmg.content.engines.upgrades.base.EngineUpgrade;
import com.drmangotea.tfmg.content.engines.upgrades.base.EngineUpgradeBuilder;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatOperation;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatOperationBuilder;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.types.VatType;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.types.VatTypeBuilder;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.Electrode;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.ElectrodeBuilder;
import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.mode.MixerMode;
import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.mode.MixerModeBuilder;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.VirtualFluidBuilder;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

import static com.drmangotea.tfmg.registry.TFMGFluids.getGasTexture;

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

    public <T extends EngineType> EngineTypeBuilder<T, TFMGRegistrate> engineType(NonNullFunction<EngineType.Properties, T> factory) {
        return engineType((TFMGRegistrate) self(), factory);
    }

    public <T extends EngineType> EngineTypeBuilder<T, TFMGRegistrate> engineType(String name, NonNullFunction<EngineType.Properties, T> factory) {
        return engineType((TFMGRegistrate) self(), name, factory);
    }

    public <T extends EngineType, P> EngineTypeBuilder<T, P> engineType(P parent, NonNullFunction<EngineType.Properties, T> factory) {
        return engineType(parent, currentName(), factory);
    }

    public <T extends EngineType, P> EngineTypeBuilder<T, P> engineType(P parent, String name, NonNullFunction<EngineType.Properties, T> factory) {
        return entry(name, callback -> EngineTypeBuilder.create(this, parent, name, callback, factory));
    }

    public <T extends MixerMode> MixerModeBuilder<T, TFMGRegistrate> mixerMode(NonNullFunction<MixerMode.Properties, T> factory) {
        return mixerMode((TFMGRegistrate) self(), factory);
    }

    public <T extends MixerMode> MixerModeBuilder<T, TFMGRegistrate> mixerMode(String name, NonNullFunction<MixerMode.Properties, T> factory) {
        return mixerMode((TFMGRegistrate) self(), name, factory);
    }

    public <T extends MixerMode, P> MixerModeBuilder<T, P> mixerMode(P parent, NonNullFunction<MixerMode.Properties, T> factory) {
        return mixerMode(parent, currentName(), factory);
    }

    public <T extends MixerMode, P> MixerModeBuilder<T, P> mixerMode(P parent, String name, NonNullFunction<MixerMode.Properties, T> factory) {
        return entry(name, callback -> MixerModeBuilder.create(this, parent, name, callback, factory));
    }

    public VatOperationBuilder<TFMGRegistrate> vatOperation(Function<ResourceLocation, VatOperation> factory) {
        return vatOperation((TFMGRegistrate) self(), factory);
    }

    public VatOperationBuilder<TFMGRegistrate> vatOperation(String name, Function<ResourceLocation, VatOperation> factory) {
        return vatOperation((TFMGRegistrate) self(), name, factory);
    }

    public <P> VatOperationBuilder<P> vatOperation(P parent, Function<ResourceLocation, VatOperation> factory) {
        return vatOperation(parent, currentName(), factory);
    }

    public <P> VatOperationBuilder<P> vatOperation(P parent, String name, Function<ResourceLocation, VatOperation> factory) {
        return entry(name, callback -> new VatOperationBuilder<>(this, parent, name, callback, factory));
    }

    public VatTypeBuilder<TFMGRegistrate> vatType(Function<ResourceLocation, VatType> factory) {
        return vatType((TFMGRegistrate) self(), factory);
    }

    public VatTypeBuilder<TFMGRegistrate> vatType(String name, Function<ResourceLocation, VatType> factory) {
        return vatType((TFMGRegistrate) self(), name, factory);
    }

    public <P> VatTypeBuilder<P> vatType(P parent, Function<ResourceLocation, VatType> factory) {
        return vatType(parent, currentName(), factory);
    }

    public <P> VatTypeBuilder<P> vatType(P parent, String name, Function<ResourceLocation, VatType> factory) {
        return entry(name, callback -> new VatTypeBuilder<>(this, parent, name, callback, factory));
    }

    public <T extends EngineUpgrade> EngineUpgradeBuilder<T, TFMGRegistrate> engineUpgrade(Function<ResourceLocation, T> factory) {
        return engineUpgrade((TFMGRegistrate) self(), factory);
    }

    public <T extends EngineUpgrade> EngineUpgradeBuilder<T, TFMGRegistrate> engineUpgrade(String name, Function<ResourceLocation, T> factory) {
        return engineUpgrade((TFMGRegistrate) self(), name, factory);
    }

    public <T extends EngineUpgrade, P> EngineUpgradeBuilder<T, P> engineUpgrade(P parent, Function<ResourceLocation, T> factory) {
        return engineUpgrade(parent, currentName(), factory);
    }

    public <T extends EngineUpgrade, P> EngineUpgradeBuilder<T, P> engineUpgrade(P parent, String name, Function<ResourceLocation, T> factory) {
        return entry(name, callback -> new EngineUpgradeBuilder<>(this, parent, name, callback, factory));
    }
}
