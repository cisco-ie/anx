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

public class Netconf {
    public static final String NS_NETCONF = "urn:ietf:params:xml:ns:netconf:base:1.0";
    public static final String NS_NETCONF_WITH_DEFAULTS = "urn:ietf:params:xml:ns:yang:ietf-netconf-with-defaults";
    public static final String NS_NETCONF_NOTIFICATION = "urn:ietf:params:xml:ns:netconf:notification:1.0";
    public static final String NS_NETMOD_NOTIFICATION = "urn:ietf:params:xml:ns:netmod:notification";
    public static final String NS_NETCONF_MONITORING = "urn:ietf:params:xml:ns:yang:ietf-netconf-monitoring";
    public static final String NS_TAILF_ACTIONS = "http://tail-f.com/ns/netconf/actions/1.0";
    public static final String NS_NCS = "http://tail-f.com/ns/netconf/ncs";

    public static final String CAP_NETCONF_10 = "urn:ietf:params:netconf:base:1.0";
    public static final String CAP_NETCONF_11 = "urn:ietf:params:netconf:base:1.1";
    public static final String CAP_CONFIRMED_COMMIT = "urn:ietf:params:netconf:capability:confirmed-commit:1.1";
    public static final String CAP_VALIDATE = "urn:ietf:params:netconf:capability:validate:1.1";
    public static final String CAP_WITH_DEFAULTS = "urn:ietf:params:netconf:capability:with-defaults:1.0";
    public static final String CAP_NOTIFICATION = "urn:ietf:params:netconf:capability:notification:1.0";
    public static final String CAP_INTERLEAVE = "urn:ietf:params:netconf:capability:interleave:1.0";
    public static final String CAP_STARTUP = "urn:ietf:params:netconf:capability:startup:1.0";
    public static final String CAP_WRITABLE_RUNNING = "urn:ietf:params:netconf:capability:writable-running:1.0";
    public static final String CAP_CANDIDATE = "urn:ietf:params:netconf:capability:candidate:1.0";
    public static final String CAP_ROLLBACK_ON_ERROR = "urn:ietf:params:netconf:capability:rollback-on-error:1.0";
    public static final String CAP_URL = "urn:ietf:params:netconf:capability:url:1.0";
    public static final String CAP_XPATH = "urn:ietf:params:netconf:capability:xpath:1.0";
    public static final String CAP_MONITORING = NS_NETCONF_MONITORING;
    public static final String CAP_TAILF_ACTIONS = NS_TAILF_ACTIONS;



    public enum Datastore {
        RUNNING, CANDIDATE, STARTUP
    }

    public enum EditConfigDefaultOperation {
        MERGE, REPLACE, NONE
    }

    public enum EditConfigTestOption {
        TEST_THEN_SET, SET, TEST_ONLY
    }

    public enum EditConfigOnErrorOption {
        STOP, CONTINUE, ROLLBACK
    }

    public enum DefaultsMode {
        REPORT_ALL, REPORT_ALL_TAGGED, TRIM, EXPLICIT
    }

    public enum NCSCommitParameter {
        NO_REVISION_DROP, NO_NETWORKING, NO_OVERWRITE, NO_OUT_OF_SYNC_CHECK
    }
}
