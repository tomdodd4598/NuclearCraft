package nc.container.processor;

import static nc.recipe.NCRecipes.chemical_reactor;

import nc.container.slot.SlotSpecificInput;
import nc.tile.processor.TileFluidProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerChemicalReactor extends ContainerFluidProcessor {
	
	public ContainerChemicalReactor(EntityPlayer player, TileFluidProcessor tileEntity) {
		super(player, tileEntity, chemical_reactor);
		
		addSlotToContainer(new SlotSpecificInput(tileEntity, 0, 132, 64, SPEED_UPGRADE));
		addSlotToContainer(new SlotSpecificInput(tileEntity, 1, 152, 64, ENERGY_UPGRADE));
		
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
