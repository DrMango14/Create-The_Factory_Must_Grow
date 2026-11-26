package com.drmangotea.tfmg.config.common;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.world.placement_modifier.BooleanConfigPlacementModifier;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFMGWorldGen extends ConfigBase {
    // bump this version to reset configured values.
    private static final int VERSION = 1;

    public static final List<ResourceLocation> DEFAULT_TOGGLES = List.of(
            TFMG.asResource("oil_deposits"), TFMG.asResource("oil_wells"), TFMG.asResource("lead_ore"),
            TFMG.asResource("nickel_ore"), TFMG.asResource("lithium_ore"), TFMG.asResource("bauxite"),
            TFMG.asResource("galena"), TFMG.asResource("lignite"), TFMG.asResource("fireclay"),
            TFMG.asResource("sulfur"), TFMG.asResource("nether_fireclay")
    );

    protected final Map<ResourceLocation, ModConfigSpec.ConfigValue<Boolean>> toggles = new HashMap<>();

    @Override
    public void registerAll(ModConfigSpec.Builder builder) {
        builder.push("toggles");
        builder.comment(Comments.toggles);
        for (ResourceLocation key : DEFAULT_TOGGLES) {
            toggles.put(key, builder.define(key.getPath(), true));
        }
        builder.pop();
        DEFAULT_TOGGLES.forEach(key -> BooleanConfigPlacementModifier.OPTIONS.put(key, () -> toggles.get(key).get()));
        super.registerAll(builder);
    }

    public final ConfigGroup oilDeposit = group(1, "oilDeposits", "Oil Deposits");
    public final ConfigInt depositMaxReserves = i(10000, 1000, "depositMaxReserves", Comments.depositMaxReserves);
    public final ConfigBool infiniteDeposits = b(false, "infiniteDeposits", Comments.infiniteDeposits);

    @Override
    public @NotNull String getName() {
        return "worldgen.v" + VERSION;
    }

    private static class Comments {
        static String toggles = "Toggle individual world generation features.";

        static String depositMaxReserves = "Sets the maximum oil reserves a reservoir can have.";
        static String infiniteDeposits = "Makes oil reservoirs bottomless.";
    }
}
