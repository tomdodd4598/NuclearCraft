package nc.container.processor;

import nc.container.SlotProcessorInput;
import nc.container.SlotSpecificInput;
import nc.recipe.NCRecipes;
import nc.tile.processor.TileItemProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;

public class ContainerRockCrusher extends ContainerItemProcessor {

	public ContainerRockCrusher(EntityPlayer player, TileItemProcessor tileEntity) {
		super(player, tileEntity, NCRecipes.Type.ROCK_CRUSHER);
		
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipeType, 0, 38, 35));
		
		addSlotToContainer(new SlotFurnaceOutput(player, tileEntity, 1, 94, 35));
		addSlotToContainer(new SlotFurnaceOutput(player, tileEntity, 2, 114, 35));
		addSlotToContainer(new SlotFurnaceOutput(player, tileEntity, 3, 134, 35));
		
		addSlotToContainer(new SlotSpecificInput(tileEntity, 4, 132, 64, speedUpgrade));
		addSlotToContainer(new SlotSpecificInput(tileEntity, 5, 152, 64, energyUpgrade));
		
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
