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

import com.cisco.stbarth.netconf.anc.*;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.repo.api.YangTextSchemaSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Main view showing schema and data tree
 */
@SuppressWarnings("serial")
public final class MainView extends VerticalLayout implements View {
    private VerticalLayout sidebarPanel;
    private Tree<WrappedYangNode> schemaTree;
    private Tree<XMLElement> dataTree;
    private XMLElement dataElements = new XMLElement(null, "data");
    private String dataQuery;
    private Netconf.Datastore dataSource;
    private Panel treePanel = new Panel();
    String host;
    String username;
    String password;
    NetconfClient client;
    NetconfYangParser parser;
    WrappedYangNode selectedNode;
    XMLElement selectedData;

    public MainView(String host, String username, String password,
            NetconfClient client, NetconfYangParser parser, Map<String,String> capabilities) {
        this.host = host;
        this.username = username;
        this.password = password;
	    this.client = client;
	    this.parser = parser;
        
        setSizeFull();
        setMargin(false);

        // Build topbar
        Label title = new Label("Advanced Netconf Explorer");
        title.addStyleName("topbartitle");
        title.setSizeUndefined();

        Label padding = new Label();

        Label connected = new Label(String.format("Device %s (%d YANG models)",
                host, parser.getSchemaContext().getModules().size()));

        Button disconnectButton = new Button(VaadinIcons.SIGN_OUT);
        disconnectButton.setPrimaryStyleName(ValoTheme.BUTTON_BORDERLESS);
        disconnectButton.addClickListener(x -> {
            try {
                client.close();
            } catch (Exception e) {}
            Page.getCurrent().reload();
        });

        HorizontalLayout topbar = new HorizontalLayout();
        topbar.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        topbar.addComponents(title, padding, connected, disconnectButton);
        topbar.setExpandRatio(padding, 1.0f);
        topbar.setWidth("100%");
        topbar.setMargin(true);
        topbar.addStyleName("topbar");
        addComponent(topbar);

        // Define main layout: sidebar + content
        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        addComponent(mainLayout);
        setExpandRatio(mainLayout, 1.0f);

        // Define static part of sidebar, this will always be visible
        VerticalLayout sidebar = new VerticalLayout();
        sidebar.addStyleName(ValoTheme.MENU_PART);
        sidebar.addStyleName("no-vertical-drag-hints");
        sidebar.addStyleName("no-horizontal-drag-hints");
        mainLayout.addComponent(new Panel(sidebar));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button homeButton = new Button("Start", VaadinIcons.HOME);
        homeButton.addClickListener(x -> showHomeScreen());
        buttonLayout.addComponent(homeButton);

        try {
            buttonLayout.addComponent(new NetconfTools(this).createComponent());
        } catch (NetconfException e) {
            Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
        sidebar.addComponent(buttonLayout);

        HorizontalLayout downloadTools = new HorizontalLayout();
        downloadTools.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);

        ComboBox<YangTextSchemaSource> modelSelect = new ComboBox<>("YANG Models");
        modelSelect.setWidth("400px");
        modelSelect.setIcon(VaadinIcons.DOWNLOAD);
        modelSelect.setItemCaptionGenerator(x -> x.getIdentifier().toYangFilename());
        modelSelect.setItems(parser.getSources().stream().sorted((a, b) ->
                a.getIdentifier().toYangFilename().compareTo(b.getIdentifier().toYangFilename())));
        modelSelect.setEmptySelectionAllowed(false);
        modelSelect.setTextInputAllowed(true);

        Button viewButton = new Button("View", VaadinIcons.FILE_CODE);
        viewButton.setEnabled(false);
        viewButton.addClickListener(x -> {
            Window yangWindow = new Window("YANG Model "
                .concat(modelSelect.getValue().getIdentifier().toYangFilename()));
            yangWindow.setModal(true);
            yangWindow.setDraggable(true);
            yangWindow.setResizable(false);
            yangWindow.setWidth("1000px");
            yangWindow.setHeight("700px");

            try {
                ByteArrayOutputStream yangStream = new ByteArrayOutputStream();
                modelSelect.getValue().copyTo(yangStream);

                TextArea yangText = new TextArea();
                yangText.setReadOnly(true);
                yangText.setSizeFull();
                yangText.setValue(new String(yangStream.toByteArray(), StandardCharsets.UTF_8));
                
                VerticalLayout yangLayout = new VerticalLayout(yangText);
                yangLayout.setSizeFull();

                yangWindow.setContent(yangLayout);
                UI.getCurrent().addWindow(yangWindow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        modelSelect.addValueChangeListener(x -> viewButton.setEnabled(x.getValue() != null));

        Button downloadButton = new Button("Download all", VaadinIcons.FILE_ZIP);
        new FileDownloader(new StreamResource(() -> {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ZipOutputStream zipStream = new ZipOutputStream(outputStream);
            zipStream.setLevel(9);

            parser.getSources().forEach(source -> {
                try {
                    zipStream.putNextEntry(new ZipEntry(source.getIdentifier().toYangFilename()));
                    source.copyTo(zipStream);
                    zipStream.closeEntry();
                } catch (IOException e) {}
            });

            try {
                zipStream.close();
            } catch (IOException e) {};

            return new ByteArrayInputStream(outputStream.toByteArray());
        }, "yang-models-" + host + ".zip")).extend(downloadButton);
        downloadTools.addComponents(modelSelect, viewButton, downloadButton);
        sidebar.addComponent(downloadTools);

        ComboBox<String> capabilitySelect = new ComboBox<>("NETCONF Capabilities", capabilities.entrySet().stream()
                .map(x -> x.getKey().concat(x.getValue())).sorted().collect(Collectors.toList()));
        capabilitySelect.setWidth("700px");
        capabilitySelect.setIcon(VaadinIcons.LINES);
        sidebar.addComponent(capabilitySelect);

        if (capabilities.containsKey("http://cisco.com/ns/yang/Cisco-IOS-XR-telemetry-model-driven-cfg"))
            sidebar.addComponent(new TelemetryTools(this).createComponent());

        sidebar.addComponent(new GNMITools(this).createComponent());

        sidebarPanel = new VerticalLayout();
        sidebarPanel.setMargin(false);
        sidebar.addComponent(sidebarPanel);
        sidebar.setExpandRatio(sidebarPanel, 1.0f);

        showHomeScreen();

        // Define content (right-hand side)
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setSizeFull();

        Panel contentPanel = new Panel(contentLayout);
        contentPanel.setSizeFull();
        mainLayout.addComponent(contentPanel);

        // Button and filter layout
        HorizontalLayout schemaFilterLayout = new HorizontalLayout();
        schemaFilterLayout.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);

        TextField schemaModuleFilter = new TextField("Search models");
        schemaModuleFilter.setWidth("150px");
        schemaModuleFilter.focus();
        schemaFilterLayout.addComponent(schemaModuleFilter);

        TextField schemaNodeFilter = new TextField("Search nodes");
        schemaNodeFilter.setWidth("150px");
        schemaFilterLayout.addComponent(schemaNodeFilter);

        Button schemaFilterApply = new Button("Apply", VaadinIcons.SEARCH);
        schemaFilterApply.setClickShortcut(KeyCode.ENTER);
        schemaFilterLayout.addComponent(schemaFilterApply);

        Button schemaFilterClear = new Button("Clear", VaadinIcons.ERASER);
        schemaFilterLayout.addComponent(schemaFilterClear);

        HorizontalLayout dataFilterLayout = new HorizontalLayout();
        dataFilterLayout.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
        dataFilterLayout.setVisible(false);

        TextField dataNodeFilter = new TextField("Search nodes");
        dataNodeFilter.setWidth("150px");
        dataNodeFilter.focus();
        dataFilterLayout.addComponent(dataNodeFilter);

        TextField dataValueFilter = new TextField("Search values");
        dataValueFilter.setWidth("150px");
        dataFilterLayout.addComponent(dataValueFilter);

        Button dataFilterApply = new Button("Apply", VaadinIcons.SEARCH);
        dataFilterApply.setClickShortcut(KeyCode.ENTER);
        dataFilterLayout.addComponent(dataFilterApply);
        dataFilterLayout.setComponentAlignment(dataFilterApply, Alignment.BOTTOM_CENTER);

        Button dataFilterClear = new Button("Clear", VaadinIcons.ERASER);
        dataFilterLayout.addComponent(dataFilterClear);
        dataFilterLayout.setComponentAlignment(dataFilterClear, Alignment.BOTTOM_CENTER);

        Consumer<Netconf.Datastore> showSourceAction = x -> {
            if (dataSource != x)
                dataQuery = null;
            dataSource = x;
            schemaFilterLayout.setVisible(false);
            dataFilterLayout.setVisible(true);
            dataFilterClear.click();
        };

        MenuBar showMenu = new MenuBar();
        schemaFilterLayout.addComponent(showMenu);
        contentLayout.addComponent(schemaFilterLayout);

        MenuItem showDataMenu = showMenu.addItem("Show Data", VaadinIcons.DATABASE, null);
        showDataMenu.addItem("Operational", x -> showSourceAction.accept(null));
        showDataMenu.addItem("Running", x -> showSourceAction.accept(Netconf.Datastore.RUNNING));
        showDataMenu.addItem("Candidate", x -> showSourceAction.accept(Netconf.Datastore.CANDIDATE));
        showDataMenu.addItem("Startup", x -> showSourceAction.accept(Netconf.Datastore.STARTUP));

        Button showSchemas = new Button("Show Schemas", VaadinIcons.FILE_TREE);
        dataFilterLayout.addComponent(showSchemas);
        dataFilterLayout.setComponentAlignment(showSchemas, Alignment.BOTTOM_CENTER);
        contentLayout.addComponent(dataFilterLayout);

        // Data or schema tree definition
        treePanel.setHeight("100%");
        contentLayout.addComponent(treePanel);
        contentLayout.setExpandRatio(treePanel, 1.0f);

        schemaFilterApply.addClickListener(e -> treePanel.setContent(
                showSchemaTree(schemaModuleFilter.getValue(), schemaNodeFilter.getValue())));

        dataFilterApply.addClickListener(e -> treePanel.setContent(
                showDataTree(dataNodeFilter.getValue(), dataValueFilter.getValue())));

        schemaFilterClear.addClickListener(e -> {
            schemaModuleFilter.clear();
            schemaModuleFilter.focus();
            schemaNodeFilter.clear();
            schemaFilterApply.click();
        });

        dataFilterClear.addClickListener(e -> {
           dataNodeFilter.clear();
           dataNodeFilter.focus();
           dataValueFilter.clear();
           dataFilterApply.click();
        });

        showSchemas.addClickListener(x -> {
            schemaFilterLayout.setVisible(true);
            dataFilterLayout.setVisible(false);
            treePanel.setContent(schemaTree);
            selectedData = null;
        });

        treePanel.setContent(showSchemaTree("", ""));
    }

    // Show the schema tree based on the current collected YANG models
    private Tree<WrappedYangNode> showSchemaTree(String moduleFilter, String fieldFilter) {
        List<String> moduleQuery = Arrays.asList(moduleFilter.toLowerCase().split(" "));
        List<String> fieldQuery = Arrays.asList(fieldFilter.toLowerCase().split(" "));

        schemaTree = new Tree<>();
        schemaTree.setSelectionMode(Grid.SelectionMode.MULTI);
        schemaTree.setItemCaptionGenerator(WrappedYangNode::getCaption);
        schemaTree.setItemIconGenerator(x -> x.isKey() ? VaadinIcons.KEY : null);
        schemaTree.addItemClickListener(x -> showYangNode(x.getItem()));

        // Iterate YANG models, apply filters and add matching schema nodes
        TreeData<WrappedYangNode> data = new TreeData<>();
        for (Module module: parser.getSchemaContext().getModules()) {
            String name = module.getName().toLowerCase();
            String description = module.getDescription().orElse("").toLowerCase();
            if (moduleQuery.stream().filter(name::contains).count() == moduleQuery.size() ||
                    moduleQuery.stream().filter(description::contains).count() == moduleQuery.size())
                new WrappedYangNode(module).addToTree(data, fieldQuery);
        }

        // Define data provide and ordering of YANG nodes and render on tree widget
        TreeDataProvider<WrappedYangNode> dataProvider = new TreeDataProvider<>(data);
        dataProvider.setSortComparator(Comparator.comparing(WrappedYangNode::isNotKey)
                .thenComparing(WrappedYangNode::getName)::compare);
        schemaTree.setDataProvider(dataProvider);

        // Expand the first 100 direct filter matches automatically
        int remain = 100;
        for (WrappedYangNode module: data.getRootItems())
            remain = module.applyExpand(schemaTree, remain);

        if (remain <= 0)
            Notification.show("Too many search results! They are all shown, but only 100 have been auto-expanded.",
                    Notification.Type.TRAY_NOTIFICATION);

        return schemaTree;
    }

    // Show a tree of live data from the device
    private Tree<XMLElement> showDataTree(String moduleFilter, String fieldFilter) {
        List<String> moduleQuery = Arrays.asList(moduleFilter.toLowerCase().split(" "));
        List<String> fieldQuery = Arrays.asList(fieldFilter.toLowerCase().split(" "));

        dataTree = new Tree<>();
        // Show name of the node/leaf and value (if available)
        dataTree.setItemCaptionGenerator(x -> x.getName().concat(x.stream().count() > 0 ? "" : (" = " + x.getText())));
        dataTree.addItemClickListener(x -> {
            // Build (X)Path of selected element and find namespace

            String path = "";
            String namespace = "";
            for (XMLElement element = x.getItem(); element != null; element = element.getParent()) {
                path = "/" + element.getName() + path;
                namespace = element.getNamespace();

                if (element.getAttribute("root").equals("1"))
                    break;
            }
            path = path.substring(1);
            selectedData = x.getItem();

            // Iterate YANG schemas to find the schema node associated with the data
            for (Module module: parser.getSchemaContext().getModules())
                if (module.getNamespace().toString().equals(namespace))
                    WrappedYangNode.byPath(new WrappedYangNode(module), path).ifPresent(this::showYangNode);
        });
        
        // Get selected schema elements and build a NETCONF combined subtree-filter to retrieve all of them with a single get-call
        LinkedList<XMLElement> subtreeFilter = new LinkedList<>();
        Set<WrappedYangNode> items = schemaTree.getSelectedItems();
        for (WrappedYangNode item: items) {
            boolean unique = true;

            for (WrappedYangNode c = item.getParent(); unique && c != null; c = c.getParent())
                if (items.contains(c))
                    unique = false;

            // Only add new subtree filter if we don't have it or any parent element selected already
            if (unique) {
                item.createNetconfTemplate().map(Stream::of).orElse(item.getChildren()
                    .map(WrappedYangNode::createNetconfTemplate).filter(Optional::isPresent).map(Optional::get))
                    .forEach(subtreeFilter::add);
            }
        }

        // Cache retrieved config data if selected fields are the same and just filters change
        String newQuery = subtreeFilter.stream().map(XMLElement::toXML).collect(Collectors.joining());
        if (!newQuery.equals(dataQuery)) {
            try (NetconfSession session = client.createSession()) {
                // Query peer using NETCONF to retrieve current data using get or get-config
                if (dataSource == null) {
                    try {
                        dataElements = subtreeFilter.isEmpty() ? session.get() : session.get(subtreeFilter);
                    } catch (NetconfException.RPCException e) {
                        e.printStackTrace();
                        Notification.show("The device cowardly refused to send operational data, thus " +
                                "displaying configuration only. You may use 'Show Schemas' to go back, " +
                                "select individual supported schemas and try 'Show Data' again.", Notification.Type.ERROR_MESSAGE);
                        dataElements = subtreeFilter.isEmpty() ? session.getConfig(Netconf.Datastore.RUNNING) :
                                session.getConfig(Netconf.Datastore.RUNNING, subtreeFilter);
                    }
                    dataQuery = newQuery;
                } else {
                    dataElements = subtreeFilter.isEmpty() ? session.getConfig(dataSource) :
                                session.getConfig(dataSource, subtreeFilter);
                }
            } catch (NetconfException e) {
                e.printStackTrace();
                Notification.show("Failed to get data: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        }

        // Collect NETCONF data for tree display
        TreeData<XMLElement> data = new TreeData<>();
        for (XMLElement element: dataElements)
            addXMLToTree(data, element, null, moduleQuery, fieldQuery);

        // Create data provider for tree and define sorting order
        TreeDataProvider<XMLElement> dataProvider = new TreeDataProvider<>(data);
        dataProvider.setSortComparator(Comparator.comparing(XMLElement::getName)::compare);
        dataTree.setDataProvider(dataProvider);

        int remain = 100;

        // Expand up to 50 direct filter matches from data tree
        if (moduleFilter.isEmpty() && fieldFilter.isEmpty()) {
            for (WrappedYangNode node : schemaTree.getSelectedItems()) {
                String path = node.getSensorPath();
                List<String> paths = Arrays.asList(path.substring(path.indexOf(':') + 1).split("/"));
                remain = expandXMLSelected(dataTree, data.getRootItems(), paths, remain);
            }
        }

        for (XMLElement element: data.getRootItems())
            remain = applyXMLExpanded(dataTree, element, remain);

        if (remain <= 0)
            Notification.show("Too many results! They are all shown, but only 100 have been auto-expanded.",
                    Notification.Type.TRAY_NOTIFICATION);

        return dataTree;
    }

    // Transform XML data to a Vaadin treedata object
    private static boolean addXMLToTree(TreeData<XMLElement> data, XMLElement element, XMLElement parent,
                                 Collection<String> nodeQuery, Collection<String> valueQuery) {
	    String name = element.getName().toLowerCase();
        boolean nodeOkay = nodeQuery.stream().filter(name::contains).count() == nodeQuery.size();
        boolean valueOkay = valueQuery.isEmpty();
        boolean okay = false;

        // Add element to tree
        data.addItem(parent, element);

        // Add dummy XML attributes to mark expansion of nodes based on filters
        if (parent == null)
            element.withAttribute("root", "1");
        else if (!nodeQuery.isEmpty() || !valueQuery.isEmpty())
            parent.withAttribute("expand", "1");

        // Once we have a match for node filter, we want all children to be visible, so clear node filter when recursing
        if (nodeOkay && !nodeQuery.isEmpty())
            nodeQuery = Collections.emptyList();

        // For value filter expand child nodes with matching terms
        for (XMLElement child: element) {
            String childText = child.stream().findAny().isPresent() ? null : child.getText().toLowerCase();
            if (childText != null && !valueQuery.isEmpty() &&
                    valueQuery.stream().filter(childText::contains).count() == valueQuery.size()) {
                element.withAttribute("expand", "1");
                valueQuery = Collections.emptyList();
                break;
            }
        }

        // Recurse for each child
        for (XMLElement child: element)
            if (addXMLToTree(data, child, element, nodeQuery, valueQuery))
                okay = true;

        okay = okay || (valueOkay && nodeOkay);

        // If we are filtered by node or value filter and none of our children are visible, remove ourselve
        if (!okay)
            data.removeItem(element);

        return okay;
    }

    // Recursively apply element expansion to a tree based on meta-attributes set by addXMLToTree
    private static int applyXMLExpanded(Tree<XMLElement> tree, XMLElement element, int limit) {
	    if (element.getAttribute("expand").equals("1") && limit > 0) {
            int limitBefore = limit;
            tree.expand(element);

            for (XMLElement child: tree.getTreeData().getChildren(element))
                limit = applyXMLExpanded(tree, child, limit);

            if (limit == limitBefore)
                --limit;
        }
        return limit;
    }

    // Apply YANG schema filters to data tree
    private static int expandXMLSelected(Tree<XMLElement> tree, Iterable<XMLElement> elements, List<String> path, int limit) {
	    if (path.size() < 1 || limit < 1)
	        return limit;

	    path = new LinkedList<>(path);
        String hop = path.remove(0);
        for (XMLElement element: elements) {
            int limitBefore = limit;
            if (element.getName().equals(hop)) {
                tree.expand(element);
                limit = expandXMLSelected(tree, tree.getTreeData().getChildren(element), new LinkedList<>(path), limit);
            }
            if (limit == limitBefore)
                --limit;
        }
        return limit;
    }

    // Render home view
    private void showHomeScreen() {
	    sidebarPanel.removeAllComponents();

        VerticalLayout warningLayout = new VerticalLayout();
        for (String warning: parser.getWarnings()) {
            Label warningLabel = new Label(warning);
            warningLabel.addStyleName(ValoTheme.LABEL_FAILURE);
            warningLayout.addComponent(warningLabel);
        }
        Panel warningPanel = new Panel("Parser Warnings", warningLayout);
        warningPanel.setHeight(200, Unit.PIXELS);
        sidebarPanel.addComponent(warningPanel);
    }

    // Show detail table for a selected YANG schema node
    void showYangNode(WrappedYangNode node) {
	    selectedNode = node;
        sidebarPanel.removeAllComponents();

        LinkedList<AbstractMap.SimpleEntry<String,String>> parameters = new LinkedList<>();
        parameters.add(new AbstractMap.SimpleEntry<>("Name", node.getName()));
        parameters.add(new AbstractMap.SimpleEntry<>("Namespace", node.getNamespace()));
        parameters.add(new AbstractMap.SimpleEntry<>("Type", node.getType() + " (" +
                        (node.isConfiguration() ? "configuration" : "operational") + ")"));

        String type = node.getDataType();
        if (!type.isEmpty())
            parameters.add(new AbstractMap.SimpleEntry<>("Data Type", type));

        String keys = node.getKeys().collect(Collectors.joining(" "));
        if (!keys.isEmpty())
            parameters.add(new AbstractMap.SimpleEntry<>("Keys", keys));

        parameters.add(new AbstractMap.SimpleEntry<>("XPath", node.getXPath()));
        parameters.add(new AbstractMap.SimpleEntry<>("Telemetry Path", node.getSensorPath()));
        parameters.add(new AbstractMap.SimpleEntry<>("Maagic Path", node.getMaagic(false)));
        parameters.add(new AbstractMap.SimpleEntry<>("Maagic QPath", node.getMaagic(true)));

        Grid<AbstractMap.SimpleEntry<String,String>> parameterGrid = new Grid<>("Parameters");
        parameterGrid.addColumn(AbstractMap.SimpleEntry::getKey).setCaption("Name");
        parameterGrid.addColumn(AbstractMap.SimpleEntry::getValue).setCaption("Value");
        parameterGrid.setItems(parameters);
        parameterGrid.setHeightMode(HeightMode.UNDEFINED);
        parameterGrid.setWidth("100%");
        sidebarPanel.addComponent(parameterGrid);

        TextArea descriptionLabel = new TextArea("Description");
        descriptionLabel.setValue(node.getDescription());
        descriptionLabel.setReadOnly(true);
        descriptionLabel.setWidth("100%");
        descriptionLabel.setRows(2);
        sidebarPanel.addComponent(descriptionLabel);

        TextArea subtreeFilter = new TextArea("Subtree Filter");
        node.createNetconfTemplate().map(XMLElement::toString).ifPresent(subtreeFilter::setValue);
        subtreeFilter.setReadOnly(true);
        subtreeFilter.setWidth("100%");
        subtreeFilter.setRows(3);
        sidebarPanel.addComponent(subtreeFilter);
    }

    public boolean searchModels(String moduleFilter, String nodeFilter) {
        Tree<WrappedYangNode> tree = showSchemaTree(moduleFilter, nodeFilter);
        treePanel.setContent(tree);
        return tree.getTreeData().getRootItems().size() > 0;
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

    @Override
    public void beforeLeave(ViewBeforeLeaveEvent event) {
	    try {
            client.close();
        } catch (NetconfException e) {
	        e.printStackTrace();
        }
    }
}
