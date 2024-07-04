package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.CreateTFMG;
import com.google.common.base.Suppliers;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum TFMGArmorMaterials implements ArmorMaterial {


    STEEL(CreateTFMG.asResource("steel").toString(), 30, new int[]{3, 6, 8, 3}, 18, () -> SoundEvents.ARMOR_EQUIP_NETHERITE, 2.0F, 0.1F,
            () -> Ingredient.of(TFMGItems.STEEL_INGOT.get()))

    ;


    private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final Supplier<SoundEvent> soundEvent;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairMaterial;

    private TFMGArmorMaterials(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability,
                              Supplier<SoundEvent> soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) {
        this.name = name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReductionAmountArray;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = Suppliers.memoize(repairMaterial::get);
    }




    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return switch (type){

            case HELMET -> MAX_DAMAGE_ARRAY[3] * this.maxDamageFactor;
            case CHESTPLATE -> MAX_DAMAGE_ARRAY[2] * this.maxDamageFactor;
            case LEGGINGS -> MAX_DAMAGE_ARRAY[1] * this.maxDamageFactor;
            case BOOTS -> MAX_DAMAGE_ARRAY[0] * this.maxDamageFactor;
        };
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return switch (type){

            case HELMET -> damageReductionAmountArray[3];
            case CHESTPLATE -> damageReductionAmountArray[2];
            case LEGGINGS -> damageReductionAmountArray[1];
            case BOOTS -> damageReductionAmountArray[0];
        };
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.soundEvent.get();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
