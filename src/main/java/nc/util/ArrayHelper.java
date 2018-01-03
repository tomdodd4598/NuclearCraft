package nc.util;

import java.util.Arrays;
import java.util.List;

public class ArrayHelper {
	
	public static <T> T[] concatenate(T[] first, T[]... rest) {
		int totalLength = first.length;
		for (T[] array : rest) totalLength += array.length;
		T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (T[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}
	
	public static <T> T[] concatenate(T[] first, T... rest) {
		int totalLength = first.length + rest.length;
		T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		System.arraycopy(rest, 0, result, offset, rest.length);
		return result;
	}
	
	public static <T> List<T> concatenate(List<T> first, List<T>... rest) {
		List<T> result = first;
		for (List<T> list : rest) result.addAll(list);
		return result;
	}
	
	public static <T> boolean isEmpty(T[][] arrays, int arrayNo) {
		if (arrays.length == 0) return true;
		else return arrays[arrayNo].length == 0;
	}
}
