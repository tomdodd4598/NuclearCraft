package nc.container.generator;

import nc.recipe.NCRecipes;
import nc.tile.generator.TileFusionCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerFusionCore extends ContainerFluidGenerator<TileFusionCore> {
	
	public ContainerFusionCore(EntityPlayer player, TileFusionCore tileEntity) {
		super(tileEntity, NCRecipes.fusion);
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + 9*i + 9, 8 + 18*j, 105 + 18*i));
			}
		}
		
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + 18*i, 163));
		}
		
		tileEntity.beginUpdatingPlayer(player);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		tile.stopUpdatingPlayer(player);
	}
}
