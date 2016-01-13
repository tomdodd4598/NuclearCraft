package com.nr.mod.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import com.nr.mod.blocks.tileentities.TileEntitySeparator;
import com.nr.mod.crafting.SeparatorRecipes;

public class ContainerSeparator extends ContainerMachine {
	public ContainerSeparator(InventoryPlayer inventory, TileEntitySeparator tileentity) {
		super(inventory, tileentity, SeparatorRecipes.instance());
		addSlotToContainer(new Slot(tileentity, 0, 41, 43));
		addSlotToContainer(new SlotBlockedInventory(tileentity, 1, 134, 29));
		addSlotToContainer(new SlotBlockedInventory(tileentity, 2, 134, 57));
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