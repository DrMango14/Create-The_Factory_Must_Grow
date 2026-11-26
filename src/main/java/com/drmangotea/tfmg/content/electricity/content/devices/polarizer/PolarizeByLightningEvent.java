package com.drmangotea.tfmg.content.electricity.content.devices.polarizer;

import com.drmangotea.tfmg.recipes.PolarizingRecipe;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;

import java.util.Optional;


@EventBusSubscriber
public class PolarizeByLightningEvent {

    @SubscribeEvent
    public static void onStruckByLightning(EntityStruckByLightningEvent event) {
        if (event.getEntity() instanceof ItemEntity itemEntity) {
            Level level = itemEntity.level();
            ItemStack stack = itemEntity.getItem();
            if (PolarizerCommons.canBePolarized(level, stack.copyWithCount(1))) {
                int successCount = 0;
                for (int i = 0; i <= stack.getCount(); i++) {
                    if(itemEntity.getRandom().nextInt(3) == 1) {
                        successCount++;
                    }
                }
                Optional<PolarizingRecipe> recipe = PolarizerCommons.getRecipe(level, stack.copyWithCount(1)).map(RecipeHolder::value);
                if (successCount > 0 && recipe.isPresent()) {
                    ItemStack assembled = PolarizerCommons.assembleResult(level, itemEntity.position(), recipe.get());
                    itemEntity.setItem(assembled.copyWithCount(successCount));
                }
                event.setCanceled(true);
                event.getLightning().discard();
            }
        }
    }
}
