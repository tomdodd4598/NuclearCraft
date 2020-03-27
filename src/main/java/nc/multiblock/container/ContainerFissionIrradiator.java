package nc.multiblock.container;

import nc.container.processor.ContainerFilteredItemProcessor;
import nc.container.slot.SlotFiltered;
import nc.container.slot.SlotFurnace;
import nc.multiblock.fission.tile.TileFissionIrradiator;
import nc.recipe.NCRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerFissionIrradiator extends ContainerFilteredItemProcessor<TileFissionIrradiator> {
	
	public ContainerFissionIrradiator(EntityPlayer player, TileFissionIrradiator irradiator) {
		super(player, irradiator, NCRecipes.fission_irradiator);
		
		irradiator.beginUpdatingPlayer(player);
		
		addSlotToContainer(new SlotFiltered.ProcessorInput(irradiator, NCRecipes.fission_irradiator, 0, 56, 35));
		
		addSlotToContainer(new SlotFurnace(player, irradiator, 1, 116, 35));
		
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
