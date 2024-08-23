package com.drmangotea.createindustry.registry;


import com.drmangotea.createindustry.CreateTFMG;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collections;

import static com.drmangotea.createindustry.registry.TFMGTags.NameSpace.FORGE;
import static com.drmangotea.createindustry.registry.TFMGTags.NameSpace.MOD;


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
        GAS(MOD),

        FLAMMABLE(MOD),

        GASOLINE(FORGE),
        DIESEL(FORGE),
        KEROSENE(FORGE),

        LPG(FORGE),
        HEAVY_OIL(FORGE),
        LUBRICATION_OIL(FORGE),
        NAPHTHA(FORGE),

        CRUDE_OIL(FORGE),

        MOLTEN_STEEL(FORGE),

        FUEL(FORGE)


        ;

        public final TagKey<Fluid> tag;
        public final boolean alwaysDatagen;

        TFMGFluidTags() {
            this(MOD);
        }

        TFMGFluidTags(TFMGTags.NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        TFMGFluidTags(TFMGTags.NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        TFMGFluidTags(TFMGTags.NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        TFMGFluidTags(TFMGTags.NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
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

    public enum TFMGEntityTags {

        BLAZE_BURNER_CAPTURABLE,
        IGNORE_SEAT,

        ;

        public final TagKey<EntityType<?>> tag;
        public final boolean alwaysDatagen;

        TFMGEntityTags() {
            this(MOD);
        }

        TFMGEntityTags(TFMGTags.NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        TFMGEntityTags(TFMGTags.NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        TFMGEntityTags(TFMGTags.NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        TFMGEntityTags(TFMGTags.NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(ForgeRegistries.ENTITY_TYPES, id);
            } else {
                tag = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }

        public boolean matches(EntityType<?> type) {
            return type.is(tag);
        }

        public boolean matches(Entity entity) {
            return matches(entity.getType());
        }

        private static void init() {}

    }

    public enum TFMGRecipeSerializerTags {

        AUTOMATION_IGNORE,

        ;

        public final TagKey<RecipeSerializer<?>> tag;
        public final boolean alwaysDatagen;

        TFMGRecipeSerializerTags() {
            this(MOD);
        }

        TFMGRecipeSerializerTags(TFMGTags.NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        TFMGRecipeSerializerTags(TFMGTags.NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        TFMGRecipeSerializerTags(TFMGTags.NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        TFMGRecipeSerializerTags(TFMGTags.NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(ForgeRegistries.RECIPE_SERIALIZERS, id);
            } else {
                tag = TagKey.create(Registry.RECIPE_SERIALIZER_REGISTRY, id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }

        public boolean matches(RecipeSerializer<?> recipeSerializer) {
            return ForgeRegistries.RECIPE_SERIALIZERS.getHolder(recipeSerializer).orElseThrow().is(tag);
        }

        private static void init() {}
    }
    
    public enum TFMGBlockTags {
        AIR_INTAKE_TRANSPARENT,
        ;
        
        public final TagKey<Block> tag;
        public final boolean alwaysDatagen;
        
        TFMGBlockTags() {
            this(MOD);
        }
        
        TFMGBlockTags(TFMGTags.NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }
        
        TFMGBlockTags(TFMGTags.NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }
        
        TFMGBlockTags(TFMGTags.NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }
        
        TFMGBlockTags(TFMGTags.NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(ForgeRegistries.BLOCKS, id);
            } else {
                tag = BlockTags.create(id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }
        
        public boolean matches(BlockState state) {
            return state.is(tag);
        }
        
        private static void init() {}

    }

    public static void init() {
        TFMGBlockTags.init();
      //  TFMGItemTags.init();
        TFMGFluidTags.init();
        TFMGEntityTags.init();
        TFMGRecipeSerializerTags.init();
    }
}
