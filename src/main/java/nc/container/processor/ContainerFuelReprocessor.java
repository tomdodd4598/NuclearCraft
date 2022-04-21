package nc.container.processor;

import nc.container.slot.*;
import nc.recipe.NCRecipes;
import nc.tile.processor.TileItemProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerFuelReprocessor extends ContainerItemProcessor<TileItemProcessor> {
	
	public ContainerFuelReprocessor(EntityPlayer player, TileItemProcessor tileEntity) {
		super(player, tileEntity, NCRecipes.fuel_reprocessor);
		
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipeHandler, 0, 30, 41));
		
		addSlotToContainer(new SlotFurnace(player, tileEntity, 1, 86, 31));
		addSlotToContainer(new SlotFurnace(player, tileEntity, 2, 106, 31));
		addSlotToContainer(new SlotFurnace(player, tileEntity, 3, 126, 31));
		addSlotToContainer(new SlotFurnace(player, tileEntity, 4, 146, 31));
		addSlotToContainer(new SlotFurnace(player, tileEntity, 5, 86, 51));
		addSlotToContainer(new SlotFurnace(player, tileEntity, 6, 106, 51));
		addSlotToContainer(new SlotFurnace(player, tileEntity, 7, 126, 51));
		addSlotToContainer(new SlotFurnace(player, tileEntity, 8, 146, 51));
		
		addSlotToContainer(new SlotSpecificInput(tileEntity, 9, 132, 76, SPEED_UPGRADE));
		addSlotToContainer(new SlotSpecificInput(tileEntity, 10, 152, 76, ENERGY_UPGRADE));
		
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(player.inventory, j + 9 * i + 9, 8 + 18 * j, 96 + 18 * i));
			}
		}
		
		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + 18 * i, 154));
		}
	}
}
