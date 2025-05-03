package nc.multiblock.machine;

import it.unimi.dsi.fastutil.ints.*;
import nc.config.NCConfig;
import nc.handler.SoundHandler;
import nc.init.NCSounds;
import nc.network.multiblock.*;
import nc.recipe.*;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.internal.fluid.Tank.TankInfo;
import nc.tile.machine.*;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.util.NCMath;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

import static nc.config.NCConfig.machine_distiller_sound_volume;

public class DistillerLogic extends MachineLogic {
	
	public final IntList trayLevels = new IntArrayList();
	
	public double refluxUnitFraction, reboilingUnitFraction, liquidDistributorFraction;
	public double refluxUnitBonus, reboilingUnitBonus, liquidDistributorBonus;
	
	public DistillerLogic(Machine machine) {
		super(machine);
	}
	
	public DistillerLogic(MachineLogic oldLogic) {
		super(oldLogic);
	}
	
	@Override
	public String getID() {
		return "distiller";
	}
	
	@Override
	public BasicRecipeHandler getRecipeHandler() {
		return NCRecipes.multiblock_distiller;
	}
	
	@Override
	public double defaultProcessTime() {
		return NCConfig.machine_distiller_time;
	}
	
	@Override
	public double defaultProcessPower() {
		return NCConfig.machine_distiller_power;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		
		if (getWorld().isRemote) {
			clearSounds();
		}
	}
	
	@Override
	public boolean isMachineWhole() {
		if (!super.isMachineWhole() || !multiblock.hasPlanarSymmetry(Axis.Y)) {
			return false;
		}
		
		baseSpeedMultiplier = 0D;
		basePowerMultiplier = 0D;
		
		trayLevels.clear();
		
		refluxUnitFraction = reboilingUnitFraction = liquidDistributorFraction = 0D;
		
		int area = multiblock.getInteriorLengthX() * multiblock.getInteriorLengthZ();
		if (area <= 0) {
			return true;
		}
		
		int minY = multiblock.getMinY(), maxY = multiblock.getMaxY();
		
		Collection<TileDistillerRefluxUnit> refluxUnits = getParts(TileDistillerRefluxUnit.class);
		for (TileDistillerRefluxUnit refluxUnit : refluxUnits) {
			BlockPos pos = refluxUnit.getPos();
			if (pos.getY() != maxY) {
				if (multiblock.getLastError() == null) {
					multiblock.setLastError("nuclearcraft.multiblock_validation.distiller.invalid_reflux_unit", pos);
				}
				return false;
			}
		}
		
		Collection<TileDistillerReboilingUnit> reboilingUnits = getParts(TileDistillerReboilingUnit.class);
		for (TileDistillerReboilingUnit reboilingUnit : reboilingUnits) {
			BlockPos pos = reboilingUnit.getPos();
			if (pos.getY() != minY) {
				if (multiblock.getLastError() == null) {
					multiblock.setLastError("nuclearcraft.multiblock_validation.distiller.invalid_reboiling_unit", pos);
				}
				return false;
			}
		}
		
		Collection<TileDistillerLiquidDistributor> liquidDistributors = getParts(TileDistillerLiquidDistributor.class);
		for (TileDistillerLiquidDistributor liquidDistributor : liquidDistributors) {
			BlockPos pos = liquidDistributor.getPos();
			if (pos.getY() != maxY) {
				if (multiblock.getLastError() == null) {
					multiblock.setLastError("nuclearcraft.multiblock_validation.distiller.invalid_liquid_distributor", pos);
				}
				return false;
			}
		}
		
		BlockPos corner = multiblock.getExtremeInteriorCoord(false, false, false);
		IntSet traySieveCache = new IntOpenHashSet();
		
		double traySieveEfficiency = 0D, packedSieveEfficiency = 0D;
		int traySieveCount = 0, packedSieveCount = 0;
		
		for (int i = 0, len = multiblock.getInteriorLengthY(); i < len; ++i) {
			BlockPos pos = corner.up(i);
			BasicRecipe blockRecipe;
			if (getWorld().getTileEntity(pos) instanceof TileDistillerSieveTray) {
				blockRecipe = RecipeHelper.blockRecipe(NCRecipes.machine_sieve_assembly, getWorld().getBlockState(pos.up()));
				if (blockRecipe == null) {
					if (multiblock.getLastError() == null) {
						multiblock.setLastError("nuclearcraft.multiblock_validation.distiller.invalid_sieve_recipe", pos);
					}
					return false;
				}
				else {
					trayLevels.add(i);
					traySieveCache.add(i + 1);
					traySieveEfficiency += blockRecipe.getMachineSieveAssemblyEfficiency();
					++traySieveCount;
				}
			}
			else if (!traySieveCache.contains(i) && (blockRecipe = RecipeHelper.blockRecipe(NCRecipes.machine_sieve_assembly, getWorld().getBlockState(pos))) != null) {
				packedSieveEfficiency += blockRecipe.getMachineSieveAssemblyEfficiency();
				++packedSieveCount;
			}
		}
		
		baseSpeedMultiplier = (double) area * (traySieveCount == 0 ? packedSieveEfficiency : (packedSieveCount == 0 ? traySieveEfficiency : (packedSieveCount * traySieveEfficiency + packedSieveEfficiency) / (1D + packedSieveCount)));
		basePowerMultiplier = reboilingUnits.size() + (double) area * (double) (traySieveCount + packedSieveCount);
		
		refluxUnitFraction = (double) refluxUnits.size() / (double) area;
		reboilingUnitFraction = (double) reboilingUnits.size() / (double) area;
		liquidDistributorFraction = (double) liquidDistributors.size() / (double) area;
		
		return true;
	}
	
