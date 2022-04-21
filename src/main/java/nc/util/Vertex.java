package nc.util;

import java.util.*;

import it.unimi.dsi.fastutil.objects.*;

/** Modified from jjnguy's answer at https://stackoverflow.com/a/3522481 */
public class Vertex<T> {
	
	public T data;
	public Vertex<T> parent;
	public final List<Vertex<T>> children = new ArrayList<>();
	
	protected Vertex(T data, Vertex<T> parent) {
		this.data = data;
		this.parent = parent;
	}
	
	public Vertex(T data) {
		this(data, null);
	}
	
	public void addChild(T childData) {
		children.add(new Vertex<>(childData, this));
	}
	
	public void addChildren(List<T> childrenData) {
		for (T childData : childrenData) {
			addChild(childData);
		}
	}
	
	public Vertex<T> getRoot() {
		Vertex<T> v = this;
		while (v.parent != null) {
			v = v.parent;
		}
		return v;
	}
	
	/** Will be cut off if/when there is a cycle. */
	public List<T> getPath(boolean rootStart) {
		ObjectSet<Vertex<T>> set = new ObjectOpenHashSet<>();
		List<T> list = new ArrayList<>();
		Vertex<T> v = this;
		while (v != null && !set.contains(v)) {
			set.add(v);
			list.add(v.data);
			v = v.parent;
		}
		if (rootStart) {
			Collections.reverse(list);
		}
		return list;
	}
	
	public boolean partOfCycle() {
		ObjectSet<Vertex<T>> set = new ObjectOpenHashSet<>();
		Vertex<T> v = this;
		while (v != null) {
			if (set.contains(v)) return true;
			set.add(v);
			v = v.parent;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "(" + parent.data + " -> " + data + " -> " + children + ")";
		
	}
}
