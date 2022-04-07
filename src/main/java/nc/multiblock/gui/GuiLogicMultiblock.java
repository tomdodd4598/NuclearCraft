package nc.multiblock.gui;

import nc.multiblock.*;
import nc.multiblock.tile.*;
import nc.network.multiblock.MultiblockUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;

public abstract class GuiLogicMultiblock<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T> & IPacketMultiblock<MULTIBLOCK, T, PACKET>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, PACKET extends MultiblockUpdatePacket, GUITILE extends IMultiblockGuiPart<MULTIBLOCK, T, PACKET, GUITILE>, L extends LOGIC> extends GuiMultiblock<MULTIBLOCK, T, PACKET, GUITILE> {
	
	protected final LOGIC logic;
	
	public GuiLogicMultiblock(EntityPlayer player, GUITILE tile) {
		super(player, tile);
		logic = multiblock.getLogic();
	}
	
	@SuppressWarnings("unchecked")
	protected L getLogic() {
		return (L) logic;
	}
}
