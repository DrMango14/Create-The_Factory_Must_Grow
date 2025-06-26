package com.drmangotea.tfmg.content.electricity.connection.cable_type;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.config.TFMGServerConfig;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.SpoolItem;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.minecraft.ChatFormatting.GRAY;

public class CableTypeStats implements TooltipModifier {
    protected final SpoolItem cableItem;

    public CableTypeStats(SpoolItem cableItem) {
        this.cableItem = cableItem;
    }

    @Nullable
    public static CableTypeStats create(Item item) {
        if (item instanceof SpoolItem cableItem) {
            if (Objects.equals(cableItem.cableTypeKey, TFMG.asResource("empty"))) return null;
            return new CableTypeStats(cableItem);
        }
        return null;
    }

    @Override
    public void modify(ItemTooltipEvent context) {
        List<Component> conductorStats = getCableTypeStats(cableItem, context.getEntity());
        if (!conductorStats.isEmpty()) {
            List<Component> tooltip = context.getToolTip();
            tooltip.add(CommonComponents.EMPTY);
            tooltip.addAll(conductorStats);
        }
    }

    public static List<Component> getCableTypeStats(SpoolItem spoolItem, Player player) {
        List<Component> list = new ArrayList<>();

        TFMGServerConfig config = TFMGConfigs.server();

        boolean hasGoggles = GogglesItem.isWearingGoggles(player);

        CreateLang.translate("tooltip.resistivity")
                .style(GRAY)
                .addTo(list);
        double resistivity = ResistivityValues.getResistivity(TFMGUtils.getCableType(spoolItem.cableTypeKey));

        LangBuilder builder = CreateLang.builder();

        builder.space().add(CreateLang.text(resistivity + "Ω")).style(ChatFormatting.GOLD).addTo(list);

        return list;
    }

    enum Resistivity {
        VERY_LOW(ChatFormatting.RED, ChatFormatting.GOLD),
        LOW(ChatFormatting.GOLD, ChatFormatting.YELLOW),
        MEDIUM(ChatFormatting.YELLOW, ChatFormatting.GREEN),
        HIGH(ChatFormatting.GREEN, ChatFormatting.DARK_GREEN)
        ;

        private final ChatFormatting absoluteColor;
        private final ChatFormatting relativeColor;

        Resistivity(ChatFormatting absoluteColor, ChatFormatting relativeColor) {
            this.absoluteColor = absoluteColor;
            this.relativeColor = relativeColor;
        }

        public ChatFormatting getAbsoluteColor() {
            return absoluteColor;
        }

        public ChatFormatting getRelativeColor() {
            return relativeColor;
        }
    }
}
