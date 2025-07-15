package com.drmangotea.tfmg.content.electricity.connection.cable_type;

import com.drmangotea.tfmg.registry.TFMGItems;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CableType {
    private String descriptionId;
    private final ResourceLocation id;
    private final int color;
    private final ItemEntry<?> spool;

    public CableType(Properties properties) {
        this.id = properties.id;
        this.color = properties.color;
        this.spool = properties.spool;
    }

    public int getColor() {
        return this.color;
    }

    public ItemEntry<?> getSpool() {
        return this.spool;
    }

    public String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("cable_type", getKey());
        }

        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public Component getDisplayName() {
        return Component.translatable(this.getOrCreateDescriptionId());
    }

    public ResourceLocation getKey() {
        return this.id;
    }

    public static class Properties {
        private ResourceLocation id;

        int color = 0xffffff;
        ItemEntry<?> spool = TFMGItems.COPPER_SPOOL;

        public Properties color(int color) {
            this.color = color;
            return this;
        }

        public Properties spool(ItemEntry<?> spool) {
            this.spool = spool;
            return this;
        }

        public Properties(ResourceLocation id) {
            this.id = id;
        }
    }
}
