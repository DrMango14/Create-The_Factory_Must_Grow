package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.TFMG;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum TFMGTiers implements Tier {





    STEEL(TFMG.asResource("steel").toString(),1000, 7.5f, 3f, 12,()-> Ingredient.of(CommonMetal.STEEL.ingots)),
    ALUMINUM(TFMG.asResource("aluminum").toString(),220, 6, 2f, 22,()-> Ingredient.of(CommonMetal.ALUMINUM.ingots)),
    LEAD(TFMG.asResource("lead").toString(),32, 2, 0.5f, 5,()-> Ingredient.of(CommonMetal.LEAD.ingots));


    public final String name;

    private final int uses;
    private final float speed;
    private final float damageBonus;
    private final int enchantValue;
    private final Supplier<Ingredient> repairMaterial;

    private TFMGTiers(String name, int uses, float speed, float damageBonus, int enchantValue,
                             Supplier<Ingredient> repairMaterial) {
        this.name = name;
        this.uses = uses;
        this.speed = speed;
        this.damageBonus = damageBonus;
        this.enchantValue = enchantValue;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return damageBonus;
    }

    @Override
    public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
        return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantValue;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return repairMaterial.get();
    }
}
