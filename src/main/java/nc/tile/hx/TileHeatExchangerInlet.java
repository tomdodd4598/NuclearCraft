package nc.tile.hx;

import com.google.common.collect.Lists;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.hx.*;
import nc.recipe.*;
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
	
	public @Nonnull NonNullList<ItemStack> inventoryStacks = NonNullList.withSize(0, ItemStack.EMPTY);
	
	public final @Nonnull List<Tank> masterTanks = Lists.newArrayList(new Tank(HeatExchanger.BASE_MAX_INPUT, NCRecipes.getValidFluids("heat_exchanger").get(0)), new Tank(HeatExchanger.BASE_MAX_OUTPUT, null));
	
	private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(TankSorption.IN);
	
	private final @Nonnull FluidTileWrapper[] fluidSides;
	private final @Nonnull GasTileWrapper gasWrapper;
	
	public @Nullable HeatExchangerTubeNetwork network;
	
	int inputTemperature = 300;
	int outputTemperature = 300;
	boolean isHeating = false;
	
	public final AbstractProcessorElement processor = new AbstractProcessorElement() {
		
		@Override
		public World getWorld() {
			return world;
		}
		
		@Override
		public BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.heat_exchanger;
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
				baseProcessTime = recipe.getHeatExchangerHeatDifference();
				inputTemperature = recipe.getHeatExchangerInputTemperature();
				outputTemperature = recipe.getHeatExchangerOutputTemperature();
				isHeating = recipe.getHeatExchangerIsHeating();
			}
		}
		
		@Override
		public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
			return inventoryStacks;
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
			if (isMasterShellInlet()) {
				return hx.shellSpeedMultiplier;
			}
			else {
				if (hx.getLogic().isCondenser()) {
					if (isHeating || hx.shellRecipe == null) {
						return 0D;
					}
					
					int shellTemperature = hx.shellRecipe.recipe.getHeatExchangerInputTemperature();
					if (outputTemperature < shellTemperature) {
						return 0D;
					}
					
					double absMeanTempDiff = getAbsMeanTempDiff(inputTemperature - shellTemperature, outputTemperature - shellTemperature);
					hx.totalTempDiff += absMeanTempDiff * network.usefulTubeCount;
					
					hx.activeContactCount += network.usefulTubeCount;
					
					++hx.activeNetworkCount;
					hx.activeTubeCount += network.usefulTubeCount;
					
					double tubeFlowDirectionMultiplier = recipeInfo.recipe.getHeatExchangerFlowDirectionMultiplier(network.tubeFlow);
					
					double heatTransferMultiplier = absMeanTempDiff * tubeFlowDirectionMultiplier * hx.shellTanks.get(0).getFluidAmountFraction();
					hx.heatDissipationRate += heatTransferMultiplier * network.baseCoolingMultiplier;
					return heatTransferMultiplier * network.baseCoolingMultiplier;
				}
				
				RecipeInfo<BasicRecipe> shellRecipeInfo = hx.masterShellInlet.processor.recipeInfo;
				if (shellRecipeInfo == null) {
					return 0D;
				}
				
				BasicRecipe shellRecipe = shellRecipeInfo.recipe;
				boolean shellIsHeating = shellRecipe.getHeatExchangerIsHeating();
				if (isHeating == shellIsHeating) {
					return 0D;
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
					return 0D;
				}
				
				boolean heating = sumTempDiff < 0;
				if (isHeating != heating) {
					return 0D;
				}
				
				if ((inletTemperatureDiff > 0 && outletTemperatureDiff < 0) || (inletTemperatureDiff < 0 && outletTemperatureDiff > 0)) {
					return 0D;
					
					/*if (heating) {
						if (shellOutputTemperature < inputTemperature) {
							return 0D;
						}
						
						double absMeanTempDiff = getAbsMeanTempDiff(inputEndShellTemperature - inputTemperature, outputEndShellTemperature - inputTemperature);
						hx.totalTempDiff += absMeanTempDiff * network.usefulTubeCount;
						
						hx.activeContactCount += network.usefulTubeCount;
						
						double heatTransferMultiplier = absMeanTempDiff * shellFlowDirectionMultiplier * getTanks().get(0).getFluidAmountFraction();
						hx.heatDissipationRate += heatTransferMultiplier * network.baseCoolingMultiplier;
						hx.shellSpeedMultiplier += heatTransferMultiplier * network.baseCoolingMultiplier;
						return 0D;
					}
					else {
						if (outputTemperature < shellInputTemperature) {
							return 0D;
						}
						
						double absMeanTempDiff = getAbsMeanTempDiff(inputTemperature - shellInputTemperature, outputTemperature - shellInputTemperature);
						hx.totalTempDiff += absMeanTempDiff * network.usefulTubeCount;
						
						hx.activeContactCount += network.usefulTubeCount;
						
						++hx.activeNetworkCount;
						hx.activeTubeCount += network.usefulTubeCount;
						
						double heatTransferMultiplier = absMeanTempDiff * tubeFlowDirectionMultiplier * hx.shellTanks.get(0).getFluidAmountFraction();
						hx.heatDissipationRate += heatTransferMultiplier * network.baseCoolingMultiplier;
						return heatTransferMultiplier * network.baseCoolingMultiplier;
					}*/
				}
				else {
					double absMeanTempDiff = getAbsMeanTempDiff(inletTemperatureDiff, outletTemperatureDiff);
					hx.totalTempDiff += absMeanTempDiff * network.usefulTubeCount;
					
					hx.activeContactCount += network.usefulTubeCount;
					
					++hx.activeNetworkCount;
					hx.activeTubeCount += network.usefulTubeCount;
					
					double tubeFlowDirectionMultiplier = recipeInfo.recipe.getHeatExchangerFlowDirectionMultiplier(network.tubeFlow);
					double shellFlowDirectionMultiplier = shellRecipe.getHeatExchangerFlowDirectionMultiplier(network.shellFlow);
					double heatTransferMultiplier = absMeanTempDiff * tubeFlowDirectionMultiplier * shellFlowDirectionMultiplier;
					
					hx.heatTransferRate += heatTransferMultiplier * network.baseCoolingMultiplier;
					hx.shellSpeedMultiplier += heatTransferMultiplier * (heating ? network.baseCoolingMultiplier : network.baseHeatingMultiplier);
					return heatTransferMultiplier * (heating ? network.baseHeatingMultiplier : network.baseCoolingMultiplier);
				}
			}
		}
		
		@Override
		public boolean isHalted() {
			HeatExchanger hx = getMultiblock();
			return hx == null || !hx.isExchangerOn;
		}
		
		@Override
		public void produceProducts() {
			if (isMasterShellInlet()) {
				getMultiblock().shellInputRate += consumedTanks.get(0).getFluidAmount();
			}
			else {
				getMultiblock().tubeInputRate += consumedTanks.get(0).getFluidAmount();
			}
			super.produceProducts();
		}
		
		@Override
		public void onResumeProcessingState() {
			if (isMasterShellInlet()) {
				getMultiblock().packetFlag |= 1;
			}
		}
		
		@Override
		public void onChangeProcessingState() {
			if (isMasterShellInlet()) {
				getMultiblock().packetFlag |= 2;
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
