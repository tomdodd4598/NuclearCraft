package nc.network.render;

import io.netty.buffer.ByteBuf;
import nc.NuclearCraft;
import nc.network.NCPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class BlockHighlightUpdatePacket extends NCPacket {
	
	protected long posLong, highlightTimeMillis;
	
	public BlockHighlightUpdatePacket() {
		super();
	}
	
	public BlockHighlightUpdatePacket(BlockPos pos, long highlightTimeMillis) {
		super();
		posLong = pos.toLong();
		this.highlightTimeMillis = highlightTimeMillis;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		posLong = buf.readLong();
		highlightTimeMillis = buf.readLong();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeLong(posLong);
		buf.writeLong(highlightTimeMillis);
	}
	
	public static class Handler implements IMessageHandler<BlockHighlightUpdatePacket, IMessage> {
		
		@Override
		public IMessage onMessage(BlockHighlightUpdatePacket message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> NuclearCraft.instance.blockOverlayTracker.highlightBlock(message.posLong, message.highlightTimeMillis));
			}
			return null;
		}
	}
}
