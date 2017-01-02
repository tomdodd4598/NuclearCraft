package nc.container.machine;

import nc.crafting.machine.RecyclerRecipes;
import nc.tile.machine.TileRecycler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerRecycler extends ContainerMachineBase {
	public ContainerRecycler(InventoryPlayer inventory, TileRecycler tileentity) {
		super(inventory, tileentity, RecyclerRecipes.instance());
		addSlotToContainer(new Slot(tileentity, 0, 41, 43));
		addSlotToContainer(new SlotFurnace(inventory.player, tileentity, 1, 130, 33));
		addSlotToContainer(new SlotFurnace(inventory.player, tileentity, 2, 150, 33));
		addSlotToContainer(new SlotFurnace(inventory.player, tileentity, 3, 130, 53));
		addSlotToContainer(new SlotFurnace(inventory.player, tileentity, 4, 150, 53));
		addSlotToContainer(new Slot(tileentity, 5, 31, 20));
		addSlotToContainer(new Slot(tileentity, 6, 51, 20));
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