package nc.container.generator;

import nc.container.SlotFurnace;
import nc.container.SlotInaccessible;
import nc.container.SlotProcessorInput;
import nc.recipe.NCRecipes;
import nc.tile.generator.TileFissionController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerFissionController extends ContainerItemGenerator<TileFissionController> {
	
	public ContainerFissionController(EntityPlayer player, TileFissionController tileEntity) {
		super(tileEntity, NCRecipes.fission);
		
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipeHandler, 0, 56, 35));
		
		addSlotToContainer(new SlotFurnace(player, tileEntity, 1, 116, 35));
		
		addSlotToContainer(new SlotInaccessible(tileEntity, 2, -4095, -4095));
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + 9*i + 9, 8 + 18*j, 95 + 18*i));
			}
		}
		
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + 18*i, 153));
		}
		
		tileEntity.beginUpdatingPlayer(player);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		tile.stopUpdatingPlayer(player);
	}
}
