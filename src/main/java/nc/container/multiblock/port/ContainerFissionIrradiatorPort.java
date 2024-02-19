package nc.container.multiblock.port;

import nc.container.slot.*;
import nc.multiblock.fission.*;
import nc.network.tile.multiblock.port.ItemPortUpdatePacket;
import nc.recipe.*;
import nc.tile.fission.*;
import nc.tile.fission.port.TileFissionIrradiatorPort;
import nc.tile.fission.port.TileFissionIrradiatorPort.FissionIrradiatorPortContainerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerFissionIrradiatorPort extends ContainerPort<FissionReactor, FissionReactorLogic, IFissionPart, TileFissionIrradiatorPort, TileFissionIrradiator, ItemPortUpdatePacket, FissionIrradiatorPortContainerInfo> {
	
	public ContainerFissionIrradiatorPort(EntityPlayer player, TileFissionIrradiatorPort tile) {
		super(player, tile);
		
		addSlotToContainer(new SlotFiltered.ProcessorInput(tile, NCRecipes.fission_irradiator, 0, 44, 35));
		addSlotToContainer(new SlotFurnace(player, tile, 1, 116, 35));
		
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(player.inventory, j + 9 * i + 9, 8 + 18 * j, 84 + 18 * i));
			}
		}
		
		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + 18 * i, 142));
		}
	}
	
	@Override
	protected BasicRecipeHandler getRecipeHandler() {
		return NCRecipes.fission_irradiator;
	}
}
