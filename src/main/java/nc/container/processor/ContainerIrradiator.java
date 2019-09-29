package nc.container.processor;

import nc.container.SlotSpecificInput;
import nc.recipe.NCRecipes;
import nc.tile.processor.TileFluidProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerIrradiator extends ContainerFluidProcessor {

	public ContainerIrradiator(EntityPlayer player, TileFluidProcessor tileEntity) {
		super(player, tileEntity, NCRecipes.irradiator);
		
		addSlotToContainer(new SlotSpecificInput(tileEntity, 0, 132, 64, speedUpgrade));
		addSlotToContainer(new SlotSpecificInput(tileEntity, 1, 152, 64, energyUpgrade));
		
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
