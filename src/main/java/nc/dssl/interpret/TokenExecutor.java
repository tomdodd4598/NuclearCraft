package nc.dssl.interpret;

import nc.dssl.*;
import nc.dssl.interpret.element.*;
import nc.dssl.interpret.element.bracket.*;
import nc.dssl.interpret.element.clazz.ClassElement;
import nc.dssl.interpret.element.iter.IterElement;
import nc.dssl.interpret.element.primitive.*;
import nc.dssl.interpret.token.BlockToken;
import nc.dssl.node.*;

import javax.annotation.*;
import java.math.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

public class TokenExecutor extends TokenReader implements HierarchicalScope {
	
	protected final DSSLHierarchy<String, Def> defHierarchy;
	protected final DSSLHierarchy<String, Macro> macroHierarchy;
	protected final DSSLHierarchy<String, Clazz> clazzHierarchy;
	
	protected TokenExecutor(Interpreter interpreter, TokenIterator iterator) {
		super(interpreter, iterator);
		defHierarchy = new DSSLHierarchy<>();
		macroHierarchy = new DSSLHierarchy<>();
		clazzHierarchy = new DSSLHierarchy<>();
		prelude();
	}
	
	public TokenExecutor(TokenIterator iterator, TokenExecutor prev, boolean child) {
		super(iterator, prev);
		defHierarchy = prev.defHierarchy.copy(child);
		macroHierarchy = prev.macroHierarchy.copy(child);
		clazzHierarchy = prev.clazzHierarchy.copy(child);
	}
	
	protected void prelude() {
		interpreter.builtIn.init();
		interpreter.builtIn.clazzMap.forEach((k, v) -> getClazzHierarchy().put(k, v, true));
	}
	
	@Override
	public @Nonnull TokenResult iterate() {
		while (iterator.hasNext()) {
			@Nonnull TokenResult readResult = read(iterator.next());
			switch (readResult) {
				case PASS:
					continue;
				case CONTINUE:
				case BREAK:
					if (isRoot()) {
						throw new IllegalArgumentException(String.format("Keyword \"%s\" can not be used by the root executor!", readResult));
					}
				default:
					return readResult;
			}
		}
		return TokenResult.PASS;
	}
	
	@Override
	protected @Nonnull TokenResult read(@Nonnull Token token) {
		if (interpreter.halt) {
			return TokenResult.QUIT;
		}
		@Nonnull TokenResult result = TOKEN_FUNCTION_MAP.apply(this, token);
		if (interpreter.debug && !DSSLHelpers.isSeparator(token)) {
			interpreter.hooks.debug(token.getText().trim().replaceAll("\\s+", " ") + " -> " + debug() + "\n");
		}
		
		for (String str : interpreter.printList) {
			interpreter.hooks.print(str);
		}
		interpreter.printList.clear();
		
		return result;
	}
	
	@Override
	public @Nullable String scopeIdentifier() {
		return null;
	}
	
	@Override
	public boolean canShadow() {
		return true;
	}
	
	@Override
	public boolean canDelete() {
		return true;
	}
	
	@Override
	public void checkCollision(@Nonnull String identifier) {
		if (BuiltIn.KEYWORDS.contains(identifier)) {
			throw new IllegalArgumentException(String.format("Identifier \"%s\" already used for keyword!", identifier));
		}
		HierarchicalScope.super.checkCollision(identifier);
	}
	
	@Override
	public DSSLHierarchy<String, Def> getDefHierarchy() {
		return defHierarchy;
	}
	
	@Override
	public DSSLHierarchy<String, Macro> getMacroHierarchy() {
		return macroHierarchy;
	}
	
	@Override
	public DSSLHierarchy<String, Clazz> getClazzHierarchy() {
		return clazzHierarchy;
	}
	
	public boolean isRoot() {
		return this == interpreter.root;
	}
	
	protected Deque<Element> stack() {
		return interpreter.stack;
	}
	
	public void push(@Nonnull Element elem) {
		stack().push(elem);
	}
	
	@SuppressWarnings("unused")
	public @Nonnull Element peek() {
		@SuppressWarnings("null") Element peek = stack().peek();
		if (peek == null) {
			throw new NoSuchElementException();
		}
		return peek;
	}
	
	@SuppressWarnings("unused")
	public @Nonnull Element[] peek(int count) {
		@Nonnull Element[] elems = new Element[count];
		Iterator<Element> iter = stack().iterator();
		int i = 0;
		while (i < count) {
			@SuppressWarnings("null") Element next = iter.next();
			if (next == null) {
				throw new NoSuchElementException();
			}
			else {
				elems[count - ++i] = next;
			}
		}
		return elems;
	}
	
	@SuppressWarnings("unused")
	public @Nonnull Element peekAt(int index) {
		@Nonnull Element elem;
		Iterator<Element> iter = stack().iterator();
		int i = 0;
		do {
			@SuppressWarnings("null") Element next = iter.next();
			if (next == null) {
				throw new NoSuchElementException();
			}
			else {
				elem = next;
			}
		}
		while (i++ < index);
		return elem;
	}
	
	@SuppressWarnings("null")
	public @Nonnull Element pop() {
		return stack().pop();
	}
	
	public @Nonnull Element[] pop(int count) {
		@Nonnull Element[] elems = new Element[count];
		for (int i = 0; i < count; ++i) {
			elems[count - i - 1] = pop();
		}
		return elems;
	}
	
	public int stackSize() {
		return stack().size();
	}
	
	protected String debug() {
		return DSSLHelpers.stream(() -> stack().descendingIterator()).map(x -> x.debug(this)).collect(Collectors.joining(" "));
	}
	
	@FunctionalInterface
	protected interface TokenFunction {
		
		@Nonnull
		TokenResult apply(TokenExecutor exec, @Nonnull Token token);
	}
	
	protected static class TokenFunctionMap {
		
		protected final Map<Class<? extends Token>, TokenFunction> internal = new HashMap<>();
		
		protected <T extends Token> void put(Class<T> clazz, TokenFunction function) {
			internal.put(clazz, function);
		}
		
		protected <T extends Token> TokenFunction get(Class<T> clazz) {
			return internal.get(clazz);
		}
		
		protected <T extends Token> @Nonnull TokenResult apply(TokenExecutor exec, @Nonnull T token) {
			@SuppressWarnings("unchecked") Class<T> clazz = (Class<T>) token.getClass();
			TokenFunction function = get(clazz);
			if (function == null) {
				throw new IllegalArgumentException(String.format("Encountered unsupported %s token \"%s\"!", clazz.getSimpleName(), token.getText()));
			}
			else {
				return function.apply(exec, token);
			}
		}
	}
	
	protected static final TokenFunctionMap TOKEN_FUNCTION_MAP = new TokenFunctionMap();
	
