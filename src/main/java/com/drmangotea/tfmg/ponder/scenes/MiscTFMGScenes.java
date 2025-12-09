package com.drmangotea.tfmg.ponder.scenes;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class MiscTFMGScenes {

    public static void diesel_engine(SceneBuilder builder, SceneBuildingUtil util) {
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

    public static void diesel_engine_expansion(SceneBuilder builder, SceneBuildingUtil util) {
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
//needs to be updated
    public static void surface_scanner(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("surface_scanner", "");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        Selection scanner = util.select().fromTo(0, 1, 0, 5, 1, 5);

        scene.world().showSection(util.select().fromTo(0, 1, 0, 5, 1, 5), Direction.UP);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("The Surface Scanner is used for finding crude oil deposits")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);
        scene.world().setKineticSpeed(scanner, 30);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("When rotation is applied, the machine starts to find the nearest oil deposit")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);


        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("When a deposit is found, compass at the top will show the direction")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);


    }



    public static void radial_engines(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("radial_engines", "");
        scene.configureBasePlate(0, 0, 5);
        scene.idle(10);

        scene.showBasePlate();


        Selection engine_small = util.select().fromTo(2, 1, 1, 2, 1, 1);

        Selection engine_large = util.select().fromTo(1, 1, 1, 1, 1, 1);


        Selection engine_lever = util.select().fromTo(3, 1, 0, 3, 1, 0);


        Selection input_pump = util.select().fromTo(3, 1, 2, 3, 1, 2);

        Selection input = util.select().fromTo(3, 1, 1, 3, 1, 1);

        Selection tank_1 = util.select().fromTo(3, 1, 3, 3, 2, 3);

        Selection tank_2 = util.select().fromTo(2, 1, 3, 2, 2, 3);


        scene.world().setKineticSpeed(engine_small, 0);


        ElementLink<WorldSectionElement> engineElement = scene.world().showIndependentSectionImmediately(engine_small);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Radial Engines are a special Type of Engine that doesn't require an exhaust block")
                .pointAt(util.vector().blockSurface(util.grid().at(4, 0, 4), Direction.WEST))
                .placeNearTarget();
        scene.idle(100);


        scene.world().setKineticSpeed(input_pump, 80);
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


        ElementLink<WorldSectionElement> inputPumpElement = scene.world().showIndependentSection(input_pump, Direction.DOWN);
        ElementLink<WorldSectionElement> tankElement1 = scene.world().showIndependentSection(tank_1, Direction.DOWN);


        ElementLink<WorldSectionElement> leverElement = scene.world().showIndependentSection(engine_lever, Direction.DOWN);
        scene.world().setKineticSpeed(engine_small, 180);
        scene.world().setKineticSpeed(engine_large, 180);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Engine will start when redstone signal is applied to the input slot or the block itself")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 1, 0), Direction.WEST))
                .placeNearTarget();

        scene.idle(100);

        scene.world().hideIndependentSection(engineElement, Direction.SOUTH);
        scene.world().hideIndependentSection(tankElement1, Direction.SOUTH);

        scene.idle(50);

        ElementLink<WorldSectionElement> largeEngineElement = scene.world().showIndependentSection(engine_large, Direction.DOWN);
        ElementLink<WorldSectionElement> tankElement2 = scene.world().showIndependentSection(tank_2, Direction.DOWN);
        scene.world().moveSection(largeEngineElement, new Vec3(1d, 0d, 0d), 0);
        scene.world().moveSection(tankElement2, new Vec3(1d, 0d, 0d), 0);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("The second variant of a radial is The Large Radial Engine which uses kerosene as fuel");
        scene.idle(50);
    }


    public static void large_generator(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("large_generator", "");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();

        Selection stator = util.select().fromTo(3, 1, 5, 5, 3, 5);
        Selection rotor = util.select().fromTo(4, 2, 3, 4, 2, 3);
        Selection kinetics1 = util.select().fromTo(4, 1, 1, 6, 2, 2);

        Selection kinetics2 = util.select().fromTo(6, 1, 3, 6, 1, 3);
        Selection cables = util.select().fromTo(1, 1, 3, 2, 2, 6);

        scene.world().setKineticSpeed(kinetics1, 120);
        scene.world().setKineticSpeed(kinetics2, 120);
        //scene.world().setKineticSpeed(rotor,120);
        scene.world().showIndependentSection(rotor, Direction.DOWN);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("The main part of the Large Generator is the Rotor")
                .pointAt(util.vector().blockSurface(util.grid().at(4, 2, 5), Direction.WEST))
                .placeNearTarget();

        scene.idle(80);

        ElementLink<WorldSectionElement> statorElement = scene.world().showIndependentSection(stator, Direction.DOWN);


        scene.world().moveSection(statorElement, new Vec3(0d, 0d, -2d), 0);

        scene.overlay().showText(75)
                .attachKeyFrame()
                .text("To complete the Large Generator, place a Stator block around the Rotor")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.WEST))
                .placeNearTarget();


        scene.idle(105);

        scene.world().showIndependentSection(kinetics1, Direction.DOWN);
        scene.world().showIndependentSection(kinetics2, Direction.DOWN);

        scene.world().setKineticSpeed(rotor, 120);
        scene.overlay().showText(65)
                .attachKeyFrame()
                .text("Providing rotational power to the Rotor will produce electric energy")
                .pointAt(util.vector().blockSurface(util.grid().at(4, 2, 3), Direction.WEST))
                .placeNearTarget();


        scene.idle(95);


        BlockPos pos = util.grid().at(3, 2, 3);
        Vec3 topOf = util.vector().topOf(pos);
        scene.overlay().showControls(topOf, Pointing.DOWN, 20).rightClick()
                .withItem(new ItemStack(AllItems.WRENCH.get()));


        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("Clicking a side with a wrench will make it the energy output");

        scene.idle(20);
        scene.world().showIndependentSection(cables, Direction.DOWN);
        scene.idle(50);

    }
}
