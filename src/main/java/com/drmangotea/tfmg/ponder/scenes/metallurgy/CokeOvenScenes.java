package com.drmangotea.tfmg.ponder.scenes.metallurgy;

import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CokeOvenScenes {
    public static void coke_oven(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("coke_oven", "");
        scene.configureBasePlate(0, 0, 6);
        scene.scaleSceneView(.7f);

        scene.showBasePlate();


        Selection coke_oven_middle = util.select().fromTo(3, 1, 2, 3, 3, 4);

        Selection coke_oven_right = util.select().fromTo(4, 1, 2, 5, 3, 4);
        Selection coke_oven_left = util.select().fromTo(2, 1, 2, 1, 3, 4);

        Selection chutes = util.select().fromTo(2, 4, 3, 4, 4, 3);
        Selection exhaust = util.select().fromTo(2, 4, 2, 4, 4, 2)
                .add(util.select().fromTo(0, 1, 2, 1, 5, 2))
                ;


        Selection creosoteOutput = util.select().fromTo(0, 1, 5, 5, 5, 5);

        ItemStack coal = new ItemStack(Items.COAL, 3);
        ItemStack coal_coke = new ItemStack(TFMGItems.COAL_COKE.get(), 10);

        scene.world().setKineticSpeed(creosoteOutput, 80);
        scene.world().setKineticSpeed(exhaust, 80);


        scene.world().showIndependentSection(coke_oven_middle, Direction.DOWN);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("The Coke Oven is a machine that produces coal coke from coal")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 2, 2), Direction.WEST))
                .placeNearTarget();

        scene.idle(90);



        scene.world().showIndependentSection(coke_oven_right, Direction.DOWN);
        scene.world().showIndependentSection(coke_oven_left, Direction.DOWN);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("It is very slow so it is beneficial to have long arrays of them")
                .pointAt(util.vector().blockSurface(util.grid().at(1, 2, 2), Direction.WEST))
                .placeNearTarget();


        scene.idle(30);


        scene.world().showIndependentSection(chutes, Direction.DOWN);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Coal can be inserted from anywhere");
        scene.idle(10);
        ElementLink<EntityElement> item = null;
        for (int i = 0; i < 3; i++) {
            scene.idle(10);

            for (int y = 0; y < 3; y++) {

                item = scene.world().createItemEntity(util.vector().centerOf(2 + y, 6, 3), util.vector().of(0, 0, 0), coal);
            }
        }
        if (item != null)
            scene.world().modifyEntity(item, Entity::discard);
        scene.idle(40);

        scene.world().showIndependentSection(creosoteOutput, Direction.DOWN);
        scene.idle(40);
        scene.world().showIndependentSection(exhaust, Direction.DOWN);

        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Carbon Dioxide is extracted at the top, Creosote can be extracted anywhere else")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 4, 2), Direction.WEST))
                .placeNearTarget();

        scene.idle(100);

        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("After some time, coal coke will fall out of the machine");

        for (int y = 0; y < 3; y++) {
            scene.world().createItemEntity(util.vector().centerOf(2 + y, 2, 1), util.vector().of(0, 0, 0), coal_coke);
        }
    }
}
