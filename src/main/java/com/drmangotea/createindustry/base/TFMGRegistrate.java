package com.drmangotea.createindustry.base;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.ForgeRegistries;

import static com.simibubi.create.foundation.data.BlockStateGen.simpleCubeAll;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class TFMGRegistrate extends CreateRegistrate {
    
    public static String autoLang(String id) {
        StringBuilder builder = new StringBuilder();
        boolean b = true;
        for (char c: id.toCharArray()) {
            if(c == '_') {
                builder.append(' ');
                b = true;
            } else {
                builder.append(b ? String.valueOf(c).toUpperCase() : c);
                b = false;
            }
        }
        return builder.toString();
    }
    
    protected TFMGRegistrate() {
        super(CreateTFMG.MOD_ID);
    }
    
    public static TFMGRegistrate create() {
        return new TFMGRegistrate();
    }
    
    public static Block getBlock(String name) {
        return CreateTFMG.REGISTRATE.get(name, ForgeRegistries.BLOCKS.getRegistryKey()).get();
    }
    public static Item getItem(String name) {
        return CreateTFMG.REGISTRATE.get(name, ForgeRegistries.ITEMS.getRegistryKey()).get();
    }
    public static Item getBucket(String name) {
        return CreateTFMG.REGISTRATE.get(name+"_bucket", ForgeRegistries.ITEMS.getRegistryKey()).get();
    }
    
    public BlockEntry<Block> coloredConcrete(String pColor) {
        return this.block(pColor + "_concrete", Block::new)
                .initialProperties(() -> Blocks.STONE)
                .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
                .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                .transform(pickaxeOnly())
                .blockstate(simpleCubeAll(pColor + "_concrete"))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .item()
                .build()
                .lang(autoLang(pColor + "_concrete"))
                .register();
    }
    
    public BlockEntry<StairBlock> coloredConcreteStair(String pColor) {
        return this.block(pColor + "_concrete_stairs", p -> new StairBlock(() -> TFMGBlocks.CONCRETE.get().defaultBlockState(), p))
                .initialProperties(() -> Blocks.STONE)
                .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
                .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateStairBlockState(c, p, pColor + "_concrete"))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.STAIRS)
                .recipe((c, p) -> p.stonecutting(DataIngredient.items(getBlock(pColor + "_concrete")), c::get, 1))
                .item()
                .transform(b -> TFMGVanillaBlockStates.transformStairItem(b, pColor + "_concrete"))
                .build()
                .lang(autoLang(pColor + "_concrete_stairs"))
                .register();
    }
    
    public BlockEntry<SlabBlock> coloredConcreteSlab(String pColor) {
        return this.block(pColor + "_concrete_slab", SlabBlock::new)
                .initialProperties(() -> Blocks.STONE)
                .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
                .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateSlabBlockState(c, p, pColor + "_concrete"))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.SLABS)
                .recipe((c, p) -> p.stonecutting(DataIngredient.items(getBlock(pColor + "_concrete")), c::get, 2))
                .item()
                .transform(customItemModel(pColor + "_concrete_bottom"))
                .lang(autoLang(pColor + "_concrete_slab"))
                .register();
    }
    
    public BlockEntry<WallBlock> coloredConcreteWall(String pColor) {
        return this.block(pColor + "_concrete_wall", WallBlock::new)
                .initialProperties(() -> Blocks.STONE)
                .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
                .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, pColor + "_concrete"))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.WALLS)
                .recipe((c, p) -> p.stonecutting(DataIngredient.items(getBlock(pColor + "_concrete")), c::get, 1))
                .item()
                .transform(b -> TFMGVanillaBlockStates.transformWallItem(b, pColor + "_concrete"))
                .build()
                .lang(autoLang(pColor + "_concrete_wall"))
                .register();
    }
    
    public BlockEntry<TFMGHorizontalDirectionalBlock> coloredCautionBlock(String pColor) {
        return this.block(pColor + "_caution_block", TFMGHorizontalDirectionalBlock::new)
                .initialProperties(() -> Blocks.COPPER_BLOCK)
                .properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
                .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                .transform(pickaxeOnly())
                .blockstate((c, p) -> p.horizontalBlock(c.get(), p.models()
                        .withExistingParent(c.getName(), p.modLoc("block/caution_block"))
                        .texture("0", p.modLoc("block/caution_block/" + pColor))
                        .texture("particle", p.modLoc("block/caution_block/" + pColor))
                ))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .item()
                .build()
                .lang(autoLang(pColor + "_caution_block"))
                .register();
    }
}
