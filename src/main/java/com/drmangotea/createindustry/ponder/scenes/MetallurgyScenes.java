package com.drmangotea.createindustry.ponder.scenes;

import com.drmangotea.createindustry.registry.TFMGItems;
import com.simibubi.create.foundation.ponder.*;
import com.simibubi.create.foundation.ponder.element.EntityElement;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.utility.Pointing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class MetallurgyScenes {

    public static void blast_furnace(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("blast_furnace", "");
        scene.configureBasePlate(0, 0, 5);
        scene.scaleSceneView(.7f);
        scene.showBasePlate();
        //
        Selection output = util.select.fromTo(2, 1, 2, 2, 1, 2);

        Selection furnace1 = util.select.fromTo(1, 1, 3, 3, 1, 3);
        Selection furnace2 = util.select.fromTo(2, 1, 4, 2, 1, 4);

        Selection reinforcement1 = util.select.fromTo(3, 1, 2, 3, 2, 2);
        Selection reinforcement2 = util.select.fromTo(3, 1, 4, 3, 2, 4);
        Selection reinforcement3 = util.select.fromTo(1, 1, 4, 1, 2, 4);
        Selection reinforcement4 = util.select.fromTo(1, 1, 2, 1, 2, 2);

        Selection furnace3 = util.select.fromTo(1, 2, 3, 3, 2, 3);
        Selection furnace4 = util.select.fromTo(2, 2, 2, 2, 2, 4);

        Selection furnace5 = util.select.fromTo(0, 3, 0, 4, 4, 4);


        Selection pipez = util.select.fromTo(0, 1, 0, 4, 2, 1);
        scene.world.setKineticSpeed(pipez, 80);

        //

        scene.world.showIndependentSection(output, Direction.DOWN);
        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("Main part of a blast furnace is a blast furnace output")
                .pointAt(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);

        scene.overlay.showText(50)
                .attachKeyFrame()
                .text("To make a blast furnace, make a cylinder of fireproof bricks around it");


        scene.world.showIndependentSection(furnace1, Direction.DOWN);
        scene.world.showIndependentSection(furnace2, Direction.DOWN);
        scene.idle(10);
        scene.world.showIndependentSection(furnace3, Direction.DOWN);
        scene.world.showIndependentSection(furnace4, Direction.DOWN);
        scene.idle(10);
        scene.world.showIndependentSection(furnace5, Direction.DOWN);

        scene.idle(70);

        scene.world.showIndependentSection(reinforcement1, Direction.DOWN);
        scene.world.showIndependentSection(reinforcement2, Direction.DOWN);
        scene.world.showIndependentSection(reinforcement3, Direction.DOWN);
        scene.world.showIndependentSection(reinforcement4, Direction.DOWN);

        scene.overlay.showText(100)
                .attachKeyFrame()
                .text("Blast furnaces need reinforcements that are at least half the total height")
                .pointAt(util.vector.blockSurface(util.grid.at(1, 2, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);

        scene.world.showIndependentSection(pipez, Direction.DOWN);

        scene.idle(20);
        scene.overlay.showText(100)
                .attachKeyFrame()
                .text("Everything is inserted to through a hole at the top");


        ItemStack stack1 = new ItemStack(TFMGItems.COAL_COKE_DUST.get());
        ItemStack stack2 = new ItemStack(TFMGItems.BLASTING_MIXTURE.get());

        scene.world.createItemEntity(util.vector.centerOf(2, 6, 3), util.vector.of(0, 0, 0), stack1);
        scene.idle(10);
        scene.world.createItemEntity(util.vector.centerOf(2, 6, 3), util.vector.of(0, 0, 0), stack1);
        scene.idle(10);
        scene.world.createItemEntity(util.vector.centerOf(2, 6, 3), util.vector.of(0, 0, 0), stack1);
        scene.idle(10);

        scene.world.createItemEntity(util.vector.centerOf(2, 6, 3), util.vector.of(0, 0, 0), stack2);
        scene.idle(10);
        scene.world.createItemEntity(util.vector.centerOf(2, 6, 3), util.vector.of(0, 0, 0), stack2);
        scene.idle(10);
        scene.world.createItemEntity(util.vector.centerOf(2, 6, 3), util.vector.of(0, 0, 0), stack2);
        scene.idle(10);

    }

    public static void coke_oven(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("coke_oven", "");
        scene.configureBasePlate(0, 0, 7);
        scene.scaleSceneView(.7f);

        scene.showBasePlate();


        Selection coke_oven_middle = util.select.fromTo(3, 1, 2, 3, 3, 4);

        Selection coke_oven_right = util.select.fromTo(4, 1, 2, 5, 3, 4);
        Selection coke_oven_left = util.select.fromTo(2, 1, 2, 1, 3, 4);

        Selection chutes = util.select.fromTo(1, 4, 4, 5, 4, 4);
        Selection exhaust = util.select.fromTo(0, 4, 2, 5, 6, 2);
        Selection output = util.select.fromTo(1, 4, 3, 5, 5, 3);

        Selection tank = util.select.fromTo(6, 1, 3, 6, 5, 3);

        ItemStack coal = new ItemStack(Items.COAL, 3);
        ItemStack coal_coke = new ItemStack(TFMGItems.COAL_COKE.get(), 10);

        scene.world.setKineticSpeed(output, 80);
        scene.world.setKineticSpeed(exhaust, 80);


        scene.world.showIndependentSection(coke_oven_middle, Direction.DOWN);
        scene.overlay.showText(70)
                .attachKeyFrame()
                .text("Coke Oven is created by placing coke oven block as shown and clicking the front block with a wrench")
                .pointAt(util.vector.blockSurface(util.grid.at(3, 2, 2), Direction.WEST))
                .placeNearTarget();

        scene.idle(90);

        scene.world.showIndependentSection(coke_oven_right, Direction.DOWN);
        scene.world.showIndependentSection(coke_oven_left, Direction.DOWN);
        scene.overlay.showText(70)
                .attachKeyFrame()
                .text("Coke Ovens are slow so it is beneficial to have long arrays of them")
                .pointAt(util.vector.blockSurface(util.grid.at(1, 2, 2), Direction.WEST))
                .placeNearTarget();


        scene.idle(30);


        scene.world.showIndependentSection(chutes, Direction.DOWN);
        scene.overlay.showText(70)
                .attachKeyFrame()
                .text("Coal can be inserted from anywhere");
        scene.idle(10);
        ElementLink<EntityElement> item = null;
        for (int i = 0; i < 3; i++) {
            scene.idle(10);

            for (int y = 0; y < 5; y++) {

                item = scene.world.createItemEntity(util.vector.centerOf(1 + y, 6, 4), util.vector.of(0, 0, 0), coal);
            }
        }
        if (item != null)
            scene.world.modifyEntity(item, Entity::discard);
        scene.idle(40);

        scene.world.showIndependentSection(exhaust, Direction.DOWN);
        scene.world.showIndependentSection(tank, Direction.DOWN);
        scene.world.showIndependentSection(output, Direction.DOWN);

        scene.overlay.showText(70)
                .attachKeyFrame()
                .text("Creosote and Carbon Dioxide have to be pumped out of the machine")
                .pointAt(util.vector.blockSurface(util.grid.at(3, 4, 2), Direction.WEST))
                .placeNearTarget();


        scene.idle(50);


        scene.overlay.showText(70)
                .attachKeyFrame()
                .text("After some time, coal coke will fall out of the machine at the front");


        for (int y = 0; y < 5; y++) {
            scene.world.createItemEntity(util.vector.centerOf(1 + y, 2, 1), util.vector.of(0, 0, 0), coal_coke);
        }


    }

    public static void casting(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("casting", "");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        Selection spout = util.select.fromTo(2, 3, 2, 2, 3, 2);
        Selection basin_empty = util.select.fromTo(2, 1, 2, 2, 1, 2);
        Selection basin_with_mold = util.select.fromTo(2, 1, 4, 2, 1, 4);

        Selection chain_drives = util.select.fromTo(5, 0, 3, 5, 3, 3);

        Selection pipez = util.select.fromTo(3, 1, 2, 4, 3, 3);

        Selection funnel = util.select.fromTo(2, 1, 1, 2, 1, 1);

        ItemStack steel_ingot = new ItemStack(TFMGItems.STEEL_INGOT.get());

        scene.world.setKineticSpeed(chain_drives, 80);
        scene.world.setKineticSpeed(pipez, 80);

        ElementLink<WorldSectionElement> emptyBasinElement = scene.world.showIndependentSection(basin_empty,Direction.DOWN);
        scene.idle(20);
        scene.world.showIndependentSection(spout, Direction.DOWN);

        scene.overlay.showText(70)
                .attachKeyFrame()
                .text("Casting is done by pouring liquid metal into a casting basin with a casting spout")
                .pointAt(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(20);
        scene.world.showIndependentSection(pipez, Direction.DOWN);
        scene.world.showIndependentSection(chain_drives, Direction.DOWN);

        scene.idle(50);


        ElementLink<WorldSectionElement> basinElement = scene.world.showIndependentSection(basin_with_mold,null);
        scene.world.hideIndependentSection(emptyBasinElement,null);

        scene.world.moveSection(basinElement,new Vec3(0d,0d,-2d),0);


        BlockPos basinPos = util.grid.at(2, 1, 2);
        Vec3 topOf = util.vector.topOf(basinPos);
        scene.overlay.showControls(new InputWindowElement(topOf, Pointing.DOWN).rightClick()
                .withItem(new ItemStack(TFMGItems.INGOT_MOLD.get())), 20);


        scene.idle(10);

        scene.overlay.showText(70)
                .attachKeyFrame()
                .text("Casting basin needs a mold to function")
                .pointAt(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.WEST))
                .placeNearTarget();

        scene.idle(20);
        scene.world.showIndependentSection(funnel,Direction.SOUTH);
        scene.idle(20);

        scene.world.createItemEntity(util.vector.centerOf(2, 1, 1), util.vector.of(0, 0, 0), steel_ingot);

    }
}
