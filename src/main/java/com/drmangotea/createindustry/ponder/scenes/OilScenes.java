package com.drmangotea.createindustry.ponder.scenes;

import com.drmangotea.createindustry.registry.TFMGItems;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.*;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.utility.Pointing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class OilScenes {

    public static void small_engines(SceneBuilder scene, SceneBuildingUtil util){
        scene.title("small_engines", "");
        scene.configureBasePlate(0, 0, 5);
        scene.setSceneOffsetY(-2);
        scene.idle(10);

        scene.showBasePlate();


        Selection gasoline_engine = util.select.fromTo(2, 1, 1, 2, 1, 2);

        Selection lpg_engine = util.select.fromTo(2, 3, 1, 2, 3, 2);

        Selection turbine_engine = util.select.fromTo(2, 5, 1, 2, 5, 2);

        Selection engine_lever = util.select.fromTo(1, 1, 1, 1, 1, 1);

        Selection exhaust = util.select.fromTo(1, 1, 2, 0, 2, 2);

        Selection input_pump = util.select.fromTo(3, 1, 1, 3, 1, 1);

        Selection tank_1 = util.select.fromTo(4, 1, 1, 4, 1, 1);

        Selection tank_2 = util.select.fromTo(4, 3, 1, 4, 3, 1);

        Selection tank_3 = util.select.fromTo(4, 5, 1, 4, 5, 1);

        scene.world.setKineticSpeed(gasoline_engine,0);
        //scene.world.showSection(util.select.fromTo(0, 2, 0, 4, 2, 4), Direction.UP);



        ElementLink<WorldSectionElement> gasolineEngineElement = scene.world.showIndependentSectionImmediately(gasoline_engine);
        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("To create a small engine, place a front part and a back part behind it")
                .pointAt(util.vector.blockSurface(util.grid.at(4, 0, 4), Direction.WEST))
                .placeNearTarget();
        scene.idle(100);


        scene.world.setKineticSpeed(exhaust,80);
        scene.world.setKineticSpeed(input_pump,80);
        ElementLink<WorldSectionElement> exhaustElement = scene.world.showIndependentSection(exhaust,Direction.DOWN);
        ElementLink<WorldSectionElement> inputPumpElement = scene.world.showIndependentSection(input_pump,Direction.DOWN);
        ElementLink<WorldSectionElement> tankElement1 = scene.world.showIndependentSection(tank_1,Direction.DOWN);

        scene.overlay.showText(70)
                .attachKeyFrame()
                .text("Fuel is inputted to the front part and exhaust is removed from the back part using pipes and an exhaust block")
                .pointAt(util.vector.blockSurface(util.grid.at(4, 0, 3), Direction.WEST))
                .placeNearTarget();
        scene.idle(80);



        ElementLink<WorldSectionElement> leverElement = scene.world.showIndependentSection(engine_lever,Direction.DOWN);
        scene.world.setKineticSpeed(gasoline_engine,225);

        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("Engine will start when redstone signal is applied to the front part")
                .pointAt(util.vector.blockSurface(util.grid.at(1, 1, 1), Direction.WEST))
                .placeNearTarget();
        scene.idle(100);



        scene.world.hideIndependentSection(gasolineEngineElement,Direction.SOUTH);
        scene.world.hideIndependentSection(tankElement1,Direction.SOUTH);

        scene.idle(50);

        ElementLink<WorldSectionElement> lpgEngineElement = scene.world.showIndependentSection(lpg_engine,Direction.DOWN);
        ElementLink<WorldSectionElement> tankElement2 = scene.world.showIndependentSection(tank_2,Direction.DOWN);
        scene.world.moveSection(lpgEngineElement,new Vec3(0d,-2d,0d),0);
        scene.world.moveSection(tankElement2,new Vec3(0d,-2d,0d),0);

        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("There are engines with Gasoline, LPG and Kerosene as a fuel");
        scene.idle(50);
        scene.world.hideIndependentSection(lpgEngineElement,Direction.SOUTH);
        scene.world.hideIndependentSection(tankElement2,Direction.SOUTH);
        scene.idle(30);
        ElementLink<WorldSectionElement> turbineEngineElement = scene.world.showIndependentSection(turbine_engine,Direction.DOWN);
        ElementLink<WorldSectionElement> tankElement3 = scene.world.showIndependentSection(tank_3,Direction.DOWN);
        scene.world.moveSection(turbineEngineElement,new Vec3(0d,-4d,0d),0);
        scene.world.moveSection(tankElement3,new Vec3(0d,-4d,0d),0);

        scene.idle(50);

    }
    public static void pumpjack(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pumpjack", "");
        scene.configureBasePlate(0, 0, 7);


        ////
        Selection pipez = util.select.fromTo(0, 2, 0, 0, 4, 0);
        Selection hammer = util.select.fromTo(3, 1, 2, 3, 4, 2);
        Selection base = util.select.fromTo(0, 1, 2, 0, 1, 2);
        Selection crank = util.select.fromTo(6, 2, 2, 6, 2, 2);
        Selection input = util.select.fromTo(5, 1, 1, 6, 1, 2);
        Selection base1 = util.select.fromTo(2, 0, 0, 6, 0, 4);
        Selection base2 = util.select.fromTo(0, 0, 0, 1, 0, 4);
        Selection deposit = util.select.fromTo(0, 1, 0, 0, 1, 0);
        Selection tank = util.select.fromTo(0, 0, 3, 1, 0, 4);

        Selection hammer_part = util.select.fromTo(1, 5, 2, 5, 5, 2);
        Selection hammer_head = util.select.fromTo(6, 5 ,2, 6, 5, 2);
        Selection hammer_connector = util.select.fromTo(0, 5, 2, 0, 5, 2);


        ////
      //  scene.scaleSceneView(.4f);
        //scene.removeShadow();



        ElementLink<WorldSectionElement> baseElement1 = scene.world.showIndependentSection(base1,Direction.UP);
        ElementLink<WorldSectionElement> baseElement2 = scene.world.showIndependentSection(base2,Direction.UP);

        scene.idle(20);
        scene.world.hideIndependentSection(baseElement2,Direction.UP);
        scene.idle(25);
        ElementLink<WorldSectionElement> depositElement = scene.world.showIndependentSection(deposit,Direction.UP);
        scene.world.moveSection(depositElement,new Vec3(0d,-4d,2d),0);

        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("First step of mining oil is building industrial pipes from deposit to surface");


        ElementLink<WorldSectionElement> pipeElement = scene.world.showIndependentSection(pipez,Direction.SOUTH);
        scene.world.moveSection(pipeElement,new Vec3(0d,-4d,2d),0);
        scene.idle(25);
        scene.world.hideIndependentSection(pipeElement,Direction.DOWN);
        scene.world.hideIndependentSection(depositElement,Direction.DOWN);
        scene.idle(25);
        scene.world.showIndependentSection(base2,Direction.SOUTH);
        scene.idle(25);
        ElementLink<WorldSectionElement> pumpjackBaseElement = scene.world.showIndependentSection(base,Direction.SOUTH);
        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("Pumpjack base has to be placed on the top of the pipe")
                .pointAt(util.vector.blockSurface(util.grid.at(0, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(40);
        ElementLink<WorldSectionElement> hammerElement1 = scene.world.showIndependentSection(hammer,Direction.UP);
        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("Pumpjack Hammer Holder needs to be placed behind it")
                .pointAt(util.vector.blockSurface(util.grid.at(3, 3, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);


        ElementLink<WorldSectionElement> connectorElement = scene.world.showIndependentSection(hammer_connector,Direction.UP);
        ElementLink<WorldSectionElement> headElement = scene.world.showIndependentSection(hammer_head,Direction.UP);
        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("Next step is building the Connector And the Head of the Pumpjack above the crank and the base")
                .pointAt(util.vector.blockSurface(util.grid.at(3, 3, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);
        ElementLink<WorldSectionElement> partElement = scene.world.showIndependentSection(hammer_part,Direction.UP);
        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("Now they need to be connected with Pumpjack Pammer Parts")
                .pointAt(util.vector.blockSurface(util.grid.at(3, 3, 2), Direction.WEST))
                .placeNearTarget();


        scene.idle(40);
        scene.world.setKineticSpeed(input,70);
        scene.world.setKineticSpeed(base1,-140);
        scene.world.showIndependentSection(input,Direction.SOUTH);
        scene.idle(10);
        scene.world.showIndependentSection(crank,Direction.SOUTH);
        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("The last step is placing a machine input (which is a power input for the pumpjack) with a pumpjack crank above it")
                .pointAt(util.vector.blockSurface(util.grid.at(5, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(40);



    }
    public static void diesel_engine(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("diesel_engine", "");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        Selection engine = util.select.fromTo(2, 1, 0, 2, 1, 2);
        Selection exhaust = util.select.fromTo(2, 1, 3, 2, 2, 4);
        Selection air = util.select.fromTo(0, 1, 0, 1, 1, 2);
        Selection input = util.select.fromTo(3, 1, 0, 4, 1, 2);



        scene.idle(30);

        ElementLink<WorldSectionElement> engineElement = scene.world.showIndependentSection(engine,Direction.DOWN);
        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("Diesel Engine is assembled by placing a shaft in the front of a diesel engine block");


        scene.idle(70);



        ElementLink<WorldSectionElement> inputElement = scene.world.showIndependentSection(input,Direction.DOWN);
        ElementLink<WorldSectionElement> exhaustElement = scene.world.showIndependentSection(exhaust,Direction.DOWN);
        scene.world.setKineticSpeed(input,80);
        scene.world.setKineticSpeed(exhaust,80);

        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("Carbon Dioxide has to be outputted by pipes and exhaust block")
                .pointAt(util.vector.blockSurface(util.grid.at(2, 2, 4), Direction.WEST))
                .placeNearTarget();
        scene.idle(40);

        ElementLink<WorldSectionElement> airElement = scene.world.showIndependentSection(air,Direction.DOWN);
        scene.world.setKineticSpeed(air,80);
        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("Diesel engines need air that can be collected with air intakes")
                .pointAt(util.vector.blockSurface(util.grid.at(0, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(10);
        scene.world.setKineticSpeed(engine,128);
        scene.idle(70);




    }
    public static void diesel_engine_expansion(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("diesel_engine_expansion", "");
        scene.configureBasePlate(0, 0, 6);
        scene.showBasePlate();

        Selection engine = util.select.fromTo(2, 1, 0, 2, 1, 2);
        Selection input = util.select.fromTo(0, 1, 0, 1, 1, 2);
        Selection exhaust = util.select.fromTo(3, 1, 0, 4, 2, 2);

        Selection expansion = util.select.fromTo(2, 1, 3, 2, 1, 3);
        Selection air = util.select.fromTo(2, 1, 4, 2, 1, 5);
        Selection coolant = util.select.fromTo(0, 1, 3, 1, 1, 3);
        Selection oil = util.select.fromTo(3, 1, 3, 4, 1, 3);

        scene.world.setKineticSpeed(input,80);
        scene.world.setKineticSpeed(exhaust,80);
        scene.world.setKineticSpeed(air,80);
        scene.world.setKineticSpeed(coolant,80);
        scene.world.setKineticSpeed(oil,80);
        scene.world.setKineticSpeed(engine,128);



        scene.world.showIndependentSection(engine,Direction.DOWN);
        scene.world.showIndependentSection(input,Direction.DOWN);
        scene.world.showIndependentSection(exhaust,Direction.DOWN);
        ElementLink<WorldSectionElement> airElement = scene.world.showIndependentSection(air,Direction.DOWN);


        scene.world.moveSection(airElement,new Vec3(0d,0d,-1d),0);

        scene.idle(30);

        scene.world.moveSection(airElement,new Vec3(0d,0d,1d),10);

        scene.idle(30);

        scene.world.showIndependentSection(expansion,Direction.DOWN);

        scene.idle(20);

        scene.world.showIndependentSection(coolant,Direction.DOWN);
        scene.world.showIndependentSection(oil,Direction.DOWN);

        scene.overlay.showText(100)
                .attachKeyFrame()
                .text("Diesel engine expansions give diesel engines 2 new fluid slots, for cooling and lubrication")
                .pointAt(util.vector.blockSurface(util.grid.at(2, 1, 3), Direction.WEST))
                .placeNearTarget();



        scene.idle(50);




    }

    public static void surface_scanner(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("surface_scanner", "");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        Selection scanner = util.select.fromTo(0, 1, 0, 5, 1, 5);

        scene.world.showSection(util.select.fromTo(0, 1, 0, 5, 1, 5), Direction.UP);
        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("Surface Scanner is used for finding crude oil deposits")
                .pointAt(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);
        scene.world.setKineticSpeed(scanner,30);
        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("When rotation is applied, the machine starts to find the nearest oil deposit")
                .pointAt(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);


        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("When a deposit is found, compass at the top will show the direction")
                .pointAt(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);


    }

    public static void distillation_tower(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("distillation_tower", "");
        scene.configureBasePlate(0, 0, 6);
        scene.showBasePlate();
        scene.scaleSceneView(.4f);
        Selection burners = util.select.fromTo(3, 1, 2, 4, 1, 3);
        Selection tank = util.select.fromTo(3, 2, 2, 4, 14, 3);
        Selection controller = util.select.fromTo(2, 1, 3, 2, 2, 3);
        Selection output = util.select.fromTo(2, 3, 3, 2, 13, 3);
        Selection oil_tank = util.select.fromTo(0, 1, 4, 2, 3, 4);
        scene.world.setKineticSpeed(oil_tank,80);

        ElementLink<WorldSectionElement> tankElement = scene.world.showIndependentSection(tank,Direction.DOWN);

        scene.overlay.showText(70)
                .attachKeyFrame()
                .text("Base of a distillation tower are steel tanks")
                .pointAt(util.vector.blockSurface(util.grid.at(4, 6, 3), Direction.WEST))
                .placeNearTarget();
        scene.idle(80);
        ElementLink<WorldSectionElement> controllerElement = scene.world.showIndependentSection(controller,Direction.DOWN);

        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("Tower is assembled by placing Steel Distillation Controller next to the tanks")
                .pointAt(util.vector.blockSurface(util.grid.at(2, 2, 3), Direction.WEST))
                .placeNearTarget();

        scene.idle(70);
        ElementLink<WorldSectionElement> outputElement = scene.world.showIndependentSection(output,Direction.DOWN);
        scene.overlay.showText(60)
                .attachKeyFrame()
                .text("To finish the multiblock place up to 6 Distillation outputs and  Industrial Pipes between them")
                .pointAt(util.vector.blockSurface(util.grid.at(2, 7, 3), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);


        ElementLink<WorldSectionElement> burnerElement = scene.world.showIndependentSection(burners,Direction.DOWN);
        scene.overlay.showText(60)
                .attachKeyFrame()
                .text("Place Blaze Burners under the tanks to power it, dial on the tower shows the power level of the structure ")
                .pointAt(util.vector.blockSurface(util.grid.at(3, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);

        ElementLink<WorldSectionElement> oilTankElement = scene.world.showIndependentSection(oil_tank,Direction.DOWN);

        scene.overlay.showText(60)
                .attachKeyFrame()
                .text("Oil is inputted into the controller block")
                .pointAt(util.vector.blockSurface(util.grid.at(2, 2, 3), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);

        scene.overlay.showText(120)
                .attachKeyFrame()
                .text("Every output block outputs one of the oil products")
                .pointAt(util.vector.blockSurface(util.grid.at(2, 1, 3), Direction.WEST))
                .placeNearTarget();

///
        scene.overlay.showText(120)
                .attachKeyFrame()
                .text("LPG")
                .colored(PonderPalette.BLUE)
                .pointAt(util.vector.blockSurface(util.grid.at(2, 13, 3), Direction.WEST))
                .placeNearTarget();

        scene.overlay.showText(120)
                .attachKeyFrame()
                .text("Gasoline")
                .colored(PonderPalette.BLUE)
                .pointAt(util.vector.blockSurface(util.grid.at(2, 11, 3), Direction.WEST))
                .placeNearTarget();



        scene.overlay.showText(120)
                .attachKeyFrame()
                .text("Naphtha")
                .colored(PonderPalette.BLUE)
                .pointAt(util.vector.blockSurface(util.grid.at(2, 9, 3), Direction.WEST))
                .placeNearTarget();

        scene.overlay.showText(120)
                .attachKeyFrame()
                .text("Kerosene")
                .colored(PonderPalette.BLUE)
                .pointAt(util.vector.blockSurface(util.grid.at(2, 7, 3), Direction.WEST))
                .placeNearTarget();

        scene.overlay.showText(120)
                .attachKeyFrame()
                .text("Diesel")
                .colored(PonderPalette.BLUE)
                .pointAt(util.vector.blockSurface(util.grid.at(2, 5, 3), Direction.WEST))
                .placeNearTarget();

        scene.overlay.showText(120)
                .attachKeyFrame()
                .text("Heavy Oil")
                .colored(PonderPalette.BLUE)
                .pointAt(util.vector.blockSurface(util.grid.at(2, 3, 3), Direction.WEST))
                .placeNearTarget();



        scene.idle(150);

    }

    public static void radial_engines(SceneBuilder scene, SceneBuildingUtil util){
        scene.title("radial_engines", "");
        scene.configureBasePlate(0, 0, 5);
        scene.idle(10);

        scene.showBasePlate();


        Selection engine_small = util.select.fromTo(2, 1, 1, 2, 1, 1);

        Selection engine_large = util.select.fromTo(1, 1, 1, 1, 1, 1);


        Selection engine_lever = util.select.fromTo(3, 1, 0, 3, 1, 0);


        Selection input_pump = util.select.fromTo(3, 1, 2, 3, 1, 2);

        Selection input = util.select.fromTo(3, 1, 1, 3, 1, 1);

        Selection tank_1 = util.select.fromTo(3, 1, 3, 3, 2, 3);

        Selection tank_2 = util.select.fromTo(2, 1, 3, 2, 2, 3);


        scene.world.setKineticSpeed(engine_small,0);




        ElementLink<WorldSectionElement> engineElement = scene.world.showIndependentSectionImmediately(engine_small);
        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("Radial Engines are a special Type of Engine that doesn't require an exhaust block and has a shaft from both sides")
                .pointAt(util.vector.blockSurface(util.grid.at(4, 0, 4), Direction.WEST))
                .placeNearTarget();
        scene.idle(100);



        scene.world.setKineticSpeed(input_pump,80);
        ElementLink<WorldSectionElement> inputElement = scene.world.showIndependentSection(input,Direction.DOWN);
        scene.idle(50);

        BlockPos inputPos = util.grid.at(2, 1, 1);
        Vec3 topOf = util.vector.topOf(inputPos);
        scene.overlay.showControls(new InputWindowElement(topOf, Pointing.DOWN).rightClick()
                .withItem(new ItemStack(AllItems.WRENCH.get())), 20);

        scene.overlay.showText(70)
                .attachKeyFrame()
                .text("Clicking the Engine from one of its sides will spawn an input slot that can accept fuel and redstone signals")
                .pointAt(util.vector.blockSurface(util.grid.at(2, 1, 1), Direction.WEST))
                .placeNearTarget();
        scene.idle(100);
        scene.overlay.showText(40)
                .attachKeyFrame()
                .text("Regular Radial Engines uses gasoline as fuel")
                .pointAt(util.vector.blockSurface(util.grid.at(2, 1, 1), Direction.WEST))
                .placeNearTarget();


        scene.idle(80);


        ElementLink<WorldSectionElement> inputPumpElement = scene.world.showIndependentSection(input_pump,Direction.DOWN);
        ElementLink<WorldSectionElement> tankElement1 = scene.world.showIndependentSection(tank_1,Direction.DOWN);


        ElementLink<WorldSectionElement> leverElement = scene.world.showIndependentSection(engine_lever,Direction.DOWN);
        scene.world.setKineticSpeed(engine_small,180);
        scene.world.setKineticSpeed(engine_large,180);

        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("Engine will start when redstone signal is applied to the input slot or the block itself")
                .pointAt(util.vector.blockSurface(util.grid.at(3, 1, 0), Direction.WEST))
                .placeNearTarget();

        scene.idle(100);



        scene.world.hideIndependentSection(engineElement,Direction.SOUTH);
        scene.world.hideIndependentSection(tankElement1,Direction.SOUTH);

        scene.idle(50);

        ElementLink<WorldSectionElement> largeEngineElement = scene.world.showIndependentSection(engine_large,Direction.DOWN);
        ElementLink<WorldSectionElement> tankElement2 = scene.world.showIndependentSection(tank_2,Direction.DOWN);
        scene.world.moveSection(largeEngineElement,new Vec3(1d,0d,0d),0);
        scene.world.moveSection(tankElement2,new Vec3(1d,0d,0d),0);

        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("The second variant of a radial is The Large Radial Engine which uses kerosene as fuel");
        scene.idle(50);



    }




}
