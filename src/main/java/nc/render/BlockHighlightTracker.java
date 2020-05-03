package nc.render;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import nc.network.PacketHandler;
import nc.network.render.BlockHighlightUpdatePacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

public class BlockHighlightTracker {
	
	private Long2LongMap highlightMap = new Long2LongOpenHashMap();
	
	public void highlightBlock(long posLong, long highlightTimeMillis) {
		highlightMap.put(posLong, System.currentTimeMillis() + highlightTimeMillis);
	}
	
	public Long2LongMap getHighlightMap() {
		return highlightMap;
	}
	
	public static void sendPacket(EntityPlayerMP player, BlockPos pos, long highlightTimeMillis) {
		PacketHandler.instance.sendTo(new BlockHighlightUpdatePacket(pos, highlightTimeMillis), player);
	}
	
	public static void sendPacket(EntityPlayerMP player, long posLong, long highlightTimeMillis) {
		sendPacket(player, BlockPos.fromLong(posLong), highlightTimeMillis);
	}
	
	public static void sendPacketToAll(BlockPos pos, long highlightTimeMillis) {
		PacketHandler.instance.sendToAll(new BlockHighlightUpdatePacket(pos, highlightTimeMillis));
	}
	
	public static void sendPacketToAll(long posLong, long highlightTimeMillis) {
		sendPacketToAll(BlockPos.fromLong(posLong), highlightTimeMillis);
	}
}
