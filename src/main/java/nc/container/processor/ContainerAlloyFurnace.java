package nc.container.processor;

import nc.container.slot.SlotFurnace;
import nc.container.slot.SlotProcessorInput;
import nc.container.slot.SlotSpecificInput;
import nc.recipe.NCRecipes;
import nc.tile.processor.TileItemProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerAlloyFurnace extends ContainerItemProcessor {

	public ContainerAlloyFurnace(EntityPlayer player, TileItemProcessor tileEntity) {
		super(player, tileEntity, NCRecipes.alloy_furnace);
		
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipeHandler, 0, 46, 35));
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipeHandler, 1, 66, 35));
		
		addSlotToContainer(new SlotFurnace(player, tileEntity, 2, 126, 35));
		
		addSlotToContainer(new SlotSpecificInput(tileEntity, 3, 132, 64, SPEED_UPGRADE));
		addSlotToContainer(new SlotSpecificInput(tileEntity, 4, 152, 64, ENERGY_UPGRADE));
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + 9*i + 9, 8 + 18*j, 84 + 18*i));
			}
		}
		
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + 18*i, 142));
		}
	}
}
