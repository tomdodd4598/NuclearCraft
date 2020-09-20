package nc.multiblock;

import net.minecraft.util.math.*;
import net.minecraft.world.chunk.IChunkProvider;

/** Chunk and block update helpers */
public final class WorldHelper {
	
	public static int getChunkXFromBlock(int blockX) {
		return blockX >> 4;
	}
	
	public static int getChunkXFromBlock(BlockPos position) {
		return position.getX() >> 4;
	}
	
	public static int getChunkZFromBlock(int blockZ) {
		return blockZ >> 4;
	}
	
	public static int getChunkZFromBlock(BlockPos position) {
		return position.getZ() >> 4;
	}
	
	public static long getChunkXZHashFromBlock(int blockX, int blockZ) {
		return ChunkPos.asLong(WorldHelper.getChunkXFromBlock(blockX), WorldHelper.getChunkZFromBlock(blockZ));
	}
	
	public static long getChunkXZHashFromBlock(BlockPos position) {
		return ChunkPos.asLong(WorldHelper.getChunkXFromBlock(position), WorldHelper.getChunkZFromBlock(position));
	}
	
	@Deprecated // use World.isBlockLoaded instead
	public static boolean blockChunkExists(IChunkProvider chunkProvider, BlockPos position) {
		return null != chunkProvider.getLoadedChunk(WorldHelper.getChunkXFromBlock(position), WorldHelper.getChunkZFromBlock(position));
	}
}
