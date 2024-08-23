package com.drmangotea.createindustry.registry;

import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum TFMGTiers implements Tier {


    STEEL(3,1000,7.5f,3f,12,() -> {
        return Ingredient.of(TFMGItems.STEEL_INGOT.get());
    }),

    ALUMINUM(2,220,6,2f,22,() -> {
        return Ingredient.of(TFMGItems.ALUMINUM_INGOT.get());
    }),
    LEAD(1,32,2,0.5f,5,() -> {
        return Ingredient.of(TFMGItems.LEAD_INGOT.get());
    })


;
    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private TFMGTiers(int pLevel, int pUses, float pSpeed, float pDamage, int pEnchantmentValue, Supplier<Ingredient> pRepairIngredient) {
        this.level = pLevel;
        this.uses = pUses;
        this.speed = pSpeed;
        this.damage = pDamage;
        this.enchantmentValue = pEnchantmentValue;
        this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
