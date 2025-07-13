package com.drmangotea.tfmg.ponder.scenes.electricity;

import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class GeneratorScenes {

    public static void electricity(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("electricity", "Electricity");
        scene.showBasePlate();


        Selection generator = util.select().fromTo(5, 1, 4, 4, 1, 4);

        Selection light1 = util.select().fromTo(3, 1, 4, 2, 2, 4);
        Selection light2 = util.select().fromTo(2, 1, 3, 2, 2, 2);
        Selection light3 = util.select().fromTo(2, 1, 0, 2, 2, 1);

        scene.world().showIndependentSection(generator, Direction.DOWN);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("A generator creates 2 values, Voltage and Power");
        scene.idle(60);


        //-------------------------Phase 1-------------------------//
        scene.world().showIndependentSection(light1, Direction.DOWN);


        scene.overlay().showText(240)
                .attachKeyFrame()
                .text(
                        """
                                Generator:
                                   Voltage(U) = 200V
                                   Max Power = 8kW
                                Light Bulb:
                                   Voltage(U) = 200V
                                   Current(I) = 2A
                                   Power(P) = 100W
                                   Resistance(R) = 100Ω"""
                )
                .independent(50)
                .colored(PonderPalette.BLUE);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("When load is applied on a generator, it takes its voltage");
        scene.idle(80);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Every an electric device has electrical resistance, light bulbs are 100 Ohm(Ω)");
        scene.idle(80);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Current with size of Voltage divided by Resistance will start flowing in the light bulb");

        scene.idle(80);
        //-------------------------Phase 2-------------------------//
        scene.world().showIndependentSection(light2, Direction.DOWN);
        scene.overlay().showText(180)
                .attachKeyFrame()
                .text(
                        """
                                Generator:
                                   Voltage(U) = 200V
                                   Max Power = 8kW
                                Light Bulb 1:
                                   Voltage(U) = 100V
                                   Current(I) = 1A
                                   Resistance(R) = 100Ω
                                   Group=0
                                Light Bulb 2:
                                   Voltage(U) = 100V
                                   Current(I) = 1A
                                   Resistance(R) = 100Ω
                                   Group=0"""
                )
                .independent(50)
                .colored(PonderPalette.BLUE);
        scene.overlay().showText(80)
                .attachKeyFrame()
                .text("Electric components can be connected with groups, by default all blocks are group 0");
        scene.idle(90);
        scene.overlay().showText(80)
                .attachKeyFrame()
                .text("Blocks that share a group split voltage between them(blocks with higher resistance get more of the split voltage)");
        scene.idle(90);
        //-------------------------Phase 3-------------------------//
        Vec3 pos = util.vector().topOf(util.grid().at(2, 2, 4));
        scene.overlay().showControls(pos, Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(TFMGItems.CONFIGURATION_WRENCH));
        scene.overlay().showText(180)
                .attachKeyFrame()
                .text(
                        """
                                Generator:
                                   Voltage(U) = 200V
                                   Max Power = 8kW
                                Light Bulb 1:
                                   Voltage(U) = 200V
                                   Current(I) = 2A
                                   Resistance(R) = 100Ω
                                   Group=1
                                Light Bulb 2:
                                   Voltage(U) = 200V
                                   Current(I) = 2A
                                   Resistance(R) = 100Ω
                                   Group=0"""
                )
                .independent(50)
                .colored(PonderPalette.BLUE);
        scene.overlay().showText(80)
                .attachKeyFrame()
                .text("Groups can be changed using the Configuration Wrench");
        scene.idle(90);
        scene.overlay().showText(80)
                .attachKeyFrame()
                .text("Blocks in their own group keep all the voltage");
        scene.idle(90);


    }

    public static void electricy_two(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("electricity_two", "Electric Subnetworks");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        Selection generator = util.select().fromTo(4, 1, 3, 2, 1, 3);

        Selection light = util.select().fromTo(0, 1, 3, 0, 2, 2);

        Selection potentiometer = util.select().fromTo(1, 1, 1, 1, 1, 1);
        Selection electricSwitch = util.select().fromTo(1, 1, 2, 1, 1, 2);
        Selection diode = util.select().fromTo(1, 1, 3, 1, 1, 3);
        Selection transformer = util.select().fromTo(1, 1, 4, 1, 1, 4);


        ElementLink<WorldSectionElement> generatorElement = scene.world().showIndependentSection(generator, Direction.DOWN);
        ElementLink<WorldSectionElement> lightElement = scene.world().showIndependentSection(light, Direction.DOWN);
        ElementLink<WorldSectionElement> diodeElement = scene.world().showIndependentSection(diode, Direction.DOWN);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Some blocks have connections from 2 sides");
        scene.idle(60);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("These blocks create a new electric network on one of their sides");
        scene.idle(60);
        scene.overlay().showText(90)
                .attachKeyFrame()
                .text("This subnetwork will get all the power from the main network but not the opposite way");
        scene.idle(100);

        //DIODE
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("The most basic block with this ability is the Diode, it has no other extra abilities");
        scene.idle(80);
        //POTENTIOMETER
        scene.world().hideIndependentSection(diodeElement, Direction.NORTH);
        ElementLink<WorldSectionElement> potentiometerElement = scene.world().showIndependentSection(potentiometer, Direction.NORTH);
        scene.world().moveSection(potentiometerElement, new Vec3(0d, 0d, 2d), 0);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Next one is the potentiometer, this one can set the percentage of voltage that gets to the subnetwork");
        scene.idle(80);
        //SWITCH
        scene.world().hideIndependentSection(potentiometerElement, Direction.NORTH);
        ElementLink<WorldSectionElement> switchElement = scene.world().showIndependentSection(electricSwitch, Direction.NORTH);
        scene.world().moveSection(switchElement, new Vec3(0d, 0d, 1d), 0);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("The switch works similarly but with redstone");
        scene.idle(80);
        //TRANSFORMER
        scene.world().hideIndependentSection(switchElement, Direction.NORTH);
        ElementLink<WorldSectionElement> transformerElement = scene.world().showIndependentSection(transformer, Direction.NORTH);
        scene.world().moveSection(transformerElement, new Vec3(0d, 0d, -1d), 0);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("The transformer changes voltage based on the ratio of turns between the primary and secondary coil");
        scene.idle(80);

    }
}
