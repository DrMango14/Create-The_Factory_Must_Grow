package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGRegistrate;
import com.drmangotea.tfmg.content.electricity.utilities.segmented_display.SegmentedDisplayTarget;
import com.simibubi.create.api.behaviour.display.DisplayTarget;
import com.tterrag.registrate.util.entry.RegistryEntry;

import java.util.function.Supplier;

public class TFMGDisplayTargets {
	private static final TFMGRegistrate REGISTRATE = TFMG.registrate();
	
	public static final RegistryEntry<DisplayTarget, SegmentedDisplayTarget>
		SEGMENTED_DISPLAY = simple("segmented_display", SegmentedDisplayTarget::new);
	
	private static <T extends DisplayTarget> RegistryEntry<DisplayTarget, T> simple(String name, Supplier<T> supplier) {
		return REGISTRATE.displayTarget(name, supplier).register();
	}
	
	public static void init() {
	}
}
