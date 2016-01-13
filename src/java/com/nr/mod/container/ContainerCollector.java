package com.nr.mod.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import com.nr.mod.blocks.tileentities.TileEntityCollector;
import com.nr.mod.crafting.CollectorRecipes;

public class ContainerCollector extends ContainerMachine {
	public ContainerCollector(InventoryPlayer inventory, TileEntityCollector tileentity) {
		super(inventory, tileentity, CollectorRecipes.instance());
		addSlotToContainer(new Slot(tileentity, 0, 12, 39));
		addSlotToContainer(new SlotBlockedInventory(tileentity, 1, 148, 39));
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