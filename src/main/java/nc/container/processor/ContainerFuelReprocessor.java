package nc.container.processor;

import nc.container.slot.SlotFurnace;
import nc.container.slot.SlotProcessorInput;
import nc.container.slot.SlotSpecificInput;
import nc.recipe.NCRecipes;
import nc.tile.processor.TileItemProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerFuelReprocessor extends ContainerItemProcessor {

	public ContainerFuelReprocessor(EntityPlayer player, TileItemProcessor tileEntity) {
		super(player, tileEntity, NCRecipes.fuel_reprocessor);
		
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipeHandler, 0, 40, 41));
		
		addSlotToContainer(new SlotFurnace(player, tileEntity, 1, 96, 31));
		addSlotToContainer(new SlotFurnace(player, tileEntity, 2, 116, 31));
		addSlotToContainer(new SlotFurnace(player, tileEntity, 3, 136, 31));
		addSlotToContainer(new SlotFurnace(player, tileEntity, 4, 96, 51));
		addSlotToContainer(new SlotFurnace(player, tileEntity, 5, 116, 51));
		addSlotToContainer(new SlotFurnace(player, tileEntity, 6, 136, 51));
		
		addSlotToContainer(new SlotSpecificInput(tileEntity, 7, 132, 76, SPEED_UPGRADE));
		addSlotToContainer(new SlotSpecificInput(tileEntity, 8, 152, 76, ENERGY_UPGRADE));
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + 9*i + 9, 8 + 18*j, 96 + 18*i));
			}
		}
		
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + 18*i, 154));
		}
	}
}
