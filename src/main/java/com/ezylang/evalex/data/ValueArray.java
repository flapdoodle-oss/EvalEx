package com.ezylang.evalex.data;

import java.util.List;

@org.immutables.value.Value.Immutable
public abstract class ValueArray {
	public abstract List<Value<?>> values();

	public static ValueArray of(Iterable<? extends Value<?>> values) {
		return ImmutableValueArray.builder()
			.values(values)
			.build();
	}
}
