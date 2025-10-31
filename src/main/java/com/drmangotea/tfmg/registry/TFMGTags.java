package com.drmangotea.tfmg.registry;


import com.drmangotea.tfmg.TFMG;

import com.drmangotea.tfmg.base.lang.TFMGLang;
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


public class TFMGTags {


    public enum NameSpace {

        MOD(TFMG.MOD_ID, true),
        COMMON("c")
        ;

        public final String id;
        public final boolean alwaysDatagenDefault;

        NameSpace(String id) {
            this(id, false);
        }

        NameSpace(String id, boolean alwaysDatagenDefault) {
            this.id = id;
            this.alwaysDatagenDefault = alwaysDatagenDefault;
        }
    }

    public enum TFMGBlockTags {
        BLAST_FURNACE_SUPPORT,
        BLAST_FURNACE_WALL,
        INDUSTRIAL_PIPE,
        ORES_LITHIUM(COMMON, "ores/lithium"),
        PUMPJACK_CONNECTOR,
        PUMPJACK_HEAD,
        PUMPJACK_PART,
        PUMPJACK_SMALL_PART,
        RAW_LITHIUM(COMMON, "raw_materials/lithium"),
        REINFORCED_BLAST_FURNACE_SUPPORT,
        REINFORCED_BLAST_FURNACE_WALL,
        STORAGE_BLOCKS_CAST_IRON(COMMON, "storage_blocks/cast_iron"),
        STORAGE_BLOCKS_COAL_COKE(COMMON, "storage_blocks/coal_coke"),
        STORAGE_BLOCKS_LITHIUM(COMMON, "storage_blocks/lithium"),
        STORAGE_BLOCKS_PLASTIC(COMMON, "storage_blocks/plastic"),
        STORAGE_BLOCKS_RAW_LITHIUM(COMMON, "storage_blocks/raw_lithium"),
        SURFACE_SCANNER_FINDABLE,
        ;

        public final TagKey<Block> tag;
        public final boolean alwaysDatagen;

        TFMGBlockTags() {
            this(MOD);
        }

        TFMGBlockTags(NameSpace namespace) {
            this(namespace, namespace.alwaysDatagenDefault);
        }

        TFMGBlockTags(NameSpace namespace, boolean alwaysDatagen) {
            this(namespace, null, alwaysDatagen);
        }

        TFMGBlockTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.alwaysDatagenDefault);
        }

        TFMGBlockTags(NameSpace namespace, String path, boolean alwaysDatagen) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? TFMGLang.asId(name()) : path);
            this.tag = BlockTags.create(id);
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
        BLAST_FURNACE_FUEL,
        DUSTS_COAL_COKE(COMMON, "dusts/coal_coke"),
        DUSTS_IRON(COMMON, "dusts/iron"),
        DUSTS_SALTPETER(COMMON, "dusts/saltpeter"),
        DUSTS_SULFUR(COMMON, "dusts/sulfur"),
        FLUX,
        INGOTS_CAST_IRON(COMMON, "ingots/cast_iron"),
        INGOTS_LITHIUM(COMMON, "ingots/lithium"),
        INGOTS_PLASTIC(COMMON, "ingots/plastic"),
        INGOTS_RUBBER(COMMON, "ingots/rubber"),
        INGOTS_SILICON(COMMON, "ingots/silicon"),
        NUGGETS_CAST_IRON(COMMON, "nuggets/cast_iron"),
        NUGGETS_LITHIUM(COMMON, "nuggets/lithium"),
        ORES_LITHIUM(COMMON, "ores/lithium"),
        PLATES_CAST_IRON(COMMON, "plates/cast_iron"),
        RAW_LITHIUM(COMMON, "raw_materials/lithium"),
        RODS_STEEL(COMMON, "rots/steel"),
        SPOOLS,
        STORAGE_BLOCKS_CAST_IRON(COMMON, "storage_blocks/cast_iron"),
        STORAGE_BLOCKS_COAL_COKE(COMMON, "storage_blocks/coal_coke"),
        STORAGE_BLOCKS_LITHIUM(COMMON, "storage_blocks/lithium"),
        STORAGE_BLOCKS_PLASTIC(COMMON, "storage_blocks/plastic"),
        STORAGE_BLOCKS_RAW_LITHIUM(COMMON, "storage_blocks/raw_lithium"),
        WIRES(COMMON),
        WIRES_ALUMINUM(COMMON, "wires/aluminum"),
        WIRES_CONSTANTAN(COMMON, "wires/constantan"),
        WIRES_COPPER(COMMON, "wires/copper"),
        ;

        public final TagKey<Item> tag;
        public final boolean alwaysDatagen;

        TFMGItemTags() {
            this(NameSpace.MOD);
        }

        TFMGItemTags(NameSpace namespace) {
            this(namespace, namespace.alwaysDatagenDefault);
        }

        TFMGItemTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.alwaysDatagenDefault);
        }

        TFMGItemTags(NameSpace namespace, boolean alwaysDatagen) {
            this(namespace, null, alwaysDatagen);
        }

        TFMGItemTags(NameSpace namespace, String path, boolean alwaysDatagen) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? TFMGLang.asId(name()) : path);
            this.tag = ItemTags.create(id);
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
            this(namespace, namespace.alwaysDatagenDefault);
        }


        TFMGFluidTags(NameSpace namespace, boolean alwaysDatagen) {
            this(namespace, null, alwaysDatagen);
        }

        TFMGFluidTags(NameSpace namespace, String path, boolean alwaysDatagen) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? TFMGLang.asId(name()) : path);
            this.tag = FluidTags.create(id);
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
