package nc.dssl.interpret.element.primitive;

import nc.dssl.DSSLHelpers;
import nc.dssl.interpret.*;
import nc.dssl.interpret.element.*;
import nc.dssl.interpret.element.iter.IterElement;
import nc.dssl.interpret.value.StringValue;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.annotation.*;
import java.util.*;
import java.util.stream.Stream;

public class StringElement extends PrimitiveElement<String, StringValue> {
	
	public StringElement(Interpreter interpreter, @Nonnull String rawValue) {
		super(interpreter, interpreter.builtIn.stringClazz, new StringValue(rawValue));
	}
	
	@Override
	public @Nonnull StringElement stringCast(TokenExecutor exec) {
		return this;
	}
	
	@Override
	public @Nonnull ListElement listCast(TokenExecutor exec) {
		return new ListElement(interpreter, internalIterable(exec));
	}
	
	@Override
	public @Nonnull SetElement setCast(TokenExecutor exec) {
		return new SetElement(interpreter, internalStream(exec).map(x -> x.toKey(exec)));
	}
	
	@Override
	public @Nonnull TokenResult onConcat(TokenExecutor exec, @Nonnull Element other) {
		exec.push(new StringElement(interpreter, toString(exec) + other.stringCast(exec).toString(exec)));
		return TokenResult.PASS;
	}
	
	@Override
	public @Nonnull TokenResult onNot(TokenExecutor exec) {
		throw unaryOpError("!");
	}
	
	@Override
	public @Nonnull IterElement iterator(TokenExecutor exec) {
		return new IterElement(interpreter) {
			
			int index = 0;
			final int length = value.raw.length();
			
			@Override
			public boolean hasNext(TokenExecutor exec) {
				return index < length;
			}
			
			@Override
			public @Nonnull Element next(TokenExecutor exec) {
				return new CharElement(interpreter, value.raw.charAt(index++));
			}
		};
	}
	
	@Override
	public @Nonnull Element iter(TokenExecutor exec) {
		return iterator(exec);
	}
	
	@Override
	public void unpack(TokenExecutor exec) {
		int length = value.raw.length();
		for (int i = 0; i < length; ++i) {
			exec.push(new CharElement(interpreter, value.raw.charAt(i)));
		}
	}
	
	@Override
	public int size(TokenExecutor exec) {
		return value.raw.length();
	}
	
	@Override
	public boolean isEmpty(TokenExecutor exec) {
		return value.raw.isEmpty();
	}
	
	@Override
	public boolean contains(TokenExecutor exec, @Nonnull Element elem) {
		if (!(elem instanceof CharElement) && !(elem instanceof StringElement)) {
			throw new IllegalArgumentException(String.format("Built-in method \"contains\" requires %s or %s element as argument!", BuiltIn.CHAR, BuiltIn.STRING));
		}
		return value.raw.contains(elem.toString(exec));
	}
	
