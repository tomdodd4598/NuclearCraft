package nc.multiblock.qComputer;

import java.util.*;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.util.*;

public abstract class QuantumGate<GATE extends QuantumGate<?>> {
	
	protected final QuantumComputer qc;
	protected final Class<GATE> gateClass;
	
	public QuantumGate(QuantumComputer qc, Class<GATE> gateClass) {
		this.qc = qc;
		this.gateClass = gateClass;
	}
	
	public abstract String getID();
	
	public abstract String[] mergerIDs();
	
	public abstract void run();
	
	public final QuantumGate<?> merge(QuantumGate<?> next) {
		if (gateClass.isInstance(next) && matchingID(next.getID(), mergerIDs())) {
			return mergeInernal(gateClass.cast(next));
		}
		else {
			return null;
		}
	}
	
	/** Returns null if next gate can not be merged with this one */
	public abstract GATE mergeInernal(GATE next);
	
	public boolean shouldMarkDirty() {
		return false;
	}
	
	public abstract ComplexMatrix singleQubitOperation();
	
	/** Adds the required decomposition of this gate to the list. */
	public abstract void addRequiredDecomposition(List<QuantumGate<?>> decomposition);
	
	public abstract List<String> getCode(int type);
	
	public static interface IControl {
		
		public IntSet c();
		
		public IntSet t();
		
		public QuantumGate<?> withoutControl();
	}
	
	public static class Measurement extends QuantumGate<Measurement> {
		
		private static final String[] ID = new String[] {"measure"};
		
		protected final IntSet n;
		
		public Measurement(QuantumComputer qc, IntSet n) {
			super(qc, Measurement.class);
			this.n = n;
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.measure(n);
		}
		
		@Override
		public Measurement mergeInernal(Measurement next) {
			IntSet nCopy = new IntOpenHashSet(n);
			nCopy.addAll(next.n);
			return new Measurement(qc, nCopy);
		}
		
		@Override
		public boolean shouldMarkDirty() {
			return true;
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return null;
		}
		
		@Override
		public void addRequiredDecomposition(List<QuantumGate<?>> decomposition) {
			if (!n.isEmpty()) {
				decomposition.add(this);
			}
		}
		
		@Override
		public List<String> getCode(int type) {
			IntList l = list(n);
			List<String> out = new ArrayList<>();
			
			if (type == 0) {
				for (int i = 0; i < l.size(); ++i) {
					out.add("measure q[" + l.getInt(i) + "] -> c[" + l.getInt(i) + "];");
				}
			}
			else if (type == 1) {
				if (!l.isEmpty()) {
					String s = pythonArray(l);
					// out.add("qc.barrier(" + s + ")");
					out.add("qc.measure(" + s + ", " + s + ")");
				}
			}
			
			return out;
		}
	}
	
	public static class Reset extends QuantumGate<Reset> {
		
		private static final String[] ID = new String[] {"reset"};
		
		public Reset(QuantumComputer qc) {
			super(qc, Reset.class);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.refreshState(true);
		}
		
		@Override
		public Reset mergeInernal(Reset next) {
			return new Reset(qc);
		}
		
		@Override
		public boolean shouldMarkDirty() {
			return true;
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return null;
		}
		
		@Override
		public void addRequiredDecomposition(List<QuantumGate<?>> decomposition) {
			decomposition.add(this);
		}
		
		@Override
		public List<String> getCode(int type) {
			int q = qc.qubitCount();
			List<String> out = new ArrayList<>();
			
			if (type == 0) {
				for (int i = 0; i < q; ++i) {
					out.add("reset q[" + i + "];");
				}
			}
			else if (type == 1) {
				if (q != 0) {
					String s = pythonArray(CollectionHelper.increasingList(q));
					// out.add("qc.barrier(" + s + ")");
					out.add("qc.reset(" + s + ")");
				}
			}
			
			return out;
		}
	}
	
	public static abstract class Basic extends QuantumGate<Basic> {
		
		protected final IntSet n;
		
		public Basic(QuantumComputer qc, IntSet n) {
			super(qc, Basic.class);
			this.n = n;
		}
		
		@Override
		public Basic mergeInernal(Basic next) {
			if (next instanceof Control && !((Control) next).c.isEmpty()) {
				return null;
			}
			
			for (int i : n) {
				if (next.n.contains(i)) {
					return null;
				}
			}
			
			IntSet nCopy = new IntOpenHashSet(n);
			nCopy.addAll(next.n);
			return newMerged(S0, nCopy);
		}
		
		public abstract Basic newMerged(IntSet cIn, IntSet tIn);
		
		@Override
		public void addRequiredDecomposition(List<QuantumGate<?>> decomposition) {
			if (!n.isEmpty()) {
				decomposition.add(this);
			}
		}
		
		@Override
		public List<String> getCode(int type) {
			IntList l = list(n);
			List<String> out = new ArrayList<>();
			
			if (type == 0) {
				for (int i = 0; i < l.size(); ++i) {
					out.add(qasmLine(l.getInt(i)));
				}
			}
			else if (type == 1) {
				if (!l.isEmpty()) {
					out.add(qiskitLine(l));
				}
			}
			
			return out;
		}
		
		public abstract String qasmLine(int i);
		
		public abstract String qiskitLine(IntList l);
	}
	
	public static class X extends Basic {
		
		private static final String[] ID = new String[] {"x", "cx"};
		
		public X(QuantumComputer qc, IntSet n) {
			super(qc, n);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.x(n);
		}
		
		@Override
		public Basic newMerged(IntSet cIn, IntSet tIn) {
			return new X(qc, tIn);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return X;
		}
		
		@Override
		public String qasmLine(int i) {
			return "x q[" + i + "];";
		}
		
		@Override
		public String qiskitLine(IntList l) {
			return "qc.x(" + pythonArray(l) + ")";
		}
	}
	
	public static class Y extends Basic {
		
		private static final String[] ID = new String[] {"y", "cy"};
		
		public Y(QuantumComputer qc, IntSet n) {
			super(qc, n);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.y(n);
		}
		
		@Override
		public Basic newMerged(IntSet cIn, IntSet tIn) {
			return new Y(qc, tIn);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return Y;
		}
		
		@Override
		public String qasmLine(int i) {
			return "y q[" + i + "];";
		}
		
