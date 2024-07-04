package com.drmangotea.tfmg.registry;



import com.drmangotea.tfmg.CreateTFMG;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collections;


public class TFMGTags {
    public static <T> TagKey<T> optionalTag(IForgeRegistry<T> registry,
                                            ResourceLocation id) {
        return registry.tags()
                .createOptionalTagKey(id, Collections.emptySet());
    }

    public static <T> TagKey<T> forgeTag(IForgeRegistry<T> registry, String path) {
        return optionalTag(registry, new ResourceLocation("forge", path));
    }

    public static TagKey<Block> forgeBlockTag(String path) {
        return forgeTag(ForgeRegistries.BLOCKS, path);
    }

    public static TagKey<Item> forgeItemTag(String path) {
        return forgeTag(ForgeRegistries.ITEMS, path);
    }

    public static TagKey<Fluid> forgeFluidTag(String path) {
        return forgeTag(ForgeRegistries.FLUIDS, path);
    }

    public enum NameSpace {

        MOD(CreateTFMG.MOD_ID, false, true),
        FORGE("forge")


        ;

        public final String id;
        public final boolean optionalDefault;
        public final boolean alwaysDatagenDefault;

        NameSpace(String id) {
            this(id, true, false);
        }

        NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
            this.id = id;
            this.optionalDefault = optionalDefault;
            this.alwaysDatagenDefault = alwaysDatagenDefault;
        }
    }
    

    public enum TFMGFluidTags {
        GAS(NameSpace.MOD),

        FLAMMABLE(NameSpace.MOD),
        GASOLINE(NameSpace.FORGE),
        DIESEL(NameSpace.FORGE),
        KEROSENE(NameSpace.FORGE),

        LPG(NameSpace.FORGE),
        HEAVY_OIL(NameSpace.FORGE),
        LUBRICATION_OIL(NameSpace.FORGE),
        NAPHTHA(NameSpace.FORGE),

        CRUDE_OIL(NameSpace.FORGE),

        MOLTEN_STEEL(NameSpace.FORGE),

        FUEL(NameSpace.FORGE)


        ;

        public final TagKey<Fluid> tag;
        public final boolean alwaysDatagen;

        TFMGFluidTags() {
            this(NameSpace.MOD);
        }

        TFMGFluidTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        TFMGFluidTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        TFMGFluidTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        TFMGFluidTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(ForgeRegistries.FLUIDS, id);
            } else {
                tag = FluidTags.create(id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }

        @SuppressWarnings("deprecation")
        public boolean matches(Fluid fluid) {
            return fluid.is(tag);
        }

        public boolean matches(FluidState state) {
            return state.is(tag);
        }

        private static void init() {}

    }

    //public enum TFMGEntityTags {
//
    //    BLAZE_BURNER_CAPTURABLE,
    //    IGNORE_SEAT,
//
    //    ;
//
    //    public final TagKey<EntityType<?>> tag;
    //    public final boolean alwaysDatagen;
//
    //    TFMGEntityTags() {
    //        this(NameSpace.MOD);
    //    }
//
    //    TFMGEntityTags(NameSpace namespace) {
    //        this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
    //    }
//
    //    TFMGEntityTags(NameSpace namespace, String path) {
    //        this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
    //    }
//
    //    TFMGEntityTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
    //        this(namespace, null, optional, alwaysDatagen);
    //    }
//
    //    TFMGEntityTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
    //        ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
    //        if (optional) {
    //            tag = optionalTag(ForgeRegistries.ENTITY_TYPES, id);
    //        } else {
    //            tag = TagKey.create(Registries.RECIPE_TYPE, id);
    //        }
    //        this.alwaysDatagen = alwaysDatagen;
    //    }
//
    //    public boolean matches(EntityType<?> type) {
    //        return type.is(tag);
    //    }
//
    //    public boolean matches(Entity entity) {
    //        return matches(entity.getType());
    //    }
//
    //    private static void init() {}
//
    //}

    public enum TFMGRecipeSerializerTags {

        AUTOMATION_IGNORE,

        ;

        public final TagKey<RecipeSerializer<?>> tag;
        public final boolean alwaysDatagen;

        TFMGRecipeSerializerTags() {
            this(NameSpace.MOD);
        }

        TFMGRecipeSerializerTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        TFMGRecipeSerializerTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        TFMGRecipeSerializerTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        TFMGRecipeSerializerTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(ForgeRegistries.RECIPE_SERIALIZERS, id);
            } else {
                tag = TagKey.create(Registries.RECIPE_SERIALIZER, id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }

        public boolean matches(RecipeSerializer<?> recipeSerializer) {
            return ForgeRegistries.RECIPE_SERIALIZERS.getHolder(recipeSerializer).orElseThrow().is(tag);
        }

        private static void init() {}
    }

    public static void init() {
      //  TFMGBlockTags.init();
      //  TFMGItemTags.init();
        TFMGFluidTags.init();
     //   TFMGEntityTags.init();
        TFMGRecipeSerializerTags.init();
    }
}
