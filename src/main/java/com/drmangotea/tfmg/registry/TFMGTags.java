package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import static com.drmangotea.tfmg.registry.TFMGTags.NameSpace.*;


public class TFMGTags {


    public enum NameSpace {
        MOD(TFMG.MOD_ID),
        COMMON("c")
        ;

        public final String id;
        NameSpace(String id) {
            this.id = id;
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

        TFMGBlockTags() {
            this(MOD);
        }
        TFMGBlockTags(NameSpace namespace) {
            this(namespace, null);
        }
        TFMGBlockTags(NameSpace namespace, String path) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? TFMGLang.asId(name()) : path);
            this.tag = BlockTags.create(id);
        }
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

        TFMGItemTags() {
            this(NameSpace.MOD);
        }
        TFMGItemTags(NameSpace namespace) {
            this(namespace, null);
        }
        TFMGItemTags(NameSpace namespace, String path) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? TFMGLang.asId(name()) : path);
            this.tag = ItemTags.create(id);
        }
    }
    public enum TFMGFluidTags {
        GAS,

        FLAMMABLE,
        FIREBOX_FUEL,
        BLAST_STOVE_FUEL,
        AIR(COMMON),
        COOLING_FLUID(COMMON),

        GASOLINE(COMMON),
        DIESEL(COMMON),
        KEROSENE(COMMON),

        CREOSOTE(COMMON),
        FURNACE_GAS(COMMON),

        LPG(COMMON),
        HEAVY_OIL(COMMON),
        LUBRICATION_OIL(COMMON),
        NAPHTHA(COMMON),
        CRUDE_OIL(COMMON),
        MOLTEN_STEEL(COMMON),
        FUEL(COMMON)

        ;

        public final TagKey<Fluid> tag;

        TFMGFluidTags() {
            this(NameSpace.MOD);
        }
        TFMGFluidTags(NameSpace namespace) {
            this(namespace, null);
        }
        TFMGFluidTags(NameSpace namespace, String path) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? TFMGLang.asId(name()) : path);
            this.tag = FluidTags.create(id);
        }
    }
}
