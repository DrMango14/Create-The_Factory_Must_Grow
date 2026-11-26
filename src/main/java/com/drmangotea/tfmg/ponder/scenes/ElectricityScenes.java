package com.drmangotea.tfmg.ponder.scenes;

import com.drmangotea.tfmg.content.machinery.misc.winding_machine.WindingMachineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
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
import net.minecraft.world.phys.Vec3;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.F.lubricationOil;

public class ElectricityScenes {
    public static void electricity(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("electricity", "Electricity");
        scene.showBasePlate();

        Selection generator = util.select().fromTo(5, 1, 4, 4, 1, 4);

        Selection light1 = util.select().fromTo(3, 1, 4, 2, 2, 4);
        Selection light2 = util.select().fromTo(2, 1, 3, 2, 2, 2);
        Selection light3 = util.select().fromTo(2, 1, 0, 2, 2, 1);

        scene.world().showIndependentSection(generator, Direction.DOWN);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("A generator creates 2 values, Voltage and Power");
        scene.idle(60);

        //-------------------------Phase 1-------------------------//
        scene.world().showIndependentSection(light1, Direction.DOWN);


        scene.overlay().showText(240)
                .attachKeyFrame()
                .text(
                        """
                                Generator:
                                   Voltage(U) = 200V
                                   Max Power = 8kW
                                Light Bulb:
                                   Voltage(U) = 200V
                                   Current(I) = 2A
                                   Power(P) = 100W
                                   Resistance(R) = 100Ω"""
                )
                .independent(50)
                .colored(PonderPalette.BLUE);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("When load is applied on a generator, it takes its voltage");
        scene.idle(80);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Every an electric device has electrical resistance, light bulbs are 100 Ohm(Ω)");
        scene.idle(80);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Current with size of Voltage divided by Resistance will start flowing in the light bulb");

        scene.idle(80);

        //-------------------------Phase 2-------------------------//
        scene.world().showIndependentSection(light2, Direction.DOWN);
        scene.overlay().showText(180)
                .attachKeyFrame()
                .text(
                        """
                                Generator:
                                   Voltage(U) = 200V
                                   Max Power = 8kW
                                Light Bulb 1:
                                   Voltage(U) = 100V
                                   Current(I) = 1A
                                   Resistance(R) = 100Ω
                                   Group=0
                                Light Bulb 2:
                                   Voltage(U) = 100V
                                   Current(I) = 1A
                                   Resistance(R) = 100Ω
                                   Group=0"""
                )
                .independent(50)
                .colored(PonderPalette.BLUE);
        scene.overlay().showText(80)
                .attachKeyFrame()
                .text("Electric components can be connected with groups, by default all blocks are group 0");
        scene.idle(90);
        scene.overlay().showText(80)
                .attachKeyFrame()
                .text("Blocks that share a group split voltage between them(blocks with higher resistance get more of the split voltage)");
        scene.idle(90);

        //-------------------------Phase 3-------------------------//
        Vec3 pos = util.vector().topOf(util.grid().at(2, 2, 4));
        scene.overlay().showControls(pos, Pointing.DOWN, 20)
                .rightClick()
                .withItem(new ItemStack(TFMGItems.CONFIGURATION_WRENCH.get()));
        scene.overlay().showText(180)
                .attachKeyFrame()
                .text(
                        """
                                Generator:
                                   Voltage(U) = 200V
                                   Max Power = 8kW
                                Light Bulb 1:
                                   Voltage(U) = 200V
                                   Current(I) = 2A
                                   Resistance(R) = 100Ω
                                   Group=1
                                Light Bulb 2:
                                   Voltage(U) = 200V
                                   Current(I) = 2A
                                   Resistance(R) = 100Ω
                                   Group=0"""
                )
                .independent(50)
                .colored(PonderPalette.BLUE);
        scene.overlay().showText(80)
                .attachKeyFrame()
                .text("Groups can be changed using the Configuration Wrench");
        scene.idle(90);
        scene.overlay().showText(80)
                .attachKeyFrame()
                .text("Blocks in their own group keep all the voltage");
        scene.idle(90);
    }

