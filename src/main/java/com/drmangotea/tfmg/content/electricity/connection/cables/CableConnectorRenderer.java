package com.drmangotea.tfmg.content.electricity.connection.cables;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.SpoolItem;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class CableConnectorRenderer extends SafeBlockEntityRenderer<CableConnectorBlockEntity> {


    public CableConnectorRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(CableConnectorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        renderPlayerHeldCable(be, ms, bufferSource, partialTicks);

        Player player = Minecraft.getInstance().player;



        for (CableConnection connection : be.connections) {
            if (connection.visible)
                TFMGUtils.renderWire(be.getLevel(), ms, bufferSource, connection.pos2, connection.pos1, connection.getLength() / 4500, new Color(connection.type.getColor()).getRed(), new Color(connection.type.getColor()).getGreen(), new Color(connection.type.getColor()).getBlue());
        }

        //for (BlockPos connection : be.connections) {
//
        //    BlockPos pos = connection;
        //   // if (connection.visible)
        //        TFMGUtils.renderWire(be.getLevel(), ms, bufferSource, new CablePos(pos.getX(),pos.getY(),pos.getZ()), be.getCablePosition(), 0, new Color(CableConnection.CableType.COPPER.color).getRed(), new Color(CableConnection.CableType.COPPER.color).getGreen(), new Color(CableConnection.CableType.COPPER.color).getBlue());
        //}

    }

    public void renderPlayerHeldCable(CableConnectorBlockEntity be, PoseStack ms, MultiBufferSource bufferSource, float partialTicks) {
        if (be.player == null)
            return;
        Player player = be.player;
        if (player.getInventory().contains(TFMGTags.TFMGItemTags.SPOOLS.tag)) {
            ItemStack stack = player.getMainHandItem();
            if (stack.getOrCreateTag().getLong("Position") != 0) {

                CablePos position = new CablePos(stack.getOrCreateTag().getDouble("XPos"), stack.getOrCreateTag().getDouble("YPos"), stack.getOrCreateTag().getDouble("ZPos"));
                BlockPos pos = BlockPos.of(stack.getOrCreateTag().getLong("Position"));

                if (pos.equals(be.getBlockPos()))
                     TFMGUtils.renderWire(be.getLevel(), ms, bufferSource, new CablePos(be.wireMovementX.getValue(partialTicks), be.wireMovementY.getValue(partialTicks), be.wireMovementZ.getValue(partialTicks)), position, 0.0001f, new Color(be.color).getRed(), new Color(be.color).getGreen(), new Color(be.color).getBlue());
                   // TFMGUtils.renderWire(be.getLevel(), ms, bufferSource, new CablePos(be.player.getX(), be.player.getY(), be.player.getZ()), position, 0.0001f, new Color(be.color).getRed(), new Color(be.color).getGreen(), new Color(be.color).getBlue());
                    //TFMGUtils.renderWire(be.getLevel(), ms, bufferSource, position, new CablePos(be.player.getX(), be.player.getY(), be.player.getZ()), 0.0001f, new Color(be.color).getRed(), new Color(be.color).getGreen(), new Color(be.color).getBlue());
            }
        }
    }
}
