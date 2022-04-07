package nc.util;

import java.util.*;

public class PermutationHelper {
	
	public static <T> List<List<T>> permutations(List<T> input) {
		List<List<T>> output = new ArrayList<>();
		if (input.isEmpty()) {
			output.add(input);
			return output;
		}
		permute(input, output, 0, input.size() - 1);
		return output;
	}
	
	public static <T> void permute(List<T> input, List<List<T>> output, int l, int r) {
		if (l == r) {
			output.add(input);
		}
		else {
			for (int i = l; i <= r; ++i) {
				input = swap(input, l, i);
				permute(input, output, l + 1, r);
				input = swap(input, l, i);
			}
		}
	}
	
	public static <T> List<T> swap(List<T> input, int i, int j) {
		T temp;
		List<T> copy = new ArrayList<>(input);
		temp = copy.get(i);
		copy.set(i, copy.get(j));
		copy.set(j, temp);
		return copy;
	}
}
