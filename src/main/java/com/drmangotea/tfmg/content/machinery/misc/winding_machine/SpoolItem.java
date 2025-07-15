package com.drmangotea.tfmg.content.machinery.misc.winding_machine;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnection;
import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnectorBlockEntity;
import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.foundation.utility.CreateLang;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


import java.util.List;

import static com.simibubi.create.foundation.utility.Debug.debugMessage;

public class SpoolItem extends Item {

    public final PartialModel model;
    public final int barColor;
    public final CableConnection.CableType type;

    public SpoolItem(Properties properties, PartialModel model, int barColor, CableConnection.CableType type) {
        super(properties);
        this.model = model;
        this.barColor = barColor;
        this.type = type;
    }


    @Override
    public void onCraftedBy(ItemStack stack, Level p_41448_, Player p_41449_) {
        stack.set(TFMGDataComponents.SPOOL_AMOUNT,1000);
        super.onCraftedBy(stack, p_41448_, p_41449_);

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isCrouching() && stack.get(TFMGDataComponents.POSITION) != 0) {
            if (level.getBlockEntity(BlockPos.of(stack.get(TFMGDataComponents.POSITION))) instanceof CableConnectorBlockEntity be)
                be.player = null;
            stack.set(TFMGDataComponents.POSITION, 0l);
            stack.remove(TFMGDataComponents.POSITION);
            stack.remove(TFMGDataComponents.X_POS);
            stack.remove(TFMGDataComponents.Y_POS);
            stack.remove(TFMGDataComponents.Z_POS);
            if (level.isClientSide)
                player.displayClientMessage(CreateLang.translateDirect("wires.removed_data")
                        .withStyle(ChatFormatting.YELLOW), true);
            return InteractionResultHolder.success(stack);

        }

