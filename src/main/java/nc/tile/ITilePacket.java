package nc.tile;

import nc.network.PacketHandler;
import nc.network.tile.TileUpdatePacket;
import net.minecraft.entity.player.*;

public interface ITilePacket<PACKET extends TileUpdatePacket> extends ITile {
	
	PACKET getTileUpdatePacket();
	
	void onTileUpdatePacket(PACKET message);
	
	default void sendTileUpdatePacketToPlayer(EntityPlayer player) {
		if (getTileWorld().isRemote) {
			return;
		}
		PacketHandler.instance.sendTo(getTileUpdatePacket(), (EntityPlayerMP) player);
	}
	
	default void sendTileUpdatePacketToAll() {
		PacketHandler.instance.sendToAll(getTileUpdatePacket());
	}
}
