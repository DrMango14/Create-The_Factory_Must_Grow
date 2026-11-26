package com.drmangotea.tfmg.ponder;

import com.drmangotea.tfmg.TFMG;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.api.registration.SharedTextRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class TFMGPonderPlugin implements PonderPlugin {
	@Override
	public String getModId() {
		return TFMG.MOD_ID;
	}

	@Override
	public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
		TFMGPonderScenes.register(helper);
	}

	@Override
	public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
		TFMGPonderTags.register(helper);
	}

	@Override
	public void registerSharedText(SharedTextRegistrationHelper helper) {
		helper.registerSharedText("rpm8", "8 RPM");
		helper.registerSharedText("rpm16", "16 RPM");
		helper.registerSharedText("rpm16_source", "Source: 16 RPM");
		helper.registerSharedText("rpm32", "32 RPM");

		helper.registerSharedText("movement_anchors", "With the help of Super Glue, larger structures can be moved.");
		helper.registerSharedText("behaviour_modify_value_panel", "This behaviour can be modified using the value panel");
		helper.registerSharedText("storage_on_contraption", "Inventories attached to the Contraption will pick up their drops automatically");
	}
}
