package com.drmangotea.tfmg.ponder.scenes;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.decoration.pipes.TFMGPipes;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankBlockEntity;
import com.drmangotea.tfmg.ponder.TFMGSceneBuilder;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

@SuppressWarnings("unused")
public class EngineScenes {
    public static void engines(SceneBuilder builder, SceneBuildingUtil util) {
        TFMGSceneBuilder scene = new TFMGSceneBuilder(builder);
        scene.title("engines", "Engines");
        scene.configureBasePlate(0, 0, 7);

        scene.showBasePlate();

        Selection engine = util.select().fromTo(3, 1, 3, 3, 1, 4);
        Selection engineFront = util.select().fromTo(3, 1, 2, 3, 1, 2);
        Selection engineFrontShaft = util.select().fromTo(2, 1, 2, 2, 1, 2);

        Selection lever = util.select().fromTo(4, 1, 2, 4, 1, 2);
        Selection cog = util.select().fromTo(3, 1, 0, 3, 1, 1);

        Selection fuelTank = util.select().fromTo(4, 1, 3, 5, 2, 3);
        Selection tank = util.select().fromTo(4, 1, 4, 4, 2, 4);
        Selection exhaust = util.select().fromTo(2, 1, 4, 1, 2, 4);
        scene.world().setKineticSpeed(fuelTank, 70);
        scene.world().setKineticSpeed(exhaust, 70);
        scene.world().setKineticSpeed(cog, 70);

        ElementLink<WorldSectionElement> engineElement = scene.world().showIndependentSection(engine, Direction.DOWN);
        ElementLink<WorldSectionElement> engineFrontElement = scene.world().showIndependentSection(engineFront, Direction.DOWN);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("To build an engine, start by placing up to 5 engine blocks in a line");

        scene.idle(70);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("The engine's tooltip will show you items needed for the assembly");
        scene.idle(60);

        Vec3 enginePos = util.vector().topOf(util.grid().at(3, 1, 2));
        scene.overlay().showControls(enginePos, Pointing.DOWN, 15)
                .rightClick()
                .withItem(new ItemStack(TFMGItems.CRANKSHAFT.get()));
        scene.idle(25);
        scene.overlay().showControls(enginePos, Pointing.DOWN, 15)
                .rightClick()
                .withItem(new ItemStack(TFMGBlocks.STEEL_COGWHEEL.get()));
        scene.idle(25);
        scene.overlay().showControls(enginePos, Pointing.DOWN, 15)
                .rightClick()
                .withItem(new ItemStack(TFMGBlocks.LARGE_STEEL_COGWHEEL.get()));
        scene.idle(40);

        scene.overlay().showControls(enginePos, Pointing.DOWN, 50)
                .rightClick()
                .withItem(new ItemStack(AllItems.EMPTY_SCHEMATIC.get()));

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Engine configuration can be changed with a schematic");
        scene.idle(60);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("Next step is inserting the cylinders (or turbine blades in turbine engines)");

        ElementLink<WorldSectionElement> engineFrontShaftElement = scene.world().showIndependentSection(engineFrontShaft, Direction.DOWN);
        scene.world().moveSection(engineFrontShaftElement, new Vec3(1d, -2d, 0d), 0);

        BlockPos pos = util.grid().at(3, 1, 2);
        for (int i = 0; i < 12; i++) {
            scene.idle(5);
            scene.tfmgInstructions().addPistonToEngine(pos);

            if (i == 3 || i == 7)
                pos = pos.south();
        }
        scene.idle(35);
        scene.world().moveSection(engineFrontShaftElement, new Vec3(0d, 2d, 0d), 0);
        scene.world().moveSection(engineFrontElement, new Vec3(0d, -2d, 0d), 0);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Shafts are inserted by right clicking");
        scene.overlay().showControls(enginePos, Pointing.DOWN, 50)
                .rightClick()
                .withItem(new ItemStack(AllBlocks.SHAFT));
        scene.idle(70);

        ElementLink<WorldSectionElement> fuelTankElement = scene.world().showIndependentSection(fuelTank, Direction.DOWN);
        scene.idle(10);
        ElementLink<WorldSectionElement> exhaustElement = scene.world().showIndependentSection(exhaust, Direction.DOWN);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Fuel input and exhaust output can be done from any block");
        scene.idle(70);

        scene.overlay().showText(50)
                .attachKeyFrame()

                .text("Every engine block can be right clicked with certain items to be upgraded");
        scene.idle(60);

        scene.overlay().showControls(util.vector().topOf(util.grid().at(3, 1, 4)), Pointing.DOWN, 40)
                .rightClick()
                .withItem(new ItemStack(TFMGBlocks.INDUSTRIAL_PIPE));

        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("For example industrial pipes make the engine consume fuel from neighboring tanks");
        scene.idle(80);
        scene.world().showIndependentSection(tank, Direction.DOWN);
        scene.idle(30);
        scene.world().setKineticSpeed(engineFrontShaft, 70);
        scene.world().showIndependentSection(lever, Direction.DOWN);
        scene.world().showIndependentSection(cog, Direction.DOWN);
        scene.overlay().showText(128)
                .attachKeyFrame()
                .text("The engine can be started with a redstone signal");
    }

