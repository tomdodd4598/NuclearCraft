package nc.quantum;

public class Helpers {

	public static int[] powers(int[] targets, int size) {
		int count = targets.length, len = size - count;
		int[] powers = new int[len];
		for (int i = 0, j = 0, k = 0; k < len; ++i) {
			if (j >= count || targets[j] != i) {
				powers[k++] = 1 << i;
			}
			else {
				++j;
			}
		}
		return powers;
	}

	public static int[] offsets(int[] powers) {
		int[] offsets = new int[1 << powers.length];
		for (int i = 1, len = offsets.length; i < len; ++i) {
			int lsb = i & -i;
			offsets[i] = offsets[i ^ lsb] | powers[Integer.numberOfTrailingZeros(lsb)];
		}
		return offsets;
	}

	public static int[] starts(int[] targets, int size) {
		return offsets(powers(targets, size));
	}

	public static int[] window(int[] targets) {
		int[] powers = new int[targets.length];
		for (int i = 0, len = targets.length; i < len; ++i) {
			powers[i] = 2 << targets[i];
		}
		return offsets(powers);
	}
}
