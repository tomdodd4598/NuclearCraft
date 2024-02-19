package nc.util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamHelper {
	
	public static <T, U> List<U> map(List<T> list, Function<? super T, ? extends U> function) {
		return list.stream().map(function).collect(Collectors.toList());
	}
	
	public static <T, U> Set<U> map(Set<T> set, Function<? super T, ? extends U> function) {
		return set.stream().map(function).collect(Collectors.toSet());
	}
}
