package nc.container.processor;

import nc.container.slot.SlotSpecificInput;
import nc.init.NCItems;
import nc.recipe.BasicRecipeHandler;
import nc.tile.processor.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerBasicUpgradeProcessor<TILE extends TileBasicUpgradeProcessor<TILE>> extends ContainerProcessor<TILE, BasicUpgradeProcessorContainerInfo<TILE>> {
	
	public ContainerBasicUpgradeProcessor(EntityPlayer player, TILE tile, BasicRecipeHandler recipeHandler) {
		super(player, tile, recipeHandler);
	}
	
	@Override
	protected void addMachineSlots(EntityPlayer player) {
		super.addMachineSlots(player);
		
		addSlotToContainer(new SlotSpecificInput(tile, info.getSpeedUpgradeSlot(), info.speedUpgradeGuiX, info.speedUpgradeGuiY, speedUpgrade()));
		addSlotToContainer(new SlotSpecificInput(tile, info.getEnergyUpgradeSlot(), info.energyUpgradeGuiX, info.energyUpgradeGuiY, energyUpgrade()));
	}
	
	@Override
	public ItemStack transferPlayerStack(EntityPlayer player, int index, int invStart, int invEnd, ItemStack stack) {
		if (stack.getItem() == NCItems.upgrade) {
			int speedUpgradeSlot = info.itemInputSize + info.itemOutputSize;
			int energyUpgradeSlot = info.itemInputSize + info.itemOutputSize + 1;
			
			if (tile.isItemValidForSlot(speedUpgradeSlot, stack)) {
				if (!mergeItemStack(stack, speedUpgradeSlot, speedUpgradeSlot + 1, false)) {
					return ItemStack.EMPTY;
				}
			}
			else if (tile.isItemValidForSlot(energyUpgradeSlot, stack)) {
				if (!mergeItemStack(stack, energyUpgradeSlot, energyUpgradeSlot + 1, false)) {
					return ItemStack.EMPTY;
				}
			}
		}
		return super.transferPlayerStack(player, index, invStart, invEnd, stack);
	}
	
	private static ItemStack speed_upgrade = null;
	private static ItemStack energy_upgrade = null;
	
	public static final ItemStack speedUpgrade() {
		if (speed_upgrade == null) {
			speed_upgrade = new ItemStack(NCItems.upgrade, 1, 0);
		}
		return speed_upgrade;
	}
	
	public static final ItemStack energyUpgrade() {
		if (energy_upgrade == null) {
			energy_upgrade = new ItemStack(NCItems.upgrade, 1, 1);
		}
		return energy_upgrade;
	}
}
