package nc.multiblock.quantum;

import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.quantum.*;
import nc.quantum.operator.Operator;
import nc.util.*;

import java.util.*;
import java.util.stream.Collectors;

public abstract class QuantumOperationWrapper {
	
	protected final QuantumComputer qc;
	
	public QuantumOperationWrapper(QuantumComputer qc) {
		this.qc = qc;
	}
	
	public abstract void run();
	
	public boolean shouldRefresh() {
		return false;
	}
	
	/**
	 * Adds the decomposition of this gate to the list.
	 */
	public abstract void addDecomposition(List<QuantumOperationWrapper> decomposition);
	
	public abstract List<String> getCode(int type);
	
	public interface IControl {
		
		int[] getControls();
		
		int[] getTargets();
		
		double[] getSingleMatrix();
		
		Operator getSingleOperator();
		
		QuantumOperationWrapper getWithoutControl();
	}
	
	public static class Measurement extends QuantumOperationWrapper {
		
		protected final int[] targets;
		
		public Measurement(QuantumComputer qc, int[] targets) {
			super(qc);
			this.targets = targets;
		}
		
		@Override
		public void run() {
			qc.measure(targets);
		}
		
		@Override
		public boolean shouldRefresh() {
			return true;
		}
		
		@Override
		public void addDecomposition(List<QuantumOperationWrapper> decomposition) {
			if (targets.length > 0) {
				decomposition.add(this);
			}
		}
		
		@Override
		public List<String> getCode(int type) {
			List<String> lines = new ArrayList<>();
			
			if (type == 0) {
				for (int target : targets) {
					lines.add("measure q[" + target + "] -> c[" + target + "];");
				}
			}
			else if (type == 1) {
				if (targets.length > 0) {
					String s = py(targets);
					lines.add("qc.measure(" + s + ", " + s + ")");
				}
			}
			
			return lines;
		}
	}
	
	public static class Reset extends QuantumOperationWrapper {
		
		public Reset(QuantumComputer qc) {
			super(qc);
		}
		
		@Override
		public void run() {
			qc.reset();
		}
		
		@Override
		public boolean shouldRefresh() {
			return true;
		}
		
		@Override
		public void addDecomposition(List<QuantumOperationWrapper> decomposition) {
			decomposition.add(this);
		}
		
		@Override
		public List<String> getCode(int type) {
			int qubits = qc.getQubitCount();
			List<String> lines = new ArrayList<>();
			
			if (type == 0) {
				for (int i = 0; i < qubits; ++i) {
					lines.add("reset q[" + i + "];");
				}
			}
			else if (type == 1) {
				if (qubits > 0) {
					lines.add("qc.reset(" + py(CollectionHelper.increasingArray(qubits)) + ")");
				}
			}
			
			return lines;
		}
	}
	
	public static abstract class Basic extends QuantumOperationWrapper {
		
		protected final int[] targets;
		
		public Basic(QuantumComputer qc, int[] targets) {
			super(qc);
			this.targets = targets;
		}
		
		@Override
		public void addDecomposition(List<QuantumOperationWrapper> decomposition) {
			if (targets.length > 0) {
				decomposition.add(this);
			}
		}
		
		@Override
		public List<String> getCode(int type) {
			List<String> lines = new ArrayList<>();
			
			if (type == 0) {
				for (int target : targets) {
					lines.add(getQasmLine(target));
				}
			}
			else if (type == 1) {
				if (targets.length > 0) {
					lines.add(getQiskitLine());
				}
			}
			
			return lines;
		}
		
		public abstract String getQasmLine(int i);
		
		public abstract String getQiskitLine();
	}
	
	public static class X extends Basic {
		
		public X(QuantumComputer qc, int[] targets) {
			super(qc, targets);
		}
		
		@Override
		public void run() {
			for (int target : targets) {
				qc.gate(new Gate(new int[] {target}, Consts.X));
			}
		}
		
		@Override
		public String getQasmLine(int i) {
			return "x q[" + i + "];";
		}
		
		@Override
		public String getQiskitLine() {
			return "qc.x(" + py(targets) + ")";
		}
	}
	
	public static class Y extends Basic {
		
		public Y(QuantumComputer qc, int[] targets) {
			super(qc, targets);
		}
		
		@Override
		public void run() {
			for (int target : targets) {
				qc.gate(new Gate(new int[] {target}, Consts.Y));
			}
		}
		
		@Override
		public String getQasmLine(int i) {
			return "y q[" + i + "];";
		}
		
		@Override
		public String getQiskitLine() {
			return "qc.y(" + py(targets) + ")";
		}
	}
	
	public static class Z extends Basic {
		
		public Z(QuantumComputer qc, int[] targets) {
			super(qc, targets);
		}
		
		@Override
		public void run() {
			for (int target : targets) {
				qc.gate(new Gate(new int[] {target}, Consts.Z));
			}
		}
		
		@Override
		public String getQasmLine(int i) {
			return "z q[" + i + "];";
		}
		
