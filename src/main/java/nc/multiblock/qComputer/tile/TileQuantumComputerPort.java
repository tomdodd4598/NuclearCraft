package nc.multiblock.qComputer.tile;

import li.cil.oc.api.machine.*;
import li.cil.oc.api.network.SimpleComponent;
import nc.multiblock.qComputer.QuantumComputer;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileQuantumComputerPort extends TileQuantumComputerPart implements SimpleComponent {
	
	public TileQuantumComputerPort() {
		super();
	}
	
	@Override
	public void onMachineAssembled(QuantumComputer multiblock) {
		doStandardNullControllerResponse(multiblock);
	}
	
	@Override
	public void onMachineBroken() {}
	
	// OpenComputers
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return "nc_quantum_computer";
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] isComplete(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled()};
	}
}
