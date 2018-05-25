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
import com.cisco.stbarth.netconf.anc.NetconfException;
import com.cisco.stbarth.netconf.anc.NetconfSession;
import com.cisco.stbarth.netconf.anc.XMLElement;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * NETCONF console utilities to run manual netconf commands
 */
public class NetconfTools {
    private MainView view;

    NetconfTools(MainView view) throws NetconfException {
        this.view = view;
    }

    Component createComponent() {
        HorizontalLayout netconfTools = new HorizontalLayout();
        netconfTools.setSizeUndefined();
        netconfTools.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        Button getCommand = new Button("NETCONF console", VaadinIcons.TOOLS);
        getCommand.addClickListener(x -> showWindow());
        netconfTools.addComponent(getCommand);
        return netconfTools;
    }

    void showWindow() {
        NetconfSession session;
        Window window = new Window("NETCONF console");
        window.setModal(true);
        window.setWidth("1000px");
        window.setHeight("700px");

        try {
            session = view.client.createSession();
            window.addCloseListener(x -> {
                try {
                    session.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (NetconfException e) {
            Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
            return;
        }

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSizeFull();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Label prepareLabel = new Label("Prepare: ");
        buttonLayout.addComponent(prepareLabel);
        buttonLayout.setComponentAlignment(prepareLabel, Alignment.MIDDLE_LEFT);
        Button prepareGet = new Button("<get>", VaadinIcons.ARROW_CIRCLE_DOWN);
        buttonLayout.addComponent(prepareGet);
        Button prepareEdit = new Button("<edit-config> (Merge)", VaadinIcons.EDIT);
        buttonLayout.addComponent(prepareEdit);
        Button prepareDelete = new Button("<edit-config> (Delete)", VaadinIcons.FILE_REMOVE);
        buttonLayout.addComponent(prepareDelete);
        Button prepareCommit = new Button("<commit>", VaadinIcons.CHECK_CIRCLE);
        buttonLayout.addComponent(prepareCommit);
        layout.addComponent(buttonLayout);

        TextArea requestArea = new TextArea();
        requestArea.setSizeFull();
        layout.addComponent(requestArea);
        layout.setExpandRatio(requestArea, 1.0f);

        HorizontalLayout submitLayout = new HorizontalLayout();
        Button submit = new Button("Send Request", VaadinIcons.ARROW_FORWARD);
        submitLayout.addComponent(submit);
        layout.addComponent(submitLayout);

        TextArea responseArea = new TextArea();
        responseArea.setSizeFull();
        layout.addComponent(responseArea);
        layout.setExpandRatio(responseArea, 1.0f);

        requestArea.addValueChangeListener(x -> responseArea.clear());

        submit.addClickListener(x -> {
            try {
                responseArea.setValue(session.call(new XMLElement(requestArea.getValue())).stream()
                        .map(XMLElement::toString).collect(Collectors.joining("\n")));
            } catch (NetconfException.RPCException e) {
                responseArea.setValue(e.getRPCReply().stream()
                        .map(XMLElement::toString).collect(Collectors.joining("\n")));
            } catch (Exception e) {
                Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });

        prepareGet.addClickListener(x -> requestArea.setValue(
                new XMLElement(Netconf.NS_NETCONF, "get", r -> Optional.ofNullable(view.selectedNode)
                        .flatMap(node -> node.createNetconfTemplate(null, view.selectedData))
                        .ifPresent(r.createChild("filter")::withChild)).toString()));

        prepareEdit.addClickListener(x -> {
            XMLElement pattern = new XMLElement(Netconf.NS_NETCONF, "edit-config");
            pattern.createChild("target").withChild("candidate");
            Optional.ofNullable(view.selectedNode).flatMap(n -> n.createNetconfTemplate("", view.selectedData))
                    .ifPresent(f -> pattern.createChild("config").withChild(f));
            requestArea.setValue(pattern.toString());
        });

        prepareDelete.addClickListener(x -> {
            XMLElement pattern = new XMLElement(Netconf.NS_NETCONF, "edit-config");
            pattern.createChild("target").withChild("candidate");
            pattern.withTextChild("default-operation", "none");
            Optional.ofNullable(view.selectedNode).flatMap(n -> n.createNetconfTemplate("delete", view.selectedData))
                    .ifPresent(f -> pattern.createChild("config").withChild(f));
            requestArea.setValue(pattern.toString());
        });

        prepareCommit.addClickListener(x -> requestArea.setValue(
                new XMLElement(Netconf.NS_NETCONF, "commit").toString()));

        window.setContent(layout);
        UI.getCurrent().addWindow(window);
    }
}
