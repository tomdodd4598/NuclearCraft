package nc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class BlockFinder {
	
	private final BlockPos pos;
	private final World world;
	private final int meta;
	
	public BlockFinder(BlockPos pos, World world) {
		this(pos, world, 4);
	}
	
	public BlockFinder(BlockPos pos, World world, int meta) {
		this.pos = pos;
		this.world = world;
		this.meta = meta;
	}
	
	public IBlockState getBlockState(BlockPos pos) {
		return world.getBlockState(pos);
	}
	
	public IBlockState getBlockState(int x, int y, int z) {
		return getBlockState(position(x, y, z));
	}
	
	public BlockPos position(int x, int y, int z) {
		if (meta == 4) return new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
		if (meta == 2) return new BlockPos(pos.getX() - z, pos.getY() + y, pos.getZ() + x);
		if (meta == 5) return new BlockPos(pos.getX() - x, pos.getY() + y, pos.getZ() - z);
		if (meta == 3) return new BlockPos(pos.getX() + z, pos.getY() + y, pos.getZ() - x);
		else return new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
	}
	
	public boolean find(BlockPos pos, Object... blocks) {
		for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] == null) continue;
			if (blocks[i] instanceof IBlockState) {
				if (getBlockState(pos) == (IBlockState)blocks[i]) return true;
			}
			if (blocks[i] instanceof String) {
				if (findOre(pos, (String)blocks[i])) return true;
			}
			boolean isSubClass = Block.class.isAssignableFrom(blocks[i].getClass());
			if (isSubClass && getBlockState(pos).getBlock() == (Block)blocks[i]) return true;
		}
		return false;
	}
	
	public boolean find(int x, int y, int z, Object... blocks) {
		return find(position(x, y, z), blocks);
	}
	
	public boolean findOre(BlockPos pos, String... names) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		for (int i = 0; i < names.length; i++) {
			List<ItemStack> stacks = OreDictionary.getOres(names[i]);
			stackList.addAll(stacks);
		}
		ItemStack stack = ItemStackHelper.blockStateToStack(getBlockState(pos));
		for (ItemStack oreStack : stackList) {
			if (oreStack.isItemEqual(stack)) return true;
		}
		return false;
	}
	
	public boolean findOre(int x, int y, int z, String... names) {
		return findOre(position(x, y, z), names);
	}
	
	public int configurationCount(BlockPos[] posArray, Object... blocks) {
		int count = 0;
		for (BlockPos pos : posArray) if (find(pos, blocks)) count++;
		return count;
	}
	
	public boolean configuration(BlockPos[] posArray, Object... blocks) {
		return configurationCount(posArray, blocks) >= posArray.length;
	}
	
	public int configurationCount(List<BlockPos[]> posArrays, Object... blocks) {
		int count = 0;
		for (BlockPos[] horizontals : posArrays) count = Math.max(count, configurationCount(horizontals, blocks));
		return count;
	}
	
	// Adjacents
	
	public boolean adjacent(BlockPos pos, int dist, Object... blocks) {
		BlockPosHelper posHelper = new BlockPosHelper(pos);
		for (BlockPos blockPos : posHelper.adjacents(dist)) if (find(blockPos, blocks)) return true;
		return false;
	}
	
	public boolean adjacent(int x, int y, int z, int dist, Object... blocks) {
		return adjacent(position(x, y, z), dist, blocks);
	}
	
	public int adjacentCount(BlockPos pos, int dist, Object... blocks) {
		int count = 0;
		BlockPosHelper posHelper = new BlockPosHelper(pos);
		for (BlockPos blockPos : posHelper.adjacents(dist)) if (find(blockPos, blocks)) count++;
		return count;
	}
	
	public int adjacentCount(int x, int y, int z, int dist, Object... blocks) {
		return adjacentCount(position(x, y, z), dist, blocks);
	}
	
	public boolean adjacentSurround(BlockPos pos, int dist, Object... blocks) {
		return adjacentCount(pos, dist, blocks) >= 6;
	}
	
	public boolean adjacentSurround(int x, int y, int z, int dist, Object... blocks) {
		return adjacentSurround(position(x, y, z), dist, blocks);
	}
	
	public boolean adjacentAnd(BlockPos pos, int dist, Object... blocks) {
		for (Object block : blocks) if (!adjacent(pos, dist, block)) return false;
		return true;
	}
	
	public boolean adjacentAnd(int x, int y, int z, int dist, Object... blocks) {
		return adjacentAnd(position(x, y, z), dist, blocks);
	}
	
	// Horizontals
	
	public int horizontalCount(BlockPos pos, int dist, Object... blocks) {
		BlockPosHelper posHelper = new BlockPosHelper(pos);
		return configurationCount(posHelper.horizontalsList(dist), blocks);
	}
	
	public int horizontalCount(int x, int y, int z, int dist, Object... blocks) {
		return horizontalCount(position(x, y, z), dist, blocks);
	}
	
	public boolean horizontal(BlockPos pos, int dist, Object... blocks) {
		return horizontalCount(pos, dist, blocks) >= 4;
	}
	
	public boolean horizontal(int x, int y, int z, int dist, Object... blocks) {
		return horizontal(position(x, y, z), dist, blocks);
	}
	
	public boolean horizontalAnd(BlockPos pos, int dist, Object... blocks) {
		for (Object block : blocks) if (!horizontal(pos, dist, block)) return false;
		return true;
	}
	
	public boolean horizontalAnd(int x, int y, int z, int dist, Object... blocks) {
		return horizontalAnd(position(x, y, z), dist, blocks);
	}
	
	// Y-Horizontal
	
	public int horizontalYCount(BlockPos pos, int dist, Object... blocks) {
		BlockPosHelper posHelper = new BlockPosHelper(pos);
		return configurationCount(posHelper.horizontalsY(dist), blocks);
	}
	
	public int horizontalYCount(int x, int y, int z, int dist, Object... blocks) {
		return horizontalYCount(position(x, y, z), dist, blocks);
	}
	
	public boolean horizontalY(BlockPos pos, int dist, Object... blocks) {
		return horizontalYCount(pos, dist, blocks) >= 4;
	}
	
	public boolean horizontalY(int x, int y, int z, int dist, Object... blocks) {
		return horizontalY(position(x, y, z), dist, blocks);
	}
	
	public boolean horizontalYAnd(BlockPos pos, int dist, Object... blocks) {
		for (Object block : blocks) if (!horizontalY(pos, dist, block)) return false;
		return true;
	}
	
	public boolean horizontalYAnd(int x, int y, int z, int dist, Object... blocks) {
		return horizontalYAnd(position(x, y, z), dist, blocks);
	}
	
	// Axials
	
	public int axialCount(BlockPos pos, int dist, Object... blocks) {
		BlockPosHelper posHelper = new BlockPosHelper(pos);
		return configurationCount(posHelper.axialsList(dist), blocks);
	}
	
	public int axialCount(int x, int y, int z, int dist, Object... blocks) {
		return axialCount(position(x, y, z), dist, blocks);
	}
	
	public boolean axial(BlockPos pos, int dist, Object... blocks) {
		return axialCount(pos, dist, blocks) >= 2;
	}
	
	public boolean axial(int x, int y, int z, int dist, Object... blocks) {
		return axial(position(x, y, z), dist, blocks);
	}
	
	public boolean axialAnd(BlockPos pos, int dist, Object... blocks) {
		for (Object block : blocks) if (!axial(pos, dist, block)) return false;
		return true;
	}
	
	public boolean axialAnd(int x, int y, int z, int dist, Object... blocks) {
		return axialAnd(position(x, y, z), dist, blocks);
	}
	
	// Vertices
	
	public int vertexCount(BlockPos pos, Object... blocks) {
		BlockPosHelper posHelper = new BlockPosHelper(pos);
		return configurationCount(posHelper.vertexList(), blocks);
	}
	
	public int vertexCount(int x, int y, int z, Object... blocks) {
		return vertexCount(position(x, y, z), blocks);
	}
	
	public boolean vertex(BlockPos pos, Object... blocks) {
		return vertexCount(pos, blocks) >= 3;
	}
	
	public boolean vertex(int x, int y, int z, Object... blocks) {
		return vertex(position(x, y, z), blocks);
	}
	
	public boolean vertexAnd(BlockPos pos, Object... blocks) {
		for (Object block : blocks) if (!vertex(pos, block)) return false;
		return true;
	}
	
	public boolean vertexAnd(int x, int y, int z, Object... blocks) {
		return vertexAnd(position(x, y, z), blocks);
	}
	
	// Other
	
	private Random rand = new Random();
	
	public BlockPos getCentre(int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
		return position((xMin + xMax)/2, (yMin + yMax)/2, (zMin + zMin)/2);
	}
	
	public int randomBetween(int min, int max) {
		return min + rand.nextInt(max + 1);
	}
	
	public BlockPos randomWithin(int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
		return position(randomBetween(xMin, xMax), randomBetween(yMin, yMax), randomBetween(zMin, zMax));
	}
}
