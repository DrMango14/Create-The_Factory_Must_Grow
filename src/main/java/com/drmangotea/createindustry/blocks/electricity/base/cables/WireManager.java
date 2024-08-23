package com.drmangotea.createindustry.blocks.electricity.base.cables;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;


public class WireManager {

    public static void renderWire(PoseStack pMatrixStack, MultiBufferSource pBuffer, BlockPos pos1, BlockPos pos2,
                                  float offsetX1,float offsetY1,float offsetZ1,float offsetX2,float offsetY2,float offsetZ2,float curve) {
       pMatrixStack.pushPose();


        Vec3 vec3 = new Vec3(0,0,0);


            BlockPos pos2Local = pos1.subtract(pos2);


            pMatrixStack.translate(0.5+offsetX1, 0.5+offsetY1, 0.5+offsetZ1);

            vec3 =  vec3.add(pos2Local.getX()-offsetX1+offsetX2+0.01,pos2Local.getY()-offsetY1+offsetY2,pos2Local.getZ()-offsetZ1+offsetZ2+0.01);



        float f = (float)(vec3.x);
        float f1 = (float)(vec3.y );
        float f2 = (float)(vec3.z);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.leash());
        Matrix4f matrix4f = pMatrixStack.last().pose();
        float f4 = Mth.fastInvSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
        float f5 = f2 * f4;
        float f6 = f * f4;


        int i =15;
        int j = 15;

        //int i = this.getBlockLightLevel(pEntityLiving, blockpos);
        //int j = this.entityRenderDispatcher.getRenderer(pLeashHolder).getBlockLightLevel(pLeashHolder, blockpos1);
        //int k = pEntityLiving.level.getBrightness(LightLayer.SKY, blockpos);
        //int l = pEntityLiving.level.getBrightness(LightLayer.SKY, blockpos1);


        int k = 15;
        int l = 15;


        for(int i1 = 0; i1 <= 24; ++i1) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.030F, 0.030F, f5, f6, i1, false,curve);
        }

        for(int j1 = 24; j1 >= 0; --j1) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.030F, 0.00F, f5, f6, j1, true,curve);
        }

        pMatrixStack.popPose();
    }








    private static void addVertexPair(VertexConsumer vertexConsumer, Matrix4f matrix4f, float p_174310_, float p_174311_, float p_174312_, int p_174313_, int p_174314_, int p_174315_, int p_174316_, float thickness, float p_174318_, float p_174319_, float p_174320_, int value, boolean p_174322_, float curve) {
        float f = (float)(value / 24.0F);
        int i = (int)Mth.lerp(f, (float)p_174313_, (float)p_174314_);
        int j = (int)Mth.lerp(f, (float)p_174315_, (float)p_174316_);
        int k = LightTexture.pack(i, j);
        float f1 = value % 2 == (p_174322_ ? 1 : 0) ? 0.7F : 1.0F;
        float red = 0.1F * f1;
        float green = 0.1F * f1;
        float blue = 0.1F * f1;
        float x = p_174310_ * f;

        float pain;


        pain = ((value*curve*24)-(value*value*curve)) * -1f;




        float y = p_174311_ > 0.0F ? p_174311_ * f * f : p_174311_ - p_174311_ * (1.0F - f) * (1.0F - f);
        float z = p_174312_ * f;
        vertexConsumer.vertex(matrix4f, x - p_174319_, y + p_174318_+pain, z + p_174320_).color(red, green, blue, 1.0F).uv2(k).endVertex();
        vertexConsumer.vertex(matrix4f, x + p_174319_, y + thickness - p_174318_+pain, z - p_174320_).color(red, green, blue, 1.0F).uv2(k).endVertex();
    }


    public enum Conductor implements StringRepresentable {

        COPPER(0.0178f,"copper")
        ;

        public float resistivity;

        private String name;

        Conductor(float resistivity, String name){
            this.resistivity = resistivity;
            this.name = name;
        }


        public float getResistivity() {
            return resistivity;
        }

        @Override
        public String getSerializedName() {
            return null;
        }
    }

}