	@Override
	public boolean containsAll(TokenExecutor exec, @Nonnull Element elem) {
		@Nullable Iterable<Element> iterable = elem.internalIterable(exec);
		if (iterable == null) {
			throw new IllegalArgumentException(String.format("Built-in method \"containsAll\" requires %s element as argument!", BuiltIn.ITERABLE));
		}
		for (@Nonnull Element e : iterable) {
			if (!contains(exec, e)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public @Nonnull Element get(TokenExecutor exec, @Nonnull Element elem) {
		return new CharElement(interpreter, value.raw.charAt(methodIndex(exec, elem, "get")));
	}
	
	@SuppressWarnings("null")
	@Override
	public @Nonnull Element slice(TokenExecutor exec, @Nonnull Element elem0, @Nonnull Element elem1) {
		return new StringElement(interpreter, value.raw.substring(methodIndex(exec, elem0, "slice", 1), methodIndex(exec, elem1, "slice", 2)));
	}
	
	@Override
	public @Nonnull Element fst(TokenExecutor exec) {
		return new CharElement(interpreter, value.raw.charAt(0));
	}
	
	@Override
	public @Nonnull Element snd(TokenExecutor exec) {
		return new CharElement(interpreter, value.raw.charAt(1));
	}
	
	@Override
	public @Nonnull Element last(TokenExecutor exec) {
		return new CharElement(interpreter, value.raw.charAt(value.raw.length() - 1));
	}
	
	@Override
	public @Nonnull Element indexOf(TokenExecutor exec, @Nonnull Element elem) {
		if (!(elem instanceof CharElement) && !(elem instanceof StringElement)) {
			throw new IllegalArgumentException(String.format("Built-in method \"indexOf\" requires %s or %s element as argument!", BuiltIn.CHAR, BuiltIn.STRING));
		}
		
		int index = value.raw.indexOf(elem.toString(exec));
		return index < 0 ? interpreter.builtIn.nullElement : new IntElement(interpreter, index);
	}
	
	@Override
	public @Nonnull Element lastIndexOf(TokenExecutor exec, @Nonnull Element elem) {
		if (!(elem instanceof CharElement) && !(elem instanceof StringElement)) {
			throw new IllegalArgumentException(String.format("Built-in method \"lastIndexOf\" requires %s or %s element as argument!", BuiltIn.CHAR, BuiltIn.STRING));
		}
		
		int index = value.raw.lastIndexOf(elem.toString(exec));
		return index < 0 ? interpreter.builtIn.nullElement : new IntElement(interpreter, index);
	}
	
	@Override
	public @Nonnull Element startsWith(TokenExecutor exec, @Nonnull Element elem) {
		if (!(elem instanceof CharElement) && !(elem instanceof StringElement)) {
			throw new IllegalArgumentException(String.format("Built-in method \"startsWith\" requires %s or %s element as argument!", BuiltIn.CHAR, BuiltIn.STRING));
		}
		return new BoolElement(interpreter, value.raw.startsWith(elem.toString(exec)));
	}
	
	@Override
	public @Nonnull Element endsWith(TokenExecutor exec, @Nonnull Element elem) {
		if (!(elem instanceof CharElement) && !(elem instanceof StringElement)) {
			throw new IllegalArgumentException(String.format("Built-in method \"endsWith\" requires %s or %s element as argument!", BuiltIn.CHAR, BuiltIn.STRING));
		}
		return new BoolElement(interpreter, value.raw.endsWith(elem.toString(exec)));
	}
	
	@Override
	public @Nonnull Element matches(TokenExecutor exec, @Nonnull Element elem) {
		if (!(elem instanceof StringElement stringElem)) {
			throw new IllegalArgumentException(String.format("Built-in method \"matches\" requires %s element as argument!", BuiltIn.STRING));
		}
		return new BoolElement(interpreter, value.raw.matches(stringElem.value.raw));
	}
	
	@SuppressWarnings("null")
	@Override
	public @Nonnull Element replace(TokenExecutor exec, @Nonnull Element elem0, @Nonnull Element elem1) {
		if (!(elem0 instanceof StringElement stringElem0)) {
			throw new IllegalArgumentException(String.format("Built-in method \"replace\" requires %s element as first argument!", BuiltIn.STRING));
		}
		
		if (!(elem1 instanceof StringElement stringElem1)) {
			throw new IllegalArgumentException(String.format("Built-in method \"replace\" requires %s element as second argument!", BuiltIn.STRING));
		}
		
		return new StringElement(interpreter, value.raw.replaceAll(stringElem0.value.raw, stringElem1.value.raw));
	}
	
	@SuppressWarnings("null")
	@Override
	public @Nonnull Element split(TokenExecutor exec, @Nonnull Element elem) {
		if (!(elem instanceof StringElement stringElem)) {
			throw new IllegalArgumentException(String.format("Built-in method \"split\" requires %s element as argument!", BuiltIn.STRING));
		}
		return new ListElement(interpreter, Arrays.stream(value.raw.split(stringElem.value.raw, -1)).map(x -> new StringElement(interpreter, x)));
	}
	
	@Override
	public @Nonnull Element lower(TokenExecutor exec) {
		return new StringElement(interpreter, DSSLHelpers.lowerCase(value.raw));
	}
	
	@Override
	public @Nonnull Element upper(TokenExecutor exec) {
		return new StringElement(interpreter, DSSLHelpers.upperCase(value.raw));
	}
	
	@SuppressWarnings("null")
	@Override
	public @Nonnull Element trim(TokenExecutor exec) {
		return new StringElement(interpreter, value.raw.trim());
	}
	
	@SuppressWarnings("null")
	@Override
	public @Nonnull Element format(TokenExecutor exec, @Nonnull Element elem) {
		@Nullable Stream<Element> stream = elem.internalStream(exec);
		if (stream == null) {
			throw new IllegalArgumentException(String.format("Built-in method \"format\" requires %s element as argument!", BuiltIn.ITERABLE));
		}
		return new StringElement(interpreter, String.format(value.raw, stream.map(x -> x.formatted(exec)).toArray()));
	}
	
	@Override
	public @Nonnull String debug(TokenExecutor exec) {
		return "\"" + StringEscapeUtils.escapeJava(value.raw) + "\"";
	}
	
	@Override
	public @Nonnull StringElement __str__(TokenExecutor exec) {
		return this;
	}
	
	@Override
	public @Nonnull StringElement __debug__(TokenExecutor exec) {
		return new StringElement(interpreter, debug(exec));
	}
	
	@Override
	public @Nonnull Element clone() {
		return new StringElement(interpreter, value.raw);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(BuiltIn.STRING, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StringElement other) {
			return value.equals(other.value);
		}
		return false;
	}
}
