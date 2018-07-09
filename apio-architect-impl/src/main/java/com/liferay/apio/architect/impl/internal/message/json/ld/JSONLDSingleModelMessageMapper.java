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

package com.liferay.apio.architect.impl.internal.message.json.ld;

import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_CONTEXT;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_EXPECTS;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_ID;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_MEMBER;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_METHOD;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_OPERATION;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_TOTAL_ITEMS;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_TYPE;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_VOCAB;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.MEDIA_TYPE;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.TYPE_COLLECTION;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.TYPE_OPERATION;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.URL_HYDRA_PROFILE;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.URL_SCHEMA_ORG;

import com.liferay.apio.architect.impl.internal.list.FunctionalList;
import com.liferay.apio.architect.impl.internal.message.json.JSONObjectBuilder;
import com.liferay.apio.architect.impl.internal.message.json.ObjectBuilder;
import com.liferay.apio.architect.impl.internal.message.json.SingleModelMessageMapper;
import com.liferay.apio.architect.operation.HTTPMethod;
import com.liferay.apio.architect.operation.Operation;
import com.liferay.apio.architect.single.model.SingleModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;

/**
 * Represents single models in JSON-LD + Hydra format.
 *
 * <p>
 * For more information, see <a href="https://json-ld.org/">JSON-LD </a> and <a
 * href="https://www.hydra-cg.com/">Hydra </a> .
 * </p>
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
@Component
public class JSONLDSingleModelMessageMapper<T>
	implements SingleModelMessageMapper<T> {

	@Override
	public String getMediaType() {
		return MEDIA_TYPE;
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
	public void mapEmbeddedOperationFormURL(
		ObjectBuilder singleModelObjectBuilder,
		ObjectBuilder operationObjectBuilder,
		FunctionalList<String> embeddedPathElements, String url) {

		operationObjectBuilder.field(
			FIELD_NAME_EXPECTS
		).stringValue(
			url
		);
	}

	@Override
	public void mapEmbeddedOperationMethod(
		ObjectBuilder singleModelObjectBuilder,
		ObjectBuilder operationObjectBuilder,
		FunctionalList<String> embeddedPathElements, HTTPMethod httpMethod) {

		operationObjectBuilder.field(
			FIELD_NAME_METHOD
		).stringValue(
			httpMethod.name()
		);
	}

	@Override
	public void mapEmbeddedResourceBooleanField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		Boolean value) {

		objectBuilder.nestedField(
			embeddedPathElements.head(), _getTail(embeddedPathElements)
		).field(
			fieldName
		).booleanValue(
			value
		);
	}

	@Override
	public void mapEmbeddedResourceBooleanListField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		List<Boolean> value) {

		objectBuilder.nestedField(
			embeddedPathElements.head(), _getTail(embeddedPathElements)
		).field(
			fieldName
		).arrayValue(
		).addAllBooleans(
			value
		);
	}

	@Override
	public void mapEmbeddedResourceLink(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		String url) {

		objectBuilder.nestedField(
			embeddedPathElements.head(), _getTail(embeddedPathElements)
		).field(
			fieldName
		).stringValue(
			url
		);
	}

	@Override
	public void mapEmbeddedResourceNumberField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		Number value) {

		objectBuilder.nestedField(
			embeddedPathElements.head(), _getTail(embeddedPathElements)
		).field(
			fieldName
		).numberValue(
			value
		);
	}

	@Override
	public void mapEmbeddedResourceNumberListField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		List<Number> value) {

		objectBuilder.nestedField(
			embeddedPathElements.head(), _getTail(embeddedPathElements)
		).field(
			fieldName
		).arrayValue(
		).addAllNumbers(
			value
		);
	}

	@Override
	public void mapEmbeddedResourceStringField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		String value) {

		objectBuilder.nestedField(
			embeddedPathElements.head(), _getTail(embeddedPathElements)
		).field(
			fieldName
		).stringValue(
			value
		);
	}

	@Override
	public void mapEmbeddedResourceStringListField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		List<String> value) {

		objectBuilder.nestedField(
			embeddedPathElements.head(), _getTail(embeddedPathElements)
		).field(
			fieldName
		).arrayValue(
		).addAllStrings(
			value
		);
	}

	@Override
	public void mapEmbeddedResourceTypes(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, List<String> types) {

		objectBuilder.nestedField(
			embeddedPathElements.head(), _getTail(embeddedPathElements)
		).field(
			FIELD_NAME_TYPE
		).arrayValue(
		).addAllStrings(
			types
		);
	}

	@Override
	public void mapEmbeddedResourceURL(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String url) {

		objectBuilder.nestedField(
			embeddedPathElements.head(), _getTail(embeddedPathElements)
		).field(
			FIELD_NAME_ID
		).stringValue(
			url
		);
	}

	@Override
	public void mapFormURL(ObjectBuilder objectBuilder, String url) {
		objectBuilder.field(
			FIELD_NAME_EXPECTS
		).stringValue(
			url
		);
	}

	@Override
	public void mapHTTPMethod(
		ObjectBuilder objectBuilder, HTTPMethod httpMethod) {

		objectBuilder.field(
			FIELD_NAME_METHOD
		).stringValue(
			httpMethod.name()
		);
	}

	@Override
	public void mapLink(
		ObjectBuilder objectBuilder, String fieldName, String url) {

		objectBuilder.field(
			fieldName
		).stringValue(
			url
		);
	}

	@Override
	public void mapLinkedResourceURL(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String url) {

		String head = embeddedPathElements.head();

		objectBuilder.nestedField(
			head, _getTail(embeddedPathElements)
		).stringValue(
			url
		);

		Optional<String> optional = embeddedPathElements.lastOptional();

		JSONObjectBuilder jsonObjectBuilder = (JSONObjectBuilder) objectBuilder;

		JSONObjectBuilder.ArrayValueStepImpl arrayValueStep =
			(JSONObjectBuilder.ArrayValueStepImpl) jsonObjectBuilder.ifElseCondition(
				optional.isPresent(),
				builder -> builder.nestedField(
					head, _getMiddle(embeddedPathElements)
				).field(
					FIELD_NAME_CONTEXT
				),
				builder -> builder.field(FIELD_NAME_CONTEXT)
			).arrayValue(
			);
		arrayValueStep.add(
			builder -> builder.field(
				optional.orElse(head)
			).field(
				FIELD_NAME_TYPE
			).stringValue(
				FIELD_NAME_ID
			)
		);
	}

	@Override
	public void mapNestedPageItemTotalCount(
		ObjectBuilder objectBuilder, int totalCount) {

		objectBuilder.field(
			FIELD_NAME_TOTAL_ITEMS
		).numberValue(
			totalCount
		);
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
		objectBuilder.field(
			FIELD_NAME_ID
		).stringValue(
			url
		);
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

	@Override
	public void mapTypes(
		ObjectBuilder objectBuilder, List<String> types) {

		objectBuilder.field(
			FIELD_NAME_TYPE
		).arrayValue(
		).addAllStrings(
			types
		);
	}

	@Override
	public void onFinish(
		ObjectBuilder resourceObjectBuilder,
		ObjectBuilder operationObjectBuilder, Operation operation) {

		operationObjectBuilder.field(
			FIELD_NAME_ID
		).stringValue(
			"_:" + operation.getName()
		);

		operationObjectBuilder.field(
			FIELD_NAME_TYPE
		).stringValue(
			TYPE_OPERATION
		);

		resourceObjectBuilder.field(
			FIELD_NAME_OPERATION
		).arrayValue(
		).add(
			operationObjectBuilder
		);
	}

	@Override
	public void onFinish(
		ObjectBuilder objectBuilder, SingleModel<T> singleModel) {

		JSONObjectBuilder jsonObjectBuilder = (JSONObjectBuilder) objectBuilder;

		jsonObjectBuilder.field(
			FIELD_NAME_CONTEXT
		).arrayValue(
			arrayBuilder -> arrayBuilder.add(
				builder -> builder.field(
					FIELD_NAME_VOCAB
				).stringValue(
					URL_SCHEMA_ORG
				)),
			arrayBuilder -> arrayBuilder.addString(URL_HYDRA_PROFILE)
		);
	}

	@Override
	public void onFinishEmbeddedOperation(
		ObjectBuilder singleModelObjectBuilder,
		ObjectBuilder operationObjectBuilder,
		FunctionalList<String> embeddedPathElements, Operation operation) {

		String head = embeddedPathElements.head();
		String[] tail = _getTail(embeddedPathElements);

		operationObjectBuilder.field(
			FIELD_NAME_ID
		).stringValue(
			"_:" + operation.getName()
		);

		operationObjectBuilder.field(
			FIELD_NAME_TYPE
		).stringValue(
			TYPE_OPERATION
		);

		singleModelObjectBuilder.nestedField(
			head, tail
		).field(
			FIELD_NAME_OPERATION
		).arrayValue(
		).add(
			operationObjectBuilder
		);
	}

	@Override
	public void onFinishNestedCollection(
		ObjectBuilder singleModelObjectBuilder,
		ObjectBuilder collectionObjectBuilder, String fieldName,
		List<?> list, FunctionalList<String> embeddedPathElements) {

		collectionObjectBuilder.field(
			FIELD_NAME_TYPE
		).arrayValue(
		).addString(
			TYPE_COLLECTION
		);

		singleModelObjectBuilder.nestedField(
			embeddedPathElements.head(), _getTail(embeddedPathElements)
		).objectValue(
			collectionObjectBuilder
		);
	}

	@Override
	public void onFinishNestedCollectionItem(
		ObjectBuilder collectionObjectBuilder,
		ObjectBuilder itemObjectBuilder, SingleModel<?> singleModel) {

		collectionObjectBuilder.field(
			FIELD_NAME_MEMBER
		).arrayValue(
		).add(
			itemObjectBuilder
		);
	}

	private String[] _getMiddle(FunctionalList<String> embeddedPathElements) {
		Stream<String> stream = embeddedPathElements.middleStream();

		return stream.toArray(String[]::new);
	}

	private String[] _getTail(FunctionalList<String> embeddedPathElements) {
		Stream<String> stream = embeddedPathElements.tailStream();

		return stream.toArray(String[]::new);
	}

}