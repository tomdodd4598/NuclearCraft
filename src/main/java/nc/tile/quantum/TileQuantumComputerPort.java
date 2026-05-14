package nc.tile.quantum;

import li.cil.oc.api.machine.*;
import li.cil.oc.api.network.SimpleComponent;
import nc.multiblock.quantum.QuantumComputer;
import nc.quantum.State;
import net.minecraftforge.fml.common.Optional;

import java.util.stream.IntStream;

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
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] isComplete(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled()};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getNumberOfQubits(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? 0 : getMultiblock().getQubitCount()};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getStateDim(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? 0 : getMultiblock().state.dim};
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getStateVector(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			return new Object[] {new double[][] {{1D, 0D}}};
		}
		else {
			State state = getMultiblock().state;
			double[] vector = state.vector;
			return new Object[] {IntStream.range(0, state.dim).mapToObj(i -> {
				int x = i << 1;
				return new double[] {vector[x], vector[x + 1]};
			}).toArray(double[][]::new)};
		}
	}
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getProbs(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? new double[] {1D} : getMultiblock().state.probs()};
	}
}
