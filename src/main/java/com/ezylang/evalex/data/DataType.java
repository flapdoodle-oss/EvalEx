package com.ezylang.evalex.data;

import com.ezylang.evalex.parser.ASTNode;

import java.math.BigDecimal;

/**
 * The supported data types.
 */
public enum DataType {
	/**
	 * A string of characters, stored as {@link String}.
	 */
	STRING,
	/**
	 * Any number, stored as {@link BigDecimal}.
	 */
	NUMBER,
	/**
	 * A boolean, stored as {@link Boolean}.
	 */
	BOOLEAN,
	/**
	 * A date time value, stored as {@link java.time.Instant}.
	 */
	DATE_TIME,
	/**
	 * A period value, stored as {@link java.time.Duration}.
	 */
	DURATION,
	/**
	 * A list evaluation values. Stored as {@link java.util.List<Value>}.
	 */
	ARRAY,
	/**
	 * A structure with pairs of name/value members. Name is a string and the value is a {@link
	 * Value}. Stored as a {@link java.util.Map}.
	 */
	STRUCTURE,
	/**
	 * Used for lazy parameter evaluation, stored as an {@link ASTNode}, which can be evaluated on
	 * demand.
	 */
	EXPRESSION_NODE,
	/**
	 * A null value
	 */
	NULL
}
