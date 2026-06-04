package nc.dssl.interpret;

import javax.annotation.Nonnull;

public interface Invokable {
	
	@Nonnull
	TokenResult invoke(TokenExecutor exec);
}