    public static void electricSubnetworks(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("electric_subnetworks", "Electric Subnetworks");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        Selection generator = util.select().fromTo(4, 1, 3, 2, 1, 3);

        Selection light = util.select().fromTo(0, 1, 3, 0, 2, 2);

        Selection potentiometer = util.select().fromTo(1, 1, 1, 1, 1, 1);
        Selection electricSwitch = util.select().fromTo(1, 1, 2, 1, 1, 2);
        Selection diode = util.select().fromTo(1, 1, 3, 1, 1, 3);
        Selection transformer = util.select().fromTo(1, 1, 4, 1, 1, 4);


        ElementLink<WorldSectionElement> generatorElement = scene.world().showIndependentSection(generator, Direction.DOWN);
        ElementLink<WorldSectionElement> lightElement = scene.world().showIndependentSection(light, Direction.DOWN);
        ElementLink<WorldSectionElement> diodeElement = scene.world().showIndependentSection(diode, Direction.DOWN);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Some blocks have connections from 2 sides");
        scene.idle(60);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("These blocks create a new electric network on one of their sides");
        scene.idle(60);
        scene.overlay().showText(90)
                .attachKeyFrame()
                .text("This subnetwork will get all the power from the main network but not the opposite way");
        scene.idle(100);

        //DIODE
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("The most basic block with this ability is the Diode, it has no other extra abilities");
        scene.idle(80);
        //POTENTIOMETER
        scene.world().hideIndependentSection(diodeElement, Direction.NORTH);
        ElementLink<WorldSectionElement> potentiometerElement = scene.world().showIndependentSection(potentiometer, Direction.NORTH);
        scene.world().moveSection(potentiometerElement, new Vec3(0d, 0d, 2d), 0);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Next one is the potentiometer, this one can set the percentage of voltage that gets to the subnetwork");
        scene.idle(80);
        //SWITCH
        scene.world().hideIndependentSection(potentiometerElement, Direction.NORTH);
        ElementLink<WorldSectionElement> switchElement = scene.world().showIndependentSection(electricSwitch, Direction.NORTH);
        scene.world().moveSection(switchElement, new Vec3(0d, 0d, 1d), 0);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("The switch works similarly but with redstone");
        scene.idle(80);
        //TRANSFORMER
        scene.world().hideIndependentSection(switchElement, Direction.NORTH);
        ElementLink<WorldSectionElement> transformerElement = scene.world().showIndependentSection(transformer, Direction.NORTH);
        scene.world().moveSection(transformerElement, new Vec3(0d, 0d, -1d), 0);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("The transformer changes voltage based on the ratio of turns between the primary and secondary coil");
        scene.idle(80);
    }

