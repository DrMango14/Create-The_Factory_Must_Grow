package com.drmangotea.tfmg.content.items.weapons.fire_extinguisher;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.spark.DryIceFlake;
import com.drmangotea.tfmg.registry.TFMGEntityTypes;
import com.drmangotea.tfmg.registry.TFMGSoundEvents;
import com.simibubi.create.foundation.item.CustomArmPoseItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.drmangotea.tfmg.registry.TFMGDataComponents.AMOUNT;

@ParametersAreNonnullByDefault
public class FireExtinguisherItem extends Item implements CustomArmPoseItem {
    public static final int DRY_ICE_CAPACITY = 500;

    public FireExtinguisherItem(Properties pProperties) {
        super(pProperties);
    }

    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int time) {
        int fillLevel = stack.getOrDefault(AMOUNT,0);
        if(fillLevel == 0) return;

        Vec3 barrelPos = TFMGUtils.getGunBarrelVec(entity, entity.getUsedItemHand() == InteractionHand.MAIN_HAND,
                new Vec3(.75f, -0.45f, 1.5f));

        DryIceFlake flake = TFMGEntityTypes.DRY_ICE_FLAKE.create(level);
        if (flake != null) {
            flake.setPos(barrelPos.x, barrelPos.y, barrelPos.z);
            flake.shoot(entity.getLookAngle().x,entity.getLookAngle().y,entity.getLookAngle().z,0.5f,10.0f);
            level.addFreshEntity(flake);
        }
        stack.set(AMOUNT, fillLevel > 0 ? fillLevel - 1 : 0);
        TFMGSoundEvents.FIRE_EXTINGUISHER.playFrom(entity, 1F, 0.04F);
        if (stack.getOrDefault(AMOUNT, 0) == 0) {
            entity.stopUsingItem();
        }
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        stack.set(AMOUNT, 500);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getOrDefault(AMOUNT, 0) > 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xffffff;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0f * stack.getOrDefault(AMOUNT, 0) / DRY_ICE_CAPACITY);
    }

    @Override @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).getOrDefault(AMOUNT, 0) > 0) {
            TFMGSoundEvents.FIRE_EXTINGUISHER_START.playFrom(player, 1F, 0.04F);
            player.startUsingItem(hand);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        super.onStopUsing(stack, entity, count);
        if (stack.getOrDefault(AMOUNT, 0) > 0) {
            TFMGSoundEvents.FIRE_EXTINGUISHER_FADE.playFrom(entity, 1F, 0.14F);
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || newStack.getItem() != oldStack.getItem();
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        return true;
    }

    @Override
    @Nullable
    public HumanoidModel.ArmPose getArmPose(ItemStack stack, AbstractClientPlayer player, InteractionHand hand) {
        if (!player.swinging) {
            return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        }
        return null;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 1000;
    }

    @Override @Nonnull
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.NONE;
    }
}
