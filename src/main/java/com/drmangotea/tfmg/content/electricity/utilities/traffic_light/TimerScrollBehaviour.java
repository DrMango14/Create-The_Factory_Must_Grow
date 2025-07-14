package com.drmangotea.tfmg.content.electricity.utilities.traffic_light;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;

import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

public class TimerScrollBehaviour extends ScrollValueBehaviour {

	public TimerScrollBehaviour(Component label, SmartBlockEntity be, ValueBoxTransform slot) {
		super(label, be, slot);
	}

	@Override
	public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
		return new ValueSettingsBoard(label, 60, 10,
			CreateLang.translatedOptions("generic.unit", "ticks", "seconds", "minutes"),
			new ValueSettingsFormatter(this::formatSettings));
	}



	@Override
	public void setValueSettings(Player player, ValueSettings valueSetting, boolean ctrlHeld) {
		int value = valueSetting.value();
		int multiplier = switch (valueSetting.row()) {
		case 0 -> 1;
		case 1 -> 20;
		default -> 60 * 20;
		};
		if (!valueSetting.equals(getValueSettings()))
			playFeedbackSound(this);
		setValue(Math.max(2, Math.max(1, value) * multiplier));
	}

	@Override
	public ValueSettings getValueSettings() {
		int row = 0;
		int value = this.value;

		if (value > 60 * 20) {
			value = value / (60 * 20);
			row = 2;
		} else if (value > 60) {
			value = value / 20;
			row = 1;
		}

		return new ValueSettings(row, value);
	}

	public MutableComponent formatSettings(ValueSettings settings) {
		int value = Math.max(1, settings.value());
		return Component.literal(switch (settings.row()) {
		case 0 -> Math.max(2, value) + "t";
		case 1 -> "0:" + (value < 10 ? "0" : "") + value;
		default -> value + ":00";
		});
	}
	
	@Override
	public String getClipboardKey() {
		return "Timings";
	}

}
