package nc.tile;

import java.util.Set;

import nc.network.PacketHandler;
import nc.network.tile.TileUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public interface ITileGui<PACKET extends TileUpdatePacket> extends ITile {
	
	public int getGuiID();
	
	public Set<EntityPlayer> getPlayersToUpdate();
	
	public PACKET getGuiUpdatePacket();
	
	public void onGuiPacket(PACKET message);
	
	public default void beginUpdatingPlayer(EntityPlayer playerToUpdate) {
		getPlayersToUpdate().add(playerToUpdate);
		sendIndividualUpdate(playerToUpdate);
	}
	
	public default void stopUpdatingPlayer(EntityPlayer playerToRemove) {
		getPlayersToUpdate().remove(playerToRemove);
	}
	
	public default void sendUpdateToListeningPlayers() {
		for (EntityPlayer player : getPlayersToUpdate()) PacketHandler.instance.sendTo(getGuiUpdatePacket(), (EntityPlayerMP) player);
	}
	
	public default void sendIndividualUpdate(EntityPlayer player) {
		if (getTileWorld().isRemote) return;
		PacketHandler.instance.sendTo(getGuiUpdatePacket(), (EntityPlayerMP) player);
	}
	
	public default void sendUpdateToAllPlayers() {
		PacketHandler.instance.sendToAll(getGuiUpdatePacket());
	}
	
	public default int getSideConfigYOffset() {
		return 0;
	}
}
