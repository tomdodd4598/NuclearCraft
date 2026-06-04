package nc.dssl.interpret;

import nc.dssl.interpret.element.BlockElement;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public interface Hooks {
	
	void print(String str);
	
	void debug(String str);
	
	String read();
	
	@Nonnull
	TokenResult onInclude(TokenExecutor exec);
	
	@Nonnull
	TokenResult onImport(TokenExecutor exec);
	
	@Nonnull
	TokenResult onNative(TokenExecutor exec);
	
	TokenIterator getBlockIterator(TokenExecutor exec, @Nonnull BlockElement block);
	
	Path getRootPath(TokenExecutor exec);
}
