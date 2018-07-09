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

package com.liferay.apio.architect.impl.internal.writer;

import static com.liferay.apio.architect.impl.internal.url.URLCreator.createCollectionPageURL;
import static com.liferay.apio.architect.impl.internal.url.URLCreator.createCollectionURL;
import static com.liferay.apio.architect.impl.internal.url.URLCreator.createNestedCollectionURL;
import static com.liferay.apio.architect.impl.internal.writer.util.WriterUtil.getFieldsWriter;
import static com.liferay.apio.architect.impl.internal.writer.util.WriterUtil.getPathOptional;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.liferay.apio.architect.impl.internal.alias.BaseRepresentorFunction;
import com.liferay.apio.architect.impl.internal.alias.PathFunction;
import com.liferay.apio.architect.impl.internal.alias.RepresentorFunction;
import com.liferay.apio.architect.impl.internal.alias.ResourceNameFunction;
import com.liferay.apio.architect.impl.internal.alias.SingleModelFunction;
import com.liferay.apio.architect.impl.internal.list.FunctionalList;
import com.liferay.apio.architect.impl.internal.message.json.JSONObjectBuilder;
import com.liferay.apio.architect.impl.internal.message.json.ObjectBuilder;
import com.liferay.apio.architect.impl.internal.message.json.PageMessageMapper;
import com.liferay.apio.architect.impl.internal.pagination.PageType;
import com.liferay.apio.architect.impl.internal.request.RequestInfo;
import com.liferay.apio.architect.impl.internal.single.model.SingleModelImpl;
import com.liferay.apio.architect.impl.internal.unsafe.Unsafe;
import com.liferay.apio.architect.operation.Operation;
import com.liferay.apio.architect.pagination.Page;
import com.liferay.apio.architect.representor.BaseRepresentor;
import com.liferay.apio.architect.single.model.SingleModel;
import com.liferay.apio.architect.uri.Path;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Writes a page.
 *
 * @author Alejandro Hernández
 * @param  <T> the model's type
 */
public class PageWriter<T> {

	/**
	 * Creates a new {@code PageWriter} object, without creating the builder.
	 *
	 * @param  function the function that creates a {@code PageWriter} from a
	 *         builder
	 * @return the {@code PageWriter} instance
	 */
	public static <T> PageWriter<T> create(
		Function<Builder<T>, PageWriter<T>> function) {

		return function.apply(new Builder<>());
	}

	public PageWriter(Builder<T> builder) {
		_page = builder._page;
		_pageMessageMapper = builder._pageMessageMapper;
		_pathFunction = builder._pathFunction;
		_representorFunction = builder._representorFunction;
		_requestInfo = builder._requestInfo;
		_resourceNameFunction = builder._resourceNameFunction;
		_singleModelFunction = builder._singleModelFunction;

		_objectBuilder = new JSONObjectBuilder();
	}

	/**
	 * Writes the handled {@link Page} to a string. This method uses a {@link
	 * FieldsWriter} to write the different fields of its items' {@link
	 * com.liferay.apio.architect.representor.Representor}. If no {@code
	 * Representor} or {@code Path} exist for the model, this method returns
	 * {@code Optional#empty()}.
	 *
	 * @return the representation of the {@code Page}, if the {@code
	 *         Representor} and {@code Path} exist for the model; returns {@code
	 *         Optional#empty()} otherwise
	 */
	public String write() {
		_pageMessageMapper.mapItemTotalCount(
			_objectBuilder, _page.getTotalCount());

		Collection<T> items = _page.getItems();

		_pageMessageMapper.mapPageCount(_objectBuilder, items.size());

		_writePageURLs();

		String url = _getCollectionURL();

		_pageMessageMapper.mapCollectionURL(_objectBuilder, url);

		String resourceName = _page.getResourceName();

		items.forEach(
			model -> _writeItem(
				new SingleModelImpl<>(
					model, resourceName, Collections.emptyList())));

		List<Operation> operations = _page.getOperations();

		OperationWriter operationWriter = new OperationWriter(
			_pageMessageMapper, _requestInfo, _objectBuilder);

		operations.forEach(operationWriter::write);

		_pageMessageMapper.onFinish(_objectBuilder, _page);

		return _objectBuilder.build();
	}

	/**
	 * Creates {@code PageWriter} instances.
	 *
	 * @param <T> the model's type
	 */
	public static class Builder<T> {

