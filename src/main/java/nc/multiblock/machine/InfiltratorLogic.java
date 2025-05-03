package nc.multiblock.machine;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import nc.config.NCConfig;
import nc.handler.SoundHandler;
import nc.init.NCSounds;
import nc.network.multiblock.*;
import nc.recipe.*;
import nc.tile.internal.fluid.Tank.TankInfo;
import nc.tile.machine.*;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.util.NCMath;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

import static nc.config.NCConfig.*;

public class InfiltratorLogic extends MachineLogic {
	
	public double pressureFluidEfficiency = 0D;
	
	public double heatingContactFraction = 0D;
	
	public double heatingContactBonus = 0D;
	
	public InfiltratorLogic(Machine machine) {
		super(machine);
	}
	
	public InfiltratorLogic(MachineLogic oldLogic) {
		super(oldLogic);
	}
	
	@Override
	public String getID() {
		return "infiltrator";
	}
	
	@Override
	public List<Set<String>> getReservoirValidFluids() {
		return NCRecipes.infiltrator_pressure_fluid.validFluids;
	}
	
	@Override
	public BasicRecipeHandler getRecipeHandler() {
		return NCRecipes.multiblock_infiltrator;
	}
	
	@Override
	public double defaultProcessTime() {
		return NCConfig.machine_infiltrator_time;
	}
	
	@Override
	public double defaultProcessPower() {
		return NCConfig.machine_infiltrator_power;
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
		if (!super.isMachineWhole()) {
			return false;
		}
		
		baseSpeedMultiplier = 0D;
		basePowerMultiplier = 0D;
		
		heatingContactFraction = 0D;
		
		Long2ObjectMap<TileInfiltratorPressureChamber> pressureChamberMap = getPartMap(TileInfiltratorPressureChamber.class);
		Long2ObjectMap<TileInfiltratorHeatingUnit> heatingUnitMap = getPartMap(TileInfiltratorHeatingUnit.class);
		
		long heatingContactCount = 0;
		
		for (TileInfiltratorPressureChamber pressureChamber : pressureChamberMap.values()) {
			BlockPos pos = pressureChamber.getPos();
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (heatingUnitMap.containsKey(pos.offset(dir).toLong())) {
					++heatingContactCount;
				}
			}
		}
		
		int pressureChamberCount = pressureChamberMap.size(), heatingUnitCount = heatingUnitMap.size();
		
		baseSpeedMultiplier = pressureChamberCount;
		basePowerMultiplier = pressureChamberCount + heatingUnitCount;
		
		heatingContactFraction = pressureChamberCount <= 0 ? 0D : heatingContactCount / (6D * pressureChamberCount);
		
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
		for (TileInfiltratorHeatingUnit heatingUnit : getParts(TileInfiltratorHeatingUnit.class)) {
			heatingUnit.setActivity(isMachineOn);
		}
	}
	
	@Override
	protected void setRecipeStats(@Nullable BasicRecipe recipe) {
		super.setRecipeStats(recipe);
		heatingContactBonus = recipe == null ? 0D : heatingContactFraction * recipe.getInfiltratorHeatingFactor();
	}
	
	protected double getReservoirLevelFraction() {
		return reservoirTanks.get(0).getFluidAmountFraction();
	}
	
	@Override
	protected double getSpeedMultiplier() {
		return baseSpeedMultiplier * pressureFluidEfficiency * (1D + heatingContactBonus) * getReservoirLevelFraction();
	}
	
	@Override
	protected double getPowerMultiplier() {
		return basePowerMultiplier * getReservoirLevelFraction();
	}
	
	@Override
	protected boolean readyToProcess() {
		return super.readyToProcess() && getReservoirLevelFraction() > 0D;
	}
	
	@Override
	public void refreshActivity() {
		super.refreshActivity();
		
		RecipeInfo<BasicRecipe> recipeInfo = NCRecipes.infiltrator_pressure_fluid.getRecipeInfoFromInputs(Collections.emptyList(), reservoirTanks.subList(0, 1));
		pressureFluidEfficiency = recipeInfo == null ? 0D : recipeInfo.recipe.getInfiltratorPressureFluidEfficiency();
	}
	
	// Client
	
	@Override
	public void onUpdateClient() {
		super.onUpdateClient();
		
		updateSounds();
	}
	
	@SideOnly(Side.CLIENT)
	protected void updateSounds() {
		if (machine_infiltrator_sound_volume == 0D) {
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
			
			float volume = (float) (machine_infiltrator_sound_volume * Math.log1p(Math.cbrt(speedMultiplier)) / 128D);
			Consumer<BlockPos> addSound = x -> getSoundMap().put(x, SoundHandler.startBlockSound(NCSounds.infiltrator_run, x, volume, 1F));
			
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
		logicTag.setDouble("pressureFluidEfficiency", pressureFluidEfficiency);
		logicTag.setDouble("heatingContactFraction", heatingContactFraction);
		logicTag.setDouble("heatingContactBonus", heatingContactBonus);
	}
	
	@Override
	public void readFromLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		super.readFromLogicTag(logicTag, syncReason);
		pressureFluidEfficiency = logicTag.getDouble("pressureFluidEfficiency");
		heatingContactFraction = logicTag.getDouble("heatingContactFraction");
		heatingContactBonus = logicTag.getDouble("heatingContactBonus");
	}
	
	// Packets
	
	@Override
	public MachineUpdatePacket getMultiblockUpdatePacket() {
		return new InfiltratorUpdatePacket(multiblock.controller.getTilePos(), multiblock.isMachineOn, isProcessing, time, baseProcessTime, baseProcessPower, tanks, baseSpeedMultiplier, basePowerMultiplier, recipeUnitInfo, pressureFluidEfficiency, heatingContactBonus);
	}
	
	@Override
	public void onMultiblockUpdatePacket(MachineUpdatePacket message) {
		super.onMultiblockUpdatePacket(message);
		if (message instanceof InfiltratorUpdatePacket packet) {
			pressureFluidEfficiency = packet.pressureFluidEfficiency;
			heatingContactBonus = packet.heatingContactBonus;
		}
	}
	
	@Override
	public InfiltratorRenderPacket getRenderPacket() {
		return new InfiltratorRenderPacket(multiblock.controller.getTilePos(), multiblock.isMachineOn, isProcessing, time, baseProcessTime, tanks, reservoirTanks);
	}
	
	@Override
	public void onRenderPacket(MachineRenderPacket message) {
		super.onRenderPacket(message);
		if (message instanceof InfiltratorRenderPacket packet) {
			boolean wasProcessing = isProcessing;
			isProcessing = packet.isProcessing;
			if (wasProcessing != isProcessing) {
				multiblock.refreshSounds = true;
			}
			time = packet.time;
			baseProcessTime = packet.baseProcessTime;
			TankInfo.readInfoList(packet.tankInfos, tanks);
			TankInfo.readInfoList(packet.reservoirTankInfos, reservoirTanks);
		}
	}
}
