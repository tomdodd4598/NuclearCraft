package nc.multiblock.turbine.tile;

import java.util.ArrayList;
import java.util.List;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import nc.Global;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.Turbine;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileTurbineComputerPort extends TileTurbinePartBase implements SimpleComponent {
	
	public TileTurbineComputerPort() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(Turbine controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	// OpenComputers
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return Global.MOD_SHORT_ID + "_turbine";
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] isComplete(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] isTurbineOn(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().isTurbineOn : false};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getLengthX(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getInteriorLengthX() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getLengthY(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getInteriorLengthY() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getLengthZ(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getInteriorLengthZ() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] isProcessing(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().isProcessing : false};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getEnergyStored(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().energyStorage.getEnergyStored() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getEnergyCapacity(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().energyStorage.getMaxEnergyStored() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getPower(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().power : 0D};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getCoilConductivity(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getEffectiveConductivity() : 0D};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getFlowDirection(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() && getMultiblock().flowDir != null ? getMultiblock().flowDir.getName() : "null"};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getTotalExpansionLevel(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().totalExpansionLevel : 0D};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getIdealTotalExpansionLevel(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().idealTotalExpansionLevel : 0D};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getExpansionLevels(Context context, Arguments args) {
		return isMultiblockAssembled() ? getMultiblock().expansionLevels.toArray() : new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getIdealExpansionLevels(Context context, Arguments args) {
		return isMultiblockAssembled() ? getMultiblock().getIdealExpansionLevels().toArray() : new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getBladeEfficiencies(Context context, Arguments args) {
		return isMultiblockAssembled() ? getMultiblock().rawBladeEfficiencies.toArray() : new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getInputRate(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getActualInputRate() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getNumberOfDynamoCoils(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getDynamoCoils().size() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getDynamoCoilStats(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			List<Object[]> stats = new ArrayList<Object[]>();
			for (TileTurbineDynamoCoil dynamoCoil : getMultiblock().getDynamoCoils()) {
				stats.add(new Object[] {
						new Object[] {dynamoCoil.getPos().getX(), dynamoCoil.getPos().getY(), dynamoCoil.getPos().getZ()},
						dynamoCoil.coilType.getName(),
						dynamoCoil.isInValidPosition
						});
			}
			return new Object[] {stats.toArray()};
		}
		else return new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] activate(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			getMultiblock().computerActivated = true;
			getMultiblock().setIsTurbineOn();
		}
		return new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] deactivate(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			getMultiblock().computerActivated = false;
			getMultiblock().setIsTurbineOn();
		}
		return new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] clearAllFluids(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			getMultiblock().clearAllFluids();
		}
		return new Object[] {};
	}
}
