package nc.dssl.interpret;

import nc.dssl.DSSLConstants;
import nc.dssl.interpret.element.*;
import nc.dssl.interpret.element.bracket.*;
import nc.dssl.interpret.element.primitive.*;

import javax.annotation.*;
import java.util.*;

public class BuiltIn {
	
	public static final String OBJECT = "Object";
	
	public static final String CLASS = "Class";
	public static final String LABEL = "Label";
	public static final String MODULE = "Module";
	
	public static final String BLOCK = "Block";
	public static final String BRACKET = "Bracket";
	public static final String NATIVE = "Native";
	public static final String NULL = "Null";
	
	public static final String PRIMITIVE = "Primitive";
	public static final String ITERABLE = "Iterable";
	
	public static final String INT = "Int";
	public static final String BOOL = "Bool";
	public static final String FLOAT = "Float";
	public static final String CHAR = "Char";
	public static final String STRING = "String";
	
	public static final String RANGE = "Range";
	public static final String LIST = "List";
	public static final String SET = "Set";
	public static final String DICT = "Dict";
	
	public static final String ITER = "Iter";
	
	protected final Interpreter interpreter;
	
	public final Map<String, Clazz> clazzMap = new HashMap<>();
	public final Map<String, Clazz> moduleMap = new HashMap<>();
	
	@SuppressWarnings("all")
	public @Nonnull Clazz objectClazz;
	
	@SuppressWarnings("all")
	public @Nonnull Clazz classClazz;
	@SuppressWarnings("all")
	public @Nonnull Clazz labelClazz;
	@SuppressWarnings("all")
	public @Nonnull Clazz moduleClazz;
	
	@SuppressWarnings("all")
	public @Nonnull Clazz blockClazz;
	@SuppressWarnings("all")
	public @Nonnull Clazz bracketClazz;
	@SuppressWarnings("all")
	public @Nonnull Clazz nativeClazz;
	@SuppressWarnings("all")
	public @Nonnull Clazz nullClazz;
	
	@SuppressWarnings("all")
	public @Nonnull Clazz primitiveClazz;
	@SuppressWarnings("all")
	public @Nonnull Clazz iterableClazz;
	
	@SuppressWarnings("all")
	public @Nonnull Clazz intClazz;
	@SuppressWarnings("all")
	public @Nonnull Clazz boolClazz;
	@SuppressWarnings("all")
	public @Nonnull Clazz floatClazz;
	@SuppressWarnings("all")
	public @Nonnull Clazz charClazz;
	@SuppressWarnings("all")
	public @Nonnull Clazz stringClazz;
	
	@SuppressWarnings("all")
	public @Nonnull Clazz rangeClazz;
	@SuppressWarnings("all")
	public @Nonnull Clazz listClazz;
	@SuppressWarnings("all")
	public @Nonnull Clazz setClazz;
	@SuppressWarnings("all")
	public @Nonnull Clazz dictClazz;
	@SuppressWarnings("all")
	public @Nonnull Clazz iterClazz;
	
	@SuppressWarnings("all")
	public @Nonnull RangeLBracketElement rangeLBracketElement;
	@SuppressWarnings("all")
	public @Nonnull RangeRBracketElement rangeRBracketElement;
	
	@SuppressWarnings("all")
	public @Nonnull ListLBracketElement listLBracketElement;
	@SuppressWarnings("all")
	public @Nonnull ListRBracketElement listRBracketElement;
	
	@SuppressWarnings("all")
	public @Nonnull SetLBracketElement setLBracketElement;
	@SuppressWarnings("all")
	public @Nonnull SetRBracketElement setRBracketElement;
	
	@SuppressWarnings("all")
	public @Nonnull DictLBracketElement dictLBracketElement;
	@SuppressWarnings("all")
	public @Nonnull DictRBracketElement dictRBracketElement;
	
	@SuppressWarnings("all")
	public @Nonnull NullElement nullElement;
	
	@SuppressWarnings("all")
	public @Nonnull Clazz mathModule;
	@SuppressWarnings("all")
	public @Nonnull Clazz constModule;
	@SuppressWarnings("all")
	public @Nonnull Clazz envModule;
	@SuppressWarnings("all")
	public @Nonnull Clazz fsModule;
	
	@SuppressWarnings("null")
	protected BuiltIn(Interpreter interpreter) {
		this.interpreter = interpreter;
	}
	