		@Override
		public String qiskitLine(IntList l) {
			return "qc.y(" + pythonArray(l) + ")";
		}
	}
	
	public static class Z extends Basic {
		
		private static final String[] ID = new String[] {"z", "cz"};
		
		public Z(QuantumComputer qc, IntSet n) {
			super(qc, n);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.z(n);
		}
		
		@Override
		public Basic newMerged(IntSet cIn, IntSet tIn) {
			return new Z(qc, tIn);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return Z;
		}
		
		@Override
		public String qasmLine(int i) {
			return "z q[" + i + "];";
		}
		
		@Override
		public String qiskitLine(IntList l) {
			return "qc.z(" + pythonArray(l) + ")";
		}
	}
	
	public static class H extends Basic {
		
		private static final String[] ID = new String[] {"h", "ch"};
		
		public H(QuantumComputer qc, IntSet n) {
			super(qc, n);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.h(n);
		}
		
		@Override
		public Basic newMerged(IntSet cIn, IntSet tIn) {
			return new H(qc, tIn);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return H;
		}
		
		@Override
		public String qasmLine(int i) {
			return "h q[" + i + "];";
		}
		
		@Override
		public String qiskitLine(IntList l) {
			return "qc.h(" + pythonArray(l) + ")";
		}
	}
	
	public static class S extends Basic {
		
		private static final String[] ID = new String[] {"s", "cs"};
		
		public S(QuantumComputer qc, IntSet n) {
			super(qc, n);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.s(n);
		}
		
		@Override
		public Basic newMerged(IntSet cIn, IntSet tIn) {
			return new S(qc, tIn);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return S;
		}
		
		@Override
		public String qasmLine(int i) {
			return "s q[" + i + "];";
		}
		
		@Override
		public String qiskitLine(IntList l) {
			return "qc.s(" + pythonArray(l) + ")";
		}
	}
	
	public static class Sdg extends Basic {
		
		private static final String[] ID = new String[] {"sdg", "csdg"};
		
		public Sdg(QuantumComputer qc, IntSet n) {
			super(qc, n);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.sdg(n);
		}
		
		@Override
		public Basic newMerged(IntSet cIn, IntSet tIn) {
			return new Sdg(qc, tIn);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return Sdg;
		}
		
		@Override
		public String qasmLine(int i) {
			return "sdg q[" + i + "];";
		}
		
		@Override
		public String qiskitLine(IntList l) {
			return "qc.sdg(" + pythonArray(l) + ")";
		}
	}
	
	public static class T extends Basic {
		
		private static final String[] ID = new String[] {"t", "ct"};
		
		public T(QuantumComputer qc, IntSet n) {
			super(qc, n);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.t(n);
		}
		
		@Override
		public Basic newMerged(IntSet cIn, IntSet tIn) {
			return new T(qc, tIn);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return T;
		}
		
		@Override
		public String qasmLine(int i) {
			return "t q[" + i + "];";
		}
		
		@Override
		public String qiskitLine(IntList l) {
			return "qc.t(" + pythonArray(l) + ")";
		}
	}
	
	public static class Tdg extends Basic {
		
		private static final String[] ID = new String[] {"tdg", "ctdg"};
		
		public Tdg(QuantumComputer qc, IntSet n) {
			super(qc, n);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.tdg(n);
		}
		
		@Override
		public Basic newMerged(IntSet cIn, IntSet tIn) {
			return new Tdg(qc, tIn);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return Tdg;
		}
		
		@Override
		public String qasmLine(int i) {
			return "tdg q[" + i + "];";
		}
		
		@Override
		public String qiskitLine(IntList l) {
			return "qc.tdg(" + pythonArray(l) + ")";
		}
	}
	
	public static abstract class BasicAngle extends QuantumGate<BasicAngle> {
		
		protected final double angle;
		protected final IntSet n;
		
		public BasicAngle(QuantumComputer qc, double angle, IntSet n) {
			super(qc, BasicAngle.class);
			this.angle = angle;
			this.n = n;
		}
		
		@Override
		public BasicAngle mergeInernal(BasicAngle next) {
			if (angle != next.angle) {
				return null;
			}
			
			if (next instanceof ControlAngle && !((ControlAngle) next).c.isEmpty()) {
				return null;
			}
			
			for (int i : n) {
				if (next.n.contains(i)) {
					return null;
				}
			}
			
			IntSet nCopy = new IntOpenHashSet(n);
			nCopy.addAll(next.n);
			return newMerged(angle, S0, nCopy);
		}
		
		public abstract BasicAngle newMerged(double angleIn, IntSet cIn, IntSet tIn);
		
		@Override
		public List<String> getCode(int type) {
			IntList l = list(n);
			List<String> out = new ArrayList<>();
			
			if (type == 0) {
				for (int i = 0; i < l.size(); ++i) {
					out.add(qasmLine(angle, l.getInt(i)));
				}
			}
			else if (type == 1) {
				if (!l.isEmpty()) {
					out.add(qiskitLine(angle, l));
				}
			}
			
			return out;
		}
		
		@Override
		public void addRequiredDecomposition(List<QuantumGate<?>> decomposition) {
			if (!n.isEmpty()) {
				decomposition.add(this);
			}
		}
		
		public abstract String qasmLine(double angleIn, int i);
		
		public abstract String qiskitLine(double angleIn, IntList l);
	}
	
	public static class P extends BasicAngle {
		
		private static final String[] ID = new String[] {"p", "cp"};
		
		public P(QuantumComputer qc, double angle, IntSet n) {
			super(qc, angle, n);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.p(angle, n);
		}
		
		@Override
		public BasicAngle newMerged(double angleIn, IntSet cIn, IntSet tIn) {
			return new P(qc, angleIn, tIn);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return p(angle);
		}
		
		@Override
		public String qasmLine(double angleIn, int i) {
			return "p(" + Math.toRadians(angleIn) + ") q[" + i + "];";
		}
		
		@Override
		public String qiskitLine(double angleIn, IntList l) {
			return "qc.p(" + Math.toRadians(angleIn) + ", " + pythonArray(l) + ")";
		}
	}
	
	public static class RX extends BasicAngle {
		
		private static final String[] ID = new String[] {"rx", "crx"};
		
		public RX(QuantumComputer qc, double angle, IntSet n) {
			super(qc, angle, n);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.rx(angle, n);
		}
		
