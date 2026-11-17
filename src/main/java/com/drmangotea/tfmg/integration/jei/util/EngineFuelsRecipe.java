package com.drmangotea.tfmg.integration.jei.util;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.content.engines.fuels.EngineFuelType;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.Internal;
import mezz.jei.common.util.ErrorUtil;
import net.createmod.catnip.net.base.ClientboundPacketPayload;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.crafting.CompoundFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import static com.drmangotea.tfmg.integration.jei.TFMGJeiConstants.asDrawable;

public record EngineFuelsRecipe(ResourceLocation id, EngineFuelType type) {

    public IDrawable getBackground() {
        return asDrawable(AllGuiTextures.JEI_SLOT);
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
        builder.addInputSlot(0, 4).setBackground(getBackground(), -1, -1)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.asList(buildIngredient().getStacks()))
                .setFluidRenderer(1000, false, 16, 16)
                .addRichTooltipCallback(new TooltipCallback(id));

        builder.addOutputSlot(63, 33).setBackground(getBackground(), -1, -1)
                .addFluidStack(type().exhaust())
                .setFluidRenderer(1000, false, 16, 16)
                .addRichTooltipCallback(new TooltipCallback(id));
    }

    public void draw(GuiGraphics graphics) {
        graphics.drawString(Minecraft.getInstance().font, TFMGLang.translate("jei.engine_fuels.speed", TFMGTexts.DECIMAL_FORMAT.format(type.speed())).component(), 19, 5, 4210752, false);
        graphics.drawString(Minecraft.getInstance().font, TFMGLang.translate("jei.engine_fuels.efficiency", TFMGTexts.DECIMAL_FORMAT.format(type.efficiency())).component(), 19, 14, 4210752, false);
        graphics.drawString(Minecraft.getInstance().font, TFMGLang.translate("jei.engine_fuels.torque", TFMGTexts.DECIMAL_FORMAT.format(type.torque())).component(), 19, 23, 4210752, false);
        graphics.drawString(Minecraft.getInstance().font, TFMGLang.translate("jei.engine_fuels.exhaust").component(), 19, 36, 4210752, false);
    }

    private FluidIngredient buildIngredient() {
        List<FluidIngredient> children = new ArrayList<>();
        type.fluids().unwrap().ifRight(list -> list.forEach(fluid -> children.add(FluidIngredient.single(fluid))));
        type.fluids().unwrap().ifLeft(tag -> children.add(FluidIngredient.tag(tag)));
        return CompoundFluidIngredient.of(children);
    }

    public static List<EngineFuelsRecipe> fromClient(Map<ResourceLocation, EngineFuelType> fuels) {
        return fuels.entrySet().stream().map(entry -> new EngineFuelsRecipe(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    public record SyncPacket(List<Holder<EngineFuelType>> fuels) implements ClientboundPacketPayload {
        public static final StreamCodec<RegistryFriendlyByteBuf, SyncPacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.holder(TFMGRegistries.ENGINE_FUEL_TYPE, EngineFuelType.STREAM_CODEC).apply(ByteBufCodecs.list()), SyncPacket::fuels,
                SyncPacket::new);

        @Override
        @OnlyIn(Dist.CLIENT)
        public void handle(LocalPlayer player) {
            ClientEngineFuels.INSTANCE.clear();
            ClientEngineFuels.INSTANCE.populate(fuels);
        }

        @Override
        public PacketTypeProvider getTypeProvider() {
            return TFMGPackets.ENGINE_FUELS;
        }
    }

    static class TooltipCallback implements IRecipeSlotRichTooltipCallback {
        private final ResourceLocation recipeName;
        private final boolean recipeFromSameModAsCategory;

        TooltipCallback(ResourceLocation recipeName) {
            this.recipeName = recipeName;
            this.recipeFromSameModAsCategory = recipeName.getNamespace().equals(TFMG.MOD_ID);
        }

        @Override
        public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
            Optional<ITypedIngredient<?>> displayedIngredient = recipeSlotView.getDisplayedIngredient();
            if (displayedIngredient.isEmpty()) {
                return;
            }

            addRecipeBy(tooltip, displayedIngredient.get());

            Minecraft minecraft = Minecraft.getInstance();
            boolean showAdvanced = minecraft.options.advancedItemTooltips || Screen.hasShiftDown();
            if (showAdvanced) {
                MutableComponent recipeId = Component.translatable("jei.tooltip.recipe.id", Component.literal(recipeName.toString()));
                tooltip.add(recipeId.withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        private void addRecipeBy(ITooltipBuilder tooltip, ITypedIngredient<?> displayedIngredient) {
            if (recipeFromSameModAsCategory) {
                return;
            }
            IModIdHelper modIdHelper = Internal.getJeiRuntime().getJeiHelpers().getModIdHelper();
            if (!modIdHelper.isDisplayingModNameEnabled()) {
                return;
            }
            String ingredientModId = getDisplayModId(displayedIngredient);
            if (ingredientModId == null) {
                return;
            }
            String recipeModId = recipeName.getNamespace();
            if (recipeModId.equals(ingredientModId)) {
                return;
            }
            Component modName = modIdHelper.getFormattedModNameComponentForModId(recipeModId);
            MutableComponent recipeBy = Component.translatable("jei.tooltip.recipe.by", modName);
            tooltip.add(recipeBy.withStyle(ChatFormatting.GRAY));
        }

        private <T> @Nullable String getDisplayModId(ITypedIngredient<T> typedIngredient) {
            IIngredientManager ingredientManager = Internal.getJeiRuntime().getIngredientManager();

            IIngredientType<T> type = typedIngredient.getType();
            T ingredient = typedIngredient.getIngredient();
            IIngredientHelper<T> ingredientHelper = ingredientManager.getIngredientHelper(type);
            try {
                return ingredientHelper.getDisplayModId(ingredient);
            } catch (RuntimeException e) {
                String ingredientInfo = ErrorUtil.getIngredientInfo(ingredient, type, ingredientManager);
                TFMG.LOGGER.error("[JEI] Caught exception from ingredient without a resource location: {}", ingredientInfo, e);
                return null;
            }
        }
    }
}