		@Override
		public String getQiskitLine() {
			return "qc.z(" + py(targets) + ")";
		}
	}
	
	public static class H extends Basic {
		
		public H(QuantumComputer qc, int[] targets) {
			super(qc, targets);
		}
		
		@Override
		public void run() {
			for (int target : targets) {
				qc.gate(new Gate(new int[] {target}, Consts.H));
			}
		}
		
		@Override
		public String getQasmLine(int i) {
			return "h q[" + i + "];";
		}
		
		@Override
		public String getQiskitLine() {
			return "qc.h(" + py(targets) + ")";
		}
	}
	
	public static class S extends Basic {
		
		public S(QuantumComputer qc, int[] targets) {
			super(qc, targets);
		}
		
		@Override
		public void run() {
			for (int target : targets) {
				qc.gate(new Gate(new int[] {target}, Consts.S));
			}
		}
		
		@Override
		public String getQasmLine(int i) {
			return "s q[" + i + "];";
		}
		
		@Override
		public String getQiskitLine() {
			return "qc.s(" + py(targets) + ")";
		}
	}
	
	public static class Sdg extends Basic {
		
		public Sdg(QuantumComputer qc, int[] targets) {
			super(qc, targets);
		}
		
		@Override
		public void run() {
			for (int target : targets) {
				qc.gate(new Gate(new int[] {target}, Consts.Sdg));
			}
		}
		
		@Override
		public String getQasmLine(int i) {
			return "sdg q[" + i + "];";
		}
		
		@Override
		public String getQiskitLine() {
			return "qc.sdg(" + py(targets) + ")";
		}
	}
	
	public static class T extends Basic {
		
		public T(QuantumComputer qc, int[] targets) {
			super(qc, targets);
		}
		
		@Override
		public void run() {
			for (int target : targets) {
				qc.gate(new Gate(new int[] {target}, Consts.T));
			}
		}
		
		@Override
		public String getQasmLine(int i) {
			return "t q[" + i + "];";
		}
		
		@Override
		public String getQiskitLine() {
			return "qc.t(" + py(targets) + ")";
		}
	}
	
	public static class Tdg extends Basic {
		
		public Tdg(QuantumComputer qc, int[] targets) {
			super(qc, targets);
		}
		
		@Override
		public void run() {
			for (int target : targets) {
				qc.gate(new Gate(new int[] {target}, Consts.Tdg));
			}
		}
		
		@Override
		public String getQasmLine(int i) {
			return "tdg q[" + i + "];";
		}
		
		@Override
		public String getQiskitLine() {
			return "qc.tdg(" + py(targets) + ")";
		}
	}
	
	public static abstract class BasicAngle extends QuantumOperationWrapper {
		
		protected final double angle;
		protected final int[] targets;
		
		public BasicAngle(QuantumComputer qc, double angle, int[] targets) {
			super(qc);
			this.angle = angle;
			this.targets = targets;
		}
		
		@Override
		public List<String> getCode(int type) {
			List<String> lines = new ArrayList<>();
			
			if (type == 0) {
				for (int target : targets) {
					lines.add(getQasmLine(target));
				}
			}
			else if (type == 1) {
				if (targets.length > 0) {
					lines.add(getQiskitLine());
				}
			}
			
			return lines;
		}
		
		@Override
		public void addDecomposition(List<QuantumOperationWrapper> decomposition) {
			if (targets.length > 0) {
				decomposition.add(this);
			}
		}
		
		public abstract String getQasmLine(int i);
		
		public abstract String getQiskitLine();
	}
	
	public static class P extends BasicAngle {
		
		public P(QuantumComputer qc, double angle, int[] targets) {
			super(qc, angle, targets);
		}
		
		@Override
		public void run() {
			for (int target : targets) {
				qc.gate(new Gate(new int[] {target}, Operator.phase(Math.toRadians(angle))));
			}
		}
		
		@Override
		public String getQasmLine(int i) {
			return "p(" + Math.toRadians(angle) + ") q[" + i + "];";
		}
		
		@Override
		public String getQiskitLine() {
			return "qc.p(" + Math.toRadians(angle) + ", " + py(targets) + ")";
		}
	}
	
	public static class RX extends BasicAngle {
		
		public RX(QuantumComputer qc, double angle, int[] targets) {
			super(qc, angle, targets);
		}
		
		@Override
		public void run() {
			for (int target : targets) {
				qc.gate(new Gate(new int[] {target}, Operator.rotateX(Math.toRadians(angle))));
			}
		}
		
		@Override
		public String getQasmLine(int i) {
			return "rx(" + Math.toRadians(angle) + ") q[" + i + "];";
		}
		
		@Override
		public String getQiskitLine() {
			return "qc.rx(" + Math.toRadians(angle) + ", " + py(targets) + ")";
		}
	}
	
	public static class RY extends BasicAngle {
		
		public RY(QuantumComputer qc, double angle, int[] targets) {
			super(qc, angle, targets);
		}
		
