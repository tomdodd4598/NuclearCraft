package nc.tile.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import nc.Global;
import nc.config.NCConfig;
import nc.enumm.MetaEnums.CoolerType;
import nc.init.NCBlocks;
import nc.recipe.NCRecipes;
import nc.tile.fluid.TileActiveCooler;
import nc.tile.internal.Tank;
import nc.util.ArrayHelper;
import nc.util.BlockFinder;
import nc.util.BlockPosHelper;
import nc.util.EnergyHelper;
import nc.util.Lang;
import nc.util.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

/*@Optional.InterfaceList({
	@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers"),
	@Optional.Interface(iface = "li.cil.oc.api.network.ManagedPeripheral", modid = "opencomputers"),
	@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = "computercraft")
})*/
public class TileFissionController extends TileItemGenerator /*implements SimpleComponent, ManagedPeripheral, IPeripheral*/ {
	
	private Random rand = new Random();
	
	public int rateMultiplier, processTime, processPower;
	public int heat, cooling, heatChange, efficiency, cells, heatMult;
	public int tickCountStructureCheck, tickCountRunCheck;
	public int minX, minY, minZ;
	public int maxX, maxY, maxZ;
	public int lengthX, lengthY, lengthZ = 3;
	public int complete, ready;
	public int ports = 1, currentEnergyStored;
	
	public String problem = Lang.localise("gui.container.fission_controller.casing_incomplete");
	public String problemPos = BlockPosHelper.stringPos(pos);
	public int problemPosX = 0, problemPosY = 0, problemPosZ = 0;
	public int problemPosBool = 0;
	
	public static final int BASE_CAPACITY = 64000, BASE_MAX_HEAT = 25000;
	
	private BlockFinder finder;
	
	private boolean newRules;
	
	public TileFissionController() {
		this(false);
	}

	public TileFissionController(boolean newRules) {
		super("fission_controller", 1, 1, 0, BASE_CAPACITY, NCRecipes.Type.FISSION);
		this.newRules = newRules;
	}
	