		/**
		 * Adds information about the page being written to the builder.
		 *
		 * @param  page the page being written
		 * @return the updated builder
		 */
		public PageMessageMapperStep page(Page<T> page) {
			_page = page;

			return new PageMessageMapperStep();
		}

		public class BuildStep {

			/**
			 * Constructs and returns a {@code PageWriter} instance with the
			 * information provided to the builder.
			 *
			 * @return the {@code PageWriter} instance
			 */
			public PageWriter<T> build() {
				return new PageWriter<>(Builder.this);
			}

		}

		public class PageMessageMapperStep {

			/**
			 * Adds information to the builder about the {@link
			 * PageMessageMapper}.
			 *
			 * @param  pageMessageMapper the {@code PageMessageMapper} headers.
			 * @return the updated builder
			 */
			public PathFunctionStep pageMessageMapper(
				PageMessageMapper<T> pageMessageMapper) {

				_pageMessageMapper = pageMessageMapper;

				return new PathFunctionStep();
			}

		}

		public class PathFunctionStep {

			/**
			 * Adds information to the builder about the function that converts
			 * an identifier to a {@link Path}.
			 *
			 * @param  pathFunction the function to map an identifier to a
			 *         {@code Path}
			 * @return the updated builder
			 */
			public ResourceNameFunctionStep pathFunction(
				PathFunction pathFunction) {

				_pathFunction = pathFunction;

				return new ResourceNameFunctionStep();
			}

		}

		public class RepresentorFunctionStep {

			/**
			 * Adds information to the builder about the function that gets a
			 * class's {@link
			 * com.liferay.apio.architect.representor.Representor}.
			 *
			 * @param  representorFunction the function that gets a class's
			 *         {@code Representor}
			 * @return the updated builder
			 */
			public RequestInfoStep representorFunction(
				RepresentorFunction representorFunction) {

				_representorFunction = representorFunction;

				return new RequestInfoStep();
			}

		}

		public class RequestInfoStep {

			/**
			 * Adds information to the builder about the request.
			 *
			 * @param  requestInfo the information obtained from the request.
			 *         This can be created by using a {@link
			 *         RequestInfo.Builder}
			 * @return the updated builder
			 */
			public SingleModelFunctionStep requestInfo(
				RequestInfo requestInfo) {

				_requestInfo = requestInfo;

				return new SingleModelFunctionStep();
			}

		}

		public class ResourceNameFunctionStep {

			/**
			 * Adds information to the builder about the function that gets the
			 * name of a class's {@link
			 * com.liferay.apio.architect.representor.Representor}.
			 *
			 * @param  resourceNameFunction the function that gets the name of a
			 *         class's {@code Representor}
			 * @return the updated builder
			 */
			public RepresentorFunctionStep resourceNameFunction(
				ResourceNameFunction resourceNameFunction) {

				_resourceNameFunction = resourceNameFunction;

				return new RepresentorFunctionStep();
			}

		}

		public class SingleModelFunctionStep {

			/**
			 * Adds information to the builder about the function that gets the
			 * {@code SingleModel} from a class using its identifier.
			 *
			 * @param  singleModelFunction the function that gets the {@code
			 *         SingleModel} of a class
			 * @return the updated builder
			 */
			public BuildStep singleModelFunction(
				SingleModelFunction singleModelFunction) {

				_singleModelFunction = singleModelFunction;

				return new BuildStep();
			}

		}

		private Page<T> _page;
		private PageMessageMapper<T> _pageMessageMapper;
		private PathFunction _pathFunction;
		private RepresentorFunction _representorFunction;
		private RequestInfo _requestInfo;
		private ResourceNameFunction _resourceNameFunction;
		private SingleModelFunction _singleModelFunction;

	}

	private String _getCollectionURL() {
		Optional<Path> optional = _page.getPathOptional();

		return optional.map(
			path -> createNestedCollectionURL(
				_requestInfo.getApplicationURL(), path, _page.getResourceName())
		).orElseGet(
			() -> createCollectionURL(
				_requestInfo.getApplicationURL(), _page.getResourceName())
		);
	}

