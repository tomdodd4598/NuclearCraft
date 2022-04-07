package nc.util;

import java.util.*;

import it.unimi.dsi.fastutil.doubles.*;
import it.unimi.dsi.fastutil.floats.*;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public class CollectionHelper {
	
	// Lists
	
	public static <T> List<T> asList(T[] array) {
		List<T> list = new ArrayList<>();
		for (T t : array) {
			list.add(t);
		}
		return list;
	}
	
	public static <T> List<T> intersect(List<T> list, Collection<T> collection) {
		List<T> out = new ArrayList<>();
		for (T t : list) {
			if (collection.contains(t)) {
				out.add(t);
			}
		}
		return out;
	}
	
	@SafeVarargs
	public static <T> List<T> intersect(List<T> first, List<T>... rest) {
		List<T> tList = new ArrayList<>();
		tList.addAll(first);
		for (List<T> list : rest) {
			tList = intersect(tList, list);
		}
		return tList;
	}
	
	@SafeVarargs
	public static <T> List<T> union(List<T> first, List<T>... rest) {
		Set<T> set = new ObjectOpenHashSet<>();
		set.addAll(first);
		for (List<T> list : rest) {
			set.addAll(list);
		}
		return new ArrayList<>(set);
	}
	
	@SafeVarargs
	public static <T> T[] concatenate(T[] first, T[]... rest) {
		int totalLength = first.length;
		for (T[] array : rest) {
			totalLength += array.length;
		}
		T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (T[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}
	
	@SafeVarargs
	public static <T> T[] concatenate(T[] first, T... rest) {
		int totalLength = first.length + rest.length;
		T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		System.arraycopy(rest, 0, result, offset, rest.length);
		return result;
	}
	
	@SafeVarargs
	public static <T> List<T> concatenate(List<T> first, List<T>... rest) {
		List<T> result = new ArrayList<>();
		result.addAll(first);
		for (List<T> list : rest) {
			result.addAll(list);
		}
		return result;
	}
	
	@SafeVarargs
	public static List<Object> concatGeneral(List<Object> first, List<Object>... rest) {
		List<Object> result = new ArrayList<>();
		result.addAll(first);
		for (List<Object> list : rest) {
			result.addAll(list);
		}
		return result;
	}
	
	@SafeVarargs
	public static IntList concatInt(List<? extends Integer> first, List<? extends Integer>... rest) {
		IntList result = new IntArrayList();
		result.addAll(first);
		for (List<? extends Integer> list : rest) {
			result.addAll(list);
		}
		return result;
	}
	
	@SafeVarargs
	public static DoubleList concatDouble(List<? extends Double> first, List<? extends Double>... rest) {
		DoubleList result = new DoubleArrayList();
		result.addAll(first);
		for (List<? extends Double> list : rest) {
			result.addAll(list);
		}
		return result;
	}
	
	@SafeVarargs
	public static LongList concatLong(List<? extends Long> first, List<? extends Long>... rest) {
		LongList result = new LongArrayList();
		result.addAll(first);
		for (List<? extends Long> list : rest) {
			result.addAll(list);
		}
		return result;
	}
	
	// Sets
	
	public static <T> Set<T> asSet(T[] array) {
		Set<T> set = new ObjectOpenHashSet<>();
		for (T t : array) {
			set.add(t);
		}
		return set;
	}
	
	public static <T> Set<T> intersect(Set<T> set1, Collection<T> set2) {
		Set<T> set = new ObjectOpenHashSet<>();
		for (T t : set1) {
			if (set2.contains(t)) {
				set.add(t);
			}
		}
		return set;
	}
	
	@SafeVarargs
	public static <T> Set<T> intersect(Set<T> first, Set<T>... rest) {
		Set<T> tSet = new ObjectOpenHashSet<>();
		tSet.addAll(first);
		for (Set<T> set : rest) {
			tSet = intersect(tSet, set);
		}
		return tSet;
	}
	
	@SafeVarargs
	public static <T> Set<T> union(Set<T> first, Set<T>... rest) {
		Set<T> tSet = new ObjectOpenHashSet<>();
		tSet.addAll(first);
		for (Set<T> set : rest) {
			tSet.addAll(set);
		}
		return tSet;
	}
	
	// NCInfo
	
	public static <T> boolean isEmpty(T[][] arrays, int arrayNo) {
		if (arrays.length <= arrayNo) {
			return true;
		}
		else {
			return arrays[arrayNo].length == 0;
		}
	}
	
	public static <T> boolean isNull(T[][] arrays, int arrayNo) {
		if (arrays.length <= arrayNo) {
			return false;
		}
		else {
			return arrays[arrayNo] == null;
		}
	}
	
	public static int sum(int[] numberArray) {
		int result = 0;
		for (int element : numberArray) {
			result += element;
		}
		return result;
	}
	
	public static int sumIntList(IntList numberList) {
		int result = 0;
		for (int i : numberList) {
			result += i;
		}
		return result;
	}
	
	public static double sum(double[] numberArray) {
		double result = 0D;
		for (double element : numberArray) {
			result += element;
		}
		return result;
	}
	
	public static double sumDoubleList(DoubleList numberList) {
		double result = 0D;
		for (double i : numberList) {
			result += i;
		}
		return result;
	}
	
	// Special cases for primitives
	
	public static IntList asIntegerList(int[] array) {
		IntList list = new IntArrayList();
		for (int t : array) {
			list.add(t);
		}
		return list;
	}
	
	public static DoubleList asDoubleList(double[] array) {
		DoubleList list = new DoubleArrayList();
		for (double t : array) {
			list.add(t);
		}
		return list;
	}
	
	public static FloatList asFloatList(float[] array) {
		FloatList list = new FloatArrayList();
		for (float t : array) {
			list.add(t);
		}
		return list;
	}
	
	// List to Array
	
	public static int[] asIntegerArray(IntList list) {
		int[] array = new int[list.size()];
		for (int i = 0; i < list.size(); ++i) {
			array[i] = list.get(i);
		}
		return array;
	}
	
	public static double[] asDoubleArray(DoubleList list) {
		double[] array = new double[list.size()];
		for (int i = 0; i < list.size(); ++i) {
			array[i] = list.get(i);
		}
		return array;
	}
	
	public static float[] asFloatArray(FloatList list) {
		float[] array = new float[list.size()];
		for (int i = 0; i < list.size(); ++i) {
			array[i] = list.get(i);
		}
		return array;
	}
	
	public static String[] asStringArray(List<String> list) {
		String[] array = new String[list.size()];
		for (int i = 0; i < list.size(); ++i) {
			array[i] = list.get(i);
		}
		return array;
	}
	
	// Other methods
	
	public static int[] increasingArray(int start, int length) {
		int[] array = new int[length];
		for (int i = 0; i < length; ++i) {
			array[i] = start + i;
		}
		return array;
	}
	
	public static int[] increasingArray(int length) {
		return increasingArray(0, length);
	}
	
	public static IntList increasingList(int start, int length) {
		IntList list = new IntArrayList();
		for (int i = 0; i < length; ++i) {
			list.add(start + i);
		}
		return list;
	}
	
	public static IntList increasingList(int length) {
		return increasingList(0, length);
	}
	
	/** Returns true if only the selected entries are true */
	public static boolean arrayXAND(boolean[] array, int... i) {
		for (int j = 0; j < array.length; ++j) {
			boolean b = 2 * i.length > array.length;
			for (int k : i) {
				if (b ^ j == k) {
					b = !b;
					break;
				}
			}
			if (b ^ array[j]) {
				return false;
			}
		}
		return true;
	}
	
	public static IntList nCopies(int n, int value) {
		return new IntArrayList(Collections.nCopies(n, value));
	}
	
	public static LongList nCopies(int n, long value) {
		return new LongArrayList(Collections.nCopies(n, value));
	}
	
	public static DoubleList nCopies(int n, double value) {
		return new DoubleArrayList(Collections.nCopies(n, value));
	}
	
	public static int[] arrayCopies(int n, int value) {
		return nCopies(n, value).toIntArray();
	}
	
	public static long[] arrayCopies(int n, long value) {
		return nCopies(n, value).toLongArray();
	}
	
	public static double[] arrayCopies(int n, double value) {
		return nCopies(n, value).toDoubleArray();
	}
}