		@Override
		public void run() {
			for (int target : targets) {
				qc.gate(new Gate(new int[] {target}, Operator.rotateY(Math.toRadians(angle))));
			}
		}
		
		@Override
		public String getQasmLine(int i) {
			return "ry(" + Math.toRadians(angle) + ") q[" + i + "];";
		}
		
		@Override
		public String getQiskitLine() {
			return "qc.ry(" + Math.toRadians(angle) + ", " + py(targets) + ")";
		}
	}
	
	public static class RZ extends BasicAngle {
		
		public RZ(QuantumComputer qc, double angle, int[] targets) {
			super(qc, angle, targets);
		}
		
		@Override
		public void run() {
			for (int target : targets) {
				qc.gate(new Gate(new int[] {target}, Operator.rotateZ(Math.toRadians(angle))));
			}
		}
		
		@Override
		public String getQasmLine(int i) {
			return "rz(" + Math.toRadians(angle) + ") q[" + i + "];";
		}
		
		@Override
		public String getQiskitLine() {
			return "qc.rz(" + Math.toRadians(angle) + ", " + py(targets) + ")";
		}
	}
	
	public static abstract class Control extends QuantumOperationWrapper implements IControl {
		
		protected final int[] controls, targets;
		
		public Control(QuantumComputer qc, int[] controls, int[] targets) {
			super(qc);
			this.controls = controls;
			this.targets = targets;
		}
		
		@Override
		public void run() {
			for (int target : targets) {
				qc.gate(new Gate(controls, new int[] {target}, getSingleOperator()));
			}
		}
		
		@Override
		public int[] getControls() {
			return controls;
		}
		
		@Override
		public int[] getTargets() {
			return targets;
		}
		
		@Override
		public void addDecomposition(List<QuantumOperationWrapper> decomposition) {
			if (controls.length == 1) {
				if (targets.length > 0) {
					decomposition.add(this);
				}
			}
			else {
				addZYZDecomposition(this, decomposition);
			}
		}
		
		@Override
		public List<String> getCode(int type) {
			if (controls.length == 0) {
				return getWithoutControl().getCode(type);
			}
			else if (controls.length == 1) {
				return getSingleControlCode(type);
			}
			
			List<QuantumOperationWrapper> decomposition = new ArrayList<>();
			addDecomposition(decomposition);
			
			return StreamHelper.flatMap(decomposition, x -> x.getCode(type));
		}
		
		public String getQasmLine(int i) {
			return ";";
		}
		
		public String getQiskitLine() {
			return "";
		}
		
		public List<String> getSingleControlCode(int type) {
			int control = controls[0];
			List<String> lines = new ArrayList<>();
			
			if (type == 0) {
				for (int target : targets) {
					lines.add(getSingleControlQasmLine(control, target));
				}
			}
			else if (type == 1) {
				if (targets.length > 0) {
					lines.add(getSingleControlQiskitLine(control));
				}
			}
			
			return lines;
		}
		
		public abstract String getSingleControlQasmLine(int control, int i);
		
		public abstract String getSingleControlQiskitLine(int control);
	}
	
	public static class CX extends Control {
		
		public CX(QuantumComputer qc, int[] controls, int[] targets) {
			super(qc, controls, targets);
		}
		
		@Override
		public double[] getSingleMatrix() {
			return Matrix.X;
		}
		
		@Override
		public Operator getSingleOperator() {
			return Consts.X;
		}
		
		@Override
		public QuantumOperationWrapper getWithoutControl() {
			return new X(qc, targets);
		}
		
		@Override
		public void addDecomposition(List<QuantumOperationWrapper> decomposition) {
			if (targets.length == 0) {
				return;
			}
			
			else if (controls.length == 0) {
				decomposition.add(getWithoutControl());
				return;
			}
			
			int controlCount = controls.length;
			if (controlCount == 1 || controlCount == 2) {
				decomposition.add(this);
				return;
			}
			
			int q = qc.getQubitCount(), h = (int) Math.ceil(q / 2D);
			boolean toffoli = q >= 5 && controlCount >= 3 && controlCount <= h;
			boolean four_cx = q >= controlCount + 2 && controlCount >= 3;
			
			// https://arxiv.org/abs/quant-ph/9503016
			if (toffoli || four_cx) {
				IntSet c_set = new IntOpenHashSet(controls);
				for (int target : targets) {
					if (toffoli) {
						IntList anc = new IntArrayList();
						for (int i = 0; i < q; ++i) {
							if (i != target && !c_set.contains(i)) {
								anc.add(i);
							}
						}
						
						CX[] repeat = new CX[2 * (controlCount - 2)];
						
						repeat[0] = new CX(qc, new int[] {anc.getInt(0), controls[0]}, new int[] {target});
						for (int i = 1; i < controlCount - 2; ++i) {
							repeat[i] = new CX(qc, new int[] {anc.getInt(i), controls[i]}, new int[] {anc.getInt(i - 1)});
							repeat[2 * (controlCount - 2) - i] = repeat[i];
						}
						repeat[controlCount - 2] = new CX(qc, new int[] {controls[controlCount - 2], controls[controlCount - 1]}, new int[] {anc.getInt(controlCount - 3)});
						
						decomposition.addAll(Arrays.asList(repeat));
						decomposition.addAll(Arrays.asList(repeat));
					}
					
					else {
						int anc = -1;
						for (int i = 0; i < q; ++i) {
							if (i != target && !c_set.contains(i)) {
								anc = i;
								break;
							}
						}
						
						IntList c_list = new IntArrayList(controls);
						int m0 = (int) Math.ceil((controlCount + 1D) / 2D), m1 = controlCount - m0 + 1;
						
						IntSet c0 = new IntRBTreeSet(c_list.subList(m1 - 1, controlCount));
						CX cx0 = new CX(qc, c0.toIntArray(), new int[] {anc});
						
						IntSet c1 = new IntRBTreeSet(c_list.subList(0, m1 - 1));
						c1.add(anc);
						CX cx1 = new CX(qc, c1.toIntArray(), new int[] {target});
						
						decomposition.add(cx0);
						decomposition.add(cx1);
						decomposition.add(cx0);
						decomposition.add(cx1);
					}
				}
			}
			else {
				addZYZDecomposition(this, decomposition);
			}
		}
		
