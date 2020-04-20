package nc.multiblock.container;

import nc.container.generator.ContainerFluidGenerator;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import nc.recipe.NCRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerSaltFissionVessel extends ContainerFluidGenerator<TileSaltFissionVessel> {
	
	public ContainerSaltFissionVessel(EntityPlayer player, TileSaltFissionVessel vessel) {
		super(player, vessel, NCRecipes.salt_fission);
		
		vessel.beginUpdatingPlayer(player);
		
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
