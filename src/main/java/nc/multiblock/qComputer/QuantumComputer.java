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
import nc.multiblock.Multiblock;
import nc.multiblock.network.*;
import nc.multiblock.qComputer.tile.*;
import nc.multiblock.tile.ITileMultiblockPart;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
import nc.network.PacketHandler;
import nc.util.*;
import nc.util.Vector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;

public class QuantumComputer extends Multiblock<IQuantumComputerPart, MultiblockUpdatePacket> {
	
	public static final ObjectSet<Class<? extends IQuantumComputerPart>> PART_CLASSES = new ObjectOpenHashSet<>();
	
	protected final PartSuperMap<IQuantumComputerPart> partSuperMap = new PartSuperMap<>();
	
	protected TileQuantumComputerController controller;
	
	protected Vector state, cache = null;
	
	protected Queue<QuantumGate> queue = new ConcurrentLinkedQueue<>();
	
	public int codeStart = -1, codeType = -1;
	protected StringBuilder codeBuilder;
	
	public QuantumComputer(World world) {
		super(world);
		for (Class<? extends IQuantumComputerPart> clazz : PART_CLASSES) {
			partSuperMap.equip(clazz);
		}
		state = new Vector(1);
	}
	
	@Override
	public PartSuperMap<IQuantumComputerPart> getPartSuperMap() {
		return partSuperMap;
	}
	
	@Override
	public void onAttachedPartWithMultiblockData(ITileMultiblockPart part, NBTTagCompound data) {
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(ITileMultiblockPart newPart) {
		onPartAdded(newPart);
		refreshState(false);
	}
	
	@Override
	protected void onBlockRemoved(ITileMultiblockPart oldPart) {
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
	protected boolean isMachineWhole(Multiblock multiblock) {
		if (getPartMap(TileQuantumComputerController.class).isEmpty()) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.no_controller", null);
			return false;
		}
		if (getPartCount(TileQuantumComputerController.class) > 1) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.too_many_controllers", null);
			return false;
		}
		int q = qubitCount(), max = getMaxQubits();
		if (q > max) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.quantum_computer.too_many_qubits", null, q, max);
			return false;
		}
		
		for (TileQuantumComputerController contr : getParts(TileQuantumComputerController.class)) {
			controller = contr;
		}
		
