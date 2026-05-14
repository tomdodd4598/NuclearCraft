package nc.tile.processor.info;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.container.ContainerFunction;
import nc.gui.GuiFunction;
import nc.integration.jei.category.info.JEIContainerConnection;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.recipe.*;
import nc.tile.TileContainerInfo;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.builder.ProcessorContainerInfoBuilder;
import nc.util.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;

public abstract class ProcessorContainerInfo<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>> extends TileContainerInfo<TILE> {
	
	protected final Class<? extends Container> containerClass;
	protected final Class<? extends GuiContainer> guiClass;
	
	protected final ContainerFunction<TILE> configContainerFunction;
	protected final GuiFunction<TILE> configGuiFunction;
	
	public final String recipeHandlerName;
	
	public final int itemInputSize;
	public final int fluidInputSize;
	public final int itemOutputSize;
	public final int fluidOutputSize;
	
	public final int[] itemInputSlots;
	public final int[] itemOutputSlots;
	
	public final int[] fluidInputTanks;
	public final int[] fluidOutputTanks;
	
	public int inputTankCapacity;
	public int outputTankCapacity;
	
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
	
	public double maxBaseProcessTime = 1D;
	public double maxBaseProcessPower = 0D;
	
	protected ProcessorContainerInfo(ProcessorContainerInfoBuilder<TILE, PACKET, INFO, ?> builder) {
		super(builder.modId, builder.name, builder.tileClass, builder.containerFunction, builder.guiFunction);
		
		containerClass = builder.containerClass;
		guiClass = builder.guiClass;
		
		configContainerFunction = builder.configContainerFunction;
		configGuiFunction = builder.configGuiFunction;
		
		recipeHandlerName = builder.recipeHandlerName;
		
		itemInputSize = builder.itemInputGuiXYWH.size();
		fluidInputSize = builder.fluidInputGuiXYWH.size();
		itemOutputSize = builder.itemOutputGuiXYWH.size();
		fluidOutputSize = builder.fluidOutputGuiXYWH.size();
		
		itemInputSlots = CollectionHelper.increasingArray(itemInputSize);
		itemOutputSlots = CollectionHelper.increasingArray(itemInputSize, itemOutputSize);
		
		fluidInputTanks = CollectionHelper.increasingArray(fluidInputSize);
		fluidOutputTanks = CollectionHelper.increasingArray(fluidInputSize, fluidOutputSize);
		
		inputTankCapacity = builder.inputTankCapacity;
		outputTankCapacity = builder.outputTankCapacity;
		
		defaultProcessTime = builder.defaultProcessTime;
		defaultProcessPower = builder.defaultProcessPower;
		
		isGenerator = builder.isGenerator;
		
		consumesInputs = builder.consumesInputs;
		losesProgress = builder.losesProgress;
		
		ocComponentName = builder.ocComponentName;
		
		guiWidth = builder.guiWH[0];
		guiHeight = builder.guiWH[1];
		
		itemInputGuiXYWH = builder.itemInputGuiXYWH;
		fluidInputGuiXYWH = builder.fluidInputGuiXYWH;
		itemOutputGuiXYWH = builder.itemOutputGuiXYWH;
		fluidOutputGuiXYWH = builder.fluidOutputGuiXYWH;
		
		itemInputStackXY = ContainerInfoHelper.stackXYList(itemInputGuiXYWH);
		itemOutputStackXY = ContainerInfoHelper.stackXYList(itemOutputGuiXYWH);
		
		itemInputSorptionButtonID = CollectionHelper.increasingArray(itemInputSize);
		fluidInputSorptionButtonID = CollectionHelper.increasingArray(itemInputSize, fluidInputSize);
		itemOutputSorptionButtonID = CollectionHelper.increasingArray(itemInputSize + fluidInputSize, itemOutputSize);
		fluidOutputSorptionButtonID = CollectionHelper.increasingArray(itemInputSize + fluidInputSize + itemOutputSize, fluidOutputSize);
		
		playerGuiX = builder.playerGuiXY[0];
		playerGuiY = builder.playerGuiXY[1];
		
		progressBarGuiX = builder.progressBarGuiXYWHUV[0];
		progressBarGuiY = builder.progressBarGuiXYWHUV[1];
		progressBarGuiW = builder.progressBarGuiXYWHUV[2];
		progressBarGuiH = builder.progressBarGuiXYWHUV[3];
		progressBarGuiU = builder.progressBarGuiXYWHUV[4];
		progressBarGuiV = builder.progressBarGuiXYWHUV[5];
		
		energyBarGuiX = builder.energyBarGuiXYWHUV[0];
		energyBarGuiY = builder.energyBarGuiXYWHUV[1];
		energyBarGuiW = builder.energyBarGuiXYWHUV[2];
		energyBarGuiH = builder.energyBarGuiXYWHUV[3];
		energyBarGuiU = builder.energyBarGuiXYWHUV[4];
		energyBarGuiV = builder.energyBarGuiXYWHUV[5];
		
		machineConfigGuiX = builder.machineConfigGuiXY[0];
		machineConfigGuiY = builder.machineConfigGuiXY[1];
		
		redstoneControlGuiX = builder.redstoneControlGuiXY[0];
		redstoneControlGuiY = builder.redstoneControlGuiXY[1];
		
		jeiCategoryEnabled = builder.jeiCategoryEnabled;
		
		jeiCategoryUid = builder.jeiCategoryUid;
		jeiTitle = builder.jeiTitle;
		jeiTexture = builder.jeiTexture;
		
		jeiBackgroundX = builder.jeiBackgroundXYWH[0];
		jeiBackgroundY = builder.jeiBackgroundXYWH[1];
		jeiBackgroundW = builder.jeiBackgroundXYWH[2];
		jeiBackgroundH = builder.jeiBackgroundXYWH[3];
		
		jeiTooltipX = builder.jeiTooltipXYWH[0];
		jeiTooltipY = builder.jeiTooltipXYWH[1];
		jeiTooltipW = builder.jeiTooltipXYWH[2];
		jeiTooltipH = builder.jeiTooltipXYWH[3];
		
		jeiClickAreaX = builder.jeiClickAreaXYWH[0];
		jeiClickAreaY = builder.jeiClickAreaXYWH[1];
		jeiClickAreaW = builder.jeiClickAreaXYWH[2];
		jeiClickAreaH = builder.jeiClickAreaXYWH[3];
	}
	
