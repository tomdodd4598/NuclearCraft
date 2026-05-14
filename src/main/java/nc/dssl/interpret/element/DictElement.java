package nc.dssl.interpret.element;

import nc.dssl.interpret.*;
import nc.dssl.interpret.element.iter.IterElement;
import nc.dssl.interpret.element.primitive.StringElement;

import javax.annotation.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DictElement extends Element {
	
	public final Map<ElementKey, Element> value;
	
	@SuppressWarnings("null")
	public <T extends Element> DictElement(TokenExecutor exec, Reverse<T> elems) {
		super(exec.interpreter, exec.interpreter.builtIn.dictClazz);
		int elemCount = elems.size();
		if ((elemCount & 1) == 1) {
			throw new IllegalArgumentException(String.format("Constructor for type \"%s\" requires even number of arguments but received %s!", BuiltIn.DICT, elemCount));
		}
		
		value = new HashMap<>();
		Iterator<T> iter = elems.iterator();
		while (iter.hasNext()) {
			value.put(iter.next().toKey(exec), iter.next());
		}
	}
	
	public DictElement(Interpreter interpreter, Map<ElementKey, Element> map) {
		super(interpreter, interpreter.builtIn.dictClazz);
		value = map;
	}
	
	@Override
	public @Nonnull StringElement stringCast(TokenExecutor exec) {
		return new StringElement(interpreter, toString(exec));
	}
	
	@Override
	public @Nonnull DictElement dictCast(TokenExecutor exec) {
		return this;
	}
	
	@Override
	public @Nonnull IterElement iterator(TokenExecutor exec) {
		return new IterElement(interpreter) {
			
			final Iterator<Entry<ElementKey, Element>> internal = value.entrySet().iterator();
			
			@Override
			public boolean hasNext(TokenExecutor exec) {
				return internal.hasNext();
			}
			
			@SuppressWarnings("null")
			@Override
			public @Nonnull Element next(TokenExecutor exec) {
				Entry<ElementKey, Element> next = internal.next();
				return new ListElement(interpreter, next.getKey().elem, next.getValue());
			}
		};
	}
	
	@Override
	public @Nonnull Element iter(TokenExecutor exec) {
		return iterator(exec);
	}
	
	@SuppressWarnings("null")
	@Override
	public void unpack(TokenExecutor exec) {
		for (Entry<ElementKey, Element> entry : value.entrySet()) {
			exec.push(new ListElement(interpreter, entry.getKey().elem, entry.getValue()));
		}
	}
	
	@Override
	public int size(TokenExecutor exec) {
		return value.size();
	}
	
	@Override
	public boolean isEmpty(TokenExecutor exec) {
		return value.isEmpty();
	}
	
	@Override
	public void remove(TokenExecutor exec, @Nonnull Element elem) {
		value.remove(elem.toKey(exec));
	}
	
	@Override
	public void removeAll(TokenExecutor exec, @Nonnull Element elem) {
		@Nullable Iterable<Element> iterable = elem.internalIterable(exec);
		if (iterable == null) {
			throw new IllegalArgumentException(String.format("Built-in method \"removeAll\" requires %s element as argument!", BuiltIn.ITERABLE));
		}
		for (@Nonnull Element e : iterable) {
			value.remove(e.toKey(exec));
		}
	}
	
	@Override
	public void clear(TokenExecutor exec) {
		value.clear();
	}
	
	@Override
	public @Nonnull Element get(TokenExecutor exec, @Nonnull Element elem) {
		@SuppressWarnings("null") Element get = value.get(elem.toKey(exec));
		return get == null ? interpreter.builtIn.nullElement : get;
	}
	
	@Override
	public void put(TokenExecutor exec, @Nonnull Element elem0, @Nonnull Element elem1) {
		value.put(elem0.toKey(exec), elem1);
	}
	
	@Override
	public void putAll(TokenExecutor exec, @Nonnull Element elem) {
		if (!(elem instanceof DictElement dict)) {
			throw new IllegalArgumentException(String.format("Built-in method \"putAll\" requires %s element as argument!", BuiltIn.DICT));
		}
		value.putAll(dict.value);
	}
	
	@Override
	public void removeEntry(TokenExecutor exec, @Nonnull Element elem0, @Nonnull Element elem1) {
		value.remove(elem0.toKey(exec), elem1);
	}
	
	@Override
	public boolean containsKey(TokenExecutor exec, @Nonnull Element elem) {
		return value.containsKey(elem.toKey(exec));
	}
	
	@Override
	public boolean containsValue(TokenExecutor exec, @Nonnull Element elem) {
		for (@Nonnull Element e : value.values()) {
			if (elem.dynEqualTo(exec, e)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean containsEntry(TokenExecutor exec, @Nonnull Element elem0, @Nonnull Element elem1) {
		@SuppressWarnings("null") Element get = value.get(elem0.toKey(exec));
		return get != null && get.dynEqualTo(exec, elem1);
	}
	
	@Override
	public @Nonnull Element keys(TokenExecutor exec) {
		return new SetElement(exec, value.keySet().stream().map(x -> x.elem));
	}
	
	@Override
	public @Nonnull Element values(TokenExecutor exec) {
		return new ListElement(interpreter, value.values());
	}
	
	@Override
	public @Nonnull Element entries(TokenExecutor exec) {
		return new SetElement(exec, internalIterable(exec));
	}
	
	@SuppressWarnings("null")
	@Override
	public @Nonnull Element clone(TokenExecutor exec) {
		Map<ElementKey, Element> map = new HashMap<>();
		for (Entry<ElementKey, Element> entry : value.entrySet()) {
			map.put(entry.getKey().clone(), entry.getValue().dynClone(exec));
		}
		return new DictElement(interpreter, map);
	}
	
	@SuppressWarnings("null")
	@Override
	public int hash(TokenExecutor exec) {
		int hash = BuiltIn.DICT.hashCode();
		for (Entry<ElementKey, Element> entry : value.entrySet()) {
			hash = 31 * hash + (entry.getKey().hashCode() ^ entry.getValue().dynHash(exec));
		}
		return hash;
	}
	
	@Override
	public @Nonnull String debug(TokenExecutor exec) {
		return "[|...|]";
	}
	
	@Override
	public @Nonnull StringElement __str__(TokenExecutor exec) {
		return stringCast(exec);
	}
	
	@Override
	public @Nonnull StringElement __debug__(TokenExecutor exec) {
		return new StringElement(interpreter, debug(exec));
	}
	
	@Override
	public @Nonnull Element clone() {
		Map<ElementKey, Element> map = new HashMap<>();
		for (Entry<ElementKey, Element> entry : value.entrySet()) {
			map.put(entry.getKey().clone(), entry.getValue().clone());
		}
		return new DictElement(interpreter, map);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(BuiltIn.DICT, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DictElement other) {
			return value.equals(other.value);
		}
		return false;
	}
	
	@Override
	public @Nonnull String toString(TokenExecutor exec) {
		Function<Element, String> f = x -> x.innerString(exec, this);
		return value.entrySet().stream().map(x -> f.apply(x.getKey().elem) + ":" + f.apply(x.getValue())).collect(Collectors.joining(", ", "[|", "|]"));
	}
}
