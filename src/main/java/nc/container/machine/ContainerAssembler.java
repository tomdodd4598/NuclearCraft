package nc.container.machine;

import nc.crafting.machine.AssemblerRecipes;
import nc.tile.machine.TileAssembler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerAssembler extends ContainerMachineBase {
	public ContainerAssembler(InventoryPlayer inventory, TileAssembler tileentity) {
		super(inventory, tileentity, AssemblerRecipes.instance());
		addSlotToContainer(new Slot(tileentity, 0, 31, 38));
		addSlotToContainer(new Slot(tileentity, 1, 51, 38));
		addSlotToContainer(new Slot(tileentity, 2, 31, 58));
		addSlotToContainer(new Slot(tileentity, 3, 51, 58));
		addSlotToContainer(new SlotFurnace(inventory.player, tileentity, 4, 140, 48));
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