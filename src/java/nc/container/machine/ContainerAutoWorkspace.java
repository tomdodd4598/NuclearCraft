package nc.container.machine;

import nc.crafting.machine.AutoWorkspaceRecipes;
import nc.tile.machine.TileAutoWorkspace;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerAutoWorkspace extends ContainerMachine {
	public ContainerAutoWorkspace(InventoryPlayer inventory, TileAutoWorkspace tileentity) {
		super(inventory, tileentity, AutoWorkspaceRecipes.instance());
		addSlotToContainer(new Slot(tileentity, 0, 8, 21));
		addSlotToContainer(new Slot(tileentity, 1, 26, 21));
		addSlotToContainer(new Slot(tileentity, 2, 44, 21));
		addSlotToContainer(new Slot(tileentity, 3, 8, 39));
		addSlotToContainer(new Slot(tileentity, 4, 26, 39));
		addSlotToContainer(new Slot(tileentity, 5, 44, 39));
		addSlotToContainer(new Slot(tileentity, 6, 8, 57));
		addSlotToContainer(new Slot(tileentity, 7, 26, 57));
		addSlotToContainer(new Slot(tileentity, 8, 44, 57));
		addSlotToContainer(new SlotFurnace(inventory.player, tileentity, 9, 134, 39));
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