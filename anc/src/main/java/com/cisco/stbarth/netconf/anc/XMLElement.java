/**
 * Copyright (c) 2018 Cisco Systems
 * 
 * Author: Steven Barth <stbarth@cisco.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cisco.stbarth.netconf.anc;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class XMLElement implements Iterable<XMLElement>, Cloneable {
    private Element element;
    private static HashMap<String,String> xpathNamespaces = new HashMap<>();

    public static class XMLException extends Exception {
        XMLException(Throwable cause) {
            super(cause);
        }
    }

    public static class XPathException extends RuntimeException {
        XPathException(Throwable cause) {
            super(cause);
        }
    }

    public XMLElement(Element element) {
        this.element = element;
    }

    /**
     * Create a wrapped XML element from an InputStream
     * @param stream
     * @throws IOException
     * @throws XMLException
     */
    public XMLElement(InputStream stream) throws IOException, XMLException {
        try {
            element = createBuilder().parse(stream).getDocumentElement();
        } catch (SAXException e) {
            throw new XMLException(e);
        }
    }

    /**
     * Create a wrapped XML element from a string containing an XML document
     * @param source
     * @throws IOException
     * @throws XMLException
     */
    public XMLElement(String source) throws IOException, XMLException {
        this(new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Create a new empty document and return the root element.
     * @param namespace
     * @param name
     */
    public XMLElement(String namespace, String name) {
        Document document = createBuilder().newDocument();
        element = document.createElementNS(namespace, name);
        document.appendChild(element);
    }

    /**
     * Create a new empty document, apply a function and return the root element.
     * @param namespace
     * @param name
     * @param consumer
     */
    public XMLElement(String namespace, String name, Consumer<XMLElement> consumer) {
        this(namespace, name);
        consumer.accept(this);
    }

    private static DocumentBuilder createBuilder() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            return factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<XMLElement> nodeListToElementStream(NodeList list) {
        return IntStream.range(0, list.getLength()).mapToObj(list::item).
                filter(n -> n.getNodeType() == Node.ELEMENT_NODE).map(Element.class::cast).map(XMLElement::new);
    }

    private Optional<Object> select(QName type, String xPathExpression, String... ns) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new NamespaceContext() {
                @Override
                public String getNamespaceURI(String prefix) {
                    return Arrays.stream(ns).filter(x -> x.charAt(prefix.length()) == '=' && x.startsWith(prefix)).
                            findAny().map(x -> x.substring(prefix.length() + 1)).orElse(
                                    xpathNamespaces.getOrDefault(prefix, element.getNamespaceURI()));
                }

                @Override
                public String getPrefix(String namespaceURI) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Iterator getPrefixes(String namespaceURI) {
                    throw new UnsupportedOperationException();
                }
            });
            return Optional.ofNullable(xpath.evaluate(xPathExpression, element, type));
        } catch (XPathExpressionException e) {
            throw new XPathException(e);
        }
    }

    /**
     * Create a stream of all child nodes satisfying an XPath query.
     *
     * Note that unless withoutNamespaces() was used, every name in a query needs to be namespace-qualified, e.g.
     * find("nm:netconf-state/nm:schemas/nm:schema", "nm=urn:ietf:params:xml:ns:yang:ietf-netconf-monitoring")
     *
     * @param xpath
     * @param namespaces Namespace definitions in the form of "prefix=URI"
     * @return
     */
    public Stream<XMLElement> find(String xpath, String... namespaces) {
        return nodeListToElementStream(select(XPathConstants.NODESET, xpath, namespaces).map(NodeList.class::cast).get());
    }

    /**
     * Create a (one-time) iterable stream of all child nodes satisfying an XPath query.
     *
     * Note that unless withoutNamespaces() was used, every name in a query needs to be namespace-qualified, e.g.
     * select("nm:netconf-state/nm:schemas/nm:schema", "nm=urn:ietf:params:xml:ns:yang:ietf-netconf-monitoring")
     *
     * @param xpath
     * @param namespaces Namespace definitions in the form of "prefix=URI"
     * @return
     */
    public Iterable<XMLElement> select(String xpath, String... namespaces) {
        return find(xpath, namespaces)::iterator;
    }

    /**
     * Select a single child node using XPath.
     *
     * Note that unless withoutNamespaces() was used, every name in a query needs to be namespace-qualified, e.g.
     * select("nm:netconf-state/nm:schemas/nm:schema", "nm=urn:ietf:params:xml:ns:yang:ietf-netconf-monitoring")
     *
     * @param xpath
     * @param namespaces Namespace definitions in the form of "prefix=URI"
     * @return
     */
    public Optional<XMLElement> selectElement(String xpath, String... namespaces) {
        return select(XPathConstants.NODE, xpath, namespaces).map(Element.class::cast).map(XMLElement::new);
    }

    /**
     * Select a single string value using XPath.
     *
     * Note that unless withoutNamespaces() was used, every name in a query needs to be namespace-qualified, e.g.
     * select("nm:netconf-state/nm:schemas/nm:schema", "nm=urn:ietf:params:xml:ns:yang:ietf-netconf-monitoring")
     *
     * @param xpath
     * @param namespaces Namespace definitions in the form of "prefix=URI"
     * @return
     */
    public Optional<String> selectString(String xpath, String... namespaces) {
        return select(XPathConstants.STRING, xpath, namespaces).map(String.class::cast);
    }

    /**
     * Select a single numeric value using XPath.
     *
     * Note that unless withoutNamespaces() was used, every name in a query needs to be namespace-qualified, e.g.
     * select("nm:netconf-state/nm:schemas/nm:schema", "nm=urn:ietf:params:xml:ns:yang:ietf-netconf-monitoring")
     *
     * @param xpath
     * @param namespaces Namespace definitions in the form of "prefix=URI"
     * @return
     */
    public Optional<Number> selectNumber(String xpath, String... namespaces) {
        return select(XPathConstants.NUMBER, xpath, namespaces).map(Number.class::cast);
    }

    /**
     * Get the local name of the element
     * @return
     */
    public String getName() {
        return element.getLocalName();
    }

    /**
     * Get the namespace of the element (if any)
     * @return
     */
    public String getNamespace() {
        return element.getNamespaceURI();
    }

    /**
     * Get the text content of the element.
     * @return
     */
    public String getText() {
        return element.getTextContent();
    }

    /**
     * Get the text content of the first immediate child element with the given name.
     * @param namespace
     * @param name
     * @return
     */
    public String getText(String namespace, String name) {
        return getFirst(namespace, name).map(XMLElement::getText).orElse("");
    }

    /**
     * Get the text element of the first immediate child element with the given name.
     * @param name
     * @return
     */
    public String getText(String name) {
        return getText(null, name);
    }

    /**
     * Get the text content of the first immediate child element with the given name.
     * @param namespace
     * @param name
     * @param defaultValue
     * @return
     */
    public String getTextOrDefault(String namespace, String name, String defaultValue) {
        return getFirst(namespace, name).map(XMLElement::getText).orElse(defaultValue);
    }

    /**
     * Get the text content of the first immediate child element with the given name.
     * @param name
     * @param defaultValue
     * @return
     */
    public String getTextOrDefault(String name, String defaultValue) {
        return getTextOrDefault(null, name, defaultValue);
    }

    /**
     * Get the value of an attribute of this element in the same namespace.
     * @param namespace
     * @param name
     * @return
     */
    public String getAttribute(String namespace, String name) {
        return element.getAttributeNS(namespace != null ? namespace : getNamespace(), name);
    }

    /**
     * Get the value of an attribute of this element that has the same namespace as this element.
     * @param name
     * @return
     */
    public String getAttribute(String name) {
        return getAttribute(null, name);
    }

    /**
     * Get all attributes of this element belonging to the given namespace.
     * @param namespace
     * @return
     */
    public Map<String,String> getAttributes(String namespace) {
        final String ns = (namespace != null) ? namespace : getNamespace();
        NamedNodeMap attributes = element.getAttributes();
        return IntStream.range(0, attributes.getLength()).mapToObj(attributes::item).
                filter(a -> (ns == null || ns.equals(a.getNamespaceURI()))).map(Attr.class::cast).
                collect(Collectors.toMap(Attr::getLocalName, Attr::getValue));
    }

    /**
     * Get all attributes of this element belonging to the same namespace.
     * @return
     */
    public Map<String,String> getAttributes() {
        return getAttributes(null);
    }

    /**
     * Get a stream of all immediate child elements.
     * @return
     */
    public Stream<XMLElement> stream() {
        return nodeListToElementStream(element.getChildNodes());
    }

    /**
     * Get a stream of all immediate child elements with given name and namespace.
     * @param namespace
     * @param name
     * @return
     */
    public Stream<XMLElement> stream(String namespace, String name) {
        final String ns = (namespace != null) ? namespace : getNamespace();
        return stream().filter(e -> e.getName().equals(name) &&
                (ns == null || ns.equals(e.getNamespace())));
    }

    /**
     * Get a stream of all immediate child elements with given name.
     * @param name
     * @return
     */
    public Stream<XMLElement> stream(String name) {
        return stream(null, name);
    }

    /**
     * Get the first immediate child element with the given name.
     * @param namespace
     * @param name
     * @return
     */
    public Optional<XMLElement> getFirst(String namespace, String name) {
        return stream(namespace, name).findFirst();
    }

    /**
     * Get the first immediate child element with the given name in the same namespace.
     * @param name
     * @return
     */
    public Optional<XMLElement> getFirst(String name) {
        return getFirst(null, name);
    }

    /**
     * Get the first immediate child element with the given name or an empty element
     * @param namespace
     * @param name
     * @return
     */
    public XMLElement getOrEmpty(String namespace, String name) {
        return stream(namespace, name).findFirst().orElse(new XMLElement(namespace, name));
    }

    /**
     * Get the first immediate child element with the given name in the same namespace.
     * @param name
     * @return
     */
    public XMLElement getOrEmpty(String name) {
        return getOrEmpty(null, name);
    }


    /**
     * Get a (one-time) iterable of all child elements with the given name
     * @param namespace
     * @param name
     * @return
     */
    public Iterable<XMLElement> getAll(String namespace, String name) {
        return stream(namespace, name)::iterator;
    }

    /**
     * Get a (one-time) iterable of all child elements with the given name in the same namespace as this element.
     * @param name
     * @return
     */
    public Iterable<XMLElement> getAll(String name) {
        return stream(null, name)::iterator;
    }

    /**
     * Get the parent element of this element.
     * @return
     */
    public XMLElement getParent() {
        Node node = element.getParentNode();
        return (node instanceof Element) ? new XMLElement((Element)node) : null;
    }

    /**
     * Get the root element of this element.
     * @return
     */
    public XMLElement getRoot() {
        return new XMLElement(element.getOwnerDocument().getDocumentElement());
    }

    /**
     * Create a new element in the same document and return it.
     * @param namespace
     * @param name
     * @return
     */
    public XMLElement create(String namespace, String name) {
        final String ns = (namespace != null) ? namespace : getNamespace();
        return new XMLElement(element.getOwnerDocument().createElementNS(ns, name));
    }

    /**
     * Create a new element in the same document, append it and return it.
     * @param namespace
     * @param name
     * @return
     */
    public XMLElement createChild(String namespace, String name) {
        XMLElement child = create(namespace, name);
        element.appendChild(child.element);
        return child;
    }

    /**
     * Create a new element in the same document and in the same namespace, append it and return it.
     * @param name
     * @return
     */
    public XMLElement createChild(String name) {
        return createChild(getNamespace(), name);
    }

    /**
     * Returns the first element with given name (creating it if it does not exist)
     * if a condition is true, otherwise deletes it and returns null.
     * 
     * @param namespace
     * @param name
     * @param condition
     * @return
     */
    public Optional<XMLElement> setChildIff(String namespace, String name, boolean condition) {
        Optional<XMLElement> child = getFirst(namespace, name);
        if (!condition) {
            child.ifPresent(XMLElement::remove);
            return Optional.empty();
        } else if (child.isPresent()) {
            return child;
        } else {
            return Optional.of(createChild(namespace, name));
        }
    }

    /**
     * Returns the first element with given name (creating it if it does not exist).
     * 
     * @param namespace
     * @param name
     * @return
     */
    public XMLElement setChild(String namespace, String name) {
        return setChildIff(namespace, name, true).get();
    }

    /**
     * Returns the first element with given name (creating it if it does not exist).
     * 
     * @param name
     * @return
     */
    public XMLElement setChild(String name) {
        return setChildIff(name, true).get();
    }

    /**
     * Returns the first element with given name (creating it if it does not exist)
     * if a condition is true, otherwise deletes it and returns null.
     * 
     * @param name
     * @param condition
     * @return
     */
    public Optional<XMLElement> setChildIff(String name, boolean condition) {
        return setChildIff(getNamespace(), name, condition);
    }

    /**
     * Remove the element from its parent element
     */
    public void remove() {
        if (element.getParentNode() != null)
            element.getParentNode().removeChild(element);
    }

    /**
     * Apply a consumer to the element and return it
     */
    public XMLElement with(Consumer<XMLElement> consumer) {
        consumer.accept(this);
        return this;
    }

    /**
     * Set the text content of this element and return the element.
     * @param value
     * @return
     */
    public XMLElement withText(String value) {
        element.setTextContent(value != null ? value : "");
        return this;
    }

    /**
     * Adds a comment node with given value to this element and return this element
     * @param value
     * @return
     */
    public XMLElement withComment(String value) {
        element.appendChild(element.getOwnerDocument().createComment(value));
        return this;
    }

    /**
     * Append the given element as a child to this one (and adopt it if necessary) and return the parent element.
     * @param child
     * @return
     */
    public XMLElement withChild(XMLElement child) {
        element.appendChild(element.getOwnerDocument().adoptNode(child.element));
        return this;
    }

    /**
     * Append the given elements as children to this one (and adopt it if necessary) and return the parent element.
     * @param children
     * @return
     */
    public XMLElement withChildren(Iterable<XMLElement> children) {
        children.forEach(this::withChild);
        return this;
    }

    /**
     * Create and append a new child element and return the parent element.
     * @param namespace
     * @param name
     * @return
     */
    public XMLElement withChild(String namespace, String name) {
        element.appendChild(create(namespace, name).element);
        return this;
    }

    /**
     * Create and append a new child element in the same namespace and return the parent element.
     * @param name
     * @return
     */
    public XMLElement withChild(String name) {
        element.appendChild(create(getNamespace(), name).element);
        return this;
    }

    /**
     * Create and append a new child element, apply a function and return the parent element.
     * @param namespace
     * @param name
     * @param consumer
     * @return
     */
    public XMLElement withChild(String namespace, String name, Consumer<XMLElement> consumer) {
        consumer.accept(createChild(namespace, name));
        return this;
    }

    /**
     * Create and append a new child element in the same namespace, apply a function and return the parent element.
     * @param name
     * @param consumer
     * @return
     */
    public XMLElement withChild(String name, Consumer<XMLElement> consumer) {
        return withChild(getNamespace(), name, consumer);
    }

    /**
     * Create and append a new child element with given text content and return the parent element.
     * @param namespace
     * @param name
     * @param value
     * @return
     */
    public XMLElement withTextChild(String namespace, String name, String value) {
        return withChild(create(namespace, name).withText(value));
    }

    /**
     * Create and append a new child element with given text content in the same namespace and return the parent.
     * @param name
     * @param value
     * @return
     */
    public XMLElement withTextChild(String name, String value) {
        return withTextChild(getNamespace(), name, value);
    }

    /**
     * Removes the first element with given name (if present) and adds a new text node if the condition is true.
     * @param namespace
     * @param name
     * @param value
     * @param condition
     * @return
     */
    public XMLElement withTextContentIff(String namespace, String name, String value, boolean condition) {
        setChildIff(namespace, name, condition).ifPresent(x -> x.withText(value));
        return this;
    }

    /**
     * Removes the first element with given name (if present) and adds a new text node if the condition is true.
     * @param name
     * @param value
     * @param condition
     * @return
     */
    public XMLElement withTextContentIff(String name, String value, boolean condition) {
        return withTextContentIff(getNamespace(), name, value, condition);
    }

    /**
     * Removes the first element with given name (if present) and adds a new text node.
     * @param namespace
     * @param name
     * @param value
     * @return
     */
    public XMLElement withTextContent(String namespace, String name, String value) {
        return withTextContentIff(namespace, name, value, true);
    }

    /**
     * Removes the first element with given name (if present) and adds a new text node.
     * @param name
     * @param value
     * @return
     */
    public XMLElement withTextContent(String name, String value) {
        return withTextContentIff(name, value, true);
    }

    /**
     * Set of unset an attribute of this element to a given value.
     * @param namespace
     * @param name
     * @param value
     * @return
     */
    public XMLElement withAttribute(String namespace, String name, String value) {
        final String ns = (namespace != null) ? namespace : getNamespace();
        if (value != null)
            element.setAttributeNS(ns, name, value);
        else
            element.removeAttributeNS(ns, name);
        return this;
    }

    /**
     * Set of unset an attribute of this element in the same namespace as to a given value.
     * @param name
     * @param value
     * @return
     */
    public XMLElement withAttribute(String name, String value) {
        return withAttribute(getNamespace(), name, value);
    }

    /**
     * Removes all instances of the child node
     * @param namespace
     * @param name
     */
    public XMLElement withoutChildren(String namespace, String name) {
        stream(namespace, name).collect(Collectors.toList()).forEach(XMLElement::remove);
        return this;
    }

    /**
     * Removes all instances of the child node
     * @param name
     */
    public XMLElement withoutChildren(String name) {
        return withoutChildren(getNamespace(), name);
    }

    /**
     * Strip all namespaces from this element, all child elements and attributes and return this element.
     * @return
     */
    public XMLElement withoutNamespaces() {
        element = (Element)stripNamespaces(element);
        return this;
    }

    /**
     * Write either this element or the whole document to a given stream.
     * @param outputStream
     * @param wholeDocument
     * @throws XMLException
     */
    public void writeTo(OutputStream outputStream, boolean wholeDocument) throws XMLException {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(wholeDocument ? element.getOwnerDocument() : element),
                    new StreamResult(outputStream));
        } catch (TransformerException e) {
            throw new XMLException(e);
        }
    }

    private Node stripNamespaces(Node node) {
        Document document = element.getOwnerDocument();
        if (node.getNodeType() == Node.ELEMENT_NODE || node.getNodeType() == Node.ATTRIBUTE_NODE)
            node = document.renameNode(node, null, node.getLocalName());
        NodeList list = node.getChildNodes();
        IntStream.range(0, list.getLength()).mapToObj(list::item).forEach(this::stripNamespaces);
        return node;
    }

    /**
     * Get the underlying DOM element wrapped by this XMLElement
     * @return
     */
    public Element getElement() {
        return element;
    }

    /**
     * Register a namespace prefix for use in XPath calls
     * @param prefix
     * @param namespace
     */
    public static void registerXPathNamespace(String prefix, String namespace) {
        xpathNamespaces.put(prefix, namespace);
    }

    @Override
    public Iterator<XMLElement> iterator() {
        return stream().iterator();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof XMLElement && element.equals(((XMLElement) obj).element);
    }

    @Override
    public int hashCode() {
        return element.hashCode();
    }

    @Override
    public String toString() {
        return toXML();
    }

    /**
     * Transform this element into a unicode XML string.
     * @return XML string
     */
    public String toXML() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            writeTo(stream, false);
            return stream.toString("UTF-8");
        } catch (XMLException | UnsupportedEncodingException e) {
            return "";
        }
    }

    @Override
    public XMLElement clone() {
        return new XMLElement((Element)element.cloneNode(true));
    }
}