		@Override
		public List<String> getCode(int type) {
			int len = controls.length;
			if (len == 0) {
				return getWithoutControl().getCode(type);
			}
			else if (len == 1) {
				return getSingleControlCode(type);
			}
			else if (len == 2) {
				return getDoubleControlCode(type);
			}
			
			List<String> lines = new ArrayList<>();
			List<QuantumOperationWrapper> decomposition = new ArrayList<>();
			
			addDecomposition(decomposition);
			for (QuantumOperationWrapper gate : decomposition) {
				lines.addAll(gate.getCode(type));
			}
			return lines;
		}
		
		@Override
		public String getSingleControlQasmLine(int control, int i) {
			return "cx q[" + control + "], q[" + i + "];";
		}
		
		@Override
		public String getSingleControlQiskitLine(int control) {
			return "qc.cx(" + control + ", " + py(targets) + ")";
		}
		
		public List<String> getDoubleControlCode(int type) {
			int c0 = controls[0], c1 = controls[1];
			
			List<String> lines = new ArrayList<>();
			
			if (type == 0) {
				for (int target : targets) {
					lines.add(getDoubleControlQasmLine(c0, c1, target));
				}
			}
			else if (type == 1) {
				if (targets.length > 0) {
					lines.add(getDoubleControlQiskitLine(c0, c1));
				}
			}
			
			return lines;
		}
		
		public String getDoubleControlQasmLine(int c0, int c1, int i) {
			return "ccx q[" + c0 + "], q[" + c1 + "], q[" + i + "];";
		}
		
		public String getDoubleControlQiskitLine(int c0, int c1) {
			return "qc.ccx(" + c0 + ", " + c1 + ", " + py(targets) + ")";
		}
	}
	
	public static class CY extends Control {
		
		public CY(QuantumComputer qc, int[] controls, int[] targets) {
			super(qc, controls, targets);
		}
		
		@Override
		public double[] getSingleMatrix() {
			return Matrix.Y;
		}
		
		@Override
		public Operator getSingleOperator() {
			return Consts.Y;
		}
		
		@Override
		public QuantumOperationWrapper getWithoutControl() {
			return new Y(qc, targets);
		}
		
		@Override
		public String getSingleControlQasmLine(int control, int i) {
			return "cy q[" + control + "], q[" + i + "];";
		}
		
		@Override
		public String getSingleControlQiskitLine(int control) {
			return "qc.cy(" + control + ", " + py(targets) + ")";
		}
	}
	
	public static class CZ extends Control {
		
		public CZ(QuantumComputer qc, int[] controls, int[] targets) {
			super(qc, controls, targets);
		}
		
		@Override
		public double[] getSingleMatrix() {
			return Matrix.Z;
		}
		
		@Override
		public Operator getSingleOperator() {
			return Consts.Z;
		}
		
		@Override
		public QuantumOperationWrapper getWithoutControl() {
			return new Z(qc, targets);
		}
		
		@Override
		public String getSingleControlQasmLine(int control, int i) {
			return "cz q[" + control + "], q[" + i + "];";
		}
		
		@Override
		public String getSingleControlQiskitLine(int control) {
			return "qc.cz(" + control + ", " + py(targets) + ")";
		}
	}
	
	public static class CH extends Control {
		
		public CH(QuantumComputer qc, int[] controls, int[] targets) {
			super(qc, controls, targets);
		}
		
		@Override
		public double[] getSingleMatrix() {
			return Matrix.H;
		}
		
		@Override
		public Operator getSingleOperator() {
			return Consts.H;
		}
		
		@Override
		public QuantumOperationWrapper getWithoutControl() {
			return new H(qc, targets);
		}
		
