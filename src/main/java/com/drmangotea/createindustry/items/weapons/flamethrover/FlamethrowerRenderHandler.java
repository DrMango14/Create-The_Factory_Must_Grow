package com.drmangotea.createindustry.items.weapons.flamethrover;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.equipment.potatoCannon.PotatoProjectileEntity;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class FlamethrowerRenderHandler extends ShootableGadgetRenderHandler {

	private float nextPitch;

	@Override
	protected void playSound(InteractionHand hand, Vec3 position) {
		PotatoProjectileEntity.playLaunchSound(Minecraft.getInstance().level, position, nextPitch);
	}

	@Override
	protected boolean appliesTo(ItemStack stack) {
		return stack.getItem() instanceof FlamethrowerItem;
	}

	@Override
	protected void transformTool(PoseStack ms, float flip, float equipProgress, float recoil, float pt) {
	}

	@Override
	protected void transformHand(PoseStack ms, float flip, float equipProgress, float recoil, float pt) {
		ms.translate(0,-5,0);
	}


}
