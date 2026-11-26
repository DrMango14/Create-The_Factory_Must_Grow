package com.drmangotea.tfmg.ponder.scenes;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

@SuppressWarnings("unused")
public class MetallurgyScenes {
    public static void blastFurnace(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("blast_furnace", "Blast Furnace");
        scene.configureBasePlate(0, 0, 6);
        scene.scaleSceneView(.7f);
        scene.showBasePlate();
        //
        Selection output = util.select().fromTo(3, 1, 2, 3, 1, 2);

        Selection furnace1 = util.select().fromTo(4, 1, 3, 2, 1, 3);
        Selection furnace2 = util.select().fromTo(3, 1, 4, 3, 1, 4);

        Selection reinforcement1 = util.select().fromTo(2, 1, 2, 2, 5, 2);
        Selection reinforcement2 = util.select().fromTo(4, 1, 2, 4, 5, 2);
        Selection reinforcement3 = util.select().fromTo(4, 1, 4, 4, 5, 4);
        Selection reinforcement4 = util.select().fromTo(2, 1, 4, 2, 5, 4);

        Selection furnace3 = util.select().fromTo(4, 2, 3, 2, 2, 3);
        Selection furnace4 = util.select().fromTo(3, 2, 2, 3, 2, 4);

        Selection furnace5 = util.select().fromTo(4, 3, 3, 2, 3, 3);
        Selection furnace6 = util.select().fromTo(3, 3, 2, 3, 3, 4);

        Selection furnace7 = util.select().fromTo(4, 4, 3, 2, 4, 3);
        Selection furnace8 = util.select().fromTo(3, 4, 2, 3, 4, 4);

        Selection furnace9 = util.select().fromTo(4, 5, 3, 2, 5, 3);
        Selection furnace10 = util.select().fromTo(3, 5, 2, 3, 5, 4);

        Selection hatch = util.select().fromTo(1, 6, 3, 3, 6, 3);
        scene.world().setKineticSpeed(hatch, 80);

        Selection steelReinforcementCorner = util.select().fromTo(0, 1, 5, 0, 5, 5);
        Selection steelReinforcement1 = util.select().fromTo(3, 2, 1, 3, 5, 1);
        Selection steelReinforcement2 = util.select().fromTo(3, 1, 5, 3, 5, 5);
        Selection steelReinforcement3 = util.select().fromTo(5, 1, 3, 5, 5, 3);
        Selection steelReinforcement4 = util.select().fromTo(1, 1, 3, 1, 1, 3);
        Selection steelReinforcement = util.select().fromTo(1, 3, 3, 1, 5, 3)
                .add(steelReinforcement1)
                .add(steelReinforcement2)
                .add(steelReinforcement3)
                .add(steelReinforcement4);

        Selection pipes1 = util.select().fromTo(0, 1, 1, 4, 0, 1);
        Selection pipes2 = util.select().fromTo(5, 1, 0, 5, 3, 1);
        Selection pipes3 = util.select().fromTo(0, 1, 3, 0, 2, 3);
        Selection pipes = util.select().fromTo(1, 2, 3, 1, 2, 3).add(pipes1).add(pipes2).add(pipes3);

        scene.world().setKineticSpeed(pipes, 80);

        scene.world().showIndependentSection(output, Direction.DOWN);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Main part of a blast furnace is a blast furnace output")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 1, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(70);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("To build a blast furnace, make a cylinder of fireproof bricks around it with one blast furnace hatch in it");

        scene.world().showIndependentSection(furnace1, Direction.DOWN);
        scene.world().showIndependentSection(furnace2, Direction.DOWN);
        scene.idle(10);
        scene.world().showIndependentSection(furnace3, Direction.DOWN);
        scene.world().showIndependentSection(furnace4, Direction.DOWN);
        scene.idle(10);
        scene.world().showIndependentSection(furnace5, Direction.DOWN);
        scene.world().showIndependentSection(furnace6, Direction.DOWN);
        scene.idle(10);
        scene.world().showIndependentSection(furnace7, Direction.DOWN);
        scene.world().showIndependentSection(furnace8, Direction.DOWN);
        scene.idle(10);
        scene.world().showIndependentSection(furnace9, Direction.DOWN);
        scene.world().showIndependentSection(furnace10, Direction.DOWN);

        scene.idle(70);

        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("This hatch will function as a hot air input")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 2, 3), Direction.WEST))
                .placeNearTarget();
        scene.idle(100);

        ElementLink<WorldSectionElement> reinforcementElement1 = scene.world().showIndependentSection(reinforcement1, Direction.DOWN);
        ElementLink<WorldSectionElement> reinforcementElement2 = scene.world().showIndependentSection(reinforcement2, Direction.DOWN);
        ElementLink<WorldSectionElement> reinforcementElement3 = scene.world().showIndependentSection(reinforcement3, Direction.DOWN);
        ElementLink<WorldSectionElement> reinforcementElement4 = scene.world().showIndependentSection(reinforcement4, Direction.DOWN);

        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Blast furnaces need reinforcements to function")
                .placeNearTarget();
        scene.idle(100);

        scene.world().showIndependentSection(pipes, Direction.DOWN);

        scene.idle(50);
        scene.overlay().showText(100)
                .attachKeyFrame()
                .text("Everything is inserted to through a hole at the top");

        ItemStack coalCoke = new ItemStack(TFMGItems.COAL_COKE_DUST.get(),20);
        ItemStack limesand = new ItemStack(TFMGItems.LIMESAND.get(),20);
        ItemStack iron = new ItemStack(AllItems.CRUSHED_IRON.get(),20);

        scene.world().createItemEntity(util.vector().centerOf(3, 6, 3), util.vector().of(0, 0, 0), coalCoke);
        scene.idle(10);
        scene.world().createItemEntity(util.vector().centerOf(3, 6, 3), util.vector().of(0, 0, 0), limesand);
        scene.idle(10);
        scene.world().createItemEntity(util.vector().centerOf(3, 6, 3), util.vector().of(0, 0, 0), iron);
        scene.idle(100);

        scene.world().showIndependentSection(hatch, Direction.DOWN);

        scene.overlay().showText(80)
                .attachKeyFrame()
                .text("Next hatch can be placed on the top hole to collect furnace gas, items directed into it will fall into the blast furnace")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 6, 3), Direction.WEST))
                .placeNearTarget();
        scene.idle(100);

        scene.world().showIndependentSection(steelReinforcement, Direction.DOWN);

        scene.world().hideIndependentSection(reinforcementElement1,Direction.UP);
        scene.world().hideIndependentSection(reinforcementElement2,Direction.UP);
        scene.world().hideIndependentSection(reinforcementElement3,Direction.UP);
        scene.world().hideIndependentSection(reinforcementElement4,Direction.UP);

        scene.idle(20);

        ElementLink<WorldSectionElement> cornerReinforcement1 = scene.world().showIndependentSection(steelReinforcementCorner,Direction.DOWN);
        ElementLink<WorldSectionElement> cornerReinforcement2 = scene.world().showIndependentSection(steelReinforcementCorner,Direction.DOWN);
        ElementLink<WorldSectionElement> cornerReinforcement3 = scene.world().showIndependentSection(steelReinforcementCorner,Direction.DOWN);
        ElementLink<WorldSectionElement> cornerReinforcement4 = scene.world().showIndependentSection(steelReinforcementCorner,Direction.DOWN);


        scene.world().moveSection(cornerReinforcement1,new Vec3(2d,0d,-1d),0);
        scene.world().moveSection(cornerReinforcement2,new Vec3(4d,0d,-1d),0);
        scene.world().moveSection(cornerReinforcement3,new Vec3(2d,0d,-3d),0);
        scene.world().moveSection(cornerReinforcement4,new Vec3(4d,0d,-3d),0);


        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Blast furnace reinforcements can be placed around the furnace to increase its speed")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 6, 3), Direction.WEST))
                .placeNearTarget();
        scene.idle(80);
    }

    public static void cokeOven(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("coke_oven", "Coke Oven");
        scene.configureBasePlate(0, 0, 6);
        scene.scaleSceneView(.7f);

        scene.showBasePlate();

        Selection cokeOvenMiddle = util.select().fromTo(3, 1, 2, 3, 3, 4);

        Selection cokeOvenRight = util.select().fromTo(4, 1, 2, 5, 3, 4);
        Selection cokeOvenLeft = util.select().fromTo(2, 1, 2, 1, 3, 4);

        Selection chutes = util.select().fromTo(2, 4, 3, 4, 4, 3);
        Selection exhaust = util.select().fromTo(2, 4, 2, 4, 4, 2)
                .add(util.select().fromTo(0, 1, 2, 1, 5, 2));

        Selection creosoteOutput = util.select().fromTo(0, 1, 5, 5, 5, 5);

        ItemStack coal = new ItemStack(Items.COAL, 3);
        ItemStack coalCoke = new ItemStack(TFMGItems.COAL_COKE.get(), 10);

        scene.world().setKineticSpeed(creosoteOutput, 80);
        scene.world().setKineticSpeed(exhaust, 80);

        scene.world().showIndependentSection(cokeOvenMiddle, Direction.DOWN);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("The Coke Oven is a machine that produces coal coke from coal")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 2, 2), Direction.WEST))
                .placeNearTarget();

        scene.idle(90);

        scene.world().showIndependentSection(cokeOvenRight, Direction.DOWN);
        scene.world().showIndependentSection(cokeOvenLeft, Direction.DOWN);
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
            scene.world().createItemEntity(util.vector().centerOf(2 + y, 2, 1), util.vector().of(0, 0, 0), coalCoke);
        }
    }

    public static void blastStove(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("blast_stove", "Blast Stove");
        scene.configureBasePlate(0, 0, 7);
        scene.scaleSceneView(.7f);
        scene.showBasePlate();

        Selection blastStove = util.select().fromTo(4, 2, 4, 4, 4, 4);
        Selection airIntake = util.select().fromTo(4, 2, 1, 6, 2, 3);
        Selection creosotePipe = util.select().fromTo(4, 1, 4, 2, 1, 4);
        Selection creosoteTank = util.select().fromTo(1, 1, 4, 1, 2, 4);
        Selection creosote = creosotePipe.add(creosoteTank);
        Selection steelCasing = util.select().fromTo(4, 1, 1, 6, 1, 6).substract(creosotePipe);

        Selection outputPipe = util.select().fromTo(4, 5, 4, 1, 4, 4).substract(blastStove);
        Selection outputTank = util.select().fromTo(1, 5, 4, 1, 6, 4);
        Selection output = outputPipe.add(outputTank);
        Selection smokestack = util.select().fromTo(6, 2, 4, 6, 4, 4);
        Selection smokestackPipe = util.select().position(5, 2, 4);

        Selection lonelyPipe = util.select().position(4, 1, 4);

        var blastStoveElement = scene.world().showIndependentSection(blastStove, Direction.UP);
        var steelCasingElement = scene.world().showIndependentSection(steelCasing, Direction.UP);
        var lonelyPipeElement = scene.world().showIndependentSection(lonelyPipe, Direction.UP);
        scene.world().setBlock(new BlockPos(4, 1, 4), TFMGBlocks.STEEL_CASING.getDefaultState(), false);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .pointAt(blastStove.getCenter())
                .placeNearTarget()
                .text("The Blast Stove is a simple but crucial machine for producing steel.");
        scene.idle(60);
        scene.overlay().showText(50)
                .placeNearTarget()
                .text("It takes in two inputs to produce Hot Air.");
        scene.idle(60);
        scene.addKeyframe();
        scene.world().hideIndependentSection(lonelyPipeElement, Direction.DOWN);
        scene.idle(20);
        scene.world().restoreBlocks(lonelyPipe);
        var creosoteElement = scene.world().showIndependentSection(creosote, Direction.DOWN);
        scene.overlay().showOutlineWithText(lonelyPipe, 50)
                .colored(PonderPalette.BLUE)
                .attachKeyFrame()
                .text("Fuel must be input through the bottom of the Blast Stove...")
                .placeNearTarget();
        FluidStack content = new FluidStack(TFMGFluids.CREOSOTE.get().getSource(), 8000);
        scene.world().modifyBlockEntity(new BlockPos(1, 1, 4), SteelTankBlockEntity.class, be -> be.getTankInventory().fill(content, IFluidHandler.FluidAction.EXECUTE));
        scene.idle(60);
        var airIntakeElement = scene.world().showIndependentSection(airIntake, Direction.DOWN);
        scene.overlay().showOutlineWithText(util.select().position(4, 2, 3), 50)
                .colored(PonderPalette.BLUE)
                .attachKeyFrame()
                .text("And air is input from the side.")
                .placeNearTarget();
        scene.idle(20);
        scene.world().setKineticSpeed(util.select().position(5, 2, 3), 32);
        scene.world().setKineticSpeed(util.select().position(2, 1, 4), 32);
        scene.world().setKineticSpeed(util.select().position(6, 2, 1), 32);
        int creosoteInTank = 8000;
        while (creosoteInTank > 0) {
            scene.world().modifyBlockEntity(new BlockPos(1, 1, 4), SteelTankBlockEntity.class, be -> be.getTankInventory().drain(1000, IFluidHandler.FluidAction.EXECUTE));
            creosoteInTank -= 1000;
            scene.idle(5);
        }
        scene.idle(10);
        scene.addKeyframe();
        var outputElement = scene.world().showIndependentSection(output, Direction.DOWN);
        scene.overlay().showOutlineWithText(util.select().position(4, 5, 4), 50)
                .colored(PonderPalette.BLUE)
                .text("Hot Air is then pumped out of the top of the Blast Stove.")
                .placeNearTarget();
        scene.idle(20);
        scene.world().setKineticSpeed(util.select().position(3, 5, 4), 32);
        scene.idle(20);
        int airInOutput = 0;
        FluidStack hotAir = new FluidStack(TFMGFluids.HOT_AIR.get().getSource(), 1000);
        while (airInOutput < 8000) {
            scene.world().modifyBlockEntity(new BlockPos(1, 4, 4), SteelTankBlockEntity.class, be -> be.getTankInventory().fill(hotAir, IFluidHandler.FluidAction.EXECUTE));
            airInOutput += 1000;
            scene.idle(5);
        }
        scene.overlay().showText(50)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Over time the Blast Stove will produce Carbon Dioxide that can stall the process...");
        scene.idle(70);
        var smokestackElement = scene.world().showIndependentSection(smokestack.add(smokestackPipe), Direction.DOWN);
        scene.overlay().showOutlineWithText(smokestack, 50)
                .colored(PonderPalette.BLUE)
                .text("To avoid this, you should construct a Smokestack or Exhaust nearby")
                .placeNearTarget();
        scene.idle(20);
        scene.world().setKineticSpeed(smokestackPipe, 32);
        scene.idle(20);
        scene.effects().emitParticles(
			new Vec3(6, 4, 4),
			(world, x, y, z) -> TFMGUtils.spawnPonderExhaust(world, x, y, z, 1),
			1, 120
		);
        scene.idle(40);
    }
}
