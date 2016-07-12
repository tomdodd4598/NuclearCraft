package nc.container.machine;

import nc.crafting.machine.IoniserRecipes;
import nc.tile.machine.TileIoniser;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerIoniser extends ContainerMachineBase {
	public ContainerIoniser(InventoryPlayer inventory, TileIoniser tileentity) {
		super(inventory, tileentity, IoniserRecipes.instance());
		addSlotToContainer(new Slot(tileentity, 0, 41, 38));
		addSlotToContainer(new Slot(tileentity, 1, 41, 58));
		addSlotToContainer(new SlotFurnace(inventory.player, tileentity, 2, 134, 34));
		addSlotToContainer(new SlotFurnace(inventory.player, tileentity, 3, 134, 62));
		addSlotToContainer(new Slot(tileentity, 4, 31, 15));
		addSlotToContainer(new Slot(tileentity, 5, 51, 15));
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