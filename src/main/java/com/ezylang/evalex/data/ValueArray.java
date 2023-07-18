package com.ezylang.evalex.data;

import java.util.List;

@org.immutables.value.Value.Immutable
public abstract class ValueArray {
	protected abstract List<Value<?>> values();

	@org.immutables.value.Value.Auxiliary
	public Value<?> get(int index) {
		return values().get(index);
	}
	
	public static ValueArray of(Iterable<? extends Value<?>> values) {
		return ImmutableValueArray.builder()
			.values(values)
			.build();
	}
}
