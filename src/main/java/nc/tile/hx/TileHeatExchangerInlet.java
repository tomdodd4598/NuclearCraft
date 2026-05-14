package nc.tile.hx;

import com.google.common.collect.Lists;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.hx.*;
import nc.recipe.*;
import nc.recipe.multiblock.CondenserRecipes;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.*;
import nc.tile.internal.processor.AbstractProcessorElement;
import nc.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.*;
import java.util.*;

import static nc.block.property.BlockProperties.FACING_ALL;
import static nc.config.NCConfig.enable_mek_gas;

public class TileHeatExchangerInlet extends TileHeatExchangerPart implements ITileFluid {
	
	public boolean isMasterInlet = false;
	
	public final @Nonnull List<Tank> masterTanks = Lists.newArrayList(new Tank(HeatExchanger.BASE_MAX_INPUT, NCRecipes.heat_exchanger.validFluids.get(0)), new Tank(HeatExchanger.BASE_MAX_OUTPUT, null));
	
	private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(TankSorption.IN);
	
	private final @Nonnull FluidTileWrapper[] fluidSides;
	private final @Nonnull GasTileWrapper gasWrapper;
	
	public @Nullable HeatExchangerTubeNetwork network;
	
	int inputTemperature = 300;
	int outputTemperature = 300;
	boolean isHeating = false;
	
	public double exchangerGrantedSpeedMultiplier = 0D;
	public double exchangerGrantedHeatTransferRate = 0D;
	
	public final AbstractProcessorElement processor = new AbstractProcessorElement() {
		
		double heatTransferRate, shellSpeedMultiplier;
		
		@Override
		public World getWorld() {
			return world;
		}
		
		@Override
		public BasicRecipeHandler getRecipeHandler() {
			HeatExchangerLogic logic = getLogic();
			return logic != null && logic.isCondenser() ? NCRecipes.condenser : NCRecipes.heat_exchanger;
		}
		
		@Override
		public void setRecipeStats(@Nullable BasicRecipe recipe) {
			if (recipe == null) {
				baseProcessTime = 1D;
				inputTemperature = 300;
				outputTemperature = 300;
				isHeating = false;
			}
			else {
				if (getRecipeHandler() instanceof CondenserRecipes) {
					baseProcessTime = recipe.getCondenserCoolingRequired();
					inputTemperature = recipe.getCondenserInputTemperature();
					outputTemperature = recipe.getCondenserOutputTemperature();
					isHeating = false;
				}
				else {
					baseProcessTime = recipe.getHeatExchangerHeatDifference();
					inputTemperature = recipe.getHeatExchangerInputTemperature();
					outputTemperature = recipe.getHeatExchangerOutputTemperature();
					isHeating = recipe.getHeatExchangerIsHeating();
				}
			}
		}
		
		@Override
		public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
			return InventoryStackList.EMPTY_LIST;
		}
		
		@Override
		public @Nonnull List<Tank> getTanks() {
			HeatExchanger hx = getMultiblock();
			return network == null && hx != null ? hx.shellTanks : masterTanks;
		}
		
		@Override
		public boolean getConsumesInputs() {
			return true;
		}
		
		@Override
		public boolean getLosesProgress() {
			return false;
		}
		
		@Override
		public int getItemInputSize() {
			return 0;
		}
		
		@Override
		public int getFluidInputSize() {
			return 1;
		}
		
		@Override
		public int getItemOutputSize() {
			return 0;
		}
		
		@Override
		public int getFluidOutputSize() {
			return 1;
		}
		
		@Override
		public int getItemInputSlot(int index) {
			return index;
		}
		
		@Override
		public int getFluidInputTank(int index) {
			return index;
		}
		
		@Override
		public int getItemOutputSlot(int index) {
			return index;
		}
		
		@Override
		public int getFluidOutputTank(int index) {
			return index + 1;
		}
		
