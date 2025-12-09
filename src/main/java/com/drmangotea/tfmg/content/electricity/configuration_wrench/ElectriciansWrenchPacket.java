package com.drmangotea.tfmg.content.electricity.configuration_wrench;


import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class ElectriciansWrenchPacket extends SimplePacketBase {


    int group;
    protected InteractionHand hand;

    public ElectriciansWrenchPacket(int group, InteractionHand hand) {
        this.group = group;
        this.hand = hand;
    }

    public ElectriciansWrenchPacket(FriendlyByteBuf buffer) {
        this.group = buffer.readInt();
        hand = buffer.readEnum(InteractionHand.class);
    }


    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(group);
        buffer.writeEnum(hand);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof ElectriciansWrenchItem) {
                applyGroup(stack);
            }
        });

        return true;
    }

    public void applyGroup(ItemStack stack){


        stack.getOrCreateTag().putInt("Number", group);
    }

}
