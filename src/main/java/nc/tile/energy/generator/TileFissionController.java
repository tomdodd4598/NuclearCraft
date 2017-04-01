package nc.tile.energy.generator;

import nc.blocks.tile.energy.generator.BlockFissionController;
import nc.crafting.generator.FissionRecipes;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class TileFissionController extends TileEnergyGeneratorProcessor {
	
	public int rateMultiplier;
	
	public int processTime;
	public int processPower;
	
	public int heat;
	public int cooling;
	public int efficiency;
	public int cells;

	public TileFissionController() {
		super("fission_controller", 1, 1, 0, 960000, FissionRecipes.instance());
	}
	
	public void update() {
		super.update();
	}
	
	public void setBlockState() {
		BlockFissionController.setState(isGenerating, world, pos);
	}
	
	// Generating

	public int getRateMultiplier() {
		return Math.max(1, rateMultiplier);
	}

	public void setRateMultiplier(int value) {
		rateMultiplier = Math.max(1, value);
	}

	public int getProcessTime() {
		return Math.max(60, processTime);
	}

	public void setProcessTime(int value) {
		processTime = Math.max(1, value);
	}

	public int getProcessPower() {
		return processPower;
	}

	public void setProcessPower(int value) {
		processPower = value;
	}
	
	// Finding Blocks
	
	/** returns true if any of blocks are at relative position {x,y,z} */
	private boolean find(int x, int y, int z, Block... blocks) {
		int xCheck = getPos().getX();
		int yCheck = getPos().getY() + y;
		int zCheck = getPos().getZ();
		
		if (getBlockMetadata() == 4) for (int i = 0; i < blocks.length; i++) {
			if (world.getBlockState(new BlockPos(xCheck + x, yCheck, zCheck + z)) == blocks[i].getDefaultState()) return true;
		}
		if (getBlockMetadata() == 2) for (int i = 0; i < blocks.length; i++) {
			if (world.getBlockState(new BlockPos(xCheck - z, yCheck, zCheck + x)) == blocks[i].getDefaultState()) return true;
		}
		if (getBlockMetadata() == 5) for (int i = 0; i < blocks.length; i++) {
			if (world.getBlockState(new BlockPos(xCheck - x, yCheck, zCheck - z)) == blocks[i].getDefaultState()) return true;
		}
		if (getBlockMetadata() == 3) for (int i = 0; i < blocks.length; i++) {
			if (world.getBlockState(new BlockPos(xCheck + z, yCheck, zCheck - x)) == blocks[i].getDefaultState()) return true;
		}
		
		return false;
	}
	
	/** returns true if any of blocks are adjacent */
	private boolean adjacentOr(int x, int y, int z, Block... blocks) {
		for (int i = 0; i < blocks.length; i++) {
			if (find(x + 1, y, z, blocks[i])) return true;
			if (find(x - 1, y, z, blocks[i])) return true;
			if (find(x, y + 1, z, blocks[i])) return true;
			if (find(x, y - 1, z, blocks[i])) return true;
			if (find(x, y, z + 1, blocks[i])) return true;
			if (find(x, y, z - 1, blocks[i])) return true;
		}
		return false;
	}
	
	/** returns true if each of blocks are adjacent */
	private boolean adjacentAnd(int x, int y, int z, Block... blocks) {
		if (blocks.length > 6) return false;
		int count = 0;
		for (int i = 0; i < blocks.length; i++) {
			if (find(x + 1, y, z, blocks[i])) count++;
			else if (find(x - 1, y, z, blocks[i])) count++;
			else if (find(x, y + 1, z, blocks[i])) count++;
			else if (find(x, y - 1, z, blocks[i])) count++;
			else if (find(x, y, z + 1, blocks[i])) count++;
			else if (find(x, y, z - 1, blocks[i])) count++;
		}
		return count >= blocks.length;
	}
	
	/** returns true if any of blocks are totally surrounding */
	private boolean surrounding(int x, int y, int z, Block... blocks) {
		for (int i = 0; i < blocks.length; i++) {
			if (find(x + 1, y, z, blocks[i])) {
				if (find(x - 1, y, z, blocks[i])) {
					if (find(x, y + 1, z, blocks[i])) {
						if (find(x, y - 1, z, blocks[i])) {
							if (find(x, y, z + 1, blocks[i])) {
								if (find(x, y, z - 1, blocks[i])) return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/** returns true if between any of blocks */
	private boolean betweenOr(int x, int y, int z, Block... blocks) {
		for (int i = 0; i < blocks.length; i++) {
			if (find(x + 1, y, z, blocks[i])) {
				if (find(x - 1, y, z, blocks[i])) return true;
			}
			if (find(x, y + 1, z, blocks[i])) {
				if (find(x, y - 1, z, blocks[i])) return true;
			}
			if (find(x, y, z + 1, blocks[i])) {
				if (find(x, y, z - 1, blocks[i])) return true;
			}
		}
		return false;
	}
	
	/** returns true if between all of blocks */
	private boolean betweenAnd(int x, int y, int z, Block... blocks) {
		if (blocks.length > 3) return false;
		int count = 0;
		for (int i = 0; i < blocks.length; i++) {
			if (find(x + 1, y, z, blocks[i])) {
				if (find(x - 1, y, z, blocks[i])) count++;
			} else if (find(x, y + 1, z, blocks[i])) {
				if (find(x, y - 1, z, blocks[i])) count++;
			} else if (find(x, y, z + 1, blocks[i])) {
				if (find(x, y, z - 1, blocks[i])) count++;
			}
		}
		return count >= blocks.length;
	}
	
	/** returns true if sandwiched by the block */
	private boolean sandwich(int x, int y, int z, Block block) {
		if (find(x + 1, y, z, block)) {
			if (find(x - 1, y, z, block)) {
				if (find(x, y + 1, z, block)) {
					if (find(x, y - 1, z, block)) return true;
				}
				if (find(x, y, z + 1, block)) {
					if (find(x, y, z - 1, block)) return true;
				}
			}
		}
		if (find(x, y + 1, z, block)) {
			if (find(x, y - 1, z, block)) {
				if (find(x, y, z + 1, block)) {
					if (find(x, y, z - 1, block)) return true;
				}
			}
		}
		return false;
	}
	
	/** returns number of the block that are adjacent */
	private int adjacent(int x, int y, int z, Block block) {
		int count = 0;
		if (find(x + 1, y, z, block)) count++;
		if (find(x - 1, y, z, block)) count++;
		if (find(x, y + 1, z, block)) count++;
		if (find(x, y - 1, z, block)) count++;
		if (find(x, y, z + 1, block)) count++;
		if (find(x, y, z - 1, block)) count++;
		return count;
	}
	
	// Finding Structure
	
	
	
	// Inventory Fields

		public int getFieldCount() {
			return 5;
		}

		public int getField(int id) {
			switch (id) {
			case 0:
				return time;
			case 1:
				return getEnergyStored();
			case 2:
				return getProcessTime();
			case 3:
				return getProcessPower();
			case 4:
				return heat;
			case 5:
				return cooling;
			case 6:
				return efficiency;
			case 7:
				return cells;
			default:
				return 0;
			}
		}

		public void setField(int id, int value) {
			switch (id) {
			case 0:
				time = value;
				break;
			case 1:
				storage.setEnergyStored(value);
				break;
			case 2:
				setProcessTime(value);
				break;
			case 3:
				setProcessPower(value);
				break;
			case 4:
				heat = value;
				break;
			case 5:
				cooling = value;
				break;
			case 6:
				efficiency = value;
				break;
			case 7:
				cells = value;
			}
		}
}