	static {
		TOKEN_FUNCTION_MAP.put(TBlank.class, TokenExecutor::onBlank);
		TOKEN_FUNCTION_MAP.put(TComment.class, TokenExecutor::onComment);
		
		TOKEN_FUNCTION_MAP.put(TLBrace.class, TokenExecutor::onLBrace);
		TOKEN_FUNCTION_MAP.put(TRBrace.class, TokenExecutor::onRBrace);
		
		TOKEN_FUNCTION_MAP.put(TDictLBracket.class, TokenExecutor::onDictLBracket);
		TOKEN_FUNCTION_MAP.put(TDictRBracket.class, TokenExecutor::onDictRBracket);
		
		TOKEN_FUNCTION_MAP.put(TSetLBracket.class, TokenExecutor::onSetLBracket);
		TOKEN_FUNCTION_MAP.put(TSetRBracket.class, TokenExecutor::onSetRBracket);
		
		TOKEN_FUNCTION_MAP.put(TListLBracket.class, TokenExecutor::onListLBracket);
		TOKEN_FUNCTION_MAP.put(TListRBracket.class, TokenExecutor::onListRBracket);
		
		TOKEN_FUNCTION_MAP.put(TRangeLBracket.class, TokenExecutor::onRangeLBracket);
		TOKEN_FUNCTION_MAP.put(TRangeRBracket.class, TokenExecutor::onRangeRBracket);
		
		TOKEN_FUNCTION_MAP.put(TInclude.class, TokenExecutor::onInclude);
		TOKEN_FUNCTION_MAP.put(TImport.class, TokenExecutor::onImport);
		
		TOKEN_FUNCTION_MAP.put(TNative.class, TokenExecutor::onNative);
		
		TOKEN_FUNCTION_MAP.put(TDef.class, TokenExecutor::onDef);
		TOKEN_FUNCTION_MAP.put(TMacro.class, TokenExecutor::onMacro);
		TOKEN_FUNCTION_MAP.put(TClass.class, TokenExecutor::onClass);
		
		TOKEN_FUNCTION_MAP.put(TDeref.class, TokenExecutor::onDeref);
		
		TOKEN_FUNCTION_MAP.put(TDelete.class, TokenExecutor::onDelete);
		
		TOKEN_FUNCTION_MAP.put(TNew.class, TokenExecutor::onNew);
		
		TOKEN_FUNCTION_MAP.put(TNull.class, TokenExecutor::onNull);
		TOKEN_FUNCTION_MAP.put(TType.class, TokenExecutor::onType);
		TOKEN_FUNCTION_MAP.put(TCast.class, TokenExecutor::onCast);
		TOKEN_FUNCTION_MAP.put(TIs.class, TokenExecutor::onIs);
		
		TOKEN_FUNCTION_MAP.put(TExch.class, TokenExecutor::onExch);
		TOKEN_FUNCTION_MAP.put(TRoll.class, TokenExecutor::onRoll);
		TOKEN_FUNCTION_MAP.put(TPop.class, TokenExecutor::onPop);
		TOKEN_FUNCTION_MAP.put(TDup.class, TokenExecutor::onDup);
		
		TOKEN_FUNCTION_MAP.put(TStacksize.class, TokenExecutor::onStacksize);
		TOKEN_FUNCTION_MAP.put(TStackindex.class, TokenExecutor::onStackindex);
		
		TOKEN_FUNCTION_MAP.put(TRead.class, TokenExecutor::onRead);
		TOKEN_FUNCTION_MAP.put(TPrint.class, TokenExecutor::onPrint);
		TOKEN_FUNCTION_MAP.put(TPrintln.class, TokenExecutor::onPrintln);
		TOKEN_FUNCTION_MAP.put(TInterpret.class, TokenExecutor::onInterpret);
		
		TOKEN_FUNCTION_MAP.put(TExec.class, TokenExecutor::onExec);
		TOKEN_FUNCTION_MAP.put(TIf.class, TokenExecutor::onIf);
		TOKEN_FUNCTION_MAP.put(TIfelse.class, TokenExecutor::onIfelse);
		TOKEN_FUNCTION_MAP.put(TLoop.class, TokenExecutor::onLoop);
		TOKEN_FUNCTION_MAP.put(TRepeat.class, TokenExecutor::onRepeat);
		TOKEN_FUNCTION_MAP.put(TForeach.class, TokenExecutor::onForeach);
		
		TOKEN_FUNCTION_MAP.put(TContinue.class, TokenExecutor::onContinue);
		TOKEN_FUNCTION_MAP.put(TBreak.class, TokenExecutor::onBreak);
		TOKEN_FUNCTION_MAP.put(TQuit.class, TokenExecutor::onQuit);
		
		TOKEN_FUNCTION_MAP.put(TEquals.class, TokenExecutor::onEquals);
		
		TOKEN_FUNCTION_MAP.put(TIncrement.class, TokenExecutor::onIncrement);
		TOKEN_FUNCTION_MAP.put(TDecrement.class, TokenExecutor::onDecrement);
		
		TOKEN_FUNCTION_MAP.put(TPlusEquals.class, TokenExecutor::onPlusEquals);
		TOKEN_FUNCTION_MAP.put(TAndEquals.class, TokenExecutor::onAndEquals);
		TOKEN_FUNCTION_MAP.put(TOrEquals.class, TokenExecutor::onOrEquals);
		TOKEN_FUNCTION_MAP.put(TXorEquals.class, TokenExecutor::onXorEquals);
		TOKEN_FUNCTION_MAP.put(TMinusEquals.class, TokenExecutor::onMinusEquals);
		TOKEN_FUNCTION_MAP.put(TConcatEquals.class, TokenExecutor::onConcatEquals);
		
		TOKEN_FUNCTION_MAP.put(TLeftShiftEquals.class, TokenExecutor::onLeftShiftEquals);
		TOKEN_FUNCTION_MAP.put(TRightShiftEquals.class, TokenExecutor::onRightShiftEquals);
		
		TOKEN_FUNCTION_MAP.put(TMultiplyEquals.class, TokenExecutor::onMultiplyEquals);
		TOKEN_FUNCTION_MAP.put(TDivideEquals.class, TokenExecutor::onDivideEquals);
		TOKEN_FUNCTION_MAP.put(TRemainderEquals.class, TokenExecutor::onRemainderEquals);
		TOKEN_FUNCTION_MAP.put(TPowerEquals.class, TokenExecutor::onPowerEquals);
		TOKEN_FUNCTION_MAP.put(TIdivideEquals.class, TokenExecutor::onIdivideEquals);
		TOKEN_FUNCTION_MAP.put(TModuloEquals.class, TokenExecutor::onModuloEquals);
		
		TOKEN_FUNCTION_MAP.put(TEqualTo.class, TokenExecutor::onEqualTo);
		TOKEN_FUNCTION_MAP.put(TNotEqualTo.class, TokenExecutor::onNotEqualTo);
		
		TOKEN_FUNCTION_MAP.put(TLessThan.class, TokenExecutor::onLessThan);
		TOKEN_FUNCTION_MAP.put(TLessOrEqual.class, TokenExecutor::onLessOrEqual);
		TOKEN_FUNCTION_MAP.put(TMoreThan.class, TokenExecutor::onMoreThan);
		TOKEN_FUNCTION_MAP.put(TMoreOrEqual.class, TokenExecutor::onMoreOrEqual);
		
		TOKEN_FUNCTION_MAP.put(TPlus.class, TokenExecutor::onPlus);
		TOKEN_FUNCTION_MAP.put(TAnd.class, TokenExecutor::onAnd);
		TOKEN_FUNCTION_MAP.put(TOr.class, TokenExecutor::onOr);
		TOKEN_FUNCTION_MAP.put(TXor.class, TokenExecutor::onXor);
		TOKEN_FUNCTION_MAP.put(TMinus.class, TokenExecutor::onMinus);
		TOKEN_FUNCTION_MAP.put(TConcat.class, TokenExecutor::onConcat);
		
		TOKEN_FUNCTION_MAP.put(TLeftShift.class, TokenExecutor::onLeftShift);
		TOKEN_FUNCTION_MAP.put(TRightShift.class, TokenExecutor::onRightShift);
		
		TOKEN_FUNCTION_MAP.put(TMultiply.class, TokenExecutor::onMultiply);
		TOKEN_FUNCTION_MAP.put(TDivide.class, TokenExecutor::onDivide);
		TOKEN_FUNCTION_MAP.put(TRemainder.class, TokenExecutor::onRemainder);
		TOKEN_FUNCTION_MAP.put(TPower.class, TokenExecutor::onPower);
		TOKEN_FUNCTION_MAP.put(TIdivide.class, TokenExecutor::onIdivide);
		TOKEN_FUNCTION_MAP.put(TModulo.class, TokenExecutor::onModulo);
		
		TOKEN_FUNCTION_MAP.put(TNot.class, TokenExecutor::onNot);
		
		TOKEN_FUNCTION_MAP.put(TIntValue.class, TokenExecutor::onIntValue);
		TOKEN_FUNCTION_MAP.put(TBoolValue.class, TokenExecutor::onBoolValue);
		TOKEN_FUNCTION_MAP.put(TFloatValue.class, TokenExecutor::onFloatValue);
		TOKEN_FUNCTION_MAP.put(TCharValue.class, TokenExecutor::onCharValue);
		
		TOKEN_FUNCTION_MAP.put(TBlockStringValue.class, TokenExecutor::onBlockStringValue);
		TOKEN_FUNCTION_MAP.put(TLineStringValue.class, TokenExecutor::onLineStringValue);
		
		TOKEN_FUNCTION_MAP.put(TIdentifier.class, TokenExecutor::onIdentifier);
		TOKEN_FUNCTION_MAP.put(TLabel.class, TokenExecutor::onLabel);
		TOKEN_FUNCTION_MAP.put(TMember.class, TokenExecutor::onMember);
		TOKEN_FUNCTION_MAP.put(TModule.class, TokenExecutor::onModule);
		
		TOKEN_FUNCTION_MAP.put(BlockToken.class, TokenExecutor::onBlock);
	}
	
