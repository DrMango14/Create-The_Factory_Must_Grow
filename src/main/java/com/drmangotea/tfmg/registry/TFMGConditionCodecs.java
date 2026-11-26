package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.recipes.condition.TagFilledCondition;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class TFMGConditionCodecs {
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS = DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, TFMG.MOD_ID);

    public static final Supplier<MapCodec<TagFilledCondition>> TAG_FILLED = CONDITION_CODECS.register("tag_filled", () -> TagFilledCondition.CODEC);

    public static void register(IEventBus modEventBus){
        CONDITION_CODECS.register(modEventBus);
    }
}
