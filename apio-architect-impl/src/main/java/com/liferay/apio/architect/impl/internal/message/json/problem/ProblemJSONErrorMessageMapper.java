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

package com.liferay.apio.architect.impl.internal.message.json.problem;

import com.liferay.apio.architect.impl.internal.message.json.ErrorMessageMapper;
import com.liferay.apio.architect.impl.internal.message.json.ObjectBuilder;

import org.osgi.service.component.annotations.Component;

/**
 * Represents errors in problem+json format. For more information on this
 * format, see <a
 * href="https://tools.ietf.org/html/draft-nottingham-http-problem-06">https://tools.ietf.org/html/draft-nottingham-http-problem-06
 * </a>.
 *
 * @author Alejandro Hernández
 */
@Component
public class ProblemJSONErrorMessageMapper implements ErrorMessageMapper {

	@Override
	public String getMediaType() {
		return "application/problem+json";
	}

	@Override
	public void mapDescription(
		ObjectBuilder objectBuilder, String description) {

		objectBuilder.field(
			"detail"
		).stringValue(
			description
		);
	}

	@Override
	public void mapStatusCode(
		ObjectBuilder objectBuilder, Integer statusCode) {

		objectBuilder.field(
			"status"
		).numberValue(
			statusCode
		);
	}

	@Override
	public void mapTitle(ObjectBuilder objectBuilder, String title) {
		objectBuilder.field(
			"title"
		).stringValue(
			title
		);
	}

	@Override
	public void mapType(ObjectBuilder objectBuilder, String type) {
		objectBuilder.field(
			"type"
		).stringValue(
			type
		);
	}

}