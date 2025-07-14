package com.drmangotea.tfmg.ponder;

import com.drmangotea.tfmg.content.engines.base.EngineBlock;
import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.gauge.SpeedGaugeBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;

import java.util.function.UnaryOperator;

public class TFMGSceneBuilder extends CreateSceneBuilder {

    private final TFMGWorldInstructions tfmgInstructions;

    public TFMGSceneBuilder(SceneBuilder baseSceneBuilder) {
        super(baseSceneBuilder);
        tfmgInstructions = new TFMGWorldInstructions();
    }


    public TFMGWorldInstructions tfmgInstructions(){
        return tfmgInstructions;
    }


    public class TFMGWorldInstructions extends WorldInstructions {

        public void addPistonToEngine(BlockPos pos) {
            modifyBlockEntity(pos, RegularEngineBlockEntity.class, be -> {
                for(int i =0;i<be.pistonInventory.getSlots();i++){
                    if(be.pistonInventory.getItem(i).isEmpty()){
                        be.pistonInventory.setStackInSlot(i, TFMGItems.ENGINE_CYLINDER.asStack());
                        break;
                    }
                }
            });
        }
        public void addShaftToEngine(BlockPos pos) {
          //  modifyBlockEntity(pos, RegularEngineBlockEntity.class, be ->
          //  be.getLevel().setBlock(be.getBlockPos(),be.getBlockState().setValue(EngineBlock.ENGINE_STATE,EngineBlock.EngineState.SHAFT),2));
        }

    }
}
