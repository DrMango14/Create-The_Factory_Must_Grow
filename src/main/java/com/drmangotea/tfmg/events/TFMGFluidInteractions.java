package com.drmangotea.tfmg.events;


import com.drmangotea.tfmg.registry.TFMGFluids;
import com.simibubi.create.api.event.PipeCollisionEvent;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TFMGFluidInteractions {


  //  @SubscribeEvent(priority = EventPriority.HIGH)
  //  public static void handlePipeFlowCollisionFallback(PipeCollisionEvent.Flow event) {
  //      Fluid f1 = event.getFirstFluid();
  //      Fluid f2 = event.getSecondFluid();
//
//
  //     if (f1 == Fluids.LAVA && FluidHelper.hasBlockState(f2)) {
  //          BlockState lavaInteraction = TFMGFluids.getLavaInteraction(FluidHelper.convertToFlowing(f2).defaultFluidState());
  //          if (lavaInteraction != null) {
  //              event.setState(lavaInteraction);
  //          }
  //      } else if (f2 == Fluids.LAVA && FluidHelper.hasBlockState(f1)) {
  //          BlockState lavaInteraction = TFMGFluids.getLavaInteraction(FluidHelper.convertToFlowing(f1).defaultFluidState());
  //          if (lavaInteraction != null) {
  //              event.setState(lavaInteraction);
  //          }
  //      }
  //  }


   // @SubscribeEvent(priority = EventPriority.HIGH)
   // public static void handlePipeSpillCollisionFallback(PipeCollisionEvent.Spill event) {
   //     Fluid pf = event.getPipeFluid();
   //     Fluid wf = event.getWorldFluid();
//
//
   //     if (pf == Fluids.LAVA) {
   //         BlockState lavaInteraction = TFMGFluids.getLavaInteraction(wf.defaultFluidState());
   //         if (lavaInteraction != null) {
   //             event.setState(lavaInteraction);
   //         }
   //     } else if (wf == Fluids.FLOWING_LAVA && FluidHelper.hasBlockState(pf)) {
   //         BlockState lavaInteraction = TFMGFluids.getLavaInteraction(FluidHelper.convertToFlowing(pf).defaultFluidState());
   //         if (lavaInteraction != null) {
   //             event.setState(lavaInteraction);
   //         }
   //     }
   // }

}
