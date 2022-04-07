package nc.multiblock;

import java.util.Set;

import nc.multiblock.tile.ITileMultiblockPart;
import nc.network.PacketHandler;
import nc.network.multiblock.MultiblockUpdatePacket;
import net.minecraft.entity.player.*;

public interface IPacketMultiblock<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>, PACKET extends MultiblockUpdatePacket> extends IMultiblock<MULTIBLOCK, T> {
	
	public Set<EntityPlayer> getMultiblockUpdatePacketListeners();
	
	public PACKET getMultiblockUpdatePacket();
	
	public void onMultiblockUpdatePacket(PACKET message);
	
	public default void addMultiblockUpdatePacketListener(EntityPlayer playerToUpdate) {
		getMultiblockUpdatePacketListeners().add(playerToUpdate);
		sendMultiblockUpdatePacketToPlayer(playerToUpdate);
	}
	
	public default void removeMultiblockUpdatePacketListener(EntityPlayer playerToRemove) {
		getMultiblockUpdatePacketListeners().remove(playerToRemove);
	}
	
	public default void sendMultiblockUpdatePacketToListeners() {
		if (getWorld().isRemote) {
			return;
		}
		PACKET packet = getMultiblockUpdatePacket();
		if (packet == null) {
			return;
		}
		for (EntityPlayer player : getMultiblockUpdatePacketListeners()) {
			PacketHandler.instance.sendTo(packet, (EntityPlayerMP) player);
		}
	}
	
	public default void sendMultiblockUpdatePacketToPlayer(EntityPlayer player) {
		if (getWorld().isRemote) {
			return;
		}
		PACKET packet = getMultiblockUpdatePacket();
		if (packet == null) {
			return;
		}
		PacketHandler.instance.sendTo(packet, (EntityPlayerMP) player);
	}
	
	public default void sendMultiblockUpdatePacketToAll() {
		if (getWorld().isRemote) {
			return;
		}
		PACKET packet = getMultiblockUpdatePacket();
		if (packet == null) {
			return;
		}
		PacketHandler.instance.sendToAll(packet);
	}
}
