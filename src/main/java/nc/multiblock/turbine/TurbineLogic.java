package nc.multiblock.turbine;

import static nc.recipe.NCRecipes.turbine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.Global;
import nc.config.NCConfig;
import nc.handler.SoundHandler;
import nc.handler.SoundHandler.SoundInfo;
import nc.init.NCSounds;
import nc.multiblock.Multiblock;
import nc.multiblock.MultiblockLogic;
import nc.multiblock.container.ContainerTurbineController;
import nc.multiblock.network.TurbineRenderPacket;
import nc.multiblock.network.TurbineUpdatePacket;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
import nc.multiblock.turbine.Turbine.PlaneDir;
import nc.multiblock.turbine.TurbineRotorBladeUtil.IBlockRotorBlade;
import nc.multiblock.turbine.TurbineRotorBladeUtil.IRotorBladeType;
import nc.multiblock.turbine.TurbineRotorBladeUtil.ITurbineRotorBlade;
import nc.multiblock.turbine.TurbineRotorBladeUtil.TurbinePartDir;
import nc.multiblock.turbine.TurbineRotorBladeUtil.TurbineRotorBladeType;
import nc.multiblock.turbine.TurbineRotorBladeUtil.TurbineRotorStatorType;
import nc.multiblock.turbine.block.BlockTurbineRotorShaft;
import nc.multiblock.turbine.tile.ITurbineController;
import nc.multiblock.turbine.tile.ITurbinePart;
import nc.multiblock.turbine.tile.TileTurbineController;
import nc.multiblock.turbine.tile.TileTurbineDynamoPart;
import nc.multiblock.turbine.tile.TileTurbineInlet;
import nc.multiblock.turbine.tile.TileTurbineOutlet;
import nc.multiblock.turbine.tile.TileTurbinePart;
import nc.multiblock.turbine.tile.TileTurbineRotorBearing;
import nc.multiblock.turbine.tile.TileTurbineRotorBlade;
import nc.multiblock.turbine.tile.TileTurbineRotorShaft;
import nc.multiblock.turbine.tile.TileTurbineRotorStator;
import nc.network.PacketHandler;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.TankSorption;
import nc.util.MaterialHelper;
import nc.util.NCUtil;
import nc.util.SoundHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TurbineLogic extends MultiblockLogic<Turbine, TurbineLogic, ITurbinePart, TurbineUpdatePacket> {
	
	public TurbineLogic(Turbine turbine) {
		super(turbine);
	}
	
	public TurbineLogic(TurbineLogic oldLogic) {
		super(oldLogic);
	}
	
	@Override
	public String getID() {
		return "turbine";
	}
	
	protected Turbine getTurbine() {
		return multiblock;
	}
	
	// Multiblock Size Limits
	
	@Override
	public int getMinimumInteriorLength() {
		return NCConfig.turbine_min_size;
	}
	
	@Override
	public int getMaximumInteriorLength() {
		return NCConfig.turbine_max_size;
	}
	
	// Multiblock Methods
	
	@Override
	public void onMachineAssembled(boolean wasAssembled) {
		onTurbineFormed(wasAssembled);
	}
	
	@Override
	public void onMachineRestored() {
		onTurbineFormed(false);
	}
	
	protected void onTurbineFormed(boolean wasAssembled) {
		for (ITurbineController contr : getPartMap(ITurbineController.class).values()) {
			getTurbine().controller = contr;
		}
		setIsTurbineOn();
		
		if (!wasAssembled && !getWorld().isRemote) {
			getTurbine().energyStorage.setStorageCapacity(Turbine.BASE_MAX_ENERGY*getTurbine().getExteriorSurfaceArea());
			getTurbine().energyStorage.setMaxTransfer(Turbine.BASE_MAX_ENERGY*getTurbine().getExteriorSurfaceArea());
			getTurbine().tanks.get(0).setCapacity(Turbine.BASE_MAX_INPUT*getTurbine().getExteriorSurfaceArea());
			getTurbine().tanks.get(1).setCapacity(Turbine.BASE_MAX_OUTPUT*getTurbine().getExteriorSurfaceArea());
		}
		
		if (getTurbine().flowDir == null) return;
		
		if (getWorld().isRemote) {
			getTurbine().inputPlane[0] = getTurbine().getInteriorPlane(getTurbine().flowDir.getOpposite(), 0, 0, 0, getTurbine().bladeLength, getTurbine().shaftWidth + getTurbine().bladeLength);
			getTurbine().inputPlane[1] = getTurbine().getInteriorPlane(getTurbine().flowDir.getOpposite(), 0, getTurbine().shaftWidth + getTurbine().bladeLength, 0, 0, getTurbine().bladeLength);
			getTurbine().inputPlane[2] = getTurbine().getInteriorPlane(getTurbine().flowDir.getOpposite(), 0, getTurbine().bladeLength, getTurbine().shaftWidth + getTurbine().bladeLength, 0, 0);
			getTurbine().inputPlane[3] = getTurbine().getInteriorPlane(getTurbine().flowDir.getOpposite(), 0, 0, getTurbine().bladeLength, getTurbine().shaftWidth + getTurbine().bladeLength, 0);
		}
		
		if (!wasAssembled && !getWorld().isRemote) {
			refreshDynamoCoils();
			
			for (TileTurbineRotorShaft rotorShaft : getPartMap(TileTurbineRotorShaft.class).values()) {
				BlockPos pos = rotorShaft.getPos();
				IBlockState state = getWorld().getBlockState(pos);
				if (state.getBlock() instanceof BlockTurbineRotorShaft) {
					getWorld().setBlockState(pos, state.withProperty(TurbineRotorBladeUtil.DIR, TurbinePartDir.INVISIBLE));
				}
			}
			
			for (TileTurbineRotorBlade rotorBlade : getPartMap(TileTurbineRotorBlade.class).values()) {
				BlockPos pos = rotorBlade.bladePos();
				IBlockState state = getWorld().getBlockState(pos);
				if (state.getBlock() instanceof IBlockRotorBlade) {
					getWorld().setBlockState(pos, state.withProperty(TurbineRotorBladeUtil.DIR, TurbinePartDir.INVISIBLE));
				}
			}
			
			for (TileTurbineRotorStator rotorStator : getPartMap(TileTurbineRotorStator.class).values()) {
				BlockPos pos = rotorStator.bladePos();
				IBlockState state = getWorld().getBlockState(pos);
				if (state.getBlock() instanceof IBlockRotorBlade) {
					getWorld().setBlockState(pos, state.withProperty(TurbineRotorBladeUtil.DIR, TurbinePartDir.INVISIBLE));
				}
			}
			
			for (TileTurbineDynamoPart dynamoPart : getPartMap(TileTurbineDynamoPart.class).values()) {
				for (EnumFacing side : EnumFacing.VALUES) {
					dynamoPart.setEnergyConnection(side == getTurbine().flowDir || side == getTurbine().flowDir.getOpposite() ? EnergyConnection.OUT : EnergyConnection.NON, side);
				}
			}
			
			for (TileTurbineOutlet outlet : getPartMap(TileTurbineOutlet.class).values()) {
				for (EnumFacing side : EnumFacing.VALUES) {
					outlet.setTankSorption(side, 0, side == getTurbine().flowDir ? TankSorption.OUT : TankSorption.NON);
				}
			}
		}
	}
	
	protected void refreshDynamoCoils() {
		if (getPartMap(TileTurbineDynamoPart.class).isEmpty()) {
			getTurbine().conductivity = 0D;
			return;
		}
		
		ObjectSet<TileTurbineDynamoPart> dynamoPartRootCache = new ObjectOpenHashSet<>(), dynamoPartRootCacheOpposite = new ObjectOpenHashSet<>();
		for (TileTurbineDynamoPart dynamoPart : getPartMap(TileTurbineDynamoPart.class).values()) {
			dynamoPart.isSearched = dynamoPart.isInValidPosition = false;
			if (dynamoPart.isSearchRoot()) {
				if (dynamoPart.getPartPosition().getFacing() == getTurbine().flowDir) {
					dynamoPartRootCache.add(dynamoPart);
				}
				else {
					dynamoPartRootCacheOpposite.add(dynamoPart);
				}
			}
		}
		
		ObjectSet<TileTurbineDynamoPart> dynamoPartCache = new ObjectOpenHashSet<>(), dynamoPartCacheOpposite = new ObjectOpenHashSet<>();
		for (TileTurbineDynamoPart dynamoCoil : dynamoPartRootCache) {
			dynamoCoil.dynamoSearch(dynamoPartCache);
		}
		for (TileTurbineDynamoPart dynamoCoil : dynamoPartRootCacheOpposite) {
			dynamoCoil.dynamoSearch(dynamoPartCacheOpposite);
		}
		
		getTurbine().dynamoCoilCount = getTurbine().dynamoCoilCountOpposite = 0;
		double newConductivity = 0D, newConductivityOpposite = 0D;
		for (TileTurbineDynamoPart dynamoPart : dynamoPartCache) {
			if (dynamoPart.conductivity != null) {
				getTurbine().dynamoCoilCount++;
				newConductivity += dynamoPart.conductivity;
			}
		}
		for (TileTurbineDynamoPart dynamoPart : dynamoPartCacheOpposite) {
			if (dynamoPart.conductivity != null) {
				getTurbine().dynamoCoilCountOpposite++;
				newConductivityOpposite += dynamoPart.conductivity;
			}
		}
		
		Long2ObjectMap<TileTurbineRotorBearing> rotorBearingMap = getPartMap(TileTurbineRotorBearing.class);
		
		newConductivity = getTurbine().dynamoCoilCount == 0 ? 0D : newConductivity*Math.min(rotorBearingMap.size()/2, getTurbine().dynamoCoilCount)/getTurbine().dynamoCoilCount;
		newConductivityOpposite = getTurbine().dynamoCoilCountOpposite == 0 ? 0D : newConductivityOpposite*Math.min(rotorBearingMap.size()/2, getTurbine().dynamoCoilCountOpposite)/getTurbine().dynamoCoilCountOpposite;
		
		getTurbine().conductivity = (newConductivity + newConductivityOpposite)/rotorBearingMap.size();
	}
	
	@Override
	public void onMachinePaused() {}
	
	@Override
	public void onMachineDisassembled() {
		if (getTurbine().flowDir != null) {
			TurbinePartDir shaftDir = getTurbine().getShaftDir();
			for (TileTurbineRotorShaft rotorShaft : getPartMap(TileTurbineRotorShaft.class).values()) {
				BlockPos pos = rotorShaft.getPos();
				rotorShaft.render = false;
				IBlockState state = getWorld().getBlockState(pos);
				if (state.getBlock() instanceof BlockTurbineRotorShaft) {
					getWorld().setBlockState(pos, state.withProperty(TurbineRotorBladeUtil.DIR, shaftDir));
				}
			}
			
			for (TileTurbineRotorBlade rotorBlade : getPartMap(TileTurbineRotorBlade.class).values()) {
				BlockPos pos = rotorBlade.bladePos();
				IBlockState state = getWorld().getBlockState(pos);
				if (state.getBlock() instanceof IBlockRotorBlade) {
					getWorld().setBlockState(pos, state.withProperty(TurbineRotorBladeUtil.DIR, rotorBlade.getDir()));
				}
			}
			
			for (TileTurbineRotorStator rotorStator : getPartMap(TileTurbineRotorStator.class).values()) {
				BlockPos pos = rotorStator.bladePos();
				IBlockState state = getWorld().getBlockState(pos);
				if (state.getBlock() instanceof IBlockRotorBlade) {
					getWorld().setBlockState(pos, state.withProperty(TurbineRotorBladeUtil.DIR, rotorStator.getDir()));
				}
			}
		}
		
		getTurbine().isTurbineOn = getTurbine().isProcessing = false;
		if (getTurbine().controller != null) getTurbine().controller.updateBlockState(false);
		getTurbine().power = getTurbine().rawPower = getTurbine().rawLimitPower = getTurbine().rawMaxPower = getTurbine().conductivity = 0D;
		getTurbine().angVel = getTurbine().rotorAngle = 0F;
		getTurbine().flowDir = null;
		getTurbine().shaftWidth = getTurbine().inertia = getTurbine().bladeLength = getTurbine().noBladeSets = getTurbine().recipeInputRate = 0;
		getTurbine().totalExpansionLevel = getTurbine().idealTotalExpansionLevel = getTurbine().maxBladeExpansionCoefficient = 1D;
		getTurbine().basePowerPerMB = getTurbine().recipeInputRateFP = 0D;
		getTurbine().expansionLevels = new DoubleArrayList();
		getTurbine().rawBladeEfficiencies = new DoubleArrayList();
		getTurbine().inputPlane[0] = getTurbine().inputPlane[1] = getTurbine().inputPlane[2] = getTurbine().inputPlane[3] = null;
		
		if (getWorld().isRemote) {
			updateSounds();
		}
	}
	
	@Override
	public boolean isMachineWhole(Multiblock multiblock) {
		int minX = getTurbine().getMinX(), minY = getTurbine().getMinY(), minZ = getTurbine().getMinZ();
		int maxX = getTurbine().getMaxX(), maxY = getTurbine().getMaxY(), maxZ = getTurbine().getMaxZ();
		
		// Bearings -> flow axis
		
		boolean dirMinX = false, dirMaxX = false, dirMinY = false, dirMaxY = false, dirMinZ = false, dirMaxZ = false;
		Axis axis = null;
		boolean tooManyAxes = false; // Is any of the bearings in more than a single axis?
		boolean notInAWall = false; // Is the bearing somewhere else in the structure other than the wall?
		
		for (TileTurbineRotorBearing rotorBearing : getPartMap(TileTurbineRotorBearing.class).values()) {
			BlockPos pos = rotorBearing.getPos();
			
			if (pos.getX() == minX) dirMinX = true;
			else if (pos.getX() == maxX) dirMaxX = true;
			else if (pos.getY() == minY) dirMinY = true;
			else if (pos.getY() == maxY) dirMaxY = true;
			else if (pos.getZ() == minZ) dirMinZ = true;
			else if (pos.getZ() == maxZ) dirMaxZ = true;
			else notInAWall = true; // If the bearing is not at any of those positions, that means our bearing isn't part of the wall at all
		}
		
		if (dirMinX && dirMaxX) {
			axis = Axis.X;
		}
		if (dirMinY && dirMaxY) {
			if (axis != null) tooManyAxes = true;
			else axis = Axis.Y;
		}
		if (dirMinZ && dirMaxZ) {
			if (axis != null) tooManyAxes = true;
			else axis = Axis.Z;
		}
		
		if (axis == null) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.need_bearings", null);
			return false;
		}
		
		if ((axis == Axis.X && getTurbine().getInteriorLengthY() != getTurbine().getInteriorLengthZ()) ||
			(axis == Axis.Y && getTurbine().getInteriorLengthZ() != getTurbine().getInteriorLengthX()) ||
			(axis == Axis.Z && getTurbine().getInteriorLengthX() != getTurbine().getInteriorLengthY()) ||
			tooManyAxes || notInAWall) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.bearings_side_square", null);
			return false;
		}
		
		// At this point, all bearings are guaranteed to be part of walls in the same axis
		// Also, it can only ever succeed up to this point if we already have at least two bearings, so no need to check for that
		
		int internalDiameter;
		if (axis == Axis.X) internalDiameter = getTurbine().getInteriorLengthY();
		else if (axis == Axis.Y) internalDiameter = getTurbine().getInteriorLengthZ();
		else internalDiameter = getTurbine().getInteriorLengthX();
		boolean isEvenDiameter = (internalDiameter & 1) == 0;
		boolean validAmountOfBearings = false;
		
		for (getTurbine().shaftWidth = isEvenDiameter? 2 : 1; getTurbine().shaftWidth <= internalDiameter - 2; getTurbine().shaftWidth += 2) {
			if (getPartMap(TileTurbineRotorBearing.class).size() == 2*getTurbine().shaftWidth*getTurbine().shaftWidth) {
				validAmountOfBearings = true;
				break;
			}
		}
		
		if (!validAmountOfBearings) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.bearings_centre_and_square", null);
			return false;
		}
		
		// Last thing that needs to be checked concerning bearings is whether they are grouped correctly at the centre of their respective walls
		
		getTurbine().bladeLength = (internalDiameter - getTurbine().shaftWidth)/2;
		
		for (BlockPos pos : getTurbine().getInteriorPlane(EnumFacing.getFacingFromAxis(AxisDirection.NEGATIVE, axis), -1, getTurbine().bladeLength, getTurbine().bladeLength, getTurbine().bladeLength, getTurbine().bladeLength)) {
			if (getWorld().getTileEntity(pos) instanceof TileTurbineRotorBearing) continue;
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.bearings_centre_and_square", pos);
			return false;
		}
		
		for (BlockPos pos : getTurbine().getInteriorPlane(EnumFacing.getFacingFromAxis(AxisDirection.POSITIVE, axis), -1, getTurbine().bladeLength, getTurbine().bladeLength, getTurbine().bladeLength, getTurbine().bladeLength)) {
			if (getWorld().getTileEntity(pos) instanceof TileTurbineRotorBearing) continue;
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.bearings_centre_and_square", pos);
			return false;
		}
		
		// All bearings should be valid by now!
		
		// Inlets/outlets -> getTurbine().flowDir
		
		getTurbine().flowDir = null;
		
		if (getPartMap(TileTurbineInlet.class).size() == 0 || getPartMap(TileTurbineOutlet.class).size() == 0) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", null);
			return false;
		}
		
		for (TileTurbineInlet inlet : getPartMap(TileTurbineInlet.class).values()) {
			BlockPos pos = inlet.getPos();
			
			if (getTurbine().isInMinWall(axis, pos)) {
				EnumFacing thisFlowDir = EnumFacing.getFacingFromAxis(AxisDirection.POSITIVE, axis);
				if (getTurbine().flowDir != null && getTurbine().flowDir != thisFlowDir) { //make sure that all inlets are in the same wall
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
					return false;
				} else getTurbine().flowDir = thisFlowDir;
			} else if (getTurbine().isInMaxWall(axis, pos)) {
				EnumFacing thisFlowDir = EnumFacing.getFacingFromAxis(AxisDirection.NEGATIVE, axis);
				if (getTurbine().flowDir != null && getTurbine().flowDir != thisFlowDir) { //make sure that all inlets are in the same wall
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
					return false;
				} else getTurbine().flowDir = thisFlowDir;
			}
			else {
				multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
				return false;
			}
		}
		
		if (getTurbine().flowDir == null) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", null);
			return false;
		}
		
		for (TileTurbineOutlet outlet : getPartMap(TileTurbineOutlet.class).values()) {
			BlockPos pos = outlet.getPos();
			
			if (!getTurbine().isInWall(getTurbine().flowDir, pos)) {
				multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
				return false;
			}
		}
		
		// Shaft
		
		int flowLength = getTurbine().getFlowLength();
		
		for (int slice = 0; slice < flowLength; slice++) {
			for (BlockPos pos : getTurbine().getInteriorPlane(EnumFacing.getFacingFromAxis(AxisDirection.POSITIVE, axis), slice, getTurbine().bladeLength, getTurbine().bladeLength, getTurbine().bladeLength, getTurbine().bladeLength)) {
				TileEntity tile = getWorld().getTileEntity(pos);
				if (tile instanceof TileTurbineRotorShaft) {
					((TileTurbineRotorShaft)tile).render = true;
					((TileTurbineRotorShaft)tile).depth = slice;
					continue;
				}
				multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.shaft_centre", pos);
				return false;
			}
		}
		
		// Interior
		
		getTurbine().inertia = getTurbine().shaftWidth*(getTurbine().shaftWidth + 4*getTurbine().bladeLength)*flowLength;
		getTurbine().noBladeSets = 0;
		
		getTurbine().totalExpansionLevel = 1D;
		getTurbine().expansionLevels = new DoubleArrayList();
		getTurbine().rawBladeEfficiencies = new DoubleArrayList();
		
		for (int depth = 0; depth < flowLength; depth++) {
			
			// Free space
			
			for (BlockPos pos : getTurbine().getInteriorPlane(getTurbine().flowDir, depth, 0, 0, getTurbine().shaftWidth + getTurbine().bladeLength, getTurbine().shaftWidth + getTurbine().bladeLength)) {
				if (!MaterialHelper.isReplaceable(getWorld().getBlockState(pos).getMaterial())) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.space_between_blades", pos);
					return false;
				}
				getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			
			for (BlockPos pos : getTurbine().getInteriorPlane(getTurbine().flowDir, depth, getTurbine().shaftWidth + getTurbine().bladeLength, 0, 0, getTurbine().shaftWidth + getTurbine().bladeLength)) {
				if (!MaterialHelper.isReplaceable(getWorld().getBlockState(pos).getMaterial())) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.space_between_blades", pos);
					return false;
				}
				getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			
			for (BlockPos pos : getTurbine().getInteriorPlane(getTurbine().flowDir, depth, 0, getTurbine().shaftWidth + getTurbine().bladeLength, getTurbine().shaftWidth + getTurbine().bladeLength, 0)) {
				if (!MaterialHelper.isReplaceable(getWorld().getBlockState(pos).getMaterial())) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.space_between_blades", pos);
					return false;
				}
				getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			
			for (BlockPos pos : getTurbine().getInteriorPlane(getTurbine().flowDir, depth, getTurbine().shaftWidth + getTurbine().bladeLength, getTurbine().shaftWidth + getTurbine().bladeLength, 0, 0)) {
				if (!MaterialHelper.isReplaceable(getWorld().getBlockState(pos).getMaterial())) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.space_between_blades", pos);
					return false;
				}
				getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			
			// Blades/stators
			
			IRotorBladeType currentBladeType = null;
			
			for (BlockPos pos : getTurbine().getInteriorPlane(getTurbine().flowDir.getOpposite(), depth, getTurbine().bladeLength, 0, getTurbine().bladeLength, getTurbine().shaftWidth + getTurbine().bladeLength)) {
				ITurbineRotorBlade thisBlade = getTurbine().getBlade(pos);
				IRotorBladeType thisBladeType = thisBlade == null ? null : thisBlade.getBladeType();
				if (thisBladeType == null) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", pos);
					return false;
				} else if (currentBladeType == null) {
					currentBladeType = thisBladeType;
				} else if (currentBladeType != thisBladeType) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.different_type_blades", pos);
					return false;
				}
				thisBlade.setDir(getTurbine().getBladeDir(PlaneDir.V));
				thisBlade.setDepth(depth);
				thisBlade.setRenderRotation(45F);
			}
			
			for (BlockPos pos : getTurbine().getInteriorPlane(getTurbine().flowDir.getOpposite(), depth, 0, getTurbine().bladeLength, getTurbine().shaftWidth + getTurbine().bladeLength, getTurbine().bladeLength)) {
				ITurbineRotorBlade thisBlade = getTurbine().getBlade(pos);
				IRotorBladeType thisBladeType = thisBlade == null ? null : thisBlade.getBladeType();
				if (thisBladeType == null) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", pos);
					return false;
				} else if (currentBladeType != thisBladeType) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.different_type_blades", pos);
					return false;
				}
				thisBlade.setDir(getTurbine().getBladeDir(PlaneDir.U));
				thisBlade.setDepth(depth);
				thisBlade.setRenderRotation(getTurbine().flowDir.getAxis() == Axis.Z ? -45F : 45F);
			}
			
			for (BlockPos pos : getTurbine().getInteriorPlane(getTurbine().flowDir.getOpposite(), depth, getTurbine().shaftWidth + getTurbine().bladeLength, getTurbine().bladeLength, 0, getTurbine().bladeLength)) {
				ITurbineRotorBlade thisBlade = getTurbine().getBlade(pos);
				IRotorBladeType thisBladeType = thisBlade == null ? null : thisBlade.getBladeType();
				if (thisBladeType == null) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", pos);
					return false;
				} else if (currentBladeType != thisBladeType) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.different_type_blades", pos);
					return false;
				}
				thisBlade.setDir(getTurbine().getBladeDir(PlaneDir.U));
				thisBlade.setDepth(depth);
				thisBlade.setRenderRotation(getTurbine().flowDir.getAxis() == Axis.Z ? 45F : -45F);
			}
			
			for (BlockPos pos : getTurbine().getInteriorPlane(getTurbine().flowDir.getOpposite(), depth, getTurbine().bladeLength, getTurbine().shaftWidth + getTurbine().bladeLength, getTurbine().bladeLength, 0)) {
				ITurbineRotorBlade thisBlade = getTurbine().getBlade(pos);
				IRotorBladeType thisBladeType = thisBlade == null ? null : thisBlade.getBladeType();
				if (thisBladeType == null) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", pos);
					return false;
				} else if (currentBladeType != thisBladeType) {
					multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.different_type_blades", pos);
					return false;
				}
				thisBlade.setDir(getTurbine().getBladeDir(PlaneDir.V));
				thisBlade.setDepth(depth);
				thisBlade.setRenderRotation(-45F);
			}
			
			if (currentBladeType == null) {
				multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", null);
				return false;
			}
			
			double prevExpansionLevel = getTurbine().totalExpansionLevel;
			if (currentBladeType == TurbineRotorStatorType.STATOR) {
				getTurbine().totalExpansionLevel *= NCConfig.turbine_stator_expansion;
				getTurbine().expansionLevels.add((prevExpansionLevel + getTurbine().totalExpansionLevel)/2D);
				getTurbine().rawBladeEfficiencies.add(0D);
			} else if (currentBladeType instanceof TurbineRotorBladeType) {
				getTurbine().totalExpansionLevel *= ((TurbineRotorBladeType)currentBladeType).getExpansionCoefficient();
				getTurbine().expansionLevels.add((prevExpansionLevel + getTurbine().totalExpansionLevel)/2D);
				getTurbine().rawBladeEfficiencies.add(((TurbineRotorBladeType)currentBladeType).getEfficiency());
				getTurbine().noBladeSets++;
				getTurbine().maxBladeExpansionCoefficient = Math.max(((TurbineRotorBladeType)currentBladeType).getExpansionCoefficient(), getTurbine().maxBladeExpansionCoefficient);
			}
		}
		
		if (!NCUtil.areEqual(getTurbine().getFlowLength(), getTurbine().expansionLevels.size(), getTurbine().rawBladeEfficiencies.size())) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", null);
			return false;
		}
		
		for (ITurbineController controller : getPartMap(ITurbineController.class).values()) {
			controller.setIsRenderer(false);
		}
		for (ITurbineController controller : getPartMap(ITurbineController.class).values()) {
			controller.setIsRenderer(true);
			break;
		}
		
		if (getTurbine().shaftWidth > 2) for (int slice = 0; slice < flowLength; slice++) {
			for (BlockPos pos : getTurbine().getInteriorPlane(getTurbine().flowDir, slice, getTurbine().bladeLength + 1, getTurbine().bladeLength + 1, getTurbine().bladeLength + 1, getTurbine().bladeLength + 1)) {
				TileEntity tile = getWorld().getTileEntity(pos);
				if (tile instanceof TileTurbineRotorShaft) {
					((TileTurbineRotorShaft)tile).render = false;
				}
			}
		}
		
		return true;
	}
	
	@Override
	public List<Pair<Class<? extends ITurbinePart>, String>> getPartBlacklist() {
		return new ArrayList<>();
	}
	
	public void onAssimilate(Multiblock assimilated) {
		if (!(assimilated instanceof Turbine)) return;
		Turbine assimilatedTurbine = (Turbine) assimilated;
		getTurbine().energyStorage.mergeEnergyStorage(assimilatedTurbine.energyStorage);
		onTurbineFormed(false);
	}
	
	public void onAssimilated(Multiblock assimilator) {}
	
	// Server
	
	@Override
	public boolean onUpdateServer() {
		boolean flag = false, wasProcessing = getTurbine().isProcessing;
		refreshRecipe();
		double previousRawPower = getTurbine().rawPower, previousRawLimitPower = getTurbine().rawLimitPower, previousRawMaxPower = getTurbine().rawMaxPower;
		getTurbine().rawLimitPower = getRawLimitProcessPower(getTurbine().recipeInputRate);
		getTurbine().rawMaxPower = getRawLimitProcessPower(getMaxRecipeRateMultiplier());
		if (canProcessInputs()) {
			getTurbine().isProcessing = true;
			produceProducts();
			getTurbine().rawPower = getNewRawProcessPower(previousRawPower, getTurbine().rawLimitPower, true);
		}
		else {
			getTurbine().isProcessing = false;
			getTurbine().rawMaxPower = previousRawMaxPower;
			getTurbine().rawPower = getNewRawProcessPower(previousRawPower, previousRawLimitPower, false);
		}
		getTurbine().power = getTurbine().rawPower*getTurbine().conductivity;
		getTurbine().angVel = getTurbine().rawMaxPower == 0D ? 0F : (float) (getTurbine().rawPower/getTurbine().rawMaxPower);
		if (wasProcessing != getTurbine().isProcessing) {
			if (getTurbine().controller != null) {
				getTurbine().sendUpdateToAllPlayers();
			}
			flag = true;
		}
		
		double tensionFactor = (getTurbine().recipeInputRate - getMaxRecipeRateMultiplier())/getMaxRecipeRateMultiplier();
		tensionFactor /= Math.max(1D, tensionFactor > 0D ? NCConfig.turbine_tension_throughput_factor - 1D : 1D);
		getTurbine().bearingTension = Math.max(0D, getTurbine().bearingTension + Math.min(1D, tensionFactor)/(1200D*getPartMap(TileTurbineRotorBearing.class).size()));
		if (getTurbine().bearingTension > 1D) {
			Iterator<TileTurbineRotorBearing> rotorBearingIterator = getPartMap(TileTurbineRotorBearing.class).values().iterator();
			while (rotorBearingIterator.hasNext()) {
				TileTurbineRotorBearing rotorBearing = rotorBearingIterator.next();
				rotorBearingIterator.remove();
				BlockPos pos = rotorBearing.getPos();
				getWorld().removeTileEntity(pos);
				getWorld().createExplosion(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 4F, true);
				getWorld().setBlockToAir(pos);
			}
			getTurbine().bearingTension = 0D;
			getTurbine().checkIfMachineIsWhole();
			return true;
		}
		
		getTurbine().energyStorage.changeEnergyStored((long)getTurbine().power);
		
		if (getTurbine().controller != null) {
			PacketHandler.instance.sendToAll(getRenderPacket());
			getTurbine().sendUpdateToListeningPlayers();
		}
		
		return flag;
	}
	
	public void setIsTurbineOn() {
		boolean oldIsTurbineOn = getTurbine().isTurbineOn;
		getTurbine().isTurbineOn = (isRedstonePowered()|| getTurbine().computerActivated) && getTurbine().isAssembled();
		if (getTurbine().isTurbineOn != oldIsTurbineOn) {
			if (getTurbine().controller != null) {
				getTurbine().controller.updateBlockState(getTurbine().isTurbineOn);
				getTurbine().sendUpdateToAllPlayers();
			}
		}
	}
	
	protected boolean isRedstonePowered() {
		if (getTurbine().controller != null && getTurbine().controller.checkIsRedstonePowered(getWorld(), getTurbine().controller.getTilePos())) return true;
		return false;
	}
	
	protected void refreshRecipe() {
		getTurbine().recipeInfo = turbine.getRecipeInfoFromInputs(new ArrayList<>(), getTurbine().tanks.subList(0, 1));
	}
	
	protected boolean canProcessInputs() {
		if (!setRecipeStats()) return false;
		return canProduceProducts();
	}
	
	protected boolean setRecipeStats() {
		if (getTurbine().recipeInfo == null || !getTurbine().isTurbineOn) {
			getTurbine().recipeInputRate = 0;
			getTurbine().basePowerPerMB = getTurbine().recipeInputRateFP = 0D;
			//getTurbine().idealTotalExpansionLevel = 1D;
			getTurbine().particleEffect = "cloud";
			getTurbine().particleSpeedMult = 1D/23.2D;
			return false;
		}
		getTurbine().basePowerPerMB = getTurbine().recipeInfo.getRecipe().getTurbinePowerPerMB();
		getTurbine().idealTotalExpansionLevel = getTurbine().recipeInfo.getRecipe().getTurbineExpansionLevel();
		getTurbine().particleEffect = getTurbine().recipeInfo.getRecipe().getTurbineParticleEffect();
		getTurbine().particleSpeedMult = getTurbine().recipeInfo.getRecipe().getTurbineParticleSpeedMultiplier();
		return true;
	}
	
	protected boolean canProduceProducts() {
		IFluidIngredient fluidProduct = getTurbine().recipeInfo.getRecipe().getFluidProducts().get(0);
		if (fluidProduct.getMaxStackSize(0) <= 0 || fluidProduct.getStack() == null) return false;
		
		int recipeInputRateDiff = getTurbine().recipeInputRate;
		getTurbine().recipeInputRate = Math.min(getTurbine().tanks.get(0).getFluidAmount(), (int)(NCConfig.turbine_tension_throughput_factor*getMaxRecipeRateMultiplier()));
		recipeInputRateDiff = Math.abs(recipeInputRateDiff - getTurbine().recipeInputRate);
		
		double roundingFactor = Math.max(0D, Math.E*Math.log1p(getTurbine().recipeInputRate/(1 + recipeInputRateDiff)));
		getTurbine().recipeInputRateFP = (roundingFactor*getTurbine().recipeInputRateFP + getTurbine().recipeInputRate)/(1D + roundingFactor);
		
		if (!getTurbine().tanks.get(1).isEmpty()) {
			if (!getTurbine().tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack())) {
				return false;
			} else if (getTurbine().tanks.get(1).getFluidAmount() + fluidProduct.getMaxStackSize(0)*getTurbine().recipeInputRate > getTurbine().tanks.get(1).getCapacity()) {
				return false;
			}
		}
		return true;
	}
	
	protected void produceProducts() {
		int fluidIngredientStackSize = getTurbine().recipeInfo.getRecipe().getFluidIngredients().get(0).getMaxStackSize(getTurbine().recipeInfo.getFluidIngredientNumbers().get(0))*getTurbine().recipeInputRate;
		if (fluidIngredientStackSize > 0) getTurbine().tanks.get(0).changeFluidAmount(-fluidIngredientStackSize);
		if (getTurbine().tanks.get(0).getFluidAmount() <= 0) getTurbine().tanks.get(0).setFluidStored(null);
		
		IFluidIngredient fluidProduct = getTurbine().recipeInfo.getRecipe().getFluidProducts().get(0);
		if (fluidProduct.getMaxStackSize(0) <= 0) return;
		if (getTurbine().tanks.get(1).isEmpty()) {
			getTurbine().tanks.get(1).setFluidStored(fluidProduct.getNextStack(0));
			getTurbine().tanks.get(1).setFluidAmount(getTurbine().tanks.get(1).getFluidAmount()*getTurbine().recipeInputRate);
		} else if (getTurbine().tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack())) {
			getTurbine().tanks.get(1).changeFluidAmount(fluidProduct.getNextStackSize(0)*getTurbine().recipeInputRate);
		}
	}
	
	public int getMaxRecipeRateMultiplier() {
		return getTurbine().getBladeVolume()*NCConfig.turbine_mb_per_blade;
	}
	
	public double getNewRawProcessPower(double previousRawPower, double maxLimitPower, boolean increasing) {
		double absoluteLeniency = getTurbine().getBladeArea()*getThroughputLeniencyMult()*NCConfig.turbine_mb_per_blade;
		double throughputEfficiency = getMaxRecipeRateMultiplier() == 0 ? 1D : Math.min(1D, (getTurbine().recipeInputRateFP + absoluteLeniency)/getMaxRecipeRateMultiplier());
		
		if (increasing) {
			return throughputEfficiency*(getTurbine().inertia*previousRawPower + maxLimitPower)/(getTurbine().inertia + 1D);
		}
		else {
			return throughputEfficiency*(getTurbine().inertia*previousRawPower)/(getTurbine().inertia + 2D);
		}
	}
	
	protected double getThroughputLeniencyMult() {
		return Math.max(NCConfig.turbine_throughput_efficiency_leniency, getTurbine().idealTotalExpansionLevel <= 1D || getTurbine().maxBladeExpansionCoefficient <= 1 ? Double.MAX_VALUE : Math.ceil(Math.log(getTurbine().idealTotalExpansionLevel)/Math.log(getTurbine().maxBladeExpansionCoefficient)));
	}
	
	public double getRawLimitProcessPower(int recipeInputRate) {
		if (getTurbine().noBladeSets == 0) return 0D;
		
		double bladeMultiplier = 0D;
		
		for (int depth = 0; depth < getTurbine().getFlowLength(); depth++) {
			if (getTurbine().rawBladeEfficiencies.get(depth) <= 0D) continue;
			bladeMultiplier += getTurbine().rawBladeEfficiencies.get(depth)*getExpansionIdealityMultiplier(getIdealExpansionLevel(depth), getTurbine().expansionLevels.get(depth));
		}
		bladeMultiplier /= getTurbine().noBladeSets;
		
		return bladeMultiplier*getExpansionIdealityMultiplier(getTurbine().idealTotalExpansionLevel, getTurbine().totalExpansionLevel)*getInputRateEfficiencyBonus(recipeInputRate)*recipeInputRate*getTurbine().basePowerPerMB;
	}
	
	public static double getExpansionIdealityMultiplier(double ideal, double actual) {
		if (ideal <= 0D || actual <= 0D) return 0D;
		return ideal < actual ? ideal/actual : actual/ideal;
	}
	
	public double getIdealExpansionLevel(int depth) {
		return Math.pow(getTurbine().idealTotalExpansionLevel, (depth + 0.5D)/getTurbine().getFlowLength());
	}
	
	private static final double Z = Math.pow(Math.E, Math.E - 1D) - 1D;
	
	public double getInputRateEfficiencyBonus(int recipeInputRate) {
		return Math.log1p(Math.log1p(Z*recipeInputRate/NCConfig.turbine_mb_per_blade));
	}
	
	public DoubleList getIdealExpansionLevels() {
		DoubleList levels = new DoubleArrayList();
		if (getTurbine().flowDir == null) {
			return levels;
		}
		for (int depth = 0; depth < getTurbine().getFlowLength(); depth++) {
			levels.add(getIdealExpansionLevel(depth));
		}
		return levels;
	}
	
	// Client
	
	@Override
	public void onUpdateClient() {
		updateParticles();
		updateSounds();
	}
	
	@SideOnly(Side.CLIENT)
	protected void updateParticles() {
		if (getTurbine().isProcessing && getTurbine().isAssembled() && !Minecraft.getMinecraft().isGamePaused()) {
			double flowSpeed = getTurbine().getFlowLength()*getTurbine().particleSpeedMult; // Particles will just reach the outlets at this speed
			double offsetX = particleSpeedOffest(), offsetY = particleSpeedOffest(), offsetZ = particleSpeedOffest();
			
			double speedX = getTurbine().flowDir == EnumFacing.WEST ? -flowSpeed : (getTurbine().flowDir == EnumFacing.EAST ? flowSpeed : offsetX);
			double speedY = getTurbine().flowDir == EnumFacing.DOWN ? -flowSpeed : (getTurbine().flowDir == EnumFacing.UP ? flowSpeed : offsetY);
			double speedZ = getTurbine().flowDir == EnumFacing.NORTH ? -flowSpeed : (getTurbine().flowDir == EnumFacing.SOUTH ? flowSpeed : offsetZ);
			
			for (Iterable<MutableBlockPos> iter : getTurbine().inputPlane) {
				if (iter != null) {
					for (BlockPos pos : iter) {
						if (rand.nextDouble() < 0.05D*getTurbine().recipeInputRateFP/getMaxRecipeRateMultiplier()) {
							double[] spawnPos = particleSpawnPos(pos);
							if (spawnPos != null) getWorld().spawnParticle(EnumParticleTypes.getByName(getTurbine().particleEffect), false, spawnPos[0], spawnPos[1], spawnPos[2], speedX, speedY, speedZ);
						}
					}
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected double particleSpeedOffest() {
		return (rand.nextDouble() - 0.5D)/(4D*Math.sqrt(getTurbine().getFlowLength()));
	}
	
	@SideOnly(Side.CLIENT)
	protected double[] particleSpawnPos(BlockPos pos) {
		double offsetU = 0.5D + (rand.nextDouble() - 0.5D)/2D;
		double offsetV = 0.5D + (rand.nextDouble() - 0.5D)/2D;
		switch (getTurbine().flowDir) {
		case DOWN:
			return new double[] {pos.getX() + offsetV, pos.getY() + 1D, pos.getZ() + offsetU};
		case UP:
			return new double[] {pos.getX() + offsetV, pos.getY(), pos.getZ() + offsetU};
		case NORTH:
			return new double[] {pos.getX() + offsetU, pos.getY() + offsetV, pos.getZ() + 1D};
		case SOUTH:
			return new double[] {pos.getX() + offsetU, pos.getY() + offsetV, pos.getZ()};
		case WEST:
			return new double[] {pos.getX() + 1D, pos.getY() + offsetU, pos.getZ() + offsetV};
		case EAST:
			return new double[] {pos.getX(), pos.getY() + offsetU, pos.getZ() + offsetV};
		default:
			return new double[] {pos.getX(), pos.getY(), pos.getZ()};
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected void updateSounds() {
		if (NCConfig.turbine_sound_volume == 0D) {
			if (getTurbine().activeSounds != null) {
				stopSounds();
				getTurbine().activeSounds.clear();
				getTurbine().activeSounds = null;
			}
			return;
		}
		
		if (getTurbine().activeSounds == null) {
			getTurbine().activeSounds = new ArrayList<>();
		}
		
		if (getTurbine().isProcessing && getTurbine().isAssembled()) {
			getTurbine().refreshSoundInfo = getTurbine().refreshSoundInfo || Math.abs(getTurbine().angVel - getTurbine().prevAngVel) > 0.025F;
			
			if (--getTurbine().soundCount > (getTurbine().refreshSoundInfo ? 186/2 : 0)) {
				return;
			}
			
			// Generate sound info if necessary
			if (getTurbine().refreshSoundInfo) {
				stopSounds();
				getTurbine().activeSounds.clear();
				final int _x = 1 + (getTurbine().getExteriorLengthX()/8), _y = 1 + (getTurbine().getExteriorLengthY()/8), _z = 1 + (getTurbine().getExteriorLengthZ()/8);
				final int[] xList = new int[_x], yList = new int[_y], zList = new int[_z];
				for (int i = 0; i < _x; i++) {
					xList[i] = getTurbine().getMinimumCoord().getX() + (i + 1)*getTurbine().getExteriorLengthX()/(_x + 1);
				}
				for (int j = 0; j < _y; j++) {
					yList[j] = getTurbine().getMinimumCoord().getY() + (j + 1)*getTurbine().getExteriorLengthY()/(_y + 1);
				}
				for (int k = 0; k < _z; k++) {
					zList[k] = getTurbine().getMinimumCoord().getZ() + (k + 1)*getTurbine().getExteriorLengthZ()/(_z + 1);
				}
				for (int i = 0; i < _x; i++) {
					for (int j = 0; j < _y; j++) {
						for (int k = 0; k < _z; k++) {
							if (i == 0 || i == _x - 1 || j == 0 || j == _y - 1 || k == 0 || k == _z - 1) {
								getTurbine().activeSounds.add(new SoundInfo(null, new BlockPos(xList[i], yList[j], zList[k])));
							}
						}
					}
				}
				getTurbine().refreshSoundInfo = false;
			}
			
			// If this machine isn't playing sounds, go ahead and play them
			for (SoundInfo activeSound : getTurbine().activeSounds) {
				if (activeSound != null && (activeSound.sound == null || !Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(activeSound.sound))) {
					activeSound.sound = SoundHandler.startTileSound(NCSounds.turbine_run, activeSound.pos, (float) ((0.125D + getTurbine().angVel*0.25D)*NCConfig.turbine_sound_volume), SoundHelper.getPitch(4F*getTurbine().angVel - 2F));
				}
			}
			
			// Always reset the count
			getTurbine().soundCount = 186;
			
			getTurbine().prevAngVel = getTurbine().angVel;
		}
		else {
			stopSounds();
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected void stopSounds() {
		if (getTurbine().activeSounds == null) {
			return;
		}
		for (SoundInfo activeSound : getTurbine().activeSounds) {
			if (activeSound != null) {
				SoundHandler.stopTileSound(activeSound.pos);
				activeSound.sound = null;
			}
		}
		getTurbine().soundCount = 0;
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
	public TurbineUpdatePacket getUpdatePacket() {
		return new TurbineUpdatePacket(getTurbine().controller.getTilePos(), getTurbine().isTurbineOn, getTurbine().energyStorage, getTurbine().power, getTurbine().rawPower, getTurbine().conductivity, getTurbine().totalExpansionLevel, getTurbine().idealTotalExpansionLevel, getTurbine().shaftWidth, getTurbine().bladeLength, getTurbine().noBladeSets, getTurbine().dynamoCoilCount, getTurbine().dynamoCoilCountOpposite, getTurbine().bearingTension);
	}
	
	@Override
	public void onPacket(TurbineUpdatePacket message) {
		
	}
	
	public TurbineRenderPacket getRenderPacket() {
		return new TurbineRenderPacket(getTurbine().controller.getTilePos(), getTurbine().particleEffect, getTurbine().particleSpeedMult, getTurbine().angVel, getTurbine().isProcessing, getTurbine().recipeInputRate, getTurbine().recipeInputRateFP);
	}
	
	public void onRenderPacket(TurbineRenderPacket message) {
		
	}
	
	public ContainerTurbineController getContainer(EntityPlayer player) {
		return new ContainerTurbineController(player, (TileTurbineController)getTurbine().controller);
	}
	
	public void clearAllMaterial() {}
	
	// Multiblock Validators
	
	@Override
	public boolean isBlockGoodForInterior(World world, int x, int y, int z, Multiblock multiblock) {
		BlockPos pos = new BlockPos(x, y, z);
		if (MaterialHelper.isReplaceable(world.getBlockState(pos).getMaterial()) || world.getTileEntity(pos) instanceof TileTurbinePart) return true;
		else return getTurbine().standardLastError(x, y, z, multiblock);
	}
}
