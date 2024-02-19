package nc.tile.processor.info;

import java.util.*;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.ints.*;
import nc.container.ContainerFunction;
import nc.gui.GuiFunction;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.recipe.*;
import nc.tile.*;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.processor.IProcessor;
import nc.util.CollectionHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

public abstract class ProcessorContainerInfo<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>> extends TileContainerInfo<TILE> {
	
	protected final ContainerFunction<TILE> configContainerFunction;
	protected final GuiFunction<TILE> configGuiFunction;
	
	public final int itemInputSize;
	public final int fluidInputSize;
	public final int itemOutputSize;
	public final int fluidOutputSize;
	
	public final int[] itemInputSlots;
	public final int[] itemOutputSlots;
	
	public final int[] fluidInputTanks;
	public final int[] fluidOutputTanks;
	
	public final int inputTankCapacity;
	public final int outputTankCapacity;
	
	public final double defaultProcessTime;
	public final double defaultProcessPower;
	
	public final boolean isGenerator;
	
	public final boolean consumesInputs;
	public final boolean losesProgress;
	
	public final String ocComponentName;
	
	public final int guiWidth;
	public final int guiHeight;
	
	public final List<int[]> itemInputGuiXYWH;
	public final List<int[]> fluidInputGuiXYWH;
	public final List<int[]> itemOutputGuiXYWH;
	public final List<int[]> fluidOutputGuiXYWH;
	
	public final List<int[]> itemInputStackXY;
	public final List<int[]> itemOutputStackXY;
	
	public final int[] itemInputSorptionButtonID;
	public final int[] fluidInputSorptionButtonID;
	public final int[] itemOutputSorptionButtonID;
	public final int[] fluidOutputSorptionButtonID;
	
	public final int playerGuiX;
	public final int playerGuiY;
	
	public final int progressBarGuiX;
	public final int progressBarGuiY;
	public final int progressBarGuiW;
	public final int progressBarGuiH;
	public final int progressBarGuiU;
	public final int progressBarGuiV;
	
	public final int energyBarGuiX;
	public final int energyBarGuiY;
	public final int energyBarGuiW;
	public final int energyBarGuiH;
	public final int energyBarGuiU;
	public final int energyBarGuiV;
	
	public final int machineConfigGuiX;
	public final int machineConfigGuiY;
	
	public final int redstoneControlGuiX;
	public final int redstoneControlGuiY;
	
	public final boolean jeiCategoryEnabled;
	
	public final String jeiCategoryUid;
	public final String jeiTitle;
	public final String jeiTexture;
	
	public final int jeiBackgroundX;
	public final int jeiBackgroundY;
	public final int jeiBackgroundW;
	public final int jeiBackgroundH;
	
	public final int jeiTooltipX;
	public final int jeiTooltipY;
	public final int jeiTooltipW;
	public final int jeiTooltipH;
	
	public final int jeiClickAreaX;
	public final int jeiClickAreaY;
	public final int jeiClickAreaW;
	public final int jeiClickAreaH;
	
