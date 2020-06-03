package nc.util;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

public final class KeyPair<L, R> extends Pair<L, R> {
	
	public final L left;
	public final R right;
	
	public KeyPair(final L left, final R right) {
		super();
		this.left = left;
		this.right = right;
	}
	
	public static <L, R> KeyPair<L, R> of(final L left, final R right) {
		return new KeyPair<>(left, right);
	}
	
	@Override
	public L getLeft() {
		return left;
	}
	
	@Override
	public R getRight() {
		return right;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Map.Entry<?, ?>) {
			final Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
			return Objects.equals(getKey(), other.getKey());
		}
		return false;
	}
	
	@Override
	public R setValue(final R value) {
		throw new UnsupportedOperationException();
	}
}
