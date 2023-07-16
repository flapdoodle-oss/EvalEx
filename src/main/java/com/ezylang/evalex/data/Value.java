package com.ezylang.evalex.data;

import com.ezylang.evalex.parser.ASTNode;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;

public abstract class Value<T> {
	@org.immutables.value.Value.Parameter
	public abstract T wrapped();

	@org.immutables.value.Value.Derived
	public abstract DataType dataType();

	public static abstract class ComparableValue<T extends Comparable<T>> extends Value<T> implements Comparable<Value<T>> {
		@org.immutables.value.Value.Auxiliary
		@Override
		public int compareTo(Value<T> other) {
			return wrapped().compareTo(other.wrapped());
		}
	}

	@org.immutables.value.Value.Immutable
	public static abstract class StringValue extends ComparableValue<String> {
		@Override
		public DataType dataType() {
			return DataType.STRING;
		}
	}

	@org.immutables.value.Value.Immutable
	public static abstract class NumberValue extends ComparableValue<BigDecimal> {
		@Override
		public DataType dataType() {
			return DataType.NUMBER;
		}
	}

	@org.immutables.value.Value.Immutable
	public static abstract class BooleanValue extends ComparableValue<Boolean> {
		@Override
		public DataType dataType() {
			return DataType.BOOLEAN;
		}
	}

	@org.immutables.value.Value.Immutable
	public static abstract class DateTimeValue extends ComparableValue<Instant> {
		@Override
		public DataType dataType() {
			return DataType.DATE_TIME;
		}
	}

	@org.immutables.value.Value.Immutable
	public static abstract class DurationValue extends ComparableValue<Duration> {
		@Override
		public DataType dataType() {
			return DataType.DURATION;
		}
	}

	@org.immutables.value.Value.Immutable
	public static abstract class ArrayValue extends Value<ValueArray> {
		@Override
		public DataType dataType() {
			return DataType.ARRAY;
		}
	}

	@org.immutables.value.Value.Immutable
	public static abstract class StructureValue extends Value<ValueMap> {
		@Override
		public DataType dataType() {
			return DataType.STRUCTURE;
		}
	}

	@org.immutables.value.Value.Immutable
	public static abstract class ExpressionValue extends Value<ASTNode> {
		@Override
		public DataType dataType() {
			return DataType.EXPRESSION_NODE;
		}
	}

	public static StringValue of(String value) {
		return ImmutableStringValue.of(value);
	}

	public static NumberValue of(BigDecimal value) {
		return ImmutableNumberValue.of(value);
	}

	public static BooleanValue of(Boolean value) {
		return ImmutableBooleanValue.of(value);
	}

	public static DateTimeValue of(Instant value) {
		return ImmutableDateTimeValue.of(value);
	}

	public static DurationValue of(Duration value) {
		return ImmutableDurationValue.of(value);
	}

	public static ArrayValue of(ValueArray value) {
		return ImmutableArrayValue.of(value);
	}

	public static StructureValue of(ValueMap value) {
		return ImmutableStructureValue.of(value);
	}

	public static ExpressionValue of(ASTNode value) {
		return ImmutableExpressionValue.of(value);
	}
}
