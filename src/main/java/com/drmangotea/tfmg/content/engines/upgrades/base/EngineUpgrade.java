package com.drmangotea.tfmg.content.engines.upgrades.base;

import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.registry.*;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class EngineUpgrade {
    private String descriptionId;
    private final ResourceLocation id;

    public EngineUpgrade(ResourceLocation id) {
        this.id = id;
    }

    public Holder.Reference<EngineUpgrade> builtInRegistryHolder() {
        Optional<ResourceKey<EngineUpgrade>> resourceKey = TFMGRegistries.ENGINE_UPGRADE_REGISTRY.getResourceKey(this);
        return resourceKey.map(TFMGRegistries.ENGINE_UPGRADE_REGISTRY::getHolderOrThrow).orElseThrow();
    }

    public ResourceLocation getKey() {
        return this.id;
    }

    public String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("engine_upgrade", getKey());
        }
        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public Component getDisplayName() {
        return Component.translatable(this.getOrCreateDescriptionId());
    }

    public float getSpeedModifier(AbstractSmallEngineBlockEntity engine) {
        return 1;
    }
    public float getEfficiencyModifier(AbstractSmallEngineBlockEntity engine) {
        return 1;
    }
    public float getTorqueModifier(AbstractSmallEngineBlockEntity engine) {
        return 1;
    }

    public void updateUpgrade(AbstractSmallEngineBlockEntity be){}

    public void tickUpgrade(AbstractSmallEngineBlockEntity engine) {}
    public void lazyTickUpgrade(AbstractSmallEngineBlockEntity engine) {}

    public void render(AbstractSmallEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light) {}
	
	public void write (CompoundTag compound, String name) {
		TFMGRegistries.ENGINE_UPGRADE_REGISTRY.byNameCodec().encodeStart(NbtOps.INSTANCE, this)
			.ifSuccess(nbt -> compound.put(name, nbt));
	}
	
	public static Optional<EngineUpgrade> read (Tag tag) {
		return TFMGRegistries.ENGINE_UPGRADE_REGISTRY.byNameCodec().parse(NbtOps.INSTANCE, tag).result();
	}

    public static Optional<Holder<EngineUpgrade>> getUpgradeFromItem(ItemStack stack) {
        return Optional.ofNullable(stack.get(TFMGDataComponents.ENGINE_UPGRADE)).map(Stored::upgrade);
    }

	@NothingNullByDefault
    public record Stored(Holder<EngineUpgrade> upgrade) implements TooltipProvider {
        public static final Codec<EngineUpgrade.Stored> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                TFMGRegistries.ENGINE_UPGRADE_REGISTRY.holderByNameCodec().fieldOf("upgrade").forGetter(EngineUpgrade.Stored::upgrade)
        ).apply(instance, EngineUpgrade.Stored::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, EngineUpgrade.Stored> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.holderRegistry(TFMGRegistries.ENGINE_UPGRADE),
                EngineUpgrade.Stored::upgrade,
                EngineUpgrade.Stored::new
        );

        @Override
        public void addToTooltip(Item.TooltipContext ctx, Consumer<Component> tooltip, TooltipFlag flag) {
            tooltip.accept(TFMGLang.translateDirect("tooltip.engine_upgrade").withStyle(ChatFormatting.AQUA));
        }
    }
}
