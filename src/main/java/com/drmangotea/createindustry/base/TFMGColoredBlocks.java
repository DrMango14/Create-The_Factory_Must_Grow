package com.drmangotea.createindustry.base;

import com.drmangotea.createindustry.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.createindustry.registry.TFMGCreativeModeTabs;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;

import static com.drmangotea.createindustry.CreateTFMG.REGISTRATE;

public class TFMGColoredBlocks {
    static {
        REGISTRATE.creativeModeTab(() -> TFMGCreativeModeTabs.TFMG_BUILDING_BLOCKS);
    }
    
    public static final BlockEntry<Block> BLACK_CONCRETE = REGISTRATE.coloredConcrete("black");
    public static final BlockEntry<Block> WHITE_CONCRETE = REGISTRATE.coloredConcrete("white");
    public static final BlockEntry<Block> BLUE_CONCRETE = REGISTRATE.coloredConcrete("blue");
    public static final BlockEntry<Block> LIGHT_BLUE_CONCRETE = REGISTRATE.coloredConcrete("light_blue");
    public static final BlockEntry<Block> RED_CONCRETE = REGISTRATE.coloredConcrete("red");
    public static final BlockEntry<Block> GREEN_CONCRETE = REGISTRATE.coloredConcrete("green");
    public static final BlockEntry<Block> LIME_CONCRETE = REGISTRATE.coloredConcrete("lime");
    public static final BlockEntry<Block> PINK_CONCRETE = REGISTRATE.coloredConcrete("pink");
    public static final BlockEntry<Block> MAGENTA_CONCRETE = REGISTRATE.coloredConcrete("magenta");
    public static final BlockEntry<Block> YELLOW_CONCRETE = REGISTRATE.coloredConcrete("yellow");
    public static final BlockEntry<Block> GRAY_CONCRETE = REGISTRATE.coloredConcrete("gray");
    public static final BlockEntry<Block> LIGHT_GRAY_CONCRETE = REGISTRATE.coloredConcrete("light_gray");
    public static final BlockEntry<Block> BROWN_CONCRETE = REGISTRATE.coloredConcrete("brown");
    public static final BlockEntry<Block> CYAN_CONCRETE = REGISTRATE.coloredConcrete("cyan");
    public static final BlockEntry<Block> PURPLE_CONCRETE = REGISTRATE.coloredConcrete("purple");
    public static final BlockEntry<Block> ORANGE_CONCRETE = REGISTRATE.coloredConcrete("orange");
    
    public static final BlockEntry<StairBlock> BLACK_CONCRETE_STAIRS = REGISTRATE.coloredConcreteStair("black");
    public static final BlockEntry<StairBlock> WHITE_CONCRETE_STAIRS = REGISTRATE.coloredConcreteStair("white");
    public static final BlockEntry<StairBlock> BLUE_CONCRETE_STAIRS = REGISTRATE.coloredConcreteStair("blue");
    public static final BlockEntry<StairBlock> LIGHT_BLUE_CONCRETE_STAIRS = REGISTRATE.coloredConcreteStair("light_blue");
    public static final BlockEntry<StairBlock> RED_CONCRETE_STAIRS = REGISTRATE.coloredConcreteStair("red");
    public static final BlockEntry<StairBlock> GREEN_CONCRETE_STAIRS = REGISTRATE.coloredConcreteStair("green");
    public static final BlockEntry<StairBlock> LIME_CONCRETE_STAIRS = REGISTRATE.coloredConcreteStair("lime");
    public static final BlockEntry<StairBlock> PINK_CONCRETE_STAIRS = REGISTRATE.coloredConcreteStair("pink");
    public static final BlockEntry<StairBlock> MAGENTA_CONCRETE_STAIRS = REGISTRATE.coloredConcreteStair("magenta");
    public static final BlockEntry<StairBlock> YELLOW_CONCRETE_STAIRS = REGISTRATE.coloredConcreteStair("yellow");
    public static final BlockEntry<StairBlock> GRAY_CONCRETE_STAIRS = REGISTRATE.coloredConcreteStair("gray");
    public static final BlockEntry<StairBlock> LIGHT_GRAY_CONCRETE_STAIRS = REGISTRATE.coloredConcreteStair("light_gray");
    public static final BlockEntry<StairBlock> BROWN_CONCRETE_STAIRS = REGISTRATE.coloredConcreteStair("brown");
    public static final BlockEntry<StairBlock> CYAN_CONCRETE_STAIRS = REGISTRATE.coloredConcreteStair("cyan");
    public static final BlockEntry<StairBlock> PURPLE_CONCRETE_STAIRS = REGISTRATE.coloredConcreteStair("purple");
    public static final BlockEntry<StairBlock> ORANGE_CONCRETE_STAIRS = REGISTRATE.coloredConcreteStair("orange");
    
