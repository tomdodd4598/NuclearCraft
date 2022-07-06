package nc.multiblock.qComputer;

import static nc.config.NCConfig.*;
import static nc.multiblock.qComputer.QuantumGate.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.io.FileUtils;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.config.NCConfig;
import nc.multiblock.Multiblock;
import nc.multiblock.qComputer.tile.*;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
import nc.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class QuantumComputer extends Multiblock<QuantumComputer, IQuantumComputerPart> {
	
	public static final ObjectSet<Class<? extends IQuantumComputerPart>> PART_CLASSES = new ObjectOpenHashSet<>();
	
	protected final PartSuperMap<QuantumComputer, IQuantumComputerPart> partSuperMap = new PartSuperMap<>();
	
	protected TileQuantumComputerController controller;
	
	protected ComplexVector state, cache = null;
	
	protected Queue<QuantumGate<?>> queue = new ConcurrentLinkedQueue<>();
	
	public int codeStart = -1, codeType = -1;
	protected StringBuilder codeBuilder;
	
	public QuantumComputer(World world) {
		super(world, QuantumComputer.class, IQuantumComputerPart.class);
		for (Class<? extends IQuantumComputerPart> clazz : PART_CLASSES) {
			partSuperMap.equip(clazz);
		}
		state = new ComplexVector(1);
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
		refreshState(false);
	}
	
	@Override
	protected void onBlockRemoved(IQuantumComputerPart oldPart) {
		onPartRemoved(oldPart);
		refreshState(false);
	}
	
	@Override
	protected void onMachineAssembled() {
		onQuantumComputerFormed();
	}
	
	@Override
	protected void onMachineRestored() {
		onQuantumComputerFormed();
	}
	
	public static int getMaxQubits() {
		return Math.max(quantum_max_qubits_live, quantum_max_qubits_code);
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
			setLastError(Global.MOD_ID + ".multiblock_validation.quantum_computer.server_disabled", null);
			return false;
		}
		
		if (getPartMap(TileQuantumComputerController.class).isEmpty()) {
			setLastError(Global.MOD_ID + ".multiblock_validation.no_controller", null);
			return false;
		}
		if (getPartCount(TileQuantumComputerController.class) > 1) {
			setLastError(Global.MOD_ID + ".multiblock_validation.too_many_controllers", null);
			return false;
		}
		int q = qubitCount(), max = getMaxQubits();
		if (q > max) {
			setLastError(Global.MOD_ID + ".multiblock_validation.quantum_computer.too_many_qubits", null, q, max);
			return false;
		}
		
		for (TileQuantumComputerController contr : getParts(TileQuantumComputerController.class)) {
			controller = contr;
		}
		
		return true;
	}
	
	@Override
	protected void onAssimilate(QuantumComputer assimilated) {
		/*if (isAssembled()) {
			onQuantumComputerFormed();
		}*/
	}
	
	@Override
	protected void onAssimilated(QuantumComputer assimilator) {}
	
	@Override
	protected boolean updateServer() {
		boolean refresh = false;
		
		int q = qubitCount();
		if (codeStart >= 0) {
			codeType = codeStart;
			codeStart = -1;
			codeBuilder = new StringBuilder();
		}
		
		QuantumGate<?> gate = queue.poll();
		if (gate != null) {
			try {
				tryLoadStateCache(dim(q));
				QuantumGate<?> merger = gate, next;
				while (merger != null) {
					next = queue.peek();
					
					if (next == null) {
						break;
					}
					
					merger = merger.merge(next);
					if (merger != null) {
						queue.poll();
						gate = merger;
					}
				}
				
				if (codeType >= 0) {
					if (q <= quantum_max_qubits_code) {
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
				else if (q <= quantum_max_qubits_live) {
					gate.run();
					refresh = gate.shouldMarkDirty();
				}
			}
			catch (OutOfMemoryError e) {
				if (controller != null) {
					WORLD.removeTileEntity(controller.getPos());
					WORLD.setBlockToAir(controller.getPos());
				}
				NCUtil.getLogger().fatal("The quantum computer with " + q + " qubits at " + getMiddleCoord().toString() + " has caused the game to run out of heap memory! The controller has been destroyed and so the multiblock has been disabled. It is HIGHLY recommended that the maximum qubit limits are lowered in the configs!");
				e.printStackTrace();
				return false;
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
		cache = ComplexVector.readFromNBT(data, "state");
	}
	
	@Override
	public void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		if (qubitCount() <= quantum_max_qubits_live) {
			state.writeToNBT(data, "state");
		}
	}
	
	protected boolean tryLoadStateCache(int dim) {
		if (cache != null && cache.dim == dim) {
			state = cache.normalize();
			cache = null;
			
			markQubitsDirty();
			return true;
		}
		return false;
	}
	
	// Quantum Logic
	
	public Collection<TileQuantumComputerQubit> getQubits() {
		return getParts(TileQuantumComputerQubit.class);
	}
	
	public int qubitCount() {
		return getPartCount(TileQuantumComputerQubit.class);
	}
	
	protected void setQubitsRedstone(int o, IntList n) {
		if (cache == null) {
			for (TileQuantumComputerQubit qubit : getQubits()) {
				if (n.contains(qubit.id)) {
					boolean result = NCMath.getBit(o, qubitCount() - qubit.id - 1) == 1;
					qubit.redstone = result;
					qubit.measureColor = result ? 1F : -1F;
					qubit.sendTileUpdatePacketToAll();
				}
			}
		}
	}
	
	protected void markQubitsDirty() {
		for (TileQuantumComputerQubit qubit : getQubits()) {
			qubit.markDirty();
			qubit.updateComparatorOutputLevel();
		}
	}
	
	protected void checkStateDim(int dim) {
		if (state.dim != dim && qubitCount() <= quantum_max_qubits_live) {
			state = new ComplexVector(dim);
		}
	}
	
	protected void collapse(int dim, int o, IntSet i, IntSet n) {
		if (i.size() <= 1) {
			state.zero();
			int j = 0;
			for (int k : i) {
				j = k;
			}
			state.re[j] = 1D;
			state.im[j] = 0D;
		}
		else {
			for (int j = 0; j < dim; ++j) {
				if (!i.contains(j)) {
					state.re[j] = state.im[j] = 0D;
				}
			}
		}
		state.normalize();
		
		setQubitsRedstone(o, list(n));
		markQubitsDirty();
	}
	
	protected void refreshState(boolean collapse) {
		int q = qubitCount();
		if (q > getMaxQubits()) {
			return;
		}
		
		int dim = dim(q);
		checkStateDim(dim);
		
		if (collapse) {
			collapse(dim, 0, set(0), set(CollectionHelper.increasingArray(q)));
		}
		
		markQubitsDirty();
	}
	
	public void measure(IntSet n) {
		IntList _n = list(n);
		int q = qubitCount(), dim = dim(q), o;
		checkStateDim(dim);
		
		int[] z = new int[_n.size()];
		for (int i = 0; i < _n.size(); ++i) {
			z[i] = q - _n.getInt(i) - 1;
		}
		
		Int2DoubleMap w = new Int2DoubleOpenHashMap();
		Int2ObjectMap<IntSet> s = new Int2ObjectOpenHashMap<>();
		IntList l = new IntArrayList();
		IntSet j = null;
		double a, sum = 0D;
		for (int i = 0; i < dim; ++i) {
			a = Complex.absSq(state.re[i], state.im[i]);
			sum += a;
			o = NCMath.onlyBits(i, z);
			if (w.containsKey(o)) {
				w.put(o, w.get(o) + a);
				s.get(o).add(i);
			}
			else {
				l.add(o);
				w.put(o, a);
				s.put(o, set(i));
			}
		}
		
		o = dim - 1;
		sum *= rand.nextDouble();
		for (int i : l) {
			sum -= w.get(i);
			if (sum < 0) {
				o = i;
				j = s.get(i);
				break;
			}
		}
		
		collapse(dim, o, j == null ? set(dim - 1) : j, n);
	}
	
	// Gates
	
	public Queue<QuantumGate<?>> getGateQueue() {
		return queue;
	}
	
	protected boolean invalid(IntList n) {
		int q = qubitCount();
		for (int i : n) {
			if (i < 0 || i >= q) {
				return true;
			}
		}
		return false;
	}
	
	protected ComplexMatrix fallback() {
		return id(qubitCount());
	}
	
	protected void gate(ComplexMatrix m) {
		checkStateDim(dim(qubitCount()));
		state.map(m);
	}
	
	protected ComplexMatrix single(ComplexMatrix m, IntList n) {
		int q = qubitCount();
		if (n.isEmpty()) {
			return id(q);
		}
		
		if (invalid(n)) {
			return fallback();
		}
		
		ComplexMatrix[] t = new ComplexMatrix[q];
		for (int j = 0; j < q; ++j) {
			t[j] = n.contains(j) ? m : I;
		}
		
		return ComplexMatrix.tensorProduct(t);
	}
	
	public void x(IntSet n) {
		gate(single(X, list(n)));
	}
	
	public void y(IntSet n) {
		gate(single(Y, list(n)));
	}
	
	public void z(IntSet n) {
		gate(single(Z, list(n)));
	}
	
	public void h(IntSet n) {
		gate(single(H, list(n)));
	}
	
	public void s(IntSet n) {
		gate(single(S, list(n)));
	}
	
	public void sdg(IntSet n) {
		gate(single(Sdg, list(n)));
	}
	
	public void t(IntSet n) {
		gate(single(T, list(n)));
	}
	
	public void tdg(IntSet n) {
		gate(single(Tdg, list(n)));
	}
	
	/** Angle in degrees! */
	public void p(double angle, IntSet n) {
		gate(single(QuantumGate.p(angle), list(n)));
	}
	
	/** Angle in degrees! */
	public void rx(double angle, IntSet n) {
		gate(single(QuantumGate.rx(angle), list(n)));
	}
	
	/** Angle in degrees! */
	public void ry(double angle, IntSet n) {
		gate(single(QuantumGate.ry(angle), list(n)));
	}
	
	/** Angle in degrees! */
	public void rz(double angle, IntSet n) {
		gate(single(QuantumGate.rz(angle), list(n)));
	}
	
	public void swap(IntList i_, IntList j_) {
		if (i_.size() != j_.size()) {
			return;
		}
		
		// IntList _i = list(i_), _j = list(j_);
		if (invalid(i_) || invalid(j_)) {
			return;
		}
		
		int q = qubitCount(), dim = dim(q), i, j, s;
		checkStateDim(dim);
		
		double re, im;
		for (int k = 0; k < dim; ++k) {
			for (int a = 0; a < i_.size(); ++a) {
				i = i_.getInt(a);
				j = j_.getInt(a);
				if (i == j) {
					continue;
				}
				
				i = q - i - 1;
				j = q - j - 1;
				if (NCMath.getBit(k, i) == 0 && NCMath.getBit(k, j) == 1) {
					s = NCMath.swap(k, i, j);
					re = state.re[k];
					im = state.im[k];
					state.re[k] = state.re[s];
					state.im[k] = state.im[s];
					state.re[s] = re;
					state.im[s] = im;
				}
			}
		}
	}
	
	public ComplexMatrix control(ComplexMatrix g, IntList c, IntList t) {
		int q = qubitCount();
		if (t.isEmpty()) {
			return id(q);
		}
		
		if (c.isEmpty()) {
			return single(g, t);
		}
		
		if (invalid(c)) {
			return fallback();
		}
		
		for (int i : c) {
			if (t.contains(i)) {
				return id(q);
			}
		}
		
		int s = dim(c.size()), k;
		ComplexMatrix m = new ComplexMatrix(dim(q));
		ComplexMatrix[] e = new ComplexMatrix[q];
		boolean b;
		for (int i = 0; i < s; ++i) {
			k = 0;
			for (int j = 0; j < q; ++j) {
				b = c.contains(j);
				e[j] = b ? NCMath.getBit(i, c.size() - k - 1) == 1 ? P_1 : P_0 : t.contains(j) && i == s - 1 ? g : I;
				if (b) {
					++k;
				}
			}
			m.add(ComplexMatrix.tensorProduct(e));
		}
		
		return m;
	}
	
	public void cx(IntSet c, IntSet t) {
		gate(control(X, list(c), list(t)));
	}
	
	public void cy(IntSet c, IntSet t) {
		gate(control(Y, list(c), list(t)));
	}
	
	public void cz(IntSet c, IntSet t) {
		gate(control(Z, list(c), list(t)));
	}
	
	public void ch(IntSet c, IntSet t) {
		gate(control(H, list(c), list(t)));
	}
	
	public void cs(IntSet c, IntSet t) {
		gate(control(S, list(c), list(t)));
	}
	
	public void csdg(IntSet c, IntSet t) {
		gate(control(Sdg, list(c), list(t)));
	}
	
	public void ct(IntSet c, IntSet t) {
		gate(control(T, list(c), list(t)));
	}
	
	public void ctdg(IntSet c, IntSet t) {
		gate(control(Tdg, list(c), list(t)));
	}
	
	/** Angle in degrees! */
	public void cp(double angle, IntSet c, IntSet t) {
		gate(control(QuantumGate.p(angle), list(c), list(t)));
	}
	
	/** Angle in degrees! */
	public void crx(double angle, IntSet c, IntSet t) {
		gate(control(QuantumGate.rx(angle), list(c), list(t)));
	}
	
	/** Angle in degrees! */
	public void cry(double angle, IntSet c, IntSet t) {
		gate(control(QuantumGate.ry(angle), list(c), list(t)));
	}
	
	/** Angle in degrees! */
	public void crz(double angle, IntSet c, IntSet t) {
		gate(control(QuantumGate.rz(angle), list(c), list(t)));
	}
	
	// Don't know how to optimise this!
	public void cswap(IntSet c_, IntList i_, IntList j_) {
		if (c_.isEmpty()) {
			swap(i_, j_);
			return;
		}
		
		if (i_.size() != j_.size()) {
			return;
		}
		
		if (invalid(i_) || invalid(j_)) {
			return;
		}
		
		for (int i : c_) {
			if (i_.contains(i) || j_.contains(i)) {
				return;
			}
		}
		
		IntList c = list(c_);
		int s = dim(c.size()), q = qubitCount(), dim = dim(q), k, i, j, w;
		checkStateDim(dim);
		
		ComplexMatrix m = new ComplexMatrix(dim), p;
		ComplexMatrix[] e = new ComplexMatrix[q];
		boolean b;
		for (int u = 0; u < s; ++u) {
			k = 0;
			for (int v = 0; v < q; ++v) {
				b = c.contains(v);
				e[v] = b ? NCMath.getBit(u, c.size() - k - 1) == 1 ? P_1 : P_0 : I;
				if (b) {
					++k;
				}
			}
			p = ComplexMatrix.tensorProduct(e);
			if (u == s - 1) {
				for (int l = 0; l < dim; ++l) {
					for (int a = 0; a < i_.size(); ++a) {
						i = i_.getInt(a);
						j = j_.getInt(a);
						if (i == j) {
							continue;
						}
						
						i = q - i - 1;
						j = q - j - 1;
						if (NCMath.getBit(l, i) == 0 && NCMath.getBit(l, j) == 1) {
							w = NCMath.swap(l, i, j);
							p.swap(l, l, w, l);
							p.swap(w, w, l, w);
						}
					}
				}
			}
			m.add(p);
		}
		
		gate(m);
	}
	
	public void printCode(EntityPlayer player) {
		if (codeType < 0) {
			return;
		}
		
		int cachedCodeType = codeType;
		codeType = -1;
		
		int q = qubitCount();
		if (q > quantum_max_qubits_code) {
			player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.controller.code_exit_too_many_qubits")));
			return;
		}
		
		String codeString = codeBuilder.toString();
		String s = IOHelper.NEW_LINE, d = s + s, time = Long.toString(System.currentTimeMillis() / 100L);
		
		if (cachedCodeType == 0) {
			if (codeString.isEmpty()) {
				player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.controller.qasm_exit_empty")));
				return;
			}
			
			File out = new File("nc_quantum/qasm/" + q + "_qubit_" + time + ".qasm");
			
			codeString = "OPENQASM 2.0;" + s + "include \"qelib1.inc\";" + d + "qreg q[" + q + "];" + s + "creg c[" + q + "];" + d + codeString;
			
			try {
				FileUtils.writeStringToFile(out, codeString);
				ITextComponent link = new TextComponentString(out.getName());
				link.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, out.getAbsolutePath())).setBold(true).setUnderlined(true);
				player.sendMessage(new TextComponentTranslation("info.nuclearcraft.multitool.quantum_computer.controller.qasm_print", new Object[] {link}));
			}
			catch (IOException e) {
				NCUtil.getLogger().catching(e);
				player.sendMessage(new TextComponentTranslation("info.nuclearcraft.multitool.quantum_computer.controller.qasm_error", new Object[] {out.getAbsolutePath()}));
			}
		}
		else if (cachedCodeType == 1) {
			if (codeString.isEmpty()) {
				player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.controller.qiskit_exit_empty")));
				return;
			}
			
			File out = new File("nc_quantum/qiskit/" + q + "_qubit_" + time + ".ipynb");
			
			codeString = "# Jupyter plot output mode" + s + "# %matplotlib inline" + d +
					
					"# Imports" + s + "import qiskit" + s + "from qiskit import IBMQ, QuantumCircuit, visualization" + s + "from qiskit.providers import ibmq" + s + "from qiskit.tools import monitor" + d +
					
					"# Number of qubits" + s + "qubits = " + q + d +
					
					"# Load IBMQ account" + s + "provider = IBMQ.load_account()" + d +
					
					"# Get backends" + s + "simulator = provider.get_backend('simulator_statevector')" + s + "device = provider.get_backend('ibmq_manila')" + s + "filtered = provider.backends(" + s + "    filters=lambda x:" + s + "    int(x.configuration().num_qubits) >= qubits" + s + "    and not x.configuration().simulator" + s + "    and x.status().operational" + s + ")" + s + "leastbusy = ibmq.least_busy(filtered) if len(filtered) > 0 else device" + d +
					
					"# Choice of backend" + s + "qc_backend = " + (q > 5 ? "simulator" : "device") + d +
					
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
				player.sendMessage(new TextComponentTranslation("info.nuclearcraft.multitool.quantum_computer.controller.qiskit_print", new Object[] {link}));
			}
			catch (IOException e) {
				NCUtil.getLogger().catching(e);
				player.sendMessage(new TextComponentTranslation("info.nuclearcraft.multitool.quantum_computer.controller.qiskit_error", new Object[] {out.getAbsolutePath()}));
			}
		}
		else {
			player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.controller.code_exit_empty")));
			return;
		}
		
		codeString = null;
	}
}
