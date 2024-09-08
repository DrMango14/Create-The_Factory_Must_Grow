package com.drmangotea.tfmg.blocks.electricity.debug;

import com.drmangotea.tfmg.CreateTFMG;
import com.drmangotea.tfmg.blocks.electricity.base.cables.ElectricNetworkManager;
import com.drmangotea.tfmg.blocks.electricity.base.cables.IElectric;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;

public class DebugRockItem extends Item {
    public DebugRockItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (level.getBlockEntity(context.getClickedPos()) instanceof BarrelBlockEntity) {

        if(!ElectricNetworkManager.networks.isEmpty())
            ElectricNetworkManager.networks.get(level).forEach((a,b)->level.setBlock(BlockPos.of(a),Blocks.GOLD_BLOCK.defaultBlockState(),3));

        }

        if (level.getBlockEntity(context.getClickedPos()) instanceof FurnaceBlockEntity) {


            ElectricNetworkManager.networks.clear();

        }



        if(player.isShiftKeyDown()) {
            if (level.getBlockEntity(context.getClickedPos()) instanceof IElectric be) {

                for (IElectric member : be.getOrCreateElectricNetwork().members) {

                    level.setBlock(BlockPos.of(member.getId()).above(), Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);

                }

            }
        }else {
            if (level.getBlockEntity(context.getClickedPos()) instanceof IElectric be) {

                be.getOrCreateElectricNetwork().members.removeIf(member->!(level.getBlockEntity(BlockPos.of(member.getId()))instanceof IElectric));
                be.getOrCreateElectricNetwork().members.removeIf(member->member.getNetwork()!=be.getNetwork());
            }
        }

        return InteractionResult.SUCCESS;
    }
}
