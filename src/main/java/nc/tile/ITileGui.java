package nc.tile;

import java.util.Set;

import nc.NuclearCraft;
import nc.network.PacketHandler;
import nc.network.tile.TileUpdatePacket;
import net.minecraft.entity.player.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public interface ITileGui<TILE extends TileEntity & ITileGui<TILE, PACKET, INFO>, PACKET extends TileUpdatePacket, INFO extends TileContainerInfo<TILE>> extends ITilePacket<PACKET> {
	
	INFO getContainerInfo();
	
	default void openGui(World world, BlockPos pos, EntityPlayer player) {
		FMLNetworkHandler.openGui(player, NuclearCraft.instance, getContainerInfo().getGuiId(), world, pos.getX(), pos.getY(), pos.getZ());
	}
	
	Set<EntityPlayer> getTileUpdatePacketListeners();
	
	default void addTileUpdatePacketListener(EntityPlayer player) {
		getTileUpdatePacketListeners().add(player);
		sendTileUpdatePacketToPlayer(player);
	}
	
	default void removeTileUpdatePacketListener(EntityPlayer player) {
		getTileUpdatePacketListeners().remove(player);
	}
	
	default void sendTileUpdatePacketToListeners() {
		for (EntityPlayer player : getTileUpdatePacketListeners()) {
			PacketHandler.instance.sendTo(getTileUpdatePacket(), (EntityPlayerMP) player);
		}
	}
}
