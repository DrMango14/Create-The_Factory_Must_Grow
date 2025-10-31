package com.drmangotea.tfmg.content.decoration.pipes;

import com.drmangotea.tfmg.base.TFMGCreativeTabs;
import com.drmangotea.tfmg.base.TFMGRegistrate;
import com.drmangotea.tfmg.base.TFMGSpriteShifts;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;

import java.util.HashMap;
import java.util.Map;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

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
