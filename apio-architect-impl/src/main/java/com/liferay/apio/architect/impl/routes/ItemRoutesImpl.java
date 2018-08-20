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

package com.liferay.apio.architect.impl.routes;

import static com.liferay.apio.architect.impl.routes.RoutesBuilderUtil.provide;
import static com.liferay.apio.architect.impl.routes.RoutesBuilderUtil.provideConsumer;

import com.liferay.apio.architect.alias.IdentifierFunction;
import com.liferay.apio.architect.alias.form.FormBuilderFunction;
import com.liferay.apio.architect.alias.routes.CustomItemFunction;
import com.liferay.apio.architect.alias.routes.DeleteItemConsumer;
import com.liferay.apio.architect.alias.routes.GetItemFunction;
import com.liferay.apio.architect.alias.routes.UpdateItemFunction;
import com.liferay.apio.architect.alias.routes.permission.HasRemovePermissionFunction;
import com.liferay.apio.architect.alias.routes.permission.HasUpdatePermissionFunction;
import com.liferay.apio.architect.consumer.throwable.ThrowableBiConsumer;
import com.liferay.apio.architect.consumer.throwable.ThrowableConsumer;
import com.liferay.apio.architect.consumer.throwable.ThrowablePentaConsumer;
import com.liferay.apio.architect.consumer.throwable.ThrowableTetraConsumer;
import com.liferay.apio.architect.consumer.throwable.ThrowableTriConsumer;
import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.custom.actions.CustomRoute;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.function.throwable.ThrowableBiFunction;
import com.liferay.apio.architect.function.throwable.ThrowableFunction;
import com.liferay.apio.architect.function.throwable.ThrowableHexaFunction;
import com.liferay.apio.architect.function.throwable.ThrowablePentaFunction;
import com.liferay.apio.architect.function.throwable.ThrowableTetraFunction;
import com.liferay.apio.architect.function.throwable.ThrowableTriFunction;
import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.impl.alias.ProvideFunction;
import com.liferay.apio.architect.impl.form.FormImpl;
import com.liferay.apio.architect.impl.operation.CreateOperation;
import com.liferay.apio.architect.impl.operation.DeleteOperation;
import com.liferay.apio.architect.impl.operation.RetrieveOperation;
import com.liferay.apio.architect.impl.operation.UpdateOperation;
import com.liferay.apio.architect.impl.single.model.SingleModelImpl;
import com.liferay.apio.architect.operation.HTTPMethod;
import com.liferay.apio.architect.operation.Operation;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.uri.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Alejandro Hernández
 */
public class ItemRoutesImpl<T, S> implements ItemRoutes<T, S> {

	public ItemRoutesImpl(BuilderImpl<T, S> builderImpl) {
		_deleteItemConsumer = builderImpl._deleteItemConsumer;
		_form = builderImpl._form;
		_singleModelFunction = builderImpl._singleModelFunction;
		_updateItemFunction = builderImpl._updateItemFunction;

		_customItemFunctions = builderImpl._customItemFunctions;
		_customRoutes = builderImpl._customRoutes;
	}

	@Override
	public Optional<Map<String, CustomItemFunction<?, S>>>
		getCustomItemFunctions() {

		return Optional.of(_customItemFunctions);
	}

	@Override
	public Map<String, CustomRoute> getCustomRoutes() {
		return _customRoutes;
	}

	@Override
	public Optional<DeleteItemConsumer<S>> getDeleteConsumerOptional() {
		return Optional.ofNullable(_deleteItemConsumer);
	}

	@Override
	public Optional<Form> getFormOptional() {
		return Optional.ofNullable(_form);
	}

	@Override
	public Optional<GetItemFunction<T, S>> getItemFunctionOptional() {
		return Optional.ofNullable(_singleModelFunction);
	}

	@Override
	public Optional<UpdateItemFunction<T, S>> getUpdateItemFunctionOptional() {
		return Optional.ofNullable(_updateItemFunction);
	}

	/**
	 * Creates the {@code ItemRoutes} of an {@link
	 * com.liferay.apio.architect.router.ItemRouter}.
	 *
	 * @param <T> the model's type
	 * @param <S> the type of the model's identifier (e.g., {@code Long}, {@code
	 *        String}, etc.)
	 */
	@SuppressWarnings("unused")
	public static class BuilderImpl<T, S> implements Builder<T, S> {

