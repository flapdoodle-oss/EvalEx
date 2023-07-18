package com.ezylang.evalex.data;

import java.util.Map;

@org.immutables.value.Value.Immutable
public abstract class ValueMap {
	protected abstract Map<String, Value<?>> values();

	@org.immutables.value.Value.Auxiliary
	public boolean containsKey(String name) {
		return values().containsKey(name);
	}

	@org.immutables.value.Value.Auxiliary
	public Value<?> get(String name) {
		return values().get(name);
	}

//	public static ImmVa bu(Iterable<? extends Value<?>> values) {
//		return ImmutableValueArray.builder()
//			.values(values)
//			.build();
//	}
}
