package com.drmangotea.tfmg.registry;


import com.drmangotea.tfmg.TFMG;
import com.simibubi.create.AllTags;

import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collections;

import static com.drmangotea.tfmg.registry.TFMGTags.NameSpace.FORGE;
import static com.drmangotea.tfmg.registry.TFMGTags.NameSpace.MOD;



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

        MOD(TFMG.MOD_ID, false, true),
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

    public enum TFMGBlockTags {
        BLAST_FURNACE_WALL,
        REINFORCED_BLAST_FURNACE_WALL,
        BLAST_FURNACE_SUPPORT,
        INDUSTRIAL_PIPE,
        REINFORCED_BLAST_FURNACE_SUPPORT,
        SURFACE_SCANNER_FINDABLE,
        PUMPJACK_PART,
        PUMPJACK_HEAD,
        PUMPJACK_CONNECTOR,
        PUMPJACK_SMALL_PART
        ;

        public final TagKey<Block> tag;
        public final boolean alwaysDatagen;

        TFMGBlockTags() {
            this(MOD);
        }

        TFMGBlockTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        TFMGBlockTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        TFMGBlockTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        TFMGBlockTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? CreateLang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(ForgeRegistries.BLOCKS, id);
            } else {
                tag = BlockTags.create(id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }

        @SuppressWarnings("deprecation")
        public boolean matches(Block block) {
            return block.builtInRegistryHolder()
                    .is(tag);
        }

        public boolean matches(ItemStack stack) {
            return stack != null && stack.getItem() instanceof BlockItem blockItem && matches(blockItem.getBlock());
        }

        public boolean matches(BlockState state) {
            return state.is(tag);
        }

        private static void init() {}

    }
    public enum TFMGItemTags {

        FLUX,
        SPOOLS,
        BLAST_FURNACE_FUEL,
        RODS,
        IRON_PLATES(FORGE, "plates/iron"),
        ALUMINUM_PLATES(FORGE, "plates/aluminum")

        ;

        public final TagKey<Item> tag;
        public final boolean alwaysDatagen;

        TFMGItemTags() {
            this(NameSpace.MOD);
        }

        TFMGItemTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        TFMGItemTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        TFMGItemTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        TFMGItemTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? CreateLang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(ForgeRegistries.ITEMS, id);
            } else {
                tag = ItemTags.create(id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }

        @SuppressWarnings("deprecation")
        public boolean matches(Item item) {
            return item.builtInRegistryHolder()
                    .is(tag);
        }

        public boolean matches(ItemStack stack) {
            return stack.is(tag);
        }

        private static void init() {}

    }
    public enum TFMGFluidTags {
        GAS(MOD),

        FLAMMABLE(MOD),
        FIREBOX_FUEL(MOD),
        BLAST_STOVE_FUEL(MOD),
        AIR(NameSpace.FORGE),
        COOLING_FLUID(NameSpace.FORGE),

        GASOLINE(NameSpace.FORGE),
        DIESEL(NameSpace.FORGE),
        KEROSENE(NameSpace.FORGE),

        CREOSOTE(NameSpace.FORGE),
        FURNACE_GAS(NameSpace.FORGE),

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
            this(MOD);
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
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? CreateLang.asId(name()) : path);
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
    

    public static void init() {
        TFMGBlockTags.init();
       // TFMGItemTags.register();
        TFMGFluidTags.init();
        //TFMGEntityTags.register();
        //TFMGRecipeSerializerTags.register();
    }
}
