package com.drmangotea.tfmg.content.machinery.misc.winding_machine;

import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.google.common.collect.ImmutableList;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

public class SpoolScrollValueBehaviour extends ScrollValueBehaviour {
	
	public SpoolScrollValueBehaviour(Component label, SmartBlockEntity be, ValueBoxTransform slot) {
		super(label, be, slot);
		this.between(0, 100);
		this.withFormatter(i -> String.valueOf(i*10));
	}
	
	@Override
	public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
		return new ValueSettingsBoard(
			label, 100, 10,
			ImmutableList.of(TFMGLang.translateDirect("winding_machine.turn_count")),
			new ValueSettingsFormatter(s -> TFMGLang.number(s.value() * 10).component())
		);
	}
	
	public int getValue() {
		return super.getValue() * 10;
	}
}
