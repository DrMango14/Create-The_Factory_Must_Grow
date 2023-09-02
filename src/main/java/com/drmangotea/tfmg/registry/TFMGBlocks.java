package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.base.TFMGBuilderTransformers;
import com.drmangotea.tfmg.base.TFMGSpriteShifts;
import com.drmangotea.tfmg.content.concrete.formwork.FormWorkBlock;
import com.drmangotea.tfmg.content.concrete.formwork.FormWorkGenerator;
import com.drmangotea.tfmg.content.deposits.FluidDepositBlock;
import com.drmangotea.tfmg.content.gadgets.explosives.napalm.NapalmBombBlock;
import com.drmangotea.tfmg.content.gadgets.explosives.thermite_grenades.ThermiteGrenadeItem;
import com.drmangotea.tfmg.content.items.TFMGFuelItem;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.logistics.chute.ChuteGenerator;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.Tags;


import static com.drmangotea.tfmg.CreateTFMG.REGISTRATE;
import static com.simibubi.create.foundation.data.BlockStateGen.simpleCubeAll;
import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.*;


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
    public static final BlockEntry<Block> FOSSILSTONE = REGISTRATE.block("fossilstone", Block::new)
            .initialProperties(() -> Blocks.OBSIDIAN)
            .properties(p -> p.strength(100f,1200f))
            .properties(p -> p.color(MaterialColor.COLOR_BLACK))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("fossilstone"))
            .item(TFMGFuelItem::fossilstone)
            .build()
            .lang("Fossilstone")
            .register();

    public static final BlockEntry<FluidDepositBlock> OIL_DEPOSIT = REGISTRATE.block("oil_deposit", FluidDepositBlock::new)
            .initialProperties(() -> Blocks.BEDROCK)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .properties(p -> p.strength(69696969))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("oil_deposit"))
            .item()
            .build()
            .lang("Oil Deposit")
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
    public static final BlockEntry<FormWorkBlock> FORMWORK_BLOCK =
            REGISTRATE.block("formwork_block", FormWorkBlock::new)
                    .initialProperties(Material.WOOD)
                    .properties(p -> p.color(MaterialColor.WOOD))
                    .properties(p -> p.requiresCorrectToolForDrops())
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .blockstate(new FormWorkGenerator()::generate)
                    .transform(axeOnly())
                    .item()
                    .build()
                    .register();




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
    public static final BlockEntry<Block> CONCRETE = REGISTRATE.block("concrete", Block::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
            .properties(p -> p.requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("concrete"))
            .tag(BlockTags.NEEDS_STONE_TOOL)
            .transform(tagBlockAndItem("concrete"))
            .build()
            .lang("Concrete")
            .register();

    public static void register() {}
}
