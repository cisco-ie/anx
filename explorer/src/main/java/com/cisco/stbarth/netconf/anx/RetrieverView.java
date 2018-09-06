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

import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * View for logging in, retrieving and parsing YANG models
 */
@SuppressWarnings("serial")
public class RetrieverView extends VerticalLayout {

	public RetrieverView(MainUI ui) {
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

        final TextField hostname = new TextField("NETCONF Device");
        hostname.setIcon(VaadinIcons.SERVER);
        hostname.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        hostname.addStyleName("darkicon");
        hostname.focus();
        hostname.addValueChangeListener((e) -> connect.setEnabled(!hostname.getValue().isEmpty()));

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

        // Connect to device and retrieve YANG models
        connect.addClickListener((ClickListener) event -> {
            ui.name = "";
            ui.username = username.getValue();
            ui.password = password.getValue();
            int port = 0;

            try {
                URI uri = new URI("http://" + hostname.getValue());
                if (uri.getHost() != null)
                    ui.name = uri.getHost();

                if (uri.getPort() <= 0) {
                    try (Socket socket = new Socket(uri.getHost(), 830)) {
                        port = 830;
                    } catch (Exception e) {
                        try (Socket socket = new Socket(uri.getHost(), 2022)) {
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

		addComponent(loginPanel);
		setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
		setExpandRatio(loginPanel, 1.0f);
	}
}
