package nc.container.processor;

import nc.container.slot.SlotFurnace;
import nc.container.slot.SlotProcessorInput;
import nc.container.slot.SlotSpecificInput;
import nc.recipe.NCRecipes;
import nc.tile.processor.TileItemProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerRockCrusher extends ContainerItemProcessor {

	public ContainerRockCrusher(EntityPlayer player, TileItemProcessor tileEntity) {
		super(player, tileEntity, NCRecipes.rock_crusher);
		
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipeHandler, 0, 38, 35));
		
		addSlotToContainer(new SlotFurnace(player, tileEntity, 1, 94, 35));
		addSlotToContainer(new SlotFurnace(player, tileEntity, 2, 114, 35));
		addSlotToContainer(new SlotFurnace(player, tileEntity, 3, 134, 35));
		
		addSlotToContainer(new SlotSpecificInput(tileEntity, 4, 132, 64, SPEED_UPGRADE));
		addSlotToContainer(new SlotSpecificInput(tileEntity, 5, 152, 64, ENERGY_UPGRADE));
		
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