		@Override
		public BasicAngle newMerged(double angleIn, IntSet cIn, IntSet tIn) {
			return new RX(qc, angleIn, tIn);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return rx(angle);
		}
		
		@Override
		public String qasmLine(double angleIn, int i) {
			return "rx(" + Math.toRadians(angleIn) + ") q[" + i + "];";
		}
		
		@Override
		public String qiskitLine(double angleIn, IntList l) {
			return "qc.rx(" + Math.toRadians(angleIn) + ", " + pythonArray(l) + ")";
		}
	}
	
	public static class RY extends BasicAngle {
		
		private static final String[] ID = new String[] {"ry", "cry"};
		
		public RY(QuantumComputer qc, double angle, IntSet n) {
			super(qc, angle, n);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.ry(angle, n);
		}
		
		@Override
		public BasicAngle newMerged(double angleIn, IntSet cIn, IntSet tIn) {
			return new RY(qc, angleIn, tIn);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return ry(angle);
		}
		
		@Override
		public String qasmLine(double angleIn, int i) {
			return "ry(" + Math.toRadians(angleIn) + ") q[" + i + "];";
		}
		
		@Override
		public String qiskitLine(double angleIn, IntList l) {
			return "qc.ry(" + Math.toRadians(angleIn) + ", " + pythonArray(l) + ")";
		}
	}
	
	public static class RZ extends BasicAngle {
		
		private static final String[] ID = new String[] {"rz", "crz"};
		
		public RZ(QuantumComputer qc, double angle, IntSet n) {
			super(qc, angle, n);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.rz(angle, n);
		}
		
		@Override
		public BasicAngle newMerged(double angleIn, IntSet cIn, IntSet tIn) {
			return new RZ(qc, angleIn, tIn);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return rz(angle);
		}
		
		@Override
		public String qasmLine(double angleIn, int i) {
			return "rz(" + Math.toRadians(angleIn) + ") q[" + i + "];";
		}
		
		@Override
		public String qiskitLine(double angleIn, IntList l) {
			return "qc.rz(" + Math.toRadians(angleIn) + ", " + pythonArray(l) + ")";
		}
	}
	
	public static abstract class Control extends Basic implements IControl {
		
		protected final IntSet c;
		
		public Control(QuantumComputer qc, IntSet c, IntSet t) {
			super(qc, t);
			this.c = c;
		}
		
		@Override
		public IntSet c() {
			return c;
		}
		
		@Override
		public IntSet t() {
			return n;
		}
		
		@Override
		public Basic mergeInernal(Basic next) {
			if (!matchingControl(c, next instanceof Control ? ((Control) next).c : S0)) {
				return null;
			}
			
			for (int i : n) {
				if (next.n.contains(i)) {
					return null;
				}
			}
			
			IntSet nCopy = new IntOpenHashSet(n);
			nCopy.addAll(next.n);
			return newMerged(c, nCopy);
		}
		
		@Override
		public void addRequiredDecomposition(List<QuantumGate<?>> decomposition) {
			if (c.size() == 1) {
				if (!n.isEmpty()) {
					decomposition.add(this);
				}
			}
			else {
				addZYZDecomposition(this, decomposition);
			}
		}
		
		@Override
		public List<String> getCode(int type) {
			if (c.isEmpty()) {
				return withoutControl().getCode(type);
			}
			else if (c.size() == 1) {
				return singleControlCode(type);
			}
			
			List<QuantumGate<?>> decomposition = new ArrayList<>();
			addRequiredDecomposition(decomposition);
			
			List<String> out = new ArrayList<>();
			for (QuantumGate<?> gate : decomposition) {
				out.addAll(gate.getCode(type));
			}
			
			return out;
		}
		
		@Override
		public final String qasmLine(int i) {
			return ";";
		}
		
		@Override
		public final String qiskitLine(IntList l) {
			return "";
		}
		
		public List<String> singleControlCode(int type) {
			int c0 = list(c).getInt(0);
			IntList l = list(n);
			List<String> out = new ArrayList<>();
			
			if (type == 0) {
				for (int i = 0; i < l.size(); ++i) {
					out.add(singleControlQasmLine(c0, l.getInt(i)));
				}
			}
			else if (type == 1) {
				if (!l.isEmpty()) {
					out.add(singleControlQiskitLine(c0, l));
				}
			}
			
			return out;
		}
		
		public abstract String singleControlQasmLine(int cIn, int i);
		
		public abstract String singleControlQiskitLine(int cIn, IntList l);
	}
	
	public static class CX extends Control {
		
		private static final String[] ID = new String[] {"cx", "x"};
		
		public CX(QuantumComputer qc, IntSet c, IntSet t) {
			super(qc, c, t);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.cx(c, n);
		}
		
		@Override
		public Basic newMerged(IntSet cIn, IntSet tIn) {
			return new CX(qc, cIn, tIn);
		}
		
		@Override
		public QuantumGate<?> withoutControl() {
			return new X(qc, n);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return X;
		}
		
		@Override
		public void addRequiredDecomposition(List<QuantumGate<?>> decomposition) {
			if (n.isEmpty()) {
				return;
			}
			
			else if (c.isEmpty()) {
				decomposition.add(withoutControl());
				return;
			}
			
			final int c_size = c.size();
			
			if (c_size == 1 || c_size == 2) {
				decomposition.add(this);
				return;
			}
			
			final int q = qc.qubitCount(), h = (int) Math.ceil(q / 2D);
			final boolean toffoli = q >= 5 && c_size >= 3 && c_size <= h;
			final boolean four_cx = q >= c_size + 2 && c_size >= 3;
			
			// https://arxiv.org/abs/quant-ph/9503016
			if (toffoli || four_cx) {
				IntList c_list = list(c);
				for (int t : list(n)) {
					if (toffoli) {
						IntList anc = new IntArrayList(q - c_size - 1);
						for (int i = 0; i < q; ++i) {
							if (i != t && !c.contains(i)) {
								anc.add(i);
							}
						}
						
						final CX[] repeat = new CX[2 * (c_size - 2)];
						
						repeat[0] = new CX(qc, set(anc.getInt(0), c_list.getInt(0)), set(t));
						for (int i = 1; i < c_size - 2; ++i) {
							repeat[i] = new CX(qc, set(anc.getInt(i), c_list.getInt(i)), set(anc.getInt(i - 1)));
							repeat[2 * (c_size - 2) - i] = repeat[i];
						}
						repeat[c_size - 2] = new CX(qc, set(c_list.getInt(c_size - 2), c_list.getInt(c_size - 1)), set(anc.getInt(c_size - 3)));
						
						for (CX cx : repeat) {
							decomposition.add(cx);
						}
						for (CX cx : repeat) {
							decomposition.add(cx);
						}
					}
					
					else {
						final int m_1 = (int) Math.ceil((c_size + 1D) / 2D), m_2 = c_size - m_1 + 1;
						
						int anc = -1;
						for (int i = 0; i < q; ++i) {
							if (i != t && !c.contains(i)) {
								anc = i;
								break;
							}
						}
						
						IntSet c_1 = new IntOpenHashSet(m_1);
						c_1.addAll(c_list.subList(m_2 - 1, c_size));
						CX cx_1 = new CX(qc, c_1, set(anc));
						
						IntSet c_2 = new IntOpenHashSet(m_2);
						c_2.add(anc);
						c_2.addAll(c_list.subList(0, m_2 - 1));
						CX cx_2 = new CX(qc, c_2, set(t));
						
						decomposition.add(cx_1);
						decomposition.add(cx_2);
						decomposition.add(cx_1);
						decomposition.add(cx_2);
					}
				}
			}
			else {
				addZYZDecomposition(this, decomposition);
			}
		}
		
