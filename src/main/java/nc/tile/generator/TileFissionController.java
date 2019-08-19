package nc.tile.generator;

import java.util.List;
import java.util.Random;
import java.util.Set;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import nc.Global;
import nc.ModCheck;
import nc.block.tile.generator.BlockFissionController;
import nc.block.tile.generator.BlockFissionControllerNewFixed;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.enumm.MetaEnums.CoolerType;
import nc.init.NCBlocks;
import nc.network.tile.FissionUpdatePacket;
import nc.radiation.RadiationHelper;
import nc.recipe.NCRecipes;
import nc.tile.IGui;
import nc.tile.dummy.TileFissionPort;
import nc.tile.fluid.TileActiveCooler;
import nc.tile.internal.fluid.Tank;
import nc.util.BlockFinder;
import nc.util.BlockPosHelper;
import nc.util.EnergyHelper;
import nc.util.Lang;
import nc.util.MaterialHelper;
import nc.util.NCMath;
import nc.util.RegistryHelper;
import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileFissionController extends TileItemGenerator implements IGui<FissionUpdatePacket>, SimpleComponent {
	
	private Random rand = new Random();
	
	public double baseProcessHeat = 0;
	public double heat, cooling, heatChange, efficiency, heatMult;
	public int cells;
	public int minX, minY, minZ;
	public int maxX, maxY, maxZ;
	public int lengthX, lengthY, lengthZ = 3;
	public int complete, ready, comparatorStrength;
	public int ports = 1, currentEnergyStored = 0, energyChange = 0;
	
	public String problem = CASING_INCOMPLETE;
	public String problemPos = "";
	public int problemPosX = 0, problemPosY = 0, problemPosZ = 0;
	
	public static final int BASE_CAPACITY = 64000, BASE_MAX_HEAT = 25000;
	
	private boolean isActivated = false;
	public boolean computerActivated = false;
	
	private Object[] oc_layout;
	
	public static final String NO_FUEL = Lang.localise("gui.container.fission_controller.no_fuel");
	public static final String GENERIC_FUEL = Lang.localise("gui.container.fission_controller.generic_fuel");
	
	public static final String CASING_INCOMPLETE = Lang.localise("gui.container.fission_controller.casing_incomplete");
	public static final String INVALID_STRUCTURE = Lang.localise("gui.container.fission_controller.invalid_structure");
	public static final String CASING_IN_INTERIOR_AT = Lang.localise("gui.container.fission_controller.casing_in_interior");
	public static final String EXTRA_CONTROLLER_AT = Lang.localise("gui.container.fission_controller.multiple_controllers");
	public static final String CASING_INCOMPLETE_AT = Lang.localise("gui.container.fission_controller.casing_incomplete_at");
	public static final String POS = Lang.localise("gui.container.fission_controller.pos");
	public static final String NO_PROBLEM = Lang.localise("gui.container.fission_controller.no_problem");
	
	private BlockFinder finder;
	
	private boolean newRules;
	
	public static class New extends TileFissionController {
		
		public New() {
			super(true);
		}
	}
	
	public static class Old extends TileFissionController {
		
		public Old() {
			super(false);
		}
	}

	private TileFissionController(boolean newRules) {
		super("fission_controller", 1, 1, 0, defaultItemSorptions(1, 1), BASE_CAPACITY, NCRecipes.fission);
		this.newRules = newRules;
	}
	
	// Ticking
	
	@Override
	public void onAdded() {
		finder = new BlockFinder(pos, world, getBlockMetadata() & 7);
		super.onAdded();
	}
	
	@Override
	public void updateGenerator() {
		if (fixControllerBlock()) {
			return;
		}
		if (!world.isRemote) {
			boolean wasActivated = isActivated, wasProcessing = isProcessing;
			isActivated = isActivated();
			isProcessing = isProcessing();
			if (wasActivated != isActivated) refreshMultiblock(true);
			else checkStructure(false);
			energyChange = getEnergyStored() - currentEnergyStored;
			if (newRules) newRun(false); else run(false);
			if (overheat()) return;
			if (isProcessing) process();
			else getRadiationSource().setRadiationLevel(0D);
			boolean shouldUpdate = false;
			if (wasProcessing != isProcessing) {
				shouldUpdate = true;
				updateBlockType();
				sendUpdateToAllPlayers();
			}
			pushEnergy();
			currentEnergyStored = getEnergyStored();
			int compStrength = getComparatorStrength();
			if (comparatorStrength != compStrength && findAdjacentComparator()) {
				shouldUpdate = true;
			}
			comparatorStrength = compStrength;
			sendUpdateToListeningPlayers();
			if (shouldUpdate) {
				markDirty();
			}
		}
	}
	
	private boolean fixControllerBlock() {
		if (!newRules) return false;
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof BlockFissionController) {
			BlockFissionController controller = (BlockFissionController) state.getBlock();
			if (!controller.isNew) {
				newRules = false;
				return false;
			}
			int meta = NCBlocks.fission_controller_idle.getMetaFromState(state);
			if (controller.isActive) meta |= 8;
			
			world.setBlockState(pos, NCBlocks.fission_controller_new_fixed.getStateFromMeta(meta));
			return true;
		}
		return false;
	}
	
	public int getComparatorStrength() {
		if (heatChange > 0 || NCConfig.fission_force_heat_comparator) {
			return (int) MathHelper.clamp(1500D/NCConfig.fission_comparator_max_heat*heat/getMaxHeat(), 0D, 15D);
		} else {
			return (int) MathHelper.clamp(15D*getEnergyStored()/getMaxEnergyStored(), 0D, 15D);
		}
	}
	
	public void refreshMultiblock(boolean checkBlocks) {
		checkStructure(checkBlocks);
		if (newRules) newRun(checkBlocks); else run(checkBlocks);
		if (checkBlocks && (!isActivated() || !readyToProcess())) stopActiveCooling();
	}
	
	@Override
	public void refreshActivity() {
		boolean canProcessInputsOld = canProcessInputs;
		super.refreshActivity();
		refreshMultiblock(canProcessInputsOld != canProcessInputs);
	}
	
	@Override
	public void refreshActivityOnProduction() {
		boolean canProcessInputsOld = canProcessInputs;
		super.refreshActivityOnProduction();
		refreshMultiblock(canProcessInputsOld != canProcessInputs);
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
		BlockPos middle = finder.position((minX + maxX)/2, (minY + maxY)/2, (minZ + maxZ)/2);
		
		IRadiationSource chunkSource = RadiationHelper.getRadiationSource(world.getChunk(middle));
		if (chunkSource != null) {
			RadiationHelper.addToSourceRadiation(chunkSource, 8D*baseProcessRadiation*cells*NCConfig.fission_fuel_use*NCConfig.fission_meltdown_radiation_multiplier);
		}
		
		IBlockState corium = RegistryHelper.getBlock(Global.MOD_ID + ":fluid_corium").getDefaultState();
		world.removeTileEntity(pos);
		world.setBlockState(pos, corium);
		
		if (NCConfig.fission_explosions) {
			world.createExplosion(null, middle.getX(), middle.getY(), middle.getZ(), getLengthX()*getLengthX() + getLengthY()*getLengthY() + getLengthZ()*getLengthZ(), true);
		}
		
		for (EnumFacing dir : EnumFacing.VALUES) {
			BlockPos offPos = pos.offset(dir);
			if (rand.nextDouble() < 0.25D) {
				world.removeTileEntity(offPos);
				world.setBlockState(offPos, corium);
			}
		}
		
		for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j <= maxY; j++) {
				for (int k = minZ; k <= maxZ; k++) {
					BlockPos position = finder.position(i, j, k);
					if (findCell(position)) {
						world.removeTileEntity(position);
						world.setBlockState(position, corium);
						
						for (EnumFacing dir : EnumFacing.VALUES) {
							BlockPos offPos = position.offset(dir);
							if (finder.find(offPos, "blockGraphite")) {
								for (EnumFacing offDir : EnumFacing.VALUES) {
									BlockPos graphiteOffPos = offPos.offset(offDir);
									if (MaterialHelper.isReplaceable(world.getBlockState(graphiteOffPos).getMaterial())) {
										world.setBlockState(graphiteOffPos, Blocks.FIRE.getDefaultState().withProperty(BlockFire.NORTH, Boolean.valueOf(offDir == EnumFacing.SOUTH)).withProperty(BlockFire.EAST, Boolean.valueOf(offDir == EnumFacing.WEST)).withProperty(BlockFire.SOUTH, Boolean.valueOf(offDir == EnumFacing.NORTH)).withProperty(BlockFire.WEST, Boolean.valueOf(offDir == EnumFacing.EAST)).withProperty(BlockFire.UPPER, Boolean.valueOf(offDir == EnumFacing.DOWN)));
									}
								}
							}
							else if (rand.nextDouble() < 0.75D) {
								world.removeTileEntity(offPos);
								world.setBlockState(offPos, corium);
							}
						}
					}
				}
			}
		}
		
		/*for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j <= maxY; j++) {
				for (int k = minZ; k <= maxZ; k++) {
					if (rand.nextDouble() < 0.18D) {
						BlockPos position = finder.position(i, j, k);
						world.removeTileEntity(position);
						world.setBlockState(position, corium);
					}
				}
			}
		}*/
	}
	
	@Override
	public void setState(boolean isActive, TileEntity tile) {
		if (getBlockType() instanceof BlockFissionControllerNewFixed) {
			((BlockFissionControllerNewFixed)getBlockType()).setStateNewFixed(isActive, this);
		}
		else super.setState(isActive, tile);
	}
	
	// Processor Stats
	
	@Override
	public boolean setRecipeStats() {
		if (recipeInfo == null) {
			baseProcessTime = defaultProcessTime;
			baseProcessPower = defaultProcessPower;
			baseProcessHeat = 0D;
			baseProcessRadiation = 0D;
			return false;
		}
		baseProcessTime = recipeInfo.getRecipe().getFissionFuelTime();
		baseProcessPower = recipeInfo.getRecipe().getFissionFuelPower();
		baseProcessHeat = recipeInfo.getRecipe().getFissionFuelHeat();
		baseProcessRadiation = recipeInfo.getRecipe().getFissionFuelRadiation();
		return true;
	}
	
	// Processing
	
	@Override
	public boolean isProcessing() {
		return readyToProcess() && isActivated;
	}
	
	@Override
	public boolean readyToProcess() {
		return canProcessInputs && hasConsumed && complete == 1;
	}
	
	private boolean isActivated() {
		return getIsRedstonePowered() || computerActivated;
	}
	
	// IC2 Tiers
	
	@Override
	public int getEUSourceTier() {
		return EnergyHelper.getEUTier(processPower);
	}
	
	@Override
	public int getEUSinkTier() {
		return 10;
	}
	
	// Reactor Stats
	
	public String getFuelName() {
		if (recipeInfo == null) return NO_FUEL;
		if (recipeInfo.getRecipe().extras().size() < 4) return GENERIC_FUEL;
		Object fuelNameInfo = recipeInfo.getRecipe().extras().get(3);
		if (fuelNameInfo instanceof String) return ((String) fuelNameInfo).replace('_', '-');
		else return GENERIC_FUEL;
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
		if (NCMath.atIntLimit(getLengthX()*getLengthY()*getLengthZ(), BASE_MAX_HEAT)) return Integer.MAX_VALUE;
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
			return newRules ? activeCoolerAdjacent(pos, 1) && activeCoolerAdjacent(pos, 3) : activeCoolerHorizontal(pos, 1) && casingAllAdjacent(pos);
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
		return finder.find(pos, NCBlocks.fission_controller_new_fixed, NCBlocks.fission_controller_idle, NCBlocks.fission_controller_active, NCBlocks.fission_controller_new_idle, NCBlocks.fission_controller_new_active);
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
		boolean found = finder.find(pos, NCBlocks.fission_port);
		if (found) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileFissionPort) {
				((TileFissionPort)tile).masterPosition = this.pos;
				return true;
			}
		}
		return false;
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
	
	private static boolean notOrigin(int x, int y, int z) {
		return x != 0 || y != 0 || z != 0;
	}
	
	// Finding Structure
	
	public boolean checkStructure(boolean checkBlocks) {
		if (checkBlocks) {
			int maxLength = NCConfig.fission_max_size + 1;
			boolean validStructure = false;
			int maxZCheck = 0;
			int minZ = 0, minX = 0, minY = 0;
			int maxZ = 0, maxX = 0, maxY = 0;
			int portCount = 0;
			for (int z = 0; z <= maxLength; z++) {
				if ((findCasingAll(0, 1, 0) || findCasingAll(0, -1, 0)) || ((findCasingAll(1, 1, 0) || findCasingAll(1, -1, 0)) && findCasingAll(1, 0, 0)) || ((findCasingAll(1, 1, 0) && !findCasingAll(1, -1, 0)) && !findCasingAll(1, 0, 0)) || ((!findCasingAll(1, 1, 0) && findCasingAll(1, -1, 0)) && !findCasingAll(1, 0, 0))) {
					if (!findCasingAll(0, 1, -z) && !findCasingAll(0, -1, -z) && (findCasingAll(0, 0, -z + 1) || findCasingAll(0, 1, -z + 1) || findCasingAll(0, -1, -z + 1))) {
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
				problem = CASING_INCOMPLETE;
				problemPos = "";
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
				problem = CASING_INCOMPLETE;
				problemPos = "";
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
				problem = CASING_INCOMPLETE;
				problemPos = "";
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
				problem = CASING_INCOMPLETE;
				problemPos = "";
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
				problem = CASING_INCOMPLETE;
				problemPos = "";
				return false;
			}
			if ((minX > 0 || maxX < 0) || (minY > 0 || maxY < 0) || (minZ > 0 || maxZ < 0) || maxX - minX < 1 || maxY - minY < 1 || maxZ - minZ < 1) {
				problem = INVALID_STRUCTURE;
				complete = 0;
				problemPos = "";
				return false;
			}
			for (int z = minZ; z <= maxZ; z++) for (int x = minX; x <= maxX; x++) for (int y = minY; y <= maxY; y++) {
				if (findController(x, y, z)) {
					if (notOrigin(x, y, z)) {
						problem = EXTRA_CONTROLLER_AT;
						complete = 0;
						problemPosX = x; problemPosY = y; problemPosZ = z;
						problemPos = POS + " " + BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
				}
			}
			for (int z = minZ + 1; z <= maxZ - 1; z++) for (int x = minX + 1; x <= maxX - 1; x++) {
				if (!findCasing(x, minY, z) && notOrigin(x, minY, z)) {
					problem = CASING_INCOMPLETE_AT;
					complete = 0;
					problemPosX = x; problemPosY = minY; problemPosZ = z;
					problemPos = POS + " " + BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
					return false;
				}
				if (!findCasing(x, maxY, z) && notOrigin(x, maxY, z)) {
					problem = CASING_INCOMPLETE_AT;
					complete = 0;
					problemPosX = x; problemPosY = maxY; problemPosZ = z;
					problemPos = POS + " " + BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
					return false;
				}
			}
			for (int y = minY + 1; y <= maxY - 1; y++) {
				for (int x = minX + 1; x <= maxX - 1; x++) {
					if (!findCasing(x, y, minZ) && notOrigin(x, y, minZ)) {
						problem = CASING_INCOMPLETE_AT;
						complete = 0;
						problemPosX = x; problemPosY = y; problemPosZ = minZ;
						problemPos = POS + " " + BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
					if (!findCasing(x, y, maxZ) && notOrigin(x, y, maxZ)) {
						problem = CASING_INCOMPLETE_AT;
						complete = 0;
						problemPosX = x; problemPosY = y; problemPosZ = maxZ;
						problemPos = POS + " " + BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
					if (findPort(x, y, minZ)) portCount++;
					if (findPort(x, y, maxZ)) portCount++;
				}
				for (int z = minZ + 1; z <= maxZ - 1; z++) {
					if (!findCasing(minX, y, z) && notOrigin(minX, y, z)) {
						problem = CASING_INCOMPLETE_AT;
						complete = 0;
						problemPosX = minX; problemPosY = y; problemPosZ = z;
						problemPos = POS + " " + BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
					if (!findCasing(maxX, y, z) && notOrigin(maxX, y, z)) {
						problem = CASING_INCOMPLETE_AT;
						complete = 0;
						problemPosX = maxX; problemPosY = y; problemPosZ = z;
						problemPos = POS + " " + BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
						return false;
					}
					if (findPort(minX, y, z)) portCount++;
					if (findPort(maxX, y, z)) portCount++;
				}
			}
			for (int z = minZ + 1; z <= maxZ - 1; z++) for (int x = minX + 1; x <= maxX - 1; x++) for (int y = minY + 1; y <= maxY - 1; y++) {
				if (findCasingAll(x, y, z)) {
					problem = CASING_IN_INTERIOR_AT;
					complete = 0;
					problemPosX = x; problemPosY = y; problemPosZ = z;
					problemPos = POS + " " + BlockPosHelper.stringPos(finder.position(problemPosX, problemPosY, problemPosZ));
					return false;
				}
			}
			problem = NO_PROBLEM;
			complete = 1;
			problemPos = "";
			this.minX = minX; this.minY = minY; this.minZ = minZ;
			this.maxX = maxX; this.maxY = maxY; this.maxZ = maxZ;
			lengthX = maxX + 1 - minX; lengthY = maxY + 1 - minY; lengthZ = maxZ + 1 - minZ;
			ports = Math.max(1, portCount);
			setCapacity();
			
			return true;
		} else return complete == 1;
	}
	
	private void setCapacity() {
		getEnergyStorage().setStorageCapacity(getNewCapacity());
		getEnergyStorage().setMaxTransfer(getNewCapacity());
	}
	
	private int getNewCapacity() {
		if (NCMath.atIntLimit(getLengthX()*getLengthY()*getLengthZ(), BASE_CAPACITY)) return Integer.MAX_VALUE;
		if (getLengthX() <= 0 || getLengthY() <= 0 || getLengthZ() <= 0) return BASE_CAPACITY;
		return BASE_CAPACITY*getLengthX()*getLengthY()*getLengthZ();
	}
	
	// Set Fuel and Power and Modify Heat
	
	private void run(boolean checkBlocks) {
		double energyThisTick = 0D;
		double fuelThisTick = 0D, heatThisTick = 0D, coolerHeatThisTick = 0D;
		int cellCount = 0;
		double energyMultThisTick = 0D, heatMultThisTick = 0D;
		
		double baseRF = baseProcessPower, baseHeat = baseProcessHeat;
		
		ready = readyToProcess() && !isActivated ? 1 : 0;

		if (checkBlocks) {
			boolean isProcessing = isProcessing();
			if (complete == 1) {
				for (int z = minZ + 1; z <= maxZ - 1; z++) for (int x = minX + 1; x <= maxX - 1; x++) for (int y = minY + 1; y <= maxY - 1; y++) {
					
					// Cells
					if (findCell(x, y, z)) {
						int extraCells = 0;
						for (EnumFacing side : EnumFacing.VALUES) {
							if (findCellOnSide(x, y, z, side) || findModeratorThenCellOnSide(x, y, z, side)) extraCells += 1;
						}
						
						cellCount++;
						energyMultThisTick += extraCells + 1;
						heatMultThisTick += (extraCells + 1)*(extraCells + 2)/2;
						if (readyToProcess()) {
							energyThisTick += baseRF*(extraCells + 1);
							heatThisTick += baseHeat*(extraCells + 1)*(extraCells + 2)/2;
						}
						
						if (isProcessing) fuelThisTick += NCConfig.fission_fuel_use;
					}
					
					// Moderators
					if (findModerator(x, y, z)) {
						if (readyToProcess()) {
							heatMultThisTick += cellCount/16D;
							heatThisTick += NCConfig.fission_heat_generation*baseRF*cellCount/16D;
						}
						if (cellAdjacent(x, y, z)) {
							energyMultThisTick += cellCount/8D;
							if (readyToProcess()) energyThisTick += NCConfig.fission_power*baseRF*cellCount/8D;
						}
					}
					
					// Passive Coolers
					for (int i = 1; i < CoolerType.values().length; i++) {
						if (findCooler(x, y, z, i)) if (coolerRequirements(x, y, z, i)) {
							coolerHeatThisTick -= CoolerType.values()[i].getCooling();
							break;
						}
					}
					
					// Active Coolers
					if (finder.find(x, y, z, NCBlocks.active_cooler)) {
						TileEntity tile = world.getTileEntity(finder.position(x, y, z));
						if (tile instanceof TileActiveCooler) {
							TileActiveCooler cooler = (TileActiveCooler) tile;
							Tank tank = cooler.getTanks().get(0);
							if (tank.getFluidAmount() > 0) {
								//double currentHeat = heat + (isProcessing ? heatThisTick : 0) + coolerHeatThisTick;
								boolean isInValidPosition = false;
								for (int i = 1; i < CoolerType.values().length; i++) {
									if (tank.getFluidName().equals(CoolerType.values()[i].getFluidName())) {
										if (coolerRequirements(x, y, z, i)) {
											coolerHeatThisTick -= NCConfig.fission_active_cooling_rate[i - 1]*NCConfig.active_cooler_max_rate/20;
											isInValidPosition = true;
											break;
										}
									}
								}
								cooler.isActive = isInValidPosition && isActivated() && readyToProcess();
							}
						}
					}
				}
			}
			
			if (complete == 1) {
				heatChange = heatThisTick + coolerHeatThisTick;
				cooling = coolerHeatThisTick;
				cells = cellCount;
				efficiency = cellCount == 0 ? 0D : 100D*energyMultThisTick/cellCount;
				heatMult = cellCount == 0 ? 0D : 100D*heatMultThisTick/cellCount;
				processPower = energyThisTick;
				speedMultiplier = fuelThisTick;
			} else {
				heatChange = cooling = 0D;
				efficiency = heatMult = 0D;
				cells = 0;
				processPower = speedMultiplier = 0D;
			}
		}
		
		if (isProcessing) {
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
	
	private void newRun(boolean checkBlocks) {
		double energyThisTick = 0D;
		double fuelThisTick = 0D, heatThisTick = 0D, coolerHeatThisTick = 0D;
		int cellCount = 0;
		double energyMultThisTick = 0D, heatMultThisTick = 0D;
		
		double baseRF = NCConfig.fission_power*baseProcessPower;
		double baseHeat = NCConfig.fission_heat_generation*baseProcessHeat;
		
		double moderatorPowerMultiplier = NCConfig.fission_moderator_extra_power/6D;
		double moderatorHeatMultiplier = NCConfig.fission_moderator_extra_heat/6D;
		
		ready = readyToProcess() && !isActivated ? 1 : 0;

		if (checkBlocks) {
			boolean isProcessing = isProcessing();
			if (complete == 1) {
				for (int z = minZ + 1; z <= maxZ - 1; z++) for (int x = minX + 1; x <= maxX - 1; x++) for (int y = minY + 1; y <= maxY - 1; y++) {
					
					// Cells
					if (findCell(x, y, z)) {
						int extraCells = 0;
						for (EnumFacing side : EnumFacing.VALUES) {
							if (findCellOnSide(x, y, z, side) || newFindModeratorThenCellOnSide(x, y, z, side)) extraCells += 1;
						}
						
						cellCount++;
						energyMultThisTick += extraCells + 1D;
						heatMultThisTick += (extraCells + 1D)*(extraCells + 2D)/2D;
						if (readyToProcess()) {
							energyThisTick += baseRF*(extraCells + 1D);
							heatThisTick += baseHeat*(extraCells + 1D)*(extraCells + 2D)/2D;
						}
						
						if (isProcessing) fuelThisTick += NCConfig.fission_fuel_use;
						
						// Adjacent Moderators
						int moderatorAdjacentCount = moderatorAdjacentCount(x, y, z);
						
						energyMultThisTick += moderatorPowerMultiplier*moderatorAdjacentCount*(extraCells + 1D);
						heatMultThisTick += moderatorHeatMultiplier*moderatorAdjacentCount*(extraCells + 1D);
						
						energyThisTick += baseRF*moderatorPowerMultiplier*moderatorAdjacentCount*(extraCells + 1D);
						heatThisTick += baseHeat*moderatorHeatMultiplier*moderatorAdjacentCount*(extraCells + 1D);
					}
					
					// Extra Moderators
					/*if (readyToProcess()) if (findModerator(x, y, z)) {
						if (!cellAdjacent(x, y, z)) heatThisTick += baseHeat;
					}*/
					
					// Passive Coolers
					for (int i = 1; i < CoolerType.values().length; i++) {
						if (findCooler(x, y, z, i)) if (coolerRequirements(x, y, z, i)) {
							coolerHeatThisTick -= CoolerType.values()[i].getCooling();
							break;
						}
					}	
					
					// Active Coolers
					if (finder.find(x, y, z, NCBlocks.active_cooler)) {
						TileEntity tile = world.getTileEntity(finder.position(x, y, z));
						if (tile instanceof TileActiveCooler) {
							TileActiveCooler cooler = (TileActiveCooler) tile;
							Tank tank = cooler.getTanks().get(0);
							if (tank.getFluidAmount() > 0) {
								//double currentHeat = heat + (isProcessing ? heatThisTick : 0) + coolerHeatThisTick;
								boolean isInValidPosition = false;
								for (int i = 1; i < CoolerType.values().length; i++) {
									if (tank.getFluidName().equals(CoolerType.values()[i].getFluidName())) {
										if (coolerRequirements(x, y, z, i)) {
											coolerHeatThisTick -= NCConfig.fission_active_cooling_rate[i - 1]*NCConfig.active_cooler_max_rate/20;
											isInValidPosition = true;
											break;
										}
									}
								}
								cooler.isActive = isInValidPosition && isActivated() && readyToProcess();
							}
						}
					}
				}
			}
			
			if (complete == 1) {
				heatChange = heatThisTick + coolerHeatThisTick;
				cooling = coolerHeatThisTick;
				cells = cellCount;
				efficiency = cellCount == 0 ? 0D : 100D*energyMultThisTick/cellCount;
				heatMult = cellCount == 0 ? 0D : 100D*heatMultThisTick/cellCount;
				processPower = energyThisTick;
				speedMultiplier = fuelThisTick;
			} else {
				heatChange = cooling = 0D;
				efficiency = heatMult = 0D;
				cells = 0;
				processPower = speedMultiplier = 0D;
			}
		}
		
		if (isProcessing) {
			if (heat + heatChange >= 0D) {
				heat += heatChange;
			} else {
				heat = 0D;
			}
		} else if (ready == 1 || complete == 1) {
			if (heat + cooling >= 0D) {
				heat += cooling;
			} else {
				heat = 0D;
			}
		}
	}
	
	public void stopActiveCooling() {
		for (int z = minZ + 1; z <= maxZ - 1; z++) for (int x = minX + 1; x <= maxX - 1; x++) for (int y = minY + 1; y <= maxY - 1; y++) {
			if (finder.find(x, y, z, NCBlocks.active_cooler)) {
				TileEntity tile = world.getTileEntity(finder.position(x, y, z));
				if (tile instanceof TileActiveCooler) {
					((TileActiveCooler) tile).isActive = false;
				}
			}
		}
	}
	
	public Object[] getOCReactorLayout() {
		if (ModCheck.openComputersLoaded() && complete == 1) {
			Object[] layout = new Object[getLengthX()*getLengthY()*getLengthZ()];
			for (int z = minZ + 1; z <= maxZ - 1; z++) for (int x = minX + 1; x <= maxX - 1; x++) for (int y = minY + 1; y <= maxY - 1; y++) {
				int arrayX = x - minX - 1; int arrayY = y - minY - 1; int arrayZ = z - minZ - 1;
				IBlockState layoutState = world.getBlockState(finder.position(x, y, z));
				String mainName = layoutState.getBlock().getRegistryName().toString();
				int meta = layoutState.getBlock().getMetaFromState(layoutState);
				layout[arrayX + getLengthX()*arrayY + getLengthX()*getLengthY()*arrayZ] = new Object[] {new Object[] {x, y, z}, new Object[] {mainName, meta}};
			}
			oc_layout = new Object[] {layout};
		}
		else oc_layout = new Object[] {};
		
		return oc_layout;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setDouble("processPower", processPower);
		nbt.setDouble("speedMultiplier", speedMultiplier);
		nbt.setDouble("baseProcessTime", baseProcessTime);
		nbt.setDouble("heat", heat);
		nbt.setDouble("cooling", cooling);
		nbt.setDouble("efficiency", efficiency);
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
		nbt.setDouble("heatChange", heatChange);
		nbt.setDouble("heatMult", heatMult);
		nbt.setInteger("complete", complete);
		nbt.setInteger("ready", ready);
		nbt.setString("problem", problem);
		nbt.setString("problemPos", problemPos);
		nbt.setInteger("problemPosX", problemPosX);
		nbt.setInteger("problemPosY", problemPosY);
		nbt.setInteger("problemPosZ", problemPosZ);
		nbt.setBoolean("newRules", newRules);
		nbt.setInteger("ports", ports);
		nbt.setInteger("currentEnergyStored", currentEnergyStored);
		nbt.setBoolean("isActivated", isActivated);
		nbt.setBoolean("computerActivated", computerActivated);
		nbt.setInteger("comparatorStrength", comparatorStrength);
		return nbt;
	}
			
	@Override
	public void readAll(NBTTagCompound nbt) {
		lengthX = nbt.getInteger("lengthX");
		lengthY = nbt.getInteger("lengthY");
		lengthZ = nbt.getInteger("lengthZ");
		getEnergyStorage().setStorageCapacity(getNewCapacity());
		super.readAll(nbt);
		processPower = nbt.getDouble("processPower");
		speedMultiplier = nbt.getDouble("speedMultiplier");
		baseProcessTime = nbt.getDouble("baseProcessTime");
		heat = nbt.getDouble("heat");
		cooling = nbt.getDouble("cooling");
		efficiency = nbt.getDouble("efficiency");
		cells = nbt.getInteger("cells");
		minX = nbt.getInteger("minX");
		minY = nbt.getInteger("minY");
		minZ = nbt.getInteger("minZ");
		maxX = nbt.getInteger("maxX");
		maxY = nbt.getInteger("maxY");
		maxZ = nbt.getInteger("maxZ");
		heatChange = nbt.getDouble("heatChange");
		heatMult = nbt.getDouble("heatMult");
		complete = nbt.getInteger("complete");
		ready = nbt.getInteger("ready");
		problem = nbt.getString("problem");
		problemPos = nbt.getString("problemPos");
		problemPosX = nbt.getInteger("problemPosX");
		problemPosY = nbt.getInteger("problemPosY");
		problemPosZ = nbt.getInteger("problemPosZ");
		newRules = nbt.getBoolean("newRules");
		ports = nbt.getInteger("ports");
		currentEnergyStored = nbt.getInteger("currentEnergyStored");
		isActivated = nbt.getBoolean("isActivated");
		computerActivated = nbt.getBoolean("computerActivated");
		comparatorStrength = nbt.getInteger("comparatorStrength");
	}
	
	// IGui
	
	@Override
	public int getGuiID() {
		return 100;
	}
	
	@Override
	public Set<EntityPlayer> getPlayersToUpdate() {
		return playersToUpdate;
	}
	
	@Override
	public FissionUpdatePacket getGuiUpdatePacket() {
		return new FissionUpdatePacket(pos, time, getEnergyStored(), baseProcessTime, baseProcessPower, heat, cooling, efficiency, cells, speedMultiplier, lengthX, lengthY, lengthZ, heatChange, complete, ready, problemPosX, problemPosY, problemPosZ, heatMult, hasConsumed, computerActivated);
	}
	
	@Override
	public void onGuiPacket(FissionUpdatePacket message) {
		time = message.time;
		getEnergyStorage().setEnergyStored(message.energyStored);
		baseProcessTime = message.baseProcessTime;
		baseProcessPower = message.baseProcessPower;
		heat = message.heat;
		cooling = message.cooling;
		efficiency = message.efficiency;
		cells = message.cells;
		speedMultiplier = message.speedMultiplier;
		lengthX = message.lengthX;
		lengthY = message.lengthY;
		lengthZ = message.lengthZ;
		heatChange = message.heatChange;
		complete = message.complete;
		ready = message.ready;
		problemPosX = message.problemPosX;
		problemPosY = message.problemPosY;
		problemPosZ = message.problemPosZ;
		heatMult = message.heatMult;
		hasConsumed = message.hasConsumed;
		computerActivated = message.computerActivated;
	}
	
	// Allow ports to open GUI
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return world.getTileEntity(pos) != this ? false : player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= Math.max(lengthX*lengthX + lengthY*lengthY + lengthZ*lengthZ, 64D);
	}
	
	// OpenComputers
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return Global.MOD_SHORT_ID + "_fission_reactor";
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] isComplete(Context context, Arguments args) {
		return new Object[] {complete == 1};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] isProcessing(Context context, Arguments args) {
		return new Object[] {isProcessing};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getProblem(Context context, Arguments args) {
		return new Object[] {problem};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getLengthX(Context context, Arguments args) {
		return new Object[] {getLengthX()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getLengthY(Context context, Arguments args) {
		return new Object[] {getLengthY()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getLengthZ(Context context, Arguments args) {
		return new Object[] {getLengthZ()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getEnergyStored(Context context, Arguments args) {
		return new Object[] {getEnergyStored()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getMaxEnergyStored(Context context, Arguments args) {
		return new Object[] {getMaxEnergyStored()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getEnergyChange(Context context, Arguments args) {
		return new Object[] {energyChange};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getCurrentProcessTime(Context context, Arguments args) {
		return new Object[] {time};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getHeatLevel(Context context, Arguments args) {
		return new Object[] {heat};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getMaxHeatLevel(Context context, Arguments args) {
		return new Object[] {getMaxHeat()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getEfficiency(Context context, Arguments args) {
		return new Object[] {efficiency};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getHeatMultiplier(Context context, Arguments args) {
		return new Object[] {heatMult};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getFissionFuelTime(Context context, Arguments args) {
		return new Object[] {recipeInfo != null ? baseProcessTime : 0D};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getFissionFuelPower(Context context, Arguments args) {
		return new Object[] {baseProcessPower};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getFissionFuelHeat(Context context, Arguments args) {
		return new Object[] {baseProcessHeat};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getFissionFuelName(Context context, Arguments args) {
		return new Object[] {getFuelName()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getReactorProcessTime(Context context, Arguments args) {
		return new Object[] {recipeInfo != null ? (cells == 0 ? baseProcessTime : baseProcessTime/cells) : 0D};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getReactorProcessPower(Context context, Arguments args) {
		return new Object[] {processPower};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getReactorProcessHeat(Context context, Arguments args) {
		return new Object[] {heatChange};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getReactorCoolingRate(Context context, Arguments args) {
		return new Object[] {cooling};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getNumberOfCells(Context context, Arguments args) {
		return new Object[] {cells};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getReactorLayout(Context context, Arguments args) {
		return getOCReactorLayout();
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] activate(Context context, Arguments args) {
		computerActivated = true;
		return new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] deactivate(Context context, Arguments args) {
		computerActivated = false;
		return new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] forceUpdate(Context context, Arguments args) {
		refreshMultiblock(true);
		return new Object[] {};
	}
}
