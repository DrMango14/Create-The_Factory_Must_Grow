package com.drmangotea.tfmg.content.engines.types;

import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.content.engines.types.regular_engine.PistonPosition;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class EngineType {
    private String descriptionId;
    private final ResourceLocation id;
    public final float speedModifier;
    public final float torqueModifier;
    public final float efficiencyModifier;
    public final List<PistonPosition> pistons;
    public final String lastRequirement;
    public final PartialModel cylinderModel;

    public EngineType(Properties properties) {
        this.speedModifier = properties.speedModifier;
        this.torqueModifier = properties.torqueModifier;
        this.efficiencyModifier = properties.efficiencyModifier;
        this.pistons = properties.pistons;
        this.lastRequirement = properties.lastRequirement;
        this.cylinderModel = properties.cylinderModel;
        this.id = properties.id;
    }

    public Holder.Reference<EngineType> builtInRegistryHolder() {
        Optional<ResourceKey<EngineType>> resourceKey = TFMGRegistries.ENGINE_TYPE_REGISTRY.getResourceKey(this);
        return resourceKey.map(TFMGRegistries.ENGINE_TYPE_REGISTRY::getHolderOrThrow).orElseThrow();
    }

    public ResourceLocation getKey() {
        return this.id;
    }

    public String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("engine_type", getKey());
        }
        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public Component getDisplayName() {
        return Component.translatable(this.getOrCreateDescriptionId());
    }

    public Predicate<FluidStack> fuelBlacklist() {
        return (fluidStack) -> false;
    }

    public boolean is(TagKey<EngineType> tag) {
        var holder = TFMGRegistries.ENGINE_TYPE_REGISTRY.getHolder(getKey());
        return holder.map(engineTypeReference -> engineTypeReference.is(tag)).orElse(false);
    }

    public boolean is(EngineType engineType) {
        return this == engineType;
    }

    public boolean is(Predicate<Holder<EngineType>> engineType) {
        var holder = TFMGRegistries.ENGINE_TYPE_REGISTRY.getHolder(getKey());
        return holder.filter(engineType).isPresent();
    }

    public boolean is(Holder<EngineType> engineType) {
        return this.is(engineType.value());
    }

    public boolean is(HolderSet<EngineType> engineType) {
        var holder = TFMGRegistries.ENGINE_TYPE_REGISTRY.getHolder(getKey());
        return holder.map(engineType::contains).orElse(false);
    }

    public static class Properties {
        private ResourceLocation id;

        public float speedModifier;
        public float torqueModifier;
        public float efficiencyModifier;
        public List<PistonPosition> pistons;
        private String lastRequirement = "pistons";
        private PartialModel cylinderModel = TFMGPartialModels.SMALL_CYLINDER;

        public Properties speed(float modifier) {
            this.speedModifier = modifier;
            return this;
        }

        public Properties torque(float modifier) {
            this.torqueModifier = modifier;
            return this;
        }

        public Properties efficiency(float modifier) {
            this.efficiencyModifier = modifier;
            return this;
        }

        public Properties pistons(List<PistonPosition> pistons) {
            this.pistons = pistons;
            return this;
        }

        public Properties lastRequirement(String name) {
            this.lastRequirement = name;
            return this;
        }

        public Properties cylinderModel(PartialModel cylinderModel) {
            this.cylinderModel = cylinderModel;
            return this;
        }

        public Properties(ResourceLocation id) {
            this.id = id;
        }
    }
}
