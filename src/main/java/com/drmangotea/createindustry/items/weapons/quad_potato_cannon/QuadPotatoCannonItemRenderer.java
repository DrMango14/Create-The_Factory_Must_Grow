package com.drmangotea.createindustry.items.weapons.quad_potato_cannon;


import com.drmangotea.createindustry.CreateTFMGClient;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.equipment.potatoCannon.PotatoCannonItem;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class QuadPotatoCannonItemRenderer extends CustomRenderedItemModelRenderer {
    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer,
                          ItemTransforms.TransformType transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance()
                .getItemRenderer();
        renderer.render(model.getOriginalModel(), light);
        LocalPlayer player = Minecraft.getInstance().player;
        boolean mainHand = player.getMainHandItem() == stack;
        boolean offHand = player.getOffhandItem() == stack;
        boolean leftHanded = player.getMainArm() == HumanoidArm.LEFT;

        float offset = .5f / 16;
        float worldTime = AnimationTickHolder.getRenderTime() / 10;
        float angle = worldTime * -25;
        float speed = CreateTFMGClient.QUAD_POTATO_CANNON_RENDER_HANDLER.getAnimation(mainHand ^ leftHanded,
                AnimationTickHolder.getPartialTicks());

        if (mainHand || offHand)
            angle += 360 * Mth.clamp(speed * 5, 0, 1);
        angle %= 360;

        ms.pushPose();
        ms.translate(0, offset, 0);
        ms.mulPose(Vector3f.ZP.rotationDegrees(angle));
        ms.translate(0, -offset, 0);
      //  renderer.render(model.getPartial("cog"), light);
        ms.popPose();

        if (transformType == ItemTransforms.TransformType.GUI) {
            PotatoCannonItem.getAmmoforPreview(stack)
                    .ifPresent(ammo -> {
                        PoseStack localMs = new PoseStack();
                        localMs.translate(-1 / 4f, -1 / 4f, 1);
                        localMs.scale(.5f, .5f, .5f);
                        TransformStack.cast(localMs)
                                .rotateY(-34);
                        itemRenderer.renderStatic(ammo, ItemTransforms.TransformType.GUI, light, OverlayTexture.NO_OVERLAY, localMs, buffer, 0);
                    });
        }

    }


    
}
