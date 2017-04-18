package nc.container.machine;

import nc.crafting.machine.SeparatorRecipes;
import nc.tile.machine.TileSeparator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerSeparator extends ContainerMachineBase {
	public ContainerSeparator(InventoryPlayer inventory, TileSeparator tileentity) {
		super(inventory, tileentity, SeparatorRecipes.instance());
		addSlotToContainer(new Slot(tileentity, 0, 41, 43));
		addSlotToContainer(new SlotFurnace(inventory.player, tileentity, 1, 134, 29));
		addSlotToContainer(new SlotFurnace(inventory.player, tileentity, 2, 134, 57));
		addSlotToContainer(new Slot(tileentity, 3, 31, 20));
		addSlotToContainer(new Slot(tileentity, 4, 51, 20));
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 92 + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 150));
		}
	}
}