package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.engines.fuels.EngineFuelType;
import com.drmangotea.tfmg.content.engines.types.EngineType;
import com.drmangotea.tfmg.content.items.weapons.flamethrover.FlamethrowerFuelType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import static com.drmangotea.tfmg.registry.TFMGTags.NameSpace.COMMON;
import static com.drmangotea.tfmg.registry.TFMGTags.NameSpace.MOD;


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

    public enum Blocks {
        BLAST_FURNACE_SUPPORT,
        BLAST_FURNACE_WALL,
		NON_DIAGONAL_WALLS("diagonalwalls","non_diagonal_walls"),
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
        BLAST_FURNACE_MELTS,
        CARRYON_BLOCK_BLACKLIST("carryon", "block_blacklist")
        ;

        public final TagKey<Block> tag;

        Blocks() {
            this(MOD);
        }
        Blocks(NameSpace namespace) {
            this(namespace, null);
        }
		Blocks(NameSpace namespace, String path) {
			ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? TFMGLang.asId(name()) : path);
			this.tag = BlockTags.create(id);
		}
		Blocks(String namespace, String path) {
			this.tag = BlockTags.create(ResourceLocation.fromNamespaceAndPath(namespace, path));
		}
    }

    public enum Items {
        BLAST_FURNACE_FUEL,
        DUSTS_COAL_COKE(COMMON, "dusts/coal_coke"),
        DUSTS_IRON(COMMON, "dusts/iron"),
        DUSTS_SALTPETER(COMMON, "dusts/saltpeter"),
        DUSTS_SULFUR(COMMON, "dusts/sulfur"),
        DUSTS_BAUXITE(COMMON, "dusts/bauxite"),
        FLUX,
        INGOTS_CAST_IRON(COMMON, "ingots/cast_iron"),
        INGOTS_LITHIUM(COMMON, "ingots/lithium"),
        INGOTS_PLASTIC(COMMON, "ingots/plastic"),
        INGOTS_RUBBER(COMMON, "ingots/rubber"),
        INGOTS_SILICON(COMMON, "ingots/silicon"),
        NUGGETS_CAST_IRON(COMMON, "nuggets/cast_iron"),
        NUGGETS_LITHIUM(COMMON, "nuggets/lithium"),
        ORES_LEAD(COMMON, "ores/lead"),
        ORES_NICKEL(COMMON, "ores/nickel"),
        ORES_LITHIUM(COMMON, "ores/lithium"),
		PLATES_CAST_IRON(COMMON, "plates/cast_iron"),
		PLATES_PLASTIC(COMMON, "plates/plastic"),
        RAW_LITHIUM(COMMON, "raw_materials/lithium"),
        RODS_STEEL(COMMON, "rods/steel"),
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
        ENGINE_TURBINE(MOD, "engine/turbine"),
        ENGINE_CYLINDER(MOD, "engine/cylinder"),
        MULTIMETERS,
        CAUTION_BLOCKS,
		CURIOS_BELT("curios","belt")
        ;

        public final TagKey<Item> tag;

        Items() {
            this(NameSpace.MOD);
        }
        Items(NameSpace namespace) {
            this(namespace, null);
        }
		Items(NameSpace namespace, String path) {
			ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? TFMGLang.asId(name()) : path);
			this.tag = ItemTags.create(id);
		}
		Items(String namespace, String path) {
			ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, path == null ? TFMGLang.asId(name()) : path);
			this.tag = ItemTags.create(id);
		}
    }

    public enum Fluids {
        GAS,

        FLAMMABLE,
        FIREBOX_FUEL,
        BLAST_STOVE_FUEL,
        AIR(COMMON),
        COOLING_FLUID(COMMON),
        HOT_AIR,
		EXHAUSTABLE,

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

        FUEL(COMMON),
        ETHYLENE(COMMON),
        PROPYLENE(COMMON),

        BUTANE(COMMON),
        PROPANE(COMMON),
        HYDROGEN(COMMON),
        NEON(COMMON),
        CARBON_DIOXIDE(COMMON),


        SULFURIC_ACID(COMMON),

        MOLTEN_STEEL(COMMON),
        MOLTEN_SLAG(COMMON),
        MOLTEN_PLASTIC(COMMON),
        LIQUID_SILICON(COMMON),

        NAPALM(COMMON),

        //CBC
        MOLTEN_CAST_IRON(COMMON),
        MOLTEN_BRONZE(COMMON),
        MOLTEN_NETHERSTEEL(COMMON)

        ;

        public final TagKey<Fluid> tag;

        Fluids() {
            this(NameSpace.MOD);
        }
        Fluids(NameSpace namespace) {
            this(namespace, null);
        }
        Fluids(NameSpace namespace, String path) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? TFMGLang.asId(name()) : path);
            this.tag = FluidTags.create(id);
        }
    }

    public enum Engines {
        SCHEMATIC_CYCLE_BLACKLIST,
        UPGRADES_ON_SIDE
        ;

        public final TagKey<EngineType> tag;

        Engines() {
            this(NameSpace.MOD);
        }
        Engines(NameSpace namespace) {
            this(namespace, null);
        }
        Engines(NameSpace namespace, String path) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? TFMGLang.asId(name()) : path);
            this.tag = TagKey.create(TFMGRegistries.ENGINE_TYPE, id);
        }
    }

    public enum EngineFuel {
        LARGE_ENGINE,
        SIMPLE_LARGE_ENGINE
        ;

        public final TagKey<EngineFuelType> tag;

        EngineFuel() {
            this(NameSpace.MOD);
        }
        EngineFuel(NameSpace namespace) {
            this(namespace, null);
        }
        EngineFuel(NameSpace namespace, String path) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? TFMGLang.asId(name()) : path);
            this.tag = TagKey.create(TFMGRegistries.ENGINE_FUEL_TYPE, id);
        }
    }

    public enum FlamethrowerFuel {
        HELLFIRE,
        COLD
        ;

        public final TagKey<FlamethrowerFuelType> tag;

        FlamethrowerFuel() {
            this(NameSpace.MOD);
        }
        FlamethrowerFuel(NameSpace namespace) {
            this(namespace, null);
        }
        FlamethrowerFuel(NameSpace namespace, String path) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? TFMGLang.asId(name()) : path);
            this.tag = TagKey.create(TFMGRegistries.FLAMETHROWER_FUEL_TYPE, id);
        }
    }
}
