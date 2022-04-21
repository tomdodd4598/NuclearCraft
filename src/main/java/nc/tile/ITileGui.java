package nc.tile;

import java.util.Set;

import nc.network.PacketHandler;
import nc.network.tile.TileUpdatePacket;
import net.minecraft.entity.player.*;

public interface ITileGui<PACKET extends TileUpdatePacket> extends ITilePacket<PACKET> {
	
	public int getGuiID();
	
	public Set<EntityPlayer> getTileUpdatePacketListeners();
	
	public default void addTileUpdatePacketListener(EntityPlayer player) {
		getTileUpdatePacketListeners().add(player);
		sendTileUpdatePacketToPlayer(player);
	}
	
	public default void removeTileUpdatePacketListener(EntityPlayer player) {
		getTileUpdatePacketListeners().remove(player);
	}
	
	public default void sendTileUpdatePacketToListeners() {
		for (EntityPlayer player : getTileUpdatePacketListeners()) {
			PacketHandler.instance.sendTo(getTileUpdatePacket(), (EntityPlayerMP) player);
		}
	}
}
