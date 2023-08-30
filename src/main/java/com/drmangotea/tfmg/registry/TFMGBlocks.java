package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.base.TFMGSpriteShifts;
import com.drmangotea.tfmg.content.gadgets.explosives.napalm.NapalmBombBlock;
import com.drmangotea.tfmg.content.gadgets.explosives.thermite_grenades.fire.BlueFireBlock;
import com.drmangotea.tfmg.content.gadgets.explosives.thermite_grenades.fire.GreenFireBlock;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.Create;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.Tags;


import static com.drmangotea.tfmg.CreateTFMG.REGISTRATE;
import static com.simibubi.create.foundation.data.BlockStateGen.simpleCubeAll;
import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static com.simibubi.create.foundation.data.TagGen.tagBlockAndItem;


public class TFMGBlocks {


    static {
        REGISTRATE.creativeModeTab(() -> TFMGCreativeModeTabs.TFMG_BASE);
    }
    //-----------------------MISC---------------------------//
    public static final BlockEntry<NapalmBombBlock> NAPALM_BOMB = REGISTRATE.block("napalm_bomb", NapalmBombBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_GREEN))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("napalm_bomb"))
            .item()
            .build()
            .lang("Napalm Bomb")
            .register();
    public static final BlockEntry<CasingBlock> STEEL_CASING = REGISTRATE.block("steel_casing", CasingBlock::new)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_LIGHT_GRAY))
            .transform(BuilderTransformers.casing(() -> TFMGSpriteShifts.STEEL_CASING))
            .register();
    public static final BlockEntry<CasingBlock> HEAVY_MACHINERY_CASING = REGISTRATE.block("heavy_machinery_casing", CasingBlock::new)
            .properties(p -> p.color(MaterialColor.TERRACOTTA_LIGHT_GRAY))
            .transform(BuilderTransformers.casing(() -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING))
            .properties(p -> p.sound(SoundType.COPPER))
            .register();






    //-----------------------MACHINES---------------------------//





    //-----------------------BUILDING BLOCKS---------------------------//
    public static final BlockEntry<Block> STEEL_BLOCK = REGISTRATE.block("steel_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.STEEL_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.STEEL_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("steel_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem("storage_blocks/steel"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Steel")
            .register();
    public static final BlockEntry<Block> CAST_IRON_BLOCK = REGISTRATE.block("cast_iron_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, TFMGSpriteShifts.CAST_IRON_BLOCK)))
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("cast_iron_block"))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem("storage_blocks/cast_iron"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Cast Iron")
            .register();





    //-----------------------CONCRETE---------------------------//
    static {
        REGISTRATE.creativeModeTab(() -> TFMGCreativeModeTabs.TFMG_CONCRETE);
    }

    public static void register() {}
}
