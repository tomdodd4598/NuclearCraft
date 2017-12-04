package nc.tile.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nc.block.fission.BlockCooler;
import nc.block.tile.dummy.BlockFissionPort;
import nc.block.tile.generator.BlockFissionController;
import nc.block.tile.passive.BlockActiveCooler;
import nc.block.tile.passive.BlockBuffer;
import nc.config.NCConfig;
import nc.fluid.Tank;
import nc.handler.EnumHandler.CoolerTypes;
import nc.init.NCBlocks;
import nc.recipe.generator.FissionRecipes;
import nc.tile.fluid.TileActiveCooler;
import nc.util.NCMath;
import nc.util.OreStackHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.oredict.OreDictionary;

public class TileFissionController extends TileItemGenerator {
	
	public int rateMultiplier;
	
	public int processTime;
	public int processPower;
	
	public int heat;
	public int cooling;
	public int heatChange;
	public int efficiency;
	public int cells;
	
	public int tickCountStructureCheck;
	public int tickCountRunCheck;
	
	public int minX, minY, minZ;
	
	public int maxX, maxY, maxZ;
	
	public int lengthX, lengthY, lengthZ;
	
	public int complete;
	public int ready;
	public String problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
	public String problemPos = stringPos(pos);
	public int problemPosX = 0;
	public int problemPosY = 0;
	public int problemPosZ = 0;
	public int problemPosBool = 0;
	
	private Random rand = new Random();

	public TileFissionController() {
		super("fission_controller", 1, 1, 0, 960000, FissionRecipes.instance());
	}
	
	public void updateGenerator() {
		boolean flag = isGenerating;
		boolean flag1 = false;
		if(!world.isRemote) {
			if (time == 0) {
				consume();
			}
		}
		tickStructureCheck();
		checkStructure();

		if(!world.isRemote) {
			tickRunCheck();
			run();
			if (overheat() == true) {
				return;
			}
			if (canProcess() && isPowered()) {
				isGenerating = true;
				time += getRateMultiplier();
				storage.changeEnergyStored(getProcessPower());
				if (time >= getProcessTime()) {
					time = 0;
					output();
				}
			} else {
				isGenerating = false;
			}
			if (flag != isGenerating) {
				flag1 = true;
				setBlockState();
			}
			pushEnergy();
			if (findAdjacentComparator() && shouldStructureCheck()) flag1 = true;
		} else {
			isGenerating = canProcess() && isPowered();
		}
		
		if (flag1) {
			markDirty();
		}
	}
	
