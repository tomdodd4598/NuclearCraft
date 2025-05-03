package nc.multiblock.turbine;

import it.unimi.dsi.fastutil.doubles.*;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.block.turbine.BlockTurbineRotorShaft;
import nc.handler.SoundHandler;
import nc.init.*;
import nc.multiblock.*;
import nc.multiblock.turbine.Turbine.PlaneDir;
import nc.multiblock.turbine.TurbineRotorBladeUtil.*;
import nc.network.multiblock.*;
import nc.recipe.*;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.*;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.tile.turbine.*;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.EnumFacing.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Vector3f;
import java.util.*;
import java.util.stream.Stream;

import static nc.config.NCConfig.*;

public class TurbineLogic extends MultiblockLogic<Turbine, TurbineLogic, ITurbinePart> implements IPacketMultiblockLogic<Turbine, TurbineLogic, ITurbinePart, TurbineUpdatePacket> {
	
	public boolean searchFlag = false;
	
	public final ObjectSet<TileTurbineDynamoPart> dynamoPartCache = new ObjectOpenHashSet<>(), dynamoPartCacheOpposite = new ObjectOpenHashSet<>();
	public final Long2ObjectMap<TileTurbineDynamoPart> componentFailCache = new Long2ObjectOpenHashMap<>(), assumedValidCache = new Long2ObjectOpenHashMap<>();
	
	public float prevAngVel = 0F;
	
	public TurbineLogic(Turbine turbine) {
		super(turbine);
	}
	
	public TurbineLogic(TurbineLogic oldLogic) {
		super(oldLogic);
		searchFlag = oldLogic.searchFlag;
		prevAngVel = oldLogic.prevAngVel;
	}
	
	@Override
	public String getID() {
		return "turbine";
	}
	
	// Multiblock Size Limits
	
	@Override
	public int getMinimumInteriorLength() {
		return turbine_min_size;
	}
	
	@Override
	public int getMaximumInteriorLength() {
		return turbine_max_size;
	}
	
	// Multiblock Methods
	
	@Override
	public void onMachineAssembled() {
		onTurbineFormed();
	}
	
	@Override
	public void onMachineRestored() {
		onTurbineFormed();
	}
	
	protected void onTurbineFormed() {
		for (ITurbineController<?> contr : getParts(ITurbineController.class)) {
			multiblock.controller = contr;
			break;
		}
		setIsTurbineOn();
		
		if (!getWorld().isRemote) {
			multiblock.energyStorage.setStorageCapacity((long) Turbine.BASE_MAX_ENERGY * multiblock.getExteriorSurfaceArea());
			multiblock.energyStorage.setMaxTransfer((long) Turbine.BASE_MAX_ENERGY * multiblock.getExteriorSurfaceArea());
			multiblock.tanks.get(0).setCapacity(Turbine.BASE_MAX_INPUT * multiblock.getExteriorSurfaceArea());
			multiblock.tanks.get(1).setCapacity(Turbine.BASE_MAX_OUTPUT * multiblock.getExteriorSurfaceArea());
		}
		
		if (multiblock.flowDir == null) {
			return;
		}
		
		if (!getWorld().isRemote) {
			componentFailCache.clear();
			do {
				assumedValidCache.clear();
				refreshDynamos();
			}
			while (searchFlag);
			
			refreshDynamoStats();
			
			for (TileTurbineRotorShaft shaft : getParts(TileTurbineRotorShaft.class)) {
				BlockPos pos = shaft.getPos();
				IBlockState state = getWorld().getBlockState(pos);
				if (state.getBlock() instanceof BlockTurbineRotorShaft) {
					getWorld().setBlockState(pos, state.withProperty(TurbineRotorBladeUtil.DIR, TurbinePartDir.INVISIBLE));
				}
			}
			
			for (TileTurbineRotorBlade blade : getParts(TileTurbineRotorBlade.class)) {
				BlockPos pos = blade.bladePos();
				IBlockState state = getWorld().getBlockState(pos);
				if (state.getBlock() instanceof IBlockRotorBlade) {
					getWorld().setBlockState(pos, state.withProperty(TurbineRotorBladeUtil.DIR, TurbinePartDir.INVISIBLE));
				}
			}
			
			for (TileTurbineRotorStator stator : getParts(TileTurbineRotorStator.class)) {
				BlockPos pos = stator.bladePos();
				IBlockState state = getWorld().getBlockState(pos);
				if (state.getBlock() instanceof IBlockRotorBlade) {
					getWorld().setBlockState(pos, state.withProperty(TurbineRotorBladeUtil.DIR, TurbinePartDir.INVISIBLE));
				}
			}
			
			for (TileTurbineDynamoPart dynamoPart : getParts(TileTurbineDynamoPart.class)) {
				for (EnumFacing side : EnumFacing.VALUES) {
					dynamoPart.setEnergyConnection(side == multiblock.flowDir || side == multiblock.flowDir.getOpposite() ? EnergyConnection.OUT : EnergyConnection.NON, side);
				}
			}
			
			for (TileTurbineOutlet outlet : getParts(TileTurbineOutlet.class)) {
				for (EnumFacing side : EnumFacing.VALUES) {
					outlet.setTankSorption(side, 0, side == multiblock.flowDir ? TankSorption.OUT : TankSorption.NON);
				}
			}
		}
		
		EnumFacing oppositeDir = multiblock.flowDir.getOpposite();
		int flowLength = multiblock.getFlowLength(), bladeLength = multiblock.bladeLength, shaftWidth = multiblock.shaftWidth;
		
		multiblock.inputPlane[0] = multiblock.getInteriorPlane(oppositeDir, 0, 0, 0, bladeLength, shaftWidth + bladeLength);
		multiblock.inputPlane[1] = multiblock.getInteriorPlane(oppositeDir, 0, shaftWidth + bladeLength, 0, 0, bladeLength);
		multiblock.inputPlane[2] = multiblock.getInteriorPlane(oppositeDir, 0, bladeLength, shaftWidth + bladeLength, 0, 0);
		multiblock.inputPlane[3] = multiblock.getInteriorPlane(oppositeDir, 0, 0, bladeLength, shaftWidth + bladeLength, 0);
		
		if (!getWorld().isRemote) {
			multiblock.renderPosArray = new Vector3f[(1 + 4 * shaftWidth) * flowLength];
			
			for (int depth = 0; depth < flowLength; ++depth) {
				for (int w = 0; w < shaftWidth; ++w) {
					multiblock.renderPosArray[w + depth * shaftWidth] = multiblock.getMiddleInteriorPlaneCoord(oppositeDir, depth, 1 + w + bladeLength, 0, shaftWidth - w + bladeLength, shaftWidth + bladeLength);
					multiblock.renderPosArray[w + (depth + flowLength) * shaftWidth] = multiblock.getMiddleInteriorPlaneCoord(oppositeDir, depth, 0, shaftWidth - w + bladeLength, shaftWidth + bladeLength, 1 + w + bladeLength);
					multiblock.renderPosArray[w + (depth + 2 * flowLength) * shaftWidth] = multiblock.getMiddleInteriorPlaneCoord(oppositeDir, depth, shaftWidth + bladeLength, 1 + w + bladeLength, 0, shaftWidth - w + bladeLength);
					multiblock.renderPosArray[w + (depth + 3 * flowLength) * shaftWidth] = multiblock.getMiddleInteriorPlaneCoord(oppositeDir, depth, shaftWidth - w + bladeLength, shaftWidth + bladeLength, 1 + w + bladeLength, 0);
				}
				multiblock.renderPosArray[depth + 4 * flowLength * shaftWidth] = multiblock.getMiddleInteriorPlaneCoord(oppositeDir, depth, 0, 0, 0, 0);
			}
			
			if (multiblock.controller != null) {
				multiblock.sendMultiblockUpdatePacketToAll();
				multiblock.markReferenceCoordForUpdate();
			}
		}
	}
	
