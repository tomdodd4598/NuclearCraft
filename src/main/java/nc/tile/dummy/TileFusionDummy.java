package nc.tile.dummy;

import li.cil.oc.api.network.SimpleComponent;
import nc.Global;
import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.recipe.NCRecipes;
import nc.tile.energyFluid.IBufferable;
import nc.tile.generator.TileFusionCore;
import nc.util.BlockFinder;
import nc.util.BlockPosHelper;
import nc.util.RecipeHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public abstract class TileFusionDummy extends TileDummy<TileFusionCore> implements IBufferable, SimpleComponent {
	
	public static class Side extends TileFusionDummy {
		public Side() {
			super("fusion_dummy_side");
		}
		
		@Override
		public void findMaster() {
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
		public void findMaster() {
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
		super(TileFusionCore.class, name, NCConfig.machine_update_rate, RecipeHelper.validFluids(NCRecipes.Type.FUSION).get(0));
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
		if (findAdjacentComparator() && shouldTileCheck()) markDirty();
	}
	
	// Redstone Flux

	@Override
	public boolean canReceiveEnergy(EnumFacing side) {
		if (hasMaster()) return !getMaster().isHotEnough();
		return false;
	}
	
	@Override
	public boolean canExtractEnergy(EnumFacing side) {
		if (hasMaster()) return getMaster().isHotEnough();
		return false;
	}
	
	// Finding Blocks
	
	protected boolean findCore(BlockPos pos) {
		return finder.find(pos, NCBlocks.fusion_core);
	}
	
	public boolean findAdjacentComparator() {
		return finder.horizontalYCount(pos, 1, Blocks.UNPOWERED_COMPARATOR, Blocks.POWERED_COMPARATOR) > 0;
	}
	
	// OpenComputers
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return Global.MOD_SHORT_ID + "_fusion_reactor_dummy";
	}
	
	/*@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] isComplete(Context context, Arguments args) {
		return new Object[] {hasMaster() ? getMaster().complete == 1 : false};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] isHotEnough(Context context, Arguments args) {
		return new Object[] {hasMaster() ? getMaster().isHotEnough() : false};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getProblem(Context context, Arguments args) {
		return new Object[] {hasMaster() ? getMaster().problem : TileFusionCore.INCORRECT_STRUCTURE};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getToroidSize(Context context, Arguments args) {
		return new Object[] {hasMaster() ? getMaster().size : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getEnergyStored(Context context, Arguments args) {
		return new Object[] {hasMaster() ? getMaster().getEnergyStored() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getMaxEnergyStored(Context context, Arguments args) {
		return new Object[] {hasMaster() ? getMaster().getMaxEnergyStored() : 1};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getTemperature(Context context, Arguments args) {
		return new Object[] {hasMaster() ? getMaster().heat*1000 : TileFusionCore.ROOM_TEMP*1000};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getEfficiency(Context context, Arguments args) {
		return new Object[] {hasMaster() ? getMaster().efficiency : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getFusionComboTime(Context context, Arguments args) {
		return new Object[] {hasMaster() ? getMaster().baseProcessTime : 1};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getFusionComboPower(Context context, Arguments args) {
		return new Object[] {hasMaster() ? getMaster().baseProcessPower : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getFusionComboHeatVariable(Context context, Arguments args) {
		return new Object[] {hasMaster() ? getMaster().processHeatVariable : 1};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getReactorProcessTime(Context context, Arguments args) {
		return new Object[] {hasMaster() ? (getMaster().size == 0 ? getMaster().baseProcessTime : getMaster().baseProcessTime/getMaster().size) : 1};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getReactorProcessPower(Context context, Arguments args) {
		return new Object[] {hasMaster() ? getMaster().processPower : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getReactorProcessHeat(Context context, Arguments args) {
		return new Object[] {hasMaster() ? getMaster().heatChange : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getReactorCoolingRate(Context context, Arguments args) {
		return new Object[] {hasMaster() ? getMaster().cooling : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] activate(Context context, Arguments args) {
		if (hasMaster()) getMaster().computerActivated = true;
		return new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] deactivate(Context context, Arguments args) {
		if (hasMaster()) getMaster().computerActivated = false;
		return new Object[] {};
	}*/
}
