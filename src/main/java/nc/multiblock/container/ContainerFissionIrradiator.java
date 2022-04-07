package nc.multiblock.container;

import nc.container.generator.ContainerFilteredItemGenerator;
import nc.container.slot.*;
import nc.multiblock.fission.tile.TileFissionIrradiator;
import nc.recipe.NCRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerFissionIrradiator extends ContainerFilteredItemGenerator<TileFissionIrradiator> {
	
	public ContainerFissionIrradiator(EntityPlayer player, TileFissionIrradiator irradiator) {
		super(player, irradiator, NCRecipes.fission_irradiator);
		
		irradiator.addTileUpdatePacketListener(player);
		
		addSlotToContainer(new SlotFiltered.ProcessorInput(irradiator, NCRecipes.fission_irradiator, 0, 56, 35));
		
		addSlotToContainer(new SlotFurnace(player, irradiator, 1, 116, 35));
		
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(player.inventory, j + 9 * i + 9, 8 + 18 * j, 84 + 18 * i));
			}
		}
		
		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + 18 * i, 142));
		}
	}
}
