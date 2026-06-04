package nc.quantum;

import nc.quantum.operator.Operator;
import nc.util.IntPair;

import java.util.*;

public class Gate {
	
	protected final int[] controls, targets, window;
	protected final Operator op;
	public final int mask;
	
	public Gate(int[] targets, Operator op) {
		this(new int[] {}, targets, op, false);
	}
	
	private Gate(int[] targets, Operator op, boolean canonical) {
		this(new int[] {}, targets, op, canonical);
	}
	
	public Gate(int[] controls, int[] targets, Operator op) {
		this(controls, targets, op, false);
	}
	
	private Gate(int[] controls, int[] targets, Operator op, boolean canonical) {
		int targetCount = op.size;
		this.controls = Arrays.copyOf(controls, controls.length);
		this.targets = Arrays.copyOf(targets, targetCount);
		
		if (!canonical) {
			Arrays.sort(this.controls);
			
			boolean sortedTargets = true;
			for (int i = 1; i < targetCount; ++i) {
				if (this.targets[i - 1] >= this.targets[i]) {
					sortedTargets = false;
					break;
				}
			}
			
			if (!sortedTargets) {
				IntPair[] basisPairs = new IntPair[targetCount];
				for (int i = 0; i < targetCount; ++i) {
					basisPairs[i] = new IntPair(i, this.targets[i]);
				}
				Arrays.sort(basisPairs, Comparator.comparingInt(p -> p.y));
				Arrays.sort(this.targets);
				
				int[] basis = new int[targetCount];
				for (int i = 0; i < targetCount; ++i) {
					basis[i] = basisPairs[i].x;
				}
				op = op.rebased(basis);
			}
		}
		this.window = Helpers.window(this.targets);
		this.op = op;
		
		int mask = 0;
		for (int control : this.controls) {
			mask |= 1 << control;
		}
		this.mask = mask;
	}
	
	public void map(double[] source, double[] target, int size) {
		int[] starts = Helpers.starts(targets, size);
		
		for (int start : starts) {
			int x = start << 1;
			if ((start & mask) == mask) {
				op.partialMap(source, target, x, window);
			}
			else {
				for (int offset : window) {
					int y = x + offset;
					target[y] = source[y];
					target[y + 1] = source[y + 1];
				}
			}
		}
	}
}
