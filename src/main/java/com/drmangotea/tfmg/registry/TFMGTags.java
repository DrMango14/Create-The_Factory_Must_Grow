package com.drmangotea.tfmg.registry;


import com.drmangotea.tfmg.TFMG;

import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.core.registries.BuiltInRegistries;
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


import static com.drmangotea.tfmg.registry.TFMGTags.NameSpace.COMMON;
import static com.drmangotea.tfmg.registry.TFMGTags.NameSpace.MOD;
import static com.simibubi.create.AllTags.optionalTag;


public class TFMGTags {


    public enum NameSpace {

        MOD(TFMG.MOD_ID, false, true),
        COMMON("c")
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

        TFMGBlockTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        TFMGBlockTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? CreateLang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(BuiltInRegistries.BLOCK, id);
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
        IRON_PLATES(COMMON, "plates/iron"),
        ALUMINUM_PLATES(COMMON, "plates/aluminum")

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
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? CreateLang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(BuiltInRegistries.ITEM, id);
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
        AIR(NameSpace.COMMON),
        COOLING_FLUID(NameSpace.COMMON),

        GASOLINE(NameSpace.COMMON),
        DIESEL(NameSpace.COMMON),
        KEROSENE(NameSpace.COMMON),

        CREOSOTE(NameSpace.COMMON),
        FURNACE_GAS(NameSpace.COMMON),

        LPG(NameSpace.COMMON),
        HEAVY_OIL(NameSpace.COMMON),
        LUBRICATION_OIL(NameSpace.COMMON),
        NAPHTHA(NameSpace.COMMON),
        CRUDE_OIL(NameSpace.COMMON),
        MOLTEN_STEEL(NameSpace.COMMON),
        FUEL(NameSpace.COMMON)

        ;

        public final TagKey<Fluid> tag;
        public final boolean alwaysDatagen;



        TFMGFluidTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }


        TFMGFluidTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        TFMGFluidTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? CreateLang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(BuiltInRegistries.FLUID, id);
            } else {
                tag = FluidTags.create(id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }


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
       // TFMGItemTags.init();
        TFMGFluidTags.init();
        //TFMGEntityTags.init();
        //TFMGRecipeSerializerTags.init();
    }
}
