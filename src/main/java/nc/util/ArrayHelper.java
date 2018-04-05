package nc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArrayHelper {
	
	public static <T> List<T> asList(T[] array) {
		List<T> list = new ArrayList<T>();
		for (T t : array) list.add(t);
		return list;
	}
	
	public static <T> List<T> intersect(List<T> list1, List<T> list2) {
		List<T> list = new ArrayList<T>();
		for (T t : list1) if(list2.contains(t)) list.add(t);
		return list;
	}
	
	public static <T> List<T> intersect(List<T> first, List<T>... rest) {
		List<T> tList = first;
		for (List<T> list : rest) tList = intersect(tList, list);
		return tList;
	}
	
	public static <T> List<T> union(List<T> first, List<T>... rest) {
		Set<T> set = new HashSet<T>();
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
		List<T> result = first;
		for (List<T> list : rest) result.addAll(list);
		return result;
	}
	
	public static <T> ArrayList<T> concatenate(ArrayList<T> first, ArrayList<T>... rest) {
		ArrayList<T> result = first;
		for (ArrayList<T> list : rest) result.addAll(list);
		return result;
	}
	
	public static <T> boolean isEmpty(T[][] arrays, int arrayNo) {
		if (arrays.length == 0) return true;
		else return arrays[arrayNo].length == 0;
	}
	
	public static int sum(int[] numberArray) {
		int result = 0;
		for (int i = 0; i < numberArray.length; i++) result += numberArray[i];
		return result;
	}
	
	public static double sum(double[] numberArray) {
		double result = 0D;
		for (int i = 0; i < numberArray.length; i++) result += numberArray[i];
		return result;
	}
	
	public static List<Integer> asList(int[] array) {
		List<Integer> list = new ArrayList<Integer>();
		for (int t : array) list.add(t);
		return list;
	}
}
