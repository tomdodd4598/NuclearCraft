package nc.container.multiblock.controller;

import nc.multiblock.machine.Machine;
import nc.network.multiblock.MachineUpdatePacket;
import nc.tile.TileContainerInfo;
import nc.tile.machine.*;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerDecayPoolController extends ContainerMultiblockController<Machine, IMachinePart, MachineUpdatePacket, TileDecayPoolController, TileContainerInfo<TileDecayPoolController>> {
	
	public ContainerDecayPoolController(EntityPlayer player, TileDecayPoolController controller) {
		super(player, controller);
	}
}
