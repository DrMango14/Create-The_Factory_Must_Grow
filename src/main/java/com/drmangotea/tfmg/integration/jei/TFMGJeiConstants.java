package com.drmangotea.tfmg.integration.jei;

import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.integration.jei.util.EngineFuelsRecipe;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import dev.metallurgists.rutile.compat.jei.category.ElementCompositionWrapper;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TFMGJeiConstants {
    @Nullable
    public static RegistryAccess registryAccess() {
        if (Minecraft.getInstance().level == null) {
            return null;
        }
        return Minecraft.getInstance().level.registryAccess();
    }

    public static ItemStack getSingleResult(ProcessingRecipe<?, ?> recipe) {
        RegistryAccess registryAccess = TFMGJeiConstants.registryAccess();
        ItemStack result = recipe.getRollableResultsAsItemStacks().getFirst();
        if (registryAccess != null)
            result = recipe.getResultItem(registryAccess);
        return result;
    }

    public static List<ItemStack> engineCylinders() {
        final List<ItemStack> engineCylinders = new ArrayList<>();

        Minecraft minecraft = Minecraft.getInstance();
        FeatureFlagSet features = Optional.ofNullable(minecraft.player)
                .map(p -> p.connection)
                .map(ClientPacketListener::enabledFeatures)
                .orElse(FeatureFlagSet.of());

        ClientLevel level = minecraft.level;
        if (level == null) {
            throw new NullPointerException("[TFMG Engine Cylinder Fetcher] minecraft.level must be set before JEI fetches ingredients");
        }

        for (Item item : BuiltInRegistries.ITEM) {
            if (item.isEnabled(features)) {
                if (!item.getDefaultInstance().is(TFMGTags.Items.ENGINE_CYLINDER.tag)) continue;
                engineCylinders.add(item.getDefaultInstance());
            }
        }

        return engineCylinders;
    }

    public static List<ItemStack> engineTurbines() {
        final List<ItemStack> engineTurbines = new ArrayList<>();

        Minecraft minecraft = Minecraft.getInstance();
        FeatureFlagSet features = Optional.ofNullable(minecraft.player)
                .map(p -> p.connection)
                .map(ClientPacketListener::enabledFeatures)
                .orElse(FeatureFlagSet.of());

        ClientLevel level = minecraft.level;
        if (level == null) {
            throw new NullPointerException("[TFMG Engine Turbine Fetcher] minecraft.level must be set before JEI fetches ingredients");
        }

        for (Item item : BuiltInRegistries.ITEM) {
            if (item.isEnabled(features)) {
                if (!item.getDefaultInstance().is(TFMGTags.Items.ENGINE_TURBINE.tag)) continue;
                engineTurbines.add(item.getDefaultInstance());
            }
        }

        return engineTurbines;
    }

    public static IDrawable asDrawable(AllGuiTextures texture) {
        return new IDrawable() {
            @Override
            public int getWidth() {
                return texture.getWidth();
            }

            @Override
            public int getHeight() {
                return texture.getHeight();
            }

            @Override
            public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
                texture.render(graphics, xOffset, yOffset);
            }
        };
    }
}
