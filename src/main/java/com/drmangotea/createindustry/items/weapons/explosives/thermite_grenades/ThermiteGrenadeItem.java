package com.drmangotea.createindustry.items.weapons.explosives.thermite_grenades;

import com.drmangotea.createindustry.registry.TFMGEntityTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ThermiteGrenadeItem extends Item {

    public final ChemicalColor flameColor;

    public ThermiteGrenadeItem(Properties p_41383_, ChemicalColor color) {
        super(p_41383_);
        this.flameColor = color;
    }

    public InteractionResultHolder<ItemStack> use(Level p_43142_, Player p_43143_, InteractionHand p_43144_) {
        ItemStack itemstack = p_43143_.getItemInHand(p_43144_);
        p_43143_.getCooldowns().addCooldown(this, 60);
        p_43142_.playSound((Player)null, p_43143_.getX(), p_43143_.getY(), p_43143_.getZ(), SoundEvents.EGG_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (p_43142_.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!p_43142_.isClientSide) {
            ThermiteGrenade grenade;
            if(flameColor==ChemicalColor.GREEN) {
                grenade = new ThermiteGrenade(p_43142_, p_43143_, flameColor, TFMGEntityTypes.ZINC_GRENADE.get());
            }else if(flameColor==ChemicalColor.BLUE) {
                grenade = new ThermiteGrenade(p_43142_, p_43143_, flameColor, TFMGEntityTypes.COPPER_GRENADE.get());
            }else {
                grenade = new ThermiteGrenade(p_43142_, p_43143_, flameColor, TFMGEntityTypes.THERMITE_GRENADE.get());
            }
            grenade.setItem(itemstack);
            grenade.shootFromRotation(p_43143_, p_43143_.getXRot(), p_43143_.getYRot(), 0.0F, 0.5F, 1.0F);
            p_43142_.addFreshEntity(grenade);
        }

        p_43143_.awardStat(Stats.ITEM_USED.get(this));
        if (!p_43143_.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, p_43142_.isClientSide());
    }
}
