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

package com.liferay.apio.architect.impl.internal.message.json.hal;

import com.liferay.apio.architect.impl.internal.list.FunctionalList;
import com.liferay.apio.architect.impl.internal.message.json.JSONObjectBuilder;
import com.liferay.apio.architect.impl.internal.message.json.ObjectBuilder;
import com.liferay.apio.architect.impl.internal.message.json.SingleModelMessageMapper;
import org.osgi.service.component.annotations.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents single models in <a
 * href="http://stateless.co/hal_specification.html">HAL </a> format.
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
@Component
public class HALSingleModelMessageMapper<T>
	implements SingleModelMessageMapper<T> {

	@Override
	public String getMediaType() {
		return "application/hal+json";
	}

	@Override
	public void mapBooleanField(
		ObjectBuilder objectBuilder, String fieldName, Boolean value) {

		objectBuilder.field(
			fieldName
		).booleanValue(
			value
		);
	}

	@Override
	public void mapBooleanListField(
		ObjectBuilder objectBuilder, String fieldName,
		List<Boolean> value) {

		objectBuilder.field(
			fieldName
		).arrayValue(
		).addAllBooleans(
			value
		);
	}

	@Override
	public void mapEmbeddedResourceBooleanField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		Boolean value) {

		_mapEmbeddedResourceField(
			objectBuilder, embeddedPathElements, fieldName,
			builder -> builder.booleanValue(value));
	}

	@Override
	public void mapEmbeddedResourceBooleanListField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		List<Boolean> value) {

		_mapEmbeddedResourceField(
			objectBuilder, embeddedPathElements, fieldName,
			builder -> builder.arrayValue(
			).addAllBooleans(
				value
			));
	}

	@Override
	public void mapEmbeddedResourceLink(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		String url) {

		Optional<String> optional = embeddedPathElements.lastOptional();

		String head = embeddedPathElements.head();

		Stream<String> middleStream = embeddedPathElements.middleStream();

		String[] middle = middleStream.toArray(String[]::new);

		objectBuilder.field(
			"_embedded"
		).ifElseCondition(
			optional.isPresent(),
			new Function<ObjectBuilder.FieldStep, ObjectBuilder.FieldStep>() {
				@Override
				public ObjectBuilder.FieldStep apply(
					ObjectBuilder.FieldStep fieldStep) {
					return fieldStep.nestedSuffixedField(
						"_embedded", head, middle
					).field(
						optional.get()
					);
				}
			},
//			builder -> builder.nestedSuffixedField(
//				"_embedded", head, middle
//			).field(
//				optional.get()
//			),
			new Function<ObjectBuilder.FieldStep, ObjectBuilder.FieldStep>() {
				@Override
				public ObjectBuilder.FieldStep apply(
					ObjectBuilder.FieldStep fieldStep) {
					return fieldStep.field(head);
				}
			}
//			builder -> builder.field(head)
		).nestedField(
			"_links", fieldName, "href"
		).stringValue(
			url
		);
	}

	@Override
	public void mapEmbeddedResourceNumberField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		Number value) {

		_mapEmbeddedResourceField(
			objectBuilder, embeddedPathElements, fieldName,
			builder -> builder.numberValue(value));
	}

	@Override
	public void mapEmbeddedResourceNumberListField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		List<Number> value) {

		_mapEmbeddedResourceField(
			objectBuilder, embeddedPathElements, fieldName,
			builder -> builder.arrayValue(
			).addAllNumbers(
				value
			));
	}

	@Override
	public void mapEmbeddedResourceStringField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		String value) {

		_mapEmbeddedResourceField(
			objectBuilder, embeddedPathElements, fieldName,
			builder -> builder.stringValue(value));
	}

	@Override
	public void mapEmbeddedResourceStringListField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		List<String> value) {

		_mapEmbeddedResourceField(
			objectBuilder, embeddedPathElements, fieldName,
			builder -> builder.arrayValue(
			).addAllStrings(
				value
			));
	}

	@Override
	public void mapEmbeddedResourceURL(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String url) {

		mapEmbeddedResourceLink(
			objectBuilder, embeddedPathElements, "self", url);
	}

	@Override
	public void mapLink(
		ObjectBuilder objectBuilder, String fieldName, String url) {

		objectBuilder.nestedField(
			"_links", fieldName, "href"
		).stringValue(
			url
		);
	}

	@Override
	public void mapLinkedResourceURL(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String url) {

		Optional<String> optional = embeddedPathElements.lastOptional();

		String head = embeddedPathElements.head();

		if (!optional.isPresent()) {
			objectBuilder.nestedField(
				"_links", head, "href"
			).stringValue(
				url
			);
		}
		else {
			Stream<String> middleStream = embeddedPathElements.middleStream();

			List<String> middleList = middleStream.collect(Collectors.toList());

			if (!middleList.isEmpty()) {
				String prelast = middleList.remove(middleList.size() - 1);

				String[] middle = middleList.toArray(
					new String[middleList.size()]);

				objectBuilder.field(
					"_embedded"
				).nestedSuffixedField(
					"_embedded", head, middle
				).nestedField(
					prelast, "_links", optional.get(), "href"
				).stringValue(
					url
				);
			}
			else {
				objectBuilder.field(
					"_embedded"
				).nestedField(
					head, "_links", optional.get(), "href"
				).stringValue(
					url
				);
			}
		}
	}

	@Override
	public void mapNumberField(
		ObjectBuilder objectBuilder, String fieldName, Number value) {

		objectBuilder.field(
			fieldName
		).numberValue(
			value
		);
	}

	@Override
	public void mapNumberListField(
		ObjectBuilder objectBuilder, String fieldName,
		List<Number> value) {

		objectBuilder.field(
			fieldName
		).arrayValue(
		).addAllNumbers(
			value
		);
	}

	@Override
	public void mapSelfURL(ObjectBuilder objectBuilder, String url) {
		mapLink(objectBuilder, "self", url);
	}

	@Override
	public void mapStringField(
		ObjectBuilder objectBuilder, String fieldName, String value) {

		objectBuilder.field(
			fieldName
		).stringValue(
			value
		);
	}

	@Override
	public void mapStringListField(
		ObjectBuilder objectBuilder, String fieldName,
		List<String> value) {

		objectBuilder.field(
			fieldName
		).arrayValue(
		).addAllStrings(
			value
		);
	}

	private void _mapEmbeddedResourceField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		Consumer<ObjectBuilder.FieldStep> consumer) {

		Optional<String> optional = embeddedPathElements.lastOptional();

		String head = embeddedPathElements.head();

		Stream<String> middleStream = embeddedPathElements.middleStream();

		String[] middle = middleStream.toArray(String[]::new);

		JSONObjectBuilder jsonObjectBuilder = (JSONObjectBuilder) objectBuilder;

		JSONObjectBuilder.FieldStepImpl embedded =
			jsonObjectBuilder.field(
				"_embedded"
			);
		ObjectBuilder.FieldStep builderStep = embedded.ifElseCondition(
			optional.isPresent(),
			builder -> builder.nestedSuffixedField(
				"_embedded", head, middle
			).field(
				optional.get()
			),
			builder -> builder.field(head)
		).field(
			fieldName
		);

		consumer.accept(builderStep);
	}

}