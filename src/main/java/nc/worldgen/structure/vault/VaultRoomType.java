package nc.worldgen.structure.vault;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import java.util.Random;

public enum VaultRoomType {
	
	ENTRY_ROOM(VaultSectorType.STANDARD, 10, 10, false, false),
	SMALL(VaultSectorType.STANDARD, 10, 8, true, false),
	LARGE(VaultSectorType.STANDARD, 12, 10, false, false),
	JUNCTION(VaultSectorType.STANDARD, 12, 12, true, false),
	STAIR_ROOM(VaultSectorType.STANDARD, 12, 12, true, false),
	SECRET_TRANSITION_ROOM(VaultSectorType.STANDARD, 10, 10, false, false),
	DEEP_SMALL(VaultSectorType.DEEP, 8, 6, true, false),
	DEEP_LARGE(VaultSectorType.DEEP, 10, 8, false, false),
	DEEP_JUNCTION(VaultSectorType.DEEP, 8, 8, true, false),
	DEEP_STAIR_ROOM(VaultSectorType.DEEP, 8, 8, true, false),
	DEEP_UNIQUE_LOOT_ROOM(VaultSectorType.DEEP, 10, 8, false, true);
	
	final VaultSectorType sectorType;
	final int halfX, halfZ;
	final boolean allowGenericLoot, isUniqueLootRoom;
	
	VaultRoomType(VaultSectorType sectorType, int halfX, int halfZ, boolean allowGenericLoot, boolean isUniqueLootRoom) {
		this.sectorType = sectorType;
		this.halfX = halfX;
		this.halfZ = halfZ;
		this.allowGenericLoot = allowGenericLoot;
		this.isUniqueLootRoom = isUniqueLootRoom;
	}
	
	private Block getConcrete(Random randomIn, boolean supported) {
		return randomIn.nextInt(sectorType == VaultSectorType.DEEP ? 4 : 8) == 0 ? (supported ? Blocks.CONCRETE_POWDER : Blocks.STAINED_HARDENED_CLAY) : Blocks.CONCRETE;
	}
	
	public IBlockState getWallState(Random randomIn) {
		return switch (this) {
			case ENTRY_ROOM -> getConcrete(randomIn, true).getStateFromMeta(0);
			case SMALL, LARGE, JUNCTION, STAIR_ROOM -> getConcrete(randomIn, true).getStateFromMeta(0);
			case SECRET_TRANSITION_ROOM -> getConcrete(randomIn, true).getStateFromMeta(0);
			case DEEP_SMALL, DEEP_LARGE, DEEP_JUNCTION, DEEP_STAIR_ROOM -> getConcrete(randomIn, true).getStateFromMeta(8);
			case DEEP_UNIQUE_LOOT_ROOM -> getConcrete(randomIn, true).getStateFromMeta(8);
		};
	}
	
	public IBlockState getFloorState(Random randomIn) {
		return switch (this) {
			case ENTRY_ROOM -> getConcrete(randomIn, true).getStateFromMeta(1);
			case SMALL, LARGE, JUNCTION, STAIR_ROOM -> getConcrete(randomIn, true).getStateFromMeta(7);
			case SECRET_TRANSITION_ROOM -> getConcrete(randomIn, true).getStateFromMeta(13);
			case DEEP_SMALL, DEEP_LARGE, DEEP_JUNCTION, DEEP_STAIR_ROOM -> getConcrete(randomIn, true).getStateFromMeta(15);
			case DEEP_UNIQUE_LOOT_ROOM -> getConcrete(randomIn, true).getStateFromMeta(11);
		};
	}
	
	public IBlockState getCeilingState(Random randomIn) {
		return switch (this) {
			case ENTRY_ROOM -> getConcrete(randomIn, false).getStateFromMeta(4);
			case SMALL, LARGE, JUNCTION, STAIR_ROOM -> getConcrete(randomIn, false).getStateFromMeta(8);
			case SECRET_TRANSITION_ROOM -> getConcrete(randomIn, false).getStateFromMeta(5);
			case DEEP_SMALL, DEEP_LARGE, DEEP_JUNCTION, DEEP_STAIR_ROOM -> getConcrete(randomIn, false).getStateFromMeta(7);
			case DEEP_UNIQUE_LOOT_ROOM -> getConcrete(randomIn, false).getStateFromMeta(3);
		};
	}
	
	public IBlockState getLightState(Random randomIn) {
		return switch (this) {
			case ENTRY_ROOM, SMALL, LARGE, JUNCTION, STAIR_ROOM, SECRET_TRANSITION_ROOM -> Blocks.SEA_LANTERN.getDefaultState();
			case DEEP_SMALL, DEEP_LARGE, DEEP_JUNCTION, DEEP_STAIR_ROOM, DEEP_UNIQUE_LOOT_ROOM -> Blocks.REDSTONE_LAMP.getDefaultState();
		};
	}
}
