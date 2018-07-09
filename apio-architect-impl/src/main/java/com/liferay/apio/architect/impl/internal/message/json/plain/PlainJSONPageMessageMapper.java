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

package com.liferay.apio.architect.impl.internal.message.json.plain;

import com.liferay.apio.architect.impl.internal.message.json.ObjectBuilder;
import com.liferay.apio.architect.impl.internal.message.json.PageMessageMapper;
import com.liferay.apio.architect.impl.internal.message.json.SingleModelMessageMapper;
import com.liferay.apio.architect.single.model.SingleModel;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;

/**
 * Represents collection pages in plain JSON.
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
@Component
public class PlainJSONPageMessageMapper<T> implements PageMessageMapper<T> {

	@Override
	public String getMediaType() {
		return "application/json";
	}

	@Override
	public Optional<SingleModelMessageMapper<T>>
		getSingleModelMessageMapperOptional() {

		return Optional.of(_singleModelMessageMapper);
	}

	@Override
	public void mapCollectionURL(
		ObjectBuilder objectBuilder, String url) {

		objectBuilder.field(
			"collection"
		).stringValue(
			url
		);
	}

	@Override
	public void mapCurrentPageURL(
		ObjectBuilder objectBuilder, String url) {

		_singleModelMessageMapper.mapSelfURL(objectBuilder, url);
	}

	@Override
	public void mapFirstPageURL(
		ObjectBuilder objectBuilder, String url) {

		objectBuilder.nestedField(
			"pages", "first"
		).stringValue(
			url
		);
	}

	@Override
	public void mapItemTotalCount(
		ObjectBuilder objectBuilder, int totalCount) {

		objectBuilder.field(
			"totalNumberOfItems"
		).numberValue(
			totalCount
		);
	}

	@Override
	public void mapLastPageURL(
		ObjectBuilder objectBuilder, String url) {

		objectBuilder.nestedField(
			"pages", "last"
		).stringValue(
			url
		);
	}

	@Override
	public void mapNextPageURL(
		ObjectBuilder objectBuilder, String url) {

		objectBuilder.nestedField(
			"pages", "next"
		).stringValue(
			url
		);
	}

	@Override
	public void mapPageCount(ObjectBuilder objectBuilder, int count) {
		objectBuilder.field(
			"numberOfItems"
		).numberValue(
			count
		);
	}

	@Override
	public void mapPreviousPageURL(
		ObjectBuilder objectBuilder, String url) {

		objectBuilder.nestedField(
			"pages", "prev"
		).stringValue(
			url
		);
	}

	@Override
	public void onFinishItem(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder, SingleModel<T> singleModel) {

		pageObjectBuilder.field(
			"elements"
		).arrayValue(
		).add(
			itemObjectBuilder
		);
	}

	private final SingleModelMessageMapper<T> _singleModelMessageMapper =
		new PlainJSONSingleModelMessageMapper<>();

}