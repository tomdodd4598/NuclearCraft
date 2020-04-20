package nc.multiblock.container;

import nc.container.processor.ContainerFluidProcessor;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.recipe.NCRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerSaltFissionHeater extends ContainerFluidProcessor<TileSaltFissionHeater> {
	
	public ContainerSaltFissionHeater(EntityPlayer player, TileSaltFissionHeater heater) {
		super(player, heater, NCRecipes.coolant_heater);
		
		heater.beginUpdatingPlayer(player);
		
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
