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
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
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

        Label welcome = new Label("Netconf Explorer");
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
            int port = 22;

            try {
                URI uri = new URI("http://" + hostname.getValue());
                if (uri.getHost() != null)
                    ui.name = uri.getHost();

                if (uri.getPort() > 0)
                    port = uri.getPort();
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

            ui.parser = new YangParser();

            try (NetconfSession session = ui.client.createSession()) {
                // Use NETCONF monitoring to query available schemas for retriving from device
                Collection<XMLElement> query = Arrays.asList(
                        new XMLElement(Netconf.NS_NETCONF_MONITORING, "netconf-state").withChild("schemas"));

                float schemasProcessed = 0;
                progressBar.setIndeterminate(false);

                ui.capabilities = session.getCapabilities().entrySet().stream()
                        .map(x -> x.getKey().concat(x.getValue())).sorted().collect(Collectors.toList());

                Map<String, String> schemas = new HashMap<>();
                session.get(query).withoutNamespaces()
                        .find("netconf-state/schemas/schema[format = 'yang']")
                        .forEach(x -> schemas.put(x.getText("identifier"), x.getText("version")));

                // Workaround for NCS, it is not exporting some essential models so we provide those manually
                if (schemas.containsKey("tailf-ncs-common") && !schemas.containsKey("tailf-common")) {
                    ClassLoader classLoader = this.getClass().getClassLoader();
                    ui.parser.registerSource("tailf-common", "2017-08-23",
                            classLoader.getResource("tailf-common.yang"));
                    ui.parser.registerSource("tailf-meta-extensions", "2017-03-08",
                            classLoader.getResource("tailf-meta-extensions.yang"));
                    ui.parser.registerSource("tailf-cli-extensions", "2017-08-23",
                            classLoader.getResource("tailf-cli-extensions.yang"));
                }

                // Iterate available YANG models, retrieve them using get-schema and add them to the ODL yangtools parser
                for (Map.Entry<String, String> entry : schemas.entrySet()) {
                    String identifier = entry.getKey();
                    String version = entry.getValue();
                    label.setValue(String.format("Retrieving schema %s@%s...", identifier, version));
                    ui.push();

                    try {
                        ui.parser.registerSource(identifier, version, session.getSchema(identifier, version, "yang")
                                .getText().getBytes(StandardCharsets.UTF_8));
                    } catch (NetconfException.RPCException f) {
                        ui.parser.addWarning(String.format("Failed to get schema for %s:%s (%s)\n",
                                identifier, version, f.getMessage()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        progressBar.setValue(++schemasProcessed / schemas.size());
                    }
                }

                // Actually parse the YANG models using ODL yangtools
                label.setValue(String.format("Analyzing schemas. This may take a minute..."));
                progressBar.setIndeterminate(true);
                ui.push();

                ui.parser.parse();

                if (ui.parser.getSchemaContext() == null)
                    throw new Exception("Failed to analyze schemas! Please verify that the YANG models of your agent are valid!");

                // Switch to main view
                ui.showMain();
            } catch (Exception e) {
                Notification.show("Failed to connect: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
                e.printStackTrace();
            } finally {
                loadingWindow.close();
                ui.removeWindow(loadingWindow);
            }
        });
        loginPanel.addComponent(fields);

		addComponent(loginPanel);
		setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
		setExpandRatio(loginPanel, 1.0f);
	}
}
