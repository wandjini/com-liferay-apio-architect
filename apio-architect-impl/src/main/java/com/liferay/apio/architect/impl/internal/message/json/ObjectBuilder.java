/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.apio.architect.impl.internal.message.json;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Creates objects (json, xml...) to be represented.
 * Instances of this interface should be used to write a complete path in
 * each call.
 *
 * @author Javier Gamarra
 * @review
 */
public interface ObjectBuilder<O> {

	/**
	 * Returns the JSON object constructed by the JSON object builder.
	 *
	 * @return the JSON object
	 */
	public O buildAsObject();

	/**
	 * Returns the string constructed by the JSON object builder.
	 *
	 * @return a string with the contet
	 * @review
	 */
	public String build();

	/**
	 * Begins creating a field inside the JSON object.
	 *
	 * @param  name the field's name
	 * @return the builder's next step
	 */
	public <U extends ObjectBuilder.FieldStep> U field(String name);

	/**
	 * Conditionally begins creating a field inside the JSON object. If the
	 * condition is met, this method returns the field step that {@code
	 * ifFunction} creates. Otherwise, this method returns the field step that
	 * {@code elseFunction} creates.
	 *
	 * @param  condition the condition to check
	 * @param  ifFunction the function that creates the field step if the
	 *         condition is {@code true}
	 * @param  elseFunction the function that creates the field step if the
	 *         condition is {@code false}
	 * @return the builder's field step
	 */
	public default <R extends ObjectBuilder, U extends ObjectBuilder.FieldStep> U ifElseCondition(
		boolean condition, Function<R, U> ifFunction,
		Function<R, U> elseFunction) {

		if (condition) {
			return ifFunction.apply((R) this);
		}
		else {
			return elseFunction.apply((R) this);
		}
	}

	/**
	 * Begins creating a nested field inside the JSON object.
	 *
	 * @param  parentName the parent field's name
	 * @param  nestedNames the nested field's list of names
	 * @return the builder's field step
	 */
	public default FieldStep nestedField(
		String parentName, String... nestedNames) {
		FieldStep fieldStep = field(parentName);

		for (String nestedName : nestedNames) {
			fieldStep = fieldStep.field(nestedName);
		}

		return fieldStep;
	}

	/**
	 * Begins creating a nested field inside the JSON object, adding a prefix to
	 * each level.
	 *
	 * <p>
	 * For example, the following {@code nestedPrefixedField} call produces the
	 * JSON object that follows it:
	 * </p>
	 *
	 * <p>
	 * <pre>
	 * {@code jsonObjectBuilder
	 * 	.nestedPrefixedField("prefix", "first", "second")
	 * 	.value(42);
	 * }
	 * </pre><pre>
	 * {@code {
	 * 	"prefix": {
	 * 	    "first": {
	 * 		"prefix": {
	 * 		    "second": 42
	 * 	        }
	 * 	    }
	 *       }
	 *   }}
	 * </pre></p>
	 *
	 * @param  prefix each field's prefix
	 * @param  parentName the parent field's name
	 * @param  nestedNames the list of the nested field names
	 * @return the builder's field step
	 */
	public default FieldStep nestedPrefixedField(
		String prefix, String parentName, String... nestedNames) {

		FieldStep fieldStep = nestedField(prefix, parentName);

		for (String nestedName : nestedNames) {
			fieldStep = fieldStep.nestedField(prefix, nestedName);
		}

		return fieldStep;
	}

	/**
	 * Begins creating a nested field inside the JSON object, adding a suffix to
	 * each level.
	 *
	 * <p>
	 * For example, the following {@code nestedSuffixedField} call produces the
	 * JSON object that follows it:
	 * </p>
	 *
	 * <p>
	 * <pre>
	 * {@code jsonObjectBuilder
	 * 	.nestedSuffixedField("suffix", "first", "second")
	 * 	.value(42);
	 * }
	 * </pre><pre>
	 * {@code {
	 * 	"first": {
	 * 	    "suffix": {
	 * 		"second": {
	 * 		    "suffix": 42
	 * 	        }
	 * 	    }
	 *       }
	 *   }}
	 * </pre></p>
	 *
	 * @param  suffix each field's suffix
	 * @param  parentName the parent field's name
	 * @param  nestedNames the list of the nested field names
	 * @return the builder's field step
	 */
	public default FieldStep nestedSuffixedField(
		String suffix, String parentName, String... nestedNames) {

		FieldStep fieldStep = nestedField(parentName, suffix);

		for (String nestedName : nestedNames) {
			fieldStep = fieldStep.nestedField(nestedName, suffix);
		}

		return fieldStep;
	}

