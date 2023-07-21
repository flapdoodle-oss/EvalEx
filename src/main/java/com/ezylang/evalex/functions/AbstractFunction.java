package com.ezylang.evalex.functions;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.Value;
import com.ezylang.evalex.data.VariableResolver;
import com.ezylang.evalex.parser.Token;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractFunction implements Function {
	private final List<FunctionParameterDefinition<?>> parameterDefinitions;

	protected AbstractFunction(FunctionParameterDefinition<?> definition, FunctionParameterDefinition<?>... definitions) {
		this.parameterDefinitions = Collections.unmodifiableList(Stream.concat(Stream.of(definition), Stream.of(definitions)).collect(Collectors.toList()));

		for (int i = 0; i < this.parameterDefinitions.size() -1; i++) {
			FunctionParameterDefinition<?> it = this.parameterDefinitions.get(i);
			if (it.isVarArg()) {
				throw new IllegalArgumentException(
					"Only last parameter may be defined as variable argument");
			}
			if (it.isOptional()) {
				throw new IllegalArgumentException(
					"Only last parameter may be defined as optional argument");
			}
		}
	}

	@Override
	public List<FunctionParameterDefinition<?>> parameterDefinitions() {
		return parameterDefinitions;
	}

	public abstract static class Single<T extends Value<?>> extends AbstractFunction {

		private final FunctionParameterDefinition<T> definition;

		protected Single(FunctionParameterDefinition<T> definition) {
			super(definition);
			if (definition.isVarArg()) throw new IllegalArgumentException("varArg is true");
			this.definition = definition;
		}

		protected Single(Class<T> parameterType, String name) {
			this(FunctionParameterDefinition.of(parameterType, name));
		}

		protected Single(Class<T> parameterType) {
			this(parameterType, "value");
		}

		@Override
		public final Value<?> evaluate(VariableResolver variableResolver, Expression expression, Token functionToken, List<Value<?>> parameterValues)
			throws EvaluationException {
			if (parameterValues.size()!=1) throw EvaluationException.ofUnsupportedDataTypeInOperation(functionToken);
			return evaluate(variableResolver, expression, functionToken, definition.parameterType().cast(parameterValues.get(0)));
		}

		public abstract Value<?> evaluate(VariableResolver variableResolver, Expression expression, Token functionToken, T parameterValue)
			throws EvaluationException;
	}

	public abstract static class SingleVararg<T extends Value<?>> extends AbstractFunction {

		private final FunctionParameterDefinition<T> definition;

		protected SingleVararg(FunctionParameterDefinition<T> definition) {
			super(definition);
			if (!definition.isVarArg()) throw new IllegalArgumentException("varArg is false");
			this.definition = definition;
		}

		protected SingleVararg(Class<T> parameterType, String name) {
			this(FunctionParameterDefinition.of(parameterType, name));
		}

		protected SingleVararg(Class<T> parameterType) {
			this(parameterType, "value");
		}

		@Override
		public final Value<?> evaluate(VariableResolver variableResolver, Expression expression, Token functionToken, List<Value<?>> parameterValues)
			throws EvaluationException {
			return evaluateVarArg(variableResolver, expression, functionToken, parameterValues.stream().map(it -> definition.parameterType().cast(it)).collect(
				Collectors.toList()));
		}

		public abstract Value<?> evaluateVarArg(VariableResolver variableResolver, Expression expression, Token functionToken, List<T> parameterValues);
	}

	public abstract static class Tuple<A extends Value<?>, B extends Value<?>> extends AbstractFunction {

		private final FunctionParameterDefinition<A> a;
		private final FunctionParameterDefinition<B> b;

		protected Tuple(FunctionParameterDefinition<A> a, FunctionParameterDefinition<B> b) {
			super(a, b);
			if (a.isVarArg()) throw new IllegalArgumentException("a.varArg is true");
			if (b.isVarArg()) throw new IllegalArgumentException("b.varArg is true");
			this.a = a;
			this.b = b;
		}

		@Override
		public final Value<?> evaluate(VariableResolver variableResolver, Expression expression, Token functionToken, List<Value<?>> parameterValues)
			throws EvaluationException {
			if (parameterValues.size()!=2) throw EvaluationException.ofUnsupportedDataTypeInOperation(functionToken);
			return evaluate(variableResolver, expression, functionToken,
				a.parameterType().cast(parameterValues.get(0)),
				b.parameterType().cast(parameterValues.get(1))
			);
		}

		public abstract Value<?> evaluate(VariableResolver variableResolver, Expression expression, Token functionToken, A a, B b)
			throws EvaluationException;
	}

	public abstract static class Triple<A extends Value<?>, B extends Value<?>, C extends Value<?>> extends AbstractFunction {

		private final FunctionParameterDefinition<A> a;
		private final FunctionParameterDefinition<B> b;
		private final FunctionParameterDefinition<C> c;

		protected Triple(FunctionParameterDefinition<A> a, FunctionParameterDefinition<B> b, FunctionParameterDefinition<C> c) {
			super(a, b, c);
			if (a.isVarArg()) throw new IllegalArgumentException("a.varArg is true");
			if (b.isVarArg()) throw new IllegalArgumentException("b.varArg is true");
			if (c.isVarArg()) throw new IllegalArgumentException("c.varArg is true");
			this.a = a;
			this.b = b;
			this.c = c;
		}

		@Override
		public final Value<?> evaluate(VariableResolver variableResolver, Expression expression, Token functionToken, List<Value<?>> parameterValues)
			throws EvaluationException {
			if (parameterValues.size()!=3) throw EvaluationException.ofUnsupportedDataTypeInOperation(functionToken);
			return evaluate(variableResolver, expression, functionToken,
				a.parameterType().cast(parameterValues.get(0)),
				b.parameterType().cast(parameterValues.get(1)),
				c.parameterType().cast(parameterValues.get(2))
			);
		}

		public abstract Value<?> evaluate(VariableResolver variableResolver, Expression expression, Token functionToken, A a, B b, C c)
			throws EvaluationException;
	}
}
