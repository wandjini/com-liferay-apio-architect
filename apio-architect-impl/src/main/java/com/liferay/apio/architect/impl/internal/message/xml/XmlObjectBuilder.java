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

package com.liferay.apio.architect.impl.internal.message.xml;

import com.liferay.apio.architect.impl.internal.message.json.ObjectBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Javier Gamarra
 * @review
 */
public class XmlObjectBuilder implements ObjectBuilder<Node> {

	private Node _node;

	public void addValue(Node jsonObject, String name, Object value) {
		name = name.replace("@", "");

		Element element = getDocument().createElement(name);

		element.setNodeValue(value.toString());
		jsonObject.appendChild(element);
	}


	@Override
	public Node buildAsObject() {
		return _node;
	}

	@Override
	public String build() {
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

	public XMLFieldStepImpl field(String name) {
		return new XMLFieldStepImpl();
	}

	public Node get(Node jsonObject, String name) {
		return jsonObject.getFirstChild().getAttributes().getNamedItem(name);
	}

	public Element getList(Optional optional) {
		return getDocument().createElement((String) optional.get());
	}

	public Node newObject(Optional optional) {

		//FIXME!

		return getDocument().createElement("a");
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

	private class XMLFieldStepImpl implements
		FieldStep<Node, XmlObjectBuilder, XMLFieldStepImpl, ArrayXMLStep> {
		@Override
		public ArrayXMLStep arrayValue() {
			return null;
		}

		@Override
		public void booleanValue(Boolean value) {

		}

		@Override
		public XMLFieldStepImpl field(String name) {
			return null;
		}

		@Override
		public void numberValue(Number value) {

		}

		@Override
		public void objectValue(
			XmlObjectBuilder objectBuilder) {

		}

		@Override
		public void stringValue(String value) {

		}
	}

	private class ArrayXMLStep
		implements ArrayValueStep<Node, XmlObjectBuilder> {
		@Override
		public void add(
			Consumer<XmlObjectBuilder> consumer) {

		}

		@Override
		public void add(
			XmlObjectBuilder objectBuilder) {

		}

		@Override
		public void addAllBooleans(Collection<Boolean> collection) {
//			list.appendChild((Node) build);
		}

		@Override
		public void addAllJsonObjects(Collection<Node> collection) {

		}

		@Override
		public void addAllNumbers(Collection<Number> collection) {

		}

		@Override
		public void addAllStrings(Collection<String> collection) {

		}

		@Override
		public void addBoolean(Boolean value) {

		}

		@Override
		public void addNumber(Number value) {

		}

		@Override
		public void addString(String value) {

		}
	}
}