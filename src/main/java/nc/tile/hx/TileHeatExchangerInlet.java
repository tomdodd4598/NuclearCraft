package nc.tile.hx;

import com.google.common.collect.Lists;
import nc.ModCheck;
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
	
	public final @Nonnull List<Tank> masterTanks = Lists.newArrayList(new Tank(HeatExchanger.BASE_MAX_INPUT, NCRecipes.heat_exchanger.validFluids.get(0)), new Tank(HeatExchanger.BASE_MAX_OUTPUT, null));
	
	private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(TankSorption.IN);
	
	private final @Nonnull FluidTileWrapper[] fluidSides;
	private final @Nonnull GasTileWrapper gasWrapper;
	
	public @Nullable HeatExchangerTubeNetwork network;
	
	public int inputTemperature = 300;
	public int outputTemperature = 300;
	public boolean isHeating = false;
	
	public double speedMultiplierProposal = 0D;
	public double heatTransferRateProposal = 0D;
	
	public class InletProcessorElement extends AbstractProcessorElement {
		
		public double heatTransferRate, shellSpeedMultiplier;
		
		@Override
		public World getWorld() {
			return world;
		}
		
		@Override
		public BasicRecipeHandler getRecipeHandler() {
			HeatExchangerLogic logic = getLogic();
			return logic == null ? NCRecipes.heat_exchanger : logic.getInletRecipeHandler(TileHeatExchangerInlet.this);
		}
		
		@Override
		public void setRecipeStats(@Nullable BasicRecipe recipe) {
			HeatExchangerLogic logic = getLogic();
			if (logic != null) {
				logic.setInletRecipeStats(TileHeatExchangerInlet.this, recipe);
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
			HeatExchangerLogic logic = getLogic();
			return logic == null ? 0D : logic.getInletSpeedMultiplier(TileHeatExchangerInlet.this);
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
			HeatExchangerLogic logic = getLogic();
			if (logic != null) {
				logic.inletProcess(TileHeatExchangerInlet.this);
			}
		}
		
		@Override
		public void refreshActivityOnProduction() {
			super.refreshActivityOnProduction();
			if (!canProcessInputs) {
				getMultiblock().refreshFlag = true;
			}
		}
	}
	
	public final InletProcessorElement processor = new InletProcessorElement();
	
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
