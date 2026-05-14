package nc.container.multiblock.port;

import nc.container.slot.*;
import nc.multiblock.fission.*;
import nc.network.tile.multiblock.port.ItemPortUpdatePacket;
import nc.recipe.*;
import nc.tile.TileContainerInfo;
import nc.tile.fission.*;
import nc.tile.fission.port.TileFissionChamberPort;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerFissionChamberPort extends ContainerPort<FissionReactor, FissionReactorLogic, IFissionPart, TileFissionChamberPort, TilePebbleFissionChamber, ItemPortUpdatePacket, TileContainerInfo<TileFissionChamberPort>> {
	
	public ContainerFissionChamberPort(EntityPlayer player, TileFissionChamberPort tile) {
		super(player, tile);
		
		addSlotToContainer(new SlotFiltered.ProcessorInput(tile, NCRecipes.pebble_fission, 0, 44, 35));
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
		return NCRecipes.pebble_fission;
	}
}
