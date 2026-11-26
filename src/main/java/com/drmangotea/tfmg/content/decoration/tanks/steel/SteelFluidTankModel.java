package com.drmangotea.tfmg.content.decoration.tanks.steel;

import com.drmangotea.tfmg.base.TFMGSpriteShifts;
import com.drmangotea.tfmg.content.decoration.tanks.TFMGFluidTankModel;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import net.minecraft.client.resources.model.BakedModel;

public class SteelFluidTankModel extends TFMGFluidTankModel {
    public static SteelFluidTankModel standard(BakedModel originalModel) {
        return new SteelFluidTankModel(
			originalModel,
			TFMGSpriteShifts.STEEL_FLUID_TANK,
			TFMGSpriteShifts.STEEL_FLUID_TANK_TOP,
			TFMGSpriteShifts.STEEL_FLUID_TANK_INNER
		);
    }
    public static SteelFluidTankModel steelVat(BakedModel originalModel) {
        return new SteelFluidTankModel(
			originalModel,
			TFMGSpriteShifts.STEEL_VAT,
			TFMGSpriteShifts.STEEL_VAT_TOP,
			TFMGSpriteShifts.STEEL_VAT_INNER
		);
    }
    public static SteelFluidTankModel castIronVat(BakedModel originalModel) {
        return new SteelFluidTankModel(
			originalModel,
			TFMGSpriteShifts.CAST_IRON_VAT,
			TFMGSpriteShifts.CAST_IRON_VAT_TOP,
			TFMGSpriteShifts.CAST_IRON_VAT_INNER
		);
    }
    public static SteelFluidTankModel fireproofVat(BakedModel originalModel) {
        return new SteelFluidTankModel(
			originalModel,
			TFMGSpriteShifts.FIREPROOF_VAT,
			TFMGSpriteShifts.STEEL_VAT_TOP,
			TFMGSpriteShifts.STEEL_VAT_INNER
		);
    }
    private SteelFluidTankModel(BakedModel originalModel, CTSpriteShiftEntry side, CTSpriteShiftEntry top, CTSpriteShiftEntry inner) {
        super(originalModel, side, top, inner);
    }
}