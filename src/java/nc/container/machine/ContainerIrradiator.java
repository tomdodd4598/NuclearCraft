package nc.container.machine;

import nc.crafting.machine.IrradiatorRecipes;
import nc.tile.machine.TileIrradiator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerIrradiator extends ContainerMachineBase {
	public ContainerIrradiator(InventoryPlayer inventory, TileIrradiator tileentity) {
		super(inventory, tileentity, IrradiatorRecipes.instance());
		addSlotToContainer(new Slot(tileentity, 0, 41, 38));
		addSlotToContainer(new Slot(tileentity, 1, 41, 58));
		addSlotToContainer(new SlotFurnace(inventory.player, tileentity, 2, 130, 38));
		addSlotToContainer(new SlotFurnace(inventory.player, tileentity, 3, 150, 48));
		addSlotToContainer(new SlotFurnace(inventory.player, tileentity, 4, 130, 58));
		addSlotToContainer(new Slot(tileentity, 5, 31, 15));
		addSlotToContainer(new Slot(tileentity, 6, 51, 15));
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