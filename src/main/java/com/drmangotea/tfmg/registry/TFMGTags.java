package com.drmangotea.tfmg.registry;


import com.drmangotea.tfmg.TFMG;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
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
        return optionalTag(registry, ResourceLocation.fromNamespaceAndPath("forge", path));
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
        MOD(TFMG.MOD_ID),
        FORGE("forge")
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
	    ORES_LITHIUM(FORGE, "ores/lithium"),
	    PUMPJACK_CONNECTOR,
	    PUMPJACK_HEAD,
	    PUMPJACK_PART,
	    PUMPJACK_SMALL_PART,
	    RAW_LITHIUM(FORGE, "raw_materials/lithium"),
	    REINFORCED_BLAST_FURNACE_SUPPORT,
	    REINFORCED_BLAST_FURNACE_WALL,
	    STORAGE_BLOCKS_CAST_IRON(FORGE, "storage_blocks/cast_iron"),
	    STORAGE_BLOCKS_COAL_COKE(FORGE, "storage_blocks/coal_coke"),
	    STORAGE_BLOCKS_LITHIUM(FORGE, "storage_blocks/lithium"),
	    STORAGE_BLOCKS_PLASTIC(FORGE, "storage_blocks/plastic"),
	    STORAGE_BLOCKS_RAW_LITHIUM(FORGE, "storage_blocks/raw_lithium"),
	    SURFACE_SCANNER_FINDABLE,
        CONCRETE(FORGE, "concrete")
        ;

        public final TagKey<Block> tag;

        TFMGBlockTags() {
            this(MOD);
        }

        TFMGBlockTags(NameSpace namespace) {
            this(namespace, null);
        }

        TFMGBlockTags(NameSpace namespace, String path) {
	        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? CreateLang.asId(name()) : path);
	        this.tag = BlockTags.create(id);
        }

    }
    public enum TFMGItemTags {
	    BLAST_FURNACE_FUEL,
	    DUSTS_COAL_COKE(FORGE, "dusts/coal_coke"),
	    DUSTS_IRON(FORGE, "dusts/iron"),
	    DUSTS_SALTPETER(FORGE, "dusts/saltpeter"),
	    DUSTS_SULFUR(FORGE, "dusts/sulfur"),
	    FLUX,
	    INGOTS_CAST_IRON(FORGE, "ingots/cast_iron"),
	    INGOTS_LITHIUM(FORGE, "ingots/lithium"),
	    INGOTS_PLASTIC(FORGE, "ingots/plastic"),
	    INGOTS_RUBBER(FORGE, "ingots/rubber"),
	    INGOTS_SILICON(FORGE, "ingots/silicon"),
	    NUGGETS_CAST_IRON(FORGE, "nuggets/cast_iron"),
	    NUGGETS_LITHIUM(FORGE, "nuggets/lithium"),
	    ORES_LITHIUM(FORGE, "ores/lithium"),
	    PLATES_CAST_IRON(FORGE, "plates/cast_iron"),
	    RAW_LITHIUM(FORGE, "raw_materials/lithium"),
	    RODS_STEEL(FORGE, "rods/steel"),
	    SPOOLS,
	    STORAGE_BLOCKS_CAST_IRON(FORGE, "storage_blocks/cast_iron"),
	    STORAGE_BLOCKS_COAL_COKE(FORGE, "storage_blocks/coal_coke"),
	    STORAGE_BLOCKS_LITHIUM(FORGE, "storage_blocks/lithium"),
	    STORAGE_BLOCKS_PLASTIC(FORGE, "storage_blocks/plastic"),
	    STORAGE_BLOCKS_RAW_LITHIUM(FORGE, "storage_blocks/raw_lithium"),
	    WIRES(FORGE),
	    WIRES_ALUMINUM(FORGE, "wires/aluminum"),
	    WIRES_CONSTANTAN(FORGE, "wires/constantan"),
	    WIRES_COPPER(FORGE, "wires/copper"),
        CONCRETE(FORGE, "concrete")
        ;

        public final TagKey<Item> tag;

        TFMGItemTags() {
            this(NameSpace.MOD);
        }

        TFMGItemTags(NameSpace namespace) {
            this(namespace, null);
        }

        TFMGItemTags(NameSpace namespace, String path) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? CreateLang.asId(name()) : path);
            this.tag = ItemTags.create(id);
        }

    }
    public enum TFMGFluidTags {
        GAS(MOD),

        FLAMMABLE(MOD),
        FIREBOX_FUEL(MOD),
        BLAST_STOVE_FUEL(MOD),
        AIR(FORGE),
        COOLING_FLUID(FORGE),

        GASOLINE(FORGE),
        DIESEL(FORGE),
        KEROSENE(FORGE),

        CREOSOTE(FORGE),
        FURNACE_GAS(FORGE),

        LPG(FORGE),
        HEAVY_OIL(FORGE),
        LUBRICATION_OIL(FORGE),
        NAPHTHA(FORGE),
        CRUDE_OIL(FORGE),
        MOLTEN_STEEL(FORGE),
        FUEL(FORGE)
        ;

        public final TagKey<Fluid> tag;

        TFMGFluidTags() {
            this(MOD);
        }

        TFMGFluidTags(NameSpace namespace) {
            this(namespace, null);
        }

        TFMGFluidTags(NameSpace namespace, String path) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? CreateLang.asId(name()) : path);
            this.tag = FluidTags.create(id);
        }

    }

}
