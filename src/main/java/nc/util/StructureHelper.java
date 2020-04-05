/** Massive thanks to McJty, maker of RFTools and many other mods, for letting me use this code! */

package nc.util;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureData;

public class StructureHelper {
	
	public static final StructureHelper CACHE = new StructureHelper();
	
	private final Object2BooleanMap<StructureCacheEntry> structureCache = new Object2BooleanOpenHashMap<>();
	
	public void clear() {
		structureCache.clear();
	}
	
	public boolean isInStructure(World world, String structure, BlockPos pos) {
		int dimension = world.provider.getDimension();
		ChunkPos cp = new ChunkPos(pos);
		long cplong = ChunkPos.asLong(cp.x, cp.z);
		StructureCacheEntry entry = new StructureCacheEntry(structure, dimension, cplong);
		if (structureCache.containsKey(entry)) {
			return structureCache.get(entry);
		}
		
		MapGenStructureData data = (MapGenStructureData) world.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, structure);
		if (data == null) {
			structureCache.put(entry, false);
			return false;
		}
		
		LongSet longs = parseStructureData(data);
		for (Long l : longs) {
			structureCache.put(new StructureCacheEntry(structure, dimension, l), true);
		}
		if (structureCache.containsKey(entry)) {
			return true;
		} else {
			structureCache.put(entry, false);
			return false;
		}
	}
	
	private static LongSet parseStructureData(MapGenStructureData data) {
		LongSet chunks = new LongOpenHashSet();
		NBTTagCompound nbttagcompound = data.getTagCompound();
		
		for (String s : nbttagcompound.getKeySet()) {
			NBTBase nbtbase = nbttagcompound.getTag(s);
			
			if (nbtbase.getId() == 10) {
				NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbtbase;
				
				if (nbttagcompound1.hasKey("ChunkX") && nbttagcompound1.hasKey("ChunkZ")) {
					int i = nbttagcompound1.getInteger("ChunkX");
					int j = nbttagcompound1.getInteger("ChunkZ");
					chunks.add(ChunkPos.asLong(i, j));
				}
			}
		}
		return chunks;
	}
	
	public static class StructureCacheEntry {
		@Nonnull private final String structure;
		private final int dimension;
		private final long chunkpos;
		
		public StructureCacheEntry(@Nonnull String structure, int dimension, long chunkpos) {
			this.structure = structure;
			this.dimension = dimension;
			this.chunkpos = chunkpos;
		}
		
		@Nonnull
		public String getStructure() {
			return structure;
		}
		
		public int getDimension() {
			return dimension;
		}
		
		public long getChunkpos() {
			return chunkpos;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			
			StructureCacheEntry that = (StructureCacheEntry) o;
			
			if (dimension != that.dimension) return false;
			if (chunkpos != that.chunkpos) return false;
			if (!structure.equals(that.structure)) return false;
			
			return true;
		}
		
		@Override
		public int hashCode() {
			int result = structure.hashCode();
			result = 31 * result + dimension;
			result = 31 * result + (int) (chunkpos ^ (chunkpos >>> 32));
			return result;
		}
	}
}
