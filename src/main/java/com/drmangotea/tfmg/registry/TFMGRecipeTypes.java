package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.recipes.*;
import com.mojang.serialization.Codec;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum TFMGRecipeTypes implements IRecipeTypeInfo, StringRepresentable {
    CASTING(CastingRecipe::new),
    INDUSTRIAL_BLASTING(() -> new IndustrialBlastingRecipe.Serializer<>(IndustrialBlastingRecipe::new)),
    COKING(CokingRecipe::new),
    DISTILLATION(DistillationRecipe::new),
    WINDING(WindingRecipe::new),
    HOT_BLAST(HotBlastRecipe::new),
    VAT(() -> new VatMachineRecipe.Serializer<>(VatMachineRecipe::new)),
	POLARIZING(() -> new PolarizingRecipe.Serializer<>(PolarizingRecipe::new)),

    /**
     * Use {@link #VAT} instead
     */
    @Deprecated(since = "1.3.2a", forRemoval = true)
    VAT_MACHINE_RECIPE(() -> new VatMachineRecipe.Serializer<>(VatMachineRecipe::new));


    public static final Predicate<RecipeHolder<?>> CAN_BE_AUTOMATED = r -> !r.id()
            .getPath()
            .endsWith("_manual_only");

    public final ResourceLocation id;
    public final Supplier<RecipeSerializer<?>> serializerSupplier;
    private final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> serializerObject;
    private final Supplier<RecipeType<?>> type;

    public static final Codec<TFMGRecipeTypes> CODEC = StringRepresentable.fromEnum(TFMGRecipeTypes::values);


	TFMGRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
        String name = TFMGLang.asId(name());
        id = TFMG.asResource(name);
        this.serializerSupplier = serializerSupplier;
        serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        type = Registers.TYPE_REGISTER.register(name, () -> RecipeType.simple(id));
    }

    TFMGRecipeTypes(StandardProcessingRecipe.Factory<?> processingFactory) {
        this(() -> new StandardProcessingRecipe.Serializer<>(processingFactory));
    }

    @ApiStatus.Internal
    public static void register(IEventBus modEventBus) {
        ShapedRecipePattern.setCraftingSize(9, 9);
        Registers.SERIALIZER_REGISTER.register(modEventBus);
        Registers.TYPE_REGISTER.register(modEventBus);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializerObject.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getType() {
        return (RecipeType<R>) type.get();
    }

    public <I extends RecipeInput, R extends Recipe<I>> Optional<RecipeHolder<R>> find(I inv, Level world) {
        return world.getRecipeManager()
                .getRecipeFor(getType(), inv, world);
    }

    public static boolean shouldIgnoreInAutomation(RecipeHolder<?> recipe) {
        RecipeSerializer<?> serializer = recipe.value().getSerializer();
        if (AllTags.AllRecipeSerializerTags.AUTOMATION_IGNORE.matches(serializer))
            return true;
        return !CAN_BE_AUTOMATED.test(recipe);
    }

    @Override
    public @NotNull String getSerializedName() {
        return id.toString();
    }

    private static class Registers {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, TFMG.MOD_ID);
        private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, TFMG.MOD_ID);
    }

}
