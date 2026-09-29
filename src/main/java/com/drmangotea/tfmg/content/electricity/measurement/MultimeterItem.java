package com.drmangotea.tfmg.content.electricity.measurement;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class MultimeterItem extends Item {

    public final int color;

    public MultimeterItem(Properties p_41383_,int color) {
        super(p_41383_);
        this.color = color;

    }

    public static boolean isHeldByPlayer(Player player){
        return player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof MultimeterItem || player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof MultimeterItem;
    }
}
