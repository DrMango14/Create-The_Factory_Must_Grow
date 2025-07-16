package com.drmangotea.tfmg.content.items.weapons.fire_extinguisher;

import com.drmangotea.tfmg.TFMGClient;
import com.drmangotea.tfmg.base.spark.DryIceFlake;
import com.drmangotea.tfmg.registry.TFMGEntityTypes;
import com.simibubi.create.foundation.item.CustomArmPoseItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class FireExtinguisherItem extends Item implements CustomArmPoseItem {


    public static final int DRY_ICE_CAPACITY = 500;


    public FireExtinguisherItem(Properties pProperties) {
        super(pProperties);
    }


    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int time) {

        CompoundTag nbt = stack.getOrCreateTag();

        int fillLevel = nbt.getInt("fill_level");
        if(fillLevel == 0) return;

        DryIceFlake flake = TFMGEntityTypes.DRY_ICE_FLAKE.create(level);
        flake.setPos(entity.getX(),entity.getY()+1.2f,entity.getZ());

        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 0.1F, 0.04F);

        nbt.putInt("fill_level",fillLevel > 0? fillLevel - 1 : 0);

        flake.shoot(entity.getLookAngle().x,entity.getLookAngle().y,entity.getLookAngle().z,0.5f,10.0f);

        level.addFreshEntity(flake);

    }




    public int getUseDuration(ItemStack stack) {
        return 696969;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xffffff;
    }

    @Override
    public int getBarWidth(ItemStack stack) {

        float fillLevel = (float)stack.getOrCreateTag().getInt("fill_level") / (float)DRY_ICE_CAPACITY;
        return Math.round(13.0f * fillLevel);

    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);

        if (level.isClientSide) {
            TFMGClient.FIRE_EXTINGUISHER_RENDER_HANDLER.dontAnimateItem(hand);
        }

        return InteractionResultHolder.pass(player.getItemInHand(hand));
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
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.NONE;
    }
}
