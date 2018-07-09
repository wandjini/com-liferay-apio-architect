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

import com.liferay.apio.architect.impl.internal.list.FunctionalList;
import com.liferay.apio.architect.operation.HTTPMethod;
import com.liferay.apio.architect.operation.Operation;
import com.liferay.apio.architect.single.model.SingleModel;

import java.util.List;

/**
 * Maps {@link SingleModel} data to its representation in a JSON object.
 * Instances of this interface work like events. The {@code
 * javax.ws.rs.ext.MessageBodyWriter} of the {@code SingleModel} calls the
 * {@code SingleModelMessageMapper} methods. In each method, developers should
 * only map the provided part of the resource to its representation in a JSON
 * object. To enable this, each method receives a {@link ObjectBuilder}.
 *
 * <p>
 * The method {@link #onFinish} is called when the writer finishes writing the
 * single model. Otherwise, the single model message mapper's methods aren't
 * called in a particular order.
 * </p>
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 * @param  <T> the model's type
 */
public interface SingleModelMessageMapper<T>
	extends MessageMapper<SingleModel<T>>, OperationMapper {

	/**
	 * Maps a resource's boolean field to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapBooleanField(
		ObjectBuilder objectBuilder, String fieldName, Boolean value) {
	}

	/**
	 * Maps a resource's boolean list field to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapBooleanListField(
		ObjectBuilder objectBuilder, String fieldName,
		List<Boolean> value) {
	}

	/**
	 * Maps an embedded resource operation form's URL to its JSON object
	 * representation.
	 *
	 * @param singleModelObjectBuilder the JSON object builder for the model
	 * @param operationObjectBuilder the JSON object builder for the
	 *        operation
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param url the operation form's URL
	 */
	public default void mapEmbeddedOperationFormURL(
		ObjectBuilder singleModelObjectBuilder,
		ObjectBuilder operationObjectBuilder,
		FunctionalList<String> embeddedPathElements, String url) {
	}

	/**
	 * Maps an embedded resource operation's method to its JSON object
	 * representation.
	 *
	 * @param singleModelObjectBuilder the JSON object builder for the model
	 * @param operationObjectBuilder the JSON object builder for the
	 *        operation
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param httpMethod the operation's method
	 */
	public default void mapEmbeddedOperationMethod(
		ObjectBuilder singleModelObjectBuilder,
		ObjectBuilder operationObjectBuilder,
		FunctionalList<String> embeddedPathElements, HTTPMethod httpMethod) {
	}

	/**
	 * Maps an embedded resource's boolean field to its JSON object
	 * representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapEmbeddedResourceBooleanField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		Boolean value) {
	}

	/**
	 * Maps an embedded resource's boolean list field to its JSON object
	 * representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapEmbeddedResourceBooleanListField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		List<Boolean> value) {
	}

	/**
	 * Maps an embedded resource's link to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param fieldName the field's name
	 * @param url the field's value
	 */
	public default void mapEmbeddedResourceLink(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		String url) {
	}

	/**
	 * Maps an embedded resource's number field to its JSON object
	 * representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapEmbeddedResourceNumberField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		Number value) {
	}

	/**
	 * Maps an embedded resource's number list field to its JSON object
	 * representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapEmbeddedResourceNumberListField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		List<Number> value) {
	}

	/**
	 * Maps an embedded resource's string field to its JSON object
	 * representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapEmbeddedResourceStringField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		String value) {
	}

	/**
	 * Maps an embedded resource's string list field to its JSON object
	 * representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapEmbeddedResourceStringListField(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		List<String> value) {
	}

	/**
	 * Maps an embedded resource's types to their JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param types the resource's types
	 */
	public default void mapEmbeddedResourceTypes(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, List<String> types) {
	}

	/**
	 * Maps an embedded resource's URL to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param url the resource's URL
	 */
	public default void mapEmbeddedResourceURL(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String url) {
	}

	/**
	 * Maps a resource's link to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param fieldName the field's name
	 * @param url the link's URL
	 */
	public default void mapLink(
		ObjectBuilder objectBuilder, String fieldName, String url) {
	}

	/**
	 * Maps a linked resource's URL to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param url the resource's URL
	 */
	public default void mapLinkedResourceURL(
		ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements, String url) {
	}

	/**
	 * Maps the total number of elements in a nested collection to its JSON
	 * object representation.
	 *
	 * @param objectBuilder the JSON object builder for the nested
	 *        collection
	 * @param totalCount the total number of elements in the collection
	 */
	public default void mapNestedPageItemTotalCount(
		ObjectBuilder objectBuilder, int totalCount) {
	}

	/**
	 * Maps a resource's number field to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapNumberField(
		ObjectBuilder objectBuilder, String fieldName, Number value) {
	}

	/**
	 * Maps a resource's number list field to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapNumberListField(
		ObjectBuilder objectBuilder, String fieldName,
		List<Number> value) {
	}

	/**
	 * Maps a resource's URL to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param url the resource's URL
	 */
	public default void mapSelfURL(
		ObjectBuilder objectBuilder, String url) {
	}

	/**
	 * Maps a resource's string field to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapStringField(
		ObjectBuilder objectBuilder, String fieldName, String value) {
	}

	/**
	 * Maps a resource's string list field to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapStringListField(
		ObjectBuilder objectBuilder, String fieldName,
		List<String> value) {
	}

	/**
	 * Maps a resource's types to their JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the model
	 * @param types the resource's types
	 */
	public default void mapTypes(
		ObjectBuilder objectBuilder, List<String> types) {
	}

	/**
	 * Finishes an embedded model's operation. This is the final
	 * embedded-operation-mapper method the writer calls.
	 *
	 * @param singleModelObjectBuilder the JSON object builder for the model
	 * @param operationObjectBuilder the JSON object builder for the
	 *        operation
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param operation the operation
	 */
	public default void onFinishEmbeddedOperation(
		ObjectBuilder singleModelObjectBuilder,
		ObjectBuilder operationObjectBuilder,
		FunctionalList<String> embeddedPathElements, Operation operation) {
	}

	/**
	 * Finishes a nested collection. This is the final nested-collection-mapper
	 * method the writer calls.
	 *
	 * @param singleModelObjectBuilder the JSON object builder for the root
	 *        model
	 * @param collectionObjectBuilder the JSON object builder for the
	 *        collection
	 * @param fieldName the collection's field name
	 * @param list the collection
	 * @param embeddedPathElements the current resource's embedded path elements
	 */
	public default void onFinishNestedCollection(
		ObjectBuilder singleModelObjectBuilder,
		ObjectBuilder collectionObjectBuilder, String fieldName,
		List<?> list, FunctionalList<String> embeddedPathElements) {
	}

	/**
	 * Finishes a nested collection item. This is the final
	 * nested-collection-item-mapper method the writer calls.
	 *
	 * @param collectionObjectBuilder the JSON object builder for the
	 *        collection
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param singleModel the single model
	 */
	public default void onFinishNestedCollectionItem(
		ObjectBuilder collectionObjectBuilder,
		ObjectBuilder itemObjectBuilder, SingleModel<?> singleModel) {
	}

}