    public static void largeTransformer(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("large_transformer", "Large Transformer");
        scene.scaleSceneView(.7f);
        scene.configureBasePlate(0, 0, 6);
        scene.showBasePlate();

        Selection largeCoils = util.select().fromTo(2, 3, 2, 2, 3, 3);
        Selection transformerDecoration = util.select().fromTo(2, 1, 1, 3, 6, 4).substract(largeCoils);

        ElementLink<WorldSectionElement> largeCoilsElement = scene.world().showIndependentSection(largeCoils, Direction.UP);
        ElementLink<WorldSectionElement> transformerDecorationElement = scene.world().showIndependentSection(transformerDecoration, Direction.UP);

        scene.idle(30);

        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Large Transformers have to be manually assembled.");
        scene.idle(80);
        scene.world().hideIndependentSection(transformerDecorationElement, Direction.UP);
        scene.overlay().showOutlineWithText(largeCoils, 70)
                .colored(PonderPalette.BLUE)
                .attachKeyFrame()
                .text("Two Large Coils need to be placed beside each-other.")
                .placeNearTarget();
        scene.idle(80);
        scene.idle(10);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("To get started, interact with one of the coils with a Laminated Magnetic Alloy Block.");
        scene.idle(20);
        scene.overlay().showControls(largeCoils.getCenter(), Pointing.RIGHT, 40)
                .rightClick()
                .withItem(new ItemStack(TFMGBlocks.LAMINATED_MAGNETIC_ALLOY_BLOCK.get()));
        scene.idle(10);
        //scene.world().setBlock(new BlockPos(2, 3, 2), TFMGBlocks.LARGE_TRANSFORMER.getDefaultState().setValue(LargeTransformerBlock.HORIZONTAL_FACING, Direction.SOUTH), true);
        //scene.world().setBlock(new BlockPos(2, 3, 3), TFMGBlocks.LARGE_TRANSFORMER.getDefaultState().setValue(LargeTransformerBlock.HORIZONTAL_FACING, Direction.NORTH), true);
        scene.idle(50);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Then, use a Steel Block to cover the internals.");
        scene.idle(20);
        scene.overlay().showControls(largeCoils.getCenter(), Pointing.RIGHT, 40)
                .rightClick()
                .withItem(new ItemStack(TFMGBlocks.STEEL_BLOCK.get()));
        scene.idle(10);
        //scene.world().modifyBlockEntity(new BlockPos(2, 3, 2), LargeTransformerBlockEntity.class, (large) -> large.constructionState = LargeTransformerBlockEntity.TransformerConstructionState.NEEDS_OIL);
        //scene.world().modifyBlockEntity(new BlockPos(2, 3, 3), LargeTransformerBlockEntity.class, (large) -> large.constructionState = LargeTransformerBlockEntity.TransformerConstructionState.NEEDS_OIL);
        //scene.world().setBlock(new BlockPos(2, 3, 2), TFMGBlocks.LARGE_TRANSFORMER.getDefaultState().setValue(LargeTransformerBlock.HORIZONTAL_FACING, Direction.SOUTH).setValue(LargeTransformerBlock.UNFINISHED_MODEL, false), true);
        //scene.world().setBlock(new BlockPos(2, 3, 3), TFMGBlocks.LARGE_TRANSFORMER.getDefaultState().setValue(LargeTransformerBlock.HORIZONTAL_FACING, Direction.NORTH).setValue(LargeTransformerBlock.UNFINISHED_MODEL, false), true);
        scene.idle(30);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("Finally, apply Lubrication Oil to the Transformer to complete its assembly.");
        scene.idle(20);
        scene.overlay().showControls(largeCoils.getCenter(), Pointing.RIGHT, 40)
                .rightClick()
                .withItem(new ItemStack(lubricationOil().getBucket()));
        scene.idle(10);
        //scene.world().setBlock(new BlockPos(2, 3, 2), TFMGBlocks.LARGE_TRANSFORMER.getDefaultState().setValue(LargeTransformerBlock.HORIZONTAL_FACING, Direction.SOUTH).setValue(LargeTransformerBlock.UNFINISHED_MODEL, false), true);
        //scene.world().setBlock(new BlockPos(2, 3, 3), TFMGBlocks.LARGE_TRANSFORMER.getDefaultState().setValue(LargeTransformerBlock.HORIZONTAL_FACING, Direction.NORTH).setValue(LargeTransformerBlock.UNFINISHED_MODEL, false), true);
        scene.idle(50);
        scene.world().showIndependentSection(transformerDecoration, Direction.DOWN);
        scene.idle(20);
        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("You now have a fully functional Large Transformer!");
    }