		@Override
		public List<String> getCode(int type) {
			
			if (c.isEmpty()) {
				return withoutControl().getCode(type);
			}
			
			int c_size = c.size();
			if (c_size == 1) {
				return singleControlCode(type);
			}
			else if (c_size == 2) {
				return doubleControlCode(type);
			}
			
			List<String> out = new ArrayList<>();
			List<QuantumGate<?>> decomposition = new ArrayList<>();
			
			addRequiredDecomposition(decomposition);
			for (QuantumGate<?> gate : decomposition) {
				out.addAll(gate.getCode(type));
			}
			return out;
		}
		
		@Override
		public String singleControlQasmLine(int cIn, int i) {
			return "cx q[" + cIn + "], q[" + i + "];";
		}
		
		@Override
		public String singleControlQiskitLine(int cIn, IntList l) {
			return "qc.cx(" + cIn + ", " + pythonArray(l) + ")";
		}
		
		public List<String> doubleControlCode(int type) {
			IntList c_list = list(c);
			int c1 = c_list.getInt(0), c2 = c_list.getInt(1);
			IntList l = list(n);
			List<String> out = new ArrayList<>();
			
			if (type == 0) {
				for (int i = 0; i < l.size(); ++i) {
					out.add(doubleControlQasmLine(c1, c2, l.getInt(i)));
				}
			}
			else if (type == 1) {
				if (!l.isEmpty()) {
					out.add(doubleControlQiskitLine(c1, c2, l));
				}
			}
			
			return out;
		}
		
		public String doubleControlQasmLine(int c1, int c2, int i) {
			return "ccx q[" + c1 + "], q[" + c2 + "], q[" + i + "];";
		}
		
		public String doubleControlQiskitLine(int c1, int c2, IntList l) {
			return "qc.ccx(" + c1 + ", " + c2 + ", " + pythonArray(l) + ")";
		}
	}
	
	public static class CY extends Control {
		
		private static final String[] ID = new String[] {"cy", "y"};
		
		public CY(QuantumComputer qc, IntSet c, IntSet t) {
			super(qc, c, t);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.cy(c, n);
		}
		
		@Override
		public Basic newMerged(IntSet cIn, IntSet tIn) {
			return new CY(qc, cIn, tIn);
		}
		
		@Override
		public QuantumGate<?> withoutControl() {
			return new Y(qc, n);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return Y;
		}
		
		@Override
		public String singleControlQasmLine(int cIn, int i) {
			return "cy q[" + cIn + "], q[" + i + "];";
		}
		
		@Override
		public String singleControlQiskitLine(int cIn, IntList l) {
			return "qc.cy(" + cIn + ", " + pythonArray(l) + ")";
		}
	}
	
	public static class CZ extends Control {
		
		private static final String[] ID = new String[] {"cz", "z"};
		
		public CZ(QuantumComputer qc, IntSet c, IntSet t) {
			super(qc, c, t);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.cz(c, n);
		}
		
		@Override
		public Basic newMerged(IntSet cIn, IntSet tIn) {
			return new CZ(qc, cIn, tIn);
		}
		
		@Override
		public QuantumGate<?> withoutControl() {
			return new Z(qc, n);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return Z;
		}
		
		@Override
		public String singleControlQasmLine(int cIn, int i) {
			return "cz q[" + cIn + "], q[" + i + "];";
		}
		
		@Override
		public String singleControlQiskitLine(int cIn, IntList l) {
			return "qc.cz(" + cIn + ", " + pythonArray(l) + ")";
		}
	}
	
	public static class CH extends Control {
		
		private static final String[] ID = new String[] {"ch", "h"};
		
		public CH(QuantumComputer qc, IntSet c, IntSet t) {
			super(qc, c, t);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.ch(c, n);
		}
		
		@Override
		public Basic newMerged(IntSet cIn, IntSet tIn) {
			return new CH(qc, cIn, tIn);
		}
		
		@Override
		public QuantumGate<?> withoutControl() {
			return new H(qc, n);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return H;
		}
		
		@Override
		public String singleControlQasmLine(int cIn, int i) {
			return "ch q[" + cIn + "], q[" + i + "];";
		}
		
		@Override
		public String singleControlQiskitLine(int cIn, IntList l) {
			return "qc.ch(" + cIn + ", " + pythonArray(l) + ")";
		}
	}
	
	public static class CS extends Control {
		
		private static final String[] ID = new String[] {"cs", "s"};
		
		public CS(QuantumComputer qc, IntSet c, IntSet t) {
			super(qc, c, t);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.cs(c, n);
		}
		
		@Override
		public Basic newMerged(IntSet cIn, IntSet tIn) {
			return new CS(qc, cIn, tIn);
		}
		
		@Override
		public QuantumGate<?> withoutControl() {
			return new S(qc, n);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return S;
		}
		
		@Override
		public String singleControlQasmLine(int cIn, int i) {
			return "cp(pi/2) q[" + cIn + "], q[" + i + "];";
		}
		
