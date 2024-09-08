package com.drmangotea.tfmg.base.tesla_coil_lightning;

import com.drmangotea.tfmg.CreateTFMG;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LightningBolt;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class TeslaCoilLightningRenderer extends EntityRenderer<TeslaCoilLightningEntity> {
    public TeslaCoilLightningRenderer(EntityRendererProvider.Context p_174286_) {
        super(p_174286_);
    }

    public void render(TeslaCoilLightningEntity lightning, float p_115267_, float p_115268_, PoseStack poseStack, MultiBufferSource source, int p_115271_) {
        float[] afloat = new float[8];
        float[] afloat1 = new float[8];
        float xTop = 0.0F;
        float zTop = 0.0F;

        float block = 0.03f;

        BlockPos pos = lightning.coilPos;

      //  CreateTFMG.LOGGER.debug("xDifference: "+lightning.coilPos.getX());


        float xDiff = (float) (lightning.getBlockX()<pos.getX() ? pos.getX()-lightning.getX() : lightning.getX()-pos.getX());
        float yDiff = (float) ((int) pos.getY()-((int)lightning.getY()));
        float zDiff = (float) ((int) pos.getZ()-((int)lightning.getZ()));

        xDiff = pos.getX();

   //     CreateTFMG.LOGGER.debug("xDifference: "+xDiff);


        RandomSource randomsource = RandomSource.create(lightning.seed);

        for(int i = 7; i >= 0; --i) {
            afloat[i] = xTop;
            afloat1[i] = zTop;





           // xTop += (float)(randomsource.nextFloat() -0.5f);
           // zTop += (float)(randomsource.nextFloat() -0.5f);


           xTop+=block*xDiff;
           //zTop+=block*2;






        }
        //xTop += 20;
        VertexConsumer vertexconsumer = source.getBuffer(RenderType.lightning());
        Matrix4f matrix4f = poseStack.last().pose();

        for(int j = 0; j < 4; ++j) {
            RandomSource randomsource1 = RandomSource.create(lightning.seed);


                int l = 7;


                int lenght = 8;



                float x = afloat[l] - xTop;
                float z = afloat1[l] - zTop;



                for(int j1 = lenght; j1 >= 1; --j1) {
                    float f4 = x;
                    float f5 = z;

                        x += (float)(randomsource1.nextFloat() -0.5f);
                        z += (float)(randomsource1.nextFloat() -0.5f);


                        x+=block*xDiff;
                        //z+=block*2;







                    quad(matrix4f, vertexconsumer, x, z, j1, f4, f5, 0.45F, 0.45F, 0.8F, 0.01f, 0.01f, false, false, true, false);
                    quad(matrix4f, vertexconsumer, x, z, j1, f4, f5, 0.45F, 0.45F, 0.8F, 0.01f, 0.01f, true, false, true, true);
                    quad(matrix4f, vertexconsumer, x, z, j1, f4, f5, 0.45F, 0.45F, 0.8F, 0.01f, 0.01f, true, true, false, true);
                    quad(matrix4f, vertexconsumer, x, z, j1, f4, f5, 0.45F, 0.45F, 0.8F, 0.01f, 0.01f, false, true, false, false);
                }
            }


    }

    private static void quad(Matrix4f matrix4f, VertexConsumer consumer, float p_115275_, float p_115276_, int y, float p_115278_, float p_115279_, float red, float green, float blue, float p_115283_, float p_115284_, boolean p_115285_, boolean p_115286_, boolean p_115287_, boolean p_115288_) {
        consumer.vertex(matrix4f, p_115275_ + (p_115285_ ? p_115284_ : -p_115284_), (float)(y/2), p_115276_ + (p_115286_ ? p_115284_ : -p_115284_)).color(red, green, blue, 0.3F).endVertex();
        consumer.vertex(matrix4f, p_115278_ + (p_115285_ ? p_115283_ : -p_115283_), (float)((y + 1)/2), p_115279_ + (p_115286_ ? p_115283_ : -p_115283_)).color(red, green, blue, 0.3F).endVertex();
        consumer.vertex(matrix4f, p_115278_ + (p_115287_ ? p_115283_ : -p_115283_), (float)((y + 1)/2), p_115279_ + (p_115288_ ? p_115283_ : -p_115283_)).color(red, green, blue, 0.3F).endVertex();
        consumer.vertex(matrix4f, p_115275_ + (p_115287_ ? p_115284_ : -p_115284_), (float)(y/2), p_115276_ + (p_115288_ ? p_115284_ : -p_115284_)).color(red, green, blue, 0.3F).endVertex();
    }

    public ResourceLocation getTextureLocation(TeslaCoilLightningEntity p_115264_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}