package nc.render;

import nc.network.PacketHandler;
import nc.network.render.BlockHighlightUpdatePacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

public class BlockHighlightTracker {
	
	private BlockPos highlightPos = null;
	private long highlightExpireTimeMillis = 0;
	
	public void highlightBlock(BlockPos pos, long highlightTimeMillis) {
		highlightPos = pos;
		this.highlightExpireTimeMillis = System.currentTimeMillis() + highlightTimeMillis;
	}
	
	public BlockPos getHighlightPos() {
		return highlightPos;
	}
	
	public long getHighlightExpireTimeMillis() {
		return highlightExpireTimeMillis;
	}
	
	public static void sendPacket(EntityPlayerMP player, BlockPos pos, long highlightTimeMillis) {
		PacketHandler.instance.sendTo(new BlockHighlightUpdatePacket(pos, highlightTimeMillis), player);
	}
}