    public static void dieselEngine(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("diesel_engine", "");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        Selection engine = util.select().fromTo(2, 1, 0, 2, 1, 2);
        Selection exhaust = util.select().fromTo(2, 1, 3, 2, 2, 4);
        Selection air = util.select().fromTo(0, 1, 0, 1, 1, 2);
        Selection input = util.select().fromTo(3, 1, 0, 4, 1, 2);

        scene.idle(30);

        ElementLink<WorldSectionElement> engineElement = scene.world().showIndependentSection(engine, Direction.DOWN);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Diesel Engines are assembled by placing a shaft in the front of a diesel engine block");


        scene.idle(70);


        ElementLink<WorldSectionElement> inputElement = scene.world().showIndependentSection(input, Direction.DOWN);
        ElementLink<WorldSectionElement> exhaustElement = scene.world().showIndependentSection(exhaust, Direction.DOWN);
        scene.world().setKineticSpeed(input, 80);
        scene.world().setKineticSpeed(exhaust, 80);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Carbon Dioxide has to be outputted by pipes and exhaust block")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 2, 4), Direction.WEST))
                .placeNearTarget();
        scene.idle(40);

        ElementLink<WorldSectionElement> airElement = scene.world().showIndependentSection(air, Direction.DOWN);
        scene.world().setKineticSpeed(air, 80);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Diesel engines need air that can be collected with air intakes")
                .pointAt(util.vector().blockSurface(util.grid().at(0, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(10);
        scene.world().setKineticSpeed(engine, 128);
        scene.idle(70);
    }

    public static void dieselEngineExpansion(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("diesel_engine_expansion", "");
        scene.configureBasePlate(0, 0, 6);
        scene.showBasePlate();

        Selection engine = util.select().fromTo(2, 1, 0, 2, 1, 2);
        Selection input = util.select().fromTo(0, 1, 0, 1, 1, 2);
        Selection exhaust = util.select().fromTo(3, 1, 0, 4, 2, 2);

        Selection expansion = util.select().fromTo(2, 1, 3, 2, 1, 3);
        Selection air = util.select().fromTo(2, 1, 4, 2, 1, 5);
        Selection coolant = util.select().fromTo(0, 1, 3, 1, 1, 3);
        Selection oil = util.select().fromTo(3, 1, 3, 4, 1, 3);

        scene.world().setKineticSpeed(input, 80);
        scene.world().setKineticSpeed(exhaust, 80);
        scene.world().setKineticSpeed(air, 80);
        scene.world().setKineticSpeed(coolant, 80);
        scene.world().setKineticSpeed(oil, 80);
        scene.world().setKineticSpeed(engine, 128);

        scene.world().showIndependentSection(engine, Direction.DOWN);
        scene.world().showIndependentSection(input, Direction.DOWN);
        scene.world().showIndependentSection(exhaust, Direction.DOWN);
        ElementLink<WorldSectionElement> airElement = scene.world().showIndependentSection(air, Direction.DOWN);

        scene.world().moveSection(airElement, new Vec3(0d, 0d, -1d), 0);

        scene.idle(30);

        scene.world().moveSection(airElement, new Vec3(0d, 0d, 1d), 10);

        scene.idle(30);

        scene.world().showIndependentSection(expansion, Direction.DOWN);

        scene.idle(20);

        scene.world().showIndependentSection(coolant, Direction.DOWN);
        scene.world().showIndependentSection(oil, Direction.DOWN);

        scene.overlay().showText(100)
                .attachKeyFrame()
                .text("Diesel engine expansions give diesel engines 2 new fluid slots, for cooling and lubrication")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 1, 3), Direction.WEST))
                .placeNearTarget();

        scene.idle(50);
    }

