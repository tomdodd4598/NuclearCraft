package nc.network.render;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.*;
import nc.NuclearCraft;
import nc.network.NCPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

public class BlockHighlightUpdatePacket extends NCPacket {
	
	protected LongCollection posLongCollection;
	protected long highlightTimeMillis;
	
	public BlockHighlightUpdatePacket() {
		super();
	}
	
	public BlockHighlightUpdatePacket(LongCollection posLongCollection, long highlightTimeMillis) {
		super();
		this.posLongCollection = posLongCollection == null ? LongSets.EMPTY_SET : posLongCollection;
		this.highlightTimeMillis = highlightTimeMillis;
	}
	
	public BlockHighlightUpdatePacket(long posLong, long highlightTimeMillis) {
		this(LongSets.singleton(posLong), highlightTimeMillis);
	}
	
	public BlockHighlightUpdatePacket(Collection<BlockPos> posCollection, long highlightTimeMillis) {
		this(new LongOpenHashSet(posCollection.stream().filter(Objects::nonNull).mapToLong(BlockPos::toLong).iterator()), highlightTimeMillis);
	}
	
	public BlockHighlightUpdatePacket(BlockPos pos, long highlightTimeMillis) {
		this(pos == null ? null : Lists.newArrayList(pos), highlightTimeMillis);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		posLongCollection = readLongs(buf);
		highlightTimeMillis = buf.readLong();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		writeLongs(buf, posLongCollection);
		buf.writeLong(highlightTimeMillis);
	}
	
	public static class Handler implements IMessageHandler<BlockHighlightUpdatePacket, IMessage> {
		
		@Override
		public IMessage onMessage(BlockHighlightUpdatePacket message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> NuclearCraft.instance.blockOverlayTracker.highlightBlocks(message.posLongCollection, message.highlightTimeMillis));
			}
			return null;
		}
	}
}
