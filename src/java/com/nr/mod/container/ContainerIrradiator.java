package com.nr.mod.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

import com.nr.mod.blocks.tileentities.TileEntityIrradiator;
import com.nr.mod.crafting.IrradiatorRecipes;

public class ContainerIrradiator extends ContainerMachine {
	public ContainerIrradiator(InventoryPlayer inventory, TileEntityIrradiator tileentity) {
		super(inventory, tileentity, IrradiatorRecipes.instance());
		addSlotToContainer(new Slot(tileentity, 0, 41, 38));
		addSlotToContainer(new Slot(tileentity, 1, 41, 58));
		addSlotToContainer(new SlotBlockedInventory(tileentity, 2, 130, 38));
		addSlotToContainer(new SlotBlockedInventory(tileentity, 3, 150, 38));
		addSlotToContainer(new SlotBlockedInventory(tileentity, 4, 130, 58));
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