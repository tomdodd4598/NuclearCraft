package com.nr.mod.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import com.nr.mod.blocks.tileentities.TileEntityElectrolyser;
import com.nr.mod.crafting.ElectrolyserRecipes;

public class ContainerElectrolyser extends ContainerMachine {
	public ContainerElectrolyser(InventoryPlayer inventory, TileEntityElectrolyser tileentity) {
		super(inventory, tileentity, ElectrolyserRecipes.instance());
		addSlotToContainer(new Slot(tileentity, 0, 41, 43));
		addSlotToContainer(new SlotBlockedInventory(tileentity, 1, 130, 33));
		addSlotToContainer(new SlotBlockedInventory(tileentity, 2, 150, 33));
		addSlotToContainer(new SlotBlockedInventory(tileentity, 3, 130, 53));
		addSlotToContainer(new SlotBlockedInventory(tileentity, 4, 150, 53));
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