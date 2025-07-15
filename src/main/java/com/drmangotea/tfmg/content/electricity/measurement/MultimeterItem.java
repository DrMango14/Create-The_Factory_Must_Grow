package com.drmangotea.tfmg.content.electricity.measurement;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class MultimeterItem extends Item {
    public MultimeterItem(Properties p_41383_) {
        super(p_41383_);
    }

    public static boolean isHeldByPlayer(Player player){
        return player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof MultimeterItem || player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof MultimeterItem;
    }
}
