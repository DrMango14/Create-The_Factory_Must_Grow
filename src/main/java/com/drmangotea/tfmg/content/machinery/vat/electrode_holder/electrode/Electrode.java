package com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode;

import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Electrode {
    private String descriptionId;
    private final ResourceLocation id;
    private final ItemEntry<?> item;
    private final int resistance;
    private final String operationId;

    public Electrode(Properties properties) {
        this.id = properties.id;
        this.item = properties.item;
        this.resistance = properties.resistance;
        this.operationId = properties.operationId;
    }

    public ItemEntry<?> getItem() {
        return this.item;
    }

    public ItemStack getStack() {
        return getItem() != null ? getItem().asStack() : ItemStack.EMPTY;
    }

    public int getResistance() {
        return this.resistance;
    }

    public String getOperationId() {
        return this.operationId;
    }

    public void tick(VatBlockEntity controllerVat, Level level, BlockPos pos, boolean active, boolean clientTick) {

    }

    public String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("electrode", getKey());
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

        ItemEntry<?> item;
        int resistance = 0;
        String operationId = "";

        public Properties item(ItemEntry<?> item) {
            this.item = item;
            return this;
        }

        public Properties resistance(int resistance) {
            this.resistance = resistance;
            return this;
        }

        public Properties operationId(String operationId) {
            this.operationId = operationId;
            return this;
        }

        public Properties(ResourceLocation id) {
            this.id = id;
        }
    }
}