		@Override
		public double getSpeedMultiplier() {
			HeatExchanger hx = getMultiblock();
			if (hx == null) {
				return 0D;
			}
			
			if (!hx.getLogic().isCondenser()) {
				return exchangerGrantedSpeedMultiplier;
			}
			
			if (isMasterShellInlet()) {
				return hx.shellSpeedMultiplier;
			}
			
			if (isHeating || hx.shellRecipe == null) {
				return 0D;
			}
			
			int shellTemperature = hx.shellRecipe.recipe.getCondenserDissipationFluidTemperature();
			if (outputTemperature < shellTemperature) {
				return 0D;
			}
			
			double absMeanTempDiff = getAbsMeanTempDiff(inputTemperature - shellTemperature, outputTemperature - shellTemperature);
			hx.totalTempDiff += absMeanTempDiff * network.usefulTubeCount;
			
			hx.activeContactCount += network.usefulTubeCount;
			
			++hx.activeNetworkCount;
			hx.activeTubeCount += network.usefulTubeCount;
			
			double tubeFlowDirectionMultiplier = recipeInfo.recipe.getCondenserFlowDirectionMultiplier(network.tubeFlow);
			
			double heatTransferMultiplier = absMeanTempDiff * tubeFlowDirectionMultiplier * hx.shellTanks.get(0).getFluidAmountFraction();
			return heatTransferRate = heatTransferMultiplier * network.baseCoolingMultiplier;
		}
		
		@Override
		public boolean isHalted() {
			HeatExchanger hx = getMultiblock();
			return hx == null || !hx.isExchangerOn;
		}
		
		@Override
		public void produceProducts() {
			int consumedAmount = consumedTanks.get(0).getFluidAmount();
			
			if (isMasterShellInlet()) {
				getMultiblock().shellInputRate += consumedAmount;
			}
			else {
				getMultiblock().tubeInputRate += consumedAmount;
			}
			
			super.produceProducts();
		}
		
		@Override
		public void onResumeProcessingState() {
			getMultiblock().packetFlag |= 1;
		}
		
		@Override
		public void onChangeProcessingState() {
			getMultiblock().packetFlag |= isMasterShellInlet() ? 2 : 1;
		}
		
		@Override
		public void process() {
			HeatExchanger hx = getMultiblock();
			if (hx != null && !hx.getLogic().isCondenser()) {
				double speedMultiplier = exchangerGrantedSpeedMultiplier;
				double maxProcessCount = baseProcessTime <= 0D ? 0D : speedMultiplier / baseProcessTime;
				
				time += speedMultiplier;
				
				int processCount = 0;
				while (time >= baseProcessTime) {
					finishProcess();
					++processCount;
				}
				
				hx.heatTransferRate += exchangerGrantedHeatTransferRate * (processCount == 0 || maxProcessCount <= 0D ? 1D : processCount / maxProcessCount);
				return;
			}
			
			heatTransferRate = shellSpeedMultiplier = 0D;
			
			double speedMultiplier = getSpeedMultiplier();
			double maxProcessCount = speedMultiplier / baseProcessTime;
			
			time += speedMultiplier;
			
			int processCount = 0;
			while (time >= baseProcessTime) {
				finishProcess();
				++processCount;
			}
			
			if (hx != null) {
				hx.heatTransferRate += heatTransferRate * (processCount == 0 ? 1D : processCount / maxProcessCount);
				hx.shellSpeedMultiplier += shellSpeedMultiplier * processCount / maxProcessCount;
			}
		}
		