	@Override
	public void onAssimilated(Machine assimilator) {
		super.onAssimilated(assimilator);
		
		if (getWorld().isRemote) {
			clearSounds();
		}
	}
	
	// Server
	
	@Override
	public void setActivity(boolean isMachineOn) {
		super.setActivity(isMachineOn);
		for (TileDistillerReboilingUnit reboilingUnit : getParts(TileDistillerReboilingUnit.class)) {
			reboilingUnit.setActivity(isMachineOn);
		}
	}
	
	@Override
	protected void setRecipeStats(@Nullable BasicRecipe recipe) {
		super.setRecipeStats(recipe);
		
		if (recipe == null) {
			refluxUnitBonus = reboilingUnitBonus = liquidDistributorBonus = 0D;
		}
		else {
			int liquidCount = 0, gasCount = 0;
			
			for (List<IFluidIngredient> fluidIngredientList : Arrays.asList(recipe.getFluidIngredients(), recipe.getFluidProducts())) {
				for (IFluidIngredient fluidIngredient : fluidIngredientList) {
					if (!fluidIngredient.isEmpty()) {
						FluidStack stack = fluidIngredient.getStack();
						Fluid fluid = stack.getFluid();
						if (fluid != null) {
							if (fluid.isGaseous(stack)) {
								++gasCount;
							}
							else {
								++liquidCount;
							}
						}
					}
				}
			}
			
			int totalCount = liquidCount + gasCount;
			refluxUnitBonus = totalCount == 0 ? 0D : refluxUnitFraction * (double) gasCount / (double) totalCount;
			reboilingUnitBonus = totalCount == 0 ? 0D : reboilingUnitFraction * (double) liquidCount / (double) totalCount;
			liquidDistributorBonus = liquidDistributorFraction / (1D + trayLevels.size());
		}
	}
	
	@Override
	protected double getSpeedMultiplier() {
		return baseSpeedMultiplier * (1D + refluxUnitBonus) * (1D + reboilingUnitBonus) * (1D + liquidDistributorBonus);
	}
	
	@Override
	protected boolean readyToProcess() {
		if (!super.readyToProcess()) {
			return false;
		}
		
		long sieveTrayCount = recipeInfo == null ? 0L : recipeInfo.recipe.getDistillerSieveTrayCount();
		return sieveTrayCount <= trayLevels.size();
	}
	
	// Client
	
	@Override
	public void onUpdateClient() {
		super.onUpdateClient();
		
		updateSounds();
	}
	