	public interface ArrayValueStep
		<O, S extends ObjectBuilder<O>> {

		public void add(Consumer<S> consumer);

		/**
		 * Adds several JSON object, created by the provided consumers, to the
		 * JSON array.
		 *
		 * @param  consumer the consumer that creates the new JSON object
		 * @param  consumers the list of consumers that creates new JSON objects
		 * @review
		 */
		public default void add(
			Consumer<S> consumer,
			Consumer<S>... consumers) {

			add(consumer);

			for (Consumer<S> objectBuilderConsumer : consumers) {

				add(objectBuilderConsumer);
			}
		}

		/**
		 * Adds the JSON object, created by the provided JSON object builder, to
		 * the JSON array.
		 *
		 * @param objectBuilder the JSON object builder containing the JSON
		 *        object to add to the JSON array
		 */
		public void add(S objectBuilder);

		/**
		 * Adds all elements of a boolean collection as elements of the JSON
		 * array.
		 *
		 * @param collection the boolean collection to add to the JSON array
		 */
		public void addAllBooleans(Collection<Boolean> collection);

		/**
		 * Adds all elements of a JSON object collection as elements of the JSON
		 * array.
		 *
		 * @param collection the JSON object collection to add to the JSON array
		 */
		public void addAllJsonObjects(Collection<O> collection);

		/**
		 * Adds all elements of a number collection as elements of the JSON
		 * array.
		 *
		 * @param collection the number collection to add to the JSON array
		 */
		public void addAllNumbers(Collection<Number> collection);

		/**
		 * Adds all elements of a string collection as elements of the JSON
		 * array.
		 *
		 * @param collection the string collection to add to the JSON array
		 */
		public void addAllStrings(Collection<String> collection);

		/**
		 * Adds a new boolean value to the JSON array.
		 *
		 * @param value the boolean value to add to the JSON array
		 */
		public void addBoolean(Boolean value);

		/**
		 * Adds a new number to the JSON array.
		 *
		 * @param value the number to add to the JSON array
		 */
		public void addNumber(Number value);

		/**
		 * Adds a new string to the JSON array.
		 *
		 * @param value the string to add to the JSON array
		 */
		public void addString(String value);

	}

