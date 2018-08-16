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

package com.liferay.apio.architect.custom.actions;

import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.operation.Method;

import java.util.Optional;

/**
 * Models a custom resource route
 *
 * @author Javier Gamarra
 * @review
 */
public abstract class CustomRoute<T> {

	public abstract Method getMethod();

	public abstract String getName();

	public Optional<Form<T>> getForm() {
		return _optionalForm;
	}

	public void setForm(Form<T> form) {
		_optionalForm = Optional.ofNullable(form);
	}

	private Optional<Form<T>> _optionalForm = Optional.empty();

}