	protected ProcessorContainerInfo(String modId, String name, Class<? extends Container> containerClass, ContainerFunction<TILE> containerFunction, Class<? extends GuiContainer> guiClass, GuiFunction<TILE> guiFunction, ContainerFunction<TILE> configContainerFunction, GuiFunction<TILE> configGuiFunction, int inputTankCapacity, int outputTankCapacity, double defaultProcessTime, double defaultProcessPower, boolean isGenerator, boolean consumesInputs, boolean losesProgress, String ocComponentName, int[] guiWH, List<int[]> itemInputGuiXYWH, List<int[]> fluidInputGuiXYWH, List<int[]> itemOutputGuiXYWH, List<int[]> fluidOutputGuiXYWH, int[] playerGuiXY, int[] progressBarGuiXYWHUV, int[] energyBarGuiXYWHUV, int[] machineConfigGuiXY, int[] redstoneControlGuiXY, boolean jeiCategoryEnabled, String jeiCategoryUid, String jeiTitle, String jeiTexture, int[] jeiBackgroundXYWH, int[] jeiTooltipXYWH, int[] jeiClickAreaXYWH) {
		super(modId, name, containerClass, containerFunction, guiClass, guiFunction);
		
		this.configContainerFunction = configContainerFunction;
		this.configGuiFunction = configGuiFunction;
		
		itemInputSize = itemInputGuiXYWH.size();
		fluidInputSize = fluidInputGuiXYWH.size();
		itemOutputSize = itemOutputGuiXYWH.size();
		fluidOutputSize = fluidOutputGuiXYWH.size();
		
		itemInputSlots = CollectionHelper.increasingArray(itemInputSize);
		itemOutputSlots = CollectionHelper.increasingArray(itemInputSize, itemOutputSize);
		
		fluidInputTanks = CollectionHelper.increasingArray(fluidInputSize);
		fluidOutputTanks = CollectionHelper.increasingArray(fluidInputSize, fluidOutputSize);
		
		this.inputTankCapacity = inputTankCapacity;
		this.outputTankCapacity = outputTankCapacity;
		
		this.defaultProcessTime = defaultProcessTime;
		this.defaultProcessPower = defaultProcessPower;
		
		this.isGenerator = isGenerator;
		
		this.consumesInputs = consumesInputs;
		this.losesProgress = losesProgress;
		
		this.ocComponentName = ocComponentName;
		
		guiWidth = guiWH[0];
		guiHeight = guiWH[1];
		
		this.itemInputGuiXYWH = itemInputGuiXYWH;
		this.fluidInputGuiXYWH = fluidInputGuiXYWH;
		this.itemOutputGuiXYWH = itemOutputGuiXYWH;
		this.fluidOutputGuiXYWH = fluidOutputGuiXYWH;
		
		itemInputStackXY = TileContainerInfoHelper.stackXYList(itemInputGuiXYWH);
		itemOutputStackXY = TileContainerInfoHelper.stackXYList(itemOutputGuiXYWH);
		
		itemInputSorptionButtonID = CollectionHelper.increasingArray(itemInputSize);
		fluidInputSorptionButtonID = CollectionHelper.increasingArray(itemInputSize, fluidInputSize);
		itemOutputSorptionButtonID = CollectionHelper.increasingArray(itemInputSize + fluidInputSize, itemOutputSize);
		fluidOutputSorptionButtonID = CollectionHelper.increasingArray(itemInputSize + fluidInputSize + itemOutputSize, fluidOutputSize);
		
		playerGuiX = playerGuiXY[0];
		playerGuiY = playerGuiXY[1];
		
		progressBarGuiX = progressBarGuiXYWHUV[0];
		progressBarGuiY = progressBarGuiXYWHUV[1];
		progressBarGuiW = progressBarGuiXYWHUV[2];
		progressBarGuiH = progressBarGuiXYWHUV[3];
		progressBarGuiU = progressBarGuiXYWHUV[4];
		progressBarGuiV = progressBarGuiXYWHUV[5];
		
		energyBarGuiX = energyBarGuiXYWHUV[0];
		energyBarGuiY = energyBarGuiXYWHUV[1];
		energyBarGuiW = energyBarGuiXYWHUV[2];
		energyBarGuiH = energyBarGuiXYWHUV[3];
		energyBarGuiU = energyBarGuiXYWHUV[4];
		energyBarGuiV = energyBarGuiXYWHUV[5];
		
		machineConfigGuiX = machineConfigGuiXY[0];
		machineConfigGuiY = machineConfigGuiXY[1];
		
		redstoneControlGuiX = redstoneControlGuiXY[0];
		redstoneControlGuiY = redstoneControlGuiXY[1];
		
		this.jeiCategoryEnabled = jeiCategoryEnabled;
		
		this.jeiCategoryUid = jeiCategoryUid;
		this.jeiTitle = jeiTitle;
		this.jeiTexture = jeiTexture;
		
		jeiBackgroundX = jeiBackgroundXYWH[0];
		jeiBackgroundY = jeiBackgroundXYWH[1];
		jeiBackgroundW = jeiBackgroundXYWH[2];
		jeiBackgroundH = jeiBackgroundXYWH[3];
		
		jeiTooltipX = jeiTooltipXYWH[0];
		jeiTooltipY = jeiTooltipXYWH[1];
		jeiTooltipW = jeiTooltipXYWH[2];
		jeiTooltipH = jeiTooltipXYWH[3];
		
		jeiClickAreaX = jeiClickAreaXYWH[0];
		jeiClickAreaY = jeiClickAreaXYWH[1];
		jeiClickAreaW = jeiClickAreaXYWH[2];
		jeiClickAreaH = jeiClickAreaXYWH[3];
	}
	
