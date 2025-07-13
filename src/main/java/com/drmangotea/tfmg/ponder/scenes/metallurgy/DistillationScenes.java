package com.drmangotea.tfmg.ponder.scenes.metallurgy;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.Direction;

public class DistillationScenes {
    public static void distillation_tower(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("distillation_tower", "");
        scene.configureBasePlate(0, 0, 6);
        scene.showBasePlate();
        scene.scaleSceneView(.6f);
        Selection burners = util.select().fromTo(3, 1, 3, 4, 1, 4);
        Selection tank = util.select().fromTo(3, 2, 3, 4, 8, 4);
        Selection controller = util.select().fromTo(3, 1, 2, 3, 2, 2);
        Selection output = util.select().fromTo(3, 3, 2, 3, 8, 2);
        Selection oil_tank = util.select().fromTo(0, 1, 0, 2, 3, 4);
        scene.world().setKineticSpeed(oil_tank, 80);

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

        scene.world().showIndependentSection(oil_tank, Direction.DOWN);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("Oil is inputted into the controller block")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 2, 3), Direction.WEST))
                .placeNearTarget();
        scene.idle(80);


    }
}
