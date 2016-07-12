package nc.container.machine;

import nc.crafting.machine.CollectorRecipes;
import nc.tile.machine.TileCollector;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerCollector extends ContainerMachineBase {
	public ContainerCollector(InventoryPlayer inventory, TileCollector tileentity) {
		super(inventory, tileentity, CollectorRecipes.instance());
		addSlotToContainer(new Slot(tileentity, 0, 12, 39));
		addSlotToContainer(new SlotFurnace(inventory.player, tileentity, 1, 148, 39));
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