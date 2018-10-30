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

package com.cisco.stbarth.netconf.anx;

import com.cisco.stbarth.netconf.anc.Netconf;
import com.cisco.stbarth.netconf.anc.XMLElement;
import com.vaadin.data.TreeData;
import com.vaadin.ui.Tree;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.*;
import org.opendaylight.yangtools.yang.model.api.Module;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * wrapper utility class to work with ODL yangtools schema nodes
 */
class WrappedYangNode {
    private Module module;
    private DataSchemaNode node;
    private WrappedYangNode parent;
    private String namespace;
    private String name;
    private String description;
    private boolean expand;
    private boolean configuration;
    private static HashMap<String,String> prefixes = new HashMap<>();

    WrappedYangNode(WrappedYangNode parent, DataSchemaNode node) {
        this.parent = parent;
        this.node = node;
        this.namespace = node.getQName().getNamespace().toString();
        this.name = node.getQName().getLocalName();
        this.description = node.getDescription().orElse("");
        this.configuration = node.isConfiguration();
    }

    WrappedYangNode(Module module) {
        this.module = module;
        this.namespace = module.getNamespace().toString();
        this.name = module.getName();
        this.description = module.getDescription().orElse("");
        prefixes.put(namespace, module.getPrefix());
    }

    // Lookup YANG node from path
    static Optional<WrappedYangNode> byPath(WrappedYangNode module, String path) {
        Optional<WrappedYangNode> node = Optional.of(module);
        for (String element: path.split("/")) {
            //FIXME: we need to filter any choice-nodes here?
            node = node.flatMap(n -> n.getChild(element));
        }
        return node;
    }

    String getName() {
        return name;
    }

    String getCaption() {
        String caption = getName();
        if (parent != null && (parent.node == null || !parent.namespace.equals(namespace)))
            caption += " (" + namespace + ")";
        return caption;
    }

    String getSensorPath() {
        String path = name;
        for (WrappedYangNode node = this.parent; node != null; node = node.parent)
            if (!(node.node instanceof CaseSchemaNode) && !(node.node instanceof ChoiceSchemaNode))
                path = node.name + (node.module != null ? ':' : '/') + path;
        return path;
    }

    String getXPath() {
        String path = String.format("%s:%s", prefixes.get(namespace), name);
        for (WrappedYangNode node = this.parent; node != null && node.node != null; node = node.parent) {
            if (!(node.node instanceof CaseSchemaNode) && !(node.node instanceof ChoiceSchemaNode))
                path = String.format("%s:%s/%s", prefixes.get(node.namespace), node.name, path);
        }
        return "/" + path;
    }

    String getMaagic(boolean qualified) {
        String nodeName = name.replaceAll("[-.]", "_");
        if (node instanceof ListSchemaNode)
            nodeName += "[...]";
        String path = !qualified ? nodeName :
            String.format("%s__%s", prefixes.get(namespace).replaceAll("[-.]", "_"), nodeName);
        for (WrappedYangNode node = this.parent; node != null && node.node != null; node = node.parent) {
            if (!(node.node instanceof CaseSchemaNode) && !(node.node instanceof ChoiceSchemaNode)) {
                nodeName = node.name.replaceAll("[-.]", "_");
                if (node.node instanceof ListSchemaNode)
                    nodeName += "[...]";
                path = !qualified ? String.format("%s.%s", nodeName, path) :
                    String.format("%s__%s.%s", prefixes.get(node.namespace).replaceAll("[-.]", "_"), nodeName, path);
            }
        }
        return path;
    }

    String getType() {
        if (node instanceof AnyXmlSchemaNode)
            return "anyxml";
        else if (node instanceof CaseSchemaNode)
            return "case";
        else if (node instanceof ChoiceSchemaNode)
            return "choice";
        else if (node instanceof ContainerSchemaNode)
            return "container";
        else if (node instanceof LeafSchemaNode)
            return "leaf";
        else if (node instanceof LeafListSchemaNode)
            return "leaf-list";
        else if (node instanceof ListSchemaNode)
            return "list";
        else
            return "module";
    }

    String getDataType() {
        return (node instanceof TypedDataSchemaNode) ? ((TypedDataSchemaNode)node).getType().getPath().getLastComponent().getLocalName() : "";
    }

    // Recursively transform YANG schema node into XML pattern (e.g. subtree filters)
    private void addChildren(XMLElement element, DataSchemaNode node) {
        if (node instanceof DataNodeContainer) {
            DataNodeContainer container = (DataNodeContainer)node;
            for (DataSchemaNode child: container.getChildNodes()) {
                String childNS = child.getQName().getNamespace().toString();
                String childName = child.getQName().getLocalName();
                Optional<XMLElement> dataChildElement = element.getFirst(childNS, childName);
                XMLElement childElement = dataChildElement.isPresent() ?
                        dataChildElement.get() : element.createChild(childNS, childName);

                childElement.withAttribute("expand", null);
                childElement.withAttribute("root", null);

                addChildren(childElement, child);

                if (!dataChildElement.isPresent()) {
                    if (child instanceof LeafSchemaNode || child instanceof LeafListSchemaNode)
                        childElement.withText(child.getDescription().orElse(""));

                    element.withComment(childElement.toString().replaceAll("\\s+$", ""));
                    childElement.remove();
                }
            }
        }
    }

