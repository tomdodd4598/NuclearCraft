package nc.network.tile;

import io.netty.buffer.ByteBuf;
import nc.tile.ITile;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public abstract class TileUpdatePacket implements IMessage {
	
	protected boolean messageValid;
	protected BlockPos pos;
	
	public TileUpdatePacket() {
		messageValid = false;
	}
	
	public abstract void readMessage(ByteBuf buf);
	
	public abstract void writeMessage(ByteBuf buf);
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			readMessage(buf);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return;
		}
		messageValid = true;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if (!messageValid) return;
		writeMessage(buf);
	}
	
	public static abstract class Handler<MESSAGE extends TileUpdatePacket, TILE> implements IMessageHandler<MESSAGE, IMessage> {
		
		@Override
		public IMessage onMessage(MESSAGE message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.CLIENT) return null;
			Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message));
			return null;
		}
		
		protected void processMessage(MESSAGE message) {
			TileEntity tile = Minecraft.getMinecraft().player.world.getTileEntity(message.pos);
			if (tile instanceof ITile) {
				onPacket(message, (TILE)tile);
			}
		}
		
		protected abstract void onPacket(MESSAGE message, TILE processor);
	}
}
