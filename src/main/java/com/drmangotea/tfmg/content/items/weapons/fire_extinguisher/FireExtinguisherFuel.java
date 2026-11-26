package com.drmangotea.tfmg.content.items.weapons.fire_extinguisher;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.registry.TFMGFireExtinguisherFuelTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

import static com.drmangotea.tfmg.content.items.weapons.flamethrover.FlamethrowerItem.FUEL_CAPACITY;

@NothingNullByDefault
public record FireExtinguisherFuel(@Nullable ResourceKey<FireExtinguisherFuelType> fuelType, int amount, int color) implements TooltipProvider {

    public static final Codec<FireExtinguisherFuel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(TFMGRegistries.FIRE_EXTINGUISHER_FUEL_TYPE).fieldOf("fuel_type").forGetter(FireExtinguisherFuel::fuelType),
            Codec.INT.fieldOf("amount").forGetter(FireExtinguisherFuel::amount),
            Codec.INT.optionalFieldOf("color", 0xFFFFFF).forGetter(fuel -> fuel.color == 0 ? 0xFFFFFF : fuel.color)
    ).apply(instance, FireExtinguisherFuel::new));

    public static final StreamCodec<ByteBuf, FireExtinguisherFuel> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(TFMGRegistries.FIRE_EXTINGUISHER_FUEL_TYPE),
            FireExtinguisherFuel::fuelType,
            ByteBufCodecs.INT,
            FireExtinguisherFuel::amount,
            ByteBufCodecs.INT,
            FireExtinguisherFuel::color,
            FireExtinguisherFuel::new
    );

    public static final FireExtinguisherFuel EMPTY = new FireExtinguisherFuel(TFMGFireExtinguisherFuelTypes.FALLBACK, 0, 0xFFFFFF);

    @Override
    public void addToTooltip(Item.TooltipContext ctx, Consumer<Component> tooltip, TooltipFlag flag) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        var registries = player.registryAccess();
        Registry<FireExtinguisherFuelType> registry = player.registryAccess().registryOrThrow(TFMGRegistries.FIRE_EXTINGUISHER_FUEL_TYPE);
        FireExtinguisherFuelType fallback = registry.get(TFMGFireExtinguisherFuelTypes.FALLBACK);
        FireExtinguisherFuelType fuelType = getFuelType(registries).orElse(registry.get(TFMGFireExtinguisherFuelTypes.FALLBACK));

        if (fuelType() == null || fuelType == null || fuelType == fallback || !this.hasFuel()) return;

        String _spread = "flamethrower.fuel.spread";
        String _speed = "flamethrower.fuel.speed";
        String _capacity = "flamethrower.fuel.capacity";

        tooltip.accept(CommonComponents.EMPTY);
        tooltip.accept(Component.translatable(fuelType().location().toLanguageKey("fire_extinguisher_fuel")).append(Component.literal(":")).withStyle(ChatFormatting.GRAY));
        MutableComponent spacing = CommonComponents.space();
        ChatFormatting green = ChatFormatting.GREEN;
        ChatFormatting darkGreen = ChatFormatting.DARK_GREEN;
        ChatFormatting red = ChatFormatting.RED;

        int spreadF = fuelType.spread();
        float speedF = fuelType.speed();

        MutableComponent spread = Component.literal("" + spreadF);
        MutableComponent speed = Component.literal(speedF == Mth.floor(speedF) ? "" + Mth.floor(speedF) : "" + speedF);

        MutableComponent fuelCapacity = Component.literal(amount() + " / " + FUEL_CAPACITY + " mB");

        spread = spread.withStyle(spreadF > 20 ? green : darkGreen);
        speed = speed.withStyle(speedF > 1 ? green : darkGreen);
        fuelCapacity = fuelCapacity.withStyle(amount() == 0 ? red : green);

        tooltip.accept(spacing.plainCopy()
                .append(TFMGLang.translateDirect(_capacity, fuelCapacity)
                        .withStyle(darkGreen)));

        tooltip.accept(spacing.plainCopy()
                .append(TFMGLang.translateDirect(_spread, spread)
                        .withStyle(darkGreen)));
        tooltip.accept(spacing.plainCopy()
                .append(TFMGLang.translateDirect(_speed, speed)
                        .withStyle(darkGreen)));
    }

    public FireExtinguisherFuel decrement(int amount) {
        if (this.amount <= amount || fuelType == TFMGFireExtinguisherFuelTypes.FALLBACK) {
            return EMPTY;
        }
        return new FireExtinguisherFuel(fuelType, this.amount - amount, color);
    }

    public FireExtinguisherFuel increment(int amount, int capacity) {
        if (fuelType == TFMGFireExtinguisherFuelTypes.FALLBACK) {
            return EMPTY;
        }
        if (this.amount + amount > capacity) {
            return new FireExtinguisherFuel(fuelType, capacity, color);
        }
        return new FireExtinguisherFuel(fuelType, this.amount + amount, color);
    }

    public static FireExtinguisherFuel createForType(RegistryAccess registryAccess, Fluid fluid, int amount) {
        Optional<Holder.Reference<FireExtinguisherFuelType>> type = FireExtinguisherFuelType.getTypeForFluid(registryAccess, fluid);
        return type.map(typeReference -> new FireExtinguisherFuel(typeReference.getKey(), amount, typeReference.value().color())).orElse(EMPTY);
    }

    public static FireExtinguisherFuel createForType(RegistryAccess registryAccess, FluidStack stack) {
        return createForType(registryAccess, stack.getFluid(), stack.getAmount());
    }

    public static FireExtinguisherFuel createForLegacy(RegistryAccess registryAccess, String fuelType, int amount) {
        ResourceKey<FireExtinguisherFuelType> key = ResourceKey.create(TFMGRegistries.FIRE_EXTINGUISHER_FUEL_TYPE, TFMG.asResource(fuelType));
        Optional<Holder.Reference<FireExtinguisherFuelType>> type = registryAccess.lookupOrThrow(TFMGRegistries.FIRE_EXTINGUISHER_FUEL_TYPE).get(key);
        return type.map(typeReference -> new FireExtinguisherFuel(typeReference.getKey(), amount, type.get().value().color())).orElse(EMPTY);
    }

    public boolean isEmpty() {
        if (fuelType == TFMGFireExtinguisherFuelTypes.FALLBACK) {
            return true;
        }
        return this.amount <= 0;
    }

    public boolean hasFuel() {
        return fuelType != TFMGFireExtinguisherFuelTypes.FALLBACK;
    }

    public Optional<FireExtinguisherFuelType> getFuelType(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(TFMGRegistries.FIRE_EXTINGUISHER_FUEL_TYPE).getOptional(fuelType);
    }

    public FireExtinguisherFuelType getFuelTypeOrThrow(RegistryAccess registryAccess) {
        return getFuelType(registryAccess).orElseThrow(() -> new IllegalStateException("No fire extinguisher fuel type found for " + fuelType));
    }
}
