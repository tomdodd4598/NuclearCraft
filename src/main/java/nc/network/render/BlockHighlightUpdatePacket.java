package nc.network.render;

import io.netty.buffer.ByteBuf;
import nc.NuclearCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class BlockHighlightUpdatePacket implements IMessage {
	
	protected boolean messageValid;
	
	protected BlockPos pos;
	protected long highlightTimeMillis;
	
	public BlockHighlightUpdatePacket() {
		messageValid = false;
	}
	
	public BlockHighlightUpdatePacket(BlockPos pos, long highlightTimeMillis) {
		this.pos = pos;
		this.highlightTimeMillis = highlightTimeMillis;
		
		messageValid = true;
	}
	
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		highlightTimeMillis = buf.readLong();
	}
	
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeLong(highlightTimeMillis);
	}
	
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
	
	public static class Handler implements IMessageHandler<BlockHighlightUpdatePacket, IMessage> {
		
		@Override
		public IMessage onMessage(BlockHighlightUpdatePacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.CLIENT) return null;
			Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message));
			return null;
		}
		
		protected void processMessage(BlockHighlightUpdatePacket message) {
			NuclearCraft.instance.blockOverlayTracker.highlightBlock(message.pos, message.highlightTimeMillis);
		}
	}
}
