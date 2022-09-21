package nc.tile;

import java.util.Set;

import nc.NuclearCraft;
import nc.handler.GuiHandler;
import nc.network.PacketHandler;
import nc.network.tile.TileUpdatePacket;
import net.minecraft.entity.player.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public interface ITileGui<TILE extends TileEntity & ITileGui<TILE, PACKET, INFO>, PACKET extends TileUpdatePacket, INFO extends TileContainerInfo<TILE>> extends ITilePacket<PACKET> {
	
	public INFO getContainerInfo();
	
	public default void openGui(World world, BlockPos pos, EntityPlayer player) {
		FMLNetworkHandler.openGui(player, NuclearCraft.instance, GuiHandler.getGuiId(getContainerInfo().name), world, pos.getX(), pos.getY(), pos.getZ());
	}
	
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