	protected void refreshDynamos() {
		searchFlag = false;
		
		if (getPartMap(TileTurbineDynamoPart.class).isEmpty()) {
			multiblock.conductivity = 0D;
			return;
		}
		
		for (TileTurbineDynamoPart dynamoPart : getParts(TileTurbineDynamoPart.class)) {
			dynamoPart.isSearched = dynamoPart.isInValidPosition = false;
		}
		
		dynamoPartCache.clear();
		dynamoPartCacheOpposite.clear();
		
		for (TileTurbineDynamoPart dynamoPart : getParts(TileTurbineDynamoPart.class)) {
			if (dynamoPart.isSearchRoot()) {
				iterateDynamoSearch(dynamoPart, dynamoPart.getPartPosition().getFacing() == multiblock.flowDir ? dynamoPartCache : dynamoPartCacheOpposite);
			}
		}
		
		for (TileTurbineDynamoPart dynamoPart : assumedValidCache.values()) {
			if (!dynamoPart.isInValidPosition) {
				componentFailCache.put(dynamoPart.getPos().toLong(), dynamoPart);
				searchFlag = true;
			}
		}
	}
	
	protected void iterateDynamoSearch(TileTurbineDynamoPart rootDynamoPart, ObjectSet<TileTurbineDynamoPart> currentDynamoPartCache) {
		final ObjectSet<TileTurbineDynamoPart> searchCache = new ObjectOpenHashSet<>();
		rootDynamoPart.dynamoSearch(currentDynamoPartCache, searchCache, componentFailCache, assumedValidCache);
		
		do {
			final Iterator<TileTurbineDynamoPart> searchIterator = searchCache.iterator();
			final ObjectSet<TileTurbineDynamoPart> searchSubCache = new ObjectOpenHashSet<>();
			while (searchIterator.hasNext()) {
				TileTurbineDynamoPart component = searchIterator.next();
				searchIterator.remove();
				component.dynamoSearch(currentDynamoPartCache, searchSubCache, componentFailCache, assumedValidCache);
			}
			searchCache.addAll(searchSubCache);
		}
		while (!searchCache.isEmpty());
	}
	
	protected void refreshDynamoStats() {
		multiblock.dynamoCoilCount = multiblock.dynamoCoilCountOpposite = 0;
		double newConductivity = 0D, newConductivityOpposite = 0D;
		for (TileTurbineDynamoPart dynamoPart : dynamoPartCache) {
			if (dynamoPart.conductivity != null) {
				++multiblock.dynamoCoilCount;
				newConductivity += dynamoPart.conductivity;
			}
		}
		for (TileTurbineDynamoPart dynamoPart : dynamoPartCacheOpposite) {
			if (dynamoPart.conductivity != null) {
				++multiblock.dynamoCoilCountOpposite;
				newConductivityOpposite += dynamoPart.conductivity;
			}
		}
		
		int bearingCount = getPartCount(TileTurbineRotorBearing.class);
		newConductivity = multiblock.dynamoCoilCount == 0 ? 0D : newConductivity / Math.max(bearingCount / 2D, multiblock.dynamoCoilCount);
		newConductivityOpposite = multiblock.dynamoCoilCountOpposite == 0 ? 0D : newConductivityOpposite / Math.max(bearingCount / 2D, multiblock.dynamoCoilCountOpposite);
		
		multiblock.conductivity = (newConductivity + newConductivityOpposite) / 2D;
	}
	
	@Override
	public void onMachinePaused() {
		// onTurbineBroken();
	}
	
	@Override
	public void onMachineDisassembled() {
		onTurbineBroken();
	}
	
	public void onTurbineBroken() {
		makeRotorVisible();
		
		multiblock.isTurbineOn = multiblock.isProcessing = false;
		if (multiblock.controller != null) {
			multiblock.controller.setActivity(false);
		}
		multiblock.power = multiblock.rawPower = multiblock.rawLimitPower = multiblock.rawMaxPower = multiblock.conductivity = multiblock.rotorEfficiency = 0D;
		multiblock.angVel = multiblock.rotorAngle = 0F;
		multiblock.flowDir = null;
		multiblock.shaftWidth = multiblock.inertia = multiblock.bladeLength = multiblock.noBladeSets = multiblock.recipeInputRate = 0;
		multiblock.totalExpansionLevel = multiblock.idealTotalExpansionLevel = 1D;
		multiblock.minBladeExpansionCoefficient = Double.MAX_VALUE;
		multiblock.maxBladeExpansionCoefficient = 1D;
		multiblock.minStatorExpansionCoefficient = 1D;
		multiblock.maxStatorExpansionCoefficient = Double.MIN_VALUE;
		multiblock.particleEffect = "cloud";
		multiblock.particleSpeedMult = 1D / 23.2D;
		multiblock.basePowerPerMB = multiblock.recipeInputRateFP = 0D;
		multiblock.expansionLevels.clear();
		multiblock.rawBladeEfficiencies.clear();
		multiblock.inputPlane[0] = multiblock.inputPlane[1] = multiblock.inputPlane[2] = multiblock.inputPlane[3] = null;
		
		for (TileTurbineDynamoPart dynamoPart : getParts(TileTurbineDynamoPart.class)) {
			dynamoPart.isSearched = dynamoPart.isInValidPosition = false;
		}
		
		if (getWorld().isRemote) {
			clearSounds();
		}
	}
	