    public static final BlockEntry<SlabBlock> BLACK_CONCRETE_SLAB = REGISTRATE.coloredConcreteSlab("black");
    public static final BlockEntry<SlabBlock> WHITE_CONCRETE_SLAB = REGISTRATE.coloredConcreteSlab("white");
    public static final BlockEntry<SlabBlock> BLUE_CONCRETE_SLAB = REGISTRATE.coloredConcreteSlab("blue");
    public static final BlockEntry<SlabBlock> LIGHT_BLUE_CONCRETE_SLAB = REGISTRATE.coloredConcreteSlab("light_blue");
    public static final BlockEntry<SlabBlock> RED_CONCRETE_SLAB = REGISTRATE.coloredConcreteSlab("red");
    public static final BlockEntry<SlabBlock> GREEN_CONCRETE_SLAB = REGISTRATE.coloredConcreteSlab("green");
    public static final BlockEntry<SlabBlock> LIME_CONCRETE_SLAB = REGISTRATE.coloredConcreteSlab("lime");
    public static final BlockEntry<SlabBlock> PINK_CONCRETE_SLAB = REGISTRATE.coloredConcreteSlab("pink");
    public static final BlockEntry<SlabBlock> MAGENTA_CONCRETE_SLAB = REGISTRATE.coloredConcreteSlab("magenta");
    public static final BlockEntry<SlabBlock> YELLOW_CONCRETE_SLAB = REGISTRATE.coloredConcreteSlab("yellow");
    public static final BlockEntry<SlabBlock> GRAY_CONCRETE_SLAB = REGISTRATE.coloredConcreteSlab("gray");
    public static final BlockEntry<SlabBlock> LIGHT_GRAY_CONCRETE_SLAB = REGISTRATE.coloredConcreteSlab("light_gray");
    public static final BlockEntry<SlabBlock> BROWN_CONCRETE_SLAB = REGISTRATE.coloredConcreteSlab("brown");
    public static final BlockEntry<SlabBlock> CYAN_CONCRETE_SLAB = REGISTRATE.coloredConcreteSlab("cyan");
    public static final BlockEntry<SlabBlock> PURPLE_CONCRETE_SLAB = REGISTRATE.coloredConcreteSlab("purple");
    public static final BlockEntry<SlabBlock> ORANGE_CONCRETE_SLAB = REGISTRATE.coloredConcreteSlab("orange");
    
