package com.drmangotea.tfmg.registry;



import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.base.palettes.TFMGPaletteBlockPattern;
import com.drmangotea.tfmg.base.palettes.TFMGPalettesVariantEntry;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Function;

import static com.drmangotea.tfmg.base.palettes.TFMGPaletteBlockPattern.STANDARD_RANGE;

public enum TFMGPaletteStoneTypes {




    BAUXITE(STANDARD_RANGE, r -> r.paletteStoneBlock("bauxite", () -> Blocks.DEEPSLATE, true, true)
            .properties(p -> p.destroyTime(1.25f))
            .register()),


    GALENA(STANDARD_RANGE, r -> r.paletteStoneBlock("galena", () -> Blocks.CALCITE, true, true)
            .properties(p -> p.destroyTime(1.25f))
            .register()),

    ;

    private Function<CreateRegistrate, NonNullSupplier<Block>> factory;
    private TFMGPalettesVariantEntry variants;

    public NonNullSupplier<Block> baseBlock;
    public TFMGPaletteBlockPattern[] variantTypes;
    public TagKey<Item> materialTag;

    TFMGPaletteStoneTypes(TFMGPaletteBlockPattern[] variantTypes,
                          Function<CreateRegistrate, NonNullSupplier<Block>> factory) {
        this.factory = factory;
        this.variantTypes = variantTypes;
    }

    public NonNullSupplier<Block> getBaseBlock() {
        return baseBlock;
    }

    public TFMGPalettesVariantEntry getVariants() {
        return variants;
    }

    @SuppressWarnings({"removal", "UnstableApiUsage"})
    public static void register(CreateRegistrate registrate) {
        for (TFMGPaletteStoneTypes paletteStoneVariants : values()) {
            paletteStoneVariants.baseBlock = paletteStoneVariants.factory.apply(registrate);
            String id = TFMGLang.asId(paletteStoneVariants.name());
            paletteStoneVariants.materialTag = AllTags.optionalTag(BuiltInRegistries.ITEM, TFMG.asResource("stone_types/" + id));
            paletteStoneVariants.variants = new TFMGPalettesVariantEntry(id, paletteStoneVariants);
        }
    }

}