	protected void makeRotorVisible() {
		multiblock.shouldSpecialRenderRotor = false;
		
		if (multiblock.flowDir != null) {
			TurbinePartDir shaftDir = multiblock.getShaftDir();
			for (TileTurbineRotorShaft shaft : getParts(TileTurbineRotorShaft.class)) {
				BlockPos pos = shaft.getPos();
				IBlockState state = getWorld().getBlockState(pos);
				if (state.getBlock() instanceof BlockTurbineRotorShaft) {
					getWorld().setBlockState(pos, state.withProperty(TurbineRotorBladeUtil.DIR, shaftDir));
				}
			}
			
			for (TileTurbineRotorBlade blade : getParts(TileTurbineRotorBlade.class)) {
				BlockPos pos = blade.bladePos();
				IBlockState state = getWorld().getBlockState(pos);
				if (state.getBlock() instanceof IBlockRotorBlade) {
					getWorld().setBlockState(pos, state.withProperty(TurbineRotorBladeUtil.DIR, blade.getDir()));
				}
			}
			
			for (TileTurbineRotorStator stator : getParts(TileTurbineRotorStator.class)) {
				BlockPos pos = stator.bladePos();
				IBlockState state = getWorld().getBlockState(pos);
				if (state.getBlock() instanceof IBlockRotorBlade) {
					getWorld().setBlockState(pos, state.withProperty(TurbineRotorBladeUtil.DIR, stator.getDir()));
				}
			}
		}
	}
	
	@Override
	public boolean isMachineWhole() {
		int minX = multiblock.getMinX(), minY = multiblock.getMinY(), minZ = multiblock.getMinZ();
		int maxX = multiblock.getMaxX(), maxY = multiblock.getMaxY(), maxZ = multiblock.getMaxZ();
		
		// Bearings -> flow axis
		
		boolean dirMinX = false, dirMaxX = false, dirMinY = false, dirMaxY = false, dirMinZ = false, dirMaxZ = false;
		Axis axis = null;
		boolean tooManyAxes = false; // Is any of the bearings in more than a single axis?
		boolean notInAWall = false; // Is the bearing somewhere else in the structure other than the wall?
		
		for (TileTurbineRotorBearing bearing : getParts(TileTurbineRotorBearing.class)) {
			BlockPos pos = bearing.getPos();
			
			if (pos.getX() == minX) {
				dirMinX = true;
			}
			else if (pos.getX() == maxX) {
				dirMaxX = true;
			}
			else if (pos.getY() == minY) {
				dirMinY = true;
			}
			else if (pos.getY() == maxY) {
				dirMaxY = true;
			}
			else if (pos.getZ() == minZ) {
				dirMinZ = true;
			}
			else if (pos.getZ() == maxZ) {
				dirMaxZ = true;
			}
			else {
				// If the bearing is not at any of those positions, that means our bearing isn't part of the wall at all
				notInAWall = true;
			}
		}
		
		if (dirMinX && dirMaxX) {
			axis = Axis.X;
		}
		if (dirMinY && dirMaxY) {
			if (axis != null) {
				tooManyAxes = true;
			}
			else {
				axis = Axis.Y;
			}
		}
		if (dirMinZ && dirMaxZ) {
			if (axis != null) {
				tooManyAxes = true;
			}
			else {
				axis = Axis.Z;
			}
		}
		
		if (axis == null) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.need_bearings", null);
			return false;
		}
		