    public static void largeGenerator(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("large_generator", "");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();

        Selection stator = util.select().fromTo(3, 1, 5, 5, 3, 5);
        Selection rotor = util.select().fromTo(4, 2, 3, 4, 2, 3);
        Selection kinetics1 = util.select().fromTo(4, 1, 1, 6, 2, 2);
        Selection kinetics2 = util.select().fromTo(6, 1, 3, 6, 1, 3);
        Selection cables = util.select().fromTo(1, 1, 3, 2, 2, 6);

        scene.world().setKineticSpeed(kinetics1, 120);
        scene.world().setKineticSpeed(kinetics2, 120);
        scene.world().showIndependentSection(rotor, Direction.DOWN);

        scene.overlay().showText(50)
                .attachKeyFrame()
                .text("The main part of the Large Generator is the Rotor")
                .pointAt(util.vector().blockSurface(util.grid().at(4, 2, 5), Direction.WEST))
                .placeNearTarget();

        scene.idle(80);

        ElementLink<WorldSectionElement> statorElement = scene.world().showIndependentSection(stator, Direction.DOWN);


        scene.world().moveSection(statorElement, new Vec3(0d, 0d, -2d), 0);

        scene.overlay().showText(75)
                .attachKeyFrame()
                .text("To complete the Large Generator, place a Stator block around the Rotor")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 2, 3), Direction.WEST))
                .placeNearTarget();


        scene.idle(105);

        scene.world().showIndependentSection(kinetics1, Direction.DOWN);
        scene.world().showIndependentSection(kinetics2, Direction.DOWN);

        scene.world().setKineticSpeed(rotor, 120);
        scene.overlay().showText(65)
                .attachKeyFrame()
                .text("Providing rotational power to the Rotor will produce electric energy")
                .pointAt(util.vector().blockSurface(util.grid().at(4, 2, 3), Direction.WEST))
                .placeNearTarget();


        scene.idle(95);

        BlockPos pos = util.grid().at(3, 2, 3);
        Vec3 topOf = util.vector().topOf(pos);
        scene.overlay().showControls(topOf, Pointing.DOWN, 20).rightClick()
                .withItem(new ItemStack(AllItems.WRENCH.get()));

        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("Clicking a side with a wrench will make it the energy output");

        scene.idle(20);
        scene.world().showIndependentSection(cables, Direction.DOWN);
        scene.idle(50);

    }

    public static void windingMachine(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("winding_machine", "Winding Machine");
        scene.showBasePlate();
        scene.configureBasePlate(0, 0, 5);

        Selection windingMachine = util.select().position(2, 2, 2);

        Selection largeCog = util.select().position(1, 0, 5);
        Selection smallerKinetics = util.select().fromTo(2, 1, 3, 2, 1, 5);

        var windingMachineElement = scene.world().showIndependentSection(windingMachine, Direction.UP);
        scene.world().moveSection(windingMachineElement, new Vec3(0d, -1d, 0d), 0);
        scene.overlay().showText(40)
                .attachKeyFrame()
                .text("Winding Machines are used to turn a spool around an item");
        scene.idle(50);
        scene.overlay().showText(40)
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(windingMachine.getCenter().subtract(0.5, 0.5, 0))
                .text("Spools can be inserted by right clicking.");
        scene.idle(20);
        scene.overlay().showControls(windingMachine.getCenter().subtract(0.5, 0.5, 0), Pointing.DOWN, 5).rightClick()
                .withItem(new ItemStack(TFMGItems.ALUMINUM_SPOOL.get()));
        scene.world().modifyBlockEntity(new BlockPos(2, 2, 2), WindingMachineBlockEntity.class, (wm) -> wm.spoolInventory.setStackInSlot(0, new ItemStack(TFMGItems.ALUMINUM_SPOOL.get())));
        scene.idle(10);
        scene.overlay().showControls(windingMachine.getCenter().subtract(0.5, 0.5, 0), Pointing.DOWN, 5).rightClick()
                .withItem(new ItemStack(TFMGItems.CONSTANTAN_SPOOL.get()));
        scene.world().modifyBlockEntity(new BlockPos(2, 2, 2), WindingMachineBlockEntity.class, (wm) -> wm.spoolInventory.setStackInSlot(0, new ItemStack(TFMGItems.CONSTANTAN_SPOOL.get())));
        scene.idle(10);
        scene.overlay().showControls(windingMachine.getCenter().subtract(0.5, 0.5, 0), Pointing.DOWN, 5).rightClick()
                .withItem(new ItemStack(TFMGItems.COPPER_SPOOL.get()));
        scene.world().modifyBlockEntity(new BlockPos(2, 2, 2), WindingMachineBlockEntity.class, (wm) -> wm.spoolInventory.setStackInSlot(0, new ItemStack(TFMGItems.COPPER_SPOOL.get())));
        scene.idle(5);
        scene.addKeyframe();
        scene.idle(15);
        scene.rotateCameraY(180);
        scene.idle(20);
        scene.overlay().showText(40)
                .placeNearTarget()
                .pointAt(windingMachine.getCenter().subtract(-0.5, 0.6, 0))
                .text("Right clicking with an input places it in the correct position");
        scene.idle(20);
        scene.overlay().showControls(windingMachine.getCenter().subtract(-0.5, 0.6, 0), Pointing.DOWN, 5).rightClick()
                .withItem(new ItemStack(TFMGBlocks.LAMINATED_MAGNETIC_ALLOY_BLOCK.get()));
        scene.world().modifyBlockEntity(new BlockPos(2, 2, 2), WindingMachineBlockEntity.class, (wm) -> wm.inventory.setStackInSlot(0, new ItemStack(TFMGBlocks.LAMINATED_MAGNETIC_ALLOY_BLOCK.get())));
        scene.idle(25);
        scene.rotateCameraY(-180);
        scene.idle(20);
        var kineticPowerElement = scene.world().showIndependentSection(largeCog.add(smallerKinetics), Direction.DOWN);
        scene.world().setBlock(new BlockPos(2, 1, 3), AllBlocks.SHAFT.getDefaultState().setValue(ShaftBlock.AXIS, Direction.Axis.Z), false);
        scene.overlay().showText(40)
                .attachKeyFrame()
                .placeNearTarget()
                .text("When rotation is provided it will begin winding...");
        scene.world().setKineticSpeed(largeCog, -16);
        scene.world().setKineticSpeed(smallerKinetics, 32);
        //TODO: Spool doesn't rotate
        scene.world().setKineticSpeed(windingMachine, 32);
        scene.idle(120);
        scene.overlay().showText(30)
                .placeNearTarget()
                .text("...and will stop on completion!");
    }

    public static void windingMachineAutomation(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("winding_machine_automation", "Winding Machine Automation");
        scene.showBasePlate();
        scene.configureBasePlate(0, 0, 5);

        Selection casing = util.select().position(2, 1, 2);
        Selection windingMachine = util.select().position(2, 2, 2);
        Selection central = casing.add(windingMachine);

        Selection largeCog = util.select().position(1, 0, 5);
        Selection smallerKinetics = util.select().fromTo(2, 1, 3, 2, 1, 5);

        Selection inputBelt = util.select().fromTo(3, 1, 2, 4, 1, 2);
        Selection inputFunnel = util.select().position(3, 2, 2);
        Selection input = inputBelt.add(inputFunnel);

        Selection deployer = util.select().position(2, 4, 2);

        Selection output = util.select().fromTo(1, 1, 2, 1, 2, 2);

        var centralElement = scene.world().showIndependentSection(central, Direction.UP);
        scene.world().moveSection(centralElement, new Vec3(0d, -1d, 0d), 0);
        scene.overlay().showText(40)
                .attachKeyFrame()
                .text("Winding Machines can be automated through various means...");
        scene.idle(20);
        scene.world().moveSection(centralElement, new Vec3(0d, 1d, 0d), 10);
        scene.idle(10);
        scene.addKeyframe();
        var inputElement = scene.world().showIndependentSection(input, Direction.DOWN);
        scene.idle(10);
        scene.overlay().showOutlineWithText(inputFunnel, 50)
                .colored(PonderPalette.GREEN)
                .attachKeyFrame()
                .text("Items can be inserted into the input slot with a funnel.")
                .placeNearTarget();
        scene.world().setKineticSpeed(inputBelt, 16);
        scene.idle(40);
        ItemStack stack = TFMGItems.ELECTROMAGNETIC_COIL.asStack(); //TODO: Review
        ElementLink<EntityElement> item = scene.world().createItemEntity(util.vector().centerOf(4, 4, 2), util.vector().of(0, 0, 0), stack);
        scene.idle(13);
        scene.world().modifyEntity(item, Entity::discard);
        BlockPos beltEnd = util.grid().at(4, 1, 2);
        scene.world().createItemOnBelt(beltEnd, Direction.DOWN, stack);
        scene.idle(35);
        scene.world().modifyBlockEntity(new BlockPos(2, 2, 2), WindingMachineBlockEntity.class, (wm) -> wm.inventory.setStackInSlot(0, stack));
        scene.idle(35);
        scene.addKeyframe();
        scene.world().hideIndependentSection(inputElement, Direction.UP);
        var deployerElement = scene.world().showIndependentSection(deployer, Direction.DOWN);
        scene.world().moveSection(centralElement, new Vec3(0d, -1d, 0d), 10);
        scene.world().moveSection(deployerElement, new Vec3(0d, -1d, 0d), 0);
        scene.idle(10);
        ItemStack spool = TFMGItems.CONSTANTAN_SPOOL.asStack();
        spool.set(TFMGDataComponents.SPOOL_AMOUNT, 50);
        scene.overlay().showText(50)
                .pointAt(deployer.getCenter())
                .text("Spools can be inserted with a Deployer")
                .placeNearTarget();
        scene.overlay().showControls(util.vector().blockSurface(new BlockPos(2, 4, 2), Direction.EAST).add(0, 0.15, 0), Pointing.RIGHT, 30)
                .withItem(spool);
        scene.idle(17);
        scene.world().modifyBlockEntityNBT(deployer, DeployerBlockEntity.class,
                nbt -> nbt.put("HeldItem", spool.saveOptional(scene.world().getHolderLookupProvider())));
        scene.idle(25);
        scene.world().moveDeployer(new BlockPos(2, 4, 2), 1, 20);
        scene.idle(20);
        scene.world().modifyBlockEntityNBT(deployer, DeployerBlockEntity.class,
                nbt -> nbt.put("HeldItem", ItemStack.EMPTY.saveOptional(scene.world().getHolderLookupProvider())));
        scene.world().modifyBlockEntity(new BlockPos(2, 2, 2), WindingMachineBlockEntity.class, (wm) -> wm.spoolInventory.setStackInSlot(0, spool));
        scene.world().moveDeployer(new BlockPos(2, 4, 2), -1, 20);
        scene.idle(20);
        scene.addKeyframe();
        scene.world().hideIndependentSection(deployerElement, Direction.UP);
        var kineticPowerElement = scene.world().showIndependentSection(largeCog.add(smallerKinetics), Direction.DOWN);
        scene.world().setBlock(new BlockPos(2, 1, 3), AllBlocks.SHAFT.getDefaultState().setValue(ShaftBlock.AXIS, Direction.Axis.Z), false);
        scene.world().setKineticSpeed(largeCog, -32);
        scene.world().setKineticSpeed(smallerKinetics, 64);
        //TODO: Spool doesn't rotate
        scene.world().setKineticSpeed(windingMachine, 64);
        scene.idle(60);
        scene.addKeyframe();
        scene.world().hideIndependentSection(kineticPowerElement, Direction.UP);
        scene.world().moveSection(centralElement, new Vec3(0d, 1d, 0d), 10);
        scene.idle(10);
        var outputElement = scene.world().showIndependentSection(output, Direction.DOWN);
        scene.idle(10);
        scene.overlay().showOutlineWithText(util.select().position(1, 2, 2), 50)
                .colored(PonderPalette.GREEN)
                .attachKeyFrame()
                .text("Funnels will only extract outputs.")
                .placeNearTarget();
        scene.idle(20);
        scene.world().flapFunnel(new BlockPos(1, 2, 2), true);
        scene.world().modifyBlockEntity(new BlockPos(2, 2, 2), WindingMachineBlockEntity.class, (wm) -> wm.outputInventory.setStackInSlot(0, ItemStack.EMPTY));
        scene.world().createItemOnBeltLike(new BlockPos(1, 1, 2), Direction.EAST, TFMGBlocks.RESISTOR.asStack());
    }
}
