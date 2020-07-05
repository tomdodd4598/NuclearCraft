package nc.multiblock.container;

import static nc.recipe.NCRecipes.solid_fission;

import nc.container.generator.ContainerFilteredItemGenerator;
import nc.container.slot.*;
import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerSolidFissionCell extends ContainerFilteredItemGenerator<TileSolidFissionCell> {
	
	public ContainerSolidFissionCell(EntityPlayer player, TileSolidFissionCell cell) {
		super(player, cell, solid_fission);
		
		cell.beginUpdatingPlayer(player);
		
		addSlotToContainer(new SlotFiltered.ProcessorInput(cell, solid_fission, 0, 56, 35));
		
		addSlotToContainer(new SlotFurnace(player, cell, 1, 116, 35));
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + 9 * i + 9, 8 + 18 * j, 84 + 18 * i));
			}
		}
		
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + 18 * i, 142));
		}
	}
}
