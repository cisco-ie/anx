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
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

/**
 * View for logging in, retrieving and parsing YANG models
 */
@SuppressWarnings("serial")
public class RetrieverView extends VerticalLayout {

	public RetrieverView(MainUI ui) {
        String profilePath = "profiles.xml";
        setSizeFull();

        // Render login form
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.addStyleName("v-login");
        loginPanel.setSizeUndefined();
        Responsive.makeResponsive(loginPanel);

        HorizontalLayout logo = new HorizontalLayout();
        logo.setSpacing(true);
        
        loginPanel.addComponent(logo);

        Label welcome = new Label("Advanced Netconf Explorer");
        welcome.addStyleName("welcome");
        welcome.addStyleName(ValoTheme.LABEL_H1);
        
        VerticalLayout labels = new VerticalLayout(welcome);
        labels.setComponentAlignment(welcome, Alignment.MIDDLE_CENTER);
        loginPanel.addComponent(labels);

        Label subtitle = new Label("YANG model and data explorer for NETCONF devices");
        subtitle.setSizeUndefined();
        subtitle.addStyleName(ValoTheme.LABEL_H4);
        subtitle.addStyleName(ValoTheme.LABEL_COLORED);
        loginPanel.addComponent(subtitle);

        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        final Button connect = new Button("Login");
        connect.addStyleName(ValoTheme.BUTTON_PRIMARY);
        connect.setClickShortcut(KeyCode.ENTER);
        connect.setEnabled(false);

        final ComboBox<String> hostname = new ComboBox<>("NETCONF Device");
        hostname.setNewItemProvider(Optional::ofNullable);
        hostname.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        hostname.addStyleName("darkicon");
        hostname.focus();
        hostname.addValueChangeListener((e) -> connect.setEnabled(!hostname.getValue().isEmpty()));

        // Read profiles from file
        XMLElement profiles;
        try {
            profiles = new XMLElement(new FileInputStream(new File(profilePath)));
        } catch (IOException | XMLElement.XMLException e) {
            profiles = new XMLElement(null, "profiles");
        }
        hostname.setItems(profiles.find("profile/hostname").map(XMLElement::getText));

        final TextField username = new TextField("Username");
        username.setIcon(VaadinIcons.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        username.addStyleName("darkicon");

        final PasswordField password = new PasswordField("Password");
        password.setIcon(VaadinIcons.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        password.addStyleName("darkicon");

        fields.addComponents(hostname, username, password, connect);
        fields.setComponentAlignment(connect, Alignment.BOTTOM_LEFT);

        HorizontalLayout extraFields = new HorizontalLayout();
        extraFields.setSpacing(true);
        extraFields.addStyleName("fields");

        final CheckBox cacheModels = new CheckBox("Cache YANG models");
        cacheModels.setValue(true);

        final CheckBox remember = new CheckBox("Remember credentials");
        extraFields.addComponents(cacheModels, remember);

        
        // Apply profile credentials if selected
        XMLElement loadedProfiles = profiles;
        hostname.addValueChangeListener(x -> {
            Optional<XMLElement> profile = loadedProfiles.find(
                String.format("profile[hostname='%s']", hostname.getValue())).findAny();
            profile.flatMap(p -> p.getFirst("username")).map(XMLElement::getText).ifPresent(username::setValue);
            profile.flatMap(p -> p.getFirst("password")).map(XMLElement::getText).ifPresent(password::setValue);
            profile.ifPresent(p -> remember.setValue(true));
        });

        // Connect to device and retrieve YANG models
        connect.addClickListener((ClickListener) event -> {
            ui.name = "";
            ui.username = username.getValue();
            ui.password = password.getValue();
            int port = 0;

            // Save profiles (there should probably be some file locking here...)
            XMLElement savedProfiles;
            try {
                savedProfiles = new XMLElement(new FileInputStream(new File(profilePath)));
            } catch (IOException | XMLElement.XMLException e) {
                savedProfiles = new XMLElement(null, "profiles");
            }

            savedProfiles.find(String.format("profile[hostname='%s']", hostname.getValue()))
                .findAny().ifPresent(XMLElement::remove);

            if (remember.getValue()) {
                savedProfiles.createChild("profile")
                        .withTextChild("hostname", hostname.getValue())
                        .withTextChild("username", ui.username)
                        .withTextChild("password", ui.password);
            }

            try {
                savedProfiles.writeTo(new FileOutputStream(new File(profilePath)), true);
            } catch (IOException | XMLElement.XMLException e) {
                e.printStackTrace();
            }

            try {
                URI uri = new URI("http://" + hostname.getValue());
                if (uri.getHost() != null)
                    ui.name = uri.getHost();

                if (uri.getPort() <= 0) {
                    try (Socket socket = new Socket()) {
                        socket.connect(new InetSocketAddress(uri.getHost(), 830), 1000);
                        port = 830;
                    } catch (Exception e) {
                        try (Socket socket = new Socket()) {
                            socket.connect(new InetSocketAddress(uri.getHost(), 2022), 1000);
                            port = 2022;
                        } catch (Exception f) {
                            port = 22;
                        }
                    }
                } else {
                    port = uri.getPort();
                }
            } catch (Exception f) {

            }

            ui.client = new NetconfSSHClient(ui.name, port, username.getValue());
            ui.client.setPassword(password.getValue());
            ui.client.setStrictHostKeyChecking(false);
            ui.client.setTimeout(3600000);
            ui.client.setKeepalive(15000);

            // Render login window
            Window loadingWindow = new Window();
            loadingWindow.setModal(true);
            loadingWindow.setResizable(false);
            loadingWindow.setClosable(false);
            loadingWindow.setDraggable(false);
            loadingWindow.setWidth("900px");
            loadingWindow.setHeight("75px");

            HorizontalLayout layout = new HorizontalLayout();
            layout.setMargin(true);
            layout.setSizeFull();

            ProgressBar progressBar = new ProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setWidth("150px");
            Label label = new Label("Connecting...");
            label.addStyleName(ValoTheme.LABEL_BOLD);
            layout.addComponents(progressBar, label);
            layout.setComponentAlignment(progressBar, Alignment.MIDDLE_LEFT);
            layout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
            layout.setExpandRatio(label, 1.0f);

            loadingWindow.setContent(layout);
            ui.addWindow(loadingWindow);
            ui.push();

            ui.parser = new NetconfYangParser();
            progressBar.setIndeterminate(false);

            if (cacheModels.getValue())
                ui.parser.setCacheDirectory(new File("..", "yangcache").toString());

            try (NetconfSession session = ui.client.createSession()) {
                Map<String, String> schemas = ui.parser.getAvailableSchemas(session);
                ui.capabilities = session.getCapabilities();

                ui.parser.retrieveSchemas(session, schemas, (iteration, identifier, version, error) -> {
                    label.setValue(String.format("Retrieving schema %s@%s: %s",
                            identifier, version, (error != null) ? error.getMessage() : "success"));
                    progressBar.setValue(((float)iteration) / schemas.size());
                    ui.push();
                });
    
                // Actually parse the YANG models using ODL yangtools
                label.setValue(String.format("Parsing schemas. This may take a minute..."));
                progressBar.setIndeterminate(true);
                ui.push();
    
                ui.parser.parse();
    
                if (ui.parser.getSchemaContext() != null)
                    ui.showMain();
                else
                    Notification.show("Failed to parse schemas: no valid YANG models found!",
                            Notification.Type.ERROR_MESSAGE);    
            } catch (Exception e) {
                Notification.show("Failed to retrieve schemas: " + (e.getCause() != null ?
                        e.getCause().getMessage() : e.getMessage()), Notification.Type.ERROR_MESSAGE);
                e.printStackTrace();
            }

            loadingWindow.close();
            ui.removeWindow(loadingWindow);
        });
        loginPanel.addComponent(fields);
        loginPanel.addComponent(extraFields);

		addComponent(loginPanel);
		setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
		setExpandRatio(loginPanel, 1.0f);
	}
}
