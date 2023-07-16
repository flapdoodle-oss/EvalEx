package com.ezylang.evalex.data;

import java.util.List;
import java.util.Map;

@org.immutables.value.Value.Immutable
public abstract class ValueMap {
	public abstract Map<String, Value<?>> values();

//	public static ImmVa bu(Iterable<? extends Value<?>> values) {
//		return ImmutableValueArray.builder()
//			.values(values)
//			.build();
//	}
}
