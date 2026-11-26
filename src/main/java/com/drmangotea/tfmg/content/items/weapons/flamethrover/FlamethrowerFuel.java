package com.drmangotea.tfmg.content.items.weapons.flamethrover;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.registry.TFMGFlamethrowerFuelTypes;
import com.drmangotea.tfmg.registry.TFMGTags;
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

public record FlamethrowerFuel(@Nullable ResourceKey<FlamethrowerFuelType> fuelType, int amount, int color) implements TooltipProvider {

    public static final Codec<FlamethrowerFuel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(TFMGRegistries.FLAMETHROWER_FUEL_TYPE).fieldOf("fuel_type").forGetter(FlamethrowerFuel::fuelType),
            Codec.INT.fieldOf("amount").forGetter(FlamethrowerFuel::amount),
            Codec.INT.optionalFieldOf("color", 0xFFFFFF).forGetter(fuel -> fuel.color == 0 ? 0xFFFFFF : fuel.color)
    ).apply(instance, FlamethrowerFuel::new));

    public static final StreamCodec<ByteBuf, FlamethrowerFuel> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(TFMGRegistries.FLAMETHROWER_FUEL_TYPE),
            FlamethrowerFuel::fuelType,
            ByteBufCodecs.INT,
            FlamethrowerFuel::amount,
            ByteBufCodecs.INT,
            FlamethrowerFuel::color,
            FlamethrowerFuel::new
    );

    public static final FlamethrowerFuel EMPTY = new FlamethrowerFuel(TFMGFlamethrowerFuelTypes.FALLBACK, 0, 0xFFFFFF);

    @Override
    public void addToTooltip(Item.TooltipContext ctx, Consumer<Component> tooltip, TooltipFlag flag) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        var registries = player.registryAccess();
        Registry<FlamethrowerFuelType> registry = player.registryAccess().registryOrThrow(TFMGRegistries.FLAMETHROWER_FUEL_TYPE);
        FlamethrowerFuelType fallback = registry.get(TFMGFlamethrowerFuelTypes.FALLBACK);
        FlamethrowerFuelType fuelType = getFuelType(registries).orElse(registry.get(TFMGFlamethrowerFuelTypes.FALLBACK));

        if (fuelType() == null || fuelType == null || fuelType == fallback || !this.hasFuel()) return;

        Fluid displayFluid = fuelType.fluids().get(0).value();
        // Just needs to pull the lang key for the fluid so amount does not matter
        Component fluidName = displayFluid.getFluidType().getDescription(new FluidStack(displayFluid, 1));

        String _spread = "flamethrower.fuel.spread";
        String _speed = "flamethrower.fuel.speed";
        String _amount = "flamethrower.fuel.amount";
        String _cold = "flamethrower.fuel.cold";
        String _hellfire = "flamethrower.fuel.hellfire";
        String _capacity = "flamethrower.fuel.capacity";

        tooltip.accept(CommonComponents.EMPTY);
        tooltip.accept(fluidName.copy().append(Component.literal(":")).withStyle(ChatFormatting.GRAY));
        MutableComponent spacing = CommonComponents.space();
        ChatFormatting green = ChatFormatting.GREEN;
        ChatFormatting darkGreen = ChatFormatting.DARK_GREEN;
        ChatFormatting red = ChatFormatting.RED;

        int spreadF = fuelType.spread();
        float speedF = fuelType.speed();
        int amountF = fuelType.amount();
        boolean coldF = registry.getHolder(fuelType().location()).filter((holder) -> holder.is(TFMGTags.FlamethrowerFuel.COLD.tag)).isPresent();
        boolean hellfireF = registry.getHolder(fuelType().location()).filter((holder) -> holder.is(TFMGTags.FlamethrowerFuel.HELLFIRE.tag)).isPresent();

        MutableComponent spread = Component.literal("" + spreadF);
        MutableComponent speed = Component.literal(speedF == Mth.floor(speedF) ? "" + Mth.floor(speedF) : "" + speedF);
        MutableComponent amount = Component.literal("" + amountF);

        MutableComponent fuelCapacity = Component.literal(amount() + " / " + FUEL_CAPACITY + " mB");

        spread = spread.withStyle(spreadF > 20 ? green : darkGreen);
        speed = speed.withStyle(speedF > 1 ? green : darkGreen);
        amount = amount.withStyle(amountF > 10 ? green : darkGreen);
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
        tooltip.accept(spacing.plainCopy()
                .append(TFMGLang.translateDirect(_amount, amount)
                        .withStyle(darkGreen)));
        if (coldF) {
            tooltip.accept(spacing.plainCopy()
                    .append(TFMGLang.translateDirect(_cold)
                            .withStyle(darkGreen)));
        } else if (hellfireF) {
            tooltip.accept(spacing.plainCopy()
                    .append(TFMGLang.translateDirect(_hellfire)
                            .withStyle(darkGreen)));
        }
    }

    public FlamethrowerFuel decrement(int amount) {
        if (this.amount <= amount || fuelType == TFMGFlamethrowerFuelTypes.FALLBACK) {
            return EMPTY;
        }
        return new FlamethrowerFuel(fuelType, this.amount - amount, color);
    }

    public FlamethrowerFuel increment(int amount, int capacity) {
        if (fuelType == TFMGFlamethrowerFuelTypes.FALLBACK) {
            return EMPTY;
        }
        if (this.amount + amount > capacity) {
            return new FlamethrowerFuel(fuelType, capacity, color);
        }
        return new FlamethrowerFuel(fuelType, this.amount + amount, color);
    }

    public static FlamethrowerFuel createForType(RegistryAccess registryAccess, Fluid fluid, int amount) {
        Optional<Holder.Reference<FlamethrowerFuelType>> type = FlamethrowerFuelType.getTypeForFluid(registryAccess, fluid);
        return type.map(typeReference -> new FlamethrowerFuel(typeReference.getKey(), amount, typeReference.value().color())).orElse(EMPTY);
    }

    public static FlamethrowerFuel createForType(RegistryAccess registryAccess, FluidStack stack) {
        return createForType(registryAccess, stack.getFluid(), stack.getAmount());
    }

    public static FlamethrowerFuel createForLegacy(RegistryAccess registryAccess, String fuelType, int amount) {
        ResourceKey<FlamethrowerFuelType> key = ResourceKey.create(TFMGRegistries.FLAMETHROWER_FUEL_TYPE, TFMG.asResource(fuelType));
        Optional<Holder.Reference<FlamethrowerFuelType>> type = registryAccess.lookupOrThrow(TFMGRegistries.FLAMETHROWER_FUEL_TYPE).get(key);
        return type.map(typeReference -> new FlamethrowerFuel(typeReference.getKey(), amount, type.get().value().color())).orElse(EMPTY);
    }

    public boolean isEmpty() {
        if (fuelType == TFMGFlamethrowerFuelTypes.FALLBACK) {
            return true;
        }
        return this.amount <= 0;
    }

    public boolean hasFuel() {
        return fuelType != TFMGFlamethrowerFuelTypes.FALLBACK;
    }

    public Optional<FlamethrowerFuelType> getFuelType(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(TFMGRegistries.FLAMETHROWER_FUEL_TYPE).getOptional(fuelType);
    }

    public FlamethrowerFuelType getFuelTypeOrThrow(RegistryAccess registryAccess) {
        return getFuelType(registryAccess).orElseThrow(() -> new IllegalStateException("No flamethrower fuel type found for " + fuelType));
    }
}
