package nc.tile.hx;

import li.cil.oc.api.machine.*;
import li.cil.oc.api.network.SimpleComponent;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.hx.HeatExchanger;
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
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
	}
	
	// OpenComputers
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return "nc_heat_exchanger";
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
