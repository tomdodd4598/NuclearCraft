package nc.tile.hx;

import li.cil.oc.api.machine.*;
import li.cil.oc.api.network.SimpleComponent;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.hx.*;
import nc.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Optional;

import java.util.*;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileHeatExchangerComputerPort extends TileHeatExchangerPart implements SimpleComponent {
	
	public TileHeatExchangerComputerPort() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(HeatExchanger multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
	}
	
	// OpenComputers
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return "nc_heat_exchanger";
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] isComplete(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled()};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] isExchangerOn(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() && getMultiblock().isExchangerOn};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getLengthX(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getInteriorLengthX() : 0};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getLengthY(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getInteriorLengthY() : 0};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getLengthZ(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getInteriorLengthZ() : 0};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getActiveNetworkCount(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().activeNetworkCount : 0};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getActiveTubeCount(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().activeTubeCount : 0};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getShellSpeedMultiplier(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().shellSpeedMultiplier : 0D};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getShellInputRate(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().shellInputRate : 0D};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getHeatTransferRate(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().heatTransferRate : 0D};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getMeanTempDiff(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			HeatExchanger hx = getMultiblock();
			return new Object[] {hx.activeContactCount == 0 ? 0D : hx.totalTempDiff / hx.activeContactCount};
		}
		else {
			return new Object[] {0D};
		}
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getNumberOfNetworks(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().networks.size() : 0};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getNetworkStats(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			List<Object[]> stats = new ArrayList<>();
			for (HeatExchangerTubeNetwork network : getMultiblock().networks) {
				stats.add(new Object[] {network.usefulTubeCount, OCHelper.vec3dInfo(network.tubeFlow), OCHelper.vec3dInfo(network.shellFlow), network.flowCosine, network.baseHeatingMultiplier, network.baseCoolingMultiplier, OCHelper.tankInfoArray(network.getTanks())});
			}
			return new Object[] {stats.toArray()};
		}
		else {
			return new Object[] {};
		}
	}
	
	protected <T extends IHeatExchangerPart> Object[] getPartCount(Class<T> type) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getPartCount(type) : 0};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getNumberOfTubes(Context context, Arguments args) {
		return getPartCount(TileHeatExchangerTube.class);
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getTubeStats(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			List<Object[]> stats = new ArrayList<>();
			for (TileHeatExchangerTube tube : getMultiblock().getParts(TileHeatExchangerTube.class)) {
				BlockPos pos = tube.getPos();
				stats.add(new Object[] {OCHelper.posInfo(pos), tube.heatTransferCoefficient, tube.heatRetentionMult, StreamHelper.map(tube.settings, Object::toString, Object[]::new), OCHelper.vec3dInfo(tube.tubeFlow), OCHelper.vec3dInfo(tube.shellFlow)});
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
			getLogic().setIsExchangerOn();
		}
		return new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] deactivate(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			getMultiblock().computerActivated = false;
			getLogic().setIsExchangerOn();
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
