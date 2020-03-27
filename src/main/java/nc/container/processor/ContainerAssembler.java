package nc.container.processor;

import nc.container.slot.SlotFurnace;
import nc.container.slot.SlotProcessorInput;
import nc.container.slot.SlotSpecificInput;
import nc.recipe.NCRecipes;
import nc.tile.processor.TileItemProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerAssembler extends ContainerItemProcessor {

	public ContainerAssembler(EntityPlayer player, TileItemProcessor tileEntity) {
		super(player, tileEntity, NCRecipes.assembler);
		
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipeHandler, 0, 46, 31));
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipeHandler, 1, 66, 31));
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipeHandler, 2, 46, 51));
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipeHandler, 3, 66, 51));
		
		addSlotToContainer(new SlotFurnace(player, tileEntity, 4, 126, 41));
		
		addSlotToContainer(new SlotSpecificInput(tileEntity, 5, 132, 76, SPEED_UPGRADE));
		addSlotToContainer(new SlotSpecificInput(tileEntity, 6, 152, 76, ENERGY_UPGRADE));
		
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