	public void setBlockState() {
		BlockFissionController.setState(isGenerating, world, pos);
	}
	
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) checkStructure();
	}
	
	public void tickStructureCheck() {
		if (tickCountStructureCheck > NCConfig.fission_update_rate) {
			tickCountStructureCheck = 0;
		} else {
			tickCountStructureCheck++;
		}
	}
	
	public boolean shouldStructureCheck() {
		return tickCountStructureCheck > NCConfig.fission_update_rate;
	}
	
	public void tickRunCheck() {
		if (tickCountRunCheck > NCConfig.fission_update_rate*2) {
			tickCountRunCheck = 0;
		} else {
			tickCountRunCheck++;
		}
	}
	
	public boolean shouldRunCheck() {
		return tickCountRunCheck > NCConfig.fission_update_rate*2;
	}
	
	public boolean findAdjacentComparator() {
		if (world.getBlockState(position(1, 0, 0)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (world.getBlockState(position(-1, 0, 0)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (world.getBlockState(position(0, 1, 0)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (world.getBlockState(position(0, -1, 0)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (world.getBlockState(position(0, 0, 1)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (world.getBlockState(position(0, 0, -1)).getBlock() instanceof BlockRedstoneComparator) return true;
		return false;
	}
	
	public boolean overheat() {
		if (heat >= getMaxHeat() && NCConfig.fission_overheat) {
			meltdown();
			return true;
		}
		return false;
	}
	
	public void meltdown() {
		world.removeTileEntity(pos);
		world.setBlockState(pos, Blocks.LAVA.getDefaultState());
		for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j <= maxY; j++) {
				for (int k = minZ; k <= maxZ; k++) {
					if (rand.nextDouble() < 0.18D) world.setBlockState(position(i, j, k), Blocks.LAVA.getStateFromMeta(rand.nextInt(2)));
				}
			}
		}
	}
	
	public BlockPos getCentre() {
		return position((minX + maxX)/2, (minY + maxY)/2, (minZ + maxZ)/2);
	}
	
	public BlockPos getCentreWithRand() {
		double mult = rand.nextDouble();
		return position(minX + (int) NCMath.Round(maxX*mult, 2), minY + (int) NCMath.Round(maxY*mult, 2), minZ + (int) NCMath.Round(maxZ*mult, 2));
	}
	
	public boolean canProcess() {
		return canProcessStacks() && complete == 1;
	}
	
	// IC2 Tiers
	
	public int getSourceTier() {
		return 4;
	}
	
	public int getSinkTier() {
		return 4;
	}
	
	// Generating

	public int getRateMultiplier() {
		return Math.max(1, rateMultiplier);
	}

	public void setRateMultiplier(int value) {
		rateMultiplier = Math.max(1, value);
	}

	public int getProcessTime() {
		return Math.max(1, processTime);
	}

	public void setProcessTime(int value) {
		processTime = Math.max(1, value);
	}

	public int getProcessPower() {
		return processPower;
	}

	public void setProcessPower(int value) {
		processPower = Math.max(0, value);
	}
	
	public ArrayList getFuelStats() {
		return getRecipe(true) != null ? getRecipe(true).extras() : null;
	}
	
	public double getBaseTime() {
		if (getFuelStats() != null) if (getFuelStats().get(0) instanceof Double) {
			return (double) getFuelStats().get(0)*NCConfig.fusion_fuel_use;
		}
		return 1;
	}
	
	public double getBasePower() {
		if (getFuelStats() != null) if (getFuelStats().get(1) instanceof Double) {
			return (double) getFuelStats().get(1);
		}
		return 0;
	}
	
	public double getBaseHeat() {
		if (getFuelStats() != null) if (getFuelStats().get(2) instanceof Double) {
			return (double) getFuelStats().get(2);
		}
		return 0;
	}
	
	public String getFuelName() {
		if (getFuelStats() != null) if (getFuelStats().get(3) instanceof String) {
			return ((String) getFuelStats().get(3)).replace('_', '-');
		}
		return I18n.translateToLocalFormatted("gui.container.fission_controller.no_fuel");
	}
	
	public int getLengthX() {
		return lengthX - 2;
	}
	
	public int getLengthY() {
		return lengthY - 2;
	}
	
	public int getLengthZ() {
		return lengthZ - 2;
	}
	
	public int getMaxHeat() {
		return 1000000;
	}
	
	// Finding Blocks
	
	/** returns true if any of blocks are at relative position {x,y,z} */
	private boolean findFromOreName(int x, int y, int z, String... names) {
		int xCheck = getPos().getX();
		int yCheck = getPos().getY() + y;
		int zCheck = getPos().getZ();
		
		List<ItemStack> types = new ArrayList<ItemStack>();
		for (int i = 0; i < names.length; i++) {
			List<ItemStack> stacks = OreDictionary.getOres(names[i]);
			types.addAll(stacks);
		}
		
		if (getBlockMetadata() == 4) for (int i = 0; i < names.length; i++) {
			ItemStack stack = OreStackHelper.blockToStack(world.getBlockState(new BlockPos(xCheck + x, yCheck, zCheck + z)));
			for (ItemStack oreStack : types) if (oreStack.isItemEqual(stack)) return true;
		}
		if (getBlockMetadata() == 2) for (int i = 0; i < names.length; i++) {
			ItemStack stack = OreStackHelper.blockToStack(world.getBlockState(new BlockPos(xCheck - z, yCheck, zCheck + x)));
			for (ItemStack oreStack : types) if (oreStack.isItemEqual(stack)) return true;
		}
		if (getBlockMetadata() == 5) for (int i = 0; i < names.length; i++) {
			ItemStack stack = OreStackHelper.blockToStack(world.getBlockState(new BlockPos(xCheck - x, yCheck, zCheck - z)));
			for (ItemStack oreStack : types) if (oreStack.isItemEqual(stack)) return true;
		}
		if (getBlockMetadata() == 3) for (int i = 0; i < names.length; i++) {
			ItemStack stack = OreStackHelper.blockToStack(world.getBlockState(new BlockPos(xCheck + z, yCheck, zCheck - x)));
			for (ItemStack oreStack : types) if (oreStack.isItemEqual(stack)) return true;
		}
		
		return false;
	}
	
	/** returns true if any of blocks are at relative position {x,y,z} */
	private boolean find(int x, int y, int z, Object... blocks) {
		int xCheck = getPos().getX();
		int yCheck = getPos().getY() + y;
		int zCheck = getPos().getZ();
		
		if (getBlockMetadata() == 4) for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] instanceof IBlockState) if (world.getBlockState(new BlockPos(xCheck + x, yCheck, zCheck + z)) == blocks[i]) return true;
			else if (blocks[i] instanceof Block) if (world.getBlockState(new BlockPos(xCheck + x, yCheck, zCheck + z)).getBlock() == blocks[i]) return true;
		}
		if (getBlockMetadata() == 2) for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] instanceof IBlockState) if (world.getBlockState(new BlockPos(xCheck - z, yCheck, zCheck + x)) == blocks[i]) return true;
			else if (blocks[i] instanceof Block) if (world.getBlockState(new BlockPos(xCheck - z, yCheck, zCheck + x)).getBlock() == blocks[i]) return true;
		}
		if (getBlockMetadata() == 5) for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] instanceof IBlockState) if (world.getBlockState(new BlockPos(xCheck - x, yCheck, zCheck - z)) == blocks[i]) return true;
			else if (blocks[i] instanceof Block) if (world.getBlockState(new BlockPos(xCheck - x, yCheck, zCheck - z)).getBlock() == blocks[i]) return true;
		}
		if (getBlockMetadata() == 3) for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] instanceof IBlockState) if (world.getBlockState(new BlockPos(xCheck + z, yCheck, zCheck - x)) == blocks[i]) return true;
			else if (blocks[i] instanceof Block) if (world.getBlockState(new BlockPos(xCheck + z, yCheck, zCheck - x)).getBlock() == blocks[i]) return true;
		}
		
		return false;
	}
	
	private IBlockState getBlockState(int x, int y, int z) {
		int xCheck = getPos().getX();
		int yCheck = getPos().getY() + y;
		int zCheck = getPos().getZ();
		
		if (getBlockMetadata() == 4) {
			return world.getBlockState(new BlockPos(xCheck + x, yCheck, zCheck + z));
		}
		if (getBlockMetadata() == 2) {
			return world.getBlockState(new BlockPos(xCheck - z, yCheck, zCheck + x));
		}
		if (getBlockMetadata() == 5) {
			return world.getBlockState(new BlockPos(xCheck - x, yCheck, zCheck - z));
		}
		if (getBlockMetadata() == 3) {
			return world.getBlockState(new BlockPos(xCheck + z, yCheck, zCheck - x));
		}
		else return Blocks.AIR.getDefaultState();
	}
	
	private BlockPos position(int x, int y, int z) {
		int xCheck = getPos().getX();
		int yCheck = getPos().getY() + y;
		int zCheck = getPos().getZ();
		
		if (getBlockMetadata() == 4) {
			return new BlockPos(xCheck + x, yCheck, zCheck + z);
		}
		if (getBlockMetadata() == 2) {
			return new BlockPos(xCheck - z, yCheck, zCheck + x);
		}
		if (getBlockMetadata() == 5) {
			return new BlockPos(xCheck - x, yCheck, zCheck - z);
		}
		if (getBlockMetadata() == 3) {
			return new BlockPos(xCheck + z, yCheck, zCheck - x);
		}
		else return new BlockPos(xCheck + x, yCheck, zCheck + z);
	}
	
	/** returns true if any of blocks are adjacent */
	private boolean adjacentOr(int x, int y, int z, Object... blocks) {
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
	private boolean adjacentAnd(int x, int y, int z, Object... blocks) {
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
	private boolean surrounding(int x, int y, int z, Object... blocks) {
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
	private boolean betweenOr(int x, int y, int z, Object... blocks) {
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
	private boolean betweenAnd(int x, int y, int z, Object... blocks) {
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
	private boolean sandwich(int x, int y, int z, Object block) {
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
	private int adjacent(int x, int y, int z, Object block) {
		int count = 0;
		if (find(x + 1, y, z, block)) count++;
		if (find(x - 1, y, z, block)) count++;
		if (find(x, y + 1, z, block)) count++;
		if (find(x, y - 1, z, block)) count++;
		if (find(x, y, z + 1, block)) count++;
		if (find(x, y, z - 1, block)) count++;
		return count;
	}
	
	private boolean findCooler(int x, int y, int z, int meta) {
		if (getBlockState(x, y, z).getBlock() instanceof BlockCooler) {
			return ((BlockCooler)getBlockState(x, y, z).getBlock()).getMetaFromState(getBlockState(x, y, z)) == meta;
		}
		return false;
	}
	
	private int adjacentToActiveGraphite(int x, int y, int z) {
		String graphite = "blockGraphite";
		IBlockState cell = NCBlocks.cell_block.getDefaultState();
		int count = 0;
		if (findFromOreName(x + 1, y, z, graphite)) {
			if (adjacentOr(x + 1, y, z, cell)) count++;
		}
		if (findFromOreName(x - 1, y, z, graphite)) {
			if (adjacentOr(x - 1, y, z, cell)) count++;
		}
		if (findFromOreName(x, y + 1, z, graphite)) {
			if (adjacentOr(x, y + 1, z, cell)) count++;
		}
		if (findFromOreName(x, y - 1, z, graphite)) {
			if (adjacentOr(x, y - 1, z, cell)) count++;
		}
		if (findFromOreName(x, y, z + 1, graphite)) {
			if (adjacentOr(x, y, z + 1, cell)) count++;
		}
		if (findFromOreName(x, y, z - 1, graphite)) {
			if (adjacentOr(x, y, z - 1, cell)) count++;
		}
		return count;
	}
	
	private boolean isAdjacentToActiveGraphite(int x, int y, int z) {
		String graphite = "blockGraphite";
		IBlockState cell = NCBlocks.cell_block.getDefaultState();
		if (findFromOreName(x + 1, y, z, graphite)) {
			if (adjacentOr(x + 1, y, z, cell)) return true;
		}
		if (findFromOreName(x - 1, y, z, graphite)) {
			if (adjacentOr(x - 1, y, z, cell)) return true;
		}
		if (findFromOreName(x, y + 1, z, graphite)) {
			if (adjacentOr(x, y + 1, z, cell)) return true;
		}
		if (findFromOreName(x, y - 1, z, graphite)) {
			if (adjacentOr(x, y - 1, z, cell)) return true;
		}
		if (findFromOreName(x, y, z + 1, graphite)) {
			if (adjacentOr(x, y, z + 1, cell)) return true;
		}
		if (findFromOreName(x, y, z - 1, graphite)) {
			if (adjacentOr(x, y, z - 1, cell)) return true;
		}
		return false;
	}
	
	private boolean isAdjacentToActiveCooler(int x, int y, int z, int meta) {
		List<int[]> posList = new ArrayList<int[]>();
		if (findCooler(x + 1, y, z, meta)) {
			posList.add(new int[] {x + 1, y, z});
		}
		if (findCooler(x - 1, y, z, meta)) {
			posList.add(new int[] {x - 1, y, z});
		}
		if (findCooler(x, y + 1, z, meta)) {
			posList.add(new int[] {x, y + 1, z});
		}
		if (findCooler(x, y - 1, z, meta)) {
			posList.add(new int[] {x, y - 1, z});
		}
		if (findCooler(x, y, z + 1, meta)) {
			posList.add(new int[] {x, y, z + 1});
		}
		if (findCooler(x, y, z - 1, meta)) {
			posList.add(new int[] {x, y, z - 1});
		}
		if (posList.isEmpty()) return false;
		for (int[] pos : posList) {
			if (meta == 1) {
				IBlockState casing = NCBlocks.fission_block.getStateFromMeta(0);
				IBlockState casing_transparent = NCBlocks.reactor_casing_transparent.getDefaultState();
				Block port = NCBlocks.fission_port;
				Block buffer = NCBlocks.buffer;
				Block door = NCBlocks.reactor_door;
				Block trapdoor = NCBlocks.reactor_trapdoor;
				if (adjacentOr(pos[0], pos[1], pos[2], casing, casing_transparent, port, buffer, door, trapdoor)) return true;
			}
			else if (meta == 2) {
				IBlockState cell = NCBlocks.cell_block.getDefaultState();
				if (adjacentOr(pos[0], pos[1], pos[2], cell)) return true;
			}
			else if (meta == 3) {
				if (isAdjacentToActiveGraphite(pos[0], pos[1], pos[2])) return true;
			}
		}
		return false;
	}
	
	private boolean findCasingPort(int x, int y, int z) {
		if (world.getBlockState(position(x, y, z)) == NCBlocks.fission_block.getStateFromMeta(0)) return true;
		if (world.getBlockState(position(x, y, z)) == NCBlocks.reactor_casing_transparent.getStateFromMeta(0)) return true;
		if (world.getBlockState(position(x, y, z)).getBlock() instanceof BlockFissionPort) return true;
		if (world.getBlockState(position(x, y, z)).getBlock() instanceof BlockBuffer) return true;
		if (world.getBlockState(position(x, y, z)).getBlock() == NCBlocks.reactor_door) return true;
		if (world.getBlockState(position(x, y, z)).getBlock() == NCBlocks.reactor_trapdoor) return true;
		return false;
	}
	
	private boolean findController(int x, int y, int z) {
		return world.getBlockState(position(x, y, z)).getBlock() instanceof BlockFissionController;
	}
	
	private boolean findActiveCooler(int x, int y, int z) {
		return world.getBlockState(position(x, y, z)).getBlock() instanceof BlockActiveCooler;
	}
	
	private boolean findCasingControllerPort(int x, int y, int z) {
		if (world.getBlockState(position(x, y, z)) == NCBlocks.fission_block.getStateFromMeta(0)) return true;
		if (world.getBlockState(position(x, y, z)) == NCBlocks.reactor_casing_transparent.getStateFromMeta(0)) return true;
		if (world.getBlockState(position(x, y, z)).getBlock() instanceof BlockFissionController) return true;
		if (world.getBlockState(position(x, y, z)).getBlock() instanceof BlockFissionPort) return true;
		if (world.getBlockState(position(x, y, z)).getBlock() instanceof BlockBuffer) return true;
		if (world.getBlockState(position(x, y, z)).getBlock() == NCBlocks.reactor_door) return true;
		if (world.getBlockState(position(x, y, z)).getBlock() == NCBlocks.reactor_trapdoor) return true;
		return false;
	}
	
	private String stringPos(BlockPos pos) {
		return "[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]";
	}
	
	// Finding Structure
	
	private boolean checkStructure() {
		if (shouldStructureCheck()) {
			int l = NCConfig.fission_max_size + 2;
			boolean f = false;
			int rz = 0;
			int z0 = 0, x0 = 0, y0 = 0;
			int z1 = 0, x1 = 0, y1 = 0;
			for (int z = 0; z <= l; z++) {
				if ((findCasingPort(0, 1, 0) || findCasingPort(0, -1, 0)) || ((findCasingPort(1, 1, 0) || findCasingPort(1, -1, 0)) && findCasingPort(1, 0, 0)) || ((findCasingPort(1, 1, 0) && !findCasingPort(1, -1, 0)) && !findCasingPort(1, 0, 0)) || ((!findCasingPort(1, 1, 0) && findCasingPort(1, -1, 0)) && !findCasingPort(1, 0, 0))) {
					if (!findCasingPort(0, 1, -z) && !findCasingPort(0, -1, -z) && (findCasingControllerPort(0, 0, -z + 1) || findCasingControllerPort(0, 1, -z + 1) || findCasingControllerPort(0, -1, -z + 1))) {
						rz = l - z;
						z0 = -z;
						f = true;
						break;
					}
				} else if (!findCasingPort(0, 0, -z) && !findCasingPort(1, 1, -z) && !findCasingPort(1, -1, -z) && findCasingControllerPort(0, 0, -z + 1) && findCasingPort(1, 0, -z) && findCasingPort(1, 1, -z + 1) && findCasingPort(1, -1, -z + 1)) {
					rz = l - z;
					z0 = -z;
					f = true;
					break;
				}
			}
			if (!f) {
				complete = 0;
				problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
				problemPosBool = 0;
				return false;
			}
			f = false;
			for (int y = 0; y <= l; y++) {
				if (!findCasingPort(x0, -y + 1, z0) && !findCasingPort(x0 + 1, -y, z0) && !findCasingPort(x0, -y, z0 + 1) && findCasingControllerPort(x0 + 1, -y, z0 + 1) && findCasingControllerPort(x0, -y + 1, z0 + 1) && findCasingControllerPort(x0 + 1, -y + 1, z0)) {
					y0 = -y;
					f = true;
					break;
				}
			}
			if (!f) {
				complete = 0;
				problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
				problemPosBool = 0;
				return false;
			}
			f = false;
			for (int z = 0; z <= rz; z++) {
				if (!findCasingPort(x0, y0 + 1, z) && !findCasingPort(x0 + 1, y0, z) && !findCasingPort(x0, y0, z - 1) && findCasingControllerPort(x0 + 1, y0, z - 1) && findCasingControllerPort(x0, y0 + 1, z - 1) && findCasingControllerPort(x0 + 1, y0 + 1, z)) {
					z1 = z;
					f = true;
					break;
				}
			}
			if (!f) {
				complete = 0;
				problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
				problemPosBool = 0;
				return false;
			}
			f = false;
			for (int x = 0; x <= l; x++) {
				if (!findCasingPort(x0 + x, y0 + 1, z0) && !findCasingPort(x0 + x - 1, y0, z0) && !findCasingPort(x0 + x, y0, z0 + 1) && findCasingControllerPort(x0 + x - 1, y0, z0 + 1) && findCasingControllerPort(x0 + x, y0 + 1, z0 + 1) && findCasingControllerPort(x0 + x - 1, y0 + 1, z0)) {
					x1 = x0 + x;
					f = true;
					break;
				}
			}
			if (!f) {
				complete = 0;
				problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
				problemPosBool = 0;
				return false;
			}
			f = false;
			for (int y = 0; y <= l; y++) {
				if (!findCasingPort(x0, y0 + y - 1, z0) && !findCasingPort(x0 + 1, y0 + y, z0) && !findCasingPort(x0, y0 + y, z0 + 1) && findCasingControllerPort(x0 + 1, y0 + y, z0 + 1) && findCasingControllerPort(x0, y0 + y - 1, z0 + 1) && findCasingControllerPort(x0 + 1, y0 + y - 1, z0)) {
					y1 = y0 + y;
					f = true;
					break;
				}
			}
			if (!f) {
				complete = 0;
				problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
				problemPosBool = 0;
				return false;
			}
			f = false;
			if ((x0 > 0 || x1 < 0) || (y0 > 0 || y1 < 0) || (z0 > 0 || z1 < 0) || x1 - x0 < 1 || y1 - y0 < 1 || z1 - z0 < 1) {
				problem = I18n.translateToLocalFormatted("gui.container.fission_controller.invalid_structure");
				complete = 0;
				problemPosBool = 0;
				return false;
			}
			for (int z = z0; z <= z1; z++) {
				for (int x = x0; x <= x1; x++) {
					for (int y = y0; y <= y1; y++) {
						if(findController(x, y, z)) {
							if (x == 0 && y == 0 && z == 0) {} else {
								problem = I18n.translateToLocalFormatted("gui.container.fission_controller.multiple_controllers");
								complete = 0;
								problemPosBool = 1;
								problemPosX = x;
								problemPosY = y;
								problemPosZ = z;
								problemPos = stringPos(position(problemPosX, problemPosY, problemPosZ));
								return false;
							}
						}
					}
				}
			}
			for (int z = z0 + 1; z <= z1 - 1; z++) {
				for (int x = x0 + 1; x <= x1 - 1; x++) {
					if(!findCasingPort(x, y0, z) && !(x == 0 && y0 == 0 && z == 0)) {
						problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete_at");
						complete = 0;
						problemPosBool = 1;
						problemPosX = x;
						problemPosY = y0;
						problemPosZ = z;
						problemPos = stringPos(position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
					if(!findCasingPort(x, y1, z) && !(x == 0 && y1 == 0 && z == 0)) {
						problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete_at");
						complete = 0;
						problemPosBool = 1;
						problemPosX = x;
						problemPosY = y1;
						problemPosZ = z;
						problemPos = stringPos(position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
				}
			}
			for (int y = y0 + 1; y <= y1 - 1; y++) {
				for (int x = x0 + 1; x <= x1 - 1; x++) {
					if(!findCasingPort(x, y, z0) && !(x == 0 && y == 0 && z0 == 0)) {
						problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete_at");
						complete = 0;
						problemPosBool = 1;
						problemPosX = x;
						problemPosY = y;
						problemPosZ = z0;
						problemPos = stringPos(position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
					if(!findCasingPort(x, y, z1) && !(x == 0 && y == 0 && z1 == 0)) {
						problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete_at");
						complete = 0;
						problemPosBool = 1;
						problemPosX = x;
						problemPosY = y;
						problemPosZ = z1;
						problemPos = stringPos(position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
				}
				for (int z = z0 + 1; z <= z1 - 1; z++) {
					if(!findCasingPort(x0, y, z) && !(x0 == 0 && y == 0 && z == 0)) {
						problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete_at");
						complete = 0;
						problemPosBool = 1;
						problemPosX = x0;
						problemPosY = y;
						problemPosZ = z;
						problemPos = stringPos(position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
					if(!findCasingPort(x1, y, z) && !(x1 == 0 && y == 0 && z == 0)) {
						problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete_at");
						complete = 0;
						problemPosBool = 1;
						problemPosX = x1;
						problemPosY = y;
						problemPosZ = z;
						problemPos = stringPos(position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
				}
			}
			for (int z = z0 + 1; z <= z1 - 1; z++) {
				for (int x = x0 + 1; x <= x1 - 1; x++) {
					for (int y = y0 + 1; y <= y1 - 1; y++) {
						if(findCasingControllerPort(x, y, z)) {
							problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_in_interior");
							complete = 0;
							problemPosBool = 1;
							problemPosX = x;
							problemPosY = y;
							problemPosZ = z;
							problemPos = stringPos(position(problemPosX, problemPosY, problemPosZ));
							return false;
						}
					}
				}
			}
			complete = 1;
			problemPosBool = 0;
			minX = x0;
			minY = y0;
			minZ = z0;
			maxX = x1;
			maxY = y1;
			maxZ = z1;
			lengthX = x1 + 1 - x0;
			lengthY = y1 + 1 - y0;
			lengthZ = z1 + 1 - z0;
			return true;
		} else {
			return complete == 1;
		}
	}
	
	// Set Fuel and Power and Modify Heat
	
	private void run() {
		double energyThisTick = 0;
		double fuelThisTick = 0;
		double heatThisTick = 0;
		double coolerHeatThisTick = 0;
		double numberOfCells = 0;
		double extraCells = 0;
		double adj1 = 0;
		double adj2 = 0;
		double adj3 = 0;
		double adj4 = 0;
		double adj5 = 0;
		double adj6 = 0;
		boolean generating = false;
		
		double baseRF = getBasePower();
		processTime = (int) getBaseTime();
		double baseHeat = getBaseHeat();
		
		String graphite = "blockGraphite";
		IBlockState cell = NCBlocks.cell_block.getDefaultState();
		IBlockState casing = NCBlocks.fission_block.getStateFromMeta(0);
		IBlockState casing_transparent = NCBlocks.reactor_casing_transparent.getDefaultState();
		
		IBlockState[] cooler = new IBlockState[CoolerTypes.values().length];
		for (int i = 0; i < CoolerTypes.values().length; i++) {
			cooler[i] = NCBlocks.cooler.getStateFromMeta(i);
		}
		
		if (canProcess() && isPowered()) {
			ready = 0;
			generating = true;
		} else if (canProcess() && !isPowered()) {
			ready = 1;
			generating = false;
		} else {
			ready = 0;
			generating = false;
		}

		if (shouldRunCheck()) {
			if (complete == 1) {
				for (int z = minZ + 1; z <= maxZ - 1; z++) {
					for (int x = minX + 1; x <= maxX - 1; x++) {
						for (int y = minY + 1; y <= maxY - 1; y++) {
							if (find(x, y, z, cell)) {
								extraCells = 0;
								if (find(x + 1, y, z, cell) || (findFromOreName(x + 1, y, z, graphite) && find(x + 2, y, z, cell))) extraCells += 1;
								if (find(x - 1, y, z, cell) || (findFromOreName(x - 1, y, z, graphite) && find(x - 2, y, z, cell))) extraCells += 1;
								if (find(x, y + 1, z, cell) || (findFromOreName(x, y + 1, z, graphite) && find(x, y + 2, z, cell))) extraCells += 1;
								if (find(x, y - 1, z, cell) || (findFromOreName(x, y - 1, z, graphite) && find(x, y - 2, z, cell))) extraCells += 1;
								if (find(x, y, z + 1, cell) || (findFromOreName(x, y, z + 1, graphite) && find(x, y, z + 2, cell))) extraCells += 1;
								if (find(x, y, z - 1, cell) || (findFromOreName(x, y, z - 1, graphite) && find(x, y, z - 2, cell))) extraCells += 1;
								
								if (extraCells == 0) numberOfCells += 1;
								else if (extraCells == 1) adj1 += 1;
								else if (extraCells == 2) adj2 += 1;
								else if (extraCells == 3) adj3 += 1;
								else if (extraCells == 4) adj4 += 1;
								else if (extraCells == 5) adj5 += 1;
								else if (extraCells == 6) adj6 += 1;
							}
						}
					}
				}
			}
			
			if (ready == 1 || generating) {
				
				energyThisTick += NCConfig.fission_power*baseRF*(numberOfCells + 2*adj1 + 3*adj2 + 4*adj3 + 5*adj4 + 6*adj5 + 7*adj6);
				heatThisTick += NCConfig.fission_heat_generation*baseHeat*(numberOfCells + 3*adj1 + 6*adj2 + 10*adj3 + 15*adj4 + 21*adj5 + 28*adj6);
				if (generating) fuelThisTick += (numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)*NCConfig.fission_fuel_use;
				
				for (int z = minZ + 1; z <= maxZ - 1; z++) {
					for (int x = minX + 1; x <= maxX - 1; x++) {
						for (int y = minY + 1; y <= maxY - 1; y++) {
							if(findFromOreName(x, y, z, graphite)) {
								heatThisTick += NCConfig.fission_heat_generation*baseHeat*(numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)/16.0D;
								if (adjacentOr(x, y, z, cell)) energyThisTick += NCConfig.fission_power*baseRF*(numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)/8.0D;
							}
						}
					}
				}
			}
			  	
			if (complete == 1) {
				for (int z = minZ + 1; z <= maxZ - 1; z++) {
					for (int x = minX + 1; x <= maxX - 1; x++) {
						for (int y = minY + 1; y <= maxY - 1; y++) {
							if(findCooler(x, y, z, 1)) {
								if (!NCConfig.fission_water_cooler_requirement) coolerHeatThisTick -= NCConfig.fission_cooling_rate[0];
								else if (adjacentOr(x, y, z, casing, casing_transparent)) coolerHeatThisTick -= NCConfig.fission_cooling_rate[0];
							}
							if(findCooler(x, y, z, 2)) {
								if (adjacentOr(x, y, z, cell)) coolerHeatThisTick -= NCConfig.fission_cooling_rate[1];
							}
							if(findCooler(x, y, z, 3)) {
								if (isAdjacentToActiveGraphite(x, y, z)) coolerHeatThisTick -= NCConfig.fission_cooling_rate[2];
							}
							if(findCooler(x, y, z, 4)) {
								if (isAdjacentToActiveCooler(x, y, z, 1) && isAdjacentToActiveCooler(x, y, z, 2)) coolerHeatThisTick -= NCConfig.fission_cooling_rate[3];
							}
							if(findCooler(x, y, z, 5)) {
								if (adjacentToActiveGraphite(x, y, z) >= 2) coolerHeatThisTick -= NCConfig.fission_cooling_rate[4];
							}
							if(findCooler(x, y, z, 6)) {
								if (adjacentAnd(x, y, z, cell, casing) || adjacentAnd(x, y, z, cell, casing_transparent)) coolerHeatThisTick -= NCConfig.fission_cooling_rate[5];
							}
							if(findCooler(x, y, z, 7)) {
								if (sandwich(x, y, z, cooler[1]) && (adjacentOr(x, y, z, casing, casing_transparent) || !NCConfig.fission_water_cooler_requirement)) coolerHeatThisTick -= NCConfig.fission_cooling_rate[6];
							}
							if(findCooler(x, y, z, 8)) {
								if (adjacentOr(x, y, z, casing, casing_transparent) && isAdjacentToActiveCooler(x, y, z, 3)) coolerHeatThisTick -= NCConfig.fission_cooling_rate[7];
							}
							if(findCooler(x, y, z, 9)) {
								if (adjacent(x, y, z, casing) + adjacent(x, y, z, casing_transparent) >= 3) coolerHeatThisTick -= NCConfig.fission_cooling_rate[8];
							}
							if(findCooler(x, y, z, 10)) {
								if (adjacent(x, y, z, cell) >= 2) coolerHeatThisTick -= NCConfig.fission_cooling_rate[9];
							}
							
							if(findActiveCooler(x, y, z)) {
								TileEntity tile = world.getTileEntity(position(x, y, z));
								if (tile != null) if (tile instanceof TileActiveCooler) {
									Tank tank = ((TileActiveCooler) tile).getTanks()[0];
									int fluidAmount = tank.getFluidAmount();
									if (fluidAmount > 0) {
										double amountModified = ((double) fluidAmount)/(2D*NCConfig.fission_update_rate);
										double currentHeat = heat + heatThisTick + coolerHeatThisTick;
										if (tank.getFluidName() == "water") {
											if (!NCConfig.fission_water_cooler_requirement) coolerHeatThisTick -= NCConfig.fission_active_cooling_rate[0]*amountModified;
											else if (adjacentOr(x, y, z, casing, casing_transparent)) coolerHeatThisTick -= NCConfig.fission_active_cooling_rate[0]*amountModified;
										}
										else if (tank.getFluidName() == "redstone") {
											if (adjacentOr(x, y, z, cell)) coolerHeatThisTick -= NCConfig.fission_active_cooling_rate[1]*amountModified;
										}
										else if (tank.getFluidName() == "glowstone") {
											if (adjacentToActiveGraphite(x, y, z) >= 2) coolerHeatThisTick -= NCConfig.fission_active_cooling_rate[2]*amountModified;
										}
										else if (tank.getFluidName() == "liquidhelium") {
											if (adjacentOr(x, y, z, casing, casing_transparent) && isAdjacentToActiveCooler(x, y, z, 3)) coolerHeatThisTick -= NCConfig.fission_active_cooling_rate[3]*amountModified;
										}
										else if (tank.getFluidName() == "ender") {
											if (adjacent(x, y, z, casing) + adjacent(x, y, z, casing_transparent) >= 3) coolerHeatThisTick -= NCConfig.fission_active_cooling_rate[4]*amountModified;
										}
										else if (tank.getFluidName() == "cryotheum") {
											if (adjacent(x, y, z, cell) >= 2) coolerHeatThisTick -= NCConfig.fission_active_cooling_rate[5]*amountModified;
										}
										if (ready == 0 && generating && currentHeat > 0) {
											double newHeat = heat + heatThisTick + coolerHeatThisTick;
											if (newHeat >= 0) ((TileActiveCooler) world.getTileEntity(position(x, y, z))).getTanks()[0].drain(fluidAmount, true); else {
												double heatFraction = currentHeat/(currentHeat - newHeat);
												((TileActiveCooler) world.getTileEntity(position(x, y, z))).getTanks()[0].drain((int) (fluidAmount*heatFraction), true);
											}
										}
									}
								}
							}
						}
					}
				}
				//if (lengthX + lengthY + lengthZ < 10) coolerHeatThisTick -= NCConfig.fission_thorium_heat_generation[0];
			}
			
			if (complete == 1) {
				heatChange = (int) (heatThisTick + coolerHeatThisTick);
				cooling = (int) coolerHeatThisTick;
				efficiency = (int) (100*(energyThisTick)/(NCConfig.fission_power*baseRF*(numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)));
				cells = (int) (numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6);
				setProcessPower((int) energyThisTick);
				setRateMultiplier((int) fuelThisTick);
			} else {
				heatChange = 0;
				cooling = 0;
				efficiency = 0;
				cells = 0;
				setProcessPower(0);
				setRateMultiplier(0);
			}
		}
		
		if (ready == 0 && generating) {
			if (heat + (int) heatChange >= 0) {
				heat += (int) heatChange;
			} else {
				heat = 0;
			}
		} else if ((ready == 1 && !generating) || complete == 1) {
			if (heat + (int) cooling >= 0) {
				heat += (int) cooling;
			} else {
				heat = 0;
			}
		}
	}
	
	// NBT
	
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		//nbt.setInteger("processTime", processTime);
		nbt.setInteger("processPower", processPower);
		nbt.setInteger("rateMultiplier", rateMultiplier);
		nbt.setInteger("heat", heat);
		nbt.setInteger("cooling", cooling);
		nbt.setInteger("efficiency", efficiency);
		nbt.setInteger("cells", cells);
		nbt.setInteger("minX", minX);
		nbt.setInteger("minY", minY);
		nbt.setInteger("minZ", minZ);
		nbt.setInteger("maxX", maxX);
		nbt.setInteger("maxY", maxY);
		nbt.setInteger("maxZ", maxZ);
		nbt.setInteger("lengthX", lengthX);
		nbt.setInteger("lengthY", lengthY);
		nbt.setInteger("lengthZ", lengthZ);
		nbt.setInteger("heatChange", heatChange);
		nbt.setInteger("complete", complete);
		nbt.setInteger("ready", ready);
		nbt.setString("problem", problem);
		nbt.setString("problemPos", problemPos);
		nbt.setInteger("problemPosX", problemPosX);
		nbt.setInteger("problemPosY", problemPosY);
		nbt.setInteger("problemPosZ", problemPosZ);
		nbt.setInteger("problemPosBool", problemPosBool);
		
		return nbt;
	}
			
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		//processTime = nbt.getInteger("processTime");
		processPower = nbt.getInteger("processPower");
		rateMultiplier = nbt.getInteger("rateMultiplier");
		heat = nbt.getInteger("heat");
		cooling = nbt.getInteger("cooling");
		efficiency = nbt.getInteger("efficiency");
		cells = nbt.getInteger("cells");
		minX = nbt.getInteger("minX");
		minY = nbt.getInteger("minY");
		minZ = nbt.getInteger("minZ");
		maxX = nbt.getInteger("maxX");
		maxY = nbt.getInteger("maxY");
		maxZ = nbt.getInteger("maxZ");
		lengthX = nbt.getInteger("lengthX");
		lengthY = nbt.getInteger("lengthY");
		lengthZ = nbt.getInteger("lengthZ");
		heatChange = nbt.getInteger("heatChange");
		complete = nbt.getInteger("complete");
		ready = nbt.getInteger("ready");
		problem = nbt.getString("problem");
		problemPos = nbt.getString("problemPos");
		problemPosX = nbt.getInteger("problemPosX");
		problemPosY = nbt.getInteger("problemPosY");
		problemPosZ = nbt.getInteger("problemPosZ");
		problemPosBool = nbt.getInteger("problemPosBool");
	}
	
	// Inventory Fields

	public int getFieldCount() {
		return 19;
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
		case 8:
			return getRateMultiplier();
		case 9:
			return lengthX;
		case 10:
			return lengthY;
		case 11:
			return lengthZ;
		case 12:
			return heatChange;
		case 13:
			return complete;
		case 14:
			return ready;
		case 15:
			return problemPosX;
		case 16:
			return problemPosY;
		case 17:
			return problemPosZ;
		case 18:
			return problemPosBool;
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
			break;
		case 8:
			setRateMultiplier(value);
			break;
		case 9:
			lengthX = value;
			break;
		case 10:
			lengthY = value;
			break;
		case 11:
			lengthZ = value;
			break;
		case 12:
			heatChange = value;
			break;
		case 13:
			complete = value;
			break;
		case 14:
			ready = value;
			break;
		case 15:
			problemPosX = value;
			break;
		case 16:
			problemPosY = value;
			break;
		case 17:
			problemPosZ = value;
			break;
		case 18:
			problemPosBool = value;
		}
	}
}