	@SideOnly(Side.CLIENT)
	protected void updateSounds() {
		if (machine_distiller_sound_volume == 0D) {
			clearSounds();
			return;
		}
		
		if (isProcessing && multiblock.isAssembled()) {
			double speedMultiplier = getSpeedMultiplier();
			double ratio = (NCMath.EPSILON + Math.abs(speedMultiplier)) / (NCMath.EPSILON + Math.abs(prevSpeedMultiplier));
			multiblock.refreshSounds |= ratio < 0.8D || ratio > 1.25D || getSoundMap().isEmpty();
			
			if (!multiblock.refreshSounds) {
				return;
			}
			multiblock.refreshSounds = false;
			
			clearSounds();
			
			if (speedMultiplier <= 0D) {
				return;
			}
			
			float volume = (float) (machine_distiller_sound_volume * Math.log1p(Math.cbrt(speedMultiplier)) / 128D);
			Consumer<BlockPos> addSound = x -> getSoundMap().put(x, SoundHandler.startBlockSound(NCSounds.distiller_run, x, volume, 1F));
			
			for (int i = 0; i < 8; ++i) {
				addSound.accept(multiblock.getExtremeInteriorCoord(NCMath.getBit(i, 0) == 1, NCMath.getBit(i, 1) == 1, NCMath.getBit(i, 2) == 1));
			}
			
			prevSpeedMultiplier = speedMultiplier;
		}
		else {
			multiblock.refreshSounds = true;
			clearSounds();
		}
	}
	
	// NBT
	
	@Override
	public void writeToLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		super.writeToLogicTag(logicTag, syncReason);
		int trayLevelCount = trayLevels.size();
		logicTag.setInteger("trayLevelCount", trayLevelCount);
		for (int i = 0; i < trayLevelCount; ++i) {
			logicTag.setInteger("trayLevel" + i, trayLevels.getInt(i));
		}
		logicTag.setDouble("refluxUnitFraction", refluxUnitFraction);
		logicTag.setDouble("reboilingUnitFraction", reboilingUnitFraction);
		logicTag.setDouble("liquidDistributorFraction", liquidDistributorFraction);
		logicTag.setDouble("refluxUnitBonus", refluxUnitBonus);
		logicTag.setDouble("reboilingUnitBonus", reboilingUnitBonus);
		logicTag.setDouble("liquidDistributorBonus", liquidDistributorBonus);
	}
	
	@Override
	public void readFromLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		super.readFromLogicTag(logicTag, syncReason);
		if (logicTag.hasKey("trayLevelCount")) {
			int trayLevelCount = logicTag.getInteger("trayLevelCount");
			trayLevels.clear();
			for (int i = 0; i < trayLevelCount; ++i) {
				trayLevels.add(logicTag.getInteger("trayLevel" + i));
			}
		}
		refluxUnitFraction = logicTag.getDouble("refluxUnitFraction");
		reboilingUnitFraction = logicTag.getDouble("reboilingUnitFraction");
		liquidDistributorFraction = logicTag.getDouble("liquidDistributorFraction");
		refluxUnitBonus = logicTag.getDouble("refluxUnitBonus");
		reboilingUnitBonus = logicTag.getDouble("reboilingUnitBonus");
		liquidDistributorBonus = logicTag.getDouble("liquidDistributorBonus");
	}
	
	// Packets
	
	@Override
	public MachineUpdatePacket getMultiblockUpdatePacket() {
		return new DistillerUpdatePacket(multiblock.controller.getTilePos(), multiblock.isMachineOn, isProcessing, time, baseProcessTime, baseProcessPower, tanks, baseSpeedMultiplier, basePowerMultiplier, recipeUnitInfo, refluxUnitBonus, reboilingUnitBonus, liquidDistributorBonus);
	}
	
	@Override
	public void onMultiblockUpdatePacket(MachineUpdatePacket message) {
		super.onMultiblockUpdatePacket(message);
		if (message instanceof DistillerUpdatePacket packet) {
			refluxUnitBonus = packet.refluxUnitBonus;
			reboilingUnitBonus = packet.reboilingUnitBonus;
			liquidDistributorBonus = packet.liquidDistributorBonus;
		}
	}
	
	@Override
	public DistillerRenderPacket getRenderPacket() {
		return new DistillerRenderPacket(multiblock.controller.getTilePos(), multiblock.isMachineOn, isProcessing, getFluidInputs(false), trayLevels);
	}
	
	@Override
	public void onRenderPacket(MachineRenderPacket message) {
		super.onRenderPacket(message);
		if (message instanceof DistillerRenderPacket packet) {
			boolean wasProcessing = isProcessing;
			isProcessing = packet.isProcessing;
			if (wasProcessing != isProcessing) {
				multiblock.refreshSounds = true;
			}
			TankInfo.readInfoList(packet.tankInfos, getFluidInputs(false));
			trayLevels.clear();
			trayLevels.addAll(packet.trayLevels);
		}
	}
}
