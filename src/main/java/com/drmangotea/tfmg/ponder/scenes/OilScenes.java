package com.drmangotea.tfmg.ponder.scenes;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class OilScenes {
    public static void pumpjack(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("pumpjack", "Pumpjack");
        scene.configureBasePlate(0, 0, 7);

        Selection pipes = util.select().fromTo(0, 2, 0, 0, 4, 0);
        Selection hammer = util.select().fromTo(3, 1, 2, 3, 3, 2);
        Selection base = util.select().fromTo(0, 1, 2, 0, 1, 2);
        Selection crank = util.select().fromTo(6, 2, 2, 6, 2, 2);
        Selection input = util.select().fromTo(5, 1, 1, 6, 1, 2);
        Selection base1 = util.select().fromTo(2, 0, 0, 6, 0, 4);
        Selection base2 = util.select().fromTo(0, 0, 0, 1, 0, 4);
        Selection deposit = util.select().fromTo(0, 1, 0, 0, 1, 0);
        Selection tank = util.select().fromTo(0, 0, 3, 1, 0, 4);

        Selection hammerPart = util.select().fromTo(1, 4, 2, 5, 4, 2);
        Selection hammerHead = util.select().fromTo(6, 4, 2, 6, 4, 2);
        Selection hammerConnector = util.select().fromTo(0, 4, 2, 0, 4, 2);

        AABB hammerGluingSelection = new AABB(util.grid().at(0, 4, 2));

        ElementLink<WorldSectionElement> baseElement1 = scene.world().showIndependentSection(base1, Direction.UP);
        ElementLink<WorldSectionElement> baseElement2 = scene.world().showIndependentSection(base2, Direction.UP);

        scene.idle(20);
        scene.world().hideIndependentSection(baseElement2, Direction.UP);
        scene.idle(25);
        ElementLink<WorldSectionElement> depositElement = scene.world().showIndependentSection(deposit, Direction.UP);
        scene.world().moveSection(depositElement, new Vec3(0d, -4d, 2d), 0);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("First step of mining oil is building industrial pipes from a deposit to the surface");

        ElementLink<WorldSectionElement> pipeElement = scene.world().showIndependentSection(pipes, Direction.SOUTH);
        scene.world().moveSection(pipeElement, new Vec3(0d, -4d, 2d), 0);
        scene.idle(25);
        scene.world().hideIndependentSection(pipeElement, Direction.DOWN);
        scene.world().hideIndependentSection(depositElement, Direction.DOWN);
        scene.idle(25);
        scene.world().showIndependentSection(base2, Direction.SOUTH);
        scene.idle(25);
        ElementLink<WorldSectionElement> pumpjackBaseElement = scene.world().showIndependentSection(base, Direction.SOUTH);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Pumpjack base has to be placed on the top of the pipe")
                .pointAt(util.vector().blockSurface(util.grid().at(0, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(40);
        ElementLink<WorldSectionElement> hammerElement1 = scene.world().showIndependentSection(hammer, Direction.UP);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Pumpjack Hammer Holder needs to be placed behind it")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 3, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);

        ElementLink<WorldSectionElement> connectorElement = scene.world().showIndependentSection(hammerConnector, Direction.UP);
        ElementLink<WorldSectionElement> headElement = scene.world().showIndependentSection(hammerHead, Direction.UP);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Next step is building the Connector And the Head of the Pumpjack above the crank and the base")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 3, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);
        ElementLink<WorldSectionElement> partElement = scene.world().showIndependentSection(hammerPart, Direction.UP);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Now they need to be connected with Pumpjack Hammer Parts")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 3, 2), Direction.WEST))
                .placeNearTarget();

        scene.idle(55);

        scene.overlay().chaseBoundingBoxOutline(PonderPalette.GREEN, hammerGluingSelection, hammerGluingSelection, 1);
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.GREEN, hammerGluingSelection, hammerGluingSelection.expandTowards(6, 0, 0), 50);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Make sure to Super Glue the parts together")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 4, 2), Direction.NORTH))
                .placeNearTarget();

        scene.idle(55);

        scene.world().setKineticSpeed(input, 70);
        scene.world().setKineticSpeed(base1, -140);
        scene.world().showIndependentSection(input, Direction.SOUTH);
        scene.idle(10);
        scene.world().showIndependentSection(crank, Direction.SOUTH);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("The last step is placing a machine input (which is the power input for the pumpjack) with a pumpjack crank above it")
                .pointAt(util.vector().blockSurface(util.grid().at(5, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(60);
    }

    //needs to be updated
    public static void surfaceScanner(SceneBuilder builder, SceneBuildingUtil util) {
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

    public static void distillationTower(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("distillation_tower", "");
        scene.configureBasePlate(0, 0, 6);
        scene.showBasePlate();
        scene.scaleSceneView(.6f);
        Selection burners = util.select().fromTo(3, 1, 3, 4, 1, 4);
        Selection tank = util.select().fromTo(3, 2, 3, 4, 8, 4);
        Selection controller = util.select().fromTo(3, 1, 2, 3, 2, 2);
        Selection output = util.select().fromTo(3, 3, 2, 3, 8, 2);
        Selection oilTank = util.select().fromTo(0, 1, 0, 2, 3, 4);
        scene.world().setKineticSpeed(oilTank, 80);

        ElementLink<WorldSectionElement> tankElement = scene.world().showIndependentSection(tank, Direction.DOWN);

        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("The base of a distillation tower is comprised of steel tanks")
                .pointAt(util.vector().blockSurface(util.grid().at(4, 6, 3), Direction.WEST))
                .placeNearTarget();
        scene.idle(80);
        ElementLink<WorldSectionElement> controllerElement = scene.world().showIndependentSection(controller, Direction.DOWN);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Tower is assembled by placing Steel Distillation Controller next to the tanks")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 2, 3), Direction.WEST))
                .placeNearTarget();

        scene.idle(70);
        ElementLink<WorldSectionElement> outputElement = scene.world().showIndependentSection(output, Direction.DOWN);
        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("To finish the multiblock, place up to 6 Distillation outputs and Industrial Pipes between them")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 7, 3), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);


        ElementLink<WorldSectionElement> burnerElement = scene.world().showIndependentSection(burners, Direction.DOWN);
        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("Place a heat source under the tanks to power it, the dial on the tower shows the power level of the structure ")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);

        scene.world().showIndependentSection(oilTank, Direction.DOWN);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("Oil is inputted into the controller block")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 2, 3), Direction.WEST))
                .placeNearTarget();
        scene.idle(80);
    }
}
