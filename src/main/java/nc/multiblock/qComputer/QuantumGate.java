package nc.multiblock.qComputer;

import it.unimi.dsi.fastutil.ints.*;

public abstract class QuantumGate<GATE extends QuantumGate> {
	
	protected final QuantumComputer qc;
	protected final Class<GATE> gateClass;
	
	public QuantumGate(QuantumComputer qc, Class<GATE> gateClass) {
		this.qc = qc;
		this.gateClass = gateClass;
	}
	
	public abstract String getID();
	
	public abstract String[] mergerIDs();
	
	public abstract void run();
	
	public final QuantumGate merge(QuantumGate next) {
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
			IntSet n = new IntOpenHashSet(this.n);
			n.addAll(next.n);
			return new Measurement(qc, n);
		}
		
		@Override
		public boolean shouldMarkDirty() {
			return true;
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
			
			IntSet n = new IntOpenHashSet(this.n);
			n.addAll(next.n);
			return newMerged(S0, n);
		}
		
		public abstract Basic newMerged(IntSet c, IntSet t);
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
		public Basic newMerged(IntSet c, IntSet t) {
			return new X(qc, t);
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
		public Basic newMerged(IntSet c, IntSet t) {
			return new Y(qc, t);
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
		public Basic newMerged(IntSet c, IntSet t) {
			return new Z(qc, t);
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
		public Basic newMerged(IntSet c, IntSet t) {
			return new H(qc, t);
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
		public Basic newMerged(IntSet c, IntSet t) {
			return new S(qc, t);
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
		public Basic newMerged(IntSet c, IntSet t) {
			return new Sdg(qc, t);
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
		public Basic newMerged(IntSet c, IntSet t) {
			return new T(qc, t);
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
		public Basic newMerged(IntSet c, IntSet t) {
			return new Tdg(qc, t);
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
			
			IntSet n = new IntOpenHashSet(this.n);
			n.addAll(next.n);
			return newMerged(angle, S0, n);
		}
		
		public abstract BasicAngle newMerged(double angle, IntSet c, IntSet t);
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
		public BasicAngle newMerged(double angle, IntSet c, IntSet t) {
			return new P(qc, angle, t);
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
		public BasicAngle newMerged(double angle, IntSet c, IntSet t) {
			return new RX(qc, angle, t);
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
		public BasicAngle newMerged(double angle, IntSet c, IntSet t) {
			return new RY(qc, angle, t);
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
		public BasicAngle newMerged(double angle, IntSet c, IntSet t) {
			return new RZ(qc, angle, t);
		}
	}
	
	public static abstract class Control extends Basic {
		
		protected final IntSet c;
		
		public Control(QuantumComputer qc, IntSet c, IntSet t) {
			super(qc, t);
			this.c = c;
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
			
			IntSet n = new IntOpenHashSet(this.n);
			n.addAll(next.n);
			return newMerged(S0, n);
		}
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
		public Basic newMerged(IntSet c, IntSet t) {
			return new CX(qc, c, t);
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
		public Basic newMerged(IntSet c, IntSet t) {
			return new CY(qc, c, t);
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
		public Basic newMerged(IntSet c, IntSet t) {
			return new CZ(qc, c, t);
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
		public Basic newMerged(IntSet c, IntSet t) {
			return new CH(qc, c, t);
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
		public Basic newMerged(IntSet c, IntSet t) {
			return new CS(qc, c, t);
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
		public Basic newMerged(IntSet c, IntSet t) {
			return new CSdg(qc, c, t);
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
		public Basic newMerged(IntSet c, IntSet t) {
			return new CT(qc, c, t);
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
		public Basic newMerged(IntSet c, IntSet t) {
			return new CTdg(qc, c, t);
		}
	}
	
	public static abstract class ControlAngle extends BasicAngle {
		
		protected final IntSet c;
		
		public ControlAngle(QuantumComputer qc, double angle, IntSet c, IntSet t) {
			super(qc, angle, t);
			this.c = c;
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
			
			IntSet n = new IntOpenHashSet(this.n);
			n.addAll(next.n);
			return newMerged(angle, S0, n);
		}
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
		public BasicAngle newMerged(double angle, IntSet c, IntSet t) {
			return new CP(qc, angle, c, t);
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
		public BasicAngle newMerged(double angle, IntSet c, IntSet t) {
			return new CRX(qc, angle, c, t);
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
		public BasicAngle newMerged(double angle, IntSet c, IntSet t) {
			return new CRY(qc, angle, c, t);
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
		public BasicAngle newMerged(double angle, IntSet c, IntSet t) {
			return new CRZ(qc, angle, c, t);
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
			qc.swap(i, j);
		}
		
		@Override
		public Swap mergeInernal(Swap next) {
			return matchingControl(S0, next instanceof ControlSwap ? ((ControlSwap) next).c : S0) ? newMerged(S0, i, j, next.i, next.j) : null;
		}
		
		public Swap newMerged(IntSet c, IntList i1, IntList j1, IntList i2, IntList j2) {
			IntList i = new IntArrayList(i1), j = new IntArrayList(j1);
			i.addAll(i2);
			j.addAll(j2);
			return c.isEmpty() ? new Swap(qc, i, j) : new ControlSwap(qc, c, i, j);
		}
	}
	
	public static class ControlSwap extends Swap {
		
		private static final String[] ID = new String[] {"cswap", "swap"};
		
		protected final IntSet c;
		
		public ControlSwap(QuantumComputer qc, IntSet c, IntList i, IntList j) {
			super(qc, i, j);
			this.c = c;
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
			qc.cswap(c, i, j);
		}
		
		@Override
		public Swap mergeInernal(Swap next) {
			return matchingControl(c, next instanceof ControlSwap ? ((ControlSwap) next).c : S0) ? newMerged(c, i, j, next.i, next.j) : null;
		}
	}
	
	public static final IntSet S0 = new IntOpenHashSet(0);
	
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
}