		@Override
		public String getSingleControlQasmLine(int control, int i) {
			return "ch q[" + control + "], q[" + i + "];";
		}
		
		@Override
		public String getSingleControlQiskitLine(int control) {
			return "qc.ch(" + control + ", " + py(targets) + ")";
		}
	}
	
	public static class CS extends Control {
		
		public CS(QuantumComputer qc, int[] controls, int[] targets) {
			super(qc, controls, targets);
		}
		
		@Override
		public double[] getSingleMatrix() {
			return Matrix.S;
		}
		
		@Override
		public Operator getSingleOperator() {
			return Consts.S;
		}
		
		@Override
		public QuantumOperationWrapper getWithoutControl() {
			return new S(qc, targets);
		}
		
		@Override
		public String getSingleControlQasmLine(int control, int i) {
			return "cp(pi/2) q[" + control + "], q[" + i + "];";
		}
		
		@Override
		public String getSingleControlQiskitLine(int control) {
			return "qc.cp(pi/2, " + control + ", " + py(targets) + ")";
		}
	}
	
	public static class CSdg extends Control {
		
		public CSdg(QuantumComputer qc, int[] controls, int[] targets) {
			super(qc, controls, targets);
		}
		
		@Override
		public double[] getSingleMatrix() {
			return Matrix.Sdg;
		}
		
		@Override
		public Operator getSingleOperator() {
			return Consts.Sdg;
		}
		
		@Override
		public QuantumOperationWrapper getWithoutControl() {
			return new Sdg(qc, targets);
		}
		
		@Override
		public String getSingleControlQasmLine(int control, int i) {
			return "cp(-pi/2) q[" + control + "], q[" + i + "];";
		}
		
		@Override
		public String getSingleControlQiskitLine(int control) {
			return "qc.cp(-pi/2, " + control + ", " + py(targets) + ")";
		}
	}
	
	public static class CT extends Control {
		
		public CT(QuantumComputer qc, int[] controls, int[] targets) {
			super(qc, controls, targets);
		}
		
		@Override
		public double[] getSingleMatrix() {
			return Matrix.T;
		}
		
		@Override
		public Operator getSingleOperator() {
			return Consts.T;
		}
		
		@Override
		public QuantumOperationWrapper getWithoutControl() {
			return new T(qc, targets);
		}
		
		@Override
		public String getSingleControlQasmLine(int control, int i) {
			return "cp(pi/4) q[" + control + "], q[" + i + "];";
		}
		
		@Override
		public String getSingleControlQiskitLine(int control) {
			return "qc.cp(pi/4, " + control + ", " + py(targets) + ")";
		}
	}
	
	public static class CTdg extends Control {
		
		public CTdg(QuantumComputer qc, int[] controls, int[] targets) {
			super(qc, controls, targets);
		}
		
		@Override
		public double[] getSingleMatrix() {
			return Matrix.Tdg;
		}
		
		@Override
		public Operator getSingleOperator() {
			return Consts.Tdg;
		}
		
		@Override
		public QuantumOperationWrapper getWithoutControl() {
			return new Tdg(qc, targets);
		}
		
		@Override
		public String getSingleControlQasmLine(int control, int i) {
			return "cp(-pi/4) q[" + control + "], q[" + i + "];";
		}
		
		@Override
		public String getSingleControlQiskitLine(int control) {
			return "qc.cp(-pi/4, " + control + ", " + py(targets) + ")";
		}
	}
	
	public static abstract class ControlAngle extends QuantumOperationWrapper implements IControl {
		
		protected final double angle;
		protected final int[] controls, targets;
		
		public ControlAngle(QuantumComputer qc, double angle, int[] controls, int[] targets) {
			super(qc);
			this.angle = angle;
			this.controls = controls;
			this.targets = targets;
		}
		
		@Override
		public void run() {
			for (int target : targets) {
				qc.gate(new Gate(controls, new int[] {target}, getSingleOperator()));
			}
		}
		
		@Override
		public int[] getControls() {
			return controls;
		}
		
		@Override
		public int[] getTargets() {
			return targets;
		}
		
		@Override
		public void addDecomposition(List<QuantumOperationWrapper> decomposition) {
			if (controls.length == 1) {
				if (targets.length > 0) {
					decomposition.add(this);
				}
			}
			else {
				addZYZDecomposition(this, decomposition);
			}
		}
		
		@Override
		public List<String> getCode(int type) {
			if (controls.length == 0) {
				return getWithoutControl().getCode(type);
			}
			else if (controls.length == 1) {
				return singleControlCode(type);
			}
			
			List<QuantumOperationWrapper> decomposition = new ArrayList<>();
			addDecomposition(decomposition);
			
			List<String> lines = new ArrayList<>();
			for (QuantumOperationWrapper gate : decomposition) {
				lines.addAll(gate.getCode(type));
			}
			
			return lines;
		}
		
		public String getQasmLine(int i) {
			return ";";
		}
		
		public String getQiskitLine() {
			return "";
		}
		