		public BuilderImpl(
			String name, ProvideFunction provideFunction,
			Consumer<String> neededProviderConsumer,
			Function<Path, ?> pathToIdentifierFunction,
			Function<S, Optional<Path>> identifierToPathFunction,
			Function<String, Optional<String>> nameFunction) {

			_name = name;
			_provideFunction = provideFunction;
			_neededProviderConsumer = neededProviderConsumer;

			_pathToIdentifierFunction = pathToIdentifierFunction::apply;
			_identifierToPathFunction = identifierToPathFunction;
			_nameFunction = nameFunction;
		}

		@Override
		public <R, U, I extends Identifier<?>> Builder<T, S> addCustomRoute(
			CustomRoute customRoute,
			ThrowableBiFunction<S, R, U> throwableBiFunction, Class<I> supplier,
			BiFunction<Credentials, S, Boolean> permissionBiFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			String name = customRoute.getName();

			_calculateForm(customRoute, formBuilderFunction, name);

			_customRoutes.put(name, customRoute);

			_customPermissionFunctions.put(name, permissionBiFunction);

			CustomItemFunction<U, S> customFunction =
				httpServletRequest -> s -> body -> Try.fromFallible(
					() -> throwableBiFunction.andThen(
						t -> new SingleModelImpl<>(
							t, _getResourceName(supplier))
					).apply(
						s, _getModel(customRoute.getForm(), body)
					));

			_customItemFunctions.put(name, customFunction);

			return this;
		}

		@Override
		public <A, B, C, D, R, U, I extends Identifier<?>> Builder<T, S>
			addCustomRoute(
				CustomRoute customRoute,
				ThrowableHexaFunction<S, R, A, B, C, D, U>
					throwableHexaFunction,
				Class<A> aClass, Class<B> bClass, Class<C> cClass,
				Class<D> dClass, Class<I> supplier,
				BiFunction<Credentials, S, Boolean> permissionBiFunction,
				FormBuilderFunction<R> formBuilderFunction) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());
			_neededProviderConsumer.accept(cClass.getName());
			_neededProviderConsumer.accept(dClass.getName());

			String name = customRoute.getName();

			_calculateForm(customRoute, formBuilderFunction, name);

			_customRoutes.put(name, customRoute);

			_customPermissionFunctions.put(name, permissionBiFunction);

			CustomItemFunction<U, S> customFunction =
				httpServletRequest -> s -> body -> provide(
					_provideFunction.apply(httpServletRequest), aClass, bClass,
					cClass, dClass,
					(a, b, c, d) -> throwableHexaFunction.andThen(
						t -> new SingleModelImpl<>(
							t, _getResourceName(supplier))
					).apply(
						s, _getModel(customRoute.getForm(), body), a, b, c, d
					));

			_customItemFunctions.put(name, customFunction);

