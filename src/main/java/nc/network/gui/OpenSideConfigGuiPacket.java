package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.NuclearCraft;
import nc.tile.ITileGui;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class OpenSideConfigGuiPacket extends TileGuiPacket {
	
	public OpenSideConfigGuiPacket() {
		super();
	}
	
	public OpenSideConfigGuiPacket(ITileGui<?, ?, ?> tile) {
		super(tile);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
	}
	
	public static class Handler extends TileGuiPacket.Handler<OpenSideConfigGuiPacket> {
		
		@Override
		protected void onPacket(OpenSideConfigGuiPacket message, EntityPlayerMP player, TileEntity tile) {
			if (tile instanceof ITileGui) {
				ITileGui<?, ?, ?> tileGui = (ITileGui<?, ?, ?>) tile;
				FMLNetworkHandler.openGui(player, NuclearCraft.instance, tileGui.getContainerInfo().getGuiId() + 1000, player.getServerWorld(), message.pos.getX(), message.pos.getY(), message.pos.getZ());
				tileGui.addTileUpdatePacketListener(player);
			}
		}
	}
}
