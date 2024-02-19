package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.NuclearCraft;
import nc.tile.ITileGui;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class OpenGuiPacket extends TileGuiPacket {
	
	protected int guiId = -1;
	
	public OpenGuiPacket() {
		super();
	}
	
	public OpenGuiPacket(BlockPos pos, int guiId) {
		super(pos);
		this.guiId = guiId;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		guiId = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(guiId);
	}
	
	public static class Handler extends TileGuiPacket.Handler<OpenGuiPacket> {
		
		@Override
		protected void onPacket(OpenGuiPacket message, EntityPlayerMP player, TileEntity tile) {
			FMLNetworkHandler.openGui(player, NuclearCraft.instance, message.guiId, player.getServerWorld(), message.pos.getX(), message.pos.getY(), message.pos.getZ());
			if (tile instanceof ITileGui) {
				((ITileGui<?, ?, ?>) tile).addTileUpdatePacketListener(player);
			}
		}
	}
}
