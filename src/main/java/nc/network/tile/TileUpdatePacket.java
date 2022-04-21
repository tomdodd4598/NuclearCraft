package nc.network.tile;

import nc.tile.*;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public abstract class TileUpdatePacket implements IMessage {
	
	protected BlockPos pos;
	
	public TileUpdatePacket() {
		
	}
	
	public static abstract class Handler<MESSAGE extends TileUpdatePacket, TILE extends ITilePacket<MESSAGE>> implements IMessageHandler<MESSAGE, IMessage> {
		
		@Override
		public IMessage onMessage(MESSAGE message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message));
			}
			return null;
		}
		
		@SuppressWarnings("unchecked")
		protected void processMessage(MESSAGE message) {
			TileEntity tile = Minecraft.getMinecraft().player.world.getTileEntity(message.pos);
			if (tile instanceof ITileGui) {
				onTileUpdatePacket(message, (TILE) tile);
			}
		}
		
		protected abstract void onTileUpdatePacket(MESSAGE message, TILE processor);
	}
}
