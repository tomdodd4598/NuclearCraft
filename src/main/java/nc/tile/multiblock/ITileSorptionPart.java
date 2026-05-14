package nc.tile.multiblock;

import nc.multiblock.*;

import java.util.*;

public interface ITileSorptionPart<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> extends ITileMultiblockPart<MULTIBLOCK, T> {
	
	boolean canReceive();
	
	boolean canExtract();
	
	default boolean canConnect() {
		return !canReceive() && !canExtract();
	}
	
	SorptionKey getSorptionKey();
	
	class SorptionKey {
		public final Object[] properties;
		
		public SorptionKey(Object... properties) {
			this.properties = properties == null ? new Object[] {} : properties;
		}
		
		@Override
		public boolean equals(Object obj) {
			return obj instanceof SorptionKey other && Arrays.deepEquals(properties, other.properties);
		}
		
		@Override
		public int hashCode() {
			return Arrays.deepHashCode(properties);
		}
	}
}
