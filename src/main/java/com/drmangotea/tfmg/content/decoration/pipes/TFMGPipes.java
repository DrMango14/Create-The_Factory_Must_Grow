package com.drmangotea.tfmg.content.decoration.pipes;

import com.drmangotea.tfmg.base.TFMGCreativeTabs;
import com.drmangotea.tfmg.base.TFMGRegistrate;
import com.drmangotea.tfmg.base.TFMGSpriteShifts;
import com.drmangotea.tfmg.config.TFMGStress;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.pipes.SmartFluidPipeGenerator;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlock;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class TFMGPipes {

    public static final TFMGRegistrate registrate = (TFMGRegistrate) REGISTRATE.setCreativeTab(TFMGCreativeTabs.TFMG_DECORATION);

    public static final Map<PipeMaterial, TFMGPipeEntry> PIPES = new HashMap<>();

    static {
        PIPES.put(PipeMaterial.BRASS, createEntry(PipeMaterial.BRASS,  TFMGSpriteShifts.BRASS_FLUID_CASING));
        PIPES.put(PipeMaterial.STEEL, createEntry(PipeMaterial.STEEL,  TFMGSpriteShifts.STEEL_FLUID_CASING));
        PIPES.put(PipeMaterial.ALUMINUM, createEntry(PipeMaterial.ALUMINUM,  TFMGSpriteShifts.ALUMINUM_FLUID_CASING));
        PIPES.put(PipeMaterial.CAST_IRON, createEntry(PipeMaterial.CAST_IRON, TFMGSpriteShifts.CAST_IRON_FLUID_CASING));
        PIPES.put(PipeMaterial.PLASTIC, createEntry(PipeMaterial.PLASTIC, TFMGSpriteShifts.PLASTIC_FLUID_CASING));
    }

    private static TFMGPipeEntry createEntry(PipeMaterial material, CTSpriteShiftEntry spriteShiftEntry) {
        return new TFMGPipeEntry(material, registrate).encasedSpriteShift(spriteShiftEntry);
    }

    public static void init() {
    }

    public enum PipeMaterial {
        BRASS("brass"),
        STEEL("steel"),
        ALUMINUM("aluminum"),
        CAST_IRON("cast_iron"),
        PLASTIC("plastic");

        public final String name;

        PipeMaterial(String name) {
            this.name = name;
        }
    }
}
