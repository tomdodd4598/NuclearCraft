package nc.tile.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.config.NCConfig;
import nc.enumm.MetaEnums.CoolerType;
import nc.handler.SoundHandler;
import nc.init.NCBlocks;
import nc.recipe.NCRecipes;
import nc.recipe.RecipeMethods;
import nc.tile.fluid.TileActiveCooler;
import nc.tile.internal.Tank;
import nc.tile.internal.EnumEnergyStorage.EnergyConnection;
import nc.util.BlockFinder;
import nc.util.BlockPosHelper;
import nc.util.Lang;
import nc.util.MaterialHelper;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidRegistry;

/*@Optional.InterfaceList({
	@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers"),
	@Optional.Interface(iface = "li.cil.oc.api.network.ManagedPeripheral", modid = "opencomputers"),
	@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = "computercraft")
})*/
public class TileFusionCore extends TileFluidGenerator /*implements SimpleComponent, ManagedPeripheral, IPeripheral*/ {
	
	private static final double ROOM_TEMP = 0.298D;
	
	public double rateMultiplier, processTime, processPower;
	public double heat = ROOM_TEMP, efficiency, cooling, heatChange; // cooling and heatChange are in K, not kK
	
	public int soundCount, coolingTickCount;
	public int size = 1;
	
	public int complete;
	public String problem = Lang.localise("gui.container.fusion_core.ring_incomplete");
	
	private BlockFinder finder;
	
	public TileFusionCore() {
		super("Fusion Core", 2, 4, 0, tankCapacities(32000, 2, 4), fluidConnections(2, 4), RecipeMethods.validFluids(NCRecipes.Type.FUSION), 8192000, NCRecipes.Type.FUSION);
		areTanksShared = false;
	}
	
	@Override
	public void onAdded() {
		finder = new BlockFinder(pos, world);
		super.onAdded();
	}
	
	@Override
	public void updateGenerator() {
		boolean wasGenerating = isGenerating;
		isGenerating = canProcess() && !isPowered();
		boolean shouldUpdate = false;
		if(!world.isRemote) if (time == 0) consume();
		tick();
		setSize();
		if(!world.isRemote) {
			if (shouldCheckCooling() && NCConfig.fusion_active_cooling) setCooling();
			double previousHeat = heat;
			run();
			heating();
			cooling();
			heatChange = 1000*(heat - previousHeat);
			plasma();
			if (overheat()) return;
			if (isGenerating) process();
			if (wasGenerating != isGenerating) {
				shouldUpdate = true;
				updateBlockType();
			}
			if (isHotEnough()) pushEnergy();
			if (findAdjacentComparator() && shouldCheck()) shouldUpdate = true;
		} else {
			isGenerating = complete == 1 && !isPowered() && time > 0 && isHotEnough();
			playSounds();
		}
		if (shouldUpdate) markDirty();
	}
	
	public boolean overheat() {
		if (heat >= getMaxHeat() && NCConfig.fusion_overheat) {
			meltdown();
			return true;
		}
		return false;
	}
	
	public void meltdown() {
		world.removeTileEntity(pos);
		world.destroyBlock(pos, false);
		world.setBlockState(pos, Blocks.LAVA.getDefaultState());
		
		BlockPosHelper helper = new BlockPosHelper(pos);
		for (BlockPos pos : helper.squareRing(ringRadius(), 1)) world.setBlockState(pos, Blocks.LAVA.getStateFromMeta(1));
		for (BlockPos pos : helper.squareTube(ringRadius(), 1)) {
			world.removeTileEntity(pos);
			world.setBlockToAir(pos);
		}
	}
	
	@Override
	public void tick() {
		if (tickCount > NCConfig.fusion_update_rate) tickCount = 0; else tickCount++;
		if (coolingTickCount > NCConfig.fission_update_rate*2) coolingTickCount = 0; else coolingTickCount++;
	}
	
	@Override
	public boolean shouldCheck() {
		return tickCount > NCConfig.fusion_update_rate;
	}
	
	public boolean shouldCheckCooling() {
		return coolingTickCount > NCConfig.fission_update_rate*2;
	}
	
	@Override
	public boolean isGenerating() {
		return complete == 1 && !isPowered() && time > 0 && isHotEnough();
	}
	