		@Override
		public String singleControlQiskitLine(int cIn, IntList l) {
			return "qc.cp(pi/2, " + cIn + ", " + pythonArray(l) + ")";
		}
	}
	
	public static class CSdg extends Control {
		
		private static final String[] ID = new String[] {"csdg", "sdg"};
		
		public CSdg(QuantumComputer qc, IntSet c, IntSet t) {
			super(qc, c, t);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.csdg(c, n);
		}
		
		@Override
		public Basic newMerged(IntSet cIn, IntSet tIn) {
			return new CSdg(qc, cIn, tIn);
		}
		
		@Override
		public QuantumGate<?> withoutControl() {
			return new Sdg(qc, n);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return Sdg;
		}
		
		@Override
		public String singleControlQasmLine(int cIn, int i) {
			return "cp(-pi/2) q[" + cIn + "], q[" + i + "];";
		}
		
		@Override
		public String singleControlQiskitLine(int cIn, IntList l) {
			return "qc.cp(-pi/2, " + cIn + ", " + pythonArray(l) + ")";
		}
	}
	
	public static class CT extends Control {
		
		private static final String[] ID = new String[] {"ct", "t"};
		
		public CT(QuantumComputer qc, IntSet c, IntSet t) {
			super(qc, c, t);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.ct(c, n);
		}
		
		@Override
		public Basic newMerged(IntSet cIn, IntSet tIn) {
			return new CT(qc, cIn, tIn);
		}
		
		@Override
		public QuantumGate<?> withoutControl() {
			return new T(qc, n);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return T;
		}
		
		@Override
		public String singleControlQasmLine(int cIn, int i) {
			return "cp(pi/4) q[" + cIn + "], q[" + i + "];";
		}
		
		@Override
		public String singleControlQiskitLine(int cIn, IntList l) {
			return "qc.cp(pi/4, " + cIn + ", " + pythonArray(l) + ")";
		}
	}
	
	public static class CTdg extends Control {
		
		private static final String[] ID = new String[] {"ctdg", "tdg"};
		
		public CTdg(QuantumComputer qc, IntSet c, IntSet t) {
			super(qc, c, t);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.ctdg(c, n);
		}
		
		@Override
		public Basic newMerged(IntSet cIn, IntSet tIn) {
			return new CTdg(qc, cIn, tIn);
		}
		
		@Override
		public QuantumGate<?> withoutControl() {
			return new Tdg(qc, n);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return Tdg;
		}
		
		@Override
		public String singleControlQasmLine(int cIn, int i) {
			return "cp(-pi/4) q[" + cIn + "], q[" + i + "];";
		}
		
		@Override
		public String singleControlQiskitLine(int cIn, IntList l) {
			return "qc.cp(-pi/4, " + cIn + ", " + pythonArray(l) + ")";
		}
	}
	
	public static abstract class ControlAngle extends BasicAngle implements IControl {
		
		protected final IntSet c;
		
		public ControlAngle(QuantumComputer qc, double angle, IntSet c, IntSet t) {
			super(qc, angle, t);
			this.c = c;
		}
		
		@Override
		public IntSet c() {
			return c;
		}
		
		@Override
		public IntSet t() {
			return n;
		}
		
		@Override
		public BasicAngle mergeInernal(BasicAngle next) {
			if (angle != next.angle) {
				return null;
			}
			
			if (!matchingControl(c, next instanceof ControlAngle ? ((ControlAngle) next).c : S0)) {
				return null;
			}
			
			for (int i : n) {
				if (next.n.contains(i)) {
					return null;
				}
			}
			
			IntSet nCopy = new IntOpenHashSet(n);
			nCopy.addAll(next.n);
			return newMerged(angle, c, nCopy);
		}
		
		@Override
		public void addRequiredDecomposition(List<QuantumGate<?>> decomposition) {
			if (c.size() == 1) {
				if (!n.isEmpty()) {
					decomposition.add(this);
				}
			}
			else {
				addZYZDecomposition(this, decomposition);
			}
		}
		
		@Override
		public List<String> getCode(int type) {
			if (c.isEmpty()) {
				return withoutControl().getCode(type);
			}
			else if (c.size() == 1) {
				return singleControlCode(type);
			}
			
			List<QuantumGate<?>> decomposition = new ArrayList<>();
			addRequiredDecomposition(decomposition);
			
			List<String> out = new ArrayList<>();
			for (QuantumGate<?> gate : decomposition) {
				out.addAll(gate.getCode(type));
			}
			
			return out;
		}
		
		@Override
		public final String qasmLine(double angleIn, int i) {
			return ";";
		}
		
		@Override
		public final String qiskitLine(double angleIn, IntList l) {
			return "";
		}
		
		public List<String> singleControlCode(int type) {
			int c0 = list(c).getInt(0);
			IntList l = list(n);
			List<String> out = new ArrayList<>();
			
			if (type == 0) {
				for (int i = 0; i < l.size(); ++i) {
					out.add(singleControlQasmLine(angle, c0, l.getInt(i)));
				}
			}
			else if (type == 1) {
				if (!l.isEmpty()) {
					out.add(singleControlQiskitLine(angle, c0, l));
				}
			}
			
			return out;
		}
		
		public abstract String singleControlQasmLine(double angleIn, int cIn, int i);
		
		public abstract String singleControlQiskitLine(double angleIn, int cIn, IntList l);
	}
	
	public static class CP extends ControlAngle {
		
		private static final String[] ID = new String[] {"cp", "p"};
		
		public CP(QuantumComputer qc, double angle, IntSet c, IntSet t) {
			super(qc, angle, c, t);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.cp(angle, c, n);
		}
		
		@Override
		public BasicAngle newMerged(double angleIn, IntSet cIn, IntSet tIn) {
			return new CP(qc, angleIn, cIn, tIn);
		}
		
		@Override
		public QuantumGate<?> withoutControl() {
			return new P(qc, angle, n);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return p(angle);
		}
		
		@Override
		public String singleControlQasmLine(double angleIn, int cIn, int i) {
			return "cp(" + Math.toRadians(angleIn) + ") q[" + cIn + "], q[" + i + "];";
		}
		
		@Override
		public String singleControlQiskitLine(double angleIn, int cIn, IntList l) {
			return "qc.cp(" + Math.toRadians(angleIn) + ", " + cIn + ", " + pythonArray(l) + ")";
		}
	}
	
