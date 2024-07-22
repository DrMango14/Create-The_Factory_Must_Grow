package com.drmangotea.createindustry.registry;


import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.blocks.machines.simple.welding_machine.WeldingRecipe;
import com.drmangotea.createindustry.recipes.casting.CastingRecipe;
import com.drmangotea.createindustry.recipes.coking.CokingRecipe;
import com.drmangotea.createindustry.recipes.distillation.DistillationRecipe;
import com.drmangotea.createindustry.recipes.gas_blasting.GasBlastingRecipe;
import com.drmangotea.createindustry.recipes.industrial_blasting.IndustrialBlastingRecipe;
import com.drmangotea.createindustry.recipes.polarizing.PolarizingRecipe;
import com.google.common.collect.ImmutableSet;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeFactory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public enum TFMGRecipeTypes implements IRecipeTypeInfo {

    CASTING(CastingRecipe::new),
    INDUSTRIAL_BLASTING(IndustrialBlastingRecipe::new),
    COKING(CokingRecipe::new),
    DISTILLATION(DistillationRecipe::new),
    WELDING(WeldingRecipe::new),
    GAS_BLASTING(GasBlastingRecipe::new),
    POLARIZING(PolarizingRecipe::new),
;

    private final ResourceLocation id;
    private final RegistryObject<RecipeSerializer<?>> serializerObject;
    @Nullable
    private final RegistryObject<RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;

    TFMGRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier, boolean registerType) {
        String name = Lang.asId(name());
        id = CreateTFMG.asResource(name);
        serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        if (registerType) {
            typeObject = Registers.TYPE_REGISTER.register(name, typeSupplier);
            type = typeObject;
        } else {
            typeObject = null;
            type = typeSupplier;
        }
    }

    TFMGRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
        String name = Lang.asId(name());
        id = CreateTFMG.asResource(name);
        serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        typeObject = Registers.TYPE_REGISTER.register(name, () -> simpleType(id));
        type = typeObject;
    }
   TFMGRecipeTypes(ProcessingRecipeFactory<?> processingFactory) {
        this(() -> new ProcessingRecipeSerializer<>(processingFactory));
    }
    public static boolean shouldIgnoreInAutomation(Recipe<?> recipe) {
        RecipeSerializer<?> serializer = recipe.getSerializer();
        if (serializer != null && AllTags.AllRecipeSerializerTags.AUTOMATION_IGNORE.matches(serializer))
            return true;
        return recipe.getId()
                .getPath()
                .endsWith("_manual_only");
    }
    public static <T extends Recipe<?>> RecipeType<T> simpleType(ResourceLocation id) {
        String stringId = id.toString();
        return new RecipeType<T>() {
            @Override
            public String toString() {
                return stringId;
            }
        };
    }

    public static void register(IEventBus modEventBus) {
        ShapedRecipe.setCraftingSize(9, 9);

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
    public <T extends RecipeType<?>> T getType() {
        return (T) type.get();
    }

    public <C extends Container, T extends Recipe<C>> Optional<T> find(C inv, Level world) {
        return world.getRecipeManager()
                .getRecipeFor(getType(), inv, world);
    }

    public static final Set<ResourceLocation> RECIPE_DENY_SET =
            ImmutableSet.of(new ResourceLocation("occultism", "spirit_trade"), new ResourceLocation("occultism", "ritual"));


    private static class Registers {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CreateTFMG.MOD_ID);
        private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, CreateTFMG.MOD_ID);
    }

}