		public List<String> singleControlCode(int type) {
			int control = controls[0];
			
			List<String> lines = new ArrayList<>();
			
			if (type == 0) {
				for (int target : targets) {
					lines.add(getSingleControlQasmLine(control, target));
				}
			}
			else if (type == 1) {
				if (targets.length > 0) {
					lines.add(getSingleControlQiskitLine(control));
				}
			}
			
			return lines;
		}
		
		public abstract String getSingleControlQasmLine(int control, int i);
		
		public abstract String getSingleControlQiskitLine(int control);
	}
	
	public static class CP extends ControlAngle {
		
		public CP(QuantumComputer qc, double angle, int[] controls, int[] targets) {
			super(qc, angle, controls, targets);
		}
		
		@Override
		public double[] getSingleMatrix() {
			return Matrix.phase(Math.toRadians(angle));
		}
		
		@Override
		public Operator getSingleOperator() {
			return Operator.phase(Math.toRadians(angle));
		}
		
		@Override
		public QuantumOperationWrapper getWithoutControl() {
			return new P(qc, angle, targets);
		}
		
		@Override
		public String getSingleControlQasmLine(int control, int i) {
			return "cp(" + Math.toRadians(angle) + ") q[" + control + "], q[" + i + "];";
		}
		
		@Override
		public String getSingleControlQiskitLine(int control) {
			return "qc.cp(" + Math.toRadians(angle) + ", " + control + ", " + py(targets) + ")";
		}
	}
	
	public static class CRX extends ControlAngle {
		
		public CRX(QuantumComputer qc, double angle, int[] controls, int[] targets) {
			super(qc, angle, controls, targets);
		}
		
		@Override
		public double[] getSingleMatrix() {
			return Matrix.rotateX(Math.toRadians(angle));
		}
		
		@Override
		public Operator getSingleOperator() {
			return Operator.rotateX(Math.toRadians(angle));
		}
		
		@Override
		public QuantumOperationWrapper getWithoutControl() {
			return new RX(qc, angle, targets);
		}
		
		@Override
		public String getSingleControlQasmLine(int control, int i) {
			return "crx(" + Math.toRadians(angle) + ") q[" + control + "], q[" + i + "];";
		}
		
		@Override
		public String getSingleControlQiskitLine(int control) {
			return "qc.crx(" + Math.toRadians(angle) + ", " + control + ", " + py(targets) + ")";
		}
	}
	
	public static class CRY extends ControlAngle {
		
		public CRY(QuantumComputer qc, double angle, int[] controls, int[] targets) {
			super(qc, angle, controls, targets);
		}
		
		@Override
		public double[] getSingleMatrix() {
			return Matrix.rotateY(Math.toRadians(angle));
		}
		
		@Override
		public Operator getSingleOperator() {
			return Operator.rotateY(Math.toRadians(angle));
		}
		
		@Override
		public QuantumOperationWrapper getWithoutControl() {
			return new RY(qc, angle, targets);
		}
		
		@Override
		public String getSingleControlQasmLine(int control, int i) {
			return "cry(" + Math.toRadians(angle) + ") q[" + control + "], q[" + i + "];";
		}
		
		@Override
		public String getSingleControlQiskitLine(int control) {
			return "qc.cry(" + Math.toRadians(angle) + ", " + control + ", " + py(targets) + ")";
		}
	}
	
	public static class CRZ extends ControlAngle {
		
		public CRZ(QuantumComputer qc, double angle, int[] controls, int[] targets) {
			super(qc, angle, controls, targets);
		}
		
		@Override
		public double[] getSingleMatrix() {
			return Matrix.rotateZ(Math.toRadians(angle));
		}
		
		@Override
		public Operator getSingleOperator() {
			return Operator.rotateZ(Math.toRadians(angle));
		}
		
		@Override
		public QuantumOperationWrapper getWithoutControl() {
			return new RZ(qc, angle, targets);
		}
		
		@Override
		public String getSingleControlQasmLine(int control, int i) {
			return "crz(" + Math.toRadians(angle) + ") q[" + control + "], q[" + i + "];";
		}
		
		@Override
		public String getSingleControlQiskitLine(int control) {
			return "qc.crz(" + Math.toRadians(angle) + ", " + control + ", " + py(targets) + ")";
		}
	}
	
	public static class Swap extends QuantumOperationWrapper {
		
		protected final int[] from, to;
		
		public Swap(QuantumComputer qc, int[] from, int[] to) {
			super(qc);
			this.from = from;
			this.to = to;
		}
		
		@Override
		public void run() {
			int len = from.length;
			if (len == to.length) {
				for (int i = 0; i < len; ++i) {
					qc.gate(new Gate(new int[] {from[i], to[i]}, Consts.SWAP));
				}
			}
		}
		
		@Override
		public void addDecomposition(List<QuantumOperationWrapper> decomposition) {
			if (from.length > 0 && from.length == to.length) {
				decomposition.add(this);
			}
		}
		
