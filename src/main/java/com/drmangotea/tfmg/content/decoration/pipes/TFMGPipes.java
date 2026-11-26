package com.drmangotea.tfmg.content.decoration.pipes;

import com.drmangotea.tfmg.base.TFMGCreativeTabs;
import com.drmangotea.tfmg.base.TFMGRegistrate;
import com.drmangotea.tfmg.base.TFMGSpriteShifts;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGPipes {

    public static final TFMGRegistrate registrate = (TFMGRegistrate) REGISTRATE.setCreativeTab(TFMGCreativeTabs.TFMG_DECORATION);

    public static final Map<PipeMaterial, TFMGPipeEntry> PIPES = Arrays.stream(PipeMaterial.values()).collect(
		() -> new EnumMap<>(PipeMaterial.class),
		(map, mat) -> map.put(mat, new TFMGPipeEntry(mat, registrate)),
		EnumMap::putAll
	);

    public static void init() {
    }

    public enum PipeMaterial {
        BRASS("brass", TFMGSpriteShifts.BRASS_FLUID_CASING, CommonMetal.BRASS.ingots, CommonMetal.BRASS.plates),
        STEEL("steel", TFMGSpriteShifts.STEEL_FLUID_CASING, CommonMetal.STEEL.ingots, CommonMetal.STEEL.plates),
        ALUMINUM("aluminum", TFMGSpriteShifts.ALUMINUM_FLUID_CASING, CommonMetal.ALUMINUM.ingots, CommonMetal.ALUMINUM.plates),
        CAST_IRON("cast_iron", TFMGSpriteShifts.CAST_IRON_FLUID_CASING, TFMGTags.Items.INGOTS_CAST_IRON.tag, TFMGTags.Items.PLATES_CAST_IRON.tag),
        PLASTIC("plastic", TFMGSpriteShifts.PLASTIC_FLUID_CASING, TFMGTags.Items.INGOTS_PLASTIC.tag, TFMGTags.Items.PLATES_PLASTIC.tag); //plastic uses sheets instead of ingots for all it's recipes

        public final String name;
		public final CTSpriteShiftEntry encasedSpriteShift;
		public final TagKey<Item> ingot, sheet;

        PipeMaterial(String name, CTSpriteShiftEntry encasedSpriteShift, TagKey<Item> ingot, TagKey<Item> sheet) {
            this.name = name;
			this.encasedSpriteShift = encasedSpriteShift;
			this.ingot = ingot;
			this.sheet = sheet;
        }
    }
}
