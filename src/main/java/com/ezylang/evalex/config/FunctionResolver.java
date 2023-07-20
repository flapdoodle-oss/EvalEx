package com.ezylang.evalex.config;

import com.ezylang.evalex.functionsx.Function;
import com.ezylang.evalex.functionsx.basic.*;
import com.ezylang.evalex.functionsx.datetime.*;
import com.ezylang.evalex.functionsx.string.StringContains;
import com.ezylang.evalex.functionsx.string.StringLowerFunction;
import com.ezylang.evalex.functionsx.string.StringUpperFunction;
import com.ezylang.evalex.functionsx.trigonometric.*;

public interface FunctionResolver {
	default boolean hasFunction(String functionName) {
		return getFunction(functionName) != null;
	}

	default FunctionResolver andThen(FunctionResolver fallback) {
		FunctionResolver that=this;

		return functionName -> {
			Function function = that.getFunction(functionName);
			if (function==null) {
				return fallback.getFunction(functionName);
			}
			return function;
		};
	}
	/**
	 * Get the function definition for a function name.
	 *
	 * @param functionName The name of the function.
	 * @return The function definition or <code>null</code> if no function was found.
	 */
	Function getFunction(String functionName);

	static FunctionResolver defaults() {
		return MapBasedFunctionResolver.builder()
			.putFunctions("ABS", new AbsFunction())
			.putFunctions("CEILING", new CeilingFunction())
			.putFunctions("FACT", new FactFunction())
			.putFunctions("FLOOR", new FloorFunction())
			.putFunctions("IF", new IfFunction())
			.putFunctions("LOG", new LogFunction())
			.putFunctions("LOG10", new Log10Function())
			.putFunctions("MAX", new MaxFunction())
			.putFunctions("MIN", new MinFunction())
			.putFunctions("NOT", new NotFunction())
			.putFunctions("RANDOM", new RandomFunction())
			.putFunctions("ROUND", new RoundFunction())
			.putFunctions("SUM", new SumFunction())
			.putFunctions("SQRT", new SqrtFunction())
//			// trigonometric
			.putFunctions("ACOS", new AcosFunction())
			.putFunctions("ACOSH", new AcosHFunction())
			.putFunctions("ACOSR", new AcosRFunction())
			.putFunctions("ACOT", new AcotFunction())
			.putFunctions("ACOTH", new AcotHFunction())
			.putFunctions("ACOTR", new AcotRFunction())
			.putFunctions("ASIN", new AsinFunction())
			.putFunctions("ASINH", new AsinHFunction())
			.putFunctions("ASINR", new AsinRFunction())
			.putFunctions("ATAN", new AtanFunction())
			.putFunctions("ATAN2", new Atan2Function())
			.putFunctions("ATAN2R", new Atan2RFunction())
			.putFunctions("ATANH", new AtanHFunction())
			.putFunctions("ATANR", new AtanRFunction())
			.putFunctions("COS", new CosFunction())
			.putFunctions("COSH", new CosHFunction())
			.putFunctions("COSR", new CosRFunction())
			.putFunctions("COT", new CotFunction())
			.putFunctions("COTH", new CotHFunction())
			.putFunctions("COTR", new CotRFunction())
			.putFunctions("CSC", new CscFunction())
			.putFunctions("CSCH", new CscHFunction())
			.putFunctions("CSCR", new CscRFunction())
			.putFunctions("DEG", new DegFunction())
			.putFunctions("RAD", new RadFunction())
			.putFunctions("SIN", new SinFunction())
			.putFunctions("SINH", new SinHFunction())
			.putFunctions("SINR", new SinRFunction())
			.putFunctions("SEC", new SecFunction())
			.putFunctions("SECH", new SecHFunction())
			.putFunctions("SECR", new SecRFunction())
			.putFunctions("TAN", new TanFunction())
			.putFunctions("TANH", new TanHFunction())
			.putFunctions("TANR", new TanRFunction())
			// string functions
			.putFunctions("STR_CONTAINS", new StringContains())
			.putFunctions("STR_LOWER", new StringLowerFunction())
			.putFunctions("STR_UPPER", new StringUpperFunction())
			// date time functions
			.putFunctions("DT_DATE_TIME", new DateTimeFunction())
			.putFunctions("DT_PARSE", new DateTimeParseFunction())
			.putFunctions("DT_ZONED_PARSE", new ZonedDateTimeParseFunction())
			.putFunctions("DT_FORMAT", new DateTimeFormatFunction())
			.putFunctions("DT_EPOCH", new DateTimeToEpochFunction())
			.putFunctions("DT_DATE_TIME_EPOCH", new DateTimeFromEpochFunction())
			.putFunctions("DT_DURATION_MILLIS", new DurationFromMillisFunction())
			.putFunctions("DT_DURATION_DAYS", new DurationFromDaysFunction())
			.putFunctions("DT_DURATION_PARSE", new DurationParseFunction())
			.build();
	}

}