		if (axis == Axis.X && multiblock.getInteriorLengthY() != multiblock.getInteriorLengthZ() || axis == Axis.Y && multiblock.getInteriorLengthZ() != multiblock.getInteriorLengthX() || axis == Axis.Z && multiblock.getInteriorLengthX() != multiblock.getInteriorLengthY() || tooManyAxes || notInAWall) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.bearings_side_square", null);
			return false;
		}
		
		// At this point, all bearings are guaranteed to be part of walls in the same axis
		// Also, it can only ever succeed up to this point if we already have at least two bearings, so no need to check for that
		
		int internalDiameter;
		if (axis == Axis.X) {
			internalDiameter = multiblock.getInteriorLengthY();
		}
		else if (axis == Axis.Y) {
			internalDiameter = multiblock.getInteriorLengthZ();
		}
		else {
			internalDiameter = multiblock.getInteriorLengthX();
		}
		boolean isEvenDiameter = (internalDiameter & 1) == 0;
		boolean validAmountOfBearings = false;
		
		for (multiblock.shaftWidth = isEvenDiameter ? 2 : 1; multiblock.shaftWidth <= internalDiameter - 2; multiblock.shaftWidth += 2) {
			if (getPartCount(TileTurbineRotorBearing.class) == 2 * multiblock.shaftWidth * multiblock.shaftWidth) {
				validAmountOfBearings = true;
				break;
			}
		}
		
		if (!validAmountOfBearings) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.bearings_center_and_square", null);
			return false;
		}
		
		// Last thing that needs to be checked concerning bearings is whether they are grouped correctly at the center of their respective walls
		
		multiblock.bladeLength = (internalDiameter - multiblock.shaftWidth) / 2;
		
		for (BlockPos pos : multiblock.getInteriorPlane(EnumFacing.getFacingFromAxis(AxisDirection.NEGATIVE, axis), -1, multiblock.bladeLength, multiblock.bladeLength, multiblock.bladeLength, multiblock.bladeLength)) {
			if (getPartMap(TileTurbineRotorBearing.class).containsKey(pos.toLong())) {
				continue;
			}
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.bearings_center_and_square", pos);
			return false;
		}
		
		for (BlockPos pos : multiblock.getInteriorPlane(EnumFacing.getFacingFromAxis(AxisDirection.POSITIVE, axis), -1, multiblock.bladeLength, multiblock.bladeLength, multiblock.bladeLength, multiblock.bladeLength)) {
			if (getPartMap(TileTurbineRotorBearing.class).containsKey(pos.toLong())) {
				continue;
			}
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.bearings_center_and_square", pos);
			return false;
		}
		
		// All bearings should be valid by now!
		
		// Inlets/outlets -> multiblock.flowDir
		
		multiblock.flowDir = null;
		
		if (getPartMap(TileTurbineInlet.class).isEmpty() || getPartMap(TileTurbineOutlet.class).isEmpty()) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", null);
			return false;
		}
		
		for (TileTurbineInlet inlet : getParts(TileTurbineInlet.class)) {
			BlockPos pos = inlet.getPos();
			
			if (multiblock.isInMinWall(axis, pos)) {
				EnumFacing thisFlowDir = EnumFacing.getFacingFromAxis(AxisDirection.POSITIVE, axis);
				// Make sure that all inlets are in the same wall
				if (multiblock.flowDir != null && multiblock.flowDir != thisFlowDir) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
					return false;
				}
				else {
					multiblock.flowDir = thisFlowDir;
				}
			}
			else if (multiblock.isInMaxWall(axis, pos)) {
				EnumFacing thisFlowDir = EnumFacing.getFacingFromAxis(AxisDirection.NEGATIVE, axis);
				// Make sure that all inlets are in the same wall
				if (multiblock.flowDir != null && multiblock.flowDir != thisFlowDir) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
					return false;
				}
				else {
					multiblock.flowDir = thisFlowDir;
				}
			}
			else {
				multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
				return false;
			}
		}
		
		if (multiblock.flowDir == null) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", null);
			return false;
		}
		
		for (TileTurbineOutlet outlet : getParts(TileTurbineOutlet.class)) {
			BlockPos pos = outlet.getPos();
			
			if (!multiblock.isInWall(multiblock.flowDir, pos)) {
				multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
				return false;
			}
		}
		
		// Interior
		
		int flowLength = multiblock.getFlowLength();
		
		for (int depth = 0; depth < flowLength; ++depth) {
			for (BlockPos pos : multiblock.getInteriorPlane(EnumFacing.getFacingFromAxis(AxisDirection.POSITIVE, axis), depth, multiblock.bladeLength, multiblock.bladeLength, multiblock.bladeLength, multiblock.bladeLength)) {
				TileTurbineRotorShaft shaft = getPartMap(TileTurbineRotorShaft.class).get(pos.toLong());
				if (shaft == null) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.shaft_center", pos);
					return false;
				}
			}
		}
		
		if (!areBladesValid()) {
			return false;
		}
		
		if (!NCMath.allEqual(multiblock.getFlowLength(), multiblock.expansionLevels.size(), multiblock.rawBladeEfficiencies.size())) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", null);
			return false;
		}
		
		for (ITurbineController<?> controller : getParts(ITurbineController.class)) {
			controller.setIsRenderer(false);
		}
		for (ITurbineController<?> controller : getParts(ITurbineController.class)) {
			if (multiblock.shouldSpecialRenderRotor) {
				controller.setIsRenderer(true);
			}
			break;
		}
		
		return true;
	}
	
	public boolean areBladesValid() {
		int flowLength = multiblock.getFlowLength();
		
		multiblock.inertia = multiblock.shaftWidth * (multiblock.shaftWidth + 4 * multiblock.bladeLength) * flowLength;
		multiblock.noBladeSets = 0;
		
		multiblock.totalExpansionLevel = 1D;
		multiblock.expansionLevels.clear();
		multiblock.rawBladeEfficiencies.clear();
		
		multiblock.bladePosArray = new BlockPos[4 * flowLength];
		multiblock.bladeAngleArray = new float[4 * flowLength];
		
		for (int depth = 0; depth < flowLength; ++depth) {
			
			// Free space
			
			for (BlockPos pos : multiblock.getInteriorPlane(multiblock.flowDir, depth, 0, 0, multiblock.shaftWidth + multiblock.bladeLength, multiblock.shaftWidth + multiblock.bladeLength)) {
				if (!MaterialHelper.isReplaceable(getWorld().getBlockState(pos).getMaterial())) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.space_between_blades", pos);
					return false;
				}
				getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			
			for (BlockPos pos : multiblock.getInteriorPlane(multiblock.flowDir, depth, multiblock.shaftWidth + multiblock.bladeLength, 0, 0, multiblock.shaftWidth + multiblock.bladeLength)) {
				if (!MaterialHelper.isReplaceable(getWorld().getBlockState(pos).getMaterial())) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.space_between_blades", pos);
					return false;
				}
				getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			
			for (BlockPos pos : multiblock.getInteriorPlane(multiblock.flowDir, depth, 0, multiblock.shaftWidth + multiblock.bladeLength, multiblock.shaftWidth + multiblock.bladeLength, 0)) {
				if (!MaterialHelper.isReplaceable(getWorld().getBlockState(pos).getMaterial())) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.space_between_blades", pos);
					return false;
				}
				getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			
			for (BlockPos pos : multiblock.getInteriorPlane(multiblock.flowDir, depth, multiblock.shaftWidth + multiblock.bladeLength, multiblock.shaftWidth + multiblock.bladeLength, 0, 0)) {
				if (!MaterialHelper.isReplaceable(getWorld().getBlockState(pos).getMaterial())) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.space_between_blades", pos);
					return false;
				}
				getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			
			// Blades/stators
			
			IRotorBladeType currentBladeType = null;
			
			for (BlockPos pos : multiblock.getInteriorPlane(multiblock.flowDir.getOpposite(), depth, multiblock.bladeLength, 0, multiblock.bladeLength, multiblock.shaftWidth + multiblock.bladeLength)) {
				ITurbineRotorBlade<?> thisBlade = multiblock.getBlade(pos);
				IRotorBladeType thisBladeType = thisBlade == null ? null : thisBlade.getBladeType();
				if (thisBladeType == null) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", pos);
					return false;
				}
				else if (currentBladeType == null) {
					currentBladeType = thisBladeType;
				}
				else if (!currentBladeType.eq(thisBladeType)) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.different_type_blades", pos);
					return false;
				}
				thisBlade.setDir(multiblock.getBladeDir(PlaneDir.V));
				
				multiblock.bladePosArray[depth] = thisBlade.bladePos();
				multiblock.bladeAngleArray[depth] = 45F;
			}
			
			for (BlockPos pos : multiblock.getInteriorPlane(multiblock.flowDir.getOpposite(), depth, 0, multiblock.bladeLength, multiblock.shaftWidth + multiblock.bladeLength, multiblock.bladeLength)) {
				ITurbineRotorBlade<?> thisBlade = multiblock.getBlade(pos);
				IRotorBladeType thisBladeType = thisBlade == null ? null : thisBlade.getBladeType();
				if (thisBladeType == null) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", pos);
					return false;
				}
				else if (!currentBladeType.eq(thisBladeType)) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.different_type_blades", pos);
					return false;
				}
				thisBlade.setDir(multiblock.getBladeDir(PlaneDir.U));
				
				multiblock.bladePosArray[depth + flowLength] = thisBlade.bladePos();
				multiblock.bladeAngleArray[depth + flowLength] = multiblock.flowDir.getAxis() == Axis.Z ? -45F : 45F;
			}
			
			for (BlockPos pos : multiblock.getInteriorPlane(multiblock.flowDir.getOpposite(), depth, multiblock.shaftWidth + multiblock.bladeLength, multiblock.bladeLength, 0, multiblock.bladeLength)) {
				ITurbineRotorBlade<?> thisBlade = multiblock.getBlade(pos);
				IRotorBladeType thisBladeType = thisBlade == null ? null : thisBlade.getBladeType();
				if (thisBladeType == null) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", pos);
					return false;
				}
				else if (!currentBladeType.eq(thisBladeType)) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.different_type_blades", pos);
					return false;
				}
				thisBlade.setDir(multiblock.getBladeDir(PlaneDir.U));
				
				multiblock.bladePosArray[depth + 2 * flowLength] = thisBlade.bladePos();
				multiblock.bladeAngleArray[depth + 2 * flowLength] = multiblock.flowDir.getAxis() == Axis.Z ? 45F : -45F;
			}
			
			for (BlockPos pos : multiblock.getInteriorPlane(multiblock.flowDir.getOpposite(), depth, multiblock.bladeLength, multiblock.shaftWidth + multiblock.bladeLength, multiblock.bladeLength, 0)) {
				ITurbineRotorBlade<?> thisBlade = multiblock.getBlade(pos);
				IRotorBladeType thisBladeType = thisBlade == null ? null : thisBlade.getBladeType();
				if (thisBladeType == null) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", pos);
					return false;
				}
				else if (!currentBladeType.eq(thisBladeType)) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.different_type_blades", pos);
					return false;
				}
				thisBlade.setDir(multiblock.getBladeDir(PlaneDir.V));
				
				multiblock.bladePosArray[depth + 3 * flowLength] = thisBlade.bladePos();
				multiblock.bladeAngleArray[depth + 3 * flowLength] = -45F;
			}
			
			if (currentBladeType == null) {
				multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", null);
				return false;
			}
			
			multiblock.expansionLevels.add(multiblock.totalExpansionLevel * Math.sqrt(currentBladeType.getExpansionCoefficient()));
			multiblock.totalExpansionLevel *= currentBladeType.getExpansionCoefficient();
			multiblock.rawBladeEfficiencies.add(currentBladeType.getEfficiency());
			
			if (currentBladeType instanceof IRotorStatorType) {
				multiblock.minStatorExpansionCoefficient = Math.min(currentBladeType.getExpansionCoefficient(), multiblock.minStatorExpansionCoefficient);
				multiblock.maxStatorExpansionCoefficient = Math.max(currentBladeType.getExpansionCoefficient(), multiblock.maxStatorExpansionCoefficient);
			}
			else {
				++multiblock.noBladeSets;
				multiblock.minBladeExpansionCoefficient = Math.min(currentBladeType.getExpansionCoefficient(), multiblock.minBladeExpansionCoefficient);
				multiblock.maxBladeExpansionCoefficient = Math.max(currentBladeType.getExpansionCoefficient(), multiblock.maxBladeExpansionCoefficient);
			}
		}
		
		return true;
	}
	
	@Override
	public List<Pair<Class<? extends ITurbinePart>, String>> getPartBlacklist() {
		return Collections.emptyList();
	}
	
	@Override
	public void onAssimilate(Turbine assimilated) {
		multiblock.energyStorage.mergeEnergyStorage(assimilated.energyStorage);
		multiblock.rawPower += assimilated.rawPower;
		multiblock.rawLimitPower += assimilated.rawLimitPower;
		multiblock.rawMaxPower += assimilated.rawMaxPower;
		assimilated.rawPower = assimilated.rawLimitPower = assimilated.rawMaxPower = 0D;
	}
	
	@Override
	public void onAssimilated(Turbine assimilator) {
		if (getWorld().isRemote) {
			clearSounds();
		}
	}
	
	// Server
	
	@Override
	public boolean onUpdateServer() {
		boolean flag = true, wasProcessing = multiblock.isProcessing;
		refreshRecipe();
		
		setRotorEfficiency();
		setEffectiveMaxLength();
		setInputRatePowerBonus();
		
		double previousRawPower = multiblock.rawPower, previousRawLimitPower = multiblock.rawLimitPower, previousRawMaxPower = multiblock.rawMaxPower;
		multiblock.rawLimitPower = getRawLimitProcessPower(multiblock.recipeInputRate);
		multiblock.rawMaxPower = getRawLimitProcessPower(getMaxRecipeRateMultiplier());
		
		boolean canProcess = canProcessInputs();
		if (canProcess) {
			multiblock.isProcessing = true;
			produceProducts();
			multiblock.rawPower = getNewRawProcessPower(previousRawPower, multiblock.rawLimitPower, true);
		}
		else {
			multiblock.isProcessing = false;
			multiblock.rawMaxPower = previousRawMaxPower;
			multiblock.rawPower = getNewRawProcessPower(previousRawPower, previousRawLimitPower, false);
		}
		
		multiblock.power = multiblock.rawPower * multiblock.conductivity * multiblock.rotorEfficiency * getExpansionIdealityMultiplier(multiblock.idealTotalExpansionLevel, multiblock.totalExpansionLevel) * getThroughputEfficiency() * multiblock.powerBonus;
		multiblock.angVel = multiblock.rawMaxPower == 0D ? 0F : (float) (turbine_render_rotor_speed * multiblock.rawPower / multiblock.rawMaxPower);
		
		if (wasProcessing != multiblock.isProcessing && multiblock.controller != null) {
			multiblock.sendMultiblockUpdatePacketToAll();
		}
		
		int maxRecipeRateMultiplier = getMaxRecipeRateMultiplier();
		double tensionFactor = maxRecipeRateMultiplier <= 0 ? 0D : (multiblock.recipeInputRate - maxRecipeRateMultiplier * (1D + turbine_tension_leniency)) / maxRecipeRateMultiplier;
		if (tensionFactor > 0D) {
			tensionFactor /= (turbine_tension_throughput_factor < 2D ? 1D : turbine_tension_throughput_factor - 1D);
		}
		else {
			tensionFactor = -Math.sqrt(-tensionFactor);
		}
		multiblock.bearingTension = Math.max(0D, multiblock.bearingTension + Math.min(1D, tensionFactor) / (1200D * getPartCount(TileTurbineRotorBearing.class)));
		if (multiblock.bearingTension > 1D) {
			bearingFailure();
			return true;
		}
		
		multiblock.energyStorage.changeEnergyStored((long) multiblock.power);
		
		if (multiblock.controller != null) {
			multiblock.sendMultiblockUpdatePacketToListeners();
			multiblock.sendRenderPacketToAll();
		}
		
		return flag;
	}
	
	protected void bearingFailure() {
		makeRotorVisible();
		
		multiblock.bearingTension = 0D;
		
		Iterator<TileTurbineRotorBearing> bearingIterator = getPartIterator(TileTurbineRotorBearing.class);
		while (bearingIterator.hasNext()) {
			TileTurbineRotorBearing bearing = bearingIterator.next();
			bearing.onBearingFailure(bearingIterator);
		}
		
		Iterator<TileTurbineRotorBlade> bladeIterator = getPartIterator(TileTurbineRotorBlade.class);
		while (bladeIterator.hasNext()) {
			TileTurbineRotorBlade blade = bladeIterator.next();
			blade.onBearingFailure(bladeIterator);
		}
		
		Iterator<TileTurbineRotorStator> statorIterator = getPartIterator(TileTurbineRotorStator.class);
		while (statorIterator.hasNext()) {
			TileTurbineRotorStator stator = statorIterator.next();
			stator.onBearingFailure(statorIterator);
		}
		
		MultiblockRegistry.INSTANCE.addDirtyMultiblock(getWorld(), multiblock);
		
		if (multiblock.controller != null) {
			multiblock.sendMultiblockUpdatePacketToAll();
		}
	}
	
	public void setIsTurbineOn() {
		boolean oldIsTurbineOn = multiblock.isTurbineOn;
		multiblock.isTurbineOn = (isRedstonePowered() || multiblock.computerActivated) && multiblock.isAssembled();
		if (multiblock.isTurbineOn != oldIsTurbineOn) {
			if (multiblock.controller != null) {
				multiblock.controller.setActivity(multiblock.isTurbineOn);
				multiblock.sendMultiblockUpdatePacketToAll();
			}
		}
	}
	
	protected boolean isRedstonePowered() {
		return Stream.concat(Stream.of(multiblock.controller), getParts(TileTurbineRedstonePort.class).stream()).anyMatch(x -> x != null && x.checkIsRedstonePowered(getWorld(), x.getTilePos()));
	}
	
	protected void refreshRecipe() {
		multiblock.recipeInfo = NCRecipes.turbine.getRecipeInfoFromInputs(Collections.emptyList(), multiblock.tanks.subList(0, 1));
	}
	
	protected boolean canProcessInputs() {
		if (!setRecipeStats() || !multiblock.isTurbineOn) {
			multiblock.recipeInputRate = 0;
			multiblock.recipeInputRateFP = 0D;
			return false;
		}
		return canProduceProducts();
	}
	
	protected boolean setRecipeStats() {
		if (multiblock.recipeInfo == null) {
			multiblock.recipeInputRate = 0;
			return false;
		}
		BasicRecipe recipe = multiblock.recipeInfo.recipe;
		multiblock.basePowerPerMB = recipe.getTurbinePowerPerMB();
		multiblock.idealTotalExpansionLevel = recipe.getTurbineExpansionLevel();
		multiblock.spinUpMultiplier = recipe.getTurbineSpinUpMultiplier();
		multiblock.particleEffect = recipe.getTurbineParticleEffect();
		multiblock.particleSpeedMult = recipe.getTurbineParticleSpeedMultiplier();
		return true;
	}
	
	protected boolean canProduceProducts() {
		IFluidIngredient fluidProduct = multiblock.recipeInfo.recipe.getFluidProducts().get(0);
		if (fluidProduct.getMaxStackSize(0) <= 0 || fluidProduct.getStack() == null) {
			return false;
		}
		
		int recipeInputRateDiff = multiblock.recipeInputRate;
		multiblock.recipeInputRate = Math.min(multiblock.tanks.get(0).getFluidAmount(), NCMath.toInt(turbine_tension_throughput_factor * getMaxRecipeRateMultiplier()));
		recipeInputRateDiff = Math.abs(recipeInputRateDiff - multiblock.recipeInputRate);
		
		double roundingFactor = Math.max(0D, 1.5D * Math.log1p(multiblock.recipeInputRate / (1D + recipeInputRateDiff)));
		multiblock.recipeInputRateFP = (roundingFactor * multiblock.recipeInputRateFP + multiblock.recipeInputRate) / (1D + roundingFactor);
		
		if (!multiblock.tanks.get(1).isEmpty()) {
			if (!multiblock.tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack())) {
				return false;
			}
			else if (multiblock.tanks.get(1).getFluidAmount() + fluidProduct.getMaxStackSize(0) * multiblock.recipeInputRate > multiblock.tanks.get(1).getCapacity()) {
				return false;
			}
		}
		return true;
	}
	
	protected void produceProducts() {
		int fluidIngredientStackSize = multiblock.recipeInfo.recipe.getFluidIngredients().get(0).getMaxStackSize(multiblock.recipeInfo.getFluidIngredientNumbers().get(0)) * multiblock.recipeInputRate;
		if (fluidIngredientStackSize > 0) {
			multiblock.tanks.get(0).changeFluidAmount(-fluidIngredientStackSize);
		}
		if (multiblock.tanks.get(0).getFluidAmount() <= 0) {
			multiblock.tanks.get(0).setFluidStored(null);
		}
		
		IFluidIngredient fluidProduct = multiblock.recipeInfo.recipe.getFluidProducts().get(0);
		if (fluidProduct.getMaxStackSize(0) <= 0) {
			return;
		}
		if (multiblock.tanks.get(1).isEmpty()) {
			multiblock.tanks.get(1).setFluidStored(fluidProduct.getNextStack(0));
			multiblock.tanks.get(1).setFluidAmount(multiblock.tanks.get(1).getFluidAmount() * multiblock.recipeInputRate);
		}
		else if (multiblock.tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack())) {
			multiblock.tanks.get(1).changeFluidAmount(fluidProduct.getNextStackSize(0) * multiblock.recipeInputRate);
		}
	}
	
	public int getMaxRecipeRateMultiplier() {
		return multiblock.getBladeVolume() * turbine_mb_per_blade;
	}
	
	public double getRawLimitProcessPower(int recipeInputRate) {
		return multiblock.noBladeSets == 0 ? 0D : recipeInputRate * multiblock.basePowerPerMB;
	}
	
	public double getNewRawProcessPower(double previousRawPower, double maxLimitPower, boolean increasing) {
		double effectiveInertia = getEffectiveInertia(increasing);
		if (increasing) {
			return (effectiveInertia * previousRawPower + maxLimitPower * multiblock.spinUpMultiplier) / (effectiveInertia + multiblock.spinUpMultiplier);
		}
		else {
			return effectiveInertia * previousRawPower / (turbine_spin_down_multiplier * (effectiveInertia + Math.log1p(effectiveInertia) + 1D));
		}
	}
	
	public double getEffectiveInertia(boolean increasing) {
		int bearingCount = getPartCount(TileTurbineRotorBearing.class);
		double mult = (Math.min(1D, (1D + 2D * multiblock.dynamoCoilCount) / bearingCount) + Math.min(1D, (1D + 2D * multiblock.dynamoCoilCountOpposite) / bearingCount)) / 2D;
		return multiblock.inertia * Math.sqrt(increasing ? mult : 1D / mult);
	}
	
	public void setRotorEfficiency() {
		multiblock.rotorEfficiency = 0D;
		
		for (int depth = 0; depth < multiblock.getFlowLength(); ++depth) {
			if (multiblock.rawBladeEfficiencies.get(depth) < 0D) {
				continue;
			}
			multiblock.rotorEfficiency += multiblock.rawBladeEfficiencies.get(depth) * getExpansionIdealityMultiplier(getIdealExpansionLevel(depth), multiblock.expansionLevels.get(depth));
		}
		multiblock.rotorEfficiency /= multiblock.noBladeSets;
	}
	
	public static double getExpansionIdealityMultiplier(double ideal, double actual) {
		if (ideal <= 0D || actual <= 0D) {
			return 0D;
		}
		return ideal < actual ? ideal / actual : actual / ideal;
	}
	
	public double getIdealExpansionLevel(int depth) {
		return Math.pow(multiblock.idealTotalExpansionLevel, (depth + 0.5D) / multiblock.getFlowLength());
	}
	
	public DoubleList getIdealExpansionLevels() {
		DoubleList levels = new DoubleArrayList();
		if (multiblock.flowDir == null) {
			return levels;
		}
		for (int depth = 0; depth < multiblock.getFlowLength(); ++depth) {
			levels.add(getIdealExpansionLevel(depth));
		}
		return levels;
	}
	
	public double getThroughputEfficiency() {
		double effectiveMinLength = multiblock.idealTotalExpansionLevel <= 1D || multiblock.maxBladeExpansionCoefficient <= 1D ? getMaximumInteriorLength() : Math.ceil(Math.log(multiblock.idealTotalExpansionLevel) / Math.log(multiblock.maxBladeExpansionCoefficient));
		double absoluteLeniency = effectiveMinLength * multiblock.getMinimumBladeArea() * turbine_mb_per_blade;
		double throughputRatio = getMaxRecipeRateMultiplier() == 0 ? 1D : Math.min(1D, (multiblock.recipeInputRateFP + absoluteLeniency) / getMaxRecipeRateMultiplier());
		return throughputRatio >= turbine_throughput_leniency_params[1] ? 1D : (1D - turbine_throughput_leniency_params[0]) * Math.sin(throughputRatio * Math.PI / (2D * turbine_throughput_leniency_params[1])) + turbine_throughput_leniency_params[0];
	}
	
	public void setEffectiveMaxLength() {
		if (multiblock.minBladeExpansionCoefficient <= 1D || multiblock.minStatorExpansionCoefficient >= 1D) {
			multiblock.effectiveMaxLength = getMaximumInteriorLength();
		}
		else {
			multiblock.effectiveMaxLength = NCMath.toInt(Math.ceil(MathHelper.clamp((Math.log(multiblock.idealTotalExpansionLevel) - getMaximumInteriorLength() * Math.log(multiblock.minStatorExpansionCoefficient)) / (Math.log(multiblock.minBladeExpansionCoefficient) - Math.log(multiblock.minStatorExpansionCoefficient)), 1D, getMaximumInteriorLength())));
		}
	}
	
	public void setInputRatePowerBonus() {
		double rate = Math.min(multiblock.recipeInputRate, getMaxRecipeRateMultiplier());
		double lengthBonus = rate / (turbine_mb_per_blade * multiblock.getBladeArea() * multiblock.effectiveMaxLength);
		double areaBonus = Math.sqrt(2D * rate / (turbine_mb_per_blade * multiblock.getFlowLength() * getMaximumInteriorLength() * multiblock.effectiveMaxLength));
		multiblock.powerBonus = 1D + turbine_power_bonus_multiplier * Math.pow(lengthBonus * areaBonus, 2D / 3D);
	}
	
	// Client
	
	@Override
	public void onUpdateClient() {
		if (multiblock.shouldSpecialRenderRotor && multiblock.flowDir != null) {
			if (multiblock.nbtUpdateRenderDataFlag) {
				multiblock.nbtUpdateRenderDataFlag = false;
				updateRenderData();
			}
			updateParticles();
		}
		updateSounds();
	}
	
	protected Object2ObjectMap<BlockPos, ISound> getSoundMap() {
		if (multiblock.soundMap == null) {
			multiblock.soundMap = new Object2ObjectOpenHashMap<>();
		}
		return multiblock.soundMap;
	}
	
	@SideOnly(Side.CLIENT)
	protected void updateSounds() {
		if (turbine_sound_volume == 0D) {
			clearSounds();
			return;
		}
		
		if (multiblock.isProcessing && multiblock.isAssembled()) {
			double ratio = (NCMath.EPSILON + Math.abs(multiblock.angVel)) / (NCMath.EPSILON + Math.abs(prevAngVel));
			multiblock.refreshSounds |= ratio < 0.8D || ratio > 1.25D || getSoundMap().isEmpty();
			
			if (!multiblock.refreshSounds) {
				return;
			}
			multiblock.refreshSounds = false;
			
			// Generate sound info if necessary
			
			clearSounds();
			
			final BlockPos minPos = multiblock.getMinimumCoord();
			final BlockPos midPos = multiblock.getMiddleCoord();
			final BlockPos maxPos = multiblock.getMaximumCoord();
			
			final int lengthX = multiblock.getExteriorLengthX();
			final int lengthY = multiblock.getExteriorLengthY();
			final int lengthZ = multiblock.getExteriorLengthZ();
			
			final int[] _x, _y, _z;
			
			if (lengthX > 8) {
				final int powX = NCMath.toInt(Math.pow(lengthX, 0.4D));
				_x = new int[] {minPos.getX() + powX, maxPos.getX() - powX};
			}
			else {
				_x = new int[] {midPos.getX()};
			}
			
			if (lengthY > 8) {
				final int powY = NCMath.toInt(Math.pow(lengthY, 0.4D));
				_y = new int[] {minPos.getY() + powY, maxPos.getY() - powY};
			}
			else {
				_y = new int[] {midPos.getY()};
			}
			
			if (lengthZ > 8) {
				final int powZ = NCMath.toInt(Math.pow(lengthZ, 0.4D));
				_z = new int[] {minPos.getZ() + powZ, maxPos.getZ() - powZ};
			}
			else {
				_z = new int[] {midPos.getZ()};
			}
			
			for (int i : _x) {
				for (int j : _y) {
					for (int k : _z) {
						BlockPos pos = new BlockPos(i, j, k);
						getSoundMap().put(pos, SoundHandler.startBlockSound(NCSounds.turbine_run, pos, (float) ((1D + multiblock.angVel * 2D / turbine_render_rotor_speed) * turbine_sound_volume / 24D), SoundHelper.getPitch(4F * multiblock.angVel / turbine_render_rotor_speed - 2F)));
					}
				}
			}
			
			prevAngVel = multiblock.angVel;
		}
		else {
			multiblock.refreshSounds = true;
			clearSounds();
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected void clearSounds() {
		getSoundMap().forEach((k, v) -> SoundHandler.stopBlockSound(k));
		getSoundMap().clear();
	}
	
	@SideOnly(Side.CLIENT)
	protected void updateParticles() {
		if (multiblock.isProcessing && multiblock.isAssembled() && !Minecraft.getMinecraft().isGamePaused()) {
			// Particles will just reach the outlets at this speed
			double flowSpeed = multiblock.getFlowLength() * multiblock.particleSpeedMult;
			double offsetX = particleSpeedOffset(), offsetY = particleSpeedOffset(), offsetZ = particleSpeedOffset();
			
			double speedX = multiblock.flowDir == EnumFacing.WEST ? -flowSpeed : multiblock.flowDir == EnumFacing.EAST ? flowSpeed : offsetX;
			double speedY = multiblock.flowDir == EnumFacing.DOWN ? -flowSpeed : multiblock.flowDir == EnumFacing.UP ? flowSpeed : offsetY;
			double speedZ = multiblock.flowDir == EnumFacing.NORTH ? -flowSpeed : multiblock.flowDir == EnumFacing.SOUTH ? flowSpeed : offsetZ;
			
			for (Iterable<MutableBlockPos> iter : multiblock.inputPlane) {
				if (iter != null) {
					for (BlockPos pos : iter) {
						if (rand.nextDouble() < turbine_particles * multiblock.recipeInputRateFP / getMaxRecipeRateMultiplier()) {
							double[] spawnPos = particleSpawnPos(pos);
							if (spawnPos != null) {
								getWorld().spawnParticle(EnumParticleTypes.getByName(multiblock.particleEffect), false, spawnPos[0], spawnPos[1], spawnPos[2], speedX, speedY, speedZ);
							}
						}
					}
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected double particleSpeedOffset() {
		return (rand.nextDouble() - 0.5D) / (4D * Math.sqrt(multiblock.getFlowLength()));
	}
	
	@SideOnly(Side.CLIENT)
	protected double[] particleSpawnPos(BlockPos pos) {
		double offsetU = 0.5D + (rand.nextDouble() - 0.5D) / 2D;
		double offsetV = 0.5D + (rand.nextDouble() - 0.5D) / 2D;
		return switch (multiblock.flowDir) {
			case DOWN -> new double[] {pos.getX() + offsetV, pos.getY() + 1D, pos.getZ() + offsetU};
			case UP -> new double[] {pos.getX() + offsetV, pos.getY(), pos.getZ() + offsetU};
			case NORTH -> new double[] {pos.getX() + offsetU, pos.getY() + offsetV, pos.getZ() + 1D};
			case SOUTH -> new double[] {pos.getX() + offsetU, pos.getY() + offsetV, pos.getZ()};
			case WEST -> new double[] {pos.getX() + 1D, pos.getY() + offsetU, pos.getZ() + offsetV};
			case EAST -> new double[] {pos.getX(), pos.getY() + offsetU, pos.getZ() + offsetV};
		};
	}
	
	@SideOnly(Side.CLIENT)
	protected void updateRenderData() {
		int flowLength = multiblock.getFlowLength();
		if (flowLength < 1 || multiblock.bladePosArray == null || multiblock.renderPosArray == null || multiblock.bladeAngleArray == null || multiblock.bladePosArray.length < 4 * flowLength) {
			multiblock.bladePosArray = null;
			multiblock.renderPosArray = null;
			multiblock.bladeAngleArray = null;
		}
		else {
			multiblock.rotorStateArray = new IBlockState[1 + 4 * flowLength];
			multiblock.rotorStateArray[4 * flowLength] = NCBlocks.turbine_rotor_shaft.getDefaultState().withProperty(TurbineRotorBladeUtil.DIR, multiblock.getShaftDir());
			
			for (int i = 0; i < multiblock.bladePosArray.length; ++i) {
				BlockPos pos = multiblock.bladePosArray[i];
				ITurbineRotorBlade<?> thisBlade = multiblock.getBlade(pos);
				multiblock.rotorStateArray[i] = thisBlade == null ? getWorld().getBlockState(pos).getBlock().getDefaultState() : thisBlade.getRenderState();
			}
			
			multiblock.bladeDepths.clear();
			multiblock.statorDepths.clear();
			
			for (int i = 0; i < flowLength; ++i) {
				if (multiblock.getBlade(multiblock.bladePosArray[i]).getBladeType() instanceof IRotorStatorType) {
					multiblock.statorDepths.add(i);
				}
				else {
					multiblock.bladeDepths.add(i);
				}
			}
		}
	}
	
	// NBT
	
	@Override
	public void writeToLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
	
	}
	
	@Override
	public void readFromLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
	
	}
	
	// Packets
	
	@Override
	public TurbineUpdatePacket getMultiblockUpdatePacket() {
		return new TurbineUpdatePacket(multiblock.controller.getTilePos(), multiblock.isTurbineOn, multiblock.energyStorage, multiblock.power, multiblock.rawPower, multiblock.conductivity, multiblock.rotorEfficiency, multiblock.powerBonus, multiblock.totalExpansionLevel, multiblock.idealTotalExpansionLevel, multiblock.shaftWidth, multiblock.bladeLength, multiblock.noBladeSets, multiblock.dynamoCoilCount, multiblock.dynamoCoilCountOpposite, multiblock.bearingTension);
	}
	
	@Override
	public void onMultiblockUpdatePacket(TurbineUpdatePacket message) {
		multiblock.isTurbineOn = message.isTurbineOn;
		multiblock.energyStorage.setEnergyStored(message.energy);
		multiblock.energyStorage.setStorageCapacity(message.capacity);
		multiblock.energyStorage.setMaxTransfer(message.capacity);
		multiblock.power = message.power;
		multiblock.rawPower = message.rawPower;
		multiblock.conductivity = message.conductivity;
		multiblock.rotorEfficiency = message.rotorEfficiency;
		multiblock.powerBonus = message.powerBonus;
		multiblock.totalExpansionLevel = message.totalExpansionLevel;
		multiblock.idealTotalExpansionLevel = message.idealTotalExpansionLevel;
		multiblock.shaftWidth = message.shaftWidth;
		multiblock.bladeLength = message.bladeLength;
		multiblock.noBladeSets = message.noBladeSets;
		multiblock.dynamoCoilCount = message.dynamoCoilCount;
		multiblock.dynamoCoilCountOpposite = message.dynamoCoilCountOpposite;
		multiblock.bearingTension = message.bearingTension;
	}
	
	public TurbineRenderPacket getRenderPacket() {
		return new TurbineRenderPacket(multiblock.controller.getTilePos(), multiblock.particleEffect, multiblock.particleSpeedMult, multiblock.angVel, multiblock.isProcessing, multiblock.recipeInputRate, multiblock.recipeInputRateFP);
	}
	
	public void onRenderPacket(TurbineRenderPacket message) {
		multiblock.particleEffect = message.particleEffect;
		multiblock.particleSpeedMult = message.particleSpeedMult;
		multiblock.angVel = message.angVel;
		boolean wasProcessing = multiblock.isProcessing;
		multiblock.isProcessing = message.isProcessing;
		if (wasProcessing != multiblock.isProcessing) {
			multiblock.refreshSounds = true;
		}
		multiblock.recipeInputRate = message.recipeInputRate;
		multiblock.recipeInputRateFP = message.recipeInputRateFP;
	}
	
	// Multiblock Validators
	
	@Override
	public boolean isBlockGoodForInterior(World world, BlockPos pos) {
		long posLong = pos.toLong();
		if (getPartMap(TileTurbineRotorShaft.class).containsKey(posLong) || getPartMap(TileTurbineRotorBlade.class).containsKey(posLong) || getPartMap(TileTurbineRotorStator.class).containsKey(posLong) || MaterialHelper.isReplaceable(world.getBlockState(pos).getMaterial())) {
			return true;
		}
		else {
			return multiblock.standardLastError(pos);
		}
	}
	
	// Clear Material
	
	@Override
	public void clearAllMaterial() {}
}
