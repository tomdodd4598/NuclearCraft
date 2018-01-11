package nc.tile.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import nc.config.NCConfig;
import nc.enumm.MetaEnums.CoolerType;
import nc.fluid.Tank;
import nc.init.NCBlocks;
import nc.recipe.NCRecipes;
import nc.tile.fluid.TileActiveCooler;
import nc.util.BlockFinder;
import nc.util.BlockPosHelper;
import nc.util.Lang;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/*@Optional.InterfaceList({
	@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers"),
	@Optional.Interface(iface = "li.cil.oc.api.network.ManagedPeripheral", modid = "opencomputers"),
	@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = "computercraft")
})*/
public class TileFissionController extends TileItemGenerator /*implements SimpleComponent, ManagedPeripheral, IPeripheral*/ {
	
	private Random rand = new Random();
	
	public int rateMultiplier, processTime, processPower;
	public int heat, cooling, heatChange, efficiency, cells;
	public int tickCountStructureCheck, tickCountRunCheck;
	public int minX, minY, minZ;
	public int maxX, maxY, maxZ;
	public int lengthX, lengthY, lengthZ = 3;
	public int complete, ready;
	
	public String problem = Lang.localise("gui.container.fission_controller.casing_incomplete");
	public String problemPos = BlockPosHelper.stringPos(pos);
	public int problemPosX = 0, problemPosY = 0, problemPosZ = 0;
	public int problemPosBool = 0;
	
	public static final int BASE_CAPACITY = 64000, BASE_MAX_HEAT = 25000;
	
	private BlockFinder finder;

	public TileFissionController() {
		super("fission_controller", 1, 1, 0, BASE_CAPACITY, NCRecipes.FISSION_RECIPES);
	}
	
	@Override
	public void onAdded() {
		finder = new BlockFinder(pos, world, getBlockMetadata());
		super.onAdded();
		tickCountStructureCheck = 2*NCConfig.fission_update_rate;
		checkStructure();
	}
	
	@Override
	public void updateGenerator() {
		boolean wasGenerating = isGenerating;
		isGenerating = canProcess() && isPowered();
		boolean shouldUpdate = false;
		if(!world.isRemote) if (time == 0) consume();
		tickStructureCheck();
		checkStructure();
		if(!world.isRemote) {
			tickRunCheck();
			run();
			if (overheat()) return;
			if (isGenerating) process();
			if (wasGenerating != isGenerating) {
				shouldUpdate = true;
				updateBlockType();
			}
			pushEnergy();
			if (findAdjacentComparator() && shouldStructureCheck()) shouldUpdate = true;
		}
		if (shouldUpdate) markDirty();
	}
	
	public void tickStructureCheck() {
		if (tickCountStructureCheck > NCConfig.fission_update_rate) tickCountStructureCheck = 0; else tickCountStructureCheck++;
	}
	
	public boolean shouldStructureCheck() {
		return tickCountStructureCheck > NCConfig.fission_update_rate;
	}
	
	public void tickRunCheck() {
		if (tickCountRunCheck > NCConfig.fission_update_rate*2) tickCountRunCheck = 0; else tickCountRunCheck++;
	}
	
	public boolean shouldRunCheck() {
		return tickCountRunCheck > NCConfig.fission_update_rate*2;
	}
	
