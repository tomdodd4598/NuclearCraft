package nc.util;

import java.util.HashMap;
 
public class Permutaions {
	
	// Returns true if arr1[] and arr2[] are permutations of each other
	public static Boolean arePermutations(Object[] arr1, Object[] arr2) {
		// Creates an empty hashMap hM
		HashMap<Object, Integer> hM = new HashMap<Object, Integer>();
		
		// Traverse through the first array and add elements to hash map
		for (int i = 0; i < arr1.length; i++) {
			Object x = arr1[i];
			if (hM.get(x) == null)
				hM.put(x, 1);
			else {
				int k = hM.get(x);
				hM.put(x, k + 1);
			}
		}
		
		// Traverse through second array and check if every element is
		// present in hash map
		for (int i = 0; i < arr2.length; i++) {
			Object x = arr2[i];
			
			// If element is not present in hash map or element
			// is not present less number of times
			if (hM.get(x) == null || hM.get(x) == 0) return false;
 
			int k = hM.get(x);
			hM.put(x, k - 1);
		}
		return true;
	}
}