	@Override
	public boolean canProcess() {
		return canProcessStacks() && complete == 1 && isHotEnough();
	}
	
	@Override
	public boolean isPowered() {
		BlockPosHelper helper = new BlockPosHelper(pos);
		for (BlockPos pos : helper.squareRing(1, 0)) if (world.isBlockPowered(pos)) return true;
		return world.isBlockPowered(pos);
	}
	
	public boolean findAdjacentComparator() {
		BlockPosHelper helper = new BlockPosHelper(pos);
		for (BlockPos pos : helper.cutoffRing(2, 0)) if (finder.find(pos, Blocks.UNPOWERED_COMPARATOR, Blocks.POWERED_COMPARATOR)) return true;
		return false;
	}
	
	public void playSounds() {
		if (soundCount >= getSoundTime()) {
			if ((isGenerating() || canProcess()) && !isPowered()) {
				if (ringRadius() > 1) playFusionSound(0, 1, 0);
				playFusionSound(ringRadius(), 1, ringRadius());
				playFusionSound(ringRadius(), 1, -ringRadius());
				playFusionSound(-ringRadius(), 1, ringRadius());
				playFusionSound(-ringRadius(), 1, -ringRadius());
				if (ringRadius() > 4) {
					playFusionSound(ringRadius(), 1, 0);
					playFusionSound(-ringRadius(), 1, 0);
					playFusionSound(0, 1, ringRadius());
					playFusionSound(0, 1, -ringRadius());
				}
			}
			soundCount = 0;
		} else soundCount ++;
	}
	
	public void playFusionSound(int x, int y, int z) {
		world.playSound(pos.getX() + x, pos.getY() + y, pos.getZ() + z, getSound(), SoundCategory.BLOCKS, 1F, 1.0F, false);
	}
	
	private int getSoundTime() {
		return !NCConfig.fusion_alternate_sound ? SoundHandler.FUSION_RUN_TIME : SoundHandler.ACCELERATOR_RUN_TIME;
	}
	
	private SoundEvent getSound() {
		return !NCConfig.fusion_alternate_sound ? SoundHandler.fusion_run : SoundHandler.accelerator_run;
	}
	
	@Override
	public boolean canExtract() {
		return isHotEnough();
	}

	@Override
	public boolean canReceive() {
		return !isHotEnough();
	}
	
	public boolean isHotEnough() {
		return heat > 8000;
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
		return (int) Math.max(1, rateMultiplier);
	}

	@Override
	public void setRateMultiplier(int value) {
		rateMultiplier = Math.max(1, value);
	}

	@Override
	public int getProcessTime() {
		return (int) Math.max(1, processTime);
	}

	@Override
	public void setProcessTime(int value) {
		processTime = Math.max(1, value);
	}

	@Override
	public int getProcessPower() {
		return (int) processPower;
	}

	@Override
	public void setProcessPower(int value) {
		processPower = value;
	}
	
	public double getMaxHeat() {
		return 20000000D;
	}
	
	public int ringRadius() {
		return size + 2;
	}
	
	public ArrayList getComboStats() {
		return getRecipe(hasConsumed) != null ? getRecipe(hasConsumed).extras() : null;
	}
	
	public double getComboTime() {
		if (getComboStats() != null) if (getComboStats().get(0) instanceof Double) {
			return (double) getComboStats().get(0)*NCConfig.fusion_fuel_use;
		}
		return NCConfig.fusion_fuel_use;
	}
	
	public double getComboPower() {
		if (getComboStats() != null) if (getComboStats().get(1) instanceof Double) {
			return (double) getComboStats().get(1);
		}
		return 0;
	}
	
	public double getComboHeatVariable() {
		if (getComboStats() != null) if (getComboStats().get(2) instanceof Double) {
			return (double) getComboStats().get(2);
		}
		return 1000;
	}
	
	// Setting Blocks
	
	private void setPlasma(BlockPos pos) {
		world.setBlockState(pos, FluidRegistry.getFluid("plasma").getBlock().getDefaultState());
	}
	
