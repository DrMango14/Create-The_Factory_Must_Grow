package com.drmangotea.tfmg.recipes.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.conditions.ICondition;

public record TagFilledCondition(TagKey<Item> tag) implements ICondition {
    public static final MapCodec<TagFilledCondition> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(ResourceLocation.CODEC.xmap((loc) -> TagKey.create(Registries.ITEM, loc), TagKey::location).fieldOf("tag").forGetter(TagFilledCondition::tag)).apply(builder, TagFilledCondition::new));

    public TagFilledCondition(String location) {
        this(ResourceLocation.parse(location));
    }

    public TagFilledCondition(String namespace, String path) {
        this(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    public TagFilledCondition(ResourceLocation tag) {
        this(TagKey.create(Registries.ITEM, tag));
    }

    @Override
    public boolean test(IContext context) {
        return !context.getTag(this.tag).isEmpty();
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    public String toString() {
        return "tag_filled(\"" + this.tag.location() + "\")";
    }
}
