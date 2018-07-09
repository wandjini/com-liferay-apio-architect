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
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_FIRST;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_ID;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_LAST;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_MEMBER;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_NEXT;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_NUMBER_OF_ITEMS;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_PREVIOUS;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_TOTAL_ITEMS;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_TYPE;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_VIEW;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.FIELD_NAME_VOCAB;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.MEDIA_TYPE;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.TYPE_COLLECTION;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.TYPE_PARTIAL_COLLECTION_VIEW;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.URL_HYDRA_PROFILE;
import static com.liferay.apio.architect.impl.internal.message.json.ld.JSONLDConstants.URL_SCHEMA_ORG;

import com.liferay.apio.architect.impl.internal.list.FunctionalList;
import com.liferay.apio.architect.impl.internal.message.json.ObjectBuilder;
import com.liferay.apio.architect.impl.internal.message.json.PageMessageMapper;
import com.liferay.apio.architect.impl.internal.message.json.SingleModelMessageMapper;
import com.liferay.apio.architect.pagination.Page;
import com.liferay.apio.architect.single.model.SingleModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;

/**
 * Represents collection pages in JSON-LD + Hydra format.
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
public class JSONLDPageMessageMapper<T> implements PageMessageMapper<T> {

	@Override
	public String getMediaType() {
		return MEDIA_TYPE;
	}

	@Override
	public Optional<SingleModelMessageMapper<T>>
		getSingleModelMessageMapperOptional() {

		return Optional.of(_singleModelMessageMapper);
	}

	@Override
	public void mapCollectionURL(
		ObjectBuilder objectBuilder, String url) {

		_singleModelMessageMapper.mapSelfURL(objectBuilder, url);
	}

	@Override
	public void mapCurrentPageURL(
		ObjectBuilder objectBuilder, String url) {

		objectBuilder.nestedField(
			FIELD_NAME_VIEW, FIELD_NAME_ID
		).stringValue(
			url
		);
	}

	@Override
	public void mapFirstPageURL(
		ObjectBuilder objectBuilder, String url) {

		objectBuilder.nestedField(
			FIELD_NAME_VIEW, FIELD_NAME_FIRST
		).stringValue(
			url
		);
	}

	@Override
	public void mapItemTotalCount(
		ObjectBuilder objectBuilder, int totalCount) {

		objectBuilder.field(
			FIELD_NAME_TOTAL_ITEMS
		).numberValue(
			totalCount
		);
	}

	@Override
	public void mapLastPageURL(
		ObjectBuilder objectBuilder, String url) {

		objectBuilder.nestedField(
			FIELD_NAME_VIEW, FIELD_NAME_LAST
		).stringValue(
			url
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
	public void mapNextPageURL(
		ObjectBuilder objectBuilder, String url) {

		objectBuilder.nestedField(
			FIELD_NAME_VIEW, FIELD_NAME_NEXT
		).stringValue(
			url
		);
	}

	@Override
	public void mapPageCount(ObjectBuilder objectBuilder, int count) {
		objectBuilder.field(
			FIELD_NAME_NUMBER_OF_ITEMS
		).numberValue(
			count
		);
	}

	@Override
	public void mapPreviousPageURL(
		ObjectBuilder objectBuilder, String url) {

		objectBuilder.nestedField(
			FIELD_NAME_VIEW, FIELD_NAME_PREVIOUS
		).stringValue(
			url
		);
	}

	@Override
	public void onFinish(ObjectBuilder objectBuilder, Page<T> page) {
		objectBuilder.field(
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

		objectBuilder.nestedField(
			FIELD_NAME_VIEW, FIELD_NAME_TYPE
		).arrayValue(
		).addString(
			TYPE_PARTIAL_COLLECTION_VIEW
		);

		objectBuilder.field(
			FIELD_NAME_TYPE
		).arrayValue(
		).addString(
			TYPE_COLLECTION
		);
	}

	@Override
	public void onFinishItem(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder, SingleModel<T> singleModel) {

		pageObjectBuilder.field(
			FIELD_NAME_MEMBER
		).arrayValue(
		).add(
			itemObjectBuilder
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

	private String[] _getTail(FunctionalList<String> embeddedPathElements) {
		Stream<String> stream = embeddedPathElements.tailStream();

		return stream.toArray(String[]::new);
	}

	private final SingleModelMessageMapper<T> _singleModelMessageMapper =
		new JSONLDSingleModelMessageMapper<>();

}