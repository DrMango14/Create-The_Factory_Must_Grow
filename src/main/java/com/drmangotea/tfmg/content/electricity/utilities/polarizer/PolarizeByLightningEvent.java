package com.drmangotea.tfmg.content.electricity.utilities.polarizer;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.registry.TFMGItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;


@EventBusSubscriber
public class PolarizeByLightningEvent {

    @SubscribeEvent
    public static void onStruckByLightning(EntityStruckByLightningEvent event) {

        if (event.getEntity() instanceof ItemEntity entity) {
           if (entity.getItem().is(TFMGItems.MAGNETIC_ALLOY_INGOT.get())) {

               int random = entity.level().random.nextInt(entity.getItem().getCount()+1);

               if(random == 1){
                    if(entity.level().random.nextBoolean()){
                        random =0;
                    }else {
                        random =1;
                    }
               }

               entity.setItem(new ItemStack(TFMGItems.MAGNET.get(),random));
               TFMGUtils.spawnElectricParticles(entity.level(), entity.blockPosition());
               event.setCanceled(true);
            }
            if (entity.getItem().is(TFMGItems.MAGNET.get())) {

                event.setCanceled(true);
            }
        }

    }

}
