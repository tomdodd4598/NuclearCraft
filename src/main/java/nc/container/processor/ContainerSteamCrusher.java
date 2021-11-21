package nc.container.processor;

import nc.container.slot.SlotFurnace;
import nc.container.slot.SlotProcessorInput;
import nc.container.slot.SlotSpecificInput;
import nc.recipe.NCRecipes;
import nc.tile.processor.TileItemFluidProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerSteamCrusher extends ContainerItemFluidProcessor {

	public ContainerSteamCrusher(EntityPlayer player, TileItemFluidProcessor tileEntity) {
		super(player, tileEntity, NCRecipes.steam_crusher);
		
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipeHandler, 0, 46, 35));
		
		addSlotToContainer(new SlotFurnace(player, tileEntity, 1, 126, 35));
		
		addSlotToContainer(new SlotSpecificInput(tileEntity, 2, 132, 64, SPEED_UPGRADE));
		
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
