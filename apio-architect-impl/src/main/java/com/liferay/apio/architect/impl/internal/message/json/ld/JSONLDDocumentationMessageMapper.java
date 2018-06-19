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
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_DESCRIPTION;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_ID;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_MEMBER;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_NUMBER_OF_ITEMS;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_PROPERTY;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_TITLE;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_TOTAL_ITEMS;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_TYPE;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_VOCAB;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.MEDIA_TYPE;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.TYPE_API_DOCUMENTATION;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.TYPE_CLASS;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.TYPE_COLLECTION;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.TYPE_OPERATION;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.TYPE_SUPPORTED_PROPERTY;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.URL_HYDRA_PROFILE;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.URL_SCHEMA_ORG;
import static com.liferay.apio.architect.operation.HTTPMethod.DELETE;
import static com.liferay.apio.architect.operation.HTTPMethod.GET;

import com.liferay.apio.architect.impl.internal.documentation.Documentation;
import com.liferay.apio.architect.impl.internal.message.json.DocumentationMessageMapper;
import com.liferay.apio.architect.impl.internal.message.json.JSONObjectBuilder;
import com.liferay.apio.architect.operation.HTTPMethod;
import com.liferay.apio.architect.operation.Operation;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.ws.rs.core.HttpHeaders;

import org.osgi.service.component.annotations.Component;

/**
 * Represents documentation in JSON-LD + Hydra format.
 *
 * <p>
 * For more information, see <a href="https://json-ld.org/">JSON-LD </a> and <a
 * href="https://www.hydra-cg.com/">Hydra </a> .
 * </p>
 *
 * @author Alejandro Hernández
 * @author Zoltán Takács
 */
