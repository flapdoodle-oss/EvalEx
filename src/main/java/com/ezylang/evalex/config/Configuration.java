package com.ezylang.evalex.config;

import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functionsx.Function;
import com.ezylang.evalex.operators.arithmetic.InfixPlusOperator;
import com.ezylang.evalex.operatorsx.Operator;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

@Value.Immutable
public abstract class Configuration {

	public static final Map<String, com.ezylang.evalex.data.Value<?>> StandardConstants = Collections.unmodifiableMap(standardConstants());

	@Value.Default
	public MathContext getMathContext() {
		return new MathContext(68, RoundingMode.HALF_EVEN);
	}

	@Value.Default
	public OperatorResolver getOperatorResolver() {
		return OperatorResolver.defaults();
	}

	@Value.Default
	public FunctionResolver getFunctionResolver() {
		return FunctionResolver.defaults();
	}


	@Value.Default
	public Map<String, com.ezylang.evalex.data.Value<?>> getDefaultConstants() {
		return standardConstants();
	}

	private static Map<String, com.ezylang.evalex.data.Value<?>> standardConstants() {
		Map<String, com.ezylang.evalex.data.Value<?>> constants = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

		constants.put("TRUE", com.ezylang.evalex.data.Value.of(true));
		constants.put("FALSE", com.ezylang.evalex.data.Value.of(false));
		constants.put(
			"PI",
			com.ezylang.evalex.data.Value.of(
				new BigDecimal(
					"3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679")));
		constants.put(
			"E",
			com.ezylang.evalex.data.Value.of(
				new BigDecimal(
					"2.71828182845904523536028747135266249775724709369995957496696762772407663")));
		//constants.put("NULL", com.ezylang.evalex.data.Value.of(null));
		return constants;
	}

	@Value.Default
	public ZoneId getDefaultZoneId() {
		return ZoneId.systemDefault();
	}

	@Value.Default
	public boolean isImplicitMultiplicationAllowed() {
		return true;
	}

	@Value.Default
	public boolean isArraysAllowed() {
		return true;
	}

	@Value.Default
	public boolean isStructuresAllowed() {
		return true;
	}

	@Value.Default
	public boolean isAllowOverwriteConstants() {
		return true;
	}

	@Value.Auxiliary
	public Configuration withAdditionalOperators(Map.Entry<String, Operator>... operators) {
		return ImmutableConfiguration.copyOf(this)
			.withOperatorResolver(MapBasedOperatorResolver.of(operators)
				.andThen(getOperatorResolver()));
	}

	@Value.Auxiliary
	public Configuration withAdditionalFunctions(Map.Entry<String, Function> ... functions) {
		return ImmutableConfiguration.copyOf(this)
			.withFunctionResolver(MapBasedFunctionResolver.of(functions)
				.andThen(getFunctionResolver()));
	}

	public static Configuration defaultConfiguration() {
		return ImmutableConfiguration.builder().build();
	}
}