	// KEYWORDS
	
	protected @Nonnull TokenResult onBlank(@Nonnull Token token) {
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onComment(@Nonnull Token token) {
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onLBrace(@Nonnull Token token) {
		TokenCollector collector = new TokenCollector(interpreter, iterator);
		collector.iterate();
		push(new BlockElement(interpreter, collector.listStack.pop()));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onRBrace(@Nonnull Token token) {
		throw new IllegalArgumentException("Encountered \"}\" token without corresponding \"{\" token!");
	}
	
	protected @Nonnull TokenResult onDictLBracket(@Nonnull Token token) {
		push(interpreter.builtIn.dictLBracketElement);
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onDictRBracket(@Nonnull Token token) {
		push(new DictElement(this, getElemsToLBracket(interpreter.builtIn.dictLBracketElement, interpreter.builtIn.dictRBracketElement)));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onSetLBracket(@Nonnull Token token) {
		push(interpreter.builtIn.setLBracketElement);
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onSetRBracket(@Nonnull Token token) {
		push(new SetElement(this, getElemsToLBracket(interpreter.builtIn.setLBracketElement, interpreter.builtIn.setRBracketElement)));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onListLBracket(@Nonnull Token token) {
		push(interpreter.builtIn.listLBracketElement);
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onListRBracket(@Nonnull Token token) {
		push(new ListElement(interpreter, getElemsToLBracket(interpreter.builtIn.listLBracketElement, interpreter.builtIn.listRBracketElement)));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onRangeLBracket(@Nonnull Token token) {
		push(interpreter.builtIn.rangeLBracketElement);
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onRangeRBracket(@Nonnull Token token) {
		push(new RangeElement(this, getElemsToLBracket(interpreter.builtIn.rangeLBracketElement, interpreter.builtIn.rangeRBracketElement)));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onInclude(@Nonnull Token token) {
		return interpreter.hooks.onInclude(this);
	}
	
	protected @Nonnull TokenResult onImport(@Nonnull Token token) {
		return interpreter.hooks.onImport(this);
	}
	
	protected @Nonnull TokenResult onNative(@Nonnull Token token) {
		return interpreter.hooks.onNative(this);
	}
	
	protected @Nonnull TokenResult onDef(@Nonnull Token token) {
		assign(pop(), pop(), AssignmentType.DEF);
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onMacro(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		if (!(elem0 instanceof LabelElement label)) {
			throw new IllegalArgumentException(String.format("Keyword \"macro\" requires %s element as first argument!", BuiltIn.LABEL));
		}
		if (!(elem1 instanceof BlockElement block)) {
			throw new IllegalArgumentException(String.format("Keyword \"macro\" requires %s element as second argument!", BuiltIn.BLOCK));
		}
		
		label.setMacro(block);
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onClass(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0;
		if (!(elem1 instanceof BlockElement block)) {
			throw new IllegalArgumentException(String.format("Keyword \"class\" requires %s element as last argument!", BuiltIn.BLOCK));
		}
		
		ArrayList<Clazz> supers = new ArrayList<>();
		while ((elem0 = pop()) instanceof ClassElement clazz) {
			supers.add(clazz.internal);
		}
		Collections.reverse(supers);
		
		if (!(elem0 instanceof LabelElement label)) {
			throw new IllegalArgumentException(String.format("Keyword \"class\" requires %s element as first argument!", BuiltIn.LABEL));
		}
		
		for (Clazz clazz : supers) {
			if (!clazz.type.canExtend()) {
				throw new IllegalArgumentException(String.format("Class \"%s\" can not extend class \"%s\"!", label.fullIdentifier, clazz.fullIdentifier));
			}
		}
		
		TokenExecutor clazzExec = block.executor(this);
		label.setClazz(ClazzType.STANDARD, clazzExec, supers);
		return clazzExec.iterate();
	}
	
	protected @Nonnull TokenResult onDeref(@Nonnull Token token) {
		@Nonnull Element elem = pop();
		if (!(elem instanceof LabelElement label)) {
			throw new IllegalArgumentException(String.format("Keyword \"deref\" requires %s element as argument!", BuiltIn.LABEL));
		}
		
		TokenResult result = scopeAction(label::getDef, label::getMacro, label::getClazz);
		if (result == null) {
			throw DSSLHelpers.defError(label.fullIdentifier);
		}
		return result;
	}
	
	protected @Nonnull TokenResult onDelete(@Nonnull Token token) {
		@Nonnull Element elem = pop();
		if (!(elem instanceof LabelElement label)) {
			throw new IllegalArgumentException(String.format("Keyword \"delete\" requires %s element as argument!", BuiltIn.LABEL));
		}
		return label.delete();
	}
	
	protected @Nonnull TokenResult onNew(@Nonnull Token token) {
		@Nonnull Element elem = pop();
		if (!(elem instanceof ClassElement clazz)) {
			throw new IllegalArgumentException(String.format("Keyword \"new\" requires %s element as argument!", BuiltIn.CLASS));
		}
		return clazz.internal.instantiate(this);
	}
	
	protected @Nonnull TokenResult onNull(@Nonnull Token token) {
		push(interpreter.builtIn.nullElement);
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onType(@Nonnull Token token) {
		push(pop().clazz.clazzElement(interpreter));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onCast(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		if (!(elem1 instanceof ClassElement clazz)) {
			throw new IllegalArgumentException(String.format("Keyword \"cast\" requires %s element as second argument!", BuiltIn.CLASS));
		}
		
		push(clazz.internal.cast(this, elem0));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onIs(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		if (!(elem1 instanceof ClassElement clazz)) {
			throw new IllegalArgumentException(String.format("Keyword \"is\" requires %s element as second argument!", BuiltIn.CLASS));
		}
		
		push(new BoolElement(interpreter, elem0.clazz.is(clazz.internal)));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onExch(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		push(elem1);
		push(elem0);
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onRoll(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		IntElement intElem0 = elem0.asInt(this);
		if (intElem0 == null) {
			throw new IllegalArgumentException(String.format("Keyword \"roll\" requires %s element as first argument!", DSSLHelpers.NON_NEGATIVE_INT));
		}
		
		int count = intElem0.primitiveInt();
		if (count < 0) {
			throw new IllegalArgumentException(String.format("Keyword \"roll\" requires %s element as first argument!", DSSLHelpers.NON_NEGATIVE_INT));
		}
		
		IntElement intElem1 = elem1.asInt(this);
		if (intElem1 == null) {
			throw new IllegalArgumentException(String.format("Keyword \"roll\" requires %s element as second argument!", BuiltIn.INT));
		}
		
		int roll = intElem1.primitiveInt();
		@Nonnull Element[] elems = pop(count);
		for (int i = 0; i < count; ++i) {
			push(elems[DSSLHelpers.mod(i - roll, count)]);
		}
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onPop(@Nonnull Token token) {
		pop();
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onDup(@Nonnull Token token) {
		push(peek());
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onStacksize(@Nonnull Token token) {
		push(new IntElement(interpreter, stackSize()));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onStackindex(@Nonnull Token token) {
		@Nonnull Element elem = pop();
		IntElement intElem = elem.asInt(this);
		if (intElem == null) {
			throw new IllegalArgumentException(String.format("Keyword \"stackindex\" requires %s element as argument!", DSSLHelpers.NON_NEGATIVE_INT));
		}
		
		int primitiveInt = intElem.primitiveInt();
		if (primitiveInt < 0) {
			throw new IllegalArgumentException(String.format("Keyword \"stackindex\" requires %s element as argument!", DSSLHelpers.NON_NEGATIVE_INT));
		}
		
		push(peekAt(primitiveInt));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onRead(@Nonnull Token token) {
		String str = interpreter.hooks.read();
		push(str == null ? interpreter.builtIn.nullElement : new StringElement(interpreter, str));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onPrint(@Nonnull Token token) {
		interpreter.printList.add(pop().stringCast(this).toString(this));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onPrintln(@Nonnull Token token) {
		interpreter.printList.add(pop().stringCast(this).toString(this));
		interpreter.printList.add("\n");
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onInterpret(@Nonnull Token token) {
		@Nonnull Element elem = pop();
		StringElement stringElem = elem.asString(this);
		if (stringElem == null) {
			throw new IllegalArgumentException(String.format("Keyword \"interpret\" requires %s element as argument!", BuiltIn.STRING));
		}
		return new TokenExecutor(new LexerIterator(stringElem.toString(this)), this, false).iterate();
	}
	
	protected @Nonnull TokenResult onExec(@Nonnull Token token) {
		@Nonnull Element elem = pop();
		if (!(elem instanceof BlockElement block)) {
			throw new IllegalArgumentException(String.format("Keyword \"exec\" requires %s element as argument!", BuiltIn.BLOCK));
		}
		return block.invoke(this);
	}
	
	protected @Nonnull TokenResult onIf(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		BoolElement boolElem = elem0.asBool(this);
		if (boolElem == null) {
			throw new IllegalArgumentException(String.format("Keyword \"if\" requires %s element as first argument!", BuiltIn.BOOL));
		}
		if (!(elem1 instanceof BlockElement block)) {
			throw new IllegalArgumentException(String.format("Keyword \"if\" requires %s element as second argument!", BuiltIn.BLOCK));
		}
		
		return boolElem.primitiveBool() ? block.invoke(this) : TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onIfelse(@Nonnull Token token) {
		@Nonnull Element elem2 = pop(), elem1 = pop(), elem0 = pop();
		BoolElement boolElem = elem0.asBool(this);
		if (boolElem == null) {
			throw new IllegalArgumentException(String.format("Keyword \"ifelse\" requires %s element as first argument!", BuiltIn.BOOL));
		}
		if (!(elem1 instanceof BlockElement ifBlock)) {
			throw new IllegalArgumentException(String.format("Keyword \"ifelse\" requires %s element as second argument!", BuiltIn.BLOCK));
		}
		if (!(elem2 instanceof BlockElement elseBlock)) {
			throw new IllegalArgumentException(String.format("Keyword \"ifelse\" requires %s element as third argument!", BuiltIn.BLOCK));
		}
		
		return (boolElem.primitiveBool() ? ifBlock : elseBlock).invoke(this);
	}
	
	protected @Nonnull TokenResult onLoop(@Nonnull Token token) {
		@Nonnull Element elem = pop();
		if (!(elem instanceof BlockElement block)) {
			throw new IllegalArgumentException(String.format("Keyword \"loop\" requires %s element as argument!", BuiltIn.BLOCK));
		}
		
		loop:
		while (true) {
			TokenResult invokeResult = block.invoke(this);
			switch (invokeResult) {
				case CONTINUE:
					continue;
				case BREAK:
					break loop;
				case QUIT:
					return TokenResult.QUIT;
				default:
					break;
			}
		}
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onRepeat(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		IntElement intElem = elem0.asInt(this);
		if (intElem == null) {
			throw new IllegalArgumentException(String.format("Keyword \"repeat\" requires %s element as first argument!", DSSLHelpers.NON_NEGATIVE_INT));
		}
		
		int primitiveInt = intElem.primitiveInt();
		if (primitiveInt < 0) {
			throw new IllegalArgumentException(String.format("Keyword \"repeat\" requires %s element as first argument!", DSSLHelpers.NON_NEGATIVE_INT));
		}
		if (!(elem1 instanceof BlockElement block)) {
			throw new IllegalArgumentException(String.format("Keyword \"repeat\" requires %s element as second argument!", BuiltIn.BLOCK));
		}
		
		loop:
		for (int i = 0; i < primitiveInt; ++i) {
			TokenResult invokeResult = block.invoke(this);
			switch (invokeResult) {
				case CONTINUE:
					continue;
				case BREAK:
					break loop;
				case QUIT:
					return TokenResult.QUIT;
				default:
					break;
			}
		}
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onForeach(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		
		@Nullable Iterable<Element> iterable = elem0.internalIterable(this);
		if (iterable == null) {
			throw new IllegalArgumentException(String.format("Keyword \"foreach\" requires %s element as first argument!", BuiltIn.ITERABLE));
		}
		if (!(elem1 instanceof BlockElement block)) {
			throw new IllegalArgumentException(String.format("Keyword \"foreach\" requires %s element as second argument!", BuiltIn.BLOCK));
		}
		
		loop:
		for (@Nonnull Element e : iterable) {
			push(e);
			TokenResult invokeResult = block.invoke(this);
			switch (invokeResult) {
				case CONTINUE:
					continue;
				case BREAK:
					break loop;
				case QUIT:
					return TokenResult.QUIT;
				default:
					break;
			}
		}
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onContinue(@Nonnull Token token) {
		return TokenResult.CONTINUE;
	}
	
	protected @Nonnull TokenResult onBreak(@Nonnull Token token) {
		return TokenResult.BREAK;
	}
	
	protected @Nonnull TokenResult onQuit(@Nonnull Token token) {
		interpreter.halt = true;
		return TokenResult.QUIT;
	}
	
	protected @Nonnull TokenResult onEquals(@Nonnull Token token) {
		assign(pop(), pop(), AssignmentType.EQUALS);
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onIncrement(@Nonnull Token token) {
		@Nonnull Element elem = peek();
		if (!(elem instanceof LabelElement label)) {
			throw new IllegalArgumentException(String.format("Increment operator \"++\" requires %s element as argument!", BuiltIn.LABEL));
		}
		
		Def def = label.getDef();
		if (def == null) {
			throw DSSLHelpers.variableError(label.fullIdentifier);
		}
		
		return opAssign(def.elem.onPlus(this, new IntElement(interpreter, BigInteger.ONE)));
	}
	
	protected @Nonnull TokenResult onDecrement(@Nonnull Token token) {
		@Nonnull Element elem = peek();
		if (!(elem instanceof LabelElement label)) {
			throw new IllegalArgumentException(String.format("Decrement operator \"--\" requires %s element as argument!", BuiltIn.LABEL));
		}
		
		Def def = label.getDef();
		if (def == null) {
			throw DSSLHelpers.variableError(label.fullIdentifier);
		}
		
		return opAssign(def.elem.onMinus(this, new IntElement(interpreter, BigInteger.ONE)));
	}
	
	protected @Nonnull TokenResult onPlusEquals(@Nonnull Token token) {
		AssignmentOpPair pair = getAssignmentOpPair(token);
		return opAssign(pair.def.elem.onPlus(this, pair.elem));
	}
	
	protected @Nonnull TokenResult onAndEquals(@Nonnull Token token) {
		AssignmentOpPair pair = getAssignmentOpPair(token);
		return opAssign(pair.def.elem.onAnd(this, pair.elem));
	}
	
	protected @Nonnull TokenResult onOrEquals(@Nonnull Token token) {
		AssignmentOpPair pair = getAssignmentOpPair(token);
		return opAssign(pair.def.elem.onOr(this, pair.elem));
	}
	
	protected @Nonnull TokenResult onXorEquals(@Nonnull Token token) {
		AssignmentOpPair pair = getAssignmentOpPair(token);
		return opAssign(pair.def.elem.onXor(this, pair.elem));
	}
	
	protected @Nonnull TokenResult onMinusEquals(@Nonnull Token token) {
		AssignmentOpPair pair = getAssignmentOpPair(token);
		return opAssign(pair.def.elem.onMinus(this, pair.elem));
	}
	
	protected @Nonnull TokenResult onConcatEquals(@Nonnull Token token) {
		AssignmentOpPair pair = getAssignmentOpPair(token);
		return opAssign(pair.def.elem.onConcat(this, pair.elem));
	}
	
	protected @Nonnull TokenResult onLeftShiftEquals(@Nonnull Token token) {
		AssignmentOpPair pair = getAssignmentOpPair(token);
		return opAssign(pair.def.elem.onLeftShift(this, pair.elem));
	}
	
	protected @Nonnull TokenResult onRightShiftEquals(@Nonnull Token token) {
		AssignmentOpPair pair = getAssignmentOpPair(token);
		return opAssign(pair.def.elem.onRightShift(this, pair.elem));
	}
	
	protected @Nonnull TokenResult onMultiplyEquals(@Nonnull Token token) {
		AssignmentOpPair pair = getAssignmentOpPair(token);
		return opAssign(pair.def.elem.onMultiply(this, pair.elem));
	}
	
	protected @Nonnull TokenResult onDivideEquals(@Nonnull Token token) {
		AssignmentOpPair pair = getAssignmentOpPair(token);
		return opAssign(pair.def.elem.onDivide(this, pair.elem));
	}
	
	protected @Nonnull TokenResult onRemainderEquals(@Nonnull Token token) {
		AssignmentOpPair pair = getAssignmentOpPair(token);
		return opAssign(pair.def.elem.onRemainder(this, pair.elem));
	}
	
	protected @Nonnull TokenResult onPowerEquals(@Nonnull Token token) {
		AssignmentOpPair pair = getAssignmentOpPair(token);
		return opAssign(pair.def.elem.onPower(this, pair.elem));
	}
	
	protected @Nonnull TokenResult onIdivideEquals(@Nonnull Token token) {
		AssignmentOpPair pair = getAssignmentOpPair(token);
		return opAssign(pair.def.elem.onIdivide(this, pair.elem));
	}
	
	protected @Nonnull TokenResult onModuloEquals(@Nonnull Token token) {
		AssignmentOpPair pair = getAssignmentOpPair(token);
		return opAssign(pair.def.elem.onModulo(this, pair.elem));
	}
	
	protected @Nonnull TokenResult onEqualTo(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onEqualTo(this, elem1);
	}
	
	protected @Nonnull TokenResult onNotEqualTo(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onNotEqualTo(this, elem1);
	}
	
	protected @Nonnull TokenResult onLessThan(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onLessThan(this, elem1);
	}
	
	protected @Nonnull TokenResult onLessOrEqual(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onLessOrEqual(this, elem1);
	}
	
	protected @Nonnull TokenResult onMoreThan(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onMoreThan(this, elem1);
	}
	
	protected @Nonnull TokenResult onMoreOrEqual(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onMoreOrEqual(this, elem1);
	}
	
	protected @Nonnull TokenResult onPlus(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onPlus(this, elem1);
	}
	
	protected @Nonnull TokenResult onAnd(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onAnd(this, elem1);
	}
	
	protected @Nonnull TokenResult onOr(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onOr(this, elem1);
	}
	
	protected @Nonnull TokenResult onXor(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onXor(this, elem1);
	}
	
	protected @Nonnull TokenResult onMinus(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onMinus(this, elem1);
	}
	
	protected @Nonnull TokenResult onConcat(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onConcat(this, elem1);
	}
	
	protected @Nonnull TokenResult onLeftShift(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onLeftShift(this, elem1);
	}
	
	protected @Nonnull TokenResult onRightShift(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onRightShift(this, elem1);
	}
	
	protected @Nonnull TokenResult onMultiply(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onMultiply(this, elem1);
	}
	
	protected @Nonnull TokenResult onDivide(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onDivide(this, elem1);
	}
	
	protected @Nonnull TokenResult onRemainder(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onRemainder(this, elem1);
	}
	
	protected @Nonnull TokenResult onPower(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onPower(this, elem1);
	}
	
	protected @Nonnull TokenResult onIdivide(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onIdivide(this, elem1);
	}
	
	protected @Nonnull TokenResult onModulo(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		return elem0.onModulo(this, elem1);
	}
	
	protected @Nonnull TokenResult onNot(@Nonnull Token token) {
		return pop().onNot(this);
	}
	
	protected @Nonnull TokenResult onIntValue(@Nonnull Token token) {
		push(new IntElement(interpreter, new BigInteger(token.getText())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onBoolValue(@Nonnull Token token) {
		push(new BoolElement(interpreter, Boolean.parseBoolean(token.getText())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onFloatValue(@Nonnull Token token) {
		push(new FloatElement(interpreter, Double.parseDouble(token.getText())));
		return TokenResult.PASS;
	}
	
	@SuppressWarnings("null")
	protected @Nonnull TokenResult onCharValue(@Nonnull Token token) {
		push(new CharElement(interpreter, DSSLHelpers.parseChar(token.getText())));
		return TokenResult.PASS;
	}
	
	@SuppressWarnings("null")
	protected @Nonnull TokenResult onBlockStringValue(@Nonnull Token token) {
		push(new StringElement(interpreter, DSSLHelpers.parseBlockString(token.getText())));
		return TokenResult.PASS;
	}
	
	@SuppressWarnings("null")
	protected @Nonnull TokenResult onLineStringValue(@Nonnull Token token) {
		push(new StringElement(interpreter, DSSLHelpers.parseLineString(token.getText())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onIdentifier(@Nonnull Token token) {
		@SuppressWarnings("null") @Nonnull String identifier = token.getText();
		checkKeyword(identifier, "an identifier");
		
		TokenResult result = scopeAction(this, identifier);
		if (result == null) {
			throw DSSLHelpers.defError(identifier);
		}
		return result;
	}
	
	protected @Nonnull TokenResult onLabel(@Nonnull Token token) {
		@SuppressWarnings("null") @Nonnull String identifier = token.getText().substring(1);
		checkKeyword(identifier, "a label identifier");
		push(new LabelElement(interpreter, this, identifier));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onMember(@Nonnull Token token) {
		@SuppressWarnings("null") @Nonnull String member = token.getText().substring(1);
		// checkKeyword(member, "a member identifier");
		
		@Nonnull Element elem = pop();
		if (elem instanceof LabelElement label) {
			push(label.extended(member));
			return TokenResult.PASS;
		}
		
		TokenResult result = elem.memberAction(this, member, true);
		if (result == null) {
			throw elem.memberAccessError(member);
		}
		return result;
	}
	
	protected @Nonnull TokenResult onModule(@Nonnull Token token) {
		@SuppressWarnings("null") @Nonnull String identifier = token.getText().substring(1);
		// checkKeyword(identifier, "a module identifier");
		push(new ModuleElement(interpreter, identifier));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult onBlock(@Nonnull Token token) {
		push(new BlockElement(interpreter, ((BlockToken) token).tokens));
		return TokenResult.PASS;
	}
	
	protected void assign(@Nonnull Element elem1, @Nonnull Element elem0, @Nonnull AssignmentType type) {
		@Nullable IterElement iter1, iter0;
		if ((iter0 = elem0.iterator(this)) != null && (iter1 = elem1.iterator(this)) != null) {
			while (iter1.hasNext(this) && iter0.hasNext(this)) {
				assign(iter1.next(this), iter0.next(this), type);
			}
			
			if (iter0.hasNext(this)) {
				throw new IllegalArgumentException(String.format("%s for %s elements requires length of second iterator to be greater than or equal to length of first iterator!", type.labelErrorPrefix(), BuiltIn.ITERABLE));
			}
		}
		else {
			if (!(elem0 instanceof LabelElement label)) {
				throw new IllegalArgumentException(String.format("%s requires %s element as first argument!", type.labelErrorPrefix(), BuiltIn.LABEL));
			}
			
			boolean def = type.equals(AssignmentType.DEF);
			if (!def && label.getDef() == null) {
				throw DSSLHelpers.variableError(label.fullIdentifier);
			}
			label.setDef(elem1, def);
		}
	}
	
	protected void checkKeyword(@Nonnull String identifier, @Nonnull String type) {
		if (BuiltIn.KEYWORDS.contains(identifier)) {
			throw new IllegalArgumentException(String.format("Keyword \"%s\" can not be used as %s!", identifier, type));
		}
	}
	
	protected Reverse<Element> getElemsToLBracket(@Nonnull LBracketElement lbracket, @Nonnull RBracketElement rbracket) {
		Deque<Element> deque = new ArrayDeque<>();
		while (!stack().isEmpty()) {
			@Nonnull Element elem = pop();
			if (elem.equals(lbracket)) {
				return new Reverse<>(deque);
			}
			else {
				deque.add(elem);
			}
		}
		throw new IllegalArgumentException(String.format("Encountered \"%s\" token without corresponding \"%s\" token!", rbracket, lbracket));
	}
	
	protected enum AssignmentType {
		
		DEF,
		EQUALS;
		
		private String labelErrorPrefix() {
			return equals(EQUALS) ? "Assignment" : "Keyword \"def\"";
		}
	}
	
	protected static class AssignmentOpPair {
		
		protected Def def;
		protected @Nonnull Element elem;
		
		public AssignmentOpPair(Def def, @Nonnull Element elem) {
			this.def = def;
			this.elem = elem;
		}
	}
	
	protected AssignmentOpPair getAssignmentOpPair(@Nonnull Token token) {
		@Nonnull Element elem1 = pop(), elem0 = peek();
		if (!(elem0 instanceof LabelElement label)) {
			throw new IllegalArgumentException(String.format("Assignment operator \"%s\" requires %s element as first argument!", token.getText(), BuiltIn.LABEL));
		}
		
		Def def = label.getDef();
		if (def == null) {
			throw DSSLHelpers.variableError(label.fullIdentifier);
		}
		
		return new AssignmentOpPair(def, elem1);
	}
	
	protected @Nonnull TokenResult opAssign(@Nonnull TokenResult opResult) {
		assign(pop(), pop(), AssignmentType.EQUALS);
		return opResult;
	}
	
	public @Nullable TokenResult scopeAction(Supplier<Def> getDef, Supplier<Macro> getMacro, Supplier<Clazz> getClazz) {
		Def def;
		Macro macro;
		Clazz clazz;
		if ((def = getDef.get()) != null) {
			return defAction(def);
		}
		else if ((macro = getMacro.get()) != null) {
			return macroAction(macro);
		}
		else if ((clazz = getClazz.get()) != null) {
			return clazzAction(clazz);
		}
		else {
			return null;
		}
	}
	
	public @Nullable Supplier<TokenResult> scopeInvokable(Supplier<Def> getDef, Supplier<Macro> getMacro, Supplier<Clazz> getClazz) {
		Def def;
		Macro macro;
		Clazz clazz;
		if ((def = getDef.get()) != null) {
			return () -> defAction(def);
		}
		else if ((macro = getMacro.get()) != null) {
			return () -> macroAction(macro);
		}
		else if ((clazz = getClazz.get()) != null) {
			return () -> clazzAction(clazz);
		}
		else {
			return null;
		}
	}
	
	public @Nonnull TokenResult defAction(@Nonnull Def def) {
		push(def.elem);
		return TokenResult.PASS;
	}
	
	public @Nonnull TokenResult macroAction(@Nonnull Macro macro) {
		return macro.invokable.invoke(this);
	}
	
	public @Nonnull TokenResult clazzAction(@Nonnull Clazz clazz) {
		push(clazz.clazzElement(interpreter));
		return TokenResult.PASS;
	}
	
	// BUILT-INS
	
	protected @Nonnull TokenResult finite() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("finite");
		push(new BoolElement(interpreter, Double.isFinite(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult infinite() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("infinite");
		push(new BoolElement(interpreter, Double.isInfinite(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult inv() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("inv");
		push(new FloatElement(interpreter, 1.0 / floatElem.primitiveFloat()));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult neg() {
		@Nonnull Element elem = pop();
		IntElement intElem = elem.asInt(this);
		if (intElem != null) {
			push(new IntElement(interpreter, intElem.value.raw.negate()));
			return TokenResult.PASS;
		}
		
		FloatElement floatElem = elem.asFloat(this);
		if (floatElem != null) {
			push(new FloatElement(interpreter, -floatElem.primitiveFloat()));
			return TokenResult.PASS;
		}
		
		throw new IllegalArgumentException(String.format("Built-in math macro \"neg\" requires %s or %s element as argument!", BuiltIn.INT, BuiltIn.FLOAT));
	}
	
	protected @Nonnull TokenResult abs() {
		@Nonnull Element elem = pop();
		IntElement intElem = elem.asInt(this);
		if (intElem != null) {
			push(new IntElement(interpreter, intElem.value.raw.abs()));
			return TokenResult.PASS;
		}
		
		FloatElement floatElem = elem.asFloat(this);
		if (floatElem != null) {
			push(new FloatElement(interpreter, Math.abs(floatElem.primitiveFloat())));
			return TokenResult.PASS;
		}
		
		throw new IllegalArgumentException(String.format("Built-in math macro \"abs\" requires %s or %s element as argument!", BuiltIn.INT, BuiltIn.FLOAT));
	}
	
	protected @Nonnull TokenResult sgn() {
		@Nonnull Element elem = pop();
		IntElement intElem = elem.asInt(this);
		if (intElem != null) {
			push(new IntElement(interpreter, intElem.value.raw.signum()));
			return TokenResult.PASS;
		}
		
		FloatElement floatElem = elem.asFloat(this);
		if (floatElem != null) {
			push(new FloatElement(interpreter, Math.signum(floatElem.primitiveFloat())));
			return TokenResult.PASS;
		}
		
		throw new IllegalArgumentException(String.format("Built-in math macro \"sgn\" requires %s or %s element as argument!", BuiltIn.INT, BuiltIn.FLOAT));
	}
	
	protected @Nonnull TokenResult floor() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("floor");
		push(new IntElement(interpreter, floatElem.bigFloat().setScale(0, RoundingMode.FLOOR).toBigInteger()));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult ceil() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("ceil");
		push(new IntElement(interpreter, floatElem.bigFloat().setScale(0, RoundingMode.CEILING).toBigInteger()));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult trunc() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("trunc");
		push(new IntElement(interpreter, floatElem.bigFloat().toBigInteger()));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult fract() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("fract");
		@Nonnull BigDecimal bd = floatElem.bigFloat();
		push(new FloatElement(interpreter, bd.subtract(new BigDecimal(bd.toBigInteger())).doubleValue()));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult round() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("round");
		push(new IntElement(interpreter, floatElem.bigFloat().round(MathContext.UNLIMITED).toBigInteger()));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult places() {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		FloatElement floatElem = elem0.asFloat(this);
		if (floatElem == null) {
			throw new IllegalArgumentException(String.format("Built-in math macro \"places\" requires %s element as first argument!", BuiltIn.FLOAT));
		}
		
		IntElement intElem = elem1.asInt(this);
		if (intElem == null) {
			throw new IllegalArgumentException(String.format("Built-in math macro \"places\" requires %s element as second argument!", BuiltIn.INT));
		}
		
		push(new FloatElement(interpreter, floatElem.bigFloat().setScale(intElem.primitiveInt(), RoundingMode.HALF_UP).doubleValue()));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult sin() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("sin");
		push(new FloatElement(interpreter, Math.sin(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult cos() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("cos");
		push(new FloatElement(interpreter, Math.cos(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult tan() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("tan");
		push(new FloatElement(interpreter, Math.tan(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult asin() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("asin");
		push(new FloatElement(interpreter, Math.asin(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult acos() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("acos");
		push(new FloatElement(interpreter, Math.acos(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult atan() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("atan");
		push(new FloatElement(interpreter, Math.atan(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult sinc() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("sinc");
		double f = floatElem.primitiveFloat();
		push(new FloatElement(interpreter, f == 0.0 ? 1.0 : Math.sin(f) / f));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult atan2() {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		FloatElement floatElem0 = elem0.asFloat(this);
		if (floatElem0 == null) {
			throw new IllegalArgumentException(String.format("Built-in math macro \"atan2\" requires %s element as first argument!", BuiltIn.FLOAT));
		}
		
		FloatElement floatElem1 = elem1.asFloat(this);
		if (floatElem1 == null) {
			throw new IllegalArgumentException(String.format("Built-in math macro \"atan2\" requires %s element as second argument!", BuiltIn.FLOAT));
		}
		
		push(new FloatElement(interpreter, Math.atan2(floatElem0.primitiveFloat(), floatElem1.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult hypot() {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		FloatElement floatElem0 = elem0.asFloat(this);
		if (floatElem0 == null) {
			throw new IllegalArgumentException(String.format("Built-in math macro \"hypot\" requires %s element as first argument!", BuiltIn.FLOAT));
		}
		
		FloatElement floatElem1 = elem1.asFloat(this);
		if (floatElem1 == null) {
			throw new IllegalArgumentException(String.format("Built-in math macro \"hypot\" requires %s element as second argument!", BuiltIn.FLOAT));
		}
		
		push(new FloatElement(interpreter, Math.hypot(floatElem0.primitiveFloat(), floatElem1.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult rads() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("rads");
		push(new FloatElement(interpreter, Math.toRadians(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult degs() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("degs");
		push(new FloatElement(interpreter, Math.toDegrees(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult exp() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("exp");
		push(new FloatElement(interpreter, Math.exp(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult ln() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("ln");
		push(new FloatElement(interpreter, Math.log(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult log2() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("log2");
		push(new FloatElement(interpreter, Math.log(floatElem.primitiveFloat()) / DSSLConstants.LN_2));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult log10() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("log10");
		push(new FloatElement(interpreter, Math.log10(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult log() {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		FloatElement floatElem0 = elem0.asFloat(this);
		if (floatElem0 == null) {
			throw new IllegalArgumentException(String.format("Built-in math macro \"log\" requires %s element as first argument!", BuiltIn.FLOAT));
		}
		
		FloatElement floatElem1 = elem1.asFloat(this);
		if (floatElem1 == null) {
			throw new IllegalArgumentException(String.format("Built-in math macro \"log\" requires %s element as second argument!", BuiltIn.FLOAT));
		}
		
		push(new FloatElement(interpreter, Math.log(floatElem0.primitiveFloat()) / Math.log(floatElem1.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult expm1() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("expm1");
		push(new FloatElement(interpreter, Math.expm1(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult ln1p() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("ln1p");
		push(new FloatElement(interpreter, Math.log1p(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult sqrt() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("sqrt");
		push(new FloatElement(interpreter, Math.sqrt(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult cbrt() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("cbrt");
		push(new FloatElement(interpreter, Math.cbrt(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult root() {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		FloatElement floatElem0 = elem0.asFloat(this);
		if (floatElem0 == null) {
			throw new IllegalArgumentException(String.format("Built-in math macro \"root\" requires %s element as first argument!", BuiltIn.FLOAT));
		}
		
		FloatElement floatElem1 = elem1.asFloat(this);
		if (floatElem1 == null) {
			throw new IllegalArgumentException(String.format("Built-in math macro \"root\" requires %s element as second argument!", BuiltIn.FLOAT));
		}
		
		push(new FloatElement(interpreter, Math.pow(floatElem0.primitiveFloat(), 1.0 / floatElem1.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult isqrt() {
		@Nonnull IntElement intElem = builtInSingleInt("isqrt");
		push(new IntElement(interpreter, DSSLHelpers.iroot(intElem.value.raw, 2)));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult icbrt() {
		@Nonnull IntElement intElem = builtInSingleInt("icbrt");
		push(new IntElement(interpreter, DSSLHelpers.iroot(intElem.value.raw, 3)));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult iroot() {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		IntElement intElem0 = elem0.asInt(this);
		if (intElem0 == null) {
			throw new IllegalArgumentException(String.format("Built-in math macro \"iroot\" requires %s element as first argument!", BuiltIn.INT));
		}
		
		IntElement intElem1 = elem1.asInt(this);
		if (intElem1 == null) {
			throw new IllegalArgumentException(String.format("Built-in math macro \"iroot\" requires %s element as second argument!", BuiltIn.INT));
		}
		
		push(new IntElement(interpreter, DSSLHelpers.iroot(intElem0.value.raw, intElem1.primitiveInt())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult sinh() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("sinh");
		push(new FloatElement(interpreter, Math.sinh(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult cosh() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("cosh");
		push(new FloatElement(interpreter, Math.cosh(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult tanh() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("tanh");
		push(new FloatElement(interpreter, Math.tanh(floatElem.primitiveFloat())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult asinh() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("asinh");
		double f = floatElem.primitiveFloat();
		push(new FloatElement(interpreter, Math.log(f + Math.sqrt(f * f + 1.0))));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult acosh() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("acosh");
		double f = floatElem.primitiveFloat();
		push(new FloatElement(interpreter, Math.log(f + Math.sqrt(f * f - 1.0))));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult atanh() {
		@Nonnull FloatElement floatElem = builtInSingleFloat("atanh");
		double f = floatElem.primitiveFloat();
		push(new FloatElement(interpreter, 0.5 * Math.log((1.0 + f) / (1.0 - f))));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult min() {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		IntElement intElem0 = elem0.asInt(this), intElem1 = elem1.asInt(this);
		boolean firstValid = false;
		if ((firstValid |= intElem0 != null) && intElem1 != null) {
			push(new IntElement(interpreter, intElem0.value.raw.min(intElem1.value.raw)));
			return TokenResult.PASS;
		}
		
		FloatElement floatElem0 = elem0.asFloat(this), floatElem1 = elem1.asFloat(this);
		if ((firstValid |= floatElem0 != null) && floatElem1 != null) {
			push(new FloatElement(interpreter, Math.min(floatElem0.primitiveFloat(), floatElem1.primitiveFloat())));
			return TokenResult.PASS;
		}
		
		throw new IllegalArgumentException(String.format("Built-in math macro \"min\" requires %s or %s element as %s argument!", BuiltIn.INT, BuiltIn.FLOAT, firstValid ? "second" : "first"));
	}
	
	protected @Nonnull TokenResult max() {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		IntElement intElem0 = elem0.asInt(this), intElem1 = elem1.asInt(this);
		boolean firstValid = false;
		if ((firstValid |= intElem0 != null) && intElem1 != null) {
			push(new IntElement(interpreter, intElem0.value.raw.max(intElem1.value.raw)));
			return TokenResult.PASS;
		}
		
		FloatElement floatElem0 = elem0.asFloat(this), floatElem1 = elem1.asFloat(this);
		if ((firstValid |= floatElem0 != null) && floatElem1 != null) {
			push(new FloatElement(interpreter, Math.max(floatElem0.primitiveFloat(), floatElem1.primitiveFloat())));
			return TokenResult.PASS;
		}
		
		throw new IllegalArgumentException(String.format("Built-in math macro \"max\" requires %s or %s element as %s argument!", BuiltIn.INT, BuiltIn.FLOAT, firstValid ? "second" : "first"));
	}
	
	protected @Nonnull TokenResult clamp() {
		@Nonnull Element elem2 = pop(), elem1 = pop(), elem0 = pop();
		IntElement intElem0 = elem0.asInt(this), intElem1 = elem1.asInt(this), intElem2 = elem2.asInt(this);
		boolean firstValid = false, secondValid = false;
		if ((firstValid |= intElem0 != null) && (secondValid |= intElem1 != null) && intElem2 != null) {
			push(new IntElement(interpreter, DSSLHelpers.clamp(intElem0.value.raw, intElem1.value.raw, intElem2.value.raw)));
			return TokenResult.PASS;
		}
		
		FloatElement floatElem0 = elem0.asFloat(this), floatElem1 = elem1.asFloat(this), floatElem2 = elem2.asFloat(this);
		if ((firstValid |= floatElem0 != null) && (secondValid |= floatElem1 != null) && floatElem2 != null) {
			push(new FloatElement(interpreter, DSSLHelpers.clamp(floatElem0.primitiveFloat(), floatElem1.primitiveFloat(), floatElem2.primitiveFloat())));
			return TokenResult.PASS;
		}
		
		throw new IllegalArgumentException(String.format("Built-in math macro \"clamp\" requires %s or %s element as %s argument!", BuiltIn.INT, BuiltIn.FLOAT, secondValid ? "third" : (firstValid ? "second" : "first")));
	}
	
	protected @Nonnull TokenResult clamp8() {
		@Nonnull IntElement intElem = builtInSingleInt("clamp8");
		push(new IntElement(interpreter, DSSLHelpers.clamp(intElem.value.raw, DSSLConstants.MIN_INT_8, DSSLConstants.MAX_INT_8)));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult clamp16() {
		@Nonnull IntElement intElem = builtInSingleInt("clamp16");
		push(new IntElement(interpreter, DSSLHelpers.clamp(intElem.value.raw, DSSLConstants.MIN_INT_16, DSSLConstants.MAX_INT_16)));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult clamp32() {
		@Nonnull IntElement intElem = builtInSingleInt("clamp32");
		push(new IntElement(interpreter, DSSLHelpers.clamp(intElem.value.raw, DSSLConstants.MIN_INT_32, DSSLConstants.MAX_INT_32)));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult clamp64() {
		@Nonnull IntElement intElem = builtInSingleInt("clamp64");
		push(new IntElement(interpreter, DSSLHelpers.clamp(intElem.value.raw, DSSLConstants.MIN_INT_64, DSSLConstants.MAX_INT_64)));
		return TokenResult.PASS;
	}
	
	protected @Nonnull IntElement builtInSingleInt(String name) {
		@Nonnull Element elem = pop();
		IntElement intElem = elem.asInt(this);
		if (intElem == null) {
			throw new IllegalArgumentException(String.format("Built-in math macro \"%s\" requires %s element as argument!", name, BuiltIn.INT));
		}
		return intElem;
	}
	
	protected @Nonnull FloatElement builtInSingleFloat(String name) {
		@Nonnull Element elem = pop();
		FloatElement floatElem = elem.asFloat(this);
		if (floatElem == null) {
			throw new IllegalArgumentException(String.format("Built-in math macro \"%s\" requires %s element as argument!", name, BuiltIn.FLOAT));
		}
		return floatElem;
	}
	
	protected @Nonnull TokenResult args() {
		push(new ListElement(interpreter, interpreter.args.stream().map(x -> new StringElement(interpreter, x))));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult rootPath() {
		push(new StringElement(interpreter, DSSLHelpers.normalizedPathString(interpreter.hooks.getRootPath(this))));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult rootDir() {
		push(new StringElement(interpreter, DSSLHelpers.normalizedPathString(interpreter.hooks.getRootPath(this).getParent())));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult fromRoot() {
		@Nonnull Element elem = pop();
		StringElement stringElem = elem.asString(this);
		if (stringElem == null) {
			throw new IllegalArgumentException(String.format("Built-in env macro \"fromRoot\" requires %s element as argument!", BuiltIn.STRING));
		}
		
		push(new StringElement(interpreter, DSSLHelpers.normalizedPathString(interpreter.hooks.getRootPath(this).getParent().resolve(stringElem.toString(this)))));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult readFile() {
		@Nonnull Element elem = pop();
		StringElement stringElem = elem.asString(this);
		if (stringElem == null) {
			throw new IllegalArgumentException(String.format("Built-in fs macro \"readFile\" requires %s element as argument!", BuiltIn.STRING));
		}
		
		push(new StringElement(interpreter, DSSLHelpers.readFile(stringElem.toString(this))));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult writeFile() {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		StringElement stringElem0 = elem0.asString(this);
		if (stringElem0 == null) {
			throw new IllegalArgumentException(String.format("Built-in fs macro \"writeFile\" requires %s element as first argument!", BuiltIn.STRING));
		}
		
		DSSLHelpers.writeFile(stringElem0.toString(this), elem1.stringCast(this).toString(this));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult readLines() {
		@Nonnull Element elem = pop();
		StringElement stringElem = elem.asString(this);
		if (stringElem == null) {
			throw new IllegalArgumentException(String.format("Built-in fs macro \"readLines\" requires %s element as argument!", BuiltIn.STRING));
		}
		
		push(new ListElement(interpreter, DSSLHelpers.readLines(stringElem.toString(this)).stream().map(x -> new StringElement(interpreter, x))));
		return TokenResult.PASS;
	}
	
	protected @Nonnull TokenResult writeLines() {
		@Nonnull Element elem1 = pop(), elem0 = pop();
		StringElement stringElem0 = elem0.asString(this);
		if (stringElem0 == null) {
			throw new IllegalArgumentException(String.format("Built-in fs macro \"writeLines\" requires %s element as first argument!", BuiltIn.STRING));
		}
		
		@Nullable Stream<Element> stream = elem1.internalStream(this);
		if (stream == null) {
			throw new IllegalArgumentException(String.format("Built-in fs macro \"writeLines\" requires %s element as second argument!", BuiltIn.ITERABLE));
		}
		
		DSSLHelpers.writeLines(stringElem0.toString(this), stream.map(x -> x.stringCast(this).toString(this)));
		return TokenResult.PASS;
	}
}
