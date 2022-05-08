package nc.tile.processor;

import java.util.*;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.ints.*;
import nc.recipe.RecipeStats;
import nc.recipe.ingredient.*;
import nc.tile.dummy.IInterfaceable;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.inventory.ITileInventory;
import nc.util.NCMath;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;

public abstract interface IProcessor<INFO extends ProcessorContainerInfo<?>> extends ITickable, ITileInventory, ITileFluid, IInterfaceable {
	
	public INFO getContainerInfo();
	
	public int getItemInputSize();
	
	public int getFluidInputSize();
	
	public int getItemOutputSize();
	
	public int getFluidOutputSize();
	
	public boolean getConsumesInputs();
	
	public boolean getLosesProgress();
	
	public List<ItemStack> getItemInputs(boolean consumed);
	
	public List<Tank> getFluidInputs(boolean consumed);
	
	public @Nonnull NonNullList<ItemStack> getConsumedStacks();
	
	public @Nonnull List<Tank> getConsumedTanks();
	
	public List<IItemIngredient> getItemIngredients();
	
	public List<IItemIngredient> getItemProducts();
	
	public List<IFluidIngredient> getFluidIngredients();
	
	public List<IFluidIngredient> getFluidProducts();
	
	public void refreshRecipe();
	
	public void refreshActivity();
	
	public void refreshActivityOnProduction();
	
	public void refreshEnergyCapacity();
	
	public static int energyCapacity(ProcessorContainerInfo<?> containerInfo, double speedMultiplier, double powerMultiplier) {
		String name = containerInfo.name;
		return NCMath.toInt(Math.ceil(RecipeStats.getProcessorMaxBaseProcessTime(name) / speedMultiplier) * Math.ceil(RecipeStats.getProcessorMaxBaseProcessPower(name) * powerMultiplier));
	}
	
	public static List<ItemSorption> defaultItemSorptions(ProcessorContainerInfo<?> containerInfo) {
		List<ItemSorption> itemSorptions = new ArrayList<>();
		for (int i = 0; i < inSize; ++i) {
			itemSorptions.add(ItemSorption.IN);
		}
		for (int i = 0; i < outSize; ++i) {
			itemSorptions.add(ItemSorption.OUT);
		}
		if (upgrades) {
			itemSorptions.add(ItemSorption.IN);
			itemSorptions.add(ItemSorption.IN);
		}
		return itemSorptions;
	}
	
	public static IntList defaultTankCapacities(ProcessorContainerInfo<?> containerInfo) {
		IntList tankCapacities = new IntArrayList();
		for (int i = 0; i < inSize + outSize; ++i) {
			tankCapacities.add(capacity);
		}
		return tankCapacities;
	}
	
	public static List<TankSorption> defaultTankSorptions(ProcessorContainerInfo<?> containerInfo) {
		List<TankSorption> tankSorptions = new ArrayList<>();
		for (int i = 0; i < inSize; ++i) {
			tankSorptions.add(TankSorption.IN);
		}
		for (int i = 0; i < outSize; ++i) {
			tankSorptions.add(TankSorption.OUT);
		}
		return tankSorptions;
	}
}
