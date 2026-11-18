package com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.cable_type;

import com.drmangotea.tfmg.content.machinery.misc.winding_machine.SpoolItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class CableType {
    private String descriptionId;
    private final ResourceLocation id;
    private final int color;
	private final PartialModel model;
	private final Supplier<SpoolItem> spool;
    private final Supplier<Item> wire;

    public CableType(Properties properties) {
        //properties.verify();
        this.id = properties.id;
        this.color = properties.color;
        this.spool = properties.spool;
        this.wire = properties.wire;
        this.model = properties.model;
    }

    public int getColor() {
        return this.color;
    }

    public Supplier<SpoolItem> getSpool() {
        return this.spool;
    }

    public Supplier<Item> getWire() {
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
        private final ResourceLocation id;

        PartialModel model = null;
		
		private double resistance = Double.MAX_VALUE;
		private int color = 0xffffff;
        private Supplier<SpoolItem> spool = null;
        private Supplier<Item> wire = null;

        public Properties color(int color) {
            this.color = color;
            return this;
        }

        public Properties spool(Supplier<SpoolItem> spool) {
            this.spool = spool;
            return this;
        }

        public Properties wire(Supplier<Item> wire) {
            this.wire = wire;
            return this;
        }

        public Properties spool(ItemEntry<SpoolItem> spool) {
            this.spool = spool;
            return this;
        }

        public Properties wire(ItemEntry<?> wire) {
            this.wire = wire::asItem;
            return this;
        }

        public Properties resistance(double resistance) {
            this.resistance = resistance;
            return this;
        }

        public Properties model(PartialModel model) {
            this.model = model;
            return this;
        }

        public Properties(ResourceLocation id) {
            this.id = id;
        }

        public void verify() throws IllegalStateException {
            if (this.spool == null) {
                throw new IllegalStateException("Spool item for " + this.id + " is not set");
            }

            if (this.wire == null) {
                throw new IllegalStateException("Wire item for " + this.id + " is not set");
            }

            if (Double.isNaN(this.resistance)) {
				throw new IllegalStateException("Resistance for " + this.id + " is not set");
            }
        }
    }
}
