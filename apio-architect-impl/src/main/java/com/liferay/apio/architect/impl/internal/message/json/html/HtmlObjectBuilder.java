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

package com.liferay.apio.architect.impl.internal.message.json.html;

import java.io.StringWriter;

import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.liferay.apio.architect.impl.internal.message.json.ObjectBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Javier Gamarra
 * @review
 */
public class HtmlObjectBuilder implements ObjectBuilder<Node> {


	private Node _node;

	@Override
	public String toString() {
		try {
			StringWriter stringWriter = new StringWriter();
			StreamResult streamResult = new StreamResult(stringWriter);
			TransformerFactory transformerFactory =
				TransformerFactory.newInstance();

			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(_node);
			transformer.transform(domSource, streamResult);

			return stringWriter.toString();
		}
		catch (TransformerException te) {
			throw new RuntimeException();
		}
	}

	protected Node newObject() {
		try {
			DocumentBuilderFactory documentBuilderFactory =
				DocumentBuilderFactory.newInstance();

			DocumentBuilder documentBuilder =
				documentBuilderFactory.newDocumentBuilder();

			Document document = documentBuilder.newDocument();

			Element body = document.createElement("body");
			document.appendChild(body);

			return body;
		}
		catch (ParserConfigurationException pce) {
			throw new RuntimeException();
		}
	}

	private Document getDocument() {
		return _node.getOwnerDocument();
	}

	@Override
	public Node buildAsObject() {
		return null;
	}

	@Override
	public String build() {
		return null;
	}

	@Override
	public <U extends FieldStep> U field(String name) {
		return null;
	}
}