    // Create XML template (e.g. subtree filter) from current YANG node
    Optional<XMLElement> createNetconfTemplate(String operation, XMLElement data) {
        XMLElement element = new XMLElement(namespace, name);

        if (operation != null && operation.isEmpty()) {
            if (data != null)
                element = data;

            // Remove any meta-attributes we may have added elsewhere
            element.withAttribute("expand", null);
            element.withAttribute("root", null);
            addChildren(element, node);
        } else if (operation != null || data != null) {
            if (operation != null && !operation.isEmpty())
                element.withAttribute(Netconf.NS_NETCONF, "operation", operation);

            if (node instanceof ListSchemaNode) {
                // For lists, we need to include key leafs
                for (QName key : ((ListSchemaNode) node).getKeyDefinition()) {
                    String keyNS = key.getNamespace().toString();
                    String keyName = key.getLocalName();
                    Optional<XMLElement> keyE = Optional.ofNullable(data).flatMap(x -> x.getFirst(keyNS, keyName));
                    if (keyE.isPresent())
                        element.withChild(keyE.get().clone());
                    else
                        element.createChild(keyNS, keyName);
                }
            }
        }

        // If we have associated data from the peer populate it in the XML template
        for (WrappedYangNode node = this.parent; node != null && node.node != null; node = node.parent) {
            if (!(node.node instanceof CaseSchemaNode) && !(node.node instanceof ChoiceSchemaNode)) {
                if (data != null)
                    data = data.getParent();

                element = new XMLElement(node.namespace, node.name).withChild(element);
                if (node.node instanceof ListSchemaNode) {
                    ListSchemaNode listNode = (ListSchemaNode)node.node;
                    for (QName key: listNode.getKeyDefinition()) {
                        String keyName = key.getLocalName();
                        String keyNS = key.getNamespace().toString();

                        Optional<XMLElement> keyV = Optional.ofNullable(data).flatMap(d -> d.getFirst(keyNS, keyName));
                        if (keyV.isPresent())
                            element.withChild(keyV.get().clone());
                        else
                            element.withChild(keyNS, keyName);
                    }
                }
            }
        }

        return Optional.ofNullable(module == null ? element : null);
    }

    Optional<XMLElement> createNetconfTemplate() {
        return createNetconfTemplate(null, null);
    }

    // Get the names of the key leafs 
    Stream<String> getKeys() {
        return (node instanceof ListSchemaNode)
            ? ((ListSchemaNode)node).getKeyDefinition().stream().map(QName::getLocalName) : Stream.empty();
    }

    boolean isKey() {
        return parent != null && parent.node instanceof ListSchemaNode &&
                ((ListSchemaNode)parent.node).getKeyDefinition().contains(node.getQName());
    }

    boolean isNotKey() {
        return isKey();
    }

    DataSchemaNode getNode() {
        return node;
    }

    WrappedYangNode getParent() {
        return parent;
    }

    // Populate YANG schema nodes, based on a filter applied to names and descriptions
    boolean addToTree(TreeData<WrappedYangNode> data, Collection<String> filter) {
        String nodeName = name.toLowerCase();
        String nodeDescription = description.toLowerCase();
        boolean okay = filter.stream().filter(nodeName::contains).count() == filter.size() ||
                filter.stream().filter(nodeDescription::contains).count() == filter.size();

        if (node != null)
            data.addItem(parent.node != null ? parent : null, this);

        if (!filter.isEmpty()) {
            if (parent != null)
                parent.expand = true;

            if (okay)
                filter = Collections.emptyList();
        }

        // Recursively add child nodes to YANG schema tree
        if (module != null) {
            for (Module submodule: module.getSubmodules()) {
                for (DataSchemaNode childNode: submodule.getChildNodes()) {
                    if (new WrappedYangNode(this, childNode).addToTree(data, filter))
                        okay = true; // if children matches filter, populate match up to current element
                }
            }
        }

        DataNodeContainer container = module != null ? module :
                node instanceof DataNodeContainer ? (DataNodeContainer)node : null;
        if (container != null) {
            HashSet<DataSchemaNode> addedNodes = new HashSet<>();
            for (DataSchemaNode childNode : container.getChildNodes()) {
                WrappedYangNode child = new WrappedYangNode(this, childNode);
                if (child.addToTree(data, filter)) {
                    addedNodes.add(childNode);
                    okay = true;
                }
            }

            // Some of the children has matched, add adjacent leafs for context
            if (!filter.isEmpty() && okay) {
                for (DataSchemaNode childNode : container.getChildNodes())
                    if (!addedNodes.contains(childNode) &&
                            (childNode instanceof LeafSchemaNode || childNode instanceof LeafListSchemaNode))
                        new WrappedYangNode(this, childNode).addToTree(data, Collections.emptyList());
            }
        }

        // If neither current element nor children match filter, remove current
        try {
            if (!okay)
                data.removeItem(this);
        } catch (IllegalArgumentException e) {
            // ignore if element is not in tree anyway
        }

        return okay;
    }

    String getDescription() {
        return description;
    }

    boolean isConfiguration() {
        return configuration;
    }

    String getNamespace() {
        return namespace;
    }

    Stream<WrappedYangNode> getChildren() {
        DataNodeContainer container = module != null ? module :
                node instanceof DataNodeContainer ? (DataNodeContainer)node : null;
        return (container == null) ? Stream.empty() :
                container.getChildNodes().stream().map(n -> new WrappedYangNode(this, n));
    }

    Optional<WrappedYangNode> getChild(String name) {
        return getChildren().filter(node -> node.getName().equals(name)).findAny();
    }

    // Apply limited expansion
    int applyExpand(Tree<WrappedYangNode> tree, int limit) {
        if (expand && limit > 0) {
            int limitBefore = limit;
            tree.expand(this);

            for (WrappedYangNode child: tree.getTreeData().getChildren(this))
                limit = child.applyExpand(tree, limit);

            if (limit == limitBefore)
                --limit;
        }
        return limit;
    }
}