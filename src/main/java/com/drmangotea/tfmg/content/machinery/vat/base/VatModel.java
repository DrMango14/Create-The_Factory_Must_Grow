package com.drmangotea.tfmg.content.machinery.vat.base;

import com.drmangotea.tfmg.base.TFMGSpriteShifts;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.fluids.tank.FluidTankCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTModel;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import net.createmod.catnip.data.Iterate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VatModel extends CTModel {
    protected static final ModelProperty<CullData> CULL_PROPERTY = new ModelProperty<>();

    public static VatModel steelVat(BakedModel originalModel) {
        return new VatModel(originalModel, TFMGSpriteShifts.STEEL_VAT, TFMGSpriteShifts.STEEL_VAT_TOP,
                TFMGSpriteShifts.STEEL_VAT_INNER);
    }
    public static VatModel castIronVat(BakedModel originalModel) {
        return new VatModel(originalModel, TFMGSpriteShifts.CAST_IRON_VAT, TFMGSpriteShifts.CAST_IRON_VAT_TOP,
                TFMGSpriteShifts.CAST_IRON_VAT_INNER);
    }
    public static VatModel fireproofVat(BakedModel originalModel) {
        return new VatModel(originalModel, TFMGSpriteShifts.FIREPROOF_VAT, TFMGSpriteShifts.STEEL_VAT_TOP,
                TFMGSpriteShifts.STEEL_VAT_INNER);
    }


    private VatModel(BakedModel originalModel, CTSpriteShiftEntry side, CTSpriteShiftEntry top,
                     CTSpriteShiftEntry inner) {
        super(originalModel, new FluidTankCTBehaviour(side, top, inner));
    }

    @Override
    protected ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state,
                                                ModelData blockEntityData) {
        super.gatherModelData(builder, world, pos, state, blockEntityData);
        CullData cullData = new CullData();
        for (Direction d : Iterate.horizontalDirections)
            cullData.setCulled(d, ConnectivityHandler.isConnected(world, pos, pos.relative(d)));
        return builder.with(CULL_PROPERTY, cullData);
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType) {
        if (side != null)
            return Collections.emptyList();

        List<BakedQuad> quads = new ArrayList<>();
        for (Direction d : Iterate.directions) {
            if (extraData.has(CULL_PROPERTY) && extraData.get(CULL_PROPERTY)
                    .isCulled(d))
                continue;
            quads.addAll(super.getQuads(state, d, rand, extraData, renderType));
        }
        quads.addAll(super.getQuads(state, null, rand, extraData, renderType));
        return quads;
    }
    private class CullData {
        boolean[] culledFaces;

        public CullData() {
            culledFaces = new boolean[4];
            Arrays.fill(culledFaces, false);
        }

        void setCulled(Direction face, boolean cull) {
            if (face.getAxis()
                    .isVertical())
                return;
            culledFaces[face.get2DDataValue()] = cull;
        }

        boolean isCulled(Direction face) {
            if (face.getAxis()
                    .isVertical())
                return false;
            return culledFaces[face.get2DDataValue()];
        }
    }
}