	public static class CRX extends ControlAngle {
		
		private static final String[] ID = new String[] {"crx", "rx"};
		
		public CRX(QuantumComputer qc, double angle, IntSet c, IntSet t) {
			super(qc, angle, c, t);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.crx(angle, c, n);
		}
		
		@Override
		public BasicAngle newMerged(double angleIn, IntSet cIn, IntSet tIn) {
			return new CRX(qc, angleIn, cIn, tIn);
		}
		
		@Override
		public QuantumGate<?> withoutControl() {
			return new RX(qc, angle, n);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return rx(angle);
		}
		
		@Override
		public String singleControlQasmLine(double angleIn, int cIn, int i) {
			return "crx(" + Math.toRadians(angleIn) + ") q[" + cIn + "], q[" + i + "];";
		}
		
		@Override
		public String singleControlQiskitLine(double angleIn, int cIn, IntList l) {
			return "qc.crx(" + Math.toRadians(angleIn) + ", " + cIn + ", " + pythonArray(l) + ")";
		}
	}
	
	public static class CRY extends ControlAngle {
		
		private static final String[] ID = new String[] {"cry", "ry"};
		
		public CRY(QuantumComputer qc, double angle, IntSet c, IntSet t) {
			super(qc, angle, c, t);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.cry(angle, c, n);
		}
		
		@Override
		public BasicAngle newMerged(double angleIn, IntSet cIn, IntSet tIn) {
			return new CRY(qc, angleIn, cIn, tIn);
		}
		
		@Override
		public QuantumGate<?> withoutControl() {
			return new RY(qc, angle, n);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return ry(angle);
		}
		
		@Override
		public String singleControlQasmLine(double angleIn, int cIn, int i) {
			return "cry(" + Math.toRadians(angleIn) + ") q[" + cIn + "], q[" + i + "];";
		}
		
		@Override
		public String singleControlQiskitLine(double angleIn, int cIn, IntList l) {
			return "qc.cry(" + Math.toRadians(angleIn) + ", " + cIn + ", " + pythonArray(l) + ")";
		}
	}
	
	public static class CRZ extends ControlAngle {
		
		private static final String[] ID = new String[] {"crz", "rz"};
		
		public CRZ(QuantumComputer qc, double angle, IntSet c, IntSet t) {
			super(qc, angle, c, t);
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			qc.crz(angle, c, n);
		}
		
		@Override
		public BasicAngle newMerged(double angleIn, IntSet cIn, IntSet tIn) {
			return new CRZ(qc, angleIn, cIn, tIn);
		}
		
		@Override
		public QuantumGate<?> withoutControl() {
			return new RZ(qc, angle, n);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return rz(angle);
		}
		
		@Override
		public String singleControlQasmLine(double angleIn, int cIn, int i) {
			return "crz(" + Math.toRadians(angleIn) + ") q[" + cIn + "], q[" + i + "];";
		}
		
		@Override
		public String singleControlQiskitLine(double angleIn, int cIn, IntList l) {
			return "qc.crz(" + Math.toRadians(angleIn) + ", " + cIn + ", " + pythonArray(l) + ")";
		}
	}
	
	public static class Swap extends QuantumGate<Swap> {
		
		private static final String[] ID = new String[] {"swap", "cswap"};
		
		protected final IntList i, j;
		
		public Swap(QuantumComputer qc, IntList i, IntList j) {
			super(qc, Swap.class);
			this.i = i;
			this.j = j;
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			if (i.size() == j.size()) {
				qc.swap(i, j);
			}
		}
		
		@Override
		public Swap mergeInernal(Swap next) {
			return matchingControl(S0, next instanceof ControlSwap ? ((ControlSwap) next).c : S0) ? newMerged(S0, i, j, next.i, next.j) : null;
		}
		
		public Swap newMerged(IntSet c, IntList i1, IntList j1, IntList i2, IntList j2) {
			IntList iCopy = new IntArrayList(i1), jCopy = new IntArrayList(j1);
			iCopy.addAll(i2);
			jCopy.addAll(j2);
			return c.isEmpty() ? new Swap(qc, iCopy, jCopy) : new ControlSwap(qc, c, iCopy, jCopy);
		}
		
		@Override
		public ComplexMatrix singleQubitOperation() {
			return null;
		}
		
		@Override
		public void addRequiredDecomposition(List<QuantumGate<?>> decomposition) {
			if (!i.isEmpty() && i.size() == j.size()) {
				decomposition.add(this);
			}
		}
		
		@Override
		public List<String> getCode(int type) {
			List<String> out = new ArrayList<>();
			
			if (i.size() == j.size()) {
				if (type == 0) {
					for (int k = 0; k < i.size(); ++k) {
						out.add("swap q[" + i.getInt(k) + "], q[" + j.getInt(k) + "];");
					}
				}
				else if (type == 1) {
					for (int k = 0; k < i.size(); ++k) {
						out.add("qc.swap(" + i.getInt(k) + ", " + j.getInt(k) + ")");
					}
				}
			}
			
			return out;
		}
	}
	
	public static class ControlSwap extends Swap implements IControl {
		
		private static final String[] ID = new String[] {"cswap", "swap"};
		
		protected final IntSet c;
		
		public ControlSwap(QuantumComputer qc, IntSet c, IntList i, IntList j) {
			super(qc, i, j);
			this.c = c;
		}
		
		@Override
		public IntSet c() {
			return c;
		}
		
		@Override
		public IntSet t() {
			return null;
		}
		
		@Override
		public String getID() {
			return ID[0];
		}
		
		@Override
		public String[] mergerIDs() {
			return ID;
		}
		
		@Override
		public void run() {
			if (i.size() == j.size()) {
				qc.cswap(c, i, j);
			}
		}
		
		@Override
		public Swap mergeInernal(Swap next) {
			return matchingControl(c, next instanceof ControlSwap ? ((ControlSwap) next).c : S0) ? newMerged(c, i, j, next.i, next.j) : null;
		}
		
		@Override
		public QuantumGate<?> withoutControl() {
			return new Swap(qc, i, j);
		}
		
