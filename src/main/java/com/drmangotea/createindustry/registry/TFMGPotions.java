package com.drmangotea.createindustry.registry;

import com.drmangotea.createindustry.CreateTFMG;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class TFMGPotions {
    public static final LazyRegistrar<Potion> POTIONS
            = LazyRegistrar.create(Registry.POTION, CreateTFMG.MOD_ID);

    public static final RegistryObject<Potion> HELLFIRE_POTION = POTIONS.register("hellfire_potion",
            () -> new Potion(new MobEffectInstance(TFMGMobEffects.HELLFIRE.get(), 600, 0)));

    public static void register() {
        POTIONS.register();
    }

}