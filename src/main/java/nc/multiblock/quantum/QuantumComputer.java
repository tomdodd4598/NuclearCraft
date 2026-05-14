package nc.multiblock.quantum;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.config.NCConfig;
import nc.multiblock.Multiblock;
import nc.quantum.*;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.tile.quantum.*;
import nc.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QuantumComputer extends Multiblock<QuantumComputer, IQuantumComputerPart> {
	
	public static final ObjectSet<Class<? extends IQuantumComputerPart>> PART_CLASSES = new ObjectOpenHashSet<>();
	
	protected final PartSuperMap<QuantumComputer, IQuantumComputerPart> partSuperMap = new PartSuperMap<>();
	
	protected TileQuantumComputerController controller;
	
	public State state;
	
	public final Queue<QuantumOperationWrapper> queue = new ConcurrentLinkedQueue<>();
	
	public int codeStart = -1, codeType = -1;
	protected StringBuilder codeBuilder;
	
	public QuantumComputer(World world) {
		super(world, QuantumComputer.class, IQuantumComputerPart.class);
		for (Class<? extends IQuantumComputerPart> clazz : PART_CLASSES) {
			partSuperMap.equip(clazz);
		}
		state = new State(0);
	}
	
	@Override
	public PartSuperMap<QuantumComputer, IQuantumComputerPart> getPartSuperMap() {
		return partSuperMap;
	}
	
	@Override
	public void onAttachedPartWithMultiblockData(IQuantumComputerPart part, NBTTagCompound data) {
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(IQuantumComputerPart newPart) {
		onPartAdded(newPart);
		refreshState();
		markQubitsDirty();
	}
	
	@Override
	protected void onBlockRemoved(IQuantumComputerPart oldPart) {
		onPartRemoved(oldPart);
		refreshState();
		markQubitsDirty();
	}
	
	@Override
	protected void onMachineAssembled() {
		onQuantumComputerFormed();
	}
	
	@Override
	protected void onMachineRestored() {
		onQuantumComputerFormed();
	}
	
	protected void onQuantumComputerFormed() {
		if (!WORLD.isRemote) {
			IntSet set = new IntOpenHashSet();
			for (TileQuantumComputerQubit qubit : getQubits()) {
				if (set.contains(qubit.id)) {
					qubit.id = -1;
				}
				else if (qubit.id >= 0) {
					set.add(qubit.id);
				}
			}
			
			int i = 0;
			for (TileQuantumComputerQubit qubit : getQubits()) {
				while (set.contains(i)) {
					++i;
				}
				if (qubit.id < 0) {
					qubit.id = i++;
				}
			}
		}
	}
	
	@Override
	protected void onMachinePaused() {}
	
	@Override
	protected void onMachineDisassembled() {}
	
	@Override
	protected int getMinimumNumberOfBlocksForAssembledMachine() {
		return 1;
	}
	
	@Override
	protected int getMaximumXSize() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	protected int getMaximumZSize() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	protected int getMaximumYSize() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	protected boolean isMachineWhole() {
		if (!NCConfig.quantum_dedicated_server && FMLCommonHandler.instance().getSide().isServer()) {
			setLastError(Global.MOD_ID + ".multiblock_validation.quantum_computer.server_disabled", Collections.emptyList());
			return false;
		}
		
		Long2ObjectMap<TileQuantumComputerController> controllerMap = getPartMap(TileQuantumComputerController.class);
		
		if (controllerMap.isEmpty()) {
			setLastError(Global.MOD_ID + ".multiblock_validation.no_controller", Collections.emptyList());
			return false;
		}
		if (controllerMap.size() > 1) {
			setLastError(Global.MOD_ID + ".multiblock_validation.too_many_controllers", controllerMap.keySet());
			return false;
		}
		
		int qubits = getQubitCount();
		if (qubits > NCConfig.quantum_max_qubits) {
			setLastError(Global.MOD_ID + ".multiblock_validation.quantum_computer.too_many_qubits", Collections.emptyList(), qubits, NCConfig.quantum_max_qubits);
			return false;
		}
		
		for (TileQuantumComputerController contr : controllerMap.values()) {
			controller = contr;
			break;
		}
		
		return true;
	}
	
	@Override
	protected void onAssimilate(QuantumComputer assimilated) {}
	
	@Override
	protected void onAssimilated(QuantumComputer assimilator) {}
	
	@Override
	protected boolean updateServer() {
		boolean refresh = false;
		
		int qubits = getQubitCount();
		if (codeStart >= 0) {
			codeType = codeStart;
			codeStart = -1;
			codeBuilder = new StringBuilder();
		}
		
		QuantumOperationWrapper gate = queue.poll();
		if (gate != null) {
			if (codeType >= 0) {
				if (qubits <= NCConfig.quantum_max_qubits) {
					List<String> code = gate.getCode(codeType);
					if (!code.isEmpty()) {
						codeBuilder.append(IOHelper.NEW_LINE);
					}
					for (String line : code) {
						codeBuilder.append(line);
						codeBuilder.append(IOHelper.NEW_LINE);
					}
				}
			}
			else if (qubits <= NCConfig.quantum_max_qubits) {
				gate.run();
				refresh = gate.shouldRefresh();
			}
		}
		
		return refresh;
	}
	
	@Override
	protected void updateClient() {}
	
	@Override
	protected boolean isBlockGoodForInterior(World world, BlockPos pos) {
		return true;
	}
	
	@Override
	public void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		if (data.hasKey("size")) {
			int size = data.getInteger("size");
			if (size <= NCConfig.quantum_max_qubits) {
				state = new State(size);
				ByteBuffer.wrap(data.getByteArray("vector")).asDoubleBuffer().get(state.vector);
			}
		}
	}
	
	@Override
	public void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		if (getQubitCount() <= NCConfig.quantum_max_qubits) {
			data.setInteger("size", state.size);
			
			ByteBuffer byteBuf = ByteBuffer.allocate(state.dim << 4);
			DoubleBuffer doubleBuf = byteBuf.asDoubleBuffer();
			doubleBuf.put(state.vector);
			data.setByteArray("vector", byteBuf.array());
		}
	}
	
	// Qubit Logic
	
	public Collection<TileQuantumComputerQubit> getQubits() {
		return getParts(TileQuantumComputerQubit.class);
	}
	
	public int getQubitCount() {
		return getPartCount(TileQuantumComputerQubit.class);
	}
	
	protected void setQubitsRedstone(int[] targets, boolean[] results) {
		Int2IntMap idMap = new Int2IntOpenHashMap();
		for (int i = 0, len = targets.length; i < len; ++i) {
			idMap.put(targets[i], i);
		}
		
		for (TileQuantumComputerQubit qubit : getQubits()) {
			int id = qubit.id;
			if (idMap.containsKey(id)) {
				boolean result = results[idMap.get(id)];
				qubit.redstone = result;
				qubit.measureColor = result ? 1F : -1F;
				qubit.sendTileUpdatePacketToAll();
			}
		}
	}
	
	protected void markQubitsDirty() {
		for (TileQuantumComputerQubit qubit : getQubits()) {
			qubit.markDirty();
			qubit.updateComparatorOutputLevel();
		}
	}
	
	// Gates
	
	public void refreshState() {
		int qubits = getQubitCount();
		if (state.size != qubits) {
			state = new State(qubits);
		}
	}
	
	public void measure(int[] targets) {
		refreshState();
		setQubitsRedstone(targets, state.measure(targets));
		markQubitsDirty();
	}
	
	public void reset() {
		state = new State(getQubitCount());
	}
	
	public void gate(Gate gate) {
		refreshState();
		state.update(gate);
	}
	
	// Code Generation
	
	public void printCode(EntityPlayer player) {
		if (codeType < 0) {
			return;
		}
		
		int cachedCodeType = codeType;
		codeType = -1;
		
		int qubits = getQubitCount();
		if (qubits > NCConfig.quantum_max_qubits) {
			player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.quantum_computer.controller.code_exit_too_many_qubits")));
			return;
		}
		
		String codeString = codeBuilder.toString();
		String s = IOHelper.NEW_LINE, d = s + s, time = Long.toString(System.currentTimeMillis() / 100L);
		
		if (cachedCodeType == 0) {
			if (codeString.isEmpty()) {
				player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.quantum_computer.controller.qasm_exit_empty")));
				return;
			}
			
			File out = new File("nc_quantum/qasm/" + qubits + "_qubit_" + time + ".qasm");
			
			codeString = "OPENQASM 2.0;" + s + "include \"qelib1.inc\";" + d + "qreg q[" + qubits + "];" + s + "creg c[" + qubits + "];" + d + codeString;
			
			try {
				FileUtils.writeStringToFile(out, codeString);
				ITextComponent link = new TextComponentString(out.getName());
				link.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, out.getAbsolutePath())).setBold(true).setUnderlined(true);
				player.sendMessage(new TextComponentTranslation("info.nuclearcraft.multitool.quantum_computer.controller.qasm_print", link));
			}
			catch (IOException e) {
				NCUtil.getLogger().catching(e);
				player.sendMessage(new TextComponentTranslation("info.nuclearcraft.multitool.quantum_computer.controller.qasm_error", out.getAbsolutePath()));
			}
		}
		else if (cachedCodeType == 1) {
			if (codeString.isEmpty()) {
				player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.quantum_computer.controller.qiskit_exit_empty")));
				return;
			}
			
			File out = new File("nc_quantum/qiskit/" + qubits + "_qubit_" + time + ".ipynb");
			
			codeString = "# Jupyter plot output mode" + s + "# %matplotlib inline" + d +
					
					"# Imports" + s + "import qiskit" + s + "from qiskit import IBMQ, QuantumCircuit, visualization" + s + "from qiskit.providers import ibmq" + s + "from qiskit.tools import monitor" + d +
					
					"# Number of qubits" + s + "qubits = " + qubits + d +
					
					"# Load IBMQ account" + s + "provider = IBMQ.load_account()" + d +
					
					"# Get backends" + s + "simulator = provider.get_backend('simulator_statevector')" + s + "device = provider.get_backend('ibmq_manila')" + s + "filtered = provider.backends(" + s + "    filters=lambda x:" + s + "    int(x.configuration().num_qubits) >= qubits" + s + "    and not x.configuration().simulator" + s + "    and x.status().operational" + s + ")" + s + "leastbusy = ibmq.least_busy(filtered) if len(filtered) > 0 else device" + d +
					
					"# Choice of backend" + s + "qc_backend = " + (qubits > 5 ? "simulator" : "device") + d +
					
					"# Construct circuit" + s + "qc = QuantumCircuit(qubits, qubits)" + d +
					
					"# Generated code" + codeString + d +
					
					"# Helper function" + s + "def run_job(circuit, backend, shots=4096, optimization_level=3):" + s + "    print(f'Using backend {backend}...')" + s + "    job = qiskit.execute(circuit, backend=backend, shots=shots, optimization_level=optimization_level)" + s + "    qiskit.tools.job_monitor(job)" + s + "    return job.result()" + s + d +
					
					"# Run circuit" + s + "result = run_job(qc, qc_backend, 4096, 3)" + s + "counts = result.get_counts(qc)" + s + "hist = visualization.plot_histogram(counts)" + s + "print('\\nCounts: ', counts)" + d +
					
					"# Save circuit diagram to file" + s + "qc.draw(output='mpl', filename='circuit.png')" + d +
					
					"# Save plot to file" + s + "hist.savefig('counts.png')" + d +
					
					"# Plot results in output - only works in Jupyter" + s + "# hist" + s;
			
			try {
				FileUtils.writeStringToFile(out, codeString);
				ITextComponent link = new TextComponentString(out.getName());
				link.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, out.getAbsolutePath())).setBold(true).setUnderlined(true);
				player.sendMessage(new TextComponentTranslation("info.nuclearcraft.multitool.quantum_computer.controller.qiskit_print", link));
			}
			catch (IOException e) {
				NCUtil.getLogger().catching(e);
				player.sendMessage(new TextComponentTranslation("info.nuclearcraft.multitool.quantum_computer.controller.qiskit_error", out.getAbsolutePath()));
			}
		}
		else {
			player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.quantum_computer.controller.code_exit_empty")));
			return;
		}
		
		codeString = null;
	}
}
