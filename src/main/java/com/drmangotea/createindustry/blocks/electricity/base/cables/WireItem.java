package com.drmangotea.createindustry.blocks.electricity.base.cables;

import com.drmangotea.createindustry.blocks.electricity.base.IHaveCables;
import com.drmangotea.createindustry.items.weapons.advanced_potato_cannon.AdvancedPotatoCannonItemRenderer;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class WireItem extends Item {



    public WireManager.Conductor material;

    public WireItem(Properties p_41383_, WireManager.Conductor material) {
        super(p_41383_);
        this.material = material;
    }



    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = context.getItemInHand();

        Player player = context.getPlayer();





        if(state.getBlock() instanceof IHaveCables){


            if(stack.getOrCreateTag().getInt("X1")!=0&&
                    stack.getOrCreateTag().getInt("Y1")!=0&&
                    stack.getOrCreateTag().getInt("Z1")!=0
            ){

                boolean placeWire = true;

                if(level.getBlockEntity(pos)!=null)
                    if(level.getBlockEntity(pos) instanceof CableConnectorBlockEntity be){


                        if(testConnection(stack,be,pos)) {
                            BlockPos pos2 = new BlockPos(stack.getOrCreateTag().getInt("X1"), stack.getOrCreateTag().getInt("Y1"), stack.getOrCreateTag().getInt("Z1"));
                            if(level.getBlockEntity(pos2) instanceof CableConnectorBlockEntity be2) {

                                if(be2.getBlockPos() == be.getBlockPos())
                                    return InteractionResult.PASS;

                                if(be.getBlockState().getValue(CableConnectorBlock.EXTENSION)||be2.getBlockState().getValue(CableConnectorBlock.EXTENSION))
                                    return InteractionResult.PASS;



                                be.addConnection(material, pos2, true);
                                be.sendData();
                                be.setChanged();
                                be2.addConnection(material, pos, false);
                                be2.sendData();
                                be2.setChanged();
                            }


                        }
                    }

                if(state.getValue(CableConnectorBlock.EXTENSION))
                    return InteractionResult.PASS;

                stack.getOrCreateTag().putInt("X1",0);
                stack.getOrCreateTag().putInt("Y1",0);
                stack.getOrCreateTag().putInt("Z1",0);





            if(player.isCreative()) {
                player.setItemInHand(context.getHand(), new ItemStack(TFMGItems.COPPER_CABLE.get(), stack.getCount()));
            }else            player.setItemInHand(context.getHand(), new ItemStack(TFMGItems.COPPER_CABLE.get(), stack.getCount() - 1));


            //if(!placeWire)
            //    player.kill();

            return InteractionResult.SUCCESS;
            }


            stack.getOrCreateTag().putInt("X1",pos.getX());
            stack.getOrCreateTag().putInt("Y1",pos.getY());
            stack.getOrCreateTag().putInt("Z1",pos.getZ());
            if(level.getBlockEntity(pos) instanceof CableConnectorBlockEntity connector)
                connector.player = player;


            return InteractionResult.SUCCESS;
        }


        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if(player.isCrouching()){


            BlockPos pos = new BlockPos(stack.getOrCreateTag().getInt("X1"),stack.getOrCreateTag().getInt("Y1"),stack.getOrCreateTag().getInt("Z1"));


            if(level.getBlockEntity(pos) instanceof CableConnectorBlockEntity be){
                be.player = null;
            }


            stack = new ItemStack(stack.getItem(),stack.getCount());

            if (!level.isClientSide)
                player.displayClientMessage(Lang.translateDirect("wires.removed_data")
                        .withStyle(ChatFormatting.YELLOW), true);

            return InteractionResultHolder.success(stack);
        }


        return super.use(level, player, hand);
    }

    private boolean testConnection(ItemStack stack, CableConnectorBlockEntity be, BlockPos pos) {






        for(WireConnection connection : be.wireConnections){

            if(
                connection.point1.getX() == stack.getOrCreateTag().getInt("X1")&&
                connection.point1.getY() == stack.getOrCreateTag().getInt("Y1")&&
                connection.point1.getZ() == stack.getOrCreateTag().getInt("Z1")&&
                connection.point2.getX() == pos.getX()&&
                connection.point2.getY() == pos.getY()&&
                connection.point2.getZ() == pos.getZ()


            ){
              return false;
            }
            if(
               connection.point2.getX() == stack.getOrCreateTag().getInt("X1")&&
               connection.point2.getY() == stack.getOrCreateTag().getInt("Y1")&&
               connection.point2.getZ() == stack.getOrCreateTag().getInt("Z1")&&
               connection.point1.getX() == pos.getX()&&
               connection.point1.getY() == pos.getY()&&
               connection.point1.getZ() == pos.getZ()

            ){
                return false;
            }

        }




        return true;
    }


}
