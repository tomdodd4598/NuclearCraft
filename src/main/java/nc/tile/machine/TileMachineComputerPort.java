package nc.tile.machine;

import li.cil.oc.api.machine.*;
import li.cil.oc.api.network.SimpleComponent;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.machine.*;
import nc.multiblock.machine.Machine;
import nc.util.OCHelper;
import net.minecraftforge.fml.common.Optional;

import java.util.Collections;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileMachineComputerPort extends TileMachinePart implements SimpleComponent {
	
	public TileMachineComputerPort() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(Machine multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
	}
	
	// OpenComputers
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return "nc_machine";
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] isComplete(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled()};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] isMachineOn(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() && getMultiblock().isMachineOn};
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
	public Object[] getIsProcessing(Context context, Arguments args) {
		Machine machine = isMultiblockAssembled() ? null : getMultiblock();
		return new Object[] {machine != null && machine.processor.isProcessing};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getCurrentTime(Context context, Arguments args) {
		Machine machine = isMultiblockAssembled() ? null : getMultiblock();
		return new Object[] {machine == null ? 0D : machine.processor.time};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getBaseProcessTime(Context context, Arguments args) {
		MachineLogic logic = isMultiblockAssembled() ? null : getLogic();
		return new Object[] {logic == null ? 1D : logic.getProcessTimeFP()};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getBaseProcessPower(Context context, Arguments args) {
		MachineLogic logic = isMultiblockAssembled() ? null : getLogic();
		return new Object[] {logic == null ? 0D : logic.getProcessPowerFP()};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getItemInputs(Context context, Arguments args) {
		Machine machine = isMultiblockAssembled() ? null : getMultiblock();
		return new Object[] {OCHelper.stackInfoArray(machine == null ? Collections.emptyList() : machine.processor.getItemInputs(false))};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getFluidInputs(Context context, Arguments args) {
		Machine machine = isMultiblockAssembled() ? null : getMultiblock();
		return new Object[] {OCHelper.tankInfoArray(machine == null ? Collections.emptyList() : machine.processor.getFluidInputs(false))};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getItemOutputs(Context context, Arguments args) {
		Machine machine = isMultiblockAssembled() ? null : getMultiblock();
		return new Object[] {OCHelper.stackInfoArray(machine == null ? Collections.emptyList() : machine.processor.getItemOutputs())};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getFluidOutputs(Context context, Arguments args) {
		Machine machine = isMultiblockAssembled() ? null : getMultiblock();
		return new Object[] {OCHelper.tankInfoArray(machine == null ? Collections.emptyList() : machine.processor.getFluidOutputs())};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] haltProcess(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			getMultiblock().fullHalt = true;
		}
		return new Object[] {};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] resumeProcess(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			getMultiblock().fullHalt = false;
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