		@Override
		public List<String> getCode(int type) {
			List<String> lines = new ArrayList<>();
			
			int len = from.length;
			if (len == to.length) {
				if (type == 0) {
					for (int i = 0; i < len; ++i) {
						lines.add("swap q[" + from[i] + "], q[" + to[i] + "];");
					}
				}
				else if (type == 1) {
					for (int i = 0; i < len; ++i) {
						lines.add("qc.swap(" + from[i] + ", " + to[i] + ")");
					}
				}
			}
			
			return lines;
		}
	}
	
	public static class ControlSwap extends QuantumOperationWrapper implements IControl {
		
		protected final int[] controls;
		protected final int[] from, to;
		
		public ControlSwap(QuantumComputer qc, int[] controls, int[] from, int[] to) {
			super(qc);
			this.controls = controls;
			this.from = from;
			this.to = to;
		}
		
		@Override
		public int[] getControls() {
			return controls;
		}
		
		@Override
		public int[] getTargets() {
			return null;
		}
		
		@Override
		public void run() {
			int len = from.length;
			if (len == to.length) {
				for (int i = 0; i < len; ++i) {
					qc.gate(new Gate(controls, new int[] {from[i], to[i]}, Consts.SWAP));
				}
			}
		}
		
		@Override
		public double[] getSingleMatrix() {
			return null;
		}
		
		@Override
		public Operator getSingleOperator() {
			return null;
		}
		
		@Override
		public QuantumOperationWrapper getWithoutControl() {
			return new Swap(qc, from, to);
		}
		
		@Override
		public void addDecomposition(List<QuantumOperationWrapper> decomposition) {
			int len = from.length;
			if (len > 0 && len == to.length) {
				if (controls.length == 0) {
					decomposition.add(getWithoutControl());
				}
				else if (controls.length == 1) {
					decomposition.add(this);
				}
				else {
					for (int i = 0; i < len; ++i) {
						IntSet c0 = new IntRBTreeSet(controls);
						c0.add(from[i]);
						CX cx0 = new CX(qc, c0.toIntArray(), new int[] {to[i]});
						
						IntSet c1 = new IntRBTreeSet(controls);
						c1.add(to[i]);
						CX cx1 = new CX(qc, c1.toIntArray(), new int[] {from[i]});
						
						addZYZDecomposition(cx0, decomposition);
						addZYZDecomposition(cx1, decomposition);
						addZYZDecomposition(cx0, decomposition);
					}
				}
			}
		}
		
		@Override
		public List<String> getCode(int type) {
			if (controls.length == 0) {
				return getWithoutControl().getCode(type);
			}
			
			List<String> lines = new ArrayList<>();
			
			if (from.length > 0 && from.length == to.length) {
				if (controls.length == 1) {
					if (type == 0) {
						String s = "cswap q[" + controls[0] + "], ";
						for (int i = 0; i < from.length; ++i) {
							lines.add(s + "q[" + from[i] + "], q[" + to[i] + "];");
						}
					}
					else if (type == 1) {
						String s = "qc.cswap(" + controls[0] + ", ";
						for (int i = 0; i < from.length; ++i) {
							lines.add(s + from[i] + ", " + to[i] + ")");
						}
					}
				}
				else {
					List<QuantumOperationWrapper> decomposition = new ArrayList<>();
					addDecomposition(decomposition);
					
					for (QuantumOperationWrapper gate : decomposition) {
						lines.addAll(gate.getCode(type));
					}
				}
			}
			
			return lines;
		}
	}
	
	// Static
	
	public static int[] arr(IntCollection ints) {
		int[] arr = ints.toIntArray();
		Arrays.sort(arr);
		return arr;
	}
	
	public static String intsToString(IntCollection ints) {
		return Arrays.toString(ints.toIntArray());
	}
	
	// Helper Methods
	
