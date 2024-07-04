package com.drmangotea.createindustry.blocks.electricity.base.cables;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;

public class CableConnectorRenderer extends SafeBlockEntityRenderer<CableConnectorBlockEntity> {
    public CableConnectorRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    protected void renderSafe(CableConnectorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {


        for(WireConnection wireConnection : be.wireConnections){
        if(wireConnection.shouldRender)
            WireManager.renderWire(ms,bufferSource,wireConnection.point1,wireConnection.point2,0,0,0,0,0,0, wireConnection.lenght/4500);

        }

        //if(be.player !=null){
        //    BlockPos pos = new BlockPos(be.player.getX(),be.player.getY(),be.player.getZ());
//
        //    WireManager.renderWire(ms,bufferSource,pos,be.getBlockPos(),0,0,0,0,0,0, 10f/4500f);
//
        //}

    }
}
