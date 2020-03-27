package nc.container.processor;

import nc.container.slot.SlotFurnace;
import nc.container.slot.SlotProcessorInput;
import nc.container.slot.SlotSpecificInput;
import nc.recipe.NCRecipes;
import nc.tile.processor.TileItemProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerDecayHastener extends ContainerItemProcessor {

	public ContainerDecayHastener(EntityPlayer player, TileItemProcessor tileEntity) {
		super(player, tileEntity, NCRecipes.decay_hastener);
		
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipeHandler, 0, 56, 35));
		
		addSlotToContainer(new SlotFurnace(player, tileEntity, 1, 116, 35));
		
		addSlotToContainer(new SlotSpecificInput(tileEntity, 2, 132, 64, SPEED_UPGRADE));
		addSlotToContainer(new SlotSpecificInput(tileEntity, 3, 152, 64, ENERGY_UPGRADE));
		
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
