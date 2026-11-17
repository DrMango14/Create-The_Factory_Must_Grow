package com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.cable_type;

import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CableType {
    private String descriptionId;
    private final ResourceLocation id;
    private final int color;

    private final PartialModel model;

    private final ItemEntry<?> spool;
    private final ItemEntry<?> wire;

    public CableType(Properties properties) {
        this.id = properties.id;
        this.color = properties.color;
        this.spool = properties.spool;
        this.wire = properties.wire;
        this.model = properties.model;
    }

    public int getColor() {
        return this.color;
    }

    public ItemEntry<?> getSpool() {
        return this.spool;
    }

    public ItemEntry<?> getWire() {
        return this.wire;
    }

    public PartialModel getModel(){
        return model;
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

        PartialModel model = TFMGPartialModels.STEEL_REINFORCED_CABLE;

        int color = 0xffffff;
        ItemEntry<?> spool = TFMGItems.COPPER_SPOOL;
        ItemEntry<?> wire = TFMGItems.COPPER_WIRE;

        public Properties color(int color) {
            this.color = color;
            return this;
        }

        public Properties spool(ItemEntry<?> spool) {
            this.spool = spool;
            return this;
        }

        public Properties wire(ItemEntry<?> wire) {
            this.wire = wire;
            return this;
        }

        public Properties model(PartialModel model) {
            this.model = model;
            return this;
        }

        public Properties(ResourceLocation id) {
            this.id = id;
        }
    }
}
