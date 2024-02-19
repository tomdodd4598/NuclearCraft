package nc.container.multiblock.port;

import nc.multiblock.fission.*;
import nc.network.tile.multiblock.port.FluidPortUpdatePacket;
import nc.recipe.*;
import nc.tile.fission.*;
import nc.tile.fission.port.TileFissionHeaterPort;
import nc.tile.fission.port.TileFissionHeaterPort.FissionHeaterPortContainerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerFissionHeaterPort extends ContainerPort<FissionReactor, FissionReactorLogic, IFissionPart, TileFissionHeaterPort, TileSaltFissionHeater, FluidPortUpdatePacket, FissionHeaterPortContainerInfo> {
	
	public ContainerFissionHeaterPort(EntityPlayer player, TileFissionHeaterPort tile) {
		super(player, tile);
		
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
		return NCRecipes.coolant_heater;
	}
}
