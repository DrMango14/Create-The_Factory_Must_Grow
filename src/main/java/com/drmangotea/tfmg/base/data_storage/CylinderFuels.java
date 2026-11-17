package com.drmangotea.tfmg.base.data_storage;

import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.engines.fuels.EngineFuelType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public record CylinderFuels(List<ResourceKey<EngineFuelType>> validFuels) implements TooltipProvider {

    public static final Codec<CylinderFuels> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(TFMGRegistries.ENGINE_FUEL_TYPE).listOf().fieldOf("valid_fuels").forGetter(CylinderFuels::validFuels)
    ).apply(instance, CylinderFuels::new));

    public static final StreamCodec<ByteBuf, CylinderFuels> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(TFMGRegistries.ENGINE_FUEL_TYPE).apply(ByteBufCodecs.list()),
            CylinderFuels::validFuels,
            CylinderFuels::new
    );

    public static final CylinderFuels EMPTY = new CylinderFuels(List.of());

    @Override
    public void addToTooltip(Item.TooltipContext ctx, Consumer<Component> tooltip, TooltipFlag flag) {
        if(validFuels == null || validFuels.isEmpty())
            return;
        tooltip.accept(TFMGLang.translateDirect("tooltip.cylinder")
                .withStyle(ChatFormatting.GRAY));
        for (var key : validFuels) {
            MutableComponent component = TFMGLang.text("- ").component()
                    .append(Component.translatable(key.location().toLanguageKey("engine_fuel")))
                    .withStyle(ChatFormatting.AQUA);
            tooltip.accept(component);
        }
    }

    @SafeVarargs
    public final CylinderFuels withAdditionalFuel(ResourceKey<EngineFuelType>... fuels) {
        // A Set is used to prevent duplicate fuel types from being added
        Set<ResourceKey<EngineFuelType>> merged = new LinkedHashSet<>(validFuels);
        merged.addAll(List.of(fuels));
        return new CylinderFuels(List.copyOf(merged));
    }

    public boolean isSame(CylinderFuels other) {
        if (other == null) return false;
        return new HashSet<>(validFuels).containsAll(other.validFuels);
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean testFuel(FluidStack fluidStack, RegistryAccess registryAccess) {
        if (isEmpty()) return false;
        for (var key : validFuels()) {
            var fuelType = registryAccess.registryOrThrow(TFMGRegistries.ENGINE_FUEL_TYPE).get(key);
            if (fuelType != null && fuelType.test(fluidStack)) {
                return true;
            }
        }
        return false;
    }
}
