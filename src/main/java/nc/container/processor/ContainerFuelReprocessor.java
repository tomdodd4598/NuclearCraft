package nc.container.processor;

import nc.container.SlotProcessorInput;
import nc.container.SlotSpecificInput;
import nc.recipe.NCRecipes;
import nc.tile.processor.TileItemProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;

public class ContainerFuelReprocessor extends ContainerItemProcessor {

	public ContainerFuelReprocessor(EntityPlayer player, TileItemProcessor tileEntity) {
		super(player, tileEntity, NCRecipes.fuel_reprocessor);
		
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipeHandler, 0, 50, 41));
		
		addSlotToContainer(new SlotFurnaceOutput(player, tileEntity, 1, 106, 31));
		addSlotToContainer(new SlotFurnaceOutput(player, tileEntity, 2, 126, 31));
		addSlotToContainer(new SlotFurnaceOutput(player, tileEntity, 3, 106, 51));
		addSlotToContainer(new SlotFurnaceOutput(player, tileEntity, 4, 126, 51));
		
		addSlotToContainer(new SlotSpecificInput(tileEntity, 5, 132, 76, speedUpgrade));
		addSlotToContainer(new SlotSpecificInput(tileEntity, 6, 152, 76, energyUpgrade));
		
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