	public BasicRecipeHandler getRecipeHandler() {
		return NCRecipes.getHandler(recipeHandlerName);
	}
	
	@Override
	public Object getNewContainer(int id, EntityPlayer player, TILE tile) {
		return (id == guiId ? containerFunction : configContainerFunction).apply(player, tile);
	}
	
	@Override
	public Object getNewGui(int id, EntityPlayer player, TILE tile) {
		return (id == guiId ? guiFunction : configGuiFunction).apply(player, tile);
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
		return maxBaseProcessPower <= 0 ? EnergyConnection.NON : (isGenerator ? EnergyConnection.OUT : EnergyConnection.IN);
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
				consumedTanks.add(new Tank(inputTankCapacity, new ObjectOpenHashSet<>()));
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
	
	public long getEnergyCapacity(double speedMultiplier, double powerMultiplier) {
		return (long) (Math.ceil(maxBaseProcessTime / speedMultiplier) * Math.ceil(maxBaseProcessPower * powerMultiplier));
	}
	
	public JEIContainerConnection getJEIContainerConnection() {
		return new JEIContainerConnection(containerClass, guiClass, 0, itemInputSize, getInventorySize(), jeiClickAreaX, jeiClickAreaY, jeiClickAreaW, jeiClickAreaH);
	}
}
