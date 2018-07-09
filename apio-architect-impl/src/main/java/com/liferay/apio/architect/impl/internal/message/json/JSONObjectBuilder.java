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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Creates JSON objects. Instances of this interface should be used to write a
 * complete path in each call.
 *
 * <p>
 * For example, this {@code nestedField} call produces the JSON object that
 * follows it:
 * </p>
 *
 * <p>
 * <pre>
 * {@code
 * jsonObjectBuilder
 * 	.nestedField("object", "inner", "other")
 * 	.value(42);
 * }
 * </pre><pre>
 * {@code {
 *      "object": {
 *          "inner": {
 *              "other": 42
 *          }
 *      }
 *   }}
 * </pre></p>
 *
 * <p>
 * {@code JSONObjectBuilder} is incremental, so additional calls add paths to
 * previous calls, respecting the previous state. For example, this {@code
 * nestedField} call adds to the preceding one:
 * </p>
 *
 * <p>
 * <pre>
 * {@code
 * jsonObjectBuilder
 *      .nestedField("object", "inner", "another")
 *      .value("Hello World!");
 * }
 * </pre><pre>
 * {@code {
 *      "object": {
 *          "inner": {
 *              "another": "Hello World!",
 *              "other": 42
 *          }
 *      }
 *   }}
 * </pre></p>
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
public class JSONObjectBuilder implements ObjectBuilder<JsonObject> {

	@Override
	public JsonObject buildAsObject() {
		return _jsonObject;
	}

	public String build() {
		return _jsonObject.toString();
	}

	public FieldStepImpl field(String name) {
		return new FieldStepImpl(name, _jsonObject);
	}

	public class ArrayValueStepImpl
		implements
		ObjectBuilder.ArrayValueStep<JsonObject, JSONObjectBuilder> {

		public ArrayValueStepImpl(JsonArray jsonArray) {
			_jsonArray = jsonArray;
		}

		@Override
		public void add(
			Consumer<JSONObjectBuilder> consumer) {
			JSONObjectBuilder objectBuilder = new JSONObjectBuilder();
			consumer.accept(objectBuilder);
			add(objectBuilder);
		}

		public void add(JSONObjectBuilder objectBuilder) {
			_jsonArray.add(objectBuilder.buildAsObject());
		}

		public void addAllBooleans(Collection<Boolean> collection) {
			Stream<Boolean> stream = collection.stream();

			stream.forEach(_jsonArray::add);
		}

		public void addAllJsonObjects(Collection<JsonObject> collection) {
			collection.forEach(_jsonArray::add);
		}

		public void addAllNumbers(Collection<Number> collection) {
			collection.forEach(_jsonArray::add);
		}

		public void addAllStrings(Collection<String> collection) {
			Stream<String> stream = collection.stream();

			stream.forEach(_jsonArray::add);
		}

		public void addBoolean(Boolean value) {
			_jsonArray.add(value);
		}

		public void addNumber(Number value) {
			_jsonArray.add(value);
		}

		public void addString(String value) {
			_jsonArray.add(value);
		}

		private final JsonArray _jsonArray;
	}

	public class FieldStepImpl
		implements
		ObjectBuilder.FieldStep<JsonObject, JSONObjectBuilder,
			FieldStepImpl, ArrayValueStepImpl> {

		public FieldStepImpl(String name, JsonObject jsonObject) {
			_name = name;
			_jsonObject = jsonObject;
		}

		public ArrayValueStepImpl arrayValue() {
			JsonArray jsonArray = Optional.ofNullable(
				_jsonObject.get(_name)
			).filter(
				JsonElement::isJsonArray
			).map(
				JsonArray.class::cast
			).orElseGet(
				JsonArray::new
			);

			_jsonObject.add(_name, jsonArray);

			return new ArrayValueStepImpl(jsonArray);
		}

		public void booleanValue(Boolean value) {
			_jsonObject.addProperty(_name, value);
		}

		public FieldStepImpl field(String name) {
			JsonObject jsonObject = Optional.ofNullable(
				_jsonObject.get(_name)
			).filter(
				JsonElement::isJsonObject
			).map(
				JsonObject.class::cast
			).orElseGet(
				JsonObject::new
			);

			_jsonObject.add(_name, jsonObject);

			return new FieldStepImpl(name, jsonObject);
		}

		public void numberValue(Number value) {
			_jsonObject.addProperty(_name, value);
		}

		public void objectValue(JSONObjectBuilder objectBuilder) {
			JsonObject jsonObject = objectBuilder._jsonObject;

			_jsonObject.add(_name, jsonObject);
		}

		public void stringValue(String value) {
			_jsonObject.addProperty(_name, value);
		}

		private final JsonObject _jsonObject;
		private final String _name;
	}


	private final JsonObject _jsonObject = new JsonObject();

}