		@Override
		public void refreshActivityOnProduction() {
			super.refreshActivityOnProduction();
			if (!canProcessInputs) {
				getMultiblock().refreshFlag = true;
			}
		}
	};
	
	
	public void resetExchangerGrants() {
		exchangerGrantedSpeedMultiplier = 0D;
		exchangerGrantedHeatTransferRate = 0D;
	}
	
	public static class ExchangerRateProposal {
		public final TileHeatExchangerInlet inlet;
		public final double commonTransfer;
		public final double tubeProcessSpeedScale;
		public final double shellProcessSpeedScale;
		public final double heatTransferRateScale;
		public final double absMeanTempDiff;
		public final int usefulTubeCount;
		
		public ExchangerRateProposal(TileHeatExchangerInlet inlet, double commonTransfer, double tubeProcessSpeedScale, double shellProcessSpeedScale, double heatTransferRateScale, double absMeanTempDiff, int usefulTubeCount) {
			this.inlet = inlet;
			this.commonTransfer = commonTransfer;
			this.tubeProcessSpeedScale = tubeProcessSpeedScale;
			this.shellProcessSpeedScale = shellProcessSpeedScale;
			this.heatTransferRateScale = heatTransferRateScale;
			this.absMeanTempDiff = absMeanTempDiff;
			this.usefulTubeCount = usefulTubeCount;
		}
		
		public ExchangerRateProposal withCommonTransfer(double newCommonTransfer) {
			return new ExchangerRateProposal(inlet, newCommonTransfer, tubeProcessSpeedScale, shellProcessSpeedScale, heatTransferRateScale, absMeanTempDiff, usefulTubeCount);
		}
	}
	
	public @Nullable ExchangerRateProposal getExchangerRateProposal() {
		HeatExchanger hx = getMultiblock();
		if (hx == null || hx.getLogic().isCondenser() || isMasterShellInlet() || network == null || processor.recipeInfo == null || hx.masterShellInlet == null || hx.masterShellInlet.processor.recipeInfo == null) {
			return null;
		}
		
		BasicRecipe shellRecipe = hx.masterShellInlet.processor.recipeInfo.recipe;
		boolean shellIsHeating = shellRecipe.getHeatExchangerIsHeating();
		if (isHeating == shellIsHeating) {
			return null;
		}
		
		int shellInputTemperature = shellRecipe.getHeatExchangerInputTemperature();
		int shellOutputTemperature = shellRecipe.getHeatExchangerOutputTemperature();
		
		boolean contraflow = network.isContraflow();
		int inputEndShellTemperature = contraflow ? shellOutputTemperature : shellInputTemperature;
		int outputEndShellTemperature = contraflow ? shellInputTemperature : shellOutputTemperature;
		
		int inletTemperatureDiff = inputTemperature - inputEndShellTemperature;
		int outletTemperatureDiff = outputTemperature - outputEndShellTemperature;
		int sumTempDiff = inletTemperatureDiff + outletTemperatureDiff;
		if (sumTempDiff == 0) {
			return null;
		}
		
		boolean heating = sumTempDiff < 0;
		if (isHeating != heating) {
			return null;
		}
		
		if ((inletTemperatureDiff > 0 && outletTemperatureDiff < 0) || (inletTemperatureDiff < 0 && outletTemperatureDiff > 0)) {
			return null;
		}
		
		double absMeanTempDiff = getAbsMeanTempDiff(inletTemperatureDiff, outletTemperatureDiff);
		
		double tubeFlowDirectionMultiplier = processor.recipeInfo.recipe.getHeatExchangerFlowDirectionMultiplier(network.tubeFlow);
		double shellFlowDirectionMultiplier = shellRecipe.getHeatExchangerFlowDirectionMultiplier(network.shellFlow);
		double commonTransfer = absMeanTempDiff * tubeFlowDirectionMultiplier * shellFlowDirectionMultiplier;
		if (commonTransfer <= 0D) {
			return null;
		}
		
		double tubeProcessSpeedScale = heating ? network.baseHeatingMultiplier : network.baseCoolingMultiplier;
		double shellProcessSpeedScale = heating ? network.baseCoolingMultiplier : network.baseHeatingMultiplier;
		double heatTransferRateScale = network.baseCoolingMultiplier;
		
		return new ExchangerRateProposal(this, commonTransfer, tubeProcessSpeedScale, shellProcessSpeedScale, heatTransferRateScale, absMeanTempDiff, network.usefulTubeCount);
	}
	
	public static double getAbsMeanTempDiff(int inTemperatureDiff, int outTemperatureDiff) {
		if (NCConfig.heat_exchanger_lmtd && inTemperatureDiff != outTemperatureDiff) {
			int absInTemperatureDiff = Math.abs(inTemperatureDiff), absOutTemperatureDiff = Math.abs(outTemperatureDiff);
			return (absInTemperatureDiff - absOutTemperatureDiff) / Math.log((double) absInTemperatureDiff / (double) absOutTemperatureDiff);
		}
		else {
			return Math.abs(0.5D * (inTemperatureDiff + outTemperatureDiff));
		}
	}
	
	public TileHeatExchangerInlet() {
		super(CuboidalPartPositionType.WALL);
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
	}
	
	@Override
	public void onMachineAssembled(HeatExchanger multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
		if (!world.isRemote) {
			EnumFacing facing = getPartPosition().getFacing();
			if (facing != null) {
				world.setBlockState(pos, world.getBlockState(pos).withProperty(FACING_ALL, facing), 2);
			}
		}
	}
	
	@Override
	public void onMachineBroken() {
		isMasterInlet = false;
		network = null;
		super.onMachineBroken();
	}
	
	public boolean isMasterShellInlet() {
		HeatExchanger hx = getMultiblock();
		return hx != null && this == hx.masterShellInlet;
	}
	
	// Fluids
	
	@Override
	public @Nonnull List<Tank> getTanks() {
		HeatExchangerLogic logic = getLogic();
		return logic == null ? Collections.emptyList() : logic.getInletTanks(network);
	}
	
	@Override
	public void clearAllTanks() {
		ITileFluid.super.clearAllTanks();
		for (Tank tank : masterTanks) {
			tank.setFluidStored(null);
		}
		for (Tank tank : processor.consumedTanks) {
			tank.setFluidStored(null);
		}
		processor.refreshAll();
	}
	
	@Override
	public @Nonnull FluidConnection[] getFluidConnections() {
		return fluidConnections;
	}
	
	@Override
	public void setFluidConnections(@Nonnull FluidConnection[] connections) {
		fluidConnections = connections;
	}
	
	@Override
	public @Nonnull FluidTileWrapper[] getFluidSides() {
		return fluidSides;
	}
	
	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		return gasWrapper;
	}
	
	@Override
	public boolean getInputTanksSeparated() {
		return false;
	}
	
	@Override
	public void setInputTanksSeparated(boolean separated) {}
	
	@Override
	public boolean getVoidUnusableFluidInput(int tankNumber) {
		return false;
	}
	
	@Override
	public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {}
	
	@Override
	public TankOutputSetting getTankOutputSetting(int tankNumber) {
		return TankOutputSetting.DEFAULT;
	}
	
	@Override
	public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		for (int i = 0; i < masterTanks.size(); ++i) {
			masterTanks.get(i).writeToNBT(nbt, "masterTanks" + i);
		}
		writeFluidConnections(nbt);
		processor.writeToNBT(nbt, "processor");
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		for (int i = 0; i < masterTanks.size(); ++i) {
			masterTanks.get(i).readFromNBT(nbt, "masterTanks" + i);
		}
		readFluidConnections(nbt);
		processor.readFromNBT(nbt, "processor");
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || (ModCheck.mekanismLoaded() && enable_mek_gas && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY)) {
			return hasFluidSideCapability(side);
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (hasFluidSideCapability(side)) {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidSide(nonNullSide(side)));
			}
			return null;
		}
		else if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			if (enable_mek_gas && hasFluidSideCapability(side)) {
				return CapabilityHelper.GAS_HANDLER_CAPABILITY.cast(getGasWrapper());
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
