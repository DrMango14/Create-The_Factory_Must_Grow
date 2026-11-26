package com.drmangotea.tfmg.base.palettes;


import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGCreativeTabs;
import com.drmangotea.tfmg.registry.TFMGPaletteStoneTypes;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.CreateLang;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.List;
import java.util.function.Supplier;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public abstract class TFMGPaletteBlockPartial<B extends Block> {
    private final String name;

    protected TFMGPaletteBlockPartial(String name) {
        this.name = name;
    }

    static {
        REGISTRATE.setCreativeTab(TFMGCreativeTabs.TFMG_DECORATION);
    }

    public @NonnullType BlockBuilder<B, CreateRegistrate> create(String variantName, TFMGPaletteBlockPattern pattern,
                                                                 BlockEntry<? extends Block> block, TFMGPaletteStoneTypes variant) {
        String patternName = CreateLang.nonPluralId(pattern.createName(variantName));
        String blockName = patternName + "_" + this.name;

        BlockBuilder<B, CreateRegistrate> blockBuilder = REGISTRATE
                .block(blockName, p -> createBlock(block))
                .blockstate((c, p) -> generateBlockState(c, p, variantName, pattern, block))
                .recipe((c, p) -> createRecipes(variant, block, c, p))
                .transform(b -> transformBlock(b, variantName, pattern));

        ItemBuilder<BlockItem, BlockBuilder<B, CreateRegistrate>> itemBuilder = blockBuilder.item()
                .transform(b -> transformItem(b, variantName, pattern));

        if (canRecycle())
            itemBuilder.tag(variant.materialTag);

        return itemBuilder.build();
    }

    protected ResourceLocation getTexture(String variantName, TFMGPaletteBlockPattern pattern, int index) {
        return TFMGPaletteBlockPattern.toLocation(variantName, pattern.getTexture(index));
    }
    protected BlockBuilder<B, CreateRegistrate> transformBlock(BlockBuilder<B, CreateRegistrate> builder,
                                                               String variantName, TFMGPaletteBlockPattern pattern) {
        getBlockTags().forEach(builder::tag);
        return builder.transform(pickaxeOnly());
    }
    protected ItemBuilder<BlockItem, BlockBuilder<B, CreateRegistrate>> transformItem(
            ItemBuilder<BlockItem, BlockBuilder<B, CreateRegistrate>> builder, String variantName,
            TFMGPaletteBlockPattern pattern) {
        getItemTags().forEach(builder::tag);
        return builder;
    }
    protected boolean canRecycle() {
        return true;
    }
    protected abstract Iterable<TagKey<Block>> getBlockTags();
    protected abstract Iterable<TagKey<Item>> getItemTags();
    protected abstract B createBlock(Supplier<? extends Block> block);
    protected abstract void createRecipes(TFMGPaletteStoneTypes type, BlockEntry<? extends Block> patternBlock,
                                          DataGenContext<Block, ? extends Block> c, RegistrateRecipeProvider p);
    protected abstract void generateBlockState(DataGenContext<Block, B> ctx, RegistrateBlockstateProvider prov, String variantName, TFMGPaletteBlockPattern pattern, Supplier<? extends Block> block);
    static class Stairs extends TFMGPaletteBlockPartial<StairBlock> {
        public Stairs() {
            super("stairs");
        }

        @Override
        protected StairBlock createBlock(Supplier<? extends Block> block) {
            return new StairBlock(block.get()
                    .defaultBlockState(), Properties.ofFullCopy(block.get()));
        }

        @Override
        protected void generateBlockState(DataGenContext<Block, StairBlock> ctx, RegistrateBlockstateProvider prov,
                                          String variantName, TFMGPaletteBlockPattern pattern, Supplier<? extends Block> block) {
            prov.stairsBlock(ctx.get(), getTexture(variantName, pattern, 0));
        }
        @Override
        protected Iterable<TagKey<Block>> getBlockTags() {
            return List.of(BlockTags.STAIRS);
        }

        @Override
        protected Iterable<TagKey<Item>> getItemTags() {
            return List.of(ItemTags.STAIRS);
        }

        @Override
        protected void createRecipes(TFMGPaletteStoneTypes type, BlockEntry<? extends Block> patternBlock,
                                     DataGenContext<Block, ? extends Block> c, RegistrateRecipeProvider p) {
            RecipeCategory category = RecipeCategory.BUILDING_BLOCKS;
            p.stairs(DataIngredient.items(patternBlock.get()), category, c, c.getName(), false);
            p.stonecutting(DataIngredient.tag(type.materialTag), category, c, 1);
        }
    }
    static class Slab extends TFMGPaletteBlockPartial<SlabBlock> {

        private final boolean customSide;

        public Slab(boolean customSide) {
            super("slab");
            this.customSide = customSide;
        }

        @Override
        protected SlabBlock createBlock(Supplier<? extends Block> block) {
            return new SlabBlock(Properties.ofFullCopy(block.get()));
        }

        @Override
        protected boolean canRecycle() {
            return false;
        }

        @Override
        protected void generateBlockState(DataGenContext<Block, SlabBlock> ctx, RegistrateBlockstateProvider prov,
                                          String variantName, TFMGPaletteBlockPattern pattern, Supplier<? extends Block> block) {
            String name = ctx.getName();
            ResourceLocation mainTexture = getTexture(variantName, pattern, 0);
            ResourceLocation sideTexture = customSide ? getTexture(variantName, pattern, 1) : mainTexture;

            ModelFile bottom = prov.models()
                    .slab(name, sideTexture, mainTexture, mainTexture);
            ModelFile top = prov.models()
                    .slabTop(name + "_top", sideTexture, mainTexture, mainTexture);
            ModelFile doubleSlab;

            if (customSide) {
                doubleSlab = prov.models()
                        .cubeColumn(name + "_double", sideTexture, mainTexture);
            } else {
                doubleSlab = prov.models()
                        .getExistingFile(prov.modLoc(pattern.createName(variantName)));
            }

            prov.slabBlock(ctx.get(), bottom, top, doubleSlab);
        }

        @Override
        protected Iterable<TagKey<Block>> getBlockTags() {
            return List.of(BlockTags.SLABS);
        }

        @Override
        protected Iterable<TagKey<Item>> getItemTags() {
            return List.of(ItemTags.SLABS);
        }

        @Override
        protected void createRecipes(TFMGPaletteStoneTypes type, BlockEntry<? extends Block> patternBlock,
                                     DataGenContext<Block, ? extends Block> c, RegistrateRecipeProvider p) {
            RecipeCategory category = RecipeCategory.BUILDING_BLOCKS;
            p.slab(DataIngredient.items(patternBlock.get()), category, c, c.getName(), false);
            p.stonecutting(DataIngredient.tag(type.materialTag), category, c, 2);
            DataIngredient ingredient = DataIngredient.items(c.get());
            ShapelessRecipeBuilder.shapeless(category, patternBlock.get())
                    .requires(ingredient.toVanilla())
                    .requires(ingredient.toVanilla())
                    .unlockedBy("has_" + c.getName(), ingredient.getCriterion(p))
                    .save(p, TFMG.MOD_ID + ":" + c.getName() + "_recycling");
        }

        @Override
        protected BlockBuilder<SlabBlock, CreateRegistrate> transformBlock(
                BlockBuilder<SlabBlock, CreateRegistrate> builder, String variantName, TFMGPaletteBlockPattern pattern) {
            builder.loot((lt, block) -> lt.add(block, lt.createSlabItemTable(block)));
            return super.transformBlock(builder, variantName, pattern);
        }

    }

    public static class Wall extends TFMGPaletteBlockPartial<WallBlock> {
        public Wall() {
            super("wall");
        }

        @Override
        protected WallBlock createBlock(Supplier<? extends Block> block) {
            return new WallBlock(Properties.ofFullCopy(block.get()));
        }

        @Override
        protected ItemBuilder<BlockItem, BlockBuilder<WallBlock, CreateRegistrate>> transformItem(
                ItemBuilder<BlockItem, BlockBuilder<WallBlock, CreateRegistrate>> builder, String variantName,
                TFMGPaletteBlockPattern pattern) {
            builder.model((c, p) -> p.wallInventory(c.getName(), getTexture(variantName, pattern, 0)));
            return super.transformItem(builder, variantName, pattern);
        }

        @Override
        protected void generateBlockState(DataGenContext<Block, WallBlock> ctx, RegistrateBlockstateProvider prov,
                                          String variantName, TFMGPaletteBlockPattern pattern, Supplier<? extends Block> block) {
            prov.wallBlock(ctx.get(), pattern.createName(variantName), getTexture(variantName, pattern, 0));
        }

        @Override
        protected Iterable<TagKey<Block>> getBlockTags() {
            return List.of(BlockTags.WALLS);
        }

        @Override
        protected Iterable<TagKey<Item>> getItemTags() {
            return List.of(ItemTags.WALLS);
        }

        @Override
        protected void createRecipes(TFMGPaletteStoneTypes type, BlockEntry<? extends Block> patternBlock,
                                     DataGenContext<Block, ? extends Block> c, RegistrateRecipeProvider p) {
            RecipeCategory category = RecipeCategory.BUILDING_BLOCKS;
            p.stonecutting(DataIngredient.tag(type.materialTag), category, c, 1);
            DataIngredient ingredient = DataIngredient.items(patternBlock.get());
            ShapedRecipeBuilder.shaped(category, c.get(), 6)
                    .pattern("XXX")
                    .pattern("XXX")
                    .define('X', ingredient.toVanilla())
                    .unlockedBy("has_" + p.safeName(ingredient), ingredient.getCriterion(p))
                    .save(p, p.safeId(c.get()));
        }

    }

}