package nc.multiblock.turbine;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.doubles.*;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.multiblock.*;
import nc.multiblock.cuboidal.CuboidalMultiblock;
import nc.multiblock.turbine.TurbineRotorBladeUtil.*;
import nc.network.multiblock.*;
import nc.recipe.*;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.fluid.Tank;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.tile.turbine.*;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.Nonnull;
import javax.vecmath.Vector3f;
import java.util.*;
import java.util.function.UnaryOperator;

import static nc.config.NCConfig.turbine_max_size;

public class Turbine extends CuboidalMultiblock<Turbine, ITurbinePart> implements ILogicMultiblock<Turbine, TurbineLogic, ITurbinePart>, IPacketMultiblock<Turbine, ITurbinePart, TurbineUpdatePacket> {
	
	public static final ObjectSet<Class<? extends ITurbinePart>> PART_CLASSES = new ObjectOpenHashSet<>();
	public static final Object2ObjectMap<String, UnaryOperator<TurbineLogic>> LOGIC_MAP = new Object2ObjectOpenHashMap<>();
	
	protected @Nonnull TurbineLogic logic = new TurbineLogic(this);
	
	protected final PartSuperMap<Turbine, ITurbinePart> partSuperMap = new PartSuperMap<>();
	
	public ITurbineController<?> controller;
	
	public final EnergyStorage energyStorage = new EnergyStorage(BASE_MAX_ENERGY);
	public final List<Tank> tanks = Lists.newArrayList(new Tank(BASE_MAX_INPUT, NCRecipes.getValidFluids("turbine").get(0)), new Tank(BASE_MAX_OUTPUT, null));
	public static final int BASE_MAX_ENERGY = 64000, BASE_MAX_INPUT = 4000, BASE_MAX_OUTPUT = 16000;
	
	public RecipeInfo<BasicRecipe> recipeInfo;
	
	public boolean isTurbineOn, computerActivated, isProcessing;
	public double power = 0D, conductivity = 0D, rotorEfficiency = 0D, powerBonus = 0D;
	public double rawPower = 0D, rawLimitPower = 0D, rawMaxPower = 0D;
	
	public EnumFacing flowDir = null;
	public int shaftWidth = 0, inertia = 0, bladeLength = 0, noBladeSets = 0, recipeInputRate = 0, dynamoCoilCount = 0, dynamoCoilCountOpposite = 0;
	public double totalExpansionLevel = 1D, idealTotalExpansionLevel = 1D, spinUpMultiplier = 1D, basePowerPerMB = 0D, recipeInputRateFP = 0D;
	
	public double minBladeExpansionCoefficient = Double.MAX_VALUE;
	public double maxBladeExpansionCoefficient = 1D;
	public double minStatorExpansionCoefficient = 1D;
	public double maxStatorExpansionCoefficient = Double.MIN_VALUE;
	public int effectiveMaxLength = turbine_max_size;
	public double bearingTension = 0D;
	
	public final DoubleList expansionLevels = new DoubleArrayList(), rawBladeEfficiencies = new DoubleArrayList();
	
	@SideOnly(Side.CLIENT)
	protected Object2ObjectMap<BlockPos, ISound> soundMap;
	public boolean refreshSounds = true;
	
	public String particleEffect = "cloud";
	public double particleSpeedMult = 1D / 23.2D;
	public float angVel = 0F, rotorAngle = 0F;
	
	@SuppressWarnings("unchecked")
	public Iterable<MutableBlockPos>[] inputPlane = new Iterable[4];
	
	public boolean nbtUpdateRenderDataFlag = false;
	public boolean shouldSpecialRenderRotor = false;
	
	public BlockPos[] bladePosArray = null;
	public Vector3f[] renderPosArray = null;
	public float[] bladeAngleArray = null;
	
	public IBlockState[] rotorStateArray = null;
	public final IntList bladeDepths = new IntArrayList(), statorDepths = new IntArrayList();
	
	protected final Set<EntityPlayer> updatePacketListeners = new ObjectOpenHashSet<>();
	
	public Turbine(World world) {
		super(world, Turbine.class, ITurbinePart.class);
		for (Class<? extends ITurbinePart> clazz : PART_CLASSES) {
			partSuperMap.equip(clazz);
		}
	}
	
