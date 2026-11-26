package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.ElectricalPropertiesType;
import com.drmangotea.tfmg.content.electricity.content.devices.base.ResistorProperties;
import com.drmangotea.tfmg.content.electricity.content.devices.electric_motor.ElectricMotorProperties;
import com.drmangotea.tfmg.content.electricity.content.devices.generators.creative_generator.CreativeGeneratorProperties;
import com.drmangotea.tfmg.content.electricity.content.devices.generators.large_generator.LargeGeneratorProperties;
import com.drmangotea.tfmg.content.electricity.content.devices.generators.three_phase_generator.ThreePhaseGeneratorProperties;
import com.drmangotea.tfmg.content.electricity.content.lights.LightProperties;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.adapters.FourSlotAdapterProperties;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.adapters.TwoSlotAdapterProperties;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.cables.CableProperties;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.energy_storage.accumulator_control_unit.AccumulatorControlUnitProperties;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.switches.electric_switch.SwitchProperties;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.switches.large_switch.LargeSwitchProperties;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.large.LargeTransformerProperties;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.small.TransformerProperties;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.ConnectorProperties;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.UnwireableConnectorProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


import static com.drmangotea.tfmg.TFMGRegistries.ELECTRIC_TYPE;

//TODO: Shift to use Registrate? Or maybe not.
public class TFMGElectricalProperties {
	static final DeferredRegister<ElectricalPropertiesType<?>> PROPERTIES = DeferredRegister.create(ELECTRIC_TYPE, TFMG.MOD_ID);
	
	public final static DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<ThreePhaseGeneratorProperties>>
		THREE_PHASE_GENERATOR = property("three_phase_generator",
			(pos, direction, d) -> new ThreePhaseGeneratorProperties(pos, direction));
	
	public final static DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<LargeGeneratorProperties>>
		LARGE_GENERATOR = property("large_generator",
		(pos, direction, d) -> new LargeGeneratorProperties(pos, direction));
	
	public final static DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<ConnectorProperties>>
		CONNECTOR = property("connector",
			(pos, direction, d) -> new ConnectorProperties(pos, direction));
	
	public final static DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<ResistorProperties>>
		RESISTOR = property("resistor",
			(pos, direction, d) -> new ResistorProperties(pos, direction));
	
	public final static DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<CableProperties>>
		CABLE = property("cable",
			(pos, direction, d) -> new CableProperties(pos, d));
	
	public final static DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<FourSlotAdapterProperties>>
		FOUR_SLOT_ADAPTER = property("four_slot_adapter",
			(pos, direction, d) -> new FourSlotAdapterProperties(pos, direction));
	
	public final static DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<TwoSlotAdapterProperties>>
		TWO_SLOT_ADAPTER = property("two_slot_adapter",
			(pos, direction, d) -> new TwoSlotAdapterProperties(pos, direction));
	
	public final static DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<LightProperties>>
		LIGHT = property("light",
			(pos, direction, d) -> new LightProperties(pos, direction));
	
	public final static DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<ElectricMotorProperties>>
		MOTOR = property("motor",
			(pos, direction, d) -> new ElectricMotorProperties(pos, direction));
	
	public final static DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<TransformerProperties>>
		TRANSFORMER = property("transformer",
			(pos, direction, d) -> new TransformerProperties(pos, direction));
	
	public final static DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<CreativeGeneratorProperties>>
		CREATIVE_GENERATOR = property("creative_generator",
			(pos, direction, d) -> new CreativeGeneratorProperties(pos));
	
	public final static DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<SwitchProperties>>
		SWITCH = property("switch",
			(pos, direction, d) -> new SwitchProperties(pos, direction));
	
	public final static DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<LargeTransformerProperties>>
		LARGE_TRANSFORMER = property("large_transformer",
			(pos, direction, d) -> new LargeTransformerProperties(pos, direction));
	
	public final static DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<AccumulatorControlUnitProperties>>
		ACCUMULATOR_CONTROL = property("accumulator_control",
			(pos, direction, d) -> new AccumulatorControlUnitProperties(pos, direction));
	
	public final static DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<LargeSwitchProperties>>
		LARGE_SWITCH = property("large_switch",
			(pos, direction, d) -> new LargeSwitchProperties(pos, direction));
	
	public final static DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<UnwireableConnectorProperties>>
		UNWIREABLE_CONNECTOR = property("unwireable_connector",
			(pos, direction, d) -> new UnwireableConnectorProperties(pos, d));
	
	public static <T extends ElectricalProperties> DeferredHolder<ElectricalPropertiesType<?>, ElectricalPropertiesType<T>> property (
		String name, ElectricalPropertiesType.PropertiesFactory<T> factory
	) {
		return PROPERTIES.register(name, id -> new ElectricalPropertiesType<>(id, factory));
	}
	
	public static void register(IEventBus bus) {
		PROPERTIES.register(bus);
	}
}
