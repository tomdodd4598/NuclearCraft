package nc.worldgen.structure.vault;

public class VaultNode {
	
	final int id;
	final VaultSectorType sectorType;
	final int level;
	final int gridX, gridZ;
	VaultRoomType roomType;
	int doorMask;
	int centerX, centerY, centerZ;
	
	VaultNode(int id, VaultSectorType sectorType, int level, int gridX, int gridZ, VaultRoomType roomType) {
		this.id = id;
		this.sectorType = sectorType;
		this.level = level;
		this.gridX = gridX;
		this.gridZ = gridZ;
		this.roomType = roomType;
	}
}
