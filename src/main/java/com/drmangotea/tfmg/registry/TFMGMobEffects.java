package com.drmangotea.tfmg.registry;


import com.drmangotea.tfmg.CreateTFMG;
import com.drmangotea.tfmg.base.effects.HellFireEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;

public class TFMGMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CreateTFMG.MOD_ID);

    public static final RegistryObject<MobEffect> HELLFIRE = MOB_EFFECTS.register("hellfire", () -> new HellFireEffect(MobEffectCategory.HARMFUL, new Color(150, 0, 0, 200).getRGB()));


    public static void register(IEventBus modEventBus){
        MOB_EFFECTS.register(modEventBus);
    }
}
