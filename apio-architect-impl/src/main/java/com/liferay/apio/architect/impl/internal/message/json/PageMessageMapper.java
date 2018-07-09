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
import com.liferay.apio.architect.pagination.Page;
import com.liferay.apio.architect.single.model.SingleModel;

import java.util.List;
import java.util.Optional;

/**
 * Maps {@link Page} data to its representation in a JSON object. Instances of
 * this interface work like events. The {@code
 * javax.ws.rs.ext.MessageBodyWriter} of the {@code Page} calls the {@code
 * PageMessageMapper} methods. In each method, developers should only map the
 * provided part of the resource to its representation in a JSON object. To
 * enable this, each method receives a {@link ObjectBuilder}.
 *
 * <p>
 * The method {@link #onFinish} is called when the writer finishes writing the
 * page. Otherwise, the page message mapper's methods aren't called in a
 * particular order.
 * </p>
 *
 * <p>
 * By default, each item method calls {@link
 * #getSingleModelMessageMapperOptional()} to get a {@link
 * SingleModelMessageMapper}.
 * </p>
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 * @param  <T> the model's type
 */
public interface PageMessageMapper<T>
	extends MessageMapper<Page<T>>, OperationMapper {

	/**
	 * Returns the {@link SingleModelMessageMapper} used by the item methods.
	 *
	 * @return the {@code SingleModelMessageMapper}
	 */
	public default Optional<SingleModelMessageMapper<T>>
		getSingleModelMessageMapperOptional() {

		return Optional.empty();
	}

	/**
	 * Maps a collection URL to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the page
	 * @param url the collection's URL
	 */
	public default void mapCollectionURL(
		ObjectBuilder objectBuilder, String url) {
	}

	/**
	 * Maps the current page's URL to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the page
	 * @param url the current page's URL
	 */
	public default void mapCurrentPageURL(
		ObjectBuilder objectBuilder, String url) {
	}

	/**
	 * Maps the first page's URL to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the page
	 * @param url the first page's URL
	 */
	public default void mapFirstPageURL(
		ObjectBuilder objectBuilder, String url) {
	}

	/**
	 * Maps a resource operation form's URL to its JSON object representation.
	 *  @param objectBuilder the JSON object builder for the
	 *        operation
	 * @param url the operation form's URL
	 */
	@Override
	public default void mapFormURL(
		ObjectBuilder objectBuilder, String url) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper -> singleModelMessageMapper.mapFormURL(
				objectBuilder, url));
	}

	/**
	 * Maps a resource operation's method to its JSON object representation.
	 *  @param objectBuilder the JSON object builder for the
	 *        operation
	 * @param httpMethod the operation's method
	 */
	@Override
	public default void mapHTTPMethod(
		ObjectBuilder objectBuilder, HTTPMethod httpMethod) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper -> singleModelMessageMapper.mapHTTPMethod(
				objectBuilder, httpMethod));
	}

	/**
	 * Maps a resource's boolean field to its JSON object representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapItemBooleanField(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder, String fieldName,
		Boolean value) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper ->
				singleModelMessageMapper.mapBooleanField(
					itemObjectBuilder, fieldName, value));
	}

	/**
	 * Maps a resource's boolean list field to its JSON object representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapItemBooleanListField(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder, String fieldName,
		List<Boolean> value) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper ->
				singleModelMessageMapper.mapBooleanListField(
					itemObjectBuilder, fieldName, value));
	}

	/**
	 * Maps an embedded resource's boolean field to its JSON object
	 * representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapItemEmbeddedResourceBooleanField(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		Boolean value) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper ->
				singleModelMessageMapper.mapEmbeddedResourceBooleanField(
					itemObjectBuilder, embeddedPathElements, fieldName,
					value));
	}

	/**
	 * Maps an embedded resource's boolean list field to its JSON object
	 * representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapItemEmbeddedResourceBooleanListField(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		List<Boolean> value) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper ->
				singleModelMessageMapper.mapEmbeddedResourceBooleanListField(
					itemObjectBuilder, embeddedPathElements, fieldName,
					value));
	}

	/**
	 * Maps an embedded resource link to its JSON object representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param fieldName the field's name
	 * @param url the link's URL
	 */
	public default void mapItemEmbeddedResourceLink(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		String url) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper ->
				singleModelMessageMapper.mapEmbeddedResourceLink(
					itemObjectBuilder, embeddedPathElements, fieldName,
					url));
	}

	/**
	 * Maps an embedded resource number field to its JSON object representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param fieldName the field's name
	 * @param number the field's value
	 */
	public default void mapItemEmbeddedResourceNumberField(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		Number number) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper ->
				singleModelMessageMapper.mapEmbeddedResourceNumberField(
					itemObjectBuilder, embeddedPathElements, fieldName,
					number));
	}

	/**
	 * Maps an embedded resource's number list field to its JSON object
	 * representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapItemEmbeddedResourceNumberListField(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		List<Number> value) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper ->
				singleModelMessageMapper.mapEmbeddedResourceNumberListField(
					itemObjectBuilder, embeddedPathElements, fieldName,
					value));
	}

	/**
	 * Maps an embedded resource string field to its JSON object representation.
	 *
	 * @param pageObjectBuilder the json object builder for the page
	 * @param itemObjectBuilder the json object builder for the item
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param fieldName the field's name
	 * @param string the field's value
	 */
	public default void mapItemEmbeddedResourceStringField(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		String string) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper ->
				singleModelMessageMapper.mapEmbeddedResourceStringField(
					itemObjectBuilder, embeddedPathElements, fieldName,
					string));
	}

	/**
	 * Maps an embedded resource's string list field to its JSON object
	 * representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapItemEmbeddedResourceStringListField(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder,
		FunctionalList<String> embeddedPathElements, String fieldName,
		List<String> value) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper ->
				singleModelMessageMapper.mapEmbeddedResourceStringListField(
					itemObjectBuilder, embeddedPathElements, fieldName,
					value));
	}

	/**
	 * Maps embedded resource types to their JSON object representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param types the resource types
	 */
	public default void mapItemEmbeddedResourceTypes(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder,
		FunctionalList<String> embeddedPathElements, List<String> types) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper ->
				singleModelMessageMapper.mapEmbeddedResourceTypes(
					itemObjectBuilder, embeddedPathElements, types));
	}

	/**
	 * Maps an embedded resource URL to its JSON object representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param url the resource's URL
	 */
	public default void mapItemEmbeddedResourceURL(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder,
		FunctionalList<String> embeddedPathElements, String url) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper ->
				singleModelMessageMapper.mapEmbeddedResourceURL(
					itemObjectBuilder, embeddedPathElements, url));
	}

	/**
	 * Maps a resource link to its JSON object representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param fieldName the field's name
	 * @param url the link's URL
	 */
	public default void mapItemLink(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder, String fieldName, String url) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper -> singleModelMessageMapper.mapLink(
				itemObjectBuilder, fieldName, url));
	}

	/**
	 * Maps a linked resource URL to its JSON object representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param embeddedPathElements the current resource's embedded path elements
	 * @param url the resource's URL
	 */
	public default void mapItemLinkedResourceURL(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder,
		FunctionalList<String> embeddedPathElements, String url) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper ->
				singleModelMessageMapper.mapLinkedResourceURL(
					itemObjectBuilder, embeddedPathElements, url));
	}

	/**
	 * Maps a resource number field to its JSON object representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param fieldName the field's name
	 * @param number the field's value
	 */
	public default void mapItemNumberField(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder, String fieldName,
		Number number) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper -> singleModelMessageMapper.mapNumberField(
				itemObjectBuilder, fieldName, number));
	}

	/**
	 * Maps a resource's number list field to its JSON object representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapItemNumberListField(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder, String fieldName,
		List<Number> value) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper ->
				singleModelMessageMapper.mapNumberListField(
					itemObjectBuilder, fieldName, value));
	}

	/**
	 * Maps a resource URL to its JSON object representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param url the resource's URL
	 */
	public default void mapItemSelfURL(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder, String url) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper -> singleModelMessageMapper.mapSelfURL(
				itemObjectBuilder, url));
	}

	/**
	 * Maps a resource string field to its JSON object representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param fieldName the field's name
	 * @param string the field's value
	 */
	public default void mapItemStringField(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder, String fieldName,
		String string) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper -> singleModelMessageMapper.mapStringField(
				itemObjectBuilder, fieldName, string));
	}

	/**
	 * Maps a resource's string list field to its JSON object representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param fieldName the field's name
	 * @param value the field's value
	 */
	public default void mapItemStringListField(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder, String fieldName,
		List<String> value) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper ->
				singleModelMessageMapper.mapStringListField(
					itemObjectBuilder, fieldName, value));
	}

	/**
	 * Maps the total number of elements in the collection to its JSON object
	 * representation.
	 *
	 * @param objectBuilder the JSON object builder for the page
	 * @param totalCount the total number of elements in the collection
	 */
	public default void mapItemTotalCount(
		ObjectBuilder objectBuilder, int totalCount) {
	}

	/**
	 * Maps resource types to their JSON object representation.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param types the resource types
	 */
	public default void mapItemTypes(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder, List<String> types) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper -> singleModelMessageMapper.mapTypes(
				itemObjectBuilder, types));
	}

	/**
	 * Maps the last page's URL to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the page
	 * @param url the last page's URL
	 */
	public default void mapLastPageURL(
		ObjectBuilder objectBuilder, String url) {
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
	 * Maps the next page's URL to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the page
	 * @param url the next page's URL
	 */
	public default void mapNextPageURL(
		ObjectBuilder objectBuilder, String url) {
	}

	/**
	 * Maps the page count to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the page
	 * @param count the number of elements in the page
	 */
	public default void mapPageCount(
		ObjectBuilder objectBuilder, int count) {
	}

	/**
	 * Maps the previous page's URL to its JSON object representation.
	 *
	 * @param objectBuilder the JSON object builder for the page
	 * @param url the previous page's URL
	 */
	public default void mapPreviousPageURL(
		ObjectBuilder objectBuilder, String url) {
	}

	/**
	 * Finishes the operation. This is the final operation-mapper method the
	 * writer calls.
	 *
	 * @param resourceObjectBuilder the JSON object builder for the page
	 * @param operationObjectBuilder the JSON object builder for the
	 *        operation
	 * @param operation the operation
	 */
	@Override
	public default void onFinish(
		ObjectBuilder resourceObjectBuilder,
		ObjectBuilder operationObjectBuilder, Operation operation) {

		Optional<SingleModelMessageMapper<T>> optional =
			getSingleModelMessageMapperOptional();

		optional.ifPresent(
			singleModelMessageMapper -> singleModelMessageMapper.onFinish(
				resourceObjectBuilder, operationObjectBuilder,
				operation));
	}

	/**
	 * Finishes the item. This is the final page message mapper method the
	 * writer calls for the item.
	 *
	 * @param pageObjectBuilder the JSON object builder for the page
	 * @param itemObjectBuilder the JSON object builder for the item
	 * @param singleModel the single model
	 */
	public default void onFinishItem(
		ObjectBuilder pageObjectBuilder,
		ObjectBuilder itemObjectBuilder, SingleModel<T> singleModel) {
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