			return this;
		}

		@Override
		public <A, B, C, R, U, I extends Identifier<?>> Builder<T, S>
			addCustomRoute(
				CustomRoute customRoute,
				ThrowablePentaFunction<S, R, A, B, C, U> throwablePentaFunction,
				Class<A> aClass, Class<B> bClass, Class<C> cClass,
				Class<I> supplier,
				BiFunction<Credentials, S, Boolean> permissionBiFunction,
				FormBuilderFunction<R> formBuilderFunction) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());
			_neededProviderConsumer.accept(cClass.getName());

			String name = customRoute.getName();

			_calculateForm(customRoute, formBuilderFunction, name);

			_customRoutes.put(name, customRoute);

			_customPermissionFunctions.put(name, permissionBiFunction);

			CustomItemFunction<U, S> customFunction =
				httpServletRequest -> s -> body -> provide(
					_provideFunction.apply(httpServletRequest), aClass, bClass,
					cClass,
					(a, b, c) -> throwablePentaFunction.andThen(
						t -> new SingleModelImpl<>(
							t, _getResourceName(supplier))
					).apply(
						s, _getModel(customRoute.getForm(), body), a, b, c
					));

			_customItemFunctions.put(name, customFunction);

			return this;
		}

		@Override
		public <A, B, R, U, I extends Identifier<?>> Builder<T, S>
			addCustomRoute(
				CustomRoute customRoute,
				ThrowableTetraFunction<S, R, A, B, U> throwableTetraFunction,
				Class<A> aClass, Class<B> bClass, Class<I> supplier,
				BiFunction<Credentials, S, Boolean> permissionBiFunction,
				FormBuilderFunction<R> formBuilderFunction) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());

			String name = customRoute.getName();

			_calculateForm(customRoute, formBuilderFunction, name);

			_customRoutes.put(name, customRoute);

			_customPermissionFunctions.put(name, permissionBiFunction);

			CustomItemFunction<U, S> customFunction =
				httpServletRequest -> s -> body -> provide(
					_provideFunction.apply(httpServletRequest), aClass, bClass,
					(a, b) -> throwableTetraFunction.andThen(
						t -> new SingleModelImpl<>(
							t, _getResourceName(supplier))
					).apply(
						s, _getModel(customRoute.getForm(), body), a, b
					));

			_customItemFunctions.put(name, customFunction);

			return this;
		}

		@Override
		public <A, R, U, I extends Identifier<?>> Builder<T, S> addCustomRoute(
			CustomRoute customRoute,
			ThrowableTriFunction<S, R, A, U> throwableTriFunction,
			Class<A> aClass, Class<I> supplier,
			BiFunction<Credentials, S, Boolean> permissionBiFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			_neededProviderConsumer.accept(aClass.getName());

			String name = customRoute.getName();

			_calculateForm(customRoute, formBuilderFunction, name);

			_customRoutes.put(name, customRoute);

			_customPermissionFunctions.put(name, permissionBiFunction);

			CustomItemFunction<U, S> customFunction =
				httpServletRequest -> s -> body -> provide(
					_provideFunction.apply(httpServletRequest), aClass,
					a -> throwableTriFunction.andThen(
						t -> new SingleModelImpl<>(
							t, _getResourceName(supplier))
					).apply(
						s, _getModel(customRoute.getForm(), body), a
					));

			_customItemFunctions.put(name, customFunction);

			return this;
		}

		@Override
		public <A> Builder<T, S> addGetter(
			ThrowableBiFunction<S, A, T> getterThrowableBiFunction,
			Class<A> aClass) {

			_neededProviderConsumer.accept(aClass.getName());

			_singleModelFunction = httpServletRequest -> s -> provide(
				_provideFunction.apply(httpServletRequest), aClass,
				Credentials.class,
				(a, credentials) -> getterThrowableBiFunction.andThen(
					t -> new SingleModelImpl<>(
						t, _name, _getOperations(credentials, s))
				).apply(
					s, a
				));

			return this;
		}

		@Override
		public Builder<T, S> addGetter(
			ThrowableFunction<S, T> getterThrowableFunction) {

			_singleModelFunction = httpServletRequest -> s -> provide(
				_provideFunction.apply(httpServletRequest), Credentials.class,
				credentials -> getterThrowableFunction.andThen(
					t -> new SingleModelImpl<>(
						t, _name, _getOperations(credentials, s))
				).apply(
					s
				));

			return this;
		}

		@Override
		public <A, B, C, D> Builder<T, S> addGetter(
			ThrowablePentaFunction<S, A, B, C, D, T>
				getterThrowablePentaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass,
			Class<D> dClass) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());
			_neededProviderConsumer.accept(cClass.getName());
			_neededProviderConsumer.accept(dClass.getName());

			_singleModelFunction = httpServletRequest -> s -> provide(
				_provideFunction.apply(httpServletRequest), aClass, bClass,
				cClass, dClass, Credentials.class,
				(a, b, c, d, credentials) ->
					getterThrowablePentaFunction.andThen(
						t -> new SingleModelImpl<>(
							t, _name, _getOperations(credentials, s))
					).apply(
						s, a, b, c, d
					));

			return this;
		}

		@Override
		public <A, B, C> Builder<T, S> addGetter(
			ThrowableTetraFunction<S, A, B, C, T> getterThrowableTetraFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());
			_neededProviderConsumer.accept(cClass.getName());

			_singleModelFunction = httpServletRequest -> s -> provide(
				_provideFunction.apply(httpServletRequest), aClass, bClass,
				cClass, Credentials.class,
				(a, b, c, credentials) -> getterThrowableTetraFunction.andThen(
					t -> new SingleModelImpl<>(
						t, _name, _getOperations(credentials, s))
				).apply(
					s, a, b, c
				));

			return this;
		}

		@Override
		public <A, B> Builder<T, S> addGetter(
			ThrowableTriFunction<S, A, B, T> getterThrowableTriFunction,
			Class<A> aClass, Class<B> bClass) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());

			_singleModelFunction = httpServletRequest -> s -> provide(
				_provideFunction.apply(httpServletRequest), aClass, bClass,
				Credentials.class,
				(a, b, credentials) -> getterThrowableTriFunction.andThen(
					t -> new SingleModelImpl<>(
						t, _name, _getOperations(credentials, s))
				).apply(
					s, a, b
				));

			return this;
		}

		@Override
		public <A> Builder<T, S> addRemover(
			ThrowableBiConsumer<S, A> removerThrowableBiConsumer,
			Class<A> aClass,
			HasRemovePermissionFunction<S> hasRemovePermissionFunction) {

			_neededProviderConsumer.accept(aClass.getName());

			_hasRemovePermissionFunction = hasRemovePermissionFunction;

			_deleteItemConsumer = httpServletRequest -> s -> provideConsumer(
				_provideFunction.apply(httpServletRequest), aClass,
				a -> removerThrowableBiConsumer.accept(s, a));

			return this;
		}

		@Override
		public Builder<T, S> addRemover(
			ThrowableConsumer<S> removerThrowableConsumer,
			HasRemovePermissionFunction<S> hasRemovePermissionFunction) {

			_hasRemovePermissionFunction = hasRemovePermissionFunction;

			_deleteItemConsumer = __ -> removerThrowableConsumer;

			return this;
		}

		@Override
		public <A, B, C, D> Builder<T, S> addRemover(
			ThrowablePentaConsumer<S, A, B, C, D> removerThrowablePentaConsumer,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			HasRemovePermissionFunction<S> hasRemovePermissionFunction) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());
			_neededProviderConsumer.accept(cClass.getName());
			_neededProviderConsumer.accept(dClass.getName());

			_hasRemovePermissionFunction = hasRemovePermissionFunction;

			_deleteItemConsumer = httpServletRequest -> s -> provideConsumer(
				_provideFunction.apply(httpServletRequest), aClass, bClass,
				cClass, dClass,
				(a, b, c, d) -> removerThrowablePentaConsumer.accept(
					s, a, b, c, d));

			return this;
		}

		@Override
		public <A, B, C> Builder<T, S> addRemover(
			ThrowableTetraConsumer<S, A, B, C> removerThrowableTetraConsumer,
			Class<A> aClass, Class<B> bClass, Class<C> cClass,
			HasRemovePermissionFunction<S> hasRemovePermissionFunction) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());
			_neededProviderConsumer.accept(cClass.getName());

			_hasRemovePermissionFunction = hasRemovePermissionFunction;

			_deleteItemConsumer = httpServletRequest -> s -> provideConsumer(
				_provideFunction.apply(httpServletRequest), aClass, bClass,
				cClass,
				(a, b, c) -> removerThrowableTetraConsumer.accept(s, a, b, c));

			return this;
		}

		@Override
		public <A, B> Builder<T, S> addRemover(
			ThrowableTriConsumer<S, A, B> removerThrowableTriConsumer,
			Class<A> aClass, Class<B> bClass,
			HasRemovePermissionFunction<S> hasRemovePermissionFunction) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());

			_hasRemovePermissionFunction = hasRemovePermissionFunction;

			_deleteItemConsumer = httpServletRequest -> s -> provideConsumer(
				_provideFunction.apply(httpServletRequest), aClass, bClass,
				(a, b) -> removerThrowableTriConsumer.accept(s, a, b));

			return this;
		}

		@Override
		public <R> Builder<T, S> addUpdater(
			ThrowableBiFunction<S, R, T> updaterThrowableBiFunction,
			HasUpdatePermissionFunction<S> hasUpdatePermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			_hasUpdatePermissionFunction = hasUpdatePermissionFunction;

			Form<R> form = formBuilderFunction.apply(
				new FormImpl.BuilderImpl<>(
					Arrays.asList("u", _name), _pathToIdentifierFunction));

			_form = form;

			_updateItemFunction = httpServletRequest -> s -> body -> provide(
				_provideFunction.apply(httpServletRequest), Credentials.class,
				credentials -> updaterThrowableBiFunction.andThen(
					t -> new SingleModelImpl<>(
						t, _name, _getOperations(credentials, s))
				).apply(
					s, form.get(body)
				));

			return this;
		}

		@Override
		public <A, B, C, D, R> Builder<T, S> addUpdater(
			ThrowableHexaFunction<S, R, A, B, C, D, T>
				updaterThrowableHexaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			HasUpdatePermissionFunction<S> hasUpdatePermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());
			_neededProviderConsumer.accept(cClass.getName());
			_neededProviderConsumer.accept(dClass.getName());

			_hasUpdatePermissionFunction = hasUpdatePermissionFunction;

			Form<R> form = formBuilderFunction.apply(
				new FormImpl.BuilderImpl<>(
					Arrays.asList("u", _name), _pathToIdentifierFunction));

			_form = form;

			_updateItemFunction = httpServletRequest -> s -> body -> provide(
				_provideFunction.apply(httpServletRequest), aClass, bClass,
				cClass, dClass, Credentials.class,
				(a, b, c, d, credentials) ->
					updaterThrowableHexaFunction.andThen(
						t -> new SingleModelImpl<>(
							t, _name, _getOperations(credentials, s))
					).apply(
						s, form.get(body), a, b, c, d
					));

			return this;
		}

		@Override
		public <A, B, C, R> Builder<T, S> addUpdater(
			ThrowablePentaFunction<S, R, A, B, C, T>
				updaterThrowablePentaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass,
			HasUpdatePermissionFunction<S> hasUpdatePermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());
			_neededProviderConsumer.accept(cClass.getName());

			_hasUpdatePermissionFunction = hasUpdatePermissionFunction;

			Form<R> form = formBuilderFunction.apply(
				new FormImpl.BuilderImpl<>(
					Arrays.asList("u", _name), _pathToIdentifierFunction));

			_form = form;

			_updateItemFunction = httpServletRequest -> s -> body -> provide(
				_provideFunction.apply(httpServletRequest), aClass, bClass,
				cClass, Credentials.class,
				(a, b, c, credentials) -> updaterThrowablePentaFunction.andThen(
					t -> new SingleModelImpl<>(
						t, _name, _getOperations(credentials, s))
				).apply(
					s, form.get(body), a, b, c
				));

			return this;
		}

		@Override
		public <A, B, R> Builder<T, S> addUpdater(
			ThrowableTetraFunction<S, R, A, B, T> updaterThrowableTetraFunction,
			Class<A> aClass, Class<B> bClass,
			HasUpdatePermissionFunction<S> hasUpdatePermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			_neededProviderConsumer.accept(aClass.getName());
			_neededProviderConsumer.accept(bClass.getName());

			_hasUpdatePermissionFunction = hasUpdatePermissionFunction;

			Form<R> form = formBuilderFunction.apply(
				new FormImpl.BuilderImpl<>(
					Arrays.asList("u", _name), _pathToIdentifierFunction));

			_form = form;

			_updateItemFunction = httpServletRequest -> s -> body -> provide(
				_provideFunction.apply(httpServletRequest), aClass, bClass,
				Credentials.class,
				(a, b, credentials) -> updaterThrowableTetraFunction.andThen(
					t -> new SingleModelImpl<>(
						t, _name, _getOperations(credentials, s))
				).apply(
					s, form.get(body), a, b
				));

			return this;
		}

		@Override
		public <A, R> Builder<T, S> addUpdater(
			ThrowableTriFunction<S, R, A, T> updaterThrowableTriFunction,
			Class<A> aClass,
			HasUpdatePermissionFunction<S> hasUpdatePermissionFunction,
			FormBuilderFunction<R> formBuilderFunction) {

			_neededProviderConsumer.accept(aClass.getName());

			_hasUpdatePermissionFunction = hasUpdatePermissionFunction;

			Form<R> form = formBuilderFunction.apply(
				new FormImpl.BuilderImpl<>(
					Arrays.asList("u", _name), _pathToIdentifierFunction));

			_form = form;

			_updateItemFunction = httpServletRequest -> s -> body -> provide(
				_provideFunction.apply(httpServletRequest), aClass,
				Credentials.class,
				(a, credentials) -> updaterThrowableTriFunction.andThen(
					t -> new SingleModelImpl<>(
						t, _name, _getOperations(credentials, s))
				).apply(
					s, form.get(body), a
				));

			return this;
		}

		@Override
		public ItemRoutes<T, S> build() {
			return new ItemRoutesImpl<>(this);
		}

		private void _calculateForm(
			CustomRoute customRoute, FormBuilderFunction<?> formBuilderFunction,
			String name) {

			if (formBuilderFunction != null) {
				Form<?> form = formBuilderFunction.apply(
					new FormImpl.BuilderImpl<>(
						Arrays.asList("p", _name, name),
						_pathToIdentifierFunction));

				customRoute.setForm(form);
			}
		}

		private Operation _createOperation(
			Form form, HTTPMethod method, String name, String path,
			String custom) {

			if (method == HTTPMethod.GET) {
				return new RetrieveOperation(name, false, path, custom);
			}
			else if (method == HTTPMethod.POST) {
				return new CreateOperation(form, name, path, custom);
			}
			else if (method == HTTPMethod.DELETE) {
				return new DeleteOperation(name, path, custom);
			}
			else if (method == HTTPMethod.PUT) {
				return new UpdateOperation(form, name, path, custom);
			}

			return null;
		}

		private <R> R _getModel(Optional<Form<?>> formOptional, Body body) {
			return (R)formOptional.map(
				form -> form.get(body)
			).orElse(
				null
			);
		}

		private List<Operation> _getOperations(
			Credentials credentials, S identifier) {

			Optional<Path> optional = _identifierToPathFunction.apply(
				identifier);

			if (!optional.isPresent()) {
				return Collections.emptyList();
			}

			Path path = optional.get();

			List<Operation> operations = new ArrayList<>();

			if (_hasRemovePermissionFunction != null) {
				Boolean canRemove = Try.fromFallible(
					() -> _hasRemovePermissionFunction.apply(
						credentials, identifier)
				).orElse(
					false
				);

				if (canRemove) {
					DeleteOperation deleteOperation = new DeleteOperation(
						_name, path.asURI());

					operations.add(deleteOperation);
				}
			}

			if (_hasUpdatePermissionFunction != null) {
				Boolean canUpdate = Try.fromFallible(
					() -> _hasUpdatePermissionFunction.apply(
						credentials, identifier)
				).orElse(
					false
				);

				if (canUpdate) {
					UpdateOperation updateOperation = new UpdateOperation(
						_form, _name, path.asURI());

					operations.add(updateOperation);
				}
			}

			Set<String> customPermissionKeys =
				_customPermissionFunctions.keySet();

			Stream<String> customPermissionKeysStream =
				customPermissionKeys.stream();

			customPermissionKeysStream.filter(
				key -> _getPermissionFunction(credentials, identifier, key)
			).forEach(
				routeEntry -> {
					CustomRoute customRoute = _customRoutes.get(routeEntry);

					Optional<Form<?>> formOptional = customRoute.getForm();

					Form form = formOptional.orElse(
						null
					);

					Operation operation = _createOperation(
						form, customRoute.getMethod(), _name, path.asURI(),
						routeEntry);

					operations.add(operation);
				}
			);

			return operations;
		}

		private Boolean _getPermissionFunction(
			Credentials credentials, S identifier, String key) {

			return Try.fromFallible(
				() -> _customPermissionFunctions.get(key)
			).map(
				function -> function.apply(credentials, identifier)
			).orElse(
				false
			);
		}

		private <I extends Identifier<?>> String _getResourceName(
			Class<I> supplier) {

			return _nameFunction.apply(
				supplier.getName()
			).orElse(
				null
			);
		}

		private Map<String, CustomItemFunction<?, S>> _customItemFunctions =
			new HashMap<>();
		private Map<String, BiFunction<Credentials, S, Boolean>>
			_customPermissionFunctions = new HashMap<>();
		private final Map<String, CustomRoute> _customRoutes = new HashMap<>();
		private DeleteItemConsumer<S> _deleteItemConsumer;
		private Form _form;
		private HasRemovePermissionFunction<S> _hasRemovePermissionFunction;
		private HasUpdatePermissionFunction<S> _hasUpdatePermissionFunction;
		private final Function<S, Optional<Path>> _identifierToPathFunction;
		private final String _name;
		private final Function<String, Optional<String>> _nameFunction;
		private final Consumer<String> _neededProviderConsumer;
		private final IdentifierFunction<?> _pathToIdentifierFunction;
		private final ProvideFunction _provideFunction;
		private GetItemFunction<T, S> _singleModelFunction;
		private UpdateItemFunction<T, S> _updateItemFunction;

	}

	private final Map<String, CustomItemFunction<?, S>> _customItemFunctions;
	private final Map<String, CustomRoute> _customRoutes;
	private final DeleteItemConsumer<S> _deleteItemConsumer;
	private final Form _form;
	private final GetItemFunction<T, S> _singleModelFunction;
	private final UpdateItemFunction<T, S> _updateItemFunction;

}