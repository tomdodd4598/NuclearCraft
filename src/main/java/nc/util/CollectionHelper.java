package nc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public class CollectionHelper {
	
	/* Lists */
	
	public static <T> List<T> asList(T[] array) {
		List<T> list = new ArrayList<>();
		for (T t : array) list.add(t);
		return list;
	}
	
	public static <T> List<T> intersect(List<T> list, Collection<T> collection) {
		List<T> out = new ArrayList<>();
		for (T t : list) if (collection.contains(t)) out.add(t);
		return out;
	}
	
	public static <T> List<T> intersect(List<T> first, List<T>... rest) {
		List<T> tList = new ArrayList<>();
		tList.addAll(first);
		for (List<T> list : rest) tList = intersect(tList, list);
		return tList;
	}
	
	public static <T> List<T> union(List<T> first, List<T>... rest) {
		Set<T> set = new ObjectOpenHashSet<>();
		set.addAll(first);
		for (List<T> list : rest) set.addAll(list);
		return new ArrayList<T>(set);
	}
	
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
		List<T> result = new ArrayList<>();
		result.addAll(first);
		for (List<T> list : rest) result.addAll(list);
		return result;
	}
	
	public static List concatGeneral(List first, List... rest) {
		List result = new ArrayList();
		result.addAll(first);
		for (List list : rest) result.addAll(list);
		return result;
	}
	
	/* Sets */
	
	public static <T> Set<T> asSet(T[] array) {
		Set<T> set = new ObjectOpenHashSet<>();
		for (T t : array) set.add(t);
		return set;
	}
	
	public static <T> Set<T> intersect(Set<T> set1, Collection<T> set2) {
		Set<T> set = new ObjectOpenHashSet<>();
		for (T t : set1) if(set2.contains(t)) set.add(t);
		return set;
	}
	
	public static <T> Set<T> intersect(Set<T> first, Set<T>... rest) {
		Set<T> tSet = new ObjectOpenHashSet<>();
		tSet.addAll(first);
		for (Set<T> set : rest) tSet = intersect(tSet, set);
		return tSet;
	}
	
	public static <T> Set<T> union(Set<T> first, Set<T>... rest) {
		Set<T> tSet = new ObjectOpenHashSet<>();
		tSet.addAll(first);
		for (Set<T> set : rest) tSet.addAll(set);
		return tSet;
	}
	
	/* NCInfo */
	
	public static <T> boolean isEmpty(T[][] arrays, int arrayNo) {
		if (arrays.length <= arrayNo) return true;
		else return arrays[arrayNo].length == 0;
	}
	
	public static <T> boolean isNull(T[][] arrays, int arrayNo) {
		if (arrays.length <= arrayNo) return false;
		else return arrays[arrayNo] == null;
	}
	
	/* ****** */
	
	public static int sum(int[] numberArray) {
		int result = 0;
		for (int i = 0; i < numberArray.length; i++) result += numberArray[i];
		return result;
	}
	
	public static int sumIntList(IntList numberList) {
		int result = 0;
		for (int i : numberList) result += i;
		return result;
	}
	
	public static double sum(double[] numberArray) {
		double result = 0D;
		for (int i = 0; i < numberArray.length; i++) result += numberArray[i];
		return result;
	}
	
	public static double sumDoubleList(DoubleList numberList) {
		double result = 0D;
		for (double i : numberList) result += i;
		return result;
	}
	
	// Special cases for primitives
	
	public static IntList asIntegerList(int[] array) {
		IntList list = new IntArrayList();
		for (int t : array) list.add(t);
		return list;
	}
	
	public static DoubleList asDoubleList(double[] array) {
		DoubleList list = new DoubleArrayList();
		for (double t : array) list.add(t);
		return list;
	}
	
	public static FloatList asFloatList(float[] array) {
		FloatList list = new FloatArrayList();
		for (float t : array) list.add(t);
		return list;
	}
	
	// List to Array
	
	public static int[] asIntegerArray(IntList list) {
		int[] array = new int[list.size()];
		for (int i = 0; i < list.size(); i++) array[i] = list.get(i);
		return array;
	}
	
	public static double[] asDoubleArray(DoubleList list) {
		double[] array = new double[list.size()];
		for (int i = 0; i < list.size(); i++) array[i] = list.get(i);
		return array;
	}
	
	public static float[] asFloatArray(FloatList list) {
		float[] array = new float[list.size()];
		for (int i = 0; i < list.size(); i++) array[i] = list.get(i);
		return array;
	}
	
	public static String[] asStringArray(List<String> list) {
		String[] array = new String[list.size()];
		for (int i = 0; i < list.size(); i++) array[i] = list.get(i);
		return array;
	}
	
	// Other methods
	
	public static int[] increasingArray(int start, int length) {
		int[] array = new int[length];
		for (int i = 0; i < length; i++) array[i] = start + i;
		return array;
	}
	
	public static int[] increasingArray(int length) {
		return increasingArray(0, length);
	}
	
	public static IntList increasingList(int start, int length) {
		IntList list = new IntArrayList();
		for (int i = 0; i < length; i++) list.add(start + i);
		return list;
	}
	
	public static IntList increasingList(int length) {
		return increasingList(0, length);
	}
	
	/** Returns true if only the selected entries are true */
	public static boolean arrayXAND(boolean[] array, int... i) {
		for (int j = 0; j < array.length; j++) {
			boolean b = 2*i.length > array.length;
			for (int k : i) {
				if (b ^ j == k) {
					b = !b;
					break;
				}
			}
			if (b ^ array[j]) return false;
		}
		return true;
	}
}
