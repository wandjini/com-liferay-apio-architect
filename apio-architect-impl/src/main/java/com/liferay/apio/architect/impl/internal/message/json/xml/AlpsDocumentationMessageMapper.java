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

package com.liferay.apio.architect.impl.internal.message.json.xml;

import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.form.FormField;
import com.liferay.apio.architect.impl.internal.documentation.Documentation;
import com.liferay.apio.architect.impl.internal.message.json.DocumentationMessageMapper;
import com.liferay.apio.architect.impl.internal.message.json.JSONObjectBuilder;
import com.liferay.apio.architect.impl.internal.message.json.ObjectBuilder;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.routes.CollectionRoutes;
import org.osgi.service.component.annotations.Component;

import javax.ws.rs.core.HttpHeaders;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents documentation in Alps format.
 *
 * @author Javier Gamarra
 * @review
 */
@Component(immediate = true)
public class AlpsDocumentationMessageMapper
	implements DocumentationMessageMapper {

	public String getMediaType() {
		return "alps/json";
	}

	public void mapDescription(
		ObjectBuilder jsonObjectBuilder, String description) {

		jsonObjectBuilder.field(
			"doc"
		).stringValue(
			description
		);
	}

	public void mapEntity(
		ObjectBuilder jsonObjectBuilder, Documentation documentation) {

		Map<String, Representor> representors =
			documentation.getRepresentors();


		Map<String, CollectionRoutes> collectionRoutes =
			documentation.getCollectionRoutes();

		ObjectBuilder.ArrayValueStep supportedClass1 = jsonObjectBuilder
			.nestedField("descriptors").arrayValue();

		ObjectBuilder jsonObjectBuilder1 = new JSONObjectBuilder();

		representors.forEach((key, representor) -> {
			ObjectBuilder.FieldStep fieldStep2 = jsonObjectBuilder1
				.nestedField("supportedProperty");

			CollectionRoutes collectionRoutes1 = collectionRoutes.get(
				key);

			List<FormField> formFields1 = null;

			if (collectionRoutes1 != null) {
				Optional formOptional = collectionRoutes1.getFormOptional();

				formFields1 =

					formOptional.isPresent() ?
						((Form) formOptional.get()).getFormFields() :
						new ArrayList<>();
			}

			final List<FormField> formFields =
				formFields1 == null ? new ArrayList<>() : formFields1;

//			representor.getStringFunctions().forEach(
//				(BiConsumer<String, Function<Object, String>>) (o, o2) -> {
//					fieldStep2.field(
//						JSONLDConstants.FIELD_NAME_TITLE).stringValue(o);
//
//					FormField search = search(formFields, o);
//
////					fieldStep2.field("required").booleanValue(
////						search != null && search.required);
//
//					fieldStep2.field("readonly").booleanValue(false);
//					fieldStep2.field("writeonly").booleanValue(false);
//				});

			if (collectionRoutes.containsKey(key)) {
				JSONObjectBuilder.FieldStep fieldStep = jsonObjectBuilder1
					.nestedField("supportedClass", "supportedOperation");

				if (collectionRoutes1.
					getGetPageFunctionOptional().isPresent()) {

					fieldStep.field("method").stringValue("GET");
					fieldStep.field("label")
						.stringValue("Retrieves all " + key + " entities");
					fieldStep.field("description").stringValue(null);
					fieldStep.field("description").stringValue(null);
					fieldStep.field("returns")
						.stringValue("http://schema.org/" + key);
				}
			}

			supportedClass1.add(jsonObjectBuilder1);
		});
	}

	public void mapTitle(JSONObjectBuilder jsonObjectBuilder, String title) {
		jsonObjectBuilder.field(
			"title"
		).stringValue(
			title
		);
	}

	public void onStart(
		JSONObjectBuilder jsonObjectBuilder, Documentation documentation,
		HttpHeaders httpHeaders) {

		jsonObjectBuilder.field(
			"version"
		).stringValue(
			"1.0"
		);
	}

	public FormField search(List<FormField> formFields, String name) {
		for (FormField formField : formFields) {
			if (formField.getName().equals(name)) {
				return formField;
			}
		}

		return null;
	}

}