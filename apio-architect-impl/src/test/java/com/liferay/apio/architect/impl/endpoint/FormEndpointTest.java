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

package com.liferay.apio.architect.impl.endpoint;

import static com.liferay.apio.architect.impl.endpoint.EndpointsTestUtil.collectionRoutes;
import static com.liferay.apio.architect.impl.endpoint.EndpointsTestUtil.emptyCollectionRoutes;
import static com.liferay.apio.architect.impl.endpoint.EndpointsTestUtil.emptyItemRoutes;
import static com.liferay.apio.architect.impl.endpoint.EndpointsTestUtil.emptyNestedCollectionRoutes;
import static com.liferay.apio.architect.impl.endpoint.EndpointsTestUtil.itemRoutes;
import static com.liferay.apio.architect.impl.endpoint.EndpointsTestUtil.nestedCollectionRoutes;
import static com.liferay.apio.architect.test.util.result.TryMatchers.aFailTry;
import static com.liferay.apio.architect.test.util.result.TryMatchers.aSuccessTry;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;

import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.functional.Try;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class FormEndpointTest {

	@Test
	public void testEmptyFormsMethodsReturnsFailure() {
		FormEndpoint formEndpoint = new FormEndpoint(
			__ -> emptyCollectionRoutes(), __ -> emptyItemRoutes(),
			(name, nestedName) -> emptyNestedCollectionRoutes());

		Try<Form> creatorFormTry = formEndpoint.creatorForm("");
		Try<Form> nestedCreatorFormTry = formEndpoint.nestedCreatorForm("", "");
		Try<Form> updaterFormTry = formEndpoint.updaterForm("");

		assertThat(creatorFormTry, is(aFailTry()));
		assertThat(nestedCreatorFormTry, is(aFailTry()));
		assertThat(updaterFormTry, is(aFailTry()));
	}

	@Test
	public void testEmptyRoutesReturnsFailure() {
		FormEndpoint formEndpoint = new FormEndpoint(
			__ -> {
				throw new NoSuchElementException();
			},
			__ -> {
				throw new NoSuchElementException();
			},
			(name, nestedName) -> {
				throw new NoSuchElementException();
			});

		Try<Form> creatorFormTry = formEndpoint.creatorForm("");
		Try<Form> nestedCreatorFormTry = formEndpoint.nestedCreatorForm("", "");
		Try<Form> updaterFormTry = formEndpoint.updaterForm("");

		assertThat(creatorFormTry, is(aFailTry()));
		assertThat(nestedCreatorFormTry, is(aFailTry()));
		assertThat(updaterFormTry, is(aFailTry()));
	}

	@Test
	public void testFunctionsReceiveResourceNames() {
		List<String> names = new ArrayList<>();

		FormEndpoint formEndpoint = new FormEndpoint(
			name -> {
				names.add(name);

				return null;
			},
			name -> {
				names.add(name);

				return null;
			},
			(name, nestedName) -> {
				names.add(name);
				names.add(nestedName);

				return null;
			});

		formEndpoint.creatorForm("a");
		formEndpoint.updaterForm("b");
		formEndpoint.nestedCreatorForm("c", "d");

		assertThat(names, contains("a", "b", "c", "d"));
	}

	@Test
	public void testValidCreatorFormMethodReturnsSuccess() {
		FormEndpoint formEndpoint = new FormEndpoint(
			__ -> collectionRoutes(), __ -> null, (name, nestedName) -> null);

		Try<Form> creatorFormTry = formEndpoint.creatorForm("");

		assertThat(creatorFormTry, is(aSuccessTry()));

		Form form = creatorFormTry.getUnchecked();

		assertThat(form.getId(), is("c/name"));
	}

	@Test
	public void testValidNestedCreatorFormMethodReturnsSuccess() {
		FormEndpoint formEndpoint = new FormEndpoint(
			__ -> null, __ -> null,
			(name, nestedName) -> nestedCollectionRoutes());

		Try<Form> nestedCreatorFormTry = formEndpoint.nestedCreatorForm("", "");

		assertThat(nestedCreatorFormTry, is(aSuccessTry()));

		Form form = nestedCreatorFormTry.getUnchecked();

		assertThat(form.getId(), is("c/name/nestedName"));
	}

	@Test
	public void testValidUpdaterFormMethodReturnsSuccess() {
		FormEndpoint formEndpoint = new FormEndpoint(
			__ -> null, __ -> itemRoutes(), (name, nestedName) -> null);

		Try<Form> updaterFormTry = formEndpoint.updaterForm("");

		assertThat(updaterFormTry, is(aSuccessTry()));

		Form form = updaterFormTry.getUnchecked();

		assertThat(form.getId(), is("u/name"));
	}

<<<<<<< HEAD:apio-architect-impl/src/test/java/com/liferay/apio/architect/impl/endpoint/FormEndpointTest.java
=======
	private static <T, S> CollectionRoutes<T, S> _collectionRoutes() {
		CollectionRoutes.Builder<T, S> builder =
			new CollectionRoutesImpl.BuilderImpl<>(
				"name", __ -> null, REQUEST_PROVIDE_FUNCTION,
				__ -> {
				},
				__ -> null);

		return builder.addCreator(
			__ -> null, HAS_ADDING_PERMISSION_FUNCTION, FORM_BUILDER_FUNCTION
		).build();
	}

	private static <T, S> CollectionRoutes<T, S> _emptyCollectionRoutes() {
		return new CollectionRoutesImpl<>(
			new CollectionRoutesImpl.BuilderImpl<>(
				"",
				__ -> null, httpServletRequest -> aClass -> Optional.empty(),
				__ -> {
				},
				__ -> null));
	}

	private static <T, S> ItemRoutes<T, S> _emptyItemRoutes() {
		return new ItemRoutesImpl<>(
			new ItemRoutesImpl.BuilderImpl<>(
				"",
				__ -> null, httpServletRequest -> aClass -> Optional.empty(),
				__ -> {
				},
				__ -> null));
	}

	private static <T, S, U> NestedCollectionRoutes<T, S, U>
		_emptyNestedCollectionRoutes() {

		return new NestedCollectionRoutesImpl<>(
			new NestedCollectionRoutesImpl.BuilderImpl<>(
				"", "", httpServletRequest -> aClass -> Optional.empty(),
				__ -> {
				},
				__ -> null));
	}

	private static <T, S> ItemRoutes<T, S> _itemRoutes() {
		ItemRoutes.Builder<T, S> builder = new ItemRoutesImpl.BuilderImpl<>(
			"name", __ -> null, REQUEST_PROVIDE_FUNCTION,
			__ -> {
			},
			__ -> null);

		return builder.addUpdater(
			(aLong, body) -> null, (credentials, s) -> true,
			FORM_BUILDER_FUNCTION
		).build();
	}

	private static <T, S, U> NestedCollectionRoutes<T, S, U>
		_nestedCollectionRoutes() {

		NestedCollectionRoutes.Builder<T, S, U> builder =
			new NestedCollectionRoutesImpl.BuilderImpl<>(
				"name", "nestedName", REQUEST_PROVIDE_FUNCTION,
				__ -> {
				},
				__ -> null);

		return builder.addCreator(
			(s, body) -> null, hasNestedAddingPermissionFunction(),
			FORM_BUILDER_FUNCTION
		).build();
	}

>>>>>>> APIO-99 adapt tests:apio-architect-impl/src/test/java/com/liferay/apio/architect/impl/internal/endpoint/FormEndpointTest.java
}