        return super.use(level, player, hand);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {



        tooltip.add(CreateLang.translateDirect("tooltip.coils", stack.get(TFMGDataComponents.SPOOL_AMOUNT)==null?0:stack.get(TFMGDataComponents.SPOOL_AMOUNT))
                .withStyle(ChatFormatting.GREEN)
        );
        if(stack.get(TFMGDataComponents.POSITION)==null)
            return;
        BlockPos pos = BlockPos.of(stack.get(TFMGDataComponents.POSITION));
        if(pos.asLong()!=0)
            tooltip.add(CreateLang.text("" + pos.getX() + " " + pos.getY() + " " + pos.getZ()).component()
                    .withStyle(ChatFormatting.AQUA)
            );
        super.appendHoverText(stack, context, tooltip, flag);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if(stack.get(TFMGDataComponents.SPOOL_AMOUNT)==null)
            return InteractionResult.PASS;
        if(level.isClientSide)
            return InteractionResult.SUCCESS;

        if(type == CableConnection.CableType.NONE)
            return InteractionResult.PASS;

         if(level.getBlockEntity(pos) instanceof CableConnectorBlockEntity be){
             if(stack.get(TFMGDataComponents.POSITION)!=null){
                 BlockPos posToConnect = BlockPos.of(stack.get(TFMGDataComponents.POSITION));
                 if(posToConnect == pos){
                     stack.set(TFMGDataComponents.POSITION,0l);
                     if (level.isClientSide)
                         player.displayClientMessage(CreateLang.translateDirect("wires.cant_connect_itself")
                                 .withStyle(ChatFormatting.YELLOW), true);
                     be.player = null;
                     be.sendData();
                     be.setChanged();
                     return InteractionResult.SUCCESS;
                 }

                 if(level.getBlockEntity(posToConnect) instanceof CableConnectorBlockEntity otherBE) {
                     //CableConnectorBlockEntity connectedBe1 = pos.asLong()>posToConnect.asLong() ? otherBE : be;
                     //CableConnectorBlockEntity connectedBe2= pos.asLong()>posToConnect.asLong() ? be : otherBE;
//
                     CableConnection connection1 = new CableConnection(be.getCablePosition(), otherBE.getCablePosition(), otherBE.getBlockPos(),type,true);
                     CableConnection connection2 = new CableConnection(otherBE.getCablePosition(), be.getCablePosition(), be.getBlockPos(),type,false);

                     float wireCost =  (connection1.getLength()/8);


                     if(stack.get(TFMGDataComponents.SPOOL_AMOUNT)<wireCost*125) {
                         return InteractionResult.PASS;
                     }
                     if(be.connections.contains(connection1)||otherBE.connections.contains(connection1)){
                         if (level.isClientSide)
                             player.displayClientMessage(CreateLang.translateDirect("wires.connection_already_created")
                                     .withStyle(ChatFormatting.YELLOW), true);
                         be.player = null;
                         be.sendData();
                         be.setChanged();
                         return InteractionResult.SUCCESS;
                     }
                   //  if(!level.isClientSide) {
                         be.connections.add(connection1);
                         otherBE.connections.add(connection2);
                         be.onPlaced();
                      //   otherBE.onPlaced();
                     //}

                   //  connectedBe1.wiresUpdated();
                     stack.set(TFMGDataComponents.SPOOL_AMOUNT, (int) (stack.get(TFMGDataComponents.SPOOL_AMOUNT)-(wireCost*125)));
                     be.player = null;
                     otherBE.player = null;
                     be.setChanged();
                     otherBE.setChanged();
                     be.sendData();
                     otherBE.sendData();
                     stack.remove(TFMGDataComponents.POSITION);
                     stack.remove(TFMGDataComponents.X_POS);
                     stack.remove(TFMGDataComponents.Y_POS);
                     stack.remove(TFMGDataComponents.Z_POS);
                 }
                 //
                 be.player = null;
//
                 return InteractionResult.SUCCESS;
             }else {
                 stack.set(TFMGDataComponents.POSITION, be.getBlockPos().asLong());
                 stack.set(TFMGDataComponents.X_POS, (int)be.getCablePosition().x());
                 stack.set(TFMGDataComponents.Y_POS, (int)be.getCablePosition().y());
                 stack.set(TFMGDataComponents.Z_POS, (int)be.getCablePosition().z());
                 be.player = player;
                 be.color = barColor;
                 be.sendData();
                 be.setChanged();
                 return InteractionResult.SUCCESS;
             }
         }
//
//
         if(level.getBlockEntity(pos) instanceof WindingMachineBlockEntity be){
             ItemStack oldSpool = ItemStack.EMPTY;
             if(!be.spool.isEmpty()){
                 oldSpool = be.spool;
             }
             be.spool = context.getItemInHand();
             context.getPlayer().setItemInHand(context.getHand(), oldSpool);
             be.sendData();
             be.setChanged();
//
             return InteractionResult.SUCCESS;
         }
         return InteractionResult.PASS;
         }
        public void removeOtherConnections(Player player, ItemStack stack){
            for(int i =0;i<player.getInventory().getContainerSize();i++){
                ItemStack inventoryStack = player.getInventory().getItem(i);
//

//
            }
        }



    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean p_41408_) {
        super.inventoryTick(stack, level, entity, slot, p_41408_);

        if(stack.get(TFMGDataComponents.SPOOL_AMOUNT)==null)
            return;

        if(stack.get(TFMGDataComponents.SPOOL_AMOUNT)==0&& entity instanceof Player player&&!stack.is(TFMGItems.EMPTY_SPOOL.get())){
            player.getInventory().setItem(slot, TFMGItems.EMPTY_SPOOL.asStack());
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return model != null;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return barColor;
    }

    @Override
    public int getBarWidth(ItemStack stack) {

        if(stack.get(TFMGDataComponents.SPOOL_AMOUNT)==null)
            return 13;

        return (int) (13f*((float)stack.get(TFMGDataComponents.SPOOL_AMOUNT)/1000));
    }

}