	@Override
	public int getGuiID() {
		return 100;
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
		if (!world.isRemote) if (time == 0) consume();
		tickStructureCheck();
		checkStructure();
		if (!world.isRemote) {
			tickRunCheck();
			if (newRules) newRun(); else run();
			if (overheat()) return;
			if (isGenerating) process();
			if (wasGenerating != isGenerating) {
				shouldUpdate = true;
				updateBlockType();
			}
			pushEnergy();
			currentEnergyStored = getEnergyStored();
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
		Block corium = RegistryHelper.getBlock(Global.MOD_ID, "fluid_corium");
		world.removeTileEntity(pos);
		world.setBlockState(pos, corium.getDefaultState());
		for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j <= maxY; j++) {
				for (int k = minZ; k <= maxZ; k++) {
					if (rand.nextDouble() < 0.18D) world.setBlockState(finder.position(i, j, k), corium.getStateFromMeta(rand.nextInt(2)));
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
		return EnergyHelper.getEUSourceTier(storage.getCapacity());
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
			return (double) getFuelStats().get(0)*NCConfig.fission_fuel_use;
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
	
	private boolean findModerator(BlockPos pos) {
		return finder.find(pos, "blockGraphite", "blockBeryllium");
	}
	
	private boolean findModerator(int x, int y, int z) {
		return findModerator(finder.position(x, y, z));
	}
	
	private boolean findCellOnSide(int x, int y, int z, EnumFacing side) {
		return findCell(finder.position(x, y, z).offset(side));
	}
	
	private boolean findModeratorThenCellOnSide(int x, int y, int z, EnumFacing side) {
		return findModerator(finder.position(x, y, z).offset(side)) && findCell(finder.position(x, y, z).offset(side, 2));
	}
	
	private boolean newFindModeratorThenCellOnSide(int x, int y, int z, EnumFacing side) {
		for (int i = 1; i <= NCConfig.fission_neutron_reach; i++) {
			for (int j = 1; j <= i; j++) if (!findModerator(finder.position(x, y, z).offset(side, j))) return false;
			if (findCell(finder.position(x, y, z).offset(side, i + 1))) return true;
		}
		return false;
	}
	
	private int moderatorAdjacentCount(BlockPos pos) {
		int count = 0;
		BlockPosHelper helper = new BlockPosHelper(pos);
		for (BlockPos blockPos : helper.adjacents()) if (findModerator(blockPos)) count++;
		return count;
	}
	
	private int moderatorAdjacentCount(int x, int y, int z) {
		return moderatorAdjacentCount(finder.position(x, y, z));
	}
	
	private int activeModeratorAdjacentCount(BlockPos pos) {
		int count = 0;
		BlockPosHelper helper = new BlockPosHelper(pos);
		for (BlockPos blockPos : helper.adjacents()) {
			if (findModerator(blockPos) && cellAdjacent(blockPos)) count++;
		}
		return count;
	}
	
	private int activeModeratorAdjacentCount(int x, int y, int z) {
		return activeModeratorAdjacentCount(finder.position(x, y, z));
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
	
	private boolean activeModeratorAdjacent(BlockPos pos) {
		BlockPosHelper helper = new BlockPosHelper(pos);
		for (BlockPos blockPos : helper.adjacents()) {
			if (findModerator(blockPos) && cellAdjacent(blockPos)) return true;
		}
		return false;
	}
	
	private boolean activeModeratorAdjacent(int x, int y, int z) {
		return activeModeratorAdjacent(finder.position(x, y, z));
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
	
	private int activeCoolerAdjacentCount(BlockPos pos, int meta) {
		int count = 0;
		BlockPosHelper helper = new BlockPosHelper(pos);
		for (BlockPos blockPos : helper.adjacents()) {
			if (findCooler(blockPos, meta)) if (coolerRequirements(blockPos, meta)) count++;
		}
		return count;
	}
	
	private int activeCoolerAdjacentCount(int x, int y, int z, int meta) {
		return activeCoolerAdjacentCount(finder.position(x, y, z), meta);
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
		switch (meta) {
		case 1: // Water
			return !NCConfig.fission_water_cooler_requirement || (newRules ? cellAdjacent(pos) || activeModeratorAdjacent(pos) : casingAllAdjacent(pos));
		case 2: // Redstone
			return cellAdjacent(pos);
		case 3: // Quartz
			return activeModeratorAdjacent(pos);
		case 4: // Gold
			return activeCoolerAdjacent(pos, 1) && activeCoolerAdjacent(pos, 2);
		case 5: // Glowstone
			return activeModeratorAdjacentCount(pos) >= 2;
		case 6: // Lapis
			return cellAdjacent(pos) && casingAllAdjacent(pos);
		case 7: // Diamond
			return newRules ? activeCoolerAdjacentCount(pos, 1) >= 2 && activeCoolerAdjacent(pos, 3) : activeCoolerHorizontal(pos, 1) && casingAllAdjacent(pos);
		case 8: // Liquid Helium
			return newRules ? activeCoolerAdjacentCount(pos, 2) == 1 && casingAllAdjacent(pos) : activeCoolerAdjacent(pos, 3) && casingAllAdjacent(pos);
		case 9: // Enderium
			return casingAllOneVertex(pos);
		case 10: // Cryotheum
			return cellAdjacentCount(pos) >= 2;
		case 11: // Iron
			return activeCoolerAdjacent(pos, 4);
		case 12: // Emerald
			return activeModeratorAdjacent(pos) && cellAdjacent(pos);
		case 13: // Copper
			return activeCoolerAdjacent(pos, 5);
		case 14: // Tin
			return activeCoolerAxial(pos, 6);
		case 15: // Magnesium
			return newRules ? casingAllAdjacent(pos) && activeModeratorAdjacent(pos) : casingAllAdjacent(pos) && activeCoolerAdjacent(pos, 8);
		default:
			return false;
		}
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
		return finder.find(pos, NCBlocks.fission_controller_idle, NCBlocks.fission_controller_active, NCBlocks.fission_controller_new_idle, NCBlocks.fission_controller_new_active);
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
	
	private boolean findPort(BlockPos pos) {
		return finder.find(pos, NCBlocks.fission_port);
	}
	
	private boolean findPort(int x, int y, int z) {
		return findPort(finder.position(x, y, z));
	}
	
	private boolean casingAllAdjacent(BlockPos pos) {
		BlockPosHelper posHelper = new BlockPosHelper(pos);
		for (BlockPos blockPos : posHelper.adjacents()) if (findCasingAll(blockPos)) return true;
		return false;
	}
	
	private boolean casingAllOneVertex(BlockPos pos) {
		int count = 0;
		BlockPosHelper posHelper = new BlockPosHelper(pos);
		posList: for (BlockPos[] vertexPosList : posHelper.vertexList()) {
			for (BlockPos blockPos : vertexPosList) if (!findCasingAll(blockPos)) continue posList;
			count++;
			if (count > 1) return false;
		}
		return count == 1;
	}
	
	// Finding Structure
	
	private boolean checkStructure() {
		if (shouldStructureCheck()) {
			int maxLength = NCConfig.fission_max_size + 1;
			boolean validStructure = false;
			int maxZCheck = 0;
			int minZ = 0, minX = 0, minY = 0;
			int maxZ = 0, maxX = 0, maxY = 0;
			int portCount = 0;
			for (int z = 0; z <= maxLength; z++) {
				if ((findCasing(0, 1, 0) || findCasing(0, -1, 0)) || ((findCasing(1, 1, 0) || findCasing(1, -1, 0)) && findCasing(1, 0, 0)) || ((findCasing(1, 1, 0) && !findCasing(1, -1, 0)) && !findCasing(1, 0, 0)) || ((!findCasing(1, 1, 0) && findCasing(1, -1, 0)) && !findCasing(1, 0, 0))) {
					if (!findCasing(0, 1, -z) && !findCasing(0, -1, -z) && (findCasingAll(0, 0, -z + 1) || findCasingAll(0, 1, -z + 1) || findCasingAll(0, -1, -z + 1))) {
						maxZCheck = maxLength - z;
						minZ = -z;
						validStructure = true;
						break;
					}
				} else if (!findCasing(0, 0, -z) && !findCasing(1, 1, -z) && !findCasing(1, -1, -z) && findCasingAll(0, 0, -z + 1) && findCasing(1, 0, -z) && findCasing(1, 1, -z + 1) && findCasing(1, -1, -z + 1)) {
					maxZCheck = maxLength - z;
					minZ = -z;
					validStructure = true;
					break;
				}
			}
			if (!validStructure) {
				complete = 0;
				problem = Lang.localise("gui.container.fission_controller.casing_incomplete");
				problemPosBool = 0;
				return false;
			}
			validStructure = false;
			for (int y = 0; y <= maxLength; y++) {
				if (!findCasing(minX, -y + 1, minZ) && !findCasing(minX + 1, -y, minZ) && !findCasing(minX, -y, minZ + 1) && findCasingAll(minX + 1, -y, minZ + 1) && findCasingAll(minX, -y + 1, minZ + 1) && findCasingAll(minX + 1, -y + 1, minZ)) {
					minY = -y;
					validStructure = true;
					break;
				}
			}
			if (!validStructure) {
				complete = 0;
				problem = Lang.localise("gui.container.fission_controller.casing_incomplete");
				problemPosBool = 0;
				return false;
			}
			validStructure = false;
			for (int z = 0; z <= maxZCheck; z++) {
				if (!findCasing(minX, minY + 1, z) && !findCasing(minX + 1, minY, z) && !findCasing(minX, minY, z - 1) && findCasingAll(minX + 1, minY, z - 1) && findCasingAll(minX, minY + 1, z - 1) && findCasingAll(minX + 1, minY + 1, z)) {
					maxZ = z;
					validStructure = true;
					break;
				}
			}
			if (!validStructure) {
				complete = 0;
				problem = Lang.localise("gui.container.fission_controller.casing_incomplete");
				problemPosBool = 0;
				return false;
			}
			validStructure = false;
			for (int x = 0; x <= maxLength; x++) {
				if (!findCasing(minX + x, minY + 1, minZ) && !findCasing(minX + x - 1, minY, minZ) && !findCasing(minX + x, minY, minZ + 1) && findCasingAll(minX + x - 1, minY, minZ + 1) && findCasingAll(minX + x, minY + 1, minZ + 1) && findCasingAll(minX + x - 1, minY + 1, minZ)) {
					maxX = minX + x;
					validStructure = true;
					break;
				}
			}
			if (!validStructure) {
				complete = 0;
				problem = Lang.localise("gui.container.fission_controller.casing_incomplete");
				problemPosBool = 0;
				return false;
			}
			validStructure = false;
			for (int y = 0; y <= maxLength; y++) {
				if (!findCasing(minX, minY + y - 1, minZ) && !findCasing(minX + 1, minY + y, minZ) && !findCasing(minX, minY + y, minZ + 1) && findCasingAll(minX + 1, minY + y, minZ + 1) && findCasingAll(minX, minY + y - 1, minZ + 1) && findCasingAll(minX + 1, minY + y - 1, minZ)) {
					maxY = minY + y;
					validStructure = true;
					break;
				}
			}
			if (!validStructure) {
				complete = 0;
				problem = Lang.localise("gui.container.fission_controller.casing_incomplete");
				problemPosBool = 0;
				return false;
			}
			if ((minX > 0 || maxX < 0) || (minY > 0 || maxY < 0) || (minZ > 0 || maxZ < 0) || maxX - minX < 1 || maxY - minY < 1 || maxZ - minZ < 1) {
				problem = Lang.localise("gui.container.fission_controller.invalid_structure");
				complete = 0;
				problemPosBool = 0;
				return false;
			}
			for (int z = minZ; z <= maxZ; z++) for (int x = minX; x <= maxX; x++) for (int y = minY; y <= maxY; y++) {
				if (findController(x, y, z)) {
					if (!(x == 0 && y == 0 && z == 0)) {
						problem = Lang.localise("gui.container.fission_controller.multiple_controllers");
						complete = 0;
						problemPosBool = 1;
						problemPosX = x; problemPosY = y; problemPosZ = z;
						problemPos = BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
				}
			}
			for (int z = minZ + 1; z <= maxZ - 1; z++) for (int x = minX + 1; x <= maxX - 1; x++) {
				if (!findCasing(x, minY, z) && !(x == 0 && minY == 0 && z == 0)) {
					problem = Lang.localise("gui.container.fission_controller.casing_incomplete_at");
					complete = 0;
					problemPosBool = 1;
					problemPosX = x; problemPosY = minY; problemPosZ = z;
					problemPos = BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
					return false;
				}
				if (!findCasing(x, maxY, z) && !(x == 0 && maxY == 0 && z == 0)) {
					problem = Lang.localise("gui.container.fission_controller.casing_incomplete_at");
					complete = 0;
					problemPosBool = 1;
					problemPosX = x; problemPosY = maxY; problemPosZ = z;
					problemPos = BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
					return false;
				}
			}
			for (int y = minY + 1; y <= maxY - 1; y++) {
				for (int x = minX + 1; x <= maxX - 1; x++) {
					if (!findCasing(x, y, minZ) && !(x == 0 && y == 0 && minZ == 0)) {
						problem = Lang.localise("gui.container.fission_controller.casing_incomplete_at");
						complete = 0;
						problemPosBool = 1;
						problemPosX = x; problemPosY = y; problemPosZ = minZ;
						problemPos = BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
					if (!findCasing(x, y, maxZ) && !(x == 0 && y == 0 && maxZ == 0)) {
						problem = Lang.localise("gui.container.fission_controller.casing_incomplete_at");
						complete = 0;
						problemPosBool = 1;
						problemPosX = x; problemPosY = y; problemPosZ = maxZ;
						problemPos = BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
					if (findPort(x, y, minZ)) portCount++;
					if (findPort(x, y, maxZ)) portCount++;
				}
				for (int z = minZ + 1; z <= maxZ - 1; z++) {
					if (!findCasing(minX, y, z) && !(minX == 0 && y == 0 && z == 0)) {
						problem = Lang.localise("gui.container.fission_controller.casing_incomplete_at");
						complete = 0;
						problemPosBool = 1;
						problemPosX = minX; problemPosY = y; problemPosZ = z;
						problemPos = BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
					if (!findCasing(maxX, y, z) && !(maxX == 0 && y == 0 && z == 0)) {
						problem = Lang.localise("gui.container.fission_controller.casing_incomplete_at");
						complete = 0;
						problemPosBool = 1;
						problemPosX = maxX; problemPosY = y; problemPosZ = z;
						problemPos = BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
					if (findPort(minX, y, z)) portCount++;
					if (findPort(maxX, y, z)) portCount++;
				}
			}
			for (int z = minZ + 1; z <= maxZ - 1; z++) for (int x = minX + 1; x <= maxX - 1; x++) for (int y = minY + 1; y <= maxY - 1; y++) {
				if (findCasingAll(x, y, z)) {
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
			this.minX = minX; this.minY = minY; this.minZ = minZ;
			this.maxX = maxX; this.maxY = maxY; this.maxZ = maxZ;
			lengthX = maxX + 1 - minX; lengthY = maxY + 1 - minY; lengthZ = maxZ + 1 - minZ;
			ports = Math.max(1, portCount);
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
		double noAdjacentCells[] = new double[] {0D, 0D, 0D, 0D, 0D, 0D, 0D};
		double energyMultThisTick = 0, heatMultThisTick = 0;
		
		setProcessTime((int) getBaseTime());
		double baseRF = getBasePower();
		double baseHeat = getBaseHeat();
		
		ready = canProcess() && !isPowered() ? 1 : 0;
		boolean generating = canProcess() && isPowered();

		if (shouldRunCheck()) {
			if (complete == 1) {
				for (int z = minZ + 1; z <= maxZ - 1; z++) for (int x = minX + 1; x <= maxX - 1; x++) for (int y = minY + 1; y <= maxY - 1; y++) {
					
					// Cells
					if (findCell(x, y, z)) {
						int extraCells = 0;
						for (EnumFacing side : EnumFacing.VALUES) {
							if (findCellOnSide(x, y, z, side) || findModeratorThenCellOnSide(x, y, z, side)) extraCells += 1;
						}
						
						for (int n = 0; n <= 6; n++) if (extraCells == n) {
							noAdjacentCells[n] += 1;
							energyMultThisTick += n + 1;
							heatMultThisTick += (n + 1)*(n + 2)/2;
							if (canProcess()) {
								energyThisTick += baseRF*(n + 1);
								heatThisTick += baseHeat*(n + 1)*(n + 2)/2;
							}
							break;
						}
						
						if (generating) fuelThisTick += NCConfig.fission_fuel_use;
					}
					
					// Moderators
					if (findModerator(x, y, z)) {
						if (canProcess()) {
							heatMultThisTick += ArrayHelper.sum(noAdjacentCells)/16D;
							heatThisTick += NCConfig.fission_heat_generation*baseRF*ArrayHelper.sum(noAdjacentCells)/16D;
						}
						if (cellAdjacent(x, y, z)) {
							energyMultThisTick += ArrayHelper.sum(noAdjacentCells)/8D;
							if (canProcess()) energyThisTick += NCConfig.fission_power*baseRF*ArrayHelper.sum(noAdjacentCells)/8D;
						}
					}
					
					// Passive Coolers
					for (int i = 1; i < CoolerType.values().length; i++) {
						if (findCooler(x, y, z, i)) if (coolerRequirements(x, y, z, i)) {
							coolerHeatThisTick -= CoolerType.values()[i].getCooling();
							break;
						}
					}
				}
				
				for (int z = minZ + 1; z <= maxZ - 1; z++) for (int x = minX + 1; x <= maxX - 1; x++) for (int y = minY + 1; y <= maxY - 1; y++) {
					
					// Active Coolers
					if (finder.find(x, y, z, NCBlocks.active_cooler)) {
						TileEntity tile = world.getTileEntity(finder.position(x, y, z));
						if (tile != null) if (tile instanceof TileActiveCooler) {
							Tank tank = ((TileActiveCooler) tile).getTanks()[0];
							int fluidAmount = tank.getFluidAmount();
							if (fluidAmount > 0) {
								double currentHeat = heat + (generating ? heatThisTick : 0) + coolerHeatThisTick;
								for (int i = 1; i < CoolerType.values().length; i++) {
									if (tank.getFluidName() == CoolerType.values()[i].getFluidName()) {
										if (coolerRequirements(x, y, z, i)) {
											coolerHeatThisTick -= (NCConfig.fission_active_cooling_rate[i - 1]*fluidAmount)/(2D*NCConfig.fission_update_rate);
											break;
										}
									}
								}
								if (currentHeat > 0) {
									double newHeat = heat + (generating ? heatThisTick : 0) + coolerHeatThisTick;
									if (newHeat >= 0) ((TileActiveCooler) world.getTileEntity(finder.position(x, y, z))).getTanks()[0].drain(MathHelper.ceil(fluidAmount), true); else {
										double heatFraction = currentHeat/(currentHeat - newHeat);
										((TileActiveCooler) world.getTileEntity(finder.position(x, y, z))).getTanks()[0].drain(MathHelper.ceil(fluidAmount*heatFraction), true);
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
				cells = (int) ArrayHelper.sum(noAdjacentCells);
				efficiency = cells == 0 ? 0 : (int) (100D*energyMultThisTick/ArrayHelper.sum(noAdjacentCells));
				heatMult = cells == 0 ? 0 : (int) (100D*heatMultThisTick/ArrayHelper.sum(noAdjacentCells));
				setProcessPower((int) energyThisTick);
				setRateMultiplier((int) fuelThisTick);
			} else {
				heatChange = 0;
				cooling = 0;
				efficiency = 0;
				heatMult = 0;
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
	
	private void newRun() {
		double energyThisTick = 0;
		double fuelThisTick = 0;
		double heatThisTick = 0;
		double coolerHeatThisTick = 0;
		double noAdjacentCells[] = new double[] {0D, 0D, 0D, 0D, 0D, 0D, 0D};
		double energyMultThisTick = 0, heatMultThisTick = 0;
		
		setProcessTime((int) getBaseTime());
		double baseRF = NCConfig.fission_power*getBasePower();
		double baseHeat = NCConfig.fission_heat_generation*getBaseHeat();
		
		double moderatorPowerMultiplier = NCConfig.fission_moderator_extra_power/6D;
		double moderatorHeatMultiplier = NCConfig.fission_moderator_extra_heat/6D;
		
		ready = canProcess() && !isPowered() ? 1 : 0;
		boolean generating = canProcess() && isPowered();

		if (shouldRunCheck()) {
			if (complete == 1) {
				for (int z = minZ + 1; z <= maxZ - 1; z++) for (int x = minX + 1; x <= maxX - 1; x++) for (int y = minY + 1; y <= maxY - 1; y++) {
					
					// Cells
					if (findCell(x, y, z)) {
						int extraCells = 0;
						for (EnumFacing side : EnumFacing.VALUES) {
							if (findCellOnSide(x, y, z, side) || newFindModeratorThenCellOnSide(x, y, z, side)) extraCells += 1;
						}
						
						for (int n = 0; n <= 6; n++) if (extraCells == n) {
							noAdjacentCells[n] += 1;
							energyMultThisTick += n + 1;
							heatMultThisTick += (n + 1)*(n + 2)/2;
							if (canProcess()) {
								energyThisTick += baseRF*(n + 1);
								heatThisTick += baseHeat*(n + 1)*(n + 2)/2;
							}
							break;
						}
						
						if (generating) fuelThisTick += NCConfig.fission_fuel_use;
						
						// Adjacent Moderator
						energyMultThisTick += moderatorPowerMultiplier*moderatorAdjacentCount(x, y, z)*(extraCells + 1);
						heatMultThisTick += moderatorHeatMultiplier*moderatorAdjacentCount(x, y, z)*(extraCells + 1);
						
						energyThisTick += baseRF*moderatorPowerMultiplier*moderatorAdjacentCount(x, y, z)*(extraCells + 1);
						heatThisTick += baseHeat*moderatorHeatMultiplier*moderatorAdjacentCount(x, y, z)*(extraCells + 1);
					}
					
					// Extra Moderators
					if (canProcess()) if (findModerator(x, y, z)) {
						if (!cellAdjacent(x, y, z)) heatThisTick += baseHeat;
					}
					
					// Passive Coolers
					for (int i = 1; i < CoolerType.values().length; i++) {
						if (findCooler(x, y, z, i)) if (coolerRequirements(x, y, z, i)) {
							coolerHeatThisTick -= CoolerType.values()[i].getCooling();
							break;
						}
					}	
				}
				
				for (int z = minZ + 1; z <= maxZ - 1; z++) for (int x = minX + 1; x <= maxX - 1; x++) for (int y = minY + 1; y <= maxY - 1; y++) {
					
					// Active Coolers
					if (finder.find(x, y, z, NCBlocks.active_cooler)) {
						TileEntity tile = world.getTileEntity(finder.position(x, y, z));
						if (tile != null) if (tile instanceof TileActiveCooler) {
							Tank tank = ((TileActiveCooler) tile).getTanks()[0];
							int fluidAmount = tank.getFluidAmount();
							if (fluidAmount > 0) {
								double currentHeat = heat + (generating ? heatThisTick : 0) + coolerHeatThisTick;
								for (int i = 1; i < CoolerType.values().length; i++) {
									if (tank.getFluidName() == CoolerType.values()[i].getFluidName()) {
										if (coolerRequirements(x, y, z, i)) {
											coolerHeatThisTick -= (NCConfig.fission_active_cooling_rate[i - 1]*fluidAmount)/(2D*NCConfig.fission_update_rate);
											break;
										}
									}
								}
								if (currentHeat > 0) {
									double newHeat = heat + (generating ? heatThisTick : 0) + coolerHeatThisTick;
									if (newHeat >= 0) ((TileActiveCooler) world.getTileEntity(finder.position(x, y, z))).getTanks()[0].drain(MathHelper.ceil(fluidAmount), true); else {
										double heatFraction = currentHeat/(currentHeat - newHeat);
										((TileActiveCooler) world.getTileEntity(finder.position(x, y, z))).getTanks()[0].drain(MathHelper.ceil(fluidAmount*heatFraction), true);
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
				cells = (int) ArrayHelper.sum(noAdjacentCells);
				efficiency = cells == 0 ? 0 : (int) (100D*energyMultThisTick/ArrayHelper.sum(noAdjacentCells));
				heatMult = cells == 0 ? 0 : (int) (100D*heatMultThisTick/ArrayHelper.sum(noAdjacentCells));
				setProcessPower((int) energyThisTick);
				setRateMultiplier((int) fuelThisTick);
			} else {
				heatChange = 0;
				cooling = 0;
				efficiency = 0;
				heatMult = 0;
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
		nbt.setInteger("processTime", processTime);
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
		nbt.setInteger("heatMult", heatMult);
		nbt.setInteger("complete", complete);
		nbt.setInteger("ready", ready);
		nbt.setString("problem", problem);
		nbt.setString("problemPos", problemPos);
		nbt.setInteger("problemPosX", problemPosX);
		nbt.setInteger("problemPosY", problemPosY);
		nbt.setInteger("problemPosZ", problemPosZ);
		nbt.setInteger("problemPosBool", problemPosBool);
		nbt.setBoolean("newRules", newRules);
		nbt.setInteger("ports", ports);
		nbt.setInteger("currentEnergyStored", currentEnergyStored);
		
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
		processTime = nbt.getInteger("processTime");
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
		heatMult = nbt.getInteger("heatMult");
		complete = nbt.getInteger("complete");
		ready = nbt.getInteger("ready");
		problem = nbt.getString("problem");
		problemPos = nbt.getString("problemPos");
		problemPosX = nbt.getInteger("problemPosX");
		problemPosY = nbt.getInteger("problemPosY");
		problemPosZ = nbt.getInteger("problemPosZ");
		problemPosBool = nbt.getInteger("problemPosBool");
		newRules = nbt.getBoolean("newRules");
		ports = nbt.getInteger("ports");
		currentEnergyStored = nbt.getInteger("currentEnergyStored");
	}
	
	// Inventory Fields

	@Override
	public int getFieldCount() {
		return 20;
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
		case 19:
			return heatMult;
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
			break;
		case 19:
			heatMult = value;
		}
	}
	
	// Allow ports to open GUI
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return world.getTileEntity(pos) != this ? false : player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= Math.max(lengthX*lengthX + lengthY*lengthY + lengthZ*lengthZ, 64.0D);
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
		for (ComputerMethod method : methods) {
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
