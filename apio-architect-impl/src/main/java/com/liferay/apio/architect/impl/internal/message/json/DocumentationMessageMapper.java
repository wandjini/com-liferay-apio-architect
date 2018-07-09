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

import com.liferay.apio.architect.impl.internal.documentation.Documentation;
import com.liferay.apio.architect.operation.Operation;

/**
 * Maps the API {@link Documentation} to its JSON object representation.
 * Instances of this interface work like events. The {@code
 * javax.ws.rs.ext.MessageBodyWriter} of the {@code Documentation} calls the
 * {@code DocumentationMessageMapper} methods. In each method, developers should
 * only map the provided part of the resource to its representation in a JSON
 * object. To enable this, each method receives a {@link ObjectBuilder}.
 *
 * <p>
 * The method {@link #onFinish} is called when the writer finishes writing the
 * documentation. Otherwise, the documentation message mapper's methods aren't
 * called in a particular order.
 * </p>
 *
 * @author Alejandro Hernández
 */
public interface DocumentationMessageMapper
	extends MessageMapper<Documentation> {

	/**
	 * Maps the API description to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the documentation
	 * @param description the API description
	 */
	public default void mapDescription(
		ObjectBuilder objectBuilder, String description) {
	}

	public default void mapOperation(
		ObjectBuilder objectBuilder, String resourceName, String type,
		Operation operation) {
	}

	public default void mapProperty(
		ObjectBuilder objectBuilder, String fieldName) {
	}

	public default void mapResource(
		ObjectBuilder objectBuilder, String resourceName) {
	}

	public default void mapResourceCollection(
		ObjectBuilder objectBuilder, String resourceName) {
	}

	/**
	 * Maps the API title to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the documentation
	 * @param title the API title
	 */
	public default void mapTitle(
		ObjectBuilder objectBuilder, String title) {
	}

	public default void onFinishOperation(
		ObjectBuilder documentationObjectBuilder,
		ObjectBuilder operationObjectBuilder, Operation operation) {
	}

	public default void onFinishProperty(
		ObjectBuilder documentationObjectBuilder,
		ObjectBuilder propertyObjectBuilder, String formField) {
	}

	public default void onFinishResource(
		ObjectBuilder documentationObjectBuilder,
		ObjectBuilder resourceObjectBuilder, String type) {
	}

}