	private void _writeBasicFields(
		FieldsWriter<?> fieldsWriter, ObjectBuilder objectBuilder) {

		fieldsWriter.writeApplicationRelativeURLFields(
			(field, value) -> _pageMessageMapper.mapItemStringField(
				_objectBuilder, objectBuilder, field, value));

		fieldsWriter.writeBooleanFields(
			(field, value) -> _pageMessageMapper.mapItemBooleanField(
				_objectBuilder, objectBuilder, field, value));

		fieldsWriter.writeBooleanListFields(
			(field, value) -> _pageMessageMapper.mapItemBooleanListField(
				_objectBuilder, objectBuilder, field, value));

		fieldsWriter.writeLocalizedStringFields(
			(field, value) -> _pageMessageMapper.mapItemStringField(
				_objectBuilder, objectBuilder, field, value));

		fieldsWriter.writeNumberFields(
			(field, value) -> _pageMessageMapper.mapItemNumberField(
				_objectBuilder, objectBuilder, field, value));

		fieldsWriter.writeNumberListFields(
			(field, value) -> _pageMessageMapper.mapItemNumberListField(
				_objectBuilder, objectBuilder, field, value));

		fieldsWriter.writeRelativeURLFields(
			(field, value) -> _pageMessageMapper.mapItemStringField(
				_objectBuilder, objectBuilder, field, value));

		fieldsWriter.writeStringFields(
			(field, value) -> _pageMessageMapper.mapItemStringField(
				_objectBuilder, objectBuilder, field, value));

		fieldsWriter.writeStringListFields(
			(field, value) -> _pageMessageMapper.mapItemStringListField(
				_objectBuilder, objectBuilder, field, value));

		fieldsWriter.writeLinks(
			(fieldName, link) -> _pageMessageMapper.mapItemLink(
				_objectBuilder, objectBuilder, fieldName, link));

		fieldsWriter.writeTypes(
			types -> _pageMessageMapper.mapItemTypes(
				_objectBuilder, objectBuilder, types));

		fieldsWriter.writeBinaries(
			(field, value) -> _pageMessageMapper.mapItemLink(
				_objectBuilder, objectBuilder, field, value));
	}

	private <U> void _writeItem(
		ObjectBuilder collectionObjectBuilder,
		SingleModel<U> singleModel, FunctionalList<String> embeddedPathElements,
		BaseRepresentorFunction baseRepresentorFunction,
		SingleModel<?> rootSingleModel) {

		Optional<Path> pathOptional = getPathOptional(
			singleModel, _pathFunction, baseRepresentorFunction,
			_representorFunction, rootSingleModel);

		if (!pathOptional.isPresent()) {
			return;
		}

		Optional<FieldsWriter<U>> fieldsWriterOptional = getFieldsWriter(
			singleModel, embeddedPathElements, _requestInfo,
			baseRepresentorFunction, _singleModelFunction, pathOptional.get());

		if (!fieldsWriterOptional.isPresent()) {
			return;
		}

		FieldsWriter<U> fieldsWriter = fieldsWriterOptional.get();

		ObjectBuilder itemObjectBuilder = new JSONObjectBuilder();

		_writeBasicFields(fieldsWriter, itemObjectBuilder);

		fieldsWriter.writeRelatedModels(
			embeddedSingleModel -> getPathOptional(
				embeddedSingleModel, _pathFunction,
				_representorFunction::apply),
			(embeddedSingleModel, embeddedPathElements1) ->
				_writeItemEmbeddedModelFields(
					embeddedSingleModel, embeddedPathElements1,
					itemObjectBuilder, baseRepresentorFunction,
					singleModel),
			(resourceURL, embeddedPathElements1) ->
				_pageMessageMapper.mapItemLinkedResourceURL(
					_objectBuilder, itemObjectBuilder,
					embeddedPathElements1, resourceURL),
			(resourceURL, embeddedPathElements1) ->
				_pageMessageMapper.mapItemEmbeddedResourceURL(
					_objectBuilder, itemObjectBuilder,
					embeddedPathElements1, resourceURL));

		_writePageNestedResources(
			baseRepresentorFunction, singleModel, itemObjectBuilder);

		_writeNestedLists(
			baseRepresentorFunction, singleModel, itemObjectBuilder, null);

		_pageMessageMapper.onFinishNestedCollectionItem(
			collectionObjectBuilder, itemObjectBuilder, singleModel);
	}

