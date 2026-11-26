package com.drmangotea.tfmg.ponder.scenes;

import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ChemistryScenes {
    public static void chemicalVat(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("chemical_vat", "Chemical Vat");
        scene.configureBasePlate(0, 0, 7);
        scene.scaleSceneView(0.7f);
        scene.showBasePlate();

        Selection chemicalVat = util.select().fromTo(2, 2, 2, 4, 3, 4);
        Selection mixer = util.select().fromTo(3, 4, 3, 3, 4, 3);

        Selection graphiteElectrodes = util.select().fromTo(3, 4, 2, 3, 4, 2)
                .add(util.select().fromTo(4, 4, 4, 4, 4, 4))
                .add(util.select().fromTo(2, 4, 4, 2, 4, 4));

        Selection copperElectrodes = util.select().fromTo(4, 4, 3, 4, 4, 3)
                .add(util.select().fromTo(2, 4, 3, 2, 4, 3));

        Selection truss = util.select().fromTo(2, 1, 2, 2, 1, 2)
                .add(util.select().fromTo(4, 1, 4, 4, 1, 4))
                .add(util.select().fromTo(4, 1, 2, 4, 1, 2))
                .add(util.select().fromTo(2, 1, 4, 2, 1, 4));

        Selection blazeBurners = util.select().fromTo(2, 1, 3, 2, 1, 3)
                .add(util.select().fromTo(3, 1, 2, 3, 1, 2))
                .add(util.select().fromTo(4, 1, 3, 4, 1, 3))
                .add(util.select().fromTo(3, 1, 4, 3, 1, 4));

        ElementLink<WorldSectionElement> vatElement = scene.world().showIndependentSection(chemicalVat, Direction.DOWN);
        scene.world().showIndependentSection(truss, Direction.DOWN);
        scene.world().rotateSection(vatElement, 0, 180, 0, 0);

        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("The chemical vat is a machine with attachments that can give it many different uses");

        scene.idle(90);

        ElementLink<WorldSectionElement> mixerElement = scene.world().showIndependentSection(mixer, Direction.UP);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("First attachment is the industrial mixer");
        scene.idle(80);


        Vec3 mixerPos = util.vector().topOf(util.grid().at(3, 4, 3));
        scene.overlay().showControls(mixerPos, Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(TFMGItems.MIXER_BLADE.get()));
        scene.idle(30);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("When the mixer blade is inserted, the vat becomes a mixer");
        scene.idle(60);

        scene.overlay().showControls(mixerPos, Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(TFMGItems.CENTRIFUGE.get()));
        scene.idle(30);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("The industrial mixer can also become a centrifuge");
        scene.idle(80);

        scene.world().hideIndependentSection(mixerElement, Direction.UP);

        scene.idle(30);

        ElementLink<WorldSectionElement> burnerElement = scene.world().showIndependentSection(blazeBurners, Direction.DOWN);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Some recipes need heating");

        scene.idle(70);

        ElementLink<WorldSectionElement> electrolyzerElement = scene.world().showIndependentSection(copperElectrodes, Direction.DOWN);


        Vec3 electrodePos1 = util.vector().topOf(util.grid().at(4, 4, 3));
        Vec3 electrodePos2 = util.vector().topOf(util.grid().at(2, 4, 3));
        scene.overlay().showControls(electrodePos1, Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(TFMGItems.COPPER_ELECTRODE.get()));
        scene.overlay().showControls(electrodePos2, Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(TFMGItems.COPPER_ELECTRODE.get()));
        scene.overlay().showText(40)
                .attachKeyFrame()
                .text("Placing 2 electrode holders with copper electrodes creates an electrolyzer");

        scene.idle(60);

        scene.world().hideIndependentSection(electrolyzerElement, Direction.UP);

        scene.idle(20);
        ElementLink<WorldSectionElement> arcFurnaceElement = scene.world().showIndependentSection(graphiteElectrodes, Direction.DOWN);
        Vec3 furnacePos1 = util.vector().topOf(util.grid().at(4, 4, 2));
        Vec3 furnacePos2 = util.vector().topOf(util.grid().at(4, 4, 4));
        Vec3 furnacePos3 = util.vector().topOf(util.grid().at(2, 4, 4));
        scene.overlay().showControls(furnacePos1, Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(TFMGItems.GRAPHITE_ELECTRODE.get()));
        scene.overlay().showControls(furnacePos2, Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(TFMGItems.GRAPHITE_ELECTRODE.get()));
        scene.overlay().showControls(furnacePos3, Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(TFMGItems.GRAPHITE_ELECTRODE.get()));
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("3 graphite electrodes create a blast furnace");

        scene.idle(60);
    }
}
