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

package com.liferay.apio.architect.operation;

import aQute.bnd.annotation.ProviderType;

import com.liferay.apio.architect.form.Form;

import java.util.Optional;

/**
 * Represents a resource's operation.
 *
 * @author Alejandro Hernández
 */
@ProviderType
public interface Operation {

	/**
	 * Returns the custom part of the operation
	 *
	 * @return the custom part of the operation
	 * @review
	 */
	public String getCustom();

	/**
	 * Returns this operation's expected form, if present; returns {@code
	 * Optional#empty()} otherwise.
	 *
	 * @return the operation's expected form, if present; {@code
	 *         Optional#empty()} otherwise
	 */
	public Optional<Form> getFormOptional();

	/**
	 * Returns the operation's method.
	 *
	 * @return the operation's method
	 * @review
	 */
	public HTTPMethod getHttpMethod();

	/**
	 * Returns the operation's name.
	 *
	 * @return the operation's name
	 * @review
	 */
	public String getName();

	/**
	 * Returns the operation's uri.
	 *
	 * @return the operation's uri
	 * @review
	 */
	public Optional<String> getURIOptional();

	/**
	 * Return {@code true} if this is a collection's operation
	 *
	 * @return {@code true} if this is a collection's operation
	 * @review
	 */
	public boolean isCollection();

	/**
	 * Return {@code true} if this is a custom operation
	 *
	 * @return {@code true} if this is a custom operation
	 * @review
	 */
	public boolean isCustom();

}