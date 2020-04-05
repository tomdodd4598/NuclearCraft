package nc.multiblock.heatExchanger.tile;

import java.util.ArrayList;
import java.util.List;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import nc.Global;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.heatExchanger.HeatExchanger;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileHeatExchangerComputerPort extends TileHeatExchangerPart implements SimpleComponent {
	
	public TileHeatExchangerComputerPort() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(HeatExchanger controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		//if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		//if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	// OpenComputers
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return Global.MOD_SHORT_ID + "_heat_exchanger";
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] isComplete(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] isHeatExchangerOn(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().isHeatExchangerOn : false};
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
	public Object[] getFractionOfTubesActive(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().fractionOfTubesActive : 0D};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getMeanEfficiency(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().efficiency : 0D};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getNumberOfExchangerTubes(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getTubes().size() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getNumberOfCondensationTubes(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getCondenserTubes().size() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getExchangerTubeStats(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			List<Object[]> stats = new ArrayList<>();
			for (TileHeatExchangerTube tube : getMultiblock().getTubes()) {
				stats.add(new Object[] {
						new Object[] {tube.getPos().getX(), tube.getPos().getY(), tube.getPos().getZ()},
						tube.conductivity,
						tube.isProcessing,
						tube.time,
						tube.recipeInfo != null ? tube.baseProcessTime/tube.getSpeedMultiplier() : 0D,
						tube.getSpeedMultiplier(),
						tube.inputTemperature,
						tube.outputTemperature,
						tube.flowDir == null ? "null" : tube.flowDir.getName()
						});
			}
			return new Object[] {stats.toArray()};
		}
		else return new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getCondensationTubeStats(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			List<Object[]> stats = new ArrayList<>();
			for (TileCondenserTube condenserTube : getMultiblock().getCondenserTubes()) {
				stats.add(new Object[] {
						new Object[] {condenserTube.getPos().getX(), condenserTube.getPos().getY(), condenserTube.getPos().getZ()},
						condenserTube.conductivity,
						condenserTube.isProcessing,
						condenserTube.time,
						condenserTube.recipeInfo != null ? condenserTube.baseProcessTime/condenserTube.getSpeedMultiplier() : 0D,
						condenserTube.getSpeedMultiplier(),
						condenserTube.condensingTemperature,
						condenserTube.adjacentTemperatures
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
			getMultiblock().setIsHeatExchangerOn();
		}
		return new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] deactivate(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			getMultiblock().computerActivated = false;
			getMultiblock().setIsHeatExchangerOn();
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