	protected void init() {
		objectClazz = clazz(new Clazz(interpreter, null, OBJECT, ClazzType.INTERNAL, null, null));
		
		classClazz = clazz(new Clazz(interpreter, CLASS, ClazzType.INTERNAL));
		labelClazz = clazz(new Clazz(interpreter, LABEL, ClazzType.INTERNAL));
		moduleClazz = clazz(new Clazz(interpreter, MODULE, ClazzType.INTERNAL));
		
		blockClazz = clazz(new Clazz(interpreter, BLOCK, ClazzType.INTERNAL));
		bracketClazz = clazz(new Clazz(interpreter, BRACKET, ClazzType.INTERNAL));
		nativeClazz = clazz(new Clazz(interpreter, NATIVE, ClazzType.INTERNAL));
		nullClazz = clazz(new Clazz(interpreter, NULL, ClazzType.INTERNAL));
		
		primitiveClazz = clazz(new Clazz(interpreter, PRIMITIVE, ClazzType.INTERNAL));
		iterableClazz = clazz(new Clazz(interpreter, ITERABLE, ClazzType.INTERNAL));
		
		intClazz = clazz(new Clazz(interpreter, INT, ClazzType.FINAL, primitiveClazz) {
			
			@Override
			public @Nonnull TokenResult instantiate(TokenExecutor exec) {
				return instantiateBuiltIn(exec, this);
			}
			
			@Override
			public @Nullable Element as(TokenExecutor exec, @Nonnull Element elem) {
				return elem.asInt(exec);
			}
			
			@Override
			public @Nonnull Element cast(TokenExecutor exec, @Nonnull Element elem) {
				return elem.intCast(exec);
			}
		});
		
		boolClazz = clazz(new Clazz(interpreter, BOOL, ClazzType.FINAL, primitiveClazz) {
			
			@Override
			public @Nonnull TokenResult instantiate(TokenExecutor exec) {
				return instantiateBuiltIn(exec, this);
			}
			
			@Override
			public @Nullable Element as(TokenExecutor exec, @Nonnull Element elem) {
				return elem.asBool(exec);
			}
			
			@Override
			public @Nonnull Element cast(TokenExecutor exec, @Nonnull Element elem) {
				return elem.boolCast(exec);
			}
		});
		
		floatClazz = clazz(new Clazz(interpreter, FLOAT, ClazzType.FINAL, primitiveClazz) {
			
			@Override
			public @Nonnull TokenResult instantiate(TokenExecutor exec) {
				return instantiateBuiltIn(exec, this);
			}
			
			@Override
			public @Nullable Element as(TokenExecutor exec, @Nonnull Element elem) {
				return elem.asFloat(exec);
			}
			
			@Override
			public @Nonnull Element cast(TokenExecutor exec, @Nonnull Element elem) {
				return elem.floatCast(exec);
			}
		});
		
		charClazz = clazz(new Clazz(interpreter, CHAR, ClazzType.FINAL, primitiveClazz) {
			
			@Override
			public @Nonnull TokenResult instantiate(TokenExecutor exec) {
				return instantiateBuiltIn(exec, this);
			}
			
			@Override
			public @Nullable Element as(TokenExecutor exec, @Nonnull Element elem) {
				return elem.asChar(exec);
			}
			
			@Override
			public @Nonnull Element cast(TokenExecutor exec, @Nonnull Element elem) {
				return elem.charCast(exec);
			}
		});
		
		stringClazz = clazz(new Clazz(interpreter, STRING, ClazzType.FINAL, primitiveClazz, iterableClazz) {
			
			@Override
			public @Nonnull TokenResult instantiate(TokenExecutor exec) {
				return instantiateBuiltIn(exec, this);
			}
			
			@Override
			public @Nullable Element as(TokenExecutor exec, @Nonnull Element elem) {
				return elem.asString(exec);
			}
			
			@Override
			public @Nonnull Element cast(TokenExecutor exec, @Nonnull Element elem) {
				return elem.stringCast(exec);
			}
		});
		
		rangeClazz = clazz(new Clazz(interpreter, RANGE, ClazzType.FINAL, iterableClazz) {
			
			@Override
			public @Nonnull TokenResult instantiate(TokenExecutor exec) {
				return instantiateBuiltIn(exec, this);
			}
			
			@Override
			public @Nonnull Element cast(TokenExecutor exec, @Nonnull Element elem) {
				return elem.rangeCast(exec);
			}
		});
		
		listClazz = clazz(new Clazz(interpreter, LIST, ClazzType.FINAL, iterableClazz) {
			
			@Override
			public @Nonnull TokenResult instantiate(TokenExecutor exec) {
				return instantiateBuiltIn(exec, this);
			}
			
			@Override
			public @Nonnull Element cast(TokenExecutor exec, @Nonnull Element elem) {
				return elem.listCast(exec);
			}
		});
		
		setClazz = clazz(new Clazz(interpreter, SET, ClazzType.FINAL, iterableClazz) {
			
			@Override
			public @Nonnull TokenResult instantiate(TokenExecutor exec) {
				return instantiateBuiltIn(exec, this);
			}
			
			@Override
			public @Nonnull Element cast(TokenExecutor exec, @Nonnull Element elem) {
				return elem.setCast(exec);
			}
		});
		
		dictClazz = clazz(new Clazz(interpreter, DICT, ClazzType.FINAL, iterableClazz) {
			
			@Override
			public @Nonnull TokenResult instantiate(TokenExecutor exec) {
				return instantiateBuiltIn(exec, this);
			}
			
			@Override
			public @Nonnull Element cast(TokenExecutor exec, @Nonnull Element elem) {
				return elem.dictCast(exec);
			}
		});
		
		iterClazz = clazz(new Clazz(interpreter, ITER, ClazzType.FINAL) {
			
			@Override
			public @Nonnull TokenResult instantiate(TokenExecutor exec) {
				@Nonnull Element elem = exec.pop();
				TokenResult result = elem.memberAction(exec, "iter", false);
				if (result == null) {
					throw new IllegalArgumentException(String.format("Constructor for type \"%s\" requires %s element as argument!", ITER, ITERABLE));
				}
				return result;
			}
		});
		
		rangeLBracketElement = new RangeLBracketElement(interpreter);
		rangeRBracketElement = new RangeRBracketElement(interpreter);
		
		listLBracketElement = new ListLBracketElement(interpreter);
		listRBracketElement = new ListRBracketElement(interpreter);
		
		setLBracketElement = new SetLBracketElement(interpreter);
		setRBracketElement = new SetRBracketElement(interpreter);
		
		dictLBracketElement = new DictLBracketElement(interpreter);
		dictRBracketElement = new DictRBracketElement(interpreter);
		
		nullElement = new NullElement(interpreter);
		
		mathModule = module("math");
		constModule = module("const");
		envModule = module("env");
		fsModule = module("fs");
		
		for (Clazz clazz : Arrays.asList(objectClazz)) {
			clazz.setMacro("__init__", x -> {
				x.push(x.pop().__init__(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("__str__", x -> {
				x.push(x.pop().__str__(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("__debug__", x -> {
				x.push(x.pop().__debug__(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("__fmt__", x -> {
				x.push(x.pop().__fmt__(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("__eq__", x -> x.pop().__eq__(x, x.pop()));
			
			clazz.setMacro("__ne__", x -> x.pop().__ne__(x, x.pop()));
			
			clazz.setMacro("__concat__", x -> x.pop().__concat__(x, x.pop()));
			
			clazz.setMacro("clone", x -> {
				x.push(x.pop().clone(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("hash", x -> {
				x.push(new IntElement(interpreter, x.pop().hash(x)));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("scope", x -> {
				x.push(x.pop().scope(x));
				return TokenResult.PASS;
			});
		}
		
		for (Clazz clazz : Arrays.asList(classClazz)) {
			clazz.setMacro("supers", x -> {
				x.push(x.pop().supers(x));
				return TokenResult.PASS;
			});
		}
		
		for (Clazz clazz : Arrays.asList(primitiveClazz)) {
			clazz.setMacro("__lt__", x -> x.pop().__lt__(x, x.pop()));
			
			clazz.setMacro("__le__", x -> x.pop().__le__(x, x.pop()));
			
			clazz.setMacro("__gt__", x -> x.pop().__gt__(x, x.pop()));
			
			clazz.setMacro("__ge__", x -> x.pop().__ge__(x, x.pop()));
			
			clazz.setMacro("__add__", x -> x.pop().__add__(x, x.pop()));
			
			clazz.setMacro("__and__", x -> x.pop().__and__(x, x.pop()));
			
			clazz.setMacro("__or__", x -> x.pop().__or__(x, x.pop()));
			
			clazz.setMacro("__xor__", x -> x.pop().__xor__(x, x.pop()));
			
			clazz.setMacro("__sub__", x -> x.pop().__sub__(x, x.pop()));
			
			clazz.setMacro("__lshift__", x -> x.pop().__lshift__(x, x.pop()));
			
			clazz.setMacro("__rshift__", x -> x.pop().__rshift__(x, x.pop()));
			
			clazz.setMacro("__mul__", x -> x.pop().__mul__(x, x.pop()));
			
			clazz.setMacro("__div__", x -> x.pop().__div__(x, x.pop()));
			
			clazz.setMacro("__rem__", x -> x.pop().__rem__(x, x.pop()));
			
			clazz.setMacro("__pow__", x -> x.pop().__pow__(x, x.pop()));
			
			clazz.setMacro("__floordiv__", x -> x.pop().__floordiv__(x, x.pop()));
			
			clazz.setMacro("__mod__", x -> x.pop().__mod__(x, x.pop()));
			
			clazz.setMacro("__not__", x -> x.pop().__not__(x));
		}
		
		for (Clazz clazz : Arrays.asList(iterableClazz)) {
			clazz.setMacro("iter", x -> {
				x.push(x.pop().iter(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("unpack", x -> {
				x.pop().unpack(x);
				return TokenResult.PASS;
			});
			
			clazz.setMacro("size", x -> {
				x.push(new IntElement(interpreter, x.pop().size(x)));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("isEmpty", x -> {
				x.push(new BoolElement(interpreter, x.pop().isEmpty(x)));
				return TokenResult.PASS;
			});
		}
		
		for (Clazz clazz : Arrays.asList(stringClazz, rangeClazz, listClazz, setClazz)) {
			clazz.setMacro("contains", x -> {
				x.push(new BoolElement(interpreter, x.pop().contains(x, x.pop())));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("containsAll", x -> {
				x.push(new BoolElement(interpreter, x.pop().containsAll(x, x.pop())));
				return TokenResult.PASS;
			});
		}
		
		for (Clazz clazz : Arrays.asList(stringClazz, rangeClazz, listClazz, dictClazz)) {
			clazz.setMacro("get", x -> {
				x.push(x.pop().get(x, x.pop()));
				return TokenResult.PASS;
			});
		}
		
		for (Clazz clazz : Arrays.asList(stringClazz, rangeClazz, listClazz)) {
			clazz.setMacro("slice", x -> {
				@Nonnull Element elem2 = x.pop(), elem1 = x.pop(), elem0 = x.pop();
				x.push(elem2.slice(x, elem0, elem1));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("fst", x -> {
				x.push(x.pop().fst(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("snd", x -> {
				x.push(x.pop().snd(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("last", x -> {
				x.push(x.pop().last(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("indexOf", x -> {
				x.push(x.pop().indexOf(x, x.pop()));
				return TokenResult.PASS;
			});
		}
		
		for (Clazz clazz : Arrays.asList(stringClazz, listClazz)) {
			clazz.setMacro("lastIndexOf", x -> {
				x.push(x.pop().lastIndexOf(x, x.pop()));
				return TokenResult.PASS;
			});
		}
		
		for (Clazz clazz : Arrays.asList(stringClazz)) {
			clazz.setMacro("startsWith", x -> {
				x.push(x.pop().startsWith(x, x.pop()));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("endsWith", x -> {
				x.push(x.pop().endsWith(x, x.pop()));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("matches", x -> {
				x.push(x.pop().matches(x, x.pop()));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("replace", x -> {
				@Nonnull Element elem2 = x.pop(), elem1 = x.pop(), elem0 = x.pop();
				x.push(elem2.replace(x, elem0, elem1));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("split", x -> {
				x.push(x.pop().split(x, x.pop()));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("lower", x -> {
				x.push(x.pop().lower(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("upper", x -> {
				x.push(x.pop().upper(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("trim", x -> {
				x.push(x.pop().trim(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("format", x -> {
				x.push(x.pop().format(x, x.pop()));
				return TokenResult.PASS;
			});
		}
		
		for (Clazz clazz : Arrays.asList(listClazz, setClazz, dictClazz)) {
			clazz.setMacro("remove", x -> {
				x.pop().remove(x, x.pop());
				return TokenResult.PASS;
			});
			
			clazz.setMacro("removeAll", x -> {
				x.pop().removeAll(x, x.pop());
				return TokenResult.PASS;
			});
			
			clazz.setMacro("clear", x -> {
				x.pop().clear(x);
				return TokenResult.PASS;
			});
		}
		
		for (Clazz clazz : Arrays.asList(listClazz)) {
			clazz.setMacro("push", x -> {
				x.pop().push(x, x.pop());
				return TokenResult.PASS;
			});
			
			clazz.setMacro("insert", x -> {
				@Nonnull Element elem2 = x.pop(), elem1 = x.pop(), elem0 = x.pop();
				elem2.insert(x, elem0, elem1);
				return TokenResult.PASS;
			});
			
			clazz.setMacro("pushAll", x -> {
				x.pop().pushAll(x, x.pop());
				return TokenResult.PASS;
			});
			
			clazz.setMacro("insertAll", x -> {
				@Nonnull Element elem2 = x.pop(), elem1 = x.pop(), elem0 = x.pop();
				elem2.insertAll(x, elem0, elem1);
				return TokenResult.PASS;
			});
			
			clazz.setMacro("pop", x -> {
				x.push(x.pop().pop(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("set", x -> {
				@Nonnull Element elem2 = x.pop(), elem1 = x.pop(), elem0 = x.pop();
				elem2.set(x, elem0, elem1);
				return TokenResult.PASS;
			});
			
			clazz.setMacro("removeValue", x -> {
				x.pop().removeValue(x, x.pop());
				return TokenResult.PASS;
			});
			
			clazz.setMacro("reverse", x -> {
				x.pop().reverse(x);
				return TokenResult.PASS;
			});
			
			clazz.setMacro("sort", x -> {
				x.pop().sort(x);
				return TokenResult.PASS;
			});
			
			clazz.setMacro("sortBy", x -> {
				x.pop().sortBy(x, x.pop());
				return TokenResult.PASS;
			});
			
			clazz.setMacro("shuffle", x -> {
				x.pop().shuffle(x);
				return TokenResult.PASS;
			});
		}
		
		for (Clazz clazz : Arrays.asList(setClazz)) {
			clazz.setMacro("add", x -> {
				x.pop().add(x, x.pop());
				return TokenResult.PASS;
			});
			
			clazz.setMacro("addAll", x -> {
				x.pop().addAll(x, x.pop());
				return TokenResult.PASS;
			});
		}
		
		for (Clazz clazz : Arrays.asList(dictClazz)) {
			clazz.setMacro("put", x -> {
				@Nonnull Element elem2 = x.pop(), elem1 = x.pop(), elem0 = x.pop();
				elem2.put(x, elem0, elem1);
				return TokenResult.PASS;
			});
			
			clazz.setMacro("putAll", x -> {
				x.pop().putAll(x, x.pop());
				return TokenResult.PASS;
			});
			
			clazz.setMacro("removeEntry", x -> {
				@Nonnull Element elem2 = x.pop(), elem1 = x.pop(), elem0 = x.pop();
				elem2.removeEntry(x, elem0, elem1);
				return TokenResult.PASS;
			});
			
			clazz.setMacro("containsKey", x -> {
				x.push(new BoolElement(interpreter, x.pop().containsKey(x, x.pop())));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("containsValue", x -> {
				x.push(new BoolElement(interpreter, x.pop().containsValue(x, x.pop())));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("containsEntry", x -> {
				@Nonnull Element elem2 = x.pop(), elem1 = x.pop(), elem0 = x.pop();
				x.push(new BoolElement(interpreter, elem2.containsEntry(x, elem0, elem1)));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("keys", x -> {
				x.push(x.pop().keys(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("values", x -> {
				x.push(x.pop().values(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("entries", x -> {
				x.push(x.pop().entries(x));
				return TokenResult.PASS;
			});
		}
		
		for (Clazz clazz : Arrays.asList(iterClazz)) {
			clazz.setMacro("collectString", x -> {
				x.push(x.pop().collectString(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("collectList", x -> {
				x.push(x.pop().collectList(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("collectSet", x -> {
				x.push(x.pop().collectSet(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("collectDict", x -> {
				x.push(x.pop().collectDict(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("stepBy", x -> {
				x.push(x.pop().stepBy(x, x.pop()));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("chain", x -> {
				x.push(x.pop().chain(x, x.pop()));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("zip", x -> {
				x.push(x.pop().zip(x, x.pop()));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("map", x -> {
				x.push(x.pop().map(x, x.pop()));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("filter", x -> {
				x.push(x.pop().filter(x, x.pop()));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("filterMap", x -> {
				x.push(x.pop().filterMap(x, x.pop()));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("enumerate", x -> {
				x.push(x.pop().enumerate(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("takeWhile", x -> {
				x.push(x.pop().takeWhile(x, x.pop()));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("mapWhile", x -> {
				x.push(x.pop().mapWhile(x, x.pop()));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("skip", x -> {
				x.push(x.pop().skip(x, x.pop()));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("take", x -> {
				x.push(x.pop().take(x, x.pop()));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("flatten", x -> {
				x.push(x.pop().flatten(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("flatMap", x -> {
				x.push(x.pop().flatMap(x, x.pop()));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("chunks", x -> {
				x.push(x.pop().chunks(x, x.pop()));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("count", x -> {
				x.push(new IntElement(interpreter, x.pop().count(x)));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("forEach", x -> {
				x.pop().forEach(x, x.pop());
				return TokenResult.PASS;
			});
			
			clazz.setMacro("all", x -> {
				x.push(new BoolElement(interpreter, x.pop().all(x)));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("any", x -> {
				x.push(new BoolElement(interpreter, x.pop().any(x)));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("min", x -> {
				x.push(x.pop().min(x));
				return TokenResult.PASS;
			});
			
			clazz.setMacro("max", x -> {
				x.push(x.pop().max(x));
				return TokenResult.PASS;
			});
		}
		
		mathModule.setMacro("finite", TokenExecutor::finite);
		mathModule.setMacro("infinite", TokenExecutor::infinite);
		
		mathModule.setMacro("inv", TokenExecutor::inv);
		mathModule.setMacro("neg", TokenExecutor::neg);
		mathModule.setMacro("abs", TokenExecutor::abs);
		mathModule.setMacro("sgn", TokenExecutor::sgn);
		
		mathModule.setMacro("floor", TokenExecutor::floor);
		mathModule.setMacro("ceil", TokenExecutor::ceil);
		mathModule.setMacro("trunc", TokenExecutor::trunc);
		mathModule.setMacro("fract", TokenExecutor::fract);
		
		mathModule.setMacro("round", TokenExecutor::round);
		mathModule.setMacro("places", TokenExecutor::places);
		
		mathModule.setMacro("sin", TokenExecutor::sin);
		mathModule.setMacro("cos", TokenExecutor::cos);
		mathModule.setMacro("tan", TokenExecutor::tan);
		mathModule.setMacro("asin", TokenExecutor::asin);
		mathModule.setMacro("acos", TokenExecutor::acos);
		mathModule.setMacro("atan", TokenExecutor::atan);
		
		mathModule.setMacro("sinc", TokenExecutor::sinc);
		mathModule.setMacro("atan2", TokenExecutor::atan2);
		
		mathModule.setMacro("hypot", TokenExecutor::hypot);
		
		mathModule.setMacro("rads", TokenExecutor::rads);
		mathModule.setMacro("degs", TokenExecutor::degs);
		
		mathModule.setMacro("exp", TokenExecutor::exp);
		mathModule.setMacro("ln", TokenExecutor::ln);
		mathModule.setMacro("log2", TokenExecutor::log2);
		mathModule.setMacro("log10", TokenExecutor::log10);
		mathModule.setMacro("log", TokenExecutor::log);
		
		mathModule.setMacro("expm1", TokenExecutor::expm1);
		mathModule.setMacro("ln1p", TokenExecutor::ln1p);
		
		mathModule.setMacro("sqrt", TokenExecutor::sqrt);
		mathModule.setMacro("cbrt", TokenExecutor::cbrt);
		mathModule.setMacro("root", TokenExecutor::root);
		
		mathModule.setMacro("isqrt", TokenExecutor::isqrt);
		mathModule.setMacro("icbrt", TokenExecutor::icbrt);
		mathModule.setMacro("iroot", TokenExecutor::iroot);
		
		mathModule.setMacro("sinh", TokenExecutor::sinh);
		mathModule.setMacro("cosh", TokenExecutor::cosh);
		mathModule.setMacro("tanh", TokenExecutor::tanh);
		mathModule.setMacro("asinh", TokenExecutor::asinh);
		mathModule.setMacro("acosh", TokenExecutor::acosh);
		mathModule.setMacro("atanh", TokenExecutor::atanh);
		
		mathModule.setMacro("min", TokenExecutor::min);
		mathModule.setMacro("max", TokenExecutor::max);
		mathModule.setMacro("clamp", TokenExecutor::clamp);
		
		mathModule.setMacro("clamp8", TokenExecutor::clamp8);
		mathModule.setMacro("clamp16", TokenExecutor::clamp16);
		mathModule.setMacro("clamp32", TokenExecutor::clamp32);
		mathModule.setMacro("clamp64", TokenExecutor::clamp64);
		
		constModule.setDef("MIN_INT_8", new IntElement(interpreter, DSSLConstants.MIN_INT_8), true);
		constModule.setDef("MAX_INT_8", new IntElement(interpreter, DSSLConstants.MAX_INT_8), true);
		
		constModule.setDef("MIN_INT_16", new IntElement(interpreter, DSSLConstants.MIN_INT_16), true);
		constModule.setDef("MAX_INT_16", new IntElement(interpreter, DSSLConstants.MAX_INT_16), true);
		
		constModule.setDef("MIN_INT_32", new IntElement(interpreter, DSSLConstants.MIN_INT_32), true);
		constModule.setDef("MAX_INT_32", new IntElement(interpreter, DSSLConstants.MAX_INT_32), true);
		
		constModule.setDef("MIN_INT_64", new IntElement(interpreter, DSSLConstants.MIN_INT_64), true);
		constModule.setDef("MAX_INT_64", new IntElement(interpreter, DSSLConstants.MAX_INT_64), true);
		
		constModule.setDef("INF", new FloatElement(interpreter, DSSLConstants.INF), true);
		constModule.setDef("NAN", new FloatElement(interpreter, DSSLConstants.NAN), true);
		constModule.setDef("MIN_F", new FloatElement(interpreter, DSSLConstants.MIN_F), true);
		constModule.setDef("MAX_F", new FloatElement(interpreter, DSSLConstants.MAX_F), true);
		constModule.setDef("EPSILON", new FloatElement(interpreter, DSSLConstants.EPSILON), true);
		
		constModule.setDef("E", new FloatElement(interpreter, DSSLConstants.E), true);
		
		constModule.setDef("PI", new FloatElement(interpreter, DSSLConstants.PI), true);
		constModule.setDef("TAU", new FloatElement(interpreter, DSSLConstants.TAU), true);
		
		constModule.setDef("INV_PI", new FloatElement(interpreter, DSSLConstants.INV_PI), true);
		constModule.setDef("INV_TAU", new FloatElement(interpreter, DSSLConstants.INV_TAU), true);
		
		constModule.setDef("SQRT_PI", new FloatElement(interpreter, DSSLConstants.SQRT_PI), true);
		constModule.setDef("SQRT_TAU", new FloatElement(interpreter, DSSLConstants.SQRT_TAU), true);
		
		constModule.setDef("INV_SQRT_PI", new FloatElement(interpreter, DSSLConstants.INV_SQRT_PI), true);
		constModule.setDef("INV_SQRT_TAU", new FloatElement(interpreter, DSSLConstants.INV_SQRT_TAU), true);
		
		constModule.setDef("INV_3", new FloatElement(interpreter, DSSLConstants.INV_3), true);
		constModule.setDef("INV_6", new FloatElement(interpreter, DSSLConstants.INV_6), true);
		
		constModule.setDef("SQRT_2", new FloatElement(interpreter, DSSLConstants.SQRT_2), true);
		constModule.setDef("SQRT_3", new FloatElement(interpreter, DSSLConstants.SQRT_3), true);
		constModule.setDef("SQRT_6", new FloatElement(interpreter, DSSLConstants.SQRT_6), true);
		constModule.setDef("SQRT_8", new FloatElement(interpreter, DSSLConstants.SQRT_8), true);
		
		constModule.setDef("INV_SQRT_2", new FloatElement(interpreter, DSSLConstants.INV_SQRT_2), true);
		constModule.setDef("INV_SQRT_3", new FloatElement(interpreter, DSSLConstants.INV_SQRT_3), true);
		constModule.setDef("INV_SQRT_6", new FloatElement(interpreter, DSSLConstants.INV_SQRT_6), true);
		constModule.setDef("INV_SQRT_8", new FloatElement(interpreter, DSSLConstants.INV_SQRT_8), true);
		
		constModule.setDef("FRAC_PI_2", new FloatElement(interpreter, DSSLConstants.FRAC_PI_2), true);
		constModule.setDef("FRAC_PI_3", new FloatElement(interpreter, DSSLConstants.FRAC_PI_3), true);
		constModule.setDef("FRAC_PI_4", new FloatElement(interpreter, DSSLConstants.FRAC_PI_4), true);
		constModule.setDef("FRAC_PI_6", new FloatElement(interpreter, DSSLConstants.FRAC_PI_6), true);
		constModule.setDef("FRAC_PI_8", new FloatElement(interpreter, DSSLConstants.FRAC_PI_8), true);
		
		constModule.setDef("FRAC_2_PI", new FloatElement(interpreter, DSSLConstants.FRAC_2_PI), true);
		constModule.setDef("FRAC_2_SQRT_PI", new FloatElement(interpreter, DSSLConstants.FRAC_2_SQRT_PI), true);
		
		constModule.setDef("LN_2", new FloatElement(interpreter, DSSLConstants.LN_2), true);
		constModule.setDef("LN_10", new FloatElement(interpreter, DSSLConstants.LN_10), true);
		constModule.setDef("LOG2_E", new FloatElement(interpreter, DSSLConstants.LOG2_E), true);
		constModule.setDef("LOG2_10", new FloatElement(interpreter, DSSLConstants.LOG2_10), true);
		constModule.setDef("LOG10_E", new FloatElement(interpreter, DSSLConstants.LOG10_E), true);
		constModule.setDef("LOG10_2", new FloatElement(interpreter, DSSLConstants.LOG10_2), true);
		
		envModule.setMacro("args", TokenExecutor::args);
		
		envModule.setMacro("rootPath", TokenExecutor::rootPath);
		envModule.setMacro("rootDir", TokenExecutor::rootDir);
		
		envModule.setMacro("fromRoot", TokenExecutor::fromRoot);
		
		fsModule.setMacro("readFile", TokenExecutor::readFile);
		fsModule.setMacro("writeFile", TokenExecutor::writeFile);
		
		fsModule.setMacro("readLines", TokenExecutor::readLines);
		fsModule.setMacro("writeLines", TokenExecutor::writeLines);
	}
	
	protected @Nonnull Clazz clazz(@Nonnull Clazz clazz) {
		clazzMap.put(clazz.fullIdentifier, clazz);
		return clazz;
	}
	
	protected @Nonnull Clazz module(@Nonnull String identifier) {
		Clazz module = new Clazz(interpreter, identifier, ClazzType.INTERNAL);
		moduleMap.put(identifier, module);
		return module;
	}
	
	protected @Nonnull TokenResult instantiateBuiltIn(TokenExecutor exec, Clazz clazz) {
		Element casted = clazz.as(exec, exec.pop());
		if (casted == null) {
			throw new IllegalArgumentException(String.format("Constructor for type \"%1$s\" requires %1$s element as argument!", clazz.fullIdentifier));
		}
		exec.push(casted);
		return TokenResult.PASS;
	}
	
	public static final Set<String> KEYWORDS = new HashSet<>();
	
	static {
		KEYWORDS.add("{");
		KEYWORDS.add("}");
		
		KEYWORDS.add("(");
		KEYWORDS.add(")");
		
		KEYWORDS.add("[");
		KEYWORDS.add("]");
		
		KEYWORDS.add("(|");
		KEYWORDS.add("|)");
		
		KEYWORDS.add("[|");
		KEYWORDS.add("|]");
		
		KEYWORDS.add("include");
		KEYWORDS.add("import");
		
		KEYWORDS.add("native");
		
		KEYWORDS.add("def");
		KEYWORDS.add("macro");
		KEYWORDS.add("class");
		
		KEYWORDS.add("delete");
		
		KEYWORDS.add("new");
		
		KEYWORDS.add("deref");
		
		KEYWORDS.add("null");
		KEYWORDS.add("type");
		KEYWORDS.add("cast");
		KEYWORDS.add("is");
		
		KEYWORDS.add("exch");
		KEYWORDS.add("roll");
		KEYWORDS.add("pop");
		KEYWORDS.add("dup");
		
		KEYWORDS.add("stacksize");
		KEYWORDS.add("stackindex");
		
		KEYWORDS.add("read");
		KEYWORDS.add("print");
		KEYWORDS.add("println");
		KEYWORDS.add("interpret");
		
		KEYWORDS.add("exec");
		KEYWORDS.add("if");
		KEYWORDS.add("ifelse");
		KEYWORDS.add("loop");
		KEYWORDS.add("repeat");
		KEYWORDS.add("foreach");
		
		KEYWORDS.add("continue");
		KEYWORDS.add("break");
		KEYWORDS.add("quit");
		
		KEYWORDS.add("=");
		
		KEYWORDS.add("++");
		KEYWORDS.add("--");
		
		KEYWORDS.add("+=");
		KEYWORDS.add("&=");
		KEYWORDS.add("|=");
		KEYWORDS.add("^=");
		KEYWORDS.add("-=");
		KEYWORDS.add("~=");
		
		KEYWORDS.add("<<=");
		KEYWORDS.add(">>=");
		
		KEYWORDS.add("*=");
		KEYWORDS.add("/=");
		KEYWORDS.add("%=");
		KEYWORDS.add("**=");
		KEYWORDS.add("//=");
		KEYWORDS.add("%%=");
		
		KEYWORDS.add("==");
		KEYWORDS.add("!=");
		
		KEYWORDS.add("<");
		KEYWORDS.add("<=");
		KEYWORDS.add(">");
		KEYWORDS.add(">=");
		
		KEYWORDS.add("+");
		KEYWORDS.add("&");
		KEYWORDS.add("|");
		KEYWORDS.add("^");
		KEYWORDS.add("-");
		KEYWORDS.add("~");
		
		KEYWORDS.add("<<");
		KEYWORDS.add(">>");
		
		KEYWORDS.add("*");
		KEYWORDS.add("/");
		KEYWORDS.add("%");
		KEYWORDS.add("**");
		KEYWORDS.add("//");
		KEYWORDS.add("%%");
		
		KEYWORDS.add("!");
	}
}
