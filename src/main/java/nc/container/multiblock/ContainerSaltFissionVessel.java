package nc.container.multiblock;

import nc.container.generator.ContainerFluidGenerator;
import nc.recipe.NCRecipes;
import nc.tile.fission.TileSaltFissionVessel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerSaltFissionVessel extends ContainerFluidGenerator<TileSaltFissionVessel> {
	
	public ContainerSaltFissionVessel(EntityPlayer player, TileSaltFissionVessel vessel) {
		super(player, vessel, NCRecipes.salt_fission);
		
		vessel.addTileUpdatePacketListener(player);
		
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
