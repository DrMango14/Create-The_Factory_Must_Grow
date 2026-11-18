package com.drmangotea.tfmg.content.engines.upgrades.base;

import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;

public interface EngineGenerator {

	ElectricalProperties getProperties(AbstractSmallEngineBlockEntity engine);
	
	//TODO: review if these are still needed
    int getVoltageGeneration(AbstractSmallEngineBlockEntity engine);
    float getPowerGeneration(AbstractSmallEngineBlockEntity engine);
}
