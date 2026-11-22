package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.base.spark.Spark;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.drmangotea.tfmg.content.electricity.ElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.large.LargeTransformerBlockEntity;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.cable_type.CableType;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.Electrode;
import com.drmangotea.tfmg.registry.TFMGEntityTypes;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.outliner.Outliner;
import net.createmod.catnip.theme.Color;
import net.createmod.ponder.api.level.PonderLevel;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.apache.commons.lang3.StringUtils;
import org.joml.Quaterniond;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class TFMGUtils {
    public static float toYRot(Direction facing) {
        return switch (facing) {
            case DOWN, UP, NORTH -> 0.0F;
            case SOUTH -> 180F;
            case WEST -> 90;
            case EAST -> 270F;
        };
    }

    public static List<Direction> invertDirections(List<Direction> directions){
        List<Direction> inverted = new ArrayList<>();

        for (Direction direction : directions) {
            inverted.add(direction.getOpposite());
        }

        return inverted;
    }

    public static ConnectingElectricalNode closestNode(IElectric be, Vec3 clickPosition) {
		if (be == null)
			return null;
		
		if (be instanceof LargeTransformerBlockEntity t && !t.isMainPart) {
			return closestNode(t.getControlledBlock(), clickPosition);
		}
		
		BlockPos pos = be.getPos();
		ElectricalNetwork network = ElectricNetworkManager.getNetwork(be.getWorld());
		
		return closestNode(network, pos, clickPosition);
	}
	
	public static ConnectingElectricalNode closestNode(ElectricalNetwork network, BlockPos pos, Vec3 clickPosition) {
		
		AtomicReference<Float> closestDistance = new AtomicReference<>((float) 1000);
		AtomicReference<ConnectingElectricalNode> closestConnector = new AtomicReference<>(null);
		Vec3 basePos = Vec3.atLowerCornerOf(pos);
		
		network.getNodes(pos).values().forEach(n -> {
			if (!(n instanceof ConnectingElectricalNode node)) return;
			
			float distance = TFMGUtils.getDistance(clickPosition, node.getPosition().add(basePos));
			if (distance >= closestDistance.get()) return;
			
			closestDistance.set(distance);
			closestConnector.set(node);
		});
		return closestConnector.get();
	}

	
    public static void createFireExplosion(Level level, Entity entity, Vec3 pos, int sparkAmount, float radius) {
        if (level.isClientSide && entity != null) level.broadcastEntityEvent(entity, (byte) 3);

        for (int i = 0; i < sparkAmount; i++) {
            float x = level.random.nextFloat() * Mth.TWO_PI;
            float y = level.random.nextFloat() * Mth.TWO_PI;
            float z = level.random.nextFloat() * Mth.TWO_PI;
            Spark spark = TFMGEntityTypes.SPARK.create(level);
			if (spark == null) continue;
            spark.moveTo(pos.x(), pos.y() + 1, pos.z());

            float f = -Mth.sin(y) * Mth.cos(x);
            float f1 = -Mth.sin(x + z);
            float f2 = Mth.cos(y) * Mth.cos(x);
            spark.shoot(f, f1, f2, 0.3f, 1);
            level.addFreshEntity(spark);
        }
        level.explode(null, pos.x(), pos.y(), pos.z(), radius, Level.ExplosionInteraction.BLOCK);
    }

    public static void playSound(Level level, BlockPos pos, SoundEvent sound, SoundSource source) {
        playSound(level, pos, sound, source, 1, 1, null);
    }

    public static void playSound(Level level, BlockPos pos, SoundEvent sound, SoundSource source, Player player) {
        playSound(level, pos, sound, source, 1, 1, player);
    }

    public static void playSound(Level level, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {
        playSound(level, pos, sound, source, volume, pitch, null);
    }

    public static void playSound(Level level, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch, Player player) {
        level.playSound(player, pos, sound, source, volume, pitch);
    }

	//what is this even for?
    public static void blowUpTank(FluidTankBlockEntity tank, int power) {
        if (tank == null) return;
        FluidTankBlockEntity be = tank.getControllerBE();
		if (be == null) return;
		BlockPos pos = be.getBlockPos();
		Level level = be.getLevel();
		if (level == null) return;

        for (int X = 0; X < be.getWidth(); X++) { for (int Z = 0; Z < be.getWidth(); Z++) { for (int Y = 0; Y < be.getHeight(); Y++) {
			level.destroyBlock(pos.offset(X, Y, Z), false);
		} } }

        createFireExplosion(level, null, Vec3.atLowerCornerWithOffset(pos, be.getWidth()  * 0.5f, be.getHeight()  * 0.5f, be.getWidth() * 0.5f), power * 15, (float) power);
    }

    public static void createOutline(Vec3 pos1, Vec3 pos2, String name, Color color) {
        createOutline(pos1, pos2, name, color, 1 / 32f);
    }

    public static void createOutline(Vec3 pos1, Vec3 pos2, String name, Color color, float width) {
        Outliner.getInstance().showAABB(name, new AABB(pos1, pos2))
                .lineWidth(width)
                .colored(color);
    }

    public static String fromId(String key) {
        String s = key.replace("_", " ");
        s = Arrays.stream(StringUtils.splitByCharacterTypeCamelCase(s)).map(StringUtils::capitalize).collect(Collectors.joining(" "));
        s = StringUtils.normalizeSpace(s);
        return s;
    }

    public static String toHumanReadable(String key) {
        String s = key.replace("_", " ");
        s = Arrays.stream(StringUtils.splitByCharacterTypeCamelCase(s)).map(StringUtils::capitalize).collect(Collectors.joining(" "));
        s = StringUtils.normalizeSpace(s);
        return s;
    }
	
	public static void spawnSmokeParticles(Level level, BlockPos pos) {
		spawnSmokeParticles(level, Vec3.atLowerCornerOf(pos), 1);
	}
	
	public static void spawnSmokeParticles(Level level, Vec3 pos, float spread) {
		RandomSource random = level.getRandom();
		if (random.nextInt(7) != 0) return;
		spawnSmokeParticles(
			level,
			pos.x() + random.nextFloat() * spread,
			pos.y() + 1,
			pos.z() + random.nextFloat() * spread
		);
	}
	
	public static void spawnSmokeParticles(Level level, double x, double y, double z) {
		level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, x, y, z, 0.0D, 0.08D, 0.0D);
	}
	
	public static void spawnPonderExhaust(PonderLevel world, double x, double y, double z, float spread) {
		RandomSource random = world.getRandom();
		if (random.nextInt(7) == 0) {
			spawnSmokeParticles(world, x + random.nextFloat()*spread, y + 1, z + random.nextFloat() * spread);
		}
	}
	
	public static float getDistance(Vec3 pos1, Vec3 pos2) {
		double x = Math.abs(pos1.x() - pos2.x());
		double y = Math.abs(pos1.y() - pos2.y());
		double z = Math.abs(pos1.z() - pos2.z());
		
		
		return (float) Math.sqrt(x * x + z * z + y * y);
	}
	
	public static float getDistance(BlockPos pos1, BlockPos pos2, boolean is2d) {
        float x = Math.abs(pos1.getX() - pos2.getX());
        float y = Math.abs(pos1.getY() - pos2.getY());
        float z = Math.abs(pos1.getZ() - pos2.getZ());

        return (float) Math.sqrt(x * x + z * z + (is2d ? 0: y * y));
    }
	
	public static Vec3 getGunBarrelVec(LivingEntity entity, boolean mainHand, Vec3 rightHandForward) {
		Vec3 start = entity.position().add(0, entity.getEyeHeight(), 0);
		int flip = mainHand == (entity.getMainArm() == HumanoidArm.RIGHT) ? -1 : 1;
		Vec3 barrelPosNoTransform = new Vec3(flip * rightHandForward.x, rightHandForward.y, rightHandForward.z)
			.xRot(-entity.getXRot() * Mth.DEG_TO_RAD)
			.yRot(-entity.getYRot() * Mth.DEG_TO_RAD);
		return start.add(barrelPosNoTransform);
	}
	
	//for Sable stuff:
	public static Vec3 rotateQuat(final Vec3 V, final Quaterniond Q) {
		double
			qww = Q.w*Q.w,
			qwx = Q.w*Q.x, qxx = Q.x*Q.x, qxy = Q.x*Q.y,
			qwy = Q.w*Q.y, qyy = Q.y*Q.y, qyz = Q.y*Q.z,
			qwz = Q.w*Q.z, qzz = Q.z*Q.z, qzx = Q.x*Q.z;
		
		double
			x = org.joml.Math.fma(V.x, qww + qxx - qyy - qzz, 2d * org.joml.Math.fma(V.y, qxy - qwz, V.z*(qzx + qwy))),
			y = org.joml.Math.fma(V.y, qww + qyy - qzz - qxx, 2d * org.joml.Math.fma(V.z, qyz - qwx, V.x*(qxy + qwz))),
			z = org.joml.Math.fma(V.z, qww + qzz - qxx - qyy, 2d * org.joml.Math.fma(V.x, qzx - qwy, V.y*(qyz + qwx)));
		
		return new Vec3(x, y, z);
	}
	
	public static EnumMap<Direction, Vec3> getRotatedNormals(final Quaterniond Q) {
		double
			qww = Q.w*Q.w,
			qwx = Q.w*Q.x, qxx = Q.x*Q.x, qxy = Q.x*Q.y,
			qwy = Q.w*Q.y, qyy = Q.y*Q.y, qyz = Q.y*Q.z,
			qwz = Q.w*Q.z, qzz = Q.z*Q.z, qzx = Q.x*Q.z;
		
		double
			XX = qww + qxx - qyy - qzz, XY = 2d*(qxy - qwz), XZ = 2d*(qzx + qwy),
			YY = qww + qyy - qzz - qxx, YZ = 2d*(qyz - qwx), YX = 2d*(qxy + qwz),
			ZZ = qww + qzz - qxx - qyy, ZX = 2d*(qzx - qwy), ZY = 2d*(qyz + qwx);
		
		return new EnumMap<>(Map.of(
			Direction.DOWN,  new Vec3(-XY,-YY,-ZY), //( 0,-1, 0)
			Direction.UP,    new Vec3( XY, YY, ZY), //( 0, 1, 0)
			Direction.NORTH, new Vec3(-XZ,-YZ,-ZZ), //( 0, 0,-1)
			Direction.SOUTH, new Vec3( XZ, YZ, ZZ), //( 0, 0, 1)
			Direction.WEST,  new Vec3(-XX,-YX,-ZX), //(-1, 0, 0)
			Direction.EAST,  new Vec3( XX, YX, ZX)  //( 1, 0, 0)
		));
	}
	
	public static LangBuilder fluidOutOfCapacity(int volume, ChatFormatting style, int capacity) {
		LangBuilder mb = CreateLang.translate("generic.unit.millibuckets");
		
		return TFMGLang.builder()
			.add(TFMGLang.number(volume).add(mb).style(style))
			.text(ChatFormatting.GRAY, " / ")
			.add(TFMGLang.number(capacity).add(mb).style(ChatFormatting.DARK_GRAY));
	}
	
	/// Populates a tooltip with all the fluid tanks given to it.
	public static boolean createFluidTooltip(List<Component> tooltip, IFluidHandler... handlers) {
		return createFluidTooltip(tooltip, false, handlers);
	}
	
	/** Populates a tooltip with all the fluid tanks given to it
	 * @param showIfEmpty determines whether fluid handling is noted if the fluid tanks are empty.
	 * **/
	public static boolean createFluidTooltip(List<Component> tooltip, boolean showIfEmpty, IFluidHandler... handlers) {
		LangBuilder mb = CreateLang.translate("generic.unit.millibuckets");
		TFMGLang.translate("goggles.fluid_storage").forGoggles(tooltip);
		
		boolean isEmpty = true;
		for (IFluidHandler handler : handlers) {
			if (handler.getTanks() == 0)
				continue;
			
			for (int i = 0; i < handler.getTanks(); i++) {
				FluidStack fluidStack = handler.getFluidInTank(i);
				if (fluidStack.isEmpty() && !showIfEmpty) continue;
				//todo: find or create lang key for empty tank
				LangBuilder name = fluidStack.isEmpty() ? TFMGLang.text("Empty") : TFMGLang.fluidName(fluidStack);
				
				name.style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
				
				fluidOutOfCapacity(fluidStack.getAmount(), ChatFormatting.DARK_GREEN, handler.getTankCapacity(i))
					.forGoggles(tooltip, 2);
				
				isEmpty = false;
			}
		}
		
		if (isEmpty & !showIfEmpty) {
			tooltip.removeLast();
			if (handlers.length == 1 && handlers[0].getTanks() == 1) {
				CreateLang.translate("gui.goggles.fluid_container.capacity")
					.add(CreateLang.number(handlers[0].getTankCapacity(0))
						.add(mb)
						.style(ChatFormatting.DARK_GREEN))
					.style(ChatFormatting.GRAY)
					.forGoggles(tooltip, 1);
				return true;
			}
			return false;
		}
		
		return true;
	}
	
	/// Populates a tooltip with information about the items contained
	public static boolean createItemTooltip(List<Component> tooltip, IItemHandler handler) {
		if (handler == null)
			return false;
		
		if (handler.getSlots() == 0)
			return false;
		
		CreateLang.translate("goggles.item_storage").forGoggles(tooltip);
		boolean isEmpty = true;
		for (int i = 0; i < handler.getSlots(); i++) {
			ItemStack itemStack = handler.getStackInSlot(i);
			
			if (itemStack.isEmpty()) continue;
			CreateLang.itemName(itemStack).style(ChatFormatting.GRAY).add(Component.literal(" x " + itemStack.getCount()).withStyle(ChatFormatting.DARK_GREEN)).forGoggles(tooltip, 1);
			isEmpty = false;
		}
		if (handler.getSlots() > 1) {
			if (isEmpty) tooltip.removeLast();
			return true;
		}
		if (!isEmpty) return true;
		
		CreateLang.translate("item_attributes.shulker_level.empty").style(ChatFormatting.DARK_GRAY).forGoggles(tooltip, 1);
		return true;
	}

    public static String formatUnits(double n, String unit) {
        if (n == 0)
            return TFMGTexts.DECIMAL_FORMAT.format(n) + unit;
        double value;
        if (n >= 1000000000) {
            value = n / 1.0E8;
            return TFMGTexts.DECIMAL_FORMAT.format(value / 10.0) + "G" + unit;
        } else if (n >= 1000000) {
            value = n / 100000.0;
            return TFMGTexts.DECIMAL_FORMAT.format(value / 10.0) + "M" + unit;
        } else if (n >= 1000) {
            value = n / 100.0;
            return TFMGTexts.DECIMAL_FORMAT.format(value / 10.0) + "k" + unit;
        }
        else if (n < 0.001) {
            value = n * 10000000.0;
            return TFMGTexts.DECIMAL_FORMAT.format(value / 10.0) + "μ" + unit;
        }
        else if (n < 1) {
            value = n * 10000.0;
            return TFMGTexts.DECIMAL_FORMAT.format(value / 10.0) + "m" + unit;
        }

        return TFMGTexts.DECIMAL_FORMAT.format(n) + unit;
    }

    public static String formatFluid(double amount) {
        if (amount == 0)
            return TFMGTexts.DECIMAL_FORMAT.format(amount) + "mB";
        double value;
        if (amount >= 1000) {
            value = amount / 1000;
            return TFMGTexts.DECIMAL_FORMAT.format(value) + "B";
        }
        return TFMGTexts.DECIMAL_FORMAT.format(amount) + "mB";
    }
	
	/** Use {@link com.drmangotea.tfmg.base.fluid.ForceableFluidTank#forceDrain(int, IFluidHandler.FluidAction)} instead. **/
	@Deprecated(since = "1.2.5")
	public static void drainFilteredTank(SmartFluidTank tank, int amount) {
        tank.setFluid(new FluidStack(tank.getFluid().getFluidHolder(), Math.max(tank.getFluidAmount() - amount, 0)));
    }
	
	/** Use {@link com.drmangotea.tfmg.base.fluid.ForceableFluidTank#forceFill(FluidStack, IFluidHandler.FluidAction)} instead. **/
	@Deprecated(since = "1.2.5")
    public static void fillFilteredTank(SmartFluidTank tank, FluidStack resource) {
        if (tank.getFluid().getFluid().isSame(resource.getFluid()) || tank.isEmpty())
            tank.setFluid(new FluidStack(resource.getFluid(), Math.min(tank.getFluidAmount() + resource.getAmount(), tank.getCapacity())));
    }

    /// //////////////////////

    public static Electrode getElectrode(ResourceLocation key) {
        return TFMGRegistries.ELECTRODE_REGISTRY.get(key);
    }

    public static CableType getCableType(ResourceLocation key) {
        return TFMGRegistries.CABLE_TYPE_REGISTRY.get(key);
    }

    public static boolean returnItemToInventory(SmartInventory container, int slot, Player player, InteractionHand hand) {
        if(!container.isEmpty()) {
            if (ItemStack.isSameItemSameComponents(player.getItemInHand(hand), container.getItem(slot))) {
                ItemStack newStack = player.getItemInHand(hand).copyWithCount(player.getItemInHand(hand).getCount() + container.getItem(slot).getCount());
                player.setItemInHand(hand, newStack);
                container.extractItem(slot, container.getItem(slot).getCount(), false);
                return true;
            } else if (player.getInventory().add(container.getItem(slot))) {
                player.drop(container.getItem(slot), false);
                container.extractItem(slot, container.getItem(slot).getCount(), false);
                return true;
            }
        }
        return false;
    }

    public static Color blendColours(Color... c) {
        if (c == null || c.length == 0) {
            return null;
        }
        float ratio = 1f / ((float) c.length);

        int a = 0;
        int r = 0;
        int g = 0;
        int b = 0;

        for (Color color : c) {
            int rgb = color.getRGB();
            int a1 = (rgb >> 24 & 0xff);
            int r1 = ((rgb & 0xff0000) >> 16);
            int g1 = ((rgb & 0xff00) >> 8);
            int b1 = (rgb & 0xff);
            a = (int) (a + (a1 * ratio));
            r = (int) (r + (r1 * ratio));
            g = (int) (g + (g1 * ratio));
            b = (int) (b + (b1 * ratio));
        }

        return new Color(a << 24 | r << 16 | g << 8 | b);
    }

    public static boolean isAprilFirst() {
        LocalDate date = LocalDate.now();
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        return month == 4 && day == 1;
    }
}