	public void plasma() {
		BlockPosHelper helper = new BlockPosHelper(pos);
		if (isGenerating() || canProcess()) {
			for (BlockPos pos : helper.squareRing(ringRadius(), 1)) if (!findPlasma(pos)) setPlasma(pos);
		}
		else if ((!isGenerating() || !canProcess()) && complete == 1) {
			for (BlockPos pos : helper.squareRing(ringRadius(), 1)) if (findPlasma(pos)) world.setBlockToAir(pos);
		}
	}
	
	// Finding Blocks
	
	private boolean findIdleElectromagnet(BlockPos pos) {
		return finder.find(pos, NCBlocks.fusion_electromagnet_idle, NCBlocks.fusion_electromagnet_transparent_idle);
	}
	
	private boolean findActiveElectromagnet(BlockPos pos) {
		return finder.find(pos, NCBlocks.fusion_electromagnet_active, NCBlocks.fusion_electromagnet_transparent_active);
	}
	
	private boolean findElectromagnet(BlockPos pos) {
		return finder.find(pos, NCBlocks.fusion_electromagnet_active, NCBlocks.fusion_electromagnet_transparent_active, NCBlocks.fusion_electromagnet_idle, NCBlocks.fusion_electromagnet_transparent_idle);
	}
	
	private boolean findAir(BlockPos pos) {
		Material mat = finder.getBlockState(pos).getMaterial();
		return MaterialHelper.isReplaceable(mat) || findPlasma(pos);
	}
	
	private boolean findPlasma(BlockPos pos) {
		return finder.find(pos, FluidRegistry.getFluid("plasma").getBlock().getDefaultState());
	}
	
	private boolean findActiveCooler(BlockPos pos) {
		if (world.getTileEntity(pos) == null) return false;
		return world.getTileEntity(pos) instanceof TileActiveCooler;
	}
	
	// Finding Ring
	
	public boolean setSize() {
		if (shouldCheck()) {
			int runningSize = 1;
			for (int r = 0; r <= NCConfig.fusion_max_size; r++) {
				if (finder.horizontalY(pos.offset(EnumFacing.UP), r + 2, NCBlocks.fusion_connector)) {
					runningSize ++;
				} else break;
			}
			size = runningSize;
			BlockPosHelper helper = new BlockPosHelper(pos);
			for (BlockPos pos : helper.squareTube(ringRadius(), 1)) {
				if (!(findElectromagnet(pos))) {
					complete = 0;
					problem = Lang.localise("gui.container.fusion_core.ring_incomplete");
					return false;
				}
			}
			for (BlockPos pos : helper.squareRing(ringRadius(), 1)) {
				if (!(findAir(pos))) {
					complete = 0;
					Lang.localise("gui.container.fusion_core.ring_blocked");
					return false;
				}
			}
			for (BlockPos pos : helper.squareTube(ringRadius(), 1)) {
				if (!(findActiveElectromagnet(pos))) {
					complete = 0;
					problem = Lang.localise("gui.container.fusion_core.power_issue");
					return false;
				}
			}
			complete = 1;
			problem = Lang.localise("gui.container.fusion_core.incorrect_structure");
			return true;
		} else {
			return complete == 1;
		}
	}
	
	// Set Fuel and Power and Modify Heat
	
	public void run() {
		processTime = (int) getComboTime();
		efficiency = efficiency();
		double heatChange = 0;
		if (canProcess() && !isPowered()) {
			heatChange = NCConfig.fusion_heat_generation*(100D - (0.9*efficiency))/2D;
			setProcessPower((int) (MathHelper.clamp(efficiency, 0D, 100D)*NCConfig.fusion_base_power*size*getComboPower()));
			setRateMultiplier(size);
		} else {
			heatChange = 0;
			setProcessPower(0);
			setRateMultiplier(0);
			
			if (heat >= 1.005D*ROOM_TEMP) {
				heat = heat-((heat/100000D)*Math.log10(1000*(heat-ROOM_TEMP)));
			}
		}
		
		if (heat + heatChange >= ROOM_TEMP) {
			heat += heatChange;
		} else {
			heat = ROOM_TEMP;
		}
	}
	
	public double efficiency() {
		if (!canProcess()) return 0;
		else if (isHotEnough()) {
			double heatMK = heat/1000D;
			double z = 7.415D*(Math.exp(-heatMK/getComboHeatVariable())+Math.tanh(heatMK/getComboHeatVariable())-1);
			return 100*Math.pow(z, 2);
	   	} else return 0;
	}
	
