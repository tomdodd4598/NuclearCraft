package nc.render;

import it.unimi.dsi.fastutil.longs.*;
import nc.network.render.BlockHighlightUpdatePacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collection;

public class BlockHighlightTracker {
	
	private final Long2LongMap highlightMap = new Long2LongOpenHashMap();
	
	public void highlightBlocks(LongIterable posLongIterable, long highlightTimeMillis) {
		for (long posLong : posLongIterable) {
			highlightBlock(posLong, highlightTimeMillis);
		}
	}
	
	public void highlightBlocks(Iterable<BlockPos> posIterable, long highlightTimeMillis) {
		for (BlockPos pos : posIterable) {
			highlightBlock(pos, highlightTimeMillis);
		}
	}
	
	public void highlightBlock(long posLong, long highlightTimeMillis) {
		highlightMap.put(posLong, System.currentTimeMillis() + highlightTimeMillis);
	}
	
	public void highlightBlock(BlockPos pos, long highlightTimeMillis) {
		if (pos != null) {
			highlightBlock(pos.toLong(), highlightTimeMillis);
		}
	}
	
	public Long2LongMap getHighlightMap() {
		return highlightMap;
	}
	
	public static @Nullable BlockHighlightUpdatePacket getPacket(LongCollection posLongCollection, long highlightTimeMillis) {
		if (posLongCollection == null || posLongCollection.isEmpty()) {
			return null;
		}
		return new BlockHighlightUpdatePacket(posLongCollection, highlightTimeMillis);
	}
	
	public static @Nullable BlockHighlightUpdatePacket getPacket(long posLong, long highlightTimeMillis) {
		return new BlockHighlightUpdatePacket(posLong, highlightTimeMillis);
	}
	
	public static @Nullable BlockHighlightUpdatePacket getPacket(Collection<BlockPos> posCollection, long highlightTimeMillis) {
		if (posCollection == null || posCollection.isEmpty()) {
			return null;
		}
		return new BlockHighlightUpdatePacket(posCollection, highlightTimeMillis);
	}
	
	public static @Nullable BlockHighlightUpdatePacket getPacket(BlockPos pos, long highlightTimeMillis) {
		return pos == null ? null : new BlockHighlightUpdatePacket(pos, highlightTimeMillis);
	}
	
	public static void sendPacket(EntityPlayerMP player, LongCollection posLongCollection, long highlightTimeMillis) {
		BlockHighlightUpdatePacket packet = getPacket(posLongCollection, highlightTimeMillis);
		if (packet != null) {
			packet.sendTo(player);
		}
	}
	
	public static void sendPacket(EntityPlayerMP player, long posLong, long highlightTimeMillis) {
		BlockHighlightUpdatePacket packet = getPacket(posLong, highlightTimeMillis);
		if (packet != null) {
			packet.sendTo(player);
		}
	}
	
	public static void sendPacket(EntityPlayerMP player, Collection<BlockPos> posCollection, long highlightTimeMillis) {
		BlockHighlightUpdatePacket packet = getPacket(posCollection, highlightTimeMillis);
		if (packet != null) {
			packet.sendTo(player);
		}
	}
	
	public static void sendPacket(EntityPlayerMP player, BlockPos pos, long highlightTimeMillis) {
		BlockHighlightUpdatePacket packet = getPacket(pos, highlightTimeMillis);
		if (packet != null) {
			packet.sendTo(player);
		}
	}
	
	public static void sendPacketToAll(LongCollection posLongCollection, long highlightTimeMillis) {
		BlockHighlightUpdatePacket packet = getPacket(posLongCollection, highlightTimeMillis);
		if (packet != null) {
			packet.sendToAll();
		}
	}
	
	public static void sendPacketToAll(long posLong, long highlightTimeMillis) {
		BlockHighlightUpdatePacket packet = getPacket(posLong, highlightTimeMillis);
		if (packet != null) {
			packet.sendToAll();
		}
	}
	
	public static void sendPacketToAll(Collection<BlockPos> posCollection, long highlightTimeMillis) {
		BlockHighlightUpdatePacket packet = getPacket(posCollection, highlightTimeMillis);
		if (packet != null) {
			packet.sendToAll();
		}
	}
	
	public static void sendPacketToAll(BlockPos pos, long highlightTimeMillis) {
		BlockHighlightUpdatePacket packet = getPacket(pos, highlightTimeMillis);
		if (packet != null) {
			packet.sendToAll();
		}
	}
}
