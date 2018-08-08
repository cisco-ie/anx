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

import com.cisco.stbarth.netconf.grpc.GRPCClient;
import com.cisco.stbarth.netconf.grpc.GNMI;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.cisco.stbarth.netconf.grpc.GRPCClient.GRPCException;
import com.cisco.stbarth.netconf.grpc.GRPCClient.GRPCClientSecurity;
import com.cisco.stbarth.netconf.anc.*;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.opendaylight.yangtools.yang.model.api.LeafListSchemaNode;
import org.opendaylight.yangtools.yang.model.api.LeafSchemaNode;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import javax.net.ssl.SSLHandshakeException;

/**
 * GNMI support tools 
 */
public class GNMITools {
    private MainView view;
    private static final String NS_EMS = "http://cisco.com/ns/yang/Cisco-IOS-XR-man-ems-cfg";

    GNMITools(MainView view) {
        this.view = view;
    }

    // Render GNMI tools components for main view
    Component createComponent() {
        HorizontalLayout gnmiTools = new HorizontalLayout();
        gnmiTools.setSizeUndefined();
        gnmiTools.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);

        TextField interval = new TextField("GNMI Subscribe Interval", "change");
        interval.setIcon(VaadinIcons.DASHBOARD);

        TextField port = new TextField("GNMI Port", "57400");

        try (NetconfSession session = view.client.createSession()) {
            XMLElement grpc = session.getConfig(Netconf.Datastore.RUNNING,
                    Arrays.asList(new XMLElement(NS_EMS, "grpc"))).getOrEmpty(NS_EMS, "grpc");
            port.setValue(grpc.getTextOrDefault("port", "57400"));
        } catch (NetconfException e) {}

        Button subscribe = new Button("GNMI Subscribe", VaadinIcons.PIE_BAR_CHART);
        subscribe.addClickListener(x -> {
            WrappedYangNode node = view.selectedNode;
            if (node != null && (node.getNode() instanceof LeafSchemaNode ||
                    node.getNode() instanceof LeafListSchemaNode))
                node = node.getParent();

            if (node != null)
                showSubscribe(Integer.parseInt(port.getValue()), node.getSensorPath(), interval.getValue());
        });

        gnmiTools.addComponents(interval, port, subscribe);
        return gnmiTools;
    }

    private void showSubscribe(int port, String path, String interval) {
        List<String> paths = Arrays.asList(path + "@" + interval);
        VerticalLayout liveLayout = new VerticalLayout();
        ComboBox<GNMI.Notification> measurements = new ComboBox<>("Measurement");
        TextArea data = new TextArea("GNMI Data (JSON format)");
        data.setReadOnly(true);
        data.setValue("Waiting for first measurement...");
        data.setSizeFull();

        measurements.setWidth("100%");
        measurements.setEmptySelectionAllowed(false);
        measurements.setTextInputAllowed(false);
        measurements.setItemCaptionGenerator(x -> String.format("[%tT] %s",
                x.getTimestamp() / 1000000, GRPCClient.formatGNMIPath(x.getPrefix())));
        measurements.addValueChangeListener(x -> {
            try {
                data.setValue(JsonFormat.printer().preservingProtoFieldNames().print(x.getValue()));
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        });

        GRPCClient tlsClient = new GRPCClient(view.host, port,
                view.username, view.password, GRPCClientSecurity.TLS_UNVERIFIED);
        GRPCClient plainClient = new GRPCClient(view.host, port,
                view.username, view.password, GRPCClientSecurity.PLAINTEXT);

        LinkedList<GNMI.Notification> telemetryData = new LinkedList<>();
        Consumer<GNMI.Notification> telemetryConsumer = x -> view.getUI().access(() -> {
            telemetryData.add(x);
            measurements.setItems(telemetryData);

            if (telemetryData.size() == 1)
                measurements.setValue(telemetryData.get(0));
        });
        Consumer<Throwable> shutdownConsumer = new Consumer<Throwable>() {
			@Override
			public void accept(Throwable t) {
                if (t instanceof GRPCException) {
                    Notification.show("Subscription error: " + t.getMessage());
                } else if (t != null && t.getCause() instanceof SSLHandshakeException) {
                    plainClient.subscribeRequest(paths, telemetryConsumer, this);
                } else if (t != null) {
                    t.printStackTrace();
                }	
			}
        };

        tlsClient.subscribeRequest(paths, telemetryConsumer, shutdownConsumer);

        Window liveWindow = new Window("GNMI data: ".concat(path));
        liveWindow.setModal(true);
        liveWindow.setResizable(false);
        liveWindow.setDraggable(false);
        liveWindow.setWidth("1000px");
        liveWindow.setHeight("700px");
        liveWindow.addCloseListener(c -> {
            try {
                tlsClient.close();
                plainClient.close();
            } catch (Exception e) {}
        });
        liveLayout.addComponents(measurements, data);
        liveLayout.setExpandRatio(data, 1.0f);
        liveLayout.setSizeFull();
        liveWindow.setContent(liveLayout);
        UI.getCurrent().addWindow(liveWindow);
    }

}