		@Override
		public void addRequiredDecomposition(List<QuantumGate<?>> decomposition) {
			if (!i.isEmpty() && i.size() == j.size()) {
				if (c.isEmpty()) {
					decomposition.add(withoutControl());
				}
				else if (c.size() == 1) {
					decomposition.add(this);
				}
				else {
					for (int k = 0; k < i.size(); ++k) {
						IntSet c_1 = set(i.getInt(k));
						c_1.addAll(c);
						CX cx_1 = new CX(qc, c_1, set(j.getInt(k)));
						
						IntSet c_2 = set(j.getInt(k));
						c_2.addAll(c);
						CX cx_2 = new CX(qc, c_2, set(i.getInt(k)));
						
						addZYZDecomposition(cx_1, decomposition);
						addZYZDecomposition(cx_2, decomposition);
						addZYZDecomposition(cx_1, decomposition);
					}
				}
			}
		}
		
		@Override
		public List<String> getCode(int type) {
			if (c.isEmpty()) {
				return withoutControl().getCode(type);
			}
			
			List<String> out = new ArrayList<>();
			
			if (!i.isEmpty() && i.size() == j.size()) {
				if (c.size() == 1) {
					IntList l = list(c);
					if (type == 0) {
						String s = "cswap q[" + l.getInt(0) + "], ";
						for (int k = 0; k < i.size(); ++k) {
							out.add(s + "q[" + i.getInt(k) + "], q[" + j.getInt(k) + "];");
						}
					}
					else if (type == 1) {
						String s = "qc.cswap(" + l.getInt(0) + ", ";
						for (int k = 0; k < i.size(); ++k) {
							out.add(s + i.getInt(k) + ", " + j.getInt(k) + ")");
						}
					}
				}
				else {
					List<QuantumGate<?>> decomposition = new ArrayList<>();
					addRequiredDecomposition(decomposition);
					
					for (QuantumGate<?> gate : decomposition) {
						out.addAll(gate.getCode(type));
					}
				}
			}
			
			return out;
		}
	}
	
	// Static
	
	public static final IntSet S0 = new IntOpenHashSet(0);
	
	public static int dim(int n) {
		return 1 << n;
	}
	
	public static ComplexMatrix id(int n) {
		return new ComplexMatrix(dim(n)).id();
	}
	
	public static IntSet set(int... n) {
		return new IntOpenHashSet(n);
	}
	
	public static IntList list(IntSet n) {
		IntList l = new IntArrayList();
		for (int i = 0; i < QuantumComputer.getMaxQubits(); ++i) {
			if (n.contains(i)) {
				l.add(i);
			}
		}
		return l;
	}
	
	public static String intSetToString(IntSet set) {
		return Arrays.toString(list(set).toIntArray());
	}
	
	public static String intListToString(IntList list) {
		return Arrays.toString(list.toIntArray());
	}
	
	public static final ComplexMatrix I = new ComplexMatrix(new double[][] {new double[] {1D, 0D, 0D, 0D}, new double[] {0D, 0D, 1D, 0D}});
	
	public static final ComplexMatrix X = new ComplexMatrix(new double[][] {new double[] {0D, 0D, 1D, 0D}, new double[] {1D, 0D, 0D, 0D}});
	
	public static final ComplexMatrix Y = new ComplexMatrix(new double[][] {new double[] {0D, 0D, 0D, -1D}, new double[] {0D, 1D, 0D, 0D}});
	
	public static final ComplexMatrix Z = new ComplexMatrix(new double[][] {new double[] {1D, 0D, 0D, 0D}, new double[] {0D, 0D, -1D, 0D}});
	
	public static final ComplexMatrix H = new ComplexMatrix(new double[][] {new double[] {1D, 0D, 1D, 0D}, new double[] {1D, 0D, -1D, 0D}}).multiply(NCMath.INV_SQRT2);
	
	public static final ComplexMatrix S = new ComplexMatrix(new double[][] {new double[] {1D, 0D, 0D, 0D}, new double[] {0D, 0D, 0D, 1D}});
	
	public static final ComplexMatrix Sdg = new ComplexMatrix(new double[][] {new double[] {1D, 0D, 0D, 0D}, new double[] {0D, 0D, 0D, -1D}});
	
	public static final ComplexMatrix T = new ComplexMatrix(new double[][] {new double[] {1D, 0D, 0D, 0D}, new double[] {0D, 0D, NCMath.INV_SQRT2, NCMath.INV_SQRT2}});
	
	public static final ComplexMatrix Tdg = new ComplexMatrix(new double[][] {new double[] {1D, 0D, 0D, 0D}, new double[] {0D, 0D, NCMath.INV_SQRT2, -NCMath.INV_SQRT2}});
	
	/** Angle in degrees! */
	public static ComplexMatrix p(double angle) {
		double[] p = Complex.phase_d(angle);
		return new ComplexMatrix(new double[][] {new double[] {1D, 0D, 0D, 0D}, new double[] {0D, 0D, p[0], p[1]}});
	}
	
	/** Angle in degrees! */
	public static ComplexMatrix rx(double angle) {
		return new ComplexMatrix(new double[][] {new double[] {NCMath.cos_d(angle / 2D), 0D, 0D, -NCMath.sin_d(angle / 2D)}, new double[] {0D, -NCMath.sin_d(angle / 2D), NCMath.cos_d(angle / 2D), 0D}});
	}
	
	/** Angle in degrees! */
	public static ComplexMatrix ry(double angle) {
		return new ComplexMatrix(new double[][] {new double[] {NCMath.cos_d(angle / 2D), 0D, -NCMath.sin_d(angle / 2D), 0D}, new double[] {NCMath.sin_d(angle / 2D), 0D, NCMath.cos_d(angle / 2D), 0D}});
	}
	
	/** Angle in degrees! */
	public static ComplexMatrix rz(double angle) {
		double[] a = Complex.phase_d(-angle / 2D), b = Complex.phase_d(angle / 2D);
		return new ComplexMatrix(new double[][] {new double[] {a[0], a[1], 0D, 0D}, new double[] {0D, 0D, b[0], b[1]}});
	}
	
	public static final ComplexMatrix P_0 = new ComplexMatrix(new double[][] {new double[] {1D, 0D, 0D, 0D}, new double[] {0D, 0D, 0D, 0D}});
	
	public static final ComplexMatrix P_1 = new ComplexMatrix(new double[][] {new double[] {0D, 0D, 0D, 0D}, new double[] {0D, 0D, 1D, 0D}});
	
	// Helper Methods
	
