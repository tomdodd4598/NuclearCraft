package nc.tile.dummy;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import nc.ModCheck;
import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.recipe.NCRecipes;
import nc.recipe.RecipeMethods;
import nc.tile.energyFluid.IBufferable;
import nc.tile.generator.TileFusionCore;
import nc.util.BlockFinder;
import nc.util.BlockPosHelper;
import nc.util.Lang;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

/*@Optional.InterfaceList({
	@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers"),
	@Optional.Interface(iface = "li.cil.oc.api.network.ManagedPeripheral", modid = "opencomputers"),
	@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = "computercraft")
})*/
public abstract class TileFusionDummy extends TileDummy implements IBufferable/*, SimpleComponent, ManagedPeripheral, IPeripheral*/ {
	
	public static class Side extends TileFusionDummy {
		public Side() {
			super("fusion_dummy_side");
		}
		
		@Override
		protected void findMaster() {
			BlockPosHelper helper = new BlockPosHelper(pos);
			for (BlockPos pos : helper.cuboid(-1, -1, -1, 1, 0, 1)) if (findCore(pos)) {
				masterPosition = pos;
				return;
			}
			masterPosition = null;
		}
	}
	
	public static class Top extends TileFusionDummy {
		public Top() {
			super("fusion_dummy_top");
		}
		
		@Override
		protected void findMaster() {
			BlockPosHelper helper = new BlockPosHelper(pos);
			for (BlockPos pos : helper.cuboid(-1, -2, -1, 1, -2, 1)) if (findCore(pos)) {
				masterPosition = pos;
				return;
			}
			masterPosition = null;
		}
	}
	
	private BlockFinder finder;
	
	public TileFusionDummy(String name) {
		super(name, NCConfig.fusion_update_rate, RecipeMethods.validFluids(NCRecipes.FUSION_RECIPES));
	}
	
	@Override
	public void onAdded() {
		finder = new BlockFinder(pos, world);
		super.onAdded();
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			pushEnergy();
			pushFluid();
		}
		if (findAdjacentComparator() && shouldUpdate()) markDirty();
	}
	
	// Redstone Flux
	
	@Override
	public boolean canExtract() {
		if (getMaster() != null) {
			if (isMaster(masterPosition)) return ((TileFusionCore) getMaster()).isHotEnough();
		}
		return false;
	}

	@Override
	public boolean canReceive() {
		if (getMaster() != null) {
			if (isMaster(masterPosition)) return !((TileFusionCore) getMaster()).isHotEnough();
		}
		return false;
	}
	
	// Finding Blocks
	
	protected boolean findCore(BlockPos pos) {
		return finder.find(pos, NCBlocks.fusion_core);
	}
	
	public boolean findAdjacentComparator() {
		return finder.horizontalYCount(pos, 1, Blocks.UNPOWERED_COMPARATOR, Blocks.POWERED_COMPARATOR) > 0;
	}
	
	// Find Master
	
	@Override
	public boolean isMaster(BlockPos pos) {
		return world.getTileEntity(pos) instanceof TileFusionCore;
	}
	
	// Capability
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (CapabilityEnergy.ENERGY == capability && energyConnection.canConnect()) {
			return (T) getStorage();
		}
		if (energyConnection != null && ModCheck.teslaLoaded() && energyConnection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && energyConnection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && energyConnection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return (T) getStorage();
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
		}
		return super.getCapability(capability, facing);
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
		if (getMaster() == null) throw new IllegalArgumentException(Lang.localise("gui.container.fusion_core.reactor_not_found"));
		if (!isMaster(masterPosition)) throw new IllegalArgumentException(Lang.localise("gui.container.fusion_core.reactor_not_found"));
		TileFusionCore core = (TileFusionCore) getMaster();
		if(method < 0 || method >= NUMBER_OF_METHODS) throw new IllegalArgumentException(Lang.localise("gui.computer.invalid_method_number"));
		if(method == 0) return new Object[] { core.complete == 1 };
		if(core.complete == 0) throw new Exception(Lang.localise("gui.container.fusion_core.reactor_not_found"));
		
		ComputerMethod computerMethod = ComputerMethod.values()[method];
		
		switch(computerMethod) {
		case isComplete:
			return new Object[] { core.complete == 1 };
		case isHotEnough:
			return new Object[] { core.isHotEnough() };
		case getProblem:
			return new Object[] { core.problem };
		case getSize:
			return new Object[] { core.size };
		case getEnergyStored:
			return new Object[] { storage.getEnergyStored() };
		case getHeatLevel:
			return new Object[] { core.heat };
		case getHeatChange:
			return new Object[] { core.heatChange/1000 };
		case getEfficiency:
			return new Object[] { core.efficiency };
		case getFuelLevels:
			return new Object[] { getTanks()[0].getFluidAmount(), getTanks()[1].getFluidAmount() };
		case getOutputLevels:
			return new Object[] { getTanks()[2].getFluidAmount(), getTanks()[3].getFluidAmount(), getTanks()[4].getFluidAmount(), getTanks()[5].getFluidAmount() };
		case getFuelTypes:
			return new Object[] { getTanks()[0].getFluidName(), getTanks()[1].getFluidName() };
		case getOutputTypes:
			return new Object[] { getTanks()[2].getFluidName(), getTanks()[3].getFluidName(), getTanks()[4].getFluidName(), getTanks()[5].getFluidName() };
		case getComboProcessTime:
			return new Object[] { core.getComboTime() };
		case getProcessTime:
			return new Object[] { core.getProcessTime() };
		case getComboPower:
			return new Object[] { core.getComboPower() };
		case getPower:
			return new Object[] { core.getProcessPower() };
		case getActiveCooling:
			return new Object[] { core.cooling/1000 };
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
