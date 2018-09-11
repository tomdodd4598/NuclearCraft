package nc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SetList<E> extends ArrayList<E> {

	private Set<E> set;

	public SetList() {
		set = new HashSet<E>();
	}

	@Override
	public boolean add(E element) {
		if(set.add(element)) {
			super.add(element);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean remove(Object obj) {
		if(set.remove(obj)) {
			boolean removed = super.remove(obj);
			while (removed && !super.isEmpty()) removed = super.remove(obj);
			return true;
		}
		return false;
	}
	
	@Override
	public E set(int index, E element) {
		E replaced = super.set(index, element);
		set.remove(replaced);
		set.add(element);
		return replaced;
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean bool = set.addAll(c);
		super.clear();
		super.addAll(set);
		return bool;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean bool = set.removeAll(c);
		super.clear();
		super.addAll(set);
		return bool;
	}
	
	@Override
	public boolean isEmpty() {
		boolean bool = set.isEmpty() || super.isEmpty();
		if (bool) {
			set.clear();
			super.clear();
		}
		return bool;
	}
	
	@Override
	public void clear() {
		super.clear();
		set.clear();
	}
}
