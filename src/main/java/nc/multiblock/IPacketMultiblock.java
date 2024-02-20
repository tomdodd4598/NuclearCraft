package nc.multiblock;

import java.util.Set;

import nc.network.PacketHandler;
import nc.network.multiblock.MultiblockUpdatePacket;
import nc.tile.multiblock.ITileMultiblockPart;
import net.minecraft.entity.player.*;

public interface IPacketMultiblock<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>, PACKET extends MultiblockUpdatePacket> extends IMultiblock<MULTIBLOCK, T> {
	
	Set<EntityPlayer> getMultiblockUpdatePacketListeners();
	
	PACKET getMultiblockUpdatePacket();
	
	void onMultiblockUpdatePacket(PACKET message);
	
	default void addMultiblockUpdatePacketListener(EntityPlayer playerToUpdate) {
		getMultiblockUpdatePacketListeners().add(playerToUpdate);
		sendMultiblockUpdatePacketToPlayer(playerToUpdate);
	}
	
	default void removeMultiblockUpdatePacketListener(EntityPlayer playerToRemove) {
		getMultiblockUpdatePacketListeners().remove(playerToRemove);
	}
	
	default void sendMultiblockUpdatePacketToListeners() {
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
	
	default void sendMultiblockUpdatePacketToPlayer(EntityPlayer player) {
		if (getWorld().isRemote) {
			return;
		}
		PACKET packet = getMultiblockUpdatePacket();
		if (packet == null) {
			return;
		}
		PacketHandler.instance.sendTo(packet, (EntityPlayerMP) player);
	}
	
	default void sendMultiblockUpdatePacketToAll() {
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
