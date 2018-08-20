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

package com.liferay.apio.architect.sample.internal.resource;

import com.liferay.apio.architect.representor.Representable;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.sample.internal.identifier.BlogSubscriptionIdentifier;
import com.liferay.apio.architect.sample.internal.model.BlogPostingModel;
import com.liferay.apio.architect.sample.internal.model.BlogSubscriptionModel;
import com.liferay.apio.architect.sample.internal.model.PersonModel;

import org.osgi.service.component.annotations.Component;

/**
 * Provides all the information necessary to expose a BlogSubscription resource
 *
 * @author Javier Gamarra
 */
@Component
public class BlogSubscriptionRepresentable
	implements
		Representable<BlogSubscriptionModel, Long, BlogSubscriptionIdentifier> {

	@Override
	public String getName() {
		return "subscription";
	}

	@Override
	public Representor<BlogSubscriptionModel> representor(
		Representor.Builder<BlogSubscriptionModel, Long> builder) {

		return builder.types(
			"BlogSubscription"
		).identifier(
			BlogSubscriptionModel::getId
		).addString(
			"blog", this::_getTitle
		).addString(
			"person", this::_getFullName
		).build();
	}

	private String _getFullName(BlogSubscriptionModel blogSubscriptionModel) {
		PersonModel personModel = blogSubscriptionModel.getPersonModel();

		return personModel.getFullName();
	}

	private String _getTitle(BlogSubscriptionModel blogSubscriptionModel) {
		BlogPostingModel blogPostingModel =
			blogSubscriptionModel.getBlogPostingModel();

		return blogPostingModel.getTitle();
	}

}