    public static void radialEngines(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("radial_engines", "");
        scene.configureBasePlate(0, 0, 5);
        scene.idle(10);

        scene.showBasePlate();

        Selection engineSmall = util.select().fromTo(2, 1, 1, 2, 1, 1);
        Selection engineLarge = util.select().fromTo(1, 1, 1, 1, 1, 1);
        Selection engineLever = util.select().fromTo(3, 1, 0, 3, 1, 0);

        Selection inputPump = util.select().fromTo(3, 1, 2, 3, 1, 2);
        Selection input = util.select().fromTo(3, 1, 1, 3, 1, 1);
        Selection tank1 = util.select().fromTo(3, 1, 3, 3, 2, 3);
        Selection tank2 = util.select().fromTo(2, 1, 3, 2, 2, 3);

        scene.world().setKineticSpeed(engineSmall, 0);

        ElementLink<WorldSectionElement> engineElement = scene.world().showIndependentSectionImmediately(engineSmall);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Radial Engines are a special Type of Engine that doesn't require an exhaust block")
                .pointAt(util.vector().blockSurface(util.grid().at(4, 0, 4), Direction.WEST))
                .placeNearTarget();
        scene.idle(100);

        scene.world().setKineticSpeed(inputPump, 80);
        ElementLink<WorldSectionElement> inputElement = scene.world().showIndependentSection(input, Direction.DOWN);
        scene.idle(50);

        BlockPos inputPos = util.grid().at(2, 1, 1);
        Vec3 topOf = util.vector().topOf(inputPos);
        scene.overlay().showControls(topOf, Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(AllItems.WRENCH.get()));

        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Clicking the Engine from one of its sides will spawn an input slot that can accept fuel and redstone signals")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 1, 1), Direction.WEST))
                .placeNearTarget();
        scene.idle(100);
        scene.overlay().showText(40)
                .attachKeyFrame()
                .text("Regular Radial Engines uses gasoline as fuel")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 1, 1), Direction.WEST))
                .placeNearTarget();


        scene.idle(80);

        ElementLink<WorldSectionElement> inputPumpElement = scene.world().showIndependentSection(inputPump, Direction.DOWN);
        ElementLink<WorldSectionElement> tankElement1 = scene.world().showIndependentSection(tank1, Direction.DOWN);
        ElementLink<WorldSectionElement> leverElement = scene.world().showIndependentSection(engineLever, Direction.DOWN);

        scene.world().setKineticSpeed(engineSmall, 180);
        scene.world().setKineticSpeed(engineLarge, 180);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Engine will start when redstone signal is applied to the input slot or the block itself")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 1, 0), Direction.WEST))
                .placeNearTarget();

        scene.idle(100);

        scene.world().hideIndependentSection(engineElement, Direction.SOUTH);
        scene.world().hideIndependentSection(tankElement1, Direction.SOUTH);

        scene.idle(50);

        ElementLink<WorldSectionElement> largeEngineElement = scene.world().showIndependentSection(engineLarge, Direction.DOWN);
        ElementLink<WorldSectionElement> tankElement2 = scene.world().showIndependentSection(tank2, Direction.DOWN);
        scene.world().moveSection(largeEngineElement, new Vec3(1d, 0d, 0d), 0);
        scene.world().moveSection(tankElement2, new Vec3(1d, 0d, 0d), 0);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("The second variant of a radial is The Large Radial Engine which uses kerosene as fuel");
        scene.idle(50);
    }

    public static void regularEngineAssembly(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("regular_engine_assembly", "Regular Engine Assembly");
        scene.configureBasePlate(1, 1, 5);
        scene.showBasePlate();

        Selection singleEngineNoShaft = util.select().position(6, 1, 6);
        Selection singleEngine = util.select().position(5, 1, 6);

        var engineElement = scene.world().showIndependentSection(singleEngineNoShaft, Direction.UP);
        scene.world().moveSection(engineElement, util.vector().of(-3, 0, -3), 0);

        scene.overlay().showText(30)
                .placeNearTarget()
                .text("Regular Engines require assembly.");
        scene.idle(40);

        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 1, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGItems.CRANKSHAFT.asStack())
                .rightClick();
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 1, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGBlocks.STEEL_COGWHEEL.asStack())
                .rightClick();
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 1, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGBlocks.LARGE_STEEL_COGWHEEL.asStack())
                .rightClick();
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 1, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(AllItems.BELT_CONNECTOR.asStack())
                .rightClick();
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 1, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.STEEL).getPipe().asStack())
                .rightClick();
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 1, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.STEEL).getPipe().asStack())
                .rightClick();
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 1, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGItems.STEEL_MECHANISM.asStack())
                .rightClick();
        scene.idle(20);
        scene.addKeyframe();
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 1, 3), Direction.UP), Pointing.DOWN, 60)
                .withItem(AllBlocks.SHAFT.asStack())
                .rightClick();
        scene.idle(10);
        scene.world().moveSection(engineElement, util.vector().of(0, -1000, 0), 0);
        scene.world().hideIndependentSection(engineElement, null);
        engineElement = scene.world().showIndependentSectionImmediately(singleEngine);
        scene.world().moveSection(engineElement, util.vector().of(-2, 0, -3), 0);
        scene.overlay().showText(60)
                .colored(PonderPalette.BLUE)
                .text("Clicking the engine with a Shaft creates the Kinetic Output")
                .pointAt(util.vector().centerOf(util.grid().at(3, 1, 3)))
                .placeNearTarget();
        scene.idle(70);
        scene.addKeyframe();
        scene.overlay().showText(50)
                .placeNearTarget()
                .text("A Regular Engine's type can be cycled by clicking it with an Empty Schematic");
        scene.idle(10);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 1, 3), Direction.UP), Pointing.DOWN, 20)
                .withItem(AllItems.EMPTY_SCHEMATIC.asStack())
                .rightClick();
        scene.idle(30);
        scene.addKeyframe();
        scene.idle(10);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 1, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGItems.ENGINE_CYLINDER.asStack())
                .rightClick();
        scene.world().moveSection(engineElement, util.vector().of(0, -1000, 0), 0);
        scene.world().hideIndependentSection(engineElement, null);
        engineElement = scene.world().showIndependentSectionImmediately(util.select().position(6, 2, 6));
        scene.world().moveSection(engineElement, util.vector().of(-3, -1, -3), 0);
        scene.overlay().showText(60)
                .colored(PonderPalette.BLUE)
                .text("Engine Cylinders of the same type must then be inserted to complete the engine.")
                .pointAt(util.vector().centerOf(util.grid().at(3, 1, 3)))
                .placeNearTarget();
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 1, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGItems.ENGINE_CYLINDER.asStack())
                .rightClick();
        scene.world().moveSection(engineElement, util.vector().of(0, -1000, 0), 0);
        scene.world().hideIndependentSection(engineElement, null);
        engineElement = scene.world().showIndependentSectionImmediately(util.select().position(5, 2, 6));
        scene.world().moveSection(engineElement, util.vector().of(-2, -1, -3), 0);
    }

    public static void regularEngineOperation(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("regular_engine_operation", "Operating a Regular Engine");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();

        Selection fullEngine = util.select().fromTo(6, 1, 2, 6, 1, 4);
        Selection doubleEngine = util.select().fromTo(4, 2, 5, 4, 2, 6);
        Selection singleEngine = util.select().position(5, 2, 6);

        Selection fuelTankPump = util.select().position(4, 1, 2);
        Selection fuelTank = util.select().fromTo(5, 1, 2, 5, 2, 2);

        Selection exhaustPump = util.select().position(2, 1, 4);
        Selection exhaust = util.select().fromTo(1, 1, 4, 1, 2, 4);

        var engineElement = scene.world().showIndependentSection(singleEngine, Direction.UP);
        scene.world().moveSection(engineElement, util.vector().of(-2, -1, -3), 0);

        scene.overlay().showText(30)
                .placeNearTarget()
                .text("Work In Progress");
        scene.idle(40);

    }

    public static void turbineEngineAssembly(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("turbine_engine_assembly", "Turbine Engine Assembly");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();

        scene.overlay().showText(30)
                .placeNearTarget()
                .text("Work In Progress");
        scene.idle(40);
    }

    public static void turbineEngineOperation(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("turbine_engine_operation", "Operating a Turbine Engine");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();

        scene.overlay().showText(30)
                .placeNearTarget()
                .text("Work In Progress");
        scene.idle(40);
    }

    public static void radialEngineAssembly(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("radial_engine_assembly", "Radial Engine Assembly");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();

        Selection unassembled = util.select().position(6, 1, 6);
        Selection unassembled1 = util.select().position(2, 1, 6);
        Selection unassembled2 = util.select().position(1, 1, 6);
        Selection unassembled3 = util.select().position(0, 1, 6);
        Selection unassembled4 = util.select().position(2, 1, 4);
        Selection unassembled5 = util.select().position(1, 1, 4);
        Selection unassembled6 = util.select().position(0, 1, 4);
        Selection unassembled7 = util.select().position(0, 1, 2);
        Selection assembled = util.select().position(5, 1, 6);

        var engineElement = scene.world().showIndependentSection(unassembled, Direction.UP);
        scene.world().moveSection(engineElement, util.vector().of(-3, 1, -3), 0);

        scene.overlay().showText(30)
                .placeNearTarget()
                .text("Radial Engines require assembly.");
        scene.idle(40);

        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGItems.CRANKSHAFT.asStack())
                .rightClick();
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGBlocks.STEEL_COGWHEEL.asStack())
                .rightClick();
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGBlocks.LARGE_STEEL_COGWHEEL.asStack())
                .rightClick();
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(AllItems.BELT_CONNECTOR.asStack())
                .rightClick();
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.STEEL).getPipe().asStack())
                .rightClick();
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGPipes.PIPES.get(TFMGPipes.PipeMaterial.STEEL).getPipe().asStack())
                .rightClick();
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGItems.STEEL_MECHANISM.asStack())
                .rightClick();
        scene.idle(24);
        scene.addKeyframe();
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGItems.ENGINE_CYLINDER.asStack())
                .rightClick();
        scene.world().moveSection(engineElement, util.vector().of(0, -1000, 0), 0);
        scene.world().hideIndependentSection(engineElement, null);
        engineElement = scene.world().showIndependentSectionImmediately(unassembled1);
        scene.world().moveSection(engineElement, util.vector().of(1, 1, -3), 0);
        scene.overlay().showText(60)
                .colored(PonderPalette.BLUE)
                .text("Engine Cylinders of the same type must then be inserted to complete the engine.")
                .pointAt(util.vector().centerOf(util.grid().at(3, 2, 3)))
                .placeNearTarget();
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGItems.ENGINE_CYLINDER.asStack())
                .rightClick();
        scene.world().moveSection(engineElement, util.vector().of(0, -1000, 0), 0);
        scene.world().hideIndependentSection(engineElement, null);
        engineElement = scene.world().showIndependentSectionImmediately(unassembled2);
        scene.world().moveSection(engineElement, util.vector().of(2, 1, -3), 0);
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGItems.ENGINE_CYLINDER.asStack())
                .rightClick();
        scene.world().moveSection(engineElement, util.vector().of(0, -1000, 0), 0);
        scene.world().hideIndependentSection(engineElement, null);
        engineElement = scene.world().showIndependentSectionImmediately(unassembled3);
        scene.world().moveSection(engineElement, util.vector().of(3, 1, -3), 0);
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGItems.ENGINE_CYLINDER.asStack())
                .rightClick();
        scene.world().moveSection(engineElement, util.vector().of(0, -1000, 0), 0);
        scene.world().hideIndependentSection(engineElement, null);
        engineElement = scene.world().showIndependentSectionImmediately(unassembled4);
        scene.world().moveSection(engineElement, util.vector().of(1, 1, -1), 0);
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGItems.ENGINE_CYLINDER.asStack())
                .rightClick();
        scene.world().moveSection(engineElement, util.vector().of(0, -1000, 0), 0);
        scene.world().hideIndependentSection(engineElement, null);
        engineElement = scene.world().showIndependentSectionImmediately(unassembled5);
        scene.world().moveSection(engineElement, util.vector().of(2, 1, -1), 0);
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGItems.ENGINE_CYLINDER.asStack())
                .rightClick();
        scene.world().moveSection(engineElement, util.vector().of(0, -1000, 0), 0);
        scene.world().hideIndependentSection(engineElement, null);
        engineElement = scene.world().showIndependentSectionImmediately(unassembled6);
        scene.world().moveSection(engineElement, util.vector().of(3, 1, -1), 0);
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGItems.ENGINE_CYLINDER.asStack())
                .rightClick();
        scene.world().moveSection(engineElement, util.vector().of(0, -1000, 0), 0);
        scene.world().hideIndependentSection(engineElement, null);
        engineElement = scene.world().showIndependentSectionImmediately(unassembled7);
        scene.world().moveSection(engineElement, util.vector().of(3, 1, 1), 0);
        scene.idle(14);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.UP), Pointing.DOWN, 7)
                .withItem(TFMGItems.ENGINE_CYLINDER.asStack())
                .rightClick();
        scene.world().moveSection(engineElement, util.vector().of(0, -1000, 0), 0);
        scene.world().hideIndependentSection(engineElement, null);
        engineElement = scene.world().showIndependentSectionImmediately(assembled);
        scene.world().moveSection(engineElement, util.vector().of(-2, 1, -3), 0);
    }

    public static void radialEngineOperation(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("radial_engine_operation", "Operating a Radial Engine");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();

        scene.overlay().showText(30)
                .placeNearTarget()
                .text("Work In Progress");
        scene.idle(40);
    }

    public static void largeEngine(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("large_engine", "Large Engine");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();

        Selection engine = util.select().position(3, 1, 3);
        Selection engineShaft = util.select().position(3, 1, 1);
        Selection disconnectedEngine = util.select().position(6, 1, 3);
        BlockPos engineShaftPos = util.grid().at(6, 1, 1);

        Selection airIntakePump = util.select().position(2, 1, 3);
        Selection airIntake = util.select().fromTo(1, 1, 3, 0, 2, 3);

        Selection fuelTankPump = util.select().position(4, 1, 3);
        Selection fuelTank = util.select().fromTo(5, 1, 3, 5, 2, 3);

        Selection exhaustPump = util.select().position(3, 1, 4);
        Selection exhaust = util.select().fromTo(3, 1, 5, 3, 2, 5);

        scene.idle(30);

        var engineElement = scene.world().showIndependentSection(disconnectedEngine, Direction.DOWN);
        scene.world().moveSection(engineElement, util.vector().of(-3, 0, 0), 0);

        scene.overlay().showText(50)
                .placeNearTarget()
                .text("Large Engines create rotation from less refined fuels.");
        scene.idle(60);

        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 1, 3), Direction.UP), Pointing.DOWN, 60)
                .withItem(AllBlocks.SHAFT.asStack())
                .rightClick();
        scene.idle(10);
        scene.world().setBlock(engineShaftPos, AllBlocks.SHAFT.getDefaultState()
                .setValue(ShaftBlock.AXIS, Direction.Axis.X), false);
        ElementLink<WorldSectionElement> engineShaftElement = scene.world().showIndependentSection(engineShaft, null);
        scene.idle(5);
        scene.world().moveSection(engineElement, util.vector().of(0, -1000, 0), 0);
        scene.world().hideIndependentSection(engineElement, null);
        engineElement = scene.world().showIndependentSectionImmediately(engine);
        scene.world().setBlock(engineShaftPos, AllBlocks.POWERED_SHAFT.getDefaultState()
                .setValue(ShaftBlock.AXIS, Direction.Axis.X), false);
        scene.effects().indicateSuccess(util.grid().at(3, 1, 1));
        scene.idle(40);
        scene.overlay().showText(80)
                .attachKeyFrame()
                .colored(PonderPalette.BLUE)
                .text("Clicking the engine with a Shaft creates the Kinetic Output")
                .pointAt(util.vector().centerOf(util.grid().at(3, 1, 3)))
                .placeNearTarget();
        scene.idle(90);
        scene.addKeyframe();
        ElementLink<WorldSectionElement> fuelTankElement = scene.world().showIndependentSection(fuelTank, Direction.DOWN);
        ElementLink<WorldSectionElement> fuelTankPumpElement = scene.world().showIndependentSection(fuelTankPump, Direction.DOWN);
        scene.idle(10);
        scene.overlay().showText(80)
                .colored(PonderPalette.BLUE)
                .text("Fuel is pumped in from the side")
                .pointAt(fuelTank.getCenter())
                .placeNearTarget();
        scene.idle(40);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(5, 2, 3), Direction.EAST), Pointing.RIGHT, 60)
                .withItem(TFMGFluids.DIESEL.get().getBucket().getDefaultInstance())
                .rightClick();
        scene.idle(10);
        FluidStack content = new FluidStack(TFMGFluids.DIESEL.get().getSource(), 8000);
        scene.world().modifyBlockEntity(new BlockPos(5, 1, 3), SteelTankBlockEntity.class, be -> be.getTankInventory().fill(content, IFluidHandler.FluidAction.EXECUTE));
        scene.idle(25);
        scene.world().setKineticSpeed(fuelTankPump, 32);
        scene.idle(30);
        scene.addKeyframe();
        scene.overlay().showText(50)
                .placeNearTarget()
                .text("Unlike other engines, Large Engines require a steady supply of air to function.");
        scene.idle(10);
        ElementLink<WorldSectionElement> airIntakeElement = scene.world().showIndependentSection(airIntake, Direction.DOWN);
        ElementLink<WorldSectionElement> airIntakePumpElement = scene.world().showIndependentSection(airIntakePump, Direction.DOWN);
        scene.idle(40);
        scene.world().setKineticSpeed(util.select().position(0, 2, 3), 32);
        scene.world().setKineticSpeed(airIntakePump, 32);
        scene.idle(10);
        scene.world().setKineticSpeed(engineShaft, 128);
        scene.idle(40);
        scene.addKeyframe();
        scene.idle(10);
        scene.world().setKineticSpeed(engineShaft, 0);
        scene.effects().indicateRedstone(util.grid().at(3, 1, 1));
        scene.idle(10);
        scene.overlay().showText(50)
                .placeNearTarget()
                .text("Over time the Large Engine will produce Carbon Dioxide which can stop it from functioning...");
        scene.idle(60);
        ElementLink<WorldSectionElement> exhaustElement = scene.world().showIndependentSection(exhaust, Direction.DOWN);
        ElementLink<WorldSectionElement> exhaustPumpElement = scene.world().showIndependentSection(exhaustPump, Direction.DOWN);
        scene.overlay().showText(50)
                .placeNearTarget()
                .text("It is a good idea to pump out any Carbon Dioxide into an Exhaust");
        scene.world().setKineticSpeed(exhaustPump, 32);
        scene.idle(10);
        scene.effects().emitParticles(
			new Vec3(3.5, 2.5, 5.5),
			(world, x, y, z) -> TFMGUtils.spawnPonderExhaust(world, x, y, z, 0.3f), 1, 120
		);
        scene.effects().indicateSuccess(util.grid().at(3, 1, 1));
        scene.world().setKineticSpeed(engineShaft, 128);
    }

    public static void simpleLargeEngine(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("simple_large_engine", "Simple Large Engine");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();

        Selection engine = util.select().position(3, 1, 3);
        Selection engineShaft = util.select().position(3, 1, 1);
        Selection disconnectedEngine = util.select().position(6, 1, 3);
        BlockPos engineShaftPos = util.grid().at(6, 1, 1);

        Selection airIntakePump = util.select().position(2, 1, 3);
        Selection airIntake = util.select().fromTo(1, 1, 3, 0, 2, 3);

        Selection fuelTankPump = util.select().position(4, 1, 3);
        Selection fuelTank = util.select().fromTo(5, 1, 3, 5, 2, 3);

        Selection exhaustPump = util.select().position(3, 1, 4);
        Selection exhaust = util.select().fromTo(3, 1, 5, 3, 2, 5);

        scene.idle(30);

        var engineElement = scene.world().showIndependentSection(disconnectedEngine, Direction.DOWN);
        scene.world().moveSection(engineElement, util.vector().of(-3, 0, 0), 0);

        scene.overlay().showText(50)
                .placeNearTarget()
                .text("Large Engines create rotation from less refined fuels.");
        scene.idle(60);

        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(3, 1, 3), Direction.UP), Pointing.DOWN, 60)
                .withItem(AllBlocks.SHAFT.asStack())
                .rightClick();
        scene.idle(10);
        scene.world().setBlock(engineShaftPos, AllBlocks.SHAFT.getDefaultState()
                .setValue(ShaftBlock.AXIS, Direction.Axis.X), false);
        ElementLink<WorldSectionElement> engineShaftElement = scene.world().showIndependentSection(engineShaft, null);
        scene.idle(5);
        scene.world().moveSection(engineElement, util.vector().of(0, -1000, 0), 0);
        scene.world().hideIndependentSection(engineElement, null);
        engineElement = scene.world().showIndependentSectionImmediately(engine);
        scene.world().setBlock(engineShaftPos, AllBlocks.POWERED_SHAFT.getDefaultState()
                .setValue(ShaftBlock.AXIS, Direction.Axis.X), false);
        scene.effects().indicateSuccess(util.grid().at(3, 1, 1));
        scene.idle(40);
        scene.overlay().showText(80)
                .attachKeyFrame()
                .colored(PonderPalette.BLUE)
                .text("Clicking the engine with a Shaft creates the Kinetic Output")
                .pointAt(util.vector().centerOf(util.grid().at(3, 1, 3)))
                .placeNearTarget();
        scene.idle(90);
        scene.addKeyframe();
        ElementLink<WorldSectionElement> fuelTankElement = scene.world().showIndependentSection(fuelTank, Direction.DOWN);
        ElementLink<WorldSectionElement> fuelTankPumpElement = scene.world().showIndependentSection(fuelTankPump, Direction.DOWN);
        scene.idle(10);
        scene.overlay().showText(80)
                .colored(PonderPalette.BLUE)
                .text("Fuel is pumped in from the side")
                .pointAt(fuelTank.getCenter())
                .placeNearTarget();
        scene.idle(40);
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(5, 2, 3), Direction.EAST), Pointing.RIGHT, 60)
                .withItem(TFMGFluids.DIESEL.get().getBucket().getDefaultInstance())
                .rightClick();
        scene.idle(10);
        FluidStack content = new FluidStack(TFMGFluids.DIESEL.get().getSource(), 8000);
        scene.world().modifyBlockEntity(new BlockPos(5, 1, 3), SteelTankBlockEntity.class, be -> be.getTankInventory().fill(content, IFluidHandler.FluidAction.EXECUTE));
        scene.idle(25);
        scene.world().setKineticSpeed(fuelTankPump, 32);
        scene.idle(30);
        scene.addKeyframe();
        scene.overlay().showText(50)
                .placeNearTarget()
                .text("Unlike other engines, Large Engines require a steady supply of air to function.");
        scene.idle(10);
        ElementLink<WorldSectionElement> airIntakeElement = scene.world().showIndependentSection(airIntake, Direction.DOWN);
        ElementLink<WorldSectionElement> airIntakePumpElement = scene.world().showIndependentSection(airIntakePump, Direction.DOWN);
        scene.idle(40);
        scene.world().setKineticSpeed(util.select().position(0, 2, 3), 32);
        scene.world().setKineticSpeed(airIntakePump, 32);
        scene.idle(10);
        scene.world().setKineticSpeed(engineShaft, 128);
        scene.idle(40);
        scene.addKeyframe();
        scene.idle(10);
        scene.world().setKineticSpeed(engineShaft, 0);
        scene.effects().indicateRedstone(util.grid().at(3, 1, 1));
        scene.idle(10);
        scene.overlay().showText(50)
                .placeNearTarget()
                .text("Over time the Large Engine will produce Carbon Dioxide which can stop it from functioning...");
        scene.idle(60);
        ElementLink<WorldSectionElement> exhaustElement = scene.world().showIndependentSection(exhaust, Direction.DOWN);
        ElementLink<WorldSectionElement> exhaustPumpElement = scene.world().showIndependentSection(exhaustPump, Direction.DOWN);
        scene.overlay().showText(50)
                .placeNearTarget()
                .text("It is a good idea to pump out any Carbon Dioxide into an Exhaust");
        scene.world().setKineticSpeed(exhaustPump, 32);
        scene.idle(10);
        scene.effects().emitParticles(
			new Vec3(3.5, 2.5, 5.5),
			(world, x, y, z) -> TFMGUtils.spawnPonderExhaust(world, x, y, z, 0.3f), 1, 120
		);
        scene.effects().indicateSuccess(util.grid().at(3, 1, 1));
        scene.world().setKineticSpeed(engineShaft, 128);
    }
}