@Component
public class JSONLDDocumentationMessageMapper
	implements DocumentationMessageMapper {

	@Override
	public String getMediaType() {
		return MEDIA_TYPE;
	}

	@Override
	public void mapDescription(
		JSONObjectBuilder jsonObjectBuilder, String description) {

		jsonObjectBuilder.field(
			FIELD_NAME_DESCRIPTION
		).stringValue(
			description
		);
	}

	@Override
	public void mapOperation(
		JSONObjectBuilder jsonObjectBuilder, String resourceName, String type,
		Operation operation) {

		jsonObjectBuilder.field(
			FIELD_NAME_ID
		).stringValue(
			"_:" + operation.getName()
		);

		jsonObjectBuilder.field(
			FIELD_NAME_TYPE
		).stringValue(
			TYPE_OPERATION
		);

		jsonObjectBuilder.field(
			"method"
		).stringValue(
			operation.getHttpMethod().toString()
		);

		String returnValue = _getReturnValue(type, operation);

		jsonObjectBuilder.field(
			"returns"
		).stringValue(
			returnValue
		);
	}

	@Override
	public void mapProperty(
		JSONObjectBuilder jsonObjectBuilder, String fieldName) {

		jsonObjectBuilder.field(
			FIELD_NAME_TYPE
		).stringValue(
			TYPE_SUPPORTED_PROPERTY
		);
		jsonObjectBuilder.field(
			FIELD_NAME_PROPERTY
		).stringValue(
			fieldName
		);
	}

	@Override
	public void mapResource(
		JSONObjectBuilder jsonObjectBuilder, String resourceType) {

		jsonObjectBuilder.field(
			FIELD_NAME_ID
		).stringValue(
			"http://schema.org/" + resourceType
		);
		jsonObjectBuilder.field(
			FIELD_NAME_TYPE
		).stringValue(
			TYPE_CLASS
		);
		jsonObjectBuilder.field(
			FIELD_NAME_TITLE
		).stringValue(
			resourceType
		);
	}

	@Override
	public void mapResourceCollection(
		JSONObjectBuilder jsonObjectBuilder, String resourceType) {

		jsonObjectBuilder.field(
			FIELD_NAME_ID
		).stringValue(
			"vocab:" + resourceType + "Collection"
		);
		jsonObjectBuilder.field(
			FIELD_NAME_TYPE
		).stringValue(
			TYPE_CLASS
		);

		jsonObjectBuilder.field(
			FIELD_NAME_TYPE
		).stringValue(
			TYPE_CLASS
		);

		jsonObjectBuilder.field(
			"subClassOf"
		).stringValue(
			"http://www.w3.org/ns/hydra/core#Collection"
		);

		jsonObjectBuilder.field(
			"description"
		).stringValue(
			"A collection of " + resourceType
		);

		jsonObjectBuilder.field(
			FIELD_NAME_TITLE
		).stringValue(
			resourceType + "Collection"
		);

		String[] collectionProperties = {
			FIELD_NAME_TOTAL_ITEMS, FIELD_NAME_MEMBER,
			FIELD_NAME_NUMBER_OF_ITEMS
		};

		Stream<String> collectionStream = Arrays.stream(collectionProperties);

		collectionStream.forEach(
			fieldName -> {
				JSONObjectBuilder propertyJsonObjectBuilder =
					new JSONObjectBuilder();

				onStartProperty(
					jsonObjectBuilder, propertyJsonObjectBuilder, fieldName);

				mapProperty(propertyJsonObjectBuilder, fieldName);

				onFinishProperty(
					jsonObjectBuilder, propertyJsonObjectBuilder, fieldName);

			});
	}

	@Override
	public void mapTitle(JSONObjectBuilder jsonObjectBuilder, String title) {
		jsonObjectBuilder.field(
			FIELD_NAME_TITLE
		).stringValue(
			title
		);
	}

	@Override
	public void onFinishOperation(
		JSONObjectBuilder documentationJsonObjectBuilder,
		JSONObjectBuilder operationJsonObjectBuilder, Operation operation) {

		documentationJsonObjectBuilder.field(
			"supportedOperation"
		).arrayValue(
		).add(
			operationJsonObjectBuilder
		);
	}

	@Override
	public void onFinishProperty(
		JSONObjectBuilder documentationJsonObjectBuilder,
		JSONObjectBuilder propertyJsonObjectBuilder, String formField) {

		documentationJsonObjectBuilder.field(
			"supportedProperty"
		).arrayValue(
		).add(
			propertyJsonObjectBuilder
		);
	}

	@Override
	public void onFinishResource(
		JSONObjectBuilder documentationJsonObjectBuilder,
		JSONObjectBuilder resourceJsonObjectBuilder, String type) {

		documentationJsonObjectBuilder.field(
			"supportedClass"
		).arrayValue(
		).add(
			resourceJsonObjectBuilder
		);
	}

	@Override
	public void onStart(
		JSONObjectBuilder jsonObjectBuilder, Documentation documentation,
		HttpHeaders httpHeaders) {

		JSONObjectBuilder.FieldStep contextBuilder = jsonObjectBuilder.field(
			FIELD_NAME_CONTEXT
		);

		contextBuilder.arrayValue(
		).add(
			builder -> builder.field(
				FIELD_NAME_VOCAB
			).stringValue(
				URL_SCHEMA_ORG
			)
		);

		contextBuilder.arrayValue(
		).addString(
			URL_HYDRA_PROFILE
		);

		Consumer<JSONObjectBuilder> expects = builder -> {
			JSONObjectBuilder.FieldStep expectBuilder = builder.nestedField(
				"expects");

			expectBuilder.field(
				FIELD_NAME_ID
			).stringValue(
				"hydra:expects"
			);

			expectBuilder.field(
				FIELD_NAME_TYPE
			).stringValue(
				FIELD_NAME_ID
			);
		};

		contextBuilder.arrayValue(
		).add(
			expects
		);

		Consumer<JSONObjectBuilder> returns = builder -> {
			JSONObjectBuilder.FieldStep returnsBuilder = builder.nestedField(
				"returns");

			returnsBuilder.field(
				FIELD_NAME_ID
			).stringValue(
				"hydra:returns"
			);

			returnsBuilder.field(
				FIELD_NAME_TYPE
			).stringValue(
				FIELD_NAME_ID
			);
		};

		contextBuilder.arrayValue(
		).add(
			returns
		);

		jsonObjectBuilder.field(
			FIELD_NAME_ID
		).stringValue(
			"http://api.example.com/doc/"
		);

		jsonObjectBuilder.field(
			FIELD_NAME_TYPE
		).stringValue(
			TYPE_API_DOCUMENTATION
		);

		JSONObjectBuilder.FieldStep propertyBuilder =
			contextBuilder.nestedField("property");

		propertyBuilder.field(
			FIELD_NAME_ID
		).stringValue(
			FIELD_NAME_PROPERTY
		);

		propertyBuilder.field(
			FIELD_NAME_TYPE
		).stringValue(
			FIELD_NAME_ID
		);

		contextBuilder.field(
			"supportedClass"
		).stringValue(
			"hydra:supportedClass"
		);

		contextBuilder.field(
			"supportedProperty"
		).stringValue(
			"hydra:supportedProperty"
		);

		contextBuilder.field(
			"supportedOperation"
		).stringValue(
			"hydra:supportedOperation"
		);

		contextBuilder.field(
			"method"
		).stringValue(
			"hydra:method"
		);

		contextBuilder.field(
			"statusCodes"
		).stringValue(
			"hydra:statusCodes"
		);

		contextBuilder.field(
			"code"
		).stringValue(
			"hydra:statusCodes"
		);
	}

	private String _getReturnValue(String resourceName, Operation operation) {
		String value = null;

		HTTPMethod httpMethod = operation.getHttpMethod();

		if (DELETE.equals(httpMethod)) {
			value = "http://www.w3.org/2002/07/owl#Nothing";
		}
		else if (operation.isCollection() && httpMethod.equals(GET)) {
			value = URL_HYDRA_PROFILE + TYPE_COLLECTION;
		}
		else {
			value = "http://schema.org/" + resourceName;
		}

		return value;
	}

}