	private void _writeItem(SingleModel<T> singleModel) {
		Optional<Path> pathOptional = getPathOptional(
			singleModel, _pathFunction, _representorFunction::apply);

		if (!pathOptional.isPresent()) {
			return;
		}

		Optional<FieldsWriter<T>> optional = getFieldsWriter(
			singleModel, null, _requestInfo, _representorFunction::apply,
			_singleModelFunction, pathOptional.get());

		if (!optional.isPresent()) {
			return;
		}

		FieldsWriter<T> fieldsWriter = optional.get();

		ObjectBuilder itemObjectBuilder = new JSONObjectBuilder();

		_writeBasicFields(fieldsWriter, itemObjectBuilder);

		fieldsWriter.writeSingleURL(
			url -> _pageMessageMapper.mapItemSelfURL(
				_objectBuilder, itemObjectBuilder, url));

		fieldsWriter.writeRelatedModels(
			embeddedSingleModel -> getPathOptional(
				embeddedSingleModel, _pathFunction,
				_representorFunction::apply),
			(embeddedSingleModel, embeddedPathElements1) ->
				_writeItemEmbeddedModelFields(
					embeddedSingleModel, embeddedPathElements1,
					itemObjectBuilder),
			(resourceURL, embeddedPathElements) ->
				_pageMessageMapper.mapItemLinkedResourceURL(
					_objectBuilder, itemObjectBuilder,
					embeddedPathElements, resourceURL),
			(resourceURL, embeddedPathElements) ->
				_pageMessageMapper.mapItemEmbeddedResourceURL(
					_objectBuilder, itemObjectBuilder,
					embeddedPathElements, resourceURL));

		fieldsWriter.writeRelatedCollections(
			_resourceNameFunction,
			(url, embeddedPathElements) ->
				_pageMessageMapper.mapItemLinkedResourceURL(
					_objectBuilder, itemObjectBuilder,
					embeddedPathElements, url));

		_writeNestedResources(
			_representorFunction::apply, singleModel, itemObjectBuilder,
			singleModel, null);

		_writeNestedLists(
			_representorFunction::apply, singleModel, itemObjectBuilder,
			null);

		_pageMessageMapper.onFinishItem(
			_objectBuilder, itemObjectBuilder, singleModel);
	}

	private <S> void _writeItemEmbeddedModelFields(
		SingleModel<S> singleModel, FunctionalList<String> embeddedPathElements,
		ObjectBuilder itemObjectBuilder) {

		_writeItemEmbeddedModelFields(
			singleModel, embeddedPathElements, itemObjectBuilder,
			_representorFunction::apply, singleModel);
	}

