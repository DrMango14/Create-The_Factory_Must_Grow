package com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode;

import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.content.machinery.vat.MultiUseAttachment;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatOperationEntry;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.ElectrodeHolderBlockEntity;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGElectrodes;
import com.drmangotea.tfmg.registry.TFMGVatOperations;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.Util;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class Electrode implements MultiUseAttachment<ElectrodeHolderBlockEntity> {
    private String descriptionId;
    private final ResourceLocation id;
    private final int resistance;
    private final VatOperationEntry operation;

    public Electrode(Properties properties) {
        this.id = properties.id;
        this.resistance = properties.resistance;
        this.operation = properties.operation;
    }

    public int getResistance() {
        return this.resistance;
    }

    public VatOperationEntry getOperation() {
        return this.operation;
    }

    @Override
    public void renderInVat(ElectrodeHolderBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, @Nullable ItemRenderer itemRenderer) {
        if (be.getLevel() == null || itemRenderer == null || be.inventory.isEmpty())
            return;
        ms.pushPose();
        ms.mulPose(Axis.XP.rotationDegrees(0));
        ms.translate(0.5, -1.4369, 0.5);
        ms.scale(3.33f, 3.33f, 3.33f);
        itemRenderer.renderStatic(be.inventory.getStackInSlot(0), ItemDisplayContext.GROUND, LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().below()), overlay, ms, buffer, be.getLevel(), 0);
        ms.popPose();
    }

    @Override
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
        private final ResourceLocation id;

        int resistance = 0;
        VatOperationEntry operation = TFMGVatOperations.NONE;

        public Properties resistance(int resistance) {
            this.resistance = resistance;
            return this;
        }

        public Properties operationId(VatOperationEntry operation) {
            this.operation = operation;
            return this;
        }

        public Properties(ResourceLocation id) {
            this.id = id;
        }
    }

    public record Stored(Holder<Electrode> electrode) {
        public static final Codec<Stored> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                TFMGRegistries.ELECTRODE_REGISTRY.holderByNameCodec().fieldOf("electrode").forGetter(Stored::electrode)
        ).apply(instance, Stored::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, Stored> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.holderRegistry(TFMGRegistries.ELECTRODE),
                Stored::electrode,
                Stored::new
        );

        public static final Stored NONE = new Stored(TFMGElectrodes.NONE);
    }
}
