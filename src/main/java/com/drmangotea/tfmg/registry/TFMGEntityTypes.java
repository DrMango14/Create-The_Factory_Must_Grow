package com.drmangotea.tfmg.registry;


import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.spark.*;
import com.drmangotea.tfmg.content.items.weapons.advanced_potato_cannon.projectile.NapalmPotato;
import com.drmangotea.tfmg.content.items.weapons.advanced_potato_cannon.projectile.NapalmPotatoRenderer;
import com.drmangotea.tfmg.content.items.weapons.explosives.napalm.NapalmBombEntity;
import com.drmangotea.tfmg.content.items.weapons.explosives.napalm.NapalmBombRenderer;
import com.drmangotea.tfmg.content.items.weapons.explosives.pipe_bomb.PipeBomb;
import com.drmangotea.tfmg.content.items.weapons.explosives.pipe_bomb.PipeBombRenderer;
import com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades.ThermiteGrenade;
import com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades.ThermiteGrenadeRenderer;
import com.drmangotea.tfmg.content.items.weapons.lithium_blade.LithiumSpark;
import com.drmangotea.tfmg.content.items.weapons.lithium_blade.LithiumSparkRenderer;
import com.simibubi.create.foundation.data.CreateEntityBuilder;

import com.simibubi.create.foundation.utility.CreateLang;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class TFMGEntityTypes {






    public static final EntityEntry<PipeBomb> PIPE_BOMB =
            register("pipe_bomb", PipeBomb::new, () -> PipeBombRenderer::new,
                    MobCategory.MISC, 4, 20, true, true, PipeBomb::build).register();

    public static final EntityEntry<NapalmPotato> NAPALM_POTATO =
            register("napalm_potato", NapalmPotato::new, () -> NapalmPotatoRenderer::new,
                    MobCategory.MISC, 4, 20, true, true, NapalmPotato::build).register();

    public static final EntityEntry<ThermiteGrenade> THERMITE_GRENADE =
            register("thermite_grenade", ThermiteGrenade::new, () -> ThermiteGrenadeRenderer::regular,
                    MobCategory.MISC, 4, 20, true, true, ThermiteGrenade::build).register();
    public static final EntityEntry<ThermiteGrenade> ZINC_GRENADE =
            register("zinc_grenade", ThermiteGrenade::new, () -> ThermiteGrenadeRenderer::green,
                    MobCategory.MISC, 4, 20, true, true, ThermiteGrenade::build).register();
    public static final EntityEntry<ThermiteGrenade> COPPER_GRENADE =
            register("copper_grenade", ThermiteGrenade::new, () -> ThermiteGrenadeRenderer::blue,
                    MobCategory.MISC, 4, 20, true, true, ThermiteGrenade::build).register();
    public static final EntityEntry<NapalmBombEntity> NAPALM_BOMB =
            register("napalm_bomb_entity", NapalmBombEntity::new, () -> NapalmBombRenderer::new,
                    MobCategory.MISC, 4, 20, true, true, NapalmBombEntity::build).register();

    public static final EntityEntry<Spark> SPARK =
            register("spark", Spark::new, () -> SparkRenderer::new,
                    MobCategory.MISC, 4, 20, true, true, Spark::build).register();
    public static final EntityEntry<GreenSpark> GREEN_SPARK =
            register("green_spark", GreenSpark::new, () -> GreenSparkRenderer::new,
                    MobCategory.MISC, 4, 20, true, true, GreenSpark::build).register();
    public static final EntityEntry<BlueSpark> BLUE_SPARK =
            register("blue_spark", BlueSpark::new, () -> BlueSparkRenderer::new,
                    MobCategory.MISC, 4, 20, true, true, BlueSpark::build).register();

    public static final EntityEntry<LithiumSpark> LITHIUM_SPARK =
            register("lithium_spark", LithiumSpark::new, () -> LithiumSparkRenderer::new,
                    MobCategory.MISC, 80, 20, true, true, LithiumSpark::build).register();





    private static <T extends Entity> CreateEntityBuilder<T, ?> register(String name, EntityType.EntityFactory<T> factory,
                                                                         NonNullSupplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer,
                                                                         MobCategory group, int range, int updateFrequency, boolean sendVelocity, boolean immuneToFire,
                                                                         NonNullConsumer<EntityType.Builder<T>> propertyBuilder) {
        String id = CreateLang.asId(name);
        return (CreateEntityBuilder<T, ?>) TFMG.REGISTRATE
                .entity(id, factory, group)
                .properties(b -> b.setTrackingRange(range)
                        .setUpdateInterval(updateFrequency)
                        .setShouldReceiveVelocityUpdates(sendVelocity))
                .properties(propertyBuilder)
                .properties(b -> {
                    if (immuneToFire)
                        b.fireImmune();
                })
                .renderer(renderer);
    }



    public static void init(){

    }
}
