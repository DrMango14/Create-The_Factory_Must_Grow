package com.drmangotea.createindustry.ponder.scenes;

import com.drmangotea.createindustry.registry.TFMGItems;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.utility.Pointing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ElectricityScenes {
    public static void large_generator(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("large_generator", "");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();

        Selection stator = util.select.fromTo(3, 1, 5, 5, 3, 5);
        Selection rotor = util.select.fromTo(4, 2, 3, 4, 2, 3);
        Selection kinetics1 = util.select.fromTo(4, 1, 1, 6 , 2, 2);

        Selection kinetics2 = util.select.fromTo(6, 1, 3, 6, 1, 3);
        Selection cables = util.select.fromTo(1, 1, 3, 2, 2, 6);

        scene.world.setKineticSpeed(kinetics1,120);
        scene.world.setKineticSpeed(kinetics2,120);
        //scene.world.setKineticSpeed(rotor,120);
        scene.world.showIndependentSection(rotor,Direction.DOWN);

        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("The main of the Large Generator is the Rotor")
                .pointAt(util.vector.blockSurface(util.grid.at(4, 2, 5), Direction.WEST))
                .placeNearTarget();

        scene.idle(80);

        ElementLink<WorldSectionElement> statorElement = scene.world.showIndependentSection(stator,Direction.DOWN);


        scene.world.moveSection(statorElement,new Vec3(0d,0d,-2d),0);

        scene.overlay.showText(75)
                .attachKeyFrame()
                .text("To complete the Large Generator, place Stator block around the Rotor")
                .pointAt(util.vector.blockSurface(util.grid.at(3, 2, 3), Direction.WEST))
                .placeNearTarget();


        scene.idle(105);

        scene.world.showIndependentSection(kinetics1,Direction.DOWN);
        scene.world.showIndependentSection(kinetics2,Direction.DOWN);

        scene.world.setKineticSpeed(rotor,120);
        scene.overlay.showText(65)
                .attachKeyFrame()
                .text("Providing rotational power to the Rotor will produce electric energy")
                .pointAt(util.vector.blockSurface(util.grid.at(4, 2, 3), Direction.WEST))
                .placeNearTarget();


        scene.idle(95);


        BlockPos pos = util.grid.at(3, 2, 3);
        Vec3 topOf = util.vector.topOf(pos);
        scene.overlay.showControls(new InputWindowElement(topOf, Pointing.DOWN).rightClick()
                .withItem(new ItemStack(AllItems.WRENCH.get())), 20);



        scene.overlay.showText(60)
                .attachKeyFrame()
                .text("Clicking a side with a wrench will make it the energy output");

        scene.idle(20);
        scene.world.showIndependentSection(cables,Direction.DOWN);
        scene.idle(50);

    }
}
