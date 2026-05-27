package nc.quantum;

import nc.util.*;

import java.util.*;

public class State {

	public final int size, dim;
	public double[] vector;
	protected double[] backup;

	protected final Random rand = new Random();

	public State(int size) {
		this.size = size;
		this.dim = 1 << size;
		this.vector = new double[dim << 1];
		vector[0] = 1.0;
		this.backup = new double[dim << 1];
	}

	public double absSq() {
		double absSq = 0.0;
		for (int i = 0, len = vector.length; i < len; i += 2) {
			double re = vector[i], im = vector[i + 1];
			absSq += re * re + im * im;
		}
		return absSq;
	}

	public void normalize() {
		double abs = Math.sqrt(absSq());
		for (int i = 0, len = vector.length; i < len; i += 2) {
			vector[i] /= abs;
			vector[i + 1] /= abs;
		}
	}

	public double[] probs() {
		for (int i = 0; i < dim; ++i) {
			int x = i << 1;
			double re = vector[x], im = vector[x + 1];
			backup[i] = re * re + im * im;
		}
		return Arrays.copyOf(backup, dim);
	}

	public double prob(int[] targets, int pattern) {
		double result = 0.0;

		int count = targets.length;
		IntPair[] targetPairs = new IntPair[count];
		for (int i = 0; i < count; ++i) {
			targetPairs[i] = new IntPair(i, targets[i]);
		}
		Arrays.sort(targetPairs, Comparator.comparingInt(p -> p.y));

		targets = new int[count];
		int offset = 0;
		for (int i = 0; i < count; ++i) {
			IntPair pair = targetPairs[i];
			targets[i] = pair.y;
			offset |= ((pattern >> pair.x) & 1) << pair.y;
		}

		int[] starts = Helpers.starts(targets, size);
		for (int start : starts) {
			int x = (offset | start) << 1;
			double re = vector[x], im = vector[x + 1];
			result += re * re + im * im;
		}

		return result;
	}

	public double prob(int[] targets, boolean[] outcomes) {
		int pattern = 0, count = targets.length;
		for (int i = 0; i < count; ++i) {
			if (outcomes[i]) {
				pattern |= 1 << i;
			}
		}
		return prob(targets, pattern);
	}

	public double probAll(int[] targets) {
		return prob(targets, (1 << targets.length) - 1);
	}

	public double probAny(int[] targets) {
		return 1.0 - prob(targets, 0);
	}

	public boolean[] measure(int[] targets) {
		int targetCount = targets.length, patternCount = 1 << targetCount;
		double[] patternProbs = new double[patternCount];

		for (int i = 0; i < dim; ++i) {
			int pattern = 0;
			for (int j = 0; j < targetCount; ++j) {
				pattern |= ((i >> targets[j]) & 1) << j;
			}

			int x = i << 1;
			double re = vector[x], im = vector[x + 1];
			patternProbs[pattern] += re * re + im * im;
		}

		double totalProb = 0.0;
		for (int i = 0; i < patternCount; ++i) {
			totalProb += patternProbs[i];
		}

		double randProb = rand.nextDouble() * totalProb, probSum = 0.0;
		int resultPattern = 0;
		for (int i = 0; i < patternCount; ++i) {
			probSum += patternProbs[i];
			if (randProb < probSum) {
				resultPattern = i;
				break;
			}
		}

		double scale = Math.sqrt(patternProbs[resultPattern]);
		outer: for (int i = 0; i < dim; ++i) {
			int x = i << 1;
			for (int j = 0; j < targetCount; ++j) {
				if (((i >> targets[j]) & 1) != ((resultPattern >> j) & 1)) {
					vector[x] = vector[x + 1] = 0.0;
					continue outer;
				}
			}
			vector[x] /= scale;
			vector[x + 1] /= scale;
		}

		boolean[] outcomes = new boolean[targetCount];
		for (int i = 0; i < targetCount; ++i) {
			outcomes[i] = ((resultPattern >> i) & 1) == 1;
		}
		return outcomes;
	}

	public Complex inner(State other) {
		double re = 0.0, im = 0.0;
		for (int i = 0, len = vector.length; i < len; i += 2) {
			double a = vector[i], b = vector[i + 1], c = other.vector[i], d = -other.vector[i + 1];
			re += a * c - b * d;
			im += a * d + b * c;
		}
		return new Complex(re, im);
	}

	public double fidelity(State other) {
		return inner(other).absSq();
	}

	public void update(Gate gate) {
		gate.map(vector, backup, size);

		double[] t = vector;
		vector = backup;
		backup = t;
	}

	public State deepCopy() {
		State copy = new State(size);
		System.arraycopy(vector, 0, copy.vector, 0, vector.length);
		return copy;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		boolean start = true;
		for (int i = 0; i < dim; ++i) {
			if (!start) {
				sb.append(", ");
			}
			int x = i << 1;
			double re = vector[x], im = vector[x + 1];
			sb.append(re);
			if (im >= 0.0) {
				sb.append('+');
			}
			sb.append(im).append('i');
			start = false;
		}
		return sb.append(']').toString();
	}
}
