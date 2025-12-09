package com.drmangotea.tfmg.ponder.scenes.metallurgy;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PumpjackScenes {
    public static void pumpjack(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("pumpjack", "Pumpjack");
        scene.configureBasePlate(0, 0, 7);


        ////
        Selection pipez = util.select().fromTo(0, 2, 0, 0, 4, 0);
        Selection hammer = util.select().fromTo(3, 1, 2, 3, 3, 2);
        Selection base = util.select().fromTo(0, 1, 2, 0, 1, 2);
        Selection crank = util.select().fromTo(6, 2, 2, 6, 2, 2);
        Selection input = util.select().fromTo(5, 1, 1, 6, 1, 2);
        Selection base1 = util.select().fromTo(2, 0, 0, 6, 0, 4);
        Selection base2 = util.select().fromTo(0, 0, 0, 1, 0, 4);
        Selection deposit = util.select().fromTo(0, 1, 0, 0, 1, 0);
        Selection tank = util.select().fromTo(0, 0, 3, 1, 0, 4);

        Selection hammer_part = util.select().fromTo(1, 4, 2, 5, 4, 2);
        Selection hammer_head = util.select().fromTo(6, 4, 2, 6, 4, 2);
        Selection hammer_connector = util.select().fromTo(0, 4, 2, 0, 4, 2);


        ////
        //  scene.scaleSceneView(.4f);
        //scene.removeShadow();


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


        ElementLink<WorldSectionElement> pipeElement = scene.world().showIndependentSection(pipez, Direction.SOUTH);
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


        ElementLink<WorldSectionElement> connectorElement = scene.world().showIndependentSection(hammer_connector, Direction.UP);
        ElementLink<WorldSectionElement> headElement = scene.world().showIndependentSection(hammer_head, Direction.UP);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Next step is building the Connector And the Head of the Pumpjack above the crank and the base")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 3, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);
        ElementLink<WorldSectionElement> partElement = scene.world().showIndependentSection(hammer_part, Direction.UP);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Now they need to be connected with Pumpjack Hammer Parts")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 3, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(50);

        AABB hammerHeadBB = new AABB(util.grid().at(6, 4, 2));

        scene.overlay().chaseBoundingBoxOutline(PonderPalette.GREEN, hammerHeadBB, hammerHeadBB, 1);
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.GREEN, hammerHeadBB, hammerHeadBB.expandTowards(-6, 0, 0), 80);
        scene.idle(10);
        scene.overlay().showControls(util.vector().centerOf(util.grid().at(4, 4, 2)), Pointing.RIGHT, 40)
                .withItem(AllItems.SUPER_GLUE.asStack());

        scene.idle(15);
        scene.overlay().showText(60)
                .pointAt(util.vector().blockSurface(util.grid().at(4, 4, 2), Direction.NORTH))
                .attachKeyFrame()
                .placeNearTarget()
                .text("Make sure to use Super Glue, otherwise the pumpjack will not assemble properly");
        scene.idle(70);

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
}