	@Override
	public @Nonnull TurbineLogic getLogic() {
		return logic;
	}
	
	@Override
	public void setLogic(String logicID) {
		if (logicID.equals(logic.getID())) {
			return;
		}
		logic = getNewLogic(LOGIC_MAP.get(logicID));
	}
	
	// Multiblock Part Getters
	
	@Override
	public PartSuperMap<Turbine, ITurbinePart> getPartSuperMap() {
		return partSuperMap;
	}
	
	// Multiblock Size Limits
	
	@Override
	protected int getMinimumInteriorLength() {
		return logic.getMinimumInteriorLength();
	}
	
	@Override
	protected int getMaximumInteriorLength() {
		return logic.getMaximumInteriorLength();
	}
	
	@Override
	protected int getMinimumNumberOfBlocksForAssembledMachine() {
		return NCMath.hollowCuboid(Math.max(5, getMinimumInteriorLength() + 2), Math.max(5, getMinimumInteriorLength() + 2), getMinimumInteriorLength() + 2);
	}
	
	// Multiblock Methods
	
	@Override
	public void onAttachedPartWithMultiblockData(ITurbinePart part, NBTTagCompound data) {
		logic.onAttachedPartWithMultiblockData(part, data);
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(ITurbinePart newPart) {
		onPartAdded(newPart);
		logic.onBlockAdded(newPart);
	}
	
	@Override
	protected void onBlockRemoved(ITurbinePart oldPart) {
		onPartRemoved(oldPart);
		logic.onBlockRemoved(oldPart);
	}
	
	@Override
	protected void onMachineAssembled() {
		logic.onMachineAssembled();
	}
	
	@Override
	protected void onMachineRestored() {
		logic.onMachineRestored();
	}
	
	@Override
	protected void onMachinePaused() {
		logic.onMachinePaused();
	}
	
	@Override
	protected void onMachineDisassembled() {
		logic.onMachineDisassembled();
	}
	
	@Override
	protected boolean isMachineWhole() {
		shouldSpecialRenderRotor = false;
		
		return setLogic(this) && super.isMachineWhole() && logic.isMachineWhole();
	}
	
	public boolean setLogic(Turbine multiblock) {
		if (getPartMap(ITurbineController.class).isEmpty()) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.no_controller", null);
			return false;
		}
		if (getPartCount(ITurbineController.class) > 1) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.too_many_controllers", null);
			return false;
		}
		
		for (ITurbineController<?> contr : getParts(ITurbineController.class)) {
			controller = contr;
			break;
		}
		
		setLogic(controller.getLogicID());
		