	public BasicRecipeHandler getRecipeHandler() {
		return NCRecipes.getHandler(name);
	}
	
	@Override
	public Object getNewContainer(int id, EntityPlayer player, TILE tile) {
		return (id == getGuiId() ? containerFunction : configContainerFunction).apply(player, tile);
	}
	
	@Override
	public Object getNewGui(int id, EntityPlayer player, TILE tile) {
		return (id == getGuiId() ? guiFunction : configGuiFunction).apply(player, tile);
	}
	
	public int getInventorySize() {
		return itemInputSize + itemOutputSize;
	}
	
	public int getCombinedInventorySize() {
		return 36 + getInventorySize();
	}
	
	public int getTankCount() {
		return fluidInputSize + fluidOutputSize;
	}
	
	public EnergyConnection defaultEnergyConnection() {
		return defaultProcessPower == 0 ? EnergyConnection.NON : (isGenerator ? EnergyConnection.OUT : EnergyConnection.IN);
	}
	
	public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
		return NonNullList.withSize(getInventorySize(), ItemStack.EMPTY);
	}
	
	public @Nonnull NonNullList<ItemStack> getConsumedStacks() {
		return NonNullList.withSize(consumesInputs ? itemInputSize : 0, ItemStack.EMPTY);
	}
	
	public @Nonnull List<Tank> getConsumedTanks() {
		@Nonnull List<Tank> consumedTanks = new ArrayList<>();
		if (consumesInputs) {
			for (int i = 0; i < fluidInputSize; ++i) {
				consumedTanks.add(new Tank(inputTankCapacity, new ArrayList<>()));
			}
		}
		return consumedTanks;
	}
	
	public int getMachineConfigButtonID() {
		return getTankCount();
	}
	
	public int getRedstoneControlButtonID() {
		return getTankCount() + 1;
	}
	
	public List<ItemSorption> defaultItemSorptions() {
		List<ItemSorption> itemSorptions = new ArrayList<>();
		for (int i = 0; i < itemInputSize; ++i) {
			itemSorptions.add(ItemSorption.IN);
		}
		for (int i = 0; i < itemOutputSize; ++i) {
			itemSorptions.add(ItemSorption.OUT);
		}
		return itemSorptions;
	}
	
	public List<ItemSorption> nonItemSorptions() {
		List<ItemSorption> itemSorptions = new ArrayList<>();
		for (int i = 0; i < getInventorySize(); ++i) {
			itemSorptions.add(ItemSorption.NON);
		}
		return itemSorptions;
	}
	
	public IntList defaultTankCapacities() {
		IntList tankCapacities = new IntArrayList();
		for (int i = 0; i < fluidInputSize; ++i) {
			tankCapacities.add(inputTankCapacity);
		}
		for (int i = 0; i < fluidOutputSize; ++i) {
			tankCapacities.add(outputTankCapacity);
		}
		return tankCapacities;
	}
	
	public List<TankSorption> defaultTankSorptions() {
		List<TankSorption> tankSorptions = new ArrayList<>();
		for (int i = 0; i < fluidInputSize; ++i) {
			tankSorptions.add(TankSorption.IN);
		}
		for (int i = 0; i < fluidOutputSize; ++i) {
			tankSorptions.add(TankSorption.OUT);
		}
		return tankSorptions;
	}
	
	public List<TankSorption> nonTankSorptions() {
		List<TankSorption> tankSorptions = new ArrayList<>();
		for (int i = 0; i < getTankCount(); ++i) {
			tankSorptions.add(TankSorption.NON);
		}
		return tankSorptions;
	}
	
	public void addPlayerSlots(Consumer<Slot> addSlotToContainer, EntityPlayer player) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer.accept(new Slot(player.inventory, j + 9 * i + 9, playerGuiX + 18 * j, playerGuiY + 18 * i));
			}
		}
		
		for (int i = 0; i < 9; ++i) {
			addSlotToContainer.accept(new Slot(player.inventory, i, playerGuiX + 18 * i, 58 + playerGuiY));
		}
	}
}