	public static boolean matchingID(String id, String[] arr) {
		for (String s : arr) {
			if (id.equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean matchingControl(IntSet c0, IntSet c1) {
		if (c0.size() != c1.size()) {
			return false;
		}
		for (int i : c0) {
			if (!c1.contains(i)) {
				return false;
			}
		}
		return true;
	}
	
	public static final Object2ObjectMap<double[], double[]> ZYZ_DECOMPOSITION_ANGLES_CACHE = new Object2ObjectOpenHashMap<>();
	
	static {
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(Matrix.I, new double[] {0D, 0D, 0D, 0D});
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(Matrix.X, new double[] {90D, -90D, 180D, 90D});
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(Matrix.Y, new double[] {90D, 0D, 180D, 0D});
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(Matrix.Z, new double[] {90D, 90D, 0D, 90D});
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(Matrix.H, new double[] {90D, 0D, 90D, 180D});
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(Matrix.S, new double[] {45D, 45D, 0D, 45D});
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(Matrix.Sdg, new double[] {-45D, -45D, 0D, -45D});
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(Matrix.T, new double[] {22.5D, 22.5D, 0D, 22.5D});
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(Matrix.Tdg, new double[] {-22.5D, -22.5D, 0D, -22.5D});
	}
	
	/**
	 * Returns the phase and Euler angles for the gate in the ZYZ basis in degrees. Translated from <a href="https://qiskit.org/documentation/_modules/qiskit/quantum_info/synthesis/one_qubit_decompose.html#OneQubitEulerDecomposer">...</a>
	 */
	public static double[] getZYZDecompositionAngles(double[] matrix) {
		if (ZYZ_DECOMPOSITION_ANGLES_CACHE.containsKey(matrix)) {
			return ZYZ_DECOMPOSITION_ANGLES_CACHE.get(matrix);
		}
		
		Complex det = Matrix.det(matrix);
		Complex phase = Complex.invSqrt(det.re, det.im);
		
		double[] m = DoubleArrays.copy(matrix);
		Matrix.multiplyBy(m, phase.re, phase.im);
		
		double ppl = 2D * Complex.arg(m[6], m[7]);
		double pml = 2D * Complex.arg(m[4], m[5]);
		
		return new double[] {-Math.toDegrees(phase.arg()), Math.toDegrees((ppl + pml) / 2D), Math.toDegrees(2D * Math.atan2(Complex.abs(m[4], m[5]), Complex.abs(m[0], m[1]))), Math.toDegrees((ppl - pml) / 2D)};
	}
	
	/**
	 * Adds the ZYZ decomposition of this gate to the list. Combines results from: <a href="https://arxiv.org/abs/quant-ph/9503016">...</a>, Nielsen, Michael A.; Chuang, Isaac L. Quantum Computation and Quantum Information
	 */
	public static <GATE extends QuantumOperationWrapper & IControl> void addZYZDecomposition(GATE gate, List<QuantumOperationWrapper> decomposition) {
		int[] targets = gate.getTargets();
		if (targets.length == 0) {
			return;
		}
		
		int[] controls = gate.getControls();
		if (controls.length == 0) {
			decomposition.add(gate.getWithoutControl());
			return;
		}
		
		double[] azyz = getZYZDecompositionAngles(gate.getSingleMatrix());
		double alpha = azyz[0], beta = azyz[1], hgam = azyz[2] / 2D, hbpd = (azyz[1] + azyz[3]) / 2D, hdmb = (azyz[3] - azyz[1]) / 2D;
		boolean hgam_f = full(hgam), hbpd_f = full(hbpd);
		
		if (controls.length == 1) {
			if (!full(hdmb)) {
				new RZ(gate.qc, hdmb, targets).addDecomposition(decomposition);
			}
			if (!hbpd_f || !hgam_f) {
				new CX(gate.qc, controls, targets).addDecomposition(decomposition);
				if (!hbpd_f) {
					new RZ(gate.qc, -hbpd, targets).addDecomposition(decomposition);
				}
				if (!hgam_f) {
					new RY(gate.qc, -hgam, targets).addDecomposition(decomposition);
				}
				new CX(gate.qc, controls, targets).addDecomposition(decomposition);
			}
			if (!hgam_f) {
				new RY(gate.qc, hgam, targets).addDecomposition(decomposition);
			}
			if (!full(beta)) {
				new RZ(gate.qc, beta, targets).addDecomposition(decomposition);
			}
			if (!full(alpha)) {
				new P(gate.qc, alpha, controls).addDecomposition(decomposition);
			}
		}
		else {
			int c0 = controls[0];
			int[] c_rot = new int[] {c0};
			IntSet c_cx_set = new IntRBTreeSet(controls);
			c_cx_set.rem(c0);
			int[] c_cx = c_cx_set.toIntArray();
			
			if (!full(hdmb)) {
				new CRZ(gate.qc, hdmb, c_rot, targets).addDecomposition(decomposition);
			}
			if (!hbpd_f || !hgam_f) {
				new CX(gate.qc, c_cx, targets).addDecomposition(decomposition);
				if (!hbpd_f) {
					new CRZ(gate.qc, -hbpd, c_rot, targets).addDecomposition(decomposition);
				}
				if (!hgam_f) {
					new CRY(gate.qc, -hgam, c_rot, targets).addDecomposition(decomposition);
				}
				new CX(gate.qc, c_cx, targets).addDecomposition(decomposition);
			}
			if (!hgam_f) {
				new CRY(gate.qc, hgam, c_rot, targets).addDecomposition(decomposition);
			}
			if (!full(beta)) {
				new CRZ(gate.qc, beta, c_rot, targets).addDecomposition(decomposition);
			}
			if (!full(alpha)) {
				new CP(gate.qc, alpha, c_cx, c_rot).addDecomposition(decomposition);
			}
		}
	}
	
	public static boolean full(double angle) {
		return angle % 720D == 0D;
	}
	
	public static String py(int[] ints) {
		if (ints.length == 1) {
			return Integer.toString(ints[0]);
		}
		return Arrays.stream(ints).mapToObj(Integer::toString).collect(Collectors.joining(", ", "(", ")"));
	}
}