	public void heating() {
		if (!canProcess()) {
			double r = 0.0001D*storage.getEnergyStored()/((double) ringRadius());
			storage.changeEnergyStored(-storage.getEnergyStored());
			heat = heat + r*NCConfig.fusion_heat_generation;
			setConnection(EnergyConnection.IN);
			if (heat < ROOM_TEMP) heat = ROOM_TEMP;
		}
		else setConnection(EnergyConnection.OUT);
	}
	
	public BlockPos getOpposite(BlockPos pos) {
		BlockPos relativePos = new BlockPos(pos.getX() - this.pos.getX(), pos.getY() - this.pos.getY(), pos.getZ() - this.pos.getZ());
		return finder.position(-relativePos.getX(), -relativePos.getY() + 2, -relativePos.getZ());
	}
	
	public void setCooling() {
		if (complete == 1) {
			List<BlockPos> posList = new ArrayList<BlockPos>();
			BlockPosHelper helper = new BlockPosHelper(pos);
			for (BlockPos pos : helper.squareTubeDiagonals(ringRadius(), 1)) {
				if (findActiveCooler(pos)) if (((TileActiveCooler) world.getTileEntity(pos)).tanks[0].getFluidAmount() > 0) posList.add(pos);
			}
			if (posList.isEmpty()) {
				cooling = 0D;
				return;
			}
			double cooled = 0;
			for (BlockPos pos : posList) {
				Tank tank = ((TileActiveCooler) world.getTileEntity(pos)).getTanks()[0];
				int fluidAmount = tank.getFluidAmount();
				double currentHeat = heat;
				if (currentHeat > ROOM_TEMP) {
					double cool_mult = posList.contains(getOpposite(pos)) ? NCConfig.fusion_heat_generation : 0.25D*NCConfig.fusion_heat_generation;
					for (int i = 1; i < CoolerType.values().length; i++) if (tank.getFluidName() == CoolerType.values()[i].getFluidName()) {
						cooled += (NCConfig.fission_active_cooling_rate[i - 1]*fluidAmount*cool_mult*2D*NCConfig.fission_update_rate)/(size*5000D);
						break;
					}
					currentHeat -= cooled;
					if (currentHeat > ROOM_TEMP) ((TileActiveCooler) world.getTileEntity(pos)).getTanks()[0].drain(fluidAmount, true);
				}
			}
			cooling = 1000D*cooled/(2D*NCConfig.fission_update_rate);
		}
	}
	
