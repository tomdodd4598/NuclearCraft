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
	
	public long heatingCount = 0L;
	
	public double heatingBonus = 0D;
	
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
	public int reservoirTankCount() {
		return 1;
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
		
		multiblock.baseSpeedMultiplier = 0D;
		multiblock.basePowerMultiplier = 0D;
		
		heatingCount = 0L;
		
		Long2ObjectMap<TileInfiltratorPressureChamber> pressureChamberMap = getPartMap(TileInfiltratorPressureChamber.class);
		Long2ObjectMap<TileInfiltratorHeatingUnit> heatingUnitMap = getPartMap(TileInfiltratorHeatingUnit.class);
		
		for (TileInfiltratorPressureChamber pressureChamber : pressureChamberMap.values()) {
			BlockPos pos = pressureChamber.getPos();
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (heatingUnitMap.containsKey(pos.offset(dir).toLong())) {
					++heatingCount;
					break;
				}
			}
		}
		
		multiblock.baseSpeedMultiplier = pressureChamberMap.size();
		multiblock.basePowerMultiplier = pressureChamberMap.size() + heatingUnitMap.size();
		
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
		heatingBonus = recipe == null ? 0D : heatingCount * recipe.getInfiltratorHeatingFactor() / getPartCount(TileInfiltratorPressureChamber.class);
	}
	
	protected double getReservoirLevelFraction() {
		return multiblock.reservoirTanks.get(0).getFluidAmountFraction();
	}
	
	@Override
	protected double getSpeedMultiplier() {
		return multiblock.baseSpeedMultiplier * pressureFluidEfficiency * (1D + heatingBonus) * getReservoirLevelFraction();
	}
	
	@Override
	protected double getPowerMultiplier() {
		return multiblock.basePowerMultiplier * getReservoirLevelFraction();
	}
	
	@Override
	protected boolean readyToProcess() {
		return super.readyToProcess() && getReservoirLevelFraction() > 0D;
	}
	
	@Override
	public void refreshActivity() {
		super.refreshActivity();
		
		RecipeInfo<BasicRecipe> recipeInfo = NCRecipes.infiltrator_pressure_fluid.getRecipeInfoFromInputs(Collections.emptyList(), multiblock.reservoirTanks.subList(0, 1));
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
		
		if (multiblock.processor.isProcessing && multiblock.isAssembled()) {
			double speedMultiplier = getSpeedMultiplier();
			double ratio = (NCMath.EPSILON + Math.abs(speedMultiplier)) / (NCMath.EPSILON + Math.abs(multiblock.prevSpeedMultiplier));
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
			
			multiblock.prevSpeedMultiplier = speedMultiplier;
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
		logicTag.setLong("heatingCount", heatingCount);
		logicTag.setDouble("heatingBonus", heatingBonus);
	}
	
	@Override
	public void readFromLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		super.readFromLogicTag(logicTag, syncReason);
		pressureFluidEfficiency = logicTag.getDouble("pressureFluidEfficiency");
		heatingCount = logicTag.getLong("heatingCount");
		heatingBonus = logicTag.getDouble("heatingBonus");
	}
	
	// Packets
	
	@Override
	public MachineUpdatePacket getMultiblockUpdatePacket() {
		return new InfiltratorUpdatePacket(multiblock.controller.getTilePos(), multiblock.isMachineOn, multiblock.processor.isProcessing, multiblock.processor.time, multiblock.processor.baseProcessTime, multiblock.baseProcessPower, multiblock.tanks, multiblock.baseSpeedMultiplier, multiblock.basePowerMultiplier, multiblock.recipeUnitInfo, pressureFluidEfficiency, heatingBonus);
	}
	
	@Override
	public void onMultiblockUpdatePacket(MachineUpdatePacket message) {
		super.onMultiblockUpdatePacket(message);
		if (message instanceof InfiltratorUpdatePacket packet) {
			pressureFluidEfficiency = packet.pressureFluidEfficiency;
			heatingBonus = packet.heatingBonus;
		}
	}
	
	@Override
	public InfiltratorRenderPacket getRenderPacket() {
		return new InfiltratorRenderPacket(multiblock.controller.getTilePos(), multiblock.isMachineOn, multiblock.processor.isProcessing, multiblock.processor.time, multiblock.processor.baseProcessTime, multiblock.tanks, multiblock.reservoirTanks);
	}
	
	@Override
	public void onRenderPacket(MachineRenderPacket message) {
		super.onRenderPacket(message);
		if (message instanceof InfiltratorRenderPacket packet) {
			boolean wasProcessing = multiblock.processor.isProcessing;
			multiblock.processor.isProcessing = packet.isProcessing;
			if (wasProcessing != multiblock.processor.isProcessing) {
				multiblock.refreshSounds = true;
			}
			multiblock.processor.time = packet.time;
			multiblock.processor.baseProcessTime = packet.baseProcessTime;
			TankInfo.readInfoList(packet.tankInfos, multiblock.tanks);
			TankInfo.readInfoList(packet.reservoirTankInfos, multiblock.reservoirTanks);
		}
	}
}