    public static final BlockEntry<WallBlock> BLACK_CONCRETE_WALL = REGISTRATE.coloredConcreteWall("black");
    public static final BlockEntry<WallBlock> WHITE_CONCRETE_WALL = REGISTRATE.coloredConcreteWall("white");
    public static final BlockEntry<WallBlock> BLUE_CONCRETE_WALL = REGISTRATE.coloredConcreteWall("blue");
    public static final BlockEntry<WallBlock> LIGHT_BLUE_CONCRETE_WALL = REGISTRATE.coloredConcreteWall("light_blue");
    public static final BlockEntry<WallBlock> RED_CONCRETE_WALL = REGISTRATE.coloredConcreteWall("red");
    public static final BlockEntry<WallBlock> GREEN_CONCRETE_WALL = REGISTRATE.coloredConcreteWall("green");
    public static final BlockEntry<WallBlock> LIME_CONCRETE_WALL = REGISTRATE.coloredConcreteWall("lime");
    public static final BlockEntry<WallBlock> PINK_CONCRETE_WALL = REGISTRATE.coloredConcreteWall("pink");
    public static final BlockEntry<WallBlock> MAGENTA_CONCRETE_WALL = REGISTRATE.coloredConcreteWall("magenta");
    public static final BlockEntry<WallBlock> YELLOW_CONCRETE_WALL = REGISTRATE.coloredConcreteWall("yellow");
    public static final BlockEntry<WallBlock> GRAY_CONCRETE_WALL = REGISTRATE.coloredConcreteWall("gray");
    public static final BlockEntry<WallBlock> LIGHT_GRAY_CONCRETE_WALL = REGISTRATE.coloredConcreteWall("light_gray");
    public static final BlockEntry<WallBlock> BROWN_CONCRETE_WALL = REGISTRATE.coloredConcreteWall("brown");
    public static final BlockEntry<WallBlock> CYAN_CONCRETE_WALL = REGISTRATE.coloredConcreteWall("cyan");
    public static final BlockEntry<WallBlock> PURPLE_CONCRETE_WALL = REGISTRATE.coloredConcreteWall("purple");
    public static final BlockEntry<WallBlock> ORANGE_CONCRETE_WALL = REGISTRATE.coloredConcreteWall("orange");
    
    public static final BlockEntry<TFMGHorizontalDirectionalBlock> WHITE_CAUTION_BLOCK = REGISTRATE.coloredCautionBlock("white");
    public static final BlockEntry<TFMGHorizontalDirectionalBlock> BLUE_CAUTION_BLOCK = REGISTRATE.coloredCautionBlock("blue");
    public static final BlockEntry<TFMGHorizontalDirectionalBlock> LIGHT_BLUE_CAUTION_BLOCK = REGISTRATE.coloredCautionBlock("light_blue");
    public static final BlockEntry<TFMGHorizontalDirectionalBlock> RED_CAUTION_BLOCK = REGISTRATE.coloredCautionBlock("red");
    public static final BlockEntry<TFMGHorizontalDirectionalBlock> GREEN_CAUTION_BLOCK = REGISTRATE.coloredCautionBlock("green");
    public static final BlockEntry<TFMGHorizontalDirectionalBlock> LIME_CAUTION_BLOCK = REGISTRATE.coloredCautionBlock("lime");
    public static final BlockEntry<TFMGHorizontalDirectionalBlock> PINK_CAUTION_BLOCK = REGISTRATE.coloredCautionBlock("pink");
    public static final BlockEntry<TFMGHorizontalDirectionalBlock> MAGENTA_CAUTION_BLOCK = REGISTRATE.coloredCautionBlock("magenta");
    public static final BlockEntry<TFMGHorizontalDirectionalBlock> YELLOW_CAUTION_BLOCK = REGISTRATE.coloredCautionBlock("yellow");
    public static final BlockEntry<TFMGHorizontalDirectionalBlock> GRAY_CAUTION_BLOCK = REGISTRATE.coloredCautionBlock("gray");
    public static final BlockEntry<TFMGHorizontalDirectionalBlock> LIGHT_GRAY_CAUTION_BLOCK = REGISTRATE.coloredCautionBlock("light_gray");
    public static final BlockEntry<TFMGHorizontalDirectionalBlock> BROWN_CAUTION_BLOCK = REGISTRATE.coloredCautionBlock("brown");
    public static final BlockEntry<TFMGHorizontalDirectionalBlock> CYAN_CAUTION_BLOCK = REGISTRATE.coloredCautionBlock("cyan");
    public static final BlockEntry<TFMGHorizontalDirectionalBlock> PURPLE_CAUTION_BLOCK = REGISTRATE.coloredCautionBlock("purple");
    public static final BlockEntry<TFMGHorizontalDirectionalBlock> ORANGE_CAUTION_BLOCK = REGISTRATE.coloredCautionBlock("orange");
    
    public static void register() {
        // NO-OP
    }
}