		return true;
	}
	
	@Override
	protected void onAssimilate(Multiblock assimilated) {
		if (isAssembled()) {
			onQuantumComputerFormed();
		}
	}
	
	@Override
	protected void onAssimilated(Multiblock assimilator) {}
	
	@Override
	protected boolean updateServer() {
		boolean refresh = false;
		
		int q = qubitCount();
		if (codeStart >= 0) {
			codeType = codeStart;
			codeStart = -1;
			codeBuilder = new StringBuilder();
		}
		
		QuantumGate gate = queue.poll();
		if (gate != null) {
			try {
				tryLoadStateCache(dim(q));
				QuantumGate merger = gate, next;
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
	protected boolean isBlockGoodForInterior(World world, int x, int y, int z, Multiblock multiblock) {
		return true;
	}
	
	@Override
	public void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		cache = Vector.readFromNBT(data, "state");
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
	
	@Override
	protected MultiblockUpdatePacket getUpdatePacket() {
		return null;
	}
	
	@Override
	public void onPacket(MultiblockUpdatePacket message) {}
	
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
					PacketHandler.instance.sendToAll(new QuantumComputerQubitRenderPacket(qubit.getPos(), qubit.measureColor));
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
			state = new Vector(dim);
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
			for (int j = 0; j < dim; j++) {
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
		for (int j = 0; j < _n.size(); j++) {
			z[j] = q - _n.getInt(j) - 1;
		}
		
		Int2DoubleMap w = new Int2DoubleOpenHashMap();
		Int2ObjectMap<IntSet> s = new Int2ObjectOpenHashMap<>();
		IntList l = new IntArrayList();
		IntSet i = null;
		double a, sum = 0D;
		for (int j = 0; j < dim; j++) {
			a = Complex.absSq(state.re[j], state.im[j]);
			sum += a;
			o = NCMath.onlyBits(j, z);
			if (w.containsKey(o)) {
				w.put(o, w.get(o) + a);
				s.get(o).add(j);
			}
			else {
				l.add(o);
				w.put(o, a);
				s.put(o, set(j));
			}
		}
		
		o = dim - 1;
		sum *= rand.nextDouble();
		for (int j : l) {
			sum -= w.get(j);
			if (sum < 0) {
				o = j;
				i = s.get(j);
				break;
			}
		}
		
		collapse(dim, o, i == null ? set(dim - 1) : i, n);
	}
	
	// Gates
	
	public Queue<QuantumGate> getGateQueue() {
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
	
	protected Matrix fallback() {
		return id(qubitCount());
	}
	
	protected void gate(Matrix m) {
		checkStateDim(dim(qubitCount()));
		state.map(m);
	}
	
	protected Matrix single(Matrix m, IntList n) {
		int q = qubitCount();
		if (n.isEmpty()) {
			return id(q);
		}
		
		if (invalid(n)) {
			return fallback();
		}
		
		Matrix[] t = new Matrix[q];
		for (int j = 0; j < q; j++) {
			t[j] = n.contains(j) ? m : I;
		}
		
		return Matrix.tensorProduct(t);
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
		for (int k = 0; k < dim; k++) {
			for (int a = 0; a < i_.size(); a++) {
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
	
	public Matrix control(Matrix g, IntList c, IntList t) {
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
		Matrix m = new Matrix(dim(q));
		Matrix[] e = new Matrix[q];
		boolean b;
		for (int i = 0; i < s; i++) {
			k = 0;
			for (int j = 0; j < q; j++) {
				b = c.contains(j);
				e[j] = b ? NCMath.getBit(i, c.size() - k - 1) == 1 ? P_1 : P_0 : t.contains(j) && i == s - 1 ? g : I;
				if (b) {
					++k;
				}
			}
			m.add(Matrix.tensorProduct(e));
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
		
		Matrix m = new Matrix(dim), p;
		Matrix[] e = new Matrix[q];
		boolean b;
		for (int u = 0; u < s; u++) {
			k = 0;
			for (int v = 0; v < q; v++) {
				b = c.contains(v);
				e[v] = b ? NCMath.getBit(u, c.size() - k - 1) == 1 ? P_1 : P_0 : I;
				if (b) {
					++k;
				}
			}
			p = Matrix.tensorProduct(e);
			if (u == s - 1) {
				for (int l = 0; l < dim; l++) {
					for (int a = 0; a < i_.size(); a++) {
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
		
		int codeType = this.codeType;
		this.codeType = -1;
		
		int q = qubitCount();
		if (q > quantum_max_qubits_code) {
			player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.controller.code_exit_too_many_qubits")));
			return;
		}
		
		String codeString = codeBuilder.toString();
		String s = IOHelper.NEW_LINE, d = s + s, time = Long.toString(System.currentTimeMillis() / 100L);
		
		if (codeType == 0) {
			if (codeString.isEmpty()) {
				player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.controller.qasm_exit_empty")));
				return;
			}
			
			File out = new File("nuclearcraft/quantum/qasm/" + q + "_qubit_" + time + ".qasm");
			
			codeString = "OPENQASM 2.0;" + s +
					"include \"qelib1.inc\";" + d +
					"qreg q[" + q + "];" + s +
					"creg c[" + q + "];" + d +
					codeString;
			
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
		else if (codeType == 1) {
			if (codeString.isEmpty()) {
				player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.controller.qiskit_exit_empty")));
				return;
			}
			
			File out = new File("nuclearcraft/quantum/qiskit/" + q + "_qubit_" + time + ".ipynb");
			
			codeString = "# Jupyter plot output mode" + s +
					"%matplotlib inline" + d +
					
					"# Standard Qiskit libraries" + s +
					"from qiskit import *" + s +
					"from qiskit.compiler import transpile, assemble" + s +
					"from qiskit.providers.ibmq import least_busy" + s +
					"from qiskit.visualization import *" + s +
					"from qiskit.tools.monitor import job_monitor" + s +
					"from qiskit.tools.jupyter import *" + d +
					
					"# Python maths" + s +
					"import numpy as np" + s +
					"from numpy import pi" + d +
					
					"# Plotting" + s +
					"from matplotlib import pyplot as plt" + d +
					
					"# Number of qubits" + s +
					"qubits = " + q + d +
					
					"# Load IBMQ account" + s +
					"provider = IBMQ.load_account()" + s +
					"simulator = provider.get_backend('ibmq_qasm_simulator')" + s +
					"device = provider.get_backend('" + (q > 5 ? "ibmq_16_melbourne" : "ibmq_santiago") + "')" + s +
					"filtered = provider.backends(filters=lambda x:" + s +
					"                             x.configuration().n_qubits >= qubits" + s +
					"                             and not x.configuration().simulator" + s +
					"                             and x.status().operational)" + s +
					"leastbusy = least_busy(filtered) if len(filtered) > 0 else device" + d +
					
					"# Choice of backend" + s +
					"qc_backend = " + (q > 16 ? "simulator" : "device") + d +
					
					"# Helper function" + s +
					"def run_job(circuit_, backend_, shots_ = 4096, opt_ = 1):" + s +
					"    print('Using {}'.format(backend_))" + s +
					"    job = execute(circuit_, backend = backend_, shots = shots_, optimization_level = opt_)" + s +
					"    job_monitor(job)" + s +
					"    return job.result()" + d +
					
					"# Construct circuit" + s +
					"qc = QuantumCircuit(qubits, qubits)" + d +
					
					codeString + s +
					
					"# Optimize circuit before running?" + s +
					"optimize = True" + d +
					
					"# Optimization" + s +
					"optimization = 1" + s +
					"if optimize:" + s +
					"    qc_cx = qc_depth = sys.maxsize" + s +
					"    for o in range(1, 4):" + s +
					"        qc_opt = transpile(qc, backend = qc_backend, seed_transpiler = " + Math.abs(rand.nextInt()) + ", optimization_level = o)" + s +
					"        if (qc_opt.count_ops().get('cx') < qc_cx" + s +
					"        or (qc_opt.count_ops().get('cx') == qc_cx and qc_opt.depth() <= qc_depth)):" + s +
					"            optimization = o" + s +
					"    print('Optimization level: {}'.format(optimization))" + d +
					
					"# Run circuit" + s +
					"result = run_job(qc, qc_backend, 4096, optimization)" + s +
					"counts = result.get_counts(qc)" + s +
					"hist = plot_histogram(counts)" + s +
					"print('\\n', counts)" + d +
					
					"# Printing results" + s +
					"# NOTE: only one diagram can be shown per Jupyter cell." + s +
					"# Either comment out all but one drawing/plotting method" + s +
					"# or move them into separate cells." + d +
					
					"# Draw circuit" + s +
					"# qc.draw()" + d +
					
					"# Plot results - only works in Jupyter" + s +
					"hist" + d +
					
					"# Save plot to file - won't work in IBM Q" + s +
					"# hist.savefig('counts.png')" + s;
			
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
