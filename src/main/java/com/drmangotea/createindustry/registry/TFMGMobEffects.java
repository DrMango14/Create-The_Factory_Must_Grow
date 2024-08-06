package com.drmangotea.createindustry.registry;


import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.effects.HellFireEffect;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.awt.*;

public class TFMGMobEffects {
    public static final LazyRegistrar<MobEffect> MOB_EFFECTS = LazyRegistrar.create(Registry.MOB_EFFECT, CreateTFMG.MOD_ID);

    public static final RegistryObject<MobEffect> HELLFIRE = MOB_EFFECTS.register("hellfire", () -> new HellFireEffect(MobEffectCategory.HARMFUL, new Color(150, 0, 0, 200).getRGB()));


    public static void register(){
        MOB_EFFECTS.register();
    }
}
