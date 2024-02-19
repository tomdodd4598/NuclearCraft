package nc.container.multiblock.port;

import nc.container.slot.*;
import nc.multiblock.fission.*;
import nc.network.tile.multiblock.port.ItemPortUpdatePacket;
import nc.recipe.*;
import nc.tile.fission.*;
import nc.tile.fission.port.TileFissionCellPort;
import nc.tile.fission.port.TileFissionCellPort.FissionCellPortContainerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerFissionCellPort extends ContainerPort<FissionReactor, FissionReactorLogic, IFissionPart, TileFissionCellPort, TileSolidFissionCell, ItemPortUpdatePacket, FissionCellPortContainerInfo> {
	
	public ContainerFissionCellPort(EntityPlayer player, TileFissionCellPort tile) {
		super(player, tile);
		
		addSlotToContainer(new SlotFiltered.ProcessorInput(tile, NCRecipes.solid_fission, 0, 44, 35));
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
		return NCRecipes.solid_fission;
	}
}