	private <S, U> void _writeItemEmbeddedModelFields(
		SingleModel<S> singleModel, FunctionalList<String> embeddedPathElements,
		ObjectBuilder itemObjectBuilder,
		BaseRepresentorFunction baseRepresentorFunction,
		SingleModel<U> rootSingleModel) {

		Optional<Path> pathOptional = getPathOptional(
			singleModel, _pathFunction, baseRepresentorFunction,
			_representorFunction, rootSingleModel);

		if (!pathOptional.isPresent()) {
			return;
		}

		Optional<FieldsWriter<S>> fieldsWriterOptional = getFieldsWriter(
			singleModel, embeddedPathElements, _requestInfo,
			baseRepresentorFunction, _singleModelFunction, pathOptional.get());

		if (!fieldsWriterOptional.isPresent()) {
			return;
		}

		FieldsWriter<S> fieldsWriter = fieldsWriterOptional.get();

		fieldsWriter.writeApplicationRelativeURLFields(
			(field, value) ->
				_pageMessageMapper.mapItemEmbeddedResourceStringField(
					_objectBuilder, itemObjectBuilder,
					embeddedPathElements, field, value));

		fieldsWriter.writeBooleanFields(
			(field, value) ->
				_pageMessageMapper.mapItemEmbeddedResourceBooleanField(
					_objectBuilder, itemObjectBuilder,
					embeddedPathElements, field, value));

		fieldsWriter.writeBooleanListFields(
			(field, value) ->
				_pageMessageMapper.mapItemEmbeddedResourceBooleanListField(
					_objectBuilder, itemObjectBuilder,
					embeddedPathElements, field, value));

		fieldsWriter.writeLocalizedStringFields(
			(field, value) ->
				_pageMessageMapper.mapItemEmbeddedResourceStringField(
					_objectBuilder, itemObjectBuilder,
					embeddedPathElements, field, value));

		fieldsWriter.writeNumberFields(
			(field, value) ->
				_pageMessageMapper.mapItemEmbeddedResourceNumberField(
					_objectBuilder, itemObjectBuilder,
					embeddedPathElements, field, value));

		fieldsWriter.writeNumberListFields(
			(field, value) ->
				_pageMessageMapper.mapItemEmbeddedResourceNumberListField(
					_objectBuilder, itemObjectBuilder,
					embeddedPathElements, field, value));

		fieldsWriter.writeRelativeURLFields(
			(field, value) ->
				_pageMessageMapper.mapItemEmbeddedResourceStringField(
					_objectBuilder, itemObjectBuilder,
					embeddedPathElements, field, value));

		fieldsWriter.writeApplicationRelativeURLFields(
			(field, value) ->
				_pageMessageMapper.mapItemEmbeddedResourceStringField(
					_objectBuilder, itemObjectBuilder,
					embeddedPathElements, field, value));

		fieldsWriter.writeStringFields(
			(field, value) ->
				_pageMessageMapper.mapItemEmbeddedResourceStringField(
					_objectBuilder, itemObjectBuilder,
					embeddedPathElements, field, value));

		fieldsWriter.writeStringListFields(
			(field, value) ->
				_pageMessageMapper.mapItemEmbeddedResourceStringListField(
					_objectBuilder, itemObjectBuilder,
					embeddedPathElements, field, value));

		fieldsWriter.writeLinks(
			(fieldName, link) -> _pageMessageMapper.mapItemEmbeddedResourceLink(
				_objectBuilder, itemObjectBuilder, embeddedPathElements,
				fieldName, link));

		fieldsWriter.writeTypes(
			types -> _pageMessageMapper.mapItemEmbeddedResourceTypes(
				_objectBuilder, itemObjectBuilder, embeddedPathElements,
				types));

		fieldsWriter.writeBinaries(
			(field, value) -> _pageMessageMapper.mapItemEmbeddedResourceLink(
				_objectBuilder, itemObjectBuilder, embeddedPathElements,
				field, value));

		fieldsWriter.writeRelatedModels(
			embeddedSingleModel -> getPathOptional(
				embeddedSingleModel, _pathFunction,
				_representorFunction::apply),
			(embeddedSingleModel, embeddedModelEmbeddedPathElements) ->
				_writeItemEmbeddedModelFields(
					embeddedSingleModel, embeddedModelEmbeddedPathElements,
					itemObjectBuilder),
			(resourceURL, resourceEmbeddedPathElements) ->
				_pageMessageMapper.mapItemLinkedResourceURL(
					_objectBuilder, itemObjectBuilder,
					resourceEmbeddedPathElements, resourceURL),
			(resourceURL, resourceEmbeddedPathElements) ->
				_pageMessageMapper.mapItemEmbeddedResourceURL(
					_objectBuilder, itemObjectBuilder,
					resourceEmbeddedPathElements, resourceURL));

		fieldsWriter.writeRelatedCollections(
			_resourceNameFunction,
			(url, resourceEmbeddedPathElements) ->
				_pageMessageMapper.mapItemLinkedResourceURL(
					_objectBuilder, itemObjectBuilder,
					resourceEmbeddedPathElements, url));

		_writeNestedLists(
			baseRepresentorFunction, singleModel, itemObjectBuilder,
			embeddedPathElements);

		_writeNestedResources(
			baseRepresentorFunction, singleModel, itemObjectBuilder,
			rootSingleModel, embeddedPathElements);
	}

	private <U> void _writeNestedList(
		String fieldName, List<U> list, ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements,
		BaseRepresentorFunction baseRepresentorFunction,
		SingleModel singleModel) {

		ObjectBuilder pageObjectBuilder = new JSONObjectBuilder();

		_pageMessageMapper.mapNestedPageItemTotalCount(
			pageObjectBuilder, list.size());

		list.forEach(
			model -> _writeItem(
				pageObjectBuilder,
				new SingleModelImpl<>(model, "", Collections.emptyList()),
				embeddedPathElements, baseRepresentorFunction, singleModel));

		_pageMessageMapper.onFinishNestedCollection(
			objectBuilder, pageObjectBuilder, fieldName, list,
			embeddedPathElements);
	}

