package com.drmangotea.createindustry.registry;


import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.effects.FrostyEffect;
import com.drmangotea.createindustry.base.effects.HellFireEffect;
import com.drmangotea.createindustry.base.util.ProperBrewingRecipe;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;

public class TFMGMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CreateTFMG.MOD_ID);

    public static final RegistryObject<MobEffect> HELLFIRE = MOB_EFFECTS.register("hellfire", () -> new HellFireEffect(MobEffectCategory.HARMFUL, new Color(150, 0, 0, 200).getRGB()));
    public static final RegistryObject<MobEffect> FROSTY = MOB_EFFECTS.register("frostbite", () -> new FrostyEffect(MobEffectCategory.HARMFUL, new Color(153, 233, 238, 200).getRGB()));
    
    
    public static void register(IEventBus modEventBus){
        MOB_EFFECTS.register(modEventBus);
    }
    
    public static ItemStack createPotion(RegistryObject<Potion> potion){
        return  PotionUtils.setPotion(new ItemStack(Items.POTION), potion.get());
    }
    
    public static ItemStack createPotion(Potion potion){
        return  PotionUtils.setPotion(new ItemStack(Items.POTION), potion);
    }
    
    public static void init(){
        BrewingRecipeRegistry.addRecipe(new ProperBrewingRecipe(Ingredient.of(createPotion(Potions.AWKWARD)), Ingredient.of(TFMGItems.LITHIUM_INGOT.get()), createPotion(TFMGPotions.HELLFIRE_POTION)));
        BrewingRecipeRegistry.addRecipe(new ProperBrewingRecipe(Ingredient.of(createPotion(Potions.AWKWARD)), Ingredient.of(Items.BLUE_ICE), createPotion(TFMGPotions.FROSTY_POTION)));
        BrewingRecipeRegistry.addRecipe(new ProperBrewingRecipe(Ingredient.of(createPotion(TFMGPotions.HELLFIRE_POTION)), Ingredient.of(Items.REDSTONE), createPotion(TFMGPotions.LONG_HELLFIRE_POTION)));
        BrewingRecipeRegistry.addRecipe(new ProperBrewingRecipe(Ingredient.of(createPotion(TFMGPotions.FROSTY_POTION)), Ingredient.of(Items.REDSTONE), createPotion(TFMGPotions.LONG_FROSTY_POTION)));
    }
}