		return true;
	}
	
	@Override
	protected void onAssimilate(Turbine assimilated) {
		logic.onAssimilate(assimilated);
	}
	
	@Override
	protected void onAssimilated(Turbine assimilator) {
		logic.onAssimilated(assimilator);
	}
	
	public int getFlowLength() {
		return getInteriorLength(flowDir);
	}
	
	protected int getBladeArea() {
		return 4 * shaftWidth * bladeLength;
	}
	
	protected int getBladeVolume() {
		return getBladeArea() * noBladeSets;
	}
	
	public double getRotorRadius() {
		return bladeLength + shaftWidth / 2D;
	}
	
	public int getMinimumBladeArea() {
		return 4 * Math.max(1, getMinimumInteriorLength() - 2);
	}
	
	public int getMinimumBladeVolume() {
		return getBladeArea() * getMinimumInteriorLength();
	}
	
	// Modified Kurtchekov stuff!
	
	protected ITurbineRotorBlade<?> getBlade(BlockPos pos) {
		long posLong = pos.toLong();
		TileTurbineRotorBlade blade = getPartMap(TileTurbineRotorBlade.class).get(posLong);
		return blade == null ? getPartMap(TileTurbineRotorStator.class).get(posLong) : blade;
	}
	
	public TurbinePartDir getShaftDir() {
		if (flowDir == null) {
			return TurbinePartDir.Y;
		}
		return switch (flowDir.getAxis()) {
			case Y -> TurbinePartDir.Y;
			case Z -> TurbinePartDir.Z;
			case X -> TurbinePartDir.X;
		};
	}
	
	public TurbinePartDir getBladeDir(PlaneDir planeDir) {
		if (flowDir == null) {
			return TurbinePartDir.Y;
		}
		switch (flowDir.getAxis()) {
			case Y:
				switch (planeDir) {
					case U:
						return TurbinePartDir.Z;
					case V:
						return TurbinePartDir.X;
					default:
						break;
				}
				break;
			case Z:
				switch (planeDir) {
					case U:
						return TurbinePartDir.X;
					case V:
						return TurbinePartDir.Y;
					default:
						break;
				}
				break;
			case X:
				switch (planeDir) {
					case U:
						return TurbinePartDir.Y;
					case V:
						return TurbinePartDir.Z;
					default:
						break;
				}
				break;
			default:
				return TurbinePartDir.Y;
		}
		return TurbinePartDir.Y;
	}
	
	public enum PlaneDir {
		U,
		V
	}
	
	// End of modified Kurtchekov stuff!
	
	// Server
	
	@Override
	protected boolean updateServer() {
		boolean flag = false;
		if (logic.onUpdateServer()) {
			flag = true;
		}
		return flag;
	}
	
	// Client
	
	@Override
	protected void updateClient() {
		logic.onUpdateClient();
	}
	
	// NBT
	
	@Override
	public void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		writeEnergy(energyStorage, data, "energyStorage");
		writeTanks(tanks, data, "tanks");
		data.setBoolean("isTurbineOn", isTurbineOn);
		data.setBoolean("computerActivated", computerActivated);
		data.setBoolean("isProcessing", isProcessing);
		data.setDouble("power", power);
		data.setDouble("rawPower", rawPower);
		data.setDouble("rawLimitPower", rawLimitPower);
		data.setDouble("rawMaxPower", rawMaxPower);
		
		data.setInteger("flowDir", flowDir == null ? -1 : flowDir.getIndex());
		
		data.setDouble("conductivity", conductivity);
		data.setDouble("rotorEfficiency", rotorEfficiency);
		data.setDouble("powerBonus", powerBonus);
		data.setString("particleEffect", particleEffect);
		data.setDouble("particleSpeedMult", particleSpeedMult);
		data.setFloat("angVel", angVel);
		data.setFloat("rotorAngle", rotorAngle);
		
		NBTHelper.writeBlockPosArray(data, bladePosArray, "bladePosArray");
		NBTHelper.writeVector3fArray(data, renderPosArray, "renderPosArray");
		NBTHelper.writeFloatArray(data, bladeAngleArray, "bladeAngleArray");
		
		data.setInteger("shaftWidth", shaftWidth);
		data.setInteger("shaftVolume", inertia);
		data.setInteger("bladeLength", bladeLength);
		data.setInteger("noBladeSets", noBladeSets);
		data.setInteger("recipeInputRate", recipeInputRate);
		data.setInteger("dynamoCoilCount", dynamoCoilCount);
		data.setInteger("dynamoCoilCountOpposite", dynamoCoilCountOpposite);
		data.setDouble("totalExpansionLevel", totalExpansionLevel);
		data.setDouble("idealTotalExpansionLevel", idealTotalExpansionLevel);
		data.setDouble("spinUpMultiplier", spinUpMultiplier);
		data.setDouble("basePowerPerMB", basePowerPerMB);
		data.setDouble("recipeInputRateFP", recipeInputRateFP);
		data.setDouble("minBladeExpansionCoefficient", minBladeExpansionCoefficient);
		data.setDouble("maxBladeExpansionCoefficient", maxBladeExpansionCoefficient);
		data.setDouble("minStatorExpansionCoefficient", minStatorExpansionCoefficient);
		data.setDouble("maxStatorExpansionCoefficient", maxStatorExpansionCoefficient);
		data.setInteger("effectiveMaxLength", effectiveMaxLength);
		data.setDouble("bearingTension", bearingTension);
		
		NBTHelper.writeDoubleCollection(data, expansionLevels, "expansionLevels");
		NBTHelper.writeDoubleCollection(data, rawBladeEfficiencies, "rawBladeEfficiencies");
		
		writeLogicNBT(data, syncReason);
	}
	
	@Override
	public void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		readEnergy(energyStorage, data, "energyStorage");
		readTanks(tanks, data, "tanks");
		isTurbineOn = data.getBoolean("isTurbineOn");
		computerActivated = data.getBoolean("computerActivated");
		isProcessing = data.getBoolean("isProcessing");
		power = data.getDouble("power");
		rawPower = data.getDouble("rawPower");
		rawLimitPower = data.getDouble("rawLimitPower");
		rawMaxPower = data.getDouble("rawMaxPower");
		
		flowDir = data.getInteger("flowDir") < 0 ? null : EnumFacing.VALUES[data.getInteger("flowDir")];
		
		conductivity = data.getDouble("conductivity");
		rotorEfficiency = data.getDouble("rotorEfficiency");
		powerBonus = data.getDouble("powerBonus");
		particleEffect = data.getString("particleEffect");
		particleSpeedMult = data.getDouble("particleSpeedMult");
		angVel = data.getFloat("angVel");
		rotorAngle = data.getFloat("rotorAngle");
		
		bladePosArray = NBTHelper.readBlockPosArray(data, "bladePosArray");
		renderPosArray = NBTHelper.readVector3fArray(data, "renderPosArray");
		bladeAngleArray = NBTHelper.readFloatArray(data, "bladeAngleArray");
		
		shaftWidth = data.getInteger("shaftWidth");
		inertia = data.getInteger("shaftVolume");
		bladeLength = data.getInteger("bladeLength");
		noBladeSets = data.getInteger("noBladeSets");
		recipeInputRate = data.getInteger("recipeInputRate");
		dynamoCoilCount = data.getInteger("dynamoCoilCount");
		dynamoCoilCountOpposite = data.getInteger("dynamoCoilCountOpposite");
		totalExpansionLevel = data.getDouble("totalExpansionLevel");
		idealTotalExpansionLevel = data.getDouble("idealTotalExpansionLevel");
		spinUpMultiplier = data.getDouble("spinUpMultiplier");
		basePowerPerMB = data.getDouble("basePowerPerMB");
		recipeInputRateFP = data.getDouble("recipeInputRateFP");
		minBladeExpansionCoefficient = data.getDouble("minBladeExpansionCoefficient");
		maxBladeExpansionCoefficient = data.getDouble("maxBladeExpansionCoefficient");
		minStatorExpansionCoefficient = data.getDouble("minStatorExpansionCoefficient");
		maxStatorExpansionCoefficient = data.getDouble("maxStatorExpansionCoefficient");
		effectiveMaxLength = data.getInteger("effectiveMaxLength");
		bearingTension = data.getDouble("bearingTension");
		
		NBTHelper.readDoubleCollection(data, expansionLevels, "expansionLevels");
		NBTHelper.readDoubleCollection(data, rawBladeEfficiencies, "rawBladeEfficiencies");
		
		nbtUpdateRenderDataFlag = true;
		
		readLogicNBT(data, syncReason);
	}
	
	// Packets
	
	@Override
	public Set<EntityPlayer> getMultiblockUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public TurbineUpdatePacket getMultiblockUpdatePacket() {
		return logic.getMultiblockUpdatePacket();
	}
	
	@Override
	public void onMultiblockUpdatePacket(TurbineUpdatePacket message) {
		logic.onMultiblockUpdatePacket(message);
	}
	
	protected TurbineRenderPacket getRenderPacket() {
		return logic.getRenderPacket();
	}
	
	public void onRenderPacket(TurbineRenderPacket message) {
		logic.onRenderPacket(message);
	}
	
	public void sendRenderPacketToPlayer(EntityPlayer player) {
		if (WORLD.isRemote) {
			return;
		}
		TurbineRenderPacket packet = getRenderPacket();
		if (packet == null) {
			return;
		}
		packet.sendTo(player);
	}
	
	public void sendRenderPacketToAll() {
		if (WORLD.isRemote) {
			return;
		}
		TurbineRenderPacket packet = getRenderPacket();
		if (packet == null) {
			return;
		}
		packet.sendToAll();
	}
	
	// Multiblock Validators
	
	@Override
	protected boolean isBlockGoodForInterior(World world, BlockPos pos) {
		return logic.isBlockGoodForInterior(world, pos);
	}
	
	// Clear Material
	
	@Override
	public void clearAllMaterial() {
		logic.clearAllMaterial();
		for (Tank tank : tanks) {
			tank.setFluidStored(null);
		}
		super.clearAllMaterial();
	}
}
