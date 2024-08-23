package com.drmangotea.createindustry.items.weapons.lithium_blade;

import com.drmangotea.createindustry.registry.TFMGEntityTypes;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.drmangotea.createindustry.registry.TFMGMobEffects;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class LitLithiumBladeItem extends SwordItem {
    public LitLithiumBladeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(2, pAttacker, (p_41007_) -> {
            p_41007_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        MobEffectInstance poison = pTarget.getEffect(TFMGMobEffects.HELLFIRE.get());

        if(poison!=null) {
            pTarget.addEffect(new MobEffectInstance(TFMGMobEffects.HELLFIRE.get(), 140 + poison.getDuration()));
        }
        pTarget.addEffect(new MobEffectInstance(TFMGMobEffects.HELLFIRE.get(),140));
        return true;
    }




    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {



        //   :3

       //for (Map.Entry<ResourceKey<MobEffect>, MobEffect> effect : ForgeRegistries.MOB_EFFECTS.getEntries()) {
       //    player.addEffect(new MobEffectInstance(, 60));
       //}





        ItemStack stack = player.getItemInHand(hand);

        CompoundTag nbt = stack.getOrCreateTag();

        if(nbt.getInt("time")<=100)
            return super.use(level, player, hand);

        nbt.putInt("time",nbt.getInt("time")-100);

        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 0.5F, 0.4F);


        Vec3 motion = player.getLookAngle();

        for(int i = 0;i<10;i++){


            LithiumSpark spark = TFMGEntityTypes.LITHIUM_SPARK.create(level);

            spark.setPos(player.getX(),player.getY()+1.3,player.getZ());


            spark.burst(player.getLookAngle().x,player.getLookAngle().y,player.getLookAngle().z,1,30);

            level.addFreshEntity(spark);

        }


        player.getCooldowns().addCooldown(TFMGItems.LIT_LITHIUM_BLADE.get(),60);




        return super.use(level, player, hand);
    }


     @Override
     public boolean isBarVisible(ItemStack pStack) {
         return true;
     }

     @Override
     public int getBarColor(ItemStack pStack) {
         return 0xDD0B13;
     }

     @Override
     public int getBarWidth(ItemStack pStack) {
         return (int) ((((float)pStack.getOrCreateTag().getInt("time")/(float)LithiumBladeItem.MAX_TIME)*12) + 1);
     }

    @Override
    public void inventoryTick(ItemStack stack, Level pLevel, Entity entity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(stack, pLevel, entity, pSlotId, pIsSelected);

        CompoundTag nbt = stack.getOrCreateTag();

        if(nbt.getInt("time")>0){
            nbt.putInt("time",nbt.getInt("time")-1);



        }else {
            ItemStack stack1 = new ItemStack(TFMGItems.LITHIUM_BLADE.get(),1,stack.getOrCreateTag());


            Map<Enchantment, Integer> enchantments = stack.getAllEnchantments();

            enchantments.forEach(stack1::enchant);



            ((Player)entity).getInventory().setItem(pSlotId,stack1);
        }

    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {

        if(!slotChanged)
            return false;

        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }

    @Override
    public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
        //nah
    }


}
