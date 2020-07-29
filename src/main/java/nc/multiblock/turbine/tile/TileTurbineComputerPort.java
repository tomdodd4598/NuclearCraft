package nc.multiblock.turbine.tile;

import java.util.*;

import li.cil.oc.api.machine.*;
import li.cil.oc.api.network.SimpleComponent;
import nc.Global;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.turbine.Turbine;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileTurbineComputerPort extends TileTurbinePart implements SimpleComponent {
	
	public TileTurbineComputerPort() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(Turbine controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		// if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		// if (getWorld().isRemote) return;
		// getWorld().setBlockState(getPos(),
		// getWorld().getBlockState(getPos()), 2);
	}
	
	// OpenComputers
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return "nc_turbine";
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
		return new Object[] {isMultiblockAssembled() ? getMultiblock().conductivity : 0D};
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
		return isMultiblockAssembled() ? getLogic().getIdealExpansionLevels().toArray() : new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getBladeEfficiencies(Context context, Arguments args) {
		return isMultiblockAssembled() ? getMultiblock().rawBladeEfficiencies.toArray() : new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getInputRate(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().recipeInputRate : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getNumberOfDynamoParts(Context context, Arguments args) {
		return new Object[] { isMultiblockAssembled() ? getMultiblock().getPartMap(TileTurbineDynamoPart.class).size() : 0 };
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getDynamoPartStats(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			List<Object[]> stats = new ArrayList<>();
			for (TileTurbineDynamoPart dynamoPart : getMultiblock().getPartMap(TileTurbineDynamoPart.class).values()) {
				stats.add(new Object[] { new Object[] { dynamoPart.getPos().getX(), dynamoPart.getPos().getY(), dynamoPart.getPos().getZ() }, dynamoPart.partName, dynamoPart.isInValidPosition });
			}
			return new Object[] {stats.toArray()};
		}
		else {
			return new Object[] {};
		}
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] activate(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			getMultiblock().computerActivated = true;
			getLogic().setIsTurbineOn();
		}
		return new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] deactivate(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			getMultiblock().computerActivated = false;
			getLogic().setIsTurbineOn();
		}
		return new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] clearAllMaterial(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			getMultiblock().clearAllMaterial();
		}
		return new Object[] {};
	}
}