	public void cooling() {
		double coolingkK = cooling/1000;
		if (heat - coolingkK < ROOM_TEMP) heat = ROOM_TEMP;
		else heat -= coolingkK;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setDouble("processTime", processTime);
		nbt.setDouble("processPower", processPower);
		nbt.setDouble("rateMultiplier", rateMultiplier);
		nbt.setDouble("heat", heat);
		nbt.setDouble("cooling", cooling);
		nbt.setDouble("heatChange", heatChange);
		nbt.setDouble("efficiency", efficiency);
		nbt.setInteger("size", size);
		nbt.setInteger("complete", complete);
		nbt.setString("problem", problem);
		return nbt;
	}
			
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		processTime = nbt.getDouble("processTime");
		processPower = nbt.getDouble("processPower");
		rateMultiplier = nbt.getDouble("rateMultiplier");
		heat = nbt.getDouble("heat");
		cooling = nbt.getDouble("cooling");
		heatChange = nbt.getDouble("heatChange");
		efficiency = nbt.getDouble("efficiency");
		size = nbt.getInteger("size");
		complete = nbt.getInteger("complete");
		problem = nbt.getString("problem");
	}
	
	// Inventory Fields
	
	@Override
	public int getFieldCount() {
		return 11;
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
			return (int) heat;
		case 5:
			return (int) efficiency;
		case 6:
			return getRateMultiplier();
		case 7:
			return size;
		case 8:
			return complete;
		case 9:
			return (int) cooling;
		case 10:
			return (int) heatChange;
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
			efficiency = value;
			break;
		case 6:
			setRateMultiplier(value);
			break;
		case 7:
			size = value;
			break;
		case 8:
			complete = value;
			break;
		case 9:
			cooling = value;
			break;
		case 10:
			heatChange = value;
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
		if(method < 0 || method >= NUMBER_OF_METHODS) throw new IllegalArgumentException(Lang.localise("gui.computer.invalid_method_number"));
		if(method == 0) return new Object[] { complete == 1 };
		if(complete == 0) throw new Exception(Lang.localise("gui.container.fusion_core.reactor_not_found"));
		
		ComputerMethod computerMethod = ComputerMethod.values()[method];
		
		switch(computerMethod) {
		case isComplete:
			return new Object[] { complete == 1 };
		case isHotEnough:
			return new Object[] { isHotEnough() };
		case getProblem:
			return new Object[] { problem };
		case getSize:
			return new Object[] { size };
		case getEnergyStored:
			return new Object[] { storage.getEnergyStored() };
		case getHeatLevel:
			return new Object[] { heat };
		case getHeatChange:
			return new Object[] { heatChange/1000 };
		case getEfficiency:
			return new Object[] { efficiency };
		case getFuelLevels:
			return new Object[] { getTanks()[0].getFluidAmount(), getTanks()[1].getFluidAmount() };
		case getOutputLevels:
			return new Object[] { getTanks()[2].getFluidAmount(), getTanks()[3].getFluidAmount(), getTanks()[4].getFluidAmount(), getTanks()[5].getFluidAmount() };
		case getFuelTypes:
			return new Object[] { getTanks()[0].getFluidName(), getTanks()[1].getFluidName() };
		case getOutputTypes:
			return new Object[] { getTanks()[2].getFluidName(), getTanks()[3].getFluidName(), getTanks()[4].getFluidName(), getTanks()[5].getFluidName() };
		case getComboProcessTime:
			return new Object[] { getComboTime() };
		case getProcessTime:
			return new Object[] { getProcessTime() };
		case getComboPower:
			return new Object[] { getComboPower() };
		case getPower:
			return new Object[] { getProcessPower() };
		case getActiveCooling:
			return new Object[] { cooling/1000 };
		case doVentFuel: {
			if(arguments.length != 1) throw new IllegalArgumentException(Lang.localise("gui.computer.number_of_arguments_error", arguments.length, 1));
			if(!(arguments[0] instanceof Integer)) throw new IllegalArgumentException(Lang.localise("gui.computer.invalid_argument_error", 0, "Number"));
			int tankNo = (int) arguments[0];
			if(tankNo < 0) throw new IllegalArgumentException(Lang.localise("gui.computer.integer_too_small_error", 0, 0));
			if(tankNo > 1) throw new IllegalArgumentException(Lang.localise("gui.computer.integer_too_large_error", 0, 1));
			getTanks()[tankNo].setFluidStored(null);
			getTanks()[tankNo + 6].setFluidStored(null);
			return null;
		}
		case doVentAllFuels: {
			for (int i = 0; i < 2; ++i) {
				getTanks()[i].setFluidStored(null);
				getTanks()[i + 6].setFluidStored(null);
			}
			return null;
		}
		case doVentOutput: {
			if(arguments.length != 1) throw new IllegalArgumentException(Lang.localise("gui.computer.number_of_arguments_error", arguments.length, 1));
			if(!(arguments[0] instanceof Integer)) throw new IllegalArgumentException(Lang.localise("gui.computer.invalid_argument_error", 0, "Number"));
			int tankNo = (int) arguments[0];
			if(tankNo < 0) throw new IllegalArgumentException(Lang.localise("gui.computer.integer_too_small_error", 0, 0));
			if(tankNo > 3) throw new IllegalArgumentException(Lang.localise("gui.computer.integer_too_large_error", 0, 3));
			getTanks()[tankNo + 2].setFluidStored(null);
			return null;
		}
		case doVentAllOutputs: {
			for (int i = 2; i < 6; ++i) getTanks()[i].setFluidStored(null);
			return null;
		}
		default: throw new Exception(Lang.localise("gui.computer.method_not_found"));
		}
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
		return Global.MOD_SHORT_ID + "_fusion_reactor";
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
		return Global.MOD_SHORT_ID + "_fusion_reactor";
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