	private <S> void _writeNestedLists(
		BaseRepresentorFunction baseRepresentorFunction,
		SingleModel<S> singleModel, ObjectBuilder objectBuilder,
		FunctionalList<String> embeddedPathElements) {

		baseRepresentorFunction.apply(
			singleModel.getResourceName()
		).<BaseRepresentor<S>>map(
			Unsafe::unsafeCast
		).map(
			BaseRepresentor::getNestedListFieldFunctions
		).map(
			List::stream
		).orElseGet(
			Stream::empty
		).forEach(
			nestedListFieldFunction -> {
				List<?> list = nestedListFieldFunction.apply(
					singleModel.getModel());

				if (list == null) {
					return;
				}

				FunctionalList<String> embeddedNestedPathElements =
					new FunctionalList<>(
						embeddedPathElements, nestedListFieldFunction.getKey());

				_writeNestedList(
					nestedListFieldFunction.getKey(), list, objectBuilder,
					embeddedNestedPathElements,
					__ -> Optional.of(
						nestedListFieldFunction.getNestedRepresentor()),
					singleModel);
			}
		);
	}

	private <S, U> void _writeNestedResources(
		BaseRepresentorFunction baseRepresentorFunction,
		SingleModel<U> singleModel, ObjectBuilder itemObjectBuilder,
		SingleModel<S> rootSingleModel,
		FunctionalList<String> embeddedPathElements) {

		baseRepresentorFunction.apply(
			singleModel.getResourceName()
		).<BaseRepresentor<U>>map(
			Unsafe::unsafeCast
		).map(
			BaseRepresentor::getNestedFieldFunctions
		).map(
			List::stream
		).orElseGet(
			Stream::empty
		).forEach(
			nestedFieldFunction -> {
				Object mappedModel = nestedFieldFunction.apply(
					singleModel.getModel());

				FunctionalList<String> embeddedNestedPathElements =
					new FunctionalList<>(
						embeddedPathElements, nestedFieldFunction.getKey());

				_writeItemEmbeddedModelFields(
					new SingleModelImpl<>(
						mappedModel, "", Collections.emptyList()),
					embeddedNestedPathElements, itemObjectBuilder,
					__ -> Optional.of(
						nestedFieldFunction.getNestedRepresentor()),
					rootSingleModel);
			}
		);
	}

	private <U> void _writePageNestedResources(
		BaseRepresentorFunction baseRepresentorFunction,
		SingleModel<U> singleModel, ObjectBuilder itemObjectBuilder) {

		baseRepresentorFunction.apply(
			singleModel.getResourceName()
		).<BaseRepresentor<U>>map(
			Unsafe::unsafeCast
		).map(
			BaseRepresentor::getNestedFieldFunctions
		).map(
			List::stream
		).orElseGet(
			Stream::empty
		).forEach(
			nestedFieldFunction -> {
				Object mappedModel = nestedFieldFunction.apply(
					singleModel.getModel());

				if (mappedModel == null) {
					return;
				}

				FunctionalList<String> embeddedNestedPathElements =
					new FunctionalList<>(null, nestedFieldFunction.getKey());

				_writeItemEmbeddedModelFields(
					new SingleModelImpl<>(
						mappedModel, "", Collections.emptyList()),
					embeddedNestedPathElements, itemObjectBuilder,
					__ -> Optional.of(
						nestedFieldFunction.getNestedRepresentor()),
					singleModel);
			}
		);
	}

	private void _writePageURLs() {
		String url = _getCollectionURL();

		_pageMessageMapper.mapCurrentPageURL(
			_objectBuilder,
			createCollectionPageURL(url, _page, PageType.CURRENT));

		_pageMessageMapper.mapFirstPageURL(
			_objectBuilder,
			createCollectionPageURL(url, _page, PageType.FIRST));

		_pageMessageMapper.mapLastPageURL(
			_objectBuilder,
			createCollectionPageURL(url, _page, PageType.LAST));

		if (_page.hasNext()) {
			_pageMessageMapper.mapNextPageURL(
				_objectBuilder,
				createCollectionPageURL(url, _page, PageType.NEXT));
		}

		if (_page.hasPrevious()) {
			_pageMessageMapper.mapPreviousPageURL(
				_objectBuilder,
				createCollectionPageURL(url, _page, PageType.PREVIOUS));
		}
	}

	private final ObjectBuilder _objectBuilder;
	private final Page<T> _page;
	private final PageMessageMapper<T> _pageMessageMapper;
	private final PathFunction _pathFunction;
	private final RepresentorFunction _representorFunction;
	private final RequestInfo _requestInfo;
	private final ResourceNameFunction _resourceNameFunction;
	private final SingleModelFunction _singleModelFunction;

}