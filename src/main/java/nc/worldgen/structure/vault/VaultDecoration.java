package nc.worldgen.structure.vault;

public enum VaultDecoration {
	CRATE_PILE,
	VOLTAIC_PILE,
	FURNACE_BENCH,
	DECAY_LEAD_LINE,
	MUSIC_LINE,
	MAGIC_LINE;
	
	public int getWeight(VaultRoomType roomType) {
		if (roomType == VaultRoomType.ENTRY_ROOM) {
			return this == CRATE_PILE ? 1 : 0;
		}
		else if (roomType == VaultRoomType.SECRET_TRANSITION_ROOM) {
			return this == CRATE_PILE || this == FURNACE_BENCH ? 1 : 0;
		}
		else if (roomType == VaultRoomType.DEEP_UNIQUE_LOOT_ROOM) {
			return switch (this) {
				case CRATE_PILE -> 0;
				case VOLTAIC_PILE -> 4;
				case FURNACE_BENCH -> 0;
				case DECAY_LEAD_LINE -> 4;
				case MUSIC_LINE -> 0;
				case MAGIC_LINE -> 1;
			};
		}
		else {
			return switch (roomType.sectorType) {
				case STANDARD -> switch (this) {
					case CRATE_PILE -> 1;
					case VOLTAIC_PILE -> 1;
					case FURNACE_BENCH -> 2;
					case DECAY_LEAD_LINE -> 1;
					case MUSIC_LINE -> 1;
					case MAGIC_LINE -> 0;
				};
				case DEEP -> switch (this) {
					case CRATE_PILE -> 1;
					case VOLTAIC_PILE -> 3;
					case FURNACE_BENCH -> 1;
					case DECAY_LEAD_LINE -> 3;
					case MUSIC_LINE -> 1;
					case MAGIC_LINE -> 1;
				};
			};
		}
	}
}