	public boolean findAdjacentComparator() {
		return finder.adjacent(pos, 1, Blocks.UNPOWERED_COMPARATOR, Blocks.POWERED_COMPARATOR);
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
					if (rand.nextDouble() < 0.18D) world.setBlockState(finder.position(i, j, k), Blocks.LAVA.getStateFromMeta(rand.nextInt(2)));
				}
			}
		}
	}
	
	@Override
	public boolean canProcess() {
		return canProcessStacks() && complete == 1;
	}
	
	// IC2 Tiers
	
	@Override
	public int getSourceTier() {
		return 4;
	}
	
	@Override
	public int getSinkTier() {
		return 4;
	}
	
	// Generating

	@Override
	public int getRateMultiplier() {
		return Math.max(1, rateMultiplier);
	}

	@Override
	public void setRateMultiplier(int value) {
		rateMultiplier = Math.max(1, value);
	}

	@Override
	public int getProcessTime() {
		return Math.max(1, processTime);
	}

	@Override
	public void setProcessTime(int value) {
		processTime = Math.max(1, value);
	}

	@Override
	public int getProcessPower() {
		return processPower;
	}

	@Override
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
		return Lang.localise("gui.container.fission_controller.no_fuel");
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
		if (atLimit(getLengthX(), getLengthY(), getLengthZ(), BASE_MAX_HEAT)) return Integer.MAX_VALUE;
		if (getLengthX() <= 0 || getLengthY() <= 0 || getLengthZ() <= 0) return BASE_MAX_HEAT;
		return BASE_MAX_HEAT*getLengthX()*getLengthY()*getLengthZ();
	}
	
	// Finding Blocks
	
	private boolean findCell(BlockPos pos) {
		return finder.find(pos, NCBlocks.cell_block);
	}
	
	private boolean findCell(int x, int y, int z) {
		return findCell(finder.position(x, y, z));
	}
	
	private boolean findGraphite(BlockPos pos) {
		return finder.find(pos, "blockGraphite");
	}
	
	private boolean findGraphite(int x, int y, int z) {
		return findGraphite(finder.position(x, y, z));
	}
	
	private boolean findCellOnSide(int x, int y, int z, EnumFacing side) {
		return findCell(finder.position(x, y, z).offset(side));
	}
	
	private boolean findGraphiteThenCellOnSide(int x, int y, int z, EnumFacing side) {
		return findGraphite(finder.position(x, y, z).offset(side)) && findCell(finder.position(x, y, z).offset(side, 2));
	}
	
	private int activeGraphiteAdjacentCount(BlockPos pos) {
		int count = 0;
		BlockPosHelper helper = new BlockPosHelper(pos);
		for (BlockPos blockPos : helper.adjacents()) {
			if (findGraphite(blockPos) && cellAdjacent(blockPos)) count++;
		}
		return count;
	}
	
	private int activeGraphiteAdjacentCount(int x, int y, int z) {
		return activeGraphiteAdjacentCount(finder.position(x, y, z));
	}
	
	private boolean cellAdjacent(BlockPos pos) {
		return finder.adjacent(pos, 1, NCBlocks.cell_block);
	}
	
	private boolean cellAdjacent(int x, int y, int z) {
		return finder.adjacent(x, y, z, 1, NCBlocks.cell_block);
	}
	
	private int cellAdjacentCount(BlockPos pos) {
		return finder.adjacentCount(pos, 1, NCBlocks.cell_block);
	}
	
	private int cellAdjacentCount(int x, int y, int z) {
		return cellAdjacentCount(finder.position(x, y, z));
	}
	
	private boolean activeGraphiteAdjacent(BlockPos pos) {
		BlockPosHelper helper = new BlockPosHelper(pos);
		for (BlockPos blockPos : helper.adjacents()) {
			if (findGraphite(blockPos) && cellAdjacent(blockPos)) return true;
		}
		return false;
	}
	
	private boolean activeGraphiteAdjacent(int x, int y, int z) {
		return activeGraphiteAdjacent(finder.position(x, y, z));
	}
	
	private boolean findCooler(BlockPos pos, int meta) {
		return finder.find(pos, NCBlocks.cooler.getStateFromMeta(meta));
	}
	
	private boolean findCooler(int x, int y, int z, int meta) {
		return findCooler(finder.position(x, y, z), meta);
	}
	
	private boolean activeCoolerAdjacent(BlockPos pos, int meta) {
		BlockPosHelper helper = new BlockPosHelper(pos);
		for (BlockPos blockPos : helper.adjacents()) {
			if (findCooler(blockPos, meta)) if (coolerRequirements(blockPos, meta)) return true;
		}
		return false;
	}
	
	private boolean activeCoolerAdjacent(int x, int y, int z, int meta) {
		return activeCoolerAdjacent(finder.position(x, y, z), meta);
	}
	
	private boolean activeCoolerConfiguration(BlockPos pos, int meta, List<BlockPos[]> posArrays) {
		for (BlockPos[] posArray : posArrays) {
			if (finder.configuration(posArray, NCBlocks.cooler.getStateFromMeta(meta))) {
				for (BlockPos blockPos : posArray) if (!coolerRequirements(blockPos, meta)) return false;
				return true;
			}
		}
		return false;
	}
	
	private boolean activeCoolerHorizontal(BlockPos pos, int meta) {
		return activeCoolerConfiguration(pos, meta, new BlockPosHelper(pos).horizontalsList());
	}
	
	private boolean activeCoolerAxial(BlockPos pos, int meta) {
		return activeCoolerConfiguration(pos, meta, new BlockPosHelper(pos).axialsList());
	}
	
	private boolean coolerRequirements(BlockPos pos, int meta) {
		if (meta == 1) { // Water
			return !NCConfig.fission_water_cooler_requirement || casingAllAdjacent(pos);
		}
		else if (meta == 2) { // Redstone
			return cellAdjacent(pos);
		}
		else if (meta == 3) { // Quartz
			return activeGraphiteAdjacent(pos);
		}
		else if (meta == 4) { // Gold
			return activeCoolerAdjacent(pos, 1) && activeCoolerAdjacent(pos, 2);
		}
		else if (meta == 5) { // Glowstone
			return activeGraphiteAdjacentCount(pos) >= 2;
		}
		else if (meta == 6) { // Lapis
			return cellAdjacent(pos) && casingAllAdjacent(pos);
		}
		else if (meta == 7) { // Diamond
			return activeCoolerHorizontal(pos, 1) && casingAllAdjacent(pos);
		}
		else if (meta == 8) { // Liquid Helium
			return activeCoolerAdjacent(pos, 3) && casingAllAdjacent(pos);
		}
		else if (meta == 9) { // Enderium
			return casingAllAdjacentCount(pos) == 3;
		}
		else if (meta == 10) { // Cryotheum
			return cellAdjacentCount(pos) >= 2;
		}
		else if (meta == 11) { // Iron
			return activeCoolerAdjacent(pos, 4);
		}
		else if (meta == 12) { // Emerald
			return activeGraphiteAdjacent(pos) && cellAdjacent(pos);
		}
		else if (meta == 13) { // Copper
			return activeCoolerAdjacent(pos, 5);
		}
		else if (meta == 14) { // Tin
			return activeCoolerAxial(pos, 6);
		}
		else if (meta == 15) { // Magnesium
			return casingAllAdjacent(pos) && activeCoolerAdjacent(pos, 8);
		}
		return false;
	}
	
	private boolean coolerRequirements(int x, int y, int z, int meta) {
		return coolerRequirements(finder.position(x, y, z), meta);
	}
	
	private boolean findCasing(BlockPos pos) {
		return finder.find(pos, NCBlocks.fission_block.getStateFromMeta(0), NCBlocks.reactor_casing_transparent, NCBlocks.fission_port, NCBlocks.buffer, NCBlocks.reactor_door, NCBlocks.reactor_trapdoor);
	}
	
	private boolean findCasing(int x, int y, int z) {
		return findCasing(finder.position(x, y, z));
	}
	
	private boolean findController(BlockPos pos) {
		return finder.find(pos, NCBlocks.fission_controller_idle, NCBlocks.fission_controller_active);
	}
	
	private boolean findController(int x, int y, int z) {
		return findController(finder.position(x, y, z));
	}
	
	private boolean findCasingAll(BlockPos pos) {
		return findCasing(pos) || findController(pos);
	}
	
	private boolean findCasingAll(int x, int y, int z) {
		return findCasingAll(finder.position(x, y, z));
	}
	
	private boolean casingAllAdjacent(BlockPos pos) {
		BlockPosHelper posHelper = new BlockPosHelper(pos);
		for (BlockPos blockPos : posHelper.adjacents()) if (findCasingAll(blockPos)) return true;
		return false;
	}
	
	private int casingAllAdjacentCount(BlockPos pos) {
		int count = 0;
		BlockPosHelper posHelper = new BlockPosHelper(pos);
		for (BlockPos blockPos : posHelper.adjacents()) if (findCasingAll(blockPos)) count++;
		return count;
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
				if ((findCasing(0, 1, 0) || findCasing(0, -1, 0)) || ((findCasing(1, 1, 0) || findCasing(1, -1, 0)) && findCasing(1, 0, 0)) || ((findCasing(1, 1, 0) && !findCasing(1, -1, 0)) && !findCasing(1, 0, 0)) || ((!findCasing(1, 1, 0) && findCasing(1, -1, 0)) && !findCasing(1, 0, 0))) {
					if (!findCasing(0, 1, -z) && !findCasing(0, -1, -z) && (findCasingAll(0, 0, -z + 1) || findCasingAll(0, 1, -z + 1) || findCasingAll(0, -1, -z + 1))) {
						rz = l - z;
						z0 = -z;
						f = true;
						break;
					}
				} else if (!findCasing(0, 0, -z) && !findCasing(1, 1, -z) && !findCasing(1, -1, -z) && findCasingAll(0, 0, -z + 1) && findCasing(1, 0, -z) && findCasing(1, 1, -z + 1) && findCasing(1, -1, -z + 1)) {
					rz = l - z;
					z0 = -z;
					f = true;
					break;
				}
			}
			if (!f) {
				complete = 0;
				problem = Lang.localise("gui.container.fission_controller.casing_incomplete");
				problemPosBool = 0;
				return false;
			}
			f = false;
			for (int y = 0; y <= l; y++) {
				if (!findCasing(x0, -y + 1, z0) && !findCasing(x0 + 1, -y, z0) && !findCasing(x0, -y, z0 + 1) && findCasingAll(x0 + 1, -y, z0 + 1) && findCasingAll(x0, -y + 1, z0 + 1) && findCasingAll(x0 + 1, -y + 1, z0)) {
					y0 = -y;
					f = true;
					break;
				}
			}
			if (!f) {
				complete = 0;
				problem = Lang.localise("gui.container.fission_controller.casing_incomplete");
				problemPosBool = 0;
				return false;
			}
			f = false;
			for (int z = 0; z <= rz; z++) {
				if (!findCasing(x0, y0 + 1, z) && !findCasing(x0 + 1, y0, z) && !findCasing(x0, y0, z - 1) && findCasingAll(x0 + 1, y0, z - 1) && findCasingAll(x0, y0 + 1, z - 1) && findCasingAll(x0 + 1, y0 + 1, z)) {
					z1 = z;
					f = true;
					break;
				}
			}
			if (!f) {
				complete = 0;
				problem = Lang.localise("gui.container.fission_controller.casing_incomplete");
				problemPosBool = 0;
				return false;
			}
			f = false;
			for (int x = 0; x <= l; x++) {
				if (!findCasing(x0 + x, y0 + 1, z0) && !findCasing(x0 + x - 1, y0, z0) && !findCasing(x0 + x, y0, z0 + 1) && findCasingAll(x0 + x - 1, y0, z0 + 1) && findCasingAll(x0 + x, y0 + 1, z0 + 1) && findCasingAll(x0 + x - 1, y0 + 1, z0)) {
					x1 = x0 + x;
					f = true;
					break;
				}
			}
			if (!f) {
				complete = 0;
				problem = Lang.localise("gui.container.fission_controller.casing_incomplete");
				problemPosBool = 0;
				return false;
			}
			f = false;
			for (int y = 0; y <= l; y++) {
				if (!findCasing(x0, y0 + y - 1, z0) && !findCasing(x0 + 1, y0 + y, z0) && !findCasing(x0, y0 + y, z0 + 1) && findCasingAll(x0 + 1, y0 + y, z0 + 1) && findCasingAll(x0, y0 + y - 1, z0 + 1) && findCasingAll(x0 + 1, y0 + y - 1, z0)) {
					y1 = y0 + y;
					f = true;
					break;
				}
			}
			if (!f) {
				complete = 0;
				problem = Lang.localise("gui.container.fission_controller.casing_incomplete");
				problemPosBool = 0;
				return false;
			}
			f = false;
			if ((x0 > 0 || x1 < 0) || (y0 > 0 || y1 < 0) || (z0 > 0 || z1 < 0) || x1 - x0 < 1 || y1 - y0 < 1 || z1 - z0 < 1) {
				problem = Lang.localise("gui.container.fission_controller.invalid_structure");
				complete = 0;
				problemPosBool = 0;
				return false;
			}
			for (int z = z0; z <= z1; z++) for (int x = x0; x <= x1; x++) for (int y = y0; y <= y1; y++) {
				if(findController(x, y, z)) {
					if (x == 0 && y == 0 && z == 0) {} else {
						problem = Lang.localise("gui.container.fission_controller.multiple_controllers");
						complete = 0;
						problemPosBool = 1;
						problemPosX = x; problemPosY = y; problemPosZ = z;
						problemPos = BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
				}
			}
			for (int z = z0 + 1; z <= z1 - 1; z++) for (int x = x0 + 1; x <= x1 - 1; x++) {
				if(!findCasing(x, y0, z) && !(x == 0 && y0 == 0 && z == 0)) {
					problem = Lang.localise("gui.container.fission_controller.casing_incomplete_at");
					complete = 0;
					problemPosBool = 1;
					problemPosX = x; problemPosY = y0; problemPosZ = z;
					problemPos = BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
					return false;
				}
				if(!findCasing(x, y1, z) && !(x == 0 && y1 == 0 && z == 0)) {
					problem = Lang.localise("gui.container.fission_controller.casing_incomplete_at");
					complete = 0;
					problemPosBool = 1;
					problemPosX = x; problemPosY = y1; problemPosZ = z;
					problemPos = BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
					return false;
				}
			}
			for (int y = y0 + 1; y <= y1 - 1; y++) {
				for (int x = x0 + 1; x <= x1 - 1; x++) {
					if(!findCasing(x, y, z0) && !(x == 0 && y == 0 && z0 == 0)) {
						problem = Lang.localise("gui.container.fission_controller.casing_incomplete_at");
						complete = 0;
						problemPosBool = 1;
						problemPosX = x; problemPosY = y; problemPosZ = z0;
						problemPos = BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
					if(!findCasing(x, y, z1) && !(x == 0 && y == 0 && z1 == 0)) {
						problem = Lang.localise("gui.container.fission_controller.casing_incomplete_at");
						complete = 0;
						problemPosBool = 1;
						problemPosX = x; problemPosY = y; problemPosZ = z1;
						problemPos = BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
				}
				for (int z = z0 + 1; z <= z1 - 1; z++) {
					if(!findCasing(x0, y, z) && !(x0 == 0 && y == 0 && z == 0)) {
						problem = Lang.localise("gui.container.fission_controller.casing_incomplete_at");
						complete = 0;
						problemPosBool = 1;
						problemPosX = x0; problemPosY = y; problemPosZ = z;
						problemPos = BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
					if(!findCasing(x1, y, z) && !(x1 == 0 && y == 0 && z == 0)) {
						problem = Lang.localise("gui.container.fission_controller.casing_incomplete_at");
						complete = 0;
						problemPosBool = 1;
						problemPosX = x1; problemPosY = y; problemPosZ = z;
						problemPos = BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
				}
			}
			for (int z = z0 + 1; z <= z1 - 1; z++) for (int x = x0 + 1; x <= x1 - 1; x++) for (int y = y0 + 1; y <= y1 - 1; y++) {
				if(findCasingAll(x, y, z)) {
					problem = Lang.localise("gui.container.fission_controller.casing_in_interior");
					complete = 0;
					problemPosBool = 1;
					problemPosX = x; problemPosY = y; problemPosZ = z;
					problemPos = BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
					return false;
				}
			}
			complete = 1;
			problemPosBool = 0;
			minX = x0; minY = y0; minZ = z0;
			maxX = x1; maxY = y1; maxZ = z1;
			lengthX = x1 + 1 - x0; lengthY = y1 + 1 - y0; lengthZ = z1 + 1 - z0;
			setCapacity();
			
			return true;
		} else return complete == 1;
	}
	
	private boolean atLimit(int x, int y, int z, int divider) {
		return x*y*z > Integer.MAX_VALUE/divider;
	}
	
	private void setCapacity() {
		storage.setStorageCapacity(getNewCapacity());
		storage.setMaxTransfer(getNewCapacity());
	}
	
	private int getNewCapacity() {
		if (atLimit(getLengthX(), getLengthY(), getLengthZ(), BASE_CAPACITY)) return Integer.MAX_VALUE;
		if (getLengthX() <= 0 || getLengthY() <= 0 || getLengthZ() <= 0) return BASE_CAPACITY;
		return BASE_CAPACITY*getLengthX()*getLengthY()*getLengthZ();
	}
	
	// Set Fuel and Power and Modify Heat
	
	private void run() {
		double energyThisTick = 0;
		double fuelThisTick = 0;
		double heatThisTick = 0;
		double coolerHeatThisTick = 0;
		double numberOfCells = 0;
		double adj1 = 0, adj2 = 0, adj3 = 0, adj4 = 0, adj5 = 0, adj6 = 0;
		double energyMultThisTick = 0;
		
		double baseRF = getBasePower();
		processTime = (int) getBaseTime();
		double baseHeat = getBaseHeat();
		
		ready = canProcess() && !isPowered() ? 1 : 0;
		boolean generating = canProcess() && isPowered();

		if (shouldRunCheck()) {
			if (complete == 1) {
				for (int z = minZ + 1; z <= maxZ - 1; z++) for (int x = minX + 1; x <= maxX - 1; x++) for (int y = minY + 1; y <= maxY - 1; y++) {
					if (findCell(x, y, z)) {
						double extraCells = 0;
						for (EnumFacing side : EnumFacing.VALUES) {
							if (findCellOnSide(x, y, z, side) || findGraphiteThenCellOnSide(x, y, z, side)) extraCells += 1;
						}
						
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
				
			energyMultThisTick += numberOfCells + 2*adj1 + 3*adj2 + 4*adj3 + 5*adj4 + 6*adj5 + 7*adj6;
			if (canProcess()) {
				energyThisTick += NCConfig.fission_power*baseRF*(numberOfCells + 2*adj1 + 3*adj2 + 4*adj3 + 5*adj4 + 6*adj5 + 7*adj6);
				heatThisTick += NCConfig.fission_heat_generation*baseHeat*(numberOfCells + 3*adj1 + 6*adj2 + 10*adj3 + 15*adj4 + 21*adj5 + 28*adj6);
				if (generating) fuelThisTick += (numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)*NCConfig.fission_fuel_use;
			}
			
			for (int z = minZ + 1; z <= maxZ - 1; z++) for (int x = minX + 1; x <= maxX - 1; x++) for (int y = minY + 1; y <= maxY - 1; y++) {
				if (findGraphite(x, y, z)) {
					if (canProcess()) heatThisTick += NCConfig.fission_heat_generation*baseRF*(numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)/16.0D;
					if (cellAdjacent(x, y, z)) {
						energyMultThisTick += (numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)/8.0D;
						if (canProcess()) energyThisTick += NCConfig.fission_power*baseRF*(numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)/8.0D;
					}
				}
			}
			  	
			if (complete == 1) {
				for (int z = minZ + 1; z <= maxZ - 1; z++) for (int x = minX + 1; x <= maxX - 1; x++) for (int y = minY + 1; y <= maxY - 1; y++) {
					for (int i = 1; i < CoolerType.values().length; i++) {
						if (findCooler(x, y, z, i)) if (coolerRequirements(x, y, z, i)) {
							coolerHeatThisTick -= NCConfig.fission_cooling_rate[i - 1];
							break;
						}
					}
					
					if(finder.find(x, y, z, NCBlocks.active_cooler)) {
						TileEntity tile = world.getTileEntity(finder.position(x, y, z));
						if (tile != null) if (tile instanceof TileActiveCooler) {
							Tank tank = ((TileActiveCooler) tile).getTanks()[0];
							int fluidAmount = tank.getFluidAmount();
							if (fluidAmount > 0) {
								double currentHeat = heat + heatThisTick + coolerHeatThisTick;
								for (int i = 1; i < CoolerType.values().length; i++) {
									if (tank.getFluidName() == CoolerType.values()[i].getFluidName()) {
										if (coolerRequirements(x, y, z, i)) {
											coolerHeatThisTick -= (NCConfig.fission_active_cooling_rate[i - 1]*fluidAmount)/(2D*NCConfig.fission_update_rate);
											break;
										}
									}
								}
								if (currentHeat > 0) {
									double newHeat = heat + heatThisTick + coolerHeatThisTick;
									if (newHeat >= 0) ((TileActiveCooler) tile).getTanks()[0].drain(fluidAmount, true); else {
										double heatFraction = currentHeat/(currentHeat - newHeat);
										((TileActiveCooler) tile).getTanks()[0].drain((int) (fluidAmount*heatFraction), true);
									}
								}
							}
						}
					}
				}
			}
			
			if (complete == 1) {
				heatChange = (int) (heatThisTick + coolerHeatThisTick);
				cooling = (int) coolerHeatThisTick;
				efficiency = (int) (100*energyMultThisTick/(numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6));
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
		
		if (generating) {
			if (heat + heatChange >= 0) {
				heat += heatChange;
			} else {
				heat = 0;
			}
		} else if (ready == 1 || complete == 1) {
			if (heat + cooling >= 0) {
				heat += cooling;
			} else {
				heat = 0;
			}
		}
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
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
			
	@Override
	public void readAll(NBTTagCompound nbt) {
		lengthX = nbt.getInteger("lengthX");
		lengthY = nbt.getInteger("lengthY");
		lengthZ = nbt.getInteger("lengthZ");
		storage.setStorageCapacity(getNewCapacity());
		super.readAll(nbt);
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

	@Override
	public int getFieldCount() {
		return 19;
	}

	@Override
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

	@Override
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
	
	// Computers
	
	public enum ComputerMethod {
		isComplete,
		isHotEnough,
		getProblem,
		getSize,
		getEnergyStored,
		getHeatLevel,
		getHeatChange,
		getEfficiency,
		getFuelLevels,
		getOutputLevels,
		getFuelTypes,
		getOutputTypes,
		getComboProcessTime,
		getProcessTime,
		getComboPower,
		getPower,
		getActiveCooling,
		doVentFuel,
		doVentAllFuels,
		doVentOutput,
		doVentAllOutputs
	}
	
	public static final int NUMBER_OF_METHODS = ComputerMethod.values().length;

	public static final String[] METHOD_NAMES = new String[NUMBER_OF_METHODS];
	static {
		ComputerMethod[] methods = ComputerMethod.values();
		for(ComputerMethod method : methods) {
			METHOD_NAMES[method.ordinal()] = method.toString();
		}
	}

	public static final Map<String, Integer> METHOD_IDS = new HashMap<String, Integer>();
	static {
		for (int i = 0; i < NUMBER_OF_METHODS; ++i) {
			METHOD_IDS.put(METHOD_NAMES[i], i);
		}
	}
	
	public Object[] callMethod(int method, Object[] arguments) throws Exception {
		return new Object[] { complete == 1 };
	}
	
	// OpenComputers
	
	/*@Override
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] invoke(String method, Context context, Arguments args) throws Exception {
		final Object[] arguments = new Object[args.count()];
		for (int i = 0; i < args.count(); ++i) {
			arguments[i] = args.checkAny(i);
		}
		final Integer methodId = METHOD_IDS.get(method);
		if (methodId == null) {
			throw new NoSuchMethodError();
		}
		return callMethod(methodId, arguments);
	}

	@Override
	@Callback
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return Global.MOD_SHORT_ID + "_fission_reactor";
	}
	
	@Override
	@Callback
	@Optional.Method(modid = "opencomputers")
	public String[] methods() {
		return METHOD_NAMES;
	}*/
	
	// ComputerCraft
	
	/*@Override
	@Optional.Method(modid = "computercraft")
	public String getType() {
		return Global.MOD_SHORT_ID + "_fission_reactor";
	}
	
	@Override
	@Optional.Method(modid = "computercraft")
	public String[] getMethodNames() {
		return METHOD_NAMES;
	}
	
	@Override
	@Optional.Method(modid = "computercraft")
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException {
		try {
			return callMethod(method, arguments);
		} catch(Exception e) {
			throw new LuaException(e.getMessage());
		}
	}
	
	@Override
	@Optional.Method(modid = "computercraft")
	public void attach(IComputerAccess computer) {}

	@Override
	@Optional.Method(modid = "computercraft")
	public void detach(IComputerAccess computer) {}

	@Override
	@Optional.Method(modid = "computercraft")
	public boolean equals(IPeripheral other) {
		return hashCode() == other.hashCode();
	}*/
}