	public static boolean matchingID(String id, String[] arr) {
		for (String s : arr) {
			if (id.equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean matchingControl(IntSet c1, IntSet c2) {
		if (c1.size() != c2.size()) {
			return false;
		}
		for (int i : c1) {
			if (!c2.contains(i)) {
				return false;
			}
		}
		return true;
	}
	
	public static final Object2ObjectMap<ComplexMatrix, double[]> ZYZ_DECOMPOSITION_ANGLES_CACHE = new Object2ObjectOpenHashMap<>();
	
	static {
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(I, new double[] {0D, 0D, 0D, 0D});
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(X, new double[] {90D, -90D, 180D, 90D});
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(Y, new double[] {90D, 0D, 180D, 0D});
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(Z, new double[] {90D, 90D, 0D, 90D});
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(H, new double[] {90D, 0D, 90D, 180D});
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(S, new double[] {45D, 45D, 0D, 45D});
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(Sdg, new double[] {-45D, -45D, 0D, -45D});
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(T, new double[] {22.5D, 22.5D, 0D, 22.5D});
		ZYZ_DECOMPOSITION_ANGLES_CACHE.put(Tdg, new double[] {-22.5D, -22.5D, 0D, -22.5D});
	}
	
	/** Returns the phase and Euler angles for the gate in the ZYZ basis in degrees. Translated from https://qiskit.org/documentation/_modules/qiskit/quantum_info/synthesis/one_qubit_decompose.html#OneQubitEulerDecomposer */
	public static double[] getZYZDecompositionAngles(ComplexMatrix matrix) {
		if (ZYZ_DECOMPOSITION_ANGLES_CACHE.containsKey(matrix)) {
			return ZYZ_DECOMPOSITION_ANGLES_CACHE.get(matrix);
		}
		
		double[] det = matrix.det();
		double[] phase = Complex.invSqrt(det[0], det[1]);
		
		ComplexMatrix m = matrix.copy();
		m.multiply(phase[0], phase[1]);
		
		double ppl = 2D * Complex.arg(m.re[1][1], m.im[1][1]);
		double pml = 2D * Complex.arg(m.re[1][0], m.im[1][0]);
		
		return new double[] {-Math.toDegrees(Complex.arg(phase[0], phase[1])), Math.toDegrees((ppl + pml) / 2D), Math.toDegrees(2D * Math.atan2(Complex.abs(m.re[1][0], m.im[1][0]), Complex.abs(m.re[0][0], m.im[0][0]))), Math.toDegrees((ppl - pml) / 2D)};
	}
	
	/** Adds the ZYZ decomposition of this gate to the list. Combines results from: https://arxiv.org/abs/quant-ph/9503016, Nielsen, Michael A.; Chuang, Isaac L. Quantum Computation and Quantum Information */
	public static <GATE extends QuantumGate<?> & IControl> void addZYZDecomposition(GATE gate, List<QuantumGate<?>> decomposition) {
		IntSet t = gate.t();
		if (t.isEmpty()) {
			return;
		}
		IntSet c = gate.c();
		
		double[] azyz = getZYZDecompositionAngles(gate.singleQubitOperation());
		double alpha = azyz[0], beta = azyz[1], hgam = azyz[2] / 2D, hbpd = (azyz[1] + azyz[3]) / 2D, hdmb = (azyz[3] - azyz[1]) / 2D;
		boolean hgam_f = full(hgam), hbpd_f = full(hbpd);
		
		if (c.isEmpty()) {
			decomposition.add(gate.withoutControl());
		}
		else if (c.size() == 1) {
			if (!full(hdmb)) {
				new RZ(gate.qc, hdmb, t).addRequiredDecomposition(decomposition);
			}
			if (!hbpd_f || !hgam_f) {
				new CX(gate.qc, c, t).addRequiredDecomposition(decomposition);
				if (!hbpd_f) {
					new RZ(gate.qc, -hbpd, t).addRequiredDecomposition(decomposition);
				}
				if (!hgam_f) {
					new RY(gate.qc, -hgam, t).addRequiredDecomposition(decomposition);
				}
				new CX(gate.qc, c, t).addRequiredDecomposition(decomposition);
			}
			if (!hgam_f) {
				new RY(gate.qc, hgam, t).addRequiredDecomposition(decomposition);
			}
			if (!full(beta)) {
				new RZ(gate.qc, beta, t).addRequiredDecomposition(decomposition);
			}
			if (!full(alpha)) {
				new P(gate.qc, alpha, c).addRequiredDecomposition(decomposition);
			}
		}
		else {
			IntList c_list = list(c);
			IntSet c_rot = set(c_list.getInt(0));
			IntSet c_cx = new IntOpenHashSet(c);
			c_cx.rem(c_list.getInt(0));
			
			if (!full(hdmb)) {
				new CRZ(gate.qc, hdmb, c_rot, t).addRequiredDecomposition(decomposition);
			}
			if (!hbpd_f || !hgam_f) {
				new CX(gate.qc, c_cx, t).addRequiredDecomposition(decomposition);
				if (!hbpd_f) {
					new CRZ(gate.qc, -hbpd, c_rot, t).addRequiredDecomposition(decomposition);
				}
				if (!hgam_f) {
					new CRY(gate.qc, -hgam, c_rot, t).addRequiredDecomposition(decomposition);
				}
				new CX(gate.qc, c_cx, t).addRequiredDecomposition(decomposition);
			}
			if (!hgam_f) {
				new CRY(gate.qc, hgam, c_rot, t).addRequiredDecomposition(decomposition);
			}
			if (!full(beta)) {
				new CRZ(gate.qc, beta, c_rot, t).addRequiredDecomposition(decomposition);
			}
			if (!full(alpha)) {
				new CP(gate.qc, alpha, c_cx, c_rot).addRequiredDecomposition(decomposition);
			}
		}
	}
	
	public static boolean full(double angle) {
		return angle % 720D == 0D;
	}
	
	public static String pythonArray(IntList list) {
		return pythonArray(list, false);
	}
	
	public static String pythonArray(IntList list, boolean forceBrackets) {
		if (!forceBrackets && list.size() == 1) {
			return Integer.toString(list.getInt(0));
		}
		
		String out = "[";
		for (int i : list) {
			out += (i + ", ");
		}
		return StringHelper.removeSuffix(out, 2) + "]";
	}
}
