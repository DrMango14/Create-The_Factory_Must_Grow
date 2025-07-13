package com.drmangotea.tfmg.ponder.scenes.kinetics.engines;

import com.drmangotea.tfmg.ponder.TFMGSceneBuilder;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
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

public class EngineScenes {

    public static void engines(SceneBuilder builder, SceneBuildingUtil util) {
        TFMGSceneBuilder scene = new TFMGSceneBuilder(builder);
        scene.title("engines", "Engines");
        scene.configureBasePlate(0, 0, 7);


        scene.showBasePlate();


        Selection engine = util.select().fromTo(3, 1, 3, 3, 1, 4);
        Selection engine_front = util.select().fromTo(3, 1, 2, 3, 1, 2);
        Selection engine_front_shaft = util.select().fromTo(2, 1, 2, 2, 1, 2);

        Selection lever = util.select().fromTo(4, 1, 2, 4, 1, 2);
        Selection cog = util.select().fromTo(3, 1, 0, 3, 1, 1);

        Selection fuelTank = util.select().fromTo(4, 1, 3, 5, 2, 3);
        Selection tank = util.select().fromTo(4, 1, 4, 4, 2, 4);
        Selection exhaust = util.select().fromTo(2, 1, 4, 1, 2, 4);
        scene.world().setKineticSpeed(fuelTank, 70);
        scene.world().setKineticSpeed(exhaust, 70);
        scene.world().setKineticSpeed(cog, 70);


        ElementLink<WorldSectionElement> engineElement = scene.world().showIndependentSection(engine, Direction.DOWN);
        ElementLink<WorldSectionElement> engineFrontElement = scene.world().showIndependentSection(engine_front, Direction.DOWN);

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
                .withItem(new ItemStack(TFMGItems.CRANKSHAFT));
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
                .withItem(new ItemStack(AllItems.EMPTY_SCHEMATIC));


        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Engine configuration can be changed with a schematic");
        scene.idle(60);


        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("Next step is inserting the cylinders (or turbine blades in turbine engines)");

        ElementLink<WorldSectionElement> engineFrontShaftElement = scene.world().showIndependentSection(engine_front_shaft, Direction.DOWN);
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
        //scene.world().hideIndependentSection(engineFrontElement,Direction.DOWN);


        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Shafts are inserted by right clicking");
        scene.overlay().showControls(enginePos, Pointing.DOWN, 50)
                .rightClick()
                .withItem(new ItemStack(AllBlocks.SHAFT));
        scene.idle(70);

        ElementLink<WorldSectionElement> fuelTankElement = scene.world().showIndependentSection(fuelTank, Direction.DOWN);
        scene.idle(10);
        ElementLink<WorldSectionElement> exhuastElement = scene.world().showIndependentSection(exhaust, Direction.DOWN);

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
        scene.world().setKineticSpeed(engine_front_shaft, 70);
        scene.world().showIndependentSection(lever, Direction.DOWN);
        scene.world().showIndependentSection(cog, Direction.DOWN);
        scene.overlay().showText(128)
                .attachKeyFrame()
                .text("The engine can be started with a redstone signal");
    }
}
