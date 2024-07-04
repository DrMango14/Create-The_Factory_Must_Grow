package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.CreateTFMG;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TFMGPotions {
    public static final DeferredRegister<Potion> POTIONS
            = DeferredRegister.create(ForgeRegistries.POTIONS, CreateTFMG.MOD_ID);

    public static final RegistryObject<Potion> HELLFIRE_POTION = POTIONS.register("hellfire_potion",
            () -> new Potion(new MobEffectInstance(TFMGMobEffects.HELLFIRE.get(), 600, 0)));



    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }

}