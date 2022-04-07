package nc.tile;

import nc.network.PacketHandler;
import nc.network.tile.TileUpdatePacket;
import net.minecraft.entity.player.*;

public interface ITilePacket<PACKET extends TileUpdatePacket> extends ITile {
	
	public PACKET getTileUpdatePacket();
	
	public void onTileUpdatePacket(PACKET message);
	
	public default void sendTileUpdatePacketToPlayer(EntityPlayer player) {
		if (getTileWorld().isRemote) {
			return;
		}
		PacketHandler.instance.sendTo(getTileUpdatePacket(), (EntityPlayerMP) player);
	}
	
	public default void sendTileUpdatePacketToAll() {
		PacketHandler.instance.sendToAll(getTileUpdatePacket());
	}
}