	/**
	 * Defines the step to add the value of a field. The step can be another
	 * JSON object (field methods), a JSON array ({@link #arrayValue()}), or a
	 * primitive value ({@link #stringValue(String)}, {@link
	 * #numberValue(Number)}, or {@link #booleanValue(Boolean)}).
	 */
	public interface FieldStep
		<O, S extends ObjectBuilder<O>,
			U extends FieldStep<O, S, U, A>, A extends ArrayValueStep<O, S>> {

		/**
		 * Begins creating a JSON array inside the field.
		 *
		 * @return the builder's array value step
		 */
		public A arrayValue();

		/**
		 * Creates a JSON array inside the field and populates it with the
		 * provided consumers.
		 *
		 * @param consumer the consumer that creates the first JSON object of
		 *        the array
		 * @param consumers the list of consumers that creates the rest of JSON
		 *        objects of the array
		 */
		public default void arrayValue(
			Consumer<A> consumer,
			Consumer<A>... consumers) {

			A arrayValueStep = arrayValue();

			consumer.accept(arrayValueStep);

			for (Consumer<A> arrayValueStepConsumer : consumers) {
				arrayValueStepConsumer.accept(arrayValueStep);
			}
		}

		/**
		 * Adds a new boolean value to the JSON object.
		 *
		 * @param value the boolean value to add to the JSON object
		 */
		public void booleanValue(Boolean value);

		/**
		 * Begins creating a new JSON object field.
		 *
		 * @param  name the new field's name
		 * @return the builder's field step
		 */
		public U field(String name);

		/**
		 * Creates a new JSON object inside the field and populates it with the
		 * provided consumers.
		 *
		 * @param consumer the consumer that first populates the JSON Object
		 * @param consumers the rest of the list of consumers that populates the
		 *        JSON Object
		 */
		public default void fields(
			Consumer<U> consumer, Consumer<U>... consumers) {

			consumer.accept((U) this);

			for (Consumer<U> fieldStepConsumer : consumers) {
				fieldStepConsumer.accept((U) this);
			}
		}

		/**
		 * Begins creating a new JSON object field, only if a condition is met.
		 * If the condition is met, this method returns the field step created
		 * by {@code ifFunction}. Otherwise, no operation is performed.
		 *
		 * @param  condition the condition to check
		 * @param  ifFunction the function that creates the field step if the
		 *         condition is {@code true}
		 * @return the builder's field step
		 */
		public default U ifCondition(
			boolean condition, Function<U, U> ifFunction) {

			if (condition) {
				return ifFunction.apply((U) this);
			}
			else {
				return (U) this;
			}
		}

		/**
		 * Begins creating a new JSON object field, where the resulting field
		 * step is conditional. If the condition is met, this method returns the
		 * field step created by {@code ifFunction}. Otherwise, this method
		 * returns the field step created by {@code elseFunction}.
		 *
		 * @param  condition the condition to check
		 * @param  ifFunction the function that creates the field step if the
		 *         condition is {@code true}
		 * @param  elseFunction the function that creates the field step if the
		 *         condition is {@code false}
		 * @return the builder's field step
		 */
		public default U ifElseCondition(
			boolean condition, Function<U, U> ifFunction,
			Function<U, U> elseFunction) {

			if (condition) {
				return ifFunction.apply((U) this);
			}
			else {
				return elseFunction.apply((U) this);
			}
		}

		/**
		 * Begins creating a new nested JSON object field.
		 *
		 * @param  parentName the parent field's name
		 * @param  nestedNames the list of the nested field names
		 * @return the builder's field step
		 */
		public default U nestedField(
			String parentName, String... nestedNames) {
			U fieldStep = field(parentName);

			for (String nestedName : nestedNames) {
				fieldStep = fieldStep.field(nestedName);
			}

			return fieldStep;
		}

		/**
		 * Begins creating a new nested JSON object field, adding a prefix to
		 * each field. This method behaves like {@link
		 * #nestedPrefixedField(String, String, String...)}.
		 *
		 * @param  prefix each field's prefix
		 * @param  parentName the parent field's name
		 * @param  nestedNames the list of the nested field names
		 * @return the builder's field step
		 */
		public default U nestedPrefixedField(
			String prefix, String parentName, String... nestedNames) {

			U fieldStep = nestedField(prefix, parentName);

			for (String nestedName : nestedNames) {
				fieldStep = fieldStep.nestedField(prefix, nestedName);
			}

			return fieldStep;
		}

		/**
		 * Begins creating a new nested JSON object field, adding a suffix to
		 * each field. This method behaves like {@link
		 * #nestedSuffixedField(String, String, String...)}.
		 *
		 * @param  suffix each field's suffix
		 * @param  parentName the parent field's name
		 * @param  nestedNames the list of the nested field names
		 * @return the builder's field step
		 */
		public default U nestedSuffixedField(
			String suffix, String parentName, String... nestedNames) {

			U fieldStep = nestedField(parentName, suffix);

			for (String nestedName : nestedNames) {
				fieldStep = fieldStep.nestedField(nestedName, suffix);
			}

			return fieldStep;
		}

		/**
		 * Adds a new number to the JSON object.
		 *
		 * @param value the number to add to the JSON object
		 */
		public void numberValue(Number value);

		/**
		 * Adds the JSON object created by another {@link ObjectBuilder}.
		 *
		 * @param  objectBuilder the {@link ObjectBuilder} whose JSON
		 *         object is going to be added
		 * @review
		 */
		public void objectValue(S objectBuilder);

		/**
		 * Adds a new string to the JSON object.
		 *
		 * @param value the string to add to the JSON object
		 */
		public void stringValue